package com.soak.attendance.service.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soak.attendance.constant.AtndStatusType;
import com.soak.attendance.constant.DateTypeDict;
import com.soak.attendance.constant.ScheduleTypeDict;
import com.soak.attendance.model.AtndManualRecord;
import com.soak.attendance.model.AtndSummarySheet;
import com.soak.attendance.model.PunchRecord;
import com.soak.attendance.model.ScheduleType;
import com.soak.attendance.service.AtndMeasureService;
import com.soak.common.metic.UUIDGenerator;
import com.soak.framework.date.DateStyle;
import com.soak.framework.date.DateUtil;
import com.soak.framework.jdbc.Condition;
import com.soak.framework.jdbc.JdbcHandler;
import com.soak.framework.jdbc.Restrictions;
import com.soak.framework.service.imp.BasicServiceImp;
import com.soak.framework.util.ExcelUtil;
import com.soak.framework.util.StringUtil;
import com.soak.structure.model.EmpInfo;

public class AtndMeasureServiceImp extends BasicServiceImp implements AtndMeasureService{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private List<EmpInfo> empInfos ;
  
  private final int LIMITRANGE = 5 ;  // 允许的加班填写误差 5 单位：分钟
  
  private final int LATELIMITRANGE = 2 ;  // 上班迟到时间 允许迟到 2  单位：分钟
  
  
  
  /**
   *  上班时间固定的    员工排班
   * 
   */
  public ScheduleTypeDict getStableScheduleType(String empno , Date eachDate) {
    // 加班 请假 出差
    String sql = "SELECT 1 FROM atnd_schedule WHERE empno = ? " ;
    Map re  = basicDao.queryOneAsMap(null , sql, empno);
    if(re == null){
//      logger.debug("SELECT 1 FROM atnd_schedule WHERE empno = " + empno );
    } else {
      return ScheduleTypeDict.DAYSHIFT;
    }
    
    return null ;
  }
  
  
  //  获取 某天 排班类型     （根据打卡时间  猜测）  unstable
  public ScheduleTypeDict getOneDayScheduleType(Date eachDate, List<AtndManualRecord> atndManualRecords) {
    List<ScheduleTypeDict> allPossible = new ArrayList<ScheduleTypeDict>();
    
    for (AtndManualRecord manualRecord : atndManualRecords) {
      if (DateUtil.isSameDay(manualRecord.getSrcDate(), eachDate)) {  // 取当天的
        Date startTime = manualRecord.getStartTime() ; 
        Date endTime = manualRecord.getEndTime() ; 
        Date  dayshiftEndtime =  DateUtil.setTime(eachDate, ScheduleTypeDict.DAYSHIFT.getEndTime());
        Date  nightshift1StartTime =  DateUtil.setTime(eachDate, ScheduleTypeDict.NIGHTSHIFT1.getStartTime());
        Date  nightshift2StartTime =  DateUtil.setTime(eachDate, ScheduleTypeDict.NIGHTSHIFT2.getStartTime());
        
        // 申请单类型    // '1:加班,2:出差,3:请假;' ,
        switch (manualRecord.getBookType()) {
          case 1: // 1:加班,
            ScheduleTypeDict  schedule = manualRecord.getCurrentScheduleType();
            if(schedule != null ){
              allPossible.add(schedule);
            } else {
              //TODO 加班 没填写 排班情况
              
            }
            break;
          case 2: // 2:出差
          case 3: // 3:请假 
            if(startTime == null || endTime == null  ){
              // 
            } else {
              schedule = ScheduleTypeDict.DAYSHIFT  ;
              if(DateUtil.timeDiff(endTime, dayshiftEndtime) >= 0 ){
                schedule = ScheduleTypeDict.DAYSHIFT ;
              } else if(DateUtil.timeDiff(nightshift1StartTime, startTime) == 0){
                schedule = ScheduleTypeDict.NIGHTSHIFT1 ;
              } else if(DateUtil.timeDiff(nightshift2StartTime, startTime) >= 0){
                schedule = ScheduleTypeDict.NIGHTSHIFT2 ;
              }
              allPossible.add(schedule);
            }
            break;
          default:
            System.out.println("defaultdefaultdefaultdefaultdefault");
            break;
        }
      }
    }
    
    // 排班    取  统计出 可能性最大 （次数最多 、如有多个并列都第一个)  的排班
    Map<ScheduleTypeDict,Integer> map = new HashMap<ScheduleTypeDict,Integer>();  
    for (ScheduleTypeDict scheduleType : allPossible) {
      Integer count =  map.get(scheduleType);
      if(count == null ){
        map.put(scheduleType, 0);
      }else {
        map.put(scheduleType, ++count);
      }
    }
    
    //遍历数据结构,找出出现次数最多的元素.  
    Set<Entry<ScheduleTypeDict,Integer>> set = map.entrySet();  
    Iterator<Entry<ScheduleTypeDict,Integer>> it = set.iterator();  
    boolean flag = true;    //判断是否第一次迭代  
    ScheduleTypeDict result = null  ;
    int value = 0;  
    while(it.hasNext()) {  
        Entry<ScheduleTypeDict,Integer> e = it.next();  
        //第一次遍历数据结构,先初始化key,value  
        if(flag) {  
            result = e.getKey();  
            value = e.getValue();  
            flag = false;  
            continue;  
        }  

        //当前元素出现次数大于当前最优值的情况  
        if(e.getValue() > value) {  
            result = e.getKey();  
            value = e.getValue();  
        }  
    }  

    // 默认白班 (如：根据加班单获取不到排班)
    if(result == null ){
      result = ScheduleTypeDict.DAYSHIFT ;
    }
    
    return result;
  }
  
  /**
   * 功能： 是否迟到  允许 2分钟 误差
   * 
   */
  public Boolean isLate(Date punchtime , Date leateTime ) {
    if(punchtime == null){
      return null ;
    }
    float interval = DateUtil.timeDiff(DateUtil.addMinutes(leateTime, LATELIMITRANGE), punchtime);
    if (interval > 0) {
      // 迟到
      return true;
    } else {
      // 正常
      return false;
    }
  }
  

  /**
   * 计算WEEKEND 工作时间 
   * 
   * @param startPunchtime
   * @param endPunchtime
   * @return
   */
  private float calculateVacationWorkedHours(ScheduleTypeDict scheduleType , Date startPunchtime, Date endPunchtime) {
    if(startPunchtime == null || endPunchtime == null || DateUtil.isSameDateTime(startPunchtime, endPunchtime) ){
      return 0 ;
    }
    
    // 上班时间之前  不计入 加班时数
    if(startPunchtime.before(scheduleType.getStartTime(startPunchtime))){
      startPunchtime = scheduleType.getStartTime(startPunchtime) ;
    }
    
    float totalHoursWorked = 0;
    if(scheduleType.getRestPeriodStart()== null || scheduleType.getRestPeriodEnd() == null ){
      totalHoursWorked = DateUtil.timeDiff(startPunchtime, endPunchtime)/ 3600 ;
      
      return totalHoursWorked ;
    }
    
    Date restPeriodStart = scheduleType.getRestPeriodStart(startPunchtime); // 休息开始时间(上午下班时间)
    Date restPeriodEnd = scheduleType.getRestPeriodEnd(startPunchtime);     // 休息结束时间(下午上班时间)
    
    if (startPunchtime.getTime() <= restPeriodStart.getTime()) {
      if (endPunchtime.getTime() <= restPeriodStart.getTime()) {   // 例如:  4：00  ~ 11:30 
        totalHoursWorked = DateUtil.timeDiff(startPunchtime, endPunchtime);
      } else if (endPunchtime.getTime() > restPeriodStart.getTime() && endPunchtime.getTime() <= restPeriodEnd.getTime()) {  //例如  6：00  ~ 12:30  
        totalHoursWorked = DateUtil.timeDiff(startPunchtime, restPeriodStart);
      } else if (endPunchtime.getTime() > restPeriodEnd.getTime()) {   // 5:00 ~ 19:00
        totalHoursWorked = DateUtil.timeDiff(startPunchtime, restPeriodStart);
        totalHoursWorked += DateUtil.timeDiff(restPeriodEnd, endPunchtime);
      }
    } else if (startPunchtime.getTime() > restPeriodStart.getTime() && startPunchtime.getTime() <= restPeriodEnd.getTime() ) {  // 12:00 ~ 
      totalHoursWorked = DateUtil.timeDiff(restPeriodEnd, endPunchtime);
    } else if (startPunchtime.getTime() >= restPeriodEnd.getTime()) {  // 2:00 ~ 
      totalHoursWorked = DateUtil.timeDiff(startPunchtime, endPunchtime);
    }
    
    if(totalHoursWorked < 0){
      totalHoursWorked = 0 ;
    }

    return totalHoursWorked / 3600 ;
  }
  
  /**
   * 计算 加班时间 
   * 
   * @param startPunchtime
   * @param endPunchtime
   * @return
   */
  private float calculateWorkdayOvertimeHours(ScheduleTypeDict scheduleType , Date startPunchtime, Date endPunchtime) {
    if(startPunchtime == null || endPunchtime == null  || DateUtil.isSameDateTime(startPunchtime, endPunchtime) ){
      return 0 ;
    }
    
    float totalHoursWorked = 0;
    Date scheduleStart = scheduleType.getStartTime(startPunchtime);
    Date scheduleEnd = scheduleType.getEndTime(scheduleStart);
    
    
    if (startPunchtime.getTime() <= scheduleStart.getTime()) {
      if (endPunchtime.getTime() <= scheduleStart.getTime()) {  // 例如:  4：00  ~ 7:30 
        totalHoursWorked = DateUtil.timeDiff(startPunchtime, endPunchtime);
      } else if (endPunchtime.getTime() > scheduleStart.getTime() &&  endPunchtime.getTime() <= scheduleEnd.getTime()) {  //例如  6：00  ~ 12:30  
        totalHoursWorked = DateUtil.timeDiff(startPunchtime, scheduleStart);
      } else if (endPunchtime.getTime() > scheduleEnd.getTime()) {   // 5:00 ~ 19:00
        totalHoursWorked = DateUtil.timeDiff(startPunchtime, scheduleStart);
        totalHoursWorked += DateUtil.timeDiff(scheduleEnd, endPunchtime);
      }
    } else if (startPunchtime.getTime() > scheduleStart.getTime() &&  startPunchtime.getTime() <= scheduleEnd.getTime() ) {
      totalHoursWorked = DateUtil.timeDiff(scheduleEnd, endPunchtime);
    } else if (startPunchtime.getTime() > scheduleEnd.getTime() ) {
      totalHoursWorked = DateUtil.timeDiff(startPunchtime, endPunchtime);
    }
    
    if(totalHoursWorked < 0){
      totalHoursWorked = 0 ;
    }

    return totalHoursWorked/3600 ;
  }
  

  /**
   * 加班时间转为 单位时间 （取近似 ）
   * 
   * @param number
   * @return
   */
  private double approximateUnit(double number) {
    double integral = Math.floor(number);
    double decimal = number - integral;

    if (decimal * 60 < 30 - LIMITRANGE) {
      decimal = 0;
    } else if (decimal * 60 >= 30 - LIMITRANGE && decimal * 60 < 60 - LIMITRANGE) {
      decimal = 0.5;
    } else if (decimal * 60 >= 60 - LIMITRANGE) {
      decimal = 1;
    }
    number = integral + decimal;
    return number;
  }
  
  
  public List<EmpInfo> getEmpInfos(){
    if(empInfos == null ){
      EmpInfo emp = new EmpInfo();
      empInfos = basicDao.findByAnnotatedSample(emp);
    }
    return empInfos ;
  }

  /**
   * 检测 当前日期 是不是工作日
   * 
   * @param date
   * @param empid
   * @return
   */
  //TODO
  public DateTypeDict checkOneDayTypeByEmpId(Date date, String empid) {

    /*// 节假日
    if(DateUtil.isSameDay(date, DateUtil.parseShortDate("2016-10-01")) || DateUtil.isSameDay(date, DateUtil.parseShortDate("2016-10-07"))    ){
      return DateTypeDict.HOLIDAY ;
    }*/
    
    // 节假日
    if(DateUtil.isBetween(date, DateUtil.parseShortDate("2016-10-01"), DateUtil.parseShortDate("2016-10-07"))){
      return DateTypeDict.HOLIDAY ;
    }
    
    // 获取 日期星期
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    
    DateTypeDict type = null ;
    
    switch(cal.get(Calendar.DAY_OF_WEEK)){
      case  Calendar.SATURDAY :
        type = DateTypeDict.WEEKEND ;
        // 特殊人员情况   有部分人 周六 算 正常上班
//        String sql = "SELECT 1 FROM atnd_special_schedule WHERE empno = ? " ;
//        Map re  = basicDao.queryOneAsMap(null , sql, empid);
//        if(re != null){
//          type = DateTypeDict.ORDINARY ;
//        }
        break;
      case  Calendar.SUNDAY :
        type = DateTypeDict.WEEKEND ;
        break;
       default : 
        type =  DateTypeDict.ORDINARY ;
    }

    return type ;
  }
  
  
  /**
   * 检测 当前日期 是不是工作日
   * 
   * @param date
   * @param empid
   * @return
   */
  public DateTypeDict checkOneDayTypeByEmpName(Date date, String empName) {
    EmpInfo emp = new EmpInfo();
    emp.setEmpName(empName);
    List<EmpInfo> emps = basicDao.findByAnnotatedSample(emp);
    if(emps.size() == 1){
      return checkOneDayTypeByEmpId(date, emps.get(0).getEmpNO());
    } else {
      return null ;
    }
  }

  /**
   * 获取所有排班类型
   * 
   * @return
   */
  public List<ScheduleType> getAllScheduletypes() {
    return basicDao.findByAnnotatedSample(new ScheduleType());
  }


  /**
   * 第一步 导入打卡记录
   * 
   * @param filePath
   */
  public void loadPunchRecord(String dirPath) {
    // File dir = new File("E:/考勤/刷卡记录/");
    File dir = new File(dirPath);
    if (dir.exists() && dir.isDirectory()) {
      File files[] = dir.listFiles();
      for (File file : files) {
        String filePath = file.getAbsolutePath();
        System.out.println(filePath);
        basicDao.truncateTable(null, "atnd_punch_record");
        // 导入打卡记录
        basicDao.loadExcelFile("atnd_punch_record", filePath);
        // 打卡记录 合并
        basicDao.callProcedure("sp_f_atnd_punch_record", new Object[] { 1 });
      }
    }
    
    // 更新打卡记录  没有员工号的部分数据
    basicDao.execute("UPDATE f_atnd_punch_record AS t1 LEFT JOIN f_emp_info T2 ON t1.empname = T2.empname SET t1.empno = T2.empno WHERE t1.empno IS  NULL");
    
  }
  
  
  /**
   * 加班单 录入
   * 
   * overtime work application form
   * 
   * @param filePath
   */
  public void loadOvertimeWorkApplicationForm(String filePath) {
    long start = System.currentTimeMillis();
    try {
      Workbook wb = WorkbookFactory.create(new FileInputStream(filePath));
      for (int i = 0; i < wb.getNumberOfSheets(); i++) {

      }
      Sheet sheet = wb.getSheetAt(0); // 下标 0 开始
      int rowCount = 0;
      List<AtndManualRecord> overtimeRecords = new ArrayList<AtndManualRecord>();
      // 循环输出表格中的内容
      for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) {
          continue;
        }

        // 获取每行记录
        List<String> cells = new ArrayList<String>();
        // 判断空行
        int cellnullcount = 0;
        for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
          // 通过 row.getCell(j).toString() 获取单元格内容，
          Cell cell = row.getCell(j);
          String cellobjTmp = ExcelUtil.convertCellToString(cell);
          cells.add(cellobjTmp);

          if(!StringUtil.isEmpty(cellobjTmp)) {
            cellnullcount++;
          }
        }

        // 判断 空行
        if (cellnullcount == 0) {
          continue;
        } else {
          rowCount++;
        }

        if (rowCount > 1) { // 跳过首行 标题
          AtndManualRecord overtimeRecord = new AtndManualRecord();
          overtimeRecord.setBookType(AtndManualRecord.OVERTIME);
          overtimeRecord.setUid(UUIDGenerator.generate());
          overtimeRecord.setEtlDate(DateUtil.getCurrentDateTime());

          ScheduleTypeDict scheduleType = null ;  // 排班
          for (int index = 0; index < cells.size(); index++) {
            String cellvalue = cells.get(index);
            if (cellvalue == null) {
              continue;
            }

            if (index == 0) { // 部门班组
              overtimeRecord.setDeptname(cellvalue);
            } else if (index == 1) { // 姓名
              overtimeRecord.setEmpname(cellvalue);
            } else if (index == 2) { // 加班日期
              overtimeRecord.setSrcDate(DateUtil.parseShortDate(cellvalue));
            } else if (index == 3) { // 加班时数
              overtimeRecord.setTotalHours(Float.valueOf(cellvalue));
            } else if (index == 4) { // 排班类型 
              //TODO 加班单    新增排班类型 
              for(ScheduleTypeDict type : ScheduleTypeDict.values()){
                String scheduleName =  type.getName();
                if(scheduleName.equals(cellvalue)){
                  scheduleType = type ;
                }
              }
              
              // 特殊处理
              if(scheduleType == null ){
                if ("白".equals(cellvalue)) {
                  scheduleType = ScheduleTypeDict.DAYSHIFT ;
                } else if ("午".equals(cellvalue)) {
                  scheduleType = ScheduleTypeDict.DAYSHIFT ;
                  overtimeRecord.setIsExempt(true);
                } else if ("夜1".equals(cellvalue)) {
                  scheduleType = ScheduleTypeDict.NIGHTSHIFT1 ;
                } else if ("夜2".equals(cellvalue)) {
                  scheduleType = ScheduleTypeDict.NIGHTSHIFT2 ;
                } else if ("晚2".equals(cellvalue)) {
                  scheduleType = ScheduleTypeDict.EVENINGSHIFT2 ;
                }else if ("特".equals(cellvalue)) {
                  // 
                }
              }
              
            } else if (index == 5) { // 开始时间
              if (cellvalue.length() == 8) { // 时间格式
                overtimeRecord.setStartTime(DateUtil.setTime(overtimeRecord.getSrcDate(), cellvalue));
              } else {  // 日期+时间 格式 
                if(cellvalue.equals("1.0")){
                  System.out.println(overtimeRecord);
                }
                overtimeRecord.setStartTime(DateUtil.parseDateString(cellvalue, DateStyle.DATETIMEFORMAT));
              }
            } else if (index == 6) { // 结束时间
              if (cellvalue.length() == 8) {  // 时间格式
                overtimeRecord.setEndTime(DateUtil.setTime(overtimeRecord.getSrcDate(), cellvalue));
              } else {  // 日期+时间 格式
                overtimeRecord.setEndTime(DateUtil.parseDateString(cellvalue, DateStyle.DATETIMEFORMAT));
              }
            }
          }

          // 设置排班 
          if(scheduleType!= null){
            overtimeRecord.setScheduleType(scheduleType.getValue());
            Boolean  isExempt  = overtimeRecord.getIsExempt() ; 
            if( (isExempt==null || !isExempt) && overtimeRecord.getStartTime()== null){
              //  “开始时间”，“结束时间”不填默认为： 加班开始时间 为 排班结束时间 
//              Date startTime = scheduleType.getEndTime(overtimeRecord.getSrcDate());
//              overtimeRecord.setStartTime(startTime);
//              overtimeRecord.setEndTime(DateUtil.addTime(startTime, Math.round(overtimeRecord.getTotalHours()*60*60)));
            }
          }
          overtimeRecords.add(overtimeRecord);
//          basicDao.saveAnnotatedBean(overtimeRecord);
        }
      }
      
      basicDao.saveAnnotatedBean(overtimeRecords);

      basicDao.execute("UPDATE atnd_manual_record AS t1 INNER JOIN f_emp_info T2 ON t1.empname = T2.empname SET t1.empno = T2.empno");

      long end = System.currentTimeMillis();
      logger.debug("共 ：" + (rowCount - 1) + "条记录， 运行时间： " + (float) (end - start) / 1000 + "秒 。平均： " + 1000 * (rowCount - 1) / (end - start) + "条/秒");
    } catch (InvalidFormatException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * 请假单 录入
   * 
   * @param filePath
   */
  public void loadOffWorkApplicationForm(String filePath) {
    long start = System.currentTimeMillis();
    try {
      Workbook wb = WorkbookFactory.create(new FileInputStream(filePath));
      Sheet sheet = wb.getSheetAt(0);  // 取第一个Sheet

      for (int i = 0; i < wb.getNumberOfSheets(); i++) {

      }
      List<AtndManualRecord> offWorkRecords = new ArrayList<AtndManualRecord>();
      int rowCount = 0;
      for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) {
          continue;
        }

        // 获取每行记录
        List<String> cells = new ArrayList<String>();
        // 判断空行
        int cellnullcount = 0;
        for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
          // 通过 row.getCell(j).toString() 获取单元格内容，
          Cell cell = row.getCell(j);
          String cellobjTmp = ExcelUtil.convertCellToString(cell);
          cells.add(cellobjTmp);

          if(!StringUtil.isEmpty(cellobjTmp)) {
            cellnullcount++;
          }
        }

        // 判断 空行
        if (cellnullcount == 0) {
          continue;
        } else {
          rowCount++;
        }

        if (rowCount > 1) { // 忽略表头
          AtndManualRecord offWorkRecord = new AtndManualRecord();
          offWorkRecord.setBookType(AtndManualRecord.OFFWORK);
          offWorkRecord.setUid(UUIDGenerator.generate());
          offWorkRecord.setEtlDate(DateUtil.getCurrentDateTime());

          for (int index = 0; index < cells.size(); index++) {
            String cellvalue = cells.get(index);
            if (cellvalue == null) {
              continue;
            }
            
            //判断日期格式类型
            String datePattern = "\\d{4}-\\d{2}-\\d{2}";  
            Pattern pattern = Pattern.compile(datePattern);  
            Matcher match = pattern.matcher(cellvalue);  

            if (index == 0) { // 部门班组
              offWorkRecord.setDeptname(cellvalue);
            } else if (index == 1) {// 姓名
              offWorkRecord.setEmpname(cellvalue);
            } else if (index == 2) {// 请假日期
              if(cellvalue.equals("8")){
                System.out.println(offWorkRecord);
              }
              offWorkRecord.setSrcDate(DateUtil.parseShortDate(cellvalue));
            } else if (index == 3) { // 小时数
              offWorkRecord.setTotalHours(Float.valueOf(cellvalue));
            } else if (index == 4) { // 类型
              offWorkRecord.setVacationType(cellvalue);
            } else if (index == 5) { // 开始时间
              if (match.matches()) {  
                offWorkRecord.setStartTime(DateUtil.parseDateString(cellvalue, DateStyle.SHORTDATEFORMAT));
              } else {  
                if (cellvalue.length() == 8) {
                  offWorkRecord.setStartTime(DateUtil.setTime(offWorkRecord.getSrcDate(), cellvalue));
                } else {
                  offWorkRecord.setStartTime(DateUtil.parseDateString(cellvalue, DateStyle.DATETIMEFORMAT));
                }
              }  
            } else if (index == 6) { // 结束时间
              if (match.matches()) {  
                offWorkRecord.setEndTime(DateUtil.parseDateString(cellvalue, DateStyle.SHORTDATEFORMAT));
              } else {
                if (cellvalue.length() == 8) {
                  offWorkRecord.setEndTime(DateUtil.setTime(offWorkRecord.getSrcDate(), cellvalue));
                } else {
                  offWorkRecord.setEndTime(DateUtil.parseDateString(cellvalue, DateStyle.DATETIMEFORMAT));
                }
              }
            } else if (index == 7) { // 备注
              offWorkRecord.setRemark(cellvalue);
            }
          }
          
          if("上午".equals(offWorkRecord.getRemark())){
            offWorkRecord.setStartTime(ScheduleTypeDict.DAYSHIFT.getStartTime(offWorkRecord.getSrcDate()));
            offWorkRecord.setEndTime(ScheduleTypeDict.DAYSHIFT.getRestPeriodStart(offWorkRecord.getSrcDate()));
          }
          
          if("下午".equals(offWorkRecord.getRemark())){
            offWorkRecord.setStartTime(ScheduleTypeDict.DAYSHIFT.getRestPeriodEnd(offWorkRecord.getSrcDate()));
            offWorkRecord.setEndTime(ScheduleTypeDict.DAYSHIFT.getEndTime(offWorkRecord.getSrcDate()));
          }
          
          
          offWorkRecords.add(offWorkRecord);
//          basicDao.saveAnnotatedBean(offWorkRecord);  // 保存
        }
      }

      basicDao.saveAnnotatedBean(offWorkRecords); // 保存 请假单
      basicDao.execute("UPDATE atnd_manual_record AS t1 INNER JOIN f_emp_info T2 ON t1.empname = T2.empname SET t1.empno = T2.empno");

      long end = System.currentTimeMillis();
      logger.debug("共 ：" + (rowCount - 1) + "条记录， 运行时间： " + (float) (end - start) / 1000 + "秒 。平均： " + 1000 * (rowCount - 1) / (end - start) + "条/秒");
    } catch (InvalidFormatException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * 出差单 录入
   * 
   * @param filePath
   */
  public void loadBusinessTripApplicationForm(String filePath) {
    long start = System.currentTimeMillis();
    try {
      Workbook wb = WorkbookFactory.create(new FileInputStream(filePath));
      Sheet sheet = wb.getSheetAt(0);
      for (int i = 0; i < wb.getNumberOfSheets(); i++) {

      }
      List<AtndManualRecord> businessTripRecords = new ArrayList<AtndManualRecord>();
      int rowCount = 0;
      // 循环输出表格中的内容
      for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) {
          continue;
        }

        List<String> cells = new ArrayList<String>();
        // 判断空行
        int cellnullcount = 0;
        for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
          // 通过 row.getCell(j).toString() 获取单元格内容，
          Cell cell = row.getCell(j);
          String cellobjTmp = ExcelUtil.convertCellToString(cell);
          cells.add(cellobjTmp);

          if(!StringUtil.isEmpty(cellobjTmp)) {
            cellnullcount++;
          }
        }

        // 判断 空行
        if (cellnullcount == 0) {
          continue;
        } else {
          rowCount++;
        }
        
        // 忽略表头
        if (rowCount > 1) {// 跳过首行 标题
          AtndManualRecord businessTripRecord = new AtndManualRecord();
          businessTripRecord.setBookType(AtndManualRecord.BUSINESSTRIP);
          businessTripRecord.setUid(UUIDGenerator.generate());
          businessTripRecord.setEtlDate(DateUtil.getCurrentDateTime());

          for (int index = 0; index < cells.size(); index++) {
            String cellvalue = cells.get(index);
            if (cellvalue == null) {
              continue;
            }

            if (index == 0) { // 部门班组
              businessTripRecord.setDeptname(cellvalue);
            } else if (index == 1) {// 姓名
              businessTripRecord.setEmpname(cellvalue);
            } else if (index == 2) {// 外出日期
              businessTripRecord.setSrcDate(DateUtil.parseShortDate(cellvalue));
            } else if (index == 3) {
              if ("全".equals(cellvalue)) {
                businessTripRecord.setStartTime(DateUtil.setTime(businessTripRecord.getSrcDate(), "7:30:00"));
                businessTripRecord.setEndTime(DateUtil.setTime(businessTripRecord.getSrcDate(), "17:00:00"));
                businessTripRecord.setTotalHours(8f);
              } else if ("上".equals(cellvalue)) {
                businessTripRecord.setStartTime(DateUtil.setTime(businessTripRecord.getSrcDate(), "7:30:00"));
                businessTripRecord.setEndTime(DateUtil.setTime(businessTripRecord.getSrcDate(), "11:30:00"));
                businessTripRecord.setTotalHours(4f);
              } else if ("下".equals(cellvalue)) {
                businessTripRecord.setStartTime(DateUtil.setTime(businessTripRecord.getSrcDate(), "13:30:00"));
                businessTripRecord.setEndTime(DateUtil.setTime(businessTripRecord.getSrcDate(), "17:00:00"));
                businessTripRecord.setTotalHours(4f);
              }
            }
          }
          // 保存
//          basicDao.saveAnnotatedBean(businessTripRecord);
          businessTripRecords.add(businessTripRecord);
        }
      }
      basicDao.saveAnnotatedBean(businessTripRecords);
      basicDao.execute("UPDATE atnd_manual_record AS t1 INNER JOIN f_emp_info T2 ON t1.empname = T2.empname SET t1.empno = T2.empno");

      long end = System.currentTimeMillis();
      logger.debug("共 ：" + (rowCount - 1) + "条记录， 运行时间： " + (float) (end - start) / 1000 + "秒 。平均： " + 1000 * (rowCount - 1) / (end - start) + "条/秒");
    } catch (InvalidFormatException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  
  

  /***
   * 考勤入口   atndDataAuditing
   */
  public void atndmeasureTest(Date startDate , Date endDate  , String...  empnos) {
//    String year = yyyyMM.substring(0, 4);
//    String mm = yyyyMM.substring(4);
//    Date startDate = DateUtil.parseShortDate(year + "-" + mm + "-" + "01"); // 月初
//    Date endDate = DateUtil.getLastDayOfMonth(startDate); // 月末
    
    
    List<AtndSummarySheet>  sheets = new ArrayList<AtndSummarySheet>();  // 考勤统计记录
    
    // 加班 请假 出差
    StringBuffer sql =  new StringBuffer("SELECT emp.empNO , emp.empNAME ,dept.DEPTNAME FROM f_emp_info emp LEFT JOIN f_dept_info dept ON emp.deptid = dept.uid ");
    //  where emp.status = 'T' 
    int length = empnos.length;
    if(length > 0 ){
      sql.append(" where emp.empNO in ( ") ;
    }
    
    for (int i = 0; i < length; i++) {
      if(i < length-1){
        sql.append("'");
        sql.append(empnos[i]);
        sql.append("'");
        sql.append(",");
      }
      if (i == length - 1) {
        sql.append("'");
        sql.append(empnos[i]);
        sql.append("'");
        sql.append(" ) ");
      }
    }
    
    List<EmpInfo> emps = basicDao.querySampleList(EmpInfo.class, sql.toString());

    // 打卡记录
    String punchSql = "SELECT recordTime FROM f_atnd_punch_record WHERE empno = ? and recordTime > ? ";
    
    for (EmpInfo emp : emps) {
      String empno = emp.getEmpNO();
      String empname = emp.getEmpName();
      String deptName = emp.getDeptName();
      
  
      // 设置查询时间
      Restrictions restrictions = new Restrictions();
      restrictions.addCondition(Condition.Between, "src_dt", startDate, endDate);
      
      // 加班单 出差单  假单
      AtndManualRecord overtimeWorkRecord = new AtndManualRecord();
      overtimeWorkRecord.setEmpno(empno);
      List<AtndManualRecord> atndManualRecords = basicDao.findByAnnotatedSample(overtimeWorkRecord, restrictions);
  
      // 打卡记录
      List<List> punchRecords = basicDao.queryForList(punchSql, empno, startDate);
      List<Date> punchtimes = new ArrayList<Date>();
      for (List<Date> punchRecord : punchRecords) {
        punchtimes.add(punchRecord.get(0));
      }
  
      // 对两个日期之间所有日期的遍历
      Long startTime = startDate.getTime();
      Long endTime = endDate.getTime();
      Long oneDay = 1000 * 60 * 60 * 24l;
      while (startTime <= endTime) {
        Date eachDate = new Date(startTime);
        startTime += oneDay;
        AtndSummarySheet sheet = new AtndSummarySheet();
        sheet.setStatdate(eachDate);
        sheet.setEmpno(empno);
        sheet.setEmpname(empname);
        sheet.setDeptname(deptName);
        
        // 检查 当天  外出、请假 、加班  单  
        checkOneDayManualRecord(sheet , atndManualRecords , punchtimes);
  
        // 数据稽核
  //      atndDataAuditing(sheet);
        sheets.add(sheet);
      }
  
      // break;
    }

    // 入库
    basicDao.saveAnnotatedBean(sheets);
  }


  /**
   * 检查 某天的加班记录
   * @param eachDate
   * @param atndManualRecords
   * @return
   */
  public void checkOneDayManualRecord(AtndSummarySheet sheet , List<AtndManualRecord> atndManualRecords ,List<Date> punchtimes) {
    // 记录排序
    Collections.sort(atndManualRecords, new Comparator<AtndManualRecord>() {
      public int compare(AtndManualRecord one, AtndManualRecord another) {
        Long oneTime =  one.getStartTime() == null ?  0  :  one.getStartTime().getTime() ;
        Long anotherTime =  another.getStartTime()  == null ?  0  :  another.getStartTime().getTime() ;
        return oneTime.compareTo(anotherTime);
      }
    });
    
    String empno = sheet.getEmpno() ;
    Date eachDate = sheet.getStatdate();
    
    // 单日排班
    //TODO 尝试 取 排班稳定的    
    ScheduleTypeDict scheduleType = getStableScheduleType(empno , eachDate);
    if(scheduleType==null){
      //TODO  排班不固定的
      scheduleType = getOneDayScheduleType(eachDate, atndManualRecords);
    }
    sheet.setScheduleType(scheduleType.getValue());
    
    // 上班基准时间
    Date  scheduleStartTime = scheduleType.getStartTime(eachDate);
    // 下班基准时间
    Date  scheduleEndTime =  scheduleType.getEndTime(eachDate);
    
    // 上班时间 附近的 打卡时间 
    Date startPunchtime =  DateUtil.getApproximatelyTime(scheduleStartTime, punchtimes);
    // 下班时间  附近的 打卡时间 
    Date endPunchtime =  DateUtil.getApproximatelyTime(scheduleEndTime, punchtimes);

    sheet.setPunchInTime(startPunchtime);
    sheet.setPunchOutTime(endPunchtime);
    
    if(startPunchtime==null){
      //TODO  无打卡记录 可能情况：        新员工、 未打卡 、   已离职  
      System.out.println("empno： " + empno + "无打卡记录 可能情况：        新员工、 未打卡 、   已离职  。");
      return ;
    } else if(startPunchtime.after(scheduleEndTime)){ //  未打卡   旷工
      sheet.setPunchInTime(null);
      sheet.setPunchOutTime(null);
      sheet.setAtndStatus(AtndStatusType.Absenteeism.getValue());
    }

    
    float overtimeHours = 0 ;  // 加班时间  
    float businessTripHours  = 0 ;
    float offWorkHours = 0  ;
    Boolean islate = null ; //  是否迟到
    Iterator<AtndManualRecord> recordIter = atndManualRecords.iterator();  
    while (recordIter.hasNext()) {  
      AtndManualRecord manualRecord = recordIter.next();   // 加班、出差、请假单 
      
      if (DateUtil.isSameDay(manualRecord.getSrcDate(), eachDate)) {
        float manualHours = manualRecord.getTotalHours() == null ? 0f : manualRecord.getTotalHours() ;
        Date manualStartTime = manualRecord.getStartTime();
        Date manualEndTime = manualRecord.getEndTime();
        
        if(manualStartTime!=null){ 
          manualEndTime = DateUtil.addTime(manualStartTime, Math.round(manualHours*3600));
        }

        // 加班  开始时间
        Date overtimeStartTime = startPunchtime ;
        Date overtimeEndTime = endPunchtime ;
        
        // 加班、出差、请假单  结束时间附近打卡记录
        if(manualEndTime!=null){ 
          overtimeStartTime = DateUtil.getApproximatelyTime(manualStartTime, punchtimes) ;
          overtimeEndTime = DateUtil.getApproximatelyTime(manualEndTime, punchtimes) ;
        }
        
        //  检测 加班单填写的开始时间是不是  排班结束时间   
        if(DateUtil.isSameDateTime(manualStartTime, scheduleEndTime)){
          overtimeStartTime =  scheduleEndTime ;
        }

        //  检测 加班单填写的结束时间是不是  排班开始时间   
        if(DateUtil.isSameDateTime(manualEndTime, scheduleStartTime)){
          overtimeEndTime =  scheduleStartTime ;
        }
        
        // 检查 工作日 休假日
        DateTypeDict eachDayType = checkOneDayTypeByEmpId(eachDate, empno) ;

        // 申请单 类型
        switch (manualRecord.getBookType()) {
          case 1:  //  加班单
            // 检查 工作日 休假日
            switch (eachDayType) {
              case ORDINARY: // 平时工作日
                Boolean isExempt = manualRecord.getIsExempt() ;   // 中午加班  isExempt 设置为 TRUE
                if(isExempt == null || isExempt.booleanValue() == false ){
                  float eachOverTimeHours = calculateWorkdayOvertimeHours(scheduleType, overtimeStartTime , overtimeEndTime);
                  // 允许误差范围
                  eachOverTimeHours = (float) approximateUnit(eachOverTimeHours) ;
                  eachOverTimeHours = Math.min(manualRecord.getTotalHours() == null ? 0 : manualRecord.getTotalHours() , eachOverTimeHours);

                  // 设置 平时 加班时间
                  overtimeHours += eachOverTimeHours ;
                  sheet.setOrdinaryOvertime(overtimeHours);
                } else { // 中午加班
                  // 设置 平时 加班时间
                  overtimeHours += manualRecord.getTotalHours();
                  sheet.setOrdinaryOvertime(overtimeHours);
                }
                break;
              case WEEKEND: // 周日 加班
                Date endPunchTime = DateUtil.getApproximatelyTime(DateUtil.addTime(startPunchtime, Math.round(manualHours*3600)), punchtimes);
                float eachOverTimeHours = calculateVacationWorkedHours(scheduleType,startPunchtime , endPunchTime);
                // 允许误差范围
                eachOverTimeHours = (float) approximateUnit(eachOverTimeHours) ;
                eachOverTimeHours = Math.min(manualRecord.getTotalHours(), eachOverTimeHours);
                overtimeHours += eachOverTimeHours;
                // 设置 周末加班
                sheet.setWeekendOvertime(overtimeHours);
                break;
              case HOLIDAY: // 休假日
                endPunchTime = DateUtil.getApproximatelyTime(DateUtil.addTime(startPunchtime, Math.round(manualHours*3600)), punchtimes);
                eachOverTimeHours = calculateVacationWorkedHours(scheduleType,startPunchtime, endPunchTime);
                // 允许误差范围
                eachOverTimeHours = (float) approximateUnit(eachOverTimeHours) ;
                eachOverTimeHours = Math.min(manualRecord.getTotalHours(), eachOverTimeHours);
                overtimeHours += eachOverTimeHours;
                // 设置 休假日加班
                sheet.setHolidayOvertime(overtimeHours);
                break;
              default:
                System.out.println("defaultdefaultdefaultdefaultdefault");
                break;
            }
            break ;
          case 2:  // 出差 
          case 3:  // 请假   
            // 检查 工作日 休假日
            switch (eachDayType) {
              case ORDINARY: // 平时工作日
                if(manualStartTime != null){
                  if(manualStartTime.after(scheduleStartTime)){  // 开始时间 在上班时间后
                    //  是否迟到
                    if(islate == null ){
                      islate = isLate(startPunchtime , scheduleStartTime);
                    }
                  } else if (DateUtil.isSameDateTime(manualStartTime, scheduleStartTime) && manualRecord.getTotalHours() < 8) {  // 开始时间 == 上班时间
                     Date endTime = manualEndTime;
                     if(scheduleType == ScheduleTypeDict.DAYSHIFT){
                       Date restPeriodStart = scheduleType.getRestPeriodStart(eachDate);
                       Date restPeriodEnd = scheduleType.getRestPeriodEnd(eachDate);
                       if( DateUtil.isSameDateTime(endTime,restPeriodStart)){
                         endTime = restPeriodEnd ;
                       }
                     }
                     //  是否迟到
                     if(islate == null ){
                       startPunchtime = DateUtil.getApproximatelyTime(endTime, punchtimes) ;
                       islate = isLate(startPunchtime , endTime);
                     }
                  }
                }

                
                if(manualRecord.getBookType() == 2  ){  // 出差 
                  businessTripHours += manualRecord.getTotalHours() ;
                  sheet.setBusinessTrip(businessTripHours);
                } else if( manualRecord.getBookType() == 3 ){ // 请假
                  offWorkHours += manualRecord.getTotalHours() ;
                  sheet.setAbsence(offWorkHours);
                }

                // 全天出差
                if(businessTripHours == 8 ){
                  sheet.setPunchInTime(null);
                  sheet.setPunchOutTime(null);
                  sheet.setAtndStatus(AtndStatusType.BusinessTrip.getValue());
                }
                //  全天请假
                if(offWorkHours == 8 ){
                  sheet.setPunchInTime(null);
                  sheet.setPunchOutTime(null);
                  sheet.setAtndStatus(AtndStatusType.Absence.getValue());
                }
                break;
              case WEEKEND:  // 周末出差 算加班
                if(manualRecord.getBookType() == 2  ){  // 出差 
                  overtimeHours += manualRecord.getTotalHours() ;
                  // 设置 周末加班
                  sheet.setWeekendOvertime(overtimeHours);
                }
                break;
              case HOLIDAY: // 休假日出差 算加班
                if(manualRecord.getBookType() == 2  ){  // 出差 
                  overtimeHours += manualRecord.getTotalHours() ;
                  // 设置 休假日加班
                  sheet.setHolidayOvertime(overtimeHours);
                }
                break;
            }
            
            break ;
        }
        
        recordIter.remove();   // 删除
      }
    }
    

    //TODO 无加班单 请假单  出差单
    if(islate == null ){
      islate = isLate(startPunchtime , scheduleStartTime);
    }
    sheet.setIsLate(islate);
    
  }
  

  private void saveAtndSummarySheet(AtndSummarySheet sheet) {
    
    StringBuffer sql = new StringBuffer("insert into s_atnd_summary_sheet (STATDATE,EMPNO,EMPNAME,DEPTNAME");
    sql.append(",ATNDSTATUS,PUNCH_IN_TIME,PUNCH_OUT_TIME,BUSINESS_TRIPHOURS,ORDINARY_OVERTIME ");
    sql.append(",WEEKEND_OVERTIME,HOLIDAY_OVERTIME,ABSENCE_HOURS,TOTAL_HOURS_WORKED,REMARK,LASTMTH_ACCUM_VACATION,CURRENT_MTH_ACCUM_VACATION ");
    sql.append(") VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");

    Object[] params = new Object[16];
    params[0] = sheet.getStatdate();
    params[1] = sheet.getEmpno();
    params[2] = sheet.getEmpname();
    params[3] = sheet.getDeptname();
    params[4] = sheet.getAtndStatus();
    params[5] = sheet.getPunchInTime();
    params[6] = sheet.getPunchOutTime();
    params[7] = sheet.getBusinessTrip();
    params[8] = sheet.getOrdinaryOvertime();
    params[9] = sheet.getWeekendOvertime();
    params[10] = sheet.getHolidayOvertime();
    params[11] = sheet.getAbsence();
    params[12] = sheet.getTotalHoursWorked();
    params[13] = sheet.getRemark();
    params[14] = sheet.getLastMthAccumVacation();
    params[15] = sheet.getCurrentMthAccumVacation();

    JdbcHandler jdbc = JdbcHandler.getInstance();
//    jdbc.execute(sql.toString(), params);
    
    basicDao.saveAnnotatedBean(sheet);    
  }

  
  public List<PunchRecord> queryPunchRecords(String empno, Date startDate, Date endDate) {
    // TODO Auto-generated method stub  // 打卡记录
//    String punchSql = "SELECT recordTime FROM f_atnd_punch_record WHERE empno = ? and recordTime > ? ";
    // 打卡记录
//    List<List> punchRecords = jdbc.queryForList(punchSql, empno, startDate);
    
    PunchRecord punchRecordSample = new PunchRecord();
    if(!StringUtil.isEmpty(empno)){
      punchRecordSample.setEmpno(empno);
    }
    
    // 设置查询时间
    // 设置查询时间
    Restrictions restrictions = new Restrictions();
    restrictions.addCondition(Condition.Between, "recordTime", new Object[]{startDate ,endDate});
//    Condition condition = Condition.Between.setParam("recordTime", new Object[]{startDate ,endDate});
//    Condition condition = Condition.GreaterOrEqual.setParam("recordTime", startDate);
    
    return basicDao.findByAnnotatedSample(punchRecordSample,restrictions);
      
  }
  
}
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
import com.soak.attendance.model.BusinessTripRecord;
import com.soak.attendance.model.EmpInfo;
import com.soak.attendance.model.OffWorkRecord;
import com.soak.attendance.model.OvertimeWorkRecord;
import com.soak.attendance.model.ScheduleType;
import com.soak.attendance.service.AtndMeasureService;
import com.soak.common.metic.UUIDGenerator;
import com.soak.framework.date.DateStyle;
import com.soak.framework.date.DateUtil;
import com.soak.framework.jdbc.Condition;
import com.soak.framework.jdbc.JdbcHandler;
import com.soak.framework.util.BeanUtil;
import com.soak.framework.util.ExcelUtil;
import com.soak.framework.util.StringUtil;

public class AtndMeasureServiceImp implements AtndMeasureService{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private JdbcHandler jdbc = JdbcHandler.getInstance();

  private List<EmpInfo> empInfos ;
  
  private final int LIMITRANGE = 5 ;  // 允许的加班填写误差 5 单位：分钟
  
  private final int LATELIMITRANGE = 2 ;  // 上班迟到时间 允许迟到 2  单位：分钟
  
  
  //  获取 某天 排班类型
  public ScheduleTypeDict getOneDayScheduleType(Date eachDate, List<AtndManualRecord> atndManualRecords) {
    List<ScheduleTypeDict> allPossible = new ArrayList<ScheduleTypeDict>();
    
    for (AtndManualRecord manualRecord : atndManualRecords) {
      if (DateUtil.isSameDay(manualRecord.getSrcDt(), eachDate)) {  // 取当天的
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
  public boolean isLate(Date punchtime , Date leateTime ) {
    float interval = DateUtil.timeDiff(DateUtil.addMinute(leateTime, LATELIMITRANGE), punchtime);
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
    float totalHoursWorked = 0;
    
    if(scheduleType.getRestPeriodStart()== null || scheduleType.getRestPeriodEnd() == null ){
      totalHoursWorked = DateUtil.timeDiff(startPunchtime, endPunchtime)/ 3600 ;
      
      return totalHoursWorked ;
    }
    Date restEnd = DateUtil.setTime(startPunchtime, scheduleType.getRestPeriodStart());
    Date restStart = DateUtil.setTime(endPunchtime, scheduleType.getRestPeriodEnd());
    
//    Date scheduleStart = DateUtil.setTime(startPunchtime, scheduleType.getStartTime());
//    Date scheduleEnd = DateUtil.setTime(endPunchtime, scheduleType.getEndTime());
    
    if (startPunchtime.getTime() <= restEnd.getTime()) {
      if (endPunchtime.getTime() <= restEnd.getTime()) {   // 例如:  4：00  ~ 11:30 
        totalHoursWorked = DateUtil.timeDiff(startPunchtime, endPunchtime);
      } else if (endPunchtime.getTime() > restEnd.getTime() && endPunchtime.getTime() <= restStart.getTime()) {  //例如  6：00  ~ 12:30  
        totalHoursWorked = DateUtil.timeDiff(startPunchtime, restEnd);
      } else if (endPunchtime.getTime() > restStart.getTime()) {   // 5:00 ~ 19:00
        totalHoursWorked = DateUtil.timeDiff(startPunchtime, restEnd);
        totalHoursWorked += DateUtil.timeDiff(restStart, endPunchtime);
      }
    } else if (startPunchtime.getTime() > restEnd.getTime() && startPunchtime.getTime() <= restStart.getTime() ) {  // 12:00 ~ 
      totalHoursWorked = DateUtil.timeDiff(restStart, endPunchtime);
    } else if (startPunchtime.getTime() >= restStart.getTime()) {  // 2:00 ~ 
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
    float totalHoursWorked = 0;
    Date scheduleStart = DateUtil.setTime(startPunchtime, scheduleType.getStartTime());
    Date scheduleEnd = DateUtil.setTime(endPunchtime, scheduleType.getEndTime());
    
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

    return totalHoursWorked / 3600 ;
  }
  
  /**
   *  根据 开始时间  结束时间  计算打卡时间
   * @param startTime
   * @param endTime
   * @param punchtimes
   * @return
   */
  private float calculateOvertimeHours(ScheduleTypeDict scheduleType, AtndManualRecord overtime , List<Date> punchtimes ) {
    Boolean isExempt = overtime.getIsExempt() ;
    if(isExempt == null || isExempt.booleanValue() == false ){
      Date startTime = overtime.getStartTime() ; 
      Date endTime = overtime.getEndTime() ; 
      Date eachDate = overtime.getSrcDt() ;
      String empno = overtime.getEmpno();
      
      // 取加班单 开始时间 附近的 打卡时间 
      Date startPunchtime =  DateUtil.getApproximatelyTime(startTime, punchtimes);
      // 取加班单 结束时间 附近的 打卡时间 
      Date endPunchtime =  DateUtil.getApproximatelyTime(endTime, punchtimes);

      if(DateUtil.timeDiff(startPunchtime, endPunchtime) == 0){
        startPunchtime = startTime ;
      }
      
      if(endPunchtime == null){
        endPunchtime = endTime ;
      }
      
      float overTimeHours = 0 ;
      DateTypeDict  dateType = checkOneDayTypeByEmpId(eachDate, empno) ;
      switch (dateType) {
        case ORDINARY: // 工作日
          overTimeHours = calculateWorkdayOvertimeHours(scheduleType ,startPunchtime, endPunchtime);
          break;
        case WEEKEND: // 周末
          overTimeHours = calculateVacationWorkedHours(scheduleType ,startPunchtime, endPunchtime);
          break;
        case HOLIDAY: // 节假日
          overTimeHours = calculateVacationWorkedHours(scheduleType ,startPunchtime, endPunchtime);
          break;
        default:
          System.out.println("defaultdefaultdefaultdefaultdefault");
          break;
      }

      // 允许误差范围
      return (float) approximateUnit(overTimeHours);
      // 设置 平时 加班时间
    } else { // 中午加班
      return overtime.getTotalHours();
    }
    
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
      empInfos = jdbc.findByAnnotatedSample(emp);
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
  public DateTypeDict checkOneDayTypeByEmpId(Date date, String empid) {

    // 节假日
    if(DateUtil.isSameDay(date, DateUtil.parseShortDate("2016-04-04")) || DateUtil.isSameDay(date, DateUtil.parseShortDate("2016-04-30"))    ){
      return DateTypeDict.HOLIDAY ;
    }
    
    // 获取 日期星期
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int weekIndex = cal.get(Calendar.DAY_OF_WEEK) - 1;

    // 判断是不是正常工作日 周一 至 周六
    if (weekIndex > 0) {
      //TODO 财务部 周六休息
      // BI00014 苏海华
      // BI00158 葛海莉
      // BI00298 汤鹤鸣
      // BI00027 苏小芳
      // BI00439 沈峰
      if (weekIndex == 6) {
        String[] caiwus = new String[] { "BI00014", "BI00158", "BI00298", "BI00027", "BI00439" };
        for (int i = 0; i < caiwus.length; i++) {
          if (caiwus[i].equals(empid)) {
            return DateTypeDict.WEEKEND ;
          }
        }
      }
      return DateTypeDict.ORDINARY ;
    } else {
      // 周日
      return DateTypeDict.WEEKEND ;
    }

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
    List<EmpInfo> emps = jdbc.findByAnnotatedSample(emp);
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
    return jdbc.findByAnnotatedSample(new ScheduleType());
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
        jdbc.truncateTable(null, "atnd_punch_record");
        // 导入打卡记录
        jdbc.loadExcelFile("", "atnd_punch_record", filePath);
        // 打卡记录 合并
        jdbc.callProcedure("sp_f_atnd_punch_record", new Object[] { 1 });
      }
    }
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
      Sheet sheet = wb.getSheetAt(0);
      int rowCount = 0;
      // 循环输出表格中的内容
      for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
        System.out.println(i);
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
          String cellobjTmp = ExcelUtil.convertCellToJava(cell);
          cells.add(cellobjTmp);

          if (StringUtil.isEmpty(cellobjTmp)) {
            cellnullcount++;
          }
        }

        // 判断 空行
        if (cellnullcount >= row.getPhysicalNumberOfCells()) {
          continue;
        } else {
          rowCount++;
        }

        if (rowCount > 1) { // 跳过首行 标题
          OvertimeWorkRecord record = new OvertimeWorkRecord();
          record.setUid(UUIDGenerator.generate());

          for (int index = 0; index < cells.size(); index++) {
            String cellvalue = cells.get(index);
            if (cellvalue == null) {
              continue;
            }

            if (index == 0) { // 部门班组
              record.setDeptname(cellvalue);
            } else if (index == 1) { // 姓名
              record.setEmpname(cellvalue);
            } else if (index == 2) { // 加班日期
              record.setSrcDt(DateUtil.parseShortDate(cellvalue));
            } else if (index == 3) { // 加班时数
              record.setTotalHours(Float.valueOf(cellvalue));
            } else if (index == 4) {
              if ("白".equals(cellvalue)) {
                record.setScheduleType(ScheduleTypeDict.DAYSHIFT.getValue());
              } else if ("午".equals(cellvalue)) {
                record.setScheduleType(ScheduleTypeDict.DAYSHIFT.getValue());
                record.setIsExempt(true);
              } else if ("夜1".equals(cellvalue)) {
                record.setScheduleType(ScheduleTypeDict.NIGHTSHIFT1.getValue());
              } else if ("夜2".equals(cellvalue)) {
                record.setScheduleType(ScheduleTypeDict.NIGHTSHIFT2.getValue());
              } else if ("晚2".equals(cellvalue)) {
                record.setScheduleType(ScheduleTypeDict.EVENINGSHIFT2.getValue());
              }else if ("特".equals(cellvalue)) {
                // 
              }
            } else if (index == 5) { // 开始时间
              if (cellvalue.length() == 8) {
                record.setStartTime(DateUtil.setTime(record.getSrcDt(), cellvalue));
              } else {
                record.setStartTime(DateUtil.parseDateString(cellvalue, DateStyle.DATEFORMAT));
              }
            } else if (index == 6) { // 结束时间
              if (cellvalue.length() == 8) {
                record.setEndTime(DateUtil.setTime(record.getSrcDt(), cellvalue));
              } else {
                record.setEndTime(DateUtil.parseDateString(cellvalue, DateStyle.DATEFORMAT));
              }
            }
          }
          
          if(record.getStartTime()==null){
            for (ScheduleTypeDict schedule : ScheduleTypeDict.values()) {
              if (schedule.getValue().equals(record.getScheduleType())) {
                Boolean  isExempt  = record.getIsExempt() ;
                if( (isExempt==null || !isExempt) && record.getStartTime()==null){
                  Date startTime = null ;
                  switch (schedule) {
                    case DAYSHIFT: // 白班
                     //  schedule.getEndTime(record.getSrcDt());
                      startTime = DateUtil.setTime(record.getSrcDt(), "17:00");
                      break;
                    case NIGHTSHIFT1: // 夜班1 19:00
                      startTime = DateUtil.setTime(DateUtil.addDay(record.getSrcDt(), 1), "03:00");
                      break;
                    case NIGHTSHIFT2: // 夜班2 19:30
                      startTime = DateUtil.setTime(DateUtil.addDay(record.getSrcDt(), 1), "03:30");
                      break;
                    case SWINGSHIFT1: 
                      startTime = DateUtil.setTime(record.getSrcDt(), "23:00");
                      break;
                    case SWINGSHIFT2: 
                      startTime = DateUtil.setTime(record.getSrcDt(), "23:30");
                      break;
                    case EVENINGSHIFT1: 
                      startTime = DateUtil.setTime(DateUtil.addDay(record.getSrcDt(), 1), "07:00");
                      break;
                    case EVENINGSHIFT2: 
                      startTime = DateUtil.setTime(DateUtil.addDay(record.getSrcDt(), 1), "07:30");
                      break;
                    default:
                      System.out.println("defaultdefaultdefaultdefaultdefault");
                      break;
                  }
                  record.setStartTime(startTime);
                  record.setEndTime(DateUtil.addTime(startTime, Math.round(record.getTotalHours()*60*60)));
                }
              }
            }
          }

          jdbc.saveAnnotatedBean(record);
        }
      }

      jdbc.execute("UPDATE atnd_overtime_record AS t1 INNER JOIN f_emp_info T2 ON t1.empname = T2.empname SET t1.empno = T2.empno");

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
      Sheet sheet = wb.getSheetAt(0);
      System.out.println(wb.getNumberOfSheets());
      System.out.println(sheet.getSheetName());

      for (int i = 0; i < wb.getNumberOfSheets(); i++) {

      }
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
          String cellobjTmp = ExcelUtil.convertCellToJava(cell);
          cells.add(cellobjTmp);

          if (StringUtil.isEmpty(cellobjTmp)) {
            cellnullcount++;
          }
        }

        // 判断 空行
        if (cellnullcount >= row.getPhysicalNumberOfCells()) {
          continue;
        } else {
          rowCount++;
        }

        if (rowCount > 1) { // 忽略表头
          OffWorkRecord record = new OffWorkRecord();
          record.setUid(UUIDGenerator.generate());

          for (int index = 0; index < cells.size(); index++) {
            String cellvalue = cells.get(index);
            if (cellvalue == null) {
              continue;
            }

            if (index == 0) { // 部门班组
              record.setDeptname(cellvalue);
            } else if (index == 1) {// 姓名
              record.setEmpname(cellvalue);
            } else if (index == 2) {// 请假日期
              record.setSrcDt(DateUtil.parseShortDate(cellvalue));
            } else if (index == 3) { // 小时数
              record.setTotalHours(Float.valueOf(cellvalue));
            } else if (index == 4) { // 类型
              record.setVacationType(cellvalue);
            } else if (index == 5) { // 开始时间
              if (cellvalue.length() == 8) {
                record.setStartTime(DateUtil.setTime(record.getSrcDt(), cellvalue));
              } else {
                record.setStartTime(DateUtil.parseDateString(cellvalue, DateStyle.DATEFORMAT));
              }
            } else if (index == 6) { // 结束时间
              if (cellvalue.length() == 8) {
                record.setEndTime(DateUtil.setTime(record.getSrcDt(), cellvalue));
              } else {
                record.setEndTime(DateUtil.parseDateString(cellvalue, DateStyle.DATEFORMAT));
              }
            }
          }
          // 保存
          jdbc.saveAnnotatedBean(record);
        }
      }

      jdbc.execute("UPDATE atnd_offwork_record AS t1 INNER JOIN f_emp_info T2 ON t1.empname = T2.empname SET t1.empno = T2.empno");

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
          String cellobjTmp = ExcelUtil.convertCellToJava(cell);
          cells.add(cellobjTmp);

          if (StringUtil.isEmpty(cellobjTmp)) {
            cellnullcount++;
          }
        }

        // 判断 空行
        if (cellnullcount >= row.getPhysicalNumberOfCells()) {
          continue;
        } else {
          rowCount++;
        }
        // 忽略表头
        if (rowCount > 1) {// 跳过首行 标题
          BusinessTripRecord record = new BusinessTripRecord();
          record.setUid(UUIDGenerator.generate());

          for (int index = 0; index < cells.size(); index++) {
            String cellvalue = cells.get(index);
            if (cellvalue == null) {
              continue;
            }

            if (index == 0) { // 部门班组
              record.setDeptname(cellvalue);
            } else if (index == 1) {// 姓名
              record.setEmpname(cellvalue);
            } else if (index == 2) {// 外出日期
              record.setSrcDt(DateUtil.parseShortDate(cellvalue));
            } else if (index == 3) {
              if ("全".equals(cellvalue)) {
                record.setStartTime(DateUtil.setTime(record.getSrcDt(), "7:30:00"));
                record.setEndTime(DateUtil.setTime(record.getSrcDt(), "17:00:00"));
                record.setTotalHours(8f);
              } else if ("上".equals(cellvalue)) {
                record.setStartTime(DateUtil.setTime(record.getSrcDt(), "7:30:00"));
                record.setEndTime(DateUtil.setTime(record.getSrcDt(), "11:30:00"));
                record.setTotalHours(4f);
              } else if ("下".equals(cellvalue)) {
                record.setStartTime(DateUtil.setTime(record.getSrcDt(), "13:30:00"));
                record.setEndTime(DateUtil.setTime(record.getSrcDt(), "17:00:00"));
                record.setTotalHours(4f);
              }
            }
          }
          // 保存
          jdbc.saveAnnotatedBean(record);
        }
      }

      jdbc.execute("UPDATE atnd_businesstrip_record AS t1 INNER JOIN f_emp_info T2 ON t1.empname = T2.empname SET t1.empno = T2.empno");

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
   * 考勤
   */
  public void atndmeasureTest(EmpInfo emp, String yyyyMM) {

    String year = yyyyMM.substring(0, 4);
    String mm = yyyyMM.substring(4);
    Date startDate = DateUtil.parseShortDate(year + "-" + mm + "-" + "01"); // 月初
    Date endDate = DateUtil.getLastDayOfMonth(startDate); // 月末

    String empno = emp.getEmpNO();
    String empname = emp.getEmpName();
    String deptName = emp.getDeptName();

    // 设置查询时间
    // Condition condition = Condition.Between.setParam("src_dt", new
    // Object[]{startDate ,endDate});
    Condition condition = Condition.Between.setParam("src_dt", startDate, endDate);

    // 加班单 出差单  假单
    AtndManualRecord overtimeWorkRecord = new AtndManualRecord();
    overtimeWorkRecord.setEmpno(empno);
    List<AtndManualRecord> atndManualRecords = jdbc.findByAnnotatedSample(overtimeWorkRecord, condition);

    // 打卡记录
    String sql = "SELECT recordTime FROM f_atnd_punch_record WHERE empno = ? and recordTime > ? ";
    List<List> punchRecords = jdbc.queryForList(sql, empno, startDate);
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

      // 入库
      saveAtndSummarySheet(sheet);
    }

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
    ScheduleTypeDict scheduleType = getOneDayScheduleType(eachDate, atndManualRecords);
    
    // 上班基准时间
    Date  scheduleStartTime = scheduleType.getStartTime(eachDate);
    // 下班基准时间
    Date  scheduleEndTime =  scheduleType.getEndTime(eachDate);
    
    // 上班时间 附近的 打卡时间 
    Date startPunchtime =  DateUtil.getApproximatelyTime(scheduleStartTime, punchtimes);
    
    if(startPunchtime==null){
      //TODO  无打卡记录 可能情况：        新员工、 未打卡 、   已离职  
      System.out.println("empno： " + empno + "无打卡记录 可能情况：        新员工、 未打卡 、   已离职  。");
      return ;
    }
    
    // 下班时间  附近的 打卡时间 
    Date endPunchtime =  DateUtil.getApproximatelyTime(scheduleEndTime, punchtimes);
    
    
    float overtimeHours = 0 ;  // 加班时间  
    float businessTripHours  = 0 ;
    float offWorkHours = 0  ;
    Boolean islate = null ; //  是否迟到
    Iterator<AtndManualRecord> recordIter = atndManualRecords.iterator();  
    while (recordIter.hasNext()) {  
      AtndManualRecord manualRecord = recordIter.next();  
      Date manualStartTime = manualRecord.getStartTime();
      Date manualEndTime = manualRecord.getEndTime() ;
      
      if (DateUtil.isSameDay(manualRecord.getSrcDt(), eachDate)) {
        // 申请单 类型
        switch (manualRecord.getBookType()) {
          case 1:  //  加班单
            if(manualEndTime!=null){ 
              Date eachPunchtime = DateUtil.getApproximatelyTime(manualEndTime, punchtimes) ;
              if(endPunchtime.before(eachPunchtime)){
                endPunchtime = eachPunchtime ;
              }
            }
            // 检查 工作日 休假日
            switch (checkOneDayTypeByEmpId(eachDate, empno)) {
              case ORDINARY: // 平时工作日
                Boolean isExempt = manualRecord.getIsExempt() ;   // 中午加班  isExempt 设置为 TRUE
                if(isExempt == null || isExempt.booleanValue() == false ){
                  float overTimeHours = this.calculateOvertimeHours(scheduleType, manualRecord , punchtimes);
                  overTimeHours = Math.min(manualRecord.getTotalHours(), overTimeHours);

                  // 设置 平时 加班时间
                  overtimeHours += overTimeHours ;
                  sheet.setOrdinaryOvertime(overtimeHours);
                } else { // 中午加班
                  //TODO
                  // 设置 平时 加班时间
                  overtimeHours += manualRecord.getTotalHours();
                  sheet.setOrdinaryOvertime(overtimeHours);
                }
                break;
              case WEEKEND: // 周日 加班
                float overTimeHours = this.calculateOvertimeHours(scheduleType,manualRecord , punchtimes);
                overTimeHours = Math.min(manualRecord.getTotalHours(), overTimeHours);
                overtimeHours += overTimeHours;
                // 设置 周末加班
                sheet.setWeekendOvertime(overtimeHours);
                break;
              case HOLIDAY: // 休假日
                overTimeHours = this.calculateOvertimeHours(scheduleType,manualRecord, punchtimes);
                overTimeHours = Math.min(manualRecord.getTotalHours(), overTimeHours);
                overtimeHours += overTimeHours;
                // 设置 周末加班
                sheet.setHolidayOvertime(overtimeHours);
                break;
              default:
                System.out.println("defaultdefaultdefaultdefaultdefault");
                break;
            }
            break ;
          case 2:  // 出差 
          case 3:  // 请假   
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
            break ;
        }
        
        
        

        System.out.println(atndManualRecords.size());
        recordIter.remove();   // 删除
        System.out.println(atndManualRecords.size());

      }
    }
    

    //TODO 无加班单 请假单  出差单
    if(islate == null ){
      islate = isLate(startPunchtime , scheduleStartTime);
    }
    sheet.setIsLate(islate);
    sheet.setPunchInTime(startPunchtime);
    sheet.setPunchOutTime(endPunchtime);
    
    BeanUtil.debugBean(sheet);
    
    
    
  }
  
  
  
  /***
   *  出差单 
   * @param sheet
   * @param businessTripRecords
   * @param punchtimes
   */
  public void checkOneDayBusinessTripRecord(AtndSummarySheet sheet , List<BusinessTripRecord> businessTripRecords ,List<Date> punchtimes) {
    Date eachDate = sheet.getStatdate();
    // 出差时间
    float tripHour = sheet.getBusinessTrip()== null ? 0 : sheet.getBusinessTrip() ;
    System.out.println(tripHour);
    for (BusinessTripRecord businessTripRecord : businessTripRecords) {
      if (DateUtil.isSameDay(businessTripRecord.getSrcDt(), eachDate)) {
        // 设置 出差时间
        tripHour += businessTripRecord.getTotalHours();
        sheet.setBusinessTrip(tripHour);
        
      }
    }
  }

  /***
   *  请假单
   * 
   * @param sheet
   * @param offWorkRecords
   * @param punchtimes
   */
  public void checkOneDayOffWorkRecordRecord(AtndSummarySheet sheet , List<OffWorkRecord> offWorkRecords ,List<Date> punchtimes) {
    Date eachDate = sheet.getStatdate();
    // 出差时间
    float absenceHour = sheet.getAbsence() == null ? 0 : sheet.getAbsence() ;
    System.out.println(absenceHour);
    for (OffWorkRecord offWorkRecord : offWorkRecords) {
      if (DateUtil.isSameDay(offWorkRecord.getSrcDt(), eachDate)) {
        // 设置 出差时间
        absenceHour += offWorkRecord.getTotalHours();
        sheet.setAbsence(absenceHour);
        
      }
    }
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
    jdbc.execute(sql.toString(), params);
  }

  
  
  
}
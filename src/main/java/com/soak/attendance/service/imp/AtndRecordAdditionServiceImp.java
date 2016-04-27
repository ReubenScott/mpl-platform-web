package com.soak.attendance.service.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soak.attendance.constant.ScheduleTypeDict;
import com.soak.attendance.model.BusinessTripRecord;
import com.soak.attendance.model.OffWorkRecord;
import com.soak.attendance.model.OvertimeWorkRecord;
import com.soak.attendance.model.ScheduleType;
import com.soak.attendance.service.AtndRecordAdditionService;
import com.soak.common.metic.UUIDGenerator;
import com.soak.framework.date.DateStyle;
import com.soak.framework.date.DateUtil;
import com.soak.framework.jdbc.JdbcHandler;
import com.soak.framework.util.ExcelUtil;
import com.soak.framework.util.StringUtil;

public class AtndRecordAdditionServiceImp implements AtndRecordAdditionService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private JdbcHandler jdbc = JdbcHandler.getInstance();

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
   * 获取所有排班类型
   * 
   * @return
   */
  public List<ScheduleType> getAllScheduletypes() {
    return jdbc.findByAnnotatedSample(new ScheduleType());
  }

  /**
   * 公式计算
   * 
   * @param expression
   * @return
   */
  private Number calculate(String expression) {
    /*
     * 初始化一个JexlContext对象，它代表一个执行JEXL表达式的上下文环境
     */
    JexlContext context = JexlHelper.createContext();
    Number obj = null;
    Expression e;
    try {
      e = ExpressionFactory.createExpression(expression);
      // 对这个Expression对象求值，传入执行JEXL表达式的上下文环境对象
      obj = (Number) e.evaluate(context);
      // 输出表达式求值结果
      System.out.println(e.getExpression() + " = " + obj);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return obj;
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
              } else if ("中".equals(cellvalue)) {
                record.setScheduleType(ScheduleTypeDict.DAYSHIFT.getValue());
              } else if ("夜1".equals(cellvalue)) {
                record.setScheduleType(ScheduleTypeDict.NIGHTSHIFT1.getValue());
              } else if ("夜2".equals(cellvalue)) {
                record.setScheduleType(ScheduleTypeDict.NIGHTSHIFT2.getValue());
              } else if ("特".equals(cellvalue)) {
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
                record.setTotalHours(8);
              } else if ("上".equals(cellvalue)) {
                record.setStartTime(DateUtil.setTime(record.getSrcDt(), "7:30:00"));
                record.setEndTime(DateUtil.setTime(record.getSrcDt(), "11:30:00"));
                record.setTotalHours(4);
              } else if ("下".equals(cellvalue)) {
                record.setStartTime(DateUtil.setTime(record.getSrcDt(), "13:30:00"));
                record.setEndTime(DateUtil.setTime(record.getSrcDt(), "17:00:00"));
                record.setTotalHours(4);
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

}

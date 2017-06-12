package com.soak.attendance.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import com.soak.attendance.model.AtndSummarySheet;
import com.soak.attendance.model.ScheduleType;
import com.soak.attendance.service.imp.AtndMeasureServiceImp;
import com.soak.common.date.DateUtil;
import com.soak.common.io.ExcelUtil;
import com.soak.common.util.BeanUtil;
import com.soak.framework.jdbc.core.JdbcTemplate;
import com.soak.framework.xml.XmlSqlMapper;
import com.soak.structure.model.DeptInfo;
import com.soak.structure.model.EmpInfo;
import com.soak.structure.service.OrganizationStructureService;
import com.soak.structure.service.imp.OrganizationStructureServiceImp;

public class AtndManualTest {

  AtndMeasureService measureService;
  OrganizationStructureService structureService;
  JdbcTemplate jdbc;

  @Before
  public void setUp() throws Exception {
    measureService = new AtndMeasureServiceImp();
    structureService = new OrganizationStructureServiceImp();
    jdbc = JdbcTemplate.getInstance();
  }

//   @Test // 第一步导入打卡数据
  public void testloadPunchRecord() {
    measureService.loadPunchRecord("E:/考勤/刷卡记录/");
  }

//  @Test
  public void testAtndRecordServiceImp() {
    for (ScheduleType type : measureService.getAllScheduletypes()) {
      BeanUtil.debugBean(type);
    }
  }

  // @Test // 第二步： 导入 请假单，出差单，加班单
  public void testloadAtndManualApplicationForm() {
    // String filePath = "E:/考勤/201703/外出单——2017年.xlsx";
    // measureService.loadBusinessTripApplicationForm(filePath);

    // String filePath = "E:/考勤/201703/请假单——2017年.xlsx";
    // measureService.loadOffWorkApplicationForm(filePath);

    // String filePath = "E:/考勤/201703/加班单——2017年.xlsx";
    // measureService.loadOvertimeWorkApplicationForm(filePath);

  }

   @Test // 第三步： 考勤统计
  public void testAtndmeasureTest() {
    // 临时计算
    Date startDate = DateUtil.parseShortDate("2017-05-01");
    Date endDate = DateUtil.parseShortDate("2017-06-10");

    // 2. checkOneDayTypeByEmpId 节假日

    // 8-1到8-4早7点晚6点 8-5早7点晚5点 8-6开始7点到5点
    // 8-6开始到8-31早上7点30 到晚上5点

    // measureService.atndmeasureTest("201607");
    // measureService.atndmeasureTest( startDate, endDate , "BI00308");
    String[] empnos = new String[]{"BI00027"
        ,"BI00015"
        ,"BI00473"
        ,"BI00008"
        ,"BI00246"
        ,"BI00105"
        ,"BI00256"
        ,"BI00503"
        ,"BI00508"
        ,"BI00006"
        ,"BI00010"
        ,"BI00062"
        ,"BI00199"
        ,"BI00066"
        ,"BI00506"
        ,"BI00126" } ;

    measureService.atndmeasureTest(startDate, endDate,empnos);
    

    // BI00181
    /*
     * // 加班 请假 出差
     * String sql = "SELECT emp.empNO , emp.empNAME ,dept.DEPTNAME FROM f_emp_info emp LEFT JOIN f_dept_info dept ON emp.deptid = dept.uid ";
     * 
     * sql += " where emp.empNO = 'BI00149'" ;
     * List<EmpInfo> emps = jdbc.querySampleList(EmpInfo.class, sql);
     * 
     * for (EmpInfo emp : emps) {
     * // 考勤统计
     * measureService.atndmeasureTest(emp, "201604");
     * // sql = sql.replaceAll("@empno", emp.getEmpNO());
     * // 构造 XSSFWorkbook 对象，strPath 传入文件路径
     * 
     * // break;
     * }
     */

  }

  // @Test // 获取部门信息
  public void testOrganizationStructureService() {
    List<DeptInfo> depts = structureService.getDeptInfo();

    String filename = "D:/考情汇总表.xlsx";// 设置下载时客户端Excel的名称
    Workbook workbook = new SXSSFWorkbook();// 创建一个Excel文件 2007

    // 设置列的样式
    CellStyle columnHeadStyle = ExcelUtil.getColumnHeadStyle(workbook);
    CellStyle ordinaryCellStyle = ExcelUtil.getOrdinaryCellStyle(workbook);

    Font font = workbook.createFont();
    font.setFontName("宋体");
    font.setFontHeightInPoints((short) 10);

    // 另一个样式
    CellStyle centerstyle = workbook.createCellStyle();
    centerstyle.setFont(font);
    centerstyle.setAlignment(CellStyle.ALIGN_CENTER);// 左右居中
    centerstyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 上下居中
    centerstyle.setWrapText(true);
    centerstyle.setBorderLeft((short) 1);
    centerstyle.setBorderRight((short) 1);
    centerstyle.setBorderBottom(CellStyle.BORDER_THIN); // 设置单元格的边框为粗体
    // centerstyle.setBottomBorderColor(XSSFColor.BLACK.index); // 设置单元格的边框颜色．
    // centerstyle.setFillForegroundColor(XSSFColor.WHITE.index);// 设置单元格的背景颜色．

    try {
      // 获取本月天数
      String yyyyMM = "201703";
      String year = yyyyMM.substring(0, 4);
      String mm = yyyyMM.substring(4);
      Date startDate = DateUtil.parseShortDate(year + "-" + mm + "-" + "01"); // 月初
      Date endDate = DateUtil.getLastDayOfMonth(startDate); // 月末
      List<Date> dates = DateUtil.getEachDateIn(startDate, endDate);

      int columnSize = dates.size() + 20;

      // TODO 获取部门 select uid , deptNO , DEPTNAME from f_dept_info
      for (DeptInfo dept : depts) {
        System.out.println(dept.getDeptNO());
        String deptId = dept.getSid();
        String deptName = dept.getDeptName();

        Sheet sheet = ExcelUtil.createHead(workbook, deptName, "蓝岛海工2016年6月份考勤表", columnSize);

        sheet.createFreezePane(4, 5);// 冻结

        // 创建第二行
        Row row1 = sheet.createRow(1);
        Cell cell1 = row1.createCell(0);
        cell1.setCellValue("本次会议时间：2009年8月31日       前次会议时间：2009年8月24日");

        cell1.setCellStyle(centerstyle);
        // 合并单元格
        CellRangeAddress range = new CellRangeAddress(1, 2, 0, 7);
        sheet.addMergedRegion(range);

        int m = 3; // 开始 行
        int k = 0; // 开始列

        // 第三行
        Row row2 = sheet.createRow(m);
        Row row3 = sheet.createRow(m + 1);
        row2.setHeight((short) 350);
        row3.setHeight((short) 350);

        Cell cell = row2.createCell(k);
        cell.setCellValue("序号");
        cell.setCellStyle(columnHeadStyle);
        range = new CellRangeAddress(m, m + 1, k, k);
        sheet.addMergedRegion(range);
        ExcelUtil.setRegionBorder(1, range, sheet, workbook);

        cell = row2.createCell(++k);
        cell.setCellValue("工号");
        cell.setCellStyle(columnHeadStyle);
        range = new CellRangeAddress(m, m + 1, k, k);
        sheet.addMergedRegion(range);
        ExcelUtil.setRegionBorder(1, range, sheet, workbook);

        cell = row2.createCell(++k);
        range = new CellRangeAddress(m, m + 1, k, k);
        cell.setCellValue("姓名");
        sheet.addMergedRegion(range);
        cell.setCellStyle(columnHeadStyle);
        ExcelUtil.setRegionBorder(1, range, sheet, workbook);

        cell = row2.createCell(++k);
        range = new CellRangeAddress(m, m + 1, k, k);
        sheet.addMergedRegion(range);
        cell.setCellValue("班次");
        cell.setCellStyle(columnHeadStyle);

        for (Date date : dates) {
          // 日期
          cell = row2.createCell(++k);
          cell.setCellValue(DateUtil.formatDate(date, "MM/dd"));
          cell.setCellStyle(columnHeadStyle);
          // 星期
          cell = row3.createCell(k);
          cell.setCellValue(DateUtil.getWeek(date).getShortCnName());
          cell.setCellStyle(columnHeadStyle);
        }

        m = 5;
        // List<EmpInfo> empInfos = jdbc.findByAnnotatedSample(null, new EmpInfo("BI00014"));
        EmpInfo empOfDept = new EmpInfo();
        empOfDept.setDeptId(deptId);
        List<EmpInfo> empInfos = jdbc.findByAnnotatedSample(empOfDept);
        for (int i = 0; i < empInfos.size(); i++) {
          EmpInfo emp = empInfos.get(i);
          k = 0;
          Row dataRow1 = sheet.createRow(m);
          Row dataRow2 = sheet.createRow(m + 1);

          cell = dataRow1.createCell(k); // 序号
          cell.setCellValue(i + 1);
          cell.setCellStyle(ordinaryCellStyle);
          range = new CellRangeAddress(m, m + 1, k, k);
          sheet.addMergedRegion(range);
          ExcelUtil.setRegionBorder(1, range, sheet, workbook);

          cell = dataRow1.createCell(++k); // 工号
          cell.setCellValue(emp.getEmpNO());
          cell.setCellStyle(ordinaryCellStyle);
          range = new CellRangeAddress(m, m + 1, k, k);
          sheet.addMergedRegion(range);
          ExcelUtil.setRegionBorder(1, range, sheet, workbook);

          cell = dataRow1.createCell(++k); // 姓名
          cell.setCellValue(emp.getEmpName());
          cell.setCellStyle(ordinaryCellStyle);
          range = new CellRangeAddress(m, m + 1, k, k);
          sheet.addMergedRegion(range);
          ExcelUtil.setRegionBorder(1, range, sheet, workbook);

          cell = dataRow1.createCell(++k); // 班次
          cell.setCellValue("休假");
          cell.setCellStyle(ordinaryCellStyle);

          cell = dataRow2.createCell(k); // 班次
          cell.setCellValue("加班");
          cell.setCellStyle(ordinaryCellStyle);

          String sql = XmlSqlMapper.getInstance().getPreparedSQL("考勤汇总");
          sql = sql.replaceAll("@startDate", DateUtil.formatShortDate(startDate));
          sql = sql.replaceAll("@endDate", DateUtil.formatShortDate(endDate));
          sql = sql.replaceAll("@empno", emp.getEmpNO());
          List<AtndSummarySheet> summarySheets = jdbc.querySampleList(AtndSummarySheet.class, sql);
          // 访问数据库，得到数据集
          for (int si = 0; si < summarySheets.size(); si++) {
            AtndSummarySheet summarySheet = summarySheets.get(si);
            // ReflectUtils.debugBean(summarySheet);
            cell = dataRow1.createCell(++k); // 请假
            if (summarySheet.getAbsence() != null) {
              cell.setCellValue(summarySheet.getAbsence());
            }
            cell.setCellStyle(ordinaryCellStyle);

            cell = dataRow2.createCell(k); // 加班
            if (summarySheet.getOverTime() != null) {
              if (summarySheet.getOverTime().floatValue() != 0f) {
                cell.setCellValue(summarySheet.getOverTime());
              }
            }
            cell.setCellStyle(ordinaryCellStyle);

          }
          m = m + 2; // 为下一条记录准备行数
        }

        // break ;
      }

      FileOutputStream fos = new FileOutputStream(new File(filename));
      workbook.write(fos);
      fos.flush();
      fos.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // @Test // 导出汇总表
  public void testExecuteHuizong() {
    String filename = "D:/考情汇总表——全.xlsx";// 设置下载时客户端Excel的名称
    //

    // Workbook workbook = new HSSFWorkbook();// 创建一个Excel文件 2003
    Workbook workbook = new SXSSFWorkbook();// 创建一个Excel文件 2007

    // 设置列宽
    // sheet.setColumnWidth(0, 1000);
    // sheet.setColumnWidth(1, 3500);
    // sheet.setColumnWidth(2, 3500);
    // sheet.setColumnWidth(3, 6500);
    // sheet.setColumnWidth(4, 6500);
    // sheet.setColumnWidth(5, 6500);
    // sheet.setColumnWidth(6, 6500);
    // sheet.setColumnWidth(7, 2500);

    // 设置列的样式
    // for (int i = 0; i <= 14; i++) {
    // sheet.setDefaultColumnStyle(i, sheetStyle);
    // }

    CellStyle columnHeadStyle = ExcelUtil.getColumnHeadStyle(workbook);
    CellStyle ordinaryCellStyle = ExcelUtil.getOrdinaryCellStyle(workbook);

    Font font = workbook.createFont();
    font.setFontName("宋体");
    font.setFontHeightInPoints((short) 10);

    // 另一个样式
    CellStyle centerstyle = workbook.createCellStyle();
    centerstyle.setFont(font);
    centerstyle.setAlignment(CellStyle.ALIGN_CENTER);// 左右居中
    centerstyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 上下居中
    centerstyle.setWrapText(true);
    // centerstyle.setLeftBorderColor(XSSFColor.BLACK.index);
    centerstyle.setBorderLeft((short) 1);
    // centerstyle.setRightBorderColor(XSSFColor.BLACK.index);
    centerstyle.setBorderRight((short) 1);
    centerstyle.setBorderBottom(CellStyle.BORDER_THIN); // 设置单元格的边框为粗体
    // centerstyle.setBottomBorderColor(XSSFColor.BLACK.index); // 设置单元格的边框颜色．
    // centerstyle.setFillForegroundColor(XSSFColor.WHITE.index);// 设置单元格的背景颜色．

    try {
      // 获取本月天数
      String yyyyMM = "201703";
      String year = yyyyMM.substring(0, 4);
      String mm = yyyyMM.substring(4);
      Date startDate = DateUtil.parseShortDate(year + "-" + mm + "-" + "01"); // 月初
      Date endDate = DateUtil.getLastDayOfMonth(startDate); // 月末
      List<Date> dates = DateUtil.getEachDateIn(startDate, endDate);

      int columnSize = dates.size() + 20;

      // TODO 获取部门 select uid , deptNO , DEPTNAME from f_dept_info

      Sheet sheet = ExcelUtil.createHead(workbook, "考勤表", "蓝岛海工2016年6月份考勤表", columnSize);

      sheet.createFreezePane(4, 5);// 冻结

      // 创建第二行
      Row row1 = sheet.createRow(1);
      Cell cell1 = row1.createCell(0);
      cell1.setCellValue("本次会议时间：2009年8月31日       前次会议时间：2009年8月24日");

      cell1.setCellStyle(centerstyle);
      // 合并单元格
      CellRangeAddress range = new CellRangeAddress(1, 2, 0, 7);
      sheet.addMergedRegion(range);

      int m = 3; // 开始 行
      int k = 0; // 开始列

      // 第三行
      Row row2 = sheet.createRow(m);
      Row row3 = sheet.createRow(m + 1);
      row2.setHeight((short) 350);
      row3.setHeight((short) 350);

      Cell cell = row2.createCell(k);
      cell.setCellValue("序号");
      cell.setCellStyle(columnHeadStyle);
      range = new CellRangeAddress(m, m + 1, k, k);
      sheet.addMergedRegion(range);
      ExcelUtil.setRegionBorder(1, range, sheet, workbook);

      cell = row2.createCell(++k);
      cell.setCellValue("工号");
      cell.setCellStyle(columnHeadStyle);
      range = new CellRangeAddress(m, m + 1, k, k);
      sheet.addMergedRegion(range);
      ExcelUtil.setRegionBorder(1, range, sheet, workbook);

      cell = row2.createCell(++k);
      range = new CellRangeAddress(m, m + 1, k, k);
      cell.setCellValue("姓名");
      sheet.addMergedRegion(range);
      cell.setCellStyle(columnHeadStyle);
      ExcelUtil.setRegionBorder(1, range, sheet, workbook);

      cell = row2.createCell(++k);
      range = new CellRangeAddress(m, m + 1, k, k);
      sheet.addMergedRegion(range);
      cell.setCellValue("班次");
      cell.setCellStyle(columnHeadStyle);

      for (Date date : dates) {
        // 日期
        cell = row2.createCell(++k);
        cell.setCellValue(DateUtil.formatDate(date, "MM/dd"));
        cell.setCellStyle(columnHeadStyle);
        // 星期
        cell = row3.createCell(k);
        cell.setCellValue(DateUtil.getWeek(date).getShortCnName());
        cell.setCellStyle(columnHeadStyle);
      }

      m = 5;
      // List<EmpInfo> empInfos = jdbc.findByAnnotatedSample(null, new EmpInfo("BI00014"));
      List<EmpInfo> empInfos = jdbc.findByAnnotatedSample(new EmpInfo());
      for (int i = 0; i < empInfos.size(); i++) {
        EmpInfo emp = empInfos.get(i);
        k = 0;
        Row dataRow1 = sheet.createRow(m);
        Row dataRow2 = sheet.createRow(m + 1);

        cell = dataRow1.createCell(k); // 序号
        cell.setCellValue(i + 1);
        cell.setCellStyle(ordinaryCellStyle);
        range = new CellRangeAddress(m, m + 1, k, k);
        sheet.addMergedRegion(range);
        ExcelUtil.setRegionBorder(1, range, sheet, workbook);

        cell = dataRow1.createCell(++k); // 工号
        cell.setCellValue(emp.getEmpNO());
        cell.setCellStyle(ordinaryCellStyle);
        range = new CellRangeAddress(m, m + 1, k, k);
        sheet.addMergedRegion(range);
        ExcelUtil.setRegionBorder(1, range, sheet, workbook);

        cell = dataRow1.createCell(++k); // 姓名
        cell.setCellValue(emp.getEmpName());
        cell.setCellStyle(ordinaryCellStyle);
        range = new CellRangeAddress(m, m + 1, k, k);
        sheet.addMergedRegion(range);
        ExcelUtil.setRegionBorder(1, range, sheet, workbook);

        cell = dataRow1.createCell(++k); // 班次
        cell.setCellValue("休假");
        cell.setCellStyle(ordinaryCellStyle);

        cell = dataRow2.createCell(k); // 班次
        cell.setCellValue("加班");
        cell.setCellStyle(ordinaryCellStyle);

        String sql = XmlSqlMapper.getInstance().getPreparedSQL("考勤汇总");
        sql = sql.replaceAll("@startDate", DateUtil.formatShortDate(startDate));
        sql = sql.replaceAll("@endDate", DateUtil.formatShortDate(endDate));
        sql = sql.replaceAll("@empno", emp.getEmpNO());
        List<AtndSummarySheet> summarySheets = jdbc.querySampleList(AtndSummarySheet.class, sql);
        // 访问数据库，得到数据集
        for (int si = 0; si < summarySheets.size(); si++) {
          AtndSummarySheet summarySheet = summarySheets.get(si);
          // ReflectUtils.debugBean(summarySheet);
          cell = dataRow1.createCell(++k); // 请假
          if (summarySheet.getAbsence() != null) {
            cell.setCellValue(summarySheet.getAbsence());
          }
          cell.setCellStyle(ordinaryCellStyle);

          cell = dataRow2.createCell(k); // 加班
          if (summarySheet.getOverTime() != null) {
            if (summarySheet.getOverTime().floatValue() != 0f) {
              cell.setCellValue(summarySheet.getOverTime());
            }
          }
          cell.setCellStyle(ordinaryCellStyle);

        }
        m = m + 2; // 为下一条记录准备行数
        // break;
      }

      // 列尾
      // int footRownumber = sheet.getLastRowNum();
      // Row footRow = sheet.createRow(footRownumber + 1);
      // Cell footRowcell = footRow.createCell(0);
      // footRowcell.setCellValue("                    审  定：XXX      审  核：XXX     汇  总：XX");
      // footRowcell.setCellStyle(centerstyle);
      // range = new CellRangeAddress(footRownumber + 1, footRownumber + 1, 0, 7);
      // sheet.addMergedRegion(range);

      FileOutputStream fos = new FileOutputStream(new File(filename));
      workbook.write(fos);
      fos.flush();
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // @Test
  public void testExecuteMingxi() {
    String sql = XmlSqlMapper.getInstance().getPreparedSQL("考勤");
    // sql = sql.replaceAll("@empno", "BI00327");

    JdbcTemplate jdbc = JdbcTemplate.getInstance();
    Workbook workbook = jdbc.exportNamelessWorkbook(sql);
    String tempFilePath = "D:/考勤201606.xlsx";

    FileOutputStream fos;
    try {
      fos = new FileOutputStream(new File(tempFilePath));
      workbook.write(fos);
      fos.flush();
      fos.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

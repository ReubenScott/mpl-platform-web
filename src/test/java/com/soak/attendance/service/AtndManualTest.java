package com.soak.attendance.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;

import com.soak.attendance.model.EmpInfo;
import com.soak.attendance.model.ScheduleType;
import com.soak.attendance.service.AtndMeasureService;
import com.soak.attendance.service.imp.AtndMeasureServiceImp;
import com.soak.framework.jdbc.JdbcHandler;
import com.soak.framework.util.BeanUtil;
import com.soak.framework.xml.XmlSqlMapper;

public class AtndManualTest {

  AtndMeasureService measureService;
  JdbcHandler jdbc;

  @Before
  public void setUp() throws Exception {
    measureService = new AtndMeasureServiceImp();
    jdbc = JdbcHandler.getInstance();
  }

//  @Test  // 第一步导入打卡数据
  public void testloadPunchRecord() {
    measureService.loadPunchRecord("E:/考勤/刷卡记录/");
  }

//  @Test
  public void testAtndRecordServiceImp() {
    for (ScheduleType type : measureService.getAllScheduletypes()) {
      BeanUtil.debugBean(type);
    }
  }

//  @Test
  public void testloadAtndManualApplicationForm() {
    String filePath = "E:/考勤/201604/4月份请假单，出差单，加班单（新）.xlsx";
//    measureService.loadOvertimeWorkApplicationForm(filePath);
//    bean.loadBusinessTripApplicationForm(filePath);
//    measureService.loadOffWorkApplicationForm(filePath);
    
  }
  
  

  @Test
  public void testAtndmeasureTest() {
    measureService.atndmeasureTest( "201604", "BI00371" , "BI00181");
//    BI00181
  /*  // 加班 请假 出差
    String sql = "SELECT emp.empNO , emp.empNAME ,dept.DEPTNAME FROM f_emp_info emp LEFT JOIN f_dept_info dept ON emp.deptid = dept.uid ";

    sql += " where emp.empNO = 'BI00149'" ;
    List<EmpInfo> emps = jdbc.querySampleList(EmpInfo.class, sql);

    for (EmpInfo emp : emps) {
      // 考勤统计
      measureService.atndmeasureTest(emp, "201604");
//      sql = sql.replaceAll("@empno", emp.getEmpNO());
      // 构造 XSSFWorkbook 对象，strPath 传入文件路径

      // break;
    }*/
    
  }
  

  // @Test
  public void testExecute() {
    String sql = XmlSqlMapper.getInstance().getPreparedSQL("考勤");
    sql = sql.replaceAll("@empno", "BI00327");

    JdbcHandler jdbc = JdbcHandler.getInstance();
    Workbook workbook = jdbc.exportExcel(sql);
    String tempFilePath = "D:/考勤2016-01-10.xlsx";

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

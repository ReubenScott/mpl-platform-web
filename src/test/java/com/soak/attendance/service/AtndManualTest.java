package com.soak.attendance.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;

import com.soak.attendance.model.ScheduleType;
import com.soak.attendance.service.AtndMeasureService;
import com.soak.attendance.service.AtndRecordAdditionService;
import com.soak.attendance.service.imp.AtndMeasureServiceImp;
import com.soak.attendance.service.imp.AtndRecordAdditionServiceImp;
import com.soak.framework.jdbc.JdbcHandler;
import com.soak.framework.util.BeanUtil;
import com.soak.framework.xml.XmlSqlMapper;

public class AtndManualTest {

  AtndMeasureService measureService;
  AtndRecordAdditionService atndRecordAdditionService ;
  JdbcHandler jdbc;

  @Before
  public void setUp() throws Exception {
    measureService = new AtndMeasureServiceImp();
    atndRecordAdditionService = new AtndRecordAdditionServiceImp();
    jdbc = JdbcHandler.getInstance();
  }

//  @Test  // 第一步导入打卡数据
  public void testloadPunchRecord() {
    atndRecordAdditionService.loadPunchRecord("E:/考勤/异常文件/");
  }

//  @Test
  public void testAtndRecordServiceImp() {
    for (ScheduleType type : atndRecordAdditionService.getAllScheduletypes()) {
      BeanUtil.debugBean(type);
    }
  }

  @Test
  public void testLoadExecute() {
    AtndRecordAdditionServiceImp bean = new AtndRecordAdditionServiceImp();
    String filePath = "E:/考勤/201604/2016年4月份请假单，出差单，加班单2.xlsx";
    bean.loadOvertimeWorkApplicationForm(filePath);
//    bean.loadBusinessTripApplicationForm(filePath);
//    bean.loadOffWorkApplicationForm(filePath);
    
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

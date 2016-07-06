package com.soak.attendance.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;


import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soak.attendance.model.PunchRecord;
import com.soak.attendance.service.AtndMeasureService;
import com.soak.common.json.JsonUtil;
import com.soak.framework.action.BaseAction;
import com.soak.framework.date.DateUtil;

public class AtndMeasureAction extends BaseAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1327853763505261778L;

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  private AtndMeasureService atndMeasureService = this.getBean("atndMeasureService");


  public AtndMeasureService getAtndMeasureService() {
    return atndMeasureService;
  }

  public void setAtndMeasureService(AtndMeasureService atndMeasureService) {
    this.atndMeasureService = atndMeasureService;
  }


  /**
   * 获取打卡记录
   */
  public void punchRecord() {
    Date startDate = DateUtil.parseShortDate("2016-04-03");
    Date endDate = DateUtil.parseShortDate("2016-04-06");
    
    List<PunchRecord>  punchRecords = atndMeasureService.queryPunchRecords("BI00124",startDate,endDate);
    
    // 定义JSON字符串
//    String jsonStr = "{\"id\": 2," + " \"title\": \"json title\", " + "\"config\": {" + "\"width\": 34," + "\"height\": 35," + "}, \"data\": ["
//        + "\"JAVA\", \"JavaScript\", \"PHP\"" + "]}";
//    // 创建JSONObject对象
//    JSONObject json = new JSONObject();
//    // 向json中添加数据
//    json.put("name", "wanglihong");
//    json.put("age", 12);
//    json.put("email", "sdsd@hotmlsom.com");
    // Convert object to JSON string  
    
    
/*    
    JsonConfig jsonConfig = new JsonConfig();  
    jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());  
  
    // 创建JSONArray数组，并将json添加到数组
//    for(PunchRecord punchRecord : punchRecords ){
//      JSONObject sss = JSONObject.fromObject(punchRecord);
//      break;
//    }
    JSONArray array = JSONArray.fromObject(punchRecords, jsonConfig);
    String jsonStr = array.toString();
    System.out.println(jsonStr);
*/    
    super.ajaxResponse(JsonUtil.toJson(punchRecords));
  }


}

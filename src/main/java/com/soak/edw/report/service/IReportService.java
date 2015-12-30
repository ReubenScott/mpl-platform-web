package com.soak.edw.report.service;

import java.util.List;

import com.soak.framework.service.IBasicService;

public interface IReportService extends IBasicService {
  
  public List<?> findLicai(String sqlName, String value);
 
}

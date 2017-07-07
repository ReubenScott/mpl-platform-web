package com.kindustry.edw.report.service;

import java.util.List;

import com.kindustry.framework.service.IBasicService;

public interface IReportService extends IBasicService {
  
  public List<?> findLicai(String sqlName, String value);
 
}

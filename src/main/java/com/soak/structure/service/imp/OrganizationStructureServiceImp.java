package com.soak.structure.service.imp;

import java.util.List;

import com.soak.framework.service.imp.BasicServiceImp;
import com.soak.structure.model.DeptInfo;
import com.soak.structure.service.OrganizationStructureService;

public class OrganizationStructureServiceImp extends BasicServiceImp implements OrganizationStructureService{

  public List<DeptInfo> getDeptInfo() {
    return basicDao.findByAnnotatedSample(new DeptInfo());
  }

  
  
}

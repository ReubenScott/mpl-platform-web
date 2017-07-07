package com.kindustry.structure.service.imp;

import java.util.List;

import com.kindustry.framework.service.imp.BasicServiceImp;
import com.kindustry.structure.model.DeptInfo;
import com.kindustry.structure.service.OrganizationStructureService;

public class OrganizationStructureServiceImp extends BasicServiceImp implements OrganizationStructureService{

  public List<DeptInfo> getDeptInfo() {
    return basicDao.findByAnnotatedSample(new DeptInfo());
  }

  
  
}

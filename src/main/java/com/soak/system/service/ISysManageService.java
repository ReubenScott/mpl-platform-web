package com.soak.system.service;

import java.util.List;

import com.soak.framework.service.IBasicService;
import com.soak.system.model.Menu;

public interface ISysManageService extends IBasicService {
  
  public List<Menu> getAllMenuList();
  
  public List<?> getMenuListByUID(String userid);
  
 
}

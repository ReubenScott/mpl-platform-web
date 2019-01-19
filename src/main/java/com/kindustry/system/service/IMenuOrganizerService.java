package com.kindustry.system.service;

import java.util.List;

import com.kindustry.framework.service.IBaseService;
import com.kindustry.system.model.Menu;

public interface IMenuOrganizerService extends IBaseService {
  
  public List<Menu> getAllMenuList();
  
  public List<?> getMenuListByUID(String userid);
  

 
}

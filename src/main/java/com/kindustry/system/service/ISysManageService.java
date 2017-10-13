package com.kindustry.system.service;

import java.util.List;

import com.kindustry.framework.service.IBasicService;
import com.kindustry.system.model.Menu;

public interface ISysManageService extends IBasicService {
  
  public List<Menu> getAllMenuList();
  
  public List<?> getMenuListByUID(String userid);
  
  public void dragMenu(String sid, String target, String targetType, String dropPosition);
}

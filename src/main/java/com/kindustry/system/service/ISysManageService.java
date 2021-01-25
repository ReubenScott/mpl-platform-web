package com.kindustry.system.service;

import java.util.List;

import com.kindustry.framework.service.IBaseService;
import com.kindustry.system.entity.Menu;

public interface ISysManageService extends IBaseService {
  
  public List<Menu> getAllMenuList();
  
  public List<?> getMenuListByUID(String userid);
  
  public void dragMenu(String sid, String target, String targetType, String dropPosition);
}

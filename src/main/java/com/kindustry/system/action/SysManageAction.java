package com.kindustry.system.action;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kindustry.common.json.JsonUtil;
import com.kindustry.framework.action.BaseAction;
import com.kindustry.framework.cache.EhCacheImpl;
import com.kindustry.system.model.Menu;
import com.kindustry.system.service.ISysManageService;

public class SysManageAction extends BaseAction {

  private static final long serialVersionUID = -8468063363697184826L;

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  private ISysManageService sysMangerService = this.getBean("sysMangerService");;

  private Menu menu;

  public void setMenu(Menu menu) {
    this.menu = menu;
  }

  public Menu getMenu() {
    return menu;
  }

  public ISysManageService getSysMangerService() {
    return sysMangerService;
  }

  public void setSysMangerService(ISysManageService sysMangerService) {
    this.sysMangerService = sysMangerService;
  }

  /**
   * 
   * 菜单树
   */
  public void menuList() {
    List<Menu> list = sysMangerService.getAllMenuList();
    // EhCacheImpl aa = this.getBean("ehCache");
    // System.out.println(aa);

    JSONArray jsonTree = new JSONArray();

    for (Menu menu : list) {
      JSONObject objcet = JSONObject.fromObject(menu);
      jsonTree.add(objcet);
    }
    // JsonUtil.toJson("root",jsonTree)
    System.out.println(jsonTree.toString());

    super.ajaxResponse(jsonTree.toString());
  }

  public void menuList2() {

    List<Menu> list = sysMangerService.getAllMenuList();
    EhCacheImpl aa = this.getBean("ehCache");
    System.out.println(aa);

    // ApplicationContext ac = new FileSystemXmlApplicationContext("applicationContext.xml");
    // logger.debug(JsonUtil.toJson("root",list));

    JSONArray jsonTree = new JSONArray();

    for (Menu menu : list) {
      JSONObject objcet = sysMenu2JsonString(menu);
      jsonTree.add(objcet);
    }
    // JsonUtil.toJson("root",jsonTree)
    System.out.println(jsonTree.toString());

    super.ajaxResponse(jsonTree.toString());
  }

  private JSONObject sysMenu2JsonString(Menu menu) {
    JSONObject jsonMenu = new JSONObject();
    try {
      jsonMenu.put("sid", menu.getSid());
      jsonMenu.put("menuName", menu.getMenuName());
      jsonMenu.put("menuLevel", menu.getMenuLevel());
      jsonMenu.put("orderNum", menu.getOrderNum());
      if (menu.getIconUrl() != null && !menu.getIconUrl().equals("")) {
        // jsonMenu.put("icon",iconpath+"/"+menu.getIconUrl());
        jsonMenu.put("iconUrl", menu.getIconUrl());
      }
      if (menu.getChildren() != null && menu.getChildren().size() > 0) {
        JSONArray child = new JSONArray();
        Set<Menu> menuset = menu.getChildren();
        for (Menu cmenu : menuset) {
          // if(cmenu.getDelFlag()==0){
          child.add(sysMenu2JsonString(cmenu));
          // }
        }
        jsonMenu.put("children", child);
        jsonMenu.put("leaf", false);
      } else {
        jsonMenu.put("leaf", true);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return jsonMenu;
  }

  // 删除菜单
  public void deleteMenu() {
    System.out.println(menu);
    System.out.println(menu.getSid());
    
    sysMangerService.deleteAnnotatedEntity(menu);
    
    JSONObject res = new JSONObject();
    res.put("success", true);
    res.put("msg", "保存成功");

    System.out.println(res.toString());

    super.ajaxResponse(res.toString());
  }

}

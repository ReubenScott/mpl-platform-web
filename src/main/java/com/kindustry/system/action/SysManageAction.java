package com.kindustry.system.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kindustry.common.json.JsonUtil;
import com.kindustry.common.util.StringUtil;
import com.kindustry.framework.action.BaseAction;
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
  // @Cacheable(value="menuCache",key="'UserMenuKey'+#userid")
  public void menuList() {
    String cacheMenu = super.cacheGet("contentCache", "menutree"); // 菜单缓存
    if (cacheMenu == null) {
      List<Menu> list = sysMangerService.getAllMenuList();
      cacheMenu = JsonUtil.toJson(list);
      super.cachePut("contentCache", "menutree", cacheMenu);
    }
    super.ajaxResponse(cacheMenu);
  }

  /*
   * private JSONObject sysMenu2JsonString(Menu menu) {
   * JSONObject jsonMenu = new JSONObject();
   * try {
   * jsonMenu.put("sid", menu.getSid());
   * jsonMenu.put("menuName", menu.getMenuName());
   * jsonMenu.put("menuLevel", menu.getMenuLevel());
   * jsonMenu.put("orderNum", menu.getOrderNum());
   * if (menu.getIconUrl() != null && !menu.getIconUrl().equals("")) {
   * // jsonMenu.put("icon",iconpath+"/"+menu.getIconUrl());
   * jsonMenu.put("iconUrl", menu.getIconUrl());
   * }
   * if (menu.getChildren() != null && menu.getChildren().size() > 0) {
   * JSONArray child = new JSONArray();
   * Set<Menu> menuset = menu.getChildren();
   * for (Menu cmenu : menuset) {
   * // if(cmenu.getDelFlag()==0){
   * child.add(sysMenu2JsonString(cmenu));
   * // }
   * }
   * jsonMenu.put("children", child);
   * jsonMenu.put("leaf", false);
   * } else {
   * jsonMenu.put("leaf", true);
   * }
   * } catch (Exception e) {
   * e.printStackTrace();
   * }
   * return jsonMenu;
   * }
   */

  //  拖动报表修改类别或顺序
  public void dragMenu() {

    HttpServletRequest request = ServletActionContext.getRequest();
    String sid = request.getParameter("sid");
    String target = request.getParameter("target");
    String targetType = request.getParameter("targetType");
    String dropPosition = request.getParameter("dropPosition");
    System.out.println(sid);
    System.out.println(target);
    System.out.println(targetType);
    System.out.println(dropPosition);
    
    sysMangerService.dragMenu(sid, target, targetType, dropPosition);
    
    
    Map res = new HashMap();
    try {
//      SysReport report = reportManEService.dragReport(sid, target, targetType, dropPosition);
      res.put("success", true);
      res.put("msg", "保存成功！");
//      res.put("sid", report.getSid());
//      if (report.getSysReportType() != null) {
//        res.put("type_id", report.getSysReportType().getSid());
//        res.put("type_name", report.getSysReportType().getReportTypeName());
//      } else {
//        res.put("type_id", "");
//        res.put("type_name", "");
//      }
//      res.put("postion", report.getPostion());
    } catch (Exception e) {
      e.printStackTrace();
      try {
        res.put("success", false);
        res.put("msg", e.getMessage());
      } catch (Exception ee) {
      }
    }
//    this.resParam = res.toString();
//    return AJAXUTF8;
  }

  // 删除菜单
  public void deleteMenu() {
    HttpServletRequest request = ServletActionContext.getRequest();
    String sid = request.getParameter("sid");

    if (!StringUtil.isEmpty(sid)) {
      sysMangerService.deleteEntityBySID(Menu.class, sid);
    }

    // sysMangerService.deleteAnnotatedEntity(menu);
    super.cachEvict("contentCache", "menutree"); // 清除缓存
    Map res = new HashMap();
    res.put("success", true);
    res.put("msg", "删除成功");
    super.ajaxResponse(JsonUtil.toJson(res));
  }

}

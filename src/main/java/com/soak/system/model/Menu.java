package com.soak.system.model;

import com.soak.framework.orm.Column;
import com.soak.framework.orm.Table;

/***
 * 
 * 系统菜单
 * 
 * @author reuben
 * 
 */
@Table(schema="bioffice", name = "sys_menu", pk = "sid")
public class Menu {

  @Column(name = "sid")
  private String uid; // 主键

  @Column(name = "menu_type")
  private String menuType; // 菜单类型 varchar(255)

  @Column(name = "menu_level")
  private Integer menuLevel; // 菜单层级 bigint(20)

  @Column(name = "menuname")
  private String menuName;// 菜单名称 varchar(255)

  @Column(name = "url")
  private String url; // 菜单url地址 varchar(255)

  @Column(name = "order_num")
  private String orderNum; // 菜单排序 bigint(20)

  @Column(name = "icon_url")
  private String iconUrl; // 菜单图标 varchar(255)

  @Column(name = "menu_event")
  private String menuEvent; // 菜单事件 varchar(255)

  @Column(name = "parent_id")
  private String parentId; // 父级菜单 varchar(255)

//  @Column(name = "del_flag")
  private Boolean isDelete; // bigint(20)

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getMenuType() {
    return menuType;
  }

  public void setMenuType(String menuType) {
    this.menuType = menuType;
  }

  public Integer getMenuLevel() {
    return menuLevel;
  }

  public void setMenuLevel(Integer menuLevel) {
    this.menuLevel = menuLevel;
  }

  public String getMenuName() {
    return menuName;
  }

  public void setMenuName(String menuName) {
    this.menuName = menuName;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(String orderNum) {
    this.orderNum = orderNum;
  }

  public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public String getMenuEvent() {
    return menuEvent;
  }

  public void setMenuEvent(String menuEvent) {
    this.menuEvent = menuEvent;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public Boolean getIsDelete() {
    return isDelete;
  }

  public void setIsDelete(Boolean isDelete) {
    this.isDelete = isDelete;
  }

}

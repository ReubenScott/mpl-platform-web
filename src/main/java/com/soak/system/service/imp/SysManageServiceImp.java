package com.soak.system.service.imp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.soak.framework.service.Imp.BasicServiceImp;
import com.soak.system.model.Menu;
import com.soak.system.service.ISysManageService;

public class SysManageServiceImp extends BasicServiceImp implements ISysManageService {

  public List<?> getMenuListByUID(String userid) {

    return null;
  }

  public List<Menu> getAllMenuList() {
    List<Menu> menus = findMenuByUser("");

    return buildMenuTree(menus);
  }
  

  private List<Menu> buildMenuTree(List<Menu> menus) {
    List<Menu> tree = new ArrayList<Menu>();
    Set<Serializable> sids = new HashSet<Serializable>();
    Set<Serializable> roots = new HashSet<Serializable>();
    for(Menu menu : menus){
      sids.add(menu.getSid());
      roots.add(menu.getParentId());
    }
    
    // 获取跟节点
    roots.removeAll(sids);
    for (Iterator<Menu> iterator = menus.iterator(); iterator.hasNext();) {
      Menu root = iterator.next();
      if (roots.contains(root.getParentId())) {
        tree.add(root);
        root.setChildren(new HashSet<Menu>());
        // 删除该数据， 减少下次的查询时间
        iterator.remove();
      }
    }
    
    //递归 子菜单
    for(Menu root : tree ){
      buildTree(root, menus);
    }
    
    return tree;
  }

  
  private void buildTree(Menu parent, List<Menu> menus) {
    for (Iterator<Menu> iterator = menus.iterator(); iterator.hasNext();) {
      Menu item = iterator.next();
      if (parent.getSid().equals(item.getParentId())) {
          parent.getChildren().add(item);
          // 删除该数据， 减少下次的查询时间
          iterator.remove();
      }
    }
    for (Menu item : parent.getChildren()) {
        buildTree(item, menus);
    }
  }
  

}

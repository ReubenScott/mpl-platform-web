package com.kindustry.system.service.imp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.kindustry.framework.service.imp.BasicServiceImp;
import com.kindustry.system.model.Menu;
import com.kindustry.system.service.ISysManageService;

public class SysManageServiceImp extends BasicServiceImp implements ISysManageService {

  public List<?> getMenuListByUID(String userid) {

    return null;
  }

  public List<Menu> getAllMenuList() {
    List<Menu> menus = findMenuByUser("");

    return buildMenuTree(menus);
  }

  /**
   * 构建菜单树， 步骤一 : 找出跟节点（允许有多个跟节点）
   * 
   * @param menus
   * @return
   */
  private List<Menu> buildMenuTree(List<Menu> menus) {
    List<Menu> tree = new ArrayList<Menu>();
    Set<Serializable> sids = new HashSet<Serializable>();
    Set<Serializable> roots = new HashSet<Serializable>();
    for (Menu menu : menus) {
      sids.add(menu.getSid());
      roots.add(menu.getPid());
    }

    // 获取跟节点
    roots.removeAll(sids);
    for (Iterator<Menu> iterator = menus.iterator(); iterator.hasNext();) {
      Menu root = iterator.next();
      if (roots.contains(root.getPid())) {
        tree.add(root);
        root.setChildren(new HashSet<Menu>());
        // 删除该数据， 减少下次的查询时间
        iterator.remove();
      }
    }

    // 递归 子菜单
    for (Menu root : tree) {
      buildTree(root, menus);
    }

    return tree;
  }

  /**
   * 构建菜单树， 步骤二 ： 连接父节点与子节点
   * 
   * @param menus
   * @return
   */
  private void buildTree(Menu parent, List<Menu> menus) {
    for (Iterator<Menu> iterator = menus.iterator(); iterator.hasNext();) {
      Menu item = iterator.next();
      if (parent.getSid().equals(item.getPid())) {
        parent.getChildren().add(item);
        // 删除该数据， 减少下次的查询时间
        iterator.remove();
      }
    }
    for (Menu item : parent.getChildren()) {
      buildTree(item, menus);
    }
  }

  /**
   *  菜单拖动
   * @param sid
   * @param target
   * @param targetType
   * @param dropPosition
   */
  public void dragMenu(String sid, String target, String targetType, String dropPosition) {
//    SysReport report = sysReportDao.get(sid);
//    if (targetType.equals("2")) {// 分类
//      SysReportType type = sysReportTypeDao.get(target);
//      if (dropPosition.equals("append")) {
//        report.setSysReportType(type);
//      } else {
//        report.setSysReportType(type.getSysReportType());
//      }
//    } else if (targetType.equals("1")) { // 报表
//      SysReport treport = sysReportDao.get(target);
//      report.setSysReportType(treport.getSysReportType());
//      if (dropPosition.equals("before")) {
//        report.setPostion(treport.getPostion() != null ? treport.getPostion() - 1 : report.getPostion());
//      } else if (dropPosition.equals("after")) {
//        report.setPostion(treport.getPostion() != null ? treport.getPostion() + 1 : report.getPostion());
//      } else {
//        throw new BizException("参数错误！");
//      }
//    } else {
//      throw new BizException("参数错误！");
//    }
//    sysReportDao.update(report);
//    return report;
  }

}

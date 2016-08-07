
-- 系统菜单
-- DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE sys_menu (
  sid varchar(255) default NULL,
  menuname varchar(255) default NULL,
  menu_type varchar(255) default NULL,
  parent_id varchar(255) default NULL,
  menu_level bigint(20) default NULL,
  url varchar(255) default NULL,
  menu_event varchar(255) default NULL,
  order_num bigint(20) default NULL,
  icon_url varchar(255) default NULL,
  helphref varchar(255) default NULL,
  del_flag int(2) default NULL,
  PRIMARY KEY(sid) 
) ;

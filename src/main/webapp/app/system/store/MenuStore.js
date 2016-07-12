Ext.define('epl.system.store.MenuStore', {
  extend : 'Ext.data.TreeStore',
  requires : 'epl.system.model.MenuModel',
  model : 'epl.system.model.MenuModel',
  autoLoad : true,
  
  proxy : {
    type : 'ajax',
    url : 'system/SysManage/menuList.htm',
    reader : {
      type : 'json',
      successProperty : 'success'
    }
  }


});

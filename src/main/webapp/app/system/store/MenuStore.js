Ext.define('epl.system.store.MenuStore', {
  extend : 'Ext.data.TreeStore',
  model : 'epl.system.model.MenuModel',
  autoLoad : false,
  
  proxy : {
    type : 'ajax',
    url : 'system/SysManage/menuList.htm',
    reader : {
      type : 'json',
      successProperty : 'success'
    }
  },

  /**
   * 排序：按照结点的orderNum进行排序。
   * @param {Ext.data.NodeInterface} node1
   * @param {Ext.data.NodeInterface} node2
   */
  sorters: [{
    sortFn: function(node1, node2) {
        var order1 = node1.get('orderNum'),
            order2 = node2.get('orderNum'),
            text1 = node1.get('text'),
            text2 = node2.get('text'),
            dt = order1 - order2;
        return (dt == 0) ? (text1 < text2 ? -1 : 1) : dt;
    }
  }]

});

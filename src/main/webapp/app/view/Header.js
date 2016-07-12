Ext.define('epl.view.Header', {
  extend : 'Ext.panel.Panel',
  alias : 'widget.north',
  border : false,
  layout : 'anchor',
  region : 'north',
  cls : 'docs-header',
  items : [ {
    id : 'header-top',
    xtype : 'box',
    cls : 'header',
    border : false,
    anchor : 'none -25',
    html : '<span style="margin-left:10px;font-size:30;color:blue;FONT-FAMILY:微软雅黑">ExtTest</span>'
  // html:'<h1>实训管理系统</h1>'
      }, new Ext.Toolbar( {
        items : [ {
          // 此处加载登录用户信息
          xtype : 'label',
          iconCls : 'grid-add',
          id : 'head-lb-1',
          cls : 'welcome',
          text : '首页',
          margin : '0 20 0 20'
        }, {
          xtype : 'button',
          id : 'xdbl-xdbl',
          margin : '0 20 0 50',
          cls : 'welcome',
          text : '信贷补录'
        }, {
          xtype : 'button',
          id : 'PunchRecordGrid',
          margin : '0 20 0 50',
          cls : 'welcome',
          text : '打卡记录'
        }, '->', {
          xtype : 'button',
          text : '打开首页',
          iconCls : 'grid-add',
          // tooltip: '全屏显示主操作窗口',
          handler : function() {

          }
        }, '-', {
          xtype : 'button',
          text : '注销',
          iconCls : 'grid-add',
          handler : function() {

          }
        }, '-' ]
      }) ]
});

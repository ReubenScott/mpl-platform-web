Ext.define('epl.system.controller.MenuManageController',{ 
  extend: 'Ext.app.Controller', 
  models: ['epl.system.model.MenuModel'],  
  stores: ['epl.system.store.MenuStore'],  

  init: function() {
    this.control({
      // 菜单管理
      'center panel[id=menu-nodePanel]' : {  
        itemcontextmenu: this.treeContextmenu ,  // 右键
        beforeitemappend: this.beforeItemAppend
      }
      
    });
  },

  beforeItemAppend : function(view, node, item, index, e){
     Ext.apply(node.data, node.raw);
  },
  // 右键菜单
  treeContextmenu : function(view, node, item, index, e){ 
    var treeContextMenu = Ext.create('Ext.menu.Menu', {
      width: 100,
//      margin: '0 0 10 0',
      floating: false,  // 通常你想设置这个为真 (默认的)
      renderTo: Ext.getBody(),  // 通常由它的包含容器呈现
      allowOtherMenus:false,
      items: [{
          text: '新建子菜单',
          iconCls:'button_add', //  icon: '/resources/images/button/add.png',
          handler: this.addChild
      },{
          text: '删除菜单',
          iconCls:'button_remove'
      }]
    });

    e.stopEvent();
    treeContextMenu.context = {view: view, node: node, item: item, index: index};
    if(node.hasChildNodes()){
   //   Ext.getCmp('delMenuItem').setDisabled(true);
    }else{
  //    Ext.getCmp('delMenuItem').setDisabled(false);
    }
    treeContextMenu.showAt(e.getXY());
  },

  /**
   * 新增子菜单
   */
  addChild : function(item, e){
//    Ext.getCmp('myform').getForm().reset();
    var context = item.parentMenu.context;
    var node = context.node;
    console.log(node.data.sid , node.data.text , node.data.menuLevel);
    
    if(!node.isRoot()){
      Ext.getCmp('menuLevel').setValue(node.data.menuLevel+1);
      Ext.getCmp('psid').setValue(node.data.sid);
      Ext.getCmp('parentText').setValue(node.data.text);
    }else{
      Ext.getCmp('menuLevel').setValue(0);
      Ext.getCmp('parentText').setValue('系统菜单');
    }
  }
  
  
});


//Ext.create('Ext.menu.Menu',{
//allowOtherMenus:false,
//items:[{
//  text:'新建下级菜单',
////  icon: '/resource/images/buttons/add.gif',
////   handler:addChild
//},{
//  text:'删除菜单',
//  id:'delMenuItem',
////  icon: '/resource/images/buttons/jian.gif',
////   handler:del
//}]
//});

//var treeContextMenu = Ext.create('Ext.menu.Menu',{
//  allowOtherMenus:false,
//  items:[{
//    text:'新建下级菜单',
//    icon: '/resource/images/buttons/add.gif',
//  //   handler:addChild
//  },{
//    text:'删除菜单',
//    id:'delMenuItem',
//    icon: '/resource/images/buttons/jian.gif',
// //   handler:del
//  }]
//});


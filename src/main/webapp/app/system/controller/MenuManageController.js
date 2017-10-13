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
    //禁用浏览器的右键相应事件  
    e.preventDefault();  
    e.stopEvent(); 

    //当点击时隐藏右键菜单  
//    this.up("menu").hide();  
    
    
    var treeContextMenu = Ext.create('Ext.menu.Menu', {
      width: 100,
      margin: '0 0 0 0',
      floating: true,  // 通常你想设置这个为真 (默认的)
      renderTo: Ext.getBody(),  // 通常由它的包含容器呈现
      allowOtherMenus:false,
      items: [{
          text: '新建子菜单',
          iconCls:'button_add', //  icon: '/resources/images/button/add.png',
          handler: this.addChild
      },{
          text: '删除菜单',
          iconCls:'button_remove',
          handler: this.delMenu
      }]
    });
    
    treeContextMenu.context = {view: view, node: node, item: item, index: index};
    
    if(node.hasChildNodes()){  // 有子菜单的禁用删除
      treeContextMenu.child("[iconCls=button_remove]").setDisabled(true);
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
  /**
   * 删除菜单
   */
  , delMenu : function(item, e){
    var context = item.parentMenu.context,
    node = context.node;
    Ext.Msg.confirm('删除','确定要删除【'+node.data.text+'】？',function(btn){
      if(btn=='yes'){
        Ext.Ajax.request({
          url: 'system/SysManage/deleteMenu.htm',
          params:{sid:node.data.sid},
          callback:function(options,success,response){
            if(success){
              var r = Ext.JSON.decode(response.responseText);
              Ext.Msg.alert('提示',r.msg);
              if(r.success){
             //   if(node.data.sid==Ext.getCmp('sid').getValue()){ //如果表单显示的是当前删除的节点，清空
             //     Ext.getCmp('myform').getForm().reset();
             //   }
                node.remove(true); //从节点上删除
              }
            }else{
              alert("请求出错");
            }
          }
        });
      }
    });
  }
  
});

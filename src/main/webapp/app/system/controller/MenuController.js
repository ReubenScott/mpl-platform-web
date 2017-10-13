Ext.define('epl.system.controller.MenuController',{ 
    extend: 'Ext.app.Controller', 
    models: ['epl.system.model.MenuModel'],  
    stores: ['epl.system.store.MenuStore'],  

    requires : [ 
 //     'epl.system.view.MenuManage',
//      'epl.system.view.MenuNodeTree',
    ], 

    // 初始化部分，下面是部分是给菜单绑定单击事件
    init: function () {  
        this.control({  
          // 菜单管理
          'center panel[id=menu-nodePanel]' : {  
              itemclick: this.showMenu
          },
          'center panel[id=menu-form] button[action=delete]': {
             click: this.deleteMenu
          }

        });
    },
    // 点击 树形菜单
    showMenu : function(selModel, record){ 
      var mid = record.get('sid') ;
//      if (record.get('leaf')) { // 如果是叶子节点 （没有子菜单）
          var editform = Ext.getCmp('menu-form').form;
          editform.findField('menu.sid').setValue(record.get('sid'));
          editform.findField('menu.menuName').setValue(record.get('text'));
          editform.findField('menu.url').setValue(record.get('url'));
          editform.findField('xtype').setValue(record.get('xtype'));
//      }
    }
    // 点击 删除菜单
    ,deleteMenu : function(btn) {
      btn.up('form').getForm().submit({
        clientValidation: true,
        url: 'system/SysManage/deleteMenu.htm',
        method:'POST',
        success:function(form,action){
          Ext.Msg.alert('提示',action.result.msg);
          var cnode;  //父节点
          if(Ext.getCmp('sid').getValue()==""){//新增节点
            Ext.getCmp('menuTree').getRootNode().cascadeBy(function(node){ //找父节点
              if(Ext.getCmp('psid').getValue()==""){ //根目录
                cnode = Ext.getCmp('menuTree').getRootNode();
                return false;
              }
              if (node.get("sid") == Ext.getCmp('psid').getValue()) {
                cnode = node;
                return false;
                   }
            });
          }
        },
        failure:function(form,action){
          Ext.Msg.alert('提示',action.result.msg);
        }
      });
    }
    

});
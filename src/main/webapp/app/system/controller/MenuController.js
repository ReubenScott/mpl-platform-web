Ext.define('epl.system.controller.MenuController',{ 
    extend: 'Ext.app.Controller', 
    models: ['epl.system.model.MenuModel'],  
    stores: ['epl.system.store.MenuStore'],  
    views: ['MainLayout'],  

    requires : [ 
        'epl.xdbl.xdbl',
    ], 

    init: function () {  
        // 初始化部分，下面是部分是给菜单绑定单击事件
        this.control({  
          'menuTree': {  
              itemclick: this.loadMenu
          },
            // 设定列表添加按钮的事件
          'north button[id=xdbl-xdbl]':{
              click: this.addTab
            }

        });
    },
    // 点击 树形菜单
    loadMenu:function(selModel, record){ 
      var mid = record.get('sid') ;
      if (record.get('leaf')) { // 如果是叶子节点 （没有子菜单）
        if(record.get('optype')=='window'){
            var win= Ext.getCmp(record.get('url'));
            if(!win){
                win=Ext.widget(record.get('url'))
            }
            win.show();
        } else {
            var panel = Ext.getCmp(mid);
            if(!panel){  
              panel ={
                  id: mid ,
                  title: record.get('text'), 
                  xtype: record.get('url'),
                  closable: true  
              };
              this.openTab(panel ,record.get('url')); 
              alert(2);
            } else { 
              var main = Ext.getCmp("content-panel"); 
              main.setActiveTab(panel);  
            } 
        }
          
      }  
    },    
    // 添加标签页
    addTab : function(button){
        var mainTabPanel = Ext.getCmp('content-panel') ;
        var items = mainTabPanel.items.items;
        for(var item in items){
           console.log(items[item].id);
           if(items[item].id == "xdbl-tabpan"){
           // return ;
           }
        }

//        var tab=mainTabPanel.add({
//            title: button.text ,
//            closable:true,  // 允许关闭，同时新建关闭按钮
//            // id : button.id,
//            // xtype:'xdbl-xdbl',
//            xtype: button.id,
//
//        });
        
        var tab =  Ext.widget(button.id,{title:'系统设置>用户管理'});
        mainTabPanel.add(tab);
        mainTabPanel.setActiveTab(tab);     
          
    },
    
    // 添加标签页
    openTab : function (panel,id){
      var o = (typeof panel == "string" ? panel : id || panel.id); 
      var main = Ext.getCmp("content-panel"); 
      var tab = main.getComponent(o);      
      if (tab) { 
          main.setActiveTab(tab);  
      } else if(typeof panel!="string"){  
          panel.id = o;  
              alert(o);
          var p = main.add(panel);  
              alert(3);
          main.setActiveTab(p);  
      }  
              alert(4); 

   } 

});
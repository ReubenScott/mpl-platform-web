/**
 * ClassName 考勤 打卡记录 管理控制器
 */
Ext.define('epl.attnd.controller.PunchRecordController', {
  extend : 'Ext.app.Controller',
  views :  [ 'epl.attnd.view.PunchRecordGrid' ],
  stores : [ 'epl.attnd.store.PunchRecordStore' ],
  models : [ 'epl.attnd.model.PunchRecord' ],

  init: function () {  
      // 初始化部分，下面是部分是给菜单绑定单击事件
      this.control({  
          // 设定列表添加按钮的事件
        'north button[id=PunchRecordGrid]':{
            click: this.addTab
          }

      });
  },    
  // 添加标签页
  addTab : function(button){
      var mainTabPanel = Ext.getCmp('content-panel') ;
      var punchGrid = mainTabPanel.child(button.id);
      if(!punchGrid){
//        tab = Ext.widget(button.id,{title:'系统设置>角色管理'});
//        mainTabPanel.add(tab);
        punchGrid = mainTabPanel.add({
            title: button.text ,
            closable:true,  // 允许关闭，同时新建关闭按钮
            // id : 'custMgrGrid',
            xtype: button.id
  
        });

        var punchGridStore = punchGrid.getStore() ;
        // 切换 请求地址
        punchGridStore.getProxy().url = punchGridStore.api['search'] ;
        //reload data
        punchGridStore.load(); 
          //grid.load({whereSql:'in ('A0')'})
      }
      mainTabPanel.setActiveTab(punchGrid);     

      
//      var items = mainTabPanel.items.items;
//      for(var item in items){
//         console.log(items[item].id);
//         if(items[item].id == "xdbl-tabpan"){
//         // return ;
//         }
//      }
  }





  
});

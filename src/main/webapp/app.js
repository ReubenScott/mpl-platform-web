Ext.onReady(function(){
	Ext.QuickTips.init();
	Ext.Loader.setConfig({
		enabled:true
	});  //开启动态加载
	Ext.application({
		name : 'epl',
		appFolder : "app", //指定根目录
		launch:function(){  // 当前页面加载完成执行函数
	        Ext.create('Ext.container.Viewport', {
			    layout: 'fit', 
			    hideBorders: true, 
	            items: {
	            	xtype: 'mainlayout'
	            }
	        });
		},
		controllers:[
			'epl.system.controller.MainController',
      'epl.system.controller.MenuController',
			'epl.system.controller.MenuManageController',
      'epl.attnd.controller.PunchRecordController',
      
      //
      'XDBLController'
		]
	});
})

Ext.onReady(function(){
	Ext.QuickTips.init();
	Ext.Loader.setConfig({
		enabled:true
	});  //开启动态加载
	Ext.application({
		name : 'app',
		appFolder : "app", //指定根目录
		launch:function(){  // 当前页面加载完成执行函数
            Ext.ariaWarn = Ext.emptyFn;
	        Ext.create('Ext.container.Viewport', {
			    layout: 'fit', 
			    hideBorders: true, 
	            items: {
	            	xtype: 'mainlayout'
	            }
	        });
		},
		controllers:[
             'app.system.controller.MainController',
			 'app.system.controller.MenuController',
			 'app.system.controller.MenuManageController',
	         'app.attnd.controller.PunchRecordController',      
	      //'XDBLController'
		]
	});
})

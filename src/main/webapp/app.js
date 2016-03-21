Ext.onReady(function(){
	Ext.QuickTips.init();
	Ext.Loader.setConfig({
		enabled:true
	});  //开启动态加载
	Ext.application({
		name : 'epl',
		appFolder : "app", //指定根目录
		launch:function(){
	        Ext.create('Ext.container.Viewport', {
			    layout: 'fit', 
			    hideBorders: true, 
	            items: {
	            	xtype: 'mainlayout'
	            }
	        });
		},
		controllers:[
			'MenuController',
			'XDBLController'
		]
	});
})

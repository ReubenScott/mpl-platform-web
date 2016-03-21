Ext.define('epl.view.Viewport',{ 
    extend: 'Ext.Viewport', 
    layout: 'fit', 
    hideBorders: true, 
    requires : [ 
        'epl.view.Header', 
        'epl.view.MenuTree', 
        'epl.view.TabPanel', 
        'epl.view.South' 
    ], 
    initComponent : function(){ 
        var me = this; 
        Ext.apply(me, { 						//这里讲items加入当前类（即Viewport），其实直接配置Viewport的items效果一样
            items: [{ 
                id:'desk', 
                layout: 'border', 				//这个是个重点，该布局实现“东西南北中”布局，具体细节可自己查找，网上一大堆
                items: [ 
                    Ext.create('epl.view.Header'), 		//创建上侧头
                    Ext.create('epl.view.MenuTree'), 		//创建左侧菜单
                    Ext.create('epl.view.TabPanel'), 	//创建中间panel选项卡
                    Ext.create('epl.view.South') 		//创建下侧栏
                ] 
            }] 
        }); 
        me.callParent(arguments); 
    } 
});

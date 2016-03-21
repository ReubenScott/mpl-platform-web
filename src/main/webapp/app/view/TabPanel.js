Ext.define('epl.view.TabPanel',{ 
    extend: 'Ext.tab.Panel', 
    alias: 'widget.center',
    initComponent : function(){ 
        Ext.apply(this,{ 
            id: 'content-panel', 
            region: 'center',  
            defaults: { 
               autoScroll:true, 
               bodyPadding: 10 
            }, 
            activeTab: 0, 
            border: false, 
	        items: [{ 
              id: 'HomePage', 
              title: '首页', 
              iconCls:'home',
              layout: {
			    type: 'hbox',
			    pack: 'start',
			    align: 'stretch'
			  },
			  items: [
			      {
                    xtype: 'panel',
                    flex: 5,
                    border: 0,
                    width: 604,
                    layout: {
                        align: 'stretch',
                        type: 'vbox'
                    },
                    header: false,
                    title: 'p1',
                    items: [
                        {
                            xtype: 'panel',
                            flex: 1,
                            layout:{
                                type: 'absolute'
                            },
                            title: '个人信息'
                        },
                        {
                            xtype: 'panel',
                            flex: 1,
                            margin: '10 0 0 0 ',
							layout: {
		                        align: 'stretch',
		                        type: 'hbox'
		                    },
		                    header: false,
		                    border: 0,
                            title: '分组题目信息',
                            items:[
	                            {
	                            	xtype: 'panel',
		                            flex: 1,
		                            margin: '0 0 0 0 ',
		                            title: '分组题目信息'
	                            },
	                            {
	                            	xtype: 'panel',
		                            flex: 1,
		                            margin: '0 0 0 10 ',
		                            title: '值日表'
	                            }
                            ]
                        },
                        {
                            xtype: 'panel',
                            flex: 1,
                            margin: '10 0 0 0 ',

                            title: '任务提示'
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    flex: 2,
                    margin: '0 0 0 10',
                    header: false,
                    border: 0,
                    layout: {
                        align: 'stretch',
                        type: 'vbox'
                    },
                    title: '公告列表',
                    items:[
	                    {
	                    	xtype: 'panel',
		                    flex: 1,
		                    margin: '0 0 0 0',
		                    title: '最新公告'
	                    },
	                    {
	                    	xtype: 'panel',
		                    flex: 1,
		                    margin: '10 0 0 0',
		                    title: '资料下载'
	                    }
                    ]
                }
			  ]
            }] 
        }); 
        this.callParent(arguments); 
    } 
}); 

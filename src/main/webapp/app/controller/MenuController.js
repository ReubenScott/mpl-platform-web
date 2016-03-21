Ext.define('epl.controller.MenuController',{ 
    extend: 'Ext.app.Controller', 
    models: ['MenuModel'],  
   	stores: ['MenuStore'],  
    views: ['MainLayout'],  

    requires : [ 
        'epl.xdbl.xdbl' 
    ], 

    init: function () {  
    //初始化部分，下面是部分是给菜单绑定单击事件，接下来会用，这里先注释  
        this.control({  
          'menuTree': {  
              itemclick: this.loadMenu
          },
            //设定列表添加按钮的事件
          'north button[id=head-lb-3]':{
              click: this.addTab
            }

        });
    },
      loadMenu:function(selModel, record){ 
        if (record.get('leaf')) {
        	if(record.get('optype')=='window'){
        		var win= Ext.getCmp(record.get('url'));
        		if(!win){
        			win=Ext.widget(record.get('url'))
        		}
        		win.show();
        	}
        	else{
        		var panel = Ext.getCmp(record.get('mid'));
	            if(!panel){  
	                panel ={
	                	id:record.get('url'),
	                    title: record.get('text'), 
	                    xtype:record.get('url'),
	                    closable: true  
	                };
                alert(1);
	                this.openTab(panel,record.get('url')); 
                alert(2);
	            }else{ 
	                var main = Ext.getCmp("content-panel"); 
	                main.setActiveTab(panel);  
	            } 
        	}
            
        }  },

    addTab : function(button){
        var mainTabPanel = Ext.getCmp('content-panel') ;
        var items = mainTabPanel.items.items;
        for(var item in items){
           // console.log(items[item].id);
           if(items[item].id == "xdbl-tabpan"){
             return ;
           }
        }

        var tab=mainTabPanel.add({
            title: button.text ,
            closable:true,  //允许关闭，同时新建关闭按钮
            // id:"xdbl-tabpan", 
            xtype:'xdbl-xdbl',

        });
        mainTabPanel.setActiveTab(tab);        
    },



     openTab : function (panel,id){
        var o = (typeof panel == "string" ? panel : id || panel.id); 
        var main = Ext.getCmp("content-panel"); 
        var tab = main.getComponent(o);      
        if (tab) { 
            main.setActiveTab(tab);  
        } else if(typeof panel!="string"){  
            panel.id = o;  
                alert(main);
            var p = main.insert(0,panel);  
                alert(3);
            main.setActiveTab(p);  
        }  
                alert(4); 

    } 

});
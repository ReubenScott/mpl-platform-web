Ext.define('epl.view.South',{ 
    extend: 'Ext.Toolbar', 
    alias: 'widget.south',
    initComponent : function(){ 
        Ext.apply(this,{ 
            id:"bottom", 
            region:"south", 
            height:23, 
            items:['->',"版权所有     Copyright © 2013 计算机学院",'->'] 
        }); 
        this.callParent(arguments); 
    } 
}); 
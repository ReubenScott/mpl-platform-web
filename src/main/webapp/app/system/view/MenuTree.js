Ext.define('epl.system.view.MenuTree',{ 
    extend: 'Ext.tree.Panel', 
    alias: 'widget.menuTree',
    requires:['epl.system.store.MenuStore'], 
    
    initComponent : function(){ 
        Ext.apply(this,{ 
            id: 'menu-panel', 
            title: '系统导航', 
            iconCls:'icon-menu', 
            margins : '0 0 -1 1', 
            region:'west', 
            border : false, 
            enableDD : false, 
            split: true, 
            width : 212, 
            minSize : 130, 
            maxSize : 300, 
            rootVisible: false, 
            containerScroll : true, 
            collapsible : true, 
            autoScroll: false,
            store:Ext.create('epl.system.store.MenuStore')
        }); 
        this.callParent(arguments); 
    } 
}); 

Ext.define('epl.store.MenuStore',{
    extend: 'Ext.data.TreeStore',
    requires: 'epl.model.MenuModel',
 	model: 'epl.model.MenuModel',
 	autoLoad: true,
 	proxy: {
        type: 'ajax',
        url: 'data/manager.json',
        reader: {
            type: 'json',
            successProperty: 'success'
        }
    } 
});

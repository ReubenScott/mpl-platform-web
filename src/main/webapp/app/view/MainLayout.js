Ext.define("epl.view.MainLayout",{
	extend:'Ext.panel.Panel',
	alias:'widget.mainlayout',
    layout:'border',
	defaults:{
		split:true,
        autoScroll:true, 
	},
    requires : [ 
        'epl.view.Header', 
        'epl.system.view.MenuTree', 
        'epl.view.TabPanel', 
        'epl.view.South' 
    ], 
    items:[{xtype:'north'},{xtype:'center'},{xtype:'menuTree'},{xtype:'south'}],
    initComponent: function() {
//        var soup = {
//            contents: [],
//            add: function(ingredient) {
//                alert(2);
//                this.contents.push(ingredient);
//            }
//        };
//        Ext.Function.interceptAfter(soup, "add", function(ingredient){
//            // Always add a bit of extra salt
//            alert(ingredient);
//            this.contents.push("salt");
//        });
//        soup.add("water");
//        soup.add("onions");
//       alert( soup.contents)
        this.callParent();
    }
});

Ext.define('epl.system.view.MenuNodeTree',{     extend: 'Ext.tree.Panel',     alias: 'widget.system.menuNodeTree',    requires:['epl.system.store.MenuStore'],         initComponent : function(){         Ext.apply(this,{             id: 'menu-nodePanel',             title: '菜单管理',             iconCls:'icon-menu',             region:'west',             enableDD : false,             split: true, //            minSize : 130, //            maxSize : 300,             rootVisible: false, //            containerScroll : true,             autoScroll:true,            store:Ext.create('epl.system.store.MenuStore'),            dockedItems:[{              xtype:'toolbar',              dock: 'bottom',              items:["->",{                xtype: 'tbtext',text: '使用右键菜单新增或删除菜单'              }]            }]        });         this.callParent(arguments);     } }); 
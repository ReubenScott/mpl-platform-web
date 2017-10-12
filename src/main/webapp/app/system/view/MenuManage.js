/**
 * ClassName 菜单管理
 */
Ext.define("epl.system.view.MenuManage",{
	extend:'Ext.panel.Panel',
	alias : 'widget.system.MenuManage',  
   // store : 'epl.system.store.MenuStore',
	
	defaults:{
		split:true,
		bodyStyle:'padding:1px'
	},
	
//	layout:'border',
//	layout:'anchor',

  layout : {
    type: 'hbox',
    align:'stretch'
  },
  
//  tbar:[
//    {xtype:'button',text:'添加',id:'add',iconCls:'button_add'},
//    {xtype:'button',text:'删除',id:'delete',iconCls:'button_remove'},
//    {xtype:'button',text:'保存',id:'save',iconCls:'button_save'}
//  ],
  
    items:[
      {
        xtype: 'system.menuNodeTree',
        flex: 0.35
//            anchor: 1
//            margin: '0 0 0 0',         
//            anchor:'20% 100%',
      },
      {
        id: 'menu-form', 
        xtype: 'form',
        flex: 0.65,
//            margin: '0 0 0 0',      
//            anchor:'80% 100%',
//            labelWidth: 30,   
        defaultType: "textfield",    

        autoScroll:true,
        defaults: {
            anchor: '92%',
            margin:'15 0 0 0',
            labelAlign:'right'
        },
        title: '编辑内容',
        items:    
        [   
            {    
                fieldLabel:"菜单ID",   
                name:'menu.sid',   
                allowBlank:false    
            }, 
            {    
              fieldLabel:'菜单名称',   
              name:'menu.menuName',   
              allowBlank:false    
            }, 
            {    
                fieldLabel:'后台地址',   
                name:'menu.url'    
            },   
            {    
                fieldLabel: '展示模版',   
                name: 'xtype'
            },   
            {    
              fieldLabel: '生日',   
              xtype:'datefield',   
              name: 'birthday',    
              width:127    
            }      
            
            
            
        ],   
        // 重置 和 保存 按钮.
        buttons: [{
            text: '重置',
            handler: function() {
                this.up('form').getForm().reset();
            }
        }, {
            text: '保存',
            formBind: true, //only enabled once the form is valid
            disabled: true,
            handler: function() {
                var form = this.up('form').getForm();
                if (form.isValid()) {
                    form.submit({
                        success: function(form, action) {
                           Ext.Msg.alert('保存成功', action.result.msg);
                        },
                        failure: function(form, action) {
                            Ext.Msg.alert('操作失败', action.result.msg);
                        }
                    });
                }
            }
          },{
            text: '删除', action: "delete" 
          }
        ]
      }
    ]
	
});
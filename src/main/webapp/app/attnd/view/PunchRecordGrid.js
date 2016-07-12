/**
 * ClassName 部门管理数据列表视图
 */
Ext.define('epl.attnd.view.PunchRecordGrid', {
  extend : 'Ext.grid.Panel',
  alias : 'widget.PunchRecordGrid',
  store : 'epl.attnd.store.PunchRecordStore',
  
  selModel:{
    selType:'checkboxmodel'
  },
  multiSelect: true,

  border:0,
  frame:true,

  tbar:[
    {xtype:'button',text:'添加',id:'add',iconCls:'table_add'},
    {xtype:'button',text:'删除',id:'delete',iconCls:'table_remove'},
    {xtype:'button',text:'保存',id:'save',iconCls:'table_save'}
  ],

  dockedItems:[{
    xtype:'pagingtoolbar',
    store:'epl.attnd.store.PunchRecordStore',
    dock:'bottom',
    displayInfo:true
  }],


  enableKeyNav:true,
  columnLines: true,


  initComponent : function() {
    this.columns = [ 
      {
        header : '序号',
        dataIndex : 'seqno',
        flex : 1
      },
      {
        header : 'cardID',
        dataIndex : 'cardID',
        flex : 1
      },
      {
        header : 'EMPNO',
        dataIndex : 'empno',
        flex : 1
      },
      {
        header : 'EMPNAME',
        dataIndex : 'empname',
        flex : 1
      },
      {
        header : 'DEPTNAME',
        dataIndex : 'deptname',
        field:{
          xtype:'textfield',
          allowBlank:false
        } ,
        flex : 1
      },
      {
        header : 'PUNCHTIME',
        dataIndex : 'punchTime',
        flex : 1
      } 
    ];


    this.editing = Ext.create("Ext.grid.plugin.CellEditing");
    this.plugins = [this.editing];

    this.callParent(arguments);
  }


});


    




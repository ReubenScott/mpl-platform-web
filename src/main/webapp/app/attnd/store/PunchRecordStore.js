Ext.define('epl.attnd.store.PunchRecordStore', {
  extend: 'Ext.data.Store', 
  model: 'epl.attnd.model.PunchRecord',

//  data: [ 
//    {name: 'Ed',    email: 'ed@se3333ncha.com'}, 
//    {name: 'Tommy', email: 'tommy@sencha.com'},
//    {name: 'Tommy111', email: 'tomm22y@sencha.com'}
//  ]

//  pageSize : 5,

  api:{
    search:'attendance/AtndMeasure/punchRecord.htm',
    update:'/extjs/extjs!updateDeptList.action',
    remove:'/extjs/extjs!updateDeptList.action'
  },

  proxy:{
    type: 'ajax',
  //  url : 'attendance/AtndMeasure/punchRecord.htm',
    reader:{
      type : 'json',
      root: 'topics'
    },
    writer:{
      type : 'json'
    }
  },

  
});


Ext.define('epl.system.model.MenuModel', { 
	extend: 'Ext.data.Model', 
// fields: ['mid', 'text','cls','stores','columns','url','expanded','optype']
    fields: [
              {name: 'sid',  type: 'string' },
              {name: 'text', type: 'string'  , mapping: 'menuName'},
              {name: 'pid',  type: 'string' },
              {name: 'leaf',  type: 'boolean'},    
              'menuType',
              'orderNum',
              'url',
              {name: 'xtype', type: 'string' , mapping: 'xtype' },
              'expanded',
              'iconUrl',
              'isDelete',
              {name: 'menuLevel', type: 'int'},
              'menuEvent'
              
              
            ],  
    hasMany:{   // 一对多和多对一
	    model: 'epl.system.model.MenuModel',  
	    name : 'children',  
	    filterProperty: 'parent_id'  //关联字段  
	}  
});



// {"iconUrl":"","isDelete":false,"menuEvent":"","menuLevel":0,"menuName":"系统管理","menuType":"1","orderNum":"5","parentId":"","uid":"00001","url":""},

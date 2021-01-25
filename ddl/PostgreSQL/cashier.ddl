
CREATE TABLE sys_menu (
  sid VARCHAR(255)  NOT NULL default '',
  menuname VARCHAR(255)  default NULL,
  menu_type VARCHAR(255)  default NULL,
  pid VARCHAR(255)  default NULL,
  menu_level INTEGER default NULL,
  url VARCHAR(255)  default NULL,
  menu_event VARCHAR(255)  default NULL,
  xtype VARCHAR(80)  default NULL,
  order_num INTEGER default NULL,
  icon_url VARCHAR(255)  default NULL,
  helphref VARCHAR(255)  default NULL,
  del_flag CHAR(1) default NULL,
  PRIMARY KEY  (sid)
) ;


-- 收银员登录信息   Cash register  
CREATE TABLE cashier_info (            
  cashier_no     VARCHAR(10)  NOT NULL ,   --   '员工号'      
  cashier_name   VARCHAR(20)  NOT NULL ,   --   '收银员姓名'      
  cashier_pwd    VARCHAR(32)  NOT NULL ,   --    登录密码
  remark         VARCHAR(100)  ,   --   '备注'     
  PRIMARY KEY(cashier_no)
) ;


-- 货号 条码 名称 规格 类别 库存 售出 单位 会员价	零售价 进价 打折	积分 更新 备注
-- ordinal, goods_barcode, goods_name, unit, specs, sale_price, member_price, discount, sold_price, volume, amount
-- 商品信息表  
CREATE TABLE goods (
  barcode VARCHAR(20) NOT NULL,     -- 商品条码
  name VARCHAR(80) NOT NULL,        -- 商品名称
  specs varchar(20),                -- 规格型号
  unit varchar(20),                 -- 单位 
  category_id varchar(20),          -- 商品类别
  cargo_no VARCHAR(20) NOT NULL,    -- 货号 
  purchase_price NUMERIC(16,6),     -- 进价
  sale_price NUMERIC(16,2),         -- 零售价
  member_price NUMERIC(16,2),       -- 会员价   
  storage_time TIMESTAMP WITHOUT TIME ZONE,    -- 首次入库时间
  update_time TIMESTAMP  WITHOUT TIME ZONE,            -- 更新时间
  branch_no varchar(20) ,           -- 机构号  暂时不用
  remarks VARCHAR(80),              -- 备注
  PRIMARY KEY (barcode)
);

--  商品种类 树形结构   
CREATE TABLE goods_category (
   category_id    VARCHAR(20) NOT NULL,  -- 商品级别id
   category_name  VARCHAR(20) NOT NULL,  -- 商品级别名称
   category_pid   VARCHAR(20) NOT NULL,  -- 父商品级别
   PRIMARY KEY (category_id)   
);


-- 会员信息  
CREATE TABLE member_info (
  member_no        VARCHAR(10)   ,   --   '会员号'      
  member_name      VARCHAR(20)   ,   --   '会员姓名'      
  member_level     VARCHAR(32)   ,   --   会员级别
  membership_card  VARCHAR(32)   ,   --   会员卡号
  telephone        VARCHAR(16)   ,   --   联系电话
  remark           VARCHAR(100)  ,   --   '备注'
  PRIMARY KEY (member_no)
) ;


-- 会员权益 商品级别
CREATE TABLE member_benefits_goods (
  member_level     VARCHAR(32)  NOT NULL ,    --   会员级别
  barcode          VARCHAR(20)  NOT NULL ,    --   商品条码 
  member_price     NUMERIC(16,2) NOT NULL ,   --   会员价    
  start_time       DATE ,  --   开始时间
  end_time         DATE ,  --   结束时间
  remark           VARCHAR(100)  ,            --   备注
  PRIMARY KEY (member_level, barcode)
) ;


--  数据字典  
CREATE TABLE dict_promotion_mode (
  promotion_mode   VARCHAR(32)  NOT NULL ,    --   促销方式： 原价打折; 商品特价 ; 捆绑销售套餐 ; 赠品销售 ; 现金返还 ; 集点购买 ; 凭证优惠
  priority         INTEGER  NOT NULL ,        --   优先级 数字越小，越优先 1,2,3,4,5,6,7,8 
  remark           VARCHAR(100)  ,            --   备注
  PRIMARY KEY (promotion_mode)
) ;


-- 促销活动 
CREATE TABLE  promotion_campaign (
  campaign_no SERIAL  ,        -- 活动编号  主键自增 Integer
  campaign_name VARCHAR(80),   -- 活动名称
  -- 促销对象
  start_time TIMESTAMP WITHOUT TIME ZONE,        --  促销开始时间
  end_time TIMESTAMP WITHOUT TIME ZONE,          --  促销结束时间
  useful CHAR(1)   ,           --  是否有效 'T'  'F'  ('T': true取值1 ， 'F': false取值0 )
  remark VARCHAR(80),  
  PRIMARY KEY (campaign_no)
);


-- 促销活动  商品特价规则   
CREATE TABLE promotion_campaign_rules (
  campaign_no INTEGER NOT NULL,         --  活动编号   来源于 promotion_campaign -> campaign_no
  ordinal INTEGER  NOT NULL,            --  序号   如：content 字段 一次装不下优惠内容  分多次累加 
  promotion_mode  VARCHAR(32)  NOT NULL ,  --  促销方式： 原价打折; 商品特价 ; 捆绑销售套餐 ; 赠品销售 ; 现金返还 ; 集点购买 ; 凭证优惠
  content text ,      --   优惠内容    尽量不要用条码 ， 能上升到特定 货品 更好 ；  (同一商品 口味不同 条码不一样)
     -- 如  套餐公式：条码1 + 条码2 + 条码3 = 4000.0 ;  
     --     促销价格  条码1 = 100  ;  条码2 = 110 ；    -- promotion_price NUMERIC(16,6) ,       --  促销价格
     --     折扣率%   条码1 = 0.95 ； 条码2 = 0.66 ； 95折 66折    -- discount  NUMERIC(16,2) ,      --  折扣率%    
     --     现金返还  条码1 + 条码2 + 条码3  = 600 - 50 ;  500 - 20    特定几件满600减50  | 全场 500减20
  remark VARCHAR(80),  
  PRIMARY KEY (campaign_no, ordinal)
) ;



-- 会员权益 种类级别  （暂时不用 废弃）  原价打折
CREATE TABLE member_benefits_category (
  member_level     VARCHAR(32)  NOT NULL ,    --  会员级别
  goods_category   VARCHAR(20)  NOT NULL ,    --  货物类别   低级类别
  member_discount  NUMERIC(16,2) NOT NULL ,   --  会员折扣率%    
  start_time       TIMESTAMP,                 --  开始时间
  end_time         TIMESTAMP,                 --  结束时间
  remark           VARCHAR(100)  ,            --  备注
  PRIMARY KEY (member_level, goods_category)
) ;


-- 订单  合计账 
CREATE TABLE order_form (
  paycode   VARCHAR(20)  NOT NULL,          --  订单号   
  guide_no  VARCHAR(20)  ,                  --  促销员   预留
  member_no   VARCHAR(10)   ,               --  会员号    
  terminal_no VARCHAR(20)  NOT NULL,        --  终端号   
  cashier_no VARCHAR(20)  NOT NULL,         --  收银员  
  total_pieces NUMERIC(16,6) NOT NULL,      --  总件数
  total_quantity NUMERIC(16,6) NOT NULL,    --  总数量
  total_amount NUMERIC(16,6) NOT NULL,      --  总金額  
  payment_mode CHAR(10) NOT NULL,           --  支付方式  
  traded_mark CHAR(1) NOT NULL,             --  交易成功标识
  trade_time TIMESTAMP,                     --  交易时间
  remark VARCHAR(80),  
  PRIMARY KEY (paycode)
);

-- 货号 条码 名称 规格 类别 库存 售出 单位 会员价  零售价 进价 打折   积分 更新 备注
-- ordinal, goods_barcode, goods_name, unit, specs, sale_price, member_price, discount, sold_price, volume, amount
-- 商品信息表  
-- 订单  明细账
CREATE TABLE order_detail (
    paycode VARCHAR(20)  NOT NULL,         -- 订单号
    barcode VARCHAR(20) NOT NULL,          -- 商品条码
    goods_name VARCHAR(80) NOT NULL,       -- 商品名称
    sale_price NUMERIC(16,6),              -- 零售价
    sold_price NUMERIC(16,6),              -- 实售价
    discount NUMERIC(6,2),                 -- 折扣率%
    quantity NUMERIC(16,6) NOT NULL,       -- 数量
    amount NUMERIC(16,6) NOT NULL,         -- 金額   
    record_time TIMESTAMP,                 -- 写入数据库时间
    remarks VARCHAR(80),                   -- 备注
    PRIMARY KEY (paycode, barcode)
);


-- 支付信息
CREATE TABLE pay_trans_info (
  paycode   VARCHAR(20)  NOT NULL,          --  订单号   
  amount    NUMERIC(16,6) NOT NULL,         --  金額  
  ordinal   INTEGER  NOT NULL,              --  序号      可能分多次支付
  payment_mode CHAR(10) NOT NULL,           --  支付方式  
  pay_amount NUMERIC(16,6) NOT NULL,        --  支付金额 
  traded_mark CHAR(1) NOT NULL,             --  本次支付成功标识
  trade_time TIMESTAMP,                     --  交易时间
  trans_number VARCHAR(20) ,                --  交易流水号 （渠道返回）
  remark VARCHAR(80),  
  PRIMARY KEY (paycode)
);





-- 商品信息表 （作废）
CREATE TABLE item
(
    item_no VARCHAR(40) NOT NULL,
    item_code VARCHAR(60),
    item_subno VARCHAR(40) NOT NULL,
    item_name VARCHAR(80) NOT NULL,
    item_subname VARCHAR(40),
    ALIAS VARCHAR(40),
    classid INTEGER NOT NULL,
    brand_no VARCHAR(40),
    proclass INTEGER,
    unit VARCHAR(10),
    item_size VARCHAR(40),
    product_area VARCHAR(40),
    sup_no VARCHAR(40),
    in_price NUMERIC(16,6) DEFAULT 0 NOT NULL,
    wholesale_price NUMERIC(16,6) DEFAULT 0 NOT NULL,
    sale_price NUMERIC(16,6) DEFAULT 0 NOT NULL,
    vip_price NUMERIC(16,6) DEFAULT 0 NOT NULL,
    in_unit VARCHAR(10),
    in_factor NUMERIC(16,6) DEFAULT 1,
    in_size VARCHAR(40),
    buy_tax NUMERIC(16,6) DEFAULT 0.17,
    sale_tax NUMERIC(16,6) DEFAULT 0.17,
    shelf_day INTEGER,
    sale_type VARCHAR(10) DEFAULT 'GX'::VARCHAR,
    bind_flag CHAR(1) DEFAULT '0'::bpchar,
    son_no VARCHAR(40),
    son_qty NUMERIC(16,6),
    allowdiscount CHAR(1) DEFAULT '1'::bpchar,
    allowbranchbuy CHAR(1) DEFAULT '1'::bpchar,
    allowbranchpx CHAR(1) DEFAULT '1'::bpchar,
    status CHAR(1) DEFAULT '1'::bpchar,
    num1 NUMERIC(14,4) DEFAULT 0,
    num2 NUMERIC(14,4) DEFAULT 0,
    num3 NUMERIC(14,4) DEFAULT 0,
    num4 NUMERIC(14,4) DEFAULT 0,
    num5 NUMERIC(14,4) DEFAULT 0,
    remark1 VARCHAR(50),
    remark2 VARCHAR(100),
    remark3 VARCHAR(255),
    createid INTEGER,
    createdate TIMESTAMP(6) WITHOUT TIME ZONE,
    userid INTEGER,
    operdate TIMESTAMP(6) WITHOUT TIME ZONE,
    has_sku CHAR(1) DEFAULT '0'::bpchar,
    valuation_flag CHAR(1) DEFAULT '0'::bpchar,
    reward_way CHAR(1) DEFAULT '0'::bpchar,
    reward_num NUMERIC(16,6) DEFAULT 0,
    purcyc INTEGER DEFAULT 15,
    arrcyc INTEGER DEFAULT 3,
    minpurqty INTEGER DEFAULT 0,
    stock_flag CHAR(1) DEFAULT '1'::bpchar,
    org_sub_rate NUMERIC(16,6) DEFAULT 0,
    spe_sub_rate NUMERIC(16,6) DEFAULT 0,
    dx_sub_rate NUMERIC(16,6) DEFAULT 0,
    PRIMARY KEY (item_no)
);




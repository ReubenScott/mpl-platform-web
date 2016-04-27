-- USE covidiendb ;

/*
    部门信息表
*/
CREATE TABLE F_dept_info (
    uid             CHAR(36)     ,
    deptNO          VARCHAR(20)  COMMENT '部门编号'     ,
    DEPTNAME        VARCHAR(20)  COMMENT '部门名称'               ,
    PRIMARY KEY (uid ) 
 ) ENGINE=INNODB DEFAULT CHARSET=UTF8 COMMENT '部门信息' ;    

 
/***
 * 员工信息  
 */
CREATE TABLE F_emp_info (
 --   uid            CHAR(36)     ,
    empNO          VARCHAR(20)  COMMENT '员工编号'     ,
    empNAME        VARCHAR(20)  COMMENT '员工姓名'               ,
    PRIMARY KEY (uid) 
 ) ENGINE=INNODB DEFAULT CHARSET=UTF8 COMMENT '员工信息' ;  
 
CREATE TABLE F_dept_emp_info (
    uid             CHAR(36)     ,
    deptNO          VARCHAR(20)  COMMENT '部门编号'     ,
    DEPTNAME        VARCHAR(20)  COMMENT '部门名称'               ,
    PRIMARY KEY (uid ) 
 ) ENGINE=INNODB DEFAULT CHARSET=UTF8 COMMENT '部门信息' ;    
 

/***
 * 排班类型   有跨天情况
 */
CREATE TABLE dim_ScheduleType (
   scheduleid VARCHAR(2) NOT NULL ,
   ScheduleName VARCHAR(32),
   STARTTIME TIME DEFAULT NULL COMMENT '开始时间',
   HOURS FLOAT(4,2) DEFAULT NULL COMMENT '小时数',
   remark VARCHAR(500) DEFAULT NULL,
   SRC_DT VARCHAR(10) DEFAULT NULL,
   ETL_DT VARCHAR(10) DEFAULT NULL,
   PRIMARY KEY  (scheduleid)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8 ;
  
INSERT INTO  dim_scheduletype VALUES ('1','白班','7:30' , 8 ,'','','') ;
INSERT INTO  dim_scheduletype VALUES ('2','夜班1','17:00' , 8 ,'','','') ;
INSERT INTO  dim_scheduletype VALUES ('3','夜班2','17:30' , 8 ,'','','') ;


/*  ----------------------  start   ----------------------------- */
/*
    考勤打卡记录 数据导入表
*/
CREATE TABLE IF NOT EXISTS ATND_PUNCH_RECORD (
    SEQNO               VARCHAR(36)            ,  
    card_id             VARCHAR(20)                     ,    
    empno               VARCHAR(20)                     ,
    empname             VARCHAR(20)                     ,
    deptname            VARCHAR(20)                     ,
    recordTime          DATETIME                     ,
    location            VARCHAR(30)            ,
    STATUS              TINYINT(1)                     ,
    remark              VARCHAR(20)                     ,
    PRIMARY KEY (SEQNO) 
) ENGINE=INNODB DEFAULT CHARSET=UTF8  COMMENT '导入打卡记录数据' ; 


/*
  create 考勤打卡记录  业务表  table
*/
CREATE TABLE IF NOT EXISTS F_ATND_PUNCH_record (
    uid                 VARCHAR(32)            ,  
    SEQNO               VARCHAR(36)            ,  
    card_ID             VARCHAR(20)                     ,    
    empno               VARCHAR(20)                     ,
    empname             VARCHAR(30)                     ,
    deptname            VARCHAR(10)                     ,
    recordTime          DATETIME                     ,
    location            VARCHAR(30)            ,
    state               TINYINT(1)      COMMENT '0:通过,1:不通过;'   , 
    remark              VARCHAR(20)                     ,
    SRC_DT              VARCHAR(10)                     ,
    ETL_DT              VARCHAR(10)                     ,
    PRIMARY KEY (uid) 
 ) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '考勤打卡记录' ;    
                                                
/*
   加班单
*/
create table atnd_overtime_record (
    uid              VARCHAR(36)            ,   
    EMPNO            VARCHAR(20)  COMMENT '员工号'     ,
    EMPNAME          VARCHAR(20)  COMMENT '员工姓名'   ,
    DEPTNAME         VARCHAR(20)  COMMENT '所属部门'   ,
    ScheduleType     VARCHAR(6)   COMMENT '排班类型 '  ,    
    STARTTIME        DATETIME     COMMENT '开始时间'  ,
    ENDTIME          DATETIME     COMMENT '结束时间'  ,
    TOTAL_HOURS      FLOAT(4,2)   COMMENT '小时数'    ,
    remark           VARCHAR(100)                     ,
    SRC_DT           DATE                     ,
    ETL_DT           DATE                    ,
    PRIMARY KEY (uid) 
 ) ENGINE=INNODB  DEFAULT CHARSET=UTF8 COMMENT '加班单'  ;    


/*
    请假单  
*/
 create table atnd_offwork_record (
    uid              VARCHAR(36)            ,   
    EMPNO            VARCHAR(20)  COMMENT '员工号'     ,
    EMPNAME          VARCHAR(20)  COMMENT '员工姓名'   ,
    DEPTNAME         VARCHAR(20)  COMMENT '所属部门'               , 
    vacationType     VARCHAR(6)   COMMENT '休假类型 '  ,  
    STARTTIME        DATETIME     COMMENT '开始时间'        ,
    ENDTIME          DATETIME     COMMENT '结束时间'  ,
    TOTAL_HOURS      FLOAT(4,2)   COMMENT '小时数'           ,
    remark           VARCHAR(100)           ,
    SRC_DT           DATE                   ,
    ETL_DT           DATE                   ,
    PRIMARY KEY (uid) 
 ) ENGINE=INNODB  DEFAULT CHARSET=UTF8 COMMENT '请假单'  ;   
 
/*
   出差单
*/
 create table atnd_businesstrip_record (
    uid              VARCHAR(36)            ,   
    EMPNO            VARCHAR(20)  COMMENT '员工号'     ,
    EMPNAME          VARCHAR(20)  COMMENT '员工姓名'   ,
    DEPTNAME         VARCHAR(20)  COMMENT '所属部门'               , 
    STARTTIME        DATETIME     COMMENT '开始时间'        ,
    ENDTIME          DATETIME     COMMENT '结束时间'  ,
    TOTAL_HOURS      FLOAT(4,2)   COMMENT '小时数'           ,
    remark           VARCHAR(100)           ,
    SRC_DT           DATE                   ,
    ETL_DT           DATE                   ,
    PRIMARY KEY (uid) 
 ) ENGINE=INNODB  DEFAULT CHARSET=UTF8 COMMENT '出差单'  ;    
 
 
 

 
-- 			 				标准出勤		出勤差		3月存休
/*
  create 考勤汇总表  table  Attendance summary sheet
*/
CREATE TABLE S_ATND_SUMMARY_SHEET (
    STATDATE            DATE         COMMENT '日期'       ,
    EMPNO               VARCHAR(20)  COMMENT '员工号'     ,
    EMPNAME             VARCHAR(20)  COMMENT '员工姓名'   ,
    DEPTNAME            VARCHAR(20)  COMMENT '所属部门'               ,
    ATNDSTATUS          VARCHAR(2)   COMMENT '考勤状态  1 正常  ；  2 迟到  ；  3 未打卡  '  ,
    PUNCH_IN_TIME       TIME               COMMENT '上班时间'         ,
    PUNCH_OUT_TIME      TIME               COMMENT '下班时间'         ,
    BUSINESS_TRIPHOURS   FLOAT(4,2)         COMMENT '出差时间'             ,   
    ORDINARY_OVERTIME   FLOAT(4,2)         COMMENT '平时加班'         ,   
    WEEKEND_OVERTIME    FLOAT(4,2)         COMMENT '周末加班'         ,
    HOLIDAY_OVERTIME    FLOAT(4,2)         COMMENT '法定加班'         ,   
    ABSENCE_HOURS       FLOAT(4,2)         COMMENT '请假时数'         ,
    TOTAL_HOURS_WORKED  FLOAT(4,2)         COMMENT '总出勤'           ,
    REMARK              VARCHAR(20)        COMMENT '备注'       ,
    LASTMTH_ACCUM_VACATION   FLOAT(4,2)    COMMENT '	上月存休'   ,  
    CURRENT_MTH_ACCUM_VACATION FLOAT(4,2)  COMMENT '	本月存休'   ,
    PRIMARY KEY (STATDATE , EMPNO ) 
 ) ENGINE=INNODB DEFAULT CHARSET=UTF8  ;    
 

 

-- 1. 员工信息 校验  维护 
SELECT * FROM f_emp_info WHERE empno IS NULL

 
-- 2. 更新 打卡记录里 员工号为空 的情况
UPDATE f_atnd_punch_record AS t1
   LEFT JOIN f_emp_info  T2
    ON t1.empname = T2.empname
SET t1.empno = T2.empno    
  WHERE t1.empno IS  NULL 
  
  
-- 更新 员工部门号
UPDATE f_emp_info AS t1
   INNER JOIN ( SELECT DISTINCT record.empno AS empno , dept.uid AS uid
       FROM f_atnd_punch_record record
        INNER JOIN f_dept_info dept
          ON record.deptname = dept.deptname     
      ) AS T2 
    ON t1.empno = T2.empno
SET t1.deptid = T2.uid    


-- 更新  补录 加班单、请假单、出差单  员工部门号
UPDATE atnd_manual_record AS t1
   INNER JOIN f_emp_info  T2
    ON t1.empname = T2.empname
SET t1.empno = T2.empno    

-- 手工录入 数据核对
SELECT DISTINCT empno ,empname FROM atnd_manual_record
 WHERE empno IN 
 ( SELECT  empno FROM atnd_manual_record 
   GROUP BY empno
   HAVING COUNT(DISTINCT empname) >1 )
  ORDER BY 1 ASC  
; 

-- 查看是否有重复
SELECT T1.* 
  FROM atnd_manual_record AS T1 
   INNER JOIN 
  ( SELECT empno , LEFT(STARTTIME,10) AS dt , book_type , COUNT(1) 
      FROM atnd_manual_record 
    GROUP BY empno , LEFT(STARTTIME,10) ,book_type
      HAVING COUNT(1) > 1 
   ) AS T2 
     ON T1.empno = T2. empno 
    AND T1.book_type = T2.book_type
    AND LEFT(T1.STARTTIME,10) = T2.dt 
    --  and T1.empno = 'BI00261'
   ORDER BY T1.empno , t1.starttime 
;



  
-- 上班 迟到 
SELECT EMPNAME 
      , LEFT(recordTime,10)   AS 日期
      , MIN(SUBSTR(recordTime,12))  AS 上班时间
      , MAX(SUBSTR(recordTime,12))  AS 下班时间
      , TIMESTAMPDIFF(MINUTE  , MIN(recordTime) , MAX(recordTime))/60  AS 小时数
      , COUNT(1) AS 打卡次数
    FROM atnd_punch_record 
  WHERE LEFT(recordTime,10) BETWEEN '2016-02-01' AND '2016-02-29'   
  GROUP BY EMPNAME , LEFT(recordTime,10)
    HAVING MIN(SUBSTR(recordTime,12)) BETWEEN  '07:31:00' AND '12:00:00'
    -- or Max(SUBSTR(recordTime,12)) < '17:00:00'
   ORDER BY 1 ASC ;




-- 打开记录  存储过程

DELIMITER $$
USE `attendance`$$

DROP PROCEDURE IF EXISTS `sp_f_atnd_punch_record`$$

CREATE  PROCEDURE sp_f_atnd_punch_record(parameter1 INT)
top:BEGIN
  DECLARE flag INT DEFAULT parameter1;  -- 声明变量flag，将参数值赋给该变量
  DECLARE uuidStr VARCHAR(32);    -- 声明一个长度为32位的字符串
  DECLARE currentTime TIMESTAMP;    -- 声明一个类型为时间戳的变量  
  
  #当sqlexception handler捕捉到异常时，设置err=1  
  DECLARE CONTINUE HANDLER FOR SQLEXCEPTION     
  BEGIN
     ROLLBACK;
     SELECT 'err';
     LEAVE top;
  END;	  
  
  #开始事务 
  START TRANSACTION;  
  
  -- 1.删除重复记录
  DELETE T2 FROM f_atnd_punch_record T2 INNER JOIN atnd_punch_record T1 ON T2.SEQNO = T1.SEQNO ;  
    
  
  BEGIN  
    -- 需要定义接收游标数据的变量 
    DECLARE V_SEQNO VARCHAR(20) ;
    DECLARE V_card_ID VARCHAR(20) ;
    DECLARE V_empno VARCHAR(20) ;
    DECLARE V_empname VARCHAR(30) ;
    DECLARE V_deptname VARCHAR(60) ;
    DECLARE V_recordTime DATETIME ;
    DECLARE V_location VARCHAR(60) ;
    DECLARE V_state TINYINT(1) ;
    DECLARE V_remark VARCHAR(60) ;
    -- 游标遍历数据结束标志
    DECLARE done BOOLEAN DEFAULT TRUE;
  
    -- 游标
    DECLARE currecord CURSOR FOR SELECT SEQNO,card_ID,empno,empname,deptname,recordTime,location,state,remark
    FROM atnd_punch_record WHERE (empno IS NOT NULL OR empname IS NOT NULL) ;    
  
    -- 将结束标志绑定到游标
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = FALSE;        
   
    OPEN currecord;
  
    REPEAT      
      FETCH currecord INTO V_SEQNO,V_card_ID,V_empno,V_empname,V_deptname,V_recordTime,V_location,V_state,V_remark;      
      
      IF done THEN
        -- 插入记录
        #uuid()函数得到的字符串是 '6ccd780c-baba-1026-9564-0040f4311e29' ，剔除里面的-，得到一个32位的字符串
        INSERT INTO f_atnd_punch_record   
          (uid,SEQNO,card_ID,empno,empname,deptname,recordTime,location,state,remark)
        VALUE 
          (REPLACE(UUID(),'-',''),V_SEQNO,V_card_ID,V_empno,V_empname,V_deptname,V_recordTime,V_location,V_state,V_remark) ;
      END IF;
  
      -- 声明结束的时候
      UNTIL done = FALSE     
   END REPEAT ;
   
   CLOSE currecord;
  
  END ;
   
   
  COMMIT;
  SELECT 'OK'; -- 将事务的执行状态返回给被调者      
   
END$$

DELIMITER ;

/*  ----------------------  end   ----------------------------- */

DELIMITER //
DROP TRIGGER IF EXISTS named_configuration_insert_trigger //
CREATE TRIGGER named_configuration_insert_trigger AFTER INSERT ON named_configuration
FOR EACH ROW
BEGIN
    INSERT INTO named_configuration_history(his_id , action, type_id, name ,
       device_type_id , version , create_time , description
    ) VALUES (
       NEW.id , 'INSERT' , NEW.type_id , NEW.name , NEW.device_type_id ,
        NEW.version ,  NEW.create_time , NEW.description
    ) ;
END ; //
DELIMITER ;


drop table S_SUMMARY

CREATE TABLE S_SUMMARY (
    EMPNAME             VARCHAR(20)  COMMENT '员工姓名'   ,
    ABSENCE_HOURS       FLOAT(4,2)         COMMENT '请假时数'         ,
    BUSINESS_TRIPHOURS   FLOAT(4,2)         COMMENT '出差时间'             , 
    REMARK              VARCHAR(20)        COMMENT '备注'       ,  
    workdate              VARCHAR(20)        COMMENT '加班日期'       ,  
    ORDINARY_OVERTIME   FLOAT(4,2)         COMMENT '平时加班'         ,  
    HOLIDAY_OVERTIME    FLOAT(4,2)         COMMENT '法定加班'         ,    
    WEEKEND_OVERTIME    FLOAT(4,2)         COMMENT '周末加班'         ,  
    HOURS_WORKED  FLOAT(4,2)         COMMENT '标准出勤'           ,
    TOTAL_HOURS_WORKED  FLOAT(4,2)         COMMENT '总出勤'           ,
    HOURS_cha  FLOAT(4,2)         COMMENT '出勤差'           ,
    LASTMTH_ACCUM_VACATION   FLOAT(4,2)    COMMENT '	上月存休'   ,  
    CURRENT_MTH_ACCUM_VACATION FLOAT(4,2)  COMMENT '	本月存休'   

 ) ENGINE=INNODB DEFAULT CHARSET=UTF8  ;    

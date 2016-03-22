-- USE covidiendb ;

/*  ----------------------  start   ----------------------------- */
/*
    考勤打卡记录 数据导入表
*/
DROP table if Exists attendance_record ;
CREATE TABLE IF NOT EXISTS attendance_record (
    SEQNO               VARCHAR(20)            ,  
    card_ID             VARCHAR(20)                     ,    
    empno               VARCHAR(20)                     ,
    empname             VARCHAR(20)                     ,
    deptname            VARCHAR(20)                     ,
    recordTime          DATETIME                     ,
    location            VARCHAR(10)            ,
    STATUS              TINYINT(1)                     ,
    remark              VARCHAR(20)                     ,
    PRIMARY KEY (SEQNO) 
) ENGINE=INNODB DEFAULT CHARSET=utf8  ;    
ALTER TABLE attendance_record  COMMENT '导入打卡记录数据';

/*
  create 考勤打卡记录  业务表  table
*/
-- DROP table if Exists named_configuration_history ;
CREATE TABLE IF NOT EXISTS f_attendance_record (
    SEQNO               VARCHAR(20)            ,  
    card_ID             VARCHAR(20)                     ,    
    empno               VARCHAR(20)                     ,
    empname             VARCHAR(30)                     ,
    deptname            VARCHAR(10)                     ,
    recordTime          DATETIME                     ,
    location            VARCHAR(10)            ,
    STATUS              TINYINT(1)      COMMENT '0:通过,1:不通过;'   ,
    remark              VARCHAR(20)                     ,
    SRC_DT              VARCHAR(10)                     ,
    ETL_DT              VARCHAR(10)                     ,
    PRIMARY KEY (SEQNO) 
 ) ENGINE=INNODB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8  ;    
ALTER TABLE f_attendance_record COMMENT '考勤打卡记录';         
                                                

-- named_configuration insert trigger
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

/*  ----------------------  end   ----------------------------- */



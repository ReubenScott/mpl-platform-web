package com.kindustry.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.kindustry.system.entity.Menu;

//import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 *
 * Permission 表数据库控制层接口
 *
 */
@MapperScan
public interface PermissionMapper /*extends BaseMapper<Permission>*/ {

	List<Menu> selectMenuByUserId(@Param("userId") Long userId, @Param("pid") Long pid);
	
  List<Menu> selectMenu();
	
	
//
//	List<Permission> selectAllByUserId(@Param("userId") Long userId);

}
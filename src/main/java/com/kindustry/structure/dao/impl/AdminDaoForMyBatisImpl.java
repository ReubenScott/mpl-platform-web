package com.kindustry.structure.dao.impl;

import org.apache.ibatis.session.SqlSession;

import com.kindustry.framework.dao.imp.BaseDaoImpl;
import com.kindustry.framework.orm.BaseEntity;
import com.kindustry.structure.dao.AdminDao;
import com.kindustry.structure.model.Admin;


public class AdminDaoForMyBatisImpl extends BaseDaoImpl<BaseEntity> implements AdminDao {

	public void addAdmin(Admin admin) {
		add(admin);
	}

	public Admin findAdminByUsername(String username) {
		//打开一个session
		Admin admin = null;
		try {
			
			admin = (Admin)session.selectOne(Admin.class.getName()+".findAdminByUsername", username);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			//关闭session
			session.close();
		}
		return admin;
	}

}

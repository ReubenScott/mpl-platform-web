package com.kindustry.structure.dao;

import com.kindustry.structure.model.Admin;


public interface AdminDao {
	public void addAdmin(Admin admin);
	public Admin findAdminByUsername(String username);
}
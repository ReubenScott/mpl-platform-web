package com.kindustry.system.service;

import com.kindustry.system.model.User;


/**
 *
 * User 表数据服务层接口
 *
 */
public interface IUserService /*extends IService<User>*/ {

	User selectByLoginName(String loginName);

	void deleteUser(Long userId);

}
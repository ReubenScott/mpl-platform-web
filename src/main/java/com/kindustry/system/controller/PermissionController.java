package com.kindustry.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kindustry.framework.web.BaseController;

/**
 * <p>
 * 角色管理相关操作
 * </p>
 *
 *
 * @Author hubin
 * @Date 2016-04-15
 */
@Controller
@RequestMapping("/perm/permission")
public class PermissionController extends BaseController {

//	@Autowired
//	private IPermissionService permissionService;
//
//	@Autowired
//	private IRolePermissionService rolePermissionService;

	@RequestMapping("/list")
	public String list(Model model) {
		return "/permission/list";
	}

//	@ResponseBody
//	@RequestMapping("/getPermissionList")
//	public String getPermissionList() {
//		Page<Permission> page = getPage();
//		return jsonPage(permissionService.selectPage(page, null));
//	}

//	@ResponseBody
//	@RequestMapping("/delete/{permId}")
//	public String delete(@PathVariable Long permId) {
//		boolean exist = rolePermissionService.existRolePermission(permId);
//		if (exist) {
//			return "false";
//		}
//		return booleanToString(permissionService.deleteById(permId));
//	}

}

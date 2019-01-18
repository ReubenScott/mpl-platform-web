package com.kindustry.common.enums;


public enum PermissionType {
	MENU(0, "菜单"), OPERATION(1, "功能");

	private final int key;
	private final String desc;

	PermissionType(final int key, final String desc) {
		this.key = key;
		this.desc = desc;
	}

	public int key() {
		return this.key;
	}

	public String desc() {
		return this.desc;
	}

}

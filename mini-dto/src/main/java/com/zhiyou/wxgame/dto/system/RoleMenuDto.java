package com.zhiyou.wxgame.dto.system;

import com.zhiyou.wxgame.dto.basedto.DTO;

public class RoleMenuDto extends DTO {
	private static final long serialVersionUID = 2526315537472855620L;
	
	private Integer id;
	private Integer roleId;
	private Integer menuId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

}

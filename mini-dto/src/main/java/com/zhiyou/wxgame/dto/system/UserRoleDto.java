package com.zhiyou.wxgame.dto.system;

import com.zhiyou.wxgame.dto.basedto.DTO;

public class UserRoleDto extends DTO{
	private static final long serialVersionUID = 4229254985692219725L;
	
	private Integer id;
	private Integer userId;
	private Integer roleId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

}

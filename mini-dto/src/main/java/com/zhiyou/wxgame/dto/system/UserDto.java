package com.zhiyou.wxgame.dto.system;

import java.util.Date;

import com.zhiyou.wxgame.dto.basedto.DTO;

public class UserDto extends DTO {
	private static final long serialVersionUID = 4961614309496307645L;

	private Integer userId;
	private String username;// 用户名
	private String name;//名称
	private String password;// 密码
	private Integer status;// 状态 0:禁用，1:正常
	private Date createTime;// 创建时间
	private Date modifiedTime;// 修改时间

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	
	private Integer roleId;

	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
}

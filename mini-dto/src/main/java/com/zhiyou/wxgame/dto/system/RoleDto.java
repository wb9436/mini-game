package com.zhiyou.wxgame.dto.system;

import java.util.Date;
import java.util.List;

import com.zhiyou.wxgame.dto.basedto.DTO;

public class RoleDto extends DTO {
	private static final long serialVersionUID = -4325912566096728285L;
	
	private Integer roleId;
	private String roleName;
	private String roleSign;
	private String remark;
	private Date createTime;
	private Date modifiedTime;
	private List<Integer> menuIds;

	public Integer getRoleId() {	
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleSign() {
		return roleSign;
	}

	public void setRoleSign(String roleSign) {
		this.roleSign = roleSign;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public List<Integer> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<Integer> menuIds) {
		this.menuIds = menuIds;
	}

}

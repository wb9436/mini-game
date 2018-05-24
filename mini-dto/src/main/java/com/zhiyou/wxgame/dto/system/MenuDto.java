package com.zhiyou.wxgame.dto.system;

import java.util.Date;

import com.zhiyou.wxgame.dto.basedto.DTO;

public class MenuDto extends DTO {
	private static final long serialVersionUID = 7740633906608495054L;

	private Integer menuId;
	private Integer parentId;// 父菜单ID，一级菜单为0
	private String name;// 菜单名称
	private String url;// 菜单URL
	private String perms;// 授权(多个用逗号分隔，如：user:list,user:create)
	private Integer type;// 类型 0：目录 1：菜单 2：按钮
	private String icon;// 菜单图标
	private Integer orderNum;// 排序
	private Date createTime;// 创建时间
	private Date modifiedTime;// 修改时间
	
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPerms() {
		return perms;
	}
	public void setPerms(String perms) {
		this.perms = perms;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
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
}

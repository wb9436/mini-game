package com.zhiyou.wxgame.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zhiyou.wxgame.dto.system.RoleMenuDto;

@Mapper
public interface RoleMenuDao {

	RoleMenuDto get(@Param("id") Integer id);

	List<RoleMenuDto> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(RoleMenuDto roleMenu);

	int update(RoleMenuDto roleMenu);

	int remove(@Param("id") Integer id);

	int batchRemove(Integer[] ids);

	List<Integer> listMenuIdByRoleId(@Param("roleId") Integer roleId);

	int removeByRoleId(@Param("roleId") Integer roleId);

	int removeByMenuId(@Param("menuId") Integer menuId);

	int batchSave(List<RoleMenuDto> list);
}

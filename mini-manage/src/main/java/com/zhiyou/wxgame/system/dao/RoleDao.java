package com.zhiyou.wxgame.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zhiyou.wxgame.dto.system.RoleDto;

@Mapper
public interface RoleDao {

	RoleDto get(@Param("roleId") Integer roleId);

	List<RoleDto> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(RoleDto role);

	int update(RoleDto role);

	int remove(@Param("roleId") Integer roleId);

	int batchRemove(Integer[] roleIds);
}

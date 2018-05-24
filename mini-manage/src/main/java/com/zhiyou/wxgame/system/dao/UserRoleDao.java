package com.zhiyou.wxgame.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zhiyou.wxgame.dto.system.UserRoleDto;

@Mapper
public interface UserRoleDao {

	UserRoleDto get(@Param("id") Integer id);

	List<UserRoleDto> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(UserRoleDto userRole);

	int update(UserRoleDto userRole);

	int remove(@Param("id") Integer id);

	int batchRemove(Integer[] ids);

	int removeByUserId(@Param("userId") Integer userId);

	int removeByRoleId(@Param("roleId") Integer roleId);

	int batchSave(List<UserRoleDto> list);

	int batchRemoveByUserId(Integer[] ids);

	UserRoleDto getByUserId(@Param("userId") Integer userId);
}

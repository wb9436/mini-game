package com.zhiyou.wxgame.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zhiyou.wxgame.dto.system.UserDto;

@Mapper
public interface UserDao {

	UserDto get(@Param("userId")Integer userId);

	List<UserDto> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(UserDto user);

	int update(UserDto user);

	int remove(@Param("userId")Integer userId);

	int batchRemove(Integer[] userIds);

	UserDto checkUser(Map<String, Object> params);
	
}

package com.zhiyou.wxgame.system.service;

import java.util.List;
import java.util.Map;

import com.zhiyou.wxgame.dto.system.UserDto;

public interface IUserService {

	UserDto getUserDto(String username, String password);

	List<UserDto> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	UserDto get(Integer id);

	int save(UserDto user);

	int update(UserDto user);

	int updatePersonal(UserDto user);

	int remove(Integer id);

	int batchremove(Integer[] userIds);

	boolean exit(Map<String, Object> params);

	int adminResetPwd(UserDto userDto) throws Exception;

}

package com.zhiyou.wxgame.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhiyou.wxgame.dto.system.UserDto;
import com.zhiyou.wxgame.dto.system.UserRoleDto;
import com.zhiyou.wxgame.system.dao.UserDao;
import com.zhiyou.wxgame.system.dao.UserRoleDao;
import com.zhiyou.wxgame.system.service.IUserService;
import com.zhiyou.wxgame.util.secret.MD5;
import com.zhiyou.wxgame.util.service.BService;

@Service
public class UserServiceImpl extends BService implements IUserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Override
	public UserDto getUserDto(String username, String password) {
		Map<String, Object> params = new HashMap<>();
		params.put("username", username);
		params.put("password", password);
		params.put("status", 1);
		return userDao.checkUser(params);
	}
	
	@Override
	public UserDto get(Integer userId) {
		return userDao.get(userId);
	}

	@Override
    public List<UserDto> list(Map<String, Object> map) {
        return userDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return userDao.count(map);
    }

    @Transactional
    @Override
    public int save(UserDto user) {
		int count = userDao.save(user);
		Integer userId = user.getUserId();
		userRoleDao.removeByUserId(userId);
		
		Integer roleId = user.getRoleId();
		UserRoleDto userRole = new UserRoleDto();
		userRole.setUserId(userId);
		userRole.setRoleId(roleId);
		userRoleDao.save(userRole);
		return count;
    }

    @Override
    public int update(UserDto user) {
        int count = userDao.update(user);
        Integer userId = user.getUserId();
        userRoleDao.removeByUserId(userId);
        
        Integer roleId = user.getRoleId();
        UserRoleDto userRole = new UserRoleDto();
		userRole.setUserId(userId);
		userRole.setRoleId(roleId);
		userRoleDao.save(userRole);
        return count;
    }

    @Override
    public int remove(Integer userId) {
    	userRoleDao.removeByUserId(userId);
        return userDao.remove(userId);
    }

    @Override
    public boolean exit(Map<String, Object> params) {
        boolean exit;
        exit = userDao.list(params).size() > 0;
        return exit;
    }

    @Override
    public int adminResetPwd(UserDto userDto) throws Exception {
    	UserDto user = get(userDto.getUserId());
        if ("admin".equals(user.getUsername())) {
            throw new Exception("超级管理员的账号不允许直接重置！");
        }
        user.setPassword(MD5.encodeUTF8(userDto.getPassword()));
        return userDao.update(user);
    }

    @Transactional
    @Override
    public int batchremove(Integer[] userIds) {
        int count = userDao.batchRemove(userIds);
        userRoleDao.batchRemoveByUserId(userIds);
        return count;
    }

    @Override
    public int updatePersonal(UserDto userDto) {
        return userDao.update(userDto);
    }

}

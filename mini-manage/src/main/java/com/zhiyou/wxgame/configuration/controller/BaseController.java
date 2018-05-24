package com.zhiyou.wxgame.configuration.controller;

import org.springframework.stereotype.Controller;

import com.zhiyou.wxgame.configuration.shiro.ShiroUtils;
import com.zhiyou.wxgame.dto.system.UserDto;

@Controller
public class BaseController {
	public UserDto getUser() {
		return ShiroUtils.getUser();
	}

	public Integer getUserId() {
		return getUser().getUserId();
	}

	public String getUsername() {
		return getUser().getUsername();
	}
}
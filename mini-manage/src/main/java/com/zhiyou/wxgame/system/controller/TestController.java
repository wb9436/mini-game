package com.zhiyou.wxgame.system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

	@RequestMapping("/hello")
	public String Hello(){
		return "hello";
	}
	
	
}

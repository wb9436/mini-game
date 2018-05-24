package com.zhiyou.wxgame.system.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhiyou.wxgame.configuration.controller.BaseController;
import com.zhiyou.wxgame.configuration.util.PageUtils;
import com.zhiyou.wxgame.configuration.util.Query;
import com.zhiyou.wxgame.dto.system.RoleDto;
import com.zhiyou.wxgame.dto.system.UserDto;
import com.zhiyou.wxgame.system.service.IRoleService;
import com.zhiyou.wxgame.system.service.IUserService;
import com.zhiyou.wxgame.util.secret.MD5;
import com.zhiyou.wxgame.util.utils.Result;

@RequestMapping("/sys/user")
@Controller
public class UserController extends BaseController {
	private String prefix = "system/user";

	@Autowired
	private IUserService userService;
	@Autowired
	private IRoleService roleService;

	@RequiresPermissions("sys:user:user")
	@GetMapping()
	public String user(Model model) {
		return prefix + "/user";
	}

	@RequiresPermissions("sys:user:user")
	@GetMapping("/list")
	@ResponseBody
	public PageUtils list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<UserDto> sysUserList = userService.list(query);
		int total = userService.count(query);
		PageUtils pageUtil = new PageUtils(sysUserList, total);
		return pageUtil;
	}

	@RequiresPermissions("sys:user:add")
	@GetMapping("/add")
	public String add(Model model) {
		List<RoleDto> roles = roleService.list();
		model.addAttribute("roles", roles);
		return prefix + "/add";
	}

	@RequiresPermissions("sys:user:add")
	@PostMapping("/save")
	@ResponseBody
	public Result save(UserDto user) {
		user.setPassword(MD5.encodeUTF8(user.getPassword()));
		if (userService.save(user) > 0) {
			return Result.ok();
		}
		return Result.error();
	}

	@RequiresPermissions("sys:user:edit")
	@GetMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") Integer id) {
		UserDto userDto = userService.get(id);
		model.addAttribute("user", userDto);
		List<RoleDto> roleDtos = roleService.list();
		model.addAttribute("roles", roleDtos);
		return prefix + "/edit";
	}

	@RequiresPermissions("sys:user:edit")
	@PostMapping("/update")
	@ResponseBody
	public Result update(UserDto user) {
		if (userService.update(user) > 0) {
			return Result.ok();
		}
		return Result.error();
	}

	@RequiresPermissions("sys:user:remove")
	@PostMapping("/remove")
	@ResponseBody
	public Result remove(Integer id) {
		if (userService.remove(id) > 0) {
			return Result.ok();
		}
		return Result.error();
	}

	@RequiresPermissions("sys:user:batchRemove")
	@PostMapping("/batchRemove")
	@ResponseBody
	public Result batchRemove(@RequestParam("ids[]") Integer[] userIds) {
		int r = userService.batchremove(userIds);
		if (r > 0) {
			return Result.ok();
		}
		return Result.error();
	}

	@PostMapping("/exit")
	@ResponseBody
	public boolean exit(@RequestParam Map<String, Object> params) {
		// 存在，不通过，false
		return !userService.exit(params);
	}

	@RequiresPermissions("sys:user:resetPwd")
	@GetMapping("/resetPwd/{id}")
	public String resetPwd(@PathVariable("id") Integer userId, Model model) {
		UserDto userDto = new UserDto();
		userDto.setUserId(userId);
		model.addAttribute("user", userDto);
		return prefix + "/reset_pwd";
	}

	@RequiresPermissions("sys:user:resetPwd")
	@PostMapping("/adminResetPwd")
	@ResponseBody
	public Result adminResetPwd(UserDto userDto) {
		try {
			userService.adminResetPwd(userDto);
			return Result.ok();
		} catch (Exception e) {
			return Result.error(1, e.getMessage());
		}
	}

	@GetMapping("/personal")
	public String personal(Model model) {
		UserDto userDto = userService.get(1);
		model.addAttribute("user", userDto);
		return prefix + "/personal";
	}

	@PostMapping("/updatePeronal")
	@ResponseBody
	public Result updatePeronal(UserDto user) {
		if (userService.updatePersonal(user) > 0) {
			return Result.ok();
		}
		return Result.error();
	}

}

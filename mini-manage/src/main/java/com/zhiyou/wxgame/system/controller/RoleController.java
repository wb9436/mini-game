package com.zhiyou.wxgame.system.controller;

import java.util.List;

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
import com.zhiyou.wxgame.dto.system.RoleDto;
import com.zhiyou.wxgame.system.service.IRoleService;
import com.zhiyou.wxgame.util.utils.Result;

@RequestMapping("/sys/role")
@Controller
public class RoleController extends BaseController{
	String prefix = "system/role";
	
	@Autowired
	private IRoleService roleService;

	@RequiresPermissions("sys:role:role")
	@GetMapping()
	public String role() {
		return prefix + "/role";
	}

	@RequiresPermissions("sys:role:role")
	@GetMapping("/list")
	@ResponseBody()
	public List<RoleDto> list() {
		List<RoleDto> roles = roleService.list();
		return roles;
	}

	@RequiresPermissions("sys:role:add")
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	@RequiresPermissions("sys:role:add")
	@PostMapping("/save")
	@ResponseBody()
	public Result save(RoleDto role) {
		if (roleService.save(role) > 0) {
			return Result.ok();
		} else {
			return Result.error(1, "保存失败");
		}
	}
	
	@RequiresPermissions("sys:role:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, Model model) {
		RoleDto roleDto = roleService.get(id);
		model.addAttribute("role", roleDto);
		return prefix + "/edit";
	}

	@RequiresPermissions("sys:role:edit")
	@PostMapping("/update")
	@ResponseBody()
	public Result update(RoleDto role) {
		if (roleService.update(role) > 0) {
			return Result.ok();
		} else {
			return Result.error(1, "保存失败");
		}
	}

	@RequiresPermissions("sys:role:remove")
	@PostMapping("/remove")
	@ResponseBody()
	public Result save(Integer id) {
		if (roleService.remove(id) > 0) {
			return Result.ok();
		} else {
			return Result.error(1, "删除失败");
		}
	}
	
	@RequiresPermissions("sys:role:batchRemove")
	@PostMapping("/batchRemove")
	@ResponseBody
	public Result batchRemove(@RequestParam("ids[]") Integer[] ids) {
		int r = roleService.batchremove(ids);
		if (r > 0) {
			return Result.ok();
		}
		return Result.error();
	}
}

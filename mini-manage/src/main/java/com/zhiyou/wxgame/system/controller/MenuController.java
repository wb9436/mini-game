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
import com.zhiyou.wxgame.configuration.util.Tree;
import com.zhiyou.wxgame.dto.system.MenuDto;
import com.zhiyou.wxgame.system.service.IMenuService;
import com.zhiyou.wxgame.util.utils.Result;

@RequestMapping("/sys/menu")
@Controller
public class MenuController extends BaseController{
	public String prefix = "system/menu";

	@Autowired
	private IMenuService menuService;

	@RequiresPermissions("sys:menu:menu")
	@GetMapping()
	public String menu(Model model) {
		return prefix + "/menu";
	}

	@RequiresPermissions("sys:menu:menu")
	@RequestMapping("/list")
	@ResponseBody
	public List<MenuDto> list(@RequestParam Map<String, Object> params) {
		List<MenuDto> menus = menuService.list(params);
		return menus;
	}

	@RequiresPermissions("sys:menu:add")
	@GetMapping("/add/{pId}")
	public String add(Model model, @PathVariable("pId") Integer pId) {
		model.addAttribute("pId", pId);
		if (pId == 0) {
			model.addAttribute("pName", "根目录");
		} else {
			model.addAttribute("pName", menuService.get(pId).getName());
		}
		return prefix + "/add";
	}

	@RequiresPermissions("sys:menu:edit")
	@GetMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") Integer id) {
		MenuDto mdo = menuService.get(id);
		Integer pId = mdo.getParentId();
		model.addAttribute("pId", pId);
		if (pId == 0) {
			model.addAttribute("pName", "根目录");
		} else {
			model.addAttribute("pName", menuService.get(pId).getName());
		}
		model.addAttribute("menu", mdo);
		return prefix + "/edit";
	}

	@RequiresPermissions("sys:menu:add")
	@PostMapping("/save")
	@ResponseBody
	public Result save(MenuDto menu) {
		if (menuService.save(menu) > 0) {
			return Result.ok();
		} else {
			return Result.error(1, "保存失败");
		}
	}

	@RequiresPermissions("sys:menu:edit")
	@PostMapping("/update")
	@ResponseBody
	public Result update(MenuDto menu) {
		if (menuService.update(menu) > 0) {
			return Result.ok();
		} else {
			return Result.error(1, "更新失败");
		}
	}

	@RequiresPermissions("sys:menu:remove")
	@PostMapping("/remove")
	@ResponseBody
	public Result remove(Integer id) {
		if (menuService.remove(id) > 0) {
			return Result.ok();
		} else {
			return Result.error(1, "删除失败");
		}
	}
	
	@GetMapping("/tree")
	@ResponseBody
	public Tree<MenuDto> tree() {
		Tree<MenuDto>  tree = menuService.getTree();
		return tree;
	}

	@GetMapping("/tree/{roleId}")
	@ResponseBody
	public Tree<MenuDto> tree(@PathVariable("roleId") Integer roleId) {
		Tree<MenuDto> tree = menuService.getTree(roleId);
		return tree;
	}

}

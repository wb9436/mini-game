package com.zhiyou.wxgame.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhiyou.wxgame.configuration.util.BuildTree;
import com.zhiyou.wxgame.configuration.util.Tree;
import com.zhiyou.wxgame.dto.system.MenuDto;
import com.zhiyou.wxgame.system.dao.MenuDao;
import com.zhiyou.wxgame.system.dao.RoleMenuDao;
import com.zhiyou.wxgame.system.service.IMenuService;
import com.zhiyou.wxgame.util.service.BService;

@Service
public class MenuServiceImpl extends BService implements IMenuService {

	@Autowired
	private MenuDao menuDao;
	@Autowired
	private RoleMenuDao roleMenuDao;

	@Override
	public List<Tree<MenuDto>> getUserListMenuTree(int userId) {
		List<Tree<MenuDto>> trees = new ArrayList<Tree<MenuDto>>();
		List<MenuDto> menuDOs = menuDao.listMenuByUserId(userId);
		for (MenuDto sysMenuDO : menuDOs) {
			Tree<MenuDto> tree = new Tree<MenuDto>();
			tree.setId(sysMenuDO.getMenuId().toString());
			tree.setParentId(sysMenuDO.getParentId().toString());
			tree.setText(sysMenuDO.getName());
			Map<String, Object> attributes = new HashMap<>(16);
			attributes.put("url", sysMenuDO.getUrl());
			attributes.put("icon", sysMenuDO.getIcon());
			tree.setAttributes(attributes);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		List<Tree<MenuDto>> list = BuildTree.buildList(trees, "0");
		return list;
	}

	@Override
	public List<MenuDto> list(Map<String, Object> params) {
		List<MenuDto> menus = menuDao.list(params);
		return menus;
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@Override
	public int remove(Integer menuId) {
		List<MenuDto> childMenuList = menuDao.childList(menuId);
		if (childMenuList != null && childMenuList.size() > 0) {
			for (int i = 0; i < childMenuList.size(); i++) {
				MenuDto childMenu = childMenuList.get(i);
				List<MenuDto> childMenuList2 = menuDao.childList(childMenu.getMenuId());
				if (childMenuList2 != null && childMenuList2.size() > 0) {
					for (int j = 0; j < childMenuList2.size(); j++) {
						MenuDto childMenu2 = childMenuList2.get(j);
						roleMenuDao.removeByMenuId(childMenu2.getMenuId());
						menuDao.remove(childMenu2.getMenuId());
					}
				}
				roleMenuDao.removeByMenuId(childMenu.getMenuId());
				menuDao.remove(childMenu.getMenuId());
			}
		}
		roleMenuDao.removeByMenuId(menuId);
		int result = menuDao.remove(menuId);
		return result;
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@Override
	public int save(MenuDto menu) {
		int r = menuDao.save(menu);
		return r;
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@Override
	public int update(MenuDto menu) {
		int r = menuDao.update(menu);
		return r;
	}

	@Override
	public MenuDto get(Integer id) {
		MenuDto menuDO = menuDao.get(id);
		return menuDO;
	}

	@Override
	public Tree<MenuDto> getTree() {
		List<Tree<MenuDto>> trees = new ArrayList<Tree<MenuDto>>();
		List<MenuDto> menuDOs = menuDao.list(new HashMap<>(16));
		for (MenuDto sysMenuDO : menuDOs) {
			Tree<MenuDto> tree = new Tree<MenuDto>();
			tree.setId(sysMenuDO.getMenuId().toString());
			tree.setParentId(sysMenuDO.getParentId().toString());
			tree.setText(sysMenuDO.getName());
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<MenuDto> t = BuildTree.build(trees);
		return t;
	}

	@Override
	public Tree<MenuDto> getTree(Integer id) {
		// 根据roleId查询权限
		List<MenuDto> menus = menuDao.list(new HashMap<String, Object>(16));
		List<Integer> menuIds = roleMenuDao.listMenuIdByRoleId(id);
		List<Integer> temp = menuIds;
		for (MenuDto menu : menus) {
			if (temp.contains(menu.getParentId())) {
				menuIds.remove(menu.getParentId());
			}
		}
		List<Tree<MenuDto>> trees = new ArrayList<Tree<MenuDto>>();
		List<MenuDto> menuDOs = menuDao.list(new HashMap<String, Object>(16));
		for (MenuDto sysMenuDO : menuDOs) {
			Tree<MenuDto> tree = new Tree<MenuDto>();
			tree.setId(sysMenuDO.getMenuId().toString());
			tree.setParentId(sysMenuDO.getParentId().toString());
			tree.setText(sysMenuDO.getName());
			Map<String, Object> state = new HashMap<>(16);
			Integer menuId = sysMenuDO.getMenuId();
			if (menuIds.contains(menuId)) {
				state.put("selected", true);
			} else {
				state.put("selected", false);
			}
			tree.setState(state);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<MenuDto> t = BuildTree.build(trees);
		return t;
	}

	@Override
	public Set<String> listPerms(Integer userId) {
		List<String> perms = menuDao.listUserPerms(userId);
		Set<String> permsSet = new HashSet<>();
		for (String perm : perms) {
			if (StringUtils.isNotBlank(perm)) {
				permsSet.addAll(Arrays.asList(perm.trim().split(",")));
			}
		}
		return permsSet;
	}

}

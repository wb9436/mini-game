package com.zhiyou.wxgame.system.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zhiyou.wxgame.configuration.util.Tree;
import com.zhiyou.wxgame.dto.system.MenuDto;

public interface IMenuService {

	List<Tree<MenuDto>> getUserListMenuTree(int userId);

	List<MenuDto> list(Map<String, Object> params);

	MenuDto get(Integer pId);

	int save(MenuDto menu);

	int update(MenuDto menu);

	int remove(Integer menuId);

	Tree<MenuDto> getTree();

	Tree<MenuDto> getTree(Integer roleId);

	Set<String> listPerms(Integer userId);

}

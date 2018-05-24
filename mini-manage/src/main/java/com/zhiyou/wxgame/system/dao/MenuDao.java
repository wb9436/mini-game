package com.zhiyou.wxgame.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zhiyou.wxgame.dto.system.MenuDto;

@Mapper
public interface MenuDao {

	MenuDto get(@Param("menuId")Integer menuId);
	
	List<MenuDto> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(MenuDto menu);
	
	int update(MenuDto menu);
	
	int remove(@Param("menuId")Integer menuId);
	
	int batchRemove(Integer[] menuIds);

	List<MenuDto> listMenuByUserId(@Param("userId")int userId);
	
	List<MenuDto> childList(@Param("parentId")Integer parentId);

	List<String> listUserPerms(@Param("userId")Integer userId);
}

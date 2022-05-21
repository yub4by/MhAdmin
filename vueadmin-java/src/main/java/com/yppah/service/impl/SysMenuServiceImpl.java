package com.yppah.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yppah.common.dto.SysMenuDto;
import com.yppah.entity.SysMenu;
import com.yppah.entity.SysUser;
import com.yppah.mapper.SysMenuMapper;
import com.yppah.mapper.SysUserMapper;
import com.yppah.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yppah.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author haifei
 * @since 2021-12-21
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    @Lazy
    private SysUserService sysUserService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysMenuDto> getCurrentUserNav() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.getByUserName(username);
        List<Long> menuIds = sysUserMapper.getNavMenuIds(sysUser.getId());
        List<SysMenu> menus = this.listByIds(menuIds);

        // list转为树状结构
        List<SysMenu> menuTree = buildTreeMenu(menus);

        // 实体转为DTO
        return convert(menuTree);
    }

    private List<SysMenuDto> convert(List<SysMenu> menuTree) {
        List<SysMenuDto> menuDtos = new ArrayList<>();
        menuTree.forEach(m -> {
            SysMenuDto dto = new SysMenuDto();
            dto.setId(m.getId());
            dto.setName(m.getPerms()); //
            dto.setTitle(m.getName()); //
            dto.setComponent(m.getComponent());
            dto.setPath(m.getPath());
            if (m.getChildren().size() > 0){
                dto.setChildren(convert(m.getChildren())); // 递归，子节点调用当前方法进行再次转换
            }
            menuDtos.add(dto);
        });
        return menuDtos;
    }

    private List<SysMenu> buildTreeMenu(List<SysMenu> menus) {
        List<SysMenu> finalMenus = new ArrayList<>();

        for (SysMenu menu: menus){
            // 1 先各自寻找到自己的孩子节点
            for (SysMenu m: menus){
                if (menu.getId() == m.getParentId()){
                    menu.getChildren().add(m);
                }
            }
            // 2 再提取出父节点
            if (menu.getParentId() == 0L){
                finalMenus.add(menu);
            }
        }
        /*System.out.println(finalMenus.toString());
        System.out.println(JSONUtil.toJsonStr(finalMenus));*/

        return finalMenus;
    }

    @Override
    public List<SysMenu> tree() {
        // 1、获取所有菜单信息
        List<SysMenu> sysMenus = this.list(new QueryWrapper<SysMenu>().orderByAsc("orderNum"));
        // 2、转为树状结构
        return buildTreeMenu(sysMenus);
    }
}

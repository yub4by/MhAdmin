package com.yppah.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yppah.entity.SysMenu;
import com.yppah.entity.SysRole;
import com.yppah.entity.SysUser;
import com.yppah.mapper.SysUserMapper;
import com.yppah.service.SysMenuService;
import com.yppah.service.SysRoleService;
import com.yppah.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yppah.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author haifei
 * @since 2021-12-21
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    @Lazy //SysMenuServiceImpl和SysUserServiceImpl存在循环依赖异常，https://blog.csdn.net/anguowei/article/details/80194644
    private SysMenuService sysMenuService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public SysUser getByUserName(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    /*@Override
    public String getUserAuthorityInfo(Long userId) {
        // ROLE_admin,ROLE_normal,sys:user:list,....
        String authority = "";

        // 获取角色编码
        List<SysRole> roles = sysRoleService.list(new QueryWrapper<SysRole>()
                .inSql("id", "select role_id from sys_user_role where user_id=" + userId)
        );
        // select * from sys_role where id in (select role_id from sys_user_role where user_id=1)
        if (roles.size() > 0) {
            String roleCodes = roles
                    .stream()
                    .map(r -> "ROLE_"+r.getCode())
                    .collect(Collectors.joining(","));
            authority = roleCodes.concat(",");
        }

        // 获取菜单操作权限编码
        *//*
            select distinct rm.menu_id
            from sys_user_role as ur
            left join
                sys_role_menu as rm
                on ur.role_id = rm.role_id
            where ur.user_id = 1
        *//*
        List<Long> menuIds = sysUserMapper.getNavMenuIds(userId);
        if (menuIds.size() > 0) {
            List<SysMenu> menus = sysMenuService.listByIds(menuIds);
            String menuPerms = menus.stream()
                    .map(m -> m.getPerms())
                    .collect(Collectors.joining(","));
            authority = authority.concat(menuPerms);
        }

        return authority;
    }*/

    @Override
    public String getUserAuthorityInfo(Long userId) {
        // ROLE_admin,ROLE_normal,sys:user:list,....
        String authority = "";

        SysUser sysUser = sysUserMapper.selectById(userId);
        // +1层缓存（缓存里存在数据时直接从缓存中获取，缓存中没有时创建数据并存到缓存中）
        if (redisUtil.hasKey("GrantedAuthority:" + sysUser.getUsername())) {
            authority = (String) redisUtil.get("GrantedAuthority:" + sysUser.getUsername());
        } else {
            // 获取角色编码
            List<SysRole> roles = sysRoleService.list(new QueryWrapper<SysRole>()
                    .inSql("id", "select role_id from sys_user_role where user_id=" + userId)
            );
            // select * from sys_role where id in (select role_id from sys_user_role where user_id=1)
            if (roles.size() > 0) {
                String roleCodes = roles
                        .stream()
                        .map(r -> "ROLE_"+r.getCode())
                        .collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }

            // 获取菜单操作权限编码
            /*
                select distinct rm.menu_id
                from sys_user_role as ur
                left join
                    sys_role_menu as rm
                    on ur.role_id = rm.role_id
                where ur.user_id = 1
            */
            List<Long> menuIds = sysUserMapper.getNavMenuIds(userId);
            if (menuIds.size() > 0) {
                List<SysMenu> menus = sysMenuService.listByIds(menuIds);
                String menuPerms = menus.stream()
                        .map(m -> m.getPerms())
                        .collect(Collectors.joining(","));
                authority = authority.concat(menuPerms);
            }

            redisUtil.set("GrantedAuthority:" + sysUser.getUsername(), authority, 60 * 60); // 缓存1H
        }

        return authority;
    }


    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtil.del("GrantedAuthority:" + username);
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {
        List<SysUser> sysUsers = this.list(new QueryWrapper<SysUser>()
                .inSql("id", "select user_id from sys_user_role where role_id=" + roleId)
        );
        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }

    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        List<SysUser> sysUsers = sysUserMapper.listByMenuId(menuId);
        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }
}

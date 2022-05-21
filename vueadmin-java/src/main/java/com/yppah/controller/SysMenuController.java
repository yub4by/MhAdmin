package com.yppah.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yppah.common.dto.SysMenuDto;
import com.yppah.common.lang.Result;
import com.yppah.entity.SysMenu;
import com.yppah.entity.SysRoleMenu;
import com.yppah.entity.SysUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author haifei
 * @since 2021-12-21
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {

    /**
     * 用户当前用户的菜单和权限信息
     * @param principal
     * @return
     */
    @GetMapping("/nav")
    public Result nav(Principal principal){
        SysUser sysUser = userService.getByUserName(principal.getName());

        // 获取权限信息
        String authorityInfo = userService.getUserAuthorityInfo(sysUser.getId());
        String[] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo, ",");

        // 获取菜单信息
        List<SysMenuDto> navs = menuService.getCurrentUserNav();

        return Result.success(MapUtil.builder()
                .put("authorities", authorityInfoArray)
                .put("navs", navs)
                .map()
        );
    }

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result info(@PathVariable(name = "id") Long id) {
        return Result.success(menuService.getById(id));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result list() {
        List<SysMenu> menus = menuService.tree();
        return Result.success(menus);
    }


    /**
     * spring-boot-starter-validation
     * SysMenu中需要验证的字段加上@NotNull或@NotBlank注解
     * 并在此接口中加上@Validated注解
     *
     * @param sysMenu
     * @return
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:menu:save')")
    public Result save(@Validated @RequestBody SysMenu sysMenu) {
        sysMenu.setCreated(LocalDateTime.now());
        menuService.save(sysMenu);
        return Result.success(sysMenu);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public Result update(@Validated @RequestBody SysMenu sysMenu) {
        sysMenu.setUpdated(LocalDateTime.now());
        menuService.updateById(sysMenu);
        userService.clearUserAuthorityInfoByMenuId(sysMenu.getId()); //注意：清除所有与该菜单相关的权限缓存
        return Result.success(sysMenu);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public Result delete(@PathVariable("id") Long id) {
        int count = menuService.count(new QueryWrapper<SysMenu>().eq("parent_id", id));
        if (count > 0) {
            return Result.fail("请先删除子菜单"); //不允许直接删除含有子菜单的父级菜单
        }
        userService.clearUserAuthorityInfoByMenuId(id); //注意：清除所有与该菜单相关的权限缓存
        menuService.removeById(id);
        roleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("menu_id", id)); // 同步删除中间关联表
        return Result.success("");
    }

}

package com.yppah.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yppah.common.lang.Const;
import com.yppah.common.lang.Result;
import com.yppah.entity.SysRole;
import com.yppah.entity.SysRoleMenu;
import com.yppah.entity.SysUserRole;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * 注意：以后个人开发前后端分离项目时，
 * 推荐前后端一起开发，避免开发完一端再开发另一端时变量命名不一致问题
 * 推荐使用swagger2库（文档工具，自动生成API接口文档）   https://www.jianshu.com/p/c79f6a14f6c9
 *
 * @author haifei
 * @since 2021-12-21
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {

    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id")  Long id) { // 注意要有@PathVariable，不然会报空指针异常
        SysRole sysRole = roleService.getById(id);
        // 获取角色相关联的菜单ID
        List<SysRoleMenu> roleMenus = roleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id", id));
        List<Long> menuIds = roleMenus.stream().map(p -> p.getMenuId()).collect(Collectors.toList());
        sysRole.setMenuIds(menuIds);
        return Result.success(sysRole);
    }

    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/list")
    public Result list(String name) {
        Page<SysRole> rolePage = roleService.page(getPage(), new QueryWrapper<SysRole>()
                .like(StrUtil.isNotBlank(name), "name", name)
        );
        return Result.success(rolePage);
    }

    @PreAuthorize("hasAuthority('sys:role:save')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody SysRole sysRole) {
        sysRole.setCreated(LocalDateTime.now());
        sysRole.setStatu(Const.STATUS_ON); // 0
        roleService.save(sysRole);
        return Result.success(sysRole);
    }

    @PreAuthorize("hasAuthority('sys:role:update')")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody SysRole sysRole) {
        sysRole.setUpdated(LocalDateTime.now());
        roleService.updateById(sysRole);
        // 注意：同步更新缓存
        userService.clearUserAuthorityInfoByRoleId(sysRole.getId());
        return Result.success(sysRole);
    }

    /**
     * 批量删除 & 单条删除
     * @param ids
     * @return
     */
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @PostMapping("/delete")
    @Transactional // 因为涉及删除多方数据，因此添加事务保证原子性
    public Result delete(@RequestBody Long[] ids) {
        roleService.removeByIds(Arrays.asList(ids));
        // 删除中间关联表中的相关数据
        userRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id", ids));
        roleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id", ids));
        // 注意：缓存同步删除
        Arrays.stream(ids).forEach(id -> {
            userService.clearUserAuthorityInfoByRoleId(id); // 同步更新缓存
        });
        return Result.success("");
    }

    /**
     * 分配权限
     * @return
     */
    @PreAuthorize("hasAuthority('sys:role:perm')")
    @PostMapping("/perm/{roleId}")
    @Transactional
    public Result perm(@PathVariable("roleId") Long roleId, @RequestBody Long[] menuIds) {
        List<SysRoleMenu> roleMenuList = new ArrayList<>();
        Arrays.stream(menuIds).forEach(menuId -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            roleMenuList.add(roleMenu);
        });
        // 先删除所有旧的相关记录
        roleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId));
        // 再(批量)保存新的数据
        roleMenuService.saveBatch(roleMenuList);
        // 注意同步更新(删除)缓存
        userService.clearUserAuthorityInfoByRoleId(roleId);
        return Result.success(menuIds);
    }

}

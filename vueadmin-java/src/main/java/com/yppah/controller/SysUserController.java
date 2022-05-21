package com.yppah.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yppah.common.dto.PassDto;
import com.yppah.common.lang.Const;
import com.yppah.common.lang.Result;
import com.yppah.entity.SysRole;
import com.yppah.entity.SysUser;
import com.yppah.entity.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; //加密算法

    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id) {
        SysUser sysUser = userService.getById(id);
        Assert.notNull(sysUser, "找不到该用户"); //防止空指针异常，为null时可以交给GlobalExceptionHandler处理
        List<SysRole> roles = roleService.getRolesByUserId(id);
        sysUser.setSysRoles(roles);
        return Result.success(sysUser);
    }

    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/list")
    public Result list(String username) { // 注意接收参数一定要与前端设置的参数一致
        Page<SysUser> userPage = userService.page(getPage(), new QueryWrapper<SysUser>()
                .like(StrUtil.isNotBlank(username), "username", username)
        );
        //通过关联表获取每个用户的角色信息（列表中需要展示角色名称，权限分配中需要角色ID）
        userPage.getRecords().forEach(u -> {
            u.setSysRoles(roleService.getRolesByUserId(u.getId()));
        });
        return Result.success(userPage);
    }

    @PreAuthorize("hasAuthority('sys:user:save')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody SysUser sysUser) {
        sysUser.setCreated(LocalDateTime.now());
        sysUser.setStatu(Const.STATUS_ON); // 0
        // 初始化默认密码888888（加密处理）
        String encodePswd = bCryptPasswordEncoder.encode(Const.DEFULT_PASSWORD);// 888888
        sysUser.setPassword(encodePswd);
        // 初始化默认头像
        sysUser.setAvatar(Const.DEFULT_AVATAR);
        userService.save(sysUser);
        return Result.success(sysUser);
    }

    @PreAuthorize("hasAuthority('sys:user:update')")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody SysUser sysUser) {
        sysUser.setUpdated(LocalDateTime.now());
        userService.updateById(sysUser);
        return Result.success(sysUser);
    }

    /**
     * 批量删除 & 单条删除
     * @param ids
     * @return
     */
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @PostMapping("/delete")
    @Transactional
    public Result delete(@RequestBody Long[] ids) {
        userService.removeByIds(Arrays.asList(ids));
        // 注意：关联表中与ids相关的记录都要同步删除掉
        userRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", ids));
        return Result.success("");
    }

    // 给指定用户分配角色
    @PreAuthorize("hasAuthority('sys:user:role')")
    @PostMapping("/role/{userId}")
    @Transactional
    public Result rolePerm(@PathVariable("userId") Long userId, @RequestBody Long[] roleIds) {
        List<SysUserRole> userRoleList = new ArrayList<>();
        Arrays.stream(roleIds).forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleList.add(userRole);
        });
        // 先删除所有旧的相关记录
        userRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        // 再(批量)保存新的数据
        userRoleService.saveBatch(userRoleList);
        // 最后同步更新(删除)缓存
        SysUser sysUser = userService.getById(userId);
        userService.clearUserAuthorityInfo(sysUser.getUsername());
        return Result.success(roleIds);
    }

    // 用户重置密码(重置为初始默认密码)
    @PostMapping("/repass")
    @PreAuthorize("hasAuthority('sys:user:repass')")
    public Result repass(@RequestBody Long userId) {
        SysUser user = userService.getById(userId);
        user.setPassword(bCryptPasswordEncoder.encode(Const.DEFULT_PASSWORD));
        user.setUpdated(LocalDateTime.now());
        userService.updateById(user);
        return Result.success("");
    }

    // 用户修改密码(重置为自定义密码)
    // 不需要权限，只要用户登录成功，就可以修改自己的密码
    @PostMapping("/updatePass")
    public Result updatePass(@Validated @RequestBody PassDto passDto, Principal principal) {
        // 获取当前用户是谁
        SysUser sysUser = userService.getByUserName(principal.getName());

        // 匹配旧密码是否正确
        boolean matches = bCryptPasswordEncoder.matches(passDto.getCurrentPass(), sysUser.getPassword());
        if (!matches){
            return Result.fail("旧密码不正确");
        }
        // 更新为新密码（注意加密）
        sysUser.setPassword(bCryptPasswordEncoder.encode(passDto.getPassword()));
        sysUser.setUpdated(LocalDateTime.now());
        userService.updateById(sysUser);
        return Result.success("");
    }

}

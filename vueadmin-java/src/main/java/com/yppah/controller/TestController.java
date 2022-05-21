package com.yppah.controller;

import com.yppah.common.lang.Result;
import com.yppah.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test")
    public Object test(){
        return sysUserService.list();
    }

    @GetMapping("/test2")
    public Result test2(){
        return Result.success(sysUserService.list());
    }

    @PreAuthorize("hasRole('admin')") // 当前/test3只有角色admin有权访问
    @GetMapping("/test3")
    public Result test3(){
        return Result.success(sysUserService.list());
    }

    @PreAuthorize("hasAnyAuthority('sys:user:list')") // 当前/test4只有拥有sys:user:list权限的用户才能访问
    @GetMapping("/test4")
    public Result test4(){
        return Result.success(sysUserService.list());
    }

    @GetMapping("/test/pswd")
    public Result pswd(){
        String password = bCryptPasswordEncoder.encode("111111"); // 生成加密后的密码
        boolean matches = bCryptPasswordEncoder.matches("111111", password); // 模拟与用户输入密码匹配
        System.out.println("匹配结果：" + matches);
        return Result.success(password);
    }

}

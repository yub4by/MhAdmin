package com.yppah.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yppah.service.*;
import com.yppah.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

public class BaseController {


    //注意不要使用privite，因为其他继承BaseController的Controller要用，用默认的包访问权限就行
    @Autowired
    HttpServletRequest req;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SysUserService userService;

    @Autowired
    SysRoleService roleService;

    @Autowired
    SysMenuService menuService;

    @Autowired
    SysUserRoleService userRoleService;

    @Autowired
    SysRoleMenuService roleMenuService;

    /**
     * 获取页面
     * @return
     */
    public Page getPage() {
        int current = ServletRequestUtils.getIntParameter(req, "current", 1);
        int size = ServletRequestUtils.getIntParameter(req, "size", 10);
        return new Page(current, size);
    }
}

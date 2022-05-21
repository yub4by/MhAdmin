package com.yppah.service;

import com.yppah.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author haifei
 * @since 2021-12-21
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUserName(String username);

    String getUserAuthorityInfo(Long userId);

    void clearUserAuthorityInfo(String username);
    void clearUserAuthorityInfoByRoleId(Long roleId);
    void clearUserAuthorityInfoByMenuId(Long menuId);
}

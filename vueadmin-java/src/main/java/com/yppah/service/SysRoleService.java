package com.yppah.service;

import com.yppah.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author haifei
 * @since 2021-12-21
 */
public interface SysRoleService extends IService<SysRole> {

    List<SysRole> getRolesByUserId(Long userId);
}

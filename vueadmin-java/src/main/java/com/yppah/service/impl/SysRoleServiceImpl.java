package com.yppah.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yppah.entity.SysRole;
import com.yppah.mapper.SysRoleMapper;
import com.yppah.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        List<SysRole> sysRoles = this.list(new QueryWrapper<SysRole>()
                .inSql("id", "select role_id from sys_user_role where user_id=" + userId)
        );
        return sysRoles;
    }
}

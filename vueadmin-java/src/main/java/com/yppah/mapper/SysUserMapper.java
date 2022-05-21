package com.yppah.mapper;

import com.yppah.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author haifei
 * @since 2021-12-21
 */
@Repository
// @Mapper和@Repository注解的区别
// https://blog.csdn.net/m0_51810758/article/details/109571197
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<Long> getNavMenuIds(Long userId);

    List<SysUser> listByMenuId(Long menuId);
}

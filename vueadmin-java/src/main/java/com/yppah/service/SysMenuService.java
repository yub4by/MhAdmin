package com.yppah.service;

import com.yppah.common.dto.SysMenuDto;
import com.yppah.entity.SysMenu;
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
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenuDto> getCurrentUserNav();

    /**
     * 树状结构
     * @return
     */
    List<SysMenu> tree();
}

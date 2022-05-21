package com.yppah.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *                  {
 * 					    name: 'SysUser',
 * 					    title: '用户管理',
 * 					    icon: 'el-icon-s-custom',
 * 					    path: '/sys/users',
 * 					    component: 'sys/User',
 * 					    children: []
 *                  }
 */

/**
 * vo是前端传给后端
 * dto是后端传给前端
 */
@Data
public class SysMenuDto implements Serializable {

    private Long id;
    private String name;
    private String title;
    private String icon;
    private String path;
    private String component;
    private List<SysMenuDto> children = new ArrayList<>();

}

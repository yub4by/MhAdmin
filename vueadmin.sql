/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50614
Source Host           : localhost:3306
Source Database       : vueadmin

Target Server Type    : MYSQL
Target Server Version : 50614
File Encoding         : 65001

Date: 2022-05-21 18:58:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(64) NOT NULL,
  `path` varchar(255) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(255) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `component` varchar(255) DEFAULT NULL,
  `type` int(5) NOT NULL COMMENT '类型     0：目录   1：菜单   2：按钮',
  `icon` varchar(32) DEFAULT NULL COMMENT '菜单图标',
  `orderNum` int(11) DEFAULT NULL COMMENT '排序',
  `created` datetime NOT NULL,
  `updated` datetime DEFAULT NULL,
  `statu` int(5) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '0', '系统管理', '', 'sys:manage', '', '0', 'el-icon-s-operation', '1', '2021-01-15 18:58:18', '2021-01-15 18:58:20', '1');
INSERT INTO `sys_menu` VALUES ('2', '1', '用户管理', '/sys/users', 'sys:user:list', 'system/User', '1', 'el-icon-s-custom', '1', '2021-01-15 19:03:45', '2021-01-15 19:03:48', '1');
INSERT INTO `sys_menu` VALUES ('3', '1', '角色管理', '/sys/roles', 'sys:role:list', 'system/Role', '1', 'el-icon-rank', '2', '2021-01-15 19:03:45', '2021-01-15 19:03:48', '1');
INSERT INTO `sys_menu` VALUES ('4', '1', '菜单管理', '/sys/menus', 'sys:menu:list', 'system/Menu', '1', 'el-icon-menu', '3', '2021-01-15 19:03:45', '2021-01-15 19:03:48', '1');
INSERT INTO `sys_menu` VALUES ('5', '0', '系统工具', '', 'sys:tools', null, '0', 'el-icon-s-tools', '2', '2021-01-15 19:06:11', null, '1');
INSERT INTO `sys_menu` VALUES ('6', '5', '数字字典', '/sys/dicts', 'sys:dict:list', 'system/Dict', '1', 'el-icon-s-order', '1', '2021-01-15 19:07:18', '2021-01-18 16:32:13', '1');
INSERT INTO `sys_menu` VALUES ('7', '3', '添加角色', '', 'sys:role:save', '', '2', '', '1', '2021-01-15 23:02:25', '2021-01-17 21:53:14', '0');
INSERT INTO `sys_menu` VALUES ('9', '2', '添加用户', null, 'sys:user:save', null, '2', null, '1', '2021-01-17 21:48:32', null, '1');
INSERT INTO `sys_menu` VALUES ('10', '2', '修改用户', null, 'sys:user:update', null, '2', null, '2', '2021-01-17 21:49:03', '2021-01-17 21:53:04', '1');
INSERT INTO `sys_menu` VALUES ('11', '2', '删除用户', null, 'sys:user:delete', null, '2', null, '3', '2021-01-17 21:49:21', null, '1');
INSERT INTO `sys_menu` VALUES ('12', '2', '分配角色', null, 'sys:user:role', null, '2', null, '4', '2021-01-17 21:49:58', null, '1');
INSERT INTO `sys_menu` VALUES ('13', '2', '重置密码', null, 'sys:user:repass', null, '2', null, '5', '2021-01-17 21:50:36', null, '1');
INSERT INTO `sys_menu` VALUES ('14', '3', '修改角色', null, 'sys:role:update', null, '2', null, '2', '2021-01-17 21:51:14', null, '1');
INSERT INTO `sys_menu` VALUES ('15', '3', '删除角色', null, 'sys:role:delete', null, '2', null, '3', '2021-01-17 21:51:39', null, '1');
INSERT INTO `sys_menu` VALUES ('16', '3', '分配权限', null, 'sys:role:perm', null, '2', null, '5', '2021-01-17 21:52:02', null, '1');
INSERT INTO `sys_menu` VALUES ('17', '4', '添加菜单', null, 'sys:menu:save', null, '2', null, '1', '2021-01-17 21:53:53', '2021-01-17 21:55:28', '1');
INSERT INTO `sys_menu` VALUES ('18', '4', '修改菜单', null, 'sys:menu:update', null, '2', null, '2', '2021-01-17 21:56:12', null, '1');
INSERT INTO `sys_menu` VALUES ('19', '4', '删除菜单', null, 'sys:menu:delete', null, '2', null, '3', '2021-01-17 21:56:36', null, '1');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `code` varchar(64) NOT NULL,
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `statu` int(5) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`) USING BTREE,
  UNIQUE KEY `code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('3', '普通用户', 'normal', '只有基本查看功能', '2021-01-04 10:09:14', '2021-01-30 08:19:52', '1');
INSERT INTO `sys_role` VALUES ('6', '超级管理员', 'admin', '系统默认最高权限，不可以编辑和任意修改', '2021-01-16 13:29:03', '2021-01-17 15:50:45', '1');
INSERT INTO `sys_role` VALUES ('11', '测试用户', 'testuser', null, '2021-12-23 17:58:36', '2021-12-23 17:58:44', '1');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('60', '6', '1');
INSERT INTO `sys_role_menu` VALUES ('61', '6', '2');
INSERT INTO `sys_role_menu` VALUES ('62', '6', '9');
INSERT INTO `sys_role_menu` VALUES ('63', '6', '10');
INSERT INTO `sys_role_menu` VALUES ('64', '6', '11');
INSERT INTO `sys_role_menu` VALUES ('65', '6', '12');
INSERT INTO `sys_role_menu` VALUES ('66', '6', '13');
INSERT INTO `sys_role_menu` VALUES ('67', '6', '3');
INSERT INTO `sys_role_menu` VALUES ('68', '6', '7');
INSERT INTO `sys_role_menu` VALUES ('69', '6', '14');
INSERT INTO `sys_role_menu` VALUES ('70', '6', '15');
INSERT INTO `sys_role_menu` VALUES ('71', '6', '16');
INSERT INTO `sys_role_menu` VALUES ('72', '6', '4');
INSERT INTO `sys_role_menu` VALUES ('73', '6', '17');
INSERT INTO `sys_role_menu` VALUES ('74', '6', '18');
INSERT INTO `sys_role_menu` VALUES ('75', '6', '19');
INSERT INTO `sys_role_menu` VALUES ('76', '6', '5');
INSERT INTO `sys_role_menu` VALUES ('77', '6', '6');
INSERT INTO `sys_role_menu` VALUES ('113', '3', '1');
INSERT INTO `sys_role_menu` VALUES ('114', '3', '2');
INSERT INTO `sys_role_menu` VALUES ('115', '3', '3');
INSERT INTO `sys_role_menu` VALUES ('116', '3', '4');
INSERT INTO `sys_role_menu` VALUES ('117', '3', '5');
INSERT INTO `sys_role_menu` VALUES ('118', '3', '6');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `city` varchar(64) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `statu` int(5) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_USERNAME` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '$2a$10$YfdTgsQKig0GinG3HrBZl.F4k91TS5A4WAuRkFRu1WeCUPnpEJ8Mu', 'https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg', '123@qq.com', '广州', '2021-01-12 22:13:53', '2021-12-23 17:19:20', '2020-12-30 08:38:37', '1');
INSERT INTO `sys_user` VALUES ('2', 'test', '$2a$10$0ilP4ZD1kLugYwLCs4pmb.ZT9cFqzOZTNaMiHxrBnVIQUGUwEvBIO', 'https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg', 'test@qq.com', null, '2021-01-30 08:20:22', '2021-01-30 08:55:57', null, '1');
INSERT INTO `sys_user` VALUES ('3', 'haifei', '$2a$10$iErj1na0O2XljBw3p8NKx.p5S3a95nEqlluAXd1O6W4oFxMHTRPKq', 'https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg', 'haifei@qq.com', 'qindgao', '2021-01-30 08:20:22', '2021-12-23 17:46:26', null, '1');
INSERT INTO `sys_user` VALUES ('5', 'one', '$2a$10$/HXMb.H8ud4a226cHCUNjuSAFdZrQSiOW6XcqVOeKQ5JNKPLfGsKS', 'https://cdn.jsdelivr.net/gh/yub4by/picx-image-hosting@master/20211115/微信图片_20211115210456.2mnk5n8q0vc0.jpg', 'one@qq.com', null, '2021-12-23 17:06:41', '2021-12-23 17:57:07', null, '1');
INSERT INTO `sys_user` VALUES ('6', '2', '$2a$10$XfQ8lcHBAVKL47A8F5WP5eN2ADeocZZzCp3enAngUa9CCF3WAkJN2', 'https://cdn.jsdelivr.net/gh/yub4by/picx-image-hosting@master/20211115/微信图片_20211115210456.2mnk5n8q0vc0.jpg', '2@11.com', null, '2021-12-23 17:06:55', null, null, '0');
INSERT INTO `sys_user` VALUES ('7', '3', '$2a$10$m8VZNoZI8BmR8Yc3YrZUhOr2txry4S7xCakWgUxQj8pMHG6Z2OYkW', 'https://cdn.jsdelivr.net/gh/yub4by/picx-image-hosting@master/20211115/微信图片_20211115210456.2mnk5n8q0vc0.jpg', '3@qq.com', null, '2021-12-23 17:07:28', null, null, '0');
INSERT INTO `sys_user` VALUES ('8', '4', '$2a$10$s8YVuz5i0lJGvMpbt/125eeyFKEc8WeT171ZJT7.WefBZhEIQgJJm', 'https://cdn.jsdelivr.net/gh/yub4by/picx-image-hosting@master/20211115/微信图片_20211115210456.2mnk5n8q0vc0.jpg', '4@qq.com', null, '2021-12-23 17:07:36', null, null, '0');
INSERT INTO `sys_user` VALUES ('9', '5', '$2a$10$cgI2lejHSt.sz5Wg2TFkW.jEpNDMitZcNUxniPXzoXbzxYsbs0UM.', 'https://cdn.jsdelivr.net/gh/yub4by/picx-image-hosting@master/20211115/微信图片_20211115210456.2mnk5n8q0vc0.jpg', '5@qq.com', null, '2021-12-23 17:07:46', null, null, '0');
INSERT INTO `sys_user` VALUES ('10', '6', '$2a$10$H8YUbFdHabpRYnEkVlVs5OsCxnyE17GfkCbdQt7BKEJ3rpAUBod26', 'https://cdn.jsdelivr.net/gh/yub4by/picx-image-hosting@master/20211115/微信图片_20211115210456.2mnk5n8q0vc0.jpg', '6@qq.com', null, '2021-12-23 17:07:56', null, null, '0');
INSERT INTO `sys_user` VALUES ('11', '7', '$2a$10$gFrZPNIYUkWeVjnKWCgr4.uD38ESSykAONrZGj8FzUIPPsORvyt.2', 'https://cdn.jsdelivr.net/gh/yub4by/picx-image-hosting@master/20211115/微信图片_20211115210456.2mnk5n8q0vc0.jpg', '7@qq.com', null, '2021-12-23 17:08:01', null, null, '0');
INSERT INTO `sys_user` VALUES ('12', '8', '$2a$10$V4q/XSBCooPb7yizbtbS0.d9oFVBMheucAFQkU5LXNiSgDsAbTOEC', 'https://cdn.jsdelivr.net/gh/yub4by/picx-image-hosting@master/20211115/微信图片_20211115210456.2mnk5n8q0vc0.jpg', '8@qq.com', null, '2021-12-23 17:08:06', null, null, '0');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('4', '1', '6');
INSERT INTO `sys_user_role` VALUES ('7', '1', '3');
INSERT INTO `sys_user_role` VALUES ('13', '2', '3');
INSERT INTO `sys_user_role` VALUES ('17', '6', '3');
INSERT INTO `sys_user_role` VALUES ('19', '5', '3');
INSERT INTO `sys_user_role` VALUES ('20', '5', '6');
INSERT INTO `sys_user_role` VALUES ('21', '3', '11');

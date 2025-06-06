-- Create database if it doesn't exist (optional, usually managed outside app)
-- CREATE DATABASE IF NOT EXISTS smart_gun_check_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE smart_gun_check_system;

-- ----------------------------
-- Table structure for sys_unit (单位表)
-- ----------------------------
DROP TABLE IF EXISTS `sys_unit`;
CREATE TABLE `sys_unit` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '单位ID',
  `unit_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '单位名称',
  `parent_id` bigint DEFAULT NULL COMMENT '上级单位ID (null for top-level)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_sys_unit_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统单位表';

-- ----------------------------
-- Table structure for sys_role (角色表)
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称 (e.g., 市局管理员)',
  `role_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色权限字符串 (e.g., admin, sub_admin, user)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_role_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';

-- ----------------------------
-- Table structure for sys_user (用户表)
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码 (hashed)',
  `unit_id` bigint DEFAULT NULL COMMENT '所属单位ID',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '账号是否启用 (1=启用, 0=禁用)',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_user_username` (`username`),
  KEY `idx_sys_user_unit_id` (`unit_id`),
  CONSTRAINT `fk_sys_user_unit_id` FOREIGN KEY (`unit_id`) REFERENCES `sys_unit` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- ----------------------------
-- Table structure for sys_user_role (用户角色关联表)
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `idx_sys_user_role_role_id` (`role_id`),
  CONSTRAINT `fk_sys_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_sys_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- ----------------------------
-- Table structure for gun_check_record (验枪记录表)
-- ----------------------------
DROP TABLE IF EXISTS `gun_check_record`;
CREATE TABLE `gun_check_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `gun_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '枪支编号',
  `police_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '民警编号 (关联用户表中的某个字段, 或独立警员ID)',
  `check_time` datetime NOT NULL COMMENT '验枪时间',
  `photo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '现场照片URL',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '验枪状态 (e.g., NORMAL, ABNORMAL)',
  `unit_id` bigint NOT NULL COMMENT '验枪单位ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_gcr_unit_id` (`unit_id`),
  KEY `idx_gcr_police_id` (`police_id`),
  KEY `idx_gcr_check_time` (`check_time`),
  KEY `idx_gcr_gun_id` (`gun_id`),
  CONSTRAINT `fk_gcr_unit_id` FOREIGN KEY (`unit_id`) REFERENCES `sys_unit` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='验枪记录表';

-- ----------------------------
-- Table structure for alarm_log (预警记录表)
-- ----------------------------
DROP TABLE IF EXISTS `alarm_log`;
CREATE TABLE `alarm_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '预警ID',
  `unit_id` bigint NOT NULL COMMENT '预警单位ID',
  `alarm_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '预警类型 (e.g., UNCHECKED, OVERTIME_CHECK)',
  `alarm_time` datetime NOT NULL COMMENT '预警发生时间',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '预警备注信息',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_al_unit_id` (`unit_id`),
  KEY `idx_al_alarm_time` (`alarm_time`),
  KEY `idx_al_alarm_type` (`alarm_type`),
  CONSTRAINT `fk_al_unit_id` FOREIGN KEY (`unit_id`) REFERENCES `sys_unit` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预警记录表';

-- ----------------------------
-- Sample Data for sys_role
-- ----------------------------
INSERT INTO `sys_role` (`role_name`, `role_key`, `create_time`, `update_time`) VALUES
('市局管理员', 'ADMIN', NOW(), NOW()),
('分局管理员', 'SUB_ADMIN', NOW(), NOW()),
('派出所人员', 'USER', NOW(), NOW());

-- ----------------------------
-- Sample Data for sys_unit (Example)
-- ----------------------------
INSERT INTO `sys_unit` (`unit_name`, `parent_id`, `create_time`, `update_time`) VALUES
('市公安局', NULL, NOW(), NOW()),
('第一分局', 1, NOW(), NOW()),
('第二分局', 1, NOW(), NOW()),
('城东派出所', 2, NOW(), NOW()),
('西城派出所', 2, NOW(), NOW());

-- ----------------------------
-- Sample Data for sys_user (Example - passwords are 'password' hashed with BCrypt)
-- Replace with actual secure password generation and hashing for production
-- $2a$10$N9qo8uLOickGWJean2060.bayv6ORs2nQ2WkYcnYNCd5N2uLIdcmq  (raw: password)
-- ----------------------------
INSERT INTO `sys_user` (`username`, `password`, `unit_id`, `enabled`, `real_name`, `create_time`, `update_time`) VALUES
('admin', '$2a$10$N9qo8uLOickGWJean2060.bayv6ORs2nQ2WkYcnYNCd5N2uLIdcmq', 1, 1, '市局管理员张三', NOW(), NOW()),
('subadmin1', '$2a$10$N9qo8uLOickGWJean2060.bayv6ORs2nQ2WkYcnYNCd5N2uLIdcmq', 2, 1, '分局管理员李四', NOW(), NOW()),
('user1', '$2a$10$N9qo8uLOickGWJean2060.bayv6ORs2nQ2WkYcnYNCd5N2uLIdcmq', 4, 1, '民警王五', NOW(), NOW());

-- ----------------------------
-- Sample Data for sys_user_role (Example)
-- ----------------------------
-- Assuming user IDs are 1, 2, 3 and role IDs are 1 (ADMIN), 2 (SUB_ADMIN), 3 (USER) based on insertion order
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1), -- admin is ADMIN
(2, 2), -- subadmin1 is SUB_ADMIN
(3, 3); -- user1 is USER

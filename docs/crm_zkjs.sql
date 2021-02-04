/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : localhost:3306
 Source Schema         : crm_zkjs

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 22/01/2021 14:08:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for 72crm_admin_attention
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_attention`;
CREATE TABLE `72crm_admin_attention`  (
  `attention_id` int(11) NOT NULL AUTO_INCREMENT,
  `be_user_id` bigint(20) NOT NULL COMMENT '被关注人',
  `attention_user_id` bigint(20) NOT NULL COMMENT '关注人',
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '企业ID',
  PRIMARY KEY (`attention_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通讯录用户关注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_admin_config
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_config`;
CREATE TABLE `72crm_admin_config`  (
  `setting_id` int(9) NOT NULL AUTO_INCREMENT,
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '状态，0:不启用 1 ： 启用',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设置名称',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '值',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`setting_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 426784 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '客户规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_config
-- ----------------------------
INSERT INTO `72crm_admin_config` VALUES (306681, 1, 'expiringContractDays', '3', '合同到期提醒');
INSERT INTO `72crm_admin_config` VALUES (306682, 1, 'putInPoolRemindDays', '2', NULL);
INSERT INTO `72crm_admin_config` VALUES (306683, 1, 'taskExamine', '1', '任务审批');
INSERT INTO `72crm_admin_config` VALUES (306684, 1, 'log', '1', '日志');
INSERT INTO `72crm_admin_config` VALUES (306685, 1, 'book', '1', '通讯录');
INSERT INTO `72crm_admin_config` VALUES (306686, 1, 'crm', '1', '客户管理');
INSERT INTO `72crm_admin_config` VALUES (306687, 1, 'project', '1', '项目管理');
INSERT INTO `72crm_admin_config` VALUES (306688, 1, 'calendar', '1', '日历');
INSERT INTO `72crm_admin_config` VALUES (306689, 0, 'hrm', '2', '人力资源管理');
INSERT INTO `72crm_admin_config` VALUES (306690, 0, 'jxc', '2', '进销存管理');
INSERT INTO `72crm_admin_config` VALUES (306691, 0, 'call', '3', '呼叫中心');
INSERT INTO `72crm_admin_config` VALUES (306692, 0, 'followRecordOption', '打电话', '跟进记录选项');
INSERT INTO `72crm_admin_config` VALUES (306693, 0, 'followRecordOption', '发邮件', '跟进记录选项');
INSERT INTO `72crm_admin_config` VALUES (306694, 0, 'followRecordOption', '发短信', '跟进记录选项');
INSERT INTO `72crm_admin_config` VALUES (306695, 0, 'followRecordOption', '见面拜访', '跟进记录选项');
INSERT INTO `72crm_admin_config` VALUES (306696, 0, 'followRecordOption', '活动', '跟进记录选项');
INSERT INTO `72crm_admin_config` VALUES (306697, 0, 'logWelcomeSpeech', '蓝天是宁静的，空气是清新的，阳光是明媚的', '默认日志欢迎语');
INSERT INTO `72crm_admin_config` VALUES (306698, 0, 'logWelcomeSpeech', '人生，最快乐的莫过于奋斗', '默认日志欢迎语');
INSERT INTO `72crm_admin_config` VALUES (306699, 0, 'logWelcomeSpeech', '工作一天辛苦了，人生，最快乐的莫过于奋斗', '默认日志欢迎语');
INSERT INTO `72crm_admin_config` VALUES (306700, 0, 'pictureSetting', NULL, '外勤签到照片上传设置');
INSERT INTO `72crm_admin_config` VALUES (337867, 0, 'email', '1', '邮箱');
INSERT INTO `72crm_admin_config` VALUES (337880, 0, 'customerPoolSettingDealDays', '3', NULL);
INSERT INTO `72crm_admin_config` VALUES (337881, 0, 'customerPoolSettingFollowupDays', '7', NULL);
INSERT INTO `72crm_admin_config` VALUES (337882, 0, 'customerPoolSetting', NULL, NULL);
INSERT INTO `72crm_admin_config` VALUES (361241, 0, 'returnVisitRemindConfig', '7', '客户回访提醒设置');
INSERT INTO `72crm_admin_config` VALUES (377624, 1, 'numberSetting', '6', '自动编号设置');
INSERT INTO `72crm_admin_config` VALUES (394007, 1, 'numberSetting', '7', '自动编号设置');
INSERT INTO `72crm_admin_config` VALUES (410390, 0, 'numberSetting', '17', '自动编号设置');
INSERT INTO `72crm_admin_config` VALUES (426773, 0, 'numberSetting', '18', '自动编号设置');
INSERT INTO `72crm_admin_config` VALUES (426776, 1, 'numberSetting', '19', '自动编号设置');
INSERT INTO `72crm_admin_config` VALUES (426777, 1, 'numberSetting', '20', '自动编号设置');
INSERT INTO `72crm_admin_config` VALUES (426778, 1, 'numberSetting', '21', '自动编号设置');
INSERT INTO `72crm_admin_config` VALUES (426780, 1, 'numberSetting', '23', '自动编号设置');
INSERT INTO `72crm_admin_config` VALUES (426783, 1, 'SYS_CONFIG', '{\"name\":\"中科金审\",\"logo\":\"/file/downFile?fileId=d9ecff7c43da4afeb417d2e6388fad63\"}', '系统LOGO配置');

-- ----------------------------
-- Table structure for 72crm_admin_dept
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_dept`;
CREATE TABLE `72crm_admin_dept`  (
  `dept_id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NULL DEFAULT 0 COMMENT '父级ID 顶级部门为0',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门名称',
  `num` int(4) NULL DEFAULT NULL COMMENT '排序 越大越靠后',
  `remark` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门备注',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16757 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_dept
-- ----------------------------
INSERT INTO `72crm_admin_dept` VALUES (16671, 0, '全公司', 1, '');
INSERT INTO `72crm_admin_dept` VALUES (16756, 16671, '测试', NULL, '');

-- ----------------------------
-- Table structure for 72crm_admin_examine
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_examine`;
CREATE TABLE `72crm_admin_examine`  (
  `examine_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_type` int(1) NOT NULL DEFAULT 1 COMMENT '1 合同 2 回款 4工时 5项目',
  `examine_type` int(1) NULL DEFAULT NULL COMMENT '审核类型 1 固定审批 2 授权审批',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批流名称',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `dept_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '部门ID（0为全部）',
  `user_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '员工ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `update_user_id` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态 1 启用 0 禁用 2 删除',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程说明',
  PRIMARY KEY (`examine_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27993 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审批流程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_examine
-- ----------------------------
INSERT INTO `72crm_admin_examine` VALUES (27963, 2, 2, '回款审批流程', NULL, NULL, NULL, NULL, 3, '2020-10-15 10:29:22', 16360, 1, '');
INSERT INTO `72crm_admin_examine` VALUES (27964, 1, 2, '合同审批流程', NULL, NULL, NULL, NULL, 3, '2020-10-15 10:28:25', 16360, 2, '说明');
INSERT INTO `72crm_admin_examine` VALUES (27966, 4, 1, '工时审批流程', NULL, NULL, ',16362,', NULL, 16362, '2020-10-16 15:54:30', 16360, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27967, 5, 2, '结项审批流程', NULL, NULL, NULL, '2020-09-28 11:52:17', 16362, '2020-10-15 15:03:56', 16362, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27968, 1, 2, '141', NULL, NULL, NULL, '2020-10-15 10:26:56', 16360, '2020-10-15 10:28:27', 16360, 2, '14141');
INSERT INTO `72crm_admin_examine` VALUES (27969, 1, 1, '7777', NULL, NULL, ',16360,', '2020-10-15 10:27:29', 16360, '2020-10-15 10:28:29', 16360, 2, '4747');
INSERT INTO `72crm_admin_examine` VALUES (27970, 2, 1, '8888', NULL, NULL, NULL, '2020-10-15 10:28:57', 16360, '2020-11-20 09:48:36', 16371, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27971, 4, 1, '14141', NULL, NULL, ',16360,', '2020-10-15 10:33:42', 16360, '2020-11-20 09:48:33', 16371, 2, '1414141');
INSERT INTO `72crm_admin_examine` VALUES (27972, 4, 1, '141', NULL, NULL, NULL, '2020-10-15 10:33:54', 16360, '2020-11-20 09:48:31', 16371, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27973, 4, 1, '77777777777777777', NULL, NULL, NULL, '2020-10-15 10:34:03', 16360, '2020-11-20 09:48:28', 16371, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27974, 5, 1, '结项审批流程', NULL, NULL, NULL, '2020-09-28 11:52:17', 16362, '2020-10-15 15:19:17', 16362, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27975, 5, 1, '结项审批流程', NULL, NULL, NULL, '2020-09-28 11:52:17', 16362, '2020-10-23 13:58:12', 16362, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27976, 1, 1, 'sdds', NULL, NULL, NULL, '2020-10-15 17:34:02', 16360, '2020-11-07 10:02:15', 16360, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27977, 1, 2, '141', NULL, NULL, NULL, '2020-10-15 17:41:11', 16360, '2020-11-20 09:48:21', 16371, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27978, 1, 1, '141', NULL, NULL, NULL, '2020-10-15 17:41:32', 16360, '2020-10-15 17:43:20', 16360, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27979, 1, 1, '141', NULL, NULL, NULL, '2020-10-15 17:41:32', 16360, '2020-11-20 09:48:18', 16371, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27980, 4, 2, '工时审批流程', NULL, NULL, ',16360,16362,16363,16364,16365,', NULL, 16362, '2020-10-23 14:05:04', 16360, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27981, 5, 1, '结项审批流程', NULL, NULL, NULL, '2020-09-28 11:52:17', 16362, '2020-10-23 14:05:31', 16360, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27982, 4, 2, '工时审批流程', NULL, NULL, ',null,null,null,null,null,', NULL, 16362, '2020-12-30 18:14:39', 16360, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27983, 5, 2, '结项审批流程', NULL, NULL, NULL, '2020-09-28 11:52:17', 16362, '2020-10-23 14:12:28', 16360, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27984, 5, 1, '结项审批流程', NULL, NULL, NULL, '2020-09-28 11:52:17', 16362, '2020-10-23 14:21:32', 16360, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27985, 5, 1, '结项审批流程', NULL, NULL, NULL, '2020-09-28 11:52:17', 16362, '2020-10-23 14:22:59', 16360, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27986, 5, 2, '结项审批流程', NULL, NULL, NULL, '2020-09-28 11:52:17', 16362, '2021-01-05 18:13:09', 16392, 0, '');
INSERT INTO `72crm_admin_examine` VALUES (27987, 1, 1, '141', NULL, NULL, NULL, '2020-10-23 17:27:29', 16360, '2020-11-20 09:48:42', 16371, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27988, 1, 2, 'sdds', NULL, NULL, NULL, '2020-10-15 17:34:02', 16360, '2020-11-20 09:48:39', 16371, 2, '');
INSERT INTO `72crm_admin_examine` VALUES (27989, 3, 2, '发票审批流程', NULL, ',16671,', NULL, '2020-12-19 09:53:04', 16389, '2020-12-19 09:53:04', 16389, 1, '');
INSERT INTO `72crm_admin_examine` VALUES (27990, 4, 2, '工时审批流程', NULL, NULL, NULL, NULL, 16362, '2020-12-30 18:14:40', 16389, 1, '');
INSERT INTO `72crm_admin_examine` VALUES (27991, 1, 2, '合同审批流程', NULL, NULL, NULL, '2021-01-05 14:57:18', 16389, '2021-01-05 14:57:18', 16389, 1, '');
INSERT INTO `72crm_admin_examine` VALUES (27992, 5, 2, '项目', NULL, NULL, ',16360,16388,16389,16390,16391,16392,16393,16394,16395,16396,16397,16398,16399,16400,16401,16402,16403,16404,16405,16406,16407,16408,16409,16410,16411,', '2021-01-05 18:13:09', 16392, '2021-01-05 18:13:09', 16392, 1, '1111');

-- ----------------------------
-- Table structure for 72crm_admin_examine_log
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_examine_log`;
CREATE TABLE `72crm_admin_examine_log`  (
  `log_id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `record_id` int(11) NULL DEFAULT NULL COMMENT '审批记录ID',
  `examine_step_id` bigint(20) NULL DEFAULT NULL COMMENT '审核步骤ID',
  `examine_status` int(1) NULL DEFAULT NULL COMMENT '审核状态 0 未审核 1 审核通过 2 审核拒绝3 撤回审核',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `examine_user` bigint(20) NULL DEFAULT NULL COMMENT '审核人',
  `examine_time` datetime(0) NULL DEFAULT NULL COMMENT '审核时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `is_recheck` int(1) NULL DEFAULT 0 COMMENT '是否是撤回之前的日志 0或者null为新数据 1：撤回之前的数据',
  `order_id` int(30) NULL DEFAULT NULL,
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 341 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审核日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_admin_examine_record
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_examine_record`;
CREATE TABLE `72crm_admin_examine_record`  (
  `record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '审核记录ID',
  `examine_id` int(11) NULL DEFAULT NULL COMMENT '审批ID',
  `examine_step_id` bigint(20) NULL DEFAULT NULL COMMENT '当前进行的审批步骤ID',
  `examine_status` int(1) NULL DEFAULT NULL COMMENT '审核状态 0 未审核 1 审核通过 2 审核拒绝 3 审核中 4 已撤回',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 317 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审核记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_admin_examine_step
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_examine_step`;
CREATE TABLE `72crm_admin_examine_step`  (
  `step_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `step_type` int(1) NULL DEFAULT NULL COMMENT '步骤类型1、负责人主管，2、指定用户（任意一人），3、指定用户（多人会签）,4、上一级审批人主管',
  `examine_id` int(11) NOT NULL COMMENT '审批ID',
  `check_user_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批人ID (使用逗号隔开) ,1,2,',
  `step_num` int(2) NULL DEFAULT 1 COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`step_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审批步骤表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_admin_field
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_field`;
CREATE TABLE `72crm_admin_field`  (
  `field_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义字段英文标识',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字段名称',
  `type` int(2) NOT NULL DEFAULT 1 COMMENT '字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划',
  `label` int(2) NOT NULL COMMENT '标签 1 线索 2 项目 3 里程碑 4 任务 5 风险管理 6 合同 7回款8.回款计划',
  `remark` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段说明',
  `input_tips` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '输入提示',
  `max_length` int(12) NULL DEFAULT NULL COMMENT '最大长度',
  `default_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '默认值',
  `is_unique` int(1) NULL DEFAULT 0 COMMENT '是否唯一 1 是 0 否',
  `is_null` int(1) NULL DEFAULT 0 COMMENT '是否必填 1 是 0 否',
  `sorting` int(5) NULL DEFAULT 1 COMMENT '排序 从小到大',
  `options` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '如果类型是选项，此处不能为空，多个选项以，隔开',
  `operating` int(1) NULL DEFAULT 0 COMMENT '是否可以删除修改 0 改删 1 改 2 删 3 无',
  `is_hidden` int(1) NOT NULL DEFAULT 0 COMMENT '是否隐藏  0不隐藏 1隐藏',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后修改时间',
  `examine_category_id` int(11) NULL DEFAULT NULL COMMENT '审批ID label为10需要',
  `field_type` int(2) NOT NULL DEFAULT 0 COMMENT '字段来源  0.自定义 1.原始固定 2原始字段但值存在扩展表中',
  `relevant` int(11) NULL DEFAULT NULL COMMENT '只有线索需要，转换客户的自定义字段ID',
  PRIMARY KEY (`field_id`) USING BTREE,
  INDEX `label`(`label`) USING BTREE,
  INDEX `update_time`(`update_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1230027 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '自定义字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_field
-- ----------------------------
INSERT INTO `72crm_admin_field` VALUES (1229736, 'customer_name', '客户名称', 1, 2, NULL, NULL, 255, '', 1, 1, 0, NULL, 1, 0, '2020-12-17 10:53:51', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229737, '客户来源', '客户来源', 3, 2, NULL, NULL, NULL, '', 0, 0, 1, '促销,搜索引擎,广告,转介绍,线上注册,线上询价,预约上门,陌拜,电话咨询,邮件咨询', 1, 0, '2020-12-17 10:53:51', NULL, 2, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229738, 'mobile', '手机', 7, 2, NULL, NULL, 255, '', 1, 0, 2, NULL, 1, 1, '2020-12-17 10:53:51', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229739, 'telephone', '电话', 1, 2, NULL, NULL, 255, '', 0, 0, 3, NULL, 1, 0, '2020-12-17 10:53:51', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229740, 'website', '网址', 1, 2, NULL, NULL, 255, '', 0, 0, 4, NULL, 1, 0, '2020-12-17 10:53:51', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229741, '客户行业', '客户行业', 3, 2, NULL, NULL, NULL, '', 0, 0, 5, '经侦,税务,监察委,海关,禁毒,刑侦,反恐,其它', 1, 0, '2020-12-17 10:53:51', NULL, 2, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229742, '客户级别', '客户级别', 3, 2, NULL, NULL, NULL, '', 0, 0, 6, 'A 已落单客户,B 成功率99%,C 成功率75%,D 成功率50%', 1, 0, '2020-12-17 10:53:51', NULL, 2, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229743, 'next_time', '下次联系时间', 13, 2, NULL, NULL, NULL, '', 0, 0, 7, NULL, 1, 0, '2020-12-17 10:53:51', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229744, 'remark', '备注', 2, 2, NULL, NULL, 255, '', 0, 0, 8, NULL, 1, 0, '2020-12-17 10:53:51', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229745, 'leads_name', '线索名称', 1, 1, NULL, NULL, 255, '', 0, 1, 1, NULL, 1, 0, '2020-12-17 14:25:23', NULL, 1, 1229736);
INSERT INTO `72crm_admin_field` VALUES (1229746, '线索来源', '线索来源', 3, 1, NULL, NULL, NULL, '', 0, 0, 2, '促销,搜索引擎,广告,转介绍,线上注册,线上询价,预约上门,陌拜,电话咨询,邮件咨询', 1, 0, '2020-12-17 14:25:23', NULL, 2, 1229737);
INSERT INTO `72crm_admin_field` VALUES (1229747, 'mobile', '手机', 7, 1, NULL, NULL, 255, '', 0, 0, 3, NULL, 1, 0, '2020-12-17 14:25:23', NULL, 1, 1229738);
INSERT INTO `72crm_admin_field` VALUES (1229748, 'telephone', '电话', 1, 1, NULL, NULL, 255, '', 0, 0, 4, NULL, 1, 0, '2020-12-17 14:25:23', NULL, 1, 1229739);
INSERT INTO `72crm_admin_field` VALUES (1229749, 'address', '地址', 1, 1, NULL, NULL, 255, '', 0, 0, 5, NULL, 1, 0, '2020-12-17 14:25:23', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229750, '客户行业', '客户行业', 3, 1, NULL, NULL, NULL, '', 0, 0, 6, 'IT,金融业,房地产,商业服务,运输/物流,生产,政府,文化传媒', 1, 0, '2020-12-17 14:25:23', NULL, 2, 1229741);
INSERT INTO `72crm_admin_field` VALUES (1229751, '客户级别', '客户级别', 3, 1, NULL, NULL, NULL, '', 0, 0, 7, 'A（重点客户）,B（普通客户）,C（非优先客户）', 1, 0, '2020-12-17 14:25:23', NULL, 2, 1229742);
INSERT INTO `72crm_admin_field` VALUES (1229752, 'next_time', '下次联系时间', 13, 1, NULL, NULL, NULL, '', 0, 0, 8, NULL, 1, 0, '2020-12-17 14:25:23', NULL, 1, 1229743);
INSERT INTO `72crm_admin_field` VALUES (1229753, 'remark', '备注', 2, 1, NULL, NULL, 255, '', 0, 0, 9, NULL, 1, 0, '2020-12-17 14:25:23', NULL, 1, 1229744);
INSERT INTO `72crm_admin_field` VALUES (1229754, 'name', '姓名', 1, 3, NULL, NULL, 255, '', 0, 1, 1, NULL, 1, 0, '2020-12-17 11:05:48', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229755, 'customer_id', '客户名称', 15, 3, NULL, NULL, NULL, '', 0, 1, 0, NULL, 1, 0, '2020-12-17 11:05:48', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229756, 'mobile', '手机', 7, 3, NULL, NULL, 255, '', 1, 1, 4, NULL, 1, 0, '2020-12-17 11:05:48', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229757, 'telephone', '电话', 1, 3, NULL, NULL, 255, '', 0, 0, 5, NULL, 1, 0, '2020-12-17 11:05:48', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229758, 'email', '邮箱', 14, 3, NULL, NULL, 255, '', 0, 0, 6, NULL, 1, 0, '2020-12-17 11:05:48', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229759, 'post', '职务', 1, 3, NULL, NULL, 255, '', 0, 0, 7, NULL, 1, 0, '2020-12-17 11:05:48', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229760, '是否关键决策人', '是否关键决策人', 3, 3, NULL, NULL, NULL, '', 0, 0, 8, '是,否', 1, 0, '2020-12-17 11:05:48', NULL, 2, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229761, 'address', '地址', 1, 3, NULL, NULL, 255, '', 0, 0, 9, NULL, 1, 0, '2020-12-17 11:05:48', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229762, 'next_time', '下次联系时间', 13, 3, NULL, NULL, NULL, '', 0, 0, 10, NULL, 1, 0, '2020-12-17 11:05:48', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229763, 'remark', '备注', 2, 3, NULL, NULL, 255, '', 0, 0, 11, NULL, 1, 0, '2020-12-17 11:05:48', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229764, '性别', '性别', 3, 3, NULL, NULL, NULL, '', 0, 1, 2, '男,女', 1, 0, '2020-12-17 11:05:48', NULL, 2, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229765, 'name', '产品名称', 1, 4, NULL, NULL, 255, '', 0, 0, 0, NULL, 1, 0, '2020-12-17 15:42:32', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229766, 'category_id', '产品类型', 19, 4, NULL, NULL, NULL, '', 0, 1, 1, NULL, 1, 0, '2020-12-17 15:42:32', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229767, '单位', '单位', 3, 4, NULL, NULL, NULL, '', 0, 1, 2, '次,年,套,份', 1, 0, '2020-12-17 15:42:32', NULL, 2, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229768, 'num', '产品编码', 5, 4, NULL, NULL, 255, '', 0, 0, 3, NULL, 1, 1, '2020-12-17 15:42:32', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229769, 'price', '价格', 6, 4, NULL, NULL, 255, '', 0, 1, 4, NULL, 1, 0, '2020-12-17 15:42:32', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229770, '是否上下架', '是否上下架', 3, 4, NULL, NULL, NULL, '', 0, 1, 5, '上架,下架', 3, 0, '2020-12-17 15:42:32', NULL, 2, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229771, 'description', '销售产品描述', 1, 4, NULL, NULL, 255, '', 0, 0, 6, NULL, 1, 0, '2020-12-17 15:42:32', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229772, 'business_name', '商机名称', 1, 5, NULL, NULL, 255, '', 0, 1, 2, NULL, 1, 0, '2020-12-17 14:27:06', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229773, 'customer_id', '客户名称', 15, 5, NULL, NULL, NULL, '', 0, 1, 3, NULL, 1, 0, '2020-12-17 14:27:06', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229774, 'money', '商机合同金额', 6, 5, NULL, NULL, 255, '', 0, 1, 4, NULL, 1, 0, '2020-12-17 14:27:06', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229775, 'deal_date', '预计成交日期', 13, 5, NULL, NULL, NULL, '', 0, 0, 6, NULL, 1, 0, '2020-12-17 14:27:06', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229776, 'remark', '客户商机需求', 2, 5, NULL, NULL, 255, '', 0, 0, 13, NULL, 1, 0, '2020-12-17 14:27:06', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229777, 'name', '合同甲方（盖章名称）', 1, 6, NULL, NULL, 255, '', 0, 1, 3, NULL, 1, 0, '2020-12-17 13:31:16', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229778, 'num', '合同编号', 1, 6, NULL, NULL, 255, '', 1, 1, 1, NULL, 1, 0, '2020-12-17 13:31:16', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229779, 'customer_id', '客户名称', 15, 6, NULL, NULL, NULL, '', 0, 1, 2, NULL, 1, 0, '2020-12-17 13:31:16', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229780, 'business_id', '商机名称', 16, 6, NULL, NULL, NULL, '', 0, 1, 4, NULL, 1, 0, '2020-12-17 13:31:16', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229781, 'money', '合同金额', 6, 6, NULL, NULL, 255, '', 0, 1, 7, NULL, 1, 0, '2020-12-17 13:31:16', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229782, 'order_date', '签约时间', 4, 6, NULL, NULL, NULL, '', 0, 1, 9, NULL, 1, 0, '2020-12-17 13:31:16', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229783, 'start_time', '合同开始时间', 4, 6, NULL, NULL, NULL, '', 0, 0, 11, NULL, 1, 1, '2020-12-17 13:31:16', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229784, 'end_time', '合同结束时间', 4, 6, NULL, NULL, NULL, '', 0, 0, 12, NULL, 1, 0, '2020-12-17 13:31:16', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229785, 'contacts_id', '客户签约人', 17, 6, NULL, NULL, NULL, '', 0, 0, 13, NULL, 1, 1, '2020-12-17 13:31:16', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229786, 'company_user_id', '销售签约人', 10, 6, NULL, NULL, NULL, '', 0, 1, 10, NULL, 1, 0, '2020-12-17 13:31:16', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229787, 'remark', '备注', 2, 6, NULL, NULL, 255, '', 0, 0, 14, NULL, 1, 0, '2020-12-17 13:31:16', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229788, '合同类型', '合同类型', 3, 6, NULL, NULL, 255, '', 0, 0, 6, '项目类,产品类,服务类,渠道合作协议,FM类合同,培训类', 0, 0, '2020-12-17 13:31:16', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229789, 'number', '回款编号', 1, 7, NULL, NULL, 255, '', 1, 1, 0, NULL, 1, 0, '2020-03-31 16:34:21', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229790, 'customer_id', '客户名称', 15, 7, NULL, NULL, NULL, '', 0, 1, 1, NULL, 1, 0, '2020-03-31 16:34:21', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229791, 'contract_id', '合同编号', 20, 7, NULL, NULL, NULL, '', 0, 1, 2, NULL, 1, 0, '2020-03-31 16:34:21', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229792, 'plan_id', '期数', 21, 7, NULL, NULL, NULL, '', 0, 0, 3, NULL, 1, 0, '2020-03-31 16:34:21', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229793, 'return_time', '回款日期', 4, 7, NULL, NULL, NULL, '', 0, 1, 4, NULL, 1, 0, '2020-03-31 16:34:21', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229794, 'money', '回款金额', 6, 7, NULL, NULL, 255, '', 0, 1, 5, NULL, 1, 0, '2020-03-31 16:34:21', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229795, 'return_type', '回款方式', 3, 7, NULL, NULL, NULL, '', 0, 0, 6, '支票,现金,邮政汇款,电汇,网上转账,支付宝,微信支付,其他,选9', 1, 0, '2020-03-31 16:34:21', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229796, 'remark', '备注', 2, 7, NULL, NULL, 255, '', 0, 0, 7, NULL, 1, 0, '2020-03-31 16:34:21', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229797, 'customer_id', '客户名称', 15, 8, NULL, NULL, NULL, '', 0, 0, 1, NULL, 1, 0, NULL, NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229798, 'contract_id', '合同编号', 20, 8, NULL, NULL, 11, '', 0, 0, 2, NULL, 1, 0, NULL, NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229799, 'money', '计划回款金额', 6, 8, NULL, NULL, NULL, '', 0, 0, 3, NULL, 1, 0, NULL, NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229800, 'return_date', '计划回款日期', 4, 8, NULL, NULL, NULL, '', 0, 0, 4, NULL, 1, 0, NULL, NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229801, 'remind', '提前几天提醒', 5, 8, NULL, NULL, 11, '', 0, 0, 5, NULL, 1, 0, NULL, NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229802, 'remark', '备注', 2, 8, NULL, NULL, 1000, '', 0, 0, 6, NULL, 1, 0, NULL, NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229803, 'content', '审批内容', 1, 10, NULL, '', NULL, '', 0, 1, 0, NULL, 3, 0, NULL, 81327, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229804, 'remark', '备注', 2, 10, NULL, '', 1000, '', 0, 0, 1, NULL, 3, 0, NULL, 81327, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229805, 'type_id', '请假类型', 3, 10, NULL, '', NULL, '年假', 0, 1, 0, '年假,事假,病假,产假,调休,婚假,丧假,其他', 3, 0, NULL, 81328, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229806, 'content', '审批内容', 1, 10, NULL, '', NULL, '', 0, 1, 1, NULL, 3, 0, NULL, 81328, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229807, 'start_time', '开始时间', 13, 10, NULL, '', NULL, '', 0, 1, 2, NULL, 3, 0, NULL, 81328, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229808, 'end_time', '结束时间', 13, 10, NULL, '', NULL, '', 0, 1, 3, NULL, 3, 0, NULL, 81328, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229809, 'duration', '时长（天）', 6, 10, NULL, '', NULL, '', 0, 1, 4, NULL, 3, 0, NULL, 81328, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229810, 'remark', '备注', 2, 10, NULL, '', 1000, '', 0, 0, 5, NULL, 3, 0, NULL, 81328, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229811, 'content', '出差事由', 1, 10, NULL, '', NULL, '', 0, 1, 0, NULL, 3, 0, '2019-06-30 18:13:08', 81329, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229812, 'remark', '备注', 2, 10, NULL, '', 1000, '', 0, 0, 1, NULL, 3, 0, '2019-06-30 18:13:08', 81329, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229813, 'duration', '出差总天数', 6, 10, NULL, '', NULL, '', 0, 1, 2, NULL, 3, 0, '2019-06-30 18:13:08', 81329, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229814, 'cause', '行程明细', 22, 10, NULL, '', NULL, '', 0, 1, 2, NULL, 3, 0, '2019-06-30 18:13:08', 81329, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229815, 'content', '加班原因', 1, 10, NULL, '', NULL, '', 0, 1, 0, NULL, 3, 0, '2019-06-30 18:13:08', 81330, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229816, 'start_time', '开始时间', 13, 10, NULL, '', NULL, '', 0, 1, 1, NULL, 3, 0, '2019-06-30 18:13:08', 81330, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229817, 'end_time', '结束时间', 13, 10, NULL, '', NULL, '', 0, 1, 2, NULL, 3, 0, '2019-06-30 18:13:08', 81330, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229818, 'duration', '加班总天数', 6, 10, NULL, '', NULL, '', 0, 1, 3, NULL, 3, 0, '2019-06-30 18:13:08', 81330, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229819, 'remark', '备注', 2, 10, NULL, '', 1000, '', 0, 0, 4, NULL, 3, 0, '2019-06-30 18:13:08', 81330, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229820, 'content', '差旅事由', 1, 10, NULL, '', NULL, '', 0, 1, 0, NULL, 3, 0, '2019-06-30 18:13:08', 81331, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229821, 'money', '报销总金额', 6, 10, NULL, '', 0, '', 0, 1, 1, NULL, 3, 0, '2019-06-30 18:13:08', 81331, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229822, 'remark', '备注', 2, 10, NULL, '', 1000, '', 0, 0, 2, NULL, 3, 0, '2019-06-30 18:13:08', 81331, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229823, 'cause', '费用明细', 23, 10, NULL, '', 1000, '', 0, 0, 2, NULL, 3, 0, '2019-06-30 18:13:08', 81331, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229824, 'content', '借款事由', 1, 10, NULL, '', NULL, '', 0, 1, 0, NULL, 3, 0, '2019-06-30 18:13:08', 81332, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229825, 'money', '借款金额（元）', 6, 10, NULL, '', 0, '', 0, 1, 1, NULL, 3, 0, '2019-06-30 18:13:08', 81332, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229826, 'remark', '备注', 2, 10, NULL, '', 1000, '', 0, 0, 2, NULL, 3, 0, '2019-06-30 18:13:08', 81332, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229828, 'work_name', '项目名称', 1, 19, NULL, NULL, NULL, '', 1, 1, 2, NULL, 1, 0, '2020-09-16 14:51:57', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229829, 'area', '区域', 12, 19, NULL, NULL, NULL, '', 1, 1, 4, NULL, 1, 0, '2020-09-29 13:41:31', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229830, 'work_phase', '项目阶段', 3, 19, NULL, NULL, NULL, '', 1, 1, 7, '项目立项,需求阶段,设计阶段,开发测试,项目初验,试运行,项目终验,结项', 1, 0, '2020-10-15 14:40:34', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229832, 'start_time', '项目开始时间', 13, 19, NULL, NULL, NULL, '', 1, 0, 9, NULL, 1, 0, '2020-10-27 09:53:43', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229833, 'dept_id', '部门', 12, 19, NULL, NULL, NULL, '', 1, 1, 3, NULL, 1, 0, '2020-09-29 10:25:11', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229834, 'manager_id', '项目经理', 28, 19, NULL, NULL, NULL, '', 1, 1, 5, NULL, 1, 0, '2020-09-16 14:52:24', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229835, 'sales_id', '销售负责人', 28, 19, NULL, NULL, NULL, '', 1, 0, 6, NULL, 1, 0, '2020-10-27 09:54:01', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229836, 'work_type', '项目类型', 3, 19, NULL, NULL, NULL, '', 1, 1, 8, '销售开拓,自主实施（有合同）,自主实施（无合同）,产品研发,案件分析服务,培训服务,运维服务,公共项目', 1, 0, '2020-09-16 14:52:40', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229838, 'end_time', '项目结束时间', 13, 19, NULL, NULL, NULL, '', 1, 0, 10, NULL, 1, 0, '2020-10-27 09:53:49', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229844, 'milestone_num', '里程碑编号', 1, 20, NULL, NULL, NULL, '', 1, 0, 1, NULL, 1, 0, '2020-11-25 10:55:27', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229845, 'milestone_name', '里程碑名称', 1, 20, NULL, NULL, NULL, '', 1, 1, 4, NULL, 1, 0, '2020-09-17 10:45:20', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229846, 'work_num', '项目编号', 1, 20, NULL, NULL, NULL, '', 1, 1, 3, NULL, 1, 0, '2020-09-17 10:45:04', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229849, 'start_time', '开始时间', 13, 20, NULL, NULL, NULL, '', 1, 1, 7, NULL, 1, 0, '2020-09-17 10:45:42', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229850, 'end_time', '结束时间', 13, 20, NULL, NULL, NULL, '', 1, 1, 8, NULL, 1, 0, '2020-09-17 10:45:45', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229851, 'main_user_id', '负责人', 28, 20, NULL, NULL, NULL, '', 1, 1, 9, NULL, 1, 0, '2020-09-17 10:45:52', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229852, 'instructions', '详细说明', 2, 20, NULL, NULL, NULL, '', 1, 1, 5, NULL, 1, 0, '2020-11-10 13:20:41', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229860, 'work_name', '项目名称', 1, 21, NULL, NULL, NULL, '', 1, 1, 0, NULL, 1, 0, '2020-11-16 15:48:32', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229861, 'before_id', '前置任务', 29, 21, NULL, NULL, NULL, '', 1, 0, 0, NULL, 1, 0, '2020-10-09 16:41:45', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229862, 'milestone_id', '里程碑', 30, 21, NULL, NULL, NULL, '', 1, 1, 2, NULL, 1, 0, '2020-10-16 15:38:02', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229863, 'class_id', '任务类型', 3, 21, NULL, NULL, NULL, '', 1, 1, 4, '管理,需求,设计,开发,测试,集成,运维,分析,公共', 1, 0, '2020-11-17 10:17:46', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229864, 'task_name', '任务名称', 1, 21, NULL, NULL, NULL, '', 1, 1, 3, NULL, 1, 0, '2020-10-16 15:38:05', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229865, 'status', '任务状态', 3, 21, NULL, NULL, NULL, '', 1, 1, 6, ' 未完成,进行中,已完成', 1, 0, '2020-10-22 15:56:15', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229866, 'completeness', '任务完成度(%)', 5, 21, NULL, NULL, NULL, '', 1, 1, 5, NULL, 1, 0, '2020-12-11 09:20:15', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229867, 'start_time', '计划开始时间', 13, 21, NULL, NULL, NULL, '', 1, 0, 12, NULL, 1, 0, '2020-10-27 09:59:19', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229868, 'end_time', '计划结束时间', 13, 21, NULL, NULL, NULL, '', 1, 0, 13, NULL, 1, 0, '2020-10-27 09:59:22', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229869, 'main_user_id', '负责人', 28, 21, NULL, NULL, NULL, '', 1, 1, 7, NULL, 1, 0, '2020-10-16 15:40:43', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229870, 'owner_user_id', '参与人', 10, 21, NULL, NULL, NULL, '', 1, 0, 8, NULL, 1, 0, '2020-11-26 18:23:00', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229872, 'priority', '优先级', 3, 21, NULL, NULL, NULL, '', 1, 1, 9, '高,中,低', 1, 0, '2020-10-16 15:40:49', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229873, 'difficulty', '任务难度', 3, 21, NULL, NULL, NULL, '', 1, 1, 10, '高,中,低', 1, 0, '2020-10-16 15:40:51', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229874, 'degree', '重要程度', 3, 21, NULL, NULL, NULL, '', 1, 1, 11, '高,中,低', 1, 0, '2020-10-16 15:40:55', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229875, 'description', '任务描述', 2, 21, NULL, NULL, NULL, '', 1, 0, 14, NULL, 1, 0, '2020-11-17 10:19:18', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229877, 'task_num', '任务编号', 1, 21, NULL, NULL, NULL, '', 1, 0, 1, NULL, 1, 0, '2020-11-25 10:55:27', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229878, 'risk_id', '风险序号', 1, 22, NULL, NULL, NULL, '', 1, 0, 0, '', 1, 0, '2020-09-14 13:53:05', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229879, 'risk_type', '风险类型', 3, 22, NULL, NULL, NULL, '', 1, 1, 0, '技术,管理,商业,社会,法律', 1, 0, '2020-09-11 10:44:43', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229880, 'risk_level', '风险等级', 3, 22, NULL, NULL, NULL, '', 1, 1, 0, '高,中,低', 1, 0, '2020-09-11 10:44:42', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229881, 'tracking_user_name', '跟踪人员', 28, 22, NULL, NULL, NULL, '', 1, 1, 0, '', 1, 0, '2020-09-14 17:11:36', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229882, 'scope', '影响范围', 1, 22, NULL, NULL, NULL, '', 1, 1, 0, '', 1, 0, '2020-09-11 10:44:36', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229883, 'solve_time', '解决日期', 13, 22, NULL, NULL, NULL, '', 1, 1, 0, '', 1, 0, '2020-09-14 09:51:51', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229884, 'risk_status', '风险状态', 3, 22, NULL, NULL, NULL, '', 1, 1, 0, '无,关闭', 1, 0, '2020-09-11 10:44:32', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229885, 'found', '发现方式', 1, 22, NULL, NULL, NULL, '', 1, 1, 0, '', 1, 0, '2020-09-11 10:44:30', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229886, 'solve_advice', '解决建议', 1, 22, NULL, NULL, NULL, '', 1, 1, 0, '', 1, 0, '2020-09-11 10:44:28', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229887, 'solve_user_name', '解决人员', 10, 22, NULL, NULL, NULL, '', 1, 1, 0, '', 1, 0, '2020-09-14 09:46:57', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229888, 'detailed', '详细说明', 1, 22, NULL, NULL, NULL, '', 1, 1, 0, '', 1, 0, '2020-09-11 10:44:23', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229889, 'found_user_name', '发现人员', 1, 22, NULL, NULL, NULL, '', 1, 1, 0, '', 1, 0, '2020-09-11 10:44:21', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229890, 'found_time', '发现日期', 13, 22, NULL, NULL, NULL, '', 1, 1, 0, '', 1, 0, '2020-09-14 09:51:59', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229891, 'found_phase', '发生阶段', 3, 22, NULL, NULL, NULL, '', 1, 1, 0, '售前阶段,立项阶段,计划阶段,需求阶段,设计阶段,开发阶段,测试阶段,实施阶段,运维阶段', 1, 0, '2020-09-11 10:44:17', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229892, 'direction', '方向', 3, 22, NULL, NULL, NULL, '', 1, 1, 0, '风险,机会', 1, 0, '2020-09-11 10:44:15', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229893, 'product_number', '产品编号', 1, 23, NULL, NULL, NULL, '', 1, 0, 0, '', 1, 0, '2020-11-25 10:55:27', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229894, 'product_name', '产品名称', 1, 23, NULL, NULL, NULL, '', 1, 1, 0, '', 1, 0, '2020-09-11 10:44:39', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229895, 'product_version', '产品版本', 1, 23, NULL, NULL, NULL, '', 1, 0, 0, '', 1, 0, '2020-11-25 10:14:37', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229896, 'product_type', '产品类型', 3, 23, NULL, NULL, NULL, '', 1, 1, 0, '基础类型,方案类型,应用类型', 1, 0, '2020-09-18 15:34:44', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229897, 'main_user_id', '产品负责人', 28, 23, NULL, NULL, NULL, '', 1, 1, 0, '', 1, 0, '2020-11-04 17:55:17', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229898, 'exploit_user_id', '开发负责人', 28, 23, NULL, NULL, NULL, '', 1, 0, 0, '', 1, 0, '2020-11-25 10:14:46', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229899, 'product_status', '产品状态', 3, 23, NULL, NULL, NULL, '', 1, 1, 0, '规划,发版', 1, 0, '2020-09-18 15:33:45', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229901, 'release_time', '发版时间', 13, 23, NULL, NULL, NULL, '', 1, 0, 0, '', 1, 0, '2020-11-25 10:14:54', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229902, 'version_describe', '版本描述', 2, 23, NULL, NULL, NULL, '', 1, 0, 0, '', 1, 0, '2020-11-25 10:10:37', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229903, 'work_num', '项目编号', 1, 19, NULL, NULL, NULL, '', 1, 0, 1, NULL, 1, 0, '2020-11-25 10:55:27', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229905, '客户属性', '客户属性', 9, 2, NULL, '', NULL, '', 0, 0, 9, '直销客户,核心级代理,战略级代理,认证级代理', 0, 0, '2020-12-17 10:53:51', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229906, 'task_id', '前置任务', 29, 20, NULL, NULL, NULL, '', 1, 0, 10, NULL, 1, 0, '2020-09-23 15:26:26', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229907, 'degree', '完成度(%)', 6, 20, NULL, NULL, NULL, '', 1, 1, 6, NULL, 1, 0, '2020-12-11 09:20:22', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229909, 'work_name', '项目名称', 1, 20, NULL, NULL, NULL, '', 1, 1, 2, NULL, 1, 0, '2020-09-25 17:52:57', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229910, 'subsystem', '子系统', 1, 24, NULL, NULL, NULL, '', 1, 1, 1, '', 1, 0, '2020-09-25 16:23:27', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229911, 'child_module', '子模块', 1, 24, NULL, NULL, NULL, '', 1, 1, 2, '', 1, 0, '2020-09-25 16:23:30', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229912, 'function1', '一级功能', 1, 24, NULL, NULL, NULL, '', 1, 1, 3, '', 1, 0, '2020-11-13 17:09:41', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229913, 'function2', '二级功能', 1, 24, NULL, NULL, NULL, '', 1, 0, 4, '', 1, 0, '2020-11-13 17:09:47', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229914, 'demand_describe', '需求描述', 2, 24, NULL, NULL, NULL, '', 1, 1, 5, '', 1, 0, '2020-11-13 17:28:45', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229915, 'demand_version', '需求版本号', 1, 24, NULL, NULL, NULL, '', 1, 0, 7, '', 1, 0, '2020-09-25 16:30:04', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229916, 'demand_number', '对标需求设计序号', 1, 24, NULL, NULL, NULL, '', 1, 0, 9, '', 1, 0, '2020-09-25 16:32:01', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229917, 'product_name', '对标产品名称', 1, 24, NULL, NULL, NULL, '', 1, 0, 10, '', 1, 0, '2020-09-25 16:32:10', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229918, 'product_version', '对标产品版本', 1, 24, NULL, NULL, NULL, '', 1, 0, 11, '', 1, 0, '2020-09-25 16:32:19', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229919, 'product_number', '产品编号', 1, 24, NULL, NULL, NULL, '', 1, 0, 12, '', 1, 0, '2020-09-25 16:32:36', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229920, 'function_name', '功能名称', 1, 24, NULL, NULL, NULL, '', 1, 0, 17, '', 1, 0, '2020-09-30 09:43:06', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229921, 'function_number', '功能编号', 1, 24, NULL, NULL, NULL, '', 1, 0, 16, '', 1, 0, '2020-09-30 09:43:03', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229924, 'demand_status', '完成状态(%)', 6, 24, NULL, NULL, NULL, '', 1, 0, 6, '', 1, 0, '2020-12-11 09:22:55', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229925, 'plan', '对标投标方案号', 1, 24, NULL, NULL, NULL, '', 1, 0, 8, '', 1, 0, '2020-09-25 16:30:22', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229926, 'demand_type', '需求类型', 3, 24, NULL, NULL, NULL, '', 1, 1, 15, '功能,模型,非功能,数据治理', 1, 0, '2020-09-25 16:35:14', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229927, 'product_status', '产品状态', 3, 25, NULL, NULL, NULL, '', 1, 1, 5, '发版,规划', 1, 0, '2020-11-05 14:38:11', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229930, 'product_name', '产品名称', 1, 25, NULL, NULL, NULL, '', 1, 0, 1, '', 1, 0, '2020-10-21 10:16:16', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229931, 'product_version', '产品版本', 1, 25, NULL, NULL, NULL, '', 1, 0, 2, '', 1, 0, '2020-10-21 10:16:19', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229932, 'main_user_id', '产品负责人', 28, 25, NULL, NULL, NULL, '', 1, 1, 3, '', 1, 0, '2020-11-05 14:38:05', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229933, 'exploit_user_id', '开发负责人', 28, 25, NULL, NULL, NULL, '', 1, 0, 4, '', 1, 0, '2020-10-21 10:16:27', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229934, 'function_number', '产品功能编号', 1, 25, NULL, NULL, NULL, '', 1, 1, 6, '', 1, 0, '2020-11-05 14:37:39', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229935, 'function_name', '产品功能名称', 1, 25, NULL, NULL, NULL, '', 1, 1, 7, '', 1, 0, '2020-11-05 14:37:43', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229936, 'function_describe', '版本描述', 2, 25, NULL, NULL, NULL, '', 1, 0, 8, '', 1, 0, '2020-11-25 13:03:38', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229937, '附件', '附件', 8, 2, NULL, '', NULL, '', 0, 0, 10, NULL, 0, 0, '2020-12-17 10:53:51', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229938, 'task_id', '任务', 29, 28, NULL, NULL, NULL, '', 1, 1, 2, '', 1, 0, '2020-09-28 16:22:47', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229939, 'main_user_id', '负责人', 28, 28, NULL, NULL, NULL, '', 1, 1, 2, '', 1, 0, '2020-09-28 16:22:40', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229940, 'subtasks_id', '子任务', 32, 28, NULL, NULL, NULL, '', 1, 0, 2, '', 1, 0, '2020-10-23 09:15:54', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229941, 'start_time', '开始时间', 13, 28, NULL, NULL, NULL, '', 1, 1, 2, '', 1, 0, '2020-10-16 15:39:05', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229942, 'end_time', '结束时间', 13, 28, NULL, NULL, NULL, '', 1, 1, 2, '', 1, 0, '2020-10-16 15:39:08', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229943, 'working_hours', '工时', 1, 28, NULL, NULL, NULL, '', 1, 1, 2, '', 1, 0, '2020-09-25 16:23:30', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229944, 'hours_type', '工时类型', 3, 28, NULL, NULL, NULL, '', 1, 1, 2, '加班,常规工时', 1, 0, '2020-10-16 11:17:16', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229945, 'progress_content', '进度内容', 1, 28, NULL, NULL, NULL, '', 1, 0, 2, '', 1, 0, '2020-10-27 10:01:39', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229947, 'pid', '所属任务', 29, 29, NULL, NULL, NULL, '', 1, 0, 5, NULL, 1, 0, '2020-10-16 16:10:17', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229948, 'milestone_id', '里程碑', 30, 29, NULL, NULL, NULL, '', 1, 1, 4, NULL, 1, 0, '2020-10-11 11:26:33', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229949, 'class_id', '任务类型', 3, 29, NULL, NULL, NULL, '', 1, 1, 6, '管理,需求,设计,开发,测试,集成,运维,分析,公共', 1, 0, '2020-11-17 10:17:35', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229950, 'task_name', '子任务名称', 1, 29, NULL, NULL, NULL, '', 1, 1, 2, NULL, 1, 0, '2020-10-27 10:01:28', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229951, 'status', '子任务状态', 3, 29, NULL, NULL, NULL, '', 1, 1, 3, '未完成,进行中,已完成', 1, 0, '2020-11-11 13:45:11', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229952, 'completeness', '任务完成度(%)', 5, 29, NULL, NULL, NULL, '', 1, 1, 7, NULL, 1, 0, '2020-12-11 09:20:29', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229953, 'start_time', '计划开始时间', 13, 29, NULL, NULL, NULL, '', 1, 1, 13, NULL, 1, 0, '2020-10-11 11:27:33', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229954, 'end_time', '计划结束时间', 13, 29, NULL, NULL, NULL, '', 1, 1, 14, NULL, 1, 0, '2020-10-11 11:27:47', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229955, 'main_user_id', '负责人', 28, 29, NULL, NULL, NULL, '', 1, 1, 11, NULL, 1, 0, '2020-10-11 11:27:23', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229956, 'owner_user_id', '参与人', 10, 29, NULL, NULL, NULL, '', 1, 1, 12, NULL, 1, 0, '2020-11-30 14:56:28', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229957, 'priority', '优先级', 3, 29, NULL, NULL, NULL, '', 1, 1, 8, '高,中,低', 1, 0, '2020-10-11 11:27:09', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229958, 'difficulty', '任务难度', 3, 29, NULL, NULL, NULL, '', 1, 1, 9, '高,中,低', 1, 0, '2020-10-11 11:27:11', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229959, 'degree', '重要程度', 3, 29, NULL, NULL, NULL, '', 1, 1, 10, '高,中,低', 1, 0, '2020-10-11 11:27:20', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229960, 'config_items', '配置项', 1, 30, NULL, NULL, NULL, '', 1, 1, 1, NULL, 1, 0, '2020-11-18 09:29:23', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229961, 'plan_time', '计划时间', 13, 30, NULL, NULL, NULL, '', 1, 1, 2, NULL, 1, 0, '2020-10-26 14:00:36', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229962, 'main_user_id', '负责人', 28, 30, NULL, NULL, NULL, '', 1, 1, 3, NULL, 1, 0, '2020-10-26 14:00:37', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229963, 'associated_id', '项目名称', 31, 30, NULL, NULL, NULL, '', 1, 1, 4, NULL, 1, 0, '2020-11-03 11:47:15', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229964, 'associated_num', '项目编号', 1, 30, NULL, NULL, NULL, '', 1, 1, 5, NULL, 1, 0, '2020-10-26 14:00:39', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229966, 'version', '版本', 1, 30, NULL, NULL, NULL, '', 1, 0, 7, NULL, 1, 0, '2020-11-18 09:29:48', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229967, 'version_time', '版本时间', 13, 30, NULL, NULL, NULL, '', 1, 0, 8, NULL, 1, 0, '2020-11-18 09:29:35', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229968, 'version_describe', '版本说明', 2, 30, NULL, NULL, NULL, '', 1, 0, 9, NULL, 1, 0, '2020-11-18 09:29:44', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229969, '客户类型', '客户类型', 1, 2, NULL, '', NULL, '', 0, 0, 11, NULL, 0, 0, '2020-12-17 10:53:51', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229970, '人员', '人员', 10, 1, NULL, '', NULL, '', 0, 0, 10, NULL, 0, 0, '2020-12-17 14:25:23', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229971, '单行文本', '单行文本', 1, 2, NULL, '', NULL, '', 0, 0, 12, NULL, 0, 0, '2020-12-17 10:53:51', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229972, '部门', '部门', 12, 1, NULL, '', NULL, '', 0, 0, 11, NULL, 0, 0, '2020-12-17 14:25:23', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229973, '测试', '测试', 1, 1, NULL, '', NULL, '', 0, 0, 12, NULL, 0, 0, '2020-12-17 14:25:23', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229974, '测试多行', '测试多行', 2, 1, NULL, '', 1000, '', 0, 0, 13, NULL, 0, 0, '2020-12-17 14:25:23', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229975, '下拉框', '下拉框', 3, 1, NULL, '', NULL, '', 0, 0, 14, '测试下拉选1,测试下拉选2,测试下拉选3', 0, 0, '2020-12-17 14:25:23', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229976, '邮箱', '邮箱', 14, 1, NULL, '', NULL, '', 0, 0, 15, NULL, 0, 0, '2020-12-17 14:25:23', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229979, '所属部门', '所属部门', 3, 2, NULL, '', NULL, '', 0, 0, 13, '华北一区,华北二区,华东区,华南区,华中区,西南区,西北一区,西北二区,市场拓展部', 0, 0, '2020-12-17 10:53:51', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229980, '客户省份', '客户省份', 1, 2, NULL, '', NULL, '', 0, 0, 14, NULL, 0, 0, '2020-12-17 10:53:51', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229981, '部门', '部门', 1, 3, NULL, '', NULL, '', 0, 1, 3, NULL, 0, 0, '2020-12-17 11:05:48', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229983, '商机合同有效额', '商机合同有效额', 6, 5, NULL, '', NULL, '', 0, 1, 5, NULL, 0, 0, '2020-12-17 14:27:06', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229984, '竞争对手', '竞争对手', 1, 5, NULL, '', NULL, '', 0, 0, 7, NULL, 0, 0, '2020-12-17 14:27:06', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229985, '是否招标', '是否招标', 3, 5, NULL, '', NULL, '', 0, 0, 9, '是,否,待定', 0, 0, '2020-12-17 14:27:06', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229986, '中标概率预估', '中标概率预估', 3, 5, NULL, '', NULL, '', 0, 0, 10, '概率25%,概率50%,概率75%,概率100%', 0, 0, '2020-12-17 14:27:06', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229987, '当年启动概率', '当年启动概率', 3, 5, NULL, '', NULL, '', 0, 0, 11, '概率25%,概率50%,概率75%,概率100%', 0, 0, '2020-12-17 14:27:06', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229988, '预计签约时间', '预计签约时间', 4, 5, NULL, '', NULL, '', 0, 0, 12, NULL, 0, 0, '2020-12-17 14:27:06', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229989, '签约产品', '签约产品', 9, 5, NULL, '', NULL, '', 0, 0, 8, '服务类,侦查机产品类,项目类,培训服务类,代理合作协议类', 0, 0, '2020-12-17 14:27:06', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229991, '商机洽谈阶段', '商机洽谈阶段', 3, 5, NULL, '', NULL, '', 0, 0, 14, '发现商机,客户采购意向明确,公司内方案定稿过评审,可研方案提交客户,客户方案评审通过,客户内部财评通过,地方财政评审通过,预招标预告公示,正式招标挂网公示,标书方案编写,合同签订流程,项目实施,验收回款,赢单,输单,取消', 0, 0, '2020-12-17 14:27:06', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229992, '所属部门', '所属部门', 3, 5, NULL, '', NULL, '', 0, 0, 1, '华北一区,华北二区,华东区,华南区,华中区,西南区,西北一区,西北二区,市场拓展部', 0, 0, '2020-12-17 14:27:06', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229993, '销售负责人', '销售负责人', 1, 5, NULL, '', NULL, '', 0, 1, 15, NULL, 0, 0, '2020-12-17 14:27:06', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229994, '合同有效额', '合同有效额', 6, 6, NULL, '', NULL, '', 0, 1, 8, NULL, 0, 0, '2020-12-17 13:31:16', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229995, '签订主体', '签订主体', 3, 6, NULL, '', NULL, '', 0, 1, 5, '中科金审（北京）科技有限公司,陕西数金软件科技有限公司,江西中科金审科技有限公司,上海金数优审科技有限公司', 0, 0, '2020-12-17 13:31:16', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229996, '销售区域', '销售区域', 3, 6, NULL, '', NULL, '', 0, 0, 0, '华北一区,华北二区,华东区,华南区,华中区,西南区,西北一区,西北二区', 0, 0, '2020-12-17 13:31:16', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229997, '签订合同附件', '签订合同附件', 8, 6, NULL, '', NULL, '', 0, 1, 15, NULL, 0, 0, '2020-12-17 13:31:16', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229998, '付款说明', '付款说明', 9, 6, NULL, '', NULL, '一次支付完成', 0, 0, 16, '一次支付完成,二次支付完成,三次支付完成', 0, 0, '2020-12-17 13:31:16', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1229999, '回款状态', '回款状态', 3, 6, NULL, '', NULL, '', 0, 0, 17, '已回全款,未回全款', 0, 0, '2020-12-17 13:31:16', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230000, '纸质合同（行政部）', '纸质合同（行政部）', 3, 6, NULL, '', NULL, '', 0, 0, 18, '已签合同-已交总部,已签合同-未交总部,未签合同', 0, 0, '2020-12-17 13:31:16', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230001, '侦查机设备签收单记录', '侦查机设备签收单记录', 1, 6, NULL, '', NULL, '', 0, 0, 19, NULL, 0, 0, '2020-12-17 13:31:16', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230002, '钉钉审批单号', '钉钉审批单号', 1, 6, NULL, '', NULL, '', 0, 0, 20, NULL, 0, 0, '2020-12-17 13:31:16', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230003, '项目状态', '项目状态', 3, 6, NULL, '', NULL, '', 0, 0, 21, '执行中,初验通过,终验通过,已结项', 0, 0, '2020-12-17 13:31:16', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230004, '业绩属性', '业绩属性', 3, 6, NULL, '', NULL, '', 0, 0, 22, '2020-1Q,2020-2Q,2020-3Q,2020-4Q', 0, 0, '2020-12-17 13:31:16', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230005, '线索编号', '线索编号', 1, 1, NULL, '', NULL, '', 0, 1, 0, NULL, 0, 0, '2020-12-17 14:25:23', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230007, '商机编号', '商机编号', 1, 5, NULL, '', NULL, '', 0, 1, 0, NULL, 0, 0, '2020-12-17 14:27:06', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230012, '数量', '数量', 5, 4, NULL, '', NULL, '', 0, 0, 7, NULL, 0, 0, '2020-12-17 15:42:32', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230013, '回访日期时间', '回访日期时间', 13, 17, NULL, '', NULL, '', 0, 0, 0, NULL, 0, 0, '2020-12-17 16:53:39', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230015, '回访目的', '回访目的', 3, 17, NULL, '', NULL, '', 0, 0, 4, '业务洽谈,项目支持,侦查机试用', 0, 0, '2020-12-17 16:53:39', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230016, '回访详情描述', '回访详情描述', 2, 17, NULL, '', 1000, '', 0, 0, 6, NULL, 0, 0, '2020-12-17 16:53:39', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230017, 'visit_number', '回访编号', 1, 17, NULL, NULL, NULL, '', 1, 1, 1, NULL, 1, 0, '2020-12-17 16:53:39', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230018, 'visit_time', '回访时间', 13, 17, NULL, NULL, NULL, '', 0, 0, 2, NULL, 1, 0, '2020-12-17 16:53:39', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230019, 'owner_user_id', '回访人', 10, 17, NULL, NULL, NULL, '', 0, 1, 5, NULL, 1, 0, '2020-12-17 16:53:39', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230020, '回访形式', '回访形式', 3, 17, NULL, NULL, NULL, '', 0, 0, 7, '见面拜访,电话,短信,邮件,微信', 1, 0, '2020-12-17 16:53:39', NULL, 2, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230021, 'customer_id', '客户名称', 15, 17, NULL, NULL, NULL, '', 0, 1, 8, NULL, 1, 0, '2020-12-17 16:53:39', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230022, 'contacts_id', '联系人', 17, 17, NULL, NULL, NULL, '', 0, 0, 9, NULL, 1, 0, '2020-12-17 16:53:39', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230023, 'contract_id', '合同编号', 20, 17, NULL, NULL, NULL, '', 0, 1, 10, NULL, 1, 0, '2020-12-17 16:53:39', NULL, 1, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230024, '客户满意度', '客户满意度', 3, 17, NULL, NULL, NULL, '', 0, 0, 11, '很满意,满意,一般,不满意,很不满意', 1, 0, '2020-12-17 16:53:39', NULL, 2, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230025, '客户反馈', '客户反馈', 2, 17, NULL, NULL, 1000, '', 0, 0, 12, NULL, 0, 0, '2020-12-17 16:53:39', NULL, 0, NULL);
INSERT INTO `72crm_admin_field` VALUES (1230026, '回访客户姓名', '回访客户姓名', 1, 17, NULL, '', NULL, '', 0, 0, 3, NULL, 0, 0, '2020-12-17 16:53:39', NULL, 0, NULL);

-- ----------------------------
-- Table structure for 72crm_admin_field_sort
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_field_sort`;
CREATE TABLE `72crm_admin_field_sort`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字段id',
  `label` int(2) NOT NULL COMMENT '标签 1 线索 2 客户 3 联系人 4 产品 5 商机 6 合同 7回款 8公海',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段中文名称',
  `type` int(2) NULL DEFAULT NULL COMMENT '字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划',
  `sort` int(5) NOT NULL DEFAULT 0 COMMENT '字段排序',
  `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '用户id',
  `is_hide` int(1) NOT NULL DEFAULT 1 COMMENT '是否隐藏 0、不隐藏 1、隐藏',
  `field_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `label`(`label`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 328548 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段排序表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_field_sort
-- ----------------------------
INSERT INTO `72crm_admin_field_sort` VALUES (327052, 17, '回访日期时间', '回访日期时间', 13, 0, 16408, 0, 1230013);
INSERT INTO `72crm_admin_field_sort` VALUES (327053, 17, 'visitNumber', '回访编号', 1, 1, 16408, 0, 1230017);
INSERT INTO `72crm_admin_field_sort` VALUES (327054, 17, '回访客户姓名', '回访客户姓名', 10, 2, 16408, 0, 1230014);
INSERT INTO `72crm_admin_field_sort` VALUES (327055, 17, 'visitTime', '回访时间', 13, 3, 16408, 0, 1230018);
INSERT INTO `72crm_admin_field_sort` VALUES (327056, 17, '回访目的', '回访目的', 3, 4, 16408, 0, 1230015);
INSERT INTO `72crm_admin_field_sort` VALUES (327057, 17, 'ownerUserName', '回访人', 10, 5, 16408, 0, 1230019);
INSERT INTO `72crm_admin_field_sort` VALUES (327058, 17, '回访详情描述', '回访详情描述', 2, 6, 16408, 0, 1230016);
INSERT INTO `72crm_admin_field_sort` VALUES (327059, 17, '回访形式', '回访形式', 3, 7, 16408, 0, 1230020);
INSERT INTO `72crm_admin_field_sort` VALUES (327060, 17, 'customerName', '客户名称', 15, 8, 16408, 0, 1230021);
INSERT INTO `72crm_admin_field_sort` VALUES (327061, 17, 'contactsName', '合同编号', 17, 9, 16408, 0, 1230022);
INSERT INTO `72crm_admin_field_sort` VALUES (327062, 17, 'contractNum', '合同编号', 20, 10, 16408, 0, 1230023);
INSERT INTO `72crm_admin_field_sort` VALUES (327063, 17, '客户满意度', '客户满意度', 3, 11, 16408, 0, 1230024);
INSERT INTO `72crm_admin_field_sort` VALUES (327064, 17, '客户反馈', '客户反馈', 2, 12, 16408, 0, 1230025);
INSERT INTO `72crm_admin_field_sort` VALUES (327065, 17, 'updateTime', '更新时间', 4, 13, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327066, 17, 'createTime', '创建时间', 4, 14, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327067, 17, 'createUserName', '创建人', 1, 15, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327068, 4, 'name', '产品名称', 1, 0, 16389, 0, 1229765);
INSERT INTO `72crm_admin_field_sort` VALUES (327069, 4, 'categoryName', '产品类型', 19, 1, 16389, 0, 1229766);
INSERT INTO `72crm_admin_field_sort` VALUES (327070, 4, '单位', '单位', 3, 2, 16389, 0, 1229767);
INSERT INTO `72crm_admin_field_sort` VALUES (327071, 4, 'price', '价格', 6, 3, 16389, 0, 1229769);
INSERT INTO `72crm_admin_field_sort` VALUES (327072, 4, '是否上下架', '是否上下架', 3, 4, 16389, 0, 1229770);
INSERT INTO `72crm_admin_field_sort` VALUES (327073, 4, 'description', '销售产品描述', 1, 5, 16389, 0, 1229771);
INSERT INTO `72crm_admin_field_sort` VALUES (327074, 4, '数量', '数量', 5, 6, 16389, 0, 1230012);
INSERT INTO `72crm_admin_field_sort` VALUES (327075, 4, 'ownerUserName', '负责人', 1, 7, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327076, 4, 'updateTime', '更新时间', 4, 8, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327077, 4, 'createTime', '创建时间', 4, 9, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327078, 4, 'createUserName', '创建人', 1, 10, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327079, 17, '回访日期时间', '回访日期时间', 13, 0, 16389, 0, 1230013);
INSERT INTO `72crm_admin_field_sort` VALUES (327080, 17, 'visitNumber', '回访编号', 1, 1, 16389, 0, 1230017);
INSERT INTO `72crm_admin_field_sort` VALUES (327081, 17, '回访客户姓名', '回访客户姓名', 10, 2, 16389, 0, 1230014);
INSERT INTO `72crm_admin_field_sort` VALUES (327082, 17, 'visitTime', '回访时间', 13, 3, 16389, 0, 1230018);
INSERT INTO `72crm_admin_field_sort` VALUES (327083, 17, '回访目的', '回访目的', 3, 4, 16389, 0, 1230015);
INSERT INTO `72crm_admin_field_sort` VALUES (327084, 17, 'ownerUserName', '回访人', 10, 5, 16389, 0, 1230019);
INSERT INTO `72crm_admin_field_sort` VALUES (327085, 17, '回访详情描述', '回访详情描述', 2, 6, 16389, 0, 1230016);
INSERT INTO `72crm_admin_field_sort` VALUES (327086, 17, '回访形式', '回访形式', 3, 7, 16389, 0, 1230020);
INSERT INTO `72crm_admin_field_sort` VALUES (327087, 17, 'customerName', '客户名称', 15, 8, 16389, 0, 1230021);
INSERT INTO `72crm_admin_field_sort` VALUES (327088, 17, 'contactsName', '合同编号', 17, 9, 16389, 0, 1230022);
INSERT INTO `72crm_admin_field_sort` VALUES (327089, 17, 'contractNum', '合同编号', 20, 10, 16389, 0, 1230023);
INSERT INTO `72crm_admin_field_sort` VALUES (327090, 17, '客户满意度', '客户满意度', 3, 11, 16389, 0, 1230024);
INSERT INTO `72crm_admin_field_sort` VALUES (327091, 17, '客户反馈', '客户反馈', 2, 12, 16389, 0, 1230025);
INSERT INTO `72crm_admin_field_sort` VALUES (327092, 17, 'updateTime', '更新时间', 4, 13, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327093, 17, 'createTime', '创建时间', 4, 14, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327094, 17, 'createUserName', '创建人', 1, 15, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327095, 19, 'workNum', '项目编号', 1, 2, 16389, 0, 1229903);
INSERT INTO `72crm_admin_field_sort` VALUES (327096, 19, 'workName', '项目名称', 1, 1, 16389, 0, 1229828);
INSERT INTO `72crm_admin_field_sort` VALUES (327097, 19, 'deptName', '部门', 12, 4, 16389, 0, 1229833);
INSERT INTO `72crm_admin_field_sort` VALUES (327098, 19, 'areaName', '区域', 12, 3, 16389, 0, 1229829);
INSERT INTO `72crm_admin_field_sort` VALUES (327099, 19, 'managerName', '项目经理', 28, 5, 16389, 0, 1229834);
INSERT INTO `72crm_admin_field_sort` VALUES (327100, 19, 'salesName', '销售负责人', 28, 6, 16389, 0, 1229835);
INSERT INTO `72crm_admin_field_sort` VALUES (327101, 19, 'workPhase', '项目阶段', 3, 7, 16389, 0, 1229830);
INSERT INTO `72crm_admin_field_sort` VALUES (327102, 19, 'workType', '项目类型', 3, 8, 16389, 0, 1229836);
INSERT INTO `72crm_admin_field_sort` VALUES (327103, 19, 'startTime', '项目开始时间', 13, 9, 16389, 0, 1229832);
INSERT INTO `72crm_admin_field_sort` VALUES (327104, 19, 'endTime', '项目结束时间', 13, 10, 16389, 0, 1229838);
INSERT INTO `72crm_admin_field_sort` VALUES (327105, 19, 'updateTime', '更新时间', 4, 11, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327106, 19, 'createTime', '创建时间', 4, 12, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327107, 19, 'createUserName', '创建人', 1, 13, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327108, 2, 'customerName', '客户名称', 1, 0, 16360, 0, 1229736);
INSERT INTO `72crm_admin_field_sort` VALUES (327109, 2, '客户来源', '客户来源', 3, 1, 16360, 0, 1229737);
INSERT INTO `72crm_admin_field_sort` VALUES (327110, 2, 'telephone', '电话', 1, 2, 16360, 0, 1229739);
INSERT INTO `72crm_admin_field_sort` VALUES (327111, 2, 'website', '网址', 1, 3, 16360, 0, 1229740);
INSERT INTO `72crm_admin_field_sort` VALUES (327112, 2, '客户行业', '客户行业', 3, 4, 16360, 0, 1229741);
INSERT INTO `72crm_admin_field_sort` VALUES (327113, 2, '客户级别', '客户级别', 3, 5, 16360, 0, 1229742);
INSERT INTO `72crm_admin_field_sort` VALUES (327114, 2, 'nextTime', '下次联系时间', 13, 6, 16360, 0, 1229743);
INSERT INTO `72crm_admin_field_sort` VALUES (327115, 2, 'remark', '备注', 2, 7, 16360, 0, 1229744);
INSERT INTO `72crm_admin_field_sort` VALUES (327116, 2, '客户属性', '客户属性', 9, 8, 16360, 0, 1229905);
INSERT INTO `72crm_admin_field_sort` VALUES (327117, 2, '附件', '附件', 8, 9, 16360, 0, 1229937);
INSERT INTO `72crm_admin_field_sort` VALUES (327118, 2, '客户类型', '客户类型', 1, 10, 16360, 0, 1229969);
INSERT INTO `72crm_admin_field_sort` VALUES (327119, 2, '单行文本', '单行文本', 1, 11, 16360, 0, 1229971);
INSERT INTO `72crm_admin_field_sort` VALUES (327120, 2, '所属部门', '所属部门', 3, 12, 16360, 0, 1229979);
INSERT INTO `72crm_admin_field_sort` VALUES (327121, 2, '客户省份', '客户省份', 1, 13, 16360, 0, 1229980);
INSERT INTO `72crm_admin_field_sort` VALUES (327122, 2, 'status', '锁定状态', 5, 14, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327123, 2, 'dealStatus', '成交状态', 3, 15, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327124, 2, 'lastTime', '最后跟进时间', 4, 16, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327125, 2, 'lastContent', '最后跟进记录', 1, 17, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327126, 2, 'detailAddress', '详细地址', 1, 18, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327127, 2, 'address', '地区定位', 18, 19, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327128, 2, 'poolDay', '距进入公海天数', 5, 20, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327129, 2, 'ownerUserName', '负责人', 1, 21, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327130, 2, 'updateTime', '更新时间', 4, 22, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327131, 2, 'createTime', '创建时间', 4, 23, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327132, 2, 'createUserName', '创建人', 1, 24, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327133, 2, 'customerName', '客户名称', 1, 0, 16398, 0, 1229736);
INSERT INTO `72crm_admin_field_sort` VALUES (327134, 2, '客户来源', '客户来源', 3, 1, 16398, 0, 1229737);
INSERT INTO `72crm_admin_field_sort` VALUES (327135, 2, 'telephone', '电话', 1, 2, 16398, 0, 1229739);
INSERT INTO `72crm_admin_field_sort` VALUES (327136, 2, 'website', '网址', 1, 3, 16398, 0, 1229740);
INSERT INTO `72crm_admin_field_sort` VALUES (327137, 2, '客户行业', '客户行业', 3, 4, 16398, 0, 1229741);
INSERT INTO `72crm_admin_field_sort` VALUES (327138, 2, '客户级别', '客户级别', 3, 5, 16398, 0, 1229742);
INSERT INTO `72crm_admin_field_sort` VALUES (327139, 2, 'nextTime', '下次联系时间', 13, 6, 16398, 0, 1229743);
INSERT INTO `72crm_admin_field_sort` VALUES (327140, 2, 'remark', '备注', 2, 7, 16398, 0, 1229744);
INSERT INTO `72crm_admin_field_sort` VALUES (327141, 2, '客户属性', '客户属性', 9, 8, 16398, 0, 1229905);
INSERT INTO `72crm_admin_field_sort` VALUES (327142, 2, '附件', '附件', 8, 9, 16398, 0, 1229937);
INSERT INTO `72crm_admin_field_sort` VALUES (327143, 2, '客户类型', '客户类型', 1, 10, 16398, 0, 1229969);
INSERT INTO `72crm_admin_field_sort` VALUES (327144, 2, '单行文本', '单行文本', 1, 11, 16398, 0, 1229971);
INSERT INTO `72crm_admin_field_sort` VALUES (327145, 2, '所属部门', '所属部门', 3, 12, 16398, 0, 1229979);
INSERT INTO `72crm_admin_field_sort` VALUES (327146, 2, '客户省份', '客户省份', 1, 13, 16398, 0, 1229980);
INSERT INTO `72crm_admin_field_sort` VALUES (327147, 2, 'status', '锁定状态', 5, 14, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327148, 2, 'dealStatus', '成交状态', 3, 15, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327149, 2, 'lastTime', '最后跟进时间', 4, 16, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327150, 2, 'lastContent', '最后跟进记录', 1, 17, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327151, 2, 'detailAddress', '详细地址', 1, 18, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327152, 2, 'address', '地区定位', 18, 19, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327153, 2, 'poolDay', '距进入公海天数', 5, 20, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327154, 2, 'ownerUserName', '负责人', 1, 21, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327155, 2, 'updateTime', '更新时间', 4, 22, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327156, 2, 'createTime', '创建时间', 4, 23, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327157, 2, 'createUserName', '创建人', 1, 24, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327158, 1, '线索编号', '线索编号', 1, 0, 16398, 0, 1230005);
INSERT INTO `72crm_admin_field_sort` VALUES (327159, 1, 'leadsName', '线索名称', 1, 1, 16398, 0, 1229745);
INSERT INTO `72crm_admin_field_sort` VALUES (327160, 1, '线索来源', '线索来源', 3, 2, 16398, 0, 1229746);
INSERT INTO `72crm_admin_field_sort` VALUES (327161, 1, 'mobile', '手机', 7, 3, 16398, 0, 1229747);
INSERT INTO `72crm_admin_field_sort` VALUES (327162, 1, 'telephone', '电话', 1, 4, 16398, 0, 1229748);
INSERT INTO `72crm_admin_field_sort` VALUES (327163, 1, 'address', '地址', 1, 5, 16398, 0, 1229749);
INSERT INTO `72crm_admin_field_sort` VALUES (327164, 1, '客户行业', '客户行业', 3, 6, 16398, 0, 1229750);
INSERT INTO `72crm_admin_field_sort` VALUES (327165, 1, '客户级别', '客户级别', 3, 7, 16398, 0, 1229751);
INSERT INTO `72crm_admin_field_sort` VALUES (327166, 1, 'nextTime', '下次联系时间', 13, 8, 16398, 0, 1229752);
INSERT INTO `72crm_admin_field_sort` VALUES (327167, 1, 'remark', '备注', 2, 9, 16398, 0, 1229753);
INSERT INTO `72crm_admin_field_sort` VALUES (327168, 1, '人员', '人员', 10, 10, 16398, 0, 1229970);
INSERT INTO `72crm_admin_field_sort` VALUES (327169, 1, '部门', '部门', 12, 11, 16398, 0, 1229972);
INSERT INTO `72crm_admin_field_sort` VALUES (327170, 1, '测试', '测试', 1, 12, 16398, 0, 1229973);
INSERT INTO `72crm_admin_field_sort` VALUES (327171, 1, '测试多行', '测试多行', 2, 13, 16398, 0, 1229974);
INSERT INTO `72crm_admin_field_sort` VALUES (327172, 1, '下拉框', '下拉框', 3, 14, 16398, 0, 1229975);
INSERT INTO `72crm_admin_field_sort` VALUES (327173, 1, '邮箱', '邮箱', 14, 15, 16398, 0, 1229976);
INSERT INTO `72crm_admin_field_sort` VALUES (327174, 1, 'lastTime', '最后跟进时间', 4, 16, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327175, 1, 'lastContent', '最后跟进记录', 1, 17, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327176, 1, 'ownerUserName', '负责人', 1, 18, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327177, 1, 'updateTime', '更新时间', 4, 19, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327178, 1, 'createTime', '创建时间', 4, 20, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327179, 1, 'createUserName', '创建人', 1, 21, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327180, 5, '商机编号', '商机编号', 1, 0, 16398, 0, 1230007);
INSERT INTO `72crm_admin_field_sort` VALUES (327181, 5, '所属部门', '所属部门', 3, 1, 16398, 0, 1229992);
INSERT INTO `72crm_admin_field_sort` VALUES (327182, 5, 'businessName', '商机名称', 1, 2, 16398, 0, 1229772);
INSERT INTO `72crm_admin_field_sort` VALUES (327183, 5, 'customerName', '客户名称', 15, 3, 16398, 0, 1229773);
INSERT INTO `72crm_admin_field_sort` VALUES (327184, 5, 'money', '商机合同金额', 6, 4, 16398, 0, 1229774);
INSERT INTO `72crm_admin_field_sort` VALUES (327185, 5, '商机合同有效额', '商机合同有效额', 6, 5, 16398, 0, 1229983);
INSERT INTO `72crm_admin_field_sort` VALUES (327186, 5, 'dealDate', '预计成交日期', 13, 6, 16398, 0, 1229775);
INSERT INTO `72crm_admin_field_sort` VALUES (327187, 5, '竞争对手', '竞争对手', 1, 7, 16398, 0, 1229984);
INSERT INTO `72crm_admin_field_sort` VALUES (327188, 5, '签约产品', '签约产品', 9, 8, 16398, 0, 1229989);
INSERT INTO `72crm_admin_field_sort` VALUES (327189, 5, '是否招标', '是否招标', 3, 9, 16398, 0, 1229985);
INSERT INTO `72crm_admin_field_sort` VALUES (327190, 5, '中标概率预估', '中标概率预估', 3, 10, 16398, 0, 1229986);
INSERT INTO `72crm_admin_field_sort` VALUES (327191, 5, '当年启动概率', '当年启动概率', 3, 11, 16398, 0, 1229987);
INSERT INTO `72crm_admin_field_sort` VALUES (327192, 5, '预计签约时间', '预计签约时间', 4, 12, 16398, 0, 1229988);
INSERT INTO `72crm_admin_field_sort` VALUES (327193, 5, 'remark', '客户商机需求', 2, 13, 16398, 0, 1229776);
INSERT INTO `72crm_admin_field_sort` VALUES (327194, 5, '商机洽谈阶段', '商机洽谈阶段', 3, 14, 16398, 0, 1229991);
INSERT INTO `72crm_admin_field_sort` VALUES (327195, 5, '销售负责人', '销售负责人', 1, 15, 16398, 0, 1229993);
INSERT INTO `72crm_admin_field_sort` VALUES (327196, 5, 'typeName', '商机状态组', 3, 16, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327197, 5, 'statusName', '商机阶段', 3, 17, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327198, 5, 'lastTime', '最后跟进时间', 4, 18, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327199, 5, 'ownerUserName', '负责人', 1, 19, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327200, 5, 'updateTime', '更新时间', 4, 20, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327201, 5, 'createTime', '创建时间', 4, 21, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327202, 5, 'createUserName', '创建人', 1, 22, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327203, 6, '销售区域', '销售区域', 3, 0, 16398, 0, 1229996);
INSERT INTO `72crm_admin_field_sort` VALUES (327204, 6, 'num', '合同编号', 1, 1, 16398, 0, 1229778);
INSERT INTO `72crm_admin_field_sort` VALUES (327205, 6, 'customerName', '客户名称', 15, 2, 16398, 0, 1229779);
INSERT INTO `72crm_admin_field_sort` VALUES (327206, 6, 'name', '合同甲方（盖章名称）', 1, 3, 16398, 0, 1229777);
INSERT INTO `72crm_admin_field_sort` VALUES (327207, 6, 'businessName', '商机名称', 16, 4, 16398, 0, 1229780);
INSERT INTO `72crm_admin_field_sort` VALUES (327208, 6, '签订主体', '签订主体', 3, 5, 16398, 0, 1229995);
INSERT INTO `72crm_admin_field_sort` VALUES (327209, 6, '合同类型', '合同类型', 3, 6, 16398, 0, 1229788);
INSERT INTO `72crm_admin_field_sort` VALUES (327210, 6, 'money', '合同金额', 6, 7, 16398, 0, 1229781);
INSERT INTO `72crm_admin_field_sort` VALUES (327211, 6, '合同有效额', '合同有效额', 6, 8, 16398, 0, 1229994);
INSERT INTO `72crm_admin_field_sort` VALUES (327212, 6, 'orderDate', '签约时间', 4, 9, 16398, 0, 1229782);
INSERT INTO `72crm_admin_field_sort` VALUES (327213, 6, 'companyUserName', '销售签约人', 10, 10, 16398, 0, 1229786);
INSERT INTO `72crm_admin_field_sort` VALUES (327214, 6, 'endTime', '合同结束时间', 4, 11, 16398, 0, 1229784);
INSERT INTO `72crm_admin_field_sort` VALUES (327215, 6, 'remark', '备注', 2, 12, 16398, 0, 1229787);
INSERT INTO `72crm_admin_field_sort` VALUES (327216, 6, '签订合同附件', '签订合同附件', 8, 13, 16398, 0, 1229997);
INSERT INTO `72crm_admin_field_sort` VALUES (327217, 6, '付款说明', '付款说明', 9, 14, 16398, 0, 1229998);
INSERT INTO `72crm_admin_field_sort` VALUES (327218, 6, '回款状态', '回款状态', 3, 15, 16398, 0, 1229999);
INSERT INTO `72crm_admin_field_sort` VALUES (327219, 6, '纸质合同（行政部）', '纸质合同（行政部）', 3, 16, 16398, 0, 1230000);
INSERT INTO `72crm_admin_field_sort` VALUES (327220, 6, '侦查机设备签收单记录', '侦查机设备签收单记录', 1, 17, 16398, 0, 1230001);
INSERT INTO `72crm_admin_field_sort` VALUES (327221, 6, '钉钉审批单号', '钉钉审批单号', 1, 18, 16398, 0, 1230002);
INSERT INTO `72crm_admin_field_sort` VALUES (327222, 6, '项目状态', '项目状态', 3, 19, 16398, 0, 1230003);
INSERT INTO `72crm_admin_field_sort` VALUES (327223, 6, '业绩属性', '业绩属性', 3, 20, 16398, 0, 1230004);
INSERT INTO `72crm_admin_field_sort` VALUES (327224, 6, 'checkStatus', '审核状态', 5, 21, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327225, 6, 'receivedMoney', '已收款金额', 6, 22, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327226, 6, 'unreceivedMoney', '未收款金额', 6, 23, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327227, 6, 'lastTime', '最后跟进时间', 4, 24, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327228, 6, 'ownerUserName', '负责人', 1, 25, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327229, 6, 'updateTime', '更新时间', 4, 26, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327230, 6, 'createTime', '创建时间', 4, 27, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327231, 6, 'createUserName', '创建人', 1, 28, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327232, 17, '回访日期时间', '回访日期时间', 13, 0, 16398, 0, 1230013);
INSERT INTO `72crm_admin_field_sort` VALUES (327233, 17, 'visitNumber', '回访编号', 1, 1, 16398, 0, 1230017);
INSERT INTO `72crm_admin_field_sort` VALUES (327234, 17, '回访客户姓名', '回访客户姓名', 10, 2, 16398, 0, 1230014);
INSERT INTO `72crm_admin_field_sort` VALUES (327235, 17, 'visitTime', '回访时间', 13, 3, 16398, 0, 1230018);
INSERT INTO `72crm_admin_field_sort` VALUES (327236, 17, '回访目的', '回访目的', 3, 4, 16398, 0, 1230015);
INSERT INTO `72crm_admin_field_sort` VALUES (327237, 17, 'ownerUserName', '回访人', 10, 5, 16398, 0, 1230019);
INSERT INTO `72crm_admin_field_sort` VALUES (327238, 17, '回访详情描述', '回访详情描述', 2, 6, 16398, 0, 1230016);
INSERT INTO `72crm_admin_field_sort` VALUES (327239, 17, '回访形式', '回访形式', 3, 7, 16398, 0, 1230020);
INSERT INTO `72crm_admin_field_sort` VALUES (327240, 17, 'customerName', '客户名称', 15, 8, 16398, 0, 1230021);
INSERT INTO `72crm_admin_field_sort` VALUES (327241, 17, 'contactsName', '合同编号', 17, 9, 16398, 0, 1230022);
INSERT INTO `72crm_admin_field_sort` VALUES (327242, 17, 'contractNum', '合同编号', 20, 10, 16398, 0, 1230023);
INSERT INTO `72crm_admin_field_sort` VALUES (327243, 17, '客户满意度', '客户满意度', 3, 11, 16398, 0, 1230024);
INSERT INTO `72crm_admin_field_sort` VALUES (327244, 17, '客户反馈', '客户反馈', 2, 12, 16398, 0, 1230025);
INSERT INTO `72crm_admin_field_sort` VALUES (327245, 17, 'updateTime', '更新时间', 4, 13, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327246, 17, 'createTime', '创建时间', 4, 14, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327247, 17, 'createUserName', '创建人', 1, 15, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327248, 2, 'customerName', '客户名称', 1, 0, 16408, 0, 1229736);
INSERT INTO `72crm_admin_field_sort` VALUES (327249, 2, '客户来源', '客户来源', 3, 1, 16408, 0, 1229737);
INSERT INTO `72crm_admin_field_sort` VALUES (327250, 2, 'telephone', '电话', 1, 2, 16408, 0, 1229739);
INSERT INTO `72crm_admin_field_sort` VALUES (327251, 2, 'website', '网址', 1, 3, 16408, 0, 1229740);
INSERT INTO `72crm_admin_field_sort` VALUES (327252, 2, '客户行业', '客户行业', 3, 4, 16408, 0, 1229741);
INSERT INTO `72crm_admin_field_sort` VALUES (327253, 2, '客户级别', '客户级别', 3, 5, 16408, 0, 1229742);
INSERT INTO `72crm_admin_field_sort` VALUES (327254, 2, 'nextTime', '下次联系时间', 13, 6, 16408, 0, 1229743);
INSERT INTO `72crm_admin_field_sort` VALUES (327255, 2, 'remark', '备注', 2, 7, 16408, 0, 1229744);
INSERT INTO `72crm_admin_field_sort` VALUES (327256, 2, '客户属性', '客户属性', 9, 8, 16408, 0, 1229905);
INSERT INTO `72crm_admin_field_sort` VALUES (327257, 2, '附件', '附件', 8, 9, 16408, 0, 1229937);
INSERT INTO `72crm_admin_field_sort` VALUES (327258, 2, '客户类型', '客户类型', 1, 10, 16408, 0, 1229969);
INSERT INTO `72crm_admin_field_sort` VALUES (327259, 2, '单行文本', '单行文本', 1, 11, 16408, 0, 1229971);
INSERT INTO `72crm_admin_field_sort` VALUES (327260, 2, '所属部门', '所属部门', 3, 12, 16408, 0, 1229979);
INSERT INTO `72crm_admin_field_sort` VALUES (327261, 2, '客户省份', '客户省份', 1, 13, 16408, 0, 1229980);
INSERT INTO `72crm_admin_field_sort` VALUES (327262, 2, 'status', '锁定状态', 5, 14, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327263, 2, 'dealStatus', '成交状态', 3, 15, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327264, 2, 'lastTime', '最后跟进时间', 4, 16, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327265, 2, 'lastContent', '最后跟进记录', 1, 17, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327266, 2, 'detailAddress', '详细地址', 1, 18, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327267, 2, 'address', '地区定位', 18, 19, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327268, 2, 'poolDay', '距进入公海天数', 5, 20, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327269, 2, 'ownerUserName', '负责人', 1, 21, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327270, 2, 'updateTime', '更新时间', 4, 22, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327271, 2, 'createTime', '创建时间', 4, 23, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327272, 2, 'createUserName', '创建人', 1, 24, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327273, 5, '商机编号', '商机编号', 1, 0, 16389, 0, 1230007);
INSERT INTO `72crm_admin_field_sort` VALUES (327274, 5, '所属部门', '所属部门', 3, 1, 16389, 0, 1229992);
INSERT INTO `72crm_admin_field_sort` VALUES (327275, 5, 'businessName', '商机名称', 1, 2, 16389, 0, 1229772);
INSERT INTO `72crm_admin_field_sort` VALUES (327276, 5, 'customerName', '客户名称', 15, 3, 16389, 0, 1229773);
INSERT INTO `72crm_admin_field_sort` VALUES (327277, 5, 'money', '商机合同金额', 6, 4, 16389, 0, 1229774);
INSERT INTO `72crm_admin_field_sort` VALUES (327278, 5, '商机合同有效额', '商机合同有效额', 6, 5, 16389, 0, 1229983);
INSERT INTO `72crm_admin_field_sort` VALUES (327279, 5, 'dealDate', '预计成交日期', 13, 6, 16389, 0, 1229775);
INSERT INTO `72crm_admin_field_sort` VALUES (327280, 5, '竞争对手', '竞争对手', 1, 7, 16389, 0, 1229984);
INSERT INTO `72crm_admin_field_sort` VALUES (327281, 5, '签约产品', '签约产品', 9, 8, 16389, 0, 1229989);
INSERT INTO `72crm_admin_field_sort` VALUES (327282, 5, '是否招标', '是否招标', 3, 9, 16389, 0, 1229985);
INSERT INTO `72crm_admin_field_sort` VALUES (327283, 5, '中标概率预估', '中标概率预估', 3, 10, 16389, 0, 1229986);
INSERT INTO `72crm_admin_field_sort` VALUES (327284, 5, '当年启动概率', '当年启动概率', 3, 11, 16389, 0, 1229987);
INSERT INTO `72crm_admin_field_sort` VALUES (327285, 5, '预计签约时间', '预计签约时间', 4, 12, 16389, 0, 1229988);
INSERT INTO `72crm_admin_field_sort` VALUES (327286, 5, 'remark', '客户商机需求', 2, 13, 16389, 0, 1229776);
INSERT INTO `72crm_admin_field_sort` VALUES (327287, 5, '商机洽谈阶段', '商机洽谈阶段', 3, 14, 16389, 0, 1229991);
INSERT INTO `72crm_admin_field_sort` VALUES (327288, 5, '销售负责人', '销售负责人', 1, 15, 16389, 0, 1229993);
INSERT INTO `72crm_admin_field_sort` VALUES (327289, 5, 'typeName', '商机状态组', 3, 16, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327290, 5, 'statusName', '商机阶段', 3, 17, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327291, 5, 'lastTime', '最后跟进时间', 4, 18, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327292, 5, 'ownerUserName', '负责人', 1, 19, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327293, 5, 'updateTime', '更新时间', 4, 20, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327294, 5, 'createTime', '创建时间', 4, 21, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327295, 5, 'createUserName', '创建人', 1, 22, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327296, 6, '销售区域', '销售区域', 3, 0, 16389, 0, 1229996);
INSERT INTO `72crm_admin_field_sort` VALUES (327297, 6, 'num', '合同编号', 1, 1, 16389, 0, 1229778);
INSERT INTO `72crm_admin_field_sort` VALUES (327298, 6, 'customerName', '客户名称', 15, 2, 16389, 0, 1229779);
INSERT INTO `72crm_admin_field_sort` VALUES (327299, 6, 'name', '合同甲方（盖章名称）', 1, 3, 16389, 0, 1229777);
INSERT INTO `72crm_admin_field_sort` VALUES (327300, 6, 'businessName', '商机名称', 16, 4, 16389, 0, 1229780);
INSERT INTO `72crm_admin_field_sort` VALUES (327301, 6, '签订主体', '签订主体', 3, 5, 16389, 0, 1229995);
INSERT INTO `72crm_admin_field_sort` VALUES (327302, 6, '合同类型', '合同类型', 3, 6, 16389, 0, 1229788);
INSERT INTO `72crm_admin_field_sort` VALUES (327303, 6, 'money', '合同金额', 6, 7, 16389, 0, 1229781);
INSERT INTO `72crm_admin_field_sort` VALUES (327304, 6, '合同有效额', '合同有效额', 6, 8, 16389, 0, 1229994);
INSERT INTO `72crm_admin_field_sort` VALUES (327305, 6, 'orderDate', '签约时间', 4, 9, 16389, 0, 1229782);
INSERT INTO `72crm_admin_field_sort` VALUES (327306, 6, 'companyUserName', '销售签约人', 10, 10, 16389, 0, 1229786);
INSERT INTO `72crm_admin_field_sort` VALUES (327307, 6, 'endTime', '合同结束时间', 4, 11, 16389, 0, 1229784);
INSERT INTO `72crm_admin_field_sort` VALUES (327308, 6, 'remark', '备注', 2, 12, 16389, 0, 1229787);
INSERT INTO `72crm_admin_field_sort` VALUES (327309, 6, '签订合同附件', '签订合同附件', 8, 13, 16389, 0, 1229997);
INSERT INTO `72crm_admin_field_sort` VALUES (327310, 6, '付款说明', '付款说明', 9, 14, 16389, 0, 1229998);
INSERT INTO `72crm_admin_field_sort` VALUES (327311, 6, '回款状态', '回款状态', 3, 15, 16389, 0, 1229999);
INSERT INTO `72crm_admin_field_sort` VALUES (327312, 6, '纸质合同（行政部）', '纸质合同（行政部）', 3, 16, 16389, 0, 1230000);
INSERT INTO `72crm_admin_field_sort` VALUES (327313, 6, '侦查机设备签收单记录', '侦查机设备签收单记录', 1, 17, 16389, 0, 1230001);
INSERT INTO `72crm_admin_field_sort` VALUES (327314, 6, '钉钉审批单号', '钉钉审批单号', 1, 18, 16389, 0, 1230002);
INSERT INTO `72crm_admin_field_sort` VALUES (327315, 6, '项目状态', '项目状态', 3, 19, 16389, 0, 1230003);
INSERT INTO `72crm_admin_field_sort` VALUES (327316, 6, '业绩属性', '业绩属性', 3, 20, 16389, 0, 1230004);
INSERT INTO `72crm_admin_field_sort` VALUES (327317, 6, 'checkStatus', '审核状态', 5, 21, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327318, 6, 'receivedMoney', '已收款金额', 6, 22, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327319, 6, 'unreceivedMoney', '未收款金额', 6, 23, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327320, 6, 'lastTime', '最后跟进时间', 4, 24, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327321, 6, 'ownerUserName', '负责人', 1, 25, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327322, 6, 'updateTime', '更新时间', 4, 26, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327323, 6, 'createTime', '创建时间', 4, 27, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327324, 6, 'createUserName', '创建人', 1, 28, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327325, 7, 'number', '回款编号', 1, 0, 16389, 0, 1229789);
INSERT INTO `72crm_admin_field_sort` VALUES (327326, 7, 'customerName', '客户名称', 15, 1, 16389, 0, 1229790);
INSERT INTO `72crm_admin_field_sort` VALUES (327327, 7, 'contractNum', '合同编号', 20, 2, 16389, 0, 1229791);
INSERT INTO `72crm_admin_field_sort` VALUES (327328, 7, 'planNum', '期数', 21, 3, 16389, 0, 1229792);
INSERT INTO `72crm_admin_field_sort` VALUES (327329, 7, 'returnTime', '回款日期', 4, 4, 16389, 0, 1229793);
INSERT INTO `72crm_admin_field_sort` VALUES (327330, 7, 'money', '回款金额', 6, 5, 16389, 0, 1229794);
INSERT INTO `72crm_admin_field_sort` VALUES (327331, 7, 'returnType', '回款方式', 3, 6, 16389, 0, 1229795);
INSERT INTO `72crm_admin_field_sort` VALUES (327332, 7, 'remark', '备注', 2, 7, 16389, 0, 1229796);
INSERT INTO `72crm_admin_field_sort` VALUES (327333, 7, 'checkStatus', '审核状态', 5, 8, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327334, 7, 'contractMoney', '合同金额', 6, 9, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327335, 7, 'ownerUserName', '负责人', 1, 10, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327336, 7, 'updateTime', '更新时间', 4, 11, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327337, 7, 'createTime', '创建时间', 4, 12, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327338, 7, 'createUserName', '创建人', 1, 13, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327339, 2, 'customerName', '客户名称', 1, 0, 16389, 0, 1229736);
INSERT INTO `72crm_admin_field_sort` VALUES (327340, 2, '客户来源', '客户来源', 3, 1, 16389, 0, 1229737);
INSERT INTO `72crm_admin_field_sort` VALUES (327341, 2, 'telephone', '电话', 1, 2, 16389, 0, 1229739);
INSERT INTO `72crm_admin_field_sort` VALUES (327342, 2, 'website', '网址', 1, 3, 16389, 0, 1229740);
INSERT INTO `72crm_admin_field_sort` VALUES (327343, 2, '客户行业', '客户行业', 3, 4, 16389, 0, 1229741);
INSERT INTO `72crm_admin_field_sort` VALUES (327344, 2, '客户级别', '客户级别', 3, 5, 16389, 0, 1229742);
INSERT INTO `72crm_admin_field_sort` VALUES (327345, 2, 'nextTime', '下次联系时间', 13, 6, 16389, 0, 1229743);
INSERT INTO `72crm_admin_field_sort` VALUES (327346, 2, 'remark', '备注', 2, 7, 16389, 0, 1229744);
INSERT INTO `72crm_admin_field_sort` VALUES (327347, 2, '客户属性', '客户属性', 9, 8, 16389, 0, 1229905);
INSERT INTO `72crm_admin_field_sort` VALUES (327348, 2, '附件', '附件', 8, 9, 16389, 0, 1229937);
INSERT INTO `72crm_admin_field_sort` VALUES (327349, 2, '客户类型', '客户类型', 1, 10, 16389, 0, 1229969);
INSERT INTO `72crm_admin_field_sort` VALUES (327350, 2, '单行文本', '单行文本', 1, 11, 16389, 0, 1229971);
INSERT INTO `72crm_admin_field_sort` VALUES (327351, 2, '所属部门', '所属部门', 3, 12, 16389, 0, 1229979);
INSERT INTO `72crm_admin_field_sort` VALUES (327352, 2, '客户省份', '客户省份', 1, 13, 16389, 0, 1229980);
INSERT INTO `72crm_admin_field_sort` VALUES (327353, 2, 'status', '锁定状态', 5, 14, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327354, 2, 'dealStatus', '成交状态', 3, 15, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327355, 2, 'lastTime', '最后跟进时间', 4, 16, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327356, 2, 'lastContent', '最后跟进记录', 1, 17, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327357, 2, 'detailAddress', '详细地址', 1, 18, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327358, 2, 'address', '地区定位', 18, 19, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327359, 2, 'poolDay', '距进入公海天数', 5, 20, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327360, 2, 'ownerUserName', '负责人', 1, 21, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327361, 2, 'updateTime', '更新时间', 4, 22, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327362, 2, 'createTime', '创建时间', 4, 23, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327363, 2, 'createUserName', '创建人', 1, 24, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327364, 1, '线索编号', '线索编号', 1, 0, 16389, 0, 1230005);
INSERT INTO `72crm_admin_field_sort` VALUES (327365, 1, 'leadsName', '线索名称', 1, 1, 16389, 0, 1229745);
INSERT INTO `72crm_admin_field_sort` VALUES (327366, 1, '线索来源', '线索来源', 3, 2, 16389, 0, 1229746);
INSERT INTO `72crm_admin_field_sort` VALUES (327367, 1, 'mobile', '手机', 7, 3, 16389, 0, 1229747);
INSERT INTO `72crm_admin_field_sort` VALUES (327368, 1, 'telephone', '电话', 1, 4, 16389, 0, 1229748);
INSERT INTO `72crm_admin_field_sort` VALUES (327369, 1, 'address', '地址', 1, 5, 16389, 0, 1229749);
INSERT INTO `72crm_admin_field_sort` VALUES (327370, 1, '客户行业', '客户行业', 3, 6, 16389, 0, 1229750);
INSERT INTO `72crm_admin_field_sort` VALUES (327371, 1, '客户级别', '客户级别', 3, 7, 16389, 0, 1229751);
INSERT INTO `72crm_admin_field_sort` VALUES (327372, 1, 'nextTime', '下次联系时间', 13, 8, 16389, 0, 1229752);
INSERT INTO `72crm_admin_field_sort` VALUES (327373, 1, 'remark', '备注', 2, 9, 16389, 0, 1229753);
INSERT INTO `72crm_admin_field_sort` VALUES (327374, 1, '人员', '人员', 10, 10, 16389, 0, 1229970);
INSERT INTO `72crm_admin_field_sort` VALUES (327375, 1, '部门', '部门', 12, 11, 16389, 0, 1229972);
INSERT INTO `72crm_admin_field_sort` VALUES (327376, 1, '测试', '测试', 1, 12, 16389, 0, 1229973);
INSERT INTO `72crm_admin_field_sort` VALUES (327377, 1, '测试多行', '测试多行', 2, 13, 16389, 0, 1229974);
INSERT INTO `72crm_admin_field_sort` VALUES (327378, 1, '下拉框', '下拉框', 3, 14, 16389, 0, 1229975);
INSERT INTO `72crm_admin_field_sort` VALUES (327379, 1, '邮箱', '邮箱', 14, 15, 16389, 0, 1229976);
INSERT INTO `72crm_admin_field_sort` VALUES (327380, 1, 'lastTime', '最后跟进时间', 4, 16, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327381, 1, 'lastContent', '最后跟进记录', 1, 17, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327382, 1, 'ownerUserName', '负责人', 1, 18, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327383, 1, 'updateTime', '更新时间', 4, 19, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327384, 1, 'createTime', '创建时间', 4, 20, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327385, 1, 'createUserName', '创建人', 1, 21, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327386, 3, 'customerName', '客户名称', 15, 0, 16389, 0, 1229755);
INSERT INTO `72crm_admin_field_sort` VALUES (327387, 3, 'name', '姓名', 1, 1, 16389, 0, 1229754);
INSERT INTO `72crm_admin_field_sort` VALUES (327388, 3, '性别', '性别', 3, 2, 16389, 0, 1229764);
INSERT INTO `72crm_admin_field_sort` VALUES (327389, 3, '部门', '部门', 1, 3, 16389, 0, 1229981);
INSERT INTO `72crm_admin_field_sort` VALUES (327390, 3, 'mobile', '手机', 7, 4, 16389, 0, 1229756);
INSERT INTO `72crm_admin_field_sort` VALUES (327391, 3, 'telephone', '电话', 1, 5, 16389, 0, 1229757);
INSERT INTO `72crm_admin_field_sort` VALUES (327392, 3, 'email', '邮箱', 14, 6, 16389, 0, 1229758);
INSERT INTO `72crm_admin_field_sort` VALUES (327393, 3, 'post', '职务', 1, 7, 16389, 0, 1229759);
INSERT INTO `72crm_admin_field_sort` VALUES (327394, 3, '是否关键决策人', '是否关键决策人', 3, 8, 16389, 0, 1229760);
INSERT INTO `72crm_admin_field_sort` VALUES (327395, 3, 'address', '地址', 1, 9, 16389, 0, 1229761);
INSERT INTO `72crm_admin_field_sort` VALUES (327396, 3, 'nextTime', '下次联系时间', 13, 10, 16389, 0, 1229762);
INSERT INTO `72crm_admin_field_sort` VALUES (327397, 3, 'remark', '备注', 2, 11, 16389, 0, 1229763);
INSERT INTO `72crm_admin_field_sort` VALUES (327398, 3, 'lastTime', '最后跟进时间', 4, 12, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327399, 3, 'ownerUserName', '负责人', 1, 13, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327400, 3, 'updateTime', '更新时间', 4, 14, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327401, 3, 'createTime', '创建时间', 4, 15, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327402, 3, 'createUserName', '创建人', 1, 16, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327403, 2, 'customerName', '客户名称', 1, 0, 16411, 0, 1229736);
INSERT INTO `72crm_admin_field_sort` VALUES (327404, 2, '客户来源', '客户来源', 3, 1, 16411, 0, 1229737);
INSERT INTO `72crm_admin_field_sort` VALUES (327405, 2, 'telephone', '电话', 1, 2, 16411, 0, 1229739);
INSERT INTO `72crm_admin_field_sort` VALUES (327406, 2, 'website', '网址', 1, 3, 16411, 0, 1229740);
INSERT INTO `72crm_admin_field_sort` VALUES (327407, 2, '客户行业', '客户行业', 3, 4, 16411, 0, 1229741);
INSERT INTO `72crm_admin_field_sort` VALUES (327408, 2, '客户级别', '客户级别', 3, 5, 16411, 0, 1229742);
INSERT INTO `72crm_admin_field_sort` VALUES (327409, 2, 'nextTime', '下次联系时间', 13, 6, 16411, 0, 1229743);
INSERT INTO `72crm_admin_field_sort` VALUES (327410, 2, 'remark', '备注', 2, 7, 16411, 0, 1229744);
INSERT INTO `72crm_admin_field_sort` VALUES (327411, 2, '客户属性', '客户属性', 9, 8, 16411, 0, 1229905);
INSERT INTO `72crm_admin_field_sort` VALUES (327412, 2, '附件', '附件', 8, 9, 16411, 0, 1229937);
INSERT INTO `72crm_admin_field_sort` VALUES (327413, 2, '客户类型', '客户类型', 1, 10, 16411, 0, 1229969);
INSERT INTO `72crm_admin_field_sort` VALUES (327414, 2, '单行文本', '单行文本', 1, 11, 16411, 0, 1229971);
INSERT INTO `72crm_admin_field_sort` VALUES (327415, 2, '所属部门', '所属部门', 3, 12, 16411, 0, 1229979);
INSERT INTO `72crm_admin_field_sort` VALUES (327416, 2, '客户省份', '客户省份', 1, 13, 16411, 0, 1229980);
INSERT INTO `72crm_admin_field_sort` VALUES (327417, 2, 'status', '锁定状态', 5, 14, 16411, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327418, 2, 'dealStatus', '成交状态', 3, 15, 16411, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327419, 2, 'lastTime', '最后跟进时间', 4, 16, 16411, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327420, 2, 'lastContent', '最后跟进记录', 1, 17, 16411, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327421, 2, 'detailAddress', '详细地址', 1, 18, 16411, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327422, 2, 'address', '地区定位', 18, 19, 16411, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327423, 2, 'poolDay', '距进入公海天数', 5, 20, 16411, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327424, 2, 'ownerUserName', '负责人', 1, 21, 16411, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327425, 2, 'updateTime', '更新时间', 4, 22, 16411, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327426, 2, 'createTime', '创建时间', 4, 23, 16411, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327427, 2, 'createUserName', '创建人', 1, 24, 16411, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327428, 19, 'workNum', '项目编号', 1, 0, 16408, 0, 1229903);
INSERT INTO `72crm_admin_field_sort` VALUES (327429, 19, 'workName', '项目名称', 1, 1, 16408, 0, 1229828);
INSERT INTO `72crm_admin_field_sort` VALUES (327430, 19, 'deptName', '部门', 12, 2, 16408, 0, 1229833);
INSERT INTO `72crm_admin_field_sort` VALUES (327431, 19, 'areaName', '区域', 12, 3, 16408, 0, 1229829);
INSERT INTO `72crm_admin_field_sort` VALUES (327432, 19, 'managerName', '项目经理', 28, 4, 16408, 0, 1229834);
INSERT INTO `72crm_admin_field_sort` VALUES (327433, 19, 'salesName', '销售负责人', 28, 5, 16408, 0, 1229835);
INSERT INTO `72crm_admin_field_sort` VALUES (327434, 19, 'workPhase', '项目阶段', 3, 6, 16408, 0, 1229830);
INSERT INTO `72crm_admin_field_sort` VALUES (327435, 19, 'workType', '项目类型', 3, 7, 16408, 0, 1229836);
INSERT INTO `72crm_admin_field_sort` VALUES (327436, 19, 'startTime', '项目开始时间', 13, 8, 16408, 0, 1229832);
INSERT INTO `72crm_admin_field_sort` VALUES (327437, 19, 'endTime', '项目结束时间', 13, 9, 16408, 0, 1229838);
INSERT INTO `72crm_admin_field_sort` VALUES (327438, 19, 'updateTime', '更新时间', 4, 10, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327439, 19, 'createTime', '创建时间', 4, 11, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327440, 19, 'createUserName', '创建人', 1, 12, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327441, 21, 'workName', '项目名称', 1, 0, 16389, 0, 1229860);
INSERT INTO `72crm_admin_field_sort` VALUES (327442, 21, 'beforeName', '前置任务', 29, 1, 16389, 0, 1229861);
INSERT INTO `72crm_admin_field_sort` VALUES (327443, 21, 'taskNum', '任务编号', 1, 2, 16389, 0, 1229877);
INSERT INTO `72crm_admin_field_sort` VALUES (327444, 21, 'milestoneName', '里程碑', 30, 3, 16389, 0, 1229862);
INSERT INTO `72crm_admin_field_sort` VALUES (327445, 21, 'taskName', '任务名称', 1, 4, 16389, 0, 1229864);
INSERT INTO `72crm_admin_field_sort` VALUES (327446, 21, 'classId', '任务类型', 3, 5, 16389, 0, 1229863);
INSERT INTO `72crm_admin_field_sort` VALUES (327447, 21, 'completeness', '任务完成度(%)', 5, 6, 16389, 0, 1229866);
INSERT INTO `72crm_admin_field_sort` VALUES (327448, 21, 'status', '任务状态', 3, 7, 16389, 0, 1229865);
INSERT INTO `72crm_admin_field_sort` VALUES (327449, 21, 'mainUserName', '负责人', 28, 8, 16389, 0, 1229869);
INSERT INTO `72crm_admin_field_sort` VALUES (327450, 21, 'ownerUserName', '参与人', 10, 9, 16389, 0, 1229870);
INSERT INTO `72crm_admin_field_sort` VALUES (327451, 21, 'priority', '优先级', 3, 10, 16389, 0, 1229872);
INSERT INTO `72crm_admin_field_sort` VALUES (327452, 21, 'difficulty', '任务难度', 3, 11, 16389, 0, 1229873);
INSERT INTO `72crm_admin_field_sort` VALUES (327453, 21, 'degree', '重要程度', 3, 12, 16389, 0, 1229874);
INSERT INTO `72crm_admin_field_sort` VALUES (327454, 21, 'startTime', '计划开始时间', 13, 13, 16389, 0, 1229867);
INSERT INTO `72crm_admin_field_sort` VALUES (327455, 21, 'endTime', '计划结束时间', 13, 14, 16389, 0, 1229868);
INSERT INTO `72crm_admin_field_sort` VALUES (327456, 21, 'description', '任务描述', 2, 15, 16389, 0, 1229875);
INSERT INTO `72crm_admin_field_sort` VALUES (327457, 21, 'updateTime', '更新时间', 4, 16, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327458, 21, 'createTime', '创建时间', 4, 17, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327459, 21, 'createUserName', '创建人', 1, 18, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327460, 29, 'taskName', '子任务名称', 1, 0, 16389, 0, 1229950);
INSERT INTO `72crm_admin_field_sort` VALUES (327461, 29, 'status', '子任务状态', 3, 1, 16389, 0, 1229951);
INSERT INTO `72crm_admin_field_sort` VALUES (327462, 29, 'milestoneName', '里程碑', 30, 2, 16389, 0, 1229948);
INSERT INTO `72crm_admin_field_sort` VALUES (327463, 29, 'pName', '所属任务', 29, 3, 16389, 0, 1229947);
INSERT INTO `72crm_admin_field_sort` VALUES (327464, 29, 'classId', '任务类型', 3, 4, 16389, 0, 1229949);
INSERT INTO `72crm_admin_field_sort` VALUES (327465, 29, 'completeness', '任务完成度(%)', 5, 5, 16389, 0, 1229952);
INSERT INTO `72crm_admin_field_sort` VALUES (327466, 29, 'priority', '优先级', 3, 6, 16389, 0, 1229957);
INSERT INTO `72crm_admin_field_sort` VALUES (327467, 29, 'difficulty', '任务难度', 3, 7, 16389, 0, 1229958);
INSERT INTO `72crm_admin_field_sort` VALUES (327468, 29, 'degree', '重要程度', 3, 8, 16389, 0, 1229959);
INSERT INTO `72crm_admin_field_sort` VALUES (327469, 29, 'mainUserName', '负责人', 28, 9, 16389, 0, 1229955);
INSERT INTO `72crm_admin_field_sort` VALUES (327470, 29, 'ownerUserName', '参与人', 10, 10, 16389, 0, 1229956);
INSERT INTO `72crm_admin_field_sort` VALUES (327471, 29, 'startTime', '计划开始时间', 13, 11, 16389, 0, 1229953);
INSERT INTO `72crm_admin_field_sort` VALUES (327472, 29, 'endTime', '计划结束时间', 13, 12, 16389, 0, 1229954);
INSERT INTO `72crm_admin_field_sort` VALUES (327473, 19, 'workNum', '项目编号', 1, 0, 16360, 0, 1229903);
INSERT INTO `72crm_admin_field_sort` VALUES (327474, 19, 'workName', '项目名称', 1, 1, 16360, 0, 1229828);
INSERT INTO `72crm_admin_field_sort` VALUES (327475, 19, 'deptName', '部门', 12, 2, 16360, 0, 1229833);
INSERT INTO `72crm_admin_field_sort` VALUES (327476, 19, 'areaName', '区域', 12, 3, 16360, 0, 1229829);
INSERT INTO `72crm_admin_field_sort` VALUES (327477, 19, 'managerName', '项目经理', 28, 4, 16360, 0, 1229834);
INSERT INTO `72crm_admin_field_sort` VALUES (327478, 19, 'salesName', '销售负责人', 28, 5, 16360, 0, 1229835);
INSERT INTO `72crm_admin_field_sort` VALUES (327479, 19, 'workPhase', '项目阶段', 3, 6, 16360, 0, 1229830);
INSERT INTO `72crm_admin_field_sort` VALUES (327480, 19, 'workType', '项目类型', 3, 7, 16360, 0, 1229836);
INSERT INTO `72crm_admin_field_sort` VALUES (327481, 19, 'startTime', '项目开始时间', 13, 8, 16360, 0, 1229832);
INSERT INTO `72crm_admin_field_sort` VALUES (327482, 19, 'endTime', '项目结束时间', 13, 9, 16360, 0, 1229838);
INSERT INTO `72crm_admin_field_sort` VALUES (327483, 19, 'updateTime', '更新时间', 4, 10, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327484, 19, 'createTime', '创建时间', 4, 11, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327485, 19, 'createUserName', '创建人', 1, 12, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327486, 19, 'workNum', '项目编号', 1, 0, 16411, 0, 1229903);
INSERT INTO `72crm_admin_field_sort` VALUES (327487, 19, 'workName', '项目名称', 1, 1, 16411, 0, 1229828);
INSERT INTO `72crm_admin_field_sort` VALUES (327488, 19, 'deptName', '部门', 12, 2, 16411, 0, 1229833);
INSERT INTO `72crm_admin_field_sort` VALUES (327489, 19, 'areaName', '区域', 12, 3, 16411, 0, 1229829);
INSERT INTO `72crm_admin_field_sort` VALUES (327490, 19, 'managerName', '项目经理', 28, 4, 16411, 0, 1229834);
INSERT INTO `72crm_admin_field_sort` VALUES (327491, 19, 'salesName', '销售负责人', 28, 5, 16411, 0, 1229835);
INSERT INTO `72crm_admin_field_sort` VALUES (327492, 19, 'workPhase', '项目阶段', 3, 6, 16411, 0, 1229830);
INSERT INTO `72crm_admin_field_sort` VALUES (327493, 19, 'workType', '项目类型', 3, 7, 16411, 0, 1229836);
INSERT INTO `72crm_admin_field_sort` VALUES (327494, 19, 'startTime', '项目开始时间', 13, 8, 16411, 0, 1229832);
INSERT INTO `72crm_admin_field_sort` VALUES (327495, 19, 'endTime', '项目结束时间', 13, 9, 16411, 0, 1229838);
INSERT INTO `72crm_admin_field_sort` VALUES (327496, 19, 'updateTime', '更新时间', 4, 10, 16411, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327497, 19, 'createTime', '创建时间', 4, 11, 16411, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327498, 19, 'createUserName', '创建人', 1, 12, 16411, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327499, 24, 'subsystem', '子系统', 1, 0, 16408, 0, 1229910);
INSERT INTO `72crm_admin_field_sort` VALUES (327500, 24, 'childModule', '子模块', 1, 1, 16408, 0, 1229911);
INSERT INTO `72crm_admin_field_sort` VALUES (327501, 24, 'function1', '一级功能', 1, 2, 16408, 0, 1229912);
INSERT INTO `72crm_admin_field_sort` VALUES (327502, 24, 'function2', '二级功能', 1, 3, 16408, 0, 1229913);
INSERT INTO `72crm_admin_field_sort` VALUES (327503, 24, 'demandDescribe', '需求描述', 2, 4, 16408, 0, 1229914);
INSERT INTO `72crm_admin_field_sort` VALUES (327504, 24, 'demandStatus', '完成状态(%)', 6, 5, 16408, 0, 1229924);
INSERT INTO `72crm_admin_field_sort` VALUES (327505, 24, 'demandVersion', '需求版本号', 1, 6, 16408, 0, 1229915);
INSERT INTO `72crm_admin_field_sort` VALUES (327506, 24, 'plan', '对标投标方案号', 1, 7, 16408, 0, 1229925);
INSERT INTO `72crm_admin_field_sort` VALUES (327507, 24, 'demandNumber', '对标需求设计序号', 1, 8, 16408, 0, 1229916);
INSERT INTO `72crm_admin_field_sort` VALUES (327508, 24, 'productName', '对标产品名称', 1, 9, 16408, 0, 1229917);
INSERT INTO `72crm_admin_field_sort` VALUES (327509, 24, 'productVersion', '对标产品版本', 1, 10, 16408, 0, 1229918);
INSERT INTO `72crm_admin_field_sort` VALUES (327510, 24, 'productNumber', '产品编号', 1, 11, 16408, 0, 1229919);
INSERT INTO `72crm_admin_field_sort` VALUES (327511, 24, 'demandType', '需求类型', 3, 12, 16408, 0, 1229926);
INSERT INTO `72crm_admin_field_sort` VALUES (327512, 24, 'functionNumber', '功能编号', 1, 13, 16408, 0, 1229921);
INSERT INTO `72crm_admin_field_sort` VALUES (327513, 24, 'functionName', '功能名称', 1, 14, 16408, 0, 1229920);
INSERT INTO `72crm_admin_field_sort` VALUES (327514, 21, 'workName', '项目名称', 1, 0, 16408, 0, 1229860);
INSERT INTO `72crm_admin_field_sort` VALUES (327515, 21, 'beforeName', '前置任务', 29, 1, 16408, 0, 1229861);
INSERT INTO `72crm_admin_field_sort` VALUES (327516, 21, 'taskNum', '任务编号', 1, 2, 16408, 0, 1229877);
INSERT INTO `72crm_admin_field_sort` VALUES (327517, 21, 'milestoneName', '里程碑', 30, 3, 16408, 0, 1229862);
INSERT INTO `72crm_admin_field_sort` VALUES (327518, 21, 'taskName', '任务名称', 1, 4, 16408, 0, 1229864);
INSERT INTO `72crm_admin_field_sort` VALUES (327519, 21, 'classId', '任务类型', 3, 5, 16408, 0, 1229863);
INSERT INTO `72crm_admin_field_sort` VALUES (327520, 21, 'completeness', '任务完成度(%)', 5, 6, 16408, 0, 1229866);
INSERT INTO `72crm_admin_field_sort` VALUES (327521, 21, 'status', '任务状态', 3, 7, 16408, 0, 1229865);
INSERT INTO `72crm_admin_field_sort` VALUES (327522, 21, 'mainUserName', '负责人', 28, 8, 16408, 0, 1229869);
INSERT INTO `72crm_admin_field_sort` VALUES (327523, 21, 'ownerUserName', '参与人', 10, 9, 16408, 0, 1229870);
INSERT INTO `72crm_admin_field_sort` VALUES (327524, 21, 'priority', '优先级', 3, 10, 16408, 0, 1229872);
INSERT INTO `72crm_admin_field_sort` VALUES (327525, 21, 'difficulty', '任务难度', 3, 11, 16408, 0, 1229873);
INSERT INTO `72crm_admin_field_sort` VALUES (327526, 21, 'degree', '重要程度', 3, 12, 16408, 0, 1229874);
INSERT INTO `72crm_admin_field_sort` VALUES (327527, 21, 'startTime', '计划开始时间', 13, 13, 16408, 0, 1229867);
INSERT INTO `72crm_admin_field_sort` VALUES (327528, 21, 'endTime', '计划结束时间', 13, 14, 16408, 0, 1229868);
INSERT INTO `72crm_admin_field_sort` VALUES (327529, 21, 'description', '任务描述', 2, 15, 16408, 0, 1229875);
INSERT INTO `72crm_admin_field_sort` VALUES (327530, 21, 'updateTime', '更新时间', 4, 16, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327531, 21, 'createTime', '创建时间', 4, 17, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327532, 21, 'createUserName', '创建人', 1, 18, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327533, 20, 'milestoneNum', '里程碑编号', 1, 0, 16408, 0, 1229844);
INSERT INTO `72crm_admin_field_sort` VALUES (327534, 20, 'workName', '项目名称', 1, 1, 16408, 0, 1229909);
INSERT INTO `72crm_admin_field_sort` VALUES (327535, 20, 'workNum', '项目编号', 1, 2, 16408, 0, 1229846);
INSERT INTO `72crm_admin_field_sort` VALUES (327536, 20, 'milestoneName', '里程碑名称', 1, 3, 16408, 0, 1229845);
INSERT INTO `72crm_admin_field_sort` VALUES (327537, 20, 'instructions', '详细说明', 2, 4, 16408, 0, 1229852);
INSERT INTO `72crm_admin_field_sort` VALUES (327538, 20, 'degree', '完成度(%)', 6, 5, 16408, 0, 1229907);
INSERT INTO `72crm_admin_field_sort` VALUES (327539, 20, 'startTime', '开始时间', 13, 6, 16408, 0, 1229849);
INSERT INTO `72crm_admin_field_sort` VALUES (327540, 20, 'endTime', '结束时间', 13, 7, 16408, 0, 1229850);
INSERT INTO `72crm_admin_field_sort` VALUES (327541, 20, 'mainUserName', '负责人', 28, 8, 16408, 0, 1229851);
INSERT INTO `72crm_admin_field_sort` VALUES (327542, 20, 'taskName', '前置任务', 29, 9, 16408, 0, 1229906);
INSERT INTO `72crm_admin_field_sort` VALUES (327543, 20, 'updateTime', '更新时间', 4, 10, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327544, 20, 'createTime', '创建时间', 4, 11, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327545, 20, 'createUserName', '创建人', 1, 12, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327546, 3, 'customerName', '客户名称', 15, 0, 16408, 0, 1229755);
INSERT INTO `72crm_admin_field_sort` VALUES (327547, 3, 'name', '姓名', 1, 1, 16408, 0, 1229754);
INSERT INTO `72crm_admin_field_sort` VALUES (327548, 3, '性别', '性别', 3, 2, 16408, 0, 1229764);
INSERT INTO `72crm_admin_field_sort` VALUES (327549, 3, '部门', '部门', 1, 3, 16408, 0, 1229981);
INSERT INTO `72crm_admin_field_sort` VALUES (327550, 3, 'mobile', '手机', 7, 4, 16408, 0, 1229756);
INSERT INTO `72crm_admin_field_sort` VALUES (327551, 3, 'telephone', '电话', 1, 5, 16408, 0, 1229757);
INSERT INTO `72crm_admin_field_sort` VALUES (327552, 3, 'email', '邮箱', 14, 6, 16408, 0, 1229758);
INSERT INTO `72crm_admin_field_sort` VALUES (327553, 3, 'post', '职务', 1, 7, 16408, 0, 1229759);
INSERT INTO `72crm_admin_field_sort` VALUES (327554, 3, '是否关键决策人', '是否关键决策人', 3, 8, 16408, 0, 1229760);
INSERT INTO `72crm_admin_field_sort` VALUES (327555, 3, 'address', '地址', 1, 9, 16408, 0, 1229761);
INSERT INTO `72crm_admin_field_sort` VALUES (327556, 3, 'nextTime', '下次联系时间', 13, 10, 16408, 0, 1229762);
INSERT INTO `72crm_admin_field_sort` VALUES (327557, 3, 'remark', '备注', 2, 11, 16408, 0, 1229763);
INSERT INTO `72crm_admin_field_sort` VALUES (327558, 3, 'lastTime', '最后跟进时间', 4, 12, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327559, 3, 'ownerUserName', '负责人', 1, 13, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327560, 3, 'updateTime', '更新时间', 4, 14, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327561, 3, 'createTime', '创建时间', 4, 15, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327562, 3, 'createUserName', '创建人', 1, 16, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327563, 5, '商机编号', '商机编号', 1, 0, 16408, 0, 1230007);
INSERT INTO `72crm_admin_field_sort` VALUES (327564, 5, '所属部门', '所属部门', 3, 1, 16408, 0, 1229992);
INSERT INTO `72crm_admin_field_sort` VALUES (327565, 5, 'businessName', '商机名称', 1, 2, 16408, 0, 1229772);
INSERT INTO `72crm_admin_field_sort` VALUES (327566, 5, 'customerName', '客户名称', 15, 3, 16408, 0, 1229773);
INSERT INTO `72crm_admin_field_sort` VALUES (327567, 5, 'money', '商机合同金额', 6, 4, 16408, 0, 1229774);
INSERT INTO `72crm_admin_field_sort` VALUES (327568, 5, '商机合同有效额', '商机合同有效额', 6, 5, 16408, 0, 1229983);
INSERT INTO `72crm_admin_field_sort` VALUES (327569, 5, 'dealDate', '预计成交日期', 13, 6, 16408, 0, 1229775);
INSERT INTO `72crm_admin_field_sort` VALUES (327570, 5, '竞争对手', '竞争对手', 1, 7, 16408, 0, 1229984);
INSERT INTO `72crm_admin_field_sort` VALUES (327571, 5, '签约产品', '签约产品', 9, 8, 16408, 0, 1229989);
INSERT INTO `72crm_admin_field_sort` VALUES (327572, 5, '是否招标', '是否招标', 3, 9, 16408, 0, 1229985);
INSERT INTO `72crm_admin_field_sort` VALUES (327573, 5, '中标概率预估', '中标概率预估', 3, 10, 16408, 0, 1229986);
INSERT INTO `72crm_admin_field_sort` VALUES (327574, 5, '当年启动概率', '当年启动概率', 3, 11, 16408, 0, 1229987);
INSERT INTO `72crm_admin_field_sort` VALUES (327575, 5, '预计签约时间', '预计签约时间', 4, 12, 16408, 0, 1229988);
INSERT INTO `72crm_admin_field_sort` VALUES (327576, 5, 'remark', '客户商机需求', 2, 13, 16408, 0, 1229776);
INSERT INTO `72crm_admin_field_sort` VALUES (327577, 5, '商机洽谈阶段', '商机洽谈阶段', 3, 14, 16408, 0, 1229991);
INSERT INTO `72crm_admin_field_sort` VALUES (327578, 5, '销售负责人', '销售负责人', 1, 15, 16408, 0, 1229993);
INSERT INTO `72crm_admin_field_sort` VALUES (327579, 5, 'typeName', '商机状态组', 3, 16, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327580, 5, 'statusName', '商机阶段', 3, 17, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327581, 5, 'lastTime', '最后跟进时间', 4, 18, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327582, 5, 'ownerUserName', '负责人', 1, 19, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327583, 5, 'updateTime', '更新时间', 4, 20, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327584, 5, 'createTime', '创建时间', 4, 21, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327585, 5, 'createUserName', '创建人', 1, 22, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327586, 6, '销售区域', '销售区域', 3, 0, 16408, 0, 1229996);
INSERT INTO `72crm_admin_field_sort` VALUES (327587, 6, 'num', '合同编号', 1, 1, 16408, 0, 1229778);
INSERT INTO `72crm_admin_field_sort` VALUES (327588, 6, 'customerName', '客户名称', 15, 2, 16408, 0, 1229779);
INSERT INTO `72crm_admin_field_sort` VALUES (327589, 6, 'name', '合同甲方（盖章名称）', 1, 3, 16408, 0, 1229777);
INSERT INTO `72crm_admin_field_sort` VALUES (327590, 6, 'businessName', '商机名称', 16, 4, 16408, 0, 1229780);
INSERT INTO `72crm_admin_field_sort` VALUES (327591, 6, '签订主体', '签订主体', 3, 5, 16408, 0, 1229995);
INSERT INTO `72crm_admin_field_sort` VALUES (327592, 6, '合同类型', '合同类型', 3, 6, 16408, 0, 1229788);
INSERT INTO `72crm_admin_field_sort` VALUES (327593, 6, 'money', '合同金额', 6, 7, 16408, 0, 1229781);
INSERT INTO `72crm_admin_field_sort` VALUES (327594, 6, '合同有效额', '合同有效额', 6, 8, 16408, 0, 1229994);
INSERT INTO `72crm_admin_field_sort` VALUES (327595, 6, 'orderDate', '签约时间', 4, 9, 16408, 0, 1229782);
INSERT INTO `72crm_admin_field_sort` VALUES (327596, 6, 'companyUserName', '销售签约人', 10, 10, 16408, 0, 1229786);
INSERT INTO `72crm_admin_field_sort` VALUES (327597, 6, 'endTime', '合同结束时间', 4, 11, 16408, 0, 1229784);
INSERT INTO `72crm_admin_field_sort` VALUES (327598, 6, 'remark', '备注', 2, 12, 16408, 0, 1229787);
INSERT INTO `72crm_admin_field_sort` VALUES (327599, 6, '签订合同附件', '签订合同附件', 8, 13, 16408, 0, 1229997);
INSERT INTO `72crm_admin_field_sort` VALUES (327600, 6, '付款说明', '付款说明', 9, 14, 16408, 0, 1229998);
INSERT INTO `72crm_admin_field_sort` VALUES (327601, 6, '回款状态', '回款状态', 3, 15, 16408, 0, 1229999);
INSERT INTO `72crm_admin_field_sort` VALUES (327602, 6, '纸质合同（行政部）', '纸质合同（行政部）', 3, 16, 16408, 0, 1230000);
INSERT INTO `72crm_admin_field_sort` VALUES (327603, 6, '侦查机设备签收单记录', '侦查机设备签收单记录', 1, 17, 16408, 0, 1230001);
INSERT INTO `72crm_admin_field_sort` VALUES (327604, 6, '钉钉审批单号', '钉钉审批单号', 1, 18, 16408, 0, 1230002);
INSERT INTO `72crm_admin_field_sort` VALUES (327605, 6, '项目状态', '项目状态', 3, 19, 16408, 0, 1230003);
INSERT INTO `72crm_admin_field_sort` VALUES (327606, 6, '业绩属性', '业绩属性', 3, 20, 16408, 0, 1230004);
INSERT INTO `72crm_admin_field_sort` VALUES (327607, 6, 'checkStatus', '审核状态', 5, 21, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327608, 6, 'receivedMoney', '已收款金额', 6, 22, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327609, 6, 'unreceivedMoney', '未收款金额', 6, 23, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327610, 6, 'lastTime', '最后跟进时间', 4, 24, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327611, 6, 'ownerUserName', '负责人', 1, 25, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327612, 6, 'updateTime', '更新时间', 4, 26, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327613, 6, 'createTime', '创建时间', 4, 27, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327614, 6, 'createUserName', '创建人', 1, 28, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327615, 7, 'number', '回款编号', 1, 0, 16408, 0, 1229789);
INSERT INTO `72crm_admin_field_sort` VALUES (327616, 7, 'customerName', '客户名称', 15, 1, 16408, 0, 1229790);
INSERT INTO `72crm_admin_field_sort` VALUES (327617, 7, 'contractNum', '合同编号', 20, 2, 16408, 0, 1229791);
INSERT INTO `72crm_admin_field_sort` VALUES (327618, 7, 'planNum', '期数', 21, 3, 16408, 0, 1229792);
INSERT INTO `72crm_admin_field_sort` VALUES (327619, 7, 'returnTime', '回款日期', 4, 4, 16408, 0, 1229793);
INSERT INTO `72crm_admin_field_sort` VALUES (327620, 7, 'money', '回款金额', 6, 5, 16408, 0, 1229794);
INSERT INTO `72crm_admin_field_sort` VALUES (327621, 7, 'returnType', '回款方式', 3, 6, 16408, 0, 1229795);
INSERT INTO `72crm_admin_field_sort` VALUES (327622, 7, 'remark', '备注', 2, 7, 16408, 0, 1229796);
INSERT INTO `72crm_admin_field_sort` VALUES (327623, 7, 'checkStatus', '审核状态', 5, 8, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327624, 7, 'contractMoney', '合同金额', 6, 9, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327625, 7, 'ownerUserName', '负责人', 1, 10, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327626, 7, 'updateTime', '更新时间', 4, 11, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327627, 7, 'createTime', '创建时间', 4, 12, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327628, 7, 'createUserName', '创建人', 1, 13, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327629, 4, 'name', '产品名称', 1, 0, 16408, 0, 1229765);
INSERT INTO `72crm_admin_field_sort` VALUES (327630, 4, 'categoryName', '产品类型', 19, 1, 16408, 0, 1229766);
INSERT INTO `72crm_admin_field_sort` VALUES (327631, 4, '单位', '单位', 3, 2, 16408, 0, 1229767);
INSERT INTO `72crm_admin_field_sort` VALUES (327632, 4, 'price', '价格', 6, 3, 16408, 0, 1229769);
INSERT INTO `72crm_admin_field_sort` VALUES (327633, 4, '是否上下架', '是否上下架', 3, 4, 16408, 0, 1229770);
INSERT INTO `72crm_admin_field_sort` VALUES (327634, 4, 'description', '销售产品描述', 1, 5, 16408, 0, 1229771);
INSERT INTO `72crm_admin_field_sort` VALUES (327635, 4, '数量', '数量', 5, 6, 16408, 0, 1230012);
INSERT INTO `72crm_admin_field_sort` VALUES (327636, 4, 'ownerUserName', '负责人', 1, 7, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327637, 4, 'updateTime', '更新时间', 4, 8, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327638, 4, 'createTime', '创建时间', 4, 9, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327639, 4, 'createUserName', '创建人', 1, 10, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327640, 30, 'configItems', '配置项', 1, 0, 16389, 0, 1229960);
INSERT INTO `72crm_admin_field_sort` VALUES (327641, 30, 'planTime', '计划时间', 13, 1, 16389, 0, 1229961);
INSERT INTO `72crm_admin_field_sort` VALUES (327642, 30, 'mainUserName', '负责人', 28, 2, 16389, 0, 1229962);
INSERT INTO `72crm_admin_field_sort` VALUES (327643, 30, 'associatedName', '项目名称', 31, 3, 16389, 0, 1229963);
INSERT INTO `72crm_admin_field_sort` VALUES (327644, 30, 'associatedNum', '项目编号', 1, 4, 16389, 0, 1229964);
INSERT INTO `72crm_admin_field_sort` VALUES (327645, 30, 'version', '版本', 1, 5, 16389, 0, 1229966);
INSERT INTO `72crm_admin_field_sort` VALUES (327646, 30, 'versionTime', '版本时间', 13, 6, 16389, 0, 1229967);
INSERT INTO `72crm_admin_field_sort` VALUES (327647, 30, 'versionDescribe', '版本说明', 2, 7, 16389, 0, 1229968);
INSERT INTO `72crm_admin_field_sort` VALUES (327648, 30, 'updateTime', '更新时间', 4, 8, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327649, 30, 'createTime', '创建时间', 4, 9, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327650, 30, 'createUserName', '创建人', 1, 10, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327651, 24, 'subsystem', '子系统', 1, 0, 16389, 0, 1229910);
INSERT INTO `72crm_admin_field_sort` VALUES (327652, 24, 'childModule', '子模块', 1, 1, 16389, 0, 1229911);
INSERT INTO `72crm_admin_field_sort` VALUES (327653, 24, 'function1', '一级功能', 1, 2, 16389, 0, 1229912);
INSERT INTO `72crm_admin_field_sort` VALUES (327654, 24, 'function2', '二级功能', 1, 3, 16389, 0, 1229913);
INSERT INTO `72crm_admin_field_sort` VALUES (327655, 24, 'demandDescribe', '需求描述', 2, 4, 16389, 0, 1229914);
INSERT INTO `72crm_admin_field_sort` VALUES (327656, 24, 'demandStatus', '完成状态(%)', 6, 5, 16389, 0, 1229924);
INSERT INTO `72crm_admin_field_sort` VALUES (327657, 24, 'demandVersion', '需求版本号', 1, 6, 16389, 0, 1229915);
INSERT INTO `72crm_admin_field_sort` VALUES (327658, 24, 'plan', '对标投标方案号', 1, 7, 16389, 0, 1229925);
INSERT INTO `72crm_admin_field_sort` VALUES (327659, 24, 'demandNumber', '对标需求设计序号', 1, 8, 16389, 0, 1229916);
INSERT INTO `72crm_admin_field_sort` VALUES (327660, 24, 'productName', '对标产品名称', 1, 9, 16389, 0, 1229917);
INSERT INTO `72crm_admin_field_sort` VALUES (327661, 24, 'productVersion', '对标产品版本', 1, 10, 16389, 0, 1229918);
INSERT INTO `72crm_admin_field_sort` VALUES (327662, 24, 'productNumber', '产品编号', 1, 11, 16389, 0, 1229919);
INSERT INTO `72crm_admin_field_sort` VALUES (327663, 24, 'demandType', '需求类型', 3, 12, 16389, 0, 1229926);
INSERT INTO `72crm_admin_field_sort` VALUES (327664, 24, 'functionNumber', '功能编号', 1, 13, 16389, 0, 1229921);
INSERT INTO `72crm_admin_field_sort` VALUES (327665, 24, 'functionName', '功能名称', 1, 14, 16389, 0, 1229920);
INSERT INTO `72crm_admin_field_sort` VALUES (327666, 20, 'milestoneNum', '里程碑编号', 1, 0, 16389, 0, 1229844);
INSERT INTO `72crm_admin_field_sort` VALUES (327667, 20, 'workName', '项目名称', 1, 1, 16389, 0, 1229909);
INSERT INTO `72crm_admin_field_sort` VALUES (327668, 20, 'workNum', '项目编号', 1, 2, 16389, 0, 1229846);
INSERT INTO `72crm_admin_field_sort` VALUES (327669, 20, 'milestoneName', '里程碑名称', 1, 3, 16389, 0, 1229845);
INSERT INTO `72crm_admin_field_sort` VALUES (327670, 20, 'instructions', '详细说明', 2, 4, 16389, 0, 1229852);
INSERT INTO `72crm_admin_field_sort` VALUES (327671, 20, 'degree', '完成度(%)', 6, 5, 16389, 0, 1229907);
INSERT INTO `72crm_admin_field_sort` VALUES (327672, 20, 'startTime', '开始时间', 13, 6, 16389, 0, 1229849);
INSERT INTO `72crm_admin_field_sort` VALUES (327673, 20, 'endTime', '结束时间', 13, 7, 16389, 0, 1229850);
INSERT INTO `72crm_admin_field_sort` VALUES (327674, 20, 'mainUserName', '负责人', 28, 8, 16389, 0, 1229851);
INSERT INTO `72crm_admin_field_sort` VALUES (327675, 20, 'taskName', '前置任务', 29, 9, 16389, 0, 1229906);
INSERT INTO `72crm_admin_field_sort` VALUES (327676, 20, 'updateTime', '更新时间', 4, 10, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327677, 20, 'createTime', '创建时间', 4, 11, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327678, 20, 'createUserName', '创建人', 1, 12, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327679, 28, 'taskName', '任务', 29, 0, 16389, 0, 1229938);
INSERT INTO `72crm_admin_field_sort` VALUES (327680, 28, 'mainUserName', '负责人', 28, 1, 16389, 0, 1229939);
INSERT INTO `72crm_admin_field_sort` VALUES (327681, 28, 'subtasksName', '子任务', 32, 2, 16389, 0, 1229940);
INSERT INTO `72crm_admin_field_sort` VALUES (327682, 28, 'startTime', '开始时间', 13, 3, 16389, 0, 1229941);
INSERT INTO `72crm_admin_field_sort` VALUES (327683, 28, 'endTime', '结束时间', 13, 4, 16389, 0, 1229942);
INSERT INTO `72crm_admin_field_sort` VALUES (327684, 28, 'workingHours', '工时', 1, 5, 16389, 0, 1229943);
INSERT INTO `72crm_admin_field_sort` VALUES (327685, 28, 'hoursType', '工时类型', 3, 6, 16389, 0, 1229944);
INSERT INTO `72crm_admin_field_sort` VALUES (327686, 28, 'progressContent', '进度内容', 1, 7, 16389, 0, 1229945);
INSERT INTO `72crm_admin_field_sort` VALUES (327687, 28, 'checkUserName', '审批人', 1, 8, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327688, 28, 'createUserName', '创建人', 1, 9, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327689, 28, 'createTime', '发起时间', 4, 10, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327690, 28, 'examineTime', '审批时间', 4, 11, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327691, 28, 'checkStatus', '审核状态', 1, 12, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327692, 29, 'taskName', '子任务名称', 1, 0, 16408, 0, 1229950);
INSERT INTO `72crm_admin_field_sort` VALUES (327693, 29, 'status', '子任务状态', 3, 1, 16408, 0, 1229951);
INSERT INTO `72crm_admin_field_sort` VALUES (327694, 29, 'milestoneName', '里程碑', 30, 2, 16408, 0, 1229948);
INSERT INTO `72crm_admin_field_sort` VALUES (327695, 29, 'pName', '所属任务', 29, 3, 16408, 0, 1229947);
INSERT INTO `72crm_admin_field_sort` VALUES (327696, 29, 'classId', '任务类型', 3, 4, 16408, 0, 1229949);
INSERT INTO `72crm_admin_field_sort` VALUES (327697, 29, 'completeness', '任务完成度(%)', 5, 5, 16408, 0, 1229952);
INSERT INTO `72crm_admin_field_sort` VALUES (327698, 29, 'priority', '优先级', 3, 6, 16408, 0, 1229957);
INSERT INTO `72crm_admin_field_sort` VALUES (327699, 29, 'difficulty', '任务难度', 3, 7, 16408, 0, 1229958);
INSERT INTO `72crm_admin_field_sort` VALUES (327700, 29, 'degree', '重要程度', 3, 8, 16408, 0, 1229959);
INSERT INTO `72crm_admin_field_sort` VALUES (327701, 29, 'mainUserName', '负责人', 28, 9, 16408, 0, 1229955);
INSERT INTO `72crm_admin_field_sort` VALUES (327702, 29, 'ownerUserName', '参与人', 10, 10, 16408, 0, 1229956);
INSERT INTO `72crm_admin_field_sort` VALUES (327703, 29, 'startTime', '计划开始时间', 13, 11, 16408, 0, 1229953);
INSERT INTO `72crm_admin_field_sort` VALUES (327704, 29, 'endTime', '计划结束时间', 13, 12, 16408, 0, 1229954);
INSERT INTO `72crm_admin_field_sort` VALUES (327705, 28, 'taskName', '任务', 29, 0, 16408, 0, 1229938);
INSERT INTO `72crm_admin_field_sort` VALUES (327706, 28, 'mainUserName', '负责人', 28, 1, 16408, 0, 1229939);
INSERT INTO `72crm_admin_field_sort` VALUES (327707, 28, 'subtasksName', '子任务', 32, 2, 16408, 0, 1229940);
INSERT INTO `72crm_admin_field_sort` VALUES (327708, 28, 'startTime', '开始时间', 13, 3, 16408, 0, 1229941);
INSERT INTO `72crm_admin_field_sort` VALUES (327709, 28, 'endTime', '结束时间', 13, 4, 16408, 0, 1229942);
INSERT INTO `72crm_admin_field_sort` VALUES (327710, 28, 'workingHours', '工时', 1, 5, 16408, 0, 1229943);
INSERT INTO `72crm_admin_field_sort` VALUES (327711, 28, 'hoursType', '工时类型', 3, 6, 16408, 0, 1229944);
INSERT INTO `72crm_admin_field_sort` VALUES (327712, 28, 'progressContent', '进度内容', 1, 7, 16408, 0, 1229945);
INSERT INTO `72crm_admin_field_sort` VALUES (327713, 28, 'checkUserName', '审批人', 1, 8, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327714, 28, 'createUserName', '创建人', 1, 9, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327715, 28, 'createTime', '发起时间', 4, 10, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327716, 28, 'examineTime', '审批时间', 4, 11, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327717, 28, 'checkStatus', '审核状态', 1, 12, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327718, 2, 'customerName', '客户名称', 1, 0, 16404, 0, 1229736);
INSERT INTO `72crm_admin_field_sort` VALUES (327719, 2, '客户来源', '客户来源', 3, 1, 16404, 0, 1229737);
INSERT INTO `72crm_admin_field_sort` VALUES (327720, 2, 'telephone', '电话', 1, 2, 16404, 0, 1229739);
INSERT INTO `72crm_admin_field_sort` VALUES (327721, 2, 'website', '网址', 1, 3, 16404, 0, 1229740);
INSERT INTO `72crm_admin_field_sort` VALUES (327722, 2, '客户行业', '客户行业', 3, 4, 16404, 0, 1229741);
INSERT INTO `72crm_admin_field_sort` VALUES (327723, 2, '客户级别', '客户级别', 3, 5, 16404, 0, 1229742);
INSERT INTO `72crm_admin_field_sort` VALUES (327724, 2, 'nextTime', '下次联系时间', 13, 6, 16404, 0, 1229743);
INSERT INTO `72crm_admin_field_sort` VALUES (327725, 2, 'remark', '备注', 2, 7, 16404, 0, 1229744);
INSERT INTO `72crm_admin_field_sort` VALUES (327726, 2, '客户属性', '客户属性', 9, 8, 16404, 0, 1229905);
INSERT INTO `72crm_admin_field_sort` VALUES (327727, 2, '附件', '附件', 8, 9, 16404, 0, 1229937);
INSERT INTO `72crm_admin_field_sort` VALUES (327728, 2, '客户类型', '客户类型', 1, 10, 16404, 0, 1229969);
INSERT INTO `72crm_admin_field_sort` VALUES (327729, 2, '单行文本', '单行文本', 1, 11, 16404, 0, 1229971);
INSERT INTO `72crm_admin_field_sort` VALUES (327730, 2, '所属部门', '所属部门', 3, 12, 16404, 0, 1229979);
INSERT INTO `72crm_admin_field_sort` VALUES (327731, 2, '客户省份', '客户省份', 1, 13, 16404, 0, 1229980);
INSERT INTO `72crm_admin_field_sort` VALUES (327732, 2, 'status', '锁定状态', 5, 14, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327733, 2, 'dealStatus', '成交状态', 3, 15, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327734, 2, 'lastTime', '最后跟进时间', 4, 16, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327735, 2, 'lastContent', '最后跟进记录', 1, 17, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327736, 2, 'detailAddress', '详细地址', 1, 18, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327737, 2, 'address', '地区定位', 18, 19, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327738, 2, 'poolDay', '距进入公海天数', 5, 20, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327739, 2, 'ownerUserName', '负责人', 1, 21, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327740, 2, 'updateTime', '更新时间', 4, 22, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327741, 2, 'createTime', '创建时间', 4, 23, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327742, 2, 'createUserName', '创建人', 1, 24, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327743, 1, '线索编号', '线索编号', 1, 0, 16404, 0, 1230005);
INSERT INTO `72crm_admin_field_sort` VALUES (327744, 1, 'leadsName', '线索名称', 1, 1, 16404, 0, 1229745);
INSERT INTO `72crm_admin_field_sort` VALUES (327745, 1, '线索来源', '线索来源', 3, 2, 16404, 0, 1229746);
INSERT INTO `72crm_admin_field_sort` VALUES (327746, 1, 'mobile', '手机', 7, 3, 16404, 0, 1229747);
INSERT INTO `72crm_admin_field_sort` VALUES (327747, 1, 'telephone', '电话', 1, 4, 16404, 0, 1229748);
INSERT INTO `72crm_admin_field_sort` VALUES (327748, 1, 'address', '地址', 1, 5, 16404, 0, 1229749);
INSERT INTO `72crm_admin_field_sort` VALUES (327749, 1, '客户行业', '客户行业', 3, 6, 16404, 0, 1229750);
INSERT INTO `72crm_admin_field_sort` VALUES (327750, 1, '客户级别', '客户级别', 3, 7, 16404, 0, 1229751);
INSERT INTO `72crm_admin_field_sort` VALUES (327751, 1, 'nextTime', '下次联系时间', 13, 8, 16404, 0, 1229752);
INSERT INTO `72crm_admin_field_sort` VALUES (327752, 1, 'remark', '备注', 2, 9, 16404, 0, 1229753);
INSERT INTO `72crm_admin_field_sort` VALUES (327753, 1, '人员', '人员', 10, 10, 16404, 0, 1229970);
INSERT INTO `72crm_admin_field_sort` VALUES (327754, 1, '部门', '部门', 12, 11, 16404, 0, 1229972);
INSERT INTO `72crm_admin_field_sort` VALUES (327755, 1, '测试', '测试', 1, 12, 16404, 0, 1229973);
INSERT INTO `72crm_admin_field_sort` VALUES (327756, 1, '测试多行', '测试多行', 2, 13, 16404, 0, 1229974);
INSERT INTO `72crm_admin_field_sort` VALUES (327757, 1, '下拉框', '下拉框', 3, 14, 16404, 0, 1229975);
INSERT INTO `72crm_admin_field_sort` VALUES (327758, 1, '邮箱', '邮箱', 14, 15, 16404, 0, 1229976);
INSERT INTO `72crm_admin_field_sort` VALUES (327759, 1, 'lastTime', '最后跟进时间', 4, 16, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327760, 1, 'lastContent', '最后跟进记录', 1, 17, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327761, 1, 'ownerUserName', '负责人', 1, 18, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327762, 1, 'updateTime', '更新时间', 4, 19, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327763, 1, 'createTime', '创建时间', 4, 20, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327764, 1, 'createUserName', '创建人', 1, 21, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327765, 3, 'customerName', '客户名称', 15, 0, 16404, 0, 1229755);
INSERT INTO `72crm_admin_field_sort` VALUES (327766, 3, 'name', '姓名', 1, 1, 16404, 0, 1229754);
INSERT INTO `72crm_admin_field_sort` VALUES (327767, 3, '性别', '性别', 3, 2, 16404, 0, 1229764);
INSERT INTO `72crm_admin_field_sort` VALUES (327768, 3, '部门', '部门', 1, 3, 16404, 0, 1229981);
INSERT INTO `72crm_admin_field_sort` VALUES (327769, 3, 'mobile', '手机', 7, 4, 16404, 0, 1229756);
INSERT INTO `72crm_admin_field_sort` VALUES (327770, 3, 'telephone', '电话', 1, 5, 16404, 0, 1229757);
INSERT INTO `72crm_admin_field_sort` VALUES (327771, 3, 'email', '邮箱', 14, 6, 16404, 0, 1229758);
INSERT INTO `72crm_admin_field_sort` VALUES (327772, 3, 'post', '职务', 1, 7, 16404, 0, 1229759);
INSERT INTO `72crm_admin_field_sort` VALUES (327773, 3, '是否关键决策人', '是否关键决策人', 3, 8, 16404, 0, 1229760);
INSERT INTO `72crm_admin_field_sort` VALUES (327774, 3, 'address', '地址', 1, 9, 16404, 0, 1229761);
INSERT INTO `72crm_admin_field_sort` VALUES (327775, 3, 'nextTime', '下次联系时间', 13, 10, 16404, 0, 1229762);
INSERT INTO `72crm_admin_field_sort` VALUES (327776, 3, 'remark', '备注', 2, 11, 16404, 0, 1229763);
INSERT INTO `72crm_admin_field_sort` VALUES (327777, 3, 'lastTime', '最后跟进时间', 4, 12, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327778, 3, 'ownerUserName', '负责人', 1, 13, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327779, 3, 'updateTime', '更新时间', 4, 14, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327780, 3, 'createTime', '创建时间', 4, 15, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327781, 3, 'createUserName', '创建人', 1, 16, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327782, 5, '商机编号', '商机编号', 1, 0, 16404, 0, 1230007);
INSERT INTO `72crm_admin_field_sort` VALUES (327783, 5, '所属部门', '所属部门', 3, 1, 16404, 0, 1229992);
INSERT INTO `72crm_admin_field_sort` VALUES (327784, 5, 'businessName', '商机名称', 1, 2, 16404, 0, 1229772);
INSERT INTO `72crm_admin_field_sort` VALUES (327785, 5, 'customerName', '客户名称', 15, 3, 16404, 0, 1229773);
INSERT INTO `72crm_admin_field_sort` VALUES (327786, 5, 'money', '商机合同金额', 6, 4, 16404, 0, 1229774);
INSERT INTO `72crm_admin_field_sort` VALUES (327787, 5, '商机合同有效额', '商机合同有效额', 6, 5, 16404, 0, 1229983);
INSERT INTO `72crm_admin_field_sort` VALUES (327788, 5, 'dealDate', '预计成交日期', 13, 6, 16404, 0, 1229775);
INSERT INTO `72crm_admin_field_sort` VALUES (327789, 5, '竞争对手', '竞争对手', 1, 7, 16404, 0, 1229984);
INSERT INTO `72crm_admin_field_sort` VALUES (327790, 5, '签约产品', '签约产品', 9, 8, 16404, 0, 1229989);
INSERT INTO `72crm_admin_field_sort` VALUES (327791, 5, '是否招标', '是否招标', 3, 9, 16404, 0, 1229985);
INSERT INTO `72crm_admin_field_sort` VALUES (327792, 5, '中标概率预估', '中标概率预估', 3, 10, 16404, 0, 1229986);
INSERT INTO `72crm_admin_field_sort` VALUES (327793, 5, '当年启动概率', '当年启动概率', 3, 11, 16404, 0, 1229987);
INSERT INTO `72crm_admin_field_sort` VALUES (327794, 5, '预计签约时间', '预计签约时间', 4, 12, 16404, 0, 1229988);
INSERT INTO `72crm_admin_field_sort` VALUES (327795, 5, 'remark', '客户商机需求', 2, 13, 16404, 0, 1229776);
INSERT INTO `72crm_admin_field_sort` VALUES (327796, 5, '商机洽谈阶段', '商机洽谈阶段', 3, 14, 16404, 0, 1229991);
INSERT INTO `72crm_admin_field_sort` VALUES (327797, 5, '销售负责人', '销售负责人', 1, 15, 16404, 0, 1229993);
INSERT INTO `72crm_admin_field_sort` VALUES (327798, 5, 'typeName', '商机状态组', 3, 16, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327799, 5, 'statusName', '商机阶段', 3, 17, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327800, 5, 'lastTime', '最后跟进时间', 4, 18, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327801, 5, 'ownerUserName', '负责人', 1, 19, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327802, 5, 'updateTime', '更新时间', 4, 20, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327803, 5, 'createTime', '创建时间', 4, 21, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327804, 5, 'createUserName', '创建人', 1, 22, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327805, 6, '销售区域', '销售区域', 3, 0, 16404, 0, 1229996);
INSERT INTO `72crm_admin_field_sort` VALUES (327806, 6, 'num', '合同编号', 1, 1, 16404, 0, 1229778);
INSERT INTO `72crm_admin_field_sort` VALUES (327807, 6, 'customerName', '客户名称', 15, 2, 16404, 0, 1229779);
INSERT INTO `72crm_admin_field_sort` VALUES (327808, 6, 'name', '合同甲方（盖章名称）', 1, 3, 16404, 0, 1229777);
INSERT INTO `72crm_admin_field_sort` VALUES (327809, 6, 'businessName', '商机名称', 16, 4, 16404, 0, 1229780);
INSERT INTO `72crm_admin_field_sort` VALUES (327810, 6, '签订主体', '签订主体', 3, 5, 16404, 0, 1229995);
INSERT INTO `72crm_admin_field_sort` VALUES (327811, 6, '合同类型', '合同类型', 3, 6, 16404, 0, 1229788);
INSERT INTO `72crm_admin_field_sort` VALUES (327812, 6, 'money', '合同金额', 6, 7, 16404, 0, 1229781);
INSERT INTO `72crm_admin_field_sort` VALUES (327813, 6, '合同有效额', '合同有效额', 6, 8, 16404, 0, 1229994);
INSERT INTO `72crm_admin_field_sort` VALUES (327814, 6, 'orderDate', '签约时间', 4, 9, 16404, 0, 1229782);
INSERT INTO `72crm_admin_field_sort` VALUES (327815, 6, 'companyUserName', '销售签约人', 10, 10, 16404, 0, 1229786);
INSERT INTO `72crm_admin_field_sort` VALUES (327816, 6, 'endTime', '合同结束时间', 4, 11, 16404, 0, 1229784);
INSERT INTO `72crm_admin_field_sort` VALUES (327817, 6, 'remark', '备注', 2, 12, 16404, 0, 1229787);
INSERT INTO `72crm_admin_field_sort` VALUES (327818, 6, '签订合同附件', '签订合同附件', 8, 13, 16404, 0, 1229997);
INSERT INTO `72crm_admin_field_sort` VALUES (327819, 6, '付款说明', '付款说明', 9, 14, 16404, 0, 1229998);
INSERT INTO `72crm_admin_field_sort` VALUES (327820, 6, '回款状态', '回款状态', 3, 15, 16404, 0, 1229999);
INSERT INTO `72crm_admin_field_sort` VALUES (327821, 6, '纸质合同（行政部）', '纸质合同（行政部）', 3, 16, 16404, 0, 1230000);
INSERT INTO `72crm_admin_field_sort` VALUES (327822, 6, '侦查机设备签收单记录', '侦查机设备签收单记录', 1, 17, 16404, 0, 1230001);
INSERT INTO `72crm_admin_field_sort` VALUES (327823, 6, '钉钉审批单号', '钉钉审批单号', 1, 18, 16404, 0, 1230002);
INSERT INTO `72crm_admin_field_sort` VALUES (327824, 6, '项目状态', '项目状态', 3, 19, 16404, 0, 1230003);
INSERT INTO `72crm_admin_field_sort` VALUES (327825, 6, '业绩属性', '业绩属性', 3, 20, 16404, 0, 1230004);
INSERT INTO `72crm_admin_field_sort` VALUES (327826, 6, 'checkStatus', '审核状态', 5, 21, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327827, 6, 'receivedMoney', '已收款金额', 6, 22, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327828, 6, 'unreceivedMoney', '未收款金额', 6, 23, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327829, 6, 'lastTime', '最后跟进时间', 4, 24, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327830, 6, 'ownerUserName', '负责人', 1, 25, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327831, 6, 'updateTime', '更新时间', 4, 26, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327832, 6, 'createTime', '创建时间', 4, 27, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327833, 6, 'createUserName', '创建人', 1, 28, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327834, 7, 'number', '回款编号', 1, 0, 16404, 0, 1229789);
INSERT INTO `72crm_admin_field_sort` VALUES (327835, 7, 'customerName', '客户名称', 15, 1, 16404, 0, 1229790);
INSERT INTO `72crm_admin_field_sort` VALUES (327836, 7, 'contractNum', '合同编号', 20, 2, 16404, 0, 1229791);
INSERT INTO `72crm_admin_field_sort` VALUES (327837, 7, 'planNum', '期数', 21, 3, 16404, 0, 1229792);
INSERT INTO `72crm_admin_field_sort` VALUES (327838, 7, 'returnTime', '回款日期', 4, 4, 16404, 0, 1229793);
INSERT INTO `72crm_admin_field_sort` VALUES (327839, 7, 'money', '回款金额', 6, 5, 16404, 0, 1229794);
INSERT INTO `72crm_admin_field_sort` VALUES (327840, 7, 'returnType', '回款方式', 3, 6, 16404, 0, 1229795);
INSERT INTO `72crm_admin_field_sort` VALUES (327841, 7, 'remark', '备注', 2, 7, 16404, 0, 1229796);
INSERT INTO `72crm_admin_field_sort` VALUES (327842, 7, 'checkStatus', '审核状态', 5, 8, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327843, 7, 'contractMoney', '合同金额', 6, 9, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327844, 7, 'ownerUserName', '负责人', 1, 10, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327845, 7, 'updateTime', '更新时间', 4, 11, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327846, 7, 'createTime', '创建时间', 4, 12, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327847, 7, 'createUserName', '创建人', 1, 13, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327848, 17, '回访日期时间', '回访日期时间', 13, 0, 16404, 0, 1230013);
INSERT INTO `72crm_admin_field_sort` VALUES (327849, 17, 'visitNumber', '回访编号', 1, 1, 16404, 0, 1230017);
INSERT INTO `72crm_admin_field_sort` VALUES (327850, 17, 'visitTime', '回访时间', 13, 2, 16404, 0, 1230018);
INSERT INTO `72crm_admin_field_sort` VALUES (327851, 17, '回访客户姓名', '回访客户姓名', 1, 3, 16404, 0, 1230026);
INSERT INTO `72crm_admin_field_sort` VALUES (327852, 17, '回访目的', '回访目的', 3, 4, 16404, 0, 1230015);
INSERT INTO `72crm_admin_field_sort` VALUES (327853, 17, 'ownerUserName', '回访人', 10, 5, 16404, 0, 1230019);
INSERT INTO `72crm_admin_field_sort` VALUES (327854, 17, '回访详情描述', '回访详情描述', 2, 6, 16404, 0, 1230016);
INSERT INTO `72crm_admin_field_sort` VALUES (327855, 17, '回访形式', '回访形式', 3, 7, 16404, 0, 1230020);
INSERT INTO `72crm_admin_field_sort` VALUES (327856, 17, 'customerName', '客户名称', 15, 8, 16404, 0, 1230021);
INSERT INTO `72crm_admin_field_sort` VALUES (327857, 17, 'contactsName', '联系人', 17, 9, 16404, 0, 1230022);
INSERT INTO `72crm_admin_field_sort` VALUES (327858, 17, 'contractNum', '合同编号', 20, 10, 16404, 0, 1230023);
INSERT INTO `72crm_admin_field_sort` VALUES (327859, 17, '客户满意度', '客户满意度', 3, 11, 16404, 0, 1230024);
INSERT INTO `72crm_admin_field_sort` VALUES (327860, 17, '客户反馈', '客户反馈', 2, 12, 16404, 0, 1230025);
INSERT INTO `72crm_admin_field_sort` VALUES (327861, 17, 'updateTime', '更新时间', 4, 13, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327862, 17, 'createTime', '创建时间', 4, 14, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327863, 17, 'createUserName', '创建人', 1, 15, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327864, 4, 'name', '产品名称', 1, 0, 16404, 0, 1229765);
INSERT INTO `72crm_admin_field_sort` VALUES (327865, 4, 'categoryName', '产品类型', 19, 1, 16404, 0, 1229766);
INSERT INTO `72crm_admin_field_sort` VALUES (327866, 4, '单位', '单位', 3, 2, 16404, 0, 1229767);
INSERT INTO `72crm_admin_field_sort` VALUES (327867, 4, 'price', '价格', 6, 3, 16404, 0, 1229769);
INSERT INTO `72crm_admin_field_sort` VALUES (327868, 4, '是否上下架', '是否上下架', 3, 4, 16404, 0, 1229770);
INSERT INTO `72crm_admin_field_sort` VALUES (327869, 4, 'description', '销售产品描述', 1, 5, 16404, 0, 1229771);
INSERT INTO `72crm_admin_field_sort` VALUES (327870, 4, '数量', '数量', 5, 6, 16404, 0, 1230012);
INSERT INTO `72crm_admin_field_sort` VALUES (327871, 4, 'ownerUserName', '负责人', 1, 7, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327872, 4, 'updateTime', '更新时间', 4, 8, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327873, 4, 'createTime', '创建时间', 4, 9, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327874, 4, 'createUserName', '创建人', 1, 10, 16404, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327875, 19, 'workNum', '项目编号', 1, 0, 16407, 0, 1229903);
INSERT INTO `72crm_admin_field_sort` VALUES (327876, 19, 'workName', '项目名称', 1, 1, 16407, 0, 1229828);
INSERT INTO `72crm_admin_field_sort` VALUES (327877, 19, 'deptName', '部门', 12, 2, 16407, 0, 1229833);
INSERT INTO `72crm_admin_field_sort` VALUES (327878, 19, 'areaName', '区域', 12, 3, 16407, 0, 1229829);
INSERT INTO `72crm_admin_field_sort` VALUES (327879, 19, 'managerName', '项目经理', 28, 4, 16407, 0, 1229834);
INSERT INTO `72crm_admin_field_sort` VALUES (327880, 19, 'salesName', '销售负责人', 28, 5, 16407, 0, 1229835);
INSERT INTO `72crm_admin_field_sort` VALUES (327881, 19, 'workPhase', '项目阶段', 3, 6, 16407, 0, 1229830);
INSERT INTO `72crm_admin_field_sort` VALUES (327882, 19, 'workType', '项目类型', 3, 7, 16407, 0, 1229836);
INSERT INTO `72crm_admin_field_sort` VALUES (327883, 19, 'startTime', '项目开始时间', 13, 8, 16407, 0, 1229832);
INSERT INTO `72crm_admin_field_sort` VALUES (327884, 19, 'endTime', '项目结束时间', 13, 9, 16407, 0, 1229838);
INSERT INTO `72crm_admin_field_sort` VALUES (327885, 19, 'updateTime', '更新时间', 4, 10, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327886, 19, 'createTime', '创建时间', 4, 11, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327887, 19, 'createUserName', '创建人', 1, 12, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327888, 21, 'workName', '项目名称', 1, 0, 16407, 0, 1229860);
INSERT INTO `72crm_admin_field_sort` VALUES (327889, 21, 'beforeName', '前置任务', 29, 1, 16407, 0, 1229861);
INSERT INTO `72crm_admin_field_sort` VALUES (327890, 21, 'taskNum', '任务编号', 1, 2, 16407, 0, 1229877);
INSERT INTO `72crm_admin_field_sort` VALUES (327891, 21, 'milestoneName', '里程碑', 30, 3, 16407, 0, 1229862);
INSERT INTO `72crm_admin_field_sort` VALUES (327892, 21, 'taskName', '任务名称', 1, 4, 16407, 0, 1229864);
INSERT INTO `72crm_admin_field_sort` VALUES (327893, 21, 'classId', '任务类型', 3, 5, 16407, 0, 1229863);
INSERT INTO `72crm_admin_field_sort` VALUES (327894, 21, 'completeness', '任务完成度(%)', 5, 6, 16407, 0, 1229866);
INSERT INTO `72crm_admin_field_sort` VALUES (327895, 21, 'status', '任务状态', 3, 7, 16407, 0, 1229865);
INSERT INTO `72crm_admin_field_sort` VALUES (327896, 21, 'mainUserName', '负责人', 28, 8, 16407, 0, 1229869);
INSERT INTO `72crm_admin_field_sort` VALUES (327897, 21, 'ownerUserName', '参与人', 10, 9, 16407, 0, 1229870);
INSERT INTO `72crm_admin_field_sort` VALUES (327898, 21, 'priority', '优先级', 3, 10, 16407, 0, 1229872);
INSERT INTO `72crm_admin_field_sort` VALUES (327899, 21, 'difficulty', '任务难度', 3, 11, 16407, 0, 1229873);
INSERT INTO `72crm_admin_field_sort` VALUES (327900, 21, 'degree', '重要程度', 3, 12, 16407, 0, 1229874);
INSERT INTO `72crm_admin_field_sort` VALUES (327901, 21, 'startTime', '计划开始时间', 13, 13, 16407, 0, 1229867);
INSERT INTO `72crm_admin_field_sort` VALUES (327902, 21, 'endTime', '计划结束时间', 13, 14, 16407, 0, 1229868);
INSERT INTO `72crm_admin_field_sort` VALUES (327903, 21, 'description', '任务描述', 2, 15, 16407, 0, 1229875);
INSERT INTO `72crm_admin_field_sort` VALUES (327904, 21, 'updateTime', '更新时间', 4, 16, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327905, 21, 'createTime', '创建时间', 4, 17, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327906, 21, 'createUserName', '创建人', 1, 18, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327907, 20, 'milestoneNum', '里程碑编号', 1, 0, 16407, 0, 1229844);
INSERT INTO `72crm_admin_field_sort` VALUES (327908, 20, 'workName', '项目名称', 1, 1, 16407, 0, 1229909);
INSERT INTO `72crm_admin_field_sort` VALUES (327909, 20, 'workNum', '项目编号', 1, 2, 16407, 0, 1229846);
INSERT INTO `72crm_admin_field_sort` VALUES (327910, 20, 'milestoneName', '里程碑名称', 1, 3, 16407, 0, 1229845);
INSERT INTO `72crm_admin_field_sort` VALUES (327911, 20, 'instructions', '详细说明', 2, 4, 16407, 0, 1229852);
INSERT INTO `72crm_admin_field_sort` VALUES (327912, 20, 'degree', '完成度(%)', 6, 5, 16407, 0, 1229907);
INSERT INTO `72crm_admin_field_sort` VALUES (327913, 20, 'startTime', '开始时间', 13, 6, 16407, 0, 1229849);
INSERT INTO `72crm_admin_field_sort` VALUES (327914, 20, 'endTime', '结束时间', 13, 7, 16407, 0, 1229850);
INSERT INTO `72crm_admin_field_sort` VALUES (327915, 20, 'mainUserName', '负责人', 28, 8, 16407, 0, 1229851);
INSERT INTO `72crm_admin_field_sort` VALUES (327916, 20, 'taskName', '前置任务', 29, 9, 16407, 0, 1229906);
INSERT INTO `72crm_admin_field_sort` VALUES (327917, 20, 'updateTime', '更新时间', 4, 10, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327918, 20, 'createTime', '创建时间', 4, 11, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327919, 20, 'createUserName', '创建人', 1, 12, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327920, 23, 'productNumber', '产品编号', 1, 0, 16389, 0, 1229893);
INSERT INTO `72crm_admin_field_sort` VALUES (327921, 23, 'productName', '产品名称', 1, 1, 16389, 0, 1229894);
INSERT INTO `72crm_admin_field_sort` VALUES (327922, 23, 'productVersion', '产品版本', 1, 2, 16389, 0, 1229895);
INSERT INTO `72crm_admin_field_sort` VALUES (327923, 23, 'productType', '产品类型', 3, 3, 16389, 0, 1229896);
INSERT INTO `72crm_admin_field_sort` VALUES (327924, 23, 'mainUserName', '产品负责人', 28, 4, 16389, 0, 1229897);
INSERT INTO `72crm_admin_field_sort` VALUES (327925, 23, 'exploitUserName', '开发负责人', 28, 5, 16389, 0, 1229898);
INSERT INTO `72crm_admin_field_sort` VALUES (327926, 23, 'productStatus', '产品状态', 3, 6, 16389, 0, 1229899);
INSERT INTO `72crm_admin_field_sort` VALUES (327927, 23, 'releaseTime', '发版时间', 13, 7, 16389, 0, 1229901);
INSERT INTO `72crm_admin_field_sort` VALUES (327928, 23, 'versionDescribe', '版本描述', 2, 8, 16389, 0, 1229902);
INSERT INTO `72crm_admin_field_sort` VALUES (327929, 23, 'updateTime', '更新时间', 4, 9, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327930, 23, 'createTime', '创建时间', 4, 10, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327931, 23, 'createUserName', '创建人', 1, 11, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327932, 19, 'workNum', '项目编号', 1, 0, 16392, 0, 1229903);
INSERT INTO `72crm_admin_field_sort` VALUES (327933, 19, 'workName', '项目名称', 1, 1, 16392, 0, 1229828);
INSERT INTO `72crm_admin_field_sort` VALUES (327934, 19, 'deptName', '部门', 12, 2, 16392, 0, 1229833);
INSERT INTO `72crm_admin_field_sort` VALUES (327935, 19, 'areaName', '区域', 12, 3, 16392, 0, 1229829);
INSERT INTO `72crm_admin_field_sort` VALUES (327936, 19, 'managerName', '项目经理', 28, 4, 16392, 0, 1229834);
INSERT INTO `72crm_admin_field_sort` VALUES (327937, 19, 'salesName', '销售负责人', 28, 5, 16392, 0, 1229835);
INSERT INTO `72crm_admin_field_sort` VALUES (327938, 19, 'workPhase', '项目阶段', 3, 6, 16392, 0, 1229830);
INSERT INTO `72crm_admin_field_sort` VALUES (327939, 19, 'workType', '项目类型', 3, 7, 16392, 0, 1229836);
INSERT INTO `72crm_admin_field_sort` VALUES (327940, 19, 'startTime', '项目开始时间', 13, 8, 16392, 0, 1229832);
INSERT INTO `72crm_admin_field_sort` VALUES (327941, 19, 'endTime', '项目结束时间', 13, 9, 16392, 0, 1229838);
INSERT INTO `72crm_admin_field_sort` VALUES (327942, 19, 'updateTime', '更新时间', 4, 10, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327943, 19, 'createTime', '创建时间', 4, 11, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327944, 19, 'createUserName', '创建人', 1, 12, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327945, 21, 'workName', '项目名称', 1, 0, 16392, 0, 1229860);
INSERT INTO `72crm_admin_field_sort` VALUES (327946, 21, 'beforeName', '前置任务', 29, 1, 16392, 0, 1229861);
INSERT INTO `72crm_admin_field_sort` VALUES (327947, 21, 'taskNum', '任务编号', 1, 2, 16392, 0, 1229877);
INSERT INTO `72crm_admin_field_sort` VALUES (327948, 21, 'milestoneName', '里程碑', 30, 3, 16392, 0, 1229862);
INSERT INTO `72crm_admin_field_sort` VALUES (327949, 21, 'taskName', '任务名称', 1, 4, 16392, 0, 1229864);
INSERT INTO `72crm_admin_field_sort` VALUES (327950, 21, 'classId', '任务类型', 3, 5, 16392, 0, 1229863);
INSERT INTO `72crm_admin_field_sort` VALUES (327951, 21, 'completeness', '任务完成度(%)', 5, 6, 16392, 0, 1229866);
INSERT INTO `72crm_admin_field_sort` VALUES (327952, 21, 'status', '任务状态', 3, 7, 16392, 0, 1229865);
INSERT INTO `72crm_admin_field_sort` VALUES (327953, 21, 'mainUserName', '负责人', 28, 8, 16392, 0, 1229869);
INSERT INTO `72crm_admin_field_sort` VALUES (327954, 21, 'ownerUserName', '参与人', 10, 9, 16392, 0, 1229870);
INSERT INTO `72crm_admin_field_sort` VALUES (327955, 21, 'priority', '优先级', 3, 10, 16392, 0, 1229872);
INSERT INTO `72crm_admin_field_sort` VALUES (327956, 21, 'difficulty', '任务难度', 3, 11, 16392, 0, 1229873);
INSERT INTO `72crm_admin_field_sort` VALUES (327957, 21, 'degree', '重要程度', 3, 12, 16392, 0, 1229874);
INSERT INTO `72crm_admin_field_sort` VALUES (327958, 21, 'startTime', '计划开始时间', 13, 13, 16392, 0, 1229867);
INSERT INTO `72crm_admin_field_sort` VALUES (327959, 21, 'endTime', '计划结束时间', 13, 14, 16392, 0, 1229868);
INSERT INTO `72crm_admin_field_sort` VALUES (327960, 21, 'description', '任务描述', 2, 15, 16392, 0, 1229875);
INSERT INTO `72crm_admin_field_sort` VALUES (327961, 21, 'updateTime', '更新时间', 4, 16, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327962, 21, 'createTime', '创建时间', 4, 17, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327963, 21, 'createUserName', '创建人', 1, 18, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327964, 20, 'milestoneNum', '里程碑编号', 1, 0, 16392, 0, 1229844);
INSERT INTO `72crm_admin_field_sort` VALUES (327965, 20, 'workName', '项目名称', 1, 1, 16392, 0, 1229909);
INSERT INTO `72crm_admin_field_sort` VALUES (327966, 20, 'workNum', '项目编号', 1, 2, 16392, 0, 1229846);
INSERT INTO `72crm_admin_field_sort` VALUES (327967, 20, 'milestoneName', '里程碑名称', 1, 3, 16392, 0, 1229845);
INSERT INTO `72crm_admin_field_sort` VALUES (327968, 20, 'instructions', '详细说明', 2, 4, 16392, 0, 1229852);
INSERT INTO `72crm_admin_field_sort` VALUES (327969, 20, 'degree', '完成度(%)', 6, 5, 16392, 0, 1229907);
INSERT INTO `72crm_admin_field_sort` VALUES (327970, 20, 'startTime', '开始时间', 13, 6, 16392, 0, 1229849);
INSERT INTO `72crm_admin_field_sort` VALUES (327971, 20, 'endTime', '结束时间', 13, 7, 16392, 0, 1229850);
INSERT INTO `72crm_admin_field_sort` VALUES (327972, 20, 'mainUserName', '负责人', 28, 8, 16392, 0, 1229851);
INSERT INTO `72crm_admin_field_sort` VALUES (327973, 20, 'taskName', '前置任务', 29, 9, 16392, 0, 1229906);
INSERT INTO `72crm_admin_field_sort` VALUES (327974, 20, 'updateTime', '更新时间', 4, 10, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327975, 20, 'createTime', '创建时间', 4, 11, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327976, 20, 'createUserName', '创建人', 1, 12, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327977, 19, 'workNum', '项目编号', 1, 0, 16388, 0, 1229903);
INSERT INTO `72crm_admin_field_sort` VALUES (327978, 19, 'workName', '项目名称', 1, 1, 16388, 0, 1229828);
INSERT INTO `72crm_admin_field_sort` VALUES (327979, 19, 'deptName', '部门', 12, 2, 16388, 0, 1229833);
INSERT INTO `72crm_admin_field_sort` VALUES (327980, 19, 'areaName', '区域', 12, 3, 16388, 0, 1229829);
INSERT INTO `72crm_admin_field_sort` VALUES (327981, 19, 'managerName', '项目经理', 28, 4, 16388, 0, 1229834);
INSERT INTO `72crm_admin_field_sort` VALUES (327982, 19, 'salesName', '销售负责人', 28, 5, 16388, 0, 1229835);
INSERT INTO `72crm_admin_field_sort` VALUES (327983, 19, 'workPhase', '项目阶段', 3, 6, 16388, 0, 1229830);
INSERT INTO `72crm_admin_field_sort` VALUES (327984, 19, 'workType', '项目类型', 3, 7, 16388, 0, 1229836);
INSERT INTO `72crm_admin_field_sort` VALUES (327985, 19, 'startTime', '项目开始时间', 13, 8, 16388, 0, 1229832);
INSERT INTO `72crm_admin_field_sort` VALUES (327986, 19, 'endTime', '项目结束时间', 13, 9, 16388, 0, 1229838);
INSERT INTO `72crm_admin_field_sort` VALUES (327987, 19, 'updateTime', '更新时间', 4, 10, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327988, 19, 'createTime', '创建时间', 4, 11, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327989, 19, 'createUserName', '创建人', 1, 12, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (327990, 21, 'workName', '项目名称', 1, 0, 16388, 0, 1229860);
INSERT INTO `72crm_admin_field_sort` VALUES (327991, 21, 'beforeName', '前置任务', 29, 1, 16388, 0, 1229861);
INSERT INTO `72crm_admin_field_sort` VALUES (327992, 21, 'taskNum', '任务编号', 1, 2, 16388, 0, 1229877);
INSERT INTO `72crm_admin_field_sort` VALUES (327993, 21, 'milestoneName', '里程碑', 30, 3, 16388, 0, 1229862);
INSERT INTO `72crm_admin_field_sort` VALUES (327994, 21, 'taskName', '任务名称', 1, 4, 16388, 0, 1229864);
INSERT INTO `72crm_admin_field_sort` VALUES (327995, 21, 'classId', '任务类型', 3, 5, 16388, 0, 1229863);
INSERT INTO `72crm_admin_field_sort` VALUES (327996, 21, 'completeness', '任务完成度(%)', 5, 6, 16388, 0, 1229866);
INSERT INTO `72crm_admin_field_sort` VALUES (327997, 21, 'status', '任务状态', 3, 7, 16388, 0, 1229865);
INSERT INTO `72crm_admin_field_sort` VALUES (327998, 21, 'mainUserName', '负责人', 28, 8, 16388, 0, 1229869);
INSERT INTO `72crm_admin_field_sort` VALUES (327999, 21, 'ownerUserName', '参与人', 10, 9, 16388, 0, 1229870);
INSERT INTO `72crm_admin_field_sort` VALUES (328000, 21, 'priority', '优先级', 3, 10, 16388, 0, 1229872);
INSERT INTO `72crm_admin_field_sort` VALUES (328001, 21, 'difficulty', '任务难度', 3, 11, 16388, 0, 1229873);
INSERT INTO `72crm_admin_field_sort` VALUES (328002, 21, 'degree', '重要程度', 3, 12, 16388, 0, 1229874);
INSERT INTO `72crm_admin_field_sort` VALUES (328003, 21, 'startTime', '计划开始时间', 13, 13, 16388, 0, 1229867);
INSERT INTO `72crm_admin_field_sort` VALUES (328004, 21, 'endTime', '计划结束时间', 13, 14, 16388, 0, 1229868);
INSERT INTO `72crm_admin_field_sort` VALUES (328005, 21, 'description', '任务描述', 2, 15, 16388, 0, 1229875);
INSERT INTO `72crm_admin_field_sort` VALUES (328006, 21, 'updateTime', '更新时间', 4, 16, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328007, 21, 'createTime', '创建时间', 4, 17, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328008, 21, 'createUserName', '创建人', 1, 18, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328009, 20, 'milestoneNum', '里程碑编号', 1, 0, 16388, 0, 1229844);
INSERT INTO `72crm_admin_field_sort` VALUES (328010, 20, 'workName', '项目名称', 1, 1, 16388, 0, 1229909);
INSERT INTO `72crm_admin_field_sort` VALUES (328011, 20, 'workNum', '项目编号', 1, 2, 16388, 0, 1229846);
INSERT INTO `72crm_admin_field_sort` VALUES (328012, 20, 'milestoneName', '里程碑名称', 1, 3, 16388, 0, 1229845);
INSERT INTO `72crm_admin_field_sort` VALUES (328013, 20, 'instructions', '详细说明', 2, 4, 16388, 0, 1229852);
INSERT INTO `72crm_admin_field_sort` VALUES (328014, 20, 'degree', '完成度(%)', 6, 5, 16388, 0, 1229907);
INSERT INTO `72crm_admin_field_sort` VALUES (328015, 20, 'startTime', '开始时间', 13, 6, 16388, 0, 1229849);
INSERT INTO `72crm_admin_field_sort` VALUES (328016, 20, 'endTime', '结束时间', 13, 7, 16388, 0, 1229850);
INSERT INTO `72crm_admin_field_sort` VALUES (328017, 20, 'mainUserName', '负责人', 28, 8, 16388, 0, 1229851);
INSERT INTO `72crm_admin_field_sort` VALUES (328018, 20, 'taskName', '前置任务', 29, 9, 16388, 0, 1229906);
INSERT INTO `72crm_admin_field_sort` VALUES (328019, 20, 'updateTime', '更新时间', 4, 10, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328020, 20, 'createTime', '创建时间', 4, 11, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328021, 20, 'createUserName', '创建人', 1, 12, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328022, 24, 'subsystem', '子系统', 1, 0, 16388, 0, 1229910);
INSERT INTO `72crm_admin_field_sort` VALUES (328023, 24, 'childModule', '子模块', 1, 1, 16388, 0, 1229911);
INSERT INTO `72crm_admin_field_sort` VALUES (328024, 24, 'function1', '一级功能', 1, 2, 16388, 0, 1229912);
INSERT INTO `72crm_admin_field_sort` VALUES (328025, 24, 'function2', '二级功能', 1, 3, 16388, 0, 1229913);
INSERT INTO `72crm_admin_field_sort` VALUES (328026, 24, 'demandDescribe', '需求描述', 2, 4, 16388, 0, 1229914);
INSERT INTO `72crm_admin_field_sort` VALUES (328027, 24, 'demandStatus', '完成状态(%)', 6, 5, 16388, 0, 1229924);
INSERT INTO `72crm_admin_field_sort` VALUES (328028, 24, 'demandVersion', '需求版本号', 1, 6, 16388, 0, 1229915);
INSERT INTO `72crm_admin_field_sort` VALUES (328029, 24, 'plan', '对标投标方案号', 1, 7, 16388, 0, 1229925);
INSERT INTO `72crm_admin_field_sort` VALUES (328030, 24, 'demandNumber', '对标需求设计序号', 1, 8, 16388, 0, 1229916);
INSERT INTO `72crm_admin_field_sort` VALUES (328031, 24, 'productName', '对标产品名称', 1, 9, 16388, 0, 1229917);
INSERT INTO `72crm_admin_field_sort` VALUES (328032, 24, 'productVersion', '对标产品版本', 1, 10, 16388, 0, 1229918);
INSERT INTO `72crm_admin_field_sort` VALUES (328033, 24, 'productNumber', '产品编号', 1, 11, 16388, 0, 1229919);
INSERT INTO `72crm_admin_field_sort` VALUES (328034, 24, 'demandType', '需求类型', 3, 12, 16388, 0, 1229926);
INSERT INTO `72crm_admin_field_sort` VALUES (328035, 24, 'functionNumber', '功能编号', 1, 13, 16388, 0, 1229921);
INSERT INTO `72crm_admin_field_sort` VALUES (328036, 24, 'functionName', '功能名称', 1, 14, 16388, 0, 1229920);
INSERT INTO `72crm_admin_field_sort` VALUES (328037, 28, 'taskName', '任务', 29, 0, 16388, 0, 1229938);
INSERT INTO `72crm_admin_field_sort` VALUES (328038, 28, 'mainUserName', '负责人', 28, 1, 16388, 0, 1229939);
INSERT INTO `72crm_admin_field_sort` VALUES (328039, 28, 'subtasksName', '子任务', 32, 2, 16388, 0, 1229940);
INSERT INTO `72crm_admin_field_sort` VALUES (328040, 28, 'startTime', '开始时间', 13, 3, 16388, 0, 1229941);
INSERT INTO `72crm_admin_field_sort` VALUES (328041, 28, 'endTime', '结束时间', 13, 4, 16388, 0, 1229942);
INSERT INTO `72crm_admin_field_sort` VALUES (328042, 28, 'workingHours', '工时', 1, 5, 16388, 0, 1229943);
INSERT INTO `72crm_admin_field_sort` VALUES (328043, 28, 'hoursType', '工时类型', 3, 6, 16388, 0, 1229944);
INSERT INTO `72crm_admin_field_sort` VALUES (328044, 28, 'progressContent', '进度内容', 1, 7, 16388, 0, 1229945);
INSERT INTO `72crm_admin_field_sort` VALUES (328045, 28, 'checkUserName', '审批人', 1, 8, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328046, 28, 'createUserName', '创建人', 1, 9, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328047, 28, 'createTime', '发起时间', 4, 10, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328048, 28, 'examineTime', '审批时间', 4, 11, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328049, 28, 'checkStatus', '审核状态', 1, 12, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328050, 23, 'productNumber', '产品编号', 1, 0, 16392, 0, 1229893);
INSERT INTO `72crm_admin_field_sort` VALUES (328051, 23, 'productName', '产品名称', 1, 1, 16392, 0, 1229894);
INSERT INTO `72crm_admin_field_sort` VALUES (328052, 23, 'productVersion', '产品版本', 1, 2, 16392, 0, 1229895);
INSERT INTO `72crm_admin_field_sort` VALUES (328053, 23, 'productType', '产品类型', 3, 3, 16392, 0, 1229896);
INSERT INTO `72crm_admin_field_sort` VALUES (328054, 23, 'mainUserName', '产品负责人', 28, 4, 16392, 0, 1229897);
INSERT INTO `72crm_admin_field_sort` VALUES (328055, 23, 'exploitUserName', '开发负责人', 28, 5, 16392, 0, 1229898);
INSERT INTO `72crm_admin_field_sort` VALUES (328056, 23, 'productStatus', '产品状态', 3, 6, 16392, 0, 1229899);
INSERT INTO `72crm_admin_field_sort` VALUES (328057, 23, 'releaseTime', '发版时间', 13, 7, 16392, 0, 1229901);
INSERT INTO `72crm_admin_field_sort` VALUES (328058, 23, 'versionDescribe', '版本描述', 2, 8, 16392, 0, 1229902);
INSERT INTO `72crm_admin_field_sort` VALUES (328059, 23, 'updateTime', '更新时间', 4, 9, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328060, 23, 'createTime', '创建时间', 4, 10, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328061, 23, 'createUserName', '创建人', 1, 11, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328062, 24, 'subsystem', '子系统', 1, 0, 16392, 0, 1229910);
INSERT INTO `72crm_admin_field_sort` VALUES (328063, 24, 'childModule', '子模块', 1, 1, 16392, 0, 1229911);
INSERT INTO `72crm_admin_field_sort` VALUES (328064, 24, 'function1', '一级功能', 1, 2, 16392, 0, 1229912);
INSERT INTO `72crm_admin_field_sort` VALUES (328065, 24, 'function2', '二级功能', 1, 3, 16392, 0, 1229913);
INSERT INTO `72crm_admin_field_sort` VALUES (328066, 24, 'demandDescribe', '需求描述', 2, 4, 16392, 0, 1229914);
INSERT INTO `72crm_admin_field_sort` VALUES (328067, 24, 'demandStatus', '完成状态(%)', 6, 5, 16392, 0, 1229924);
INSERT INTO `72crm_admin_field_sort` VALUES (328068, 24, 'demandVersion', '需求版本号', 1, 6, 16392, 0, 1229915);
INSERT INTO `72crm_admin_field_sort` VALUES (328069, 24, 'plan', '对标投标方案号', 1, 7, 16392, 0, 1229925);
INSERT INTO `72crm_admin_field_sort` VALUES (328070, 24, 'demandNumber', '对标需求设计序号', 1, 8, 16392, 0, 1229916);
INSERT INTO `72crm_admin_field_sort` VALUES (328071, 24, 'productName', '对标产品名称', 1, 9, 16392, 0, 1229917);
INSERT INTO `72crm_admin_field_sort` VALUES (328072, 24, 'productVersion', '对标产品版本', 1, 10, 16392, 0, 1229918);
INSERT INTO `72crm_admin_field_sort` VALUES (328073, 24, 'productNumber', '产品编号', 1, 11, 16392, 0, 1229919);
INSERT INTO `72crm_admin_field_sort` VALUES (328074, 24, 'demandType', '需求类型', 3, 12, 16392, 0, 1229926);
INSERT INTO `72crm_admin_field_sort` VALUES (328075, 24, 'functionNumber', '功能编号', 1, 13, 16392, 0, 1229921);
INSERT INTO `72crm_admin_field_sort` VALUES (328076, 24, 'functionName', '功能名称', 1, 14, 16392, 0, 1229920);
INSERT INTO `72crm_admin_field_sort` VALUES (328077, 25, 'productName', '产品名称', 1, 0, 16389, 0, 1229930);
INSERT INTO `72crm_admin_field_sort` VALUES (328078, 25, 'productVersion', '产品版本', 1, 1, 16389, 0, 1229931);
INSERT INTO `72crm_admin_field_sort` VALUES (328079, 25, 'mainUserName', '产品负责人', 28, 2, 16389, 0, 1229932);
INSERT INTO `72crm_admin_field_sort` VALUES (328080, 25, 'exploitUserName', '开发负责人', 28, 3, 16389, 0, 1229933);
INSERT INTO `72crm_admin_field_sort` VALUES (328081, 25, 'productStatus', '产品状态', 3, 4, 16389, 0, 1229927);
INSERT INTO `72crm_admin_field_sort` VALUES (328082, 25, 'functionNumber', '产品功能编号', 1, 5, 16389, 0, 1229934);
INSERT INTO `72crm_admin_field_sort` VALUES (328083, 25, 'functionName', '产品功能名称', 1, 6, 16389, 0, 1229935);
INSERT INTO `72crm_admin_field_sort` VALUES (328084, 25, 'functionDescribe', '版本描述', 2, 7, 16389, 0, 1229936);
INSERT INTO `72crm_admin_field_sort` VALUES (328085, 25, 'updateTime', '更新时间', 4, 8, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328086, 25, 'createTime', '创建时间', 4, 9, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328087, 25, 'createUserName', '创建人', 1, 10, 16389, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328088, 30, 'configItems', '配置项', 1, 0, 16408, 0, 1229960);
INSERT INTO `72crm_admin_field_sort` VALUES (328089, 30, 'planTime', '计划时间', 13, 1, 16408, 0, 1229961);
INSERT INTO `72crm_admin_field_sort` VALUES (328090, 30, 'mainUserName', '负责人', 28, 2, 16408, 0, 1229962);
INSERT INTO `72crm_admin_field_sort` VALUES (328091, 30, 'associatedName', '项目名称', 31, 3, 16408, 0, 1229963);
INSERT INTO `72crm_admin_field_sort` VALUES (328092, 30, 'associatedNum', '项目编号', 1, 4, 16408, 0, 1229964);
INSERT INTO `72crm_admin_field_sort` VALUES (328093, 30, 'version', '版本', 1, 5, 16408, 0, 1229966);
INSERT INTO `72crm_admin_field_sort` VALUES (328094, 30, 'versionTime', '版本时间', 13, 6, 16408, 0, 1229967);
INSERT INTO `72crm_admin_field_sort` VALUES (328095, 30, 'versionDescribe', '版本说明', 2, 7, 16408, 0, 1229968);
INSERT INTO `72crm_admin_field_sort` VALUES (328096, 30, 'updateTime', '更新时间', 4, 8, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328097, 30, 'createTime', '创建时间', 4, 9, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328098, 30, 'createUserName', '创建人', 1, 10, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328099, 23, 'productNumber', '产品编号', 1, 0, 16408, 0, 1229893);
INSERT INTO `72crm_admin_field_sort` VALUES (328100, 23, 'productName', '产品名称', 1, 1, 16408, 0, 1229894);
INSERT INTO `72crm_admin_field_sort` VALUES (328101, 23, 'productVersion', '产品版本', 1, 2, 16408, 0, 1229895);
INSERT INTO `72crm_admin_field_sort` VALUES (328102, 23, 'productType', '产品类型', 3, 3, 16408, 0, 1229896);
INSERT INTO `72crm_admin_field_sort` VALUES (328103, 23, 'mainUserName', '产品负责人', 28, 4, 16408, 0, 1229897);
INSERT INTO `72crm_admin_field_sort` VALUES (328104, 23, 'exploitUserName', '开发负责人', 28, 5, 16408, 0, 1229898);
INSERT INTO `72crm_admin_field_sort` VALUES (328105, 23, 'productStatus', '产品状态', 3, 6, 16408, 0, 1229899);
INSERT INTO `72crm_admin_field_sort` VALUES (328106, 23, 'releaseTime', '发版时间', 13, 7, 16408, 0, 1229901);
INSERT INTO `72crm_admin_field_sort` VALUES (328107, 23, 'versionDescribe', '版本描述', 2, 8, 16408, 0, 1229902);
INSERT INTO `72crm_admin_field_sort` VALUES (328108, 23, 'updateTime', '更新时间', 4, 9, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328109, 23, 'createTime', '创建时间', 4, 10, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328110, 23, 'createUserName', '创建人', 1, 11, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328111, 30, 'configItems', '配置项', 1, 0, 16392, 0, 1229960);
INSERT INTO `72crm_admin_field_sort` VALUES (328112, 30, 'planTime', '计划时间', 13, 1, 16392, 0, 1229961);
INSERT INTO `72crm_admin_field_sort` VALUES (328113, 30, 'mainUserName', '负责人', 28, 2, 16392, 0, 1229962);
INSERT INTO `72crm_admin_field_sort` VALUES (328114, 30, 'associatedName', '项目名称', 31, 3, 16392, 0, 1229963);
INSERT INTO `72crm_admin_field_sort` VALUES (328115, 30, 'associatedNum', '项目编号', 1, 4, 16392, 0, 1229964);
INSERT INTO `72crm_admin_field_sort` VALUES (328116, 30, 'version', '版本', 1, 5, 16392, 0, 1229966);
INSERT INTO `72crm_admin_field_sort` VALUES (328117, 30, 'versionTime', '版本时间', 13, 6, 16392, 0, 1229967);
INSERT INTO `72crm_admin_field_sort` VALUES (328118, 30, 'versionDescribe', '版本说明', 2, 7, 16392, 0, 1229968);
INSERT INTO `72crm_admin_field_sort` VALUES (328119, 30, 'updateTime', '更新时间', 4, 8, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328120, 30, 'createTime', '创建时间', 4, 9, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328121, 30, 'createUserName', '创建人', 1, 10, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328122, 28, 'taskName', '任务', 29, 0, 16392, 0, 1229938);
INSERT INTO `72crm_admin_field_sort` VALUES (328123, 28, 'mainUserName', '负责人', 28, 1, 16392, 0, 1229939);
INSERT INTO `72crm_admin_field_sort` VALUES (328124, 28, 'subtasksName', '子任务', 32, 2, 16392, 0, 1229940);
INSERT INTO `72crm_admin_field_sort` VALUES (328125, 28, 'startTime', '开始时间', 13, 3, 16392, 0, 1229941);
INSERT INTO `72crm_admin_field_sort` VALUES (328126, 28, 'endTime', '结束时间', 13, 4, 16392, 0, 1229942);
INSERT INTO `72crm_admin_field_sort` VALUES (328127, 28, 'workingHours', '工时', 1, 5, 16392, 0, 1229943);
INSERT INTO `72crm_admin_field_sort` VALUES (328128, 28, 'hoursType', '工时类型', 3, 6, 16392, 0, 1229944);
INSERT INTO `72crm_admin_field_sort` VALUES (328129, 28, 'progressContent', '进度内容', 1, 7, 16392, 0, 1229945);
INSERT INTO `72crm_admin_field_sort` VALUES (328130, 28, 'checkUserName', '审批人', 1, 8, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328131, 28, 'createUserName', '创建人', 1, 9, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328132, 28, 'createTime', '发起时间', 4, 10, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328133, 28, 'examineTime', '审批时间', 4, 11, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328134, 28, 'checkStatus', '审核状态', 1, 12, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328135, 29, 'taskName', '子任务名称', 1, 0, 16392, 0, 1229950);
INSERT INTO `72crm_admin_field_sort` VALUES (328136, 29, 'status', '子任务状态', 3, 1, 16392, 0, 1229951);
INSERT INTO `72crm_admin_field_sort` VALUES (328137, 29, 'milestoneName', '里程碑', 30, 2, 16392, 0, 1229948);
INSERT INTO `72crm_admin_field_sort` VALUES (328138, 29, 'pName', '所属任务', 29, 3, 16392, 0, 1229947);
INSERT INTO `72crm_admin_field_sort` VALUES (328139, 29, 'classId', '任务类型', 3, 4, 16392, 0, 1229949);
INSERT INTO `72crm_admin_field_sort` VALUES (328140, 29, 'completeness', '任务完成度(%)', 5, 5, 16392, 0, 1229952);
INSERT INTO `72crm_admin_field_sort` VALUES (328141, 29, 'priority', '优先级', 3, 6, 16392, 0, 1229957);
INSERT INTO `72crm_admin_field_sort` VALUES (328142, 29, 'difficulty', '任务难度', 3, 7, 16392, 0, 1229958);
INSERT INTO `72crm_admin_field_sort` VALUES (328143, 29, 'degree', '重要程度', 3, 8, 16392, 0, 1229959);
INSERT INTO `72crm_admin_field_sort` VALUES (328144, 29, 'mainUserName', '负责人', 28, 9, 16392, 0, 1229955);
INSERT INTO `72crm_admin_field_sort` VALUES (328145, 29, 'ownerUserName', '参与人', 10, 10, 16392, 0, 1229956);
INSERT INTO `72crm_admin_field_sort` VALUES (328146, 29, 'startTime', '计划开始时间', 13, 11, 16392, 0, 1229953);
INSERT INTO `72crm_admin_field_sort` VALUES (328147, 29, 'endTime', '计划结束时间', 13, 12, 16392, 0, 1229954);
INSERT INTO `72crm_admin_field_sort` VALUES (328148, 28, 'taskName', '任务', 29, 0, 16407, 0, 1229938);
INSERT INTO `72crm_admin_field_sort` VALUES (328149, 28, 'mainUserName', '负责人', 28, 1, 16407, 0, 1229939);
INSERT INTO `72crm_admin_field_sort` VALUES (328150, 28, 'subtasksName', '子任务', 32, 2, 16407, 0, 1229940);
INSERT INTO `72crm_admin_field_sort` VALUES (328151, 28, 'startTime', '开始时间', 13, 3, 16407, 0, 1229941);
INSERT INTO `72crm_admin_field_sort` VALUES (328152, 28, 'endTime', '结束时间', 13, 4, 16407, 0, 1229942);
INSERT INTO `72crm_admin_field_sort` VALUES (328153, 28, 'workingHours', '工时', 1, 5, 16407, 0, 1229943);
INSERT INTO `72crm_admin_field_sort` VALUES (328154, 28, 'hoursType', '工时类型', 3, 6, 16407, 0, 1229944);
INSERT INTO `72crm_admin_field_sort` VALUES (328155, 28, 'progressContent', '进度内容', 1, 7, 16407, 0, 1229945);
INSERT INTO `72crm_admin_field_sort` VALUES (328156, 28, 'checkUserName', '审批人', 1, 8, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328157, 28, 'createUserName', '创建人', 1, 9, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328158, 28, 'createTime', '发起时间', 4, 10, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328159, 28, 'examineTime', '审批时间', 4, 11, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328160, 28, 'checkStatus', '审核状态', 1, 12, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328161, 21, 'workName', '项目名称', 1, 0, 16360, 0, 1229860);
INSERT INTO `72crm_admin_field_sort` VALUES (328162, 21, 'beforeName', '前置任务', 29, 1, 16360, 0, 1229861);
INSERT INTO `72crm_admin_field_sort` VALUES (328163, 21, 'taskNum', '任务编号', 1, 2, 16360, 0, 1229877);
INSERT INTO `72crm_admin_field_sort` VALUES (328164, 21, 'milestoneName', '里程碑', 30, 3, 16360, 0, 1229862);
INSERT INTO `72crm_admin_field_sort` VALUES (328165, 21, 'taskName', '任务名称', 1, 4, 16360, 0, 1229864);
INSERT INTO `72crm_admin_field_sort` VALUES (328166, 21, 'classId', '任务类型', 3, 5, 16360, 0, 1229863);
INSERT INTO `72crm_admin_field_sort` VALUES (328167, 21, 'completeness', '任务完成度(%)', 5, 6, 16360, 0, 1229866);
INSERT INTO `72crm_admin_field_sort` VALUES (328168, 21, 'status', '任务状态', 3, 7, 16360, 0, 1229865);
INSERT INTO `72crm_admin_field_sort` VALUES (328169, 21, 'mainUserName', '负责人', 28, 8, 16360, 0, 1229869);
INSERT INTO `72crm_admin_field_sort` VALUES (328170, 21, 'ownerUserName', '参与人', 10, 9, 16360, 0, 1229870);
INSERT INTO `72crm_admin_field_sort` VALUES (328171, 21, 'priority', '优先级', 3, 10, 16360, 0, 1229872);
INSERT INTO `72crm_admin_field_sort` VALUES (328172, 21, 'difficulty', '任务难度', 3, 11, 16360, 0, 1229873);
INSERT INTO `72crm_admin_field_sort` VALUES (328173, 21, 'degree', '重要程度', 3, 12, 16360, 0, 1229874);
INSERT INTO `72crm_admin_field_sort` VALUES (328174, 21, 'startTime', '计划开始时间', 13, 13, 16360, 0, 1229867);
INSERT INTO `72crm_admin_field_sort` VALUES (328175, 21, 'endTime', '计划结束时间', 13, 14, 16360, 0, 1229868);
INSERT INTO `72crm_admin_field_sort` VALUES (328176, 21, 'description', '任务描述', 2, 15, 16360, 0, 1229875);
INSERT INTO `72crm_admin_field_sort` VALUES (328177, 21, 'updateTime', '更新时间', 4, 16, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328178, 21, 'createTime', '创建时间', 4, 17, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328179, 21, 'createUserName', '创建人', 1, 18, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328180, 29, 'taskName', '子任务名称', 1, 0, 16360, 0, 1229950);
INSERT INTO `72crm_admin_field_sort` VALUES (328181, 29, 'status', '子任务状态', 3, 1, 16360, 0, 1229951);
INSERT INTO `72crm_admin_field_sort` VALUES (328182, 29, 'milestoneName', '里程碑', 30, 2, 16360, 0, 1229948);
INSERT INTO `72crm_admin_field_sort` VALUES (328183, 29, 'pName', '所属任务', 29, 3, 16360, 0, 1229947);
INSERT INTO `72crm_admin_field_sort` VALUES (328184, 29, 'classId', '任务类型', 3, 4, 16360, 0, 1229949);
INSERT INTO `72crm_admin_field_sort` VALUES (328185, 29, 'completeness', '任务完成度(%)', 5, 5, 16360, 0, 1229952);
INSERT INTO `72crm_admin_field_sort` VALUES (328186, 29, 'priority', '优先级', 3, 6, 16360, 0, 1229957);
INSERT INTO `72crm_admin_field_sort` VALUES (328187, 29, 'difficulty', '任务难度', 3, 7, 16360, 0, 1229958);
INSERT INTO `72crm_admin_field_sort` VALUES (328188, 29, 'degree', '重要程度', 3, 8, 16360, 0, 1229959);
INSERT INTO `72crm_admin_field_sort` VALUES (328189, 29, 'mainUserName', '负责人', 28, 9, 16360, 0, 1229955);
INSERT INTO `72crm_admin_field_sort` VALUES (328190, 29, 'ownerUserName', '参与人', 10, 10, 16360, 0, 1229956);
INSERT INTO `72crm_admin_field_sort` VALUES (328191, 29, 'startTime', '计划开始时间', 13, 11, 16360, 0, 1229953);
INSERT INTO `72crm_admin_field_sort` VALUES (328192, 29, 'endTime', '计划结束时间', 13, 12, 16360, 0, 1229954);
INSERT INTO `72crm_admin_field_sort` VALUES (328193, 28, 'taskName', '任务', 29, 0, 16360, 0, 1229938);
INSERT INTO `72crm_admin_field_sort` VALUES (328194, 28, 'mainUserName', '负责人', 28, 1, 16360, 0, 1229939);
INSERT INTO `72crm_admin_field_sort` VALUES (328195, 28, 'subtasksName', '子任务', 32, 2, 16360, 0, 1229940);
INSERT INTO `72crm_admin_field_sort` VALUES (328196, 28, 'startTime', '开始时间', 13, 3, 16360, 0, 1229941);
INSERT INTO `72crm_admin_field_sort` VALUES (328197, 28, 'endTime', '结束时间', 13, 4, 16360, 0, 1229942);
INSERT INTO `72crm_admin_field_sort` VALUES (328198, 28, 'workingHours', '工时', 1, 5, 16360, 0, 1229943);
INSERT INTO `72crm_admin_field_sort` VALUES (328199, 28, 'hoursType', '工时类型', 3, 6, 16360, 0, 1229944);
INSERT INTO `72crm_admin_field_sort` VALUES (328200, 28, 'progressContent', '进度内容', 1, 7, 16360, 0, 1229945);
INSERT INTO `72crm_admin_field_sort` VALUES (328201, 28, 'checkUserName', '审批人', 1, 8, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328202, 28, 'createUserName', '创建人', 1, 9, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328203, 28, 'createTime', '发起时间', 4, 10, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328204, 28, 'examineTime', '审批时间', 4, 11, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328205, 28, 'checkStatus', '审核状态', 1, 12, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328206, 3, 'customerName', '客户名称', 15, 1, 16398, 0, 1229755);
INSERT INTO `72crm_admin_field_sort` VALUES (328207, 3, 'name', '姓名', 1, 5, 16398, 0, 1229754);
INSERT INTO `72crm_admin_field_sort` VALUES (328208, 3, '性别', '性别', 3, 0, 16398, 1, 1229764);
INSERT INTO `72crm_admin_field_sort` VALUES (328209, 3, '部门', '部门', 1, 3, 16398, 0, 1229981);
INSERT INTO `72crm_admin_field_sort` VALUES (328210, 3, 'mobile', '手机', 7, 4, 16398, 0, 1229756);
INSERT INTO `72crm_admin_field_sort` VALUES (328211, 3, 'telephone', '电话', 1, 6, 16398, 0, 1229757);
INSERT INTO `72crm_admin_field_sort` VALUES (328212, 3, 'email', '邮箱', 14, 0, 16398, 1, 1229758);
INSERT INTO `72crm_admin_field_sort` VALUES (328213, 3, 'post', '职务', 1, 0, 16398, 1, 1229759);
INSERT INTO `72crm_admin_field_sort` VALUES (328214, 3, '是否关键决策人', '是否关键决策人', 3, 0, 16398, 1, 1229760);
INSERT INTO `72crm_admin_field_sort` VALUES (328215, 3, 'address', '地址', 1, 0, 16398, 1, 1229761);
INSERT INTO `72crm_admin_field_sort` VALUES (328216, 3, 'nextTime', '下次联系时间', 13, 7, 16398, 0, 1229762);
INSERT INTO `72crm_admin_field_sort` VALUES (328217, 3, 'remark', '备注', 2, 8, 16398, 0, 1229763);
INSERT INTO `72crm_admin_field_sort` VALUES (328218, 3, 'lastTime', '最后跟进时间', 4, 9, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328219, 3, 'ownerUserName', '负责人', 1, 2, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328220, 3, 'updateTime', '更新时间', 4, 10, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328221, 3, 'createTime', '创建时间', 4, 11, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328222, 3, 'createUserName', '创建人', 1, 12, 16398, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328223, 20, 'milestoneNum', '里程碑编号', 1, 0, 16360, 0, 1229844);
INSERT INTO `72crm_admin_field_sort` VALUES (328224, 20, 'workName', '项目名称', 1, 1, 16360, 0, 1229909);
INSERT INTO `72crm_admin_field_sort` VALUES (328225, 20, 'workNum', '项目编号', 1, 2, 16360, 0, 1229846);
INSERT INTO `72crm_admin_field_sort` VALUES (328226, 20, 'milestoneName', '里程碑名称', 1, 3, 16360, 0, 1229845);
INSERT INTO `72crm_admin_field_sort` VALUES (328227, 20, 'instructions', '详细说明', 2, 4, 16360, 0, 1229852);
INSERT INTO `72crm_admin_field_sort` VALUES (328228, 20, 'degree', '完成度(%)', 6, 5, 16360, 0, 1229907);
INSERT INTO `72crm_admin_field_sort` VALUES (328229, 20, 'startTime', '开始时间', 13, 6, 16360, 0, 1229849);
INSERT INTO `72crm_admin_field_sort` VALUES (328230, 20, 'endTime', '结束时间', 13, 7, 16360, 0, 1229850);
INSERT INTO `72crm_admin_field_sort` VALUES (328231, 20, 'mainUserName', '负责人', 28, 8, 16360, 0, 1229851);
INSERT INTO `72crm_admin_field_sort` VALUES (328232, 20, 'taskName', '前置任务', 29, 9, 16360, 0, 1229906);
INSERT INTO `72crm_admin_field_sort` VALUES (328233, 20, 'updateTime', '更新时间', 4, 10, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328234, 20, 'createTime', '创建时间', 4, 11, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328235, 20, 'createUserName', '创建人', 1, 12, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328236, 24, 'subsystem', '子系统', 1, 0, 16407, 0, 1229910);
INSERT INTO `72crm_admin_field_sort` VALUES (328237, 24, 'childModule', '子模块', 1, 1, 16407, 0, 1229911);
INSERT INTO `72crm_admin_field_sort` VALUES (328238, 24, 'function1', '一级功能', 1, 2, 16407, 0, 1229912);
INSERT INTO `72crm_admin_field_sort` VALUES (328239, 24, 'function2', '二级功能', 1, 3, 16407, 0, 1229913);
INSERT INTO `72crm_admin_field_sort` VALUES (328240, 24, 'demandDescribe', '需求描述', 2, 4, 16407, 0, 1229914);
INSERT INTO `72crm_admin_field_sort` VALUES (328241, 24, 'demandStatus', '完成状态(%)', 6, 5, 16407, 0, 1229924);
INSERT INTO `72crm_admin_field_sort` VALUES (328242, 24, 'demandVersion', '需求版本号', 1, 6, 16407, 0, 1229915);
INSERT INTO `72crm_admin_field_sort` VALUES (328243, 24, 'plan', '对标投标方案号', 1, 7, 16407, 0, 1229925);
INSERT INTO `72crm_admin_field_sort` VALUES (328244, 24, 'demandNumber', '对标需求设计序号', 1, 8, 16407, 0, 1229916);
INSERT INTO `72crm_admin_field_sort` VALUES (328245, 24, 'productName', '对标产品名称', 1, 9, 16407, 0, 1229917);
INSERT INTO `72crm_admin_field_sort` VALUES (328246, 24, 'productVersion', '对标产品版本', 1, 10, 16407, 0, 1229918);
INSERT INTO `72crm_admin_field_sort` VALUES (328247, 24, 'productNumber', '产品编号', 1, 11, 16407, 0, 1229919);
INSERT INTO `72crm_admin_field_sort` VALUES (328248, 24, 'demandType', '需求类型', 3, 12, 16407, 0, 1229926);
INSERT INTO `72crm_admin_field_sort` VALUES (328249, 24, 'functionNumber', '功能编号', 1, 13, 16407, 0, 1229921);
INSERT INTO `72crm_admin_field_sort` VALUES (328250, 24, 'functionName', '功能名称', 1, 14, 16407, 0, 1229920);
INSERT INTO `72crm_admin_field_sort` VALUES (328251, 2, 'customerName', '客户名称', 1, 0, 16407, 0, 1229736);
INSERT INTO `72crm_admin_field_sort` VALUES (328252, 2, '客户来源', '客户来源', 3, 1, 16407, 0, 1229737);
INSERT INTO `72crm_admin_field_sort` VALUES (328253, 2, 'telephone', '电话', 1, 2, 16407, 0, 1229739);
INSERT INTO `72crm_admin_field_sort` VALUES (328254, 2, 'website', '网址', 1, 3, 16407, 0, 1229740);
INSERT INTO `72crm_admin_field_sort` VALUES (328255, 2, '客户行业', '客户行业', 3, 4, 16407, 0, 1229741);
INSERT INTO `72crm_admin_field_sort` VALUES (328256, 2, '客户级别', '客户级别', 3, 5, 16407, 0, 1229742);
INSERT INTO `72crm_admin_field_sort` VALUES (328257, 2, 'nextTime', '下次联系时间', 13, 6, 16407, 0, 1229743);
INSERT INTO `72crm_admin_field_sort` VALUES (328258, 2, 'remark', '备注', 2, 7, 16407, 0, 1229744);
INSERT INTO `72crm_admin_field_sort` VALUES (328259, 2, '客户属性', '客户属性', 9, 8, 16407, 0, 1229905);
INSERT INTO `72crm_admin_field_sort` VALUES (328260, 2, '附件', '附件', 8, 9, 16407, 0, 1229937);
INSERT INTO `72crm_admin_field_sort` VALUES (328261, 2, '客户类型', '客户类型', 1, 10, 16407, 0, 1229969);
INSERT INTO `72crm_admin_field_sort` VALUES (328262, 2, '单行文本', '单行文本', 1, 11, 16407, 0, 1229971);
INSERT INTO `72crm_admin_field_sort` VALUES (328263, 2, '所属部门', '所属部门', 3, 12, 16407, 0, 1229979);
INSERT INTO `72crm_admin_field_sort` VALUES (328264, 2, '客户省份', '客户省份', 1, 13, 16407, 0, 1229980);
INSERT INTO `72crm_admin_field_sort` VALUES (328265, 2, 'status', '锁定状态', 5, 14, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328266, 2, 'dealStatus', '成交状态', 3, 15, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328267, 2, 'lastTime', '最后跟进时间', 4, 16, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328268, 2, 'lastContent', '最后跟进记录', 1, 17, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328269, 2, 'detailAddress', '详细地址', 1, 18, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328270, 2, 'address', '地区定位', 18, 19, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328271, 2, 'poolDay', '距进入公海天数', 5, 20, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328272, 2, 'ownerUserName', '负责人', 1, 21, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328273, 2, 'updateTime', '更新时间', 4, 22, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328274, 2, 'createTime', '创建时间', 4, 23, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328275, 2, 'createUserName', '创建人', 1, 24, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328276, 1, '线索编号', '线索编号', 1, 0, 16407, 0, 1230005);
INSERT INTO `72crm_admin_field_sort` VALUES (328277, 1, 'leadsName', '线索名称', 1, 1, 16407, 0, 1229745);
INSERT INTO `72crm_admin_field_sort` VALUES (328278, 1, '线索来源', '线索来源', 3, 2, 16407, 0, 1229746);
INSERT INTO `72crm_admin_field_sort` VALUES (328279, 1, 'mobile', '手机', 7, 3, 16407, 0, 1229747);
INSERT INTO `72crm_admin_field_sort` VALUES (328280, 1, 'telephone', '电话', 1, 4, 16407, 0, 1229748);
INSERT INTO `72crm_admin_field_sort` VALUES (328281, 1, 'address', '地址', 1, 5, 16407, 0, 1229749);
INSERT INTO `72crm_admin_field_sort` VALUES (328282, 1, '客户行业', '客户行业', 3, 6, 16407, 0, 1229750);
INSERT INTO `72crm_admin_field_sort` VALUES (328283, 1, '客户级别', '客户级别', 3, 7, 16407, 0, 1229751);
INSERT INTO `72crm_admin_field_sort` VALUES (328284, 1, 'nextTime', '下次联系时间', 13, 8, 16407, 0, 1229752);
INSERT INTO `72crm_admin_field_sort` VALUES (328285, 1, 'remark', '备注', 2, 9, 16407, 0, 1229753);
INSERT INTO `72crm_admin_field_sort` VALUES (328286, 1, '人员', '人员', 10, 10, 16407, 0, 1229970);
INSERT INTO `72crm_admin_field_sort` VALUES (328287, 1, '部门', '部门', 12, 11, 16407, 0, 1229972);
INSERT INTO `72crm_admin_field_sort` VALUES (328288, 1, '测试', '测试', 1, 12, 16407, 0, 1229973);
INSERT INTO `72crm_admin_field_sort` VALUES (328289, 1, '测试多行', '测试多行', 2, 13, 16407, 0, 1229974);
INSERT INTO `72crm_admin_field_sort` VALUES (328290, 1, '下拉框', '下拉框', 3, 14, 16407, 0, 1229975);
INSERT INTO `72crm_admin_field_sort` VALUES (328291, 1, '邮箱', '邮箱', 14, 15, 16407, 0, 1229976);
INSERT INTO `72crm_admin_field_sort` VALUES (328292, 1, 'lastTime', '最后跟进时间', 4, 16, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328293, 1, 'lastContent', '最后跟进记录', 1, 17, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328294, 1, 'ownerUserName', '负责人', 1, 18, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328295, 1, 'updateTime', '更新时间', 4, 19, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328296, 1, 'createTime', '创建时间', 4, 20, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328297, 1, 'createUserName', '创建人', 1, 21, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328298, 5, '商机编号', '商机编号', 1, 0, 16407, 0, 1230007);
INSERT INTO `72crm_admin_field_sort` VALUES (328299, 5, '所属部门', '所属部门', 3, 1, 16407, 0, 1229992);
INSERT INTO `72crm_admin_field_sort` VALUES (328300, 5, 'businessName', '商机名称', 1, 2, 16407, 0, 1229772);
INSERT INTO `72crm_admin_field_sort` VALUES (328301, 5, 'customerName', '客户名称', 15, 3, 16407, 0, 1229773);
INSERT INTO `72crm_admin_field_sort` VALUES (328302, 5, 'money', '商机合同金额', 6, 4, 16407, 0, 1229774);
INSERT INTO `72crm_admin_field_sort` VALUES (328303, 5, '商机合同有效额', '商机合同有效额', 6, 5, 16407, 0, 1229983);
INSERT INTO `72crm_admin_field_sort` VALUES (328304, 5, 'dealDate', '预计成交日期', 13, 6, 16407, 0, 1229775);
INSERT INTO `72crm_admin_field_sort` VALUES (328305, 5, '竞争对手', '竞争对手', 1, 7, 16407, 0, 1229984);
INSERT INTO `72crm_admin_field_sort` VALUES (328306, 5, '签约产品', '签约产品', 9, 8, 16407, 0, 1229989);
INSERT INTO `72crm_admin_field_sort` VALUES (328307, 5, '是否招标', '是否招标', 3, 9, 16407, 0, 1229985);
INSERT INTO `72crm_admin_field_sort` VALUES (328308, 5, '中标概率预估', '中标概率预估', 3, 10, 16407, 0, 1229986);
INSERT INTO `72crm_admin_field_sort` VALUES (328309, 5, '当年启动概率', '当年启动概率', 3, 11, 16407, 0, 1229987);
INSERT INTO `72crm_admin_field_sort` VALUES (328310, 5, '预计签约时间', '预计签约时间', 4, 12, 16407, 0, 1229988);
INSERT INTO `72crm_admin_field_sort` VALUES (328311, 5, 'remark', '客户商机需求', 2, 13, 16407, 0, 1229776);
INSERT INTO `72crm_admin_field_sort` VALUES (328312, 5, '商机洽谈阶段', '商机洽谈阶段', 3, 14, 16407, 0, 1229991);
INSERT INTO `72crm_admin_field_sort` VALUES (328313, 5, '销售负责人', '销售负责人', 1, 15, 16407, 0, 1229993);
INSERT INTO `72crm_admin_field_sort` VALUES (328314, 5, 'typeName', '商机状态组', 3, 16, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328315, 5, 'statusName', '商机阶段', 3, 17, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328316, 5, 'lastTime', '最后跟进时间', 4, 18, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328317, 5, 'ownerUserName', '负责人', 1, 19, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328318, 5, 'updateTime', '更新时间', 4, 20, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328319, 5, 'createTime', '创建时间', 4, 21, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328320, 5, 'createUserName', '创建人', 1, 22, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328321, 6, '销售区域', '销售区域', 3, 0, 16407, 0, 1229996);
INSERT INTO `72crm_admin_field_sort` VALUES (328322, 6, 'num', '合同编号', 1, 1, 16407, 0, 1229778);
INSERT INTO `72crm_admin_field_sort` VALUES (328323, 6, 'customerName', '客户名称', 15, 2, 16407, 0, 1229779);
INSERT INTO `72crm_admin_field_sort` VALUES (328324, 6, 'name', '合同甲方（盖章名称）', 1, 3, 16407, 0, 1229777);
INSERT INTO `72crm_admin_field_sort` VALUES (328325, 6, 'businessName', '商机名称', 16, 4, 16407, 0, 1229780);
INSERT INTO `72crm_admin_field_sort` VALUES (328326, 6, '签订主体', '签订主体', 3, 5, 16407, 0, 1229995);
INSERT INTO `72crm_admin_field_sort` VALUES (328327, 6, '合同类型', '合同类型', 3, 6, 16407, 0, 1229788);
INSERT INTO `72crm_admin_field_sort` VALUES (328328, 6, 'money', '合同金额', 6, 7, 16407, 0, 1229781);
INSERT INTO `72crm_admin_field_sort` VALUES (328329, 6, '合同有效额', '合同有效额', 6, 8, 16407, 0, 1229994);
INSERT INTO `72crm_admin_field_sort` VALUES (328330, 6, 'orderDate', '签约时间', 4, 9, 16407, 0, 1229782);
INSERT INTO `72crm_admin_field_sort` VALUES (328331, 6, 'companyUserName', '销售签约人', 10, 10, 16407, 0, 1229786);
INSERT INTO `72crm_admin_field_sort` VALUES (328332, 6, 'endTime', '合同结束时间', 4, 11, 16407, 0, 1229784);
INSERT INTO `72crm_admin_field_sort` VALUES (328333, 6, 'remark', '备注', 2, 12, 16407, 0, 1229787);
INSERT INTO `72crm_admin_field_sort` VALUES (328334, 6, '签订合同附件', '签订合同附件', 8, 13, 16407, 0, 1229997);
INSERT INTO `72crm_admin_field_sort` VALUES (328335, 6, '付款说明', '付款说明', 9, 14, 16407, 0, 1229998);
INSERT INTO `72crm_admin_field_sort` VALUES (328336, 6, '回款状态', '回款状态', 3, 15, 16407, 0, 1229999);
INSERT INTO `72crm_admin_field_sort` VALUES (328337, 6, '纸质合同（行政部）', '纸质合同（行政部）', 3, 16, 16407, 0, 1230000);
INSERT INTO `72crm_admin_field_sort` VALUES (328338, 6, '侦查机设备签收单记录', '侦查机设备签收单记录', 1, 17, 16407, 0, 1230001);
INSERT INTO `72crm_admin_field_sort` VALUES (328339, 6, '钉钉审批单号', '钉钉审批单号', 1, 18, 16407, 0, 1230002);
INSERT INTO `72crm_admin_field_sort` VALUES (328340, 6, '项目状态', '项目状态', 3, 19, 16407, 0, 1230003);
INSERT INTO `72crm_admin_field_sort` VALUES (328341, 6, '业绩属性', '业绩属性', 3, 20, 16407, 0, 1230004);
INSERT INTO `72crm_admin_field_sort` VALUES (328342, 6, 'checkStatus', '审核状态', 5, 21, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328343, 6, 'receivedMoney', '已收款金额', 6, 22, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328344, 6, 'unreceivedMoney', '未收款金额', 6, 23, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328345, 6, 'lastTime', '最后跟进时间', 4, 24, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328346, 6, 'ownerUserName', '负责人', 1, 25, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328347, 6, 'updateTime', '更新时间', 4, 26, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328348, 6, 'createTime', '创建时间', 4, 27, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328349, 6, 'createUserName', '创建人', 1, 28, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328350, 7, 'number', '回款编号', 1, 0, 16407, 0, 1229789);
INSERT INTO `72crm_admin_field_sort` VALUES (328351, 7, 'customerName', '客户名称', 15, 1, 16407, 0, 1229790);
INSERT INTO `72crm_admin_field_sort` VALUES (328352, 7, 'contractNum', '合同编号', 20, 2, 16407, 0, 1229791);
INSERT INTO `72crm_admin_field_sort` VALUES (328353, 7, 'planNum', '期数', 21, 3, 16407, 0, 1229792);
INSERT INTO `72crm_admin_field_sort` VALUES (328354, 7, 'returnTime', '回款日期', 4, 4, 16407, 0, 1229793);
INSERT INTO `72crm_admin_field_sort` VALUES (328355, 7, 'money', '回款金额', 6, 5, 16407, 0, 1229794);
INSERT INTO `72crm_admin_field_sort` VALUES (328356, 7, 'returnType', '回款方式', 3, 6, 16407, 0, 1229795);
INSERT INTO `72crm_admin_field_sort` VALUES (328357, 7, 'remark', '备注', 2, 7, 16407, 0, 1229796);
INSERT INTO `72crm_admin_field_sort` VALUES (328358, 7, 'checkStatus', '审核状态', 5, 8, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328359, 7, 'contractMoney', '合同金额', 6, 9, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328360, 7, 'ownerUserName', '负责人', 1, 10, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328361, 7, 'updateTime', '更新时间', 4, 11, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328362, 7, 'createTime', '创建时间', 4, 12, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328363, 7, 'createUserName', '创建人', 1, 13, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328364, 17, '回访日期时间', '回访日期时间', 13, 0, 16407, 0, 1230013);
INSERT INTO `72crm_admin_field_sort` VALUES (328365, 17, 'visitNumber', '回访编号', 1, 1, 16407, 0, 1230017);
INSERT INTO `72crm_admin_field_sort` VALUES (328366, 17, 'visitTime', '回访时间', 13, 2, 16407, 0, 1230018);
INSERT INTO `72crm_admin_field_sort` VALUES (328367, 17, '回访客户姓名', '回访客户姓名', 1, 3, 16407, 0, 1230026);
INSERT INTO `72crm_admin_field_sort` VALUES (328368, 17, '回访目的', '回访目的', 3, 4, 16407, 0, 1230015);
INSERT INTO `72crm_admin_field_sort` VALUES (328369, 17, 'ownerUserName', '回访人', 10, 5, 16407, 0, 1230019);
INSERT INTO `72crm_admin_field_sort` VALUES (328370, 17, '回访详情描述', '回访详情描述', 2, 6, 16407, 0, 1230016);
INSERT INTO `72crm_admin_field_sort` VALUES (328371, 17, '回访形式', '回访形式', 3, 7, 16407, 0, 1230020);
INSERT INTO `72crm_admin_field_sort` VALUES (328372, 17, 'customerName', '客户名称', 15, 8, 16407, 0, 1230021);
INSERT INTO `72crm_admin_field_sort` VALUES (328373, 17, 'contactsName', '联系人', 17, 9, 16407, 0, 1230022);
INSERT INTO `72crm_admin_field_sort` VALUES (328374, 17, 'contractNum', '合同编号', 20, 10, 16407, 0, 1230023);
INSERT INTO `72crm_admin_field_sort` VALUES (328375, 17, '客户满意度', '客户满意度', 3, 11, 16407, 0, 1230024);
INSERT INTO `72crm_admin_field_sort` VALUES (328376, 17, '客户反馈', '客户反馈', 2, 12, 16407, 0, 1230025);
INSERT INTO `72crm_admin_field_sort` VALUES (328377, 17, 'updateTime', '更新时间', 4, 13, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328378, 17, 'createTime', '创建时间', 4, 14, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328379, 17, 'createUserName', '创建人', 1, 15, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328380, 4, 'name', '产品名称', 1, 0, 16407, 0, 1229765);
INSERT INTO `72crm_admin_field_sort` VALUES (328381, 4, 'categoryName', '产品类型', 19, 1, 16407, 0, 1229766);
INSERT INTO `72crm_admin_field_sort` VALUES (328382, 4, '单位', '单位', 3, 2, 16407, 0, 1229767);
INSERT INTO `72crm_admin_field_sort` VALUES (328383, 4, 'price', '价格', 6, 3, 16407, 0, 1229769);
INSERT INTO `72crm_admin_field_sort` VALUES (328384, 4, '是否上下架', '是否上下架', 3, 4, 16407, 0, 1229770);
INSERT INTO `72crm_admin_field_sort` VALUES (328385, 4, 'description', '销售产品描述', 1, 5, 16407, 0, 1229771);
INSERT INTO `72crm_admin_field_sort` VALUES (328386, 4, '数量', '数量', 5, 6, 16407, 0, 1230012);
INSERT INTO `72crm_admin_field_sort` VALUES (328387, 4, 'ownerUserName', '负责人', 1, 7, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328388, 4, 'updateTime', '更新时间', 4, 8, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328389, 4, 'createTime', '创建时间', 4, 9, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328390, 4, 'createUserName', '创建人', 1, 10, 16407, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328391, 1, '线索编号', '线索编号', 1, 0, 16360, 0, 1230005);
INSERT INTO `72crm_admin_field_sort` VALUES (328392, 1, 'leadsName', '线索名称', 1, 1, 16360, 0, 1229745);
INSERT INTO `72crm_admin_field_sort` VALUES (328393, 1, '线索来源', '线索来源', 3, 2, 16360, 0, 1229746);
INSERT INTO `72crm_admin_field_sort` VALUES (328394, 1, 'mobile', '手机', 7, 3, 16360, 0, 1229747);
INSERT INTO `72crm_admin_field_sort` VALUES (328395, 1, 'telephone', '电话', 1, 4, 16360, 0, 1229748);
INSERT INTO `72crm_admin_field_sort` VALUES (328396, 1, 'address', '地址', 1, 5, 16360, 0, 1229749);
INSERT INTO `72crm_admin_field_sort` VALUES (328397, 1, '客户行业', '客户行业', 3, 6, 16360, 0, 1229750);
INSERT INTO `72crm_admin_field_sort` VALUES (328398, 1, '客户级别', '客户级别', 3, 7, 16360, 0, 1229751);
INSERT INTO `72crm_admin_field_sort` VALUES (328399, 1, 'nextTime', '下次联系时间', 13, 8, 16360, 0, 1229752);
INSERT INTO `72crm_admin_field_sort` VALUES (328400, 1, 'remark', '备注', 2, 9, 16360, 0, 1229753);
INSERT INTO `72crm_admin_field_sort` VALUES (328401, 1, '人员', '人员', 10, 10, 16360, 0, 1229970);
INSERT INTO `72crm_admin_field_sort` VALUES (328402, 1, '部门', '部门', 12, 11, 16360, 0, 1229972);
INSERT INTO `72crm_admin_field_sort` VALUES (328403, 1, '测试', '测试', 1, 12, 16360, 0, 1229973);
INSERT INTO `72crm_admin_field_sort` VALUES (328404, 1, '测试多行', '测试多行', 2, 13, 16360, 0, 1229974);
INSERT INTO `72crm_admin_field_sort` VALUES (328405, 1, '下拉框', '下拉框', 3, 14, 16360, 0, 1229975);
INSERT INTO `72crm_admin_field_sort` VALUES (328406, 1, '邮箱', '邮箱', 14, 15, 16360, 0, 1229976);
INSERT INTO `72crm_admin_field_sort` VALUES (328407, 1, 'lastTime', '最后跟进时间', 4, 16, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328408, 1, 'lastContent', '最后跟进记录', 1, 17, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328409, 1, 'ownerUserName', '负责人', 1, 18, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328410, 1, 'updateTime', '更新时间', 4, 19, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328411, 1, 'createTime', '创建时间', 4, 20, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328412, 1, 'createUserName', '创建人', 1, 21, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328413, 24, 'subsystem', '子系统', 1, 0, 16360, 0, 1229910);
INSERT INTO `72crm_admin_field_sort` VALUES (328414, 24, 'childModule', '子模块', 1, 1, 16360, 0, 1229911);
INSERT INTO `72crm_admin_field_sort` VALUES (328415, 24, 'function1', '一级功能', 1, 2, 16360, 0, 1229912);
INSERT INTO `72crm_admin_field_sort` VALUES (328416, 24, 'function2', '二级功能', 1, 3, 16360, 0, 1229913);
INSERT INTO `72crm_admin_field_sort` VALUES (328417, 24, 'demandDescribe', '需求描述', 2, 4, 16360, 0, 1229914);
INSERT INTO `72crm_admin_field_sort` VALUES (328418, 24, 'demandStatus', '完成状态(%)', 6, 5, 16360, 0, 1229924);
INSERT INTO `72crm_admin_field_sort` VALUES (328419, 24, 'demandVersion', '需求版本号', 1, 6, 16360, 0, 1229915);
INSERT INTO `72crm_admin_field_sort` VALUES (328420, 24, 'plan', '对标投标方案号', 1, 7, 16360, 0, 1229925);
INSERT INTO `72crm_admin_field_sort` VALUES (328421, 24, 'demandNumber', '对标需求设计序号', 1, 8, 16360, 0, 1229916);
INSERT INTO `72crm_admin_field_sort` VALUES (328422, 24, 'productName', '对标产品名称', 1, 9, 16360, 0, 1229917);
INSERT INTO `72crm_admin_field_sort` VALUES (328423, 24, 'productVersion', '对标产品版本', 1, 10, 16360, 0, 1229918);
INSERT INTO `72crm_admin_field_sort` VALUES (328424, 24, 'productNumber', '产品编号', 1, 11, 16360, 0, 1229919);
INSERT INTO `72crm_admin_field_sort` VALUES (328425, 24, 'demandType', '需求类型', 3, 12, 16360, 0, 1229926);
INSERT INTO `72crm_admin_field_sort` VALUES (328426, 24, 'functionNumber', '功能编号', 1, 13, 16360, 0, 1229921);
INSERT INTO `72crm_admin_field_sort` VALUES (328427, 24, 'functionName', '功能名称', 1, 14, 16360, 0, 1229920);
INSERT INTO `72crm_admin_field_sort` VALUES (328428, 7, 'number', '回款编号', 1, 0, 16360, 0, 1229789);
INSERT INTO `72crm_admin_field_sort` VALUES (328429, 7, 'customerName', '客户名称', 15, 1, 16360, 0, 1229790);
INSERT INTO `72crm_admin_field_sort` VALUES (328430, 7, 'contractNum', '合同编号', 20, 2, 16360, 0, 1229791);
INSERT INTO `72crm_admin_field_sort` VALUES (328431, 7, 'planNum', '期数', 21, 3, 16360, 0, 1229792);
INSERT INTO `72crm_admin_field_sort` VALUES (328432, 7, 'returnTime', '回款日期', 4, 4, 16360, 0, 1229793);
INSERT INTO `72crm_admin_field_sort` VALUES (328433, 7, 'money', '回款金额', 6, 5, 16360, 0, 1229794);
INSERT INTO `72crm_admin_field_sort` VALUES (328434, 7, 'returnType', '回款方式', 3, 6, 16360, 0, 1229795);
INSERT INTO `72crm_admin_field_sort` VALUES (328435, 7, 'remark', '备注', 2, 7, 16360, 0, 1229796);
INSERT INTO `72crm_admin_field_sort` VALUES (328436, 7, 'checkStatus', '审核状态', 5, 8, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328437, 7, 'contractMoney', '合同金额', 6, 9, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328438, 7, 'ownerUserName', '负责人', 1, 10, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328439, 7, 'updateTime', '更新时间', 4, 11, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328440, 7, 'createTime', '创建时间', 4, 12, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328441, 7, 'createUserName', '创建人', 1, 13, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328442, 1, '线索编号', '线索编号', 1, 0, 16392, 0, 1230005);
INSERT INTO `72crm_admin_field_sort` VALUES (328443, 1, 'leadsName', '线索名称', 1, 1, 16392, 0, 1229745);
INSERT INTO `72crm_admin_field_sort` VALUES (328444, 1, '线索来源', '线索来源', 3, 2, 16392, 0, 1229746);
INSERT INTO `72crm_admin_field_sort` VALUES (328445, 1, 'mobile', '手机', 7, 3, 16392, 0, 1229747);
INSERT INTO `72crm_admin_field_sort` VALUES (328446, 1, 'telephone', '电话', 1, 4, 16392, 0, 1229748);
INSERT INTO `72crm_admin_field_sort` VALUES (328447, 1, 'address', '地址', 1, 5, 16392, 0, 1229749);
INSERT INTO `72crm_admin_field_sort` VALUES (328448, 1, '客户行业', '客户行业', 3, 6, 16392, 0, 1229750);
INSERT INTO `72crm_admin_field_sort` VALUES (328449, 1, '客户级别', '客户级别', 3, 7, 16392, 0, 1229751);
INSERT INTO `72crm_admin_field_sort` VALUES (328450, 1, 'nextTime', '下次联系时间', 13, 8, 16392, 0, 1229752);
INSERT INTO `72crm_admin_field_sort` VALUES (328451, 1, 'remark', '备注', 2, 9, 16392, 0, 1229753);
INSERT INTO `72crm_admin_field_sort` VALUES (328452, 1, '人员', '人员', 10, 10, 16392, 0, 1229970);
INSERT INTO `72crm_admin_field_sort` VALUES (328453, 1, '部门', '部门', 12, 11, 16392, 0, 1229972);
INSERT INTO `72crm_admin_field_sort` VALUES (328454, 1, '测试', '测试', 1, 12, 16392, 0, 1229973);
INSERT INTO `72crm_admin_field_sort` VALUES (328455, 1, '测试多行', '测试多行', 2, 13, 16392, 0, 1229974);
INSERT INTO `72crm_admin_field_sort` VALUES (328456, 1, '下拉框', '下拉框', 3, 14, 16392, 0, 1229975);
INSERT INTO `72crm_admin_field_sort` VALUES (328457, 1, '邮箱', '邮箱', 14, 15, 16392, 0, 1229976);
INSERT INTO `72crm_admin_field_sort` VALUES (328458, 1, 'lastTime', '最后跟进时间', 4, 16, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328459, 1, 'lastContent', '最后跟进记录', 1, 17, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328460, 1, 'ownerUserName', '负责人', 1, 18, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328461, 1, 'updateTime', '更新时间', 4, 19, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328462, 1, 'createTime', '创建时间', 4, 20, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328463, 1, 'createUserName', '创建人', 1, 21, 16392, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328464, 29, 'taskName', '子任务名称', 1, 0, 16407, 0, 1229950);
INSERT INTO `72crm_admin_field_sort` VALUES (328465, 29, 'status', '子任务状态', 3, 1, 16407, 0, 1229951);
INSERT INTO `72crm_admin_field_sort` VALUES (328466, 29, 'milestoneName', '里程碑', 30, 2, 16407, 0, 1229948);
INSERT INTO `72crm_admin_field_sort` VALUES (328467, 29, 'pName', '所属任务', 29, 3, 16407, 0, 1229947);
INSERT INTO `72crm_admin_field_sort` VALUES (328468, 29, 'classId', '任务类型', 3, 4, 16407, 0, 1229949);
INSERT INTO `72crm_admin_field_sort` VALUES (328469, 29, 'completeness', '任务完成度(%)', 5, 5, 16407, 0, 1229952);
INSERT INTO `72crm_admin_field_sort` VALUES (328470, 29, 'priority', '优先级', 3, 6, 16407, 0, 1229957);
INSERT INTO `72crm_admin_field_sort` VALUES (328471, 29, 'difficulty', '任务难度', 3, 7, 16407, 0, 1229958);
INSERT INTO `72crm_admin_field_sort` VALUES (328472, 29, 'degree', '重要程度', 3, 8, 16407, 0, 1229959);
INSERT INTO `72crm_admin_field_sort` VALUES (328473, 29, 'mainUserName', '负责人', 28, 9, 16407, 0, 1229955);
INSERT INTO `72crm_admin_field_sort` VALUES (328474, 29, 'ownerUserName', '参与人', 10, 10, 16407, 0, 1229956);
INSERT INTO `72crm_admin_field_sort` VALUES (328475, 29, 'startTime', '计划开始时间', 13, 11, 16407, 0, 1229953);
INSERT INTO `72crm_admin_field_sort` VALUES (328476, 29, 'endTime', '计划结束时间', 13, 12, 16407, 0, 1229954);
INSERT INTO `72crm_admin_field_sort` VALUES (328477, 29, 'taskName', '子任务名称', 1, 0, 16388, 0, 1229950);
INSERT INTO `72crm_admin_field_sort` VALUES (328478, 29, 'status', '子任务状态', 3, 1, 16388, 0, 1229951);
INSERT INTO `72crm_admin_field_sort` VALUES (328479, 29, 'milestoneName', '里程碑', 30, 2, 16388, 0, 1229948);
INSERT INTO `72crm_admin_field_sort` VALUES (328480, 29, 'pName', '所属任务', 29, 3, 16388, 0, 1229947);
INSERT INTO `72crm_admin_field_sort` VALUES (328481, 29, 'classId', '任务类型', 3, 4, 16388, 0, 1229949);
INSERT INTO `72crm_admin_field_sort` VALUES (328482, 29, 'completeness', '任务完成度(%)', 5, 5, 16388, 0, 1229952);
INSERT INTO `72crm_admin_field_sort` VALUES (328483, 29, 'priority', '优先级', 3, 6, 16388, 0, 1229957);
INSERT INTO `72crm_admin_field_sort` VALUES (328484, 29, 'difficulty', '任务难度', 3, 7, 16388, 0, 1229958);
INSERT INTO `72crm_admin_field_sort` VALUES (328485, 29, 'degree', '重要程度', 3, 8, 16388, 0, 1229959);
INSERT INTO `72crm_admin_field_sort` VALUES (328486, 29, 'mainUserName', '负责人', 28, 9, 16388, 0, 1229955);
INSERT INTO `72crm_admin_field_sort` VALUES (328487, 29, 'ownerUserName', '参与人', 10, 10, 16388, 0, 1229956);
INSERT INTO `72crm_admin_field_sort` VALUES (328488, 29, 'startTime', '计划开始时间', 13, 11, 16388, 0, 1229953);
INSERT INTO `72crm_admin_field_sort` VALUES (328489, 29, 'endTime', '计划结束时间', 13, 12, 16388, 0, 1229954);
INSERT INTO `72crm_admin_field_sort` VALUES (328490, 2, 'customerName', '客户名称', 1, 0, 16388, 0, 1229736);
INSERT INTO `72crm_admin_field_sort` VALUES (328491, 2, '客户来源', '客户来源', 3, 1, 16388, 0, 1229737);
INSERT INTO `72crm_admin_field_sort` VALUES (328492, 2, 'telephone', '电话', 1, 2, 16388, 0, 1229739);
INSERT INTO `72crm_admin_field_sort` VALUES (328493, 2, 'website', '网址', 1, 3, 16388, 0, 1229740);
INSERT INTO `72crm_admin_field_sort` VALUES (328494, 2, '客户行业', '客户行业', 3, 4, 16388, 0, 1229741);
INSERT INTO `72crm_admin_field_sort` VALUES (328495, 2, '客户级别', '客户级别', 3, 5, 16388, 0, 1229742);
INSERT INTO `72crm_admin_field_sort` VALUES (328496, 2, 'nextTime', '下次联系时间', 13, 6, 16388, 0, 1229743);
INSERT INTO `72crm_admin_field_sort` VALUES (328497, 2, 'remark', '备注', 2, 7, 16388, 0, 1229744);
INSERT INTO `72crm_admin_field_sort` VALUES (328498, 2, '客户属性', '客户属性', 9, 8, 16388, 0, 1229905);
INSERT INTO `72crm_admin_field_sort` VALUES (328499, 2, '附件', '附件', 8, 9, 16388, 0, 1229937);
INSERT INTO `72crm_admin_field_sort` VALUES (328500, 2, '客户类型', '客户类型', 1, 10, 16388, 0, 1229969);
INSERT INTO `72crm_admin_field_sort` VALUES (328501, 2, '单行文本', '单行文本', 1, 11, 16388, 0, 1229971);
INSERT INTO `72crm_admin_field_sort` VALUES (328502, 2, '所属部门', '所属部门', 3, 12, 16388, 0, 1229979);
INSERT INTO `72crm_admin_field_sort` VALUES (328503, 2, '客户省份', '客户省份', 1, 13, 16388, 0, 1229980);
INSERT INTO `72crm_admin_field_sort` VALUES (328504, 2, 'status', '锁定状态', 5, 14, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328505, 2, 'dealStatus', '成交状态', 3, 15, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328506, 2, 'lastTime', '最后跟进时间', 4, 16, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328507, 2, 'lastContent', '最后跟进记录', 1, 17, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328508, 2, 'detailAddress', '详细地址', 1, 18, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328509, 2, 'address', '地区定位', 18, 19, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328510, 2, 'poolDay', '距进入公海天数', 5, 20, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328511, 2, 'ownerUserName', '负责人', 1, 21, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328512, 2, 'updateTime', '更新时间', 4, 22, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328513, 2, 'createTime', '创建时间', 4, 23, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328514, 2, 'createUserName', '创建人', 1, 24, 16388, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328515, 30, 'configItems', '配置项', 1, 0, 16360, 0, 1229960);
INSERT INTO `72crm_admin_field_sort` VALUES (328516, 30, 'planTime', '计划时间', 13, 1, 16360, 0, 1229961);
INSERT INTO `72crm_admin_field_sort` VALUES (328517, 30, 'mainUserName', '负责人', 28, 2, 16360, 0, 1229962);
INSERT INTO `72crm_admin_field_sort` VALUES (328518, 30, 'associatedName', '项目名称', 31, 3, 16360, 0, 1229963);
INSERT INTO `72crm_admin_field_sort` VALUES (328519, 30, 'associatedNum', '项目编号', 1, 4, 16360, 0, 1229964);
INSERT INTO `72crm_admin_field_sort` VALUES (328520, 30, 'version', '版本', 1, 5, 16360, 0, 1229966);
INSERT INTO `72crm_admin_field_sort` VALUES (328521, 30, 'versionTime', '版本时间', 13, 6, 16360, 0, 1229967);
INSERT INTO `72crm_admin_field_sort` VALUES (328522, 30, 'versionDescribe', '版本说明', 2, 7, 16360, 0, 1229968);
INSERT INTO `72crm_admin_field_sort` VALUES (328523, 30, 'updateTime', '更新时间', 4, 8, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328524, 30, 'createTime', '创建时间', 4, 9, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328525, 30, 'createUserName', '创建人', 1, 10, 16360, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328526, 1, '线索编号', '线索编号', 1, 0, 16408, 0, 1230005);
INSERT INTO `72crm_admin_field_sort` VALUES (328527, 1, 'leadsName', '线索名称', 1, 1, 16408, 0, 1229745);
INSERT INTO `72crm_admin_field_sort` VALUES (328528, 1, '线索来源', '线索来源', 3, 2, 16408, 0, 1229746);
INSERT INTO `72crm_admin_field_sort` VALUES (328529, 1, 'mobile', '手机', 7, 3, 16408, 0, 1229747);
INSERT INTO `72crm_admin_field_sort` VALUES (328530, 1, 'telephone', '电话', 1, 4, 16408, 0, 1229748);
INSERT INTO `72crm_admin_field_sort` VALUES (328531, 1, 'address', '地址', 1, 5, 16408, 0, 1229749);
INSERT INTO `72crm_admin_field_sort` VALUES (328532, 1, '客户行业', '客户行业', 3, 6, 16408, 0, 1229750);
INSERT INTO `72crm_admin_field_sort` VALUES (328533, 1, '客户级别', '客户级别', 3, 7, 16408, 0, 1229751);
INSERT INTO `72crm_admin_field_sort` VALUES (328534, 1, 'nextTime', '下次联系时间', 13, 8, 16408, 0, 1229752);
INSERT INTO `72crm_admin_field_sort` VALUES (328535, 1, 'remark', '备注', 2, 9, 16408, 0, 1229753);
INSERT INTO `72crm_admin_field_sort` VALUES (328536, 1, '人员', '人员', 10, 10, 16408, 0, 1229970);
INSERT INTO `72crm_admin_field_sort` VALUES (328537, 1, '部门', '部门', 12, 11, 16408, 0, 1229972);
INSERT INTO `72crm_admin_field_sort` VALUES (328538, 1, '测试', '测试', 1, 12, 16408, 0, 1229973);
INSERT INTO `72crm_admin_field_sort` VALUES (328539, 1, '测试多行', '测试多行', 2, 13, 16408, 0, 1229974);
INSERT INTO `72crm_admin_field_sort` VALUES (328540, 1, '下拉框', '下拉框', 3, 14, 16408, 0, 1229975);
INSERT INTO `72crm_admin_field_sort` VALUES (328541, 1, '邮箱', '邮箱', 14, 15, 16408, 0, 1229976);
INSERT INTO `72crm_admin_field_sort` VALUES (328542, 1, 'lastTime', '最后跟进时间', 4, 16, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328543, 1, 'lastContent', '最后跟进记录', 1, 17, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328544, 1, 'ownerUserName', '负责人', 1, 18, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328545, 1, 'updateTime', '更新时间', 4, 19, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328546, 1, 'createTime', '创建时间', 4, 20, 16408, 0, NULL);
INSERT INTO `72crm_admin_field_sort` VALUES (328547, 1, 'createUserName', '创建人', 1, 21, 16408, 0, NULL);

-- ----------------------------
-- Table structure for 72crm_admin_field_style
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_field_style`;
CREATE TABLE `72crm_admin_field_style`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '样式表id',
  `style` int(5) NULL DEFAULT NULL COMMENT '字段宽度',
  `type` int(2) NULL DEFAULT NULL COMMENT '字段类型 \'1 线索 2 客户 3 联系人 4 产品 5 商机 6 合同 7回款 8公海\'',
  `field_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字段样式表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_field_style
-- ----------------------------
INSERT INTO `72crm_admin_field_style` VALUES (1, 100, 6, 'remark', 16360, '2020-09-16 15:36:35', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (2, 141, 0, 'workName', 16362, '2020-10-13 17:46:53', '2020-10-14 16:31:31');
INSERT INTO `72crm_admin_field_style` VALUES (3, 116, 0, 'ownerUserName', 16362, '2020-10-14 16:54:15', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (4, 183, 0, 'workNum', 16360, '2020-10-26 18:18:32', '2020-10-27 09:36:46');
INSERT INTO `72crm_admin_field_style` VALUES (5, 127, 0, 'endTime', 16371, '2020-11-02 14:21:55', '2020-11-02 15:27:17');
INSERT INTO `72crm_admin_field_style` VALUES (6, 161, 0, 'startTime', 16371, '2020-11-02 14:21:57', '2020-11-02 15:27:15');
INSERT INTO `72crm_admin_field_style` VALUES (7, 129, 0, 'workType', 16371, '2020-11-02 14:21:59', '2020-11-02 15:27:13');
INSERT INTO `72crm_admin_field_style` VALUES (8, 118, 0, 'workPhase', 16371, '2020-11-02 14:22:00', '2020-11-02 15:27:12');
INSERT INTO `72crm_admin_field_style` VALUES (9, 156, 0, 'workNum', 16371, '2020-11-02 15:27:02', '2020-11-27 18:43:16');
INSERT INTO `72crm_admin_field_style` VALUES (10, 138, 0, 'workName', 16371, '2020-11-02 15:27:04', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (11, 137, 0, 'deptName', 16371, '2020-11-02 15:27:05', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (12, 132, 0, 'area', 16371, '2020-11-02 15:27:06', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (13, 120, 0, 'managerName', 16371, '2020-11-02 15:27:08', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (14, 131, 0, 'salesName', 16371, '2020-11-02 15:27:10', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (15, 137, 0, 'associatedName', 16362, '2020-11-03 09:57:03', '2020-11-03 11:49:07');
INSERT INTO `72crm_admin_field_style` VALUES (16, 181, 0, 'workNum', 16362, '2020-11-03 16:00:30', '2020-11-27 18:46:19');
INSERT INTO `72crm_admin_field_style` VALUES (17, 124, 0, 'configItems', 16371, '2020-11-04 14:59:24', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (18, 99, 0, 'createUserName', 16373, '2020-11-06 15:38:49', '2020-11-06 15:38:49');
INSERT INTO `72crm_admin_field_style` VALUES (19, 131, 4, 'productNumber', 16362, '2020-11-12 17:53:44', '2020-11-25 10:46:33');
INSERT INTO `72crm_admin_field_style` VALUES (20, 140, 4, 'productNumber', 16360, '2020-11-12 17:56:06', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (21, 155, 0, 'createTime', 16371, '2020-11-17 11:45:38', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (22, 125, 0, 'updateTime', 16362, '2020-11-17 11:50:44', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (23, 161, 4, 'updateTime', 16362, '2020-11-18 17:28:41', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (24, 133, 0, 'associatedNum', 16378, '2020-11-19 11:12:39', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (25, 183, 2, 'poolDay', 16362, '2020-11-20 14:49:10', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (26, 145, 4, 'productNumber', 16371, '2020-11-25 09:33:49', '2020-11-25 10:19:03');
INSERT INTO `72crm_admin_field_style` VALUES (27, 202, 4, 'createUserName', 16360, '2020-11-25 10:03:31', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (28, 134, 0, 'workNum', 16372, '2020-11-30 14:02:51', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (29, 184, 0, 'deptName', 16362, '2020-12-04 16:10:09', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (30, 160, 0, 'areaName', 16362, '2020-12-04 16:10:12', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (31, 183, 0, 'createTime', 16408, '2020-12-15 09:19:31', '2020-12-21 16:16:33');
INSERT INTO `72crm_admin_field_style` VALUES (32, 214, 0, 'updateTime', 16408, '2020-12-15 09:20:01', '2020-12-21 16:16:29');
INSERT INTO `72crm_admin_field_style` VALUES (33, 95, 5, '商机编号', 16398, '2020-12-16 17:27:57', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (34, 141, 5, '商机编号', 16360, '2020-12-17 14:32:35', '2020-12-17 14:33:39');
INSERT INTO `72crm_admin_field_style` VALUES (35, 159, 5, 'typeName', 16360, '2020-12-17 14:33:29', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (36, 272, 4, 'name', 16360, '2020-12-17 15:40:47', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (37, 181, 4, 'categoryName', 16360, '2020-12-17 15:40:50', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (38, 120, 7, 'createTime', 16408, '2020-12-21 16:17:24', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (39, 143, 3, 'createTime', 16408, '2020-12-21 16:17:37', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (40, 135, 3, 'updateTime', 16408, '2020-12-21 16:17:42', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (41, 161, 4, 'createTime', 16408, '2020-12-21 16:20:39', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (42, 193, 2, 'createTime', 16408, '2020-12-23 14:43:48', NULL);
INSERT INTO `72crm_admin_field_style` VALUES (43, 128, 0, 'workName', 16408, '2020-12-30 13:06:27', NULL);

-- ----------------------------
-- Table structure for 72crm_admin_file
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_file`;
CREATE TABLE `72crm_admin_file`  (
  `file_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '附件名称',
  `size` bigint(20) NOT NULL COMMENT '附件大小（字节）',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件真实路径',
  `file_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件路径',
  `file_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'file' COMMENT '文件类型',
  `batch_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次id',
  PRIMARY KEY (`file_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 463 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '附件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_admin_login_log
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_login_log`;
CREATE TABLE `72crm_admin_login_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_user_id` bigint(20) NOT NULL COMMENT '操作人id',
  `login_time` datetime(0) NOT NULL COMMENT '登录时间',
  `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录ip地址',
  `login_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录地点',
  `device_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备类型',
  `core` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终端内核',
  `platform` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '平台',
  `imei` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IEMI设备号',
  `auth_result` int(2) NULL DEFAULT NULL COMMENT '认证结果 1成功 2失败',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 881 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登录日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_login_log
-- ----------------------------
INSERT INTO `72crm_admin_login_log` VALUES (730, 16360, '2020-12-10 15:25:21', '61.163.148.99', '中国河南省郑州市联通', '网页', 'Chrome76.0.3809.100', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (731, 16360, '2020-12-10 15:29:42', '61.163.148.99', '中国河南省郑州市联通', '移动端', 'Unknownnull', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (732, 16360, '2020-12-10 15:29:58', '61.163.148.99', '中国河南省郑州市联通', '移动端', 'Chrome70.0.3538.110', 'Android', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (733, 16360, '2020-12-10 15:30:26', '61.163.148.99', '中国河南省郑州市联通', '移动端', 'Chrome70.0.3538.110', 'Android', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (734, 16360, '2020-12-10 15:31:17', '61.163.148.99', '中国河南省郑州市联通', '移动端', 'Unknownnull', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (735, 16360, '2020-12-10 15:32:46', '61.163.148.99', '中国河南省郑州市联通', '移动端', 'Chrome83.0.4103.101', 'Android', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (736, 16360, '2020-12-10 15:33:11', '61.163.148.99', '中国河南省郑州市联通', '移动端', 'Unknownnull', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (737, 16360, '2020-12-10 15:34:25', '61.163.148.99', '中国河南省郑州市联通', '移动端', 'Unknownnull', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (738, 16360, '2020-12-10 15:35:07', '61.163.148.99', '中国河南省郑州市联通', '移动端', 'Chrome70.0.3538.110', 'Android', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (739, 16360, '2020-12-10 15:36:58', '61.163.148.99', '中国河南省郑州市联通', '移动端', 'Unknownnull', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (740, 16360, '2020-12-10 15:40:47', '192.168.1.66', '内网', '移动端', 'Safari13.0.3', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (741, 16360, '2020-12-10 16:54:09', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (742, 16360, '2020-12-10 16:55:05', '192.168.1.69', '内网', '移动端', 'Safari13.0.3', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (743, 16360, '2020-12-10 17:04:09', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (744, 16360, '2020-12-10 17:13:05', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Unknownnull', 'Unknown', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (745, 16360, '2020-12-10 17:17:22', '61.163.146.60', '中国河南省郑州市联通', '移动端', 'Unknownnull', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (746, 16360, '2020-12-10 17:19:57', '61.163.146.60', '中国河南省郑州市联通', '移动端', 'Unknownnull', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (747, 16360, '2020-12-10 17:20:39', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Unknownnull', 'Unknown', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (748, 16360, '2020-12-10 17:25:01', '192.168.1.69', '内网', '移动端', 'Safari13.0.3', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (749, 16360, '2020-12-10 17:27:46', '61.163.146.60', '中国河南省郑州市联通', '移动端', 'Unknownnull', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (750, 16360, '2020-12-10 17:31:18', '192.168.1.69', '内网', '移动端', 'Safari13.0.3', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (751, 16360, '2020-12-10 18:07:07', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Unknownnull', 'Unknown', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (752, 16360, '2020-12-11 09:14:48', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (753, 16360, '2020-12-11 09:20:31', '123.55.1.140', '中国河南省洛阳市电信', '网页', 'Chrome85.0.4183.121', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (754, 16360, '2020-12-11 09:22:30', '123.55.1.140', '中国河南省洛阳市电信', '网页', 'Chrome80.0.3987.163', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (755, 16360, '2020-12-11 09:23:47', '192.168.1.66', '内网', '移动端', 'Safari13.0.3', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (756, 16360, '2020-12-11 09:51:00', '61.163.146.60', '中国河南省郑州市联通', '移动端', 'Unknownnull', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (757, 16360, '2020-12-11 09:51:51', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Firefox83.0', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (758, 16388, '2020-12-11 09:55:53', '192.168.1.66', '内网', '移动端', 'Safari13.0.3', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (759, 16388, '2020-12-11 09:56:12', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Firefox83.0', 'Windows 10 or Windows Server 2016', NULL, 2);
INSERT INTO `72crm_admin_login_log` VALUES (760, 16388, '2020-12-11 09:56:14', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Firefox83.0', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (761, 16360, '2020-12-11 10:52:43', '123.55.1.140', '中国河南省洛阳市电信', '网页', 'Chrome80.0.3987.163', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (762, 16389, '2020-12-11 13:01:28', '123.55.1.140', '中国河南省洛阳市电信', '网页', 'Chrome80.0.3987.163', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (763, 16360, '2020-12-11 13:27:21', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Firefox83.0', 'Windows 10 or Windows Server 2016', NULL, 2);
INSERT INTO `72crm_admin_login_log` VALUES (764, 16360, '2020-12-11 13:27:22', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Firefox83.0', 'Windows 10 or Windows Server 2016', NULL, 2);
INSERT INTO `72crm_admin_login_log` VALUES (765, 16360, '2020-12-11 13:27:29', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Firefox83.0', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (766, 16360, '2020-12-11 13:57:48', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (767, 16388, '2020-12-11 14:26:17', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Firefox83.0', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (768, 16360, '2020-12-11 14:49:53', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (769, 16360, '2020-12-11 15:00:21', '45.251.20.210', '中国北京北京市电信', '网页', 'Chrome84.0.4147.125', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (770, 16392, '2020-12-11 15:04:22', '45.251.20.210', '中国北京北京市电信', '网页', 'Chrome84.0.4147.125', 'Windows 10 or Windows Server 2016', NULL, 2);
INSERT INTO `72crm_admin_login_log` VALUES (771, 16392, '2020-12-11 15:04:37', '45.251.20.210', '中国北京北京市电信', '网页', 'Chrome84.0.4147.125', 'Windows 10 or Windows Server 2016', NULL, 2);
INSERT INTO `72crm_admin_login_log` VALUES (772, 16360, '2020-12-11 15:04:43', '45.251.20.210', '中国北京北京市电信', '网页', 'Chrome84.0.4147.125', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (773, 16360, '2020-12-11 15:18:41', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (774, 16360, '2020-12-11 15:18:57', '45.251.20.210', '中国北京北京市电信', '网页', 'Chrome84.0.4147.125', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (775, 16360, '2020-12-11 15:19:04', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (776, 16360, '2020-12-11 15:19:56', '45.251.20.210', '中国北京北京市电信', '网页', 'Chrome84.0.4147.125', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (777, 16408, '2020-12-11 15:20:13', '61.163.146.60', '中国河南省郑州市联通', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (778, 16392, '2020-12-11 15:34:46', '45.251.20.210', '中国北京北京市电信', '网页', 'Chrome84.0.4147.125', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (779, 16360, '2020-12-11 15:36:08', '45.251.20.210', '中国北京北京市电信', '网页', 'Chrome84.0.4147.125', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (780, 16389, '2020-12-11 21:53:55', '223.88.130.173', '中国河南省洛阳市移动', '网页', 'Chrome80.0.3987.163', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (781, 16389, '2020-12-14 09:04:48', '1.199.193.51', '中国河南省洛阳市电信', '网页', 'Chrome80.0.3987.163', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (782, 16388, '2020-12-14 09:23:39', '61.163.151.203', '中国河南省郑州市联通', '网页', 'Chrome86.0.4240.111', 'Windows 10 or Windows Server 2016', NULL, 2);
INSERT INTO `72crm_admin_login_log` VALUES (783, 16388, '2020-12-14 09:23:52', '61.163.151.203', '中国河南省郑州市联通', '网页', 'Chrome86.0.4240.111', 'Windows 10 or Windows Server 2016', NULL, 2);
INSERT INTO `72crm_admin_login_log` VALUES (784, 16388, '2020-12-14 09:23:58', '61.163.151.203', '中国河南省郑州市联通', '网页', 'Chrome86.0.4240.111', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (785, 16360, '2020-12-14 09:40:27', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (786, 16394, '2020-12-14 10:32:38', '106.87.0.231', '中国重庆重庆市电信', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (787, 16404, '2020-12-14 10:55:58', '103.85.165.10', '孟加拉000', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (788, 16394, '2020-12-14 10:56:49', '106.87.0.231', '中国重庆重庆市电信', '移动端', 'Unknownnull', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (789, 16404, '2020-12-14 11:05:51', '103.85.165.10', '孟加拉000', '移动端', 'Chrome79.0.3945.116', 'Android', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (790, 16389, '2020-12-14 11:22:10', '1.199.193.51', '中国河南省洛阳市电信', '网页', 'Chrome80.0.3987.163', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (791, 16389, '2020-12-14 11:23:51', '1.199.193.51', '中国河南省洛阳市电信', '网页', 'Chrome80.0.3987.163', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (792, 16389, '2020-12-14 11:25:55', '61.163.151.203', '中国河南省郑州市联通', '网页', 'Firefox83.0', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (793, 16360, '2020-12-14 11:29:32', '1.199.193.51', '中国河南省洛阳市电信', '网页', 'Chrome80.0.3987.163', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (794, 16389, '2020-12-14 11:29:53', '1.199.193.51', '中国河南省洛阳市电信', '网页', 'Chrome80.0.3987.163', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (795, 16408, '2020-12-14 11:31:02', '61.163.151.203', '中国河南省郑州市联通', '网页', 'Firefox83.0', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (796, 16389, '2020-12-14 11:32:28', '1.199.193.51', '中国河南省洛阳市电信', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (797, 16389, '2020-12-14 11:33:27', '1.199.193.51', '中国河南省洛阳市电信', '网页', 'Chrome80.0.3987.163', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (798, 16408, '2020-12-14 11:33:39', '61.163.151.203', '中国河南省郑州市联通', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (799, 16405, '2020-12-14 17:56:16', '103.85.165.10', '孟加拉000', '网页', 'Chrome69.0.3497.100', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (800, 16360, '2020-12-14 17:59:35', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (801, 16405, '2020-12-14 18:00:01', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (802, 16399, '2020-12-15 11:00:10', '45.251.20.210', '中国北京北京市电信', '网页', 'Chrome78.0.3904.108', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (803, 16360, '2020-12-15 11:22:53', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (804, 16360, '2020-12-15 11:26:13', '45.251.20.210', '中国北京北京市电信', '网页', 'Chrome84.0.4147.125', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (805, 16360, '2020-12-15 13:31:56', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (806, 16389, '2020-12-15 16:39:59', '123.149.163.116', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (807, 16360, '2020-12-16 10:15:47', '223.104.3.242', '中国北京北京市移动', '移动端', 'Unknownnull', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (808, 16398, '2020-12-16 16:49:11', '45.251.20.210', '中国北京北京市电信', '网页', 'Chrome72.0.3626.81', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (809, 16360, '2020-12-17 10:14:36', '103.85.165.10', '孟加拉000', '网页', 'Chrome72.0.3626.81', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (810, 16398, '2020-12-17 10:48:59', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (811, 16407, '2020-12-17 10:56:59', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (812, 16389, '2020-12-17 14:27:33', '123.149.163.116', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (813, 16398, '2020-12-17 14:28:59', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (814, 16408, '2020-12-17 14:37:05', '125.47.74.249', '中国河南省郑州市联通', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (815, 16408, '2020-12-17 14:43:17', '125.47.74.249', '中国河南省郑州市联通', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (816, 16360, '2020-12-17 16:44:21', '123.149.163.116', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (817, 16389, '2020-12-17 16:45:02', '123.149.163.116', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (818, 16398, '2020-12-17 16:46:17', '103.85.165.10', '孟加拉000', '网页', 'Chrome72.0.3626.81', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (819, 16408, '2020-12-17 17:08:21', '125.47.74.249', '中国河南省郑州市联通', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (820, 16389, '2020-12-17 17:08:50', '123.149.163.116', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (821, 16389, '2020-12-17 17:12:04', '123.149.163.116', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (822, 16389, '2020-12-17 17:12:48', '123.149.163.116', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (823, 16360, '2020-12-17 17:13:06', '45.251.20.210', '中国北京北京市电信', '网页', 'Chrome84.0.4147.125', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (824, 16407, '2020-12-17 17:16:16', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 2);
INSERT INTO `72crm_admin_login_log` VALUES (825, 16407, '2020-12-17 17:16:27', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (826, 16411, '2020-12-17 17:25:12', '103.85.165.10', '孟加拉000', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (827, 16360, '2020-12-17 17:29:36', '45.251.20.210', '中国北京北京市电信', '网页', 'Chrome84.0.4147.125', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (828, 16389, '2020-12-19 15:16:40', '123.163.19.67', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (829, 16389, '2020-12-22 10:34:45', '1.199.193.162', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (830, 16404, '2020-12-22 11:39:40', '103.85.165.10', '孟加拉000', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (831, 16408, '2020-12-22 13:26:45', '61.163.150.50', '中国河南省郑州市联通', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (832, 16407, '2020-12-23 11:03:47', '117.136.38.172', '中国北京北京市移动', '移动端', 'Unknownnull', 'iPhone', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (833, 16389, '2020-12-23 11:34:05', '1.199.193.162', '中国河南省洛阳市电信', '移动端', 'Chrome52.0.2743.100', 'Android', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (834, 16389, '2020-12-28 14:11:54', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (835, 16389, '2020-12-28 15:26:42', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (836, 16408, '2020-12-28 15:35:37', '61.163.148.100', '中国河南省郑州市联通', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (837, 16360, '2020-12-29 17:29:56', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (838, 16392, '2020-12-29 17:34:58', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (839, 16388, '2020-12-29 17:53:35', '61.163.148.100', '中国河南省郑州市联通', '网页', 'Chrome86.0.4240.111', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (840, 16389, '2020-12-29 18:01:43', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (841, 16360, '2020-12-29 18:07:14', '61.163.148.100', '中国河南省郑州市联通', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (842, 16389, '2020-12-29 18:20:17', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (843, 16389, '2020-12-30 09:24:44', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (844, 16389, '2020-12-30 09:27:02', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (845, 16389, '2020-12-30 13:03:19', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (846, 16398, '2020-12-30 16:33:20', '45.251.20.210', '中国北京北京市电信', '网页', 'Chrome72.0.3626.81', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (847, 16407, '2020-12-30 17:39:11', '103.85.165.10', '孟加拉000', '网页', 'Chrome87.0.4280.88', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (848, 16389, '2020-12-30 17:57:20', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (849, 16389, '2020-12-30 18:04:36', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (850, 16360, '2020-12-30 18:04:43', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (851, 16407, '2020-12-30 18:06:18', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (852, 16389, '2020-12-30 18:06:43', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (853, 16360, '2020-12-30 18:07:24', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (854, 16360, '2020-12-30 18:10:33', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (855, 16389, '2020-12-30 18:10:39', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (856, 16360, '2020-12-30 18:15:59', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (857, 16389, '2020-12-30 18:20:44', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (858, 16360, '2020-12-30 18:22:13', '123.149.162.228', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (859, 16407, '2020-12-30 19:46:31', '103.85.165.10', '孟加拉000', '网页', 'Chrome87.0.4280.88', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (860, 16407, '2020-12-31 10:46:45', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (861, 16389, '2020-12-31 17:40:48', '123.149.160.192', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (862, 16360, '2020-12-31 17:44:05', '123.149.160.192', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (863, 16389, '2020-12-31 17:44:23', '123.149.160.192', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (864, 16389, '2021-01-04 11:34:49', '61.163.151.41', '中国河南省郑州市联通', '网页', 'Chrome76.0.3809.100', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (865, 16389, '2021-01-04 12:00:45', '123.55.2.95', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (866, 16407, '2021-01-04 16:59:01', '45.251.20.210', '中国北京北京市电信', '网页', 'Chrome69.0.3497.81', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (867, 16392, '2021-01-04 17:00:39', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (868, 16407, '2021-01-05 18:42:50', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (869, 16407, '2021-01-05 18:44:01', '103.85.165.10', '孟加拉000', '网页', 'Safari14.0.1', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (870, 16389, '2021-01-06 10:19:18', '1.199.194.111', '中国河南省洛阳市电信', '网页', 'Chrome86.0.4240.198', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (871, 16388, '2021-01-06 10:24:30', '61.163.145.188', '中国河南省郑州市联通', '网页', 'Chrome86.0.4240.111', 'Windows 10 or Windows Server 2016', NULL, 2);
INSERT INTO `72crm_admin_login_log` VALUES (872, 16388, '2021-01-06 10:24:37', '61.163.145.188', '中国河南省郑州市联通', '网页', 'Chrome86.0.4240.111', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (873, 16388, '2021-01-06 10:40:10', '61.163.145.188', '中国河南省郑州市联通', '网页', 'Safari13.1.2', 'OSX', NULL, 2);
INSERT INTO `72crm_admin_login_log` VALUES (874, 16388, '2021-01-06 10:40:14', '61.163.145.188', '中国河南省郑州市联通', '网页', 'Safari13.1.2', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (875, 16388, '2021-01-06 10:45:30', '61.163.145.188', '中国河南省郑州市联通', '网页', 'Safari14.0.2', 'OSX', NULL, 2);
INSERT INTO `72crm_admin_login_log` VALUES (876, 16388, '2021-01-06 10:45:36', '61.163.145.188', '中国河南省郑州市联通', '网页', 'Safari14.0.2', 'OSX', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (877, 16408, '2021-01-06 15:18:47', '61.163.145.188', '中国河南省郑州市联通', '网页', 'Chrome87.0.4280.88', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (878, 16360, '2021-01-07 17:17:57', '192.168.1.6', '内网', '网页', 'Chrome87.0.4280.141', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (879, 16408, '2021-01-11 16:25:14', '127.0.0.1', '内网', '网页', 'Firefox84.0', 'Windows 10 or Windows Server 2016', NULL, 1);
INSERT INTO `72crm_admin_login_log` VALUES (880, 16408, '2021-01-22 10:08:03', '127.0.0.1', '内网', '网页', 'Chrome87.0.4280.141', 'Windows 10 or Windows Server 2016', NULL, 1);

-- ----------------------------
-- Table structure for 72crm_admin_menu
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_menu`;
CREATE TABLE `72crm_admin_menu`  (
  `menu_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '上级菜单ID',
  `menu_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单名称',
  `realm` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '链接地址',
  `menu_type` int(1) NULL DEFAULT NULL COMMENT '菜单类型  1目录 2 菜单 3 按钮 4特殊',
  `sort` int(4) UNSIGNED NULL DEFAULT 0 COMMENT '排序（同级有效）',
  `status` int(4) NULL DEFAULT 1 COMMENT '状态 1 启用 0 禁用',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单说明',
  PRIMARY KEY (`menu_id`) USING BTREE,
  INDEX `menu_id`(`menu_id`) USING BTREE,
  INDEX `parent_id`(`parent_id`) USING BTREE,
  INDEX `realm`(`realm`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 635 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_menu
-- ----------------------------
INSERT INTO `72crm_admin_menu` VALUES (1, 0, '全部', 'crm', 0, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (2, 0, '全部', 'bi', 0, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (3, 0, '全部', 'manage', 0, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (9, 1, '线索管理', 'leads', 1, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (10, 1, '客户管理', 'customer', 1, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (11, 1, '联系人管理', 'contacts', 1, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (12, 1, '商机管理', 'business', 1, 4, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (13, 1, '合同管理', 'contract', 1, 5, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (14, 1, '回款管理', 'receivables', 1, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (15, 1, '产品管理', 'product', 1, 7, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (17, 9, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (18, 9, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (19, 9, '查看列表', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (20, 9, '查看详情', 'read', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (21, 9, '导入', 'excelimport', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (22, 9, '导出', 'excelexport', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (23, 9, '删除', 'delete', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (24, 9, '转移', 'transfer', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (25, 9, '转化', 'transform', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (26, 10, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (27, 10, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (28, 10, '查看列表', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (29, 10, '查看详情', 'read', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (30, 10, '导入', 'excelimport', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (31, 10, '导出', 'excelexport', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (32, 10, '删除', 'delete', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (33, 10, '转移', 'transfer', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (34, 10, '放入公海', 'putinpool', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (35, 10, '锁定/解锁', 'lock', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (36, 10, '编辑团队成员', 'teamsave', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (40, 11, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (41, 11, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (42, 11, '查看列表', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (43, 11, '查看详情', 'read', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (44, 11, '删除', 'delete', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (45, 11, '转移', 'transfer', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (46, 12, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (47, 12, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (48, 12, '查看列表', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (49, 12, '查看详情', 'read', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (50, 12, '删除', 'delete', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (51, 12, '转移', 'transfer', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (52, 12, '编辑团队成员', 'teamsave', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (53, 13, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (54, 13, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (55, 13, '查看列表', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (56, 13, '查看详情', 'read', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (57, 13, '删除', 'delete', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (58, 13, '转移', 'transfer', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (59, 13, '编辑团队成员', 'teamsave', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (60, 14, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (61, 14, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (62, 14, '查看列表', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (63, 14, '查看详情', 'read', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (64, 14, '删除', 'delete', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (65, 15, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (66, 15, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (67, 15, '查看列表', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (68, 15, '查看详情', 'read', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (69, 15, '上架/下架', 'status', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (70, 15, '转移', 'transfer', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (71, 14, '转移', 'transfer', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (97, 2, '业绩目标完成情况', 'achievement', 1, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (98, 2, '销售漏斗', 'business', 1, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (99, 2, '员工客户分析', 'customer', 1, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (101, 2, '员工业绩分析', 'contract', 1, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (102, 97, '查看', 'read', 3, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (103, 98, '查看', 'read', 3, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (104, 99, '查看', 'read', 3, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (106, 101, '查看', 'read', 3, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (107, 11, '联系人导出', 'excelexport', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (108, 11, '联系人导入', 'excelimport', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (109, 15, '产品导入', 'excelimport', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (110, 15, '产品导出', 'excelexport', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (117, 2, '产品分析', 'product', 1, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (118, 117, '查看', 'read', 3, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (123, 2, '客户画像分析', 'portrait', 1, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (124, 123, '查看', 'read', 3, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (125, 2, '排行榜', 'ranking', 1, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (126, 125, '查看', 'read', 3, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (146, 2, '办公分析', 'oa', 1, 10, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (147, 146, '查看', 'read', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (148, 0, '全部', 'oa', 0, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (149, 0, '全部', 'project', 0, 0, 1, '项目管理角色权限');
INSERT INTO `72crm_admin_menu` VALUES (150, 148, '通讯录', 'book', 1, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (151, 150, '查看', 'read', 3, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (152, 149, '项目管理', 'projectManage', 1, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (153, 152, '新建项目', 'save', 3, 2, 1, 'projectSave');
INSERT INTO `72crm_admin_menu` VALUES (160, 3, '企业首页', 'system', 1, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (161, 160, '查看', 'read', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (162, 160, '编辑', 'update', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (163, 3, '应用管理', 'configSet', 1, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (164, 163, '查看', 'read', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (165, 163, '停用/启用', 'update', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (166, 3, '员工与部门管理', 'users', 1, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (167, 166, '部门/员工查看', 'read', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (168, 166, '员工新建', 'userSave', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (169, 166, '员工禁用/激活', 'userEnables', 3, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (170, 166, '员工操作', 'userUpdate', 3, 4, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (171, 166, '部门新建', 'deptSave', 3, 5, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (172, 166, '部门编辑', 'deptUpdate', 3, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (173, 166, '部门删除', 'deptDelete', 3, 7, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (174, 3, '角色权限管理', 'permission', 1, 4, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (175, 174, '角色权限设置', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (176, 3, '工作台设置', 'oa', 1, 5, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (177, 176, '办公审批管理', 'examine', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (178, 3, '审批流程管理', 'examineFlow', 1, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (179, 178, '审批流程管理', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (180, 3, '客户管理设置', 'crm', 1, 7, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (181, 180, '自定义字段设置', 'field', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (182, 180, '客户公海规则', 'pool', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (183, 180, '业务参数设置', 'setting', 3, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (184, 180, '业绩目标设置', 'achievement', 3, 4, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (187, 148, '公告', 'announcement', 1, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (188, 187, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (189, 187, '编辑', 'update', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (190, 187, '删除', 'delete', 3, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (191, 10, '设置成交状态', 'dealStatus', 3, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (192, 13, '合同作废', 'discard', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (193, 2, '呼叫中心', 'call', 1, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (194, 193, '查询通话记录', 'index', 1, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (195, 193, '通话记录分析', 'analysis', 1, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (196, 1, '小程序管理', 'applet', 1, 8, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (197, 196, '小程序列表', 'index', 1, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (198, 196, '导出', 'excelexport', 1, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (199, 196, '删除', 'delete', 1, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (200, 1, '市场活动', 'marketing', 1, 1, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (201, 200, '新建', 'save', 3, 1, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (202, 200, '查看列表', 'index', 3, 1, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (204, 200, '编辑', 'update', 3, 1, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (205, 200, '删除', 'delete', 3, 1, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (206, 200, '启用/停用', 'updateStatus', 3, 1, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (207, 200, '查看详情', 'read', 3, 1, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (208, 13, '导出', 'excelexport', 3, 1, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (209, 12, '导出', 'excelexport', 3, 1, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (211, 15, '删除', 'delete', 3, 1, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (212, 14, '导出', 'excelexport', 3, 1, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (213, 1, '外勤', 'outwork', 1, 9, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (214, 213, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (215, 213, '查看', 'read', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (216, 213, '删除', 'delete', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (217, 213, '设置', 'setting', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (218, 10, '附近的客户', 'nearbyCustomer', 3, 1, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (220, 3, '小程序管理设置', 'card', 1, 9, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (221, 220, '小程序管理', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (230, 3, '系统日志', 'adminLog', 1, 10, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (231, 230, '系统登录日志', 'systemLog', 3, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (232, 230, '数据操作日志', 'actionRecord', 3, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (300, 0, '项目管理', 'work', 0, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (301, 300, '项目', 'project', 1, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (310, 301, '项目设置', 'setWork', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (311, 301, '项目导出', 'excelExport', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (312, 301, '新建任务列表', 'saveTaskClass', 3, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (313, 301, '编辑任务列表', 'updateTaskClass', 3, 4, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (314, 301, '移动任务列表', 'updateClassOrder', 3, 5, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (315, 301, '删除任务列表', 'deleteTaskClass', 3, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (316, 301, '新建任务', 'saveTask', 3, 7, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (317, 301, '完成任务', 'setTaskStatus', 3, 8, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (318, 301, '编辑任务标题', 'setTaskTitle', 3, 9, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (319, 301, '编辑任务描述', 'setTaskDescription', 3, 10, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (320, 301, '分配任务', 'setTaskMainUser', 3, 11, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (321, 301, '设置任务时间', 'setTaskTime', 3, 12, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (322, 301, '设置任务标签', 'setTaskLabel', 3, 13, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (323, 301, '添加任务参与人', 'setTaskOwnerUser', 3, 14, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (324, 301, '设置任务优先级', 'setTaskPriority', 3, 15, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (325, 301, '移动任务', 'setTaskOrder', 3, 16, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (326, 301, '归档任务', 'archiveTask', 3, 17, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (327, 301, '删除任务', 'deleteTask', 3, 18, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (328, 301, '彻底删除任务', 'cleanTask', 3, 19, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (329, 301, '任务添加附件', 'uploadTaskFile', 3, 20, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (330, 301, '任务删除附件', 'deleteTaskFile', 3, 21, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (331, 301, '项目导入', 'excelImport', 3, 22, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (332, 301, '新建子任务', 'addChildTask', 3, 23, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (333, 301, '编辑子任务', 'updateChildTask', 3, 24, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (334, 301, '删除子任务', 'deleteChildTask', 3, 25, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (335, 301, '恢复任务', 'restoreTask', 3, 26, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (336, 301, '关联业务', 'saveTaskRelation', 3, 27, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (337, 301, '完成子任务', 'setChildTaskStatus', 3, 28, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (400, 1, '客户回访管理', 'visit', 1, 8, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (401, 400, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (402, 400, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (403, 400, '查看列表', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (404, 400, '查看详情', 'read', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (405, 400, '删除', 'delete', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (420, 1, '发票管理', 'invoice', 1, 9, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (421, 420, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (422, 420, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (423, 420, '查看列表', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (424, 420, '查看详情', 'read', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (425, 420, '删除', 'delete', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (426, 420, '转移', 'transfer', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (427, 420, '标记开票', 'updateInvoiceStatus', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (428, 420, '重置开票信息', 'resetInvoiceStatus', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (431, 152, '查看项目', 'index', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (432, 152, '删除项目', 'delete', 3, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (434, 152, '导出项目', 'excelexport', 3, 5, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (435, 152, '更改项目阶段', 'updatePhase', 3, 6, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (436, 152, '查看项目详情', 'readDetails', 3, 7, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (437, 152, '修改项目详情', 'updateDetails', 3, 8, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (438, 149, '里程碑', 'milestone', 1, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (439, 438, '查看里程碑', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (440, 438, '删除里程碑', 'delete', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (442, 438, '导出里程碑', 'excelexport', 3, 4, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (444, 438, '查看历史变更记录', 'readHistorical', 3, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (445, 438, '导出历史变更记录', 'excelexportHistorical', 3, 7, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (446, 438, '更改完成度', 'updateDegree', 3, 8, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (447, 149, '需求', 'demand', 1, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (448, 447, '查看需求', 'index', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (449, 447, '删除需求', 'delete', 3, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (451, 447, '导出需求', 'excelexport', 3, 4, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (452, 447, '导入需求', 'excelImport', 3, 5, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (453, 447, '查看历史变更记录', 'readHistorical', 3, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (454, 447, '导出历史变更记录', 'excelexportHistorical', 3, 7, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (455, 149, '配置计划', 'configManage', 1, 4, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (456, 455, '查看配置计划', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (457, 455, '删除配置计划', 'delete', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (459, 455, '导出配置计划', 'excelexport', 3, 4, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (461, 455, '查看历史变更记录', 'readHistorical', 3, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (462, 455, '导出历史变更记录', 'excelexportHistorical', 3, 7, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (463, 149, '资源配置', 'resources', 1, 5, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (464, 463, '查看资源配置', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (465, 463, '删除资源配置', 'delete', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (467, 463, '导出资源配置', 'excelexport', 3, 4, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (468, 463, '导入资源配置', 'excelImport', 3, 5, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (469, 463, '查看历史变更记录', 'readHistorical', 3, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (470, 463, '导出历史变更记录', 'excelexportHistorical', 3, 7, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (471, 149, '任务计划', 'task', 1, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (472, 471, '查看任务计划', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (473, 471, '删除任务计划', 'delete', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (475, 471, '导出任务计划', 'excelexport', 3, 4, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (476, 471, '导入任务计划', 'excelImport', 3, 5, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (477, 471, '查看历史变更记录', 'readHistorical', 3, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (478, 471, '导出历史变更记录', 'excelexportHistorical', 3, 7, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (479, 471, '新建子任务', 'createSubtasks', 3, 8, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (480, 471, '批量增加参与人', 'saveParticipant', 3, 9, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (481, 471, '批量移除参与人', 'delParticipant', 3, 10, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (484, 149, '任务资料', 'taskFile', 1, 8, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (485, 484, '查看任务资料', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (486, 484, '删除任务资料', 'delete', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (488, 484, '导出任务资料', 'excelexport', 3, 4, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (489, 484, '上传', 'save', 3, 5, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (490, 484, '下载', 'download', 3, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (491, 149, '任务项目信息', 'taskWorkInformation', 1, 9, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (492, 491, '查看任务项目信息', 'details', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (493, 149, '子任务管理', 'subtasks', 1, 10, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (494, 493, '查看子任务', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (495, 493, '删除子任务', 'delete', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (497, 493, '导出子任务', 'excelexport', 3, 4, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (498, 149, '工时提报', 'taskHours', 1, 11, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (499, 498, '查看', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (502, 498, '导出', 'excelexport', 3, 4, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (503, 498, '导出记录', 'exportRecord', 3, 5, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (504, 498, '上传成果', 'uploadResults', 3, 6, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (505, 498, '撤回', 'withdraw', 3, 7, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (506, 149, '人力资源', 'userResources', 1, 12, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (507, 506, '查看人力资源', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (508, 506, '删除人力资源', 'delete', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (510, 506, '导出人力资源', 'excelexport', 3, 4, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (512, 506, '工时历史记录', 'hoursHistory', 3, 7, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (513, 506, '人员任务列表', 'userTask', 3, 8, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (514, 149, '风险管理', 'risk', 1, 13, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (515, 514, '查看风险管理', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (516, 514, '新建风险管理', 'save', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (517, 514, '编辑风险管理', 'update', 3, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (518, 514, '删除风险管理', 'delete', 3, 4, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (519, 514, '导出风险管理', 'excelexport', 3, 5, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (520, 514, '导入风险管理', 'excelImport', 3, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (521, 149, '变更记录', 'change', 1, 14, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (522, 521, '查看变更记录', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (523, 521, '新建变更记录', 'save', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (524, 521, '编辑变更记录', 'update', 3, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (525, 521, '删除变更记录', 'delete', 3, 4, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (526, 521, '导出变更记录', 'excelexport', 3, 5, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (527, 149, '历史变更记录', 'historical', 1, 15, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (528, 527, '查看历史变更记录', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (529, 527, '导出历史变更记录', 'excelexport', 3, 2, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (530, 149, '需求管理项目列表', 'demandWork', 1, 16, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (531, 530, '查看', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (532, 530, '编辑', 'update', 3, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (533, 530, '导出', 'excelexport', 3, 5, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (534, 149, '需求列表', 'demandList', 1, 17, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (535, 534, '查看', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (536, 534, '新建', 'save', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (537, 534, '编辑', 'update', 3, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (538, 534, '删除', 'delete', 3, 4, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (539, 534, '导出', 'excelexport', 3, 5, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (540, 534, '产品对标', 'tag', 3, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (543, 149, '产品', 'product', 1, 19, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (544, 543, '查看', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (545, 543, '新建', 'save', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (546, 543, '编辑', 'update', 3, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (547, 543, '删除', 'delete', 3, 4, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (548, 543, '导出', 'excelexport', 3, 5, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (549, 543, '导入', 'excelImport', 3, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (550, 543, '产品状态修改', 'status', 3, 7, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (552, 149, '产品使用统计', 'productStatistics', 1, 20, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (553, 552, '查看', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (554, 552, '导出', 'excelexport', 3, 5, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (555, 149, '产品功能', 'productFunction', 1, 21, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (556, 555, '查看', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (557, 555, '新建', 'save', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (558, 555, '编辑', 'update', 3, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (559, 555, '删除', 'delete', 3, 4, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (560, 555, '导出', 'excelexport', 3, 5, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (561, 555, '导入', 'excelImport', 3, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (562, 543, '查看版本更新记录', 'readHistorical', 3, 7, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (563, 149, '配置管理', 'workConfig', 1, 22, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (564, 563, '查看', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (565, 563, '新建', 'save', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (566, 563, '编辑', 'update', 3, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (567, 563, '删除', 'delete', 3, 4, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (568, 563, '导出', 'excelexport', 3, 5, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (569, 563, '导入', 'excelImport', 3, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (570, 149, '工时锁定', 'hoursLock', 1, 23, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (571, 570, '查看', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (572, 570, '新建', 'save', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (573, 570, '导出', 'excelexport', 3, 5, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (577, 149, '工时导出记录', 'hoursExcelexport', 1, 25, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (578, 577, '查看', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (579, 577, '导出', 'excelexport', 3, 5, 1, '');
INSERT INTO `72crm_admin_menu` VALUES (581, 152, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (582, 438, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (583, 438, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (584, 447, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (585, 447, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (586, 455, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (587, 455, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (588, 463, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (589, 463, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (590, 471, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (591, 471, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (592, 482, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (593, 482, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (596, 506, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (602, 484, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (603, 149, '项目详情任务资料', 'workFile', 1, 8, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (604, 603, '查看项目任务资料', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (605, 603, '删除项目任务资料', 'delete', 3, 2, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (607, 603, '上传', 'save', 3, 5, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (608, 603, '下载', 'download', 3, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (609, 603, '编辑', 'update', 3, 3, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (610, 493, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (611, 493, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (612, 498, '编辑', 'update', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (613, 498, '新建', 'save', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (614, 152, '查看历史变更记录', 'readHistorical', 3, 6, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (615, 152, '导出历史变更记录', 'excelexportHistorical', 3, 7, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (616, 471, '查询任务详细信息', 'readDetails', 3, 0, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (617, 570, '删除', 'delete', 3, 4, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (618, 149, '项目统计图', 'workStatistics', 1, 9, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (619, 618, '查看', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (620, 618, '导出', 'excelexport', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (621, 618, '下载', 'download', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (622, 618, '项目成本比率', 'workCost', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (623, 149, '产品统计图', 'productStatistical', 1, 11, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (624, 623, '查看', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (625, 623, '导出', 'excelexport', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (626, 623, '下载', 'download', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (627, 149, '风险统计图', 'riskStatistics', 1, 12, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (628, 627, '查看', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (629, 627, '导出', 'excelexport', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (630, 627, '下载', 'download', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (631, 149, '人力资源统计图', 'manpowerStatistics', 1, 13, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (632, 631, '查看', 'index', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (633, 631, '导出', 'excelexport', 3, 1, 1, NULL);
INSERT INTO `72crm_admin_menu` VALUES (634, 631, '下载', 'download', 3, 1, 1, NULL);

-- ----------------------------
-- Table structure for 72crm_admin_message
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_message`;
CREATE TABLE `72crm_admin_message`  (
  `message_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息标题',
  `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `label` int(2) NULL DEFAULT NULL COMMENT '消息大类 1 任务 2 日志 3 oa审批 4公告 5 日程 6 crm消息',
  `type` int(2) NULL DEFAULT NULL COMMENT '消息类型 详见AdminMessageEnum',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '关联ID',
  `create_user` bigint(20) NOT NULL COMMENT '消息创建者 0为系统',
  `recipient_user` bigint(20) NOT NULL COMMENT '接收人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_read` int(1) NULL DEFAULT 0 COMMENT '是否已读 0 未读 1 已读',
  `read_time` datetime(0) NULL DEFAULT NULL COMMENT '已读时间',
  PRIMARY KEY (`message_id`) USING BTREE,
  INDEX `recipient_user`(`recipient_user`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 315 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_admin_model_sort
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_model_sort`;
CREATE TABLE `72crm_admin_model_sort`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(4) NOT NULL COMMENT '导航类型 1头部导航 2客户管理左侧导航',
  `model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模块  1仪表盘 2待办事项 3线索 4客户 5联系人 6商机 7合同 8回款 9发票 10回访 11产品 12市场活动',
  `sort` int(4) NOT NULL COMMENT '排序',
  `is_hidden` int(2) NULL DEFAULT 0 COMMENT '是否隐藏  0不隐藏 1隐藏',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客户管理导航栏排序表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_admin_number_setting
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_number_setting`;
CREATE TABLE `72crm_admin_number_setting`  (
  `setting_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '设置id',
  `pid` int(11) NOT NULL COMMENT '父级设置id',
  `sort` int(2) NOT NULL COMMENT '编号顺序',
  `type` int(2) NOT NULL COMMENT '编号类型 1文本 2日期 3数字',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文本内容或日期格式或起始编号',
  `increase_number` int(11) NULL DEFAULT NULL COMMENT '递增数',
  `reset_type` int(2) NULL DEFAULT NULL COMMENT '重新编号周期 1每天 2每月 3每年 4从不',
  `last_number` int(10) NULL DEFAULT NULL COMMENT '上次生成的编号',
  `last_date` date NULL DEFAULT NULL COMMENT '上次生成的时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  PRIMARY KEY (`setting_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '编号设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_number_setting
-- ----------------------------
INSERT INTO `72crm_admin_number_setting` VALUES (81, 426780, 0, 1, 'CP', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (82, 426780, 1, 2, 'yyyyMMdd', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (83, 426780, 2, 3, '1', 1, 4, 1, '2020-12-11', NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (84, 426777, 0, 1, 'LCB', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (85, 426777, 1, 2, 'yyyyMMdd', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (86, 426777, 2, 3, '1', 1, 4, 1, '2021-01-06', NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (87, 377624, 0, 1, 'OP', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (88, 377624, 1, 2, 'yyyyMMdd', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (89, 377624, 2, 3, '01', 2, 4, NULL, NULL, NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (90, 426778, 0, 1, 'RW', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (91, 426778, 1, 2, 'yyyyMMdd', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (92, 426778, 2, 3, '1', 1, 4, 2, '2021-01-06', NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (93, 426776, 0, 1, 'XM', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (95, 426776, 1, 2, 'yyyyMMdd', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (96, 426776, 2, 3, '1', 1, 4, 1, '2021-01-05', NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (97, 394007, 0, 1, 'BH', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (98, 394007, 1, 2, 'yyyy', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `72crm_admin_number_setting` VALUES (99, 394007, 2, 3, '1', 1, 4, 2, '2020-12-17', NULL, NULL);

-- ----------------------------
-- Table structure for 72crm_admin_official_img
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_official_img`;
CREATE TABLE `72crm_admin_official_img`  (
  `official_img_id` int(11) NOT NULL AUTO_INCREMENT,
  `size` bigint(20) NULL DEFAULT NULL COMMENT '附件大小（字节）',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件真实路径',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件路径',
  `type` int(1) NULL DEFAULT NULL COMMENT '1.官网设置 2.名片海报',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tactic` int(6) NULL DEFAULT NULL COMMENT '0',
  PRIMARY KEY (`official_img_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '官网图片' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_admin_role
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_role`;
CREATE TABLE `72crm_admin_role`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `role_type` int(1) NULL DEFAULT NULL COMMENT '0、自定义角色1、管理角色 2、客户管理角色 3、人事角色 4、财务角色 5、项目角色 8、项目自定义角色',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` int(3) NULL DEFAULT 1 COMMENT '1 启用 0 禁用',
  `data_type` int(1) NOT NULL DEFAULT 5 COMMENT '数据权限 1、本人，2、本人及下属，3、本部门，4、本部门及下属部门，5、全部 ',
  `is_hidden` int(1) NOT NULL DEFAULT 1 COMMENT '0 隐藏 1 不隐藏',
  `label` int(2) NULL DEFAULT NULL COMMENT '1 系统项目管理员角色 2 项目管理角色 3 项目编辑角色 4 项目只读角色',
  PRIMARY KEY (`role_id`) USING BTREE,
  INDEX `role_type`(`role_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 198341 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_role
-- ----------------------------
INSERT INTO `72crm_admin_role` VALUES (198324, '超级管理员', 1, 'admin', 1, 5, 1, 5);
INSERT INTO `72crm_admin_role` VALUES (198325, '系统设置管理员', 1, NULL, 1, 2, 1, 6);
INSERT INTO `72crm_admin_role` VALUES (198326, '部门与员工管理员', 1, NULL, 1, 5, 1, 7);
INSERT INTO `72crm_admin_role` VALUES (198327, '审批流管理员', 1, NULL, 1, 5, 1, 8);
INSERT INTO `72crm_admin_role` VALUES (198328, '工作台管理员', 1, NULL, 1, 5, 1, 9);
INSERT INTO `72crm_admin_role` VALUES (198329, '客户管理员', 1, NULL, 1, 5, 1, 10);
INSERT INTO `72crm_admin_role` VALUES (198330, '公告管理员', 7, NULL, 1, 5, 1, 11);
INSERT INTO `72crm_admin_role` VALUES (198331, '销售经理角色', 2, NULL, 1, 5, 1, NULL);
INSERT INTO `72crm_admin_role` VALUES (198332, '销售员角色', 2, NULL, 1, 2, 1, NULL);
INSERT INTO `72crm_admin_role` VALUES (198334, '管理', 5, '系统默认权限，包含所在项目所有权限', 1, 5, 0, 2);
INSERT INTO `72crm_admin_role` VALUES (198335, '编辑', 5, '成员初始加入时默认享有的权限', 1, 5, 1, 3);
INSERT INTO `72crm_admin_role` VALUES (198336, '只读', 5, '项目只读角色', 1, 1, 1, 4);
INSERT INTO `72crm_admin_role` VALUES (198339, '项目管理角色', 8, NULL, 1, 2, 1, NULL);
INSERT INTO `72crm_admin_role` VALUES (198340, '销售经理权限', 1, NULL, 1, 5, 1, NULL);

-- ----------------------------
-- Table structure for 72crm_admin_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_role_menu`;
CREATE TABLE `72crm_admin_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `menu_id`(`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2557097 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单对应关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_role_menu
-- ----------------------------
INSERT INTO `72crm_admin_role_menu` VALUES (2538675, 198325, 3);
INSERT INTO `72crm_admin_role_menu` VALUES (2538676, 198325, 160);
INSERT INTO `72crm_admin_role_menu` VALUES (2538677, 198325, 161);
INSERT INTO `72crm_admin_role_menu` VALUES (2538678, 198325, 162);
INSERT INTO `72crm_admin_role_menu` VALUES (2538679, 198325, 163);
INSERT INTO `72crm_admin_role_menu` VALUES (2538680, 198325, 164);
INSERT INTO `72crm_admin_role_menu` VALUES (2538681, 198325, 165);
INSERT INTO `72crm_admin_role_menu` VALUES (2538682, 198325, 166);
INSERT INTO `72crm_admin_role_menu` VALUES (2538683, 198325, 167);
INSERT INTO `72crm_admin_role_menu` VALUES (2538684, 198325, 168);
INSERT INTO `72crm_admin_role_menu` VALUES (2538685, 198325, 169);
INSERT INTO `72crm_admin_role_menu` VALUES (2538686, 198325, 170);
INSERT INTO `72crm_admin_role_menu` VALUES (2538687, 198325, 171);
INSERT INTO `72crm_admin_role_menu` VALUES (2538688, 198325, 172);
INSERT INTO `72crm_admin_role_menu` VALUES (2538689, 198325, 173);
INSERT INTO `72crm_admin_role_menu` VALUES (2538690, 198325, 174);
INSERT INTO `72crm_admin_role_menu` VALUES (2538691, 198325, 175);
INSERT INTO `72crm_admin_role_menu` VALUES (2538692, 198325, 176);
INSERT INTO `72crm_admin_role_menu` VALUES (2538693, 198325, 177);
INSERT INTO `72crm_admin_role_menu` VALUES (2538694, 198325, 178);
INSERT INTO `72crm_admin_role_menu` VALUES (2538695, 198325, 179);
INSERT INTO `72crm_admin_role_menu` VALUES (2538696, 198325, 180);
INSERT INTO `72crm_admin_role_menu` VALUES (2538697, 198325, 181);
INSERT INTO `72crm_admin_role_menu` VALUES (2538698, 198325, 182);
INSERT INTO `72crm_admin_role_menu` VALUES (2538699, 198325, 183);
INSERT INTO `72crm_admin_role_menu` VALUES (2538700, 198325, 184);
INSERT INTO `72crm_admin_role_menu` VALUES (2538701, 198325, 185);
INSERT INTO `72crm_admin_role_menu` VALUES (2538702, 198325, 186);
INSERT INTO `72crm_admin_role_menu` VALUES (2538703, 198326, 166);
INSERT INTO `72crm_admin_role_menu` VALUES (2538704, 198326, 167);
INSERT INTO `72crm_admin_role_menu` VALUES (2538705, 198326, 168);
INSERT INTO `72crm_admin_role_menu` VALUES (2538706, 198326, 169);
INSERT INTO `72crm_admin_role_menu` VALUES (2538707, 198326, 170);
INSERT INTO `72crm_admin_role_menu` VALUES (2538708, 198326, 171);
INSERT INTO `72crm_admin_role_menu` VALUES (2538709, 198326, 172);
INSERT INTO `72crm_admin_role_menu` VALUES (2538710, 198326, 173);
INSERT INTO `72crm_admin_role_menu` VALUES (2538711, 198327, 178);
INSERT INTO `72crm_admin_role_menu` VALUES (2538712, 198327, 179);
INSERT INTO `72crm_admin_role_menu` VALUES (2538713, 198328, 176);
INSERT INTO `72crm_admin_role_menu` VALUES (2538714, 198328, 177);
INSERT INTO `72crm_admin_role_menu` VALUES (2538715, 198329, 180);
INSERT INTO `72crm_admin_role_menu` VALUES (2538716, 198329, 181);
INSERT INTO `72crm_admin_role_menu` VALUES (2538717, 198329, 182);
INSERT INTO `72crm_admin_role_menu` VALUES (2538718, 198329, 183);
INSERT INTO `72crm_admin_role_menu` VALUES (2538719, 198329, 184);
INSERT INTO `72crm_admin_role_menu` VALUES (2538720, 198330, 187);
INSERT INTO `72crm_admin_role_menu` VALUES (2538721, 198330, 188);
INSERT INTO `72crm_admin_role_menu` VALUES (2538722, 198330, 189);
INSERT INTO `72crm_admin_role_menu` VALUES (2538723, 198330, 190);
INSERT INTO `72crm_admin_role_menu` VALUES (2538854, 198335, 300);
INSERT INTO `72crm_admin_role_menu` VALUES (2538855, 198335, 301);
INSERT INTO `72crm_admin_role_menu` VALUES (2538856, 198335, 310);
INSERT INTO `72crm_admin_role_menu` VALUES (2538857, 198335, 311);
INSERT INTO `72crm_admin_role_menu` VALUES (2538858, 198335, 312);
INSERT INTO `72crm_admin_role_menu` VALUES (2538859, 198335, 313);
INSERT INTO `72crm_admin_role_menu` VALUES (2538860, 198335, 314);
INSERT INTO `72crm_admin_role_menu` VALUES (2538861, 198335, 315);
INSERT INTO `72crm_admin_role_menu` VALUES (2538862, 198335, 316);
INSERT INTO `72crm_admin_role_menu` VALUES (2538863, 198335, 317);
INSERT INTO `72crm_admin_role_menu` VALUES (2538864, 198335, 318);
INSERT INTO `72crm_admin_role_menu` VALUES (2538865, 198335, 319);
INSERT INTO `72crm_admin_role_menu` VALUES (2538866, 198335, 320);
INSERT INTO `72crm_admin_role_menu` VALUES (2538867, 198335, 321);
INSERT INTO `72crm_admin_role_menu` VALUES (2538868, 198335, 322);
INSERT INTO `72crm_admin_role_menu` VALUES (2538869, 198335, 323);
INSERT INTO `72crm_admin_role_menu` VALUES (2538870, 198335, 324);
INSERT INTO `72crm_admin_role_menu` VALUES (2538871, 198335, 325);
INSERT INTO `72crm_admin_role_menu` VALUES (2538872, 198335, 326);
INSERT INTO `72crm_admin_role_menu` VALUES (2538873, 198335, 327);
INSERT INTO `72crm_admin_role_menu` VALUES (2538874, 198335, 328);
INSERT INTO `72crm_admin_role_menu` VALUES (2538875, 198335, 329);
INSERT INTO `72crm_admin_role_menu` VALUES (2538876, 198335, 330);
INSERT INTO `72crm_admin_role_menu` VALUES (2538877, 198335, 331);
INSERT INTO `72crm_admin_role_menu` VALUES (2538878, 198335, 332);
INSERT INTO `72crm_admin_role_menu` VALUES (2538879, 198335, 333);
INSERT INTO `72crm_admin_role_menu` VALUES (2538880, 198335, 334);
INSERT INTO `72crm_admin_role_menu` VALUES (2538881, 198335, 335);
INSERT INTO `72crm_admin_role_menu` VALUES (2538882, 198335, 336);
INSERT INTO `72crm_admin_role_menu` VALUES (2538883, 198335, 337);
INSERT INTO `72crm_admin_role_menu` VALUES (2541912, 198332, 1);
INSERT INTO `72crm_admin_role_menu` VALUES (2541913, 198332, 9);
INSERT INTO `72crm_admin_role_menu` VALUES (2541914, 198332, 17);
INSERT INTO `72crm_admin_role_menu` VALUES (2541915, 198332, 18);
INSERT INTO `72crm_admin_role_menu` VALUES (2541916, 198332, 19);
INSERT INTO `72crm_admin_role_menu` VALUES (2541917, 198332, 20);
INSERT INTO `72crm_admin_role_menu` VALUES (2541918, 198332, 21);
INSERT INTO `72crm_admin_role_menu` VALUES (2541919, 198332, 22);
INSERT INTO `72crm_admin_role_menu` VALUES (2541920, 198332, 23);
INSERT INTO `72crm_admin_role_menu` VALUES (2541921, 198332, 24);
INSERT INTO `72crm_admin_role_menu` VALUES (2541922, 198332, 25);
INSERT INTO `72crm_admin_role_menu` VALUES (2541923, 198332, 10);
INSERT INTO `72crm_admin_role_menu` VALUES (2541924, 198332, 26);
INSERT INTO `72crm_admin_role_menu` VALUES (2541925, 198332, 27);
INSERT INTO `72crm_admin_role_menu` VALUES (2541926, 198332, 28);
INSERT INTO `72crm_admin_role_menu` VALUES (2541927, 198332, 29);
INSERT INTO `72crm_admin_role_menu` VALUES (2541928, 198332, 30);
INSERT INTO `72crm_admin_role_menu` VALUES (2541929, 198332, 31);
INSERT INTO `72crm_admin_role_menu` VALUES (2541930, 198332, 32);
INSERT INTO `72crm_admin_role_menu` VALUES (2541931, 198332, 33);
INSERT INTO `72crm_admin_role_menu` VALUES (2541932, 198332, 34);
INSERT INTO `72crm_admin_role_menu` VALUES (2541933, 198332, 35);
INSERT INTO `72crm_admin_role_menu` VALUES (2541934, 198332, 36);
INSERT INTO `72crm_admin_role_menu` VALUES (2541935, 198332, 191);
INSERT INTO `72crm_admin_role_menu` VALUES (2541936, 198332, 218);
INSERT INTO `72crm_admin_role_menu` VALUES (2541937, 198332, 11);
INSERT INTO `72crm_admin_role_menu` VALUES (2541938, 198332, 40);
INSERT INTO `72crm_admin_role_menu` VALUES (2541939, 198332, 41);
INSERT INTO `72crm_admin_role_menu` VALUES (2541940, 198332, 42);
INSERT INTO `72crm_admin_role_menu` VALUES (2541941, 198332, 43);
INSERT INTO `72crm_admin_role_menu` VALUES (2541942, 198332, 44);
INSERT INTO `72crm_admin_role_menu` VALUES (2541943, 198332, 45);
INSERT INTO `72crm_admin_role_menu` VALUES (2541944, 198332, 107);
INSERT INTO `72crm_admin_role_menu` VALUES (2541945, 198332, 108);
INSERT INTO `72crm_admin_role_menu` VALUES (2541946, 198332, 12);
INSERT INTO `72crm_admin_role_menu` VALUES (2541947, 198332, 46);
INSERT INTO `72crm_admin_role_menu` VALUES (2541948, 198332, 47);
INSERT INTO `72crm_admin_role_menu` VALUES (2541949, 198332, 48);
INSERT INTO `72crm_admin_role_menu` VALUES (2541950, 198332, 49);
INSERT INTO `72crm_admin_role_menu` VALUES (2541951, 198332, 50);
INSERT INTO `72crm_admin_role_menu` VALUES (2541952, 198332, 51);
INSERT INTO `72crm_admin_role_menu` VALUES (2541953, 198332, 52);
INSERT INTO `72crm_admin_role_menu` VALUES (2541954, 198332, 209);
INSERT INTO `72crm_admin_role_menu` VALUES (2541955, 198332, 13);
INSERT INTO `72crm_admin_role_menu` VALUES (2541956, 198332, 53);
INSERT INTO `72crm_admin_role_menu` VALUES (2541957, 198332, 54);
INSERT INTO `72crm_admin_role_menu` VALUES (2541958, 198332, 55);
INSERT INTO `72crm_admin_role_menu` VALUES (2541959, 198332, 56);
INSERT INTO `72crm_admin_role_menu` VALUES (2541960, 198332, 57);
INSERT INTO `72crm_admin_role_menu` VALUES (2541961, 198332, 58);
INSERT INTO `72crm_admin_role_menu` VALUES (2541962, 198332, 59);
INSERT INTO `72crm_admin_role_menu` VALUES (2541963, 198332, 192);
INSERT INTO `72crm_admin_role_menu` VALUES (2541964, 198332, 208);
INSERT INTO `72crm_admin_role_menu` VALUES (2541965, 198332, 14);
INSERT INTO `72crm_admin_role_menu` VALUES (2541966, 198332, 60);
INSERT INTO `72crm_admin_role_menu` VALUES (2541967, 198332, 61);
INSERT INTO `72crm_admin_role_menu` VALUES (2541968, 198332, 62);
INSERT INTO `72crm_admin_role_menu` VALUES (2541969, 198332, 63);
INSERT INTO `72crm_admin_role_menu` VALUES (2541970, 198332, 64);
INSERT INTO `72crm_admin_role_menu` VALUES (2541971, 198332, 71);
INSERT INTO `72crm_admin_role_menu` VALUES (2541972, 198332, 212);
INSERT INTO `72crm_admin_role_menu` VALUES (2541973, 198332, 15);
INSERT INTO `72crm_admin_role_menu` VALUES (2541974, 198332, 65);
INSERT INTO `72crm_admin_role_menu` VALUES (2541975, 198332, 66);
INSERT INTO `72crm_admin_role_menu` VALUES (2541976, 198332, 67);
INSERT INTO `72crm_admin_role_menu` VALUES (2541977, 198332, 68);
INSERT INTO `72crm_admin_role_menu` VALUES (2541978, 198332, 69);
INSERT INTO `72crm_admin_role_menu` VALUES (2541979, 198332, 70);
INSERT INTO `72crm_admin_role_menu` VALUES (2541980, 198332, 109);
INSERT INTO `72crm_admin_role_menu` VALUES (2541981, 198332, 110);
INSERT INTO `72crm_admin_role_menu` VALUES (2541982, 198332, 211);
INSERT INTO `72crm_admin_role_menu` VALUES (2541983, 198332, 196);
INSERT INTO `72crm_admin_role_menu` VALUES (2541984, 198332, 197);
INSERT INTO `72crm_admin_role_menu` VALUES (2541985, 198332, 198);
INSERT INTO `72crm_admin_role_menu` VALUES (2541986, 198332, 199);
INSERT INTO `72crm_admin_role_menu` VALUES (2541987, 198332, 200);
INSERT INTO `72crm_admin_role_menu` VALUES (2541988, 198332, 201);
INSERT INTO `72crm_admin_role_menu` VALUES (2541989, 198332, 202);
INSERT INTO `72crm_admin_role_menu` VALUES (2541990, 198332, 204);
INSERT INTO `72crm_admin_role_menu` VALUES (2541991, 198332, 205);
INSERT INTO `72crm_admin_role_menu` VALUES (2541992, 198332, 206);
INSERT INTO `72crm_admin_role_menu` VALUES (2541993, 198332, 207);
INSERT INTO `72crm_admin_role_menu` VALUES (2541994, 198332, 213);
INSERT INTO `72crm_admin_role_menu` VALUES (2541995, 198332, 214);
INSERT INTO `72crm_admin_role_menu` VALUES (2541996, 198332, 215);
INSERT INTO `72crm_admin_role_menu` VALUES (2541997, 198332, 216);
INSERT INTO `72crm_admin_role_menu` VALUES (2541998, 198332, 217);
INSERT INTO `72crm_admin_role_menu` VALUES (2541999, 198332, 400);
INSERT INTO `72crm_admin_role_menu` VALUES (2542000, 198332, 401);
INSERT INTO `72crm_admin_role_menu` VALUES (2542001, 198332, 402);
INSERT INTO `72crm_admin_role_menu` VALUES (2542002, 198332, 403);
INSERT INTO `72crm_admin_role_menu` VALUES (2542003, 198332, 404);
INSERT INTO `72crm_admin_role_menu` VALUES (2542004, 198332, 405);
INSERT INTO `72crm_admin_role_menu` VALUES (2542005, 198332, 420);
INSERT INTO `72crm_admin_role_menu` VALUES (2542006, 198332, 421);
INSERT INTO `72crm_admin_role_menu` VALUES (2542007, 198332, 422);
INSERT INTO `72crm_admin_role_menu` VALUES (2542008, 198332, 423);
INSERT INTO `72crm_admin_role_menu` VALUES (2542009, 198332, 424);
INSERT INTO `72crm_admin_role_menu` VALUES (2542010, 198332, 425);
INSERT INTO `72crm_admin_role_menu` VALUES (2542011, 198332, 426);
INSERT INTO `72crm_admin_role_menu` VALUES (2542012, 198332, 427);
INSERT INTO `72crm_admin_role_menu` VALUES (2542013, 198332, 428);
INSERT INTO `72crm_admin_role_menu` VALUES (2542014, 198332, 97);
INSERT INTO `72crm_admin_role_menu` VALUES (2542015, 198332, 102);
INSERT INTO `72crm_admin_role_menu` VALUES (2542016, 198332, 98);
INSERT INTO `72crm_admin_role_menu` VALUES (2542017, 198332, 103);
INSERT INTO `72crm_admin_role_menu` VALUES (2542018, 198332, 99);
INSERT INTO `72crm_admin_role_menu` VALUES (2542019, 198332, 104);
INSERT INTO `72crm_admin_role_menu` VALUES (2542020, 198332, 101);
INSERT INTO `72crm_admin_role_menu` VALUES (2542021, 198332, 106);
INSERT INTO `72crm_admin_role_menu` VALUES (2542022, 198332, 117);
INSERT INTO `72crm_admin_role_menu` VALUES (2542023, 198332, 118);
INSERT INTO `72crm_admin_role_menu` VALUES (2542024, 198332, 123);
INSERT INTO `72crm_admin_role_menu` VALUES (2542025, 198332, 124);
INSERT INTO `72crm_admin_role_menu` VALUES (2542026, 198332, 125);
INSERT INTO `72crm_admin_role_menu` VALUES (2542027, 198332, 126);
INSERT INTO `72crm_admin_role_menu` VALUES (2556644, 198340, 163);
INSERT INTO `72crm_admin_role_menu` VALUES (2556645, 198340, 164);
INSERT INTO `72crm_admin_role_menu` VALUES (2556646, 198340, 165);
INSERT INTO `72crm_admin_role_menu` VALUES (2556647, 198340, 231);
INSERT INTO `72crm_admin_role_menu` VALUES (2556807, 198339, 149);
INSERT INTO `72crm_admin_role_menu` VALUES (2556808, 198339, 152);
INSERT INTO `72crm_admin_role_menu` VALUES (2556809, 198339, 153);
INSERT INTO `72crm_admin_role_menu` VALUES (2556810, 198339, 431);
INSERT INTO `72crm_admin_role_menu` VALUES (2556811, 198339, 432);
INSERT INTO `72crm_admin_role_menu` VALUES (2556812, 198339, 434);
INSERT INTO `72crm_admin_role_menu` VALUES (2556813, 198339, 435);
INSERT INTO `72crm_admin_role_menu` VALUES (2556814, 198339, 436);
INSERT INTO `72crm_admin_role_menu` VALUES (2556815, 198339, 437);
INSERT INTO `72crm_admin_role_menu` VALUES (2556816, 198339, 581);
INSERT INTO `72crm_admin_role_menu` VALUES (2556817, 198339, 614);
INSERT INTO `72crm_admin_role_menu` VALUES (2556818, 198339, 615);
INSERT INTO `72crm_admin_role_menu` VALUES (2556819, 198339, 438);
INSERT INTO `72crm_admin_role_menu` VALUES (2556820, 198339, 439);
INSERT INTO `72crm_admin_role_menu` VALUES (2556821, 198339, 440);
INSERT INTO `72crm_admin_role_menu` VALUES (2556822, 198339, 442);
INSERT INTO `72crm_admin_role_menu` VALUES (2556823, 198339, 444);
INSERT INTO `72crm_admin_role_menu` VALUES (2556824, 198339, 445);
INSERT INTO `72crm_admin_role_menu` VALUES (2556825, 198339, 446);
INSERT INTO `72crm_admin_role_menu` VALUES (2556826, 198339, 582);
INSERT INTO `72crm_admin_role_menu` VALUES (2556827, 198339, 583);
INSERT INTO `72crm_admin_role_menu` VALUES (2556828, 198339, 447);
INSERT INTO `72crm_admin_role_menu` VALUES (2556829, 198339, 448);
INSERT INTO `72crm_admin_role_menu` VALUES (2556830, 198339, 449);
INSERT INTO `72crm_admin_role_menu` VALUES (2556831, 198339, 451);
INSERT INTO `72crm_admin_role_menu` VALUES (2556832, 198339, 452);
INSERT INTO `72crm_admin_role_menu` VALUES (2556833, 198339, 453);
INSERT INTO `72crm_admin_role_menu` VALUES (2556834, 198339, 454);
INSERT INTO `72crm_admin_role_menu` VALUES (2556835, 198339, 584);
INSERT INTO `72crm_admin_role_menu` VALUES (2556836, 198339, 585);
INSERT INTO `72crm_admin_role_menu` VALUES (2556837, 198339, 455);
INSERT INTO `72crm_admin_role_menu` VALUES (2556838, 198339, 456);
INSERT INTO `72crm_admin_role_menu` VALUES (2556839, 198339, 457);
INSERT INTO `72crm_admin_role_menu` VALUES (2556840, 198339, 459);
INSERT INTO `72crm_admin_role_menu` VALUES (2556841, 198339, 461);
INSERT INTO `72crm_admin_role_menu` VALUES (2556842, 198339, 462);
INSERT INTO `72crm_admin_role_menu` VALUES (2556843, 198339, 586);
INSERT INTO `72crm_admin_role_menu` VALUES (2556844, 198339, 587);
INSERT INTO `72crm_admin_role_menu` VALUES (2556845, 198339, 463);
INSERT INTO `72crm_admin_role_menu` VALUES (2556846, 198339, 464);
INSERT INTO `72crm_admin_role_menu` VALUES (2556847, 198339, 465);
INSERT INTO `72crm_admin_role_menu` VALUES (2556848, 198339, 467);
INSERT INTO `72crm_admin_role_menu` VALUES (2556849, 198339, 468);
INSERT INTO `72crm_admin_role_menu` VALUES (2556850, 198339, 469);
INSERT INTO `72crm_admin_role_menu` VALUES (2556851, 198339, 470);
INSERT INTO `72crm_admin_role_menu` VALUES (2556852, 198339, 588);
INSERT INTO `72crm_admin_role_menu` VALUES (2556853, 198339, 589);
INSERT INTO `72crm_admin_role_menu` VALUES (2556854, 198339, 471);
INSERT INTO `72crm_admin_role_menu` VALUES (2556855, 198339, 472);
INSERT INTO `72crm_admin_role_menu` VALUES (2556856, 198339, 473);
INSERT INTO `72crm_admin_role_menu` VALUES (2556857, 198339, 475);
INSERT INTO `72crm_admin_role_menu` VALUES (2556858, 198339, 476);
INSERT INTO `72crm_admin_role_menu` VALUES (2556859, 198339, 477);
INSERT INTO `72crm_admin_role_menu` VALUES (2556860, 198339, 478);
INSERT INTO `72crm_admin_role_menu` VALUES (2556861, 198339, 479);
INSERT INTO `72crm_admin_role_menu` VALUES (2556862, 198339, 480);
INSERT INTO `72crm_admin_role_menu` VALUES (2556863, 198339, 481);
INSERT INTO `72crm_admin_role_menu` VALUES (2556864, 198339, 590);
INSERT INTO `72crm_admin_role_menu` VALUES (2556865, 198339, 591);
INSERT INTO `72crm_admin_role_menu` VALUES (2556866, 198339, 616);
INSERT INTO `72crm_admin_role_menu` VALUES (2556867, 198339, 484);
INSERT INTO `72crm_admin_role_menu` VALUES (2556868, 198339, 485);
INSERT INTO `72crm_admin_role_menu` VALUES (2556869, 198339, 486);
INSERT INTO `72crm_admin_role_menu` VALUES (2556870, 198339, 488);
INSERT INTO `72crm_admin_role_menu` VALUES (2556871, 198339, 489);
INSERT INTO `72crm_admin_role_menu` VALUES (2556872, 198339, 490);
INSERT INTO `72crm_admin_role_menu` VALUES (2556873, 198339, 602);
INSERT INTO `72crm_admin_role_menu` VALUES (2556874, 198339, 491);
INSERT INTO `72crm_admin_role_menu` VALUES (2556875, 198339, 492);
INSERT INTO `72crm_admin_role_menu` VALUES (2556876, 198339, 493);
INSERT INTO `72crm_admin_role_menu` VALUES (2556877, 198339, 494);
INSERT INTO `72crm_admin_role_menu` VALUES (2556878, 198339, 495);
INSERT INTO `72crm_admin_role_menu` VALUES (2556879, 198339, 497);
INSERT INTO `72crm_admin_role_menu` VALUES (2556880, 198339, 610);
INSERT INTO `72crm_admin_role_menu` VALUES (2556881, 198339, 611);
INSERT INTO `72crm_admin_role_menu` VALUES (2556882, 198339, 498);
INSERT INTO `72crm_admin_role_menu` VALUES (2556883, 198339, 499);
INSERT INTO `72crm_admin_role_menu` VALUES (2556884, 198339, 502);
INSERT INTO `72crm_admin_role_menu` VALUES (2556885, 198339, 503);
INSERT INTO `72crm_admin_role_menu` VALUES (2556886, 198339, 504);
INSERT INTO `72crm_admin_role_menu` VALUES (2556887, 198339, 505);
INSERT INTO `72crm_admin_role_menu` VALUES (2556888, 198339, 612);
INSERT INTO `72crm_admin_role_menu` VALUES (2556889, 198339, 613);
INSERT INTO `72crm_admin_role_menu` VALUES (2556890, 198339, 506);
INSERT INTO `72crm_admin_role_menu` VALUES (2556891, 198339, 507);
INSERT INTO `72crm_admin_role_menu` VALUES (2556892, 198339, 508);
INSERT INTO `72crm_admin_role_menu` VALUES (2556893, 198339, 510);
INSERT INTO `72crm_admin_role_menu` VALUES (2556894, 198339, 512);
INSERT INTO `72crm_admin_role_menu` VALUES (2556895, 198339, 513);
INSERT INTO `72crm_admin_role_menu` VALUES (2556896, 198339, 596);
INSERT INTO `72crm_admin_role_menu` VALUES (2556897, 198339, 514);
INSERT INTO `72crm_admin_role_menu` VALUES (2556898, 198339, 515);
INSERT INTO `72crm_admin_role_menu` VALUES (2556899, 198339, 516);
INSERT INTO `72crm_admin_role_menu` VALUES (2556900, 198339, 517);
INSERT INTO `72crm_admin_role_menu` VALUES (2556901, 198339, 518);
INSERT INTO `72crm_admin_role_menu` VALUES (2556902, 198339, 519);
INSERT INTO `72crm_admin_role_menu` VALUES (2556903, 198339, 520);
INSERT INTO `72crm_admin_role_menu` VALUES (2556904, 198339, 521);
INSERT INTO `72crm_admin_role_menu` VALUES (2556905, 198339, 522);
INSERT INTO `72crm_admin_role_menu` VALUES (2556906, 198339, 523);
INSERT INTO `72crm_admin_role_menu` VALUES (2556907, 198339, 524);
INSERT INTO `72crm_admin_role_menu` VALUES (2556908, 198339, 525);
INSERT INTO `72crm_admin_role_menu` VALUES (2556909, 198339, 526);
INSERT INTO `72crm_admin_role_menu` VALUES (2556910, 198339, 527);
INSERT INTO `72crm_admin_role_menu` VALUES (2556911, 198339, 528);
INSERT INTO `72crm_admin_role_menu` VALUES (2556912, 198339, 529);
INSERT INTO `72crm_admin_role_menu` VALUES (2556913, 198339, 530);
INSERT INTO `72crm_admin_role_menu` VALUES (2556914, 198339, 531);
INSERT INTO `72crm_admin_role_menu` VALUES (2556915, 198339, 532);
INSERT INTO `72crm_admin_role_menu` VALUES (2556916, 198339, 533);
INSERT INTO `72crm_admin_role_menu` VALUES (2556917, 198339, 534);
INSERT INTO `72crm_admin_role_menu` VALUES (2556918, 198339, 535);
INSERT INTO `72crm_admin_role_menu` VALUES (2556919, 198339, 536);
INSERT INTO `72crm_admin_role_menu` VALUES (2556920, 198339, 537);
INSERT INTO `72crm_admin_role_menu` VALUES (2556921, 198339, 538);
INSERT INTO `72crm_admin_role_menu` VALUES (2556922, 198339, 539);
INSERT INTO `72crm_admin_role_menu` VALUES (2556923, 198339, 540);
INSERT INTO `72crm_admin_role_menu` VALUES (2556924, 198339, 543);
INSERT INTO `72crm_admin_role_menu` VALUES (2556925, 198339, 544);
INSERT INTO `72crm_admin_role_menu` VALUES (2556926, 198339, 545);
INSERT INTO `72crm_admin_role_menu` VALUES (2556927, 198339, 546);
INSERT INTO `72crm_admin_role_menu` VALUES (2556928, 198339, 547);
INSERT INTO `72crm_admin_role_menu` VALUES (2556929, 198339, 548);
INSERT INTO `72crm_admin_role_menu` VALUES (2556930, 198339, 549);
INSERT INTO `72crm_admin_role_menu` VALUES (2556931, 198339, 550);
INSERT INTO `72crm_admin_role_menu` VALUES (2556932, 198339, 562);
INSERT INTO `72crm_admin_role_menu` VALUES (2556933, 198339, 552);
INSERT INTO `72crm_admin_role_menu` VALUES (2556934, 198339, 553);
INSERT INTO `72crm_admin_role_menu` VALUES (2556935, 198339, 554);
INSERT INTO `72crm_admin_role_menu` VALUES (2556936, 198339, 555);
INSERT INTO `72crm_admin_role_menu` VALUES (2556937, 198339, 556);
INSERT INTO `72crm_admin_role_menu` VALUES (2556938, 198339, 557);
INSERT INTO `72crm_admin_role_menu` VALUES (2556939, 198339, 558);
INSERT INTO `72crm_admin_role_menu` VALUES (2556940, 198339, 559);
INSERT INTO `72crm_admin_role_menu` VALUES (2556941, 198339, 560);
INSERT INTO `72crm_admin_role_menu` VALUES (2556942, 198339, 561);
INSERT INTO `72crm_admin_role_menu` VALUES (2556943, 198339, 563);
INSERT INTO `72crm_admin_role_menu` VALUES (2556944, 198339, 564);
INSERT INTO `72crm_admin_role_menu` VALUES (2556945, 198339, 565);
INSERT INTO `72crm_admin_role_menu` VALUES (2556946, 198339, 566);
INSERT INTO `72crm_admin_role_menu` VALUES (2556947, 198339, 567);
INSERT INTO `72crm_admin_role_menu` VALUES (2556948, 198339, 568);
INSERT INTO `72crm_admin_role_menu` VALUES (2556949, 198339, 569);
INSERT INTO `72crm_admin_role_menu` VALUES (2556950, 198339, 570);
INSERT INTO `72crm_admin_role_menu` VALUES (2556951, 198339, 571);
INSERT INTO `72crm_admin_role_menu` VALUES (2556952, 198339, 572);
INSERT INTO `72crm_admin_role_menu` VALUES (2556953, 198339, 573);
INSERT INTO `72crm_admin_role_menu` VALUES (2556954, 198339, 617);
INSERT INTO `72crm_admin_role_menu` VALUES (2556955, 198339, 577);
INSERT INTO `72crm_admin_role_menu` VALUES (2556956, 198339, 578);
INSERT INTO `72crm_admin_role_menu` VALUES (2556957, 198339, 579);
INSERT INTO `72crm_admin_role_menu` VALUES (2556958, 198339, 603);
INSERT INTO `72crm_admin_role_menu` VALUES (2556959, 198339, 604);
INSERT INTO `72crm_admin_role_menu` VALUES (2556960, 198339, 605);
INSERT INTO `72crm_admin_role_menu` VALUES (2556961, 198339, 607);
INSERT INTO `72crm_admin_role_menu` VALUES (2556962, 198339, 608);
INSERT INTO `72crm_admin_role_menu` VALUES (2556963, 198339, 609);
INSERT INTO `72crm_admin_role_menu` VALUES (2556964, 198339, 618);
INSERT INTO `72crm_admin_role_menu` VALUES (2556965, 198339, 619);
INSERT INTO `72crm_admin_role_menu` VALUES (2556966, 198339, 620);
INSERT INTO `72crm_admin_role_menu` VALUES (2556967, 198339, 621);
INSERT INTO `72crm_admin_role_menu` VALUES (2556968, 198339, 622);
INSERT INTO `72crm_admin_role_menu` VALUES (2556969, 198339, 623);
INSERT INTO `72crm_admin_role_menu` VALUES (2556970, 198339, 624);
INSERT INTO `72crm_admin_role_menu` VALUES (2556971, 198339, 625);
INSERT INTO `72crm_admin_role_menu` VALUES (2556972, 198339, 626);
INSERT INTO `72crm_admin_role_menu` VALUES (2556973, 198339, 627);
INSERT INTO `72crm_admin_role_menu` VALUES (2556974, 198339, 628);
INSERT INTO `72crm_admin_role_menu` VALUES (2556975, 198339, 629);
INSERT INTO `72crm_admin_role_menu` VALUES (2556976, 198339, 630);
INSERT INTO `72crm_admin_role_menu` VALUES (2556977, 198339, 631);
INSERT INTO `72crm_admin_role_menu` VALUES (2556978, 198339, 632);
INSERT INTO `72crm_admin_role_menu` VALUES (2556979, 198339, 633);
INSERT INTO `72crm_admin_role_menu` VALUES (2556980, 198339, 634);
INSERT INTO `72crm_admin_role_menu` VALUES (2556981, 198331, 1);
INSERT INTO `72crm_admin_role_menu` VALUES (2556982, 198331, 9);
INSERT INTO `72crm_admin_role_menu` VALUES (2556983, 198331, 17);
INSERT INTO `72crm_admin_role_menu` VALUES (2556984, 198331, 18);
INSERT INTO `72crm_admin_role_menu` VALUES (2556985, 198331, 19);
INSERT INTO `72crm_admin_role_menu` VALUES (2556986, 198331, 20);
INSERT INTO `72crm_admin_role_menu` VALUES (2556987, 198331, 21);
INSERT INTO `72crm_admin_role_menu` VALUES (2556988, 198331, 22);
INSERT INTO `72crm_admin_role_menu` VALUES (2556989, 198331, 23);
INSERT INTO `72crm_admin_role_menu` VALUES (2556990, 198331, 24);
INSERT INTO `72crm_admin_role_menu` VALUES (2556991, 198331, 25);
INSERT INTO `72crm_admin_role_menu` VALUES (2556992, 198331, 10);
INSERT INTO `72crm_admin_role_menu` VALUES (2556993, 198331, 26);
INSERT INTO `72crm_admin_role_menu` VALUES (2556994, 198331, 27);
INSERT INTO `72crm_admin_role_menu` VALUES (2556995, 198331, 28);
INSERT INTO `72crm_admin_role_menu` VALUES (2556996, 198331, 29);
INSERT INTO `72crm_admin_role_menu` VALUES (2556997, 198331, 30);
INSERT INTO `72crm_admin_role_menu` VALUES (2556998, 198331, 31);
INSERT INTO `72crm_admin_role_menu` VALUES (2556999, 198331, 32);
INSERT INTO `72crm_admin_role_menu` VALUES (2557000, 198331, 33);
INSERT INTO `72crm_admin_role_menu` VALUES (2557001, 198331, 34);
INSERT INTO `72crm_admin_role_menu` VALUES (2557002, 198331, 35);
INSERT INTO `72crm_admin_role_menu` VALUES (2557003, 198331, 36);
INSERT INTO `72crm_admin_role_menu` VALUES (2557004, 198331, 191);
INSERT INTO `72crm_admin_role_menu` VALUES (2557005, 198331, 218);
INSERT INTO `72crm_admin_role_menu` VALUES (2557006, 198331, 11);
INSERT INTO `72crm_admin_role_menu` VALUES (2557007, 198331, 40);
INSERT INTO `72crm_admin_role_menu` VALUES (2557008, 198331, 41);
INSERT INTO `72crm_admin_role_menu` VALUES (2557009, 198331, 42);
INSERT INTO `72crm_admin_role_menu` VALUES (2557010, 198331, 43);
INSERT INTO `72crm_admin_role_menu` VALUES (2557011, 198331, 44);
INSERT INTO `72crm_admin_role_menu` VALUES (2557012, 198331, 45);
INSERT INTO `72crm_admin_role_menu` VALUES (2557013, 198331, 107);
INSERT INTO `72crm_admin_role_menu` VALUES (2557014, 198331, 108);
INSERT INTO `72crm_admin_role_menu` VALUES (2557015, 198331, 12);
INSERT INTO `72crm_admin_role_menu` VALUES (2557016, 198331, 46);
INSERT INTO `72crm_admin_role_menu` VALUES (2557017, 198331, 47);
INSERT INTO `72crm_admin_role_menu` VALUES (2557018, 198331, 48);
INSERT INTO `72crm_admin_role_menu` VALUES (2557019, 198331, 49);
INSERT INTO `72crm_admin_role_menu` VALUES (2557020, 198331, 50);
INSERT INTO `72crm_admin_role_menu` VALUES (2557021, 198331, 51);
INSERT INTO `72crm_admin_role_menu` VALUES (2557022, 198331, 52);
INSERT INTO `72crm_admin_role_menu` VALUES (2557023, 198331, 209);
INSERT INTO `72crm_admin_role_menu` VALUES (2557024, 198331, 13);
INSERT INTO `72crm_admin_role_menu` VALUES (2557025, 198331, 53);
INSERT INTO `72crm_admin_role_menu` VALUES (2557026, 198331, 54);
INSERT INTO `72crm_admin_role_menu` VALUES (2557027, 198331, 55);
INSERT INTO `72crm_admin_role_menu` VALUES (2557028, 198331, 56);
INSERT INTO `72crm_admin_role_menu` VALUES (2557029, 198331, 57);
INSERT INTO `72crm_admin_role_menu` VALUES (2557030, 198331, 58);
INSERT INTO `72crm_admin_role_menu` VALUES (2557031, 198331, 59);
INSERT INTO `72crm_admin_role_menu` VALUES (2557032, 198331, 192);
INSERT INTO `72crm_admin_role_menu` VALUES (2557033, 198331, 208);
INSERT INTO `72crm_admin_role_menu` VALUES (2557034, 198331, 14);
INSERT INTO `72crm_admin_role_menu` VALUES (2557035, 198331, 60);
INSERT INTO `72crm_admin_role_menu` VALUES (2557036, 198331, 61);
INSERT INTO `72crm_admin_role_menu` VALUES (2557037, 198331, 62);
INSERT INTO `72crm_admin_role_menu` VALUES (2557038, 198331, 63);
INSERT INTO `72crm_admin_role_menu` VALUES (2557039, 198331, 64);
INSERT INTO `72crm_admin_role_menu` VALUES (2557040, 198331, 71);
INSERT INTO `72crm_admin_role_menu` VALUES (2557041, 198331, 212);
INSERT INTO `72crm_admin_role_menu` VALUES (2557042, 198331, 15);
INSERT INTO `72crm_admin_role_menu` VALUES (2557043, 198331, 65);
INSERT INTO `72crm_admin_role_menu` VALUES (2557044, 198331, 66);
INSERT INTO `72crm_admin_role_menu` VALUES (2557045, 198331, 67);
INSERT INTO `72crm_admin_role_menu` VALUES (2557046, 198331, 68);
INSERT INTO `72crm_admin_role_menu` VALUES (2557047, 198331, 69);
INSERT INTO `72crm_admin_role_menu` VALUES (2557048, 198331, 70);
INSERT INTO `72crm_admin_role_menu` VALUES (2557049, 198331, 109);
INSERT INTO `72crm_admin_role_menu` VALUES (2557050, 198331, 110);
INSERT INTO `72crm_admin_role_menu` VALUES (2557051, 198331, 211);
INSERT INTO `72crm_admin_role_menu` VALUES (2557052, 198331, 196);
INSERT INTO `72crm_admin_role_menu` VALUES (2557053, 198331, 197);
INSERT INTO `72crm_admin_role_menu` VALUES (2557054, 198331, 198);
INSERT INTO `72crm_admin_role_menu` VALUES (2557055, 198331, 199);
INSERT INTO `72crm_admin_role_menu` VALUES (2557056, 198331, 200);
INSERT INTO `72crm_admin_role_menu` VALUES (2557057, 198331, 201);
INSERT INTO `72crm_admin_role_menu` VALUES (2557058, 198331, 202);
INSERT INTO `72crm_admin_role_menu` VALUES (2557059, 198331, 204);
INSERT INTO `72crm_admin_role_menu` VALUES (2557060, 198331, 205);
INSERT INTO `72crm_admin_role_menu` VALUES (2557061, 198331, 206);
INSERT INTO `72crm_admin_role_menu` VALUES (2557062, 198331, 207);
INSERT INTO `72crm_admin_role_menu` VALUES (2557063, 198331, 213);
INSERT INTO `72crm_admin_role_menu` VALUES (2557064, 198331, 214);
INSERT INTO `72crm_admin_role_menu` VALUES (2557065, 198331, 215);
INSERT INTO `72crm_admin_role_menu` VALUES (2557066, 198331, 216);
INSERT INTO `72crm_admin_role_menu` VALUES (2557067, 198331, 217);
INSERT INTO `72crm_admin_role_menu` VALUES (2557068, 198331, 400);
INSERT INTO `72crm_admin_role_menu` VALUES (2557069, 198331, 401);
INSERT INTO `72crm_admin_role_menu` VALUES (2557070, 198331, 402);
INSERT INTO `72crm_admin_role_menu` VALUES (2557071, 198331, 403);
INSERT INTO `72crm_admin_role_menu` VALUES (2557072, 198331, 404);
INSERT INTO `72crm_admin_role_menu` VALUES (2557073, 198331, 405);
INSERT INTO `72crm_admin_role_menu` VALUES (2557074, 198331, 420);
INSERT INTO `72crm_admin_role_menu` VALUES (2557075, 198331, 421);
INSERT INTO `72crm_admin_role_menu` VALUES (2557076, 198331, 422);
INSERT INTO `72crm_admin_role_menu` VALUES (2557077, 198331, 423);
INSERT INTO `72crm_admin_role_menu` VALUES (2557078, 198331, 424);
INSERT INTO `72crm_admin_role_menu` VALUES (2557079, 198331, 425);
INSERT INTO `72crm_admin_role_menu` VALUES (2557080, 198331, 426);
INSERT INTO `72crm_admin_role_menu` VALUES (2557081, 198331, 427);
INSERT INTO `72crm_admin_role_menu` VALUES (2557082, 198331, 428);
INSERT INTO `72crm_admin_role_menu` VALUES (2557083, 198331, 97);
INSERT INTO `72crm_admin_role_menu` VALUES (2557084, 198331, 102);
INSERT INTO `72crm_admin_role_menu` VALUES (2557085, 198331, 98);
INSERT INTO `72crm_admin_role_menu` VALUES (2557086, 198331, 103);
INSERT INTO `72crm_admin_role_menu` VALUES (2557087, 198331, 99);
INSERT INTO `72crm_admin_role_menu` VALUES (2557088, 198331, 104);
INSERT INTO `72crm_admin_role_menu` VALUES (2557089, 198331, 101);
INSERT INTO `72crm_admin_role_menu` VALUES (2557090, 198331, 106);
INSERT INTO `72crm_admin_role_menu` VALUES (2557091, 198331, 117);
INSERT INTO `72crm_admin_role_menu` VALUES (2557092, 198331, 118);
INSERT INTO `72crm_admin_role_menu` VALUES (2557093, 198331, 123);
INSERT INTO `72crm_admin_role_menu` VALUES (2557094, 198331, 124);
INSERT INTO `72crm_admin_role_menu` VALUES (2557095, 198331, 125);
INSERT INTO `72crm_admin_role_menu` VALUES (2557096, 198331, 126);

-- ----------------------------
-- Table structure for 72crm_admin_scene
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_scene`;
CREATE TABLE `72crm_admin_scene`  (
  `scene_id` int(10) NOT NULL AUTO_INCREMENT,
  `type` int(5) NOT NULL COMMENT '分类',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '场景名称',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `sort` int(5) NOT NULL COMMENT '排序ID',
  `data` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属性值',
  `is_hide` int(1) NOT NULL COMMENT '1隐藏',
  `is_system` int(1) NOT NULL COMMENT '1系统0自定义',
  `bydata` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统参数',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`scene_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 124590 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '场景' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_scene
-- ----------------------------
INSERT INTO `72crm_admin_scene` VALUES (123700, 19, '全部项目', 16362, 0, '', 0, 1, NULL, '2020-11-26 17:43:00', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123701, 19, '我负责的项目', 16362, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16362}}', 0, 1, NULL, '2020-11-26 17:43:00', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123702, 24, '全部需求', 16362, 0, '', 0, 1, NULL, '2020-11-26 17:43:05', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123703, 24, '已对标需求', 16362, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"1\"}}', 0, 1, NULL, '2020-11-26 17:43:05', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123704, 24, '未对标需求', 16362, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"0\"}}', 0, 1, NULL, '2020-11-26 17:43:05', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123705, 19, '全部项目', 16371, 0, '', 0, 1, NULL, '2020-11-26 17:43:52', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123706, 19, '我负责的项目', 16371, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16371}}', 0, 1, NULL, '2020-11-26 17:43:52', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123707, 21, '全部任务', 16371, 0, '', 0, 1, NULL, '2020-11-26 17:43:56', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123708, 21, '下属负责的任务', 16371, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"16372,0\"}}', 0, 1, NULL, '2020-11-26 17:43:56', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123709, 21, '我负责的任务', 16371, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16371}}', 0, 1, NULL, '2020-11-26 17:43:56', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123710, 21, '我参与的任务', 16371, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16371,\"}}', 0, 1, NULL, '2020-11-26 17:43:56', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123711, 29, '全部任务', 16371, 0, '', 0, 1, NULL, '2020-11-26 17:49:14', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123712, 29, '下属负责的任务', 16371, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"16372,0\"}}', 0, 1, NULL, '2020-11-26 17:49:14', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123713, 29, '我负责的任务', 16371, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16371}}', 0, 1, NULL, '2020-11-26 17:49:14', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123714, 29, '我参与的任务', 16371, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16371,\"}}', 0, 1, NULL, '2020-11-26 17:49:14', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123715, 23, '全部产品', 16371, 0, '', 0, 1, NULL, '2020-11-26 17:58:32', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123716, 23, '发版的产品', 16371, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"发版\"}}', 0, 1, NULL, '2020-11-26 17:58:32', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123717, 23, '规划的产品', 16371, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"规划\"}}', 0, 1, NULL, '2020-11-26 17:58:32', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123718, 21, '全部任务', 16362, 0, '', 0, 1, NULL, '2020-11-26 18:05:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123719, 21, '下属负责的任务', 16362, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"16378,0,16380,0\"}}', 0, 1, NULL, '2020-11-26 18:05:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123720, 21, '我负责的任务', 16362, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16362}}', 0, 1, NULL, '2020-11-26 18:05:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123721, 21, '我参与的任务', 16362, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16362,\"}}', 0, 1, NULL, '2020-11-26 18:05:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123722, 29, '全部任务', 16362, 0, '', 0, 1, NULL, '2020-11-26 18:05:17', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123723, 29, '下属负责的任务', 16362, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"16378,0,16380,0\"}}', 0, 1, NULL, '2020-11-26 18:05:17', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123724, 29, '我负责的任务', 16362, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16362}}', 0, 1, NULL, '2020-11-26 18:05:17', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123725, 29, '我参与的任务', 16362, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16362,\"}}', 0, 1, NULL, '2020-11-26 18:05:17', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123726, 19, '全部项目', 16372, 0, '', 0, 1, NULL, '2020-11-26 18:09:53', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123727, 19, '我负责的项目', 16372, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16372}}', 0, 1, NULL, '2020-11-26 18:09:53', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123728, 21, '全部任务', 16372, 0, '', 0, 1, NULL, '2020-11-26 18:09:57', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123729, 21, '下属负责的任务', 16372, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-11-26 18:09:57', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123730, 21, '我负责的任务', 16372, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16372}}', 0, 1, NULL, '2020-11-26 18:09:57', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123731, 21, '我参与的任务', 16372, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16372,\"}}', 0, 1, NULL, '2020-11-26 18:09:57', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123732, 29, '全部任务', 16372, 0, '', 0, 1, NULL, '2020-11-26 18:13:48', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123733, 29, '下属负责的任务', 16372, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-11-26 18:13:48', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123734, 29, '我负责的任务', 16372, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16372}}', 0, 1, NULL, '2020-11-26 18:13:48', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123735, 29, '我参与的任务', 16372, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16372,\"}}', 0, 1, NULL, '2020-11-26 18:13:48', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123736, 24, '全部需求', 16371, 0, '', 0, 1, NULL, '2020-11-26 18:14:55', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123737, 24, '已对标需求', 16371, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"1\"}}', 0, 1, NULL, '2020-11-26 18:14:55', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123738, 24, '未对标需求', 16371, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"0\"}}', 0, 1, NULL, '2020-11-26 18:14:55', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123752, 19, '全部项目', 16378, 0, '', 0, 1, NULL, '2020-11-26 18:56:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123753, 19, '我负责的项目', 16378, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16378}}', 0, 1, NULL, '2020-11-26 18:56:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123754, 6, '全部合同', 16362, 0, '', 0, 1, NULL, '2020-11-26 19:10:11', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123755, 6, '我负责的合同', 16362, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16362}}', 0, 1, NULL, '2020-11-26 19:10:11', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123756, 6, '下属负责的合同', 16362, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"16378,0,16380,0\"}}', 0, 1, NULL, '2020-11-26 19:10:11', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123757, 6, '我参与的合同', 16362, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16362}}', 0, 1, NULL, '2020-11-26 19:10:11', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123758, 20, '全部里程碑', 16371, 0, '', 0, 1, NULL, '2020-11-26 19:29:53', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123763, 5, '全部商机', 16374, 0, '', 0, 1, NULL, '2020-11-27 09:48:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123764, 5, '我负责的商机', 16374, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16374}}', 0, 1, NULL, '2020-11-27 09:48:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123765, 5, '下属负责的商机', 16374, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-11-27 09:48:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123766, 5, '我参与的商机', 16374, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16374}}', 0, 1, NULL, '2020-11-27 09:48:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123767, 5, '我关注的商机', 16374, 0, '{\"business_id\":{\"condition\":\"in\",\"name\":\"business_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-11-27 09:48:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123768, 5, '全部商机', 16362, 0, '', 0, 1, NULL, '2020-11-27 11:01:27', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123769, 5, '我负责的商机', 16362, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16362}}', 0, 1, NULL, '2020-11-27 11:01:27', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123770, 5, '下属负责的商机', 16362, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"16378,0,16380,0\"}}', 0, 1, NULL, '2020-11-27 11:01:27', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123771, 5, '我参与的商机', 16362, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16362}}', 0, 1, NULL, '2020-11-27 11:01:27', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123772, 5, '我关注的商机', 16362, 0, '{\"business_id\":{\"condition\":\"in\",\"name\":\"business_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-11-27 11:01:27', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123773, 7, '全部回款', 16362, 0, '', 0, 1, NULL, '2020-11-27 11:01:34', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123774, 7, '我负责的回款', 16362, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16362}}', 0, 1, NULL, '2020-11-27 11:01:34', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123775, 7, '下属负责的回款', 16362, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"16378,0,16380,0\"}}', 0, 1, NULL, '2020-11-27 11:01:34', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123776, 3, '全部联系人', 16362, 0, '', 0, 1, NULL, '2020-11-27 11:01:36', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123777, 3, '我负责的联系人', 16362, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16362}}', 0, 1, NULL, '2020-11-27 11:01:36', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123778, 3, '下属负责的联系人', 16362, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"16378,0,16380,0\"}}', 0, 1, NULL, '2020-11-27 11:01:36', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123779, 3, '我关注的联系人', 16362, 0, '{\"contacts_id\":{\"condition\":\"in\",\"name\":\"contacts_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-11-27 11:01:36', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123780, 2, '全部客户', 16371, 0, '', 0, 1, NULL, '2020-11-27 11:25:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123781, 2, '我负责的客户', 16371, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16371}}', 0, 1, NULL, '2020-11-27 11:25:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123782, 2, '下属负责的客户', 16371, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"16372,0\"}}', 0, 1, NULL, '2020-11-27 11:25:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123783, 2, '我参与的客户', 16371, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16371}}', 0, 1, NULL, '2020-11-27 11:25:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123784, 2, '我关注的客户', 16371, 0, '{\"customer_id\":{\"condition\":\"in\",\"name\":\"customer_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-11-27 11:25:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123785, 1, '全部线索', 16371, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-11-27 11:27:45', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123786, 1, '我负责的线索', 16371, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16371},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-11-27 11:27:45', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123787, 1, '下属负责的线索', 16371, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"16372,0\"},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-11-27 11:27:45', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123788, 1, '已转化的线索', 16371, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":\"1\"}}', 0, 1, 'transform', '2020-11-27 11:27:45', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123789, 1, '我关注的线索', 16371, 0, '{\"leads_id\":{\"condition\":\"in\",\"name\":\"leads_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-11-27 11:27:45', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123790, 2, '全部客户', 16362, 0, '', 0, 1, NULL, '2020-11-27 11:34:24', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123791, 2, '我负责的客户', 16362, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16362}}', 0, 1, NULL, '2020-11-27 11:34:24', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123792, 2, '下属负责的客户', 16362, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"16378,0,16380,0\"}}', 0, 1, NULL, '2020-11-27 11:34:24', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123793, 2, '我参与的客户', 16362, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16362}}', 0, 1, NULL, '2020-11-27 11:34:24', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123794, 2, '我关注的客户', 16362, 0, '{\"customer_id\":{\"condition\":\"in\",\"name\":\"customer_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-11-27 11:34:24', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123804, 20, '全部里程碑', 16372, 0, '', 0, 1, NULL, '2020-11-27 13:22:11', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123805, 24, '全部需求', 16372, 0, '', 0, 1, NULL, '2020-11-27 13:22:12', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123806, 24, '已对标需求', 16372, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"1\"}}', 0, 1, NULL, '2020-11-27 13:22:12', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123807, 24, '未对标需求', 16372, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"0\"}}', 0, 1, NULL, '2020-11-27 13:22:12', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123811, 20, '全部里程碑', 16362, 0, '', 0, 1, NULL, '2020-11-27 14:43:37', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123817, 23, '全部产品', 16362, 0, '', 0, 1, NULL, '2020-11-27 18:00:39', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123818, 23, '发版的产品', 16362, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"发版\"}}', 0, 1, NULL, '2020-11-27 18:00:39', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123819, 23, '规划的产品', 16362, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"规划\"}}', 0, 1, NULL, '2020-11-27 18:00:39', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123820, 19, '全部项目', 16383, 0, '', 0, 1, NULL, '2020-11-30 09:59:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123821, 19, '我负责的项目', 16383, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16383}}', 0, 1, NULL, '2020-11-30 09:59:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123822, 19, '全部项目', 16379, 0, '', 0, 1, NULL, '2020-11-30 10:07:37', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123823, 19, '我负责的项目', 16379, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16379}}', 0, 1, NULL, '2020-11-30 10:07:37', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123832, 23, '全部产品', 16383, 0, '', 0, 1, NULL, '2020-11-30 10:26:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123833, 23, '发版的产品', 16383, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"发版\"}}', 0, 1, NULL, '2020-11-30 10:26:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123834, 23, '规划的产品', 16383, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"规划\"}}', 0, 1, NULL, '2020-11-30 10:26:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123838, 20, '全部里程碑', 16383, 0, '', 0, 1, NULL, '2020-11-30 11:13:14', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123846, 23, '全部产品', 16372, 0, '', 0, 1, NULL, '2020-11-30 16:57:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123847, 23, '发版的产品', 16372, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"发版\"}}', 0, 1, NULL, '2020-11-30 16:57:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123848, 23, '规划的产品', 16372, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"规划\"}}', 0, 1, NULL, '2020-11-30 16:57:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123867, 19, '全部项目', 16374, 0, '', 0, 1, NULL, '2020-12-01 15:05:33', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123868, 19, '我负责的项目', 16374, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16374}}', 0, 1, NULL, '2020-12-01 15:05:33', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123869, 1, '全部线索', 16374, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-01 15:31:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123870, 1, '我负责的线索', 16374, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16374},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-01 15:31:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123871, 1, '下属负责的线索', 16374, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-01 15:31:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123872, 1, '已转化的线索', 16374, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":\"1\"}}', 0, 1, 'transform', '2020-12-01 15:31:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123873, 1, '我关注的线索', 16374, 0, '{\"leads_id\":{\"condition\":\"in\",\"name\":\"leads_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-01 15:31:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123874, 2, '全部客户', 16374, 0, '', 0, 1, NULL, '2020-12-01 15:31:14', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123875, 2, '我负责的客户', 16374, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16374}}', 0, 1, NULL, '2020-12-01 15:31:14', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123876, 2, '下属负责的客户', 16374, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 15:31:14', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123877, 2, '我参与的客户', 16374, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16374}}', 0, 1, NULL, '2020-12-01 15:31:14', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123878, 2, '我关注的客户', 16374, 0, '{\"customer_id\":{\"condition\":\"in\",\"name\":\"customer_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-01 15:31:14', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123879, 3, '全部联系人', 16374, 0, '', 0, 1, NULL, '2020-12-01 15:31:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123880, 3, '我负责的联系人', 16374, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16374}}', 0, 1, NULL, '2020-12-01 15:31:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123881, 3, '下属负责的联系人', 16374, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 15:31:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123882, 3, '我关注的联系人', 16374, 0, '{\"contacts_id\":{\"condition\":\"in\",\"name\":\"contacts_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-01 15:31:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123883, 6, '全部合同', 16374, 0, '', 0, 1, NULL, '2020-12-01 15:31:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123884, 6, '我负责的合同', 16374, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16374}}', 0, 1, NULL, '2020-12-01 15:31:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123885, 6, '下属负责的合同', 16374, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 15:31:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123886, 6, '我参与的合同', 16374, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16374}}', 0, 1, NULL, '2020-12-01 15:31:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123887, 19, '全部项目', 16384, 0, '', 0, 1, NULL, '2020-12-01 15:33:32', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123888, 19, '我负责的项目', 16384, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16384}}', 0, 1, NULL, '2020-12-01 15:33:32', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123889, 20, '全部里程碑', 16384, 0, '', 0, 1, NULL, '2020-12-01 15:34:16', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123890, 24, '全部需求', 16384, 0, '', 0, 1, NULL, '2020-12-01 15:34:18', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123891, 24, '已对标需求', 16384, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"1\"}}', 0, 1, NULL, '2020-12-01 15:34:18', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123892, 24, '未对标需求', 16384, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 15:34:18', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123893, 21, '全部任务', 16384, 0, '', 0, 1, NULL, '2020-12-01 15:34:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123894, 21, '下属负责的任务', 16384, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 15:34:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123895, 21, '我负责的任务', 16384, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16384}}', 0, 1, NULL, '2020-12-01 15:34:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123896, 21, '我参与的任务', 16384, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16384,\"}}', 0, 1, NULL, '2020-12-01 15:34:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123897, 20, '全部里程碑', 16374, 0, '', 0, 1, NULL, '2020-12-01 15:38:35', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123898, 21, '全部任务', 16374, 0, '', 0, 1, NULL, '2020-12-01 15:38:36', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123899, 21, '下属负责的任务', 16374, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 15:38:36', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123900, 21, '我负责的任务', 16374, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16374}}', 0, 1, NULL, '2020-12-01 15:38:36', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123901, 21, '我参与的任务', 16374, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16374,\"}}', 0, 1, NULL, '2020-12-01 15:38:36', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123902, 23, '全部产品', 16374, 0, '', 0, 1, NULL, '2020-12-01 15:40:42', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123903, 23, '发版的产品', 16374, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"发版\"}}', 0, 1, NULL, '2020-12-01 15:40:42', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123904, 23, '规划的产品', 16374, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"规划\"}}', 0, 1, NULL, '2020-12-01 15:40:42', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123905, 3, '全部联系人', 16387, 0, '', 0, 1, NULL, '2020-12-01 17:17:39', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123906, 3, '我负责的联系人', 16387, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16387}}', 0, 1, NULL, '2020-12-01 17:17:39', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123907, 3, '下属负责的联系人', 16387, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 17:17:39', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123908, 3, '我关注的联系人', 16387, 0, '{\"contacts_id\":{\"condition\":\"in\",\"name\":\"contacts_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-01 17:17:39', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123909, 19, '全部项目', 16387, 0, '', 0, 1, NULL, '2020-12-01 17:17:45', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123910, 19, '我负责的项目', 16387, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16387}}', 0, 1, NULL, '2020-12-01 17:17:45', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123911, 20, '全部里程碑', 16387, 0, '', 0, 1, NULL, '2020-12-01 17:25:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123912, 24, '全部需求', 16387, 0, '', 0, 1, NULL, '2020-12-01 17:27:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123913, 24, '已对标需求', 16387, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"1\"}}', 0, 1, NULL, '2020-12-01 17:27:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123914, 24, '未对标需求', 16387, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 17:27:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123915, 24, '全部需求', 16374, 0, '', 0, 1, NULL, '2020-12-01 17:30:09', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123916, 24, '已对标需求', 16374, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"1\"}}', 0, 1, NULL, '2020-12-01 17:30:09', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123917, 24, '未对标需求', 16374, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 17:30:09', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123918, 1, '全部线索', 16384, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-01 17:35:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123919, 1, '我负责的线索', 16384, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16384},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-01 17:35:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123920, 1, '下属负责的线索', 16384, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-01 17:35:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123921, 1, '已转化的线索', 16384, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":\"1\"}}', 0, 1, 'transform', '2020-12-01 17:35:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123922, 1, '我关注的线索', 16384, 0, '{\"leads_id\":{\"condition\":\"in\",\"name\":\"leads_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-01 17:35:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123932, 2, '全部客户', 16384, 0, '', 0, 1, NULL, '2020-12-01 17:36:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123933, 2, '我负责的客户', 16384, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16384}}', 0, 1, NULL, '2020-12-01 17:36:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123934, 2, '下属负责的客户', 16384, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 17:36:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123935, 2, '我参与的客户', 16384, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16384}}', 0, 1, NULL, '2020-12-01 17:36:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123936, 2, '我关注的客户', 16384, 0, '{\"customer_id\":{\"condition\":\"in\",\"name\":\"customer_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-01 17:36:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123937, 3, '全部联系人', 16384, 0, '', 0, 1, NULL, '2020-12-01 17:36:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123938, 3, '我负责的联系人', 16384, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16384}}', 0, 1, NULL, '2020-12-01 17:36:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123939, 3, '下属负责的联系人', 16384, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 17:36:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123940, 3, '我关注的联系人', 16384, 0, '{\"contacts_id\":{\"condition\":\"in\",\"name\":\"contacts_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-01 17:36:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123941, 5, '全部商机', 16384, 0, '', 0, 1, NULL, '2020-12-01 17:36:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123942, 5, '我负责的商机', 16384, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16384}}', 0, 1, NULL, '2020-12-01 17:36:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123943, 5, '下属负责的商机', 16384, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 17:36:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123944, 5, '我参与的商机', 16384, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16384}}', 0, 1, NULL, '2020-12-01 17:36:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123945, 5, '我关注的商机', 16384, 0, '{\"business_id\":{\"condition\":\"in\",\"name\":\"business_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-01 17:36:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123946, 6, '全部合同', 16384, 0, '', 0, 1, NULL, '2020-12-01 17:36:22', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123947, 6, '我负责的合同', 16384, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16384}}', 0, 1, NULL, '2020-12-01 17:36:22', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123948, 6, '下属负责的合同', 16384, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 17:36:22', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123949, 6, '我参与的合同', 16384, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16384}}', 0, 1, NULL, '2020-12-01 17:36:22', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123950, 7, '全部回款', 16384, 0, '', 0, 1, NULL, '2020-12-01 17:36:23', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123951, 7, '我负责的回款', 16384, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16384}}', 0, 1, NULL, '2020-12-01 17:36:23', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123952, 7, '下属负责的回款', 16384, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 17:36:23', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123953, 17, '全部回访', 16384, 0, '', 0, 1, NULL, '2020-12-01 17:36:25', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123954, 17, '我负责的回访', 16384, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16384}}', 0, 1, NULL, '2020-12-01 17:36:25', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123955, 17, '下属负责的回访', 16384, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 17:36:25', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123956, 4, '全部产品', 16384, 0, '', 0, 1, NULL, '2020-12-01 17:36:25', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123957, 4, '上架的产品', 16384, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"上架\"}}', 0, 1, NULL, '2020-12-01 17:36:25', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123958, 4, '下架的产品', 16384, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"下架\"}}', 0, 1, NULL, '2020-12-01 17:36:25', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123959, 21, '全部任务', 16387, 0, '', 0, 1, NULL, '2020-12-01 17:37:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123960, 21, '下属负责的任务', 16387, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 17:37:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123961, 21, '我负责的任务', 16387, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16387}}', 0, 1, NULL, '2020-12-01 17:37:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123962, 21, '我参与的任务', 16387, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16387,\"}}', 0, 1, NULL, '2020-12-01 17:37:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123963, 23, '全部产品', 16384, 0, '', 0, 1, NULL, '2020-12-01 17:49:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123964, 23, '发版的产品', 16384, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"发版\"}}', 0, 1, NULL, '2020-12-01 17:49:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123965, 23, '规划的产品', 16384, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"规划\"}}', 0, 1, NULL, '2020-12-01 17:49:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123966, 23, '全部产品', 16387, 0, '', 0, 1, NULL, '2020-12-01 17:59:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123967, 23, '发版的产品', 16387, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"发版\"}}', 0, 1, NULL, '2020-12-01 17:59:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123968, 23, '规划的产品', 16387, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"规划\"}}', 0, 1, NULL, '2020-12-01 17:59:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123969, 2, '全部客户', 16387, 0, '', 0, 1, NULL, '2020-12-01 18:00:54', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123970, 2, '我负责的客户', 16387, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16387}}', 0, 1, NULL, '2020-12-01 18:00:54', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123971, 2, '下属负责的客户', 16387, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-01 18:00:54', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123972, 2, '我参与的客户', 16387, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16387}}', 0, 1, NULL, '2020-12-01 18:00:54', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123973, 2, '我关注的客户', 16387, 0, '{\"customer_id\":{\"condition\":\"in\",\"name\":\"customer_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-01 18:00:54', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123974, 28, '1111', 16374, 99999, '{\"task_id\":{\"condition\":\"contains\",\"value\":\"111\",\"formType\":\"task\",\"name\":\"task_id\"}}', 0, 0, NULL, '2020-12-03 10:37:17', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123975, 5, '全部商机', 16377, 0, '', 0, 1, NULL, '2020-12-04 11:09:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123976, 5, '我负责的商机', 16377, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16377}}', 0, 1, NULL, '2020-12-04 11:09:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123977, 5, '下属负责的商机', 16377, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-04 11:09:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123978, 5, '我参与的商机', 16377, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16377}}', 0, 1, NULL, '2020-12-04 11:09:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123979, 5, '我关注的商机', 16377, 0, '{\"business_id\":{\"condition\":\"in\",\"name\":\"business_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-04 11:09:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123980, 5, '全部商机', 16377, 0, '', 0, 1, NULL, '2020-12-04 11:09:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123981, 5, '我负责的商机', 16377, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16377}}', 0, 1, NULL, '2020-12-04 11:09:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123982, 5, '下属负责的商机', 16377, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-04 11:09:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123983, 5, '我参与的商机', 16377, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16377}}', 0, 1, NULL, '2020-12-04 11:09:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123984, 5, '我关注的商机', 16377, 0, '{\"business_id\":{\"condition\":\"in\",\"name\":\"business_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-04 11:09:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123985, 2, '全部客户', 16377, 0, '', 0, 1, NULL, '2020-12-04 11:45:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123986, 2, '我负责的客户', 16377, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16377}}', 0, 1, NULL, '2020-12-04 11:45:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123987, 2, '下属负责的客户', 16377, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-04 11:45:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123988, 2, '我参与的客户', 16377, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16377}}', 0, 1, NULL, '2020-12-04 11:45:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123989, 2, '我关注的客户', 16377, 0, '{\"customer_id\":{\"condition\":\"in\",\"name\":\"customer_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-04 11:45:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123990, 3, '全部联系人', 16377, 0, '', 0, 1, NULL, '2020-12-04 13:22:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123991, 3, '我负责的联系人', 16377, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16377}}', 0, 1, NULL, '2020-12-04 13:22:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123992, 3, '下属负责的联系人', 16377, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-04 13:22:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123993, 3, '我关注的联系人', 16377, 0, '{\"contacts_id\":{\"condition\":\"in\",\"name\":\"contacts_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-04 13:22:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123994, 19, '全部项目', 16377, 0, '', 0, 1, NULL, '2020-12-05 10:15:15', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123995, 19, '我负责的项目', 16377, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16377}}', 0, 1, NULL, '2020-12-05 10:15:15', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123996, 21, '全部任务', 16377, 0, '', 0, 1, NULL, '2020-12-05 10:24:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123997, 21, '下属负责的任务', 16377, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-05 10:24:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123998, 21, '我负责的任务', 16377, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16377}}', 0, 1, NULL, '2020-12-05 10:24:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (123999, 21, '我参与的任务', 16377, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16377,\"}}', 0, 1, NULL, '2020-12-05 10:24:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124000, 29, '全部任务', 16374, 0, '', 0, 1, NULL, '2020-12-07 09:14:40', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124001, 29, '下属负责的任务', 16374, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-07 09:14:40', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124002, 29, '我负责的任务', 16374, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16374}}', 0, 1, NULL, '2020-12-07 09:14:40', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124003, 29, '我参与的任务', 16374, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16374,\"}}', 0, 1, NULL, '2020-12-07 09:14:40', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124004, 1, '全部线索', 16362, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-07 14:15:29', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124005, 1, '我负责的线索', 16362, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16362},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-07 14:15:29', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124006, 1, '下属负责的线索', 16362, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"16378,0,16380,0\"},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-07 14:15:29', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124007, 1, '已转化的线索', 16362, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":\"1\"}}', 0, 1, 'transform', '2020-12-07 14:15:29', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124008, 1, '我关注的线索', 16362, 0, '{\"leads_id\":{\"condition\":\"in\",\"name\":\"leads_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-07 14:15:29', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124009, 7, '全部回款', 16374, 0, '', 0, 1, NULL, '2020-12-07 14:21:29', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124010, 7, '我负责的回款', 16374, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16374}}', 0, 1, NULL, '2020-12-07 14:21:29', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124011, 7, '下属负责的回款', 16374, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-07 14:21:29', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124012, 17, '全部回访', 16374, 0, '', 0, 1, NULL, '2020-12-07 14:21:30', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124013, 17, '我负责的回访', 16374, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16374}}', 0, 1, NULL, '2020-12-07 14:21:30', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124014, 17, '下属负责的回访', 16374, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-07 14:21:30', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124015, 4, '全部产品', 16374, 0, '', 0, 1, NULL, '2020-12-07 14:21:30', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124016, 4, '上架的产品', 16374, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"上架\"}}', 0, 1, NULL, '2020-12-07 14:21:30', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124017, 4, '下架的产品', 16374, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"下架\"}}', 0, 1, NULL, '2020-12-07 14:21:30', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124018, 5, '全部商机', 16371, 0, '', 0, 1, NULL, '2020-12-08 11:43:36', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124019, 5, '我负责的商机', 16371, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16371}}', 0, 1, NULL, '2020-12-08 11:43:36', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124020, 5, '下属负责的商机', 16371, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"16372,0\"}}', 0, 1, NULL, '2020-12-08 11:43:36', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124021, 5, '我参与的商机', 16371, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16371}}', 0, 1, NULL, '2020-12-08 11:43:36', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124022, 5, '我关注的商机', 16371, 0, '{\"business_id\":{\"condition\":\"in\",\"name\":\"business_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-08 11:43:36', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124023, 7, '全部回款', 16371, 0, '', 0, 1, NULL, '2020-12-10 14:51:00', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124024, 7, '我负责的回款', 16371, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16371}}', 0, 1, NULL, '2020-12-10 14:51:00', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124025, 7, '下属负责的回款', 16371, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"16372,0\"}}', 0, 1, NULL, '2020-12-10 14:51:00', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124026, 17, '全部回访', 16362, 0, '', 0, 1, NULL, '2020-12-10 15:05:47', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124027, 17, '我负责的回访', 16362, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16362}}', 0, 1, NULL, '2020-12-10 15:05:47', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124028, 17, '下属负责的回访', 16362, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"16378,0,16380,0\"}}', 0, 1, NULL, '2020-12-10 15:05:47', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124029, 4, '全部产品', 16362, 0, '', 0, 1, NULL, '2020-12-10 15:05:49', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124030, 4, '上架的产品', 16362, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"上架\"}}', 0, 1, NULL, '2020-12-10 15:05:49', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124031, 4, '下架的产品', 16362, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"下架\"}}', 0, 1, NULL, '2020-12-10 15:05:49', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124130, 19, '全部项目', 16408, 0, '', 0, 1, NULL, '2020-12-11 15:20:16', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124131, 19, '我负责的项目', 16408, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16408}}', 0, 1, NULL, '2020-12-11 15:20:16', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124132, 21, '全部任务', 16408, 0, '', 0, 1, NULL, '2020-12-11 15:20:33', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124133, 21, '下属负责的任务', 16408, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-11 15:20:33', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124134, 21, '我负责的任务', 16408, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16408}}', 0, 1, NULL, '2020-12-11 15:20:33', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124135, 21, '我参与的任务', 16408, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16408,\"}}', 0, 1, NULL, '2020-12-11 15:20:33', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124136, 20, '全部里程碑', 16408, 0, '', 0, 1, NULL, '2020-12-11 15:21:16', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124140, 29, '全部任务', 16408, 0, '', 0, 1, NULL, '2020-12-11 17:54:03', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124141, 29, '下属负责的任务', 16408, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-11 17:54:03', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124142, 29, '我负责的任务', 16408, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16408}}', 0, 1, NULL, '2020-12-11 17:54:03', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124143, 29, '我参与的任务', 16408, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16408,\"}}', 0, 1, NULL, '2020-12-11 17:54:03', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124154, 19, '全部项目', 16394, 0, '', 0, 1, NULL, '2020-12-14 10:32:49', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124155, 19, '我负责的项目', 16394, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16394}}', 0, 1, NULL, '2020-12-14 10:32:49', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124156, 2, '全部客户', 16394, 0, '', 0, 1, NULL, '2020-12-14 10:57:31', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124157, 2, '我负责的客户', 16394, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16394}}', 0, 1, NULL, '2020-12-14 10:57:31', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124158, 2, '下属负责的客户', 16394, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-14 10:57:31', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124159, 2, '我参与的客户', 16394, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16394}}', 0, 1, NULL, '2020-12-14 10:57:31', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124160, 2, '我关注的客户', 16394, 0, '{\"customer_id\":{\"condition\":\"in\",\"name\":\"customer_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-14 10:57:31', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124161, 19, '全部项目', 16404, 0, '', 0, 1, NULL, '2020-12-14 11:00:27', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124162, 19, '我负责的项目', 16404, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16404}}', 0, 1, NULL, '2020-12-14 11:00:27', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124163, 23, '全部产品', 16404, 0, '', 0, 1, NULL, '2020-12-14 11:01:05', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124164, 23, '发版的产品', 16404, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"发版\"}}', 0, 1, NULL, '2020-12-14 11:01:05', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124165, 23, '规划的产品', 16404, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"规划\"}}', 0, 1, NULL, '2020-12-14 11:01:05', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124166, 2, '全部客户', 16404, 0, '', 0, 1, NULL, '2020-12-14 11:06:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124167, 2, '我负责的客户', 16404, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16404}}', 0, 1, NULL, '2020-12-14 11:06:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124168, 2, '下属负责的客户', 16404, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-14 11:06:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124169, 2, '我参与的客户', 16404, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16404}}', 0, 1, NULL, '2020-12-14 11:06:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124170, 2, '我关注的客户', 16404, 0, '{\"customer_id\":{\"condition\":\"in\",\"name\":\"customer_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-14 11:06:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124171, 5, '全部商机', 16404, 0, '', 0, 1, NULL, '2020-12-14 11:06:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124172, 5, '我负责的商机', 16404, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16404}}', 0, 1, NULL, '2020-12-14 11:06:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124173, 5, '下属负责的商机', 16404, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-14 11:06:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124174, 5, '我参与的商机', 16404, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16404}}', 0, 1, NULL, '2020-12-14 11:06:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124175, 5, '我关注的商机', 16404, 0, '{\"business_id\":{\"condition\":\"in\",\"name\":\"business_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-14 11:06:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124186, 19, '全部项目', 16405, 0, '', 0, 1, NULL, '2020-12-14 17:56:46', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124187, 19, '我负责的项目', 16405, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16405}}', 0, 1, NULL, '2020-12-14 17:56:46', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124188, 24, '全部需求', 16408, 0, '', 0, 1, NULL, '2020-12-15 09:19:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124189, 24, '已对标需求', 16408, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"1\"}}', 0, 1, NULL, '2020-12-15 09:19:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124190, 24, '未对标需求', 16408, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-15 09:19:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124191, 19, '全部项目', 16399, 0, '', 0, 1, NULL, '2020-12-15 11:01:23', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124192, 19, '我负责的项目', 16399, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16399}}', 0, 1, NULL, '2020-12-15 11:01:23', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124193, 2, '全部客户', 16399, 0, '', 0, 1, NULL, '2020-12-15 11:30:41', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124194, 2, '我负责的客户', 16399, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16399}}', 0, 1, NULL, '2020-12-15 11:30:41', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124195, 2, '下属负责的客户', 16399, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-15 11:30:41', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124196, 2, '我参与的客户', 16399, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16399}}', 0, 1, NULL, '2020-12-15 11:30:41', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124197, 2, '我关注的客户', 16399, 0, '{\"customer_id\":{\"condition\":\"in\",\"name\":\"customer_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-15 11:30:41', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124198, 3, '全部联系人', 16399, 0, '', 0, 1, NULL, '2020-12-15 11:31:01', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124199, 3, '我负责的联系人', 16399, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16399}}', 0, 1, NULL, '2020-12-15 11:31:01', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124200, 3, '下属负责的联系人', 16399, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-15 11:31:01', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124201, 3, '我关注的联系人', 16399, 0, '{\"contacts_id\":{\"condition\":\"in\",\"name\":\"contacts_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-15 11:31:01', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124202, 1, '全部线索', 16399, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-15 13:13:46', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124203, 1, '我负责的线索', 16399, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16399},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-15 13:13:46', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124204, 1, '下属负责的线索', 16399, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-15 13:13:46', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124205, 1, '已转化的线索', 16399, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":\"1\"}}', 0, 1, 'transform', '2020-12-15 13:13:46', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124206, 1, '我关注的线索', 16399, 0, '{\"leads_id\":{\"condition\":\"in\",\"name\":\"leads_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-15 13:13:46', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124207, 5, '全部商机', 16399, 0, '', 0, 1, NULL, '2020-12-15 13:13:55', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124208, 5, '我负责的商机', 16399, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16399}}', 0, 1, NULL, '2020-12-15 13:13:55', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124209, 5, '下属负责的商机', 16399, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-15 13:13:55', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124210, 5, '我参与的商机', 16399, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16399}}', 0, 1, NULL, '2020-12-15 13:13:55', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124211, 5, '我关注的商机', 16399, 0, '{\"business_id\":{\"condition\":\"in\",\"name\":\"business_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-15 13:13:55', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124212, 6, '全部合同', 16399, 0, '', 0, 1, NULL, '2020-12-15 13:13:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124213, 6, '我负责的合同', 16399, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16399}}', 0, 1, NULL, '2020-12-15 13:13:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124214, 6, '下属负责的合同', 16399, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-15 13:13:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124215, 6, '我参与的合同', 16399, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16399}}', 0, 1, NULL, '2020-12-15 13:13:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124216, 7, '全部回款', 16399, 0, '', 0, 1, NULL, '2020-12-15 13:14:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124217, 7, '我负责的回款', 16399, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16399}}', 0, 1, NULL, '2020-12-15 13:14:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124218, 7, '下属负责的回款', 16399, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-15 13:14:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124219, 17, '全部回访', 16399, 0, '', 0, 1, NULL, '2020-12-15 13:14:33', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124220, 17, '我负责的回访', 16399, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16399}}', 0, 1, NULL, '2020-12-15 13:14:33', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124221, 17, '下属负责的回访', 16399, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-15 13:14:33', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124222, 4, '全部产品', 16399, 0, '', 0, 1, NULL, '2020-12-15 13:14:37', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124223, 4, '上架的产品', 16399, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"上架\"}}', 0, 1, NULL, '2020-12-15 13:14:37', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124224, 4, '下架的产品', 16399, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"下架\"}}', 0, 1, NULL, '2020-12-15 13:14:37', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124237, 2, '全部客户', 16398, 0, '', 0, 1, NULL, '2020-12-16 16:49:56', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124238, 2, '我负责的客户', 16398, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16398}}', 0, 1, NULL, '2020-12-16 16:49:56', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124239, 2, '下属负责的客户', 16398, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-16 16:49:56', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124240, 2, '我参与的客户', 16398, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16398}}', 0, 1, NULL, '2020-12-16 16:49:56', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124241, 2, '我关注的客户', 16398, 0, '{\"customer_id\":{\"condition\":\"in\",\"name\":\"customer_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-16 16:49:56', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124242, 3, '全部联系人', 16398, 0, '', 0, 1, NULL, '2020-12-16 17:27:29', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124243, 3, '我负责的联系人', 16398, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16398}}', 0, 1, NULL, '2020-12-16 17:27:29', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124244, 3, '下属负责的联系人', 16398, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-16 17:27:29', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124245, 3, '我关注的联系人', 16398, 0, '{\"contacts_id\":{\"condition\":\"in\",\"name\":\"contacts_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-16 17:27:29', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124246, 5, '全部商机', 16398, 0, '', 0, 1, NULL, '2020-12-16 17:27:46', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124247, 5, '我负责的商机', 16398, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16398}}', 0, 1, NULL, '2020-12-16 17:27:46', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124248, 5, '下属负责的商机', 16398, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-16 17:27:46', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124249, 5, '我参与的商机', 16398, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16398}}', 0, 1, NULL, '2020-12-16 17:27:46', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124250, 5, '我关注的商机', 16398, 0, '{\"business_id\":{\"condition\":\"in\",\"name\":\"business_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-16 17:27:46', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124260, 2, '全部客户', 16407, 0, '', 0, 1, NULL, '2020-12-17 10:57:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124261, 2, '我负责的客户', 16407, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16407}}', 0, 1, NULL, '2020-12-17 10:57:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124262, 2, '下属负责的客户', 16407, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-17 10:57:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124263, 2, '我参与的客户', 16407, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16407}}', 0, 1, NULL, '2020-12-17 10:57:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124264, 2, '我关注的客户', 16407, 0, '{\"customer_id\":{\"condition\":\"in\",\"name\":\"customer_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-17 10:57:19', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124265, 3, '全部联系人', 16407, 0, '', 0, 1, NULL, '2020-12-17 10:58:02', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124266, 3, '我负责的联系人', 16407, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16407}}', 0, 1, NULL, '2020-12-17 10:58:02', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124267, 3, '下属负责的联系人', 16407, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-17 10:58:02', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124268, 3, '我关注的联系人', 16407, 0, '{\"contacts_id\":{\"condition\":\"in\",\"name\":\"contacts_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-17 10:58:02', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124269, 5, '全部商机', 16407, 0, '', 0, 1, NULL, '2020-12-17 11:00:18', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124270, 5, '我负责的商机', 16407, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16407}}', 0, 1, NULL, '2020-12-17 11:00:18', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124271, 5, '下属负责的商机', 16407, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-17 11:00:18', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124272, 5, '我参与的商机', 16407, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16407}}', 0, 1, NULL, '2020-12-17 11:00:18', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124273, 5, '我关注的商机', 16407, 0, '{\"business_id\":{\"condition\":\"in\",\"name\":\"business_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-17 11:00:18', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124288, 19, '全部项目', 16407, 0, '', 0, 1, NULL, '2020-12-17 14:02:52', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124289, 19, '我负责的项目', 16407, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16407}}', 0, 1, NULL, '2020-12-17 14:02:52', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124294, 1, '全部线索', 16408, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-17 14:24:41', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124295, 1, '我负责的线索', 16408, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16408},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-17 14:24:41', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124296, 1, '下属负责的线索', 16408, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-17 14:24:41', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124297, 1, '已转化的线索', 16408, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":\"1\"}}', 0, 1, 'transform', '2020-12-17 14:24:41', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124298, 1, '我关注的线索', 16408, 0, '{\"leads_id\":{\"condition\":\"in\",\"name\":\"leads_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-17 14:24:41', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124299, 5, '全部商机', 16408, 0, '', 0, 1, NULL, '2020-12-17 14:24:45', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124300, 5, '我负责的商机', 16408, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16408}}', 0, 1, NULL, '2020-12-17 14:24:45', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124301, 5, '下属负责的商机', 16408, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-17 14:24:45', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124302, 5, '我参与的商机', 16408, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16408}}', 0, 1, NULL, '2020-12-17 14:24:45', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124303, 5, '我关注的商机', 16408, 0, '{\"business_id\":{\"condition\":\"in\",\"name\":\"business_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-17 14:24:45', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124318, 6, '全部合同', 16398, 0, '', 0, 1, NULL, '2020-12-17 14:36:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124319, 6, '我负责的合同', 16398, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16398}}', 0, 1, NULL, '2020-12-17 14:36:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124320, 6, '下属负责的合同', 16398, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-17 14:36:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124321, 6, '我参与的合同', 16398, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16398}}', 0, 1, NULL, '2020-12-17 14:36:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124325, 7, '全部回款', 16398, 0, '', 0, 1, NULL, '2020-12-17 14:39:46', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124326, 7, '我负责的回款', 16398, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16398}}', 0, 1, NULL, '2020-12-17 14:39:46', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124327, 7, '下属负责的回款', 16398, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-17 14:39:46', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124331, 1, '全部线索', 16398, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-17 14:48:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124332, 1, '我负责的线索', 16398, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16398},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-17 14:48:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124333, 1, '下属负责的线索', 16398, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-17 14:48:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124334, 1, '已转化的线索', 16398, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":\"1\"}}', 0, 1, 'transform', '2020-12-17 14:48:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124335, 1, '我关注的线索', 16398, 0, '{\"leads_id\":{\"condition\":\"in\",\"name\":\"leads_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-17 14:48:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124339, 17, '全部回访', 16398, 0, '', 0, 1, NULL, '2020-12-17 15:01:34', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124340, 17, '我负责的回访', 16398, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16398}}', 0, 1, NULL, '2020-12-17 15:01:34', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124341, 17, '下属负责的回访', 16398, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-17 15:01:34', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124342, 4, '全部产品', 16398, 0, '', 0, 1, NULL, '2020-12-17 15:05:16', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124343, 4, '上架的产品', 16398, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"上架\"}}', 0, 1, NULL, '2020-12-17 15:05:16', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124344, 4, '下架的产品', 16398, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"下架\"}}', 0, 1, NULL, '2020-12-17 15:05:16', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124355, 17, '全部回访', 16408, 0, '', 0, 1, NULL, '2020-12-17 16:19:12', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124356, 17, '我负责的回访', 16408, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16408}}', 0, 1, NULL, '2020-12-17 16:19:12', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124357, 17, '下属负责的回访', 16408, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-17 16:19:12', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124366, 2, '全部客户', 16408, 0, '', 0, 1, NULL, '2020-12-17 16:49:54', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124367, 2, '我负责的客户', 16408, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16408}}', 0, 1, NULL, '2020-12-17 16:49:54', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124368, 2, '下属负责的客户', 16408, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-17 16:49:54', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124369, 2, '我参与的客户', 16408, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16408}}', 0, 1, NULL, '2020-12-17 16:49:54', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124370, 2, '我关注的客户', 16408, 0, '{\"customer_id\":{\"condition\":\"in\",\"name\":\"customer_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-17 16:49:54', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124380, 3, '全部联系人', 16408, 0, '', 0, 1, NULL, '2020-12-21 16:17:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124381, 3, '我负责的联系人', 16408, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16408}}', 0, 1, NULL, '2020-12-21 16:17:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124382, 3, '下属负责的联系人', 16408, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-21 16:17:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124383, 3, '我关注的联系人', 16408, 0, '{\"contacts_id\":{\"condition\":\"in\",\"name\":\"contacts_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-21 16:17:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124384, 6, '全部合同', 16408, 0, '', 0, 1, NULL, '2020-12-21 16:17:15', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124385, 6, '我负责的合同', 16408, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16408}}', 0, 1, NULL, '2020-12-21 16:17:15', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124386, 6, '下属负责的合同', 16408, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-21 16:17:15', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124387, 6, '我参与的合同', 16408, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16408}}', 0, 1, NULL, '2020-12-21 16:17:15', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124388, 7, '全部回款', 16408, 0, '', 0, 1, NULL, '2020-12-21 16:17:17', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124389, 7, '我负责的回款', 16408, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16408}}', 0, 1, NULL, '2020-12-21 16:17:17', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124390, 7, '下属负责的回款', 16408, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-21 16:17:17', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124391, 4, '全部产品', 16408, 0, '', 0, 1, NULL, '2020-12-21 16:20:34', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124392, 4, '上架的产品', 16408, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"上架\"}}', 0, 1, NULL, '2020-12-21 16:20:34', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124393, 4, '下架的产品', 16408, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"下架\"}}', 0, 1, NULL, '2020-12-21 16:20:34', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124394, 1, '全部线索', 16404, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-22 11:40:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124395, 1, '我负责的线索', 16404, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16404},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-22 11:40:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124396, 1, '下属负责的线索', 16404, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-22 11:40:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124397, 1, '已转化的线索', 16404, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":\"1\"}}', 0, 1, 'transform', '2020-12-22 11:40:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124398, 1, '我关注的线索', 16404, 0, '{\"leads_id\":{\"condition\":\"in\",\"name\":\"leads_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-22 11:40:28', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124399, 3, '全部联系人', 16404, 0, '', 0, 1, NULL, '2020-12-22 11:40:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124400, 3, '我负责的联系人', 16404, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16404}}', 0, 1, NULL, '2020-12-22 11:40:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124401, 3, '下属负责的联系人', 16404, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-22 11:40:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124402, 3, '我关注的联系人', 16404, 0, '{\"contacts_id\":{\"condition\":\"in\",\"name\":\"contacts_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-22 11:40:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124403, 6, '全部合同', 16404, 0, '', 0, 1, NULL, '2020-12-22 11:41:09', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124404, 6, '我负责的合同', 16404, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16404}}', 0, 1, NULL, '2020-12-22 11:41:09', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124405, 6, '下属负责的合同', 16404, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-22 11:41:09', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124406, 6, '我参与的合同', 16404, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16404}}', 0, 1, NULL, '2020-12-22 11:41:09', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124407, 7, '全部回款', 16404, 0, '', 0, 1, NULL, '2020-12-22 11:41:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124408, 7, '我负责的回款', 16404, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16404}}', 0, 1, NULL, '2020-12-22 11:41:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124409, 7, '下属负责的回款', 16404, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-22 11:41:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124410, 17, '全部回访', 16404, 0, '', 0, 1, NULL, '2020-12-22 11:41:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124411, 17, '我负责的回访', 16404, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16404}}', 0, 1, NULL, '2020-12-22 11:41:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124412, 17, '下属负责的回访', 16404, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-22 11:41:20', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124413, 4, '全部产品', 16404, 0, '', 0, 1, NULL, '2020-12-22 11:41:23', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124414, 4, '上架的产品', 16404, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"上架\"}}', 0, 1, NULL, '2020-12-22 11:41:23', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124415, 4, '下架的产品', 16404, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"下架\"}}', 0, 1, NULL, '2020-12-22 11:41:23', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124416, 21, '全部任务', 16407, 0, '', 0, 1, NULL, '2020-12-23 11:12:26', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124417, 21, '下属负责的任务', 16407, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-23 11:12:26', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124418, 21, '我负责的任务', 16407, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16407}}', 0, 1, NULL, '2020-12-23 11:12:26', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124419, 21, '我参与的任务', 16407, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16407,\"}}', 0, 1, NULL, '2020-12-23 11:12:26', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124420, 29, '全部任务', 16407, 0, '', 0, 1, NULL, '2020-12-23 11:23:04', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124421, 29, '下属负责的任务', 16407, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-23 11:23:04', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124422, 29, '我负责的任务', 16407, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16407}}', 0, 1, NULL, '2020-12-23 11:23:04', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124423, 29, '我参与的任务', 16407, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16407,\"}}', 0, 1, NULL, '2020-12-23 11:23:04', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124424, 20, '全部里程碑', 16407, 0, '', 0, 1, NULL, '2020-12-23 11:40:32', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124425, 19, '全部项目', 16392, 0, '', 0, 1, NULL, '2020-12-29 17:37:16', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124426, 19, '我负责的项目', 16392, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16392}}', 0, 1, NULL, '2020-12-29 17:37:16', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124427, 20, '全部里程碑', 16392, 0, '', 0, 1, NULL, '2020-12-29 17:42:48', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124441, 23, '全部产品', 16392, 0, '', 0, 1, NULL, '2020-12-30 11:14:38', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124442, 23, '发版的产品', 16392, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"发版\"}}', 0, 1, NULL, '2020-12-30 11:14:38', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124443, 23, '规划的产品', 16392, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"规划\"}}', 0, 1, NULL, '2020-12-30 11:14:38', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124448, 23, '全部产品', 16408, 0, '', 0, 1, NULL, '2020-12-30 13:06:40', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124449, 23, '发版的产品', 16408, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"发版\"}}', 0, 1, NULL, '2020-12-30 13:06:40', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124450, 23, '规划的产品', 16408, 0, '{\"product_status\":{\"condition\":\"is\",\"name\":\"product_status\",\"value\":\"规划\"}}', 0, 1, NULL, '2020-12-30 13:06:40', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124461, 19, '全部项目', 16360, 0, '', 0, 1, NULL, '2020-12-30 16:48:03', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124462, 19, '我负责的项目', 16360, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16360}}', 0, 1, NULL, '2020-12-30 16:48:03', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124463, 2, '全部客户', 16360, 0, '', 0, 1, NULL, '2020-12-30 17:05:22', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124464, 2, '我负责的客户', 16360, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16360}}', 0, 1, NULL, '2020-12-30 17:05:22', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124465, 2, '下属负责的客户', 16360, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"16388,16389,0,16390,16391,16392,0,16393,0,16398,0,16399,0,16400,0,16394,0,16395,16396,0,16397,0,16408,0,16409,16401,16402,0,16403,0,16404,0,16405,0,16406,0,16407,0,16411,0,16410,0\"}}', 0, 1, NULL, '2020-12-30 17:05:22', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124466, 2, '我参与的客户', 16360, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16360}}', 0, 1, NULL, '2020-12-30 17:05:22', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124467, 2, '我关注的客户', 16360, 0, '{\"customer_id\":{\"condition\":\"in\",\"name\":\"customer_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-30 17:05:22', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124473, 24, '全部需求', 16392, 0, '', 0, 1, NULL, '2020-12-30 17:18:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124474, 24, '已对标需求', 16392, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"1\"}}', 0, 1, NULL, '2020-12-30 17:18:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124475, 24, '未对标需求', 16392, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-30 17:18:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124476, 21, '全部任务', 16392, 0, '', 0, 1, NULL, '2020-12-30 17:18:09', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124477, 21, '下属负责的任务', 16392, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-30 17:18:09', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124478, 21, '我负责的任务', 16392, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16392}}', 0, 1, NULL, '2020-12-30 17:18:09', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124479, 21, '我参与的任务', 16392, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16392,\"}}', 0, 1, NULL, '2020-12-30 17:18:09', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124480, 29, '全部任务', 16392, 0, '', 0, 1, NULL, '2020-12-30 17:19:01', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124481, 29, '下属负责的任务', 16392, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-30 17:19:01', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124482, 29, '我负责的任务', 16392, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16392}}', 0, 1, NULL, '2020-12-30 17:19:01', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124483, 29, '我参与的任务', 16392, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16392,\"}}', 0, 1, NULL, '2020-12-30 17:19:01', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124488, 19, '全部项目', 16389, 0, '', 0, 1, NULL, '2020-12-30 18:03:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124489, 19, '我负责的项目', 16389, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16389}}', 0, 1, NULL, '2020-12-30 18:03:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124490, 21, '全部任务', 16389, 0, '', 0, 1, NULL, '2020-12-30 18:04:04', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124491, 21, '下属负责的任务', 16389, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-30 18:04:04', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124492, 21, '我负责的任务', 16389, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16389}}', 0, 1, NULL, '2020-12-30 18:04:04', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124493, 21, '我参与的任务', 16389, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16389,\"}}', 0, 1, NULL, '2020-12-30 18:04:04', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124494, 21, '全部任务', 16360, 0, '', 0, 1, NULL, '2020-12-30 18:04:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124495, 21, '下属负责的任务', 16360, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"16388,16389,0,16390,16391,16392,0,16393,0,16398,0,16399,0,16400,0,16394,0,16395,16396,0,16397,0,16408,0,16409,16401,16402,0,16403,0,16404,0,16405,0,16406,0,16407,0,16411,0,16410,0\"}}', 0, 1, NULL, '2020-12-30 18:04:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124496, 21, '我负责的任务', 16360, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16360}}', 0, 1, NULL, '2020-12-30 18:04:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124497, 21, '我参与的任务', 16360, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16360,\"}}', 0, 1, NULL, '2020-12-30 18:04:50', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124498, 29, '全部任务', 16360, 0, '', 0, 1, NULL, '2020-12-30 18:04:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124499, 29, '下属负责的任务', 16360, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"16388,16389,0,16390,16391,16392,0,16393,0,16398,0,16399,0,16400,0,16394,0,16395,16396,0,16397,0,16408,0,16409,16401,16402,0,16403,0,16404,0,16405,0,16406,0,16407,0,16411,0,16410,0\"}}', 0, 1, NULL, '2020-12-30 18:04:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124500, 29, '我负责的任务', 16360, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16360}}', 0, 1, NULL, '2020-12-30 18:04:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124501, 29, '我参与的任务', 16360, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16360,\"}}', 0, 1, NULL, '2020-12-30 18:04:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124502, 29, '全部任务', 16389, 0, '', 0, 1, NULL, '2020-12-30 18:07:02', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124503, 29, '下属负责的任务', 16389, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-30 18:07:02', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124504, 29, '我负责的任务', 16389, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16389}}', 0, 1, NULL, '2020-12-30 18:07:02', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124505, 29, '我参与的任务', 16389, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16389,\"}}', 0, 1, NULL, '2020-12-30 18:07:02', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124506, 20, '全部里程碑', 16360, 0, '', 0, 1, NULL, '2020-12-30 18:52:31', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124507, 24, '全部需求', 16407, 0, '', 0, 1, NULL, '2020-12-30 19:53:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124508, 24, '已对标需求', 16407, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"1\"}}', 0, 1, NULL, '2020-12-30 19:53:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124509, 24, '未对标需求', 16407, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-30 19:53:44', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124510, 1, '全部线索', 16407, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-30 21:01:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124511, 1, '我负责的线索', 16407, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16407},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-30 21:01:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124512, 1, '下属负责的线索', 16407, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-30 21:01:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124513, 1, '已转化的线索', 16407, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":\"1\"}}', 0, 1, 'transform', '2020-12-30 21:01:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124514, 1, '我关注的线索', 16407, 0, '{\"leads_id\":{\"condition\":\"in\",\"name\":\"leads_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-30 21:01:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124515, 6, '全部合同', 16407, 0, '', 0, 1, NULL, '2020-12-31 11:00:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124516, 6, '我负责的合同', 16407, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16407}}', 0, 1, NULL, '2020-12-31 11:00:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124517, 6, '下属负责的合同', 16407, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-31 11:00:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124518, 6, '我参与的合同', 16407, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16407}}', 0, 1, NULL, '2020-12-31 11:00:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124519, 7, '全部回款', 16407, 0, '', 0, 1, NULL, '2020-12-31 11:00:09', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124520, 7, '我负责的回款', 16407, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16407}}', 0, 1, NULL, '2020-12-31 11:00:09', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124521, 7, '下属负责的回款', 16407, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-31 11:00:09', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124522, 17, '全部回访', 16407, 0, '', 0, 1, NULL, '2020-12-31 11:00:12', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124523, 17, '我负责的回访', 16407, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16407}}', 0, 1, NULL, '2020-12-31 11:00:12', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124524, 17, '下属负责的回访', 16407, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-31 11:00:12', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124525, 4, '全部产品', 16407, 0, '', 0, 1, NULL, '2020-12-31 11:00:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124526, 4, '上架的产品', 16407, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"上架\"}}', 0, 1, NULL, '2020-12-31 11:00:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124527, 4, '下架的产品', 16407, 0, '{\"是否上下架\":{\"condition\":\"is\",\"name\":\"是否上下架\",\"value\":\"下架\"}}', 0, 1, NULL, '2020-12-31 11:00:13', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124528, 1, '全部线索', 16360, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-31 14:05:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124529, 1, '我负责的线索', 16360, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16360},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-31 14:05:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124530, 1, '下属负责的线索', 16360, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"16388,16389,0,16390,16391,16392,0,16393,0,16398,0,16399,0,16400,0,16394,0,16395,16396,0,16397,0,16408,0,16409,16401,16402,0,16403,0,16404,0,16405,0,16406,0,16407,0,16411,0,16410,0\"},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2020-12-31 14:05:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124531, 1, '已转化的线索', 16360, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":\"1\"}}', 0, 1, 'transform', '2020-12-31 14:05:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124532, 1, '我关注的线索', 16360, 0, '{\"leads_id\":{\"condition\":\"in\",\"name\":\"leads_id\",\"value\":\"0\"}}', 0, 1, 'star', '2020-12-31 14:05:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124533, 24, '全部需求', 16360, 0, '', 0, 1, NULL, '2020-12-31 14:39:01', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124534, 24, '已对标需求', 16360, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"1\"}}', 0, 1, NULL, '2020-12-31 14:39:01', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124535, 24, '未对标需求', 16360, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-31 14:39:01', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124536, 7, '全部回款', 16360, 0, '', 0, 1, NULL, '2020-12-31 14:48:12', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124537, 7, '我负责的回款', 16360, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16360}}', 0, 1, NULL, '2020-12-31 14:48:12', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124538, 7, '下属负责的回款', 16360, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"16388,16389,0,16390,16391,16392,0,16393,0,16398,0,16399,0,16400,0,16394,0,16395,16396,0,16397,0,16408,0,16409,16401,16402,0,16403,0,16404,0,16405,0,16406,0,16407,0,16411,0,16410,0\"}}', 0, 1, NULL, '2020-12-31 14:48:12', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124539, 24, '全部需求', 16389, 0, '', 0, 1, NULL, '2020-12-31 17:41:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124540, 24, '已对标需求', 16389, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"1\"}}', 0, 1, NULL, '2020-12-31 17:41:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124541, 24, '未对标需求', 16389, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"0\"}}', 0, 1, NULL, '2020-12-31 17:41:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124542, 20, '全部里程碑', 16389, 0, '', 0, 1, NULL, '2020-12-31 17:41:21', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124543, 2, '全部客户', 16389, 0, '', 0, 1, NULL, '2021-01-04 11:34:51', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124544, 2, '我负责的客户', 16389, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16389}}', 0, 1, NULL, '2021-01-04 11:34:51', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124545, 2, '下属负责的客户', 16389, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2021-01-04 11:34:51', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124546, 2, '我参与的客户', 16389, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16389}}', 0, 1, NULL, '2021-01-04 11:34:51', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124547, 2, '我关注的客户', 16389, 0, '{\"customer_id\":{\"condition\":\"in\",\"name\":\"customer_id\",\"value\":\"0\"}}', 0, 1, 'star', '2021-01-04 11:34:51', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124548, 3, '全部联系人', 16389, 0, '', 0, 1, NULL, '2021-01-04 12:00:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124549, 3, '我负责的联系人', 16389, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16389}}', 0, 1, NULL, '2021-01-04 12:00:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124550, 3, '下属负责的联系人', 16389, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2021-01-04 12:00:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124551, 3, '我关注的联系人', 16389, 0, '{\"contacts_id\":{\"condition\":\"in\",\"name\":\"contacts_id\",\"value\":\"0\"}}', 0, 1, 'star', '2021-01-04 12:00:07', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124552, 5, '全部商机', 16389, 0, '', 0, 1, NULL, '2021-01-04 16:47:31', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124553, 5, '我负责的商机', 16389, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16389}}', 0, 1, NULL, '2021-01-04 16:47:31', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124554, 5, '下属负责的商机', 16389, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2021-01-04 16:47:31', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124555, 5, '我参与的商机', 16389, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16389}}', 0, 1, NULL, '2021-01-04 16:47:31', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124556, 5, '我关注的商机', 16389, 0, '{\"business_id\":{\"condition\":\"in\",\"name\":\"business_id\",\"value\":\"0\"}}', 0, 1, 'star', '2021-01-04 16:47:31', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124557, 1, '全部线索', 16389, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2021-01-04 18:20:00', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124558, 1, '我负责的线索', 16389, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16389},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2021-01-04 18:20:00', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124559, 1, '下属负责的线索', 16389, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2021-01-04 18:20:00', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124560, 1, '已转化的线索', 16389, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":\"1\"}}', 0, 1, 'transform', '2021-01-04 18:20:00', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124561, 1, '我关注的线索', 16389, 0, '{\"leads_id\":{\"condition\":\"in\",\"name\":\"leads_id\",\"value\":\"0\"}}', 0, 1, 'star', '2021-01-04 18:20:00', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124562, 6, '全部合同', 16389, 0, '', 0, 1, NULL, '2021-01-05 13:49:10', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124563, 6, '我负责的合同', 16389, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16389}}', 0, 1, NULL, '2021-01-05 13:49:10', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124564, 6, '下属负责的合同', 16389, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"}}', 0, 1, NULL, '2021-01-05 13:49:10', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124565, 6, '我参与的合同', 16389, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16389}}', 0, 1, NULL, '2021-01-05 13:49:10', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124566, 1, '全部线索', 16392, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2021-01-05 18:39:52', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124567, 1, '我负责的线索', 16392, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16392},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2021-01-05 18:39:52', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124568, 1, '下属负责的线索', 16392, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"0\"},\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":0}}', 0, 1, NULL, '2021-01-05 18:39:52', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124569, 1, '已转化的线索', 16392, 0, '{\"is_transform\":{\"condition\":\"is\",\"name\":\"is_transform\",\"value\":\"1\"}}', 0, 1, 'transform', '2021-01-05 18:39:52', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124570, 1, '我关注的线索', 16392, 0, '{\"leads_id\":{\"condition\":\"in\",\"name\":\"leads_id\",\"value\":\"0\"}}', 0, 1, 'star', '2021-01-05 18:39:52', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124571, 19, '全部项目', 16388, 0, '', 0, 1, NULL, '2021-01-06 10:24:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124572, 19, '我负责的项目', 16388, 0, '{\"manager_id\":{\"condition\":\"is\",\"name\":\"manager_id\",\"value\":16388}}', 0, 1, NULL, '2021-01-06 10:24:59', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124573, 21, '全部任务', 16388, 0, '', 0, 1, NULL, '2021-01-06 10:25:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124574, 21, '下属负责的任务', 16388, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"16389,0\"}}', 0, 1, NULL, '2021-01-06 10:25:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124575, 21, '我负责的任务', 16388, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16388}}', 0, 1, NULL, '2021-01-06 10:25:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124576, 21, '我参与的任务', 16388, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16388,\"}}', 0, 1, NULL, '2021-01-06 10:25:08', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124577, 20, '全部里程碑', 16388, 0, '', 0, 1, NULL, '2021-01-06 10:25:09', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124578, 24, '全部需求', 16388, 0, '', 0, 1, NULL, '2021-01-06 10:25:10', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124579, 24, '已对标需求', 16388, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"1\"}}', 0, 1, NULL, '2021-01-06 10:25:10', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124580, 24, '未对标需求', 16388, 0, '{\"is_associated\":{\"condition\":\"is\",\"name\":\"is_associated\",\"value\":\"0\"}}', 0, 1, NULL, '2021-01-06 10:25:10', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124581, 29, '全部任务', 16388, 0, '', 0, 1, NULL, '2021-01-06 10:29:38', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124582, 29, '下属负责的任务', 16388, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":\"16389,0\"}}', 0, 1, NULL, '2021-01-06 10:29:38', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124583, 29, '我负责的任务', 16388, 0, '{\"main_user_id\":{\"condition\":\"is\",\"name\":\"main_user_id\",\"value\":16388}}', 0, 1, NULL, '2021-01-06 10:29:38', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124584, 29, '我参与的任务', 16388, 0, '{\"owner_user_id\":{\"condition\":\"contains\",\"name\":\"owner_user_id\",\"value\":\",16388,\"}}', 0, 1, NULL, '2021-01-06 10:29:38', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124585, 2, '全部客户', 16388, 0, '', 0, 1, NULL, '2021-01-06 11:00:48', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124586, 2, '我负责的客户', 16388, 0, '{\"owner_user_id\":{\"condition\":\"is\",\"name\":\"owner_user_id\",\"value\":16388}}', 0, 1, NULL, '2021-01-06 11:00:48', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124587, 2, '下属负责的客户', 16388, 0, '{\"owner_user_id\":{\"condition\":\"in\",\"name\":\"owner_user_id\",\"value\":\"16389,0\"}}', 0, 1, NULL, '2021-01-06 11:00:48', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124588, 2, '我参与的客户', 16388, 0, '{\"ro_user_id\":{\"condition\":\"takePart\",\"name\":\"ro_user_id\",\"value\":16388}}', 0, 1, NULL, '2021-01-06 11:00:48', NULL);
INSERT INTO `72crm_admin_scene` VALUES (124589, 2, '我关注的客户', 16388, 0, '{\"customer_id\":{\"condition\":\"in\",\"name\":\"customer_id\",\"value\":\"0\"}}', 0, 1, 'star', '2021-01-06 11:00:48', NULL);

-- ----------------------------
-- Table structure for 72crm_admin_scene_default
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_scene_default`;
CREATE TABLE `72crm_admin_scene_default`  (
  `default_id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(5) NOT NULL COMMENT '类型',
  `user_id` bigint(20) NOT NULL COMMENT '人员ID',
  `scene_id` int(11) NOT NULL COMMENT '场景ID',
  PRIMARY KEY (`default_id`) USING BTREE,
  UNIQUE INDEX `default_id`(`default_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '场景默认关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_scene_default
-- ----------------------------
INSERT INTO `72crm_admin_scene_default` VALUES (1, 24, 16360, 122020);
INSERT INTO `72crm_admin_scene_default` VALUES (2, 21, 16372, 122414);
INSERT INTO `72crm_admin_scene_default` VALUES (4, 28, 16360, 122595);

-- ----------------------------
-- Table structure for 72crm_admin_system_log
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_system_log`;
CREATE TABLE `72crm_admin_system_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_user_id` int(20) NOT NULL COMMENT '操作人id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `types` int(4) NULL DEFAULT NULL COMMENT '模块 1企业首页 2应用管理 3员工和部门管理 4名片小程序管理 5角色权限管理 6审批流（合同/回款） 7审批流（办公） 8项目管理 9客户管理 10系统日志管理 11其他设置',
  `behavior` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '行为',
  `object` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作对象',
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作详情',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_admin_user
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_user`;
CREATE TABLE `72crm_admin_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '安全符',
  `img` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `realname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `num` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '员工编号',
  `mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `sex` int(1) NULL DEFAULT NULL COMMENT '0 未选择 1 男 2 女 ',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门',
  `post` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '岗位',
  `status` int(3) NULL DEFAULT 2 COMMENT '状态,0禁用,1正常,2未激活',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '直属上级ID',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后登录IP 注意兼容IPV6',
  `is_resources` int(5) NULL DEFAULT 0 COMMENT '是否所属人力资源 1是，0否',
  `job_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工号',
  `rank` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职级',
  `condition` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '在职状态',
  `area` int(11) NULL DEFAULT NULL COMMENT '区域',
  `work_id` int(11) NULL DEFAULT NULL COMMENT '人力资源所属项目ID',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `parent_id`(`parent_id`) USING BTREE,
  INDEX `dept_id`(`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16412 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_user
-- ----------------------------
INSERT INTO `72crm_admin_user` VALUES (16360, '18888888888', 'eb8ce79be940ea15e2bb6dd58af648f1', '8b2ae9467c1643109e32eb6fd2c84ceb', NULL, '2020-03-12 15:48:16', 'admin', NULL, '18888888888', NULL, 1, 16671, '标准岗位', 1, NULL, '2021-01-07 17:17:57', '192.168.1.6', 1, '141', '14', '2', 16671, 69636);
INSERT INTO `72crm_admin_user` VALUES (16408, '18339646181', 'd6aaa4883889258d21002e811038d429', '915d77a98d8448b6a5ec20815afd607a', NULL, '2020-12-11 15:19:32', 'zyy', '961928214021669', '18339646181', NULL, NULL, 16671, NULL, 1, 16360, '2021-01-22 10:08:03', '127.0.0.1', 0, NULL, NULL, NULL, 16671, NULL);

-- ----------------------------
-- Table structure for 72crm_admin_user_config
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_user_config`;
CREATE TABLE `72crm_admin_user_config`  (
  `setting_id` int(9) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '状态，0:不启用 1 ： 启用',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设置名称',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '值',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`setting_id`) USING BTREE,
  INDEX `name`(`name`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 121318 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_user_config
-- ----------------------------
INSERT INTO `72crm_admin_user_config` VALUES (121086, 16360, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121087, 16360, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121088, 16360, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121089, 16360, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121090, 16360, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121091, 16363, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121092, 16363, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121093, 16363, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121094, 16363, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121095, 16363, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121096, 16364, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121097, 16364, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121098, 16364, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121099, 16364, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121100, 16362, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121101, 16364, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121102, 16365, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121103, 16365, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121104, 16365, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121105, 16365, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121106, 16365, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121107, 16366, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121108, 16366, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121109, 16366, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121110, 16366, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121111, 16366, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121112, 16367, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121113, 16367, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121114, 16367, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121115, 16367, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121116, 16368, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121117, 16368, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121118, 16368, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121119, 16368, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121120, 16369, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121121, 16369, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121122, 16369, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121123, 16369, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121124, 16370, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121125, 16370, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121126, 16370, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121127, 16370, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121128, 16371, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121129, 16371, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121130, 16371, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121131, 16371, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121132, 16371, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121133, 16372, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121134, 16372, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121135, 16372, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121136, 16372, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121137, 16372, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121138, 16373, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121139, 16373, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121140, 16373, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121141, 16373, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121142, 16373, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121143, 16374, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121144, 16374, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121145, 16374, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121146, 16374, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121147, 16374, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121148, 16362, 1, 'ActivityPhrase', '456464', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121149, 16375, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121150, 16375, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121151, 16375, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121152, 16375, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121153, 16375, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121154, 16376, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121155, 16376, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121156, 16376, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121157, 16376, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121158, 16376, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121159, 16377, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121160, 16377, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121161, 16377, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121162, 16377, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121163, 16377, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121164, 16378, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121165, 16378, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121166, 16378, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121167, 16378, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121168, 16378, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121169, 16379, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121170, 16379, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121171, 16379, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121172, 16379, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121173, 16379, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121174, 16380, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121175, 16380, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121176, 16380, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121177, 16380, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121178, 16380, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121179, 16381, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121180, 16381, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121181, 16381, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121182, 16381, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121183, 16381, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121184, 16382, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121185, 16382, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121186, 16382, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121187, 16382, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121188, 16383, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121189, 16383, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121190, 16383, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121191, 16383, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121192, 16383, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121193, 16384, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121194, 16384, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121195, 16384, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121196, 16384, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121197, 16385, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121198, 16385, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121199, 16385, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121200, 16385, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121201, 16386, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121202, 16386, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121203, 16386, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121204, 16386, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121205, 16384, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121206, 16387, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121207, 16387, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121208, 16387, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121209, 16387, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121210, 16387, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121211, 16388, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121212, 16388, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121213, 16388, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121214, 16388, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121215, 16388, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121216, 16389, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121217, 16389, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121218, 16389, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121219, 16389, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121220, 16389, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121221, 16390, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121222, 16390, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121223, 16390, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121224, 16390, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121225, 16391, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121226, 16391, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121227, 16391, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121228, 16391, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121229, 16392, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121230, 16392, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121231, 16392, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121232, 16392, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121233, 16393, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121234, 16393, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121235, 16393, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121236, 16393, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121237, 16394, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121238, 16394, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121239, 16394, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121240, 16394, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121241, 16395, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121242, 16395, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121243, 16395, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121244, 16395, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121245, 16396, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121246, 16396, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121247, 16396, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121248, 16396, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121249, 16397, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121250, 16397, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121251, 16397, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121252, 16397, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121253, 16398, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121254, 16398, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121255, 16398, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121256, 16398, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121257, 16399, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121258, 16399, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121259, 16399, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121260, 16399, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121261, 16400, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121262, 16400, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121263, 16400, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121264, 16400, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121265, 16401, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121266, 16401, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121267, 16401, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121268, 16401, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121269, 16402, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121270, 16402, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121271, 16402, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121272, 16402, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121273, 16403, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121274, 16403, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121275, 16403, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121276, 16403, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121277, 16404, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121278, 16404, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121279, 16404, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121280, 16404, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121281, 16405, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121282, 16405, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121283, 16405, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121284, 16405, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121285, 16406, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121286, 16406, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121287, 16406, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121288, 16406, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121289, 16407, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121290, 16407, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121291, 16407, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121292, 16407, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121293, 16408, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121294, 16408, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121295, 16408, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121296, 16408, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121297, 16408, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121298, 16392, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121299, 16409, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121300, 16409, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121301, 16409, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121302, 16409, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121303, 16394, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121304, 16404, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121305, 16405, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121306, 16399, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121307, 16398, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121308, 16407, 1, 'readNotice', NULL, '升级日志阅读状态');
INSERT INTO `72crm_admin_user_config` VALUES (121309, 16410, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121310, 16410, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121311, 16410, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121312, 16410, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121313, 16411, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121314, 16411, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121315, 16411, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121316, 16411, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `72crm_admin_user_config` VALUES (121317, 16411, 1, 'readNotice', NULL, '升级日志阅读状态');

-- ----------------------------
-- Table structure for 72crm_admin_user_his_table
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_user_his_table`;
CREATE TABLE `72crm_admin_user_his_table`  (
  `his_table_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `his_table` int(1) NULL DEFAULT NULL COMMENT '0 没有 1 有',
  `company_id` bigint(20) NULL DEFAULT NULL,
  `type` int(1) NULL DEFAULT 1 COMMENT '1.坐席授权 2.设置默认名片 3.关联员工',
  PRIMARY KEY (`his_table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户应用配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_admin_user_role
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_user_role`;
CREATE TABLE `72crm_admin_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22524 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色对应关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_admin_user_role
-- ----------------------------
INSERT INTO `72crm_admin_user_role` VALUES (22025, 16362, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22026, 16363, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22027, 16364, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22032, 16365, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22033, 16365, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22034, 16366, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22035, 16367, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22036, 16368, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22037, 16369, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22038, 16370, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22043, 16363, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22050, 16374, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22054, 16376, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22055, 16377, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22060, 16379, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22061, 16379, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22072, 16372, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22075, 16375, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22076, 16381, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22077, 16381, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22082, 16380, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22086, 16382, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22087, 16378, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22088, 16373, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22089, 16373, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22093, 16371, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22094, 16383, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22095, 16384, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22096, 16386, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22097, 16387, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22200, 16388, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22202, 16390, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22203, 16391, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22204, 16392, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22205, 16393, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22206, 16394, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22207, 16395, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22208, 16396, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22209, 16397, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22210, 16398, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22211, 16399, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22212, 16400, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22213, 16401, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22214, 16402, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22215, 16403, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22216, 16404, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22217, 16405, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22218, 16406, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22219, 16407, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22220, 16408, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22221, 16409, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22222, 16410, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22223, 16411, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22225, 16388, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22227, 16390, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22228, 16391, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22229, 16392, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22230, 16393, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22231, 16394, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22232, 16395, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22233, 16396, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22234, 16397, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22235, 16398, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22236, 16399, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22237, 16400, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22238, 16401, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22239, 16402, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22240, 16403, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22241, 16404, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22242, 16405, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22243, 16406, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22244, 16407, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22245, 16408, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22246, 16409, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22247, 16410, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22248, 16411, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22250, 16388, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22252, 16390, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22253, 16391, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22254, 16392, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22255, 16393, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22256, 16394, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22257, 16395, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22258, 16396, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22259, 16397, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22260, 16398, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22261, 16399, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22262, 16400, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22263, 16401, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22264, 16402, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22265, 16403, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22266, 16404, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22267, 16405, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22268, 16406, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22269, 16407, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22270, 16408, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22271, 16409, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22272, 16410, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22273, 16411, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22275, 16388, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22277, 16390, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22278, 16391, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22279, 16392, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22280, 16393, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22281, 16394, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22282, 16395, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22283, 16396, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22284, 16397, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22285, 16398, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22286, 16399, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22287, 16400, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22288, 16401, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22289, 16402, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22290, 16403, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22291, 16404, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22292, 16405, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22293, 16406, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22294, 16407, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22295, 16408, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22296, 16409, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22297, 16410, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22298, 16411, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22300, 16388, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22302, 16390, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22303, 16391, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22304, 16392, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22305, 16393, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22306, 16394, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22307, 16395, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22308, 16396, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22309, 16397, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22310, 16398, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22311, 16399, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22312, 16400, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22313, 16401, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22314, 16402, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22315, 16403, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22316, 16404, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22317, 16405, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22318, 16406, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22319, 16407, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22320, 16408, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22321, 16409, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22322, 16410, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22323, 16411, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22325, 16388, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22327, 16390, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22328, 16391, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22329, 16392, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22330, 16393, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22331, 16394, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22332, 16395, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22333, 16396, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22334, 16397, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22335, 16398, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22336, 16399, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22337, 16400, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22338, 16401, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22339, 16402, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22340, 16403, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22341, 16404, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22342, 16405, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22343, 16406, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22344, 16407, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22345, 16408, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22346, 16409, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22347, 16410, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22348, 16411, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22350, 16388, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22352, 16390, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22353, 16391, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22354, 16392, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22355, 16393, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22356, 16394, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22357, 16395, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22358, 16396, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22359, 16397, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22360, 16398, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22361, 16399, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22362, 16400, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22363, 16401, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22364, 16402, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22365, 16403, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22366, 16404, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22367, 16405, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22368, 16406, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22369, 16407, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22370, 16408, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22371, 16409, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22372, 16410, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22373, 16411, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22375, 16388, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22377, 16390, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22378, 16391, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22379, 16392, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22380, 16393, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22381, 16394, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22382, 16395, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22383, 16396, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22384, 16397, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22385, 16398, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22386, 16399, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22387, 16400, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22388, 16401, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22389, 16402, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22390, 16403, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22391, 16404, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22392, 16405, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22393, 16406, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22394, 16407, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22395, 16408, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22396, 16409, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22397, 16410, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22398, 16411, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22400, 16388, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22402, 16390, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22403, 16391, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22404, 16392, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22405, 16393, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22406, 16394, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22407, 16395, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22408, 16396, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22409, 16397, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22410, 16398, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22411, 16399, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22412, 16400, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22413, 16401, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22414, 16402, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22415, 16403, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22416, 16404, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22417, 16405, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22418, 16406, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22420, 16408, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22421, 16409, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22422, 16410, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22423, 16411, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22425, 16388, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22427, 16390, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22428, 16391, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22429, 16392, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22430, 16393, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22431, 16394, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22432, 16395, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22433, 16396, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22434, 16397, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22435, 16398, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22436, 16399, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22437, 16400, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22438, 16401, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22439, 16402, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22440, 16403, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22441, 16404, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22442, 16405, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22443, 16406, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22445, 16408, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22446, 16409, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22447, 16410, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22448, 16411, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22450, 16388, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22452, 16390, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22453, 16391, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22454, 16392, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22455, 16393, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22456, 16394, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22457, 16395, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22458, 16396, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22459, 16397, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22460, 16398, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22461, 16399, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22462, 16400, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22463, 16401, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22464, 16402, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22465, 16403, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22466, 16404, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22467, 16405, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22468, 16406, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22469, 16407, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22470, 16408, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22471, 16409, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22472, 16410, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22473, 16411, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22474, 16407, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22475, 16407, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22495, 16360, 198339);
INSERT INTO `72crm_admin_user_role` VALUES (22496, 16360, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22497, 16360, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22498, 16360, 198325);
INSERT INTO `72crm_admin_user_role` VALUES (22499, 16360, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22500, 16360, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22501, 16360, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22502, 16360, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22503, 16360, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22504, 16360, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22505, 16360, 198332);
INSERT INTO `72crm_admin_user_role` VALUES (22515, 16389, 198327);
INSERT INTO `72crm_admin_user_role` VALUES (22516, 16389, 198326);
INSERT INTO `72crm_admin_user_role` VALUES (22517, 16389, 198340);
INSERT INTO `72crm_admin_user_role` VALUES (22518, 16389, 198324);
INSERT INTO `72crm_admin_user_role` VALUES (22519, 16389, 198331);
INSERT INTO `72crm_admin_user_role` VALUES (22520, 16389, 198330);
INSERT INTO `72crm_admin_user_role` VALUES (22521, 16389, 198329);
INSERT INTO `72crm_admin_user_role` VALUES (22522, 16389, 198328);
INSERT INTO `72crm_admin_user_role` VALUES (22523, 16389, 198332);

-- ----------------------------
-- Table structure for 72crm_admin_visiting_card
-- ----------------------------
DROP TABLE IF EXISTS `72crm_admin_visiting_card`;
CREATE TABLE `72crm_admin_visiting_card`  (
  `card_id` int(11) NOT NULL AUTO_INCREMENT,
  `card_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名片名称',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `user_id` bigint(11) NULL DEFAULT NULL COMMENT '关联员工id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `openid` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `wechat_number` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信号',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网址',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '简介',
  `weixin_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信小程序码',
  `official_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '海报名片',
  PRIMARY KEY (`card_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '名片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_call_record
-- ----------------------------
DROP TABLE IF EXISTS `72crm_call_record`;
CREATE TABLE `72crm_call_record`  (
  `call_record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 记录id',
  `number` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电话号码',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始振铃时间',
  `answer_time` datetime(0) NULL DEFAULT NULL COMMENT '接通时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `talk_time` int(10) NULL DEFAULT 0 COMMENT '通话时长（秒）',
  `dial_time` int(10) NULL DEFAULT 0 COMMENT '摘机时长',
  `state` int(2) NULL DEFAULT NULL COMMENT '通话状态 (0未振铃，1未接通，2接通，3呼入未接通)',
  `type` int(2) NULL DEFAULT NULL COMMENT '通话类型 (0呼出，1呼入)',
  `model` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联模块 leads，customer，contacts',
  `model_id` int(11) NULL DEFAULT NULL COMMENT '关联模块ID',
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '录音文件路径',
  `size` int(10) NULL DEFAULT 0 COMMENT '录音文件大小',
  `file_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `call_upload` tinyint(1) NULL DEFAULT 0 COMMENT '0：CRM服务器; 1：上传至阿里云',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次',
  PRIMARY KEY (`call_record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通话记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_card_weixin_browse
-- ----------------------------
DROP TABLE IF EXISTS `72crm_card_weixin_browse`;
CREATE TABLE `72crm_card_weixin_browse`  (
  `browse_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '名片表',
  `weixin_leads_id` bigint(20) NULL DEFAULT NULL COMMENT '微信线索表',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `num` int(11) NULL DEFAULT 0 COMMENT '浏览次数',
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`browse_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信浏览名片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_card_weixin_leads
-- ----------------------------
DROP TABLE IF EXISTS `72crm_card_weixin_leads`;
CREATE TABLE `72crm_card_weixin_leads`  (
  `weixin_leads_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_transform` int(1) NULL DEFAULT 0 COMMENT '1已转化 0 未转化',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `weixin_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信头像',
  `weixin_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信名称',
  `weixin_number` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信号',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `openid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信唯一标识',
  `sex` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`weixin_leads_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信线索' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_card_weixin_leads_user
-- ----------------------------
DROP TABLE IF EXISTS `72crm_card_weixin_leads_user`;
CREATE TABLE `72crm_card_weixin_leads_user`  (
  `weixin_user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '被关注的员工id',
  `weixin_leads_id` bigint(20) NULL DEFAULT NULL COMMENT '微信线索id',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `relevance_user_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  PRIMARY KEY (`weixin_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '名片夹' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_achievement
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_achievement`;
CREATE TABLE `72crm_crm_achievement`  (
  `achievement_id` int(11) NOT NULL AUTO_INCREMENT,
  `obj_id` int(11) NULL DEFAULT NULL COMMENT '对象ID',
  `type` int(2) NULL DEFAULT 0 COMMENT '1公司2部门3员工',
  `year` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年',
  `january` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '一月',
  `february` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '二月',
  `march` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '三月',
  `april` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '四月',
  `may` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '五月',
  `june` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '六月',
  `july` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '七月',
  `august` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '八月',
  `september` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '九月',
  `october` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '十月',
  `november` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '十一月',
  `december` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '十二月',
  `status` int(2) NULL DEFAULT NULL COMMENT '1销售（目标）2回款（目标）',
  `yeartarget` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '年目标',
  PRIMARY KEY (`achievement_id`) USING BTREE,
  INDEX `obj_id`(`obj_id`, `type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业绩目标' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_action_record
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_action_record`;
CREATE TABLE `72crm_crm_action_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_user_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `ip_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `types` int(4) NOT NULL COMMENT '模块类型',
  `action_id` int(11) NULL DEFAULT NULL COMMENT '被操作对象ID',
  `object` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对象',
  `behavior` int(4) NULL DEFAULT NULL COMMENT '行为',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `detail` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '详情',
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '公司ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `types`(`types`, `action_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 411 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字段操作记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_action_record
-- ----------------------------
INSERT INTO `72crm_crm_action_record` VALUES (1, 16362, '2020-10-29 10:07:48', '192.168.1.6', 7, 1, '012', 3, '', '删除了回款：012', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (2, 16362, '2020-10-29 10:07:55', '192.168.1.6', 5, 1, '1414', 3, '', '删除了商机：1414', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (3, 16362, '2020-10-29 10:08:03', '192.168.1.6', 2, 4, '测试客户', 3, '', '删除了客户：测试客户', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (4, 16362, '2020-10-29 10:08:03', '192.168.1.6', 2, 3, '41', 3, '', '删除了客户：41', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (5, 16362, '2020-10-29 10:08:03', '192.168.1.6', 2, 2, '14', 3, '', '删除了客户：14', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (6, 16362, '2020-10-29 10:08:03', '192.168.1.6', 2, 2, '1414', 3, '', '删除了客户：1414', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (12, 16362, '2020-10-29 10:08:16', '192.168.1.6', 6, 3, 'sds', 3, '', '删除了合同：sds', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (13, 16362, '2020-10-29 10:08:16', '192.168.1.6', 6, 2, '123213', 3, '', '删除了合同：123213', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (16, 16362, '2020-10-29 10:08:16', '192.168.1.6', 6, 1, '', 3, '', '删除了合同：null', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (17, 16362, '2020-10-29 10:08:16', '192.168.1.6', 6, 1, '', 3, '', '删除了合同：null', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (18, 16362, '2020-10-29 10:08:16', '192.168.1.6', 6, 1, '', 3, '', '删除了合同：null', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (19, 16362, '2020-10-29 10:08:16', '192.168.1.6', 6, 1, '', 3, '', '删除了合同：null', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (20, 16362, '2020-10-29 10:08:16', '192.168.1.6', 6, 3, 'sds', 3, '', '删除了合同：sds', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (21, 16362, '2020-11-06 10:41:15', '127.0.0.1', 11, 1, '2020-11-06', 1, '', '新建了日志：2020-11-06', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (25, 16362, '2020-11-06 14:42:20', '127.0.0.1', 11, 2, '2020-11-06', 1, '', '新建了日志：2020-11-06', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (26, 16360, '2020-11-07 09:57:40', '192.168.1.6', 12, 1, '14', 1, '', '新建了办公审批：14', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (28, 16360, '2020-11-07 09:58:02', '192.168.1.6', 6, 90, '141', 20, '', '提交了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (30, 16360, '2020-11-07 09:59:14', '192.168.1.6', 6, 91, '14', 20, '', '提交了合同：14', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (32, 16360, '2020-11-07 09:59:38', '192.168.1.6', 6, 92, '141111111', 20, '', '提交了合同：141111111', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (34, 16360, '2020-11-07 10:00:48', '192.168.1.6', 6, 93, '141dsdsd', 20, '', '提交了合同：141dsdsd', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (36, 16360, '2020-11-07 10:02:45', '192.168.1.6', 6, 94, '11111111111111111', 20, '', '提交了合同：11111111111111111', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (37, 16360, '2020-11-07 10:02:59', '192.168.1.6', 6, 94, '11111111111111111', 27, '', '通过了合同：11111111111111111', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (39, 16360, '2020-11-07 10:03:24', '192.168.1.6', 7, 95, '141', 20, '', '提交了回款：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (40, 16360, '2020-11-07 10:24:56', '192.168.1.6', 6, 93, '141dsdsd', 29, '', '撤回了合同：141dsdsd', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (41, 16362, '2020-11-09 10:09:57', '127.0.0.1', 11, 4, '2020-11-09', 1, '', '新建了日志：2020-11-09', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (43, 16360, '2020-11-09 10:16:00', '192.168.1.28', 6, 97, '1414', 20, '', '提交了合同：1414', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (51, 16362, '2020-11-11 10:05:55', '192.168.1.28', 7, 2, '141', 3, '', '删除了回款：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (52, 16362, '2020-11-11 10:06:02', '192.168.1.28', 5, 3, '963', 3, '', '删除了商机：963', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (53, 16362, '2020-11-11 10:06:02', '192.168.1.28', 5, 2, '141', 3, '', '删除了商机：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (54, 16362, '2020-11-11 10:06:02', '192.168.1.28', 5, 2, '141', 3, '', '删除了商机：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (73, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 10, '1414', 3, '', '删除了合同：1414', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (74, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 9, '14177', 3, '', '删除了合同：14177', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (80, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 10, '1414', 3, '', '删除了合同：1414', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (87, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 10, '1414', 3, '', '删除了合同：1414', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (88, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 9, '14177', 3, '', '删除了合同：14177', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (89, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 8, '11111111111111111', 3, '', '删除了合同：11111111111111111', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (90, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 7, '141dsdsd', 3, '', '删除了合同：141dsdsd', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (94, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 10, '1414', 3, '', '删除了合同：1414', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (95, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 9, '14177', 3, '', '删除了合同：14177', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (96, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 5, '141', 3, '', '删除了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (97, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 5, '141', 3, '', '删除了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (98, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 5, '141', 3, '', '删除了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (99, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 5, '141', 3, '', '删除了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (100, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 5, '141', 3, '', '删除了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (101, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 5, '141', 3, '', '删除了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (102, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 5, '141', 3, '', '删除了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (103, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 5, '141', 3, '', '删除了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (104, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 5, '141', 3, '', '删除了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (105, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 5, '141', 3, '', '删除了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (106, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 5, '141', 3, '', '删除了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (107, 16362, '2020-11-11 10:06:14', '192.168.1.28', 6, 5, '141', 3, '', '删除了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (108, 16362, '2020-11-11 10:06:47', '192.168.1.28', 2, 7, '14141', 3, '', '删除了客户：14141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (109, 16362, '2020-11-11 10:06:47', '192.168.1.28', 2, 6, '14141', 3, '', '删除了客户：14141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (110, 16362, '2020-11-11 10:06:47', '192.168.1.28', 2, 5, '444', 3, '', '删除了客户：444', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (112, 16360, '2020-11-11 11:04:33', '192.168.1.28', 6, 12, '141', 1, '[\"新建了合同\"]', '新建了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (113, 16360, '2020-11-11 11:04:33', '192.168.1.28', 6, 108, '141', 20, '', '提交了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (114, 16360, '2020-11-11 11:04:42', '192.168.1.28', 6, 108, '141', 27, '', '通过了合同：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (115, 16360, '2020-11-11 11:05:00', '192.168.1.28', 7, 3, '141', 1, '[\"新建了回款\"]', '新建了回款：141', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (183, 16362, '2020-11-23 17:40:36', '127.0.0.1', 1, 70, '测试', 1, '[\"新建了线索\"]', '新建了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (184, 16362, '2020-11-23 17:40:50', '127.0.0.1', 1, 19, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (185, 16362, '2020-11-23 17:40:50', '127.0.0.1', 1, 20, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (186, 16362, '2020-11-23 17:40:50', '127.0.0.1', 1, 24, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (187, 16362, '2020-11-23 17:40:50', '127.0.0.1', 1, 24, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (188, 16362, '2020-11-23 17:40:50', '127.0.0.1', 1, 24, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (189, 16362, '2020-11-23 17:40:50', '127.0.0.1', 1, 24, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (190, 16362, '2020-11-23 17:40:50', '127.0.0.1', 1, 24, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (191, 16362, '2020-11-23 17:40:50', '127.0.0.1', 1, 28, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (192, 16362, '2020-11-23 17:40:50', '127.0.0.1', 1, 29, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (193, 16362, '2020-11-23 17:40:50', '127.0.0.1', 1, 29, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (194, 16362, '2020-11-23 17:40:50', '127.0.0.1', 1, 29, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (195, 16362, '2020-11-23 17:40:50', '127.0.0.1', 1, 29, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (196, 16362, '2020-11-23 17:40:50', '127.0.0.1', 1, 70, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (197, 16362, '2020-11-23 17:40:50', '127.0.0.1', 1, 70, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (198, 16362, '2020-11-23 17:40:50', '127.0.0.1', 1, 70, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (199, 16362, '2020-11-23 17:40:53', '127.0.0.1', 1, 33, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (200, 16362, '2020-11-23 17:40:53', '127.0.0.1', 1, 33, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (201, 16362, '2020-11-23 17:40:53', '127.0.0.1', 1, 44, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (202, 16362, '2020-11-23 17:40:53', '127.0.0.1', 1, 44, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (203, 16362, '2020-11-23 17:40:53', '127.0.0.1', 1, 44, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (204, 16362, '2020-11-23 17:40:53', '127.0.0.1', 1, 44, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (205, 16362, '2020-11-23 17:40:53', '127.0.0.1', 1, 44, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (206, 16362, '2020-11-23 17:40:53', '127.0.0.1', 1, 69, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (207, 16362, '2020-11-23 17:40:53', '127.0.0.1', 1, 69, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (208, 16362, '2020-11-23 17:40:53', '127.0.0.1', 1, 69, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (209, 16362, '2020-11-23 17:40:53', '127.0.0.1', 1, 69, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (210, 16362, '2020-11-23 17:40:53', '127.0.0.1', 1, 69, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (211, 16362, '2020-11-23 17:40:53', '127.0.0.1', 1, 69, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (212, 16362, '2020-11-23 17:40:53', '127.0.0.1', 1, 69, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (213, 16362, '2020-11-23 17:40:53', '127.0.0.1', 1, 69, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (214, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 45, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (215, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 46, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (216, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 55, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (217, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 55, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (218, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 55, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (219, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 55, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (220, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 55, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (221, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 62, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (222, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 62, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (223, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 62, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (224, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 62, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (225, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 62, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (231, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 13, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (232, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 13, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (233, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 13, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (234, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 13, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (235, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 13, '测试', 3, '', '删除了线索：测试', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (236, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 2, '测试线索', 3, '', '删除了线索：测试线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (237, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 2, '测试线索', 3, '', '删除了线索：测试线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (238, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 2, '测试线索', 3, '', '删除了线索：测试线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (239, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 2, '测试线索', 3, '', '删除了线索：测试线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (240, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 2, '测试线索', 3, '', '删除了线索：测试线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (241, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 2, '测试线索', 3, '', '删除了线索：测试线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (242, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 2, '测试线索', 3, '', '删除了线索：测试线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (243, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 2, '测试线索', 3, '', '删除了线索：测试线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (244, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 2, '测试线索', 3, '', '删除了线索：测试线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (245, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 2, '测试线索', 3, '', '删除了线索：测试线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (246, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 2, '测试线索', 3, '', '删除了线索：测试线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (247, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 2, '测试线索', 3, '', '删除了线索：测试线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (248, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 2, '测试线索', 3, '', '删除了线索：测试线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (249, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 2, '测试线索', 3, '', '删除了线索：测试线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (250, 16362, '2020-11-23 17:40:59', '127.0.0.1', 1, 2, '测试线索', 3, '', '删除了线索：测试线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (251, 16360, '2020-11-25 16:31:39', '192.168.1.14', 4, 3, '说的1', 1, '[\"新建了产品\"]', '新建了产品：说的1', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (252, 16360, '2020-11-25 16:31:39', '192.168.1.14', 4, 4, '说的2', 1, '[\"新建了产品\"]', '新建了产品：说的2', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (253, 16360, '2020-11-25 16:31:39', '192.168.1.14', 4, 5, '说的3', 1, '[\"新建了产品\"]', '新建了产品：说的3', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (254, 16360, '2020-11-25 16:31:39', '192.168.1.14', 4, 6, '说的4', 1, '[\"新建了产品\"]', '新建了产品：说的4', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (255, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 7, '说的5', 1, '[\"新建了产品\"]', '新建了产品：说的5', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (256, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 8, '说的6', 1, '[\"新建了产品\"]', '新建了产品：说的6', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (257, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 9, '说的7', 1, '[\"新建了产品\"]', '新建了产品：说的7', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (258, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 10, '说的8', 1, '[\"新建了产品\"]', '新建了产品：说的8', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (259, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 11, '说的9', 1, '[\"新建了产品\"]', '新建了产品：说的9', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (260, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 12, '说的10', 1, '[\"新建了产品\"]', '新建了产品：说的10', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (261, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 13, '说的11', 1, '[\"新建了产品\"]', '新建了产品：说的11', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (262, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 14, '说的12', 1, '[\"新建了产品\"]', '新建了产品：说的12', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (263, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 15, '说的13', 1, '[\"新建了产品\"]', '新建了产品：说的13', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (264, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 16, '说的14', 1, '[\"新建了产品\"]', '新建了产品：说的14', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (265, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 17, '说的15', 1, '[\"新建了产品\"]', '新建了产品：说的15', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (266, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 18, '说的16', 1, '[\"新建了产品\"]', '新建了产品：说的16', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (267, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 19, '说的17', 1, '[\"新建了产品\"]', '新建了产品：说的17', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (268, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 20, '说的18', 1, '[\"新建了产品\"]', '新建了产品：说的18', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (269, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 21, '说的19', 1, '[\"新建了产品\"]', '新建了产品：说的19', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (270, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 22, '说的20', 1, '[\"新建了产品\"]', '新建了产品：说的20', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (271, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 23, '说的21', 1, '[\"新建了产品\"]', '新建了产品：说的21', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (272, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 24, '说的22', 1, '[\"新建了产品\"]', '新建了产品：说的22', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (273, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 25, '说的23', 1, '[\"新建了产品\"]', '新建了产品：说的23', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (274, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 26, '说的24', 1, '[\"新建了产品\"]', '新建了产品：说的24', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (275, 16360, '2020-11-25 16:31:40', '192.168.1.14', 4, 27, '说的25', 1, '[\"新建了产品\"]', '新建了产品：说的25', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (277, 16362, '2020-11-27 13:37:57', '192.168.1.14', 3, 4, '吧', 1, '[\"新建了联系人\"]', '新建了联系人：吧', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (281, 16362, '2020-11-27 13:38:46', '192.168.1.14', 3, 2, 'a', 3, '', '删除了联系人：a', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (282, 16362, '2020-11-27 13:38:46', '192.168.1.14', 3, 5, '', 3, '', '删除了联系人：null', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (283, 16362, '2020-11-27 13:38:46', '192.168.1.14', 3, 2, 'A', 3, '', '删除了联系人：A', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (284, 16362, '2020-11-27 13:38:46', '192.168.1.14', 3, 2, 'a', 3, '', '删除了联系人：a', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (285, 16362, '2020-11-27 13:38:46', '192.168.1.14', 3, 5, '', 3, '', '删除了联系人：null', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (286, 16362, '2020-11-27 13:38:46', '192.168.1.14', 3, 2, 'a', 3, '', '删除了联系人：a', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (287, 16362, '2020-11-27 13:38:46', '192.168.1.14', 3, 2, 'a', 3, '', '删除了联系人：a', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (289, 16362, '2020-11-27 13:38:51', '192.168.1.14', 3, 7, '吧', 1, '[\"新建了联系人\"]', '新建了联系人：吧', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (294, 16362, '2020-11-27 13:39:22', '192.168.1.14', 3, 8, '', 3, '', '删除了联系人：null', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (296, 16362, '2020-11-27 13:39:22', '192.168.1.14', 3, 3, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (297, 16362, '2020-11-27 13:39:22', '192.168.1.14', 3, 8, '', 3, '', '删除了联系人：null', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (298, 16362, '2020-11-27 13:39:22', '192.168.1.14', 3, 3, '', 3, '', '删除了联系人：null', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (299, 16362, '2020-11-27 13:39:22', '192.168.1.14', 3, 3, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (301, 16362, '2020-11-27 13:39:46', '192.168.1.14', 3, 10, '吧', 1, '[\"新建了联系人\"]', '新建了联系人：吧', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (302, 16362, '2020-11-27 13:39:46', '192.168.1.14', 3, 11, 'A', 1, '[\"新建了联系人\"]', '新建了联系人：A', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (306, 16362, '2020-11-27 13:40:30', '192.168.1.14', 3, 6, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (307, 16362, '2020-11-27 13:40:30', '192.168.1.14', 3, 6, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (308, 16362, '2020-11-27 13:40:30', '192.168.1.14', 3, 6, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (309, 16362, '2020-11-27 13:40:30', '192.168.1.14', 3, 11, '', 3, '', '删除了联系人：null', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (310, 16362, '2020-11-27 13:40:30', '192.168.1.14', 3, 6, '', 3, '', '删除了联系人：null', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (311, 16362, '2020-11-27 13:41:41', '192.168.1.14', 3, 9, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (312, 16362, '2020-11-27 13:41:41', '192.168.1.14', 3, 9, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (313, 16362, '2020-11-27 13:41:41', '192.168.1.14', 3, 9, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (314, 16362, '2020-11-27 13:41:41', '192.168.1.14', 3, 9, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (315, 16362, '2020-11-27 13:41:41', '192.168.1.14', 3, 9, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (316, 16362, '2020-11-27 13:41:41', '192.168.1.14', 3, 9, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (317, 16362, '2020-11-27 13:41:41', '192.168.1.14', 3, 9, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (318, 16362, '2020-11-27 13:41:41', '192.168.1.14', 3, 9, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (319, 16362, '2020-11-27 13:41:41', '192.168.1.14', 3, 9, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (321, 16362, '2020-11-27 13:43:41', '192.168.1.14', 3, 16, '吧', 1, '[\"新建了联系人\"]', '新建了联系人：吧', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (322, 16362, '2020-11-27 13:43:41', '192.168.1.14', 3, 17, 'A', 1, '[\"新建了联系人\"]', '新建了联系人：A', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (326, 16362, '2020-11-27 13:44:17', '192.168.1.14', 3, 12, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (327, 16362, '2020-11-27 13:44:17', '192.168.1.14', 3, 12, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (328, 16362, '2020-11-27 13:44:17', '192.168.1.14', 3, 12, 'zyy', 3, '', '删除了联系人：zyy', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (329, 16362, '2020-11-27 13:44:17', '192.168.1.14', 3, 15, '', 3, '', '删除了联系人：null', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (330, 16362, '2020-11-27 13:44:17', '192.168.1.14', 3, 12, '', 3, '', '删除了联系人：null', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (331, 16384, '2020-12-01 17:39:12', '103.85.165.10', 1, 2, 'OP-LJJ-01', 1, '[\"新建了线索\"]', '新建了线索：OP-LJJ-01', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (332, 16384, '2020-12-01 17:39:28', '103.85.165.10', 1, 3, 'OP-LJJ-02', 1, '[\"新建了线索\"]', '新建了线索：OP-LJJ-02', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (333, 16384, '2020-12-01 17:41:09', '103.85.165.10', 2, 13, 'TEST-01', 1, '[\"新建了客户\"]', '新建了客户：TEST-01', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (334, 16384, '2020-12-01 17:41:23', '103.85.165.10', 3, 17, 'Test-02', 1, '[\"新建了联系人\"]', '新建了联系人：Test-02', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (335, 16384, '2020-12-01 17:43:06', '103.85.165.10', 4, 30, '人行', 1, '[\"新建了产品\"]', '新建了产品：人行', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (337, 16398, '2020-12-16 17:37:02', '45.251.20.210', 4, 31, '侦查机ZK4100', 1, '[\"新建了产品\"]', '新建了产品：侦查机ZK4100', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (342, 16407, '2020-12-17 10:59:49', '103.85.165.10', 3, 19, '杨兆悦', 1, '[\"新建了联系人\"]', '新建了联系人：杨兆悦', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (343, 16407, '2020-12-17 11:00:57', '103.85.165.10', 3, 19, '杨兆悦', 3, '', '删除了联系人：杨兆悦', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (349, 16389, '2020-12-17 11:08:06', '123.149.163.116', 2, 17, '1', 1, '[\"新建了客户\"]', '新建了客户：1', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (350, 16389, '2020-12-17 11:08:16', '123.149.163.116', 2, 17, '1', 3, '', '删除了客户：1', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (351, 16389, '2020-12-17 11:09:51', '123.149.163.116', 2, 18, '444', 1, '[\"新建了客户\"]', '新建了客户：444', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (352, 16389, '2020-12-17 11:10:08', '123.149.163.116', 2, 18, '444', 3, '', '删除了客户：444', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (353, 16389, '2020-12-17 11:25:57', '123.149.163.116', 3, 20, '11', 1, '[\"新建了联系人\"]', '新建了联系人：11', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (354, 16389, '2020-12-17 11:26:02', '123.149.163.116', 3, 20, '11', 3, '', '删除了联系人：11', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (358, 16360, '2020-12-17 14:38:55', '103.85.165.10', 6, 18, 'op2020121702003', 1, '[\"新建了合同\"]', '新建了合同：op2020121702003', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (359, 16360, '2020-12-17 14:46:34', '103.85.165.10', 7, 7, 'BH-2020-1', 1, '[\"新建了回款\"]', '新建了回款：BH-2020-1', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (360, 16360, '2020-12-17 14:46:34', '103.85.165.10', 7, 288, 'BH-2020-1', 20, '', '提交了回款：BH-2020-1', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (361, 16398, '2020-12-17 14:48:04', '103.85.165.10', 7, 288, 'BH-2020-1', 27, '', '通过了回款：BH-2020-1', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (362, 16360, '2020-12-17 14:50:18', '103.85.165.10', 7, 8, 'BH-2020-2', 1, '[\"新建了回款\"]', '新建了回款：BH-2020-2', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (363, 16360, '2020-12-17 14:50:18', '103.85.165.10', 7, 289, 'BH-2020-2', 20, '', '提交了回款：BH-2020-2', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (364, 16398, '2020-12-17 14:51:12', '103.85.165.10', 7, 289, 'BH-2020-2', 27, '', '通过了回款：BH-2020-2', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (365, 16360, '2020-12-17 15:00:33', '103.85.165.10', 18, 1, '202000123', 1, '[\"新建了发票\"]', '新建了发票：202000123', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (366, 16360, '2020-12-17 15:24:48', '103.85.165.10', 4, 32, '侦查机类', 1, '[\"新建了产品\"]', '新建了产品：侦查机类', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (367, 16360, '2020-12-17 15:35:47', '103.85.165.10', 4, 33, 'ZK4300(TS)', 1, '[\"新建了产品\"]', '新建了产品：ZK4300(TS)', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (368, 16360, '2020-12-17 15:36:03', '103.85.165.10', 4, 32, '侦查机ZK4100', 3, '', '删除了产品：侦查机ZK4100', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (369, 16360, '2020-12-17 15:36:03', '103.85.165.10', 4, 32, '侦查机类', 3, '', '删除了产品：侦查机类', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (370, 16360, '2020-12-17 15:37:25', '103.85.165.10', 4, 34, '侦查机 ZK4300（TP）', 1, '[\"新建了产品\"]', '新建了产品：侦查机 ZK4300（TP）', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (371, 16360, '2020-12-17 15:37:49', '103.85.165.10', 4, 33, 'ZK4300(TS)', 2, '[\"将产品名称 由ZK4300(TS)修改为展迹图 ZK4300(TS)。\"]', '将产品名称 由ZK4300(TS)修改为展迹图 ZK4300(TS)。', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (372, 16360, '2020-12-17 15:38:08', '103.85.165.10', 4, 34, '侦查机 ZK4300（TP）', 2, '[\"将产品名称 由侦查机 ZK4300（TP）修改为展迹图 ZK4300（TP）。\"]', '将产品名称 由侦查机 ZK4300（TP）修改为展迹图 ZK4300（TP）。', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (373, 16360, '2020-12-17 15:38:59', '103.85.165.10', 4, 35, '展迹图', 1, '[\"新建了产品\"]', '新建了产品：展迹图', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (374, 16360, '2020-12-17 15:39:23', '103.85.165.10', 4, 35, '展迹图', 2, '[\"将产品名称 由展迹图修改为展迹图 ZK4300（TU）。\"]', '将产品名称 由展迹图修改为展迹图 ZK4300（TU）。', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (375, 16360, '2020-12-17 15:40:24', '103.85.165.10', 4, 36, '分析服务类', 1, '[\"新建了产品\"]', '新建了产品：分析服务类', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (376, 16360, '2020-12-17 15:45:06', '103.85.165.10', 4, 37, '项目类', 1, '[\"新建了产品\"]', '新建了产品：项目类', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (377, 16360, '2020-12-17 15:45:30', '103.85.165.10', 4, 38, '培训类', 1, '[\"新建了产品\"]', '新建了产品：培训类', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (378, 16360, '2020-12-17 15:46:23', '103.85.165.10', 4, 39, '代理商合作协议', 1, '[\"新建了产品\"]', '新建了产品：代理商合作协议', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (379, 16360, '2020-12-17 15:49:07', '103.85.165.10', 6, 19, 'op202012160023', 1, '[\"新建了合同\"]', '新建了合同：op202012160023', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (380, 16398, '2020-12-17 16:15:42', '103.85.165.10', 10, 2, '新产品营销', 1, '', '新建了推广：新产品营销', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (391, 16392, '2021-01-05 18:40:29', '103.85.165.10', 1, 5, '华南数据分析服务项目线索', 1, '[\"新建了线索\"]', '新建了线索：华南数据分析服务项目线索', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (392, 16392, '2021-01-05 18:41:39', '103.85.165.10', 1, 5, '华南数据分析服务项目线索', 2, '[\"将人员 由空修改为16388,16389,16391,16392,16393,16394,16395,16396,16397,16398,16399,16400,16401,16402,16403,16404,16405,16406,16407,16408,16409,16410,16411。\"]', '将人员 由空修改为16388,16389,16391,16392,16393,16394,16395,16396,16397,16398,16399,16400,16401,16402,16403,16404,16405,16406,16407,16408,16409,16410,16411。', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (396, 16408, '2021-01-22 13:48:11', '127.0.0.1', 4, 34, '展迹图 ZK4300（TU）', 3, '', '删除了产品：展迹图 ZK4300（TU）', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (397, 16408, '2021-01-22 13:48:11', '127.0.0.1', 4, 33, '展迹图 ZK4300（TP）', 3, '', '删除了产品：展迹图 ZK4300（TP）', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (398, 16408, '2021-01-22 13:48:11', '127.0.0.1', 4, 33, '展迹图 ZK4300（TP）', 3, '', '删除了产品：展迹图 ZK4300（TP）', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (399, 16408, '2021-01-22 13:48:11', '127.0.0.1', 4, 33, '展迹图 ZK4300（TP）', 3, '', '删除了产品：展迹图 ZK4300（TP）', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (400, 16408, '2021-01-22 13:48:11', '127.0.0.1', 4, 33, '展迹图 ZK4300（TP）', 3, '', '删除了产品：展迹图 ZK4300（TP）', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (401, 16408, '2021-01-22 13:48:11', '127.0.0.1', 4, 33, '展迹图 ZK4300(TS)', 3, '', '删除了产品：展迹图 ZK4300(TS)', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (402, 16408, '2021-01-22 13:48:11', '127.0.0.1', 4, 33, '展迹图 ZK4300(TS)', 3, '', '删除了产品：展迹图 ZK4300(TS)', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (403, 16408, '2021-01-22 13:48:33', '127.0.0.1', 5, 8, '华南数据分析服务', 3, '', '删除了商机：华南数据分析服务', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (404, 16408, '2021-01-22 13:48:33', '127.0.0.1', 5, 9, '中科金审测试一', 3, '', '删除了商机：中科金审测试一', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (405, 16408, '2021-01-22 13:48:33', '127.0.0.1', 5, 9, '中科金审测试二', 3, '', '删除了商机：中科金审测试二', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (406, 16408, '2021-01-22 13:48:45', '127.0.0.1', 3, 18, '范艳', 3, '', '删除了联系人：范艳', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (407, 16408, '2021-01-22 13:48:45', '127.0.0.1', 3, 18, '范艳', 3, '', '删除了联系人：范艳', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (408, 16408, '2021-01-22 13:48:45', '127.0.0.1', 3, 18, '范艳', 3, '', '删除了联系人：范艳', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (409, 16408, '2021-01-22 13:48:45', '127.0.0.1', 3, 18, '范艳', 3, '', '删除了联系人：范艳', NULL);
INSERT INTO `72crm_crm_action_record` VALUES (410, 16408, '2021-01-22 13:48:50', '127.0.0.1', 2, 15, '华南数据分析服务项目线索', 3, '', '删除了客户：华南数据分析服务项目线索', NULL);

-- ----------------------------
-- Table structure for 72crm_crm_activity
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_activity`;
CREATE TABLE `72crm_crm_activity`  (
  `activity_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动id',
  `type` int(1) NULL DEFAULT NULL COMMENT '活动类型 1 跟进记录 2 创建记录 3 商机阶段变更 4 外勤签到',
  `category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟进类型',
  `activity_type` int(1) NOT NULL COMMENT '活动类型 1 线索 2 客户 3 联系人 4 产品 5 商机 6 合同 7回款 8日志 9审批 10日程 11任务 12 发邮件',
  `activity_type_id` int(11) NOT NULL COMMENT '活动类型Id',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动内容',
  `business_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联商机',
  `contacts_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联联系人',
  `next_time` datetime(0) NULL DEFAULT NULL COMMENT '下次联系时间',
  `status` int(2) NULL DEFAULT 1 COMMENT '0 删除 1 未删除',
  `lng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经度',
  `lat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签到地址',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人id',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次id',
  PRIMARY KEY (`activity_id`) USING BTREE,
  INDEX `72crm_crm_activity_type_activity_type_index`(`type`, `activity_type`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'crm活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_activity
-- ----------------------------
INSERT INTO `72crm_crm_activity` VALUES (73, 2, NULL, 2, 14, '创建了客户: ', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16360, '2020-12-10 15:36:33', NULL);
INSERT INTO `72crm_crm_activity` VALUES (74, 1, '打电话', 2, 14, '这是APP的客户 ', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16360, '2020-12-10 15:36:33', 'e4ef8d5292438f836f6608d990af4af6');
INSERT INTO `72crm_crm_activity` VALUES (75, 2, NULL, 2, 15, '创建了客户: ', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16398, '2021-01-22 13:48:50', NULL);
INSERT INTO `72crm_crm_activity` VALUES (76, 2, NULL, 2, 16, '创建了客户: ', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16360, '2021-01-22 13:48:50', NULL);
INSERT INTO `72crm_crm_activity` VALUES (77, 2, NULL, 3, 18, '创建了联系人: ', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16360, '2021-01-22 13:48:45', NULL);
INSERT INTO `72crm_crm_activity` VALUES (78, 2, NULL, 3, 19, '创建了联系人: ', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16407, '2020-12-17 11:00:57', NULL);
INSERT INTO `72crm_crm_activity` VALUES (79, 2, NULL, 2, 17, '创建了客户: ', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16389, '2020-12-17 11:08:16', NULL);
INSERT INTO `72crm_crm_activity` VALUES (80, 2, NULL, 2, 18, '创建了客户: ', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16389, '2020-12-17 11:10:08', NULL);
INSERT INTO `72crm_crm_activity` VALUES (81, 2, NULL, 3, 20, '创建了联系人: ', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16389, '2020-12-17 11:26:02', NULL);
INSERT INTO `72crm_crm_activity` VALUES (82, 2, NULL, 5, 8, '创建了商机: ', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16360, '2021-01-22 13:48:33', NULL);
INSERT INTO `72crm_crm_activity` VALUES (83, 2, NULL, 5, 9, '创建了商机: ', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16360, '2021-01-22 13:48:33', NULL);
INSERT INTO `72crm_crm_activity` VALUES (84, 2, NULL, 6, 18, '创建了合同: ', NULL, NULL, NULL, 1, NULL, NULL, NULL, 16360, '2020-12-17 14:38:55', NULL);
INSERT INTO `72crm_crm_activity` VALUES (85, 2, NULL, 7, 7, '创建了回款: ', NULL, NULL, NULL, 1, NULL, NULL, NULL, 16360, '2020-12-17 14:46:34', NULL);
INSERT INTO `72crm_crm_activity` VALUES (86, 2, NULL, 7, 8, '创建了回款: ', NULL, NULL, NULL, 1, NULL, NULL, NULL, 16360, '2020-12-17 14:50:18', NULL);
INSERT INTO `72crm_crm_activity` VALUES (87, 2, NULL, 6, 19, '创建了合同: ', NULL, NULL, NULL, 1, NULL, NULL, NULL, 16360, '2020-12-17 15:49:07', NULL);
INSERT INTO `72crm_crm_activity` VALUES (88, 2, NULL, 8, 6, '创建了日志: ', NULL, NULL, NULL, 1, NULL, NULL, NULL, 16360, '2020-12-17 16:32:08', NULL);
INSERT INTO `72crm_crm_activity` VALUES (89, 2, NULL, 8, 6, '创建了日志: ', NULL, NULL, NULL, 1, NULL, NULL, NULL, 16360, '2020-12-17 16:33:16', NULL);
INSERT INTO `72crm_crm_activity` VALUES (90, 2, NULL, 8, 7, '创建了日志: ', NULL, NULL, NULL, 1, NULL, NULL, NULL, 16398, '2020-12-17 16:33:32', NULL);
INSERT INTO `72crm_crm_activity` VALUES (91, 2, NULL, 8, 6, '创建了日志: ', NULL, NULL, NULL, 1, NULL, NULL, NULL, 16360, '2020-12-17 16:38:24', NULL);
INSERT INTO `72crm_crm_activity` VALUES (92, 2, NULL, 3, 21, '创建了联系人: ', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16360, '2021-01-22 13:48:45', NULL);
INSERT INTO `72crm_crm_activity` VALUES (93, 2, NULL, 8, 8, '创建了日志: ', NULL, NULL, NULL, 1, NULL, NULL, NULL, 16389, '2020-12-17 16:43:58', NULL);
INSERT INTO `72crm_crm_activity` VALUES (94, 2, NULL, 8, 9, '创建了日志: ', NULL, NULL, NULL, 1, NULL, NULL, NULL, 16389, '2020-12-22 10:37:27', NULL);
INSERT INTO `72crm_crm_activity` VALUES (95, 1, '打电话', 2, 15, '跟进', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16398, '2021-01-22 13:48:50', '3663f4bb2aca7c124c91bc83269de43f');
INSERT INTO `72crm_crm_activity` VALUES (96, 2, NULL, 5, 10, '创建了商机: ', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16407, '2021-01-22 13:48:33', NULL);
INSERT INTO `72crm_crm_activity` VALUES (97, 3, NULL, 5, 10, '采购意向明确', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16407, '2021-01-22 13:48:33', NULL);
INSERT INTO `72crm_crm_activity` VALUES (98, 3, NULL, 5, 10, '合同签订阶段', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16407, '2021-01-22 13:48:33', NULL);
INSERT INTO `72crm_crm_activity` VALUES (99, 3, NULL, 5, 10, '合同签订完成', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16407, '2021-01-22 13:48:33', NULL);
INSERT INTO `72crm_crm_activity` VALUES (100, 3, NULL, 5, 10, '赢单', NULL, NULL, NULL, 0, NULL, NULL, NULL, 16407, '2021-01-22 13:48:33', NULL);

-- ----------------------------
-- Table structure for 72crm_crm_activity_relation
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_activity_relation`;
CREATE TABLE `72crm_crm_activity_relation`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_id` int(11) NOT NULL,
  `type` int(1) NOT NULL COMMENT '3 联系人 5 商机',
  `type_id` int(11) NOT NULL,
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '活动关联商机联系人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_area
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_area`;
CREATE TABLE `72crm_crm_area`  (
  `code_id` int(11) NULL DEFAULT NULL,
  `parent_id` int(11) NULL DEFAULT NULL,
  `city_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '地名表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_area
-- ----------------------------
INSERT INTO `72crm_crm_area` VALUES (230403, 230400, '工农区');
INSERT INTO `72crm_crm_area` VALUES (230402, 230400, '向阳区');
INSERT INTO `72crm_crm_area` VALUES (230407, 230400, '兴山区');
INSERT INTO `72crm_crm_area` VALUES (230406, 230400, '东山区');
INSERT INTO `72crm_crm_area` VALUES (230422, 230400, '绥滨县');
INSERT INTO `72crm_crm_area` VALUES (230405, 230400, '兴安区');
INSERT INTO `72crm_crm_area` VALUES (230421, 230400, '萝北县');
INSERT INTO `72crm_crm_area` VALUES (230404, 230400, '南山区');
INSERT INTO `72crm_crm_area` VALUES (652328, 652300, '木垒哈萨克自治县');
INSERT INTO `72crm_crm_area` VALUES (652301, 652300, '昌吉市');
INSERT INTO `72crm_crm_area` VALUES (652302, 652300, '阜康市');
INSERT INTO `72crm_crm_area` VALUES (652323, 652300, '呼图壁县');
INSERT INTO `72crm_crm_area` VALUES (652325, 652300, '奇台县');
INSERT INTO `72crm_crm_area` VALUES (652324, 652300, '玛纳斯县');
INSERT INTO `72crm_crm_area` VALUES (652327, 652300, '吉木萨尔县');
INSERT INTO `72crm_crm_area` VALUES (512022, 512000, '乐至县');
INSERT INTO `72crm_crm_area` VALUES (512021, 512000, '安岳县');
INSERT INTO `72crm_crm_area` VALUES (512002, 512000, '雁江区');
INSERT INTO `72crm_crm_area` VALUES (370725, 370700, '昌乐县');
INSERT INTO `72crm_crm_area` VALUES (370724, 370700, '临朐县');
INSERT INTO `72crm_crm_area` VALUES (370705, 370700, '奎文区');
INSERT INTO `72crm_crm_area` VALUES (370785, 370700, '高密市');
INSERT INTO `72crm_crm_area` VALUES (370704, 370700, '坊子区');
INSERT INTO `72crm_crm_area` VALUES (370784, 370700, '安丘市');
INSERT INTO `72crm_crm_area` VALUES (370786, 370700, '昌邑市');
INSERT INTO `72crm_crm_area` VALUES (370781, 370700, '青州市');
INSERT INTO `72crm_crm_area` VALUES (370703, 370700, '寒亭区');
INSERT INTO `72crm_crm_area` VALUES (370783, 370700, '寿光市');
INSERT INTO `72crm_crm_area` VALUES (370702, 370700, '潍城区');
INSERT INTO `72crm_crm_area` VALUES (370782, 370700, '诸城市');
INSERT INTO `72crm_crm_area` VALUES (640200, 640000, '石嘴山市');
INSERT INTO `72crm_crm_area` VALUES (640300, 640000, '吴忠市');
INSERT INTO `72crm_crm_area` VALUES (640400, 640000, '固原市');
INSERT INTO `72crm_crm_area` VALUES (640100, 640000, '银川市');
INSERT INTO `72crm_crm_area` VALUES (640500, 640000, '中卫市');
INSERT INTO `72crm_crm_area` VALUES (341024, 341000, '祁门县');
INSERT INTO `72crm_crm_area` VALUES (341021, 341000, '歙县');
INSERT INTO `72crm_crm_area` VALUES (341004, 341000, '徽州区');
INSERT INTO `72crm_crm_area` VALUES (341023, 341000, '黟县');
INSERT INTO `72crm_crm_area` VALUES (341022, 341000, '休宁县');
INSERT INTO `72crm_crm_area` VALUES (341003, 341000, '黄山区');
INSERT INTO `72crm_crm_area` VALUES (341002, 341000, '屯溪区');
INSERT INTO `72crm_crm_area` VALUES (140322, 140300, '盂县');
INSERT INTO `72crm_crm_area` VALUES (140321, 140300, '平定县');
INSERT INTO `72crm_crm_area` VALUES (140311, 140300, '郊区');
INSERT INTO `72crm_crm_area` VALUES (140302, 140300, '城区');
INSERT INTO `72crm_crm_area` VALUES (140303, 140300, '矿区');
INSERT INTO `72crm_crm_area` VALUES (469030, 469000, '琼中黎族苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (469029, 469000, '保亭黎族苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (469028, 469000, '陵水黎族自治县');
INSERT INTO `72crm_crm_area` VALUES (469027, 469000, '乐东黎族自治县');
INSERT INTO `72crm_crm_area` VALUES (469026, 469000, '昌江黎族自治县');
INSERT INTO `72crm_crm_area` VALUES (469025, 469000, '白沙黎族自治县');
INSERT INTO `72crm_crm_area` VALUES (469024, 469000, '临高县');
INSERT INTO `72crm_crm_area` VALUES (469007, 469000, '东方市');
INSERT INTO `72crm_crm_area` VALUES (469006, 469000, '万宁市');
INSERT INTO `72crm_crm_area` VALUES (469005, 469000, '文昌市');
INSERT INTO `72crm_crm_area` VALUES (469002, 469000, '琼海市');
INSERT INTO `72crm_crm_area` VALUES (469001, 469000, '五指山市');
INSERT INTO `72crm_crm_area` VALUES (469023, 469000, '澄迈县');
INSERT INTO `72crm_crm_area` VALUES (469022, 469000, '屯昌县');
INSERT INTO `72crm_crm_area` VALUES (469021, 469000, '定安县');
INSERT INTO `72crm_crm_area` VALUES (520222, 520200, '盘州市');
INSERT INTO `72crm_crm_area` VALUES (520221, 520200, '水城县');
INSERT INTO `72crm_crm_area` VALUES (520203, 520200, '六枝特区');
INSERT INTO `72crm_crm_area` VALUES (520201, 520200, '钟山区');
INSERT INTO `72crm_crm_area` VALUES (430102, 430100, '芙蓉区');
INSERT INTO `72crm_crm_area` VALUES (430182, 430100, '宁乡市');
INSERT INTO `72crm_crm_area` VALUES (430103, 430100, '天心区');
INSERT INTO `72crm_crm_area` VALUES (430181, 430100, '浏阳市');
INSERT INTO `72crm_crm_area` VALUES (430112, 430100, '望城区');
INSERT INTO `72crm_crm_area` VALUES (430111, 430100, '雨花区');
INSERT INTO `72crm_crm_area` VALUES (430104, 430100, '岳麓区');
INSERT INTO `72crm_crm_area` VALUES (430105, 430100, '开福区');
INSERT INTO `72crm_crm_area` VALUES (430121, 430100, '长沙县');
INSERT INTO `72crm_crm_area` VALUES (532523, 532500, '屏边苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (532524, 532500, '建水县');
INSERT INTO `72crm_crm_area` VALUES (532525, 532500, '石屏县');
INSERT INTO `72crm_crm_area` VALUES (532527, 532500, '泸西县');
INSERT INTO `72crm_crm_area` VALUES (532504, 532500, '弥勒市');
INSERT INTO `72crm_crm_area` VALUES (532528, 532500, '元阳县');
INSERT INTO `72crm_crm_area` VALUES (532529, 532500, '红河县');
INSERT INTO `72crm_crm_area` VALUES (532530, 532500, '金平苗族瑶族傣族自治县');
INSERT INTO `72crm_crm_area` VALUES (532531, 532500, '绿春县');
INSERT INTO `72crm_crm_area` VALUES (532532, 532500, '河口瑶族自治县');
INSERT INTO `72crm_crm_area` VALUES (532501, 532500, '个旧市');
INSERT INTO `72crm_crm_area` VALUES (532502, 532500, '开远市');
INSERT INTO `72crm_crm_area` VALUES (532503, 532500, '蒙自市');
INSERT INTO `72crm_crm_area` VALUES (810100, 810000, '香港城区');
INSERT INTO `72crm_crm_area` VALUES (131122, 131100, '武邑县');
INSERT INTO `72crm_crm_area` VALUES (131123, 131100, '武强县');
INSERT INTO `72crm_crm_area` VALUES (131121, 131100, '枣强县');
INSERT INTO `72crm_crm_area` VALUES (131126, 131100, '故城县');
INSERT INTO `72crm_crm_area` VALUES (131127, 131100, '景县');
INSERT INTO `72crm_crm_area` VALUES (131124, 131100, '饶阳县');
INSERT INTO `72crm_crm_area` VALUES (131125, 131100, '安平县');
INSERT INTO `72crm_crm_area` VALUES (131128, 131100, '阜城县');
INSERT INTO `72crm_crm_area` VALUES (131102, 131100, '桃城区');
INSERT INTO `72crm_crm_area` VALUES (131182, 131100, '深州市');
INSERT INTO `72crm_crm_area` VALUES (131103, 131100, '冀州区');
INSERT INTO `72crm_crm_area` VALUES (711774, 711700, '彰化市');
INSERT INTO `72crm_crm_area` VALUES (711775, 711700, '芬园乡');
INSERT INTO `72crm_crm_area` VALUES (711786, 711700, '埔心乡');
INSERT INTO `72crm_crm_area` VALUES (711787, 711700, '溪湖镇');
INSERT INTO `72crm_crm_area` VALUES (711784, 711700, '社头乡');
INSERT INTO `72crm_crm_area` VALUES (711785, 711700, '永靖乡');
INSERT INTO `72crm_crm_area` VALUES (711790, 711700, '田中镇');
INSERT INTO `72crm_crm_area` VALUES (711791, 711700, '北斗镇');
INSERT INTO `72crm_crm_area` VALUES (711788, 711700, '大村乡');
INSERT INTO `72crm_crm_area` VALUES (711789, 711700, '埔盐乡');
INSERT INTO `72crm_crm_area` VALUES (711778, 711700, '鹿港镇');
INSERT INTO `72crm_crm_area` VALUES (711779, 711700, '福兴乡');
INSERT INTO `72crm_crm_area` VALUES (711776, 711700, '花坛乡');
INSERT INTO `72crm_crm_area` VALUES (711777, 711700, '秀水乡');
INSERT INTO `72crm_crm_area` VALUES (711782, 711700, '伸港乡');
INSERT INTO `72crm_crm_area` VALUES (711783, 711700, '员林镇');
INSERT INTO `72crm_crm_area` VALUES (711780, 711700, '线西乡');
INSERT INTO `72crm_crm_area` VALUES (711781, 711700, '和美镇');
INSERT INTO `72crm_crm_area` VALUES (711794, 711700, '溪州乡');
INSERT INTO `72crm_crm_area` VALUES (711795, 711700, '竹塘乡');
INSERT INTO `72crm_crm_area` VALUES (711792, 711700, '田尾乡');
INSERT INTO `72crm_crm_area` VALUES (711793, 711700, '埤头乡');
INSERT INTO `72crm_crm_area` VALUES (711798, 711700, '芳苑乡');
INSERT INTO `72crm_crm_area` VALUES (711799, 711700, '二水乡');
INSERT INTO `72crm_crm_area` VALUES (711796, 711700, '二林镇');
INSERT INTO `72crm_crm_area` VALUES (711797, 711700, '大城乡');
INSERT INTO `72crm_crm_area` VALUES (511028, 511000, '隆昌市');
INSERT INTO `72crm_crm_area` VALUES (511011, 511000, '东兴区');
INSERT INTO `72crm_crm_area` VALUES (511025, 511000, '资中县');
INSERT INTO `72crm_crm_area` VALUES (511024, 511000, '威远县');
INSERT INTO `72crm_crm_area` VALUES (511002, 511000, '市中区');
INSERT INTO `72crm_crm_area` VALUES (420902, 420900, '孝南区');
INSERT INTO `72crm_crm_area` VALUES (420982, 420900, '安陆市');
INSERT INTO `72crm_crm_area` VALUES (420981, 420900, '应城市');
INSERT INTO `72crm_crm_area` VALUES (420922, 420900, '大悟县');
INSERT INTO `72crm_crm_area` VALUES (420923, 420900, '云梦县');
INSERT INTO `72crm_crm_area` VALUES (420984, 420900, '汉川市');
INSERT INTO `72crm_crm_area` VALUES (420921, 420900, '孝昌县');
INSERT INTO `72crm_crm_area` VALUES (340100, 340000, '合肥市');
INSERT INTO `72crm_crm_area` VALUES (341700, 340000, '池州市');
INSERT INTO `72crm_crm_area` VALUES (340800, 340000, '安庆市');
INSERT INTO `72crm_crm_area` VALUES (341600, 340000, '亳州市');
INSERT INTO `72crm_crm_area` VALUES (340300, 340000, '蚌埠市');
INSERT INTO `72crm_crm_area` VALUES (341100, 340000, '滁州市');
INSERT INTO `72crm_crm_area` VALUES (340200, 340000, '芜湖市');
INSERT INTO `72crm_crm_area` VALUES (341000, 340000, '黄山市');
INSERT INTO `72crm_crm_area` VALUES (341800, 340000, '宣城市');
INSERT INTO `72crm_crm_area` VALUES (340500, 340000, '马鞍山市');
INSERT INTO `72crm_crm_area` VALUES (341300, 340000, '宿州市');
INSERT INTO `72crm_crm_area` VALUES (340400, 340000, '淮南市');
INSERT INTO `72crm_crm_area` VALUES (341200, 340000, '阜阳市');
INSERT INTO `72crm_crm_area` VALUES (340700, 340000, '铜陵市');
INSERT INTO `72crm_crm_area` VALUES (341500, 340000, '六安市');
INSERT INTO `72crm_crm_area` VALUES (340600, 340000, '淮北市');
INSERT INTO `72crm_crm_area` VALUES (710702, 710700, '信义区');
INSERT INTO `72crm_crm_area` VALUES (710703, 710700, '中正区');
INSERT INTO `72crm_crm_area` VALUES (710799, 710700, '其它区');
INSERT INTO `72crm_crm_area` VALUES (710701, 710700, '仁爱区');
INSERT INTO `72crm_crm_area` VALUES (710706, 710700, '暖暖区');
INSERT INTO `72crm_crm_area` VALUES (710707, 710700, '七堵区');
INSERT INTO `72crm_crm_area` VALUES (710704, 710700, '中山区');
INSERT INTO `72crm_crm_area` VALUES (710705, 710700, '安乐区');
INSERT INTO `72crm_crm_area` VALUES (220211, 220200, '丰满区');
INSERT INTO `72crm_crm_area` VALUES (220203, 220200, '龙潭区');
INSERT INTO `72crm_crm_area` VALUES (220283, 220200, '舒兰市');
INSERT INTO `72crm_crm_area` VALUES (220202, 220200, '昌邑区');
INSERT INTO `72crm_crm_area` VALUES (220282, 220200, '桦甸市');
INSERT INTO `72crm_crm_area` VALUES (220281, 220200, '蛟河市');
INSERT INTO `72crm_crm_area` VALUES (220221, 220200, '永吉县');
INSERT INTO `72crm_crm_area` VALUES (220204, 220200, '船营区');
INSERT INTO `72crm_crm_area` VALUES (220284, 220200, '磐石市');
INSERT INTO `72crm_crm_area` VALUES (450621, 450600, '上思县');
INSERT INTO `72crm_crm_area` VALUES (450602, 450600, '港口区');
INSERT INTO `72crm_crm_area` VALUES (450603, 450600, '防城区');
INSERT INTO `72crm_crm_area` VALUES (450681, 450600, '东兴市');
INSERT INTO `72crm_crm_area` VALUES (360502, 360500, '渝水区');
INSERT INTO `72crm_crm_area` VALUES (360521, 360500, '分宜县');
INSERT INTO `72crm_crm_area` VALUES (620602, 620600, '凉州区');
INSERT INTO `72crm_crm_area` VALUES (620621, 620600, '民勤县');
INSERT INTO `72crm_crm_area` VALUES (620623, 620600, '天祝藏族自治县');
INSERT INTO `72crm_crm_area` VALUES (620622, 620600, '古浪县');
INSERT INTO `72crm_crm_area` VALUES (411702, 411700, '驿城区');
INSERT INTO `72crm_crm_area` VALUES (411728, 411700, '遂平县');
INSERT INTO `72crm_crm_area` VALUES (411729, 411700, '新蔡县');
INSERT INTO `72crm_crm_area` VALUES (411726, 411700, '泌阳县');
INSERT INTO `72crm_crm_area` VALUES (411727, 411700, '汝南县');
INSERT INTO `72crm_crm_area` VALUES (411724, 411700, '正阳县');
INSERT INTO `72crm_crm_area` VALUES (411725, 411700, '确山县');
INSERT INTO `72crm_crm_area` VALUES (411722, 411700, '上蔡县');
INSERT INTO `72crm_crm_area` VALUES (411723, 411700, '平舆县');
INSERT INTO `72crm_crm_area` VALUES (411721, 411700, '西平县');
INSERT INTO `72crm_crm_area` VALUES (130181, 130100, '辛集市');
INSERT INTO `72crm_crm_area` VALUES (130183, 130100, '晋州市');
INSERT INTO `72crm_crm_area` VALUES (130121, 130100, '井陉县');
INSERT INTO `72crm_crm_area` VALUES (130184, 130100, '新乐市');
INSERT INTO `72crm_crm_area` VALUES (130123, 130100, '正定县');
INSERT INTO `72crm_crm_area` VALUES (130125, 130100, '行唐县');
INSERT INTO `72crm_crm_area` VALUES (130127, 130100, '高邑县');
INSERT INTO `72crm_crm_area` VALUES (130126, 130100, '灵寿县');
INSERT INTO `72crm_crm_area` VALUES (130129, 130100, '赞皇县');
INSERT INTO `72crm_crm_area` VALUES (130128, 130100, '深泽县');
INSERT INTO `72crm_crm_area` VALUES (130131, 130100, '平山县');
INSERT INTO `72crm_crm_area` VALUES (130130, 130100, '无极县');
INSERT INTO `72crm_crm_area` VALUES (130133, 130100, '赵县');
INSERT INTO `72crm_crm_area` VALUES (130132, 130100, '元氏县');
INSERT INTO `72crm_crm_area` VALUES (130102, 130100, '长安区');
INSERT INTO `72crm_crm_area` VALUES (130105, 130100, '新华区');
INSERT INTO `72crm_crm_area` VALUES (130104, 130100, '桥西区');
INSERT INTO `72crm_crm_area` VALUES (130107, 130100, '井陉矿区');
INSERT INTO `72crm_crm_area` VALUES (130109, 130100, '藁城区');
INSERT INTO `72crm_crm_area` VALUES (130108, 130100, '裕华区');
INSERT INTO `72crm_crm_area` VALUES (130111, 130100, '栾城区');
INSERT INTO `72crm_crm_area` VALUES (130110, 130100, '鹿泉区');
INSERT INTO `72crm_crm_area` VALUES (330822, 330800, '常山县');
INSERT INTO `72crm_crm_area` VALUES (330881, 330800, '江山市');
INSERT INTO `72crm_crm_area` VALUES (330803, 330800, '衢江区');
INSERT INTO `72crm_crm_area` VALUES (330802, 330800, '柯城区');
INSERT INTO `72crm_crm_area` VALUES (330825, 330800, '龙游县');
INSERT INTO `72crm_crm_area` VALUES (330824, 330800, '开化县');
INSERT INTO `72crm_crm_area` VALUES (510500, 510000, '泸州市');
INSERT INTO `72crm_crm_area` VALUES (511300, 510000, '南充市');
INSERT INTO `72crm_crm_area` VALUES (510400, 510000, '攀枝花市');
INSERT INTO `72crm_crm_area` VALUES (512000, 510000, '资阳市');
INSERT INTO `72crm_crm_area` VALUES (510700, 510000, '绵阳市');
INSERT INTO `72crm_crm_area` VALUES (511500, 510000, '宜宾市');
INSERT INTO `72crm_crm_area` VALUES (510600, 510000, '德阳市');
INSERT INTO `72crm_crm_area` VALUES (511400, 510000, '眉山市');
INSERT INTO `72crm_crm_area` VALUES (510100, 510000, '成都市');
INSERT INTO `72crm_crm_area` VALUES (510900, 510000, '遂宁市');
INSERT INTO `72crm_crm_area` VALUES (511700, 510000, '达州市');
INSERT INTO `72crm_crm_area` VALUES (513300, 510000, '甘孜藏族自治州');
INSERT INTO `72crm_crm_area` VALUES (510800, 510000, '广元市');
INSERT INTO `72crm_crm_area` VALUES (511600, 510000, '广安市');
INSERT INTO `72crm_crm_area` VALUES (513200, 510000, '阿坝藏族羌族自治州');
INSERT INTO `72crm_crm_area` VALUES (510300, 510000, '自贡市');
INSERT INTO `72crm_crm_area` VALUES (511100, 510000, '乐山市');
INSERT INTO `72crm_crm_area` VALUES (511900, 510000, '巴中市');
INSERT INTO `72crm_crm_area` VALUES (511000, 510000, '内江市');
INSERT INTO `72crm_crm_area` VALUES (511800, 510000, '雅安市');
INSERT INTO `72crm_crm_area` VALUES (513400, 510000, '凉山彝族自治州');
INSERT INTO `72crm_crm_area` VALUES (211011, 211000, '太子河区');
INSERT INTO `72crm_crm_area` VALUES (211003, 211000, '文圣区');
INSERT INTO `72crm_crm_area` VALUES (211002, 211000, '白塔区');
INSERT INTO `72crm_crm_area` VALUES (211081, 211000, '灯塔市');
INSERT INTO `72crm_crm_area` VALUES (211005, 211000, '弓长岭区');
INSERT INTO `72crm_crm_area` VALUES (211021, 211000, '辽阳县');
INSERT INTO `72crm_crm_area` VALUES (211004, 211000, '宏伟区');
INSERT INTO `72crm_crm_area` VALUES (522327, 522300, '册亨县');
INSERT INTO `72crm_crm_area` VALUES (522326, 522300, '望谟县');
INSERT INTO `72crm_crm_area` VALUES (522325, 522300, '贞丰县');
INSERT INTO `72crm_crm_area` VALUES (522324, 522300, '晴隆县');
INSERT INTO `72crm_crm_area` VALUES (522323, 522300, '普安县');
INSERT INTO `72crm_crm_area` VALUES (522322, 522300, '兴仁县');
INSERT INTO `72crm_crm_area` VALUES (522301, 522300, '兴义市');
INSERT INTO `72crm_crm_area` VALUES (522328, 522300, '安龙县');
INSERT INTO `72crm_crm_area` VALUES (441426, 441400, '平远县');
INSERT INTO `72crm_crm_area` VALUES (441427, 441400, '蕉岭县');
INSERT INTO `72crm_crm_area` VALUES (441424, 441400, '五华县');
INSERT INTO `72crm_crm_area` VALUES (441422, 441400, '大埔县');
INSERT INTO `72crm_crm_area` VALUES (441423, 441400, '丰顺县');
INSERT INTO `72crm_crm_area` VALUES (441402, 441400, '梅江区');
INSERT INTO `72crm_crm_area` VALUES (441403, 441400, '梅县区');
INSERT INTO `72crm_crm_area` VALUES (441481, 441400, '兴宁市');
INSERT INTO `72crm_crm_area` VALUES (150626, 150600, '乌审旗');
INSERT INTO `72crm_crm_area` VALUES (150627, 150600, '伊金霍洛旗');
INSERT INTO `72crm_crm_area` VALUES (150624, 150600, '鄂托克旗');
INSERT INTO `72crm_crm_area` VALUES (150625, 150600, '杭锦旗');
INSERT INTO `72crm_crm_area` VALUES (150602, 150600, '东胜区');
INSERT INTO `72crm_crm_area` VALUES (150603, 150600, '康巴什区');
INSERT INTO `72crm_crm_area` VALUES (150622, 150600, '准格尔旗');
INSERT INTO `72crm_crm_area` VALUES (150623, 150600, '鄂托克前旗');
INSERT INTO `72crm_crm_area` VALUES (150621, 150600, '达拉特旗');
INSERT INTO `72crm_crm_area` VALUES (410726, 410700, '延津县');
INSERT INTO `72crm_crm_area` VALUES (410711, 410700, '牧野区');
INSERT INTO `72crm_crm_area` VALUES (410727, 410700, '封丘县');
INSERT INTO `72crm_crm_area` VALUES (410724, 410700, '获嘉县');
INSERT INTO `72crm_crm_area` VALUES (410725, 410700, '原阳县');
INSERT INTO `72crm_crm_area` VALUES (410704, 410700, '凤泉区');
INSERT INTO `72crm_crm_area` VALUES (410721, 410700, '新乡县');
INSERT INTO `72crm_crm_area` VALUES (410702, 410700, '红旗区');
INSERT INTO `72crm_crm_area` VALUES (410782, 410700, '辉县市');
INSERT INTO `72crm_crm_area` VALUES (410703, 410700, '卫滨区');
INSERT INTO `72crm_crm_area` VALUES (410781, 410700, '卫辉市');
INSERT INTO `72crm_crm_area` VALUES (410728, 410700, '长垣县');
INSERT INTO `72crm_crm_area` VALUES (530521, 530500, '施甸县');
INSERT INTO `72crm_crm_area` VALUES (530523, 530500, '龙陵县');
INSERT INTO `72crm_crm_area` VALUES (530524, 530500, '昌宁县');
INSERT INTO `72crm_crm_area` VALUES (530581, 530500, '腾冲市');
INSERT INTO `72crm_crm_area` VALUES (530502, 530500, '隆阳区');
INSERT INTO `72crm_crm_area` VALUES (210400, 210000, '抚顺市');
INSERT INTO `72crm_crm_area` VALUES (211200, 210000, '铁岭市');
INSERT INTO `72crm_crm_area` VALUES (210500, 210000, '本溪市');
INSERT INTO `72crm_crm_area` VALUES (211300, 210000, '朝阳市');
INSERT INTO `72crm_crm_area` VALUES (210600, 210000, '丹东市');
INSERT INTO `72crm_crm_area` VALUES (211400, 210000, '葫芦岛市');
INSERT INTO `72crm_crm_area` VALUES (210700, 210000, '锦州市');
INSERT INTO `72crm_crm_area` VALUES (210800, 210000, '营口市');
INSERT INTO `72crm_crm_area` VALUES (210100, 210000, '沈阳市');
INSERT INTO `72crm_crm_area` VALUES (210900, 210000, '阜新市');
INSERT INTO `72crm_crm_area` VALUES (210200, 210000, '大连市');
INSERT INTO `72crm_crm_area` VALUES (211000, 210000, '辽阳市');
INSERT INTO `72crm_crm_area` VALUES (210300, 210000, '鞍山市');
INSERT INTO `72crm_crm_area` VALUES (211100, 210000, '盘锦市');
INSERT INTO `72crm_crm_area` VALUES (440404, 440400, '金湾区');
INSERT INTO `72crm_crm_area` VALUES (440402, 440400, '香洲区');
INSERT INTO `72crm_crm_area` VALUES (440403, 440400, '斗门区');
INSERT INTO `72crm_crm_area` VALUES (350305, 350300, '秀屿区');
INSERT INTO `72crm_crm_area` VALUES (350304, 350300, '荔城区');
INSERT INTO `72crm_crm_area` VALUES (350322, 350300, '仙游县');
INSERT INTO `72crm_crm_area` VALUES (350303, 350300, '涵江区');
INSERT INTO `72crm_crm_area` VALUES (350302, 350300, '城厢区');
INSERT INTO `72crm_crm_area` VALUES (320612, 320600, '通州区');
INSERT INTO `72crm_crm_area` VALUES (320611, 320600, '港闸区');
INSERT INTO `72crm_crm_area` VALUES (320684, 320600, '海门市');
INSERT INTO `72crm_crm_area` VALUES (320621, 320600, '海安县');
INSERT INTO `72crm_crm_area` VALUES (320623, 320600, '如东县');
INSERT INTO `72crm_crm_area` VALUES (320681, 320600, '启东市');
INSERT INTO `72crm_crm_area` VALUES (320602, 320600, '崇川区');
INSERT INTO `72crm_crm_area` VALUES (320682, 320600, '如皋市');
INSERT INTO `72crm_crm_area` VALUES (431222, 431200, '沅陵县');
INSERT INTO `72crm_crm_area` VALUES (431223, 431200, '辰溪县');
INSERT INTO `72crm_crm_area` VALUES (431221, 431200, '中方县');
INSERT INTO `72crm_crm_area` VALUES (431202, 431200, '鹤城区');
INSERT INTO `72crm_crm_area` VALUES (431281, 431200, '洪江市');
INSERT INTO `72crm_crm_area` VALUES (431230, 431200, '通道侗族自治县');
INSERT INTO `72crm_crm_area` VALUES (431228, 431200, '芷江侗族自治县');
INSERT INTO `72crm_crm_area` VALUES (431229, 431200, '靖州苗族侗族自治县');
INSERT INTO `72crm_crm_area` VALUES (431226, 431200, '麻阳苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (431227, 431200, '新晃侗族自治县');
INSERT INTO `72crm_crm_area` VALUES (431224, 431200, '溆浦县');
INSERT INTO `72crm_crm_area` VALUES (431225, 431200, '会同县');
INSERT INTO `72crm_crm_area` VALUES (230503, 230500, '岭东区');
INSERT INTO `72crm_crm_area` VALUES (230502, 230500, '尖山区');
INSERT INTO `72crm_crm_area` VALUES (230523, 230500, '宝清县');
INSERT INTO `72crm_crm_area` VALUES (230506, 230500, '宝山区');
INSERT INTO `72crm_crm_area` VALUES (230522, 230500, '友谊县');
INSERT INTO `72crm_crm_area` VALUES (230505, 230500, '四方台区');
INSERT INTO `72crm_crm_area` VALUES (230521, 230500, '集贤县');
INSERT INTO `72crm_crm_area` VALUES (230524, 230500, '饶河县');
INSERT INTO `72crm_crm_area` VALUES (341125, 341100, '定远县');
INSERT INTO `72crm_crm_area` VALUES (341124, 341100, '全椒县');
INSERT INTO `72crm_crm_area` VALUES (341126, 341100, '凤阳县');
INSERT INTO `72crm_crm_area` VALUES (341122, 341100, '来安县');
INSERT INTO `72crm_crm_area` VALUES (341181, 341100, '天长市');
INSERT INTO `72crm_crm_area` VALUES (341103, 341100, '南谯区');
INSERT INTO `72crm_crm_area` VALUES (341102, 341100, '琅琊区');
INSERT INTO `72crm_crm_area` VALUES (341182, 341100, '明光市');
INSERT INTO `72crm_crm_area` VALUES (610403, 610400, '杨陵区');
INSERT INTO `72crm_crm_area` VALUES (610402, 610400, '秦都区');
INSERT INTO `72crm_crm_area` VALUES (610404, 610400, '渭城区');
INSERT INTO `72crm_crm_area` VALUES (610425, 610400, '礼泉县');
INSERT INTO `72crm_crm_area` VALUES (610424, 610400, '乾县');
INSERT INTO `72crm_crm_area` VALUES (610427, 610400, '彬县');
INSERT INTO `72crm_crm_area` VALUES (610426, 610400, '永寿县');
INSERT INTO `72crm_crm_area` VALUES (610429, 610400, '旬邑县');
INSERT INTO `72crm_crm_area` VALUES (610428, 610400, '长武县');
INSERT INTO `72crm_crm_area` VALUES (610431, 610400, '武功县');
INSERT INTO `72crm_crm_area` VALUES (610430, 610400, '淳化县');
INSERT INTO `72crm_crm_area` VALUES (610481, 610400, '兴平市');
INSERT INTO `72crm_crm_area` VALUES (610423, 610400, '泾阳县');
INSERT INTO `72crm_crm_area` VALUES (610422, 610400, '三原县');
INSERT INTO `72crm_crm_area` VALUES (712898, 712800, '东引乡');
INSERT INTO `72crm_crm_area` VALUES (712899, 712800, '莒光乡');
INSERT INTO `72crm_crm_area` VALUES (712896, 712800, '南竿乡');
INSERT INTO `72crm_crm_area` VALUES (712897, 712800, '北竿乡');
INSERT INTO `72crm_crm_area` VALUES (520327, 520300, '凤冈县');
INSERT INTO `72crm_crm_area` VALUES (520326, 520300, '务川仡佬族苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (520325, 520300, '道真仡佬族苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (520324, 520300, '正安县');
INSERT INTO `72crm_crm_area` VALUES (520323, 520300, '绥阳县');
INSERT INTO `72crm_crm_area` VALUES (520322, 520300, '桐梓县');
INSERT INTO `72crm_crm_area` VALUES (520303, 520300, '汇川区');
INSERT INTO `72crm_crm_area` VALUES (520302, 520300, '红花岗区');
INSERT INTO `72crm_crm_area` VALUES (520330, 520300, '习水县');
INSERT INTO `72crm_crm_area` VALUES (520329, 520300, '余庆县');
INSERT INTO `72crm_crm_area` VALUES (520328, 520300, '湄潭县');
INSERT INTO `72crm_crm_area` VALUES (520304, 520300, '播州区');
INSERT INTO `72crm_crm_area` VALUES (520382, 520300, '仁怀市');
INSERT INTO `72crm_crm_area` VALUES (520381, 520300, '赤水市');
INSERT INTO `72crm_crm_area` VALUES (640105, 640100, '西夏区');
INSERT INTO `72crm_crm_area` VALUES (640121, 640100, '永宁县');
INSERT INTO `72crm_crm_area` VALUES (640104, 640100, '兴庆区');
INSERT INTO `72crm_crm_area` VALUES (640106, 640100, '金凤区');
INSERT INTO `72crm_crm_area` VALUES (640122, 640100, '贺兰县');
INSERT INTO `72crm_crm_area` VALUES (640181, 640100, '灵武市');
INSERT INTO `72crm_crm_area` VALUES (532601, 532600, '文山市');
INSERT INTO `72crm_crm_area` VALUES (532622, 532600, '砚山县');
INSERT INTO `72crm_crm_area` VALUES (532623, 532600, '西畴县');
INSERT INTO `72crm_crm_area` VALUES (532624, 532600, '麻栗坡县');
INSERT INTO `72crm_crm_area` VALUES (532625, 532600, '马关县');
INSERT INTO `72crm_crm_area` VALUES (532626, 532600, '丘北县');
INSERT INTO `72crm_crm_area` VALUES (532627, 532600, '广南县');
INSERT INTO `72crm_crm_area` VALUES (532628, 532600, '富宁县');
INSERT INTO `72crm_crm_area` VALUES (140481, 140400, '潞城市');
INSERT INTO `72crm_crm_area` VALUES (140423, 140400, '襄垣县');
INSERT INTO `72crm_crm_area` VALUES (140421, 140400, '长治县');
INSERT INTO `72crm_crm_area` VALUES (140426, 140400, '黎城县');
INSERT INTO `72crm_crm_area` VALUES (140427, 140400, '壶关县');
INSERT INTO `72crm_crm_area` VALUES (140424, 140400, '屯留县');
INSERT INTO `72crm_crm_area` VALUES (140425, 140400, '平顺县');
INSERT INTO `72crm_crm_area` VALUES (140430, 140400, '沁县');
INSERT INTO `72crm_crm_area` VALUES (140431, 140400, '沁源县');
INSERT INTO `72crm_crm_area` VALUES (140428, 140400, '长子县');
INSERT INTO `72crm_crm_area` VALUES (140429, 140400, '武乡县');
INSERT INTO `72crm_crm_area` VALUES (140402, 140400, '城区');
INSERT INTO `72crm_crm_area` VALUES (140411, 140400, '郊区');
INSERT INTO `72crm_crm_area` VALUES (370881, 370800, '曲阜市');
INSERT INTO `72crm_crm_area` VALUES (370832, 370800, '梁山县');
INSERT INTO `72crm_crm_area` VALUES (370883, 370800, '邹城市');
INSERT INTO `72crm_crm_area` VALUES (370829, 370800, '嘉祥县');
INSERT INTO `72crm_crm_area` VALUES (370812, 370800, '兖州区');
INSERT INTO `72crm_crm_area` VALUES (370828, 370800, '金乡县');
INSERT INTO `72crm_crm_area` VALUES (370831, 370800, '泗水县');
INSERT INTO `72crm_crm_area` VALUES (370830, 370800, '汶上县');
INSERT INTO `72crm_crm_area` VALUES (370811, 370800, '任城区');
INSERT INTO `72crm_crm_area` VALUES (370827, 370800, '鱼台县');
INSERT INTO `72crm_crm_area` VALUES (370826, 370800, '微山县');
INSERT INTO `72crm_crm_area` VALUES (810116, 810100, '沙田区');
INSERT INTO `72crm_crm_area` VALUES (810117, 810100, '葵青区');
INSERT INTO `72crm_crm_area` VALUES (810118, 810100, '离岛区');
INSERT INTO `72crm_crm_area` VALUES (810112, 810100, '元朗区');
INSERT INTO `72crm_crm_area` VALUES (810113, 810100, '北区');
INSERT INTO `72crm_crm_area` VALUES (810114, 810100, '大埔区');
INSERT INTO `72crm_crm_area` VALUES (810115, 810100, '西贡区');
INSERT INTO `72crm_crm_area` VALUES (810108, 810100, '黄大仙区');
INSERT INTO `72crm_crm_area` VALUES (810109, 810100, '观塘区');
INSERT INTO `72crm_crm_area` VALUES (810110, 810100, '荃湾区');
INSERT INTO `72crm_crm_area` VALUES (810111, 810100, '屯门区');
INSERT INTO `72crm_crm_area` VALUES (810104, 810100, '南区');
INSERT INTO `72crm_crm_area` VALUES (810105, 810100, '油尖旺区');
INSERT INTO `72crm_crm_area` VALUES (810106, 810100, '深水埗区');
INSERT INTO `72crm_crm_area` VALUES (810107, 810100, '九龙城区');
INSERT INTO `72crm_crm_area` VALUES (810101, 810100, '中西区');
INSERT INTO `72crm_crm_area` VALUES (810102, 810100, '湾仔区');
INSERT INTO `72crm_crm_area` VALUES (810103, 810100, '东区');
INSERT INTO `72crm_crm_area` VALUES (511111, 511100, '沙湾区');
INSERT INTO `72crm_crm_area` VALUES (511126, 511100, '夹江县');
INSERT INTO `72crm_crm_area` VALUES (511124, 511100, '井研县');
INSERT INTO `72crm_crm_area` VALUES (511123, 511100, '犍为县');
INSERT INTO `72crm_crm_area` VALUES (511102, 511100, '市中区');
INSERT INTO `72crm_crm_area` VALUES (511133, 511100, '马边彝族自治县');
INSERT INTO `72crm_crm_area` VALUES (511181, 511100, '峨眉山市');
INSERT INTO `72crm_crm_area` VALUES (511132, 511100, '峨边彝族自治县');
INSERT INTO `72crm_crm_area` VALUES (511113, 511100, '金口河区');
INSERT INTO `72crm_crm_area` VALUES (511129, 511100, '沐川县');
INSERT INTO `72crm_crm_area` VALUES (511112, 511100, '五通桥区');
INSERT INTO `72crm_crm_area` VALUES (430211, 430200, '天元区');
INSERT INTO `72crm_crm_area` VALUES (430224, 430200, '茶陵县');
INSERT INTO `72crm_crm_area` VALUES (430225, 430200, '炎陵县');
INSERT INTO `72crm_crm_area` VALUES (430223, 430200, '攸县');
INSERT INTO `72crm_crm_area` VALUES (430204, 430200, '石峰区');
INSERT INTO `72crm_crm_area` VALUES (430221, 430200, '株洲县');
INSERT INTO `72crm_crm_area` VALUES (430202, 430200, '荷塘区');
INSERT INTO `72crm_crm_area` VALUES (430203, 430200, '芦淞区');
INSERT INTO `72crm_crm_area` VALUES (430281, 430200, '醴陵市');
INSERT INTO `72crm_crm_area` VALUES (340181, 340100, '巢湖市');
INSERT INTO `72crm_crm_area` VALUES (340103, 340100, '庐阳区');
INSERT INTO `72crm_crm_area` VALUES (340102, 340100, '瑶海区');
INSERT INTO `72crm_crm_area` VALUES (340124, 340100, '庐江县');
INSERT INTO `72crm_crm_area` VALUES (340111, 340100, '包河区');
INSERT INTO `72crm_crm_area` VALUES (340121, 340100, '长丰县');
INSERT INTO `72crm_crm_area` VALUES (340104, 340100, '蜀山区');
INSERT INTO `72crm_crm_area` VALUES (340123, 340100, '肥西县');
INSERT INTO `72crm_crm_area` VALUES (340122, 340100, '肥东县');
INSERT INTO `72crm_crm_area` VALUES (450722, 450700, '浦北县');
INSERT INTO `72crm_crm_area` VALUES (450721, 450700, '灵山县');
INSERT INTO `72crm_crm_area` VALUES (450702, 450700, '钦南区');
INSERT INTO `72crm_crm_area` VALUES (450703, 450700, '钦北区');
INSERT INTO `72crm_crm_area` VALUES (421024, 421000, '江陵县');
INSERT INTO `72crm_crm_area` VALUES (421022, 421000, '公安县');
INSERT INTO `72crm_crm_area` VALUES (421023, 421000, '监利县');
INSERT INTO `72crm_crm_area` VALUES (421087, 421000, '松滋市');
INSERT INTO `72crm_crm_area` VALUES (421002, 421000, '沙市区');
INSERT INTO `72crm_crm_area` VALUES (421003, 421000, '荆州区');
INSERT INTO `72crm_crm_area` VALUES (421083, 421000, '洪湖市');
INSERT INTO `72crm_crm_area` VALUES (421081, 421000, '石首市');
INSERT INTO `72crm_crm_area` VALUES (220323, 220300, '伊通满族自治县');
INSERT INTO `72crm_crm_area` VALUES (220322, 220300, '梨树县');
INSERT INTO `72crm_crm_area` VALUES (220303, 220300, '铁东区');
INSERT INTO `72crm_crm_area` VALUES (220302, 220300, '铁西区');
INSERT INTO `72crm_crm_area` VALUES (220382, 220300, '双辽市');
INSERT INTO `72crm_crm_area` VALUES (220381, 220300, '公主岭市');
INSERT INTO `72crm_crm_area` VALUES (330903, 330900, '普陀区');
INSERT INTO `72crm_crm_area` VALUES (330902, 330900, '定海区');
INSERT INTO `72crm_crm_area` VALUES (330921, 330900, '岱山县');
INSERT INTO `72crm_crm_area` VALUES (330922, 330900, '嵊泗县');
INSERT INTO `72crm_crm_area` VALUES (510183, 510100, '邛崃市');
INSERT INTO `72crm_crm_area` VALUES (510182, 510100, '彭州市');
INSERT INTO `72crm_crm_area` VALUES (510117, 510100, '郫都区');
INSERT INTO `72crm_crm_area` VALUES (510181, 510100, '都江堰市');
INSERT INTO `72crm_crm_area` VALUES (510116, 510100, '双流区');
INSERT INTO `72crm_crm_area` VALUES (510115, 510100, '温江区');
INSERT INTO `72crm_crm_area` VALUES (510114, 510100, '新都区');
INSERT INTO `72crm_crm_area` VALUES (510113, 510100, '青白江区');
INSERT INTO `72crm_crm_area` VALUES (510112, 510100, '龙泉驿区');
INSERT INTO `72crm_crm_area` VALUES (510121, 510100, '金堂县');
INSERT INTO `72crm_crm_area` VALUES (510185, 510100, '简阳市');
INSERT INTO `72crm_crm_area` VALUES (510184, 510100, '崇州市');
INSERT INTO `72crm_crm_area` VALUES (510132, 510100, '新津县');
INSERT INTO `72crm_crm_area` VALUES (510131, 510100, '蒲江县');
INSERT INTO `72crm_crm_area` VALUES (510129, 510100, '大邑县');
INSERT INTO `72crm_crm_area` VALUES (510108, 510100, '成华区');
INSERT INTO `72crm_crm_area` VALUES (510107, 510100, '武侯区');
INSERT INTO `72crm_crm_area` VALUES (510106, 510100, '金牛区');
INSERT INTO `72crm_crm_area` VALUES (510105, 510100, '青羊区');
INSERT INTO `72crm_crm_area` VALUES (510104, 510100, '锦江区');
INSERT INTO `72crm_crm_area` VALUES (620702, 620700, '甘州区');
INSERT INTO `72crm_crm_area` VALUES (620721, 620700, '肃南裕固族自治县');
INSERT INTO `72crm_crm_area` VALUES (620723, 620700, '临泽县');
INSERT INTO `72crm_crm_area` VALUES (620722, 620700, '民乐县');
INSERT INTO `72crm_crm_area` VALUES (620725, 620700, '山丹县');
INSERT INTO `72crm_crm_area` VALUES (620724, 620700, '高台县');
INSERT INTO `72crm_crm_area` VALUES (130209, 130200, '曹妃甸区');
INSERT INTO `72crm_crm_area` VALUES (130208, 130200, '丰润区');
INSERT INTO `72crm_crm_area` VALUES (130281, 130200, '遵化市');
INSERT INTO `72crm_crm_area` VALUES (130283, 130200, '迁安市');
INSERT INTO `72crm_crm_area` VALUES (130223, 130200, '滦县');
INSERT INTO `72crm_crm_area` VALUES (130225, 130200, '乐亭县');
INSERT INTO `72crm_crm_area` VALUES (130224, 130200, '滦南县');
INSERT INTO `72crm_crm_area` VALUES (130227, 130200, '迁西县');
INSERT INTO `72crm_crm_area` VALUES (130229, 130200, '玉田县');
INSERT INTO `72crm_crm_area` VALUES (130203, 130200, '路北区');
INSERT INTO `72crm_crm_area` VALUES (130202, 130200, '路南区');
INSERT INTO `72crm_crm_area` VALUES (130205, 130200, '开平区');
INSERT INTO `72crm_crm_area` VALUES (130204, 130200, '古冶区');
INSERT INTO `72crm_crm_area` VALUES (130207, 130200, '丰南区');
INSERT INTO `72crm_crm_area` VALUES (441523, 441500, '陆河县');
INSERT INTO `72crm_crm_area` VALUES (441521, 441500, '海丰县');
INSERT INTO `72crm_crm_area` VALUES (441502, 441500, '城区');
INSERT INTO `72crm_crm_area` VALUES (441581, 441500, '陆丰市');
INSERT INTO `72crm_crm_area` VALUES (710802, 710800, '北区');
INSERT INTO `72crm_crm_area` VALUES (710803, 710800, '香山区');
INSERT INTO `72crm_crm_area` VALUES (710899, 710800, '其它区');
INSERT INTO `72crm_crm_area` VALUES (710801, 710800, '东区');
INSERT INTO `72crm_crm_area` VALUES (360622, 360600, '余江县');
INSERT INTO `72crm_crm_area` VALUES (360681, 360600, '贵溪市');
INSERT INTO `72crm_crm_area` VALUES (360602, 360600, '月湖区');
INSERT INTO `72crm_crm_area` VALUES (211122, 211100, '盘山县');
INSERT INTO `72crm_crm_area` VALUES (211104, 211100, '大洼区');
INSERT INTO `72crm_crm_area` VALUES (211103, 211100, '兴隆台区');
INSERT INTO `72crm_crm_area` VALUES (211102, 211100, '双台子区');
INSERT INTO `72crm_crm_area` VALUES (530602, 530600, '昭阳区');
INSERT INTO `72crm_crm_area` VALUES (530621, 530600, '鲁甸县');
INSERT INTO `72crm_crm_area` VALUES (530622, 530600, '巧家县');
INSERT INTO `72crm_crm_area` VALUES (530623, 530600, '盐津县');
INSERT INTO `72crm_crm_area` VALUES (530624, 530600, '大关县');
INSERT INTO `72crm_crm_area` VALUES (530625, 530600, '永善县');
INSERT INTO `72crm_crm_area` VALUES (530626, 530600, '绥江县');
INSERT INTO `72crm_crm_area` VALUES (530627, 530600, '镇雄县');
INSERT INTO `72crm_crm_area` VALUES (530628, 530600, '彝良县');
INSERT INTO `72crm_crm_area` VALUES (530629, 530600, '威信县');
INSERT INTO `72crm_crm_area` VALUES (530630, 530600, '水富县');
INSERT INTO `72crm_crm_area` VALUES (420100, 420000, '武汉市');
INSERT INTO `72crm_crm_area` VALUES (420900, 420000, '孝感市');
INSERT INTO `72crm_crm_area` VALUES (420800, 420000, '荆门市');
INSERT INTO `72crm_crm_area` VALUES (420300, 420000, '十堰市');
INSERT INTO `72crm_crm_area` VALUES (421100, 420000, '黄冈市');
INSERT INTO `72crm_crm_area` VALUES (420200, 420000, '黄石市');
INSERT INTO `72crm_crm_area` VALUES (421000, 420000, '荆州市');
INSERT INTO `72crm_crm_area` VALUES (429000, 420000, '省直辖县级行政单位');
INSERT INTO `72crm_crm_area` VALUES (420500, 420000, '宜昌市');
INSERT INTO `72crm_crm_area` VALUES (421300, 420000, '随州市');
INSERT INTO `72crm_crm_area` VALUES (421200, 420000, '咸宁市');
INSERT INTO `72crm_crm_area` VALUES (422800, 420000, '恩施土家族苗族自治州');
INSERT INTO `72crm_crm_area` VALUES (420700, 420000, '鄂州市');
INSERT INTO `72crm_crm_area` VALUES (420600, 420000, '襄阳市');
INSERT INTO `72crm_crm_area` VALUES (650402, 650400, '高昌区');
INSERT INTO `72crm_crm_area` VALUES (650421, 650400, '鄯善县');
INSERT INTO `72crm_crm_area` VALUES (650422, 650400, '托克逊县');
INSERT INTO `72crm_crm_area` VALUES (150722, 150700, '莫力达瓦达斡尔族自治旗');
INSERT INTO `72crm_crm_area` VALUES (150723, 150700, '鄂伦春自治旗');
INSERT INTO `72crm_crm_area` VALUES (150784, 150700, '额尔古纳市');
INSERT INTO `72crm_crm_area` VALUES (150721, 150700, '阿荣旗');
INSERT INTO `72crm_crm_area` VALUES (150785, 150700, '根河市');
INSERT INTO `72crm_crm_area` VALUES (150726, 150700, '新巴尔虎左旗');
INSERT INTO `72crm_crm_area` VALUES (150727, 150700, '新巴尔虎右旗');
INSERT INTO `72crm_crm_area` VALUES (150724, 150700, '鄂温克族自治旗');
INSERT INTO `72crm_crm_area` VALUES (150725, 150700, '陈巴尔虎旗');
INSERT INTO `72crm_crm_area` VALUES (150702, 150700, '海拉尔区');
INSERT INTO `72crm_crm_area` VALUES (150703, 150700, '扎赉诺尔区');
INSERT INTO `72crm_crm_area` VALUES (150782, 150700, '牙克石市');
INSERT INTO `72crm_crm_area` VALUES (150783, 150700, '扎兰屯市');
INSERT INTO `72crm_crm_area` VALUES (150781, 150700, '满洲里市');
INSERT INTO `72crm_crm_area` VALUES (440514, 440500, '潮南区');
INSERT INTO `72crm_crm_area` VALUES (440515, 440500, '澄海区');
INSERT INTO `72crm_crm_area` VALUES (440512, 440500, '濠江区');
INSERT INTO `72crm_crm_area` VALUES (440513, 440500, '潮阳区');
INSERT INTO `72crm_crm_area` VALUES (440511, 440500, '金平区');
INSERT INTO `72crm_crm_area` VALUES (440507, 440500, '龙湖区');
INSERT INTO `72crm_crm_area` VALUES (440523, 440500, '南澳县');
INSERT INTO `72crm_crm_area` VALUES (410822, 410800, '博爱县');
INSERT INTO `72crm_crm_area` VALUES (410823, 410800, '武陟县');
INSERT INTO `72crm_crm_area` VALUES (410804, 410800, '马村区');
INSERT INTO `72crm_crm_area` VALUES (410821, 410800, '修武县');
INSERT INTO `72crm_crm_area` VALUES (410802, 410800, '解放区');
INSERT INTO `72crm_crm_area` VALUES (410882, 410800, '沁阳市');
INSERT INTO `72crm_crm_area` VALUES (410803, 410800, '中站区');
INSERT INTO `72crm_crm_area` VALUES (410883, 410800, '孟州市');
INSERT INTO `72crm_crm_area` VALUES (410811, 410800, '山阳区');
INSERT INTO `72crm_crm_area` VALUES (410825, 410800, '温县');
INSERT INTO `72crm_crm_area` VALUES (210115, 210100, '辽中区');
INSERT INTO `72crm_crm_area` VALUES (210114, 210100, '于洪区');
INSERT INTO `72crm_crm_area` VALUES (210113, 210100, '沈北新区');
INSERT INTO `72crm_crm_area` VALUES (210112, 210100, '浑南区');
INSERT INTO `72crm_crm_area` VALUES (210181, 210100, '新民市');
INSERT INTO `72crm_crm_area` VALUES (210123, 210100, '康平县');
INSERT INTO `72crm_crm_area` VALUES (210124, 210100, '法库县');
INSERT INTO `72crm_crm_area` VALUES (210103, 210100, '沈河区');
INSERT INTO `72crm_crm_area` VALUES (210102, 210100, '和平区');
INSERT INTO `72crm_crm_area` VALUES (210106, 210100, '铁西区');
INSERT INTO `72crm_crm_area` VALUES (210105, 210100, '皇姑区');
INSERT INTO `72crm_crm_area` VALUES (210104, 210100, '大东区');
INSERT INTO `72crm_crm_area` VALUES (210111, 210100, '苏家屯区');
INSERT INTO `72crm_crm_area` VALUES (513223, 513200, '茂县');
INSERT INTO `72crm_crm_area` VALUES (513222, 513200, '理县');
INSERT INTO `72crm_crm_area` VALUES (513221, 513200, '汶川县');
INSERT INTO `72crm_crm_area` VALUES (513231, 513200, '阿坝县');
INSERT INTO `72crm_crm_area` VALUES (513230, 513200, '壤塘县');
INSERT INTO `72crm_crm_area` VALUES (513228, 513200, '黑水县');
INSERT INTO `72crm_crm_area` VALUES (513227, 513200, '小金县');
INSERT INTO `72crm_crm_area` VALUES (513226, 513200, '金川县');
INSERT INTO `72crm_crm_area` VALUES (513225, 513200, '九寨沟县');
INSERT INTO `72crm_crm_area` VALUES (513224, 513200, '松潘县');
INSERT INTO `72crm_crm_area` VALUES (513201, 513200, '马尔康市');
INSERT INTO `72crm_crm_area` VALUES (513233, 513200, '红原县');
INSERT INTO `72crm_crm_area` VALUES (513232, 513200, '若尔盖县');
INSERT INTO `72crm_crm_area` VALUES (320724, 320700, '灌南县');
INSERT INTO `72crm_crm_area` VALUES (320706, 320700, '海州区');
INSERT INTO `72crm_crm_area` VALUES (320722, 320700, '东海县');
INSERT INTO `72crm_crm_area` VALUES (320707, 320700, '赣榆区');
INSERT INTO `72crm_crm_area` VALUES (320723, 320700, '灌云县');
INSERT INTO `72crm_crm_area` VALUES (320703, 320700, '连云区');
INSERT INTO `72crm_crm_area` VALUES (419001, 419000, '济源市');
INSERT INTO `72crm_crm_area` VALUES (120100, 120000, '天津城区');
INSERT INTO `72crm_crm_area` VALUES (640202, 640200, '大武口区');
INSERT INTO `72crm_crm_area` VALUES (640205, 640200, '惠农区');
INSERT INTO `72crm_crm_area` VALUES (640221, 640200, '平罗县');
INSERT INTO `72crm_crm_area` VALUES (431302, 431300, '娄星区');
INSERT INTO `72crm_crm_area` VALUES (431382, 431300, '涟源市');
INSERT INTO `72crm_crm_area` VALUES (431381, 431300, '冷水江市');
INSERT INTO `72crm_crm_area` VALUES (431322, 431300, '新化县');
INSERT INTO `72crm_crm_area` VALUES (431321, 431300, '双峰县');
INSERT INTO `72crm_crm_area` VALUES (222403, 222400, '敦化市');
INSERT INTO `72crm_crm_area` VALUES (222402, 222400, '图们市');
INSERT INTO `72crm_crm_area` VALUES (222401, 222400, '延吉市');
INSERT INTO `72crm_crm_area` VALUES (222406, 222400, '和龙市');
INSERT INTO `72crm_crm_area` VALUES (222405, 222400, '龙井市');
INSERT INTO `72crm_crm_area` VALUES (222404, 222400, '珲春市');
INSERT INTO `72crm_crm_area` VALUES (222426, 222400, '安图县');
INSERT INTO `72crm_crm_area` VALUES (222424, 222400, '汪清县');
INSERT INTO `72crm_crm_area` VALUES (350421, 350400, '明溪县');
INSERT INTO `72crm_crm_area` VALUES (350423, 350400, '清流县');
INSERT INTO `72crm_crm_area` VALUES (350481, 350400, '永安市');
INSERT INTO `72crm_crm_area` VALUES (350403, 350400, '三元区');
INSERT INTO `72crm_crm_area` VALUES (350402, 350400, '梅列区');
INSERT INTO `72crm_crm_area` VALUES (350429, 350400, '泰宁县');
INSERT INTO `72crm_crm_area` VALUES (350428, 350400, '将乐县');
INSERT INTO `72crm_crm_area` VALUES (350430, 350400, '建宁县');
INSERT INTO `72crm_crm_area` VALUES (350425, 350400, '大田县');
INSERT INTO `72crm_crm_area` VALUES (350424, 350400, '宁化县');
INSERT INTO `72crm_crm_area` VALUES (350427, 350400, '沙县');
INSERT INTO `72crm_crm_area` VALUES (350426, 350400, '尤溪县');
INSERT INTO `72crm_crm_area` VALUES (230624, 230600, '杜尔伯特蒙古族自治县');
INSERT INTO `72crm_crm_area` VALUES (230603, 230600, '龙凤区');
INSERT INTO `72crm_crm_area` VALUES (230602, 230600, '萨尔图区');
INSERT INTO `72crm_crm_area` VALUES (230623, 230600, '林甸县');
INSERT INTO `72crm_crm_area` VALUES (230606, 230600, '大同区');
INSERT INTO `72crm_crm_area` VALUES (230622, 230600, '肇源县');
INSERT INTO `72crm_crm_area` VALUES (230605, 230600, '红岗区');
INSERT INTO `72crm_crm_area` VALUES (230621, 230600, '肇州县');
INSERT INTO `72crm_crm_area` VALUES (230604, 230600, '让胡路区');
INSERT INTO `72crm_crm_area` VALUES (610523, 610500, '大荔县');
INSERT INTO `72crm_crm_area` VALUES (610522, 610500, '潼关县');
INSERT INTO `72crm_crm_area` VALUES (610525, 610500, '澄城县');
INSERT INTO `72crm_crm_area` VALUES (610524, 610500, '合阳县');
INSERT INTO `72crm_crm_area` VALUES (610527, 610500, '白水县');
INSERT INTO `72crm_crm_area` VALUES (610526, 610500, '蒲城县');
INSERT INTO `72crm_crm_area` VALUES (610528, 610500, '富平县');
INSERT INTO `72crm_crm_area` VALUES (610581, 610500, '韩城市');
INSERT INTO `72crm_crm_area` VALUES (610503, 610500, '华州区');
INSERT INTO `72crm_crm_area` VALUES (610502, 610500, '临渭区');
INSERT INTO `72crm_crm_area` VALUES (610582, 610500, '华阴市');
INSERT INTO `72crm_crm_area` VALUES (370983, 370900, '肥城市');
INSERT INTO `72crm_crm_area` VALUES (370902, 370900, '泰山区');
INSERT INTO `72crm_crm_area` VALUES (370982, 370900, '新泰市');
INSERT INTO `72crm_crm_area` VALUES (370911, 370900, '岱岳区');
INSERT INTO `72crm_crm_area` VALUES (370921, 370900, '宁阳县');
INSERT INTO `72crm_crm_area` VALUES (370923, 370900, '东平县');
INSERT INTO `72crm_crm_area` VALUES (341221, 341200, '临泉县');
INSERT INTO `72crm_crm_area` VALUES (341204, 341200, '颍泉区');
INSERT INTO `72crm_crm_area` VALUES (341222, 341200, '太和县');
INSERT INTO `72crm_crm_area` VALUES (341203, 341200, '颍东区');
INSERT INTO `72crm_crm_area` VALUES (341202, 341200, '颍州区');
INSERT INTO `72crm_crm_area` VALUES (341282, 341200, '界首市');
INSERT INTO `72crm_crm_area` VALUES (341225, 341200, '阜南县');
INSERT INTO `72crm_crm_area` VALUES (341226, 341200, '颍上县');
INSERT INTO `72crm_crm_area` VALUES (140502, 140500, '城区');
INSERT INTO `72crm_crm_area` VALUES (140581, 140500, '高平市');
INSERT INTO `72crm_crm_area` VALUES (140522, 140500, '阳城县');
INSERT INTO `72crm_crm_area` VALUES (140521, 140500, '沁水县');
INSERT INTO `72crm_crm_area` VALUES (140524, 140500, '陵川县');
INSERT INTO `72crm_crm_area` VALUES (140525, 140500, '泽州县');
INSERT INTO `72crm_crm_area` VALUES (711982, 711900, '番路乡');
INSERT INTO `72crm_crm_area` VALUES (711983, 711900, '梅山乡');
INSERT INTO `72crm_crm_area` VALUES (711994, 711900, '新港乡');
INSERT INTO `72crm_crm_area` VALUES (711995, 711900, '民雄乡');
INSERT INTO `72crm_crm_area` VALUES (711992, 711900, '东石乡');
INSERT INTO `72crm_crm_area` VALUES (711993, 711900, '六脚乡');
INSERT INTO `72crm_crm_area` VALUES (711998, 711900, '义竹乡');
INSERT INTO `72crm_crm_area` VALUES (711999, 711900, '布袋镇');
INSERT INTO `72crm_crm_area` VALUES (711996, 711900, '大林镇');
INSERT INTO `72crm_crm_area` VALUES (711997, 711900, '溪口乡');
INSERT INTO `72crm_crm_area` VALUES (711986, 711900, '中埔乡');
INSERT INTO `72crm_crm_area` VALUES (711987, 711900, '大埔乡');
INSERT INTO `72crm_crm_area` VALUES (711984, 711900, '竹崎乡');
INSERT INTO `72crm_crm_area` VALUES (711985, 711900, '阿里山乡');
INSERT INTO `72crm_crm_area` VALUES (711990, 711900, '太保市');
INSERT INTO `72crm_crm_area` VALUES (711991, 711900, '朴子市');
INSERT INTO `72crm_crm_area` VALUES (711988, 711900, '水上乡');
INSERT INTO `72crm_crm_area` VALUES (711989, 711900, '鹿草乡');
INSERT INTO `72crm_crm_area` VALUES (520423, 520400, '镇宁布依族苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (520422, 520400, '普定县');
INSERT INTO `72crm_crm_area` VALUES (520403, 520400, '平坝区');
INSERT INTO `72crm_crm_area` VALUES (520402, 520400, '西秀区');
INSERT INTO `72crm_crm_area` VALUES (520425, 520400, '紫云苗族布依族自治县');
INSERT INTO `72crm_crm_area` VALUES (520424, 520400, '关岭布依族苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (430304, 430300, '岳塘区');
INSERT INTO `72crm_crm_area` VALUES (430321, 430300, '湘潭县');
INSERT INTO `72crm_crm_area` VALUES (430302, 430300, '雨湖区');
INSERT INTO `72crm_crm_area` VALUES (430382, 430300, '韶山市');
INSERT INTO `72crm_crm_area` VALUES (430381, 430300, '湘乡市');
INSERT INTO `72crm_crm_area` VALUES (460100, 460000, '海口市');
INSERT INTO `72crm_crm_area` VALUES (460400, 460000, '儋州市');
INSERT INTO `72crm_crm_area` VALUES (460300, 460000, '三沙市');
INSERT INTO `72crm_crm_area` VALUES (460200, 460000, '三亚市');
INSERT INTO `72crm_crm_area` VALUES (469000, 460000, '省直辖县级行政单位');
INSERT INTO `72crm_crm_area` VALUES (421126, 421100, '蕲春县');
INSERT INTO `72crm_crm_area` VALUES (421127, 421100, '黄梅县');
INSERT INTO `72crm_crm_area` VALUES (421124, 421100, '英山县');
INSERT INTO `72crm_crm_area` VALUES (421125, 421100, '浠水县');
INSERT INTO `72crm_crm_area` VALUES (421122, 421100, '红安县');
INSERT INTO `72crm_crm_area` VALUES (421123, 421100, '罗田县');
INSERT INTO `72crm_crm_area` VALUES (421121, 421100, '团风县');
INSERT INTO `72crm_crm_area` VALUES (421102, 421100, '黄州区');
INSERT INTO `72crm_crm_area` VALUES (421182, 421100, '武穴市');
INSERT INTO `72crm_crm_area` VALUES (421181, 421100, '麻城市');
INSERT INTO `72crm_crm_area` VALUES (340225, 340200, '无为县');
INSERT INTO `72crm_crm_area` VALUES (340208, 340200, '三山区');
INSERT INTO `72crm_crm_area` VALUES (340221, 340200, '芜湖县');
INSERT INTO `72crm_crm_area` VALUES (340207, 340200, '鸠江区');
INSERT INTO `72crm_crm_area` VALUES (340223, 340200, '南陵县');
INSERT INTO `72crm_crm_area` VALUES (340222, 340200, '繁昌县');
INSERT INTO `72crm_crm_area` VALUES (340203, 340200, '弋江区');
INSERT INTO `72crm_crm_area` VALUES (340202, 340200, '镜湖区');
INSERT INTO `72crm_crm_area` VALUES (220403, 220400, '西安区');
INSERT INTO `72crm_crm_area` VALUES (220402, 220400, '龙山区');
INSERT INTO `72crm_crm_area` VALUES (220422, 220400, '东辽县');
INSERT INTO `72crm_crm_area` VALUES (220421, 220400, '东丰县');
INSERT INTO `72crm_crm_area` VALUES (450804, 450800, '覃塘区');
INSERT INTO `72crm_crm_area` VALUES (450821, 450800, '平南县');
INSERT INTO `72crm_crm_area` VALUES (450802, 450800, '港北区');
INSERT INTO `72crm_crm_area` VALUES (450803, 450800, '港南区');
INSERT INTO `72crm_crm_area` VALUES (450881, 450800, '桂平市');
INSERT INTO `72crm_crm_area` VALUES (360704, 360700, '赣县区');
INSERT INTO `72crm_crm_area` VALUES (360781, 360700, '瑞金市');
INSERT INTO `72crm_crm_area` VALUES (360725, 360700, '崇义县');
INSERT INTO `72crm_crm_area` VALUES (360724, 360700, '上犹县');
INSERT INTO `72crm_crm_area` VALUES (360727, 360700, '龙南县');
INSERT INTO `72crm_crm_area` VALUES (360726, 360700, '安远县');
INSERT INTO `72crm_crm_area` VALUES (360723, 360700, '大余县');
INSERT INTO `72crm_crm_area` VALUES (360722, 360700, '信丰县');
INSERT INTO `72crm_crm_area` VALUES (360733, 360700, '会昌县');
INSERT INTO `72crm_crm_area` VALUES (360732, 360700, '兴国县');
INSERT INTO `72crm_crm_area` VALUES (360703, 360700, '南康区');
INSERT INTO `72crm_crm_area` VALUES (360735, 360700, '石城县');
INSERT INTO `72crm_crm_area` VALUES (360702, 360700, '章贡区');
INSERT INTO `72crm_crm_area` VALUES (360734, 360700, '寻乌县');
INSERT INTO `72crm_crm_area` VALUES (360729, 360700, '全南县');
INSERT INTO `72crm_crm_area` VALUES (360728, 360700, '定南县');
INSERT INTO `72crm_crm_area` VALUES (360731, 360700, '于都县');
INSERT INTO `72crm_crm_area` VALUES (360730, 360700, '宁都县');
INSERT INTO `72crm_crm_area` VALUES (630200, 630000, '海东市');
INSERT INTO `72crm_crm_area` VALUES (632200, 630000, '海北藏族自治州');
INSERT INTO `72crm_crm_area` VALUES (632600, 630000, '果洛藏族自治州');
INSERT INTO `72crm_crm_area` VALUES (632300, 630000, '黄南藏族自治州');
INSERT INTO `72crm_crm_area` VALUES (632700, 630000, '玉树藏族自治州');
INSERT INTO `72crm_crm_area` VALUES (632800, 630000, '海西蒙古族藏族自治州');
INSERT INTO `72crm_crm_area` VALUES (630100, 630000, '西宁市');
INSERT INTO `72crm_crm_area` VALUES (632500, 630000, '海南藏族自治州');
INSERT INTO `72crm_crm_area` VALUES (130321, 130300, '青龙满族自治县');
INSERT INTO `72crm_crm_area` VALUES (130304, 130300, '北戴河区');
INSERT INTO `72crm_crm_area` VALUES (130306, 130300, '抚宁区');
INSERT INTO `72crm_crm_area` VALUES (130322, 130300, '昌黎县');
INSERT INTO `72crm_crm_area` VALUES (130324, 130300, '卢龙县');
INSERT INTO `72crm_crm_area` VALUES (130303, 130300, '山海关区');
INSERT INTO `72crm_crm_area` VALUES (130302, 130300, '海港区');
INSERT INTO `72crm_crm_area` VALUES (331024, 331000, '仙居县');
INSERT INTO `72crm_crm_area` VALUES (331021, 331000, '玉环市');
INSERT INTO `72crm_crm_area` VALUES (331004, 331000, '路桥区');
INSERT INTO `72crm_crm_area` VALUES (331023, 331000, '天台县');
INSERT INTO `72crm_crm_area` VALUES (331022, 331000, '三门县');
INSERT INTO `72crm_crm_area` VALUES (331081, 331000, '温岭市');
INSERT INTO `72crm_crm_area` VALUES (331003, 331000, '黄岩区');
INSERT INTO `72crm_crm_area` VALUES (331002, 331000, '椒江区');
INSERT INTO `72crm_crm_area` VALUES (331082, 331000, '临海市');
INSERT INTO `72crm_crm_area` VALUES (710902, 710900, '西区');
INSERT INTO `72crm_crm_area` VALUES (710999, 710900, '其它区');
INSERT INTO `72crm_crm_area` VALUES (710901, 710900, '东区');
INSERT INTO `72crm_crm_area` VALUES (232723, 232700, '漠河县');
INSERT INTO `72crm_crm_area` VALUES (232722, 232700, '塔河县');
INSERT INTO `72crm_crm_area` VALUES (232721, 232700, '呼玛县');
INSERT INTO `72crm_crm_area` VALUES (232704, 232700, '呼中区');
INSERT INTO `72crm_crm_area` VALUES (232703, 232700, '新林区');
INSERT INTO `72crm_crm_area` VALUES (232702, 232700, '松岭区');
INSERT INTO `72crm_crm_area` VALUES (232701, 232700, '加格达奇区');
INSERT INTO `72crm_crm_area` VALUES (420102, 420100, '江岸区');
INSERT INTO `72crm_crm_area` VALUES (420103, 420100, '江汉区');
INSERT INTO `72crm_crm_area` VALUES (420111, 420100, '洪山区');
INSERT INTO `72crm_crm_area` VALUES (420106, 420100, '武昌区');
INSERT INTO `72crm_crm_area` VALUES (420107, 420100, '青山区');
INSERT INTO `72crm_crm_area` VALUES (420104, 420100, '硚口区');
INSERT INTO `72crm_crm_area` VALUES (420105, 420100, '汉阳区');
INSERT INTO `72crm_crm_area` VALUES (420116, 420100, '黄陂区');
INSERT INTO `72crm_crm_area` VALUES (420117, 420100, '新洲区');
INSERT INTO `72crm_crm_area` VALUES (420114, 420100, '蔡甸区');
INSERT INTO `72crm_crm_area` VALUES (420115, 420100, '江夏区');
INSERT INTO `72crm_crm_area` VALUES (420112, 420100, '东西湖区');
INSERT INTO `72crm_crm_area` VALUES (420113, 420100, '汉南区');
INSERT INTO `72crm_crm_area` VALUES (211202, 211200, '银州区');
INSERT INTO `72crm_crm_area` VALUES (211282, 211200, '开原市');
INSERT INTO `72crm_crm_area` VALUES (211281, 211200, '调兵山市');
INSERT INTO `72crm_crm_area` VALUES (211223, 211200, '西丰县');
INSERT INTO `72crm_crm_area` VALUES (211221, 211200, '铁岭县');
INSERT INTO `72crm_crm_area` VALUES (211204, 211200, '清河区');
INSERT INTO `72crm_crm_area` VALUES (211224, 211200, '昌图县');
INSERT INTO `72crm_crm_area` VALUES (530702, 530700, '古城区');
INSERT INTO `72crm_crm_area` VALUES (530721, 530700, '玉龙纳西族自治县');
INSERT INTO `72crm_crm_area` VALUES (530722, 530700, '永胜县');
INSERT INTO `72crm_crm_area` VALUES (530723, 530700, '华坪县');
INSERT INTO `72crm_crm_area` VALUES (530724, 530700, '宁蒗彝族自治县');
INSERT INTO `72crm_crm_area` VALUES (441622, 441600, '龙川县');
INSERT INTO `72crm_crm_area` VALUES (441623, 441600, '连平县');
INSERT INTO `72crm_crm_area` VALUES (441621, 441600, '紫金县');
INSERT INTO `72crm_crm_area` VALUES (441602, 441600, '源城区');
INSERT INTO `72crm_crm_area` VALUES (441624, 441600, '和平县');
INSERT INTO `72crm_crm_area` VALUES (441625, 441600, '东源县');
INSERT INTO `72crm_crm_area` VALUES (620825, 620800, '庄浪县');
INSERT INTO `72crm_crm_area` VALUES (620824, 620800, '华亭县');
INSERT INTO `72crm_crm_area` VALUES (620826, 620800, '静宁县');
INSERT INTO `72crm_crm_area` VALUES (620802, 620800, '崆峒区');
INSERT INTO `72crm_crm_area` VALUES (620821, 620800, '泾川县');
INSERT INTO `72crm_crm_area` VALUES (620823, 620800, '崇信县');
INSERT INTO `72crm_crm_area` VALUES (620822, 620800, '灵台县');
INSERT INTO `72crm_crm_area` VALUES (650521, 650500, '巴里坤哈萨克自治县');
INSERT INTO `72crm_crm_area` VALUES (650522, 650500, '伊吾县');
INSERT INTO `72crm_crm_area` VALUES (650502, 650500, '伊州区');
INSERT INTO `72crm_crm_area` VALUES (150802, 150800, '临河区');
INSERT INTO `72crm_crm_area` VALUES (150822, 150800, '磴口县');
INSERT INTO `72crm_crm_area` VALUES (150823, 150800, '乌拉特前旗');
INSERT INTO `72crm_crm_area` VALUES (150821, 150800, '五原县');
INSERT INTO `72crm_crm_area` VALUES (150826, 150800, '杭锦后旗');
INSERT INTO `72crm_crm_area` VALUES (150824, 150800, '乌拉特中旗');
INSERT INTO `72crm_crm_area` VALUES (150825, 150800, '乌拉特后旗');
INSERT INTO `72crm_crm_area` VALUES (410902, 410900, '华龙区');
INSERT INTO `72crm_crm_area` VALUES (410928, 410900, '濮阳县');
INSERT INTO `72crm_crm_area` VALUES (410926, 410900, '范县');
INSERT INTO `72crm_crm_area` VALUES (410927, 410900, '台前县');
INSERT INTO `72crm_crm_area` VALUES (410922, 410900, '清丰县');
INSERT INTO `72crm_crm_area` VALUES (410923, 410900, '南乐县');
INSERT INTO `72crm_crm_area` VALUES (513327, 513300, '炉霍县');
INSERT INTO `72crm_crm_area` VALUES (513326, 513300, '道孚县');
INSERT INTO `72crm_crm_area` VALUES (513325, 513300, '雅江县');
INSERT INTO `72crm_crm_area` VALUES (513324, 513300, '九龙县');
INSERT INTO `72crm_crm_area` VALUES (513323, 513300, '丹巴县');
INSERT INTO `72crm_crm_area` VALUES (513322, 513300, '泸定县');
INSERT INTO `72crm_crm_area` VALUES (513335, 513300, '巴塘县');
INSERT INTO `72crm_crm_area` VALUES (513334, 513300, '理塘县');
INSERT INTO `72crm_crm_area` VALUES (513301, 513300, '康定市');
INSERT INTO `72crm_crm_area` VALUES (513333, 513300, '色达县');
INSERT INTO `72crm_crm_area` VALUES (513332, 513300, '石渠县');
INSERT INTO `72crm_crm_area` VALUES (513331, 513300, '白玉县');
INSERT INTO `72crm_crm_area` VALUES (513330, 513300, '德格县');
INSERT INTO `72crm_crm_area` VALUES (513329, 513300, '新龙县');
INSERT INTO `72crm_crm_area` VALUES (513328, 513300, '甘孜县');
INSERT INTO `72crm_crm_area` VALUES (513338, 513300, '得荣县');
INSERT INTO `72crm_crm_area` VALUES (513337, 513300, '稻城县');
INSERT INTO `72crm_crm_area` VALUES (513336, 513300, '乡城县');
INSERT INTO `72crm_crm_area` VALUES (330100, 330000, '杭州市');
INSERT INTO `72crm_crm_area` VALUES (330500, 330000, '湖州市');
INSERT INTO `72crm_crm_area` VALUES (330900, 330000, '舟山市');
INSERT INTO `72crm_crm_area` VALUES (330400, 330000, '嘉兴市');
INSERT INTO `72crm_crm_area` VALUES (330800, 330000, '衢州市');
INSERT INTO `72crm_crm_area` VALUES (330300, 330000, '温州市');
INSERT INTO `72crm_crm_area` VALUES (330700, 330000, '金华市');
INSERT INTO `72crm_crm_area` VALUES (331100, 330000, '丽水市');
INSERT INTO `72crm_crm_area` VALUES (330200, 330000, '宁波市');
INSERT INTO `72crm_crm_area` VALUES (330600, 330000, '绍兴市');
INSERT INTO `72crm_crm_area` VALUES (331000, 330000, '台州市');
INSERT INTO `72crm_crm_area` VALUES (210211, 210200, '甘井子区');
INSERT INTO `72crm_crm_area` VALUES (210224, 210200, '长海县');
INSERT INTO `72crm_crm_area` VALUES (210214, 210200, '普兰店区');
INSERT INTO `72crm_crm_area` VALUES (210213, 210200, '金州区');
INSERT INTO `72crm_crm_area` VALUES (210212, 210200, '旅顺口区');
INSERT INTO `72crm_crm_area` VALUES (210203, 210200, '西岗区');
INSERT INTO `72crm_crm_area` VALUES (210283, 210200, '庄河市');
INSERT INTO `72crm_crm_area` VALUES (210202, 210200, '中山区');
INSERT INTO `72crm_crm_area` VALUES (210281, 210200, '瓦房店市');
INSERT INTO `72crm_crm_area` VALUES (210204, 210200, '沙河口区');
INSERT INTO `72crm_crm_area` VALUES (440608, 440600, '高明区');
INSERT INTO `72crm_crm_area` VALUES (440606, 440600, '顺德区');
INSERT INTO `72crm_crm_area` VALUES (440607, 440600, '三水区');
INSERT INTO `72crm_crm_area` VALUES (440604, 440600, '禅城区');
INSERT INTO `72crm_crm_area` VALUES (440605, 440600, '南海区');
INSERT INTO `72crm_crm_area` VALUES (350581, 350500, '石狮市');
INSERT INTO `72crm_crm_area` VALUES (350503, 350500, '丰泽区');
INSERT INTO `72crm_crm_area` VALUES (350583, 350500, '南安市');
INSERT INTO `72crm_crm_area` VALUES (350502, 350500, '鲤城区');
INSERT INTO `72crm_crm_area` VALUES (350582, 350500, '晋江市');
INSERT INTO `72crm_crm_area` VALUES (350525, 350500, '永春县');
INSERT INTO `72crm_crm_area` VALUES (350524, 350500, '安溪县');
INSERT INTO `72crm_crm_area` VALUES (350527, 350500, '金门县');
INSERT INTO `72crm_crm_area` VALUES (350526, 350500, '德化县');
INSERT INTO `72crm_crm_area` VALUES (350505, 350500, '泉港区');
INSERT INTO `72crm_crm_area` VALUES (350521, 350500, '惠安县');
INSERT INTO `72crm_crm_area` VALUES (350504, 350500, '洛江区');
INSERT INTO `72crm_crm_area` VALUES (610632, 610600, '黄陵县');
INSERT INTO `72crm_crm_area` VALUES (610603, 610600, '安塞区');
INSERT INTO `72crm_crm_area` VALUES (610602, 610600, '宝塔区');
INSERT INTO `72crm_crm_area` VALUES (610625, 610600, '志丹县');
INSERT INTO `72crm_crm_area` VALUES (610627, 610600, '甘泉县');
INSERT INTO `72crm_crm_area` VALUES (610626, 610600, '吴起县');
INSERT INTO `72crm_crm_area` VALUES (610629, 610600, '洛川县');
INSERT INTO `72crm_crm_area` VALUES (610628, 610600, '富县');
INSERT INTO `72crm_crm_area` VALUES (610631, 610600, '黄龙县');
INSERT INTO `72crm_crm_area` VALUES (610630, 610600, '宜川县');
INSERT INTO `72crm_crm_area` VALUES (610621, 610600, '延长县');
INSERT INTO `72crm_crm_area` VALUES (610623, 610600, '子长县');
INSERT INTO `72crm_crm_area` VALUES (610622, 610600, '延川县');
INSERT INTO `72crm_crm_area` VALUES (320804, 320800, '淮阴区');
INSERT INTO `72crm_crm_area` VALUES (320803, 320800, '淮安区');
INSERT INTO `72crm_crm_area` VALUES (320812, 320800, '清江浦区');
INSERT INTO `72crm_crm_area` VALUES (320813, 320800, '洪泽区');
INSERT INTO `72crm_crm_area` VALUES (320830, 320800, '盱眙县');
INSERT INTO `72crm_crm_area` VALUES (320831, 320800, '金湖县');
INSERT INTO `72crm_crm_area` VALUES (320826, 320800, '涟水县');
INSERT INTO `72crm_crm_area` VALUES (120101, 120100, '和平区');
INSERT INTO `72crm_crm_area` VALUES (120103, 120100, '河西区');
INSERT INTO `72crm_crm_area` VALUES (120102, 120100, '河东区');
INSERT INTO `72crm_crm_area` VALUES (120105, 120100, '河北区');
INSERT INTO `72crm_crm_area` VALUES (120104, 120100, '南开区');
INSERT INTO `72crm_crm_area` VALUES (120106, 120100, '红桥区');
INSERT INTO `72crm_crm_area` VALUES (120111, 120100, '西青区');
INSERT INTO `72crm_crm_area` VALUES (120110, 120100, '东丽区');
INSERT INTO `72crm_crm_area` VALUES (120113, 120100, '北辰区');
INSERT INTO `72crm_crm_area` VALUES (120112, 120100, '津南区');
INSERT INTO `72crm_crm_area` VALUES (120115, 120100, '宝坻区');
INSERT INTO `72crm_crm_area` VALUES (120114, 120100, '武清区');
INSERT INTO `72crm_crm_area` VALUES (120117, 120100, '宁河区');
INSERT INTO `72crm_crm_area` VALUES (120116, 120100, '滨海新区');
INSERT INTO `72crm_crm_area` VALUES (120119, 120100, '蓟州区');
INSERT INTO `72crm_crm_area` VALUES (120118, 120100, '静海区');
INSERT INTO `72crm_crm_area` VALUES (640381, 640300, '青铜峡市');
INSERT INTO `72crm_crm_area` VALUES (640303, 640300, '红寺堡区');
INSERT INTO `72crm_crm_area` VALUES (640302, 640300, '利通区');
INSERT INTO `72crm_crm_area` VALUES (640323, 640300, '盐池县');
INSERT INTO `72crm_crm_area` VALUES (640324, 640300, '同心县');
INSERT INTO `72crm_crm_area` VALUES (500100, 500000, '市辖区');
INSERT INTO `72crm_crm_area` VALUES (500200, 500000, '县');
INSERT INTO `72crm_crm_area` VALUES (230722, 230700, '嘉荫县');
INSERT INTO `72crm_crm_area` VALUES (230703, 230700, '南岔区');
INSERT INTO `72crm_crm_area` VALUES (230702, 230700, '伊春区');
INSERT INTO `72crm_crm_area` VALUES (230707, 230700, '新青区');
INSERT INTO `72crm_crm_area` VALUES (230706, 230700, '翠峦区');
INSERT INTO `72crm_crm_area` VALUES (230705, 230700, '西林区');
INSERT INTO `72crm_crm_area` VALUES (230704, 230700, '友好区');
INSERT INTO `72crm_crm_area` VALUES (230711, 230700, '乌马河区');
INSERT INTO `72crm_crm_area` VALUES (230710, 230700, '五营区');
INSERT INTO `72crm_crm_area` VALUES (230709, 230700, '金山屯区');
INSERT INTO `72crm_crm_area` VALUES (230708, 230700, '美溪区');
INSERT INTO `72crm_crm_area` VALUES (230715, 230700, '红星区');
INSERT INTO `72crm_crm_area` VALUES (230714, 230700, '乌伊岭区');
INSERT INTO `72crm_crm_area` VALUES (230713, 230700, '带岭区');
INSERT INTO `72crm_crm_area` VALUES (230712, 230700, '汤旺河区');
INSERT INTO `72crm_crm_area` VALUES (230781, 230700, '铁力市');
INSERT INTO `72crm_crm_area` VALUES (230716, 230700, '上甘岭区');
INSERT INTO `72crm_crm_area` VALUES (341302, 341300, '埇桥区');
INSERT INTO `72crm_crm_area` VALUES (341324, 341300, '泗县');
INSERT INTO `72crm_crm_area` VALUES (341321, 341300, '砀山县');
INSERT INTO `72crm_crm_area` VALUES (341323, 341300, '灵璧县');
INSERT INTO `72crm_crm_area` VALUES (341322, 341300, '萧县');
INSERT INTO `72crm_crm_area` VALUES (520502, 520500, '七星关区');
INSERT INTO `72crm_crm_area` VALUES (520527, 520500, '赫章县');
INSERT INTO `72crm_crm_area` VALUES (520526, 520500, '威宁彝族回族苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (520525, 520500, '纳雍县');
INSERT INTO `72crm_crm_area` VALUES (520524, 520500, '织金县');
INSERT INTO `72crm_crm_area` VALUES (520523, 520500, '金沙县');
INSERT INTO `72crm_crm_area` VALUES (520522, 520500, '黔西县');
INSERT INTO `72crm_crm_area` VALUES (520521, 520500, '大方县');
INSERT INTO `72crm_crm_area` VALUES (140624, 140600, '怀仁县');
INSERT INTO `72crm_crm_area` VALUES (140602, 140600, '朔城区');
INSERT INTO `72crm_crm_area` VALUES (140603, 140600, '平鲁区');
INSERT INTO `72crm_crm_area` VALUES (140622, 140600, '应县');
INSERT INTO `72crm_crm_area` VALUES (140623, 140600, '右玉县');
INSERT INTO `72crm_crm_area` VALUES (140621, 140600, '山阴县');
INSERT INTO `72crm_crm_area` VALUES (371003, 371000, '文登区');
INSERT INTO `72crm_crm_area` VALUES (371083, 371000, '乳山市');
INSERT INTO `72crm_crm_area` VALUES (371002, 371000, '环翠区');
INSERT INTO `72crm_crm_area` VALUES (371082, 371000, '荣成市');
INSERT INTO `72crm_crm_area` VALUES (622921, 622900, '临夏县');
INSERT INTO `72crm_crm_area` VALUES (622923, 622900, '永靖县');
INSERT INTO `72crm_crm_area` VALUES (622922, 622900, '康乐县');
INSERT INTO `72crm_crm_area` VALUES (622925, 622900, '和政县');
INSERT INTO `72crm_crm_area` VALUES (622924, 622900, '广河县');
INSERT INTO `72crm_crm_area` VALUES (622927, 622900, '积石山保安族东乡族撒拉族自治县');
INSERT INTO `72crm_crm_area` VALUES (622926, 622900, '东乡族自治县');
INSERT INTO `72crm_crm_area` VALUES (622901, 622900, '临夏市');
INSERT INTO `72crm_crm_area` VALUES (460108, 460100, '美兰区');
INSERT INTO `72crm_crm_area` VALUES (460107, 460100, '琼山区');
INSERT INTO `72crm_crm_area` VALUES (460106, 460100, '龙华区');
INSERT INTO `72crm_crm_area` VALUES (460105, 460100, '秀英区');
INSERT INTO `72crm_crm_area` VALUES (511303, 511300, '高坪区');
INSERT INTO `72crm_crm_area` VALUES (511302, 511300, '顺庆区');
INSERT INTO `72crm_crm_area` VALUES (511381, 511300, '阆中市');
INSERT INTO `72crm_crm_area` VALUES (511325, 511300, '西充县');
INSERT INTO `72crm_crm_area` VALUES (511324, 511300, '仪陇县');
INSERT INTO `72crm_crm_area` VALUES (511323, 511300, '蓬安县');
INSERT INTO `72crm_crm_area` VALUES (511322, 511300, '营山县');
INSERT INTO `72crm_crm_area` VALUES (511321, 511300, '南部县');
INSERT INTO `72crm_crm_area` VALUES (511304, 511300, '嘉陵区');
INSERT INTO `72crm_crm_area` VALUES (152922, 152900, '阿拉善右旗');
INSERT INTO `72crm_crm_area` VALUES (152923, 152900, '额济纳旗');
INSERT INTO `72crm_crm_area` VALUES (152921, 152900, '阿拉善左旗');
INSERT INTO `72crm_crm_area` VALUES (430406, 430400, '雁峰区');
INSERT INTO `72crm_crm_area` VALUES (430422, 430400, '衡南县');
INSERT INTO `72crm_crm_area` VALUES (430407, 430400, '石鼓区');
INSERT INTO `72crm_crm_area` VALUES (430423, 430400, '衡山县');
INSERT INTO `72crm_crm_area` VALUES (430405, 430400, '珠晖区');
INSERT INTO `72crm_crm_area` VALUES (430421, 430400, '衡阳县');
INSERT INTO `72crm_crm_area` VALUES (430482, 430400, '常宁市');
INSERT INTO `72crm_crm_area` VALUES (430481, 430400, '耒阳市');
INSERT INTO `72crm_crm_area` VALUES (430412, 430400, '南岳区');
INSERT INTO `72crm_crm_area` VALUES (430426, 430400, '祁东县');
INSERT INTO `72crm_crm_area` VALUES (430408, 430400, '蒸湘区');
INSERT INTO `72crm_crm_area` VALUES (430424, 430400, '衡东县');
INSERT INTO `72crm_crm_area` VALUES (532801, 532800, '景洪市');
INSERT INTO `72crm_crm_area` VALUES (532822, 532800, '勐海县');
INSERT INTO `72crm_crm_area` VALUES (532823, 532800, '勐腊县');
INSERT INTO `72crm_crm_area` VALUES (340311, 340300, '淮上区');
INSERT INTO `72crm_crm_area` VALUES (340321, 340300, '怀远县');
INSERT INTO `72crm_crm_area` VALUES (340304, 340300, '禹会区');
INSERT INTO `72crm_crm_area` VALUES (340323, 340300, '固镇县');
INSERT INTO `72crm_crm_area` VALUES (340322, 340300, '五河县');
INSERT INTO `72crm_crm_area` VALUES (340303, 340300, '蚌山区');
INSERT INTO `72crm_crm_area` VALUES (340302, 340300, '龙子湖区');
INSERT INTO `72crm_crm_area` VALUES (450902, 450900, '玉州区');
INSERT INTO `72crm_crm_area` VALUES (450903, 450900, '福绵区');
INSERT INTO `72crm_crm_area` VALUES (450981, 450900, '北流市');
INSERT INTO `72crm_crm_area` VALUES (450924, 450900, '兴业县');
INSERT INTO `72crm_crm_area` VALUES (450922, 450900, '陆川县');
INSERT INTO `72crm_crm_area` VALUES (450923, 450900, '博白县');
INSERT INTO `72crm_crm_area` VALUES (450921, 450900, '容县');
INSERT INTO `72crm_crm_area` VALUES (370500, 370000, '东营市');
INSERT INTO `72crm_crm_area` VALUES (371300, 370000, '临沂市');
INSERT INTO `72crm_crm_area` VALUES (370400, 370000, '枣庄市');
INSERT INTO `72crm_crm_area` VALUES (371200, 370000, '莱芜市');
INSERT INTO `72crm_crm_area` VALUES (370700, 370000, '潍坊市');
INSERT INTO `72crm_crm_area` VALUES (371500, 370000, '聊城市');
INSERT INTO `72crm_crm_area` VALUES (370600, 370000, '烟台市');
INSERT INTO `72crm_crm_area` VALUES (371400, 370000, '德州市');
INSERT INTO `72crm_crm_area` VALUES (370100, 370000, '济南市');
INSERT INTO `72crm_crm_area` VALUES (370900, 370000, '泰安市');
INSERT INTO `72crm_crm_area` VALUES (371700, 370000, '菏泽市');
INSERT INTO `72crm_crm_area` VALUES (370800, 370000, '济宁市');
INSERT INTO `72crm_crm_area` VALUES (371600, 370000, '滨州市');
INSERT INTO `72crm_crm_area` VALUES (370300, 370000, '淄博市');
INSERT INTO `72crm_crm_area` VALUES (371100, 370000, '日照市');
INSERT INTO `72crm_crm_area` VALUES (370200, 370000, '青岛市');
INSERT INTO `72crm_crm_area` VALUES (371000, 370000, '威海市');
INSERT INTO `72crm_crm_area` VALUES (421222, 421200, '通城县');
INSERT INTO `72crm_crm_area` VALUES (421223, 421200, '崇阳县');
INSERT INTO `72crm_crm_area` VALUES (421221, 421200, '嘉鱼县');
INSERT INTO `72crm_crm_area` VALUES (421202, 421200, '咸安区');
INSERT INTO `72crm_crm_area` VALUES (421281, 421200, '赤壁市');
INSERT INTO `72crm_crm_area` VALUES (421224, 421200, '通山县');
INSERT INTO `72crm_crm_area` VALUES (220503, 220500, '二道江区');
INSERT INTO `72crm_crm_area` VALUES (220502, 220500, '东昌区');
INSERT INTO `72crm_crm_area` VALUES (220582, 220500, '集安市');
INSERT INTO `72crm_crm_area` VALUES (220581, 220500, '梅河口市');
INSERT INTO `72crm_crm_area` VALUES (220523, 220500, '辉南县');
INSERT INTO `72crm_crm_area` VALUES (220521, 220500, '通化县');
INSERT INTO `72crm_crm_area` VALUES (220524, 220500, '柳河县');
INSERT INTO `72crm_crm_area` VALUES (331125, 331100, '云和县');
INSERT INTO `72crm_crm_area` VALUES (331124, 331100, '松阳县');
INSERT INTO `72crm_crm_area` VALUES (331127, 331100, '景宁畲族自治县');
INSERT INTO `72crm_crm_area` VALUES (331126, 331100, '庆元县');
INSERT INTO `72crm_crm_area` VALUES (331121, 331100, '青田县');
INSERT INTO `72crm_crm_area` VALUES (331123, 331100, '遂昌县');
INSERT INTO `72crm_crm_area` VALUES (331122, 331100, '缙云县');
INSERT INTO `72crm_crm_area` VALUES (331181, 331100, '龙泉市');
INSERT INTO `72crm_crm_area` VALUES (331102, 331100, '莲都区');
INSERT INTO `72crm_crm_area` VALUES (510311, 510300, '沿滩区');
INSERT INTO `72crm_crm_area` VALUES (510322, 510300, '富顺县');
INSERT INTO `72crm_crm_area` VALUES (510321, 510300, '荣县');
INSERT INTO `72crm_crm_area` VALUES (510304, 510300, '大安区');
INSERT INTO `72crm_crm_area` VALUES (510303, 510300, '贡井区');
INSERT INTO `72crm_crm_area` VALUES (510302, 510300, '自流井区');
INSERT INTO `72crm_crm_area` VALUES (630105, 630100, '城北区');
INSERT INTO `72crm_crm_area` VALUES (630121, 630100, '大通回族土族自治县');
INSERT INTO `72crm_crm_area` VALUES (630104, 630100, '城西区');
INSERT INTO `72crm_crm_area` VALUES (630123, 630100, '湟源县');
INSERT INTO `72crm_crm_area` VALUES (630122, 630100, '湟中县');
INSERT INTO `72crm_crm_area` VALUES (630103, 630100, '城中区');
INSERT INTO `72crm_crm_area` VALUES (630102, 630100, '城东区');
INSERT INTO `72crm_crm_area` VALUES (130433, 130400, '馆陶县');
INSERT INTO `72crm_crm_area` VALUES (130432, 130400, '广平县');
INSERT INTO `72crm_crm_area` VALUES (130403, 130400, '丛台区');
INSERT INTO `72crm_crm_area` VALUES (130435, 130400, '曲周县');
INSERT INTO `72crm_crm_area` VALUES (130402, 130400, '邯山区');
INSERT INTO `72crm_crm_area` VALUES (130434, 130400, '魏县');
INSERT INTO `72crm_crm_area` VALUES (130404, 130400, '复兴区');
INSERT INTO `72crm_crm_area` VALUES (130407, 130400, '肥乡区');
INSERT INTO `72crm_crm_area` VALUES (130406, 130400, '峰峰矿区');
INSERT INTO `72crm_crm_area` VALUES (130408, 130400, '永年区');
INSERT INTO `72crm_crm_area` VALUES (130481, 130400, '武安市');
INSERT INTO `72crm_crm_area` VALUES (130423, 130400, '临漳县');
INSERT INTO `72crm_crm_area` VALUES (130425, 130400, '大名县');
INSERT INTO `72crm_crm_area` VALUES (130424, 130400, '成安县');
INSERT INTO `72crm_crm_area` VALUES (130427, 130400, '磁县');
INSERT INTO `72crm_crm_area` VALUES (130426, 130400, '涉县');
INSERT INTO `72crm_crm_area` VALUES (130431, 130400, '鸡泽县');
INSERT INTO `72crm_crm_area` VALUES (130430, 130400, '邱县');
INSERT INTO `72crm_crm_area` VALUES (441702, 441700, '江城区');
INSERT INTO `72crm_crm_area` VALUES (441781, 441700, '阳春市');
INSERT INTO `72crm_crm_area` VALUES (441704, 441700, '阳东区');
INSERT INTO `72crm_crm_area` VALUES (441721, 441700, '阳西县');
INSERT INTO `72crm_crm_area` VALUES (360803, 360800, '青原区');
INSERT INTO `72crm_crm_area` VALUES (360802, 360800, '吉州区');
INSERT INTO `72crm_crm_area` VALUES (360821, 360800, '吉安县');
INSERT INTO `72crm_crm_area` VALUES (360823, 360800, '峡江县');
INSERT INTO `72crm_crm_area` VALUES (360822, 360800, '吉水县');
INSERT INTO `72crm_crm_area` VALUES (360881, 360800, '井冈山市');
INSERT INTO `72crm_crm_area` VALUES (360829, 360800, '安福县');
INSERT INTO `72crm_crm_area` VALUES (360828, 360800, '万安县');
INSERT INTO `72crm_crm_area` VALUES (360830, 360800, '永新县');
INSERT INTO `72crm_crm_area` VALUES (360825, 360800, '永丰县');
INSERT INTO `72crm_crm_area` VALUES (360824, 360800, '新干县');
INSERT INTO `72crm_crm_area` VALUES (360827, 360800, '遂川县');
INSERT INTO `72crm_crm_area` VALUES (360826, 360800, '泰和县');
INSERT INTO `72crm_crm_area` VALUES (211303, 211300, '龙城区');
INSERT INTO `72crm_crm_area` VALUES (211302, 211300, '双塔区');
INSERT INTO `72crm_crm_area` VALUES (211382, 211300, '凌源市');
INSERT INTO `72crm_crm_area` VALUES (211381, 211300, '北票市');
INSERT INTO `72crm_crm_area` VALUES (211322, 211300, '建平县');
INSERT INTO `72crm_crm_area` VALUES (211321, 211300, '朝阳县');
INSERT INTO `72crm_crm_area` VALUES (211324, 211300, '喀喇沁左翼蒙古族自治县');
INSERT INTO `72crm_crm_area` VALUES (540200, 540000, '日喀则市');
INSERT INTO `72crm_crm_area` VALUES (540600, 540000, '那曲地区');
INSERT INTO `72crm_crm_area` VALUES (540300, 540000, '昌都市');
INSERT INTO `72crm_crm_area` VALUES (540400, 540000, '林芝市');
INSERT INTO `72crm_crm_area` VALUES (540100, 540000, '拉萨市');
INSERT INTO `72crm_crm_area` VALUES (540500, 540000, '山南市');
INSERT INTO `72crm_crm_area` VALUES (542500, 540000, '阿里地区');
INSERT INTO `72crm_crm_area` VALUES (620921, 620900, '金塔县');
INSERT INTO `72crm_crm_area` VALUES (620923, 620900, '肃北蒙古族自治县');
INSERT INTO `72crm_crm_area` VALUES (620922, 620900, '瓜州县');
INSERT INTO `72crm_crm_area` VALUES (620924, 620900, '阿克塞哈萨克族自治县');
INSERT INTO `72crm_crm_area` VALUES (620981, 620900, '玉门市');
INSERT INTO `72crm_crm_area` VALUES (620902, 620900, '肃州区');
INSERT INTO `72crm_crm_area` VALUES (620982, 620900, '敦煌市');
INSERT INTO `72crm_crm_area` VALUES (420222, 420200, '阳新县');
INSERT INTO `72crm_crm_area` VALUES (420204, 420200, '下陆区');
INSERT INTO `72crm_crm_area` VALUES (420205, 420200, '铁山区');
INSERT INTO `72crm_crm_area` VALUES (420202, 420200, '黄石港区');
INSERT INTO `72crm_crm_area` VALUES (420203, 420200, '西塞山区');
INSERT INTO `72crm_crm_area` VALUES (420281, 420200, '大冶市');
INSERT INTO `72crm_crm_area` VALUES (522631, 522600, '黎平县');
INSERT INTO `72crm_crm_area` VALUES (522630, 522600, '台江县');
INSERT INTO `72crm_crm_area` VALUES (522629, 522600, '剑河县');
INSERT INTO `72crm_crm_area` VALUES (522628, 522600, '锦屏县');
INSERT INTO `72crm_crm_area` VALUES (522627, 522600, '天柱县');
INSERT INTO `72crm_crm_area` VALUES (522626, 522600, '岑巩县');
INSERT INTO `72crm_crm_area` VALUES (522625, 522600, '镇远县');
INSERT INTO `72crm_crm_area` VALUES (522624, 522600, '三穗县');
INSERT INTO `72crm_crm_area` VALUES (522636, 522600, '丹寨县');
INSERT INTO `72crm_crm_area` VALUES (522635, 522600, '麻江县');
INSERT INTO `72crm_crm_area` VALUES (522634, 522600, '雷山县');
INSERT INTO `72crm_crm_area` VALUES (522601, 522600, '凯里市');
INSERT INTO `72crm_crm_area` VALUES (522633, 522600, '从江县');
INSERT INTO `72crm_crm_area` VALUES (522632, 522600, '榕江县');
INSERT INTO `72crm_crm_area` VALUES (522623, 522600, '施秉县');
INSERT INTO `72crm_crm_area` VALUES (522622, 522600, '黄平县');
INSERT INTO `72crm_crm_area` VALUES (330182, 330100, '建德市');
INSERT INTO `72crm_crm_area` VALUES (330112, 330100, '临安区');
INSERT INTO `72crm_crm_area` VALUES (330127, 330100, '淳安县');
INSERT INTO `72crm_crm_area` VALUES (330122, 330100, '桐庐县');
INSERT INTO `72crm_crm_area` VALUES (330103, 330100, '下城区');
INSERT INTO `72crm_crm_area` VALUES (330102, 330100, '上城区');
INSERT INTO `72crm_crm_area` VALUES (330109, 330100, '萧山区');
INSERT INTO `72crm_crm_area` VALUES (330108, 330100, '滨江区');
INSERT INTO `72crm_crm_area` VALUES (330111, 330100, '富阳区');
INSERT INTO `72crm_crm_area` VALUES (330110, 330100, '余杭区');
INSERT INTO `72crm_crm_area` VALUES (330105, 330100, '拱墅区');
INSERT INTO `72crm_crm_area` VALUES (330104, 330100, '江干区');
INSERT INTO `72crm_crm_area` VALUES (330106, 330100, '西湖区');
INSERT INTO `72crm_crm_area` VALUES (150928, 150900, '察哈尔右翼后旗');
INSERT INTO `72crm_crm_area` VALUES (150929, 150900, '四子王旗');
INSERT INTO `72crm_crm_area` VALUES (150902, 150900, '集宁区');
INSERT INTO `72crm_crm_area` VALUES (150981, 150900, '丰镇市');
INSERT INTO `72crm_crm_area` VALUES (150922, 150900, '化德县');
INSERT INTO `72crm_crm_area` VALUES (150923, 150900, '商都县');
INSERT INTO `72crm_crm_area` VALUES (150921, 150900, '卓资县');
INSERT INTO `72crm_crm_area` VALUES (150926, 150900, '察哈尔右翼前旗');
INSERT INTO `72crm_crm_area` VALUES (150927, 150900, '察哈尔右翼中旗');
INSERT INTO `72crm_crm_area` VALUES (150924, 150900, '兴和县');
INSERT INTO `72crm_crm_area` VALUES (150925, 150900, '凉城县');
INSERT INTO `72crm_crm_area` VALUES (530824, 530800, '景谷傣族彝族自治县');
INSERT INTO `72crm_crm_area` VALUES (530825, 530800, '镇沅彝族哈尼族拉祜族自治县');
INSERT INTO `72crm_crm_area` VALUES (530826, 530800, '江城哈尼族彝族自治县');
INSERT INTO `72crm_crm_area` VALUES (530827, 530800, '孟连傣族拉祜族佤族自治县');
INSERT INTO `72crm_crm_area` VALUES (530828, 530800, '澜沧拉祜族自治县');
INSERT INTO `72crm_crm_area` VALUES (530829, 530800, '西盟佤族自治县');
INSERT INTO `72crm_crm_area` VALUES (530802, 530800, '思茅区');
INSERT INTO `72crm_crm_area` VALUES (530821, 530800, '宁洱哈尼族彝族自治县');
INSERT INTO `72crm_crm_area` VALUES (530822, 530800, '墨江哈尼族自治县');
INSERT INTO `72crm_crm_area` VALUES (530823, 530800, '景东彝族自治县');
INSERT INTO `72crm_crm_area` VALUES (440704, 440700, '江海区');
INSERT INTO `72crm_crm_area` VALUES (440784, 440700, '鹤山市');
INSERT INTO `72crm_crm_area` VALUES (440705, 440700, '新会区');
INSERT INTO `72crm_crm_area` VALUES (440785, 440700, '恩平市');
INSERT INTO `72crm_crm_area` VALUES (440703, 440700, '蓬江区');
INSERT INTO `72crm_crm_area` VALUES (440783, 440700, '开平市');
INSERT INTO `72crm_crm_area` VALUES (440781, 440700, '台山市');
INSERT INTO `72crm_crm_area` VALUES (710600, 710000, '南投县');
INSERT INTO `72crm_crm_area` VALUES (711400, 710000, '桃园县');
INSERT INTO `72crm_crm_area` VALUES (710700, 710000, '基隆市');
INSERT INTO `72crm_crm_area` VALUES (711500, 710000, '苗栗县');
INSERT INTO `72crm_crm_area` VALUES (710400, 710000, '台中市');
INSERT INTO `72crm_crm_area` VALUES (711200, 710000, '宜兰县');
INSERT INTO `72crm_crm_area` VALUES (712800, 710000, '连江县');
INSERT INTO `72crm_crm_area` VALUES (710500, 710000, '金门县');
INSERT INTO `72crm_crm_area` VALUES (711300, 710000, '新竹县');
INSERT INTO `72crm_crm_area` VALUES (712100, 710000, '云林县');
INSERT INTO `72crm_crm_area` VALUES (710200, 710000, '高雄市');
INSERT INTO `72crm_crm_area` VALUES (712600, 710000, '花莲县');
INSERT INTO `72crm_crm_area` VALUES (710300, 710000, '台南市');
INSERT INTO `72crm_crm_area` VALUES (711100, 710000, '新北市');
INSERT INTO `72crm_crm_area` VALUES (711900, 710000, '嘉义县');
INSERT INTO `72crm_crm_area` VALUES (712700, 710000, '澎湖县');
INSERT INTO `72crm_crm_area` VALUES (710800, 710000, '新竹市');
INSERT INTO `72crm_crm_area` VALUES (712400, 710000, '屏东县');
INSERT INTO `72crm_crm_area` VALUES (710100, 710000, '台北市');
INSERT INTO `72crm_crm_area` VALUES (710900, 710000, '嘉义市');
INSERT INTO `72crm_crm_area` VALUES (711700, 710000, '彰化县');
INSERT INTO `72crm_crm_area` VALUES (712500, 710000, '台东县');
INSERT INTO `72crm_crm_area` VALUES (411024, 411000, '鄢陵县');
INSERT INTO `72crm_crm_area` VALUES (411025, 411000, '襄城县');
INSERT INTO `72crm_crm_area` VALUES (411002, 411000, '魏都区');
INSERT INTO `72crm_crm_area` VALUES (411082, 411000, '长葛市');
INSERT INTO `72crm_crm_area` VALUES (411003, 411000, '建安区');
INSERT INTO `72crm_crm_area` VALUES (411081, 411000, '禹州市');
INSERT INTO `72crm_crm_area` VALUES (210323, 210300, '岫岩满族自治县');
INSERT INTO `72crm_crm_area` VALUES (210321, 210300, '台安县');
INSERT INTO `72crm_crm_area` VALUES (210304, 210300, '立山区');
INSERT INTO `72crm_crm_area` VALUES (210311, 210300, '千山区');
INSERT INTO `72crm_crm_area` VALUES (210303, 210300, '铁西区');
INSERT INTO `72crm_crm_area` VALUES (210302, 210300, '铁东区');
INSERT INTO `72crm_crm_area` VALUES (210381, 210300, '海城市');
INSERT INTO `72crm_crm_area` VALUES (513423, 513400, '盐源县');
INSERT INTO `72crm_crm_area` VALUES (513422, 513400, '木里藏族自治县');
INSERT INTO `72crm_crm_area` VALUES (513431, 513400, '昭觉县');
INSERT INTO `72crm_crm_area` VALUES (513430, 513400, '金阳县');
INSERT INTO `72crm_crm_area` VALUES (513429, 513400, '布拖县');
INSERT INTO `72crm_crm_area` VALUES (513428, 513400, '普格县');
INSERT INTO `72crm_crm_area` VALUES (513427, 513400, '宁南县');
INSERT INTO `72crm_crm_area` VALUES (513426, 513400, '会东县');
INSERT INTO `72crm_crm_area` VALUES (513425, 513400, '会理县');
INSERT INTO `72crm_crm_area` VALUES (513424, 513400, '德昌县');
INSERT INTO `72crm_crm_area` VALUES (513437, 513400, '雷波县');
INSERT INTO `72crm_crm_area` VALUES (513436, 513400, '美姑县');
INSERT INTO `72crm_crm_area` VALUES (513435, 513400, '甘洛县');
INSERT INTO `72crm_crm_area` VALUES (513434, 513400, '越西县');
INSERT INTO `72crm_crm_area` VALUES (513401, 513400, '西昌市');
INSERT INTO `72crm_crm_area` VALUES (513433, 513400, '冕宁县');
INSERT INTO `72crm_crm_area` VALUES (513432, 513400, '喜德县');
INSERT INTO `72crm_crm_area` VALUES (320981, 320900, '东台市');
INSERT INTO `72crm_crm_area` VALUES (320902, 320900, '亭湖区');
INSERT INTO `72crm_crm_area` VALUES (320903, 320900, '盐都区');
INSERT INTO `72crm_crm_area` VALUES (320924, 320900, '射阳县');
INSERT INTO `72crm_crm_area` VALUES (320925, 320900, '建湖县');
INSERT INTO `72crm_crm_area` VALUES (320904, 320900, '大丰区');
INSERT INTO `72crm_crm_area` VALUES (320921, 320900, '响水县');
INSERT INTO `72crm_crm_area` VALUES (320922, 320900, '滨海县');
INSERT INTO `72crm_crm_area` VALUES (320923, 320900, '阜宁县');
INSERT INTO `72crm_crm_area` VALUES (632221, 632200, '门源回族自治县');
INSERT INTO `72crm_crm_area` VALUES (632223, 632200, '海晏县');
INSERT INTO `72crm_crm_area` VALUES (632222, 632200, '祁连县');
INSERT INTO `72crm_crm_area` VALUES (632224, 632200, '刚察县');
INSERT INTO `72crm_crm_area` VALUES (500103, 500100, '渝中区');
INSERT INTO `72crm_crm_area` VALUES (500102, 500100, '涪陵区');
INSERT INTO `72crm_crm_area` VALUES (500101, 500100, '万州区');
INSERT INTO `72crm_crm_area` VALUES (500111, 500100, '大足区');
INSERT INTO `72crm_crm_area` VALUES (500110, 500100, '綦江区');
INSERT INTO `72crm_crm_area` VALUES (500109, 500100, '北碚区');
INSERT INTO `72crm_crm_area` VALUES (500108, 500100, '南岸区');
INSERT INTO `72crm_crm_area` VALUES (500107, 500100, '九龙坡区');
INSERT INTO `72crm_crm_area` VALUES (500106, 500100, '沙坪坝区');
INSERT INTO `72crm_crm_area` VALUES (500105, 500100, '江北区');
INSERT INTO `72crm_crm_area` VALUES (500104, 500100, '大渡口区');
INSERT INTO `72crm_crm_area` VALUES (500119, 500100, '南川区');
INSERT INTO `72crm_crm_area` VALUES (500118, 500100, '永川区');
INSERT INTO `72crm_crm_area` VALUES (500117, 500100, '合川区');
INSERT INTO `72crm_crm_area` VALUES (500116, 500100, '江津区');
INSERT INTO `72crm_crm_area` VALUES (500115, 500100, '长寿区');
INSERT INTO `72crm_crm_area` VALUES (500114, 500100, '黔江区');
INSERT INTO `72crm_crm_area` VALUES (500113, 500100, '巴南区');
INSERT INTO `72crm_crm_area` VALUES (500112, 500100, '渝北区');
INSERT INTO `72crm_crm_area` VALUES (500120, 500100, '璧山区');
INSERT INTO `72crm_crm_area` VALUES (500151, 500100, '铜梁区');
INSERT INTO `72crm_crm_area` VALUES (500156, 500100, '武隆区');
INSERT INTO `72crm_crm_area` VALUES (500155, 500100, '梁平区');
INSERT INTO `72crm_crm_area` VALUES (500154, 500100, '开州区');
INSERT INTO `72crm_crm_area` VALUES (500153, 500100, '荣昌区');
INSERT INTO `72crm_crm_area` VALUES (500152, 500100, '潼南区');
INSERT INTO `72crm_crm_area` VALUES (610729, 610700, '留坝县');
INSERT INTO `72crm_crm_area` VALUES (610728, 610700, '镇巴县');
INSERT INTO `72crm_crm_area` VALUES (610730, 610700, '佛坪县');
INSERT INTO `72crm_crm_area` VALUES (610703, 610700, '南郑区');
INSERT INTO `72crm_crm_area` VALUES (610702, 610700, '汉台区');
INSERT INTO `72crm_crm_area` VALUES (610723, 610700, '洋县');
INSERT INTO `72crm_crm_area` VALUES (610722, 610700, '城固县');
INSERT INTO `72crm_crm_area` VALUES (610725, 610700, '勉县');
INSERT INTO `72crm_crm_area` VALUES (610724, 610700, '西乡县');
INSERT INTO `72crm_crm_area` VALUES (610727, 610700, '略阳县');
INSERT INTO `72crm_crm_area` VALUES (610726, 610700, '宁强县');
INSERT INTO `72crm_crm_area` VALUES (350629, 350600, '华安县');
INSERT INTO `72crm_crm_area` VALUES (350628, 350600, '平和县');
INSERT INTO `72crm_crm_area` VALUES (350625, 350600, '长泰县');
INSERT INTO `72crm_crm_area` VALUES (350624, 350600, '诏安县');
INSERT INTO `72crm_crm_area` VALUES (350627, 350600, '南靖县');
INSERT INTO `72crm_crm_area` VALUES (350626, 350600, '东山县');
INSERT INTO `72crm_crm_area` VALUES (350623, 350600, '漳浦县');
INSERT INTO `72crm_crm_area` VALUES (350622, 350600, '云霄县');
INSERT INTO `72crm_crm_area` VALUES (350681, 350600, '龙海市');
INSERT INTO `72crm_crm_area` VALUES (350603, 350600, '龙文区');
INSERT INTO `72crm_crm_area` VALUES (350602, 350600, '芗城区');
INSERT INTO `72crm_crm_area` VALUES (623001, 623000, '合作市');
INSERT INTO `72crm_crm_area` VALUES (623021, 623000, '临潭县');
INSERT INTO `72crm_crm_area` VALUES (623023, 623000, '舟曲县');
INSERT INTO `72crm_crm_area` VALUES (623022, 623000, '卓尼县');
INSERT INTO `72crm_crm_area` VALUES (623025, 623000, '玛曲县');
INSERT INTO `72crm_crm_area` VALUES (623024, 623000, '迭部县');
INSERT INTO `72crm_crm_area` VALUES (623027, 623000, '夏河县');
INSERT INTO `72crm_crm_area` VALUES (623026, 623000, '碌曲县');
INSERT INTO `72crm_crm_area` VALUES (230803, 230800, '向阳区');
INSERT INTO `72crm_crm_area` VALUES (230883, 230800, '抚远市');
INSERT INTO `72crm_crm_area` VALUES (230882, 230800, '富锦市');
INSERT INTO `72crm_crm_area` VALUES (230881, 230800, '同江市');
INSERT INTO `72crm_crm_area` VALUES (230822, 230800, '桦南县');
INSERT INTO `72crm_crm_area` VALUES (230805, 230800, '东风区');
INSERT INTO `72crm_crm_area` VALUES (230804, 230800, '前进区');
INSERT INTO `72crm_crm_area` VALUES (230811, 230800, '郊区');
INSERT INTO `72crm_crm_area` VALUES (230826, 230800, '桦川县');
INSERT INTO `72crm_crm_area` VALUES (230828, 230800, '汤原县');
INSERT INTO `72crm_crm_area` VALUES (652701, 652700, '博乐市');
INSERT INTO `72crm_crm_area` VALUES (652702, 652700, '阿拉山口市');
INSERT INTO `72crm_crm_area` VALUES (652723, 652700, '温泉县');
INSERT INTO `72crm_crm_area` VALUES (652722, 652700, '精河县');
INSERT INTO `72crm_crm_area` VALUES (410500, 410000, '安阳市');
INSERT INTO `72crm_crm_area` VALUES (411300, 410000, '南阳市');
INSERT INTO `72crm_crm_area` VALUES (410400, 410000, '平顶山市');
INSERT INTO `72crm_crm_area` VALUES (411200, 410000, '三门峡市');
INSERT INTO `72crm_crm_area` VALUES (410700, 410000, '新乡市');
INSERT INTO `72crm_crm_area` VALUES (411500, 410000, '信阳市');
INSERT INTO `72crm_crm_area` VALUES (410600, 410000, '鹤壁市');
INSERT INTO `72crm_crm_area` VALUES (411400, 410000, '商丘市');
INSERT INTO `72crm_crm_area` VALUES (410100, 410000, '郑州市');
INSERT INTO `72crm_crm_area` VALUES (410900, 410000, '濮阳市');
INSERT INTO `72crm_crm_area` VALUES (411700, 410000, '驻马店市');
INSERT INTO `72crm_crm_area` VALUES (410800, 410000, '焦作市');
INSERT INTO `72crm_crm_area` VALUES (411600, 410000, '周口市');
INSERT INTO `72crm_crm_area` VALUES (410300, 410000, '洛阳市');
INSERT INTO `72crm_crm_area` VALUES (411100, 410000, '漯河市');
INSERT INTO `72crm_crm_area` VALUES (410200, 410000, '开封市');
INSERT INTO `72crm_crm_area` VALUES (411000, 410000, '许昌市');
INSERT INTO `72crm_crm_area` VALUES (419000, 410000, '省直辖县级行政单位');
INSERT INTO `72crm_crm_area` VALUES (371121, 371100, '五莲县');
INSERT INTO `72crm_crm_area` VALUES (371122, 371100, '莒县');
INSERT INTO `72crm_crm_area` VALUES (371103, 371100, '岚山区');
INSERT INTO `72crm_crm_area` VALUES (371102, 371100, '东港区');
INSERT INTO `72crm_crm_area` VALUES (640425, 640400, '彭阳县');
INSERT INTO `72crm_crm_area` VALUES (640424, 640400, '泾源县');
INSERT INTO `72crm_crm_area` VALUES (640402, 640400, '原州区');
INSERT INTO `72crm_crm_area` VALUES (640423, 640400, '隆德县');
INSERT INTO `72crm_crm_area` VALUES (640422, 640400, '西吉县');
INSERT INTO `72crm_crm_area` VALUES (140722, 140700, '左权县');
INSERT INTO `72crm_crm_area` VALUES (140723, 140700, '和顺县');
INSERT INTO `72crm_crm_area` VALUES (140721, 140700, '榆社县');
INSERT INTO `72crm_crm_area` VALUES (140726, 140700, '太谷县');
INSERT INTO `72crm_crm_area` VALUES (140727, 140700, '祁县');
INSERT INTO `72crm_crm_area` VALUES (140724, 140700, '昔阳县');
INSERT INTO `72crm_crm_area` VALUES (140725, 140700, '寿阳县');
INSERT INTO `72crm_crm_area` VALUES (140728, 140700, '平遥县');
INSERT INTO `72crm_crm_area` VALUES (140729, 140700, '灵石县');
INSERT INTO `72crm_crm_area` VALUES (140702, 140700, '榆次区');
INSERT INTO `72crm_crm_area` VALUES (140781, 140700, '介休市');
INSERT INTO `72crm_crm_area` VALUES (520628, 520600, '松桃苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (520627, 520600, '沿河土家族自治县');
INSERT INTO `72crm_crm_area` VALUES (520626, 520600, '德江县');
INSERT INTO `72crm_crm_area` VALUES (520625, 520600, '印江土家族苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (520624, 520600, '思南县');
INSERT INTO `72crm_crm_area` VALUES (520623, 520600, '石阡县');
INSERT INTO `72crm_crm_area` VALUES (520622, 520600, '玉屏侗族自治县');
INSERT INTO `72crm_crm_area` VALUES (520621, 520600, '江口县');
INSERT INTO `72crm_crm_area` VALUES (520603, 520600, '万山区');
INSERT INTO `72crm_crm_area` VALUES (520602, 520600, '碧江区');
INSERT INTO `72crm_crm_area` VALUES (430502, 430500, '双清区');
INSERT INTO `72crm_crm_area` VALUES (430503, 430500, '大祥区');
INSERT INTO `72crm_crm_area` VALUES (430581, 430500, '武冈市');
INSERT INTO `72crm_crm_area` VALUES (430528, 430500, '新宁县');
INSERT INTO `72crm_crm_area` VALUES (430529, 430500, '城步苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (430511, 430500, '北塔区');
INSERT INTO `72crm_crm_area` VALUES (430527, 430500, '绥宁县');
INSERT INTO `72crm_crm_area` VALUES (430524, 430500, '隆回县');
INSERT INTO `72crm_crm_area` VALUES (430525, 430500, '洞口县');
INSERT INTO `72crm_crm_area` VALUES (430522, 430500, '新邵县');
INSERT INTO `72crm_crm_area` VALUES (430523, 430500, '邵阳县');
INSERT INTO `72crm_crm_area` VALUES (430521, 430500, '邵东县');
INSERT INTO `72crm_crm_area` VALUES (532922, 532900, '漾濞彝族自治县');
INSERT INTO `72crm_crm_area` VALUES (532923, 532900, '祥云县');
INSERT INTO `72crm_crm_area` VALUES (532924, 532900, '宾川县');
INSERT INTO `72crm_crm_area` VALUES (532925, 532900, '弥渡县');
INSERT INTO `72crm_crm_area` VALUES (532926, 532900, '南涧彝族自治县');
INSERT INTO `72crm_crm_area` VALUES (532927, 532900, '巍山彝族回族自治县');
INSERT INTO `72crm_crm_area` VALUES (532928, 532900, '永平县');
INSERT INTO `72crm_crm_area` VALUES (532929, 532900, '云龙县');
INSERT INTO `72crm_crm_area` VALUES (532930, 532900, '洱源县');
INSERT INTO `72crm_crm_area` VALUES (532931, 532900, '剑川县');
INSERT INTO `72crm_crm_area` VALUES (532932, 532900, '鹤庆县');
INSERT INTO `72crm_crm_area` VALUES (532901, 532900, '大理市');
INSERT INTO `72crm_crm_area` VALUES (712194, 712100, '二仑乡');
INSERT INTO `72crm_crm_area` VALUES (712195, 712100, '北港镇');
INSERT INTO `72crm_crm_area` VALUES (712192, 712100, '莿桐乡');
INSERT INTO `72crm_crm_area` VALUES (712193, 712100, '西螺镇');
INSERT INTO `72crm_crm_area` VALUES (712198, 712100, '四湖乡');
INSERT INTO `72crm_crm_area` VALUES (712199, 712100, '元长乡');
INSERT INTO `72crm_crm_area` VALUES (712196, 712100, '水林乡');
INSERT INTO `72crm_crm_area` VALUES (712197, 712100, '口湖乡');
INSERT INTO `72crm_crm_area` VALUES (712186, 712100, '台西乡');
INSERT INTO `72crm_crm_area` VALUES (712187, 712100, '仑背乡');
INSERT INTO `72crm_crm_area` VALUES (712184, 712100, '褒忠乡');
INSERT INTO `72crm_crm_area` VALUES (712185, 712100, '东势乡');
INSERT INTO `72crm_crm_area` VALUES (712190, 712100, '林内乡');
INSERT INTO `72crm_crm_area` VALUES (712191, 712100, '古坑乡');
INSERT INTO `72crm_crm_area` VALUES (712188, 712100, '麦寮乡');
INSERT INTO `72crm_crm_area` VALUES (712189, 712100, '斗六市');
INSERT INTO `72crm_crm_area` VALUES (712182, 712100, '虎尾镇');
INSERT INTO `72crm_crm_area` VALUES (712183, 712100, '土库镇');
INSERT INTO `72crm_crm_area` VALUES (712180, 712100, '斗南镇');
INSERT INTO `72crm_crm_area` VALUES (712181, 712100, '大埤乡');
INSERT INTO `72crm_crm_area` VALUES (460205, 460200, '崖州区');
INSERT INTO `72crm_crm_area` VALUES (460204, 460200, '天涯区');
INSERT INTO `72crm_crm_area` VALUES (460203, 460200, '吉阳区');
INSERT INTO `72crm_crm_area` VALUES (460202, 460200, '海棠区');
INSERT INTO `72crm_crm_area` VALUES (511425, 511400, '青神县');
INSERT INTO `72crm_crm_area` VALUES (511424, 511400, '丹棱县');
INSERT INTO `72crm_crm_area` VALUES (511423, 511400, '洪雅县');
INSERT INTO `72crm_crm_area` VALUES (511421, 511400, '仁寿县');
INSERT INTO `72crm_crm_area` VALUES (511403, 511400, '彭山区');
INSERT INTO `72crm_crm_area` VALUES (511402, 511400, '东坡区');
INSERT INTO `72crm_crm_area` VALUES (110100, 110000, '北京城区');
INSERT INTO `72crm_crm_area` VALUES (370103, 370100, '市中区');
INSERT INTO `72crm_crm_area` VALUES (370102, 370100, '历下区');
INSERT INTO `72crm_crm_area` VALUES (370113, 370100, '长清区');
INSERT INTO `72crm_crm_area` VALUES (370112, 370100, '历城区');
INSERT INTO `72crm_crm_area` VALUES (370114, 370100, '章丘区');
INSERT INTO `72crm_crm_area` VALUES (370125, 370100, '济阳县');
INSERT INTO `72crm_crm_area` VALUES (370124, 370100, '平阴县');
INSERT INTO `72crm_crm_area` VALUES (370126, 370100, '商河县');
INSERT INTO `72crm_crm_area` VALUES (370105, 370100, '天桥区');
INSERT INTO `72crm_crm_area` VALUES (370104, 370100, '槐荫区');
INSERT INTO `72crm_crm_area` VALUES (630203, 630200, '平安区');
INSERT INTO `72crm_crm_area` VALUES (630202, 630200, '乐都区');
INSERT INTO `72crm_crm_area` VALUES (630223, 630200, '互助土族自治县');
INSERT INTO `72crm_crm_area` VALUES (630222, 630200, '民和回族土族自治县');
INSERT INTO `72crm_crm_area` VALUES (630225, 630200, '循化撒拉族自治县');
INSERT INTO `72crm_crm_area` VALUES (630224, 630200, '化隆回族自治县');
INSERT INTO `72crm_crm_area` VALUES (421303, 421300, '曾都区');
INSERT INTO `72crm_crm_area` VALUES (421381, 421300, '广水市');
INSERT INTO `72crm_crm_area` VALUES (421321, 421300, '随县');
INSERT INTO `72crm_crm_area` VALUES (340405, 340400, '八公山区');
INSERT INTO `72crm_crm_area` VALUES (340421, 340400, '凤台县');
INSERT INTO `72crm_crm_area` VALUES (340404, 340400, '谢家集区');
INSERT INTO `72crm_crm_area` VALUES (340406, 340400, '潘集区');
INSERT INTO `72crm_crm_area` VALUES (340422, 340400, '寿县');
INSERT INTO `72crm_crm_area` VALUES (340403, 340400, '田家庵区');
INSERT INTO `72crm_crm_area` VALUES (340402, 340400, '大通区');
INSERT INTO `72crm_crm_area` VALUES (711146, 711100, '三峡区');
INSERT INTO `72crm_crm_area` VALUES (711147, 711100, '树林区');
INSERT INTO `72crm_crm_area` VALUES (711144, 711100, '中和区');
INSERT INTO `72crm_crm_area` VALUES (711145, 711100, '土城区');
INSERT INTO `72crm_crm_area` VALUES (711150, 711100, '新庄区');
INSERT INTO `72crm_crm_area` VALUES (711151, 711100, '泰山区');
INSERT INTO `72crm_crm_area` VALUES (711149, 711100, '三重区');
INSERT INTO `72crm_crm_area` VALUES (711138, 711100, '双溪区');
INSERT INTO `72crm_crm_area` VALUES (711136, 711100, '瑞芳区');
INSERT INTO `72crm_crm_area` VALUES (711137, 711100, '平溪区');
INSERT INTO `72crm_crm_area` VALUES (711142, 711100, '乌来区');
INSERT INTO `72crm_crm_area` VALUES (711143, 711100, '永和区');
INSERT INTO `72crm_crm_area` VALUES (711140, 711100, '新店区');
INSERT INTO `72crm_crm_area` VALUES (711141, 711100, '坪林区');
INSERT INTO `72crm_crm_area` VALUES (711130, 711100, '万里区');
INSERT INTO `72crm_crm_area` VALUES (711134, 711100, '深坑区');
INSERT INTO `72crm_crm_area` VALUES (711132, 711100, '板桥区');
INSERT INTO `72crm_crm_area` VALUES (711133, 711100, '汐止区');
INSERT INTO `72crm_crm_area` VALUES (711154, 711100, '五股区');
INSERT INTO `72crm_crm_area` VALUES (711155, 711100, '八里区');
INSERT INTO `72crm_crm_area` VALUES (711152, 711100, '林口区');
INSERT INTO `72crm_crm_area` VALUES (711156, 711100, '淡水区');
INSERT INTO `72crm_crm_area` VALUES (711157, 711100, '三芝区');
INSERT INTO `72crm_crm_area` VALUES (220602, 220600, '浑江区');
INSERT INTO `72crm_crm_area` VALUES (220681, 220600, '临江市');
INSERT INTO `72crm_crm_area` VALUES (220623, 220600, '长白朝鲜族自治县');
INSERT INTO `72crm_crm_area` VALUES (220622, 220600, '靖宇县');
INSERT INTO `72crm_crm_area` VALUES (220605, 220600, '江源区');
INSERT INTO `72crm_crm_area` VALUES (220621, 220600, '抚松县');
INSERT INTO `72crm_crm_area` VALUES (451030, 451000, '西林县');
INSERT INTO `72crm_crm_area` VALUES (451031, 451000, '隆林各族自治县');
INSERT INTO `72crm_crm_area` VALUES (451028, 451000, '乐业县');
INSERT INTO `72crm_crm_area` VALUES (451029, 451000, '田林县');
INSERT INTO `72crm_crm_area` VALUES (451026, 451000, '那坡县');
INSERT INTO `72crm_crm_area` VALUES (451027, 451000, '凌云县');
INSERT INTO `72crm_crm_area` VALUES (451024, 451000, '德保县');
INSERT INTO `72crm_crm_area` VALUES (451022, 451000, '田东县');
INSERT INTO `72crm_crm_area` VALUES (451023, 451000, '平果县');
INSERT INTO `72crm_crm_area` VALUES (451021, 451000, '田阳县');
INSERT INTO `72crm_crm_area` VALUES (451002, 451000, '右江区');
INSERT INTO `72crm_crm_area` VALUES (451081, 451000, '靖西市');
INSERT INTO `72crm_crm_area` VALUES (360981, 360900, '丰城市');
INSERT INTO `72crm_crm_area` VALUES (360983, 360900, '高安市');
INSERT INTO `72crm_crm_area` VALUES (360902, 360900, '袁州区');
INSERT INTO `72crm_crm_area` VALUES (360982, 360900, '樟树市');
INSERT INTO `72crm_crm_area` VALUES (360925, 360900, '靖安县');
INSERT INTO `72crm_crm_area` VALUES (360924, 360900, '宜丰县');
INSERT INTO `72crm_crm_area` VALUES (360926, 360900, '铜鼓县');
INSERT INTO `72crm_crm_area` VALUES (360921, 360900, '奉新县');
INSERT INTO `72crm_crm_area` VALUES (360923, 360900, '上高县');
INSERT INTO `72crm_crm_area` VALUES (360922, 360900, '万载县');
INSERT INTO `72crm_crm_area` VALUES (621002, 621000, '西峰区');
INSERT INTO `72crm_crm_area` VALUES (621021, 621000, '庆城县');
INSERT INTO `72crm_crm_area` VALUES (621023, 621000, '华池县');
INSERT INTO `72crm_crm_area` VALUES (621022, 621000, '环县');
INSERT INTO `72crm_crm_area` VALUES (621025, 621000, '正宁县');
INSERT INTO `72crm_crm_area` VALUES (621024, 621000, '合水县');
INSERT INTO `72crm_crm_area` VALUES (621027, 621000, '镇原县');
INSERT INTO `72crm_crm_area` VALUES (621026, 621000, '宁县');
INSERT INTO `72crm_crm_area` VALUES (130529, 130500, '巨鹿县');
INSERT INTO `72crm_crm_area` VALUES (130528, 130500, '宁晋县');
INSERT INTO `72crm_crm_area` VALUES (130531, 130500, '广宗县');
INSERT INTO `72crm_crm_area` VALUES (130530, 130500, '新河县');
INSERT INTO `72crm_crm_area` VALUES (130533, 130500, '威县');
INSERT INTO `72crm_crm_area` VALUES (130532, 130500, '平乡县');
INSERT INTO `72crm_crm_area` VALUES (130503, 130500, '桥西区');
INSERT INTO `72crm_crm_area` VALUES (130535, 130500, '临西县');
INSERT INTO `72crm_crm_area` VALUES (130502, 130500, '桥东区');
INSERT INTO `72crm_crm_area` VALUES (130534, 130500, '清河县');
INSERT INTO `72crm_crm_area` VALUES (130581, 130500, '南宫市');
INSERT INTO `72crm_crm_area` VALUES (130582, 130500, '沙河市');
INSERT INTO `72crm_crm_area` VALUES (130521, 130500, '邢台县');
INSERT INTO `72crm_crm_area` VALUES (130523, 130500, '内丘县');
INSERT INTO `72crm_crm_area` VALUES (130522, 130500, '临城县');
INSERT INTO `72crm_crm_area` VALUES (130525, 130500, '隆尧县');
INSERT INTO `72crm_crm_area` VALUES (130524, 130500, '柏乡县');
INSERT INTO `72crm_crm_area` VALUES (130527, 130500, '南和县');
INSERT INTO `72crm_crm_area` VALUES (130526, 130500, '任县');
INSERT INTO `72crm_crm_area` VALUES (510422, 510400, '盐边县');
INSERT INTO `72crm_crm_area` VALUES (510421, 510400, '米易县');
INSERT INTO `72crm_crm_area` VALUES (510403, 510400, '西区');
INSERT INTO `72crm_crm_area` VALUES (510402, 510400, '东区');
INSERT INTO `72crm_crm_area` VALUES (510411, 510400, '仁和区');
INSERT INTO `72crm_crm_area` VALUES (420324, 420300, '竹溪县');
INSERT INTO `72crm_crm_area` VALUES (420325, 420300, '房县');
INSERT INTO `72crm_crm_area` VALUES (420322, 420300, '郧西县');
INSERT INTO `72crm_crm_area` VALUES (420323, 420300, '竹山县');
INSERT INTO `72crm_crm_area` VALUES (420304, 420300, '郧阳区');
INSERT INTO `72crm_crm_area` VALUES (420302, 420300, '茅箭区');
INSERT INTO `72crm_crm_area` VALUES (420303, 420300, '张湾区');
INSERT INTO `72crm_crm_area` VALUES (420381, 420300, '丹江口市');
INSERT INTO `72crm_crm_area` VALUES (211403, 211400, '龙港区');
INSERT INTO `72crm_crm_area` VALUES (211402, 211400, '连山区');
INSERT INTO `72crm_crm_area` VALUES (211481, 211400, '兴城市');
INSERT INTO `72crm_crm_area` VALUES (211422, 211400, '建昌县');
INSERT INTO `72crm_crm_area` VALUES (211421, 211400, '绥中县');
INSERT INTO `72crm_crm_area` VALUES (211404, 211400, '南票区');
INSERT INTO `72crm_crm_area` VALUES (522727, 522700, '平塘县');
INSERT INTO `72crm_crm_area` VALUES (522726, 522700, '独山县');
INSERT INTO `72crm_crm_area` VALUES (522725, 522700, '瓮安县');
INSERT INTO `72crm_crm_area` VALUES (522723, 522700, '贵定县');
INSERT INTO `72crm_crm_area` VALUES (522722, 522700, '荔波县');
INSERT INTO `72crm_crm_area` VALUES (522702, 522700, '福泉市');
INSERT INTO `72crm_crm_area` VALUES (522701, 522700, '都匀市');
INSERT INTO `72crm_crm_area` VALUES (522732, 522700, '三都水族自治县');
INSERT INTO `72crm_crm_area` VALUES (522731, 522700, '惠水县');
INSERT INTO `72crm_crm_area` VALUES (522730, 522700, '龙里县');
INSERT INTO `72crm_crm_area` VALUES (522729, 522700, '长顺县');
INSERT INTO `72crm_crm_area` VALUES (522728, 522700, '罗甸县');
INSERT INTO `72crm_crm_area` VALUES (540121, 540100, '林周县');
INSERT INTO `72crm_crm_area` VALUES (540122, 540100, '当雄县');
INSERT INTO `72crm_crm_area` VALUES (540123, 540100, '尼木县');
INSERT INTO `72crm_crm_area` VALUES (540124, 540100, '曲水县');
INSERT INTO `72crm_crm_area` VALUES (540126, 540100, '达孜县');
INSERT INTO `72crm_crm_area` VALUES (540127, 540100, '墨竹工卡县');
INSERT INTO `72crm_crm_area` VALUES (540102, 540100, '城关区');
INSERT INTO `72crm_crm_area` VALUES (540103, 540100, '堆龙德庆区');
INSERT INTO `72crm_crm_area` VALUES (441826, 441800, '连南瑶族自治县');
INSERT INTO `72crm_crm_area` VALUES (441825, 441800, '连山壮族瑶族自治县');
INSERT INTO `72crm_crm_area` VALUES (441823, 441800, '阳山县');
INSERT INTO `72crm_crm_area` VALUES (441821, 441800, '佛冈县');
INSERT INTO `72crm_crm_area` VALUES (441802, 441800, '清城区');
INSERT INTO `72crm_crm_area` VALUES (441882, 441800, '连州市');
INSERT INTO `72crm_crm_area` VALUES (441803, 441800, '清新区');
INSERT INTO `72crm_crm_area` VALUES (441881, 441800, '英德市');
INSERT INTO `72crm_crm_area` VALUES (450500, 450000, '北海市');
INSERT INTO `72crm_crm_area` VALUES (451300, 450000, '来宾市');
INSERT INTO `72crm_crm_area` VALUES (450400, 450000, '梧州市');
INSERT INTO `72crm_crm_area` VALUES (451200, 450000, '河池市');
INSERT INTO `72crm_crm_area` VALUES (450700, 450000, '钦州市');
INSERT INTO `72crm_crm_area` VALUES (450600, 450000, '防城港市');
INSERT INTO `72crm_crm_area` VALUES (451400, 450000, '崇左市');
INSERT INTO `72crm_crm_area` VALUES (450100, 450000, '南宁市');
INSERT INTO `72crm_crm_area` VALUES (450900, 450000, '玉林市');
INSERT INTO `72crm_crm_area` VALUES (450800, 450000, '贵港市');
INSERT INTO `72crm_crm_area` VALUES (450300, 450000, '桂林市');
INSERT INTO `72crm_crm_area` VALUES (451100, 450000, '贺州市');
INSERT INTO `72crm_crm_area` VALUES (450200, 450000, '柳州市');
INSERT INTO `72crm_crm_area` VALUES (451000, 450000, '百色市');
INSERT INTO `72crm_crm_area` VALUES (411122, 411100, '临颍县');
INSERT INTO `72crm_crm_area` VALUES (411104, 411100, '召陵区');
INSERT INTO `72crm_crm_area` VALUES (411121, 411100, '舞阳县');
INSERT INTO `72crm_crm_area` VALUES (411102, 411100, '源汇区');
INSERT INTO `72crm_crm_area` VALUES (411103, 411100, '郾城区');
INSERT INTO `72crm_crm_area` VALUES (530921, 530900, '凤庆县');
INSERT INTO `72crm_crm_area` VALUES (530922, 530900, '云县');
INSERT INTO `72crm_crm_area` VALUES (530923, 530900, '永德县');
INSERT INTO `72crm_crm_area` VALUES (530924, 530900, '镇康县');
INSERT INTO `72crm_crm_area` VALUES (530925, 530900, '双江拉祜族佤族布朗族傣族自治县');
INSERT INTO `72crm_crm_area` VALUES (530926, 530900, '耿马傣族佤族自治县');
INSERT INTO `72crm_crm_area` VALUES (530927, 530900, '沧源佤族自治县');
INSERT INTO `72crm_crm_area` VALUES (530902, 530900, '临翔区');
INSERT INTO `72crm_crm_area` VALUES (330213, 330200, '奉化区');
INSERT INTO `72crm_crm_area` VALUES (330212, 330200, '鄞州区');
INSERT INTO `72crm_crm_area` VALUES (330225, 330200, '象山县');
INSERT INTO `72crm_crm_area` VALUES (330211, 330200, '镇海区');
INSERT INTO `72crm_crm_area` VALUES (330226, 330200, '宁海县');
INSERT INTO `72crm_crm_area` VALUES (330205, 330200, '江北区');
INSERT INTO `72crm_crm_area` VALUES (330206, 330200, '北仑区');
INSERT INTO `72crm_crm_area` VALUES (330281, 330200, '余姚市');
INSERT INTO `72crm_crm_area` VALUES (330203, 330200, '海曙区');
INSERT INTO `72crm_crm_area` VALUES (330282, 330200, '慈溪市');
INSERT INTO `72crm_crm_area` VALUES (710112, 710100, '文山区');
INSERT INTO `72crm_crm_area` VALUES (710106, 710100, '万华区');
INSERT INTO `72crm_crm_area` VALUES (710107, 710100, '信义区');
INSERT INTO `72crm_crm_area` VALUES (710104, 710100, '松山区');
INSERT INTO `72crm_crm_area` VALUES (710105, 710100, '大安区');
INSERT INTO `72crm_crm_area` VALUES (710110, 710100, '内湖区');
INSERT INTO `72crm_crm_area` VALUES (710111, 710100, '南港区');
INSERT INTO `72crm_crm_area` VALUES (710108, 710100, '士林区');
INSERT INTO `72crm_crm_area` VALUES (710109, 710100, '北投区');
INSERT INTO `72crm_crm_area` VALUES (710102, 710100, '大同区');
INSERT INTO `72crm_crm_area` VALUES (710103, 710100, '中山区');
INSERT INTO `72crm_crm_area` VALUES (710199, 710100, '其它区');
INSERT INTO `72crm_crm_area` VALUES (710101, 710100, '中正区');
INSERT INTO `72crm_crm_area` VALUES (210403, 210400, '东洲区');
INSERT INTO `72crm_crm_area` VALUES (210402, 210400, '新抚区');
INSERT INTO `72crm_crm_area` VALUES (210423, 210400, '清原满族自治县');
INSERT INTO `72crm_crm_area` VALUES (210422, 210400, '新宾满族自治县');
INSERT INTO `72crm_crm_area` VALUES (210421, 210400, '抚顺县');
INSERT INTO `72crm_crm_area` VALUES (210404, 210400, '望花区');
INSERT INTO `72crm_crm_area` VALUES (210411, 210400, '顺城区');
INSERT INTO `72crm_crm_area` VALUES (632321, 632300, '同仁县');
INSERT INTO `72crm_crm_area` VALUES (632323, 632300, '泽库县');
INSERT INTO `72crm_crm_area` VALUES (632322, 632300, '尖扎县');
INSERT INTO `72crm_crm_area` VALUES (632324, 632300, '河南蒙古族自治县');
INSERT INTO `72crm_crm_area` VALUES (440823, 440800, '遂溪县');
INSERT INTO `72crm_crm_area` VALUES (440804, 440800, '坡头区');
INSERT INTO `72crm_crm_area` VALUES (440802, 440800, '赤坎区');
INSERT INTO `72crm_crm_area` VALUES (440882, 440800, '雷州市');
INSERT INTO `72crm_crm_area` VALUES (440803, 440800, '霞山区');
INSERT INTO `72crm_crm_area` VALUES (440883, 440800, '吴川市');
INSERT INTO `72crm_crm_area` VALUES (440881, 440800, '廉江市');
INSERT INTO `72crm_crm_area` VALUES (440811, 440800, '麻章区');
INSERT INTO `72crm_crm_area` VALUES (440825, 440800, '徐闻县');
INSERT INTO `72crm_crm_area` VALUES (350725, 350700, '政和县');
INSERT INTO `72crm_crm_area` VALUES (350724, 350700, '松溪县');
INSERT INTO `72crm_crm_area` VALUES (350721, 350700, '顺昌县');
INSERT INTO `72crm_crm_area` VALUES (350723, 350700, '光泽县');
INSERT INTO `72crm_crm_area` VALUES (350722, 350700, '浦城县');
INSERT INTO `72crm_crm_area` VALUES (350781, 350700, '邵武市');
INSERT INTO `72crm_crm_area` VALUES (350703, 350700, '建阳区');
INSERT INTO `72crm_crm_area` VALUES (350783, 350700, '建瓯市');
INSERT INTO `72crm_crm_area` VALUES (350702, 350700, '延平区');
INSERT INTO `72crm_crm_area` VALUES (350782, 350700, '武夷山市');
INSERT INTO `72crm_crm_area` VALUES (620200, 620000, '嘉峪关市');
INSERT INTO `72crm_crm_area` VALUES (621000, 620000, '庆阳市');
INSERT INTO `72crm_crm_area` VALUES (620300, 620000, '金昌市');
INSERT INTO `72crm_crm_area` VALUES (621100, 620000, '定西市');
INSERT INTO `72crm_crm_area` VALUES (620800, 620000, '平凉市');
INSERT INTO `72crm_crm_area` VALUES (620100, 620000, '兰州市');
INSERT INTO `72crm_crm_area` VALUES (620900, 620000, '酒泉市');
INSERT INTO `72crm_crm_area` VALUES (620600, 620000, '武威市');
INSERT INTO `72crm_crm_area` VALUES (623000, 620000, '甘南藏族自治州');
INSERT INTO `72crm_crm_area` VALUES (620700, 620000, '张掖市');
INSERT INTO `72crm_crm_area` VALUES (620400, 620000, '白银市');
INSERT INTO `72crm_crm_area` VALUES (621200, 620000, '陇南市');
INSERT INTO `72crm_crm_area` VALUES (620500, 620000, '天水市');
INSERT INTO `72crm_crm_area` VALUES (622900, 620000, '临夏回族自治州');
INSERT INTO `72crm_crm_area` VALUES (321012, 321000, '江都区');
INSERT INTO `72crm_crm_area` VALUES (321084, 321000, '高邮市');
INSERT INTO `72crm_crm_area` VALUES (321023, 321000, '宝应县');
INSERT INTO `72crm_crm_area` VALUES (321081, 321000, '仪征市');
INSERT INTO `72crm_crm_area` VALUES (321002, 321000, '广陵区');
INSERT INTO `72crm_crm_area` VALUES (321003, 321000, '邗江区');
INSERT INTO `72crm_crm_area` VALUES (500231, 500200, '垫江县');
INSERT INTO `72crm_crm_area` VALUES (500230, 500200, '丰都县');
INSERT INTO `72crm_crm_area` VALUES (500229, 500200, '城口县');
INSERT INTO `72crm_crm_area` VALUES (500243, 500200, '彭水苗族土家族自治县');
INSERT INTO `72crm_crm_area` VALUES (500242, 500200, '酉阳土家族苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (500241, 500200, '秀山土家族苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (500240, 500200, '石柱土家族自治县');
INSERT INTO `72crm_crm_area` VALUES (500238, 500200, '巫溪县');
INSERT INTO `72crm_crm_area` VALUES (500237, 500200, '巫山县');
INSERT INTO `72crm_crm_area` VALUES (500236, 500200, '奉节县');
INSERT INTO `72crm_crm_area` VALUES (500235, 500200, '云阳县');
INSERT INTO `72crm_crm_area` VALUES (500233, 500200, '忠县');
INSERT INTO `72crm_crm_area` VALUES (150400, 150000, '赤峰市');
INSERT INTO `72crm_crm_area` VALUES (150800, 150000, '巴彦淖尔市');
INSERT INTO `72crm_crm_area` VALUES (150100, 150000, '呼和浩特市');
INSERT INTO `72crm_crm_area` VALUES (150500, 150000, '通辽市');
INSERT INTO `72crm_crm_area` VALUES (150900, 150000, '乌兰察布市');
INSERT INTO `72crm_crm_area` VALUES (152500, 150000, '锡林郭勒盟');
INSERT INTO `72crm_crm_area` VALUES (152900, 150000, '阿拉善盟');
INSERT INTO `72crm_crm_area` VALUES (150200, 150000, '包头市');
INSERT INTO `72crm_crm_area` VALUES (150600, 150000, '鄂尔多斯市');
INSERT INTO `72crm_crm_area` VALUES (152200, 150000, '兴安盟');
INSERT INTO `72crm_crm_area` VALUES (150300, 150000, '乌海市');
INSERT INTO `72crm_crm_area` VALUES (150700, 150000, '呼伦贝尔市');
INSERT INTO `72crm_crm_area` VALUES (410102, 410100, '中原区');
INSERT INTO `72crm_crm_area` VALUES (410182, 410100, '荥阳市');
INSERT INTO `72crm_crm_area` VALUES (410103, 410100, '二七区');
INSERT INTO `72crm_crm_area` VALUES (410183, 410100, '新密市');
INSERT INTO `72crm_crm_area` VALUES (410181, 410100, '巩义市');
INSERT INTO `72crm_crm_area` VALUES (410108, 410100, '惠济区');
INSERT INTO `72crm_crm_area` VALUES (410106, 410100, '上街区');
INSERT INTO `72crm_crm_area` VALUES (410122, 410100, '中牟县');
INSERT INTO `72crm_crm_area` VALUES (410104, 410100, '管城回族区');
INSERT INTO `72crm_crm_area` VALUES (410184, 410100, '新郑市');
INSERT INTO `72crm_crm_area` VALUES (410105, 410100, '金水区');
INSERT INTO `72crm_crm_area` VALUES (410185, 410100, '登封市');
INSERT INTO `72crm_crm_area` VALUES (230903, 230900, '桃山区');
INSERT INTO `72crm_crm_area` VALUES (230902, 230900, '新兴区');
INSERT INTO `72crm_crm_area` VALUES (230921, 230900, '勃利县');
INSERT INTO `72crm_crm_area` VALUES (230904, 230900, '茄子河区');
INSERT INTO `72crm_crm_area` VALUES (341525, 341500, '霍山县');
INSERT INTO `72crm_crm_area` VALUES (341524, 341500, '金寨县');
INSERT INTO `72crm_crm_area` VALUES (341504, 341500, '叶集区');
INSERT INTO `72crm_crm_area` VALUES (341523, 341500, '舒城县');
INSERT INTO `72crm_crm_area` VALUES (341522, 341500, '霍邱县');
INSERT INTO `72crm_crm_area` VALUES (341503, 341500, '裕安区');
INSERT INTO `72crm_crm_area` VALUES (341502, 341500, '金安区');
INSERT INTO `72crm_crm_area` VALUES (610825, 610800, '定边县');
INSERT INTO `72crm_crm_area` VALUES (610824, 610800, '靖边县');
INSERT INTO `72crm_crm_area` VALUES (610827, 610800, '米脂县');
INSERT INTO `72crm_crm_area` VALUES (610826, 610800, '绥德县');
INSERT INTO `72crm_crm_area` VALUES (610829, 610800, '吴堡县');
INSERT INTO `72crm_crm_area` VALUES (610828, 610800, '佳县');
INSERT INTO `72crm_crm_area` VALUES (610831, 610800, '子洲县');
INSERT INTO `72crm_crm_area` VALUES (610830, 610800, '清涧县');
INSERT INTO `72crm_crm_area` VALUES (610881, 610800, '神木市');
INSERT INTO `72crm_crm_area` VALUES (610803, 610800, '横山区');
INSERT INTO `72crm_crm_area` VALUES (610802, 610800, '榆阳区');
INSERT INTO `72crm_crm_area` VALUES (610822, 610800, '府谷县');
INSERT INTO `72crm_crm_area` VALUES (640521, 640500, '中宁县');
INSERT INTO `72crm_crm_area` VALUES (640522, 640500, '海原县');
INSERT INTO `72crm_crm_area` VALUES (640502, 640500, '沙坡头区');
INSERT INTO `72crm_crm_area` VALUES (140802, 140800, '盐湖区');
INSERT INTO `72crm_crm_area` VALUES (140882, 140800, '河津市');
INSERT INTO `72crm_crm_area` VALUES (140881, 140800, '永济市');
INSERT INTO `72crm_crm_area` VALUES (140822, 140800, '万荣县');
INSERT INTO `72crm_crm_area` VALUES (140823, 140800, '闻喜县');
INSERT INTO `72crm_crm_area` VALUES (140821, 140800, '临猗县');
INSERT INTO `72crm_crm_area` VALUES (140826, 140800, '绛县');
INSERT INTO `72crm_crm_area` VALUES (140827, 140800, '垣曲县');
INSERT INTO `72crm_crm_area` VALUES (140824, 140800, '稷山县');
INSERT INTO `72crm_crm_area` VALUES (140825, 140800, '新绛县');
INSERT INTO `72crm_crm_area` VALUES (140830, 140800, '芮城县');
INSERT INTO `72crm_crm_area` VALUES (140828, 140800, '夏县');
INSERT INTO `72crm_crm_area` VALUES (140829, 140800, '平陆县');
INSERT INTO `72crm_crm_area` VALUES (320100, 320000, '南京市');
INSERT INTO `72crm_crm_area` VALUES (320900, 320000, '盐城市');
INSERT INTO `72crm_crm_area` VALUES (320800, 320000, '淮安市');
INSERT INTO `72crm_crm_area` VALUES (320300, 320000, '徐州市');
INSERT INTO `72crm_crm_area` VALUES (321100, 320000, '镇江市');
INSERT INTO `72crm_crm_area` VALUES (320200, 320000, '无锡市');
INSERT INTO `72crm_crm_area` VALUES (321000, 320000, '扬州市');
INSERT INTO `72crm_crm_area` VALUES (320500, 320000, '苏州市');
INSERT INTO `72crm_crm_area` VALUES (321300, 320000, '宿迁市');
INSERT INTO `72crm_crm_area` VALUES (320400, 320000, '常州市');
INSERT INTO `72crm_crm_area` VALUES (321200, 320000, '泰州市');
INSERT INTO `72crm_crm_area` VALUES (320700, 320000, '连云港市');
INSERT INTO `72crm_crm_area` VALUES (320600, 320000, '南通市');
INSERT INTO `72crm_crm_area` VALUES (371203, 371200, '钢城区');
INSERT INTO `72crm_crm_area` VALUES (371202, 371200, '莱城区');
INSERT INTO `72crm_crm_area` VALUES (652825, 652800, '且末县');
INSERT INTO `72crm_crm_area` VALUES (652824, 652800, '若羌县');
INSERT INTO `72crm_crm_area` VALUES (652827, 652800, '和静县');
INSERT INTO `72crm_crm_area` VALUES (652826, 652800, '焉耆回族自治县');
INSERT INTO `72crm_crm_area` VALUES (652829, 652800, '博湖县');
INSERT INTO `72crm_crm_area` VALUES (652828, 652800, '和硕县');
INSERT INTO `72crm_crm_area` VALUES (652801, 652800, '库尔勒市');
INSERT INTO `72crm_crm_area` VALUES (652823, 652800, '尉犁县');
INSERT INTO `72crm_crm_area` VALUES (652822, 652800, '轮台县');
INSERT INTO `72crm_crm_area` VALUES (460323, 460300, '中沙群岛的岛礁及其海域');
INSERT INTO `72crm_crm_area` VALUES (460322, 460300, '南沙群岛');
INSERT INTO `72crm_crm_area` VALUES (460321, 460300, '西沙群岛');
INSERT INTO `72crm_crm_area` VALUES (511527, 511500, '筠连县');
INSERT INTO `72crm_crm_area` VALUES (511526, 511500, '珙县');
INSERT INTO `72crm_crm_area` VALUES (511525, 511500, '高县');
INSERT INTO `72crm_crm_area` VALUES (511524, 511500, '长宁县');
INSERT INTO `72crm_crm_area` VALUES (511523, 511500, '江安县');
INSERT INTO `72crm_crm_area` VALUES (511521, 511500, '宜宾县');
INSERT INTO `72crm_crm_area` VALUES (511503, 511500, '南溪区');
INSERT INTO `72crm_crm_area` VALUES (511502, 511500, '翠屏区');
INSERT INTO `72crm_crm_area` VALUES (511529, 511500, '屏山县');
INSERT INTO `72crm_crm_area` VALUES (511528, 511500, '兴文县');
INSERT INTO `72crm_crm_area` VALUES (430626, 430600, '平江县');
INSERT INTO `72crm_crm_area` VALUES (430611, 430600, '君山区');
INSERT INTO `72crm_crm_area` VALUES (430624, 430600, '湘阴县');
INSERT INTO `72crm_crm_area` VALUES (430623, 430600, '华容县');
INSERT INTO `72crm_crm_area` VALUES (430621, 430600, '岳阳县');
INSERT INTO `72crm_crm_area` VALUES (430602, 430600, '岳阳楼区');
INSERT INTO `72crm_crm_area` VALUES (430682, 430600, '临湘市');
INSERT INTO `72crm_crm_area` VALUES (430603, 430600, '云溪区');
INSERT INTO `72crm_crm_area` VALUES (430681, 430600, '汨罗市');
INSERT INTO `72crm_crm_area` VALUES (340503, 340500, '花山区');
INSERT INTO `72crm_crm_area` VALUES (340521, 340500, '当涂县');
INSERT INTO `72crm_crm_area` VALUES (340504, 340500, '雨山区');
INSERT INTO `72crm_crm_area` VALUES (340523, 340500, '和县');
INSERT INTO `72crm_crm_area` VALUES (340506, 340500, '博望区');
INSERT INTO `72crm_crm_area` VALUES (340522, 340500, '含山县');
INSERT INTO `72crm_crm_area` VALUES (110113, 110100, '顺义区');
INSERT INTO `72crm_crm_area` VALUES (110112, 110100, '通州区');
INSERT INTO `72crm_crm_area` VALUES (110115, 110100, '大兴区');
INSERT INTO `72crm_crm_area` VALUES (110114, 110100, '昌平区');
INSERT INTO `72crm_crm_area` VALUES (110117, 110100, '平谷区');
INSERT INTO `72crm_crm_area` VALUES (110116, 110100, '怀柔区');
INSERT INTO `72crm_crm_area` VALUES (110119, 110100, '延庆区');
INSERT INTO `72crm_crm_area` VALUES (110118, 110100, '密云区');
INSERT INTO `72crm_crm_area` VALUES (110101, 110100, '东城区');
INSERT INTO `72crm_crm_area` VALUES (110102, 110100, '西城区');
INSERT INTO `72crm_crm_area` VALUES (110105, 110100, '朝阳区');
INSERT INTO `72crm_crm_area` VALUES (110107, 110100, '石景山区');
INSERT INTO `72crm_crm_area` VALUES (110106, 110100, '丰台区');
INSERT INTO `72crm_crm_area` VALUES (110109, 110100, '门头沟区');
INSERT INTO `72crm_crm_area` VALUES (110108, 110100, '海淀区');
INSERT INTO `72crm_crm_area` VALUES (110111, 110100, '房山区');
INSERT INTO `72crm_crm_area` VALUES (451122, 451100, '钟山县');
INSERT INTO `72crm_crm_area` VALUES (451123, 451100, '富川瑶族自治县');
INSERT INTO `72crm_crm_area` VALUES (451121, 451100, '昭平县');
INSERT INTO `72crm_crm_area` VALUES (451102, 451100, '八步区');
INSERT INTO `72crm_crm_area` VALUES (451103, 451100, '平桂区');
INSERT INTO `72crm_crm_area` VALUES (370213, 370200, '李沧区');
INSERT INTO `72crm_crm_area` VALUES (370212, 370200, '崂山区');
INSERT INTO `72crm_crm_area` VALUES (370215, 370200, '即墨区');
INSERT INTO `72crm_crm_area` VALUES (370214, 370200, '城阳区');
INSERT INTO `72crm_crm_area` VALUES (370211, 370200, '黄岛区');
INSERT INTO `72crm_crm_area` VALUES (370285, 370200, '莱西市');
INSERT INTO `72crm_crm_area` VALUES (370281, 370200, '胶州市');
INSERT INTO `72crm_crm_area` VALUES (370203, 370200, '市北区');
INSERT INTO `72crm_crm_area` VALUES (370283, 370200, '平度市');
INSERT INTO `72crm_crm_area` VALUES (370202, 370200, '市南区');
INSERT INTO `72crm_crm_area` VALUES (220723, 220700, '乾安县');
INSERT INTO `72crm_crm_area` VALUES (220722, 220700, '长岭县');
INSERT INTO `72crm_crm_area` VALUES (220721, 220700, '前郭尔罗斯蒙古族自治县');
INSERT INTO `72crm_crm_area` VALUES (220702, 220700, '宁江区');
INSERT INTO `72crm_crm_area` VALUES (220781, 220700, '扶余市');
INSERT INTO `72crm_crm_area` VALUES (540232, 540200, '仲巴县');
INSERT INTO `72crm_crm_area` VALUES (540233, 540200, '亚东县');
INSERT INTO `72crm_crm_area` VALUES (540202, 540200, '桑珠孜区');
INSERT INTO `72crm_crm_area` VALUES (540234, 540200, '吉隆县');
INSERT INTO `72crm_crm_area` VALUES (540235, 540200, '聂拉木县');
INSERT INTO `72crm_crm_area` VALUES (540236, 540200, '萨嘎县');
INSERT INTO `72crm_crm_area` VALUES (540237, 540200, '岗巴县');
INSERT INTO `72crm_crm_area` VALUES (540224, 540200, '萨迦县');
INSERT INTO `72crm_crm_area` VALUES (540225, 540200, '拉孜县');
INSERT INTO `72crm_crm_area` VALUES (540226, 540200, '昂仁县');
INSERT INTO `72crm_crm_area` VALUES (540227, 540200, '谢通门县');
INSERT INTO `72crm_crm_area` VALUES (540228, 540200, '白朗县');
INSERT INTO `72crm_crm_area` VALUES (540229, 540200, '仁布县');
INSERT INTO `72crm_crm_area` VALUES (540230, 540200, '康马县');
INSERT INTO `72crm_crm_area` VALUES (540231, 540200, '定结县');
INSERT INTO `72crm_crm_area` VALUES (540221, 540200, '南木林县');
INSERT INTO `72crm_crm_area` VALUES (540222, 540200, '江孜县');
INSERT INTO `72crm_crm_area` VALUES (540223, 540200, '定日县');
INSERT INTO `72crm_crm_area` VALUES (510503, 510500, '纳溪区');
INSERT INTO `72crm_crm_area` VALUES (510502, 510500, '江阳区');
INSERT INTO `72crm_crm_area` VALUES (510525, 510500, '古蔺县');
INSERT INTO `72crm_crm_area` VALUES (510524, 510500, '叙永县');
INSERT INTO `72crm_crm_area` VALUES (510522, 510500, '合江县');
INSERT INTO `72crm_crm_area` VALUES (510521, 510500, '泸县');
INSERT INTO `72crm_crm_area` VALUES (510504, 510500, '龙马潭区');
INSERT INTO `72crm_crm_area` VALUES (621102, 621100, '安定区');
INSERT INTO `72crm_crm_area` VALUES (621121, 621100, '通渭县');
INSERT INTO `72crm_crm_area` VALUES (621123, 621100, '渭源县');
INSERT INTO `72crm_crm_area` VALUES (621122, 621100, '陇西县');
INSERT INTO `72crm_crm_area` VALUES (621125, 621100, '漳县');
INSERT INTO `72crm_crm_area` VALUES (621124, 621100, '临洮县');
INSERT INTO `72crm_crm_area` VALUES (621126, 621100, '岷县');
INSERT INTO `72crm_crm_area` VALUES (130624, 130600, '阜平县');
INSERT INTO `72crm_crm_area` VALUES (130627, 130600, '唐县');
INSERT INTO `72crm_crm_area` VALUES (130626, 130600, '定兴县');
INSERT INTO `72crm_crm_area` VALUES (130629, 130600, '容城县');
INSERT INTO `72crm_crm_area` VALUES (130628, 130600, '高阳县');
INSERT INTO `72crm_crm_area` VALUES (130631, 130600, '望都县');
INSERT INTO `72crm_crm_area` VALUES (130630, 130600, '涞源县');
INSERT INTO `72crm_crm_area` VALUES (130633, 130600, '易县');
INSERT INTO `72crm_crm_area` VALUES (130632, 130600, '安新县');
INSERT INTO `72crm_crm_area` VALUES (130635, 130600, '蠡县');
INSERT INTO `72crm_crm_area` VALUES (130602, 130600, '竞秀区');
INSERT INTO `72crm_crm_area` VALUES (130634, 130600, '曲阳县');
INSERT INTO `72crm_crm_area` VALUES (130637, 130600, '博野县');
INSERT INTO `72crm_crm_area` VALUES (130636, 130600, '顺平县');
INSERT INTO `72crm_crm_area` VALUES (130607, 130600, '满城区');
INSERT INTO `72crm_crm_area` VALUES (130606, 130600, '莲池区');
INSERT INTO `72crm_crm_area` VALUES (130638, 130600, '雄县');
INSERT INTO `72crm_crm_area` VALUES (130609, 130600, '徐水区');
INSERT INTO `72crm_crm_area` VALUES (130608, 130600, '清苑区');
INSERT INTO `72crm_crm_area` VALUES (130681, 130600, '涿州市');
INSERT INTO `72crm_crm_area` VALUES (130683, 130600, '安国市');
INSERT INTO `72crm_crm_area` VALUES (130682, 130600, '定州市');
INSERT INTO `72crm_crm_area` VALUES (130684, 130600, '高碑店市');
INSERT INTO `72crm_crm_area` VALUES (130623, 130600, '涞水县');
INSERT INTO `72crm_crm_area` VALUES (441926, 441900, '石龙镇');
INSERT INTO `72crm_crm_area` VALUES (441927, 441900, '石排镇');
INSERT INTO `72crm_crm_area` VALUES (441924, 441900, '沙田镇');
INSERT INTO `72crm_crm_area` VALUES (441925, 441900, '石碣镇');
INSERT INTO `72crm_crm_area` VALUES (441922, 441900, '桥头镇');
INSERT INTO `72crm_crm_area` VALUES (441923, 441900, '清溪镇');
INSERT INTO `72crm_crm_area` VALUES (441920, 441900, '南城街道');
INSERT INTO `72crm_crm_area` VALUES (441921, 441900, '企石镇');
INSERT INTO `72crm_crm_area` VALUES (441934, 441900, '中堂镇');
INSERT INTO `72crm_crm_area` VALUES (441935, 441900, '茶山镇');
INSERT INTO `72crm_crm_area` VALUES (441932, 441900, '谢岗镇');
INSERT INTO `72crm_crm_area` VALUES (441933, 441900, '樟木头镇');
INSERT INTO `72crm_crm_area` VALUES (441930, 441900, '万江街道');
INSERT INTO `72crm_crm_area` VALUES (441931, 441900, '望牛墩镇');
INSERT INTO `72crm_crm_area` VALUES (441928, 441900, '松山湖管委会');
INSERT INTO `72crm_crm_area` VALUES (441929, 441900, '塘厦镇');
INSERT INTO `72crm_crm_area` VALUES (441902, 441900, '常平镇');
INSERT INTO `72crm_crm_area` VALUES (441903, 441900, '太朗镇');
INSERT INTO `72crm_crm_area` VALUES (441901, 441900, '长安镇');
INSERT INTO `72crm_crm_area` VALUES (441910, 441900, '高埗镇');
INSERT INTO `72crm_crm_area` VALUES (441911, 441900, '莞城街道');
INSERT INTO `72crm_crm_area` VALUES (441908, 441900, '东坑镇');
INSERT INTO `72crm_crm_area` VALUES (441909, 441900, '凤岗镇');
INSERT INTO `72crm_crm_area` VALUES (441906, 441900, '东城街道');
INSERT INTO `72crm_crm_area` VALUES (441907, 441900, '东莞生态园');
INSERT INTO `72crm_crm_area` VALUES (441904, 441900, '大岭山镇');
INSERT INTO `72crm_crm_area` VALUES (441905, 441900, '道滘镇');
INSERT INTO `72crm_crm_area` VALUES (441918, 441900, '寮步镇');
INSERT INTO `72crm_crm_area` VALUES (441919, 441900, '麻涌镇');
INSERT INTO `72crm_crm_area` VALUES (441916, 441900, '虎门镇');
INSERT INTO `72crm_crm_area` VALUES (441917, 441900, '黄江镇');
INSERT INTO `72crm_crm_area` VALUES (441914, 441900, '厚街镇');
INSERT INTO `72crm_crm_area` VALUES (441915, 441900, '虎门港管委会');
INSERT INTO `72crm_crm_area` VALUES (441912, 441900, '横沥镇');
INSERT INTO `72crm_crm_area` VALUES (441913, 441900, '洪梅镇');
INSERT INTO `72crm_crm_area` VALUES (711298, 711200, '南澳乡');
INSERT INTO `72crm_crm_area` VALUES (711299, 711200, '钓鱼台');
INSERT INTO `72crm_crm_area` VALUES (711296, 711200, '冬山乡');
INSERT INTO `72crm_crm_area` VALUES (711297, 711200, '苏澳镇');
INSERT INTO `72crm_crm_area` VALUES (711290, 711200, '壮围乡');
INSERT INTO `72crm_crm_area` VALUES (711291, 711200, '员山乡');
INSERT INTO `72crm_crm_area` VALUES (711288, 711200, '头城镇');
INSERT INTO `72crm_crm_area` VALUES (711289, 711200, '礁溪乡');
INSERT INTO `72crm_crm_area` VALUES (711294, 711200, '大同乡');
INSERT INTO `72crm_crm_area` VALUES (711295, 711200, '五结乡');
INSERT INTO `72crm_crm_area` VALUES (711292, 711200, '罗东镇');
INSERT INTO `72crm_crm_area` VALUES (711293, 711200, '三星乡');
INSERT INTO `72crm_crm_area` VALUES (711287, 711200, '宜兰市');
INSERT INTO `72crm_crm_area` VALUES (361028, 361000, '资溪县');
INSERT INTO `72crm_crm_area` VALUES (361030, 361000, '广昌县');
INSERT INTO `72crm_crm_area` VALUES (361025, 361000, '乐安县');
INSERT INTO `72crm_crm_area` VALUES (361024, 361000, '崇仁县');
INSERT INTO `72crm_crm_area` VALUES (361027, 361000, '金溪县');
INSERT INTO `72crm_crm_area` VALUES (361026, 361000, '宜黄县');
INSERT INTO `72crm_crm_area` VALUES (361021, 361000, '南城县');
INSERT INTO `72crm_crm_area` VALUES (361023, 361000, '南丰县');
INSERT INTO `72crm_crm_area` VALUES (361022, 361000, '黎川县');
INSERT INTO `72crm_crm_area` VALUES (361003, 361000, '东乡区');
INSERT INTO `72crm_crm_area` VALUES (361002, 361000, '临川区');
INSERT INTO `72crm_crm_area` VALUES (450102, 450100, '兴宁区');
INSERT INTO `72crm_crm_area` VALUES (450103, 450100, '青秀区');
INSERT INTO `72crm_crm_area` VALUES (450110, 450100, '武鸣区');
INSERT INTO `72crm_crm_area` VALUES (450126, 450100, '宾阳县');
INSERT INTO `72crm_crm_area` VALUES (450127, 450100, '横县');
INSERT INTO `72crm_crm_area` VALUES (450108, 450100, '良庆区');
INSERT INTO `72crm_crm_area` VALUES (450124, 450100, '马山县');
INSERT INTO `72crm_crm_area` VALUES (450109, 450100, '邕宁区');
INSERT INTO `72crm_crm_area` VALUES (450125, 450100, '上林县');
INSERT INTO `72crm_crm_area` VALUES (450107, 450100, '西乡塘区');
INSERT INTO `72crm_crm_area` VALUES (450123, 450100, '隆安县');
INSERT INTO `72crm_crm_area` VALUES (450105, 450100, '江南区');
INSERT INTO `72crm_crm_area` VALUES (659002, 659000, '阿拉尔市');
INSERT INTO `72crm_crm_area` VALUES (659003, 659000, '图木舒克市');
INSERT INTO `72crm_crm_area` VALUES (659001, 659000, '石河子市');
INSERT INTO `72crm_crm_area` VALUES (659006, 659000, '铁门关市');
INSERT INTO `72crm_crm_area` VALUES (659007, 659000, '双河市');
INSERT INTO `72crm_crm_area` VALUES (659004, 659000, '五家渠市');
INSERT INTO `72crm_crm_area` VALUES (659005, 659000, '北屯市');
INSERT INTO `72crm_crm_area` VALUES (659008, 659000, '可克达拉市');
INSERT INTO `72crm_crm_area` VALUES (659009, 659000, '昆玉市');
INSERT INTO `72crm_crm_area` VALUES (710210, 710200, '楠梓区');
INSERT INTO `72crm_crm_area` VALUES (710211, 710200, '小港区');
INSERT INTO `72crm_crm_area` VALUES (710208, 710200, '三民区');
INSERT INTO `72crm_crm_area` VALUES (710209, 710200, '左营区');
INSERT INTO `72crm_crm_area` VALUES (710299, 710200, '其它区');
INSERT INTO `72crm_crm_area` VALUES (710250, 710200, '梓官区');
INSERT INTO `72crm_crm_area` VALUES (710251, 710200, '弥陀区');
INSERT INTO `72crm_crm_area` VALUES (710248, 710200, '燕巢区');
INSERT INTO `72crm_crm_area` VALUES (710249, 710200, '桥头区');
INSERT INTO `72crm_crm_area` VALUES (710254, 710200, '凤山区');
INSERT INTO `72crm_crm_area` VALUES (710255, 710200, '大寮区');
INSERT INTO `72crm_crm_area` VALUES (710252, 710200, '永安区');
INSERT INTO `72crm_crm_area` VALUES (710253, 710200, '湖内区');
INSERT INTO `72crm_crm_area` VALUES (710242, 710200, '仁武区');
INSERT INTO `72crm_crm_area` VALUES (710243, 710200, '大社区');
INSERT INTO `72crm_crm_area` VALUES (710241, 710200, '苓雅区');
INSERT INTO `72crm_crm_area` VALUES (710246, 710200, '阿莲区');
INSERT INTO `72crm_crm_area` VALUES (710247, 710200, '田寮区');
INSERT INTO `72crm_crm_area` VALUES (710244, 710200, '冈山区');
INSERT INTO `72crm_crm_area` VALUES (710245, 710200, '路竹区');
INSERT INTO `72crm_crm_area` VALUES (710202, 710200, '前金区');
INSERT INTO `72crm_crm_area` VALUES (710266, 710200, '那玛夏区');
INSERT INTO `72crm_crm_area` VALUES (710203, 710200, '芩雅区');
INSERT INTO `72crm_crm_area` VALUES (710267, 710200, '茂林区');
INSERT INTO `72crm_crm_area` VALUES (710264, 710200, '甲仙区');
INSERT INTO `72crm_crm_area` VALUES (710201, 710200, '新兴区');
INSERT INTO `72crm_crm_area` VALUES (710265, 710200, '桃源区');
INSERT INTO `72crm_crm_area` VALUES (710206, 710200, '旗津区');
INSERT INTO `72crm_crm_area` VALUES (710207, 710200, '前镇区');
INSERT INTO `72crm_crm_area` VALUES (710204, 710200, '盐埕区');
INSERT INTO `72crm_crm_area` VALUES (710268, 710200, '茄萣区');
INSERT INTO `72crm_crm_area` VALUES (710205, 710200, '鼓山区');
INSERT INTO `72crm_crm_area` VALUES (710258, 710200, '大树区');
INSERT INTO `72crm_crm_area` VALUES (710259, 710200, '旗山区');
INSERT INTO `72crm_crm_area` VALUES (710256, 710200, '林园区');
INSERT INTO `72crm_crm_area` VALUES (710257, 710200, '鸟松区');
INSERT INTO `72crm_crm_area` VALUES (710262, 710200, '内门区');
INSERT INTO `72crm_crm_area` VALUES (710263, 710200, '杉林区');
INSERT INTO `72crm_crm_area` VALUES (710260, 710200, '美浓区');
INSERT INTO `72crm_crm_area` VALUES (710261, 710200, '六龟区');
INSERT INTO `72crm_crm_area` VALUES (330324, 330300, '永嘉县');
INSERT INTO `72crm_crm_area` VALUES (330327, 330300, '苍南县');
INSERT INTO `72crm_crm_area` VALUES (330326, 330300, '平阳县');
INSERT INTO `72crm_crm_area` VALUES (330305, 330300, '洞头区');
INSERT INTO `72crm_crm_area` VALUES (330304, 330300, '瓯海区');
INSERT INTO `72crm_crm_area` VALUES (330381, 330300, '瑞安市');
INSERT INTO `72crm_crm_area` VALUES (330303, 330300, '龙湾区');
INSERT INTO `72crm_crm_area` VALUES (330302, 330300, '鹿城区');
INSERT INTO `72crm_crm_area` VALUES (330382, 330300, '乐清市');
INSERT INTO `72crm_crm_area` VALUES (330329, 330300, '泰顺县');
INSERT INTO `72crm_crm_area` VALUES (330328, 330300, '文成县');
INSERT INTO `72crm_crm_area` VALUES (440902, 440900, '茂南区');
INSERT INTO `72crm_crm_area` VALUES (440982, 440900, '化州市');
INSERT INTO `72crm_crm_area` VALUES (440983, 440900, '信宜市');
INSERT INTO `72crm_crm_area` VALUES (440981, 440900, '高州市');
INSERT INTO `72crm_crm_area` VALUES (440904, 440900, '电白区');
INSERT INTO `72crm_crm_area` VALUES (360100, 360000, '南昌市');
INSERT INTO `72crm_crm_area` VALUES (360500, 360000, '新余市');
INSERT INTO `72crm_crm_area` VALUES (360900, 360000, '宜春市');
INSERT INTO `72crm_crm_area` VALUES (360400, 360000, '九江市');
INSERT INTO `72crm_crm_area` VALUES (360800, 360000, '吉安市');
INSERT INTO `72crm_crm_area` VALUES (360300, 360000, '萍乡市');
INSERT INTO `72crm_crm_area` VALUES (360700, 360000, '赣州市');
INSERT INTO `72crm_crm_area` VALUES (361100, 360000, '上饶市');
INSERT INTO `72crm_crm_area` VALUES (360200, 360000, '景德镇市');
INSERT INTO `72crm_crm_area` VALUES (360600, 360000, '鹰潭市');
INSERT INTO `72crm_crm_area` VALUES (361000, 360000, '抚州市');
INSERT INTO `72crm_crm_area` VALUES (411221, 411200, '渑池县');
INSERT INTO `72crm_crm_area` VALUES (411202, 411200, '湖滨区');
INSERT INTO `72crm_crm_area` VALUES (411282, 411200, '灵宝市');
INSERT INTO `72crm_crm_area` VALUES (411203, 411200, '陕州区');
INSERT INTO `72crm_crm_area` VALUES (411281, 411200, '义马市');
INSERT INTO `72crm_crm_area` VALUES (411224, 411200, '卢氏县');
INSERT INTO `72crm_crm_area` VALUES (210503, 210500, '溪湖区');
INSERT INTO `72crm_crm_area` VALUES (210502, 210500, '平山区');
INSERT INTO `72crm_crm_area` VALUES (210522, 210500, '桓仁满族自治县');
INSERT INTO `72crm_crm_area` VALUES (210505, 210500, '南芬区');
INSERT INTO `72crm_crm_area` VALUES (210521, 210500, '本溪满族自治县');
INSERT INTO `72crm_crm_area` VALUES (210504, 210500, '明山区');
INSERT INTO `72crm_crm_area` VALUES (321111, 321100, '润州区');
INSERT INTO `72crm_crm_area` VALUES (321181, 321100, '丹阳市');
INSERT INTO `72crm_crm_area` VALUES (321102, 321100, '京口区');
INSERT INTO `72crm_crm_area` VALUES (321182, 321100, '扬中市');
INSERT INTO `72crm_crm_area` VALUES (321183, 321100, '句容市');
INSERT INTO `72crm_crm_area` VALUES (321112, 321100, '丹徒区');
INSERT INTO `72crm_crm_area` VALUES (620105, 620100, '安宁区');
INSERT INTO `72crm_crm_area` VALUES (620121, 620100, '永登县');
INSERT INTO `72crm_crm_area` VALUES (620104, 620100, '西固区');
INSERT INTO `72crm_crm_area` VALUES (620123, 620100, '榆中县');
INSERT INTO `72crm_crm_area` VALUES (620122, 620100, '皋兰县');
INSERT INTO `72crm_crm_area` VALUES (620111, 620100, '红古区');
INSERT INTO `72crm_crm_area` VALUES (620103, 620100, '七里河区');
INSERT INTO `72crm_crm_area` VALUES (620102, 620100, '城关区');
INSERT INTO `72crm_crm_area` VALUES (350821, 350800, '长汀县');
INSERT INTO `72crm_crm_area` VALUES (350823, 350800, '上杭县');
INSERT INTO `72crm_crm_area` VALUES (350881, 350800, '漳平市');
INSERT INTO `72crm_crm_area` VALUES (350803, 350800, '永定区');
INSERT INTO `72crm_crm_area` VALUES (350802, 350800, '新罗区');
INSERT INTO `72crm_crm_area` VALUES (350825, 350800, '连城县');
INSERT INTO `72crm_crm_area` VALUES (350824, 350800, '武平县');
INSERT INTO `72crm_crm_area` VALUES (150102, 150100, '新城区');
INSERT INTO `72crm_crm_area` VALUES (150103, 150100, '回民区');
INSERT INTO `72crm_crm_area` VALUES (150122, 150100, '托克托县');
INSERT INTO `72crm_crm_area` VALUES (150123, 150100, '和林格尔县');
INSERT INTO `72crm_crm_area` VALUES (150104, 150100, '玉泉区');
INSERT INTO `72crm_crm_area` VALUES (150105, 150100, '赛罕区');
INSERT INTO `72crm_crm_area` VALUES (150121, 150100, '土默特左旗');
INSERT INTO `72crm_crm_area` VALUES (150124, 150100, '清水河县');
INSERT INTO `72crm_crm_area` VALUES (150125, 150100, '武川县');
INSERT INTO `72crm_crm_area` VALUES (530600, 530000, '昭通市');
INSERT INTO `72crm_crm_area` VALUES (530700, 530000, '丽江市');
INSERT INTO `72crm_crm_area` VALUES (532300, 530000, '楚雄彝族自治州');
INSERT INTO `72crm_crm_area` VALUES (533100, 530000, '德宏傣族景颇族自治州');
INSERT INTO `72crm_crm_area` VALUES (530400, 530000, '玉溪市');
INSERT INTO `72crm_crm_area` VALUES (532800, 530000, '西双版纳傣族自治州');
INSERT INTO `72crm_crm_area` VALUES (530500, 530000, '保山市');
INSERT INTO `72crm_crm_area` VALUES (532900, 530000, '大理白族自治州');
INSERT INTO `72crm_crm_area` VALUES (532600, 530000, '文山壮族苗族自治州');
INSERT INTO `72crm_crm_area` VALUES (533400, 530000, '迪庆藏族自治州');
INSERT INTO `72crm_crm_area` VALUES (530300, 530000, '曲靖市');
INSERT INTO `72crm_crm_area` VALUES (530800, 530000, '普洱市');
INSERT INTO `72crm_crm_area` VALUES (530100, 530000, '昆明市');
INSERT INTO `72crm_crm_area` VALUES (530900, 530000, '临沧市');
INSERT INTO `72crm_crm_area` VALUES (532500, 530000, '红河哈尼族彝族自治州');
INSERT INTO `72crm_crm_area` VALUES (533300, 530000, '怒江傈僳族自治州');
INSERT INTO `72crm_crm_area` VALUES (231025, 231000, '林口县');
INSERT INTO `72crm_crm_area` VALUES (231003, 231000, '阳明区');
INSERT INTO `72crm_crm_area` VALUES (231083, 231000, '海林市');
INSERT INTO `72crm_crm_area` VALUES (231002, 231000, '东安区');
INSERT INTO `72crm_crm_area` VALUES (231081, 231000, '绥芬河市');
INSERT INTO `72crm_crm_area` VALUES (231086, 231000, '东宁市');
INSERT INTO `72crm_crm_area` VALUES (231005, 231000, '西安区');
INSERT INTO `72crm_crm_area` VALUES (231085, 231000, '穆棱市');
INSERT INTO `72crm_crm_area` VALUES (231004, 231000, '爱民区');
INSERT INTO `72crm_crm_area` VALUES (231084, 231000, '宁安市');
INSERT INTO `72crm_crm_area` VALUES (610921, 610900, '汉阴县');
INSERT INTO `72crm_crm_area` VALUES (610923, 610900, '宁陕县');
INSERT INTO `72crm_crm_area` VALUES (610922, 610900, '石泉县');
INSERT INTO `72crm_crm_area` VALUES (610925, 610900, '岚皋县');
INSERT INTO `72crm_crm_area` VALUES (610924, 610900, '紫阳县');
INSERT INTO `72crm_crm_area` VALUES (610927, 610900, '镇坪县');
INSERT INTO `72crm_crm_area` VALUES (610926, 610900, '平利县');
INSERT INTO `72crm_crm_area` VALUES (610929, 610900, '白河县');
INSERT INTO `72crm_crm_area` VALUES (610928, 610900, '旬阳县');
INSERT INTO `72crm_crm_area` VALUES (610902, 610900, '汉滨区');
INSERT INTO `72crm_crm_area` VALUES (410212, 410200, '祥符区');
INSERT INTO `72crm_crm_area` VALUES (410225, 410200, '兰考县');
INSERT INTO `72crm_crm_area` VALUES (410222, 410200, '通许县');
INSERT INTO `72crm_crm_area` VALUES (410223, 410200, '尉氏县');
INSERT INTO `72crm_crm_area` VALUES (410204, 410200, '鼓楼区');
INSERT INTO `72crm_crm_area` VALUES (410205, 410200, '禹王台区');
INSERT INTO `72crm_crm_area` VALUES (410221, 410200, '杞县');
INSERT INTO `72crm_crm_area` VALUES (410202, 410200, '龙亭区');
INSERT INTO `72crm_crm_area` VALUES (410203, 410200, '顺河回族区');
INSERT INTO `72crm_crm_area` VALUES (320116, 320100, '六合区');
INSERT INTO `72crm_crm_area` VALUES (320117, 320100, '溧水区');
INSERT INTO `72crm_crm_area` VALUES (320102, 320100, '玄武区');
INSERT INTO `72crm_crm_area` VALUES (320118, 320100, '高淳区');
INSERT INTO `72crm_crm_area` VALUES (320113, 320100, '栖霞区');
INSERT INTO `72crm_crm_area` VALUES (320114, 320100, '雨花台区');
INSERT INTO `72crm_crm_area` VALUES (320115, 320100, '江宁区');
INSERT INTO `72crm_crm_area` VALUES (320111, 320100, '浦口区');
INSERT INTO `72crm_crm_area` VALUES (320104, 320100, '秦淮区');
INSERT INTO `72crm_crm_area` VALUES (320105, 320100, '建邺区');
INSERT INTO `72crm_crm_area` VALUES (320106, 320100, '鼓楼区');
INSERT INTO `72crm_crm_area` VALUES (371302, 371300, '兰山区');
INSERT INTO `72crm_crm_area` VALUES (371329, 371300, '临沭县');
INSERT INTO `72crm_crm_area` VALUES (371312, 371300, '河东区');
INSERT INTO `72crm_crm_area` VALUES (371328, 371300, '蒙阴县');
INSERT INTO `72crm_crm_area` VALUES (371325, 371300, '费县');
INSERT INTO `72crm_crm_area` VALUES (371324, 371300, '兰陵县');
INSERT INTO `72crm_crm_area` VALUES (371311, 371300, '罗庄区');
INSERT INTO `72crm_crm_area` VALUES (371327, 371300, '莒南县');
INSERT INTO `72crm_crm_area` VALUES (371326, 371300, '平邑县');
INSERT INTO `72crm_crm_area` VALUES (371321, 371300, '沂南县');
INSERT INTO `72crm_crm_area` VALUES (371323, 371300, '沂水县');
INSERT INTO `72crm_crm_area` VALUES (371322, 371300, '郯城县');
INSERT INTO `72crm_crm_area` VALUES (533102, 533100, '瑞丽市');
INSERT INTO `72crm_crm_area` VALUES (533103, 533100, '芒市');
INSERT INTO `72crm_crm_area` VALUES (533122, 533100, '梁河县');
INSERT INTO `72crm_crm_area` VALUES (533123, 533100, '盈江县');
INSERT INTO `72crm_crm_area` VALUES (533124, 533100, '陇川县');
INSERT INTO `72crm_crm_area` VALUES (341621, 341600, '涡阳县');
INSERT INTO `72crm_crm_area` VALUES (341623, 341600, '利辛县');
INSERT INTO `72crm_crm_area` VALUES (341622, 341600, '蒙城县');
INSERT INTO `72crm_crm_area` VALUES (341602, 341600, '谯城区');
INSERT INTO `72crm_crm_area` VALUES (140930, 140900, '河曲县');
INSERT INTO `72crm_crm_area` VALUES (140931, 140900, '保德县');
INSERT INTO `72crm_crm_area` VALUES (140928, 140900, '五寨县');
INSERT INTO `72crm_crm_area` VALUES (140929, 140900, '岢岚县');
INSERT INTO `72crm_crm_area` VALUES (140902, 140900, '忻府区');
INSERT INTO `72crm_crm_area` VALUES (140932, 140900, '偏关县');
INSERT INTO `72crm_crm_area` VALUES (140981, 140900, '原平市');
INSERT INTO `72crm_crm_area` VALUES (140922, 140900, '五台县');
INSERT INTO `72crm_crm_area` VALUES (140923, 140900, '代县');
INSERT INTO `72crm_crm_area` VALUES (140921, 140900, '定襄县');
INSERT INTO `72crm_crm_area` VALUES (140926, 140900, '静乐县');
INSERT INTO `72crm_crm_area` VALUES (140927, 140900, '神池县');
INSERT INTO `72crm_crm_area` VALUES (140924, 140900, '繁峙县');
INSERT INTO `72crm_crm_area` VALUES (140925, 140900, '宁武县');
INSERT INTO `72crm_crm_area` VALUES (430726, 430700, '石门县');
INSERT INTO `72crm_crm_area` VALUES (430724, 430700, '临澧县');
INSERT INTO `72crm_crm_area` VALUES (430725, 430700, '桃源县');
INSERT INTO `72crm_crm_area` VALUES (430722, 430700, '汉寿县');
INSERT INTO `72crm_crm_area` VALUES (430723, 430700, '澧县');
INSERT INTO `72crm_crm_area` VALUES (430721, 430700, '安乡县');
INSERT INTO `72crm_crm_area` VALUES (430702, 430700, '武陵区');
INSERT INTO `72crm_crm_area` VALUES (430703, 430700, '鼎城区');
INSERT INTO `72crm_crm_area` VALUES (430781, 430700, '津市市');
INSERT INTO `72crm_crm_area` VALUES (652923, 652900, '库车县');
INSERT INTO `72crm_crm_area` VALUES (652922, 652900, '温宿县');
INSERT INTO `72crm_crm_area` VALUES (652925, 652900, '新和县');
INSERT INTO `72crm_crm_area` VALUES (652924, 652900, '沙雅县');
INSERT INTO `72crm_crm_area` VALUES (652927, 652900, '乌什县');
INSERT INTO `72crm_crm_area` VALUES (652926, 652900, '拜城县');
INSERT INTO `72crm_crm_area` VALUES (652929, 652900, '柯坪县');
INSERT INTO `72crm_crm_area` VALUES (652928, 652900, '阿瓦提县');
INSERT INTO `72crm_crm_area` VALUES (652901, 652900, '阿克苏市');
INSERT INTO `72crm_crm_area` VALUES (230400, 230000, '鹤岗市');
INSERT INTO `72crm_crm_area` VALUES (231200, 230000, '绥化市');
INSERT INTO `72crm_crm_area` VALUES (230500, 230000, '双鸭山市');
INSERT INTO `72crm_crm_area` VALUES (230600, 230000, '大庆市');
INSERT INTO `72crm_crm_area` VALUES (230700, 230000, '伊春市');
INSERT INTO `72crm_crm_area` VALUES (230800, 230000, '佳木斯市');
INSERT INTO `72crm_crm_area` VALUES (230100, 230000, '哈尔滨市');
INSERT INTO `72crm_crm_area` VALUES (230900, 230000, '七台河市');
INSERT INTO `72crm_crm_area` VALUES (230200, 230000, '齐齐哈尔市');
INSERT INTO `72crm_crm_area` VALUES (231000, 230000, '牡丹江市');
INSERT INTO `72crm_crm_area` VALUES (230300, 230000, '鸡西市');
INSERT INTO `72crm_crm_area` VALUES (231100, 230000, '黑河市');
INSERT INTO `72crm_crm_area` VALUES (232700, 230000, '大兴安岭地区');
INSERT INTO `72crm_crm_area` VALUES (460416, 460400, '新州镇');
INSERT INTO `72crm_crm_area` VALUES (460407, 460400, '光村镇');
INSERT INTO `72crm_crm_area` VALUES (460406, 460400, '兰洋镇');
INSERT INTO `72crm_crm_area` VALUES (460405, 460400, '雅星镇');
INSERT INTO `72crm_crm_area` VALUES (460404, 460400, '大成镇');
INSERT INTO `72crm_crm_area` VALUES (460403, 460400, '南丰镇');
INSERT INTO `72crm_crm_area` VALUES (460402, 460400, '和庆镇');
INSERT INTO `72crm_crm_area` VALUES (460401, 460400, '那大镇');
INSERT INTO `72crm_crm_area` VALUES (460415, 460400, '东成镇');
INSERT INTO `72crm_crm_area` VALUES (460414, 460400, '排浦镇');
INSERT INTO `72crm_crm_area` VALUES (460413, 460400, '中和镇');
INSERT INTO `72crm_crm_area` VALUES (460412, 460400, '白马井镇');
INSERT INTO `72crm_crm_area` VALUES (460411, 460400, '王五镇');
INSERT INTO `72crm_crm_area` VALUES (460410, 460400, '峨蔓镇');
INSERT INTO `72crm_crm_area` VALUES (460409, 460400, '海头镇');
INSERT INTO `72crm_crm_area` VALUES (460408, 460400, '木棠镇');
INSERT INTO `72crm_crm_area` VALUES (511623, 511600, '邻水县');
INSERT INTO `72crm_crm_area` VALUES (511622, 511600, '武胜县');
INSERT INTO `72crm_crm_area` VALUES (511621, 511600, '岳池县');
INSERT INTO `72crm_crm_area` VALUES (511603, 511600, '前锋区');
INSERT INTO `72crm_crm_area` VALUES (511602, 511600, '广安区');
INSERT INTO `72crm_crm_area` VALUES (511681, 511600, '华蓥市');
INSERT INTO `72crm_crm_area` VALUES (370305, 370300, '临淄区');
INSERT INTO `72crm_crm_area` VALUES (370321, 370300, '桓台县');
INSERT INTO `72crm_crm_area` VALUES (370304, 370300, '博山区');
INSERT INTO `72crm_crm_area` VALUES (370323, 370300, '沂源县');
INSERT INTO `72crm_crm_area` VALUES (370306, 370300, '周村区');
INSERT INTO `72crm_crm_area` VALUES (370322, 370300, '高青县');
INSERT INTO `72crm_crm_area` VALUES (370303, 370300, '张店区');
INSERT INTO `72crm_crm_area` VALUES (370302, 370300, '淄川区');
INSERT INTO `72crm_crm_area` VALUES (340621, 340600, '濉溪县');
INSERT INTO `72crm_crm_area` VALUES (340604, 340600, '烈山区');
INSERT INTO `72crm_crm_area` VALUES (340603, 340600, '相山区');
INSERT INTO `72crm_crm_area` VALUES (340602, 340600, '杜集区');
INSERT INTO `72crm_crm_area` VALUES (220802, 220800, '洮北区');
INSERT INTO `72crm_crm_area` VALUES (220882, 220800, '大安市');
INSERT INTO `72crm_crm_area` VALUES (220881, 220800, '洮南市');
INSERT INTO `72crm_crm_area` VALUES (220822, 220800, '通榆县');
INSERT INTO `72crm_crm_area` VALUES (220821, 220800, '镇赉县');
INSERT INTO `72crm_crm_area` VALUES (540328, 540300, '芒康县');
INSERT INTO `72crm_crm_area` VALUES (540329, 540300, '洛隆县');
INSERT INTO `72crm_crm_area` VALUES (540330, 540300, '边坝县');
INSERT INTO `72crm_crm_area` VALUES (540302, 540300, '卡若区');
INSERT INTO `72crm_crm_area` VALUES (540321, 540300, '江达县');
INSERT INTO `72crm_crm_area` VALUES (540322, 540300, '贡觉县');
INSERT INTO `72crm_crm_area` VALUES (540323, 540300, '类乌齐县');
INSERT INTO `72crm_crm_area` VALUES (540324, 540300, '丁青县');
INSERT INTO `72crm_crm_area` VALUES (540325, 540300, '察雅县');
INSERT INTO `72crm_crm_area` VALUES (540326, 540300, '八宿县');
INSERT INTO `72crm_crm_area` VALUES (540327, 540300, '左贡县');
INSERT INTO `72crm_crm_area` VALUES (451222, 451200, '天峨县');
INSERT INTO `72crm_crm_area` VALUES (451223, 451200, '凤山县');
INSERT INTO `72crm_crm_area` VALUES (451221, 451200, '南丹县');
INSERT INTO `72crm_crm_area` VALUES (451202, 451200, '金城江区');
INSERT INTO `72crm_crm_area` VALUES (451203, 451200, '宜州区');
INSERT INTO `72crm_crm_area` VALUES (451228, 451200, '都安瑶族自治县');
INSERT INTO `72crm_crm_area` VALUES (451229, 451200, '大化瑶族自治县');
INSERT INTO `72crm_crm_area` VALUES (451226, 451200, '环江毛南族自治县');
INSERT INTO `72crm_crm_area` VALUES (451227, 451200, '巴马瑶族自治县');
INSERT INTO `72crm_crm_area` VALUES (451224, 451200, '东兰县');
INSERT INTO `72crm_crm_area` VALUES (451225, 451200, '罗城仫佬族自治县');
INSERT INTO `72crm_crm_area` VALUES (361125, 361100, '横峰县');
INSERT INTO `72crm_crm_area` VALUES (361124, 361100, '铅山县');
INSERT INTO `72crm_crm_area` VALUES (361127, 361100, '余干县');
INSERT INTO `72crm_crm_area` VALUES (361126, 361100, '弋阳县');
INSERT INTO `72crm_crm_area` VALUES (361121, 361100, '上饶县');
INSERT INTO `72crm_crm_area` VALUES (361123, 361100, '玉山县');
INSERT INTO `72crm_crm_area` VALUES (361181, 361100, '德兴市');
INSERT INTO `72crm_crm_area` VALUES (361103, 361100, '广丰区');
INSERT INTO `72crm_crm_area` VALUES (361102, 361100, '信州区');
INSERT INTO `72crm_crm_area` VALUES (361129, 361100, '万年县');
INSERT INTO `72crm_crm_area` VALUES (361128, 361100, '鄱阳县');
INSERT INTO `72crm_crm_area` VALUES (361130, 361100, '婺源县');
INSERT INTO `72crm_crm_area` VALUES (152224, 152200, '突泉县');
INSERT INTO `72crm_crm_area` VALUES (152202, 152200, '阿尔山市');
INSERT INTO `72crm_crm_area` VALUES (152201, 152200, '乌兰浩特市');
INSERT INTO `72crm_crm_area` VALUES (152222, 152200, '科尔沁右翼中旗');
INSERT INTO `72crm_crm_area` VALUES (152223, 152200, '扎赉特旗');
INSERT INTO `72crm_crm_area` VALUES (152221, 152200, '科尔沁右翼前旗');
INSERT INTO `72crm_crm_area` VALUES (130723, 130700, '康保县');
INSERT INTO `72crm_crm_area` VALUES (130722, 130700, '张北县');
INSERT INTO `72crm_crm_area` VALUES (130725, 130700, '尚义县');
INSERT INTO `72crm_crm_area` VALUES (130724, 130700, '沽源县');
INSERT INTO `72crm_crm_area` VALUES (130727, 130700, '阳原县');
INSERT INTO `72crm_crm_area` VALUES (130726, 130700, '蔚县');
INSERT INTO `72crm_crm_area` VALUES (130728, 130700, '怀安县');
INSERT INTO `72crm_crm_area` VALUES (130731, 130700, '涿鹿县');
INSERT INTO `72crm_crm_area` VALUES (130730, 130700, '怀来县');
INSERT INTO `72crm_crm_area` VALUES (130732, 130700, '赤城县');
INSERT INTO `72crm_crm_area` VALUES (130703, 130700, '桥西区');
INSERT INTO `72crm_crm_area` VALUES (130702, 130700, '桥东区');
INSERT INTO `72crm_crm_area` VALUES (130705, 130700, '宣化区');
INSERT INTO `72crm_crm_area` VALUES (130706, 130700, '下花园区');
INSERT INTO `72crm_crm_area` VALUES (130709, 130700, '崇礼区');
INSERT INTO `72crm_crm_area` VALUES (130708, 130700, '万全区');
INSERT INTO `72crm_crm_area` VALUES (711394, 711300, '竹东镇');
INSERT INTO `72crm_crm_area` VALUES (711395, 711300, '五峰乡');
INSERT INTO `72crm_crm_area` VALUES (711392, 711300, '芎林乡');
INSERT INTO `72crm_crm_area` VALUES (711393, 711300, '宝山乡');
INSERT INTO `72crm_crm_area` VALUES (711398, 711300, '北埔乡');
INSERT INTO `72crm_crm_area` VALUES (711399, 711300, '峨眉乡');
INSERT INTO `72crm_crm_area` VALUES (711396, 711300, '横山乡');
INSERT INTO `72crm_crm_area` VALUES (711397, 711300, '尖石乡');
INSERT INTO `72crm_crm_area` VALUES (711387, 711300, '竹北市');
INSERT INTO `72crm_crm_area` VALUES (711390, 711300, '新埔镇');
INSERT INTO `72crm_crm_area` VALUES (711391, 711300, '关西镇');
INSERT INTO `72crm_crm_area` VALUES (711388, 711300, '湖口乡');
INSERT INTO `72crm_crm_area` VALUES (711389, 711300, '新丰乡');
INSERT INTO `72crm_crm_area` VALUES (510626, 510600, '罗江县');
INSERT INTO `72crm_crm_area` VALUES (510623, 510600, '中江县');
INSERT INTO `72crm_crm_area` VALUES (510603, 510600, '旌阳区');
INSERT INTO `72crm_crm_area` VALUES (510683, 510600, '绵竹市');
INSERT INTO `72crm_crm_area` VALUES (510682, 510600, '什邡市');
INSERT INTO `72crm_crm_area` VALUES (510681, 510600, '广汉市');
INSERT INTO `72crm_crm_area` VALUES (420582, 420500, '当阳市');
INSERT INTO `72crm_crm_area` VALUES (420583, 420500, '枝江市');
INSERT INTO `72crm_crm_area` VALUES (420581, 420500, '宜都市');
INSERT INTO `72crm_crm_area` VALUES (420526, 420500, '兴山县');
INSERT INTO `72crm_crm_area` VALUES (420527, 420500, '秭归县');
INSERT INTO `72crm_crm_area` VALUES (420525, 420500, '远安县');
INSERT INTO `72crm_crm_area` VALUES (420502, 420500, '西陵区');
INSERT INTO `72crm_crm_area` VALUES (420503, 420500, '伍家岗区');
INSERT INTO `72crm_crm_area` VALUES (420528, 420500, '长阳土家族自治县');
INSERT INTO `72crm_crm_area` VALUES (420529, 420500, '五峰土家族自治县');
INSERT INTO `72crm_crm_area` VALUES (420506, 420500, '夷陵区');
INSERT INTO `72crm_crm_area` VALUES (420504, 420500, '点军区');
INSERT INTO `72crm_crm_area` VALUES (420505, 420500, '猇亭区');
INSERT INTO `72crm_crm_area` VALUES (442022, 442000, '五桂山街道');
INSERT INTO `72crm_crm_area` VALUES (442023, 442000, '西区街道');
INSERT INTO `72crm_crm_area` VALUES (442020, 442000, '石岐区街道');
INSERT INTO `72crm_crm_area` VALUES (442021, 442000, '坦洲镇');
INSERT INTO `72crm_crm_area` VALUES (442018, 442000, '沙溪镇');
INSERT INTO `72crm_crm_area` VALUES (442019, 442000, '神湾镇');
INSERT INTO `72crm_crm_area` VALUES (442016, 442000, '三角镇');
INSERT INTO `72crm_crm_area` VALUES (442017, 442000, '三乡镇');
INSERT INTO `72crm_crm_area` VALUES (442024, 442000, '小榄镇');
INSERT INTO `72crm_crm_area` VALUES (442006, 442000, '阜沙镇');
INSERT INTO `72crm_crm_area` VALUES (442007, 442000, '港口镇');
INSERT INTO `72crm_crm_area` VALUES (442004, 442000, '东区街道');
INSERT INTO `72crm_crm_area` VALUES (442005, 442000, '东升镇');
INSERT INTO `72crm_crm_area` VALUES (442002, 442000, '大涌镇');
INSERT INTO `72crm_crm_area` VALUES (442003, 442000, '东凤镇');
INSERT INTO `72crm_crm_area` VALUES (442001, 442000, '板芙镇');
INSERT INTO `72crm_crm_area` VALUES (442014, 442000, '南区街道');
INSERT INTO `72crm_crm_area` VALUES (442015, 442000, '南头镇');
INSERT INTO `72crm_crm_area` VALUES (442012, 442000, '民众镇');
INSERT INTO `72crm_crm_area` VALUES (442013, 442000, '南朗镇');
INSERT INTO `72crm_crm_area` VALUES (442010, 442000, '黄圃镇');
INSERT INTO `72crm_crm_area` VALUES (442011, 442000, '火炬开发区街道');
INSERT INTO `72crm_crm_area` VALUES (442008, 442000, '古镇镇');
INSERT INTO `72crm_crm_area` VALUES (442009, 442000, '横栏镇');
INSERT INTO `72crm_crm_area` VALUES (710346, 710300, '仁德区');
INSERT INTO `72crm_crm_area` VALUES (710347, 710300, '关庙区');
INSERT INTO `72crm_crm_area` VALUES (710344, 710300, '楠西区');
INSERT INTO `72crm_crm_area` VALUES (710345, 710300, '南化区');
INSERT INTO `72crm_crm_area` VALUES (710350, 710300, '麻豆区');
INSERT INTO `72crm_crm_area` VALUES (710351, 710300, '佳里区');
INSERT INTO `72crm_crm_area` VALUES (710348, 710300, '龙崎区');
INSERT INTO `72crm_crm_area` VALUES (710349, 710300, '官田区');
INSERT INTO `72crm_crm_area` VALUES (710339, 710300, '永康区');
INSERT INTO `72crm_crm_area` VALUES (710342, 710300, '左镇区');
INSERT INTO `72crm_crm_area` VALUES (710343, 710300, '玉井区');
INSERT INTO `72crm_crm_area` VALUES (710340, 710300, '归仁区');
INSERT INTO `72crm_crm_area` VALUES (710341, 710300, '新化区');
INSERT INTO `72crm_crm_area` VALUES (710362, 710300, '下营区');
INSERT INTO `72crm_crm_area` VALUES (710363, 710300, '柳营区');
INSERT INTO `72crm_crm_area` VALUES (710360, 710300, '东山区');
INSERT INTO `72crm_crm_area` VALUES (710361, 710300, '六甲区');
INSERT INTO `72crm_crm_area` VALUES (710302, 710300, '东区');
INSERT INTO `72crm_crm_area` VALUES (710366, 710300, '大内区');
INSERT INTO `72crm_crm_area` VALUES (710303, 710300, '南区');
INSERT INTO `72crm_crm_area` VALUES (710367, 710300, '山上区');
INSERT INTO `72crm_crm_area` VALUES (710364, 710300, '盐水区');
INSERT INTO `72crm_crm_area` VALUES (710301, 710300, '中西区');
INSERT INTO `72crm_crm_area` VALUES (710365, 710300, '善化区');
INSERT INTO `72crm_crm_area` VALUES (710354, 710300, '将军区');
INSERT INTO `72crm_crm_area` VALUES (710355, 710300, '学甲区');
INSERT INTO `72crm_crm_area` VALUES (710352, 710300, '西港区');
INSERT INTO `72crm_crm_area` VALUES (710353, 710300, '七股区');
INSERT INTO `72crm_crm_area` VALUES (710358, 710300, '后壁区');
INSERT INTO `72crm_crm_area` VALUES (710359, 710300, '白河区');
INSERT INTO `72crm_crm_area` VALUES (710356, 710300, '北门区');
INSERT INTO `72crm_crm_area` VALUES (710357, 710300, '新营区');
INSERT INTO `72crm_crm_area` VALUES (710306, 710300, '安南区');
INSERT INTO `72crm_crm_area` VALUES (710304, 710300, '北区');
INSERT INTO `72crm_crm_area` VALUES (710368, 710300, '新市区');
INSERT INTO `72crm_crm_area` VALUES (710305, 710300, '安平区');
INSERT INTO `72crm_crm_area` VALUES (710369, 710300, '安定区');
INSERT INTO `72crm_crm_area` VALUES (710399, 710300, '其它区');
INSERT INTO `72crm_crm_area` VALUES (621225, 621200, '西和县');
INSERT INTO `72crm_crm_area` VALUES (621224, 621200, '康县');
INSERT INTO `72crm_crm_area` VALUES (621227, 621200, '徽县');
INSERT INTO `72crm_crm_area` VALUES (621226, 621200, '礼县');
INSERT INTO `72crm_crm_area` VALUES (621228, 621200, '两当县');
INSERT INTO `72crm_crm_area` VALUES (621202, 621200, '武都区');
INSERT INTO `72crm_crm_area` VALUES (621221, 621200, '成县');
INSERT INTO `72crm_crm_area` VALUES (621223, 621200, '宕昌县');
INSERT INTO `72crm_crm_area` VALUES (621222, 621200, '文县');
INSERT INTO `72crm_crm_area` VALUES (450226, 450200, '三江侗族自治县');
INSERT INTO `72crm_crm_area` VALUES (450224, 450200, '融安县');
INSERT INTO `72crm_crm_area` VALUES (450225, 450200, '融水苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (450206, 450200, '柳江区');
INSERT INTO `72crm_crm_area` VALUES (450222, 450200, '柳城县');
INSERT INTO `72crm_crm_area` VALUES (450223, 450200, '鹿寨县');
INSERT INTO `72crm_crm_area` VALUES (450204, 450200, '柳南区');
INSERT INTO `72crm_crm_area` VALUES (450205, 450200, '柳北区');
INSERT INTO `72crm_crm_area` VALUES (450202, 450200, '城中区');
INSERT INTO `72crm_crm_area` VALUES (450203, 450200, '鱼峰区');
INSERT INTO `72crm_crm_area` VALUES (120000, 100000, '天津市');
INSERT INTO `72crm_crm_area` VALUES (320000, 100000, '江苏省');
INSERT INTO `72crm_crm_area` VALUES (360000, 100000, '江西省');
INSERT INTO `72crm_crm_area` VALUES (440000, 100000, '广东省');
INSERT INTO `72crm_crm_area` VALUES (520000, 100000, '贵州省');
INSERT INTO `72crm_crm_area` VALUES (640000, 100000, '宁夏回族自治区');
INSERT INTO `72crm_crm_area` VALUES (130000, 100000, '河北省');
INSERT INTO `72crm_crm_area` VALUES (210000, 100000, '辽宁省');
INSERT INTO `72crm_crm_area` VALUES (330000, 100000, '浙江省');
INSERT INTO `72crm_crm_area` VALUES (370000, 100000, '山东省');
INSERT INTO `72crm_crm_area` VALUES (410000, 100000, '河南省');
INSERT INTO `72crm_crm_area` VALUES (450000, 100000, '广西壮族自治区');
INSERT INTO `72crm_crm_area` VALUES (530000, 100000, '云南省');
INSERT INTO `72crm_crm_area` VALUES (610000, 100000, '陕西省');
INSERT INTO `72crm_crm_area` VALUES (650000, 100000, '新疆维吾尔自治区');
INSERT INTO `72crm_crm_area` VALUES (810000, 100000, '香港特别行政区');
INSERT INTO `72crm_crm_area` VALUES (140000, 100000, '山西省');
INSERT INTO `72crm_crm_area` VALUES (220000, 100000, '吉林省');
INSERT INTO `72crm_crm_area` VALUES (340000, 100000, '安徽省');
INSERT INTO `72crm_crm_area` VALUES (420000, 100000, '湖北省');
INSERT INTO `72crm_crm_area` VALUES (460000, 100000, '海南省');
INSERT INTO `72crm_crm_area` VALUES (500000, 100000, '重庆市');
INSERT INTO `72crm_crm_area` VALUES (540000, 100000, '西藏自治区');
INSERT INTO `72crm_crm_area` VALUES (620000, 100000, '甘肃省');
INSERT INTO `72crm_crm_area` VALUES (820000, 100000, '澳门特别行政区');
INSERT INTO `72crm_crm_area` VALUES (110000, 100000, '北京市');
INSERT INTO `72crm_crm_area` VALUES (150000, 100000, '内蒙古自治区');
INSERT INTO `72crm_crm_area` VALUES (230000, 100000, '黑龙江省');
INSERT INTO `72crm_crm_area` VALUES (310000, 100000, '上海市');
INSERT INTO `72crm_crm_area` VALUES (350000, 100000, '福建省');
INSERT INTO `72crm_crm_area` VALUES (430000, 100000, '湖南省');
INSERT INTO `72crm_crm_area` VALUES (510000, 100000, '四川省');
INSERT INTO `72crm_crm_area` VALUES (630000, 100000, '青海省');
INSERT INTO `72crm_crm_area` VALUES (710000, 100000, '台湾省');
INSERT INTO `72crm_crm_area` VALUES (360103, 360100, '西湖区');
INSERT INTO `72crm_crm_area` VALUES (360102, 360100, '东湖区');
INSERT INTO `72crm_crm_area` VALUES (360112, 360100, '新建区');
INSERT INTO `72crm_crm_area` VALUES (360124, 360100, '进贤县');
INSERT INTO `72crm_crm_area` VALUES (360111, 360100, '青山湖区');
INSERT INTO `72crm_crm_area` VALUES (360105, 360100, '湾里区');
INSERT INTO `72crm_crm_area` VALUES (360121, 360100, '南昌县');
INSERT INTO `72crm_crm_area` VALUES (360104, 360100, '青云谱区');
INSERT INTO `72crm_crm_area` VALUES (360123, 360100, '安义县');
INSERT INTO `72crm_crm_area` VALUES (620201, 620200, '市辖区');
INSERT INTO `72crm_crm_area` VALUES (620291, 620200, '长城区');
INSERT INTO `72crm_crm_area` VALUES (620290, 620200, '雄关区');
INSERT INTO `72crm_crm_area` VALUES (620293, 620200, '新城镇');
INSERT INTO `72crm_crm_area` VALUES (620292, 620200, '镜铁区');
INSERT INTO `72crm_crm_area` VALUES (620295, 620200, '文殊镇');
INSERT INTO `72crm_crm_area` VALUES (620294, 620200, '峪泉镇');
INSERT INTO `72crm_crm_area` VALUES (411302, 411300, '宛城区');
INSERT INTO `72crm_crm_area` VALUES (411303, 411300, '卧龙区');
INSERT INTO `72crm_crm_area` VALUES (411330, 411300, '桐柏县');
INSERT INTO `72crm_crm_area` VALUES (411328, 411300, '唐河县');
INSERT INTO `72crm_crm_area` VALUES (411329, 411300, '新野县');
INSERT INTO `72crm_crm_area` VALUES (411381, 411300, '邓州市');
INSERT INTO `72crm_crm_area` VALUES (411326, 411300, '淅川县');
INSERT INTO `72crm_crm_area` VALUES (411327, 411300, '社旗县');
INSERT INTO `72crm_crm_area` VALUES (411324, 411300, '镇平县');
INSERT INTO `72crm_crm_area` VALUES (411325, 411300, '内乡县');
INSERT INTO `72crm_crm_area` VALUES (411322, 411300, '方城县');
INSERT INTO `72crm_crm_area` VALUES (411323, 411300, '西峡县');
INSERT INTO `72crm_crm_area` VALUES (411321, 411300, '南召县');
INSERT INTO `72crm_crm_area` VALUES (330421, 330400, '嘉善县');
INSERT INTO `72crm_crm_area` VALUES (330481, 330400, '海宁市');
INSERT INTO `72crm_crm_area` VALUES (330483, 330400, '桐乡市');
INSERT INTO `72crm_crm_area` VALUES (330402, 330400, '南湖区');
INSERT INTO `72crm_crm_area` VALUES (330482, 330400, '平湖市');
INSERT INTO `72crm_crm_area` VALUES (330424, 330400, '海盐县');
INSERT INTO `72crm_crm_area` VALUES (330411, 330400, '秀洲区');
INSERT INTO `72crm_crm_area` VALUES (445122, 445100, '饶平县');
INSERT INTO `72crm_crm_area` VALUES (445102, 445100, '湘桥区');
INSERT INTO `72crm_crm_area` VALUES (445103, 445100, '潮安区');
INSERT INTO `72crm_crm_area` VALUES (210624, 210600, '宽甸满族自治县');
INSERT INTO `72crm_crm_area` VALUES (210603, 210600, '振兴区');
INSERT INTO `72crm_crm_area` VALUES (210602, 210600, '元宝区');
INSERT INTO `72crm_crm_area` VALUES (210682, 210600, '凤城市');
INSERT INTO `72crm_crm_area` VALUES (210681, 210600, '东港市');
INSERT INTO `72crm_crm_area` VALUES (210604, 210600, '振安区');
INSERT INTO `72crm_crm_area` VALUES (350981, 350900, '福安市');
INSERT INTO `72crm_crm_area` VALUES (350902, 350900, '蕉城区');
INSERT INTO `72crm_crm_area` VALUES (350982, 350900, '福鼎市');
INSERT INTO `72crm_crm_area` VALUES (350925, 350900, '周宁县');
INSERT INTO `72crm_crm_area` VALUES (350924, 350900, '寿宁县');
INSERT INTO `72crm_crm_area` VALUES (350926, 350900, '柘荣县');
INSERT INTO `72crm_crm_area` VALUES (350921, 350900, '霞浦县');
INSERT INTO `72crm_crm_area` VALUES (350923, 350900, '屏南县');
INSERT INTO `72crm_crm_area` VALUES (350922, 350900, '古田县');
INSERT INTO `72crm_crm_area` VALUES (611002, 611000, '商州区');
INSERT INTO `72crm_crm_area` VALUES (611021, 611000, '洛南县');
INSERT INTO `72crm_crm_area` VALUES (611023, 611000, '商南县');
INSERT INTO `72crm_crm_area` VALUES (611022, 611000, '丹凤县');
INSERT INTO `72crm_crm_area` VALUES (611025, 611000, '镇安县');
INSERT INTO `72crm_crm_area` VALUES (611024, 611000, '山阳县');
INSERT INTO `72crm_crm_area` VALUES (611026, 611000, '柞水县');
INSERT INTO `72crm_crm_area` VALUES (321204, 321200, '姜堰区');
INSERT INTO `72crm_crm_area` VALUES (321281, 321200, '兴化市');
INSERT INTO `72crm_crm_area` VALUES (321202, 321200, '海陵区');
INSERT INTO `72crm_crm_area` VALUES (321282, 321200, '靖江市');
INSERT INTO `72crm_crm_area` VALUES (321203, 321200, '高港区');
INSERT INTO `72crm_crm_area` VALUES (321283, 321200, '泰兴市');
INSERT INTO `72crm_crm_area` VALUES (654025, 654000, '新源县');
INSERT INTO `72crm_crm_area` VALUES (654024, 654000, '巩留县');
INSERT INTO `72crm_crm_area` VALUES (654027, 654000, '特克斯县');
INSERT INTO `72crm_crm_area` VALUES (654026, 654000, '昭苏县');
INSERT INTO `72crm_crm_area` VALUES (654028, 654000, '尼勒克县');
INSERT INTO `72crm_crm_area` VALUES (654003, 654000, '奎屯市');
INSERT INTO `72crm_crm_area` VALUES (654002, 654000, '伊宁市');
INSERT INTO `72crm_crm_area` VALUES (654021, 654000, '伊宁县');
INSERT INTO `72crm_crm_area` VALUES (654004, 654000, '霍尔果斯市');
INSERT INTO `72crm_crm_area` VALUES (654023, 654000, '霍城县');
INSERT INTO `72crm_crm_area` VALUES (654022, 654000, '察布查尔锡伯自治县');
INSERT INTO `72crm_crm_area` VALUES (150202, 150200, '东河区');
INSERT INTO `72crm_crm_area` VALUES (150203, 150200, '昆都仑区');
INSERT INTO `72crm_crm_area` VALUES (150206, 150200, '白云鄂博矿区');
INSERT INTO `72crm_crm_area` VALUES (150222, 150200, '固阳县');
INSERT INTO `72crm_crm_area` VALUES (150207, 150200, '九原区');
INSERT INTO `72crm_crm_area` VALUES (150223, 150200, '达尔罕茂明安联合旗');
INSERT INTO `72crm_crm_area` VALUES (150204, 150200, '青山区');
INSERT INTO `72crm_crm_area` VALUES (150205, 150200, '石拐区');
INSERT INTO `72crm_crm_area` VALUES (150221, 150200, '土默特右旗');
INSERT INTO `72crm_crm_area` VALUES (410311, 410300, '洛龙区');
INSERT INTO `72crm_crm_area` VALUES (410306, 410300, '吉利区');
INSERT INTO `72crm_crm_area` VALUES (410304, 410300, '瀍河回族区');
INSERT INTO `72crm_crm_area` VALUES (410305, 410300, '涧西区');
INSERT INTO `72crm_crm_area` VALUES (410381, 410300, '偃师市');
INSERT INTO `72crm_crm_area` VALUES (410326, 410300, '汝阳县');
INSERT INTO `72crm_crm_area` VALUES (410327, 410300, '宜阳县');
INSERT INTO `72crm_crm_area` VALUES (410324, 410300, '栾川县');
INSERT INTO `72crm_crm_area` VALUES (410325, 410300, '嵩县');
INSERT INTO `72crm_crm_area` VALUES (410322, 410300, '孟津县');
INSERT INTO `72crm_crm_area` VALUES (410323, 410300, '新安县');
INSERT INTO `72crm_crm_area` VALUES (410302, 410300, '老城区');
INSERT INTO `72crm_crm_area` VALUES (410303, 410300, '西工区');
INSERT INTO `72crm_crm_area` VALUES (410328, 410300, '洛宁县');
INSERT INTO `72crm_crm_area` VALUES (410329, 410300, '伊川县');
INSERT INTO `72crm_crm_area` VALUES (530124, 530100, '富民县');
INSERT INTO `72crm_crm_area` VALUES (530125, 530100, '宜良县');
INSERT INTO `72crm_crm_area` VALUES (530126, 530100, '石林彝族自治县');
INSERT INTO `72crm_crm_area` VALUES (530127, 530100, '嵩明县');
INSERT INTO `72crm_crm_area` VALUES (530112, 530100, '西山区');
INSERT INTO `72crm_crm_area` VALUES (530113, 530100, '东川区');
INSERT INTO `72crm_crm_area` VALUES (530114, 530100, '呈贡区');
INSERT INTO `72crm_crm_area` VALUES (530115, 530100, '晋宁区');
INSERT INTO `72crm_crm_area` VALUES (530181, 530100, '安宁市');
INSERT INTO `72crm_crm_area` VALUES (530111, 530100, '官渡区');
INSERT INTO `72crm_crm_area` VALUES (530128, 530100, '禄劝彝族苗族自治县');
INSERT INTO `72crm_crm_area` VALUES (530129, 530100, '寻甸回族彝族自治县');
INSERT INTO `72crm_crm_area` VALUES (530102, 530100, '五华区');
INSERT INTO `72crm_crm_area` VALUES (530103, 530100, '盘龙区');
INSERT INTO `72crm_crm_area` VALUES (632521, 632500, '共和县');
INSERT INTO `72crm_crm_area` VALUES (632523, 632500, '贵德县');
INSERT INTO `72crm_crm_area` VALUES (632522, 632500, '同德县');
INSERT INTO `72crm_crm_area` VALUES (632525, 632500, '贵南县');
INSERT INTO `72crm_crm_area` VALUES (632524, 632500, '兴海县');
INSERT INTO `72crm_crm_area` VALUES (231123, 231100, '逊克县');
INSERT INTO `72crm_crm_area` VALUES (231121, 231100, '嫩江县');
INSERT INTO `72crm_crm_area` VALUES (231124, 231100, '孙吴县');
INSERT INTO `72crm_crm_area` VALUES (231102, 231100, '爱辉区');
INSERT INTO `72crm_crm_area` VALUES (231182, 231100, '五大连池市');
INSERT INTO `72crm_crm_area` VALUES (231181, 231100, '北安市');
INSERT INTO `72crm_crm_area` VALUES (341702, 341700, '贵池区');
INSERT INTO `72crm_crm_area` VALUES (341721, 341700, '东至县');
INSERT INTO `72crm_crm_area` VALUES (341723, 341700, '青阳县');
INSERT INTO `72crm_crm_area` VALUES (341722, 341700, '石台县');
INSERT INTO `72crm_crm_area` VALUES (653001, 653000, '阿图什市');
INSERT INTO `72crm_crm_area` VALUES (653023, 653000, '阿合奇县');
INSERT INTO `72crm_crm_area` VALUES (653022, 653000, '阿克陶县');
INSERT INTO `72crm_crm_area` VALUES (653024, 653000, '乌恰县');
INSERT INTO `72crm_crm_area` VALUES (440100, 440000, '广州市');
INSERT INTO `72crm_crm_area` VALUES (440900, 440000, '茂名市');
INSERT INTO `72crm_crm_area` VALUES (441700, 440000, '阳江市');
INSERT INTO `72crm_crm_area` VALUES (440800, 440000, '湛江市');
INSERT INTO `72crm_crm_area` VALUES (441600, 440000, '河源市');
INSERT INTO `72crm_crm_area` VALUES (440300, 440000, '深圳市');
INSERT INTO `72crm_crm_area` VALUES (441900, 440000, '东莞市');
INSERT INTO `72crm_crm_area` VALUES (445100, 440000, '潮州市');
INSERT INTO `72crm_crm_area` VALUES (440200, 440000, '韶关市');
INSERT INTO `72crm_crm_area` VALUES (441800, 440000, '清远市');
INSERT INTO `72crm_crm_area` VALUES (440500, 440000, '汕头市');
INSERT INTO `72crm_crm_area` VALUES (441300, 440000, '惠州市');
INSERT INTO `72crm_crm_area` VALUES (445300, 440000, '云浮市');
INSERT INTO `72crm_crm_area` VALUES (440400, 440000, '珠海市');
INSERT INTO `72crm_crm_area` VALUES (441200, 440000, '肇庆市');
INSERT INTO `72crm_crm_area` VALUES (442000, 440000, '中山市');
INSERT INTO `72crm_crm_area` VALUES (445200, 440000, '揭阳市');
INSERT INTO `72crm_crm_area` VALUES (440700, 440000, '江门市');
INSERT INTO `72crm_crm_area` VALUES (441500, 440000, '汕尾市');
INSERT INTO `72crm_crm_area` VALUES (440600, 440000, '佛山市');
INSERT INTO `72crm_crm_area` VALUES (441400, 440000, '梅州市');
INSERT INTO `72crm_crm_area` VALUES (141026, 141000, '安泽县');
INSERT INTO `72crm_crm_area` VALUES (141027, 141000, '浮山县');
INSERT INTO `72crm_crm_area` VALUES (141024, 141000, '洪洞县');
INSERT INTO `72crm_crm_area` VALUES (141025, 141000, '古县');
INSERT INTO `72crm_crm_area` VALUES (141030, 141000, '大宁县');
INSERT INTO `72crm_crm_area` VALUES (141031, 141000, '隰县');
INSERT INTO `72crm_crm_area` VALUES (141028, 141000, '吉县');
INSERT INTO `72crm_crm_area` VALUES (141029, 141000, '乡宁县');
INSERT INTO `72crm_crm_area` VALUES (141002, 141000, '尧都区');
INSERT INTO `72crm_crm_area` VALUES (141034, 141000, '汾西县');
INSERT INTO `72crm_crm_area` VALUES (141032, 141000, '永和县');
INSERT INTO `72crm_crm_area` VALUES (141033, 141000, '蒲县');
INSERT INTO `72crm_crm_area` VALUES (141082, 141000, '霍州市');
INSERT INTO `72crm_crm_area` VALUES (141081, 141000, '侯马市');
INSERT INTO `72crm_crm_area` VALUES (141022, 141000, '翼城县');
INSERT INTO `72crm_crm_area` VALUES (141023, 141000, '襄汾县');
INSERT INTO `72crm_crm_area` VALUES (141021, 141000, '曲沃县');
INSERT INTO `72crm_crm_area` VALUES (320213, 320200, '梁溪区');
INSERT INTO `72crm_crm_area` VALUES (320214, 320200, '新吴区');
INSERT INTO `72crm_crm_area` VALUES (320211, 320200, '滨湖区');
INSERT INTO `72crm_crm_area` VALUES (320205, 320200, '锡山区');
INSERT INTO `72crm_crm_area` VALUES (320206, 320200, '惠山区');
INSERT INTO `72crm_crm_area` VALUES (320281, 320200, '江阴市');
INSERT INTO `72crm_crm_area` VALUES (320282, 320200, '宜兴市');
INSERT INTO `72crm_crm_area` VALUES (371428, 371400, '武城县');
INSERT INTO `72crm_crm_area` VALUES (371425, 371400, '齐河县');
INSERT INTO `72crm_crm_area` VALUES (371424, 371400, '临邑县');
INSERT INTO `72crm_crm_area` VALUES (371427, 371400, '夏津县');
INSERT INTO `72crm_crm_area` VALUES (371426, 371400, '平原县');
INSERT INTO `72crm_crm_area` VALUES (371423, 371400, '庆云县');
INSERT INTO `72crm_crm_area` VALUES (371422, 371400, '宁津县');
INSERT INTO `72crm_crm_area` VALUES (371481, 371400, '乐陵市');
INSERT INTO `72crm_crm_area` VALUES (371403, 371400, '陵城区');
INSERT INTO `72crm_crm_area` VALUES (371402, 371400, '德城区');
INSERT INTO `72crm_crm_area` VALUES (371482, 371400, '禹城市');
INSERT INTO `72crm_crm_area` VALUES (511703, 511700, '达川区');
INSERT INTO `72crm_crm_area` VALUES (511702, 511700, '通川区');
INSERT INTO `72crm_crm_area` VALUES (511781, 511700, '万源市');
INSERT INTO `72crm_crm_area` VALUES (511725, 511700, '渠县');
INSERT INTO `72crm_crm_area` VALUES (511724, 511700, '大竹县');
INSERT INTO `72crm_crm_area` VALUES (511723, 511700, '开江县');
INSERT INTO `72crm_crm_area` VALUES (511722, 511700, '宣汉县');
INSERT INTO `72crm_crm_area` VALUES (430822, 430800, '桑植县');
INSERT INTO `72crm_crm_area` VALUES (430821, 430800, '慈利县');
INSERT INTO `72crm_crm_area` VALUES (430802, 430800, '永定区');
INSERT INTO `72crm_crm_area` VALUES (430811, 430800, '武陵源区');
INSERT INTO `72crm_crm_area` VALUES (230113, 230100, '双城区');
INSERT INTO `72crm_crm_area` VALUES (230112, 230100, '阿城区');
INSERT INTO `72crm_crm_area` VALUES (230183, 230100, '尚志市');
INSERT INTO `72crm_crm_area` VALUES (230123, 230100, '依兰县');
INSERT INTO `72crm_crm_area` VALUES (230184, 230100, '五常市');
INSERT INTO `72crm_crm_area` VALUES (230127, 230100, '木兰县');
INSERT INTO `72crm_crm_area` VALUES (230126, 230100, '巴彦县');
INSERT INTO `72crm_crm_area` VALUES (230125, 230100, '宾县');
INSERT INTO `72crm_crm_area` VALUES (230124, 230100, '方正县');
INSERT INTO `72crm_crm_area` VALUES (230129, 230100, '延寿县');
INSERT INTO `72crm_crm_area` VALUES (230128, 230100, '通河县');
INSERT INTO `72crm_crm_area` VALUES (230103, 230100, '南岗区');
INSERT INTO `72crm_crm_area` VALUES (230102, 230100, '道里区');
INSERT INTO `72crm_crm_area` VALUES (230104, 230100, '道外区');
INSERT INTO `72crm_crm_area` VALUES (230111, 230100, '呼兰区');
INSERT INTO `72crm_crm_area` VALUES (230110, 230100, '香坊区');
INSERT INTO `72crm_crm_area` VALUES (230109, 230100, '松北区');
INSERT INTO `72crm_crm_area` VALUES (230108, 230100, '平房区');
INSERT INTO `72crm_crm_area` VALUES (340711, 340700, '郊区');
INSERT INTO `72crm_crm_area` VALUES (340705, 340700, '铜官区');
INSERT INTO `72crm_crm_area` VALUES (340706, 340700, '义安区');
INSERT INTO `72crm_crm_area` VALUES (340722, 340700, '枞阳县');
INSERT INTO `72crm_crm_area` VALUES (610200, 610000, '铜川市');
INSERT INTO `72crm_crm_area` VALUES (610600, 610000, '延安市');
INSERT INTO `72crm_crm_area` VALUES (611000, 610000, '商洛市');
INSERT INTO `72crm_crm_area` VALUES (610300, 610000, '宝鸡市');
INSERT INTO `72crm_crm_area` VALUES (610700, 610000, '汉中市');
INSERT INTO `72crm_crm_area` VALUES (610400, 610000, '咸阳市');
INSERT INTO `72crm_crm_area` VALUES (610800, 610000, '榆林市');
INSERT INTO `72crm_crm_area` VALUES (610100, 610000, '西安市');
INSERT INTO `72crm_crm_area` VALUES (610500, 610000, '渭南市');
INSERT INTO `72crm_crm_area` VALUES (610900, 610000, '安康市');
INSERT INTO `72crm_crm_area` VALUES (712474, 712400, '盐埔乡');
INSERT INTO `72crm_crm_area` VALUES (712475, 712400, '长治乡');
INSERT INTO `72crm_crm_area` VALUES (712472, 712400, '里港乡');
INSERT INTO `72crm_crm_area` VALUES (712473, 712400, '高树乡');
INSERT INTO `72crm_crm_area` VALUES (712478, 712400, '内埔乡');
INSERT INTO `72crm_crm_area` VALUES (712479, 712400, '万丹乡');
INSERT INTO `72crm_crm_area` VALUES (712476, 712400, '麟洛乡');
INSERT INTO `72crm_crm_area` VALUES (712477, 712400, '竹田乡');
INSERT INTO `72crm_crm_area` VALUES (712467, 712400, '屏东市');
INSERT INTO `72crm_crm_area` VALUES (712470, 712400, '玛家乡');
INSERT INTO `72crm_crm_area` VALUES (712471, 712400, '九如乡');
INSERT INTO `72crm_crm_area` VALUES (712468, 712400, '三地门乡');
INSERT INTO `72crm_crm_area` VALUES (712469, 712400, '雾台乡');
INSERT INTO `72crm_crm_area` VALUES (712490, 712400, '佳冬乡');
INSERT INTO `72crm_crm_area` VALUES (712491, 712400, '新园乡');
INSERT INTO `72crm_crm_area` VALUES (712488, 712400, '东港镇');
INSERT INTO `72crm_crm_area` VALUES (712489, 712400, '琉球乡');
INSERT INTO `72crm_crm_area` VALUES (712494, 712400, '春日乡');
INSERT INTO `72crm_crm_area` VALUES (712495, 712400, '狮子乡');
INSERT INTO `72crm_crm_area` VALUES (712492, 712400, '枋寮乡');
INSERT INTO `72crm_crm_area` VALUES (712493, 712400, '枋山乡');
INSERT INTO `72crm_crm_area` VALUES (712482, 712400, '来义乡');
INSERT INTO `72crm_crm_area` VALUES (712483, 712400, '万峦乡');
INSERT INTO `72crm_crm_area` VALUES (712480, 712400, '潮州镇');
INSERT INTO `72crm_crm_area` VALUES (712481, 712400, '泰武乡');
INSERT INTO `72crm_crm_area` VALUES (712486, 712400, '南州乡');
INSERT INTO `72crm_crm_area` VALUES (712487, 712400, '林边乡');
INSERT INTO `72crm_crm_area` VALUES (712484, 712400, '莰顶乡');
INSERT INTO `72crm_crm_area` VALUES (712485, 712400, '新埤乡');
INSERT INTO `72crm_crm_area` VALUES (712498, 712400, '恒春镇');
INSERT INTO `72crm_crm_area` VALUES (712499, 712400, '满州乡');
INSERT INTO `72crm_crm_area` VALUES (712496, 712400, '车城乡');
INSERT INTO `72crm_crm_area` VALUES (712497, 712400, '牡丹乡');
INSERT INTO `72crm_crm_area` VALUES (140400, 140000, '长治市');
INSERT INTO `72crm_crm_area` VALUES (140800, 140000, '运城市');
INSERT INTO `72crm_crm_area` VALUES (140100, 140000, '太原市');
INSERT INTO `72crm_crm_area` VALUES (140500, 140000, '晋城市');
INSERT INTO `72crm_crm_area` VALUES (140900, 140000, '忻州市');
INSERT INTO `72crm_crm_area` VALUES (140200, 140000, '大同市');
INSERT INTO `72crm_crm_area` VALUES (140600, 140000, '朔州市');
INSERT INTO `72crm_crm_area` VALUES (141000, 140000, '临汾市');
INSERT INTO `72crm_crm_area` VALUES (140300, 140000, '阳泉市');
INSERT INTO `72crm_crm_area` VALUES (140700, 140000, '晋中市');
INSERT INTO `72crm_crm_area` VALUES (141100, 140000, '吕梁市');
INSERT INTO `72crm_crm_area` VALUES (451302, 451300, '兴宾区');
INSERT INTO `72crm_crm_area` VALUES (451381, 451300, '合山市');
INSERT INTO `72crm_crm_area` VALUES (451324, 451300, '金秀瑶族自治县');
INSERT INTO `72crm_crm_area` VALUES (451322, 451300, '象州县');
INSERT INTO `72crm_crm_area` VALUES (451323, 451300, '武宣县');
INSERT INTO `72crm_crm_area` VALUES (451321, 451300, '忻城县');
INSERT INTO `72crm_crm_area` VALUES (711498, 711400, '大园乡');
INSERT INTO `72crm_crm_area` VALUES (711499, 711400, '芦竹乡');
INSERT INTO `72crm_crm_area` VALUES (711496, 711400, '大溪镇');
INSERT INTO `72crm_crm_area` VALUES (711497, 711400, '复兴乡');
INSERT INTO `72crm_crm_area` VALUES (711490, 711400, '杨梅市');
INSERT INTO `72crm_crm_area` VALUES (711491, 711400, '新屋乡');
INSERT INTO `72crm_crm_area` VALUES (711488, 711400, '平镇市');
INSERT INTO `72crm_crm_area` VALUES (711489, 711400, '龙潭乡');
INSERT INTO `72crm_crm_area` VALUES (711494, 711400, '龟山乡');
INSERT INTO `72crm_crm_area` VALUES (711495, 711400, '八德市');
INSERT INTO `72crm_crm_area` VALUES (711492, 711400, '观音乡');
INSERT INTO `72crm_crm_area` VALUES (711493, 711400, '桃园市');
INSERT INTO `72crm_crm_area` VALUES (711487, 711400, '中坜市');
INSERT INTO `72crm_crm_area` VALUES (370405, 370400, '台儿庄区');
INSERT INTO `72crm_crm_area` VALUES (370404, 370400, '峄城区');
INSERT INTO `72crm_crm_area` VALUES (370406, 370400, '山亭区');
INSERT INTO `72crm_crm_area` VALUES (370481, 370400, '滕州市');
INSERT INTO `72crm_crm_area` VALUES (370403, 370400, '薛城区');
INSERT INTO `72crm_crm_area` VALUES (370402, 370400, '市中区');
INSERT INTO `72crm_crm_area` VALUES (510727, 510700, '平武县');
INSERT INTO `72crm_crm_area` VALUES (510726, 510700, '北川羌族自治县');
INSERT INTO `72crm_crm_area` VALUES (510725, 510700, '梓潼县');
INSERT INTO `72crm_crm_area` VALUES (510723, 510700, '盐亭县');
INSERT INTO `72crm_crm_area` VALUES (510722, 510700, '三台县');
INSERT INTO `72crm_crm_area` VALUES (510705, 510700, '安州区');
INSERT INTO `72crm_crm_area` VALUES (510704, 510700, '游仙区');
INSERT INTO `72crm_crm_area` VALUES (510703, 510700, '涪城区');
INSERT INTO `72crm_crm_area` VALUES (510781, 510700, '江油市');
INSERT INTO `72crm_crm_area` VALUES (130881, 130800, '平泉市');
INSERT INTO `72crm_crm_area` VALUES (130803, 130800, '双滦区');
INSERT INTO `72crm_crm_area` VALUES (130802, 130800, '双桥区');
INSERT INTO `72crm_crm_area` VALUES (130821, 130800, '承德县');
INSERT INTO `72crm_crm_area` VALUES (130804, 130800, '鹰手营子矿区');
INSERT INTO `72crm_crm_area` VALUES (130822, 130800, '兴隆县');
INSERT INTO `72crm_crm_area` VALUES (130825, 130800, '隆化县');
INSERT INTO `72crm_crm_area` VALUES (130824, 130800, '滦平县');
INSERT INTO `72crm_crm_area` VALUES (130827, 130800, '宽城满族自治县');
INSERT INTO `72crm_crm_area` VALUES (130826, 130800, '丰宁满族自治县');
INSERT INTO `72crm_crm_area` VALUES (130828, 130800, '围场满族蒙古族自治县');
INSERT INTO `72crm_crm_area` VALUES (310100, 310000, '上海城区');
INSERT INTO `72crm_crm_area` VALUES (540424, 540400, '波密县');
INSERT INTO `72crm_crm_area` VALUES (540425, 540400, '察隅县');
INSERT INTO `72crm_crm_area` VALUES (540426, 540400, '朗县');
INSERT INTO `72crm_crm_area` VALUES (540402, 540400, '巴宜区');
INSERT INTO `72crm_crm_area` VALUES (540421, 540400, '工布江达县');
INSERT INTO `72crm_crm_area` VALUES (540422, 540400, '米林县');
INSERT INTO `72crm_crm_area` VALUES (540423, 540400, '墨脱县');
INSERT INTO `72crm_crm_area` VALUES (450311, 450300, '雁山区');
INSERT INTO `72crm_crm_area` VALUES (450304, 450300, '象山区');
INSERT INTO `72crm_crm_area` VALUES (450305, 450300, '七星区');
INSERT INTO `72crm_crm_area` VALUES (450312, 450300, '临桂区');
INSERT INTO `72crm_crm_area` VALUES (450326, 450300, '永福县');
INSERT INTO `72crm_crm_area` VALUES (450327, 450300, '灌阳县');
INSERT INTO `72crm_crm_area` VALUES (450324, 450300, '全州县');
INSERT INTO `72crm_crm_area` VALUES (450325, 450300, '兴安县');
INSERT INTO `72crm_crm_area` VALUES (450323, 450300, '灵川县');
INSERT INTO `72crm_crm_area` VALUES (450321, 450300, '阳朔县');
INSERT INTO `72crm_crm_area` VALUES (450302, 450300, '秀峰区');
INSERT INTO `72crm_crm_area` VALUES (450303, 450300, '叠彩区');
INSERT INTO `72crm_crm_area` VALUES (450332, 450300, '恭城瑶族自治县');
INSERT INTO `72crm_crm_area` VALUES (450330, 450300, '平乐县');
INSERT INTO `72crm_crm_area` VALUES (450331, 450300, '荔浦县');
INSERT INTO `72crm_crm_area` VALUES (450328, 450300, '龙胜各族自治县');
INSERT INTO `72crm_crm_area` VALUES (450329, 450300, '资源县');
INSERT INTO `72crm_crm_area` VALUES (420626, 420600, '保康县');
INSERT INTO `72crm_crm_area` VALUES (420624, 420600, '南漳县');
INSERT INTO `72crm_crm_area` VALUES (420625, 420600, '谷城县');
INSERT INTO `72crm_crm_area` VALUES (420606, 420600, '樊城区');
INSERT INTO `72crm_crm_area` VALUES (420607, 420600, '襄州区');
INSERT INTO `72crm_crm_area` VALUES (420684, 420600, '宜城市');
INSERT INTO `72crm_crm_area` VALUES (420602, 420600, '襄城区');
INSERT INTO `72crm_crm_area` VALUES (420682, 420600, '老河口市');
INSERT INTO `72crm_crm_area` VALUES (420683, 420600, '枣阳市');
INSERT INTO `72crm_crm_area` VALUES (330503, 330500, '南浔区');
INSERT INTO `72crm_crm_area` VALUES (330502, 330500, '吴兴区');
INSERT INTO `72crm_crm_area` VALUES (330521, 330500, '德清县');
INSERT INTO `72crm_crm_area` VALUES (330523, 330500, '安吉县');
INSERT INTO `72crm_crm_area` VALUES (330522, 330500, '长兴县');
INSERT INTO `72crm_crm_area` VALUES (620302, 620300, '金川区');
INSERT INTO `72crm_crm_area` VALUES (620321, 620300, '永昌县');
INSERT INTO `72crm_crm_area` VALUES (710408, 710400, '南屯区');
INSERT INTO `72crm_crm_area` VALUES (710402, 710400, '东区');
INSERT INTO `72crm_crm_area` VALUES (710403, 710400, '南区');
INSERT INTO `72crm_crm_area` VALUES (710401, 710400, '中区');
INSERT INTO `72crm_crm_area` VALUES (710406, 710400, '北屯区');
INSERT INTO `72crm_crm_area` VALUES (710407, 710400, '西屯区');
INSERT INTO `72crm_crm_area` VALUES (710404, 710400, '西区');
INSERT INTO `72crm_crm_area` VALUES (710405, 710400, '北区');
INSERT INTO `72crm_crm_area` VALUES (710431, 710400, '太平区');
INSERT INTO `72crm_crm_area` VALUES (710442, 710400, '大雅区');
INSERT INTO `72crm_crm_area` VALUES (710443, 710400, '神冈区');
INSERT INTO `72crm_crm_area` VALUES (710440, 710400, '新社区');
INSERT INTO `72crm_crm_area` VALUES (710441, 710400, '潭子区');
INSERT INTO `72crm_crm_area` VALUES (710446, 710400, '龙井区');
INSERT INTO `72crm_crm_area` VALUES (710447, 710400, '梧栖区');
INSERT INTO `72crm_crm_area` VALUES (710444, 710400, '大肚区');
INSERT INTO `72crm_crm_area` VALUES (710445, 710400, '沙鹿区');
INSERT INTO `72crm_crm_area` VALUES (710434, 710400, '乌日区');
INSERT INTO `72crm_crm_area` VALUES (710435, 710400, '丰原区');
INSERT INTO `72crm_crm_area` VALUES (710499, 710400, '其它区');
INSERT INTO `72crm_crm_area` VALUES (710432, 710400, '大里区');
INSERT INTO `72crm_crm_area` VALUES (710433, 710400, '雾峰区');
INSERT INTO `72crm_crm_area` VALUES (710438, 710400, '东势区');
INSERT INTO `72crm_crm_area` VALUES (710439, 710400, '和平区');
INSERT INTO `72crm_crm_area` VALUES (710436, 710400, '后里区');
INSERT INTO `72crm_crm_area` VALUES (710437, 710400, '石冈区');
INSERT INTO `72crm_crm_area` VALUES (710450, 710400, '外埔区');
INSERT INTO `72crm_crm_area` VALUES (710451, 710400, '大安区');
INSERT INTO `72crm_crm_area` VALUES (710448, 710400, '清水区');
INSERT INTO `72crm_crm_area` VALUES (710449, 710400, '大甲区');
INSERT INTO `72crm_crm_area` VALUES (360222, 360200, '浮梁县');
INSERT INTO `72crm_crm_area` VALUES (360281, 360200, '乐平市');
INSERT INTO `72crm_crm_area` VALUES (360203, 360200, '珠山区');
INSERT INTO `72crm_crm_area` VALUES (360202, 360200, '昌江区');
INSERT INTO `72crm_crm_area` VALUES (411426, 411400, '夏邑县');
INSERT INTO `72crm_crm_area` VALUES (411424, 411400, '柘城县');
INSERT INTO `72crm_crm_area` VALUES (411425, 411400, '虞城县');
INSERT INTO `72crm_crm_area` VALUES (411422, 411400, '睢县');
INSERT INTO `72crm_crm_area` VALUES (411423, 411400, '宁陵县');
INSERT INTO `72crm_crm_area` VALUES (411421, 411400, '民权县');
INSERT INTO `72crm_crm_area` VALUES (411402, 411400, '梁园区');
INSERT INTO `72crm_crm_area` VALUES (411403, 411400, '睢阳区');
INSERT INTO `72crm_crm_area` VALUES (411481, 411400, '永城市');
INSERT INTO `72crm_crm_area` VALUES (210711, 210700, '太和区');
INSERT INTO `72crm_crm_area` VALUES (210727, 210700, '义县');
INSERT INTO `72crm_crm_area` VALUES (210726, 210700, '黑山县');
INSERT INTO `72crm_crm_area` VALUES (210703, 210700, '凌河区');
INSERT INTO `72crm_crm_area` VALUES (210702, 210700, '古塔区');
INSERT INTO `72crm_crm_area` VALUES (210782, 210700, '北镇市');
INSERT INTO `72crm_crm_area` VALUES (210781, 210700, '凌海市');
INSERT INTO `72crm_crm_area` VALUES (321302, 321300, '宿城区');
INSERT INTO `72crm_crm_area` VALUES (321324, 321300, '泗洪县');
INSERT INTO `72crm_crm_area` VALUES (321311, 321300, '宿豫区');
INSERT INTO `72crm_crm_area` VALUES (321322, 321300, '沭阳县');
INSERT INTO `72crm_crm_area` VALUES (321323, 321300, '泗阳县');
INSERT INTO `72crm_crm_area` VALUES (632621, 632600, '玛沁县');
INSERT INTO `72crm_crm_area` VALUES (632623, 632600, '甘德县');
INSERT INTO `72crm_crm_area` VALUES (632622, 632600, '班玛县');
INSERT INTO `72crm_crm_area` VALUES (632625, 632600, '久治县');
INSERT INTO `72crm_crm_area` VALUES (632624, 632600, '达日县');
INSERT INTO `72crm_crm_area` VALUES (632626, 632600, '玛多县');
INSERT INTO `72crm_crm_area` VALUES (445222, 445200, '揭西县');
INSERT INTO `72crm_crm_area` VALUES (445202, 445200, '榕城区');
INSERT INTO `72crm_crm_area` VALUES (445203, 445200, '揭东区');
INSERT INTO `72crm_crm_area` VALUES (445281, 445200, '普宁市');
INSERT INTO `72crm_crm_area` VALUES (445224, 445200, '惠来县');
INSERT INTO `72crm_crm_area` VALUES (653000, 650000, '克孜勒苏柯尔克孜自治州');
INSERT INTO `72crm_crm_area` VALUES (652300, 650000, '昌吉回族自治州');
INSERT INTO `72crm_crm_area` VALUES (653100, 650000, '喀什地区');
INSERT INTO `72crm_crm_area` VALUES (650400, 650000, '吐鲁番市');
INSERT INTO `72crm_crm_area` VALUES (652800, 650000, '巴音郭楞蒙古自治州');
INSERT INTO `72crm_crm_area` VALUES (650500, 650000, '哈密市');
INSERT INTO `72crm_crm_area` VALUES (652900, 650000, '阿克苏地区');
INSERT INTO `72crm_crm_area` VALUES (650200, 650000, '克拉玛依市');
INSERT INTO `72crm_crm_area` VALUES (654200, 650000, '塔城地区');
INSERT INTO `72crm_crm_area` VALUES (659000, 650000, '自治区直辖县级行政单位');
INSERT INTO `72crm_crm_area` VALUES (652700, 650000, '博尔塔拉蒙古自治州');
INSERT INTO `72crm_crm_area` VALUES (654300, 650000, '阿勒泰地区');
INSERT INTO `72crm_crm_area` VALUES (653200, 650000, '和田地区');
INSERT INTO `72crm_crm_area` VALUES (654000, 650000, '伊犁哈萨克自治州');
INSERT INTO `72crm_crm_area` VALUES (650100, 650000, '乌鲁木齐市');
INSERT INTO `72crm_crm_area` VALUES (150304, 150300, '乌达区');
INSERT INTO `72crm_crm_area` VALUES (150302, 150300, '海勃湾区');
INSERT INTO `72crm_crm_area` VALUES (150303, 150300, '海南区');
INSERT INTO `72crm_crm_area` VALUES (440118, 440100, '增城区');
INSERT INTO `72crm_crm_area` VALUES (440103, 440100, '荔湾区');
INSERT INTO `72crm_crm_area` VALUES (440117, 440100, '从化区');
INSERT INTO `72crm_crm_area` VALUES (440114, 440100, '花都区');
INSERT INTO `72crm_crm_area` VALUES (440115, 440100, '南沙区');
INSERT INTO `72crm_crm_area` VALUES (440112, 440100, '黄埔区');
INSERT INTO `72crm_crm_area` VALUES (440113, 440100, '番禺区');
INSERT INTO `72crm_crm_area` VALUES (440111, 440100, '白云区');
INSERT INTO `72crm_crm_area` VALUES (440106, 440100, '天河区');
INSERT INTO `72crm_crm_area` VALUES (440104, 440100, '越秀区');
INSERT INTO `72crm_crm_area` VALUES (440105, 440100, '海珠区');
INSERT INTO `72crm_crm_area` VALUES (231283, 231200, '海伦市');
INSERT INTO `72crm_crm_area` VALUES (231202, 231200, '北林区');
INSERT INTO `72crm_crm_area` VALUES (231282, 231200, '肇东市');
INSERT INTO `72crm_crm_area` VALUES (231281, 231200, '安达市');
INSERT INTO `72crm_crm_area` VALUES (231223, 231200, '青冈县');
INSERT INTO `72crm_crm_area` VALUES (231222, 231200, '兰西县');
INSERT INTO `72crm_crm_area` VALUES (231221, 231200, '望奎县');
INSERT INTO `72crm_crm_area` VALUES (231226, 231200, '绥棱县');
INSERT INTO `72crm_crm_area` VALUES (231225, 231200, '明水县');
INSERT INTO `72crm_crm_area` VALUES (231224, 231200, '庆安县');
INSERT INTO `72crm_crm_area` VALUES (653129, 653100, '伽师县');
INSERT INTO `72crm_crm_area` VALUES (653128, 653100, '岳普湖县');
INSERT INTO `72crm_crm_area` VALUES (653131, 653100, '塔什库尔干塔吉克自治县');
INSERT INTO `72crm_crm_area` VALUES (653130, 653100, '巴楚县');
INSERT INTO `72crm_crm_area` VALUES (653101, 653100, '喀什市');
INSERT INTO `72crm_crm_area` VALUES (653121, 653100, '疏附县');
INSERT INTO `72crm_crm_area` VALUES (653123, 653100, '英吉沙县');
INSERT INTO `72crm_crm_area` VALUES (653122, 653100, '疏勒县');
INSERT INTO `72crm_crm_area` VALUES (653125, 653100, '莎车县');
INSERT INTO `72crm_crm_area` VALUES (653124, 653100, '泽普县');
INSERT INTO `72crm_crm_area` VALUES (653127, 653100, '麦盖提县');
INSERT INTO `72crm_crm_area` VALUES (653126, 653100, '叶城县');
INSERT INTO `72crm_crm_area` VALUES (410422, 410400, '叶县');
INSERT INTO `72crm_crm_area` VALUES (410423, 410400, '鲁山县');
INSERT INTO `72crm_crm_area` VALUES (410404, 410400, '石龙区');
INSERT INTO `72crm_crm_area` VALUES (410421, 410400, '宝丰县');
INSERT INTO `72crm_crm_area` VALUES (410402, 410400, '新华区');
INSERT INTO `72crm_crm_area` VALUES (410482, 410400, '汝州市');
INSERT INTO `72crm_crm_area` VALUES (410403, 410400, '卫东区');
INSERT INTO `72crm_crm_area` VALUES (410481, 410400, '舞钢市');
INSERT INTO `72crm_crm_area` VALUES (410411, 410400, '湛河区');
INSERT INTO `72crm_crm_area` VALUES (410425, 410400, '郏县');
INSERT INTO `72crm_crm_area` VALUES (320324, 320300, '睢宁县');
INSERT INTO `72crm_crm_area` VALUES (320311, 320300, '泉山区');
INSERT INTO `72crm_crm_area` VALUES (320305, 320300, '贾汪区');
INSERT INTO `72crm_crm_area` VALUES (320321, 320300, '丰县');
INSERT INTO `72crm_crm_area` VALUES (320322, 320300, '沛县');
INSERT INTO `72crm_crm_area` VALUES (320381, 320300, '新沂市');
INSERT INTO `72crm_crm_area` VALUES (320302, 320300, '鼓楼区');
INSERT INTO `72crm_crm_area` VALUES (320382, 320300, '邳州市');
INSERT INTO `72crm_crm_area` VALUES (320303, 320300, '云龙区');
INSERT INTO `72crm_crm_area` VALUES (320312, 320300, '铜山区');
INSERT INTO `72crm_crm_area` VALUES (371525, 371500, '冠县');
INSERT INTO `72crm_crm_area` VALUES (371524, 371500, '东阿县');
INSERT INTO `72crm_crm_area` VALUES (371526, 371500, '高唐县');
INSERT INTO `72crm_crm_area` VALUES (371521, 371500, '阳谷县');
INSERT INTO `72crm_crm_area` VALUES (371523, 371500, '茌平县');
INSERT INTO `72crm_crm_area` VALUES (371522, 371500, '莘县');
INSERT INTO `72crm_crm_area` VALUES (371581, 371500, '临清市');
INSERT INTO `72crm_crm_area` VALUES (371502, 371500, '东昌府区');
INSERT INTO `72crm_crm_area` VALUES (542521, 542500, '普兰县');
INSERT INTO `72crm_crm_area` VALUES (542522, 542500, '札达县');
INSERT INTO `72crm_crm_area` VALUES (542523, 542500, '噶尔县');
INSERT INTO `72crm_crm_area` VALUES (542524, 542500, '日土县');
INSERT INTO `72crm_crm_area` VALUES (542525, 542500, '革吉县');
INSERT INTO `72crm_crm_area` VALUES (542526, 542500, '改则县');
INSERT INTO `72crm_crm_area` VALUES (542527, 542500, '措勤县');
INSERT INTO `72crm_crm_area` VALUES (820100, 820000, '澳门城区');
INSERT INTO `72crm_crm_area` VALUES (341825, 341800, '旌德县');
INSERT INTO `72crm_crm_area` VALUES (341824, 341800, '绩溪县');
INSERT INTO `72crm_crm_area` VALUES (341821, 341800, '郎溪县');
INSERT INTO `72crm_crm_area` VALUES (341823, 341800, '泾县');
INSERT INTO `72crm_crm_area` VALUES (341822, 341800, '广德县');
INSERT INTO `72crm_crm_area` VALUES (341881, 341800, '宁国市');
INSERT INTO `72crm_crm_area` VALUES (341802, 341800, '宣州区');
INSERT INTO `72crm_crm_area` VALUES (141122, 141100, '交城县');
INSERT INTO `72crm_crm_area` VALUES (141123, 141100, '兴县');
INSERT INTO `72crm_crm_area` VALUES (141121, 141100, '文水县');
INSERT INTO `72crm_crm_area` VALUES (141126, 141100, '石楼县');
INSERT INTO `72crm_crm_area` VALUES (141127, 141100, '岚县');
INSERT INTO `72crm_crm_area` VALUES (141124, 141100, '临县');
INSERT INTO `72crm_crm_area` VALUES (141125, 141100, '柳林县');
INSERT INTO `72crm_crm_area` VALUES (141130, 141100, '交口县');
INSERT INTO `72crm_crm_area` VALUES (141128, 141100, '方山县');
INSERT INTO `72crm_crm_area` VALUES (141129, 141100, '中阳县');
INSERT INTO `72crm_crm_area` VALUES (141102, 141100, '离石区');
INSERT INTO `72crm_crm_area` VALUES (141182, 141100, '汾阳市');
INSERT INTO `72crm_crm_area` VALUES (141181, 141100, '孝义市');
INSERT INTO `72crm_crm_area` VALUES (430902, 430900, '资阳区');
INSERT INTO `72crm_crm_area` VALUES (430903, 430900, '赫山区');
INSERT INTO `72crm_crm_area` VALUES (430981, 430900, '沅江市');
INSERT INTO `72crm_crm_area` VALUES (430922, 430900, '桃江县');
INSERT INTO `72crm_crm_area` VALUES (430923, 430900, '安化县');
INSERT INTO `72crm_crm_area` VALUES (430921, 430900, '南县');
INSERT INTO `72crm_crm_area` VALUES (350100, 350000, '福州市');
INSERT INTO `72crm_crm_area` VALUES (350500, 350000, '泉州市');
INSERT INTO `72crm_crm_area` VALUES (350900, 350000, '宁德市');
INSERT INTO `72crm_crm_area` VALUES (350400, 350000, '三明市');
INSERT INTO `72crm_crm_area` VALUES (350800, 350000, '龙岩市');
INSERT INTO `72crm_crm_area` VALUES (350300, 350000, '莆田市');
INSERT INTO `72crm_crm_area` VALUES (350700, 350000, '南平市');
INSERT INTO `72crm_crm_area` VALUES (350200, 350000, '厦门市');
INSERT INTO `72crm_crm_area` VALUES (350600, 350000, '漳州市');
INSERT INTO `72crm_crm_area` VALUES (230208, 230200, '梅里斯达斡尔族区');
INSERT INTO `72crm_crm_area` VALUES (230281, 230200, '讷河市');
INSERT INTO `72crm_crm_area` VALUES (230223, 230200, '依安县');
INSERT INTO `72crm_crm_area` VALUES (230221, 230200, '龙江县');
INSERT INTO `72crm_crm_area` VALUES (230227, 230200, '富裕县');
INSERT INTO `72crm_crm_area` VALUES (230225, 230200, '甘南县');
INSERT INTO `72crm_crm_area` VALUES (230224, 230200, '泰来县');
INSERT INTO `72crm_crm_area` VALUES (230231, 230200, '拜泉县');
INSERT INTO `72crm_crm_area` VALUES (230230, 230200, '克东县');
INSERT INTO `72crm_crm_area` VALUES (230229, 230200, '克山县');
INSERT INTO `72crm_crm_area` VALUES (230203, 230200, '建华区');
INSERT INTO `72crm_crm_area` VALUES (230202, 230200, '龙沙区');
INSERT INTO `72crm_crm_area` VALUES (230207, 230200, '碾子山区');
INSERT INTO `72crm_crm_area` VALUES (230206, 230200, '富拉尔基区');
INSERT INTO `72crm_crm_area` VALUES (230205, 230200, '昂昂溪区');
INSERT INTO `72crm_crm_area` VALUES (230204, 230200, '铁锋区');
INSERT INTO `72crm_crm_area` VALUES (533323, 533300, '福贡县');
INSERT INTO `72crm_crm_area` VALUES (533324, 533300, '贡山独龙族怒族自治县');
INSERT INTO `72crm_crm_area` VALUES (533325, 533300, '兰坪白族普米族自治县');
INSERT INTO `72crm_crm_area` VALUES (533301, 533300, '泸水市');
INSERT INTO `72crm_crm_area` VALUES (610122, 610100, '蓝田县');
INSERT INTO `72crm_crm_area` VALUES (610124, 610100, '周至县');
INSERT INTO `72crm_crm_area` VALUES (610113, 610100, '雁塔区');
INSERT INTO `72crm_crm_area` VALUES (610112, 610100, '未央区');
INSERT INTO `72crm_crm_area` VALUES (610115, 610100, '临潼区');
INSERT INTO `72crm_crm_area` VALUES (610114, 610100, '阎良区');
INSERT INTO `72crm_crm_area` VALUES (610117, 610100, '高陵区');
INSERT INTO `72crm_crm_area` VALUES (610116, 610100, '长安区');
INSERT INTO `72crm_crm_area` VALUES (610118, 610100, '鄠邑区');
INSERT INTO `72crm_crm_area` VALUES (610104, 610100, '莲湖区');
INSERT INTO `72crm_crm_area` VALUES (610111, 610100, '灞桥区');
INSERT INTO `72crm_crm_area` VALUES (610103, 610100, '碑林区');
INSERT INTO `72crm_crm_area` VALUES (610102, 610100, '新城区');
INSERT INTO `72crm_crm_area` VALUES (712586, 712500, '兰屿乡');
INSERT INTO `72crm_crm_area` VALUES (712587, 712500, '延平乡');
INSERT INTO `72crm_crm_area` VALUES (712584, 712500, '台东市');
INSERT INTO `72crm_crm_area` VALUES (712585, 712500, '绿岛乡');
INSERT INTO `72crm_crm_area` VALUES (712590, 712500, '关山镇');
INSERT INTO `72crm_crm_area` VALUES (712591, 712500, '海端乡');
INSERT INTO `72crm_crm_area` VALUES (712588, 712500, '卑南乡');
INSERT INTO `72crm_crm_area` VALUES (712589, 712500, '鹿野乡');
INSERT INTO `72crm_crm_area` VALUES (712594, 712500, '成功镇');
INSERT INTO `72crm_crm_area` VALUES (712595, 712500, '长滨乡');
INSERT INTO `72crm_crm_area` VALUES (712592, 712500, '池上乡');
INSERT INTO `72crm_crm_area` VALUES (712593, 712500, '东河乡');
INSERT INTO `72crm_crm_area` VALUES (712598, 712500, '达仁乡');
INSERT INTO `72crm_crm_area` VALUES (712599, 712500, '太麻里乡');
INSERT INTO `72crm_crm_area` VALUES (712596, 712500, '金峰乡');
INSERT INTO `72crm_crm_area` VALUES (712597, 712500, '大武乡');
INSERT INTO `72crm_crm_area` VALUES (511827, 511800, '宝兴县');
INSERT INTO `72crm_crm_area` VALUES (511826, 511800, '芦山县');
INSERT INTO `72crm_crm_area` VALUES (511825, 511800, '天全县');
INSERT INTO `72crm_crm_area` VALUES (511824, 511800, '石棉县');
INSERT INTO `72crm_crm_area` VALUES (511823, 511800, '汉源县');
INSERT INTO `72crm_crm_area` VALUES (511822, 511800, '荥经县');
INSERT INTO `72crm_crm_area` VALUES (511803, 511800, '名山区');
INSERT INTO `72crm_crm_area` VALUES (511802, 511800, '雨城区');
INSERT INTO `72crm_crm_area` VALUES (370503, 370500, '河口区');
INSERT INTO `72crm_crm_area` VALUES (370502, 370500, '东营区');
INSERT INTO `72crm_crm_area` VALUES (370505, 370500, '垦利区');
INSERT INTO `72crm_crm_area` VALUES (370523, 370500, '广饶县');
INSERT INTO `72crm_crm_area` VALUES (370522, 370500, '利津县');
INSERT INTO `72crm_crm_area` VALUES (532328, 532300, '元谋县');
INSERT INTO `72crm_crm_area` VALUES (532329, 532300, '武定县');
INSERT INTO `72crm_crm_area` VALUES (532331, 532300, '禄丰县');
INSERT INTO `72crm_crm_area` VALUES (532301, 532300, '楚雄市');
INSERT INTO `72crm_crm_area` VALUES (532322, 532300, '双柏县');
INSERT INTO `72crm_crm_area` VALUES (532323, 532300, '牟定县');
INSERT INTO `72crm_crm_area` VALUES (532324, 532300, '南华县');
INSERT INTO `72crm_crm_area` VALUES (532325, 532300, '姚安县');
INSERT INTO `72crm_crm_area` VALUES (532326, 532300, '大姚县');
INSERT INTO `72crm_crm_area` VALUES (532327, 532300, '永仁县');
INSERT INTO `72crm_crm_area` VALUES (340822, 340800, '怀宁县');
INSERT INTO `72crm_crm_area` VALUES (340881, 340800, '桐城市');
INSERT INTO `72crm_crm_area` VALUES (340803, 340800, '大观区');
INSERT INTO `72crm_crm_area` VALUES (340802, 340800, '迎江区');
INSERT INTO `72crm_crm_area` VALUES (340828, 340800, '岳西县');
INSERT INTO `72crm_crm_area` VALUES (340825, 340800, '太湖县');
INSERT INTO `72crm_crm_area` VALUES (340824, 340800, '潜山县');
INSERT INTO `72crm_crm_area` VALUES (340811, 340800, '宜秀区');
INSERT INTO `72crm_crm_area` VALUES (340827, 340800, '望江县');
INSERT INTO `72crm_crm_area` VALUES (340826, 340800, '宿松县');
INSERT INTO `72crm_crm_area` VALUES (140181, 140100, '古交市');
INSERT INTO `72crm_crm_area` VALUES (140106, 140100, '迎泽区');
INSERT INTO `72crm_crm_area` VALUES (140122, 140100, '阳曲县');
INSERT INTO `72crm_crm_area` VALUES (140107, 140100, '杏花岭区');
INSERT INTO `72crm_crm_area` VALUES (140123, 140100, '娄烦县');
INSERT INTO `72crm_crm_area` VALUES (140105, 140100, '小店区');
INSERT INTO `72crm_crm_area` VALUES (140121, 140100, '清徐县');
INSERT INTO `72crm_crm_area` VALUES (140110, 140100, '晋源区');
INSERT INTO `72crm_crm_area` VALUES (140108, 140100, '尖草坪区');
INSERT INTO `72crm_crm_area` VALUES (140109, 140100, '万柏林区');
INSERT INTO `72crm_crm_area` VALUES (711594, 711500, '大湖乡');
INSERT INTO `72crm_crm_area` VALUES (711595, 711500, '泰安乡');
INSERT INTO `72crm_crm_area` VALUES (711592, 711500, '头屋乡');
INSERT INTO `72crm_crm_area` VALUES (711593, 711500, '公馆乡');
INSERT INTO `72crm_crm_area` VALUES (711598, 711500, '西湖乡');
INSERT INTO `72crm_crm_area` VALUES (711599, 711500, '卓兰镇');
INSERT INTO `72crm_crm_area` VALUES (711596, 711500, '铜锣乡');
INSERT INTO `72crm_crm_area` VALUES (711597, 711500, '三义乡');
INSERT INTO `72crm_crm_area` VALUES (711586, 711500, '狮潭乡');
INSERT INTO `72crm_crm_area` VALUES (711587, 711500, '后龙镇');
INSERT INTO `72crm_crm_area` VALUES (711584, 711500, '三湾乡');
INSERT INTO `72crm_crm_area` VALUES (711585, 711500, '南庄乡');
INSERT INTO `72crm_crm_area` VALUES (711590, 711500, '苗栗市');
INSERT INTO `72crm_crm_area` VALUES (711591, 711500, '造桥乡');
INSERT INTO `72crm_crm_area` VALUES (711588, 711500, '通霄镇');
INSERT INTO `72crm_crm_area` VALUES (711589, 711500, '苑里镇');
INSERT INTO `72crm_crm_area` VALUES (711582, 711500, '竹南镇');
INSERT INTO `72crm_crm_area` VALUES (711583, 711500, '头份镇');
INSERT INTO `72crm_crm_area` VALUES (520100, 520000, '贵阳市');
INSERT INTO `72crm_crm_area` VALUES (520500, 520000, '毕节市');
INSERT INTO `72crm_crm_area` VALUES (520400, 520000, '安顺市');
INSERT INTO `72crm_crm_area` VALUES (520300, 520000, '遵义市');
INSERT INTO `72crm_crm_area` VALUES (522300, 520000, '黔西南布依族苗族自治州');
INSERT INTO `72crm_crm_area` VALUES (522700, 520000, '黔南布依族苗族自治州');
INSERT INTO `72crm_crm_area` VALUES (520200, 520000, '六盘水市');
INSERT INTO `72crm_crm_area` VALUES (520600, 520000, '铜仁市');
INSERT INTO `72crm_crm_area` VALUES (522600, 520000, '黔东南苗族侗族自治州');
INSERT INTO `72crm_crm_area` VALUES (451424, 451400, '大新县');
INSERT INTO `72crm_crm_area` VALUES (451425, 451400, '天等县');
INSERT INTO `72crm_crm_area` VALUES (451422, 451400, '宁明县');
INSERT INTO `72crm_crm_area` VALUES (451423, 451400, '龙州县');
INSERT INTO `72crm_crm_area` VALUES (451421, 451400, '扶绥县');
INSERT INTO `72crm_crm_area` VALUES (451402, 451400, '江州区');
INSERT INTO `72crm_crm_area` VALUES (451481, 451400, '凭祥市');
INSERT INTO `72crm_crm_area` VALUES (310116, 310100, '金山区');
INSERT INTO `72crm_crm_area` VALUES (310117, 310100, '松江区');
INSERT INTO `72crm_crm_area` VALUES (310118, 310100, '青浦区');
INSERT INTO `72crm_crm_area` VALUES (310151, 310100, '崇明区');
INSERT INTO `72crm_crm_area` VALUES (310112, 310100, '闵行区');
INSERT INTO `72crm_crm_area` VALUES (310113, 310100, '宝山区');
INSERT INTO `72crm_crm_area` VALUES (310114, 310100, '嘉定区');
INSERT INTO `72crm_crm_area` VALUES (310115, 310100, '浦东新区');
INSERT INTO `72crm_crm_area` VALUES (310120, 310100, '奉贤区');
INSERT INTO `72crm_crm_area` VALUES (310101, 310100, '黄浦区');
INSERT INTO `72crm_crm_area` VALUES (310109, 310100, '虹口区');
INSERT INTO `72crm_crm_area` VALUES (310110, 310100, '杨浦区');
INSERT INTO `72crm_crm_area` VALUES (310104, 310100, '徐汇区');
INSERT INTO `72crm_crm_area` VALUES (310105, 310100, '长宁区');
INSERT INTO `72crm_crm_area` VALUES (310106, 310100, '静安区');
INSERT INTO `72crm_crm_area` VALUES (310107, 310100, '普陀区');
INSERT INTO `72crm_crm_area` VALUES (130981, 130900, '泊头市');
INSERT INTO `72crm_crm_area` VALUES (130983, 130900, '黄骅市');
INSERT INTO `72crm_crm_area` VALUES (130982, 130900, '任丘市');
INSERT INTO `72crm_crm_area` VALUES (130921, 130900, '沧县');
INSERT INTO `72crm_crm_area` VALUES (130984, 130900, '河间市');
INSERT INTO `72crm_crm_area` VALUES (130923, 130900, '东光县');
INSERT INTO `72crm_crm_area` VALUES (130922, 130900, '青县');
INSERT INTO `72crm_crm_area` VALUES (130925, 130900, '盐山县');
INSERT INTO `72crm_crm_area` VALUES (130924, 130900, '海兴县');
INSERT INTO `72crm_crm_area` VALUES (130927, 130900, '南皮县');
INSERT INTO `72crm_crm_area` VALUES (130926, 130900, '肃宁县');
INSERT INTO `72crm_crm_area` VALUES (130929, 130900, '献县');
INSERT INTO `72crm_crm_area` VALUES (130928, 130900, '吴桥县');
INSERT INTO `72crm_crm_area` VALUES (130930, 130900, '孟村回族自治县');
INSERT INTO `72crm_crm_area` VALUES (130903, 130900, '运河区');
INSERT INTO `72crm_crm_area` VALUES (130902, 130900, '新华区');
INSERT INTO `72crm_crm_area` VALUES (510823, 510800, '剑阁县');
INSERT INTO `72crm_crm_area` VALUES (510822, 510800, '青川县');
INSERT INTO `72crm_crm_area` VALUES (510821, 510800, '旺苍县');
INSERT INTO `72crm_crm_area` VALUES (510802, 510800, '利州区');
INSERT INTO `72crm_crm_area` VALUES (510812, 510800, '朝天区');
INSERT INTO `72crm_crm_area` VALUES (510811, 510800, '昭化区');
INSERT INTO `72crm_crm_area` VALUES (510824, 510800, '苍溪县');
INSERT INTO `72crm_crm_area` VALUES (420704, 420700, '鄂城区');
INSERT INTO `72crm_crm_area` VALUES (420702, 420700, '梁子湖区');
INSERT INTO `72crm_crm_area` VALUES (420703, 420700, '华容区');
INSERT INTO `72crm_crm_area` VALUES (540521, 540500, '扎囊县');
INSERT INTO `72crm_crm_area` VALUES (540522, 540500, '贡嘎县');
INSERT INTO `72crm_crm_area` VALUES (540523, 540500, '桑日县');
INSERT INTO `72crm_crm_area` VALUES (540524, 540500, '琼结县');
INSERT INTO `72crm_crm_area` VALUES (540525, 540500, '曲松县');
INSERT INTO `72crm_crm_area` VALUES (540526, 540500, '措美县');
INSERT INTO `72crm_crm_area` VALUES (540527, 540500, '洛扎县');
INSERT INTO `72crm_crm_area` VALUES (540528, 540500, '加查县');
INSERT INTO `72crm_crm_area` VALUES (540529, 540500, '隆子县');
INSERT INTO `72crm_crm_area` VALUES (540530, 540500, '错那县');
INSERT INTO `72crm_crm_area` VALUES (540531, 540500, '浪卡子县');
INSERT INTO `72crm_crm_area` VALUES (540502, 540500, '乃东区');
INSERT INTO `72crm_crm_area` VALUES (220400, 220000, '辽源市');
INSERT INTO `72crm_crm_area` VALUES (220800, 220000, '白城市');
INSERT INTO `72crm_crm_area` VALUES (222400, 220000, '延边朝鲜族自治州');
INSERT INTO `72crm_crm_area` VALUES (220100, 220000, '长春市');
INSERT INTO `72crm_crm_area` VALUES (220500, 220000, '通化市');
INSERT INTO `72crm_crm_area` VALUES (220200, 220000, '吉林市');
INSERT INTO `72crm_crm_area` VALUES (220600, 220000, '白山市');
INSERT INTO `72crm_crm_area` VALUES (220300, 220000, '四平市');
INSERT INTO `72crm_crm_area` VALUES (220700, 220000, '松原市');
INSERT INTO `72crm_crm_area` VALUES (450406, 450400, '龙圩区');
INSERT INTO `72crm_crm_area` VALUES (450422, 450400, '藤县');
INSERT INTO `72crm_crm_area` VALUES (450423, 450400, '蒙山县');
INSERT INTO `72crm_crm_area` VALUES (450405, 450400, '长洲区');
INSERT INTO `72crm_crm_area` VALUES (450421, 450400, '苍梧县');
INSERT INTO `72crm_crm_area` VALUES (450403, 450400, '万秀区');
INSERT INTO `72crm_crm_area` VALUES (450481, 450400, '岑溪市');
INSERT INTO `72crm_crm_area` VALUES (360321, 360300, '莲花县');
INSERT INTO `72crm_crm_area` VALUES (360323, 360300, '芦溪县');
INSERT INTO `72crm_crm_area` VALUES (360322, 360300, '上栗县');
INSERT INTO `72crm_crm_area` VALUES (360302, 360300, '安源区');
INSERT INTO `72crm_crm_area` VALUES (360313, 360300, '湘东区');
INSERT INTO `72crm_crm_area` VALUES (411526, 411500, '潢川县');
INSERT INTO `72crm_crm_area` VALUES (411527, 411500, '淮滨县');
INSERT INTO `72crm_crm_area` VALUES (411524, 411500, '商城县');
INSERT INTO `72crm_crm_area` VALUES (411525, 411500, '固始县');
INSERT INTO `72crm_crm_area` VALUES (411522, 411500, '光山县');
INSERT INTO `72crm_crm_area` VALUES (411523, 411500, '新县');
INSERT INTO `72crm_crm_area` VALUES (411521, 411500, '罗山县');
INSERT INTO `72crm_crm_area` VALUES (411502, 411500, '浉河区');
INSERT INTO `72crm_crm_area` VALUES (411503, 411500, '平桥区');
INSERT INTO `72crm_crm_area` VALUES (411528, 411500, '息县');
INSERT INTO `72crm_crm_area` VALUES (330624, 330600, '新昌县');
INSERT INTO `72crm_crm_area` VALUES (330604, 330600, '上虞区');
INSERT INTO `72crm_crm_area` VALUES (330681, 330600, '诸暨市');
INSERT INTO `72crm_crm_area` VALUES (330603, 330600, '柯桥区');
INSERT INTO `72crm_crm_area` VALUES (330683, 330600, '嵊州市');
INSERT INTO `72crm_crm_area` VALUES (330602, 330600, '越城区');
INSERT INTO `72crm_crm_area` VALUES (710507, 710500, '金沙镇');
INSERT INTO `72crm_crm_area` VALUES (710510, 710500, '金城镇');
INSERT INTO `72crm_crm_area` VALUES (710511, 710500, '烈屿乡');
INSERT INTO `72crm_crm_area` VALUES (710508, 710500, '金湖镇');
INSERT INTO `72crm_crm_area` VALUES (710509, 710500, '金宁乡');
INSERT INTO `72crm_crm_area` VALUES (710512, 710500, '乌坵乡');
INSERT INTO `72crm_crm_area` VALUES (654201, 654200, '塔城市');
INSERT INTO `72crm_crm_area` VALUES (654202, 654200, '乌苏市');
INSERT INTO `72crm_crm_area` VALUES (654221, 654200, '额敏县');
INSERT INTO `72crm_crm_area` VALUES (654223, 654200, '沙湾县');
INSERT INTO `72crm_crm_area` VALUES (654225, 654200, '裕民县');
INSERT INTO `72crm_crm_area` VALUES (654224, 654200, '托里县');
INSERT INTO `72crm_crm_area` VALUES (654226, 654200, '和布克赛尔蒙古自治县');
INSERT INTO `72crm_crm_area` VALUES (445302, 445300, '云城区');
INSERT INTO `72crm_crm_area` VALUES (445303, 445300, '云安区');
INSERT INTO `72crm_crm_area` VALUES (445381, 445300, '罗定市');
INSERT INTO `72crm_crm_area` VALUES (445322, 445300, '郁南县');
INSERT INTO `72crm_crm_area` VALUES (445321, 445300, '新兴县');
INSERT INTO `72crm_crm_area` VALUES (210803, 210800, '西市区');
INSERT INTO `72crm_crm_area` VALUES (210802, 210800, '站前区');
INSERT INTO `72crm_crm_area` VALUES (210882, 210800, '大石桥市');
INSERT INTO `72crm_crm_area` VALUES (210881, 210800, '盖州市');
INSERT INTO `72crm_crm_area` VALUES (210804, 210800, '鲅鱼圈区');
INSERT INTO `72crm_crm_area` VALUES (210811, 210800, '老边区');
INSERT INTO `72crm_crm_area` VALUES (530381, 530300, '宣威市');
INSERT INTO `72crm_crm_area` VALUES (530302, 530300, '麒麟区');
INSERT INTO `72crm_crm_area` VALUES (530303, 530300, '沾益区');
INSERT INTO `72crm_crm_area` VALUES (530321, 530300, '马龙县');
INSERT INTO `72crm_crm_area` VALUES (530322, 530300, '陆良县');
INSERT INTO `72crm_crm_area` VALUES (530323, 530300, '师宗县');
INSERT INTO `72crm_crm_area` VALUES (530324, 530300, '罗平县');
INSERT INTO `72crm_crm_area` VALUES (530325, 530300, '富源县');
INSERT INTO `72crm_crm_area` VALUES (530326, 530300, '会泽县');
INSERT INTO `72crm_crm_area` VALUES (632701, 632700, '玉树市');
INSERT INTO `72crm_crm_area` VALUES (632723, 632700, '称多县');
INSERT INTO `72crm_crm_area` VALUES (632722, 632700, '杂多县');
INSERT INTO `72crm_crm_area` VALUES (632725, 632700, '囊谦县');
INSERT INTO `72crm_crm_area` VALUES (632724, 632700, '治多县');
INSERT INTO `72crm_crm_area` VALUES (632726, 632700, '曲麻莱县');
INSERT INTO `72crm_crm_area` VALUES (441223, 441200, '广宁县');
INSERT INTO `72crm_crm_area` VALUES (441204, 441200, '高要区');
INSERT INTO `72crm_crm_area` VALUES (441284, 441200, '四会市');
INSERT INTO `72crm_crm_area` VALUES (441202, 441200, '端州区');
INSERT INTO `72crm_crm_area` VALUES (441203, 441200, '鼎湖区');
INSERT INTO `72crm_crm_area` VALUES (441226, 441200, '德庆县');
INSERT INTO `72crm_crm_area` VALUES (441224, 441200, '怀集县');
INSERT INTO `72crm_crm_area` VALUES (441225, 441200, '封开县');
INSERT INTO `72crm_crm_area` VALUES (620403, 620400, '平川区');
INSERT INTO `72crm_crm_area` VALUES (620402, 620400, '白银区');
INSERT INTO `72crm_crm_area` VALUES (620421, 620400, '靖远县');
INSERT INTO `72crm_crm_area` VALUES (620423, 620400, '景泰县');
INSERT INTO `72crm_crm_area` VALUES (620422, 620400, '会宁县');
INSERT INTO `72crm_crm_area` VALUES (650105, 650100, '水磨沟区');
INSERT INTO `72crm_crm_area` VALUES (650121, 650100, '乌鲁木齐县');
INSERT INTO `72crm_crm_area` VALUES (650104, 650100, '新市区');
INSERT INTO `72crm_crm_area` VALUES (650107, 650100, '达坂城区');
INSERT INTO `72crm_crm_area` VALUES (650106, 650100, '头屯河区');
INSERT INTO `72crm_crm_area` VALUES (650109, 650100, '米东区');
INSERT INTO `72crm_crm_area` VALUES (650103, 650100, '沙依巴克区');
INSERT INTO `72crm_crm_area` VALUES (650102, 650100, '天山区');
INSERT INTO `72crm_crm_area` VALUES (150402, 150400, '红山区');
INSERT INTO `72crm_crm_area` VALUES (150403, 150400, '元宝山区');
INSERT INTO `72crm_crm_area` VALUES (150422, 150400, '巴林左旗');
INSERT INTO `72crm_crm_area` VALUES (150423, 150400, '巴林右旗');
INSERT INTO `72crm_crm_area` VALUES (150404, 150400, '松山区');
INSERT INTO `72crm_crm_area` VALUES (150421, 150400, '阿鲁科尔沁旗');
INSERT INTO `72crm_crm_area` VALUES (150426, 150400, '翁牛特旗');
INSERT INTO `72crm_crm_area` VALUES (150424, 150400, '林西县');
INSERT INTO `72crm_crm_area` VALUES (150425, 150400, '克什克腾旗');
INSERT INTO `72crm_crm_area` VALUES (150430, 150400, '敖汉旗');
INSERT INTO `72crm_crm_area` VALUES (150428, 150400, '喀喇沁旗');
INSERT INTO `72crm_crm_area` VALUES (150429, 150400, '宁城县');
INSERT INTO `72crm_crm_area` VALUES (410502, 410500, '文峰区');
INSERT INTO `72crm_crm_area` VALUES (410503, 410500, '北关区');
INSERT INTO `72crm_crm_area` VALUES (410581, 410500, '林州市');
INSERT INTO `72crm_crm_area` VALUES (410526, 410500, '滑县');
INSERT INTO `72crm_crm_area` VALUES (410527, 410500, '内黄县');
INSERT INTO `72crm_crm_area` VALUES (410506, 410500, '龙安区');
INSERT INTO `72crm_crm_area` VALUES (410522, 410500, '安阳县');
INSERT INTO `72crm_crm_area` VALUES (410523, 410500, '汤阴县');
INSERT INTO `72crm_crm_area` VALUES (410505, 410500, '殷都区');
INSERT INTO `72crm_crm_area` VALUES (820108, 820100, '圣方济各堂区');
INSERT INTO `72crm_crm_area` VALUES (820104, 820100, '大堂区');
INSERT INTO `72crm_crm_area` VALUES (820105, 820100, '风顺堂区');
INSERT INTO `72crm_crm_area` VALUES (820106, 820100, '嘉模堂区');
INSERT INTO `72crm_crm_area` VALUES (820107, 820100, '路凼填海区');
INSERT INTO `72crm_crm_area` VALUES (820101, 820100, '花地玛堂区');
INSERT INTO `72crm_crm_area` VALUES (820102, 820100, '花王堂区');
INSERT INTO `72crm_crm_area` VALUES (820103, 820100, '望德堂区');
INSERT INTO `72crm_crm_area` VALUES (440229, 440200, '翁源县');
INSERT INTO `72crm_crm_area` VALUES (440224, 440200, '仁化县');
INSERT INTO `72crm_crm_area` VALUES (440222, 440200, '始兴县');
INSERT INTO `72crm_crm_area` VALUES (440204, 440200, '浈江区');
INSERT INTO `72crm_crm_area` VALUES (440205, 440200, '曲江区');
INSERT INTO `72crm_crm_area` VALUES (440282, 440200, '南雄市');
INSERT INTO `72crm_crm_area` VALUES (440203, 440200, '武江区');
INSERT INTO `72crm_crm_area` VALUES (440232, 440200, '乳源瑶族自治县');
INSERT INTO `72crm_crm_area` VALUES (440233, 440200, '新丰县');
INSERT INTO `72crm_crm_area` VALUES (440281, 440200, '乐昌市');
INSERT INTO `72crm_crm_area` VALUES (533401, 533400, '香格里拉市');
INSERT INTO `72crm_crm_area` VALUES (533422, 533400, '德钦县');
INSERT INTO `72crm_crm_area` VALUES (533423, 533400, '维西傈僳族自治县');
INSERT INTO `72crm_crm_area` VALUES (350181, 350100, '福清市');
INSERT INTO `72crm_crm_area` VALUES (350182, 350100, '长乐市');
INSERT INTO `72crm_crm_area` VALUES (350125, 350100, '永泰县');
INSERT INTO `72crm_crm_area` VALUES (350124, 350100, '闽清县');
INSERT INTO `72crm_crm_area` VALUES (350121, 350100, '闽侯县');
INSERT INTO `72crm_crm_area` VALUES (350123, 350100, '罗源县');
INSERT INTO `72crm_crm_area` VALUES (350122, 350100, '连江县');
INSERT INTO `72crm_crm_area` VALUES (350103, 350100, '台江区');
INSERT INTO `72crm_crm_area` VALUES (350102, 350100, '鼓楼区');
INSERT INTO `72crm_crm_area` VALUES (350128, 350100, '平潭县');
INSERT INTO `72crm_crm_area` VALUES (350111, 350100, '晋安区');
INSERT INTO `72crm_crm_area` VALUES (350105, 350100, '马尾区');
INSERT INTO `72crm_crm_area` VALUES (350104, 350100, '仓山区');
INSERT INTO `72crm_crm_area` VALUES (610203, 610200, '印台区');
INSERT INTO `72crm_crm_area` VALUES (610202, 610200, '王益区');
INSERT INTO `72crm_crm_area` VALUES (610204, 610200, '耀州区');
INSERT INTO `72crm_crm_area` VALUES (610222, 610200, '宜君县');
INSERT INTO `72crm_crm_area` VALUES (712686, 712600, '花莲市');
INSERT INTO `72crm_crm_area` VALUES (712687, 712600, '新城乡');
INSERT INTO `72crm_crm_area` VALUES (712698, 712600, '卓溪乡');
INSERT INTO `72crm_crm_area` VALUES (712699, 712600, '富里乡');
INSERT INTO `72crm_crm_area` VALUES (712696, 712600, '万荣乡');
INSERT INTO `72crm_crm_area` VALUES (712697, 712600, '玉里镇');
INSERT INTO `72crm_crm_area` VALUES (712690, 712600, '吉安乡');
INSERT INTO `72crm_crm_area` VALUES (712691, 712600, '寿丰乡');
INSERT INTO `72crm_crm_area` VALUES (712688, 712600, '太鲁阁');
INSERT INTO `72crm_crm_area` VALUES (712689, 712600, '秀林乡');
INSERT INTO `72crm_crm_area` VALUES (712694, 712600, '丰滨乡');
INSERT INTO `72crm_crm_area` VALUES (712695, 712600, '瑞穗乡');
INSERT INTO `72crm_crm_area` VALUES (712692, 712600, '凤林镇');
INSERT INTO `72crm_crm_area` VALUES (712693, 712600, '光复乡');
INSERT INTO `72crm_crm_area` VALUES (320404, 320400, '钟楼区');
INSERT INTO `72crm_crm_area` VALUES (320481, 320400, '溧阳市');
INSERT INTO `72crm_crm_area` VALUES (320402, 320400, '天宁区');
INSERT INTO `72crm_crm_area` VALUES (320412, 320400, '武进区');
INSERT INTO `72crm_crm_area` VALUES (320413, 320400, '金坛区');
INSERT INTO `72crm_crm_area` VALUES (320411, 320400, '新北区');
INSERT INTO `72crm_crm_area` VALUES (371621, 371600, '惠民县');
INSERT INTO `72crm_crm_area` VALUES (371623, 371600, '无棣县');
INSERT INTO `72crm_crm_area` VALUES (371622, 371600, '阳信县');
INSERT INTO `72crm_crm_area` VALUES (371603, 371600, '沾化区');
INSERT INTO `72crm_crm_area` VALUES (371602, 371600, '滨城区');
INSERT INTO `72crm_crm_area` VALUES (371625, 371600, '博兴县');
INSERT INTO `72crm_crm_area` VALUES (371626, 371600, '邹平县');
INSERT INTO `72crm_crm_area` VALUES (422822, 422800, '建始县');
INSERT INTO `72crm_crm_area` VALUES (422823, 422800, '巴东县');
INSERT INTO `72crm_crm_area` VALUES (422802, 422800, '利川市');
INSERT INTO `72crm_crm_area` VALUES (422801, 422800, '恩施市');
INSERT INTO `72crm_crm_area` VALUES (422828, 422800, '鹤峰县');
INSERT INTO `72crm_crm_area` VALUES (422826, 422800, '咸丰县');
INSERT INTO `72crm_crm_area` VALUES (422827, 422800, '来凤县');
INSERT INTO `72crm_crm_area` VALUES (422825, 422800, '宣恩县');
INSERT INTO `72crm_crm_area` VALUES (653225, 653200, '策勒县');
INSERT INTO `72crm_crm_area` VALUES (653224, 653200, '洛浦县');
INSERT INTO `72crm_crm_area` VALUES (653227, 653200, '民丰县');
INSERT INTO `72crm_crm_area` VALUES (653226, 653200, '于田县');
INSERT INTO `72crm_crm_area` VALUES (653201, 653200, '和田市');
INSERT INTO `72crm_crm_area` VALUES (653221, 653200, '和田县');
INSERT INTO `72crm_crm_area` VALUES (653223, 653200, '皮山县');
INSERT INTO `72crm_crm_area` VALUES (653222, 653200, '墨玉县');
INSERT INTO `72crm_crm_area` VALUES (511923, 511900, '平昌县');
INSERT INTO `72crm_crm_area` VALUES (511922, 511900, '南江县');
INSERT INTO `72crm_crm_area` VALUES (511921, 511900, '通江县');
INSERT INTO `72crm_crm_area` VALUES (511903, 511900, '恩阳区');
INSERT INTO `72crm_crm_area` VALUES (511902, 511900, '巴州区');
INSERT INTO `72crm_crm_area` VALUES (431028, 431000, '安仁县');
INSERT INTO `72crm_crm_area` VALUES (431026, 431000, '汝城县');
INSERT INTO `72crm_crm_area` VALUES (431027, 431000, '桂东县');
INSERT INTO `72crm_crm_area` VALUES (431024, 431000, '嘉禾县');
INSERT INTO `72crm_crm_area` VALUES (431025, 431000, '临武县');
INSERT INTO `72crm_crm_area` VALUES (431022, 431000, '宜章县');
INSERT INTO `72crm_crm_area` VALUES (431023, 431000, '永兴县');
INSERT INTO `72crm_crm_area` VALUES (431021, 431000, '桂阳县');
INSERT INTO `72crm_crm_area` VALUES (431002, 431000, '北湖区');
INSERT INTO `72crm_crm_area` VALUES (431003, 431000, '苏仙区');
INSERT INTO `72crm_crm_area` VALUES (431081, 431000, '资兴市');
INSERT INTO `72crm_crm_area` VALUES (230307, 230300, '麻山区');
INSERT INTO `72crm_crm_area` VALUES (230306, 230300, '城子河区');
INSERT INTO `72crm_crm_area` VALUES (230305, 230300, '梨树区');
INSERT INTO `72crm_crm_area` VALUES (230321, 230300, '鸡东县');
INSERT INTO `72crm_crm_area` VALUES (230304, 230300, '滴道区');
INSERT INTO `72crm_crm_area` VALUES (230303, 230300, '恒山区');
INSERT INTO `72crm_crm_area` VALUES (230302, 230300, '鸡冠区');
INSERT INTO `72crm_crm_area` VALUES (230382, 230300, '密山市');
INSERT INTO `72crm_crm_area` VALUES (230381, 230300, '虎林市');
INSERT INTO `72crm_crm_area` VALUES (520103, 520100, '云岩区');
INSERT INTO `72crm_crm_area` VALUES (520102, 520100, '南明区');
INSERT INTO `72crm_crm_area` VALUES (520181, 520100, '清镇市');
INSERT INTO `72crm_crm_area` VALUES (520115, 520100, '观山湖区');
INSERT INTO `72crm_crm_area` VALUES (520113, 520100, '白云区');
INSERT INTO `72crm_crm_area` VALUES (520112, 520100, '乌当区');
INSERT INTO `72crm_crm_area` VALUES (520111, 520100, '花溪区');
INSERT INTO `72crm_crm_area` VALUES (520123, 520100, '修文县');
INSERT INTO `72crm_crm_area` VALUES (520122, 520100, '息烽县');
INSERT INTO `72crm_crm_area` VALUES (520121, 520100, '开阳县');
INSERT INTO `72crm_crm_area` VALUES (140226, 140200, '左云县');
INSERT INTO `72crm_crm_area` VALUES (140211, 140200, '南郊区');
INSERT INTO `72crm_crm_area` VALUES (140227, 140200, '大同县');
INSERT INTO `72crm_crm_area` VALUES (140224, 140200, '灵丘县');
INSERT INTO `72crm_crm_area` VALUES (140225, 140200, '浑源县');
INSERT INTO `72crm_crm_area` VALUES (140212, 140200, '新荣区');
INSERT INTO `72crm_crm_area` VALUES (140202, 140200, '城区');
INSERT INTO `72crm_crm_area` VALUES (140203, 140200, '矿区');
INSERT INTO `72crm_crm_area` VALUES (140222, 140200, '天镇县');
INSERT INTO `72crm_crm_area` VALUES (140223, 140200, '广灵县');
INSERT INTO `72crm_crm_area` VALUES (140221, 140200, '阳高县');
INSERT INTO `72crm_crm_area` VALUES (370613, 370600, '莱山区');
INSERT INTO `72crm_crm_area` VALUES (370612, 370600, '牟平区');
INSERT INTO `72crm_crm_area` VALUES (370611, 370600, '福山区');
INSERT INTO `72crm_crm_area` VALUES (370685, 370600, '招远市');
INSERT INTO `72crm_crm_area` VALUES (370684, 370600, '蓬莱市');
INSERT INTO `72crm_crm_area` VALUES (370687, 370600, '海阳市');
INSERT INTO `72crm_crm_area` VALUES (370686, 370600, '栖霞市');
INSERT INTO `72crm_crm_area` VALUES (370681, 370600, '龙口市');
INSERT INTO `72crm_crm_area` VALUES (370683, 370600, '莱州市');
INSERT INTO `72crm_crm_area` VALUES (370602, 370600, '芝罘区');
INSERT INTO `72crm_crm_area` VALUES (370634, 370600, '长岛县');
INSERT INTO `72crm_crm_area` VALUES (370682, 370600, '莱阳市');
INSERT INTO `72crm_crm_area` VALUES (540602, 540600, '色尼区');
INSERT INTO `72crm_crm_area` VALUES (540621, 540600, '嘉黎县');
INSERT INTO `72crm_crm_area` VALUES (540622, 540600, '比如县');
INSERT INTO `72crm_crm_area` VALUES (540623, 540600, '聂荣县');
INSERT INTO `72crm_crm_area` VALUES (540624, 540600, '安多县');
INSERT INTO `72crm_crm_area` VALUES (540625, 540600, '申扎县');
INSERT INTO `72crm_crm_area` VALUES (540626, 540600, '索县');
INSERT INTO `72crm_crm_area` VALUES (540627, 540600, '班戈县');
INSERT INTO `72crm_crm_area` VALUES (540628, 540600, '巴青县');
INSERT INTO `72crm_crm_area` VALUES (540629, 540600, '尼玛县');
INSERT INTO `72crm_crm_area` VALUES (540630, 540600, '双湖县');
INSERT INTO `72crm_crm_area` VALUES (510903, 510900, '船山区');
INSERT INTO `72crm_crm_area` VALUES (510923, 510900, '大英县');
INSERT INTO `72crm_crm_area` VALUES (510922, 510900, '射洪县');
INSERT INTO `72crm_crm_area` VALUES (510921, 510900, '蓬溪县');
INSERT INTO `72crm_crm_area` VALUES (510904, 510900, '安居区');
INSERT INTO `72crm_crm_area` VALUES (152530, 152500, '正蓝旗');
INSERT INTO `72crm_crm_area` VALUES (152531, 152500, '多伦县');
INSERT INTO `72crm_crm_area` VALUES (152528, 152500, '镶黄旗');
INSERT INTO `72crm_crm_area` VALUES (152529, 152500, '正镶白旗');
INSERT INTO `72crm_crm_area` VALUES (152502, 152500, '锡林浩特市');
INSERT INTO `72crm_crm_area` VALUES (152501, 152500, '二连浩特市');
INSERT INTO `72crm_crm_area` VALUES (152522, 152500, '阿巴嘎旗');
INSERT INTO `72crm_crm_area` VALUES (152523, 152500, '苏尼特左旗');
INSERT INTO `72crm_crm_area` VALUES (152526, 152500, '西乌珠穆沁旗');
INSERT INTO `72crm_crm_area` VALUES (152527, 152500, '太仆寺旗');
INSERT INTO `72crm_crm_area` VALUES (152524, 152500, '苏尼特右旗');
INSERT INTO `72crm_crm_area` VALUES (152525, 152500, '东乌珠穆沁旗');
INSERT INTO `72crm_crm_area` VALUES (430500, 430000, '邵阳市');
INSERT INTO `72crm_crm_area` VALUES (431300, 430000, '娄底市');
INSERT INTO `72crm_crm_area` VALUES (430400, 430000, '衡阳市');
INSERT INTO `72crm_crm_area` VALUES (431200, 430000, '怀化市');
INSERT INTO `72crm_crm_area` VALUES (430700, 430000, '常德市');
INSERT INTO `72crm_crm_area` VALUES (433100, 430000, '湘西土家族苗族自治州');
INSERT INTO `72crm_crm_area` VALUES (430600, 430000, '岳阳市');
INSERT INTO `72crm_crm_area` VALUES (430100, 430000, '长沙市');
INSERT INTO `72crm_crm_area` VALUES (430900, 430000, '益阳市');
INSERT INTO `72crm_crm_area` VALUES (430800, 430000, '张家界市');
INSERT INTO `72crm_crm_area` VALUES (430300, 430000, '湘潭市');
INSERT INTO `72crm_crm_area` VALUES (431100, 430000, '永州市');
INSERT INTO `72crm_crm_area` VALUES (430200, 430000, '株洲市');
INSERT INTO `72crm_crm_area` VALUES (431000, 430000, '郴州市');
INSERT INTO `72crm_crm_area` VALUES (131025, 131000, '大城县');
INSERT INTO `72crm_crm_area` VALUES (131024, 131000, '香河县');
INSERT INTO `72crm_crm_area` VALUES (131026, 131000, '文安县');
INSERT INTO `72crm_crm_area` VALUES (131028, 131000, '大厂回族自治县');
INSERT INTO `72crm_crm_area` VALUES (131082, 131000, '三河市');
INSERT INTO `72crm_crm_area` VALUES (131003, 131000, '广阳区');
INSERT INTO `72crm_crm_area` VALUES (131002, 131000, '安次区');
INSERT INTO `72crm_crm_area` VALUES (131081, 131000, '霸州市');
INSERT INTO `72crm_crm_area` VALUES (131023, 131000, '永清县');
INSERT INTO `72crm_crm_area` VALUES (131022, 131000, '固安县');
INSERT INTO `72crm_crm_area` VALUES (450502, 450500, '海城区');
INSERT INTO `72crm_crm_area` VALUES (450503, 450500, '银海区');
INSERT INTO `72crm_crm_area` VALUES (450512, 450500, '铁山港区');
INSERT INTO `72crm_crm_area` VALUES (450521, 450500, '合浦县');
INSERT INTO `72crm_crm_area` VALUES (710626, 710600, '鹿谷乡');
INSERT INTO `72crm_crm_area` VALUES (710624, 710600, '信义乡');
INSERT INTO `72crm_crm_area` VALUES (710625, 710600, '竹山镇');
INSERT INTO `72crm_crm_area` VALUES (710618, 710600, '埔里镇');
INSERT INTO `72crm_crm_area` VALUES (710619, 710600, '仁爱乡');
INSERT INTO `72crm_crm_area` VALUES (710616, 710600, '草屯镇');
INSERT INTO `72crm_crm_area` VALUES (710617, 710600, '国姓乡');
INSERT INTO `72crm_crm_area` VALUES (710622, 710600, '水里乡');
INSERT INTO `72crm_crm_area` VALUES (710623, 710600, '鱼池乡');
INSERT INTO `72crm_crm_area` VALUES (710620, 710600, '名间乡');
INSERT INTO `72crm_crm_area` VALUES (710621, 710600, '集集镇');
INSERT INTO `72crm_crm_area` VALUES (710614, 710600, '南投市');
INSERT INTO `72crm_crm_area` VALUES (710615, 710600, '中寮乡');
INSERT INTO `72crm_crm_area` VALUES (420822, 420800, '沙洋县');
INSERT INTO `72crm_crm_area` VALUES (420804, 420800, '掇刀区');
INSERT INTO `72crm_crm_area` VALUES (420821, 420800, '京山县');
INSERT INTO `72crm_crm_area` VALUES (420802, 420800, '东宝区');
INSERT INTO `72crm_crm_area` VALUES (420881, 420800, '钟祥市');
INSERT INTO `72crm_crm_area` VALUES (220113, 220100, '九台区');
INSERT INTO `72crm_crm_area` VALUES (220112, 220100, '双阳区');
INSERT INTO `72crm_crm_area` VALUES (220103, 220100, '宽城区');
INSERT INTO `72crm_crm_area` VALUES (220183, 220100, '德惠市');
INSERT INTO `72crm_crm_area` VALUES (220102, 220100, '南关区');
INSERT INTO `72crm_crm_area` VALUES (220182, 220100, '榆树市');
INSERT INTO `72crm_crm_area` VALUES (220106, 220100, '绿园区');
INSERT INTO `72crm_crm_area` VALUES (220122, 220100, '农安县');
INSERT INTO `72crm_crm_area` VALUES (220105, 220100, '二道区');
INSERT INTO `72crm_crm_area` VALUES (220104, 220100, '朝阳区');
INSERT INTO `72crm_crm_area` VALUES (330727, 330700, '磐安县');
INSERT INTO `72crm_crm_area` VALUES (330726, 330700, '浦江县');
INSERT INTO `72crm_crm_area` VALUES (330784, 330700, '永康市');
INSERT INTO `72crm_crm_area` VALUES (330723, 330700, '武义县');
INSERT INTO `72crm_crm_area` VALUES (330781, 330700, '兰溪市');
INSERT INTO `72crm_crm_area` VALUES (330703, 330700, '金东区');
INSERT INTO `72crm_crm_area` VALUES (330783, 330700, '东阳市');
INSERT INTO `72crm_crm_area` VALUES (330702, 330700, '婺城区');
INSERT INTO `72crm_crm_area` VALUES (330782, 330700, '义乌市');
INSERT INTO `72crm_crm_area` VALUES (433126, 433100, '古丈县');
INSERT INTO `72crm_crm_area` VALUES (433127, 433100, '永顺县');
INSERT INTO `72crm_crm_area` VALUES (433124, 433100, '花垣县');
INSERT INTO `72crm_crm_area` VALUES (433125, 433100, '保靖县');
INSERT INTO `72crm_crm_area` VALUES (433122, 433100, '泸溪县');
INSERT INTO `72crm_crm_area` VALUES (433123, 433100, '凤凰县');
INSERT INTO `72crm_crm_area` VALUES (433101, 433100, '吉首市');
INSERT INTO `72crm_crm_area` VALUES (433130, 433100, '龙山县');
INSERT INTO `72crm_crm_area` VALUES (429006, 429000, '天门市');
INSERT INTO `72crm_crm_area` VALUES (429004, 429000, '仙桃市');
INSERT INTO `72crm_crm_area` VALUES (429005, 429000, '潜江市');
INSERT INTO `72crm_crm_area` VALUES (429021, 429000, '神农架林区');
INSERT INTO `72crm_crm_area` VALUES (130400, 130000, '邯郸市');
INSERT INTO `72crm_crm_area` VALUES (130800, 130000, '承德市');
INSERT INTO `72crm_crm_area` VALUES (130100, 130000, '石家庄市');
INSERT INTO `72crm_crm_area` VALUES (130500, 130000, '邢台市');
INSERT INTO `72crm_crm_area` VALUES (130900, 130000, '沧州市');
INSERT INTO `72crm_crm_area` VALUES (130200, 130000, '唐山市');
INSERT INTO `72crm_crm_area` VALUES (130600, 130000, '保定市');
INSERT INTO `72crm_crm_area` VALUES (131000, 130000, '廊坊市');
INSERT INTO `72crm_crm_area` VALUES (130300, 130000, '秦皇岛市');
INSERT INTO `72crm_crm_area` VALUES (130700, 130000, '张家口市');
INSERT INTO `72crm_crm_area` VALUES (131100, 130000, '衡水市');
INSERT INTO `72crm_crm_area` VALUES (650203, 650200, '克拉玛依区');
INSERT INTO `72crm_crm_area` VALUES (650202, 650200, '独山子区');
INSERT INTO `72crm_crm_area` VALUES (650205, 650200, '乌尔禾区');
INSERT INTO `72crm_crm_area` VALUES (650204, 650200, '白碱滩区');
INSERT INTO `72crm_crm_area` VALUES (441302, 441300, '惠城区');
INSERT INTO `72crm_crm_area` VALUES (441303, 441300, '惠阳区');
INSERT INTO `72crm_crm_area` VALUES (441324, 441300, '龙门县');
INSERT INTO `72crm_crm_area` VALUES (441322, 441300, '博罗县');
INSERT INTO `72crm_crm_area` VALUES (441323, 441300, '惠东县');
INSERT INTO `72crm_crm_area` VALUES (360421, 360400, '九江县');
INSERT INTO `72crm_crm_area` VALUES (360423, 360400, '武宁县');
INSERT INTO `72crm_crm_area` VALUES (360481, 360400, '瑞昌市');
INSERT INTO `72crm_crm_area` VALUES (360482, 360400, '共青城市');
INSERT INTO `72crm_crm_area` VALUES (360429, 360400, '湖口县');
INSERT INTO `72crm_crm_area` VALUES (360428, 360400, '都昌县');
INSERT INTO `72crm_crm_area` VALUES (360430, 360400, '彭泽县');
INSERT INTO `72crm_crm_area` VALUES (360425, 360400, '永修县');
INSERT INTO `72crm_crm_area` VALUES (360424, 360400, '修水县');
INSERT INTO `72crm_crm_area` VALUES (360427, 360400, '庐山市');
INSERT INTO `72crm_crm_area` VALUES (360426, 360400, '德安县');
INSERT INTO `72crm_crm_area` VALUES (360403, 360400, '浔阳区');
INSERT INTO `72crm_crm_area` VALUES (360402, 360400, '柴桑区');
INSERT INTO `72crm_crm_area` VALUES (654301, 654300, '阿勒泰市');
INSERT INTO `72crm_crm_area` VALUES (654321, 654300, '布尔津县');
INSERT INTO `72crm_crm_area` VALUES (654323, 654300, '福海县');
INSERT INTO `72crm_crm_area` VALUES (654322, 654300, '富蕴县');
INSERT INTO `72crm_crm_area` VALUES (654325, 654300, '青河县');
INSERT INTO `72crm_crm_area` VALUES (654324, 654300, '哈巴河县');
INSERT INTO `72crm_crm_area` VALUES (654326, 654300, '吉木乃县');
INSERT INTO `72crm_crm_area` VALUES (411622, 411600, '西华县');
INSERT INTO `72crm_crm_area` VALUES (411623, 411600, '商水县');
INSERT INTO `72crm_crm_area` VALUES (411621, 411600, '扶沟县');
INSERT INTO `72crm_crm_area` VALUES (411602, 411600, '川汇区');
INSERT INTO `72crm_crm_area` VALUES (411681, 411600, '项城市');
INSERT INTO `72crm_crm_area` VALUES (411628, 411600, '鹿邑县');
INSERT INTO `72crm_crm_area` VALUES (411626, 411600, '淮阳县');
INSERT INTO `72crm_crm_area` VALUES (411627, 411600, '太康县');
INSERT INTO `72crm_crm_area` VALUES (411624, 411600, '沈丘县');
INSERT INTO `72crm_crm_area` VALUES (411625, 411600, '郸城县');
INSERT INTO `72crm_crm_area` VALUES (210903, 210900, '新邱区');
INSERT INTO `72crm_crm_area` VALUES (210902, 210900, '海州区');
INSERT INTO `72crm_crm_area` VALUES (210922, 210900, '彰武县');
INSERT INTO `72crm_crm_area` VALUES (210905, 210900, '清河门区');
INSERT INTO `72crm_crm_area` VALUES (210921, 210900, '阜新蒙古族自治县');
INSERT INTO `72crm_crm_area` VALUES (210904, 210900, '太平区');
INSERT INTO `72crm_crm_area` VALUES (210911, 210900, '细河区');
INSERT INTO `72crm_crm_area` VALUES (620521, 620500, '清水县');
INSERT INTO `72crm_crm_area` VALUES (620523, 620500, '甘谷县');
INSERT INTO `72crm_crm_area` VALUES (620522, 620500, '秦安县');
INSERT INTO `72crm_crm_area` VALUES (620525, 620500, '张家川回族自治县');
INSERT INTO `72crm_crm_area` VALUES (620524, 620500, '武山县');
INSERT INTO `72crm_crm_area` VALUES (620503, 620500, '麦积区');
INSERT INTO `72crm_crm_area` VALUES (620502, 620500, '秦州区');
INSERT INTO `72crm_crm_area` VALUES (150502, 150500, '科尔沁区');
INSERT INTO `72crm_crm_area` VALUES (150581, 150500, '霍林郭勒市');
INSERT INTO `72crm_crm_area` VALUES (150522, 150500, '科尔沁左翼后旗');
INSERT INTO `72crm_crm_area` VALUES (150523, 150500, '开鲁县');
INSERT INTO `72crm_crm_area` VALUES (150521, 150500, '科尔沁左翼中旗');
INSERT INTO `72crm_crm_area` VALUES (150526, 150500, '扎鲁特旗');
INSERT INTO `72crm_crm_area` VALUES (150524, 150500, '库伦旗');
INSERT INTO `72crm_crm_area` VALUES (150525, 150500, '奈曼旗');
INSERT INTO `72crm_crm_area` VALUES (530424, 530400, '华宁县');
INSERT INTO `72crm_crm_area` VALUES (530425, 530400, '易门县');
INSERT INTO `72crm_crm_area` VALUES (530426, 530400, '峨山彝族自治县');
INSERT INTO `72crm_crm_area` VALUES (530427, 530400, '新平彝族傣族自治县');
INSERT INTO `72crm_crm_area` VALUES (530428, 530400, '元江哈尼族彝族傣族自治县');
INSERT INTO `72crm_crm_area` VALUES (530402, 530400, '红塔区');
INSERT INTO `72crm_crm_area` VALUES (530403, 530400, '江川区');
INSERT INTO `72crm_crm_area` VALUES (530422, 530400, '澄江县');
INSERT INTO `72crm_crm_area` VALUES (530423, 530400, '通海县');
INSERT INTO `72crm_crm_area` VALUES (632825, 632800, '海西蒙古族藏族自治州直辖');
INSERT INTO `72crm_crm_area` VALUES (632801, 632800, '格尔木市');
INSERT INTO `72crm_crm_area` VALUES (632802, 632800, '德令哈市');
INSERT INTO `72crm_crm_area` VALUES (632821, 632800, '乌兰县');
INSERT INTO `72crm_crm_area` VALUES (632823, 632800, '天峻县');
INSERT INTO `72crm_crm_area` VALUES (632822, 632800, '都兰县');
INSERT INTO `72crm_crm_area` VALUES (440310, 440300, '坪山区');
INSERT INTO `72crm_crm_area` VALUES (440308, 440300, '盐田区');
INSERT INTO `72crm_crm_area` VALUES (440309, 440300, '龙华区');
INSERT INTO `72crm_crm_area` VALUES (440306, 440300, '宝安区');
INSERT INTO `72crm_crm_area` VALUES (440307, 440300, '龙岗区');
INSERT INTO `72crm_crm_area` VALUES (440304, 440300, '福田区');
INSERT INTO `72crm_crm_area` VALUES (440305, 440300, '南山区');
INSERT INTO `72crm_crm_area` VALUES (440303, 440300, '罗湖区');
INSERT INTO `72crm_crm_area` VALUES (410611, 410600, '淇滨区');
INSERT INTO `72crm_crm_area` VALUES (410622, 410600, '淇县');
INSERT INTO `72crm_crm_area` VALUES (410621, 410600, '浚县');
INSERT INTO `72crm_crm_area` VALUES (410602, 410600, '鹤山区');
INSERT INTO `72crm_crm_area` VALUES (410603, 410600, '山城区');
INSERT INTO `72crm_crm_area` VALUES (320581, 320500, '常熟市');
INSERT INTO `72crm_crm_area` VALUES (320582, 320500, '张家港市');
INSERT INTO `72crm_crm_area` VALUES (320583, 320500, '昆山市');
INSERT INTO `72crm_crm_area` VALUES (320508, 320500, '姑苏区');
INSERT INTO `72crm_crm_area` VALUES (320509, 320500, '吴江区');
INSERT INTO `72crm_crm_area` VALUES (320505, 320500, '虎丘区');
INSERT INTO `72crm_crm_area` VALUES (320585, 320500, '太仓市');
INSERT INTO `72crm_crm_area` VALUES (320506, 320500, '吴中区');
INSERT INTO `72crm_crm_area` VALUES (320507, 320500, '相城区');
INSERT INTO `72crm_crm_area` VALUES (371703, 371700, '定陶区');
INSERT INTO `72crm_crm_area` VALUES (371702, 371700, '牡丹区');
INSERT INTO `72crm_crm_area` VALUES (371728, 371700, '东明县');
INSERT INTO `72crm_crm_area` VALUES (371725, 371700, '郓城县');
INSERT INTO `72crm_crm_area` VALUES (371724, 371700, '巨野县');
INSERT INTO `72crm_crm_area` VALUES (371726, 371700, '鄄城县');
INSERT INTO `72crm_crm_area` VALUES (371721, 371700, '曹县');
INSERT INTO `72crm_crm_area` VALUES (371723, 371700, '成武县');
INSERT INTO `72crm_crm_area` VALUES (371722, 371700, '单县');
INSERT INTO `72crm_crm_area` VALUES (610329, 610300, '麟游县');
INSERT INTO `72crm_crm_area` VALUES (610328, 610300, '千阳县');
INSERT INTO `72crm_crm_area` VALUES (610331, 610300, '太白县');
INSERT INTO `72crm_crm_area` VALUES (610330, 610300, '凤县');
INSERT INTO `72crm_crm_area` VALUES (610303, 610300, '金台区');
INSERT INTO `72crm_crm_area` VALUES (610302, 610300, '渭滨区');
INSERT INTO `72crm_crm_area` VALUES (610304, 610300, '陈仓区');
INSERT INTO `72crm_crm_area` VALUES (610323, 610300, '岐山县');
INSERT INTO `72crm_crm_area` VALUES (610322, 610300, '凤翔县');
INSERT INTO `72crm_crm_area` VALUES (610324, 610300, '扶风县');
INSERT INTO `72crm_crm_area` VALUES (610327, 610300, '陇县');
INSERT INTO `72crm_crm_area` VALUES (610326, 610300, '眉县');
INSERT INTO `72crm_crm_area` VALUES (712794, 712700, '马公市');
INSERT INTO `72crm_crm_area` VALUES (712795, 712700, '西屿乡');
INSERT INTO `72crm_crm_area` VALUES (712798, 712700, '白沙乡');
INSERT INTO `72crm_crm_area` VALUES (712799, 712700, '湖西乡');
INSERT INTO `72crm_crm_area` VALUES (712796, 712700, '望安乡');
INSERT INTO `72crm_crm_area` VALUES (712797, 712700, '七美乡');
INSERT INTO `72crm_crm_area` VALUES (431126, 431100, '宁远县');
INSERT INTO `72crm_crm_area` VALUES (431127, 431100, '蓝山县');
INSERT INTO `72crm_crm_area` VALUES (431124, 431100, '道县');
INSERT INTO `72crm_crm_area` VALUES (431125, 431100, '江永县');
INSERT INTO `72crm_crm_area` VALUES (431122, 431100, '东安县');
INSERT INTO `72crm_crm_area` VALUES (431123, 431100, '双牌县');
INSERT INTO `72crm_crm_area` VALUES (431121, 431100, '祁阳县');
INSERT INTO `72crm_crm_area` VALUES (431102, 431100, '零陵区');
INSERT INTO `72crm_crm_area` VALUES (431103, 431100, '冷水滩区');
INSERT INTO `72crm_crm_area` VALUES (431128, 431100, '新田县');
INSERT INTO `72crm_crm_area` VALUES (431129, 431100, '江华瑶族自治县');
INSERT INTO `72crm_crm_area` VALUES (350213, 350200, '翔安区');
INSERT INTO `72crm_crm_area` VALUES (350212, 350200, '同安区');
INSERT INTO `72crm_crm_area` VALUES (350211, 350200, '集美区');
INSERT INTO `72crm_crm_area` VALUES (350205, 350200, '海沧区');
INSERT INTO `72crm_crm_area` VALUES (350206, 350200, '湖里区');
INSERT INTO `72crm_crm_area` VALUES (350203, 350200, '思明区');

-- ----------------------------
-- Table structure for 72crm_crm_business
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_business`;
CREATE TABLE `72crm_crm_business`  (
  `business_id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) NULL DEFAULT NULL COMMENT '商机状态组',
  `status_id` int(11) NULL DEFAULT NULL COMMENT '销售阶段',
  `next_time` datetime(0) NULL DEFAULT NULL COMMENT '下次联系时间',
  `customer_id` int(11) NOT NULL COMMENT '客户ID',
  `contacts_id` int(11) NULL DEFAULT NULL COMMENT '首要联系人ID',
  `deal_date` datetime(0) NULL DEFAULT NULL COMMENT '预计成交日期',
  `business_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商机名称',
  `money` decimal(18, 2) NULL DEFAULT NULL COMMENT '商机金额',
  `discount_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '整单折扣',
  `total_price` decimal(17, 2) NULL DEFAULT NULL COMMENT '产品总金额',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次 比如附件批次',
  `ro_user_id` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '只读权限',
  `rw_user_id` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '读写权限',
  `is_end` int(4) NOT NULL DEFAULT 0 COMMENT '1赢单2输单3无效',
  `status_remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `status` int(1) NULL DEFAULT 1 COMMENT '1正常 3  删除',
  `last_time` datetime(0) NULL DEFAULT NULL COMMENT '最后跟进时间',
  PRIMARY KEY (`business_id`) USING BTREE,
  INDEX `owner_user_id`(`owner_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商机表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_business
-- ----------------------------
INSERT INTO `72crm_crm_business` VALUES (8, 13774, 52173, NULL, 15, NULL, '2020-12-18 00:00:00', '中科金审测试一', 158000.00, 20.20, 158000.00, '', 16360, 16360, '2020-12-17 13:55:37', '2020-12-17 14:33:00', '2ef597a44394468e8bf0bbd25e7a6629', ',', ',', 0, '', 3, NULL);
INSERT INTO `72crm_crm_business` VALUES (9, 13775, 52181, NULL, 15, NULL, '2020-12-24 00:00:00', '中科金审测试二', 100000.00, 49.49, 100000.00, '', 16360, 16360, '2020-12-17 14:31:21', '2020-12-17 14:31:21', '4dca21f82a7740b38f9a7ae4173b6be5', ',', ',', 0, '', 3, NULL);
INSERT INTO `72crm_crm_business` VALUES (10, 13775, 52184, NULL, 19, NULL, NULL, '华南数据分析服务', 2000000.00, 0.00, 0.00, '', 16407, 16407, '2021-01-05 18:59:54', '2021-01-05 18:59:54', 'adaa433537bb4dd7b2df4599207e8190', ',', ',', 1, '', 3, NULL);

-- ----------------------------
-- Table structure for 72crm_crm_business_change
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_business_change`;
CREATE TABLE `72crm_crm_business_change`  (
  `change_id` int(10) NOT NULL AUTO_INCREMENT,
  `business_id` int(10) NOT NULL COMMENT '商机ID',
  `status_id` int(10) NOT NULL COMMENT '阶段ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人',
  PRIMARY KEY (`change_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商机阶段变化表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_business_change
-- ----------------------------
INSERT INTO `72crm_crm_business_change` VALUES (1, 6, 52174, '2020-11-25 18:20:00', 16362);
INSERT INTO `72crm_crm_business_change` VALUES (2, 10, 52182, '2021-01-05 19:00:26', 16407);
INSERT INTO `72crm_crm_business_change` VALUES (3, 10, 52183, '2021-01-05 19:00:29', 16407);
INSERT INTO `72crm_crm_business_change` VALUES (4, 10, 52184, '2021-01-05 19:00:33', 16407);

-- ----------------------------
-- Table structure for 72crm_crm_business_data
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_business_data`;
CREATE TABLE `72crm_crm_business_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`(191)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商机扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_business_product
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_business_product`;
CREATE TABLE `72crm_crm_business_product`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT,
  `business_id` int(11) NOT NULL COMMENT '商机ID',
  `product_id` int(11) NOT NULL COMMENT '产品ID',
  `price` decimal(18, 2) NOT NULL COMMENT '产品单价',
  `sales_price` decimal(18, 2) NOT NULL COMMENT '销售价格',
  `num` decimal(10, 2) NOT NULL COMMENT '数量',
  `discount` int(10) NOT NULL COMMENT '折扣',
  `subtotal` decimal(18, 2) NOT NULL COMMENT '小计（折扣后价格）',
  `unit` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '单位',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商机产品关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_business_product
-- ----------------------------
INSERT INTO `72crm_crm_business_product` VALUES (3, 6, 3, 141.00, 141.00, 1.00, 0, 141.00, '块');
INSERT INTO `72crm_crm_business_product` VALUES (4, 6, 13, 151.00, 151.00, 1.00, 0, 151.00, '块');

-- ----------------------------
-- Table structure for 72crm_crm_business_status
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_business_status`;
CREATE TABLE `72crm_crm_business_status`  (
  `status_id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) NOT NULL COMMENT '商机状态类别ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `rate` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '赢单率',
  `order_num` int(4) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`status_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52185 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商机状态' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_business_status
-- ----------------------------
INSERT INTO `72crm_crm_business_status` VALUES (52173, 13774, '验证客户', '20', 1);
INSERT INTO `72crm_crm_business_status` VALUES (52174, 13774, '需求分析', '30', 2);
INSERT INTO `72crm_crm_business_status` VALUES (52175, 13774, '方案/报价', '80', 3);
INSERT INTO `72crm_crm_business_status` VALUES (52180, 13775, '发现商机阶段', '25', 1);
INSERT INTO `72crm_crm_business_status` VALUES (52181, 13775, '明确商机需求', '50', 2);
INSERT INTO `72crm_crm_business_status` VALUES (52182, 13775, '采购意向明确', '75', 3);
INSERT INTO `72crm_crm_business_status` VALUES (52183, 13775, '合同签订阶段', '95', 4);
INSERT INTO `72crm_crm_business_status` VALUES (52184, 13775, '合同签订完成', '100', 5);

-- ----------------------------
-- Table structure for 72crm_crm_business_type
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_business_type`;
CREATE TABLE `72crm_crm_business_type`  (
  `type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `dept_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门ID',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '1启用0禁用',
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13776 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商机状态组类别' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_business_type
-- ----------------------------
INSERT INTO `72crm_crm_business_type` VALUES (13774, '销售流程商机组', '', 3, '2019-05-11 16:25:09', NULL, 2);
INSERT INTO `72crm_crm_business_type` VALUES (13775, '销售商机流程', '', 16360, '2020-12-17 14:24:52', '2020-12-17 14:28:37', 1);

-- ----------------------------
-- Table structure for 72crm_crm_business_user_star
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_business_user_star`;
CREATE TABLE `72crm_crm_business_user_star`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `business_id` int(11) NOT NULL COMMENT '客户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户商机标星关系表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_contacts
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_contacts`;
CREATE TABLE `72crm_crm_contacts`  (
  `contacts_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人名称',
  `next_time` datetime(0) NULL DEFAULT NULL COMMENT '下次联系时间',
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '手机',
  `telephone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `post` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务',
  `customer_id` int(11) NOT NULL COMMENT '客户ID',
  `address` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '地址',
  `remark` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次',
  `last_time` datetime(0) NULL DEFAULT NULL COMMENT '最后跟进时间',
  PRIMARY KEY (`contacts_id`) USING BTREE,
  INDEX `owner_user_id`(`owner_user_id`) USING BTREE,
  INDEX `customer_id`(`customer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '联系人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_contacts_business
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_contacts_business`;
CREATE TABLE `72crm_crm_contacts_business`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `business_id` int(11) NOT NULL,
  `contacts_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商机联系人关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_contacts_data
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_contacts_data`;
CREATE TABLE `72crm_crm_contacts_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`(191)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '联系人扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_contacts_user_star
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_contacts_user_star`;
CREATE TABLE `72crm_crm_contacts_user_star`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `contacts_id` int(11) NOT NULL COMMENT '客户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户联系人标星关系表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_contract
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_contract`;
CREATE TABLE `72crm_crm_contract`  (
  `contract_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同名称',
  `customer_id` int(11) NULL DEFAULT NULL COMMENT '客户ID',
  `business_id` int(11) NULL DEFAULT NULL COMMENT '商机ID',
  `check_status` int(4) NOT NULL DEFAULT 0 COMMENT '0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废',
  `examine_record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录ID',
  `order_date` datetime(0) NULL DEFAULT NULL COMMENT '下单日期',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同编号',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `money` decimal(18, 2) NULL DEFAULT NULL COMMENT '合同金额',
  `discount_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '整单折扣',
  `total_price` decimal(17, 2) NULL DEFAULT NULL COMMENT '产品总金额',
  `types` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同类型',
  `payment_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '付款方式',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次 比如附件批次',
  `ro_user_id` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '只读权限',
  `rw_user_id` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '读写权限',
  `contacts_id` int(11) NULL DEFAULT NULL COMMENT '客户签约人（联系人id）',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `company_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司签约人',
  `last_time` datetime(0) NULL DEFAULT NULL COMMENT '最后跟进时间',
  `received_money` decimal(17, 2) NULL DEFAULT 0.00,
  `unreceived_money` decimal(17, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`contract_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '合同表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_contract_data
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_contract_data`;
CREATE TABLE `72crm_crm_contract_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`(191)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '合同扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_contract_product
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_contract_product`;
CREATE TABLE `72crm_crm_contract_product`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT,
  `contract_id` int(11) NOT NULL COMMENT '合同ID',
  `product_id` int(11) NOT NULL COMMENT '产品ID',
  `price` decimal(18, 2) NOT NULL COMMENT '产品单价',
  `sales_price` decimal(18, 2) NOT NULL COMMENT '销售价格',
  `num` decimal(10, 2) NOT NULL COMMENT '数量',
  `discount` decimal(18, 4) NOT NULL COMMENT '折扣',
  `subtotal` decimal(18, 2) NOT NULL COMMENT '小计（折扣后价格）',
  `unit` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '单位',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '合同产品关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_customer
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_customer`;
CREATE TABLE `72crm_crm_customer`  (
  `customer_id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `followup` int(11) NULL DEFAULT NULL COMMENT '跟进状态 0未跟进1已跟进',
  `is_lock` int(1) NOT NULL DEFAULT 0 COMMENT '1锁定',
  `next_time` datetime(0) NULL DEFAULT NULL COMMENT '下次联系时间',
  `deal_status` int(4) NULL DEFAULT 0 COMMENT '成交状态 0 未成交 1 已成交',
  `deal_time` datetime(0) NULL DEFAULT NULL COMMENT '成交时间',
  `contacts_id` int(11) NULL DEFAULT NULL COMMENT '首要联系人ID',
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `telephone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '电话',
  `website` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '网址',
  `email` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `remark` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `ro_user_id` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '只读权限',
  `rw_user_id` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '读写权限',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '省市区',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '定位信息',
  `detail_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '详细地址',
  `lng` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '地理位置经度',
  `lat` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '地理位置维度',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次 比如附件批次',
  `status` int(1) NULL DEFAULT 1 COMMENT '客户状态 1 正常 2锁定 3删除',
  `last_time` datetime(0) NULL DEFAULT NULL COMMENT '最后跟进时间',
  `pool_time` datetime(0) NULL DEFAULT NULL COMMENT '放入公海时间',
  `is_receive` int(1) NULL DEFAULT NULL COMMENT '1 分配 2 领取',
  `last_content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后一条跟进记录',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '接收到客户时间',
  `pre_owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '进入公海前负责人id',
  PRIMARY KEY (`customer_id`) USING BTREE,
  INDEX `update_time`(`update_time`) USING BTREE,
  INDEX `owner_user_id`(`owner_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '客户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_customer_data
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_customer_data`;
CREATE TABLE `72crm_crm_customer_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`(191)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客户扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_customer_pool
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_customer_pool`;
CREATE TABLE `72crm_crm_customer_pool`  (
  `pool_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公海id',
  `pool_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公海名称',
  `admin_user_id` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '管理员 “,”分割',
  `member_user_id` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '公海规则员工成员 “,”分割',
  `member_dept_id` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '公海规则部门成员 “,”分割',
  `status` int(2) NOT NULL DEFAULT 1 COMMENT '状态 0 停用 1启用',
  `pre_owner_setting` int(2) NOT NULL COMMENT '前负责人领取规则 0不限制 1限制',
  `pre_owner_setting_day` int(4) NULL DEFAULT NULL COMMENT '前负责人领取规则限制天数',
  `receive_setting` int(2) NOT NULL COMMENT '是否限制领取频率 0不限制 1限制',
  `receive_num` int(4) NULL DEFAULT NULL COMMENT '领取频率规则',
  `remind_setting` int(2) NOT NULL COMMENT '是否设置提前提醒 0不开启 1开启',
  `remind_day` int(11) NULL DEFAULT NULL COMMENT '提醒规则天数',
  `put_in_rule` int(2) NOT NULL COMMENT '收回规则 0不自动收回 1自动收回',
  `create_user_id` bigint(20) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`pool_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35955 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公海表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_customer_pool
-- ----------------------------
INSERT INTO `72crm_crm_customer_pool` VALUES (35954, '系统默认公海', '16360,16362', '16360,16362', '', 1, 0, NULL, 0, NULL, 0, NULL, 1, 0, '2019-06-30 18:13:08');

-- ----------------------------
-- Table structure for 72crm_crm_customer_pool_field_setting
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_customer_pool_field_setting`;
CREATE TABLE `72crm_crm_customer_pool_field_setting`  (
  `setting_id` int(11) NOT NULL AUTO_INCREMENT,
  `pool_id` int(11) NOT NULL COMMENT '公海id',
  `field_id` int(11) NULL DEFAULT NULL COMMENT '字段id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段中文名称',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `type` int(2) NOT NULL COMMENT '字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划',
  `is_hidden` int(2) NOT NULL DEFAULT 0 COMMENT '是否隐藏 0不隐藏 1隐藏',
  PRIMARY KEY (`setting_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 459953 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公海列表页字段设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_customer_pool_field_setting
-- ----------------------------
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459930, 35954, 1229736, '客户名称', 'customerName', 1, 0);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459932, 35954, 1229739, '电话', 'telephone', 1, 0);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459933, 35954, 1229740, '网址', 'website', 1, 0);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459934, 35954, 1229743, '下次联系时间', 'nextTime', 13, 0);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459935, 35954, 1229744, '备注', 'remark', 2, 0);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459936, 35954, 1229742, '客户级别', '客户级别', 3, 0);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459937, 35954, 1229737, '客户来源', '客户来源', 3, 0);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459938, 35954, 1229741, '客户行业', '客户行业', 3, 0);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459939, 35954, NULL, '成交状态', 'dealStatus', 3, 0);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459940, 35954, NULL, '最后跟进时间', 'lastTime', 4, 0);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459941, 35954, NULL, '更新时间', 'updateTime', 4, 0);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459942, 35954, NULL, '创建时间', 'createTime', 4, 0);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459943, 35954, NULL, '创建人', 'createUserName', 10, 0);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459945, 35954, 1229905, '客户属性', '客户属性', 9, 1);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459946, 35954, 1229937, '附件', '附件', 8, 1);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459947, 35954, 1229969, '客户类型', '客户类型', 1, 1);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459948, 35954, NULL, '最后跟进记录', 'lastContent', 2, 1);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459949, 35954, NULL, '详细地址', 'detailAddress', 1, 1);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459950, 35954, NULL, '地区定位', 'address', 18, 1);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459951, 35954, NULL, '前负责人', 'preOwnerUserName', 10, 1);
INSERT INTO `72crm_crm_customer_pool_field_setting` VALUES (459952, 35954, 1229971, '单行文本', '单行文本', 1, 1);

-- ----------------------------
-- Table structure for 72crm_crm_customer_pool_field_sort
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_customer_pool_field_sort`;
CREATE TABLE `72crm_crm_customer_pool_field_sort`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pool_id` int(11) NOT NULL COMMENT '公海id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `field_id` int(11) NULL DEFAULT NULL COMMENT '字段id',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段中文名称',
  `type` int(2) NOT NULL COMMENT '字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划',
  `sort` int(5) NOT NULL COMMENT '字段排序',
  `is_hidden` int(1) NOT NULL COMMENT '是否隐藏 0、不隐藏 1、隐藏',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公海列表页字段排序表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_customer_pool_field_sort
-- ----------------------------
INSERT INTO `72crm_crm_customer_pool_field_sort` VALUES (1, 35954, 16389, 1229736, 'customerName', '客户名称', 1, 0, 0);
INSERT INTO `72crm_crm_customer_pool_field_sort` VALUES (2, 35954, 16389, 1229739, 'telephone', '电话', 1, 1, 0);
INSERT INTO `72crm_crm_customer_pool_field_sort` VALUES (3, 35954, 16389, 1229740, 'website', '网址', 1, 2, 0);
INSERT INTO `72crm_crm_customer_pool_field_sort` VALUES (4, 35954, 16389, 1229743, 'nextTime', '下次联系时间', 13, 3, 0);
INSERT INTO `72crm_crm_customer_pool_field_sort` VALUES (5, 35954, 16389, 1229744, 'remark', '备注', 2, 4, 0);
INSERT INTO `72crm_crm_customer_pool_field_sort` VALUES (6, 35954, 16389, 1229742, '客户级别', '客户级别', 3, 5, 0);
INSERT INTO `72crm_crm_customer_pool_field_sort` VALUES (7, 35954, 16389, 1229737, '客户来源', '客户来源', 3, 6, 0);
INSERT INTO `72crm_crm_customer_pool_field_sort` VALUES (8, 35954, 16389, 1229741, '客户行业', '客户行业', 3, 7, 0);
INSERT INTO `72crm_crm_customer_pool_field_sort` VALUES (9, 35954, 16389, NULL, 'dealStatus', '成交状态', 3, 8, 0);
INSERT INTO `72crm_crm_customer_pool_field_sort` VALUES (10, 35954, 16389, NULL, 'lastTime', '最后跟进时间', 4, 9, 0);
INSERT INTO `72crm_crm_customer_pool_field_sort` VALUES (11, 35954, 16389, NULL, 'updateTime', '更新时间', 4, 10, 0);
INSERT INTO `72crm_crm_customer_pool_field_sort` VALUES (12, 35954, 16389, NULL, 'createTime', '创建时间', 4, 11, 0);
INSERT INTO `72crm_crm_customer_pool_field_sort` VALUES (13, 35954, 16389, NULL, 'createUserName', '创建人', 10, 12, 0);

-- ----------------------------
-- Table structure for 72crm_crm_customer_pool_field_style
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_customer_pool_field_style`;
CREATE TABLE `72crm_crm_customer_pool_field_style`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pool_id` int(11) NOT NULL COMMENT '公海id',
  `style` int(5) NOT NULL COMMENT '字段宽度',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公海列表页字段样式表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_customer_pool_relation
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_customer_pool_relation`;
CREATE TABLE `72crm_crm_customer_pool_relation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) NOT NULL COMMENT '客户id',
  `pool_id` int(11) NOT NULL COMMENT '公海id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `pool_id`(`pool_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客户公海关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_customer_pool_rule
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_customer_pool_rule`;
CREATE TABLE `72crm_crm_customer_pool_rule`  (
  `rule_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收回规则id',
  `pool_id` int(11) NOT NULL COMMENT '公海id',
  `type` int(2) NOT NULL COMMENT '收回规则判断类型 1跟进记录 2商机 3成交状态',
  `deal_handle` int(2) NULL DEFAULT NULL COMMENT '已成交客户是否进入公海 0不进入 1进入',
  `business_handle` int(2) NULL DEFAULT NULL COMMENT '有商机客户是否进入公海 0不进入 1进入',
  `customer_level_setting` int(2) NOT NULL COMMENT '客户级别设置 1全部 2根据级别分别设置',
  `level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户级别 1全部 2 A（重要客户）3 B（普通客户）4 C（非优先客户）',
  `limit_day` int(4) NOT NULL COMMENT '公海规则限制天数',
  PRIMARY KEY (`rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公海收回规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_customer_pool_rule
-- ----------------------------
INSERT INTO `72crm_crm_customer_pool_rule` VALUES (1, 35954, 1, 1, 1, 1, '所有客户', 100);

-- ----------------------------
-- Table structure for 72crm_crm_customer_setting
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_customer_setting`;
CREATE TABLE `72crm_crm_customer_setting`  (
  `setting_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `setting_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `customer_num` int(11) NULL DEFAULT NULL COMMENT '可拥有客户数量',
  `customer_deal` int(1) NULL DEFAULT 0 COMMENT '成交客户是否占用数量 0 不占用 1 占用',
  `type` int(1) NULL DEFAULT NULL COMMENT '类型 1 拥有客户数限制 2 锁定客户数限制',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`setting_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工拥有以及锁定客户数限制' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_customer_settinguser
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_customer_settinguser`;
CREATE TABLE `72crm_crm_customer_settinguser`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `setting_id` int(11) NOT NULL COMMENT '客户规则限制ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `type` int(1) NULL DEFAULT NULL COMMENT '1 员工 2 部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工拥有以及锁定客户员工关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_customer_user_star
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_customer_user_star`;
CREATE TABLE `72crm_crm_customer_user_star`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `customer_id` int(11) NOT NULL COMMENT '客户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户客户标星关系表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_instrument_sort
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_instrument_sort`;
CREATE TABLE `72crm_crm_instrument_sort`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `model_id` int(11) NOT NULL COMMENT '模块id 1、合同金额目标及完成情况 2、数据汇总 3、回款金额目标及完成情况 4、业绩指标完成率 5、销售漏斗 6、遗忘提醒 7、排行榜',
  `list` int(4) NOT NULL COMMENT '列 1左侧 2右侧',
  `sort` int(4) NOT NULL COMMENT '排序',
  `is_hidden` int(4) NOT NULL DEFAULT 0 COMMENT '是否隐藏 0显示 1隐藏',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仪表盘排序表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_instrument_sort
-- ----------------------------
INSERT INTO `72crm_crm_instrument_sort` VALUES (13, 16360, 6, 1, 0, 0);
INSERT INTO `72crm_crm_instrument_sort` VALUES (14, 16360, 5, 1, 1, 0);
INSERT INTO `72crm_crm_instrument_sort` VALUES (15, 16360, 4, 1, 2, 0);
INSERT INTO `72crm_crm_instrument_sort` VALUES (16, 16360, 1, 2, 0, 0);
INSERT INTO `72crm_crm_instrument_sort` VALUES (17, 16360, 2, 2, 1, 0);
INSERT INTO `72crm_crm_instrument_sort` VALUES (18, 16360, 7, 2, 2, 0);

-- ----------------------------
-- Table structure for 72crm_crm_invoice
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_invoice`;
CREATE TABLE `72crm_crm_invoice`  (
  `invoice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '发票id',
  `invoice_apply_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发票申请编号',
  `customer_id` int(11) NOT NULL COMMENT '客户id',
  `contract_id` int(11) NULL DEFAULT NULL COMMENT '合同id',
  `invoice_money` decimal(10, 2) NOT NULL COMMENT '开票金额',
  `invoice_date` date NULL DEFAULT NULL COMMENT '开票日期',
  `invoice_type` int(2) NOT NULL COMMENT '开票类型',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `title_type` int(2) NULL DEFAULT NULL COMMENT '抬头类型 1单位 2个人',
  `invoice_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开票抬头',
  `tax_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纳税识别号',
  `deposit_bank` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户行',
  `deposit_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户账户',
  `deposit_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开票地址',
  `telephone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `contacts_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人名称',
  `contacts_mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `contacts_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮寄地址',
  `examine_record_id` int(11) NULL DEFAULT NULL COMMENT '审批记录id',
  `check_status` int(2) NULL DEFAULT NULL COMMENT '审核状态 0待审核、1通过、2拒绝、3审核中、4撤回',
  `owner_user_id` bigint(20) NOT NULL COMMENT '负责人id',
  `invoice_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票号码',
  `real_invoice_date` date NULL DEFAULT NULL COMMENT '实际开票日期',
  `logistics_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `invoice_status` int(2) NULL DEFAULT NULL COMMENT '开票状态 0未开票 1已开票',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '批次id',
  PRIMARY KEY (`invoice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发票表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_invoice_info
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_invoice_info`;
CREATE TABLE `72crm_crm_invoice_info`  (
  `info_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '发票信息id',
  `customer_id` int(11) NOT NULL COMMENT '客户id',
  `title_type` int(2) NULL DEFAULT NULL COMMENT '抬头类型 1单位 2个人',
  `invoice_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开票抬头',
  `tax_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纳税识别号',
  `deposit_bank` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户行',
  `deposit_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户账户',
  `deposit_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开票地址',
  `telephone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发票信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_leads
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_leads`;
CREATE TABLE `72crm_crm_leads`  (
  `leads_id` int(11) NOT NULL AUTO_INCREMENT,
  `is_transform` int(1) NULL DEFAULT 0 COMMENT '1已转化 0 未转化',
  `followup` int(11) NULL DEFAULT NULL COMMENT '跟进状态 0未跟进1已跟进',
  `leads_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '线索名称',
  `customer_id` int(11) NULL DEFAULT NULL COMMENT '客户id',
  `next_time` datetime(0) NULL DEFAULT NULL COMMENT '下次联系时间',
  `telephone` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `mobile` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `address` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '地址',
  `remark` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次 比如附件批次',
  `last_time` datetime(0) NULL DEFAULT NULL COMMENT '最后跟进时间',
  `last_content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后一条跟进记录',
  PRIMARY KEY (`leads_id`) USING BTREE,
  INDEX `owner_user_id`(`owner_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '线索表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_leads_data
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_leads_data`;
CREATE TABLE `72crm_crm_leads_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`(191)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '线索自定义字段存值表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_leads_user_star
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_leads_user_star`;
CREATE TABLE `72crm_crm_leads_user_star`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `leads_id` int(11) NOT NULL COMMENT '客户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户线索标星关系表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_marketing
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_marketing`;
CREATE TABLE `72crm_crm_marketing`  (
  `marketing_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '营销id',
  `marketing_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '营销名称',
  `crm_type` int(1) NOT NULL DEFAULT 1 COMMENT '1线索  2客户',
  `end_time` datetime(0) NOT NULL COMMENT '截止时间',
  `relation_user_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关联人员ID',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '1启用  0禁用',
  `second` int(1) NOT NULL DEFAULT 0 COMMENT '每个客户只能填写次数 0 1',
  `field_data_id` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '营销内容填写字段',
  `browse` int(10) NULL DEFAULT 0 COMMENT '浏览数',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `share_num` int(11) NULL DEFAULT 0 COMMENT '分享数',
  `submit_num` int(11) NULL DEFAULT 0 COMMENT '提交数',
  `synopsis` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '简介',
  `main_file_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '首图id',
  `detail_file_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动地址',
  `marketing_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动类型',
  `marketing_money` decimal(11, 2) NULL DEFAULT NULL COMMENT '活动金额',
  PRIMARY KEY (`marketing_id`) USING BTREE,
  INDEX `status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '营销表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_marketing_info
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_marketing_info`;
CREATE TABLE `72crm_crm_marketing_info`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `marketing_id` int(11) NOT NULL COMMENT '关联ID',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '0未同步  1同步成功  2同步失败',
  `field_info` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '营销内容填写字段内容',
  `device` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设备号',
  `owner_user_id` bigint(20) NOT NULL COMMENT '关联ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '营销数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_owner_record
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_owner_record`;
CREATE TABLE `72crm_crm_owner_record`  (
  `record_id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) NOT NULL COMMENT '对象id',
  `type` int(4) NOT NULL COMMENT '对象类型',
  `pre_owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '上一负责人',
  `post_owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '接手负责人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '负责人变更记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_owner_record
-- ----------------------------
INSERT INTO `72crm_crm_owner_record` VALUES (1, 16, 9, 16398, NULL, '2020-12-17 16:59:05');
INSERT INTO `72crm_crm_owner_record` VALUES (2, 16, 9, NULL, 16389, '2020-12-17 17:10:47');
INSERT INTO `72crm_crm_owner_record` VALUES (3, 16, 9, 16389, NULL, '2020-12-17 17:10:59');
INSERT INTO `72crm_crm_owner_record` VALUES (4, 16, 9, NULL, 16389, '2020-12-17 17:11:11');
INSERT INTO `72crm_crm_owner_record` VALUES (5, 16, 9, 16389, NULL, '2020-12-17 17:12:09');
INSERT INTO `72crm_crm_owner_record` VALUES (6, 16, 9, NULL, 16389, '2020-12-17 17:12:13');
INSERT INTO `72crm_crm_owner_record` VALUES (7, 16, 9, 16389, NULL, '2020-12-17 17:12:24');
INSERT INTO `72crm_crm_owner_record` VALUES (8, 16, 9, NULL, 16389, '2020-12-17 17:12:28');
INSERT INTO `72crm_crm_owner_record` VALUES (9, 16, 9, 16389, NULL, '2020-12-17 17:12:53');
INSERT INTO `72crm_crm_owner_record` VALUES (10, 16, 9, NULL, 16389, '2020-12-17 17:12:57');
INSERT INTO `72crm_crm_owner_record` VALUES (11, 16, 9, 16389, NULL, '2020-12-17 17:13:58');
INSERT INTO `72crm_crm_owner_record` VALUES (12, 16, 9, NULL, 16389, '2020-12-17 17:14:09');
INSERT INTO `72crm_crm_owner_record` VALUES (13, 16, 9, 16389, NULL, '2020-12-17 17:58:09');
INSERT INTO `72crm_crm_owner_record` VALUES (14, 16, 9, NULL, 16389, '2020-12-17 17:58:13');

-- ----------------------------
-- Table structure for 72crm_crm_product
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_product`;
CREATE TABLE `72crm_crm_product`  (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品名称',
  `num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品编码',
  `unit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `price` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '价格',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态 1 上架 0 下架 3 删除',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '产品分类ID',
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '产品描述',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次',
  PRIMARY KEY (`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_product_category
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_product_category`;
CREATE TABLE `72crm_crm_product_category`  (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `pid` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16877 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_product_data
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_product_data`;
CREATE TABLE `72crm_crm_product_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`(191)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 142 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '产品自定义字段存值表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_product_detail_img
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_product_detail_img`;
CREATE TABLE `72crm_crm_product_detail_img`  (
  `img_id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NULL DEFAULT NULL COMMENT '产品id',
  `remarks` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `main_file_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主图',
  `detail_file_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`img_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '产品详情图片' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_product_user
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_product_user`;
CREATE TABLE `72crm_crm_product_user`  (
  `product_user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`product_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '产品员工小程序显示关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_receivables
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_receivables`;
CREATE TABLE `72crm_crm_receivables`  (
  `receivables_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '回款ID',
  `number` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回款编号',
  `plan_id` int(11) NULL DEFAULT NULL COMMENT '回款计划ID',
  `customer_id` int(11) NULL DEFAULT NULL COMMENT '客户ID',
  `contract_id` int(11) NULL DEFAULT NULL COMMENT '合同ID',
  `check_status` int(4) NULL DEFAULT NULL COMMENT '0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交',
  `examine_record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录ID',
  `return_time` date NULL DEFAULT NULL COMMENT '回款日期',
  `return_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回款方式',
  `money` decimal(17, 2) NULL DEFAULT NULL COMMENT '回款金额',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次',
  PRIMARY KEY (`receivables_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '回款表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_receivables_data
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_receivables_data`;
CREATE TABLE `72crm_crm_receivables_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`(191)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '回款自定义字段存值表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_receivables_plan
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_receivables_plan`;
CREATE TABLE `72crm_crm_receivables_plan`  (
  `plan_id` int(11) NOT NULL AUTO_INCREMENT,
  `num` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期数',
  `receivables_id` int(11) NULL DEFAULT NULL COMMENT '回款ID',
  `status` int(4) NULL DEFAULT NULL COMMENT '1完成 0 未完成',
  `money` decimal(18, 2) NULL DEFAULT NULL COMMENT '计划回款金额',
  `return_date` datetime(0) NULL DEFAULT NULL COMMENT '计划回款日期',
  `return_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计划回款方式',
  `remind` int(4) NULL DEFAULT NULL COMMENT '提前几天提醒',
  `remind_date` datetime(0) NULL DEFAULT NULL COMMENT '提醒日期',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `file_batch` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件批次ID',
  `contract_id` int(11) NOT NULL COMMENT '合同ID',
  `customer_id` int(11) NULL DEFAULT NULL COMMENT '客户ID',
  PRIMARY KEY (`plan_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '回款计划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_receivables_plan
-- ----------------------------
INSERT INTO `72crm_crm_receivables_plan` VALUES (1, '1', 8, NULL, 2222.00, '2020-12-17 00:00:00', '支票', NULL, NULL, '', 16398, NULL, '2020-12-17 14:44:51', '2020-12-17 14:50:19', 'bea4a9e800d44e02ad69fb63a03c5b4b', 18, 15);

-- ----------------------------
-- Table structure for 72crm_crm_return_visit
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_return_visit`;
CREATE TABLE `72crm_crm_return_visit`  (
  `visit_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '回访id',
  `visit_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '回访编号',
  `visit_time` datetime(0) NULL DEFAULT NULL COMMENT '回访时间',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '回访人id',
  `customer_id` int(11) NULL DEFAULT NULL COMMENT '客户id',
  `contract_id` int(11) NULL DEFAULT NULL COMMENT '合同id',
  `contacts_id` int(11) NULL DEFAULT NULL COMMENT '联系人id',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '批次id',
  PRIMARY KEY (`visit_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '回访表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_crm_return_visit_data
-- ----------------------------
DROP TABLE IF EXISTS `72crm_crm_return_visit_data`;
CREATE TABLE `72crm_crm_return_visit_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '回访信息自定义字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_crm_return_visit_data
-- ----------------------------
INSERT INTO `72crm_crm_return_visit_data` VALUES (1, 1230013, '回访日期时间', '2020-12-17 00:00:00', '2020-12-17 16:10:14', '73975986e8864fcebef78f271d9394d7');
INSERT INTO `72crm_crm_return_visit_data` VALUES (3, 1230015, '回访目的', '业务洽谈', '2020-12-17 16:10:14', '73975986e8864fcebef78f271d9394d7');
INSERT INTO `72crm_crm_return_visit_data` VALUES (4, 1230016, '回访详情描述', '主要洽淡事宜', '2020-12-17 16:10:14', '73975986e8864fcebef78f271d9394d7');
INSERT INTO `72crm_crm_return_visit_data` VALUES (5, 1230013, '回访日期时间', '2020-12-17 00:00:00', '2020-12-17 16:10:17', '00138d9cedeb4588971e9a4b4a643757');
INSERT INTO `72crm_crm_return_visit_data` VALUES (7, 1230015, '回访目的', '业务洽谈', '2020-12-17 16:10:17', '00138d9cedeb4588971e9a4b4a643757');
INSERT INTO `72crm_crm_return_visit_data` VALUES (8, 1230016, '回访详情描述', '主要洽淡事宜', '2020-12-17 16:10:17', '00138d9cedeb4588971e9a4b4a643757');
INSERT INTO `72crm_crm_return_visit_data` VALUES (9, 1230013, '回访日期时间', '2020-12-17 00:00:00', '2020-12-17 16:13:34', '2b8b8c2c271847ffb6632951fa87f6b0');
INSERT INTO `72crm_crm_return_visit_data` VALUES (11, 1230015, '回访目的', '侦查机试用', '2020-12-17 16:13:34', '2b8b8c2c271847ffb6632951fa87f6b0');
INSERT INTO `72crm_crm_return_visit_data` VALUES (12, 1230016, '回访详情描述', '回访客户', '2020-12-17 16:13:34', '2b8b8c2c271847ffb6632951fa87f6b0');
INSERT INTO `72crm_crm_return_visit_data` VALUES (13, 1230013, '回访日期时间', '2020-12-17 00:00:00', '2020-12-17 16:13:40', '826e5474bba04672a60c000733c3d9f9');
INSERT INTO `72crm_crm_return_visit_data` VALUES (15, 1230015, '回访目的', '侦查机试用', '2020-12-17 16:13:40', '826e5474bba04672a60c000733c3d9f9');
INSERT INTO `72crm_crm_return_visit_data` VALUES (16, 1230016, '回访详情描述', '回访客户', '2020-12-17 16:13:40', '826e5474bba04672a60c000733c3d9f9');
INSERT INTO `72crm_crm_return_visit_data` VALUES (17, 1230013, '回访日期时间', '2020-12-17 00:00:00', '2020-12-17 16:13:48', '54049f02c44b49fab8a91846d778ef47');
INSERT INTO `72crm_crm_return_visit_data` VALUES (19, 1230015, '回访目的', '侦查机试用', '2020-12-17 16:13:48', '54049f02c44b49fab8a91846d778ef47');
INSERT INTO `72crm_crm_return_visit_data` VALUES (20, 1230016, '回访详情描述', '回访客户', '2020-12-17 16:13:48', '54049f02c44b49fab8a91846d778ef47');
INSERT INTO `72crm_crm_return_visit_data` VALUES (21, 1230013, '回访日期时间', '2020-12-17 00:00:00', '2020-12-17 16:13:56', 'ee71c476303a4e7ba6f165febdbe93c0');
INSERT INTO `72crm_crm_return_visit_data` VALUES (23, 1230015, '回访目的', '侦查机试用', '2020-12-17 16:13:56', 'ee71c476303a4e7ba6f165febdbe93c0');
INSERT INTO `72crm_crm_return_visit_data` VALUES (24, 1230016, '回访详情描述', '回访客户', '2020-12-17 16:13:56', 'ee71c476303a4e7ba6f165febdbe93c0');
INSERT INTO `72crm_crm_return_visit_data` VALUES (25, 1230013, '回访日期时间', '2020-12-17 00:00:00', '2020-12-17 16:16:50', 'c7d11ceab4e7402d9c2b25497d453215');
INSERT INTO `72crm_crm_return_visit_data` VALUES (27, 1230015, '回访目的', '业务洽谈', '2020-12-17 16:16:50', 'c7d11ceab4e7402d9c2b25497d453215');
INSERT INTO `72crm_crm_return_visit_data` VALUES (28, 1230016, '回访详情描述', '', '2020-12-17 16:16:50', 'c7d11ceab4e7402d9c2b25497d453215');
INSERT INTO `72crm_crm_return_visit_data` VALUES (29, 1230013, '回访日期时间', '2020-12-17 00:00:00', '2020-12-17 16:17:27', '9e6a23ad35dd4a79a1a05e4cb7719a5a');
INSERT INTO `72crm_crm_return_visit_data` VALUES (31, 1230015, '回访目的', '业务洽谈', '2020-12-17 16:17:27', '9e6a23ad35dd4a79a1a05e4cb7719a5a');
INSERT INTO `72crm_crm_return_visit_data` VALUES (32, 1230016, '回访详情描述', '', '2020-12-17 16:17:27', '9e6a23ad35dd4a79a1a05e4cb7719a5a');
INSERT INTO `72crm_crm_return_visit_data` VALUES (33, 1230013, '回访日期时间', '', '2020-12-17 16:19:14', '407509004f3846e8843dec06e2401108');
INSERT INTO `72crm_crm_return_visit_data` VALUES (35, 1230015, '回访目的', '', '2020-12-17 16:19:14', '407509004f3846e8843dec06e2401108');
INSERT INTO `72crm_crm_return_visit_data` VALUES (36, 1230016, '回访详情描述', '', '2020-12-17 16:19:14', '407509004f3846e8843dec06e2401108');
INSERT INTO `72crm_crm_return_visit_data` VALUES (37, 1230013, '回访日期时间', '', '2020-12-17 16:19:15', '2bd4926e3a544b53b62e26c876fc1d25');
INSERT INTO `72crm_crm_return_visit_data` VALUES (39, 1230015, '回访目的', '', '2020-12-17 16:19:15', '2bd4926e3a544b53b62e26c876fc1d25');
INSERT INTO `72crm_crm_return_visit_data` VALUES (40, 1230016, '回访详情描述', '', '2020-12-17 16:19:15', '2bd4926e3a544b53b62e26c876fc1d25');

-- ----------------------------
-- Table structure for 72crm_email_account
-- ----------------------------
DROP TABLE IF EXISTS `72crm_email_account`;
CREATE TABLE `72crm_email_account`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱账号',
  `email_password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱密码',
  `send_nick` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送昵称',
  `configuration_mode` int(2) NULL DEFAULT 1 COMMENT '配置邮箱方式 1：自动配置 2：手动配置',
  `service_type` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收信服务类型 :POP3、IMAP',
  `receiving_server` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收信服务器地址',
  `is_receiving` int(2) NULL DEFAULT 1 COMMENT '收信服务器是否启用ssl 代理 0：未启用 1：已启用',
  `receiving_ssl` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收信服务器 SSL 端口',
  `smtp_server` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'SMTP服务器',
  `is_smtp` int(2) NULL DEFAULT 1 COMMENT 'smtp服务器是否启用ssl 代理 0：未启用 1：已启用',
  `smtp_ssl` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'smtp端口号',
  `signature` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '个性签名',
  `email_count` int(11) NULL DEFAULT NULL COMMENT '邮件总数量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮箱账号表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_email_file
-- ----------------------------
DROP TABLE IF EXISTS `72crm_email_file`;
CREATE TABLE `72crm_email_file`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `file_size` bigint(20) NULL DEFAULT NULL COMMENT '文件大小',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件批次ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮箱文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_email_lately
-- ----------------------------
DROP TABLE IF EXISTS `72crm_email_lately`;
CREATE TABLE `72crm_email_lately`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) NULL DEFAULT NULL COMMENT '客户id 为空时表示不是列表客户 不为空时表示客户',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮箱最近联系人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_email_record
-- ----------------------------
DROP TABLE IF EXISTS `72crm_email_record`;
CREATE TABLE `72crm_email_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email_account_id` int(11) NULL DEFAULT NULL COMMENT '发送账号id',
  `sender` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送人昵称',
  `sender_email` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '发送人邮箱',
  `receipt_name` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `receipt_emails` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '收件人邮箱集合 逗号分隔',
  `cc_name` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `cc_emails` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '抄送邮箱 逗号隔开',
  `theme` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主题',
  `attachment` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `email_type` int(2) NULL DEFAULT NULL COMMENT '邮箱类型 1：收件箱 2：发件箱 3：草稿箱 4：删除箱 5：垃圾箱',
  `is_read` int(2) NULL DEFAULT 0 COMMENT '是否已读取 0：未读 1：已读',
  `is_start` int(2) NULL DEFAULT 0 COMMENT '是否标星 0：未标星  1：已标星',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `record_type` int(2) NULL DEFAULT NULL COMMENT '记录类型 1：发送 2：保存草稿 3：删除邮件 4：星标邮件 5：垃圾邮件 ',
  `message_id` int(2) NULL DEFAULT NULL COMMENT '邮件id',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_del` int(2) NULL DEFAULT 0 COMMENT '0:未删除 1：已删除',
  `email_uid` bigint(32) NULL DEFAULT NULL COMMENT '邮件uid',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮箱记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_milestone
-- ----------------------------
DROP TABLE IF EXISTS `72crm_milestone`;
CREATE TABLE `72crm_milestone`  (
  `milestone_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '里程id',
  `milestone_num` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '里程碑编号',
  `milestone_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '里程名称',
  `work_id` int(11) NULL DEFAULT NULL COMMENT '项目id',
  `task_id` int(11) NULL DEFAULT NULL COMMENT '前置任务id',
  `degree` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '完成度',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `main_user_id` int(11) NULL DEFAULT NULL COMMENT '负责人id',
  `instructions` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '详细说明',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `work_num` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目编号',
  `work_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目名称',
  `create_user_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`milestone_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 118 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '里程碑表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_milestone
-- ----------------------------
INSERT INTO `72crm_milestone` VALUES (101, 'LCB-20201211-2', '11212', 1, NULL, '100', '2020-12-11 00:00:00', '2020-12-12 00:00:00', 16360, '1212', '2020-12-11 09:16:52', '2020-12-11 09:16:52', 'XM-20201210-1', '中科金审', NULL);
INSERT INTO `72crm_milestone` VALUES (104, 'LCB-20201230-1', '测试里程碑', 5, NULL, '30', '2020-12-30 00:00:00', '2020-12-31 00:00:00', 16389, '0.0', '2020-12-30 12:53:06', '2020-12-30 12:53:06', 'XM-20201229', '测试项目3', 16389);
INSERT INTO `72crm_milestone` VALUES (105, 'LCB-20201230-2', '测试', 3, NULL, '12', '2020-12-22 00:00:00', '2021-01-08 00:00:00', 16407, '测试', '2020-12-30 17:18:02', '2020-12-30 17:18:02', 'XM-20201217-1', '测试项目1', 16392);
INSERT INTO `72crm_milestone` VALUES (106, 'LCB-20201230-3', '线索研判阶段', 8, NULL, '0', '2020-12-01 00:00:00', '2020-12-31 00:00:00', 16407, '线索研判阶段与客户接触', '2020-12-30 19:53:34', '2020-12-30 19:53:34', 'XM-20201230-1', '华南分析服务项目', 16407);
INSERT INTO `72crm_milestone` VALUES (107, 'LCB-20201231-1', '销售商务洽谈阶段', 9, NULL, '10', '2020-12-31 00:00:00', '2021-01-01 00:00:00', 16389, '商务谈判', '2020-12-31 13:50:23', '2020-12-31 13:50:23', 'XM-20201231-1', '测试', 16360);
INSERT INTO `72crm_milestone` VALUES (108, 'LCB-20201231-2', '分析师进场阶段', 9, NULL, '0', '2020-12-31 00:00:00', '2021-01-01 00:00:00', 16389, '数据分析', '2020-12-31 14:40:01', '2020-12-31 15:21:57', 'XM-20201231-1', '测试', 16360);
INSERT INTO `72crm_milestone` VALUES (109, 'LCB-20201231-3', '分析阶段', 9, NULL, '10', '2020-12-30 00:00:00', '2020-12-31 00:00:00', 16389, '分析', '2020-12-31 15:26:14', '2020-12-31 15:26:14', 'XM-20201231-1', '测试', 16360);
INSERT INTO `72crm_milestone` VALUES (110, 'LCB-20201231-4', '落地阶段', 9, NULL, '0', '2020-12-30 00:00:00', '2020-12-31 00:00:00', 16389, '落地执行', '2020-12-31 16:02:20', '2020-12-31 16:02:20', 'XM-20201231-1', '协同执行流测试', 16360);
INSERT INTO `72crm_milestone` VALUES (111, 'LCB-20201231-5', '审计阶段', 9, NULL, '0', '2020-12-31 00:00:00', '2021-01-01 00:00:00', 16389, '审计需求', '2020-12-31 16:10:31', '2020-12-31 16:10:31', 'XM-20201231-1', '协同执行流测试', 16360);
INSERT INTO `72crm_milestone` VALUES (112, 'LCB-20201231-6', '分析师离场阶段', 9, NULL, '10', '2020-12-30 00:00:00', '2020-12-31 00:00:00', 16389, '分析师离场', '2020-12-31 16:29:25', '2020-12-31 16:29:25', 'XM-20201231-1', '协同执行流测试', 16360);
INSERT INTO `72crm_milestone` VALUES (113, 'LCB-20201231-7', '售前需求对接阶段', 9, NULL, '100', '2020-12-31 00:00:00', '2021-01-01 00:00:00', 16389, '售前需求对接', '2020-12-31 17:02:27', '2020-12-31 17:02:27', 'XM-20201231-1', '协同执行流测试', 16360);
INSERT INTO `72crm_milestone` VALUES (114, 'LCB-20201231-8', '销售报价阶段', 9, NULL, '100', '2020-12-31 00:00:00', '2021-01-01 00:00:00', 16389, '销售报价', '2020-12-31 17:02:48', '2020-12-31 17:02:48', 'XM-20201231-1', '协同执行流测试', 16360);
INSERT INTO `72crm_milestone` VALUES (115, 'LCB-20210105-1', '销售商务洽谈阶段', 10, NULL, '0', '2021-01-05 00:00:00', '2021-01-08 00:00:00', 16407, '测试', '2021-01-05 18:34:07', '2021-01-05 18:34:07', 'XM-20210105-1', '华南数据分析项目', 16392);
INSERT INTO `72crm_milestone` VALUES (116, 'LCB-20210105-2', '销售报价阶段-数据分析里程碑', 10, NULL, '0', '2021-01-05 00:00:00', '2021-01-19 00:00:00', 16407, '测试', '2021-01-05 19:04:22', '2021-01-05 19:04:22', 'XM-20210105-1', '华南数据分析项目', 16407);
INSERT INTO `72crm_milestone` VALUES (117, 'LCB-20210106-1', '测试', 6, NULL, '100', '2021-01-06 09:00:00', '2021-01-31 21:00:00', 16360, '测试', '2021-01-06 10:27:08', '2021-01-06 10:27:08', 'XM-20201229-1', '测试项目4', 16388);

-- ----------------------------
-- Table structure for 72crm_oa_announcement
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_announcement`;
CREATE TABLE `72crm_oa_announcement`  (
  `announcement_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `dept_ids` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '通知部门',
  `owner_user_ids` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '通知人',
  `read_user_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '已读用户',
  PRIMARY KEY (`announcement_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_oa_announcement
-- ----------------------------
INSERT INTO `72crm_oa_announcement` VALUES (1, '14', '14141', 16360, '2020-10-12 14:41:44', '2020-10-12 14:41:44', NULL, NULL, '', ',16360,', NULL);

-- ----------------------------
-- Table structure for 72crm_oa_calendar_type
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_calendar_type`;
CREATE TABLE `72crm_oa_calendar_type`  (
  `type_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日历类型id',
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型名称',
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色',
  `type` int(1) NULL DEFAULT NULL COMMENT '1 系统类型 2 自定义类型',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_user_id` bigint(20) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 89343 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日历类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_oa_calendar_type
-- ----------------------------
INSERT INTO `72crm_oa_calendar_type` VALUES (89338, '分配的任务', '1', 1, '2020-01-13 09:44:05', 16360, NULL);
INSERT INTO `72crm_oa_calendar_type` VALUES (89339, '需联系的客户', '2', 1, '2020-01-13 09:44:05', 16360, NULL);
INSERT INTO `72crm_oa_calendar_type` VALUES (89340, '即将到期的合同', '3', 1, '2020-01-13 09:44:05', 16360, NULL);
INSERT INTO `72crm_oa_calendar_type` VALUES (89341, '计划回款', '4', 1, '2020-01-13 09:44:05', 16360, NULL);
INSERT INTO `72crm_oa_calendar_type` VALUES (89342, '开会', '#53D397', 2, '2020-01-13 09:44:05', 16360, NULL);

-- ----------------------------
-- Table structure for 72crm_oa_calendar_type_user
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_calendar_type_user`;
CREATE TABLE `72crm_oa_calendar_type_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `type_id` int(11) NOT NULL COMMENT '日历类型id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户关联日历类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_oa_calendar_type_user
-- ----------------------------
INSERT INTO `72crm_oa_calendar_type_user` VALUES (21, 16360, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (22, 16360, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (23, 16360, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (24, 16360, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (25, 16360, 89342);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (26, 16364, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (27, 16364, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (28, 16364, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (29, 16364, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (30, 16364, 89342);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (31, 16373, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (32, 16373, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (33, 16373, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (34, 16373, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (35, 16373, 89342);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (36, 16376, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (37, 16376, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (38, 16376, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (39, 16376, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (40, 16376, 89342);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (41, 16375, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (42, 16375, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (43, 16375, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (44, 16375, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (45, 16375, 89342);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (46, 16378, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (47, 16378, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (48, 16378, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (49, 16378, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (50, 16378, 89342);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (51, 16383, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (52, 16383, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (53, 16383, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (54, 16383, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (55, 16383, 89342);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (56, 16379, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (57, 16379, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (58, 16379, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (59, 16379, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (60, 16379, 89342);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (61, 16384, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (62, 16384, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (63, 16384, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (64, 16384, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (65, 16384, 89342);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (66, 16362, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (67, 16362, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (68, 16362, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (69, 16362, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (70, 16362, 89342);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (71, 16388, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (72, 16388, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (73, 16388, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (74, 16388, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (75, 16388, 89342);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (76, 16392, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (77, 16392, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (78, 16392, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (79, 16392, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (80, 16392, 89342);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (81, 16389, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (82, 16389, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (83, 16389, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (84, 16389, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (85, 16389, 89342);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (86, 16404, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (87, 16404, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (88, 16404, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (89, 16404, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (90, 16404, 89342);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (91, 16399, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (92, 16399, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (93, 16399, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (94, 16399, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (95, 16399, 89342);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (96, 16398, 89338);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (97, 16398, 89339);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (98, 16398, 89340);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (99, 16398, 89341);
INSERT INTO `72crm_oa_calendar_type_user` VALUES (100, 16398, 89342);

-- ----------------------------
-- Table structure for 72crm_oa_event
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_event`;
CREATE TABLE `72crm_oa_event`  (
  `event_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '标题',
  `type_id` int(1) NOT NULL COMMENT '日程类型',
  `start_time` datetime(0) NOT NULL COMMENT '开始时间',
  `end_time` datetime(0) NOT NULL COMMENT '结束时间',
  `owner_user_ids` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参与人',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `repetition_type` int(1) NULL DEFAULT 1 COMMENT '重复类型 1从不重复 2每天 3每周 4每月 5每年',
  `repeat_rate` int(10) NULL DEFAULT NULL COMMENT '重复频率',
  `repeat_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '3:周/4:月',
  `end_type` int(1) NULL DEFAULT NULL COMMENT '结束类型 1从不 2重复次数 3结束日期',
  `end_type_config` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '2:次数/3:时间',
  `repeat_start_time` datetime(0) NOT NULL COMMENT '循环开始时间',
  `repeat_end_time` datetime(0) NULL DEFAULT NULL COMMENT '循环结束时间',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`event_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_oa_event_notice
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_event_notice`;
CREATE TABLE `72crm_oa_event_notice`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) NOT NULL COMMENT '日程ID',
  `type` int(1) NOT NULL COMMENT '1分钟 2小时 3天',
  `value` int(2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日程提醒设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_oa_event_notice
-- ----------------------------
INSERT INTO `72crm_oa_event_notice` VALUES (1, 1, 1, 5);

-- ----------------------------
-- Table structure for 72crm_oa_event_relation
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_event_relation`;
CREATE TABLE `72crm_oa_event_relation`  (
  `eventrelation_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日程关联业务表',
  `event_id` int(11) NOT NULL COMMENT '日程ID',
  `customer_ids` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户IDs',
  `contacts_ids` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人IDs',
  `business_ids` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商机IDs',
  `contract_ids` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同IDs',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`eventrelation_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日程关联业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_oa_event_update_record
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_event_update_record`;
CREATE TABLE `72crm_oa_event_update_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) NOT NULL,
  `time` bigint(20) NOT NULL COMMENT '标题',
  `status` int(1) NULL DEFAULT NULL COMMENT '1 删除本次 2 修改本次 3 修改本次及以后',
  `new_event_id` int(11) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '日程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_oa_examine
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_examine`;
CREATE TABLE `72crm_oa_examine`  (
  `examine_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL DEFAULT 1 COMMENT '审批类型',
  `content` varchar(800) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `remark` varchar(800) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `type_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请假类型',
  `money` decimal(18, 2) NULL DEFAULT NULL COMMENT '差旅、报销总金额',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `duration` decimal(18, 2) NULL DEFAULT NULL COMMENT '时长',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件批次id',
  PRIMARY KEY (`examine_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '审批表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_oa_examine_category
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_examine_category`;
CREATE TABLE `72crm_oa_examine_category`  (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `type` int(1) NULL DEFAULT 0 COMMENT '1 普通审批 2 请假审批 3 出差审批 4 加班审批 5 差旅报销 6 借款申请 0 自定义审批',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `status` int(1) NULL DEFAULT 1 COMMENT '1启用，0禁用',
  `is_sys` int(1) NULL DEFAULT NULL COMMENT '1为系统类型，不能删除',
  `examine_type` int(1) NULL DEFAULT NULL COMMENT '1固定2自选',
  `user_ids` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '可见范围（员工）',
  `dept_ids` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '可见范围（部门）',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(1) NULL DEFAULT 0 COMMENT '1已删除',
  `delete_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `delete_user_id` bigint(20) NULL DEFAULT NULL COMMENT '删除人ID',
  PRIMARY KEY (`category_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 81333 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '审批类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_oa_examine_category
-- ----------------------------
INSERT INTO `72crm_oa_examine_category` VALUES (81327, '普通审批', '普通审批', 'wk wk-l-record,#3ABCFB', 1, 3, 1, 1, 2, '', '', '2019-04-26 15:06:34', '2019-04-26 15:06:34', 0, NULL, NULL);
INSERT INTO `72crm_oa_examine_category` VALUES (81328, '请假审批', '请假审批', 'wk wk-leave,#00CAAB', 2, 3, 1, 1, 2, '', '', '2019-04-17 18:52:44', '2019-04-17 18:52:44', 0, NULL, NULL);
INSERT INTO `72crm_oa_examine_category` VALUES (81329, '出差审批', '出差审批', 'wk wk-trip,#3ABCFB', 3, 3, 1, 1, 2, '', '', '2019-04-17 18:52:50', '2019-04-17 18:52:50', 0, NULL, NULL);
INSERT INTO `72crm_oa_examine_category` VALUES (81330, '加班审批', '加班审批', 'wk wk-overtime,#FAAD14', 4, 3, 1, 1, 2, '', '', '2019-04-17 18:52:59', '2019-04-17 18:52:59', 0, NULL, NULL);
INSERT INTO `72crm_oa_examine_category` VALUES (81331, '差旅报销', '差旅报销', 'wk wk-reimbursement,#3ABCFB', 5, 3, 1, 1, 2, '', '', '2019-04-17 18:53:13', '2019-04-17 18:53:13', 0, NULL, NULL);
INSERT INTO `72crm_oa_examine_category` VALUES (81332, '借款申请', '借款申请', 'wk wk-go-out,#FF6033', 6, 3, 1, 1, 2, '', '', '2019-04-17 18:54:44', '2019-04-17 18:54:44', 0, NULL, NULL);

-- ----------------------------
-- Table structure for 72crm_oa_examine_data
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_examine_data`;
CREATE TABLE `72crm_oa_examine_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL COMMENT '字段id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'oa审批自定义字段存值表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_oa_examine_log
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_examine_log`;
CREATE TABLE `72crm_oa_examine_log`  (
  `log_id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `record_id` int(11) NULL DEFAULT NULL COMMENT '审批记录ID',
  `examine_step_id` bigint(20) NULL DEFAULT NULL COMMENT '审核步骤ID',
  `examine_status` int(1) NULL DEFAULT NULL COMMENT '审核状态 0 未审核 1 审核通过 2 审核拒绝4 撤回审核',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `examine_user` bigint(20) NULL DEFAULT NULL COMMENT '审核人',
  `examine_time` datetime(0) NULL DEFAULT NULL COMMENT '审核时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `is_recheck` int(1) NULL DEFAULT 0 COMMENT '是否是撤回之前的日志 0或者null为新数据 1：撤回之前的数据',
  `order_id` int(30) NULL DEFAULT NULL COMMENT '排序id',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审核日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_oa_examine_record
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_examine_record`;
CREATE TABLE `72crm_oa_examine_record`  (
  `record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '审核记录ID',
  `examine_id` int(11) NULL DEFAULT NULL COMMENT '审批ID',
  `examine_step_id` bigint(20) NULL DEFAULT NULL COMMENT '当前进行的审批步骤ID',
  `examine_status` int(1) NULL DEFAULT NULL COMMENT '审核状态 0 未审核 1 审核通过 2 审核拒绝 3 审核中 4 已撤回',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审核记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_oa_examine_relation
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_examine_relation`;
CREATE TABLE `72crm_oa_examine_relation`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '审批关联业务表',
  `examine_id` int(11) NULL DEFAULT NULL COMMENT '审批ID',
  `customer_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户IDs',
  `contacts_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人IDs',
  `business_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商机IDs',
  `contract_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同IDs',
  `status` int(2) NULL DEFAULT 1 COMMENT '状态1可用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '审批关联业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_oa_examine_step
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_examine_step`;
CREATE TABLE `72crm_oa_examine_step`  (
  `step_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `step_type` int(1) NULL DEFAULT NULL COMMENT '步骤类型1、负责人主管，2、指定用户（任意一人），3、指定用户（多人会签）,4、上一级审批人主管',
  `category_id` int(11) NOT NULL COMMENT '审批ID',
  `check_user_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批人ID (使用逗号隔开) ,1,2,',
  `step_num` int(2) NULL DEFAULT 1 COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`step_id`) USING BTREE,
  INDEX `category_id`(`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审批步骤表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_oa_examine_travel
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_examine_travel`;
CREATE TABLE `72crm_oa_examine_travel`  (
  `travel_id` int(11) NOT NULL AUTO_INCREMENT,
  `examine_id` int(11) NOT NULL COMMENT '审批ID',
  `start_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出发地',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '出发时间',
  `end_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的地',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '到达时间',
  `traffic` decimal(18, 2) NULL DEFAULT NULL COMMENT '交通费',
  `stay` decimal(18, 2) NULL DEFAULT NULL COMMENT '住宿费',
  `diet` decimal(18, 2) NULL DEFAULT NULL COMMENT '餐饮费',
  `other` decimal(18, 2) NULL DEFAULT NULL COMMENT '其他费用',
  `money` decimal(18, 2) NULL DEFAULT NULL COMMENT '金额',
  `vehicle` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交通工具',
  `trip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单程往返（单程、往返）',
  `duration` decimal(18, 2) NULL DEFAULT NULL COMMENT '时长',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次id',
  PRIMARY KEY (`travel_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '差旅行程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_oa_log
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_log`;
CREATE TABLE `72crm_oa_log`  (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_id` int(2) NOT NULL DEFAULT 1 COMMENT '日志类型（1日报，2周报，3月报）',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '日志内容',
  `tomorrow` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '明日工作内容',
  `question` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '遇到问题',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `send_user_ids` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通知人',
  `send_dept_ids` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通知部门',
  `read_user_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '已读人',
  `batch_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件批次ID',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '工作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_oa_log
-- ----------------------------
INSERT INTO `72crm_oa_log` VALUES (1, 1, NULL, '1111', '22222', '3333', 16362, '2020-11-06 10:41:15', NULL, '', '', ',,', 'de552ea7efb09754e60601c08d5888ba');
INSERT INTO `72crm_oa_log` VALUES (2, 1, NULL, '123', '123', '123', 16362, '2020-11-06 14:42:20', NULL, '', '', ',,', 'ae260525d994b6acb32ae6811e274488');
INSERT INTO `72crm_oa_log` VALUES (3, 1, NULL, '123', '123', '123', 16362, '2020-11-06 15:40:28', NULL, '', '', ',,', '8b71dd55f2d3cd88b678d4f3f65fbf4a');
INSERT INTO `72crm_oa_log` VALUES (4, 1, NULL, '字段内容', '字段内容', '字段内容', 16362, '2020-11-09 10:09:57', NULL, '', '', ',,', '029e03b8a91cddd5886996259b4c71a6');
INSERT INTO `72crm_oa_log` VALUES (6, 1, NULL, '1：跟进江苏客户，已对接\n2：完成南京合同签订', '出差南京', NULL, 16360, '2020-12-17 16:38:24', '2020-12-17 16:38:24', ',16360,16398,', '', ',,', '59221ea765c158f5cd9cd7341b24d2cc');
INSERT INTO `72crm_oa_log` VALUES (7, 1, NULL, 'CRM系统测试工作。', 'CRM继续测试。', '有一些问题', 16398, '2020-12-17 16:33:33', NULL, ',16360,', '', ',,', '6ca25b91b036c170c260ab39d208f292');
INSERT INTO `72crm_oa_log` VALUES (8, 1, NULL, 'admin', NULL, NULL, 16389, '2020-12-17 16:43:59', NULL, '', '', ',,', '1df5231aef14811aa1de7b38df555272');
INSERT INTO `72crm_oa_log` VALUES (9, 1, NULL, 'dddd', NULL, NULL, 16389, '2020-12-22 10:37:27', NULL, '', '', ',,', '8d6dd0264089744ed0f71575b9a24982');

-- ----------------------------
-- Table structure for 72crm_oa_log_bulletin
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_log_bulletin`;
CREATE TABLE `72crm_oa_log_bulletin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `log_id` int(11) NULL DEFAULT NULL COMMENT '日志ID',
  `type` int(1) NULL DEFAULT NULL COMMENT '关联类型 1 客户 2 商机 3 合同 4 回款 5 跟进记录 ',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '类型ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '工作日志与业务ID关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_oa_log_bulletin
-- ----------------------------
INSERT INTO `72crm_oa_log_bulletin` VALUES (1, 6, 2, 8, '2020-12-17 16:32:09');
INSERT INTO `72crm_oa_log_bulletin` VALUES (2, 6, 2, 9, '2020-12-17 16:32:09');
INSERT INTO `72crm_oa_log_bulletin` VALUES (3, 6, 3, 18, '2020-12-17 16:32:09');
INSERT INTO `72crm_oa_log_bulletin` VALUES (4, 6, 3, 19, '2020-12-17 16:32:09');
INSERT INTO `72crm_oa_log_bulletin` VALUES (5, 6, 4, 7, '2020-12-17 16:32:09');
INSERT INTO `72crm_oa_log_bulletin` VALUES (6, 6, 4, 8, '2020-12-17 16:32:09');
INSERT INTO `72crm_oa_log_bulletin` VALUES (7, 7, 1, 16, '2020-12-17 16:33:33');
INSERT INTO `72crm_oa_log_bulletin` VALUES (8, 8, 1, 17, '2020-12-17 16:43:59');
INSERT INTO `72crm_oa_log_bulletin` VALUES (9, 8, 1, 18, '2020-12-17 16:43:59');

-- ----------------------------
-- Table structure for 72crm_oa_log_record
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_log_record`;
CREATE TABLE `72crm_oa_log_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `log_id` int(11) NOT NULL,
  `customer_num` int(5) NULL DEFAULT 0 COMMENT '客户数量',
  `business_num` int(11) NULL DEFAULT 0 COMMENT '商机数量',
  `contract_num` int(11) NULL DEFAULT 0 COMMENT '合同数量',
  `receivables_money` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '回款金额',
  `activity_num` int(11) NULL DEFAULT 0 COMMENT '跟进记录',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志关联销售简报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_oa_log_record
-- ----------------------------
INSERT INTO `72crm_oa_log_record` VALUES (1, 1, 0, 0, 0, 0.00, 0, '2020-11-06 10:41:15');
INSERT INTO `72crm_oa_log_record` VALUES (2, 2, 0, 0, 0, 0.00, 0, '2020-11-06 14:42:20');
INSERT INTO `72crm_oa_log_record` VALUES (3, 3, 0, 0, 0, 0.00, 0, '2020-11-06 15:40:28');
INSERT INTO `72crm_oa_log_record` VALUES (4, 4, 0, 0, 0, 0.00, 0, '2020-11-09 10:09:57');
INSERT INTO `72crm_oa_log_record` VALUES (5, 5, 0, 0, 0, 0.00, 0, '2020-12-04 13:23:02');
INSERT INTO `72crm_oa_log_record` VALUES (6, 6, 0, 2, 2, 110000.00, 0, '2020-12-17 16:32:09');
INSERT INTO `72crm_oa_log_record` VALUES (7, 7, 1, 0, 0, 0.00, 0, '2020-12-17 16:33:33');
INSERT INTO `72crm_oa_log_record` VALUES (8, 8, 2, 0, 0, 0.00, 0, '2020-12-17 16:43:59');
INSERT INTO `72crm_oa_log_record` VALUES (9, 9, 0, 0, 0, 0.00, 0, '2020-12-22 10:37:27');

-- ----------------------------
-- Table structure for 72crm_oa_log_relation
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_log_relation`;
CREATE TABLE `72crm_oa_log_relation`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志关联业务表',
  `log_id` int(11) NULL DEFAULT NULL COMMENT '日志ID',
  `customer_ids` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户IDs',
  `contacts_ids` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人IDs',
  `business_ids` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商机IDs',
  `contract_ids` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同IDs',
  `status` int(2) NULL DEFAULT NULL COMMENT '状态1可用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日志关联业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_oa_log_relation
-- ----------------------------
INSERT INTO `72crm_oa_log_relation` VALUES (1, 1, '', '', '', '', NULL, '2020-11-06 10:41:16');
INSERT INTO `72crm_oa_log_relation` VALUES (2, 2, '', '', '', '', NULL, '2020-11-06 14:42:20');
INSERT INTO `72crm_oa_log_relation` VALUES (3, 3, '', '', '', '', NULL, '2020-11-06 15:40:28');
INSERT INTO `72crm_oa_log_relation` VALUES (4, 4, '', '', '', '', NULL, '2020-11-09 10:09:57');
INSERT INTO `72crm_oa_log_relation` VALUES (8, 7, '', '', '', '', NULL, '2020-12-17 16:33:33');
INSERT INTO `72crm_oa_log_relation` VALUES (9, 6, '', '', '', '', NULL, '2020-12-17 16:38:25');
INSERT INTO `72crm_oa_log_relation` VALUES (10, 8, '', '', '', '', NULL, '2020-12-17 16:43:59');
INSERT INTO `72crm_oa_log_relation` VALUES (11, 9, '', '', '', '', NULL, '2020-12-22 10:37:28');

-- ----------------------------
-- Table structure for 72crm_oa_log_rule
-- ----------------------------
DROP TABLE IF EXISTS `72crm_oa_log_rule`;
CREATE TABLE `72crm_oa_log_rule`  (
  `rule_id` int(11) NOT NULL AUTO_INCREMENT,
  `status` int(2) NOT NULL DEFAULT 1 COMMENT '状态 0停用 1启用',
  `member_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '需要提交的员工id，“,”分割',
  `type` int(2) NULL DEFAULT NULL COMMENT '日志类型 1日报 2周报 3月报',
  `effective_day` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '每周需要统计的日期 1-6是周一到周六 7是周日',
  `start_day` int(2) NULL DEFAULT NULL COMMENT '开始日期',
  `start_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开始时间',
  `end_day` int(2) NULL DEFAULT NULL COMMENT '结束日期',
  `end_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结束时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50491 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_oa_log_rule
-- ----------------------------
INSERT INTO `72crm_oa_log_rule` VALUES (50488, 0, '16360,16388,16389,16390,16391,16392,16393,16394,16395,16396,16397,16398,16399,16400,16401,16402,16403,16404,16405,16406,16407,16408,16409,16410', 1, '1,2,3,4,5,7,6', NULL, '00:00', NULL, '23:00', 0, '2020-03-28 08:53:52');
INSERT INTO `72crm_oa_log_rule` VALUES (50489, 0, NULL, 2, NULL, 1, NULL, 7, NULL, 0, '2020-03-28 08:53:52');
INSERT INTO `72crm_oa_log_rule` VALUES (50490, 0, NULL, 3, NULL, 1, NULL, 31, NULL, 0, '2020-03-28 08:53:52');

-- ----------------------------
-- Table structure for 72crm_sork_record
-- ----------------------------
DROP TABLE IF EXISTS `72crm_sork_record`;
CREATE TABLE `72crm_sork_record`  (
  `record_id` int(11) NOT NULL AUTO_INCREMENT,
  `work_id` int(11) NULL DEFAULT NULL COMMENT '项目ID',
  `record_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更项',
  `record_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更类型',
  `forward_time` datetime(0) NULL DEFAULT NULL COMMENT '提出时间',
  `record_content` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更原因',
  `record_plan` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更方案',
  `record_impact` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更影响',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目详情-变更记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_task
-- ----------------------------
DROP TABLE IF EXISTS `72crm_task`;
CREATE TABLE `72crm_task`  (
  `task_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务表',
  `task_num` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务编号',
  `work_id` int(11) NOT NULL DEFAULT 0 COMMENT '项目ID',
  `pid` int(11) NULL DEFAULT NULL COMMENT '上级任务ID',
  `before_id` int(11) NULL DEFAULT NULL COMMENT '前置任务ID',
  `milestone_id` int(11) NULL DEFAULT NULL COMMENT '里程碑ID',
  `class_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务类型分类id',
  `task_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务名称',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '任务完成状态 1正在进行2延期3归档 5结束',
  `completeness` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务完成度',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `main_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `owner_user_id` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '团队成员ID',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人ID',
  `priority` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '优先级 从大到小 3高 2中 1低 0无',
  `difficulty` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务难度 3高 2中 1低',
  `degree` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '重要程度 3高 2中 1低',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '描述',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '新建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `label_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签 ,号拼接',
  `is_top` int(2) NULL DEFAULT 0 COMMENT '工作台展示 0收件箱 1今天要做，2下一步要做，3以后要做',
  `is_open` int(2) NULL DEFAULT 1 COMMENT '是否公开',
  `order_num` int(4) NULL DEFAULT 999 COMMENT '排序ID',
  `top_order_num` int(4) NULL DEFAULT 999 COMMENT '我的任务排序ID',
  `archive_time` datetime(0) NULL DEFAULT NULL COMMENT '归档时间',
  `ishidden` int(2) NULL DEFAULT 0 COMMENT '是否删除 0 未删除 1 删除',
  `hidden_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `is_archive` int(2) NULL DEFAULT 0 COMMENT '1归档',
  PRIMARY KEY (`task_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 352 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_task_comment
-- ----------------------------
DROP TABLE IF EXISTS `72crm_task_comment`;
CREATE TABLE `72crm_task_comment`  (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论表',
  `user_id` bigint(20) NOT NULL COMMENT '评论人ID',
  `content` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容(答案)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '新建时间',
  `main_id` int(11) NULL DEFAULT 0 COMMENT '主评论的id',
  `pid` bigint(20) NULL DEFAULT 0 COMMENT '回复对象ID',
  `status` int(2) NULL DEFAULT 1 COMMENT '状态 ',
  `type_id` int(11) NULL DEFAULT 0 COMMENT '评论项目任务ID 或评论其他模块ID',
  `favour` int(7) NULL DEFAULT 0 COMMENT '赞',
  `type` int(2) NULL DEFAULT 0 COMMENT '评论分类 1：任务评论  2：日志评论',
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_task_data
-- ----------------------------
DROP TABLE IF EXISTS `72crm_task_data`;
CREATE TABLE `72crm_task_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NULL DEFAULT NULL COMMENT '字段表ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `value` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段值',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_task_file
-- ----------------------------
DROP TABLE IF EXISTS `72crm_task_file`;
CREATE TABLE `72crm_task_file`  (
  `file_id` int(11) NOT NULL AUTO_INCREMENT,
  `work_id` int(11) NULL DEFAULT NULL COMMENT '项目id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名称',
  `file_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件全名称',
  `file_attr` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资料属性(属于什么 例如：任务资料)',
  `main_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务状态',
  `task_id` int(11) NULL DEFAULT NULL COMMENT '主任务ID',
  `subtasks_id` int(11) NULL DEFAULT NULL COMMENT '子任务ID',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件真实路径',
  `file_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件路径',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `size` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件大小（字节）',
  `file_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'file' COMMENT '文件类型',
  `batch_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次id',
  `type` int(2) NULL DEFAULT NULL COMMENT '1:项目下任务资料  2：任务详情任务资料',
  PRIMARY KEY (`file_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 164 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务资料' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_task_hours
-- ----------------------------
DROP TABLE IF EXISTS `72crm_task_hours`;
CREATE TABLE `72crm_task_hours`  (
  `hours_id` int(11) NOT NULL AUTO_INCREMENT,
  `work_id` int(11) NULL DEFAULT NULL COMMENT '项目ID',
  `task_id` int(11) NULL DEFAULT NULL COMMENT '任务ID',
  `task_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主任务名称',
  `main_user_id` int(11) NULL DEFAULT NULL COMMENT '负责人ID',
  `subtasks_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子任务名称',
  `subtasks_id` int(11) NULL DEFAULT NULL COMMENT '子任务ID',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `working_hours` int(11) NULL DEFAULT NULL COMMENT '工时',
  `hours_type` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工时类型',
  `progress_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '进度内容',
  `create_user_id` int(11) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `examine_time` datetime(0) NULL DEFAULT NULL COMMENT '审批时间',
  `examine_record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录ID',
  `check_status` int(4) NULL DEFAULT NULL COMMENT '0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废',
  `check_user_id` int(11) NULL DEFAULT NULL COMMENT '审批人',
  `is_lock` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`hours_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务工时' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_task_hours_examine
-- ----------------------------
DROP TABLE IF EXISTS `72crm_task_hours_examine`;
CREATE TABLE `72crm_task_hours_examine`  (
  `id` int(11) NOT NULL,
  `examine_id` int(11) NULL DEFAULT NULL COMMENT '审批ID（例如项目的id等）',
  `check_user_id` int(11) NULL DEFAULT NULL COMMENT '审批人ID',
  `examine_status` int(2) NULL DEFAULT NULL COMMENT '审核状态 0 审核中 1 审核通过 2 审核拒绝 3 已撤回',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '工时审核表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_task_hours_lock
-- ----------------------------
DROP TABLE IF EXISTS `72crm_task_hours_lock`;
CREATE TABLE `72crm_task_hours_lock`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lock_time` datetime(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '工时锁定表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_task_hours_record
-- ----------------------------
DROP TABLE IF EXISTS `72crm_task_hours_record`;
CREATE TABLE `72crm_task_hours_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导出文件名称',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '导出人员',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '导出时间',
  `task_id` int(11) NULL DEFAULT NULL,
  `work_id` int(32) NULL DEFAULT NULL COMMENT '导出项目ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '工时导出记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_task_relation
-- ----------------------------
DROP TABLE IF EXISTS `72crm_task_relation`;
CREATE TABLE `72crm_task_relation`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务关联业务表',
  `task_id` int(11) NULL DEFAULT NULL COMMENT '任务ID',
  `customer_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户IDs',
  `contacts_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人IDs',
  `business_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商机IDs',
  `contract_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同IDs',
  `status` int(2) NULL DEFAULT NULL COMMENT '状态1可用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务关联业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work`;
CREATE TABLE `72crm_work`  (
  `work_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `work_num` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目编号',
  `work_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '项目名字',
  `work_phase` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目阶段',
  `area` int(11) NULL DEFAULT NULL COMMENT '区域',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `work_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目类型',
  `customer_id` int(11) NULL DEFAULT NULL COMMENT '关联客户id',
  `manager_id` int(11) NULL DEFAULT NULL COMMENT '项目经理id',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `sales_id` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '销售负责人',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `examine_record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录ID',
  `check_status` int(5) NULL DEFAULT 6 COMMENT '审核状态',
  `examine_time` datetime(0) NULL DEFAULT NULL COMMENT '审核通过时间',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次',
  `status` int(2) NOT NULL DEFAULT 1 COMMENT '状态 1启用 3归档 2 删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `description` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '描述',
  `color` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '颜色',
  `is_open` int(2) NOT NULL DEFAULT 1 COMMENT '是否所有人可见 1 是 0 否',
  `owner_role` int(2) NULL DEFAULT NULL COMMENT '公开项目成员角色id 1：可编辑  2：只读',
  `archive_time` datetime(0) NULL DEFAULT NULL COMMENT '归档时间',
  `delete_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `is_system_cover` int(2) NULL DEFAULT NULL COMMENT '是否是系统自带封面 0不是 1是',
  `cover_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目封面路径 仅系统自带封面需要',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `owner_user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `examine` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '立项评审',
  `solution` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方案对标',
  `risk` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '风险',
  `unscramble` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同解读',
  `target` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标',
  `contract_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同金额',
  `effective_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '有效金额',
  `implement_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '实施成本',
  `maintenance_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '运维成本',
  `budget` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预算',
  `practical_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '实际成本',
  `customer_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `customer_contact` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户联系人',
  `customer_calls` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户联系电话',
  `forecast_work` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '预估工作量',
  `label_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签',
  PRIMARY KEY (`work_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_change
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_change`;
CREATE TABLE `72crm_work_change`  (
  `change_id` int(11) NOT NULL AUTO_INCREMENT,
  `work_id` int(11) NULL DEFAULT NULL COMMENT '项目ID',
  `change_project` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更项',
  `change_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更类型 1计划变更 2需求变更 3成本变更 4人员变更',
  `forward_time` datetime(0) NULL DEFAULT NULL COMMENT '提出时间',
  `forward_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提出人员',
  `forward_why` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更原因',
  `forward_plan` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更方案',
  `forward_impact` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更影响',
  `forward_main_user_id` int(11) NULL DEFAULT NULL COMMENT '变更负责人',
  `owner_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参与人',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `instructions` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明',
  `is_through` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否通过 1通过 2未通过',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `batch_id` varchar(33) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次',
  `change_num` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更编号',
  PRIMARY KEY (`change_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '变更表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_change_user
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_change_user`;
CREATE TABLE `72crm_work_change_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `change_id` int(11) NULL DEFAULT NULL COMMENT '变更ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '成员ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '变更表关联人员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_collect
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_collect`;
CREATE TABLE `72crm_work_collect`  (
  `collect_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '项目收藏id',
  `work_id` int(11) NOT NULL COMMENT '项目id',
  `user_id` bigint(11) NOT NULL COMMENT '用户id',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`collect_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_config
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_config`;
CREATE TABLE `72crm_work_config`  (
  `config_id` int(11) NOT NULL AUTO_INCREMENT,
  `work_id` int(11) NULL DEFAULT NULL,
  `config_items` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `plan_time` datetime(0) NULL DEFAULT NULL,
  `main_user_id` int(11) NULL DEFAULT NULL,
  `create_user_id` int(11) NULL DEFAULT NULL,
  `associated_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联名字',
  `associated_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联边号',
  `associated_id` int(11) NULL DEFAULT NULL COMMENT '关联id',
  `version` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `version_time` datetime(0) NULL DEFAULT NULL,
  `version_describe` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `type` int(2) NULL DEFAULT NULL,
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '配置管理和配置计划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_data
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_data`;
CREATE TABLE `72crm_work_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NULL DEFAULT NULL COMMENT '字段表ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '字段值',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_demand
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_demand`;
CREATE TABLE `72crm_work_demand`  (
  `demand_id` int(11) NOT NULL AUTO_INCREMENT,
  `work_id` int(11) NULL DEFAULT NULL COMMENT '项目ID',
  `subsystem` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子系统',
  `child_module` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子模块',
  `function1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级功能',
  `function2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二级功能',
  `demand_describe` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '需求描述',
  `demand_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '需求类型',
  `demand_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '完成状态',
  `demand_version` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '需求版本号',
  `plan` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对标投标方案号',
  `demand_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对标需求设计序号',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `is_associated` int(2) NULL DEFAULT 0 COMMENT '是否存在关联',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`demand_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 202 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目详情-需求表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_demand_function
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_demand_function`;
CREATE TABLE `72crm_work_demand_function`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demand_id` int(11) NULL DEFAULT NULL COMMENT '需求ID',
  `function_id` int(11) NULL DEFAULT NULL COMMENT '功能ID',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '产品ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 148 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目关联功能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_demand_product
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_demand_product`;
CREATE TABLE `72crm_work_demand_product`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demand_id` int(11) NULL DEFAULT NULL COMMENT '需求ID',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '产品ID',
  `product_percentage` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对标百分比',
  `product_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对标描述情况',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 201 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '需求对标产品表关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_historical
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_historical`;
CREATE TABLE `72crm_work_historical`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operation_user_id` int(11) NULL DEFAULT NULL COMMENT '操作人ID',
  `change_time` datetime(0) NULL DEFAULT NULL COMMENT '历史变更时间',
  `module_one` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模块1',
  `work_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目名称',
  `module_two` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块2',
  `module_three` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块3',
  `behavior` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行为',
  `field` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `content_before` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容变更前',
  `content_after` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容变更后',
  `number` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '序号',
  `operation_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更对象id',
  `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `work_id` int(32) NULL DEFAULT NULL COMMENT '所属项目ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 71530 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目汇总历史变更记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_historical_record
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_historical_record`;
CREATE TABLE `72crm_work_historical_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operation_user_id` int(11) NULL DEFAULT NULL COMMENT '操作人ID',
  `change_time` datetime(0) NULL DEFAULT NULL COMMENT '历史变更时间',
  `module` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块',
  `behavior` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行为',
  `field` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `content_before` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容变更前',
  `content_after` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容变更后',
  `number` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '序号',
  `operation_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更id',
  `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `work_id` int(11) NULL DEFAULT NULL COMMENT '所属项目ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 999 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '历史变更记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_information
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_information`;
CREATE TABLE `72crm_work_information`  (
  `id` int(11) NOT NULL,
  `work_id` int(11) NULL DEFAULT NULL COMMENT '项目ID',
  `work_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目名称',
  `examine` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '立项评审',
  `work_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目类型',
  `solution` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方案对标',
  `risk` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '风险',
  `unscramble` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同解读',
  `target` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标',
  `contract_amount` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同金额',
  `effective_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '有效金额',
  `implement_cost` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实施成本',
  `maintenance_cost` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运维成本',
  `budget` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预算',
  `practical_cost` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实际成本',
  `customer_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `customer_contact` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户联系人',
  `Customer_calls` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户联系电话',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '立项信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_order
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_order`;
CREATE TABLE `72crm_work_order`  (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `work_id` int(11) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  `order_num` int(4) NOT NULL DEFAULT 999,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目排序表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_product
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_product`;
CREATE TABLE `72crm_work_product`  (
  `product_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '产品id',
  `product_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品编号',
  `product_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `product_version` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品版本',
  `product_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品类型',
  `main_user_id` int(11) NULL DEFAULT NULL COMMENT '产品负责人',
  `exploit_user_id` int(11) NULL DEFAULT NULL COMMENT '开发负责人',
  `product_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品状态',
  `release_time` datetime(0) NULL DEFAULT NULL COMMENT '发版时间',
  `version_describe` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '版本描述',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_product_function
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_product_function`;
CREATE TABLE `72crm_work_product_function`  (
  `function_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '功能ID',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '产品ID',
  `function_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能编号',
  `function_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能名称',
  `function_describe` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '功能描述',
  `product_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `product_version` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品版本',
  `main_user_id` int(11) NULL DEFAULT NULL COMMENT '产品负责人',
  `product_number` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品编号',
  `exploit_user_id` int(11) NULL DEFAULT NULL COMMENT '开发负责人',
  `product_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品状态 0:规划 1:发版',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`function_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品功能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_product_history
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_product_history`;
CREATE TABLE `72crm_work_product_history`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NULL DEFAULT NULL COMMENT '产品功能',
  `old_version` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品老版本',
  `new_version` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品新版本',
  `describe` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '版本描述',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品版本更新记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_resources_setting
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_resources_setting`;
CREATE TABLE `72crm_work_resources_setting`  (
  `resources_id` int(11) NOT NULL AUTO_INCREMENT,
  `work_id` int(11) NULL DEFAULT NULL COMMENT '项目ID',
  `resources_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源名称',
  `resources_describe` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源描述',
  `resources_provide` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源提供方',
  `main_user_id` int(11) NULL DEFAULT NULL COMMENT '负责人ID',
  `plan_time` datetime(0) NULL DEFAULT NULL COMMENT '计划时间',
  `actual_time` datetime(0) NULL DEFAULT NULL COMMENT '实际时间',
  `resources_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源状态 1：已到位   2：未到位',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`resources_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 116 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目详情-资源配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_risk
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_risk`;
CREATE TABLE `72crm_work_risk`  (
  `risk_id` int(11) NOT NULL AUTO_INCREMENT,
  `work_id` int(11) NULL DEFAULT NULL COMMENT '项目ID',
  `risk_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '风险类型 1：技术  2：管理  3：商业  4：社会  5：法律',
  `risk_level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '风险等级 1：低  2：中  3：高',
  `tracking_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '跟踪人员',
  `scope` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '影响范围',
  `solve_time` datetime(0) NULL DEFAULT NULL COMMENT '解决日期',
  `risk_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '风险状态 1：空  2：关闭',
  `solve_advice` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '解决建议',
  `solve_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '解决人员',
  `detailed` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细说明',
  `found_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发现人员',
  `found_time` datetime(0) NULL DEFAULT NULL COMMENT '发现日期',
  `found_phase` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发生阶段',
  `found_way` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发现方式',
  `direction` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方向',
  `batch_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `create_user_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`risk_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 204 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目详情-风险管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_task_class
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_task_class`;
CREATE TABLE `72crm_work_task_class`  (
  `class_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务分类表',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `status` int(2) NULL DEFAULT 0 COMMENT '状态1正常',
  `work_id` int(11) NULL DEFAULT NULL COMMENT '项目ID',
  `order_num` int(4) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`class_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_task_historical
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_task_historical`;
CREATE TABLE `72crm_work_task_historical`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operation_user_id` int(11) NULL DEFAULT NULL COMMENT '操作人ID',
  `change_time` datetime(0) NULL DEFAULT NULL COMMENT '历史变更时间',
  `module_one` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块1',
  `module_two` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块2',
  `behavior` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行为',
  `field` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `content_before` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容变更前',
  `content_after` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容变更后',
  `number` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '序号',
  `operation_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更id',
  `task_id` int(11) NULL DEFAULT NULL COMMENT '所属任务ID',
  `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `work_id` int(11) NULL DEFAULT NULL COMMENT '所属项目',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 659 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目汇总历史变更记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_task_label
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_task_label`;
CREATE TABLE `72crm_work_task_label`  (
  `label_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签名',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `status` int(2) NULL DEFAULT 0 COMMENT '状态',
  `color` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '颜色',
  PRIMARY KEY (`label_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of 72crm_work_task_label
-- ----------------------------
INSERT INTO `72crm_work_task_label` VALUES (8, '大型', '2020-11-02 14:00:59', 16371, 0, '#8983F3');
INSERT INTO `72crm_work_task_label` VALUES (9, '中型', '2020-11-02 14:01:10', 16371, 0, '#4586FF');
INSERT INTO `72crm_work_task_label` VALUES (10, '小型', '2020-11-02 14:01:18', 16371, 0, '#3498DB');
INSERT INTO `72crm_work_task_label` VALUES (12, '严重', '2020-11-02 14:06:53', 16371, 0, '#FF6699');
INSERT INTO `72crm_work_task_label` VALUES (13, '良', '2020-11-12 16:24:54', 16360, 0, '#53D397');
INSERT INTO `72crm_work_task_label` VALUES (14, '反洗钱', '2020-12-01 17:19:51', 16387, 0, '#53D397');

-- ----------------------------
-- Table structure for 72crm_work_task_label_order
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_task_label_order`;
CREATE TABLE `72crm_work_task_label_order`  (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `label_id` int(11) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  `order_num` int(4) NOT NULL DEFAULT 999,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目标签排序表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_task_log
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_task_log`;
CREATE TABLE `72crm_work_task_log`  (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '项目日志表',
  `user_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `status` int(2) NULL DEFAULT 0 COMMENT '状态 4删除',
  `task_id` int(11) NULL DEFAULT 0 COMMENT '任务ID',
  `work_id` int(11) NULL DEFAULT 0 COMMENT '项目ID',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_type
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_type`;
CREATE TABLE `72crm_work_type`  (
  `work_type_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '项目类型id',
  `type_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`work_type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_user
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_user`;
CREATE TABLE `72crm_work_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `work_id` int(11) NOT NULL COMMENT '项目ID',
  `user_id` bigint(20) NOT NULL COMMENT '成员ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 167 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目成员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 72crm_work_user_record
-- ----------------------------
DROP TABLE IF EXISTS `72crm_work_user_record`;
CREATE TABLE `72crm_work_user_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `work_id` int(11) NULL DEFAULT NULL COMMENT '项目id',
  `num_id` int(11) NOT NULL COMMENT '关联对象id',
  `user_id` bigint(20) NOT NULL COMMENT '成员ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  `type` int(2) NULL DEFAULT NULL COMMENT '关联类型，详情请查看WorkUserEnum.java',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 133796 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目成员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime(0) NOT NULL,
  `log_modified` datetime(0) NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50561
 Source Host           : localhost:3306
 Source Schema         : 360kao1

 Target Server Type    : MySQL
 Target Server Version : 50561
 File Encoding         : 65001

 Date: 08/04/2021 15:14:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for assessment_state
-- ----------------------------
DROP TABLE IF EXISTS `assessment_state`;
CREATE TABLE `assessment_state`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `State` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '1-手动考核  2-自动考核',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for attachment
-- ----------------------------
DROP TABLE IF EXISTS `attachment`;
CREATE TABLE `attachment`  (
  `AttachmentCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '附件编号',
  `MoneyCard` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发薪号',
  `FileName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件名称',
  `FilePath` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件保存路径',
  `Year` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年份',
  `Month` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '季度',
  `Depart` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门',
  PRIMARY KEY (`AttachmentCode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for attachment_copy1
-- ----------------------------
DROP TABLE IF EXISTS `attachment_copy1`;
CREATE TABLE `attachment_copy1`  (
  `AttachmentCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '附件编号',
  `MoneyCard` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发薪号',
  `FileName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件名称',
  `FilePath` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件保存路径',
  `Year` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年份',
  `Month` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '季度',
  `Depart` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门',
  PRIMARY KEY (`AttachmentCode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for authorization_user
-- ----------------------------
DROP TABLE IF EXISTS `authorization_user`;
CREATE TABLE `authorization_user`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Moneycard` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '发薪号',
  `UserName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `DepartmentName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `StartTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '开始时间',
  `EndTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '结束时间',
  `Flag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '3' COMMENT '代理标识  1-代理前 2-代理中 3-未授权  4-已完成',
  `IsPersonnel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '是否是人事部成员 1-否 2-是',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1003 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理用户表-人事部' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for branch
-- ----------------------------
DROP TABLE IF EXISTS `branch`;
CREATE TABLE `branch`  (
  `BranchCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `BranchName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `UpBranchCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `BranchDesc` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `FullBranchCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '级联支部字符串',
  PRIMARY KEY (`BranchCode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `DepartmentCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DepartmentName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `UpDepartmentCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `DepartmentDesc` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `FullDepartmentCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  PRIMARY KEY (`DepartmentCode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for dept_check
-- ----------------------------
DROP TABLE IF EXISTS `dept_check`;
CREATE TABLE `dept_check`  (
  `DeptCheckCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主键',
  `MoneyCard` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发薪号',
  `FileName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件名称',
  `FilePath` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件保存路径',
  `Year` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年份',
  `Depart` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for dept_complete
-- ----------------------------
DROP TABLE IF EXISTS `dept_complete`;
CREATE TABLE `dept_complete`  (
  `CompleteCode` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `MoneyCard` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发薪号',
  `DeptName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `ExcelComplete` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'excel表完成情况 2：未完成  1：已完成',
  `PdfCompleteYear` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人、分管院长上传分数签字pdf完成情况  2：未完成  1：已完成',
  `PdfCompleteMonth` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人上传分数签字pdf完成情况  2：未完成  1：已完成',
  `Complete` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '完成情况 2-未完成  1-已完成',
  `Year` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年份',
  `Month` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '季度',
  PRIMARY KEY (`CompleteCode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门完成情况' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for duty
-- ----------------------------
DROP TABLE IF EXISTS `duty`;
CREATE TABLE `duty`  (
  `DutyCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `StationCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `DutyName` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DutyType` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `OrderId` int(11) NULL DEFAULT NULL,
  `UpDutyCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `BScore` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `AScore` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `CScore` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `DScore` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `FullStationCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  PRIMARY KEY (`DutyCode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for evaluate_delete
-- ----------------------------
DROP TABLE IF EXISTS `evaluate_delete`;
CREATE TABLE `evaluate_delete`  (
  `EvaluateId` int(11) NOT NULL COMMENT '评估报告主键',
  PRIMARY KEY (`EvaluateId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '删除的用户相关的评估报告表主键' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for evaluation_branch_department
-- ----------------------------
DROP TABLE IF EXISTS `evaluation_branch_department`;
CREATE TABLE `evaluation_branch_department`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `department_id` int(11) NOT NULL,
  `branch_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 525 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for evaluation_clinical
-- ----------------------------
DROP TABLE IF EXISTS `evaluation_clinical`;
CREATE TABLE `evaluation_clinical`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `user_id` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发薪号',
  `sex` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `birth` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出生日期',
  `hire_date` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '聘用时间',
  `job_content` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作内容',
  `education_level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '教育程度',
  `current_position` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前职位',
  `political_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '政治面貌',
  `self_summary` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '自我总结',
  `dept_head_opinion` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '部门负责人意见',
  `branch_opinion` tinyint(1) NULL DEFAULT NULL COMMENT '党支部综合意见1-优秀 2-良好 3-一般 4-较差',
  `step` tinyint(1) NULL DEFAULT NULL COMMENT '填写步骤',
  `title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职称',
  `party_committees_id` int(11) NULL DEFAULT NULL COMMENT '党委id',
  `branch_id` int(11) NULL DEFAULT NULL COMMENT '党支部id',
  `general_branch_id` int(11) NULL DEFAULT NULL COMMENT '党总支id',
  `level` tinyint(2) NULL DEFAULT NULL,
  `score` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `year` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_index`(`user_id`) USING BTREE,
  INDEX `level_index`(`level`) USING BTREE,
  INDEX `step_index`(`step`) USING BTREE,
  INDEX `year_index`(`year`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6955 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for evaluation_clinical_score
-- ----------------------------
DROP TABLE IF EXISTS `evaluation_clinical_score`;
CREATE TABLE `evaluation_clinical_score`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发薪号',
  `item` int(2) NULL DEFAULT NULL COMMENT '条目名称',
  `self_score` int(2) NULL DEFAULT NULL COMMENT '自我评分',
  `head_score` int(2) NULL DEFAULT NULL COMMENT '主任评分',
  `branch_score` int(2) NULL DEFAULT NULL COMMENT '党委评分',
  `step` int(2) NULL DEFAULT NULL COMMENT '填写进度 0-初始化 1-自评完成 2-领导完成 3-党委完成',
  `question` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目',
  `max_score` int(2) NULL DEFAULT NULL COMMENT '题目最高分',
  `year` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年份',
  `self_submit_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自我评分人',
  `self_submit_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自我评分时间',
  `head_submit_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主任评分人',
  `head_submit_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主任评分时间',
  `branch_submit_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '书记评分人',
  `branch_submit_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '书记评分时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 90940 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for evaluation_department
-- ----------------------------
DROP TABLE IF EXISTS `evaluation_department`;
CREATE TABLE `evaluation_department`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `department_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名字',
  `director_user_id` int(11) NULL DEFAULT NULL COMMENT '主任发薪号',
  `department_order` int(255) NULL DEFAULT NULL COMMENT '排序',
  `secretary_user_id` int(30) NULL DEFAULT NULL COMMENT '书记发薪号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 351 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for evaluation_department_user
-- ----------------------------
DROP TABLE IF EXISTS `evaluation_department_user`;
CREATE TABLE `evaluation_department_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `department_id` int(11) NULL DEFAULT NULL COMMENT '部门id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_index`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8049 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for evaluation_non_clinical
-- ----------------------------
DROP TABLE IF EXISTS `evaluation_non_clinical`;
CREATE TABLE `evaluation_non_clinical`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `user_id` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发薪号',
  `sex` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `birth` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出生日期',
  `hire_date` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '聘用时间',
  `job_content` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作内容',
  `education_level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '教育程度',
  `current_position` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前职位',
  `political_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '政治面貌',
  `self_summary` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '自我总结',
  `dept_head_opinion` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '部门负责人意见',
  `branch_opinion` int(1) NULL DEFAULT NULL COMMENT '党支部综合意见1-优秀 2-良好 3-一般 4-较差',
  `step` int(1) NULL DEFAULT NULL COMMENT '填写步骤',
  `title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职称',
  `party_committees_id` int(11) NULL DEFAULT NULL COMMENT '党委id',
  `branch_id` int(11) NULL DEFAULT NULL COMMENT '党支部id',
  `general_branch_id` int(11) NULL DEFAULT NULL COMMENT '党总支id',
  `level` tinyint(2) NULL DEFAULT NULL COMMENT '等级',
  `score` int(255) NULL DEFAULT NULL COMMENT '得分',
  `year` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年份',
  `self_submit_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自我评分人',
  `self_submit_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自我评分时间',
  `head_submit_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主任评分人',
  `head_submit_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主任评分时间',
  `branch_submit_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '书记评分人',
  `branch_submit_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '书记评分时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_index`(`user_id`) USING BTREE,
  INDEX `level_index`(`level`) USING BTREE,
  INDEX `step_level`(`step`) USING BTREE,
  INDEX `year_index`(`year`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1235 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for evaluationreport
-- ----------------------------
DROP TABLE IF EXISTS `evaluationreport`;
CREATE TABLE `evaluationreport`  (
  `Id` int(4) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `UserCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户编码 ',
  `TotalScore` double(10, 2) NULL DEFAULT NULL COMMENT '总得分',
  `BasicScore` double(10, 2) NULL DEFAULT NULL COMMENT '基础得分',
  `KeyScore` double(10, 2) NULL DEFAULT NULL COMMENT '关键得分',
  `AvgScore` double(10, 2) NULL DEFAULT NULL COMMENT '整体平均分',
  `MaxScore` double(10, 2) NULL DEFAULT NULL COMMENT '最高分',
  `MinScore` double(10, 2) NULL DEFAULT NULL COMMENT '最低分',
  `ScoreDifference` double(10, 2) NULL DEFAULT NULL COMMENT '分值差',
  `TotalCompareLast` double(10, 2) NULL DEFAULT NULL COMMENT '总分较去年提升百分比',
  `TotalCompareMark` double(10, 2) NULL DEFAULT NULL COMMENT '总分较满分差距百分比',
  `Year` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年份',
  `Month` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '季度',
  `MaxCompareLast` double(10, 2) NULL DEFAULT NULL COMMENT '最高分较去年提升百分比',
  `MaxCompareMark` double(10, 2) NULL DEFAULT NULL COMMENT '最高分较满分差距百分比',
  `MinCompareLast` double(10, 2) NULL DEFAULT NULL COMMENT '最低分较去年提升百分比',
  `MinCompareMark` double(10, 2) NULL DEFAULT NULL COMMENT '最低分较满分差距百分比',
  `Plan` double(10, 2) NULL DEFAULT NULL COMMENT '进度',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1382 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for excel_log
-- ----------------------------
DROP TABLE IF EXISTS `excel_log`;
CREATE TABLE `excel_log`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MoneyCard` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '上传人发薪号',
  `OperateTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作时间',
  `Year` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '年份',
  `Month` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '季度',
  `SavePath` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '附件保存地址',
  `DepartmentName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门名称',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 288 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上传分数excel日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for h_user
-- ----------------------------
DROP TABLE IF EXISTS `h_user`;
CREATE TABLE `h_user`  (
  `u_id` char(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发薪号',
  `u_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '123456' COMMENT '密码',
  `u_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `u_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户图片（头像）',
  `u_sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `u_birth` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出生日期',
  `u_home_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `u_nation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '民族',
  `u_native_place` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '籍贯',
  `u_id_card` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证',
  `u_old_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '老发薪号',
  `u_job_time` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参加工作时间',
  `u_hospital_time` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来医院工作时间',
  `u_job_number` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工号',
  `u_email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `u_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `u_title_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职称级别',
  `u_technical_position1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专业技术职务1',
  `u_position_time1` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务1时间',
  `u_technical_position2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专业技术职务2',
  `u_position_time2` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务2时间',
  `u_employment_unit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '聘任单位',
  `u_check_department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '核对科室',
  `u_statistic_department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统计科室',
  `u_his_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'HIS部门编号',
  `u_update_status` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新状态（2为更新，1为新增，-1为删除，其它为未改变）',
  `u_activate_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '激活状态(1已激活，其它为失败)',
  `u_check_data_status` tinyint(1) NULL DEFAULT 0 COMMENT '用户资料审核状态（0为未提交审核,,1为已提交待审核，2为已审核）',
  `u_tel` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '办公电话',
  `u_degree` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学位',
  `u_edu` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学历',
  PRIMARY KEY (`u_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for history_score
-- ----------------------------
DROP TABLE IF EXISTS `history_score`;
CREATE TABLE `history_score`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `UserName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `MoneyCard` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发薪号',
  `DepartmentName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `StationName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '岗位名称',
  `StationCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '岗位编号',
  `RoleCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `ScoreStatus` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评分状态',
  `State` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '季节状态',
  `Month` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '季度',
  `Year` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年份',
  `AScore` double(11, 2) NULL DEFAULT NULL COMMENT 'A类平均分',
  `BScore` double(11, 2) NULL DEFAULT NULL COMMENT 'B类平均分',
  `CScore` double(11, 2) NULL DEFAULT NULL COMMENT 'C类平均分',
  `DScore` double(11, 2) NULL DEFAULT NULL COMMENT 'D类平均分',
  `TotalScore` double(11, 2) NULL DEFAULT NULL COMMENT '总分',
  `UserCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36559 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for hrp_user
-- ----------------------------
DROP TABLE IF EXISTS `hrp_user`;
CREATE TABLE `hrp_user`  (
  `u_id` char(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发薪号',
  `u_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '123456' COMMENT '密码',
  `u_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `u_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户图片（头像）',
  `u_sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `u_birth` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出生日期',
  `u_home_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `u_nation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '民族',
  `u_native_place` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '籍贯',
  `u_id_card` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证',
  `u_old_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '老发薪号',
  `u_job_time` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参加工作时间',
  `u_hospital_time` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来医院工作时间',
  `u_job_number` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工号',
  `u_email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `u_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `u_title_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职称级别',
  `u_technical_position1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专业技术职务1',
  `u_position_time1` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务1时间',
  `u_technical_position2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专业技术职务2',
  `u_position_time2` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务2时间',
  `u_employment_unit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '聘任单位',
  `u_check_department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '核对科室',
  `u_statistic_department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统计科室',
  `u_his_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'HIS部门编号',
  `u_update_status` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新状态（2为更新，1为新增，-1为删除，其它为未改变）',
  `u_activate_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '激活状态(1已激活，其它为失败)',
  `u_check_data_status` tinyint(1) NULL DEFAULT 0 COMMENT '用户资料审核状态（0为未提交审核,,1为已提交待审核，2为已审核）',
  `u_tel` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '办公电话',
  `u_degree` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学位',
  `u_edu` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学历',
  PRIMARY KEY (`u_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for hrp_user_old
-- ----------------------------
DROP TABLE IF EXISTS `hrp_user_old`;
CREATE TABLE `hrp_user_old`  (
  `u_id` char(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发薪号',
  `u_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '123456' COMMENT '密码',
  `u_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `u_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户图片（头像）',
  `u_sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `u_birth` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出生日期',
  `u_home_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `u_nation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '民族',
  `u_native_place` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '籍贯',
  `u_id_card` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证',
  `u_old_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '老发薪号',
  `u_job_time` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参加工作时间',
  `u_hospital_time` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来医院工作时间',
  `u_job_number` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工号',
  `u_email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `u_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `u_title_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职称级别',
  `u_technical_position1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专业技术职务1',
  `u_position_time1` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务1时间',
  `u_technical_position2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专业技术职务2',
  `u_position_time2` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务2时间',
  `u_employment_unit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '聘任单位',
  `u_check_department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '核对科室',
  `u_statistic_department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统计科室',
  `u_his_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'HIS部门编号',
  `u_update_status` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新状态（2为更新，1为新增，-1为删除，其它为未改变）',
  `u_activate_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '激活状态(1已激活，其它为失败)',
  `u_check_data_status` tinyint(1) NULL DEFAULT 0 COMMENT '用户资料审核状态（0为未提交审核,,1为已提交待审核，2为已审核）',
  `u_tel` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '办公电话',
  `u_degree` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学位',
  `u_edu` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学历',
  PRIMARY KEY (`u_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for logintips
-- ----------------------------
DROP TABLE IF EXISTS `logintips`;
CREATE TABLE `logintips`  (
  `LoginInfo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '提示信息',
  `InportInfo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评分导入提示信息',
  `LoginType` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '0:不开启提示    1:开启提示',
  `RoleCode` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色编码',
  `InportLogin` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评分导入开关提示 0:不开启提示    1:开启提示'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for manual_set_time
-- ----------------------------
DROP TABLE IF EXISTS `manual_set_time`;
CREATE TABLE `manual_set_time`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Year` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '年份',
  `Month` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '季度',
  `Time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '手动设置时间',
  `CreateTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建时间',
  `UpdateTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改时间',
  `CreateMoneyCard` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人发薪号',
  `UpdateMoneyCard` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改人发薪号',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for medical_ethics_dic
-- ----------------------------
DROP TABLE IF EXISTS `medical_ethics_dic`;
CREATE TABLE `medical_ethics_dic`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dic_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型编码',
  `dic_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典编码',
  `dic_value` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典中文值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for medical_ethics_msg
-- ----------------------------
DROP TABLE IF EXISTS `medical_ethics_msg`;
CREATE TABLE `medical_ethics_msg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '发薪号',
  `party_committees_id` int(11) NULL DEFAULT NULL COMMENT '党委id',
  `general_branch_id` int(11) NULL DEFAULT NULL COMMENT '党总支id',
  `branch_id` int(11) NULL DEFAULT NULL COMMENT '党支部id',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `is_delete` int(11) NOT NULL DEFAULT 0 COMMENT '删除状态：0-未删除；1-已删除',
  `person_type` int(2) NULL DEFAULT NULL COMMENT '0-临床  1-非临床',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_index`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8640 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for medical_ethics_msg_temp
-- ----------------------------
DROP TABLE IF EXISTS `medical_ethics_msg_temp`;
CREATE TABLE `medical_ethics_msg_temp`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '发薪号',
  `party_committees_id` int(11) NULL DEFAULT NULL COMMENT '党委id',
  `general_branch_id` int(11) NULL DEFAULT NULL COMMENT '党总支id',
  `branch_id` int(11) NULL DEFAULT NULL COMMENT '党支部id',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `status` int(11) NULL DEFAULT 0 COMMENT '提交状态：0-未提交；1-已提交',
  `person_type` int(11) NULL DEFAULT 0 COMMENT '人员类型：0-医务人员，1-非临床人员',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9655 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for medical_ethics_role_info
-- ----------------------------
DROP TABLE IF EXISTS `medical_ethics_role_info`;
CREATE TABLE `medical_ethics_role_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '角色代号：100-管理员；101-书记；200-普通用户',
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '角色名',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for medical_ethics_user
-- ----------------------------
DROP TABLE IF EXISTS `medical_ethics_user`;
CREATE TABLE `medical_ethics_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '发薪号',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `sex` int(1) NULL DEFAULT 1 COMMENT '0-女；1-男',
  `party_committees_id` int(11) NULL DEFAULT NULL COMMENT '党委id',
  `general_branch_id` int(11) NULL DEFAULT 0 COMMENT '党总支id',
  `branch_id` int(11) NULL DEFAULT 0 COMMENT '党支部id',
  `id_card` varchar(18) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `political_status` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '政治面貌',
  `education_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '文化程度',
  `current_position` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '现聘职位',
  `hire_date` date NULL DEFAULT NULL COMMENT '聘用日期',
  `job_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工作内容',
  `is_delete` int(11) NOT NULL DEFAULT 0 COMMENT '删除状态：0-未删除；1-已删除',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_index`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9416 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for medical_ethics_user_role
-- ----------------------------
DROP TABLE IF EXISTS `medical_ethics_user_role`;
CREATE TABLE `medical_ethics_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '发薪号',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `party_id` int(11) NULL DEFAULT NULL COMMENT '角色所在的部门id',
  `party_level` int(255) NULL DEFAULT NULL,
  `department_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_index`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10659 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for message_log
-- ----------------------------
DROP TABLE IF EXISTS `message_log`;
CREATE TABLE `message_log`  (
  `LogCode` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键 ',
  `ReceiverCode` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接收人编码',
  `MessageContent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '短信内容',
  `SenderCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送人编码',
  `ReceiveTime` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接收时间',
  PRIMARY KEY (`LogCode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '短信日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for message_template
-- ----------------------------
DROP TABLE IF EXISTS `message_template`;
CREATE TABLE `message_template`  (
  `TemplateCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主键',
  `TemplateName` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `TemplateContent` varchar(12000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板内容',
  `Flag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1-组织部  2-人事部'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for monthsummary
-- ----------------------------
DROP TABLE IF EXISTS `monthsummary`;
CREATE TABLE `monthsummary`  (
  `SerialNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `Title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `EmployeeCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `Pubdate` datetime NULL DEFAULT NULL,
  `Content` varchar(21000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `Year` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `Month` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `FormatText` blob NULL,
  `State` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `SavePath` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `FileName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `IsSend` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否已发送  0:否  1:是',
  `ScoreStatus` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '打分状态 1：未评分  2:未完成  3：已完成',
  PRIMARY KEY (`SerialNo`) USING BTREE,
  INDEX `index_employeecode`(`EmployeeCode`, `Year`, `Month`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for party_branch_relations
-- ----------------------------
DROP TABLE IF EXISTS `party_branch_relations`;
CREATE TABLE `party_branch_relations`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `relations_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '关系名',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父id',
  `hospital_district` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '院区',
  `person_type` int(11) NULL DEFAULT 0 COMMENT '人员类型：0-医务人员，1-非临床人员',
  `is_delete` int(11) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否；1-是',
  `leader_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `leader_user_id` int(11) NULL DEFAULT NULL,
  `level` int(255) NULL DEFAULT NULL,
  `excellent_percent` int(255) NULL DEFAULT NULL,
  `current_excellent_percent` double(255, 0) NULL DEFAULT NULL,
  `director_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `director_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 312 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for personnel_authorization
-- ----------------------------
DROP TABLE IF EXISTS `personnel_authorization`;
CREATE TABLE `personnel_authorization`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Agent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理人发薪号',
  `AgentUserName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理人姓名',
  `DeptUser` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门长发薪号',
  `DeptUserName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门长姓名',
  `StartTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始时间',
  `EndTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结束时间',
  `Flag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理标识  1-代理前 2-代理中 3-停止代理 4-已完成',
  `DepartmentName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `IsPersonnel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否是人事处 1-否 2-是',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '授权日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for personnel_scorring
-- ----------------------------
DROP TABLE IF EXISTS `personnel_scorring`;
CREATE TABLE `personnel_scorring`  (
  `UserCode` int(20) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `Moneycard` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发薪号',
  `UserName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `DepartmentName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `Score` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分数',
  `Ranks` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '季度绩效平均等次',
  `Year` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年份',
  `Month` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '季度',
  `Notes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`UserCode`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3607 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '人事评分表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for personnel_user
-- ----------------------------
DROP TABLE IF EXISTS `personnel_user`;
CREATE TABLE `personnel_user`  (
  `Moneycard` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '发薪号',
  `UserName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `DepartmentName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `StartTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '开始时间',
  `EndTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结束时间',
  `Flag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '3' COMMENT '代理标识  1-代理前 2-代理中 3-停止代理',
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 792 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rater
-- ----------------------------
DROP TABLE IF EXISTS `rater`;
CREATE TABLE `rater`  (
  `RaterCode` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户编号',
  `ScorringCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职员代码',
  `ScorringName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `LeaderPhone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主任电话号码',
  `Department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科室',
  `Remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `Phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `Remarks2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注2',
  `Flag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否是代理人标识  0-不是  1-是',
  PRIMARY KEY (`RaterCode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '人事部评分人员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for resultdetail
-- ----------------------------
DROP TABLE IF EXISTS `resultdetail`;
CREATE TABLE `resultdetail`  (
  `Id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ResultReportCode` int(10) NULL DEFAULT NULL COMMENT '综合结果编码',
  `DutyName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目',
  `AScore` double(255, 2) NULL DEFAULT NULL COMMENT 'A类评分',
  `BScore` double(255, 2) NULL DEFAULT NULL COMMENT 'B类评分',
  `CScore` double(255, 2) NULL DEFAULT NULL COMMENT 'C类评分',
  `DScore` double(255, 2) NULL DEFAULT NULL COMMENT 'D类评分',
  `Score` double(255, 2) NULL DEFAULT NULL COMMENT '个人得分',
  `AvgScore` double(255, 2) NULL DEFAULT NULL COMMENT '总体平均分',
  `OrderId` int(2) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2698 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for resultreport
-- ----------------------------
DROP TABLE IF EXISTS `resultreport`;
CREATE TABLE `resultreport`  (
  `Id` int(4) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ResultReportCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '维度编号  0:基础评分  1:关键评分',
  `ResultReportName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `EvaluationReportCode` int(4) NULL DEFAULT NULL COMMENT '测评报告编码',
  `AScore` double(11, 2) NULL DEFAULT NULL COMMENT 'A类评分',
  `BScore` double(11, 2) NULL DEFAULT NULL COMMENT 'B类评分',
  `CScore` double(11, 2) NULL DEFAULT NULL COMMENT 'C类评分',
  `DScore` double(11, 2) NULL DEFAULT NULL COMMENT 'D类评分',
  `Score` double(11, 2) NULL DEFAULT NULL COMMENT '个人得分',
  `AvgScore` double(11, 2) NULL DEFAULT NULL COMMENT '总体平均分',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1273 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `RoleCode` varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `RoleName` varchar(100) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `RoleDescription` varchar(100) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  PRIMARY KEY (`RoleCode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score`  (
  `ScorringCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ScorredCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ScoreType` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `Id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `index_scorring`(`ScorringCode`) USING BTREE,
  INDEX `index_scorred`(`ScorredCode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for scoredetail
-- ----------------------------
DROP TABLE IF EXISTS `scoredetail`;
CREATE TABLE `scoredetail`  (
  `SerialNo` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `FSerialNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'ScoreFlow表的id',
  `DSerialNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `Score` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  PRIMARY KEY (`SerialNo`) USING BTREE,
  INDEX `index_fserialno`(`FSerialNo`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for scoreflow
-- ----------------------------
DROP TABLE IF EXISTS `scoreflow`;
CREATE TABLE `scoreflow`  (
  `SerialNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `MSerialNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `ScoredCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ScorringCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ScoreDate` date NULL DEFAULT NULL,
  `ScoreType` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `State` int(2) NULL DEFAULT NULL,
  `Score` double NULL DEFAULT 0,
  `Ratio` double NULL DEFAULT 0,
  PRIMARY KEY (`SerialNo`) USING BTREE,
  INDEX `index_mserialno`(`MSerialNo`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for scorehistory
-- ----------------------------
DROP TABLE IF EXISTS `scorehistory`;
CREATE TABLE `scorehistory`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `UserCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户编号',
  `Year` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年份',
  `Month` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '季度',
  `ScoreStatus` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '打分状态 1-未评分 2-未完成  3-已完成',
  `AScore` double(11, 2) NULL DEFAULT 0.00,
  `BScore` double(11, 2) NULL DEFAULT 0.00,
  `CScore` double(11, 2) NULL DEFAULT 0.00,
  `DScore` double(11, 2) NULL DEFAULT 0.00,
  `TotalScore` double(11, 2) NULL DEFAULT 0.00,
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `index-scorehistory`(`UserCode`, `Year`, `Month`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7344 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for station
-- ----------------------------
DROP TABLE IF EXISTS `station`;
CREATE TABLE `station`  (
  `StationCode` varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `StationName` varchar(100) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `DepartmentCode` varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `StationDesc` varchar(2000) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `DutyDesc` varchar(2000) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Relation1` varchar(500) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Relation2` varchar(500) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Station1` varchar(500) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Station2` varchar(500) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Xueli` varchar(100) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Zhiyezige` varchar(100) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Peixun` varchar(200) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Gongzuojingyan` varchar(500) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Gexingtezheng` varchar(500) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Jibenjineng` varchar(500) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `OrderId` int(4) NULL DEFAULT NULL,
  `FullDepartmentCode` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  INDEX `index_station`(`StationCode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for survey_answer
-- ----------------------------
DROP TABLE IF EXISTS `survey_answer`;
CREATE TABLE `survey_answer`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MoneyCard` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '' COMMENT '发薪号',
  `SurveyInfoId` int(11) NULL DEFAULT NULL COMMENT '问卷id',
  `QuestionId` int(11) NULL DEFAULT NULL COMMENT '问题id',
  `Content` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '' COMMENT '填空答案',
  `OptionId` int(11) NULL DEFAULT NULL COMMENT '选项id',
  `SubmitTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '' COMMENT '提交时间',
  `GapText` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '选项填空',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5852 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '问卷调查  ---答案表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for survey_option
-- ----------------------------
DROP TABLE IF EXISTS `survey_option`;
CREATE TABLE `survey_option`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增长',
  `SurveyInfoId` int(11) NULL DEFAULT NULL COMMENT '问卷id',
  `QuestionId` int(11) NULL DEFAULT NULL COMMENT '问题id',
  `OptionContent` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '选项',
  `OptionOrder` int(11) NULL DEFAULT NULL COMMENT '排序',
  `Gap` int(1) NULL DEFAULT 0 COMMENT '是否必填 0-否 1-是',
  `GapText` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2424 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '问卷调查 --选项表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for survey_option_preview
-- ----------------------------
DROP TABLE IF EXISTS `survey_option_preview`;
CREATE TABLE `survey_option_preview`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增长',
  `SurveyInfoId` int(11) NULL DEFAULT NULL COMMENT '问卷id',
  `QuestionId` int(11) NULL DEFAULT NULL COMMENT '问题id',
  `OptionContent` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '选项',
  `OptionOrder` int(11) NULL DEFAULT NULL COMMENT '排序',
  `Gap` int(1) NULL DEFAULT 0 COMMENT '是否必填 0-否 1-是',
  `GapText` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4454 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '问卷调查 --预览问卷选项表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for survey_question
-- ----------------------------
DROP TABLE IF EXISTS `survey_question`;
CREATE TABLE `survey_question`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `SurveyInfoId` int(11) NULL DEFAULT NULL COMMENT '问卷主键',
  `QuestionTitle` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '问题',
  `Type` int(1) NULL DEFAULT NULL COMMENT '问题类型 1-单选 2-多选 3-填空',
  `UpdateTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '' COMMENT '修改时间',
  `QuestionOrder` int(11) NULL DEFAULT NULL COMMENT '排序',
  `IsWrite` int(1) NULL DEFAULT 1 COMMENT '是否必填 0-否 1-是',
  `Multiple` int(1) NULL DEFAULT -1 COMMENT '分组设置(非多选题型为0)',
  `MultipleText` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '分组内容',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 659 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '问卷调查  --问题表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for survey_question_preview
-- ----------------------------
DROP TABLE IF EXISTS `survey_question_preview`;
CREATE TABLE `survey_question_preview`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `SurveyInfoId` int(11) NULL DEFAULT NULL COMMENT '问卷主键',
  `QuestionTitle` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '问题',
  `Type` int(1) NULL DEFAULT NULL COMMENT '问题类型 1-单选 2-多选 3-填空 4-一级标题',
  `UpdateTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '' COMMENT '修改时间',
  `QuestionOrder` int(11) NULL DEFAULT NULL COMMENT '排序',
  `IsWrite` int(1) NULL DEFAULT 1 COMMENT '是否必填 0-否 1-是',
  `Multiple` int(1) NULL DEFAULT -1 COMMENT '分组设置(非多选题型为0)',
  `MultipleText` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '分组内容',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1162 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '问卷调查  --预览问卷问题表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for survey_surveyinfo
-- ----------------------------
DROP TABLE IF EXISTS `survey_surveyinfo`;
CREATE TABLE `survey_surveyinfo`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Title` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '' COMMENT '标题',
  `Content` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '问卷说明',
  `PublishStatus` int(1) NULL DEFAULT 1 COMMENT '发布状态 1-未发布 2-已发布',
  `Flag` int(1) NULL DEFAULT 1 COMMENT '删除状态 1-未删除 2-软删除',
  `StarStatus` int(1) NULL DEFAULT 1 COMMENT '是否是星标状态 1-否  2-是',
  `Link` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '' COMMENT '问卷链接',
  `AnswerCount` int(11) NULL DEFAULT 0 COMMENT '答卷人数',
  `CreateTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '' COMMENT '创建时间',
  `UpdateTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '' COMMENT '修改时间',
  `CreateUser` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '' COMMENT '创建用户发薪号',
  `UpdateUser` varchar(255) CHARACTER SET utf32 COLLATE utf32_general_ci NULL DEFAULT '' COMMENT '修改用户发薪号',
  `BatchJson` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '题目选项字符串',
  `SurveyType` int(1) NULL DEFAULT 1 COMMENT '1-院内  2-院外',
  `PreviewId` int(11) NULL DEFAULT NULL COMMENT '预览问卷id',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000037 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '问卷调查  --问卷表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for survey_surveyinfo_preview
-- ----------------------------
DROP TABLE IF EXISTS `survey_surveyinfo_preview`;
CREATE TABLE `survey_surveyinfo_preview`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Title` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '' COMMENT '标题',
  `Content` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '问卷说明',
  `BatchJson` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '题目选项字符串',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000046 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '问卷调查  --预览问卷表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `UserCode` varchar(10) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `UserName` varchar(30) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `Password` varchar(128) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `UserState` varchar(2) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL DEFAULT '1' COMMENT '0-停用\r\n            1-启用\r\n            ',
  `Mobile` varchar(60) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Operator` varchar(10) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `StationCode` varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `PicturePath` varchar(500) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Sex` varchar(10) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Nation` varchar(50) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Education` varchar(1) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Email` varchar(60) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Political` varchar(1) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `MoneyCard` varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `BranchCode` varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `ARatio` double NULL DEFAULT 0,
  `BRatio` double NULL DEFAULT 0,
  `CRatio` double NULL DEFAULT 0,
  `DRatio` double NULL DEFAULT 0,
  `FullStationCode` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `FullBranchCode` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Flag` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '0' COMMENT '删除标识 0：未删除  1：已删除',
  `RoleType` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '角色类型   0:组织部   1:人事部',
  `IsAgent` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '0' COMMENT '是否是代理部长 0-否  1-是',
  PRIMARY KEY (`UserCode`) USING BTREE,
  INDEX `index_user`(`RoleType`, `Flag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci COMMENT = '\r\n\r\n' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for user_copy
-- ----------------------------
DROP TABLE IF EXISTS `user_copy`;
CREATE TABLE `user_copy`  (
  `UserCode` varchar(10) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `UserName` varchar(30) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `Password` varchar(128) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `UserState` varchar(2) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL DEFAULT '1' COMMENT '0-停用\r\n            1-启用\r\n            ',
  `Mobile` varchar(60) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Operator` varchar(10) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `StationCode` varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `PicturePath` varchar(500) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Sex` varchar(10) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Nation` varchar(50) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Education` varchar(1) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Email` varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Political` varchar(1) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `MoneyCard` varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `BranchCode` varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `ARatio` double NULL DEFAULT 0,
  `BRatio` double NULL DEFAULT 0,
  `CRatio` double NULL DEFAULT 0,
  `DRatio` double NULL DEFAULT 0,
  `FullStationCode` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `FullBranchCode` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '',
  `Flag` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '0' COMMENT '删除标识 0：未删除  1：已删除',
  `RoleType` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL COMMENT '角色类型   0:组织部   1:人事部',
  `IsAgent` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '0' COMMENT '是否是代理部长 0-否  1-是',
  PRIMARY KEY (`UserCode`) USING BTREE,
  INDEX `index_user`(`RoleType`, `Flag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci COMMENT = '\r\n\r\n' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `UserCode` varchar(10) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `RoleCode` varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  PRIMARY KEY (`UserCode`, `RoleCode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for vote_dept
-- ----------------------------
DROP TABLE IF EXISTS `vote_dept`;
CREATE TABLE `vote_dept`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '科室名称',
  `info` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '优秀事迹',
  `picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '图片',
  `count` int(11) NULL DEFAULT 0 COMMENT '总票数',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '十佳文明科室表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for vote_dept_info
-- ----------------------------
DROP TABLE IF EXISTS `vote_dept_info`;
CREATE TABLE `vote_dept_info`  (
  `dept_id` int(255) NOT NULL COMMENT '主键/被投票人发薪号',
  `info` varchar(20000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优秀事迹',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '医务工作者优秀事迹表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for vote_info
-- ----------------------------
DROP TABLE IF EXISTS `vote_info`;
CREATE TABLE `vote_info`  (
  `worker_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键/文明科室主键',
  `info` varchar(20000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优秀事迹',
  PRIMARY KEY (`worker_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文明科室优秀事迹表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for vote_rules
-- ----------------------------
DROP TABLE IF EXISTS `vote_rules`;
CREATE TABLE `vote_rules`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `time` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '投票时间',
  `phone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '咨询电话',
  `way` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投票方式',
  `proportion` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '投票比例',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '投票规则表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for vote_user
-- ----------------------------
DROP TABLE IF EXISTS `vote_user`;
CREATE TABLE `vote_user`  (
  `u_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发薪号',
  `u_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '123456' COMMENT '密码',
  `u_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `u_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户图片（头像）',
  `u_sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `u_birth` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出生日期',
  `u_home_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `u_nation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '民族',
  `u_native_place` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '籍贯',
  `u_id_card` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证',
  `u_old_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '老发薪号',
  `u_job_time` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参加工作时间',
  `u_hospital_time` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来医院工作时间',
  `u_job_number` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工号',
  `u_email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `u_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `u_title_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职称级别',
  `u_technical_position1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专业技术职务1',
  `u_position_time1` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务1时间',
  `u_technical_position2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专业技术职务2',
  `u_position_time2` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务2时间',
  `u_employment_unit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '聘任单位',
  `u_check_department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '核对科室',
  `u_statistic_department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统计科室',
  `u_his_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'HIS部门编号',
  `u_update_status` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新状态（2为更新，1为新增，-1为删除，其它为未改变）',
  `u_activate_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '激活状态(1已激活，其它为失败)',
  `u_check_data_status` tinyint(1) NULL DEFAULT 0 COMMENT '用户资料审核状态（0为未提交审核,,1为已提交待审核，2为已审核）',
  `u_tel` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '办公电话',
  `u_degree` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学位',
  `u_edu` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学历',
  `h_count` int(11) NULL DEFAULT NULL COMMENT '医务工作者剩余票数',
  `d_count` int(11) NULL DEFAULT NULL COMMENT '科室剩余票数',
  `flag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否已投票  1-未投 2-已投',
  PRIMARY KEY (`u_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for vote_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `vote_user_dept`;
CREATE TABLE `vote_user_dept`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投票人主键/发薪号',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '文明科室主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26020 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '投票人与被投票人中间表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for vote_user_worker
-- ----------------------------
DROP TABLE IF EXISTS `vote_user_worker`;
CREATE TABLE `vote_user_worker`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投票人主键',
  `worker_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被投票人主键',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '医务工作者类型 1-医师  2-护理  3-技师',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27600 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '投票人与被投票人中间表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for vote_worker
-- ----------------------------
DROP TABLE IF EXISTS `vote_worker`;
CREATE TABLE `vote_worker`  (
  `worker_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '被投人发薪号/主键',
  `worker_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '被投票人姓名',
  `dept` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '科室',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '医务工作者类型 1-医师  2-护理  3-技师',
  `picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '图片',
  `info` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '优秀事迹',
  `count` int(11) NULL DEFAULT 0 COMMENT '总票数',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '编号',
  PRIMARY KEY (`worker_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '十佳医务工作者表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

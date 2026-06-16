/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : hx5134_property_management

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 31/01/2026 23:07:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '活动内容',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动地点',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `max_participants` int(0) NULL DEFAULT NULL COMMENT '最大参与人数',
  `current_participants` int(0) NULL DEFAULT 0 COMMENT '当前参与人数',
  `status` int(0) NULL DEFAULT 0 COMMENT '状态：0-未开始，1-进行中，2-已结束',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动图片',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity
-- ----------------------------
INSERT INTO `activity` VALUES (1, '春节联欢晚会', '欢度春节，共庆佳节', '小区广场', '2024-02-10 19:00:00', '2024-02-10 21:00:00', 200, 50, 0, 'https://picsum.photos/400/300?random=1', '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `activity` VALUES (2, '元宵节猜灯谜', '猜灯谜赢奖品', '小区活动中心', '2024-02-24 14:00:00', '2024-02-24 17:00:00', 100, 30, 1, 'https://picsum.photos/400/300?random=2', '2026-01-29 11:49:03', '2026-01-31 23:05:36');
INSERT INTO `activity` VALUES (3, '春季运动会', '趣味运动会', '小区运动场', '2024-03-15 09:00:00', '2024-03-15 12:00:00', 150, 80, 1, 'https://picsum.photos/400/300?random=3', '2026-01-29 11:49:03', '2026-01-31 23:05:39');
INSERT INTO `activity` VALUES (4, '亲子烘焙课', '学习制作蛋糕', '小区活动室', '2024-03-20 14:00:00', '2024-03-20 16:00:00', 20, 15, 0, 'https://picsum.photos/400/300?random=4', '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `activity` VALUES (5, '健康义诊', '免费体检咨询', '小区医务室', '2024-03-25 09:00:00', '2024-03-25 11:00:00', 50, 0, 0, 'https://picsum.photos/400/300?random=5', '2026-01-29 11:49:03', '2026-01-29 11:49:03');

-- ----------------------------
-- Table structure for activity_participant
-- ----------------------------
DROP TABLE IF EXISTS `activity_participant`;
CREATE TABLE `activity_participant`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `activity_id` bigint(0) NOT NULL COMMENT '活动ID',
  `owner_id` bigint(0) NOT NULL COMMENT '业主ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_activity_owner`(`activity_id`, `owner_id`) USING BTREE,
  INDEX `owner_id`(`owner_id`) USING BTREE,
  CONSTRAINT `activity_participant_ibfk_1` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `activity_participant_ibfk_2` FOREIGN KEY (`owner_id`) REFERENCES `owner` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '活动参与表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity_participant
-- ----------------------------
INSERT INTO `activity_participant` VALUES (1, 1, 1, '2026-01-31 22:57:44');
INSERT INTO `activity_participant` VALUES (2, 3, 1, '2026-01-31 22:58:37');
INSERT INTO `activity_participant` VALUES (3, 5, 1, '2026-01-31 23:06:33');
INSERT INTO `activity_participant` VALUES (4, 4, 1, '2026-01-31 23:06:35');

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '123456', '系统管理员', '2026-01-29 11:49:03', '2026-01-29 11:49:03');

-- ----------------------------
-- Table structure for complaint
-- ----------------------------
DROP TABLE IF EXISTS `complaint`;
CREATE TABLE `complaint`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `owner_id` bigint(0) NOT NULL COMMENT '业主ID',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型：投诉/建议/需求',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容',
  `status` int(0) NULL DEFAULT 0 COMMENT '状态：0-待处理，1-已处理',
  `reply` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '回复',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `owner_id`(`owner_id`) USING BTREE,
  CONSTRAINT `complaint_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `owner` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '投诉建议表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of complaint
-- ----------------------------
INSERT INTO `complaint` VALUES (1, 1, '投诉', '噪音扰民', '楼上经常深夜有噪音', 1, '马上沟通', '2026-01-29 11:49:03', '2026-01-31 23:05:32');
INSERT INTO `complaint` VALUES (2, 2, '建议', '增加健身器材', '希望在广场增加一些健身器材', 1, '已记录，将纳入下月计划', '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `complaint` VALUES (3, 3, '需求', '停车位紧张', '希望增加临时停车位', 1, '啊', '2026-01-29 11:49:03', '2026-01-31 22:57:18');
INSERT INTO `complaint` VALUES (4, 4, '投诉', '垃圾清理不及时', '垃圾桶经常满溢', 1, '已增加清理频次', '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `complaint` VALUES (5, 5, '建议', '增设快递柜', '希望增设智能快递柜', 0, NULL, '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `complaint` VALUES (6, 1, '投诉', '噪音投诉', '测试投诉', 0, NULL, '2026-01-31 22:58:50', '2026-01-31 22:58:50');
INSERT INTO `complaint` VALUES (7, 1, '需求', '需要钱', '测试', 0, NULL, '2026-01-31 23:06:47', '2026-01-31 23:06:47');

-- ----------------------------
-- Table structure for fee
-- ----------------------------
DROP TABLE IF EXISTS `fee`;
CREATE TABLE `fee`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `owner_id` bigint(0) NOT NULL COMMENT '业主ID',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型：物业费/水费/电费/燃气费',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `fee_month` date NULL DEFAULT NULL COMMENT '费用月份',
  `status` int(0) NULL DEFAULT 0 COMMENT '状态：0-未缴费，1-已缴费',
  `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '缴费时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `owner_id`(`owner_id`) USING BTREE,
  CONSTRAINT `fee_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `owner` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '费用表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fee
-- ----------------------------
INSERT INTO `fee` VALUES (1, 1, '物业费', 268.50, '2024-01-01', 1, '2024-01-05 10:30:00', '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `fee` VALUES (2, 1, '水费', 45.20, '2024-01-01', 1, '2026-01-31 23:05:58', '2026-01-29 11:49:03', '2026-01-31 23:05:58');
INSERT INTO `fee` VALUES (3, 1, '电费', 128.60, '2024-01-01', 1, '2026-01-31 22:58:10', '2026-01-29 11:49:03', '2026-01-31 22:58:10');
INSERT INTO `fee` VALUES (4, 2, '物业费', 268.50, '2024-01-01', 1, '2024-01-03 14:20:00', '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `fee` VALUES (5, 2, '水费', 38.50, '2024-01-01', 1, '2024-01-03 14:20:00', '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `fee` VALUES (6, 3, '物业费', 360.00, '2024-01-01', 0, NULL, '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `fee` VALUES (7, 3, '燃气费', 56.80, '2024-01-01', 0, NULL, '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `fee` VALUES (8, 4, '物业费', 285.00, '2024-01-01', 1, '2024-01-10 09:15:00', '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `fee` VALUES (9, 5, '物业费', 420.00, '2024-01-01', 0, NULL, '2026-01-29 11:49:03', '2026-01-29 11:49:03');

-- ----------------------------
-- Table structure for owner
-- ----------------------------
DROP TABLE IF EXISTS `owner`;
CREATE TABLE `owner`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `status` int(0) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '业主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of owner
-- ----------------------------
INSERT INTO `owner` VALUES (1, 'zhangsan', '123456', '张三', '13800138001', 'zhangsan@qq.com', '110101199001011234', 1, '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `owner` VALUES (2, 'lisi', '123456', '李四', '13800138002', 'lisi@qq.com', '110101199002021234', 1, '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `owner` VALUES (3, 'wangwu', '123456', '王五', '13800138003', 'wangwu@qq.com', '110101199003031234', 1, '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `owner` VALUES (4, 'zhaoliu', '123456', '赵六', '13800138004', 'zhaoliu@qq.com', '110101199004041234', 1, '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `owner` VALUES (5, 'qianqi', '123456', '钱七', '13800138005', 'qianqi@qq.com', '110101199005051234', 1, '2026-01-29 11:49:03', '2026-01-29 11:49:03');

-- ----------------------------
-- Table structure for parking_space
-- ----------------------------
DROP TABLE IF EXISTS `parking_space`;
CREATE TABLE `parking_space`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `space_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车位编号',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型：购买/年租/月租',
  `status` int(0) NULL DEFAULT 0 COMMENT '状态：0-空闲，1-已占用',
  `owner_id` bigint(0) NULL DEFAULT NULL COMMENT '业主ID',
  `start_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `space_no`(`space_no`) USING BTREE,
  INDEX `owner_id`(`owner_id`) USING BTREE,
  CONSTRAINT `parking_space_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `owner` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '停车位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of parking_space
-- ----------------------------
INSERT INTO `parking_space` VALUES (1, 'A001', '1', 0, 1, '2024-01-01', '2099-12-31', 150000.00, '2026-01-29 11:49:03', '2026-01-31 23:04:15');
INSERT INTO `parking_space` VALUES (2, 'A002', '年租', 0, 2, '2024-01-01', '2024-12-31', 3600.00, '2026-01-29 11:49:03', '2026-01-31 23:05:08');
INSERT INTO `parking_space` VALUES (3, 'A003', '月租', 1, 3, '2024-01-01', '2024-01-31', 300.00, '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `parking_space` VALUES (4, 'A004', '空闲', 1, 1, NULL, NULL, 300.00, '2026-01-29 11:49:03', '2026-01-31 23:05:12');
INSERT INTO `parking_space` VALUES (5, 'A005', '空闲', 0, NULL, NULL, NULL, 3600.00, '2026-01-29 11:49:03', '2026-01-29 11:49:03');

-- ----------------------------
-- Table structure for property
-- ----------------------------
DROP TABLE IF EXISTS `property`;
CREATE TABLE `property`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `building_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '楼栋号',
  `unit_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单元号',
  `room_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房间号',
  `area` decimal(10, 2) NULL DEFAULT NULL COMMENT '面积(m²)',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '户型',
  `owner_id` bigint(0) NULL DEFAULT NULL COMMENT '业主ID',
  `status` int(0) NULL DEFAULT 0 COMMENT '状态：0-空置，1-已售，2-出租',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `owner_id`(`owner_id`) USING BTREE,
  CONSTRAINT `property_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `owner` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房产表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of property
-- ----------------------------
INSERT INTO `property` VALUES (1, '1栋', '1单元', '101', 89.50, '两室一厅', 1, 2, '2026-01-29 11:49:03', '2026-01-31 23:05:00');
INSERT INTO `property` VALUES (2, '1栋', '1单元', '102', 89.50, '两室一厅', 2, 0, '2026-01-29 11:49:03', '2026-01-31 23:04:04');
INSERT INTO `property` VALUES (3, '1栋', '2单元', '201', 120.00, '三室两厅', 3, 1, '2026-01-29 11:49:03', '2026-01-31 23:05:03');
INSERT INTO `property` VALUES (4, '2栋', '1单元', '301', 95.00, '两室两厅', 4, 0, '2026-01-29 11:49:03', '2026-01-31 23:04:09');
INSERT INTO `property` VALUES (5, '2栋', '2单元', '401', 140.00, '四室两厅', 5, 0, '2026-01-29 11:49:03', '2026-01-31 23:04:11');

-- ----------------------------
-- Table structure for repair
-- ----------------------------
DROP TABLE IF EXISTS `repair`;
CREATE TABLE `repair`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `owner_id` bigint(0) NOT NULL COMMENT '业主ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '报修标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '报修内容',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '报修地点',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `status` int(0) NULL DEFAULT 0 COMMENT '状态：0-待处理，1-处理中，2-已完成',
  `reply` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '回复',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `owner_id`(`owner_id`) USING BTREE,
  CONSTRAINT `repair_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `owner` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '报修表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of repair
-- ----------------------------
INSERT INTO `repair` VALUES (1, 1, '水管漏水', '厨房水龙头漏水严重', '1栋1单元101', '13800138001', 2, NULL, '2026-01-29 11:49:03', '2026-01-31 22:52:26');
INSERT INTO `repair` VALUES (2, 2, '电灯不亮', '客厅灯坏了', '1栋1单元102', '13800138002', 1, '已安排维修人员', '2026-01-29 11:49:03', '2026-01-31 23:05:21');
INSERT INTO `repair` VALUES (3, 3, '门锁损坏', '大门锁芯坏了', '1栋2单元201', '13800138003', 2, '已更换新锁', '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `repair` VALUES (4, 4, '窗户漏风', '卧室窗户密封不好', '2栋1单元301', '13800138004', 2, '好的', '2026-01-29 11:49:03', '2026-01-31 22:57:06');
INSERT INTO `repair` VALUES (5, 5, '墙面渗水', '卫生间墙面渗水', '2栋2单元401', '13800138005', 1, '正在查找原因', '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `repair` VALUES (6, 1, '下水道漏水', '保修测试', '1栋101', '13800138001', 0, NULL, '2026-01-31 22:58:31', '2026-01-31 22:58:31');
INSERT INTO `repair` VALUES (7, 1, '下水道堵了', '报修测试', '一单元01', '13800138001', 0, NULL, '2026-01-31 23:06:27', '2026-01-31 23:06:27');

-- ----------------------------
-- Table structure for visitor
-- ----------------------------
DROP TABLE IF EXISTS `visitor`;
CREATE TABLE `visitor`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `owner_id` bigint(0) NOT NULL COMMENT '业主ID',
  `visitor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '访客姓名',
  `visitor_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访客电话',
  `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `visit_reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '来访事由',
  `visit_time` datetime(0) NULL DEFAULT NULL COMMENT '来访时间',
  `parking_needed` int(0) NULL DEFAULT 0 COMMENT '是否需要停车位：0-否，1-是',
  `status` int(0) NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已通过',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `owner_id`(`owner_id`) USING BTREE,
  CONSTRAINT `visitor_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `owner` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '访客表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of visitor
-- ----------------------------
INSERT INTO `visitor` VALUES (1, 1, '王大明', '13900139001', '110101199010101234', '探亲', '2024-01-20 10:00:00', 1, 1, '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `visitor` VALUES (2, 2, '李小红', '13900139002', '110101199020201234', '朋友聚会', '2024-01-21 15:00:00', 0, 1, '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `visitor` VALUES (3, 3, '张伟', '13900139003', '110101199030301234', '商务洽谈', '2024-01-22 09:00:00', 1, 0, '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `visitor` VALUES (4, 4, '刘芳', '13900139004', '110101199040401234', '亲戚拜访', '2024-01-23 14:00:00', 0, 0, '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `visitor` VALUES (5, 5, '陈强', '13900139005', '110101199050501234', '朋友来访', '2024-01-24 18:00:00', 1, 0, '2026-01-29 11:49:03', '2026-01-29 11:49:03');
INSERT INTO `visitor` VALUES (6, 1, '李四', '13800000000', '410922200308140715', '串门', '2026-01-31 14:59:12', 0, 1, '2026-01-31 22:59:14', '2026-01-31 23:05:42');
INSERT INTO `visitor` VALUES (7, 1, '王五', '13800000000', '410922200308140716', '串门', '2026-01-30 16:00:00', 0, 0, '2026-01-31 23:07:12', '2026-01-31 23:07:12');

SET FOREIGN_KEY_CHECKS = 1;

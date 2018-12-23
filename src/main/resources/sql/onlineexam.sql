/*
Navicat MySQL Data Transfer

Source Server         : skywalker
Source Server Version : 50617
Source Host           : localhost:3306
Source Database       : onlineexam

Target Server Type    : MYSQL
Target Server Version : 50617
File Encoding         : 65001

Date: 2015-09-27 10:45:31
*/

create database onlineexam;
use onlineexam;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `class`
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cno` int(11) NOT NULL,
  `gid` int(11) NOT NULL,
  `mid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `gid` (`gid`),
  KEY `mid` (`mid`),
  CONSTRAINT `class_ibfk_1` FOREIGN KEY (`gid`) REFERENCES `grade` (`id`),
  CONSTRAINT `class_ibfk_2` FOREIGN KEY (`mid`) REFERENCES `major` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES ('1', '2', '1', '62');
INSERT INTO `class` VALUES ('2', '1', '1', '62');
INSERT INTO `class` VALUES ('3', '3', '1', '62');
INSERT INTO `class` VALUES ('4', '4', '1', '62');
INSERT INTO `class` VALUES ('5', '1', '7', '35');

-- ----------------------------
-- Table structure for `exam`
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `timelimit` int(11) DEFAULT '60',
  `endtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `status` varchar(10) NOT NULL,
  `points` int(11) NOT NULL,
  `singlepoints` int(11) NOT NULL,
  `multipoints` int(11) NOT NULL,
  `judgepoints` int(11) NOT NULL,
  `tid` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tid` (`tid`),
  CONSTRAINT `exam_ibfk_1` FOREIGN KEY (`tid`) REFERENCES `teacher` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of exam
-- ----------------------------
INSERT INTO `exam` VALUES ('6', '社会常识', '10', '2015-09-24 21:25:12', 'RUNNED', '50', '10', '20', '20', '1000');
INSERT INTO `exam` VALUES ('7', '地理常识', '2', '2015-09-24 12:10:40', 'RUNNED', '50', '10', '20', '20', '1000');
INSERT INTO `exam` VALUES ('8', '历史常识', '10', '2015-09-24 12:15:40', 'RUNNED', '40', '10', '20', '10', '1000');
INSERT INTO `exam` VALUES ('9', '常识考试(二)', '10', '2015-09-28 17:18:54', 'RUNNING', '40', '10', '20', '10', '1000');
INSERT INTO `exam` VALUES ('10', '测试多选班级', '10', '2015-09-29 10:18:49', 'RUNNING', '40', '10', '20', '10', '1000');
INSERT INTO `exam` VALUES ('11', '历史知识', '10', '2015-10-04 09:24:22', 'RUNNING', '40', '10', '20', '10', '1000');

-- ----------------------------
-- Table structure for `examinationresult`
-- ----------------------------
DROP TABLE IF EXISTS `examinationresult`;
CREATE TABLE `examinationresult` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eid` int(11) NOT NULL,
  `sid` varchar(256) NOT NULL,
  `point` int(11) NOT NULL,
  `time` datetime NOT NULL,
  `examtitle` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of examinationresult
-- ----------------------------
INSERT INTO `examinationresult` VALUES ('5', '6', '201201050537', '30', '2015-09-23 17:15:11', '社会常识');
INSERT INTO `examinationresult` VALUES ('6', '9', '201201050537', '0', '2015-09-23 18:44:00', '常识考试(二)');
INSERT INTO `examinationresult` VALUES ('7', '7', '201201050537', '50', '2015-09-24 18:55:48', '地理常识');
INSERT INTO `examinationresult` VALUES ('8', '11', '201201050537', '40', '2015-09-27 09:26:36', '历史知识');

-- ----------------------------
-- Table structure for `examinationresult_question`
-- ----------------------------
DROP TABLE IF EXISTS `examinationresult_question`;
CREATE TABLE `examinationresult_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `erid` int(11) NOT NULL,
  `qid` int(11) NOT NULL,
  `isright` bit(1) NOT NULL,
  `wronganswer` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of examinationresult_question
-- ----------------------------
INSERT INTO `examinationresult_question` VALUES ('13', '5', '13', '', '3');
INSERT INTO `examinationresult_question` VALUES ('14', '5', '11', '', '0');
INSERT INTO `examinationresult_question` VALUES ('15', '5', '12', '', '0');
INSERT INTO `examinationresult_question` VALUES ('16', '6', '21', '', '');
INSERT INTO `examinationresult_question` VALUES ('17', '6', '22', '', '');
INSERT INTO `examinationresult_question` VALUES ('18', '6', '23', '', '');
INSERT INTO `examinationresult_question` VALUES ('19', '7', '15', '', '2');
INSERT INTO `examinationresult_question` VALUES ('20', '7', '16', '', '0,1,3');
INSERT INTO `examinationresult_question` VALUES ('21', '7', '17', '', '1');
INSERT INTO `examinationresult_question` VALUES ('22', '8', '24', '', '2');
INSERT INTO `examinationresult_question` VALUES ('23', '8', '25', '', '2,3');
INSERT INTO `examinationresult_question` VALUES ('24', '8', '26', '', '0');

-- ----------------------------
-- Table structure for `exam_class`
-- ----------------------------
DROP TABLE IF EXISTS `exam_class`;
CREATE TABLE `exam_class` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eid` int(11) NOT NULL,
  `cid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `eid` (`eid`),
  KEY `cid` (`cid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of exam_class
-- ----------------------------
INSERT INTO `exam_class` VALUES ('12', '6', '1');
INSERT INTO `exam_class` VALUES ('13', '7', '1');
INSERT INTO `exam_class` VALUES ('14', '8', '1');
INSERT INTO `exam_class` VALUES ('15', '9', '1');
INSERT INTO `exam_class` VALUES ('16', '10', '1');
INSERT INTO `exam_class` VALUES ('17', '10', '2');
INSERT INTO `exam_class` VALUES ('18', '11', '1');
INSERT INTO `exam_class` VALUES ('19', '11', '2');

-- ----------------------------
-- Table structure for `exam_question`
-- ----------------------------
DROP TABLE IF EXISTS `exam_question`;
CREATE TABLE `exam_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eid` int(11) NOT NULL,
  `qid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `eid` (`eid`),
  KEY `qid` (`qid`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of exam_question
-- ----------------------------
INSERT INTO `exam_question` VALUES ('7', '6', '13');
INSERT INTO `exam_question` VALUES ('8', '6', '11');
INSERT INTO `exam_question` VALUES ('9', '6', '12');
INSERT INTO `exam_question` VALUES ('10', '7', '15');
INSERT INTO `exam_question` VALUES ('11', '7', '16');
INSERT INTO `exam_question` VALUES ('12', '7', '17');
INSERT INTO `exam_question` VALUES ('13', '8', '18');
INSERT INTO `exam_question` VALUES ('14', '8', '19');
INSERT INTO `exam_question` VALUES ('15', '8', '20');
INSERT INTO `exam_question` VALUES ('16', '9', '21');
INSERT INTO `exam_question` VALUES ('17', '9', '22');
INSERT INTO `exam_question` VALUES ('18', '9', '23');
INSERT INTO `exam_question` VALUES ('19', '10', '15');
INSERT INTO `exam_question` VALUES ('20', '10', '11');
INSERT INTO `exam_question` VALUES ('21', '10', '23');
INSERT INTO `exam_question` VALUES ('22', '11', '24');
INSERT INTO `exam_question` VALUES ('23', '11', '25');
INSERT INTO `exam_question` VALUES ('24', '11', '26');

-- ----------------------------
-- Table structure for `grade`
-- ----------------------------
DROP TABLE IF EXISTS `grade`;
CREATE TABLE `grade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `grade` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of grade
-- ----------------------------
INSERT INTO `grade` VALUES ('1', '2012');
INSERT INTO `grade` VALUES ('7', '2014');

-- ----------------------------
-- Table structure for `major`
-- ----------------------------
DROP TABLE IF EXISTS `major`;
CREATE TABLE `major` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of major
-- ----------------------------
INSERT INTO `major` VALUES ('1', '电气信息类');
INSERT INTO `major` VALUES ('2', '电气工程及其自动化');
INSERT INTO `major` VALUES ('3', '采矿工程');
INSERT INTO `major` VALUES ('4', '机械设计制造及其自动化');
INSERT INTO `major` VALUES ('5', '自动化');
INSERT INTO `major` VALUES ('6', '电气工程与自动化');
INSERT INTO `major` VALUES ('7', '矿物加工工程');
INSERT INTO `major` VALUES ('8', '材料成型及控制工程');
INSERT INTO `major` VALUES ('9', '电子信息工程');
INSERT INTO `major` VALUES ('10', '网络工程');
INSERT INTO `major` VALUES ('11', '地质工程');
INSERT INTO `major` VALUES ('12', '过程装备与控制工程');
INSERT INTO `major` VALUES ('13', '通信工程');
INSERT INTO `major` VALUES ('14', '建筑学');
INSERT INTO `major` VALUES ('15', '金属材料工程');
INSERT INTO `major` VALUES ('16', '测控技术与仪器');
INSERT INTO `major` VALUES ('17', '计算机科学与技术');
INSERT INTO `major` VALUES ('18', '城市规划');
INSERT INTO `major` VALUES ('19', '无机非金属材料工程');
INSERT INTO `major` VALUES ('20', '热能与动力工程');
INSERT INTO `major` VALUES ('21', '生物医学工程');
INSERT INTO `major` VALUES ('22', '土木工程');
INSERT INTO `major` VALUES ('23', '建筑环境与设备工程');
INSERT INTO `major` VALUES ('24', '环境工程');
INSERT INTO `major` VALUES ('25', '物流工程');
INSERT INTO `major` VALUES ('26', '水利水电工程');
INSERT INTO `major` VALUES ('27', '安全工程');
INSERT INTO `major` VALUES ('28', '工程力学');
INSERT INTO `major` VALUES ('29', '水文与水资源工程');
INSERT INTO `major` VALUES ('30', '化学工程与工艺');
INSERT INTO `major` VALUES ('31', '生物工程');
INSERT INTO `major` VALUES ('32', '测绘工程');
INSERT INTO `major` VALUES ('33', '交通运输');
INSERT INTO `major` VALUES ('34', '遥感科学与技术');
INSERT INTO `major` VALUES ('35', '交通工程');
INSERT INTO `major` VALUES ('36', '高分子材料与工程');
INSERT INTO `major` VALUES ('37', '车辆工程');
INSERT INTO `major` VALUES ('38', '城市地下空间工程');
INSERT INTO `major` VALUES ('39', '机械电子工程');
INSERT INTO `major` VALUES ('40', '石油工程');
INSERT INTO `major` VALUES ('41', '勘查技术与工程');
INSERT INTO `major` VALUES ('42', '资源勘查工程');
INSERT INTO `major` VALUES ('43', '油气储运工程');
INSERT INTO `major` VALUES ('44', '船舶与海洋工程');
INSERT INTO `major` VALUES ('45', '材料科学与工程');
INSERT INTO `major` VALUES ('46', '软件工程');
INSERT INTO `major` VALUES ('47', '机械类');
INSERT INTO `major` VALUES ('48', '数字媒体技术');
INSERT INTO `major` VALUES ('49', '物联网工程');
INSERT INTO `major` VALUES ('50', '教育技术学');
INSERT INTO `major` VALUES ('51', '广告学');
INSERT INTO `major` VALUES ('52', '音乐学');
INSERT INTO `major` VALUES ('53', '艺术设计');
INSERT INTO `major` VALUES ('54', '英语');
INSERT INTO `major` VALUES ('55', '日语');
INSERT INTO `major` VALUES ('56', '俄语');
INSERT INTO `major` VALUES ('57', '汉语言文学');
INSERT INTO `major` VALUES ('58', '法学');
INSERT INTO `major` VALUES ('59', '信息与计算科学');
INSERT INTO `major` VALUES ('60', '地理信息系统');
INSERT INTO `major` VALUES ('61', '数学与应用数学');
INSERT INTO `major` VALUES ('62', '电子信息科学与技术');
INSERT INTO `major` VALUES ('63', '应用物理学');
INSERT INTO `major` VALUES ('64', '环境科学');
INSERT INTO `major` VALUES ('65', '应用化学');
INSERT INTO `major` VALUES ('66', '统计学');
INSERT INTO `major` VALUES ('67', '地球物理学');
INSERT INTO `major` VALUES ('68', '资源环境与城乡规划管理');
INSERT INTO `major` VALUES ('69', '材料物理');
INSERT INTO `major` VALUES ('70', '材料化学');
INSERT INTO `major` VALUES ('71', '化学');
INSERT INTO `major` VALUES ('72', '地质学');
INSERT INTO `major` VALUES ('73', '数学类');
INSERT INTO `major` VALUES ('74', '理论与应用力学');
INSERT INTO `major` VALUES ('75', '物流管理');
INSERT INTO `major` VALUES ('76', '行政管理');
INSERT INTO `major` VALUES ('77', '工程管理');
INSERT INTO `major` VALUES ('78', '会计学');
INSERT INTO `major` VALUES ('79', '电子商务');
INSERT INTO `major` VALUES ('80', '信息管理与信息系统');
INSERT INTO `major` VALUES ('81', '工业工程');
INSERT INTO `major` VALUES ('82', '市场营销');
INSERT INTO `major` VALUES ('83', '工商管理');
INSERT INTO `major` VALUES ('84', '财务管理');
INSERT INTO `major` VALUES ('85', '公共事业管理');
INSERT INTO `major` VALUES ('86', '工商管理类');
INSERT INTO `major` VALUES ('87', '管理科学与工程类');
INSERT INTO `major` VALUES ('88', '财政学');
INSERT INTO `major` VALUES ('89', '金融学');
INSERT INTO `major` VALUES ('90', '经济学');
INSERT INTO `major` VALUES ('91', '国际经济与贸易');
INSERT INTO `major` VALUES ('92', '基佬滚粗');

-- ----------------------------
-- Table structure for `manager`
-- ----------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `modified` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manager
-- ----------------------------
INSERT INTO `manager` VALUES ('1', 'admin', '21232f297a57a5a743894a0e4a801fc3', '');

-- ----------------------------
-- Table structure for `question`
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `optiona` varchar(255) DEFAULT NULL,
  `optionb` varchar(255) DEFAULT NULL,
  `optionc` varchar(255) DEFAULT NULL,
  `optiond` varchar(255) DEFAULT NULL,
  `point` int(11) NOT NULL,
  `type` varchar(10) NOT NULL,
  `answer` varchar(255) NOT NULL,
  `tid` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tid` (`tid`),
  CONSTRAINT `question_ibfk_1` FOREIGN KEY (`tid`) REFERENCES `teacher` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES ('8', '阿森纳是第几?', '3', '1', '4', '1', '10', 'MULTI', '0,1,2', '1000');
INSERT INTO `question` VALUES ('10', '蛤蛤是谁?', '胡锦涛', '习近平', '江泽民', '李克强', '20', 'SINGLE', '2', '1000');
INSERT INTO `question` VALUES ('11', '下列哪个网站被墙了?', 'Google', 'Facebook', 'Baidu', 'Taobao', '20', 'MULTI', '0,1', '1000');
INSERT INTO `question` VALUES ('12', '江泽民是哈哈吗?', null, null, null, null, '20', 'JUDGE', '0', '1000');
INSERT INTO `question` VALUES ('13', '美国在哪个州?', '亚洲', '欧洲', '非洲', '北美', '10', 'SINGLE', '3', '1000');
INSERT INTO `question` VALUES ('14', '小明是SB吗?', null, null, null, null, '10', 'JUDGE', '1', '1000');
INSERT INTO `question` VALUES ('15', '哪个国家被叫做\'棒子\'?', '日本', '澳大利亚', '韩国', '俄罗斯', '10', 'SINGLE', '2', '1000');
INSERT INTO `question` VALUES ('16', '哪些地区是英国的组成部分?', '英格兰', '苏格兰', '加泰罗尼亚', '威尔士', '20', 'MULTI', '0,1,3', '1000');
INSERT INTO `question` VALUES ('17', '直布罗陀属于西班牙吗?', null, null, null, null, '20', 'JUDGE', '1', '1000');
INSERT INTO `question` VALUES ('18', '谁是光武帝?', '刘秀', '刘彻', '刘邦', '刘备', '10', 'SINGLE', '0', '1000');
INSERT INTO `question` VALUES ('19', '哪些是大一统的王朝?', ' 汉朝', '唐朝', '明朝', '宋朝', '20', 'MULTI', '0,1,2', '1000');
INSERT INTO `question` VALUES ('20', '诸葛亮是吴的吗?', null, null, null, null, '10', 'JUDGE', '0', '1000');
INSERT INTO `question` VALUES ('21', '法布雷加斯在哪个队?', '巴塞罗那', '切尔西', '阿森纳', '曼城', '10', 'SINGLE', '1', '1000');
INSERT INTO `question` VALUES ('22', '哪些国家不是欧洲的?', '英国', '美国', '德国', '土耳其', '20', 'MULTI', '1,3', '1000');
INSERT INTO `question` VALUES ('23', '斯科尔斯退役了吗?', null, null, null, null, '10', 'JUDGE', '0', '1000');
INSERT INTO `question` VALUES ('24', '布兰妮是哪国的?', '加拿大', '澳大利亚', '美国', '英国', '10', 'SINGLE', '2', '1000');
INSERT INTO `question` VALUES ('25', '哪些城市做过首都?', '北京', '上海', '西安', '洛阳', '20', 'MULTI', '2,3', '1000');
INSERT INTO `question` VALUES ('26', '十三朝古都是西安吗?', null, null, null, null, '10', 'JUDGE', '0', '1000');

-- ----------------------------
-- Table structure for `student`
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `cid` int(11) NOT NULL,
  `modified` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `cid` (`cid`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `class` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('20122249870401', 'skywalker', '81dc9bdb52d04dc20036dbd8313ed055', '1', '');

-- ----------------------------
-- Table structure for `teacher`
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `modified` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('1000', 'skywalker', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1111', '郭琛', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1112', '刁荣春', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1113', '王俊红', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1114', '邓莲菊', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1115', '刘文波', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1116', '孙贵合', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1117', '孙洪军', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1118', '李文凤', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1119', '李洁玲', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1120', '张龙 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1121', '路书芳', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1122', '王苹 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1123', '邢军 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1124', '刘丽云', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1125', '许德昌', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1126', '李冬梅', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1127', '张亚红', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1128', '陈爱民', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1129', '果征 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1130', '赵月灵', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1131', '寇富弄', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1132', '金英华', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1133', '付红焱', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1134', '郝康平', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1135', '魏红娟', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1136', '张美荣', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1137', '苗金明', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1138', '周海霞', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1139', '韩凤荣', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1140', '王易 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1141', '付忠广', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1142', '刘姗姗', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1143', '纪韶 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1144', '杨卫民', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1145', '张招崇', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1146', '金鑫 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1147', '茹秀英', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1148', '秦红岭', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1149', '唐建平', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1150', '曹金珍', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1151', '天津市', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1152', '刘娟 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1153', '李建霞', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1154', '时美华', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1155', '张静秋', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1156', '孟庆阳', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1157', '白翠红', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1158', '邢泽运', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1159', '刘月升', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1160', '刘庆利', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1161', '林茂 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1162', '谭娜 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1163', '李藐 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1164', '郑芸 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1165', '刘春艳', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1166', '苗利军', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1167', '王美蕾', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1168', '孙滨 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1169', '李建国', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1170', '马薇 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1171', '马幼捷', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1172', '孙保存', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1173', '杨德健', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1174', '姚永红', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1175', '山西省', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1176', '李艳霞', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1177', '弓志青', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1178', '王世亚', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1179', '王秀萍', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1180', '王晓燕', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1181', '申艳红', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1182', '李媛 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1183', '李美玲', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1184', '李素红', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1185', '李瑞萍', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1186', '杨海艳', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1187', '张翘 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1188', '曹秀萍', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1189', '韩海青', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1190', '卜利红', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1191', '白晓燕', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1192', '吕萍 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1193', '李丹霞', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1194', '李志勇', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1195', '李惠荣', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1196', '杨玉纹', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1197', '张青荣', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1198', '侯雪姣', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1199', '贾娜娜', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1200', '郭庆庆', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1201', '郭建光', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1202', '王劼 ', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1203', '刘桂兰', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1204', '闫丽珍', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1205', '许风玲', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1206', '王凤英', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1207', '史德仁', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1208', '任巨波', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1209', '张广德', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('1210', '李菊福', '81dc9bdb52d04dc20036dbd8313ed055', '');
INSERT INTO `teacher` VALUES ('﻿1110', '郑燕斌', '81dc9bdb52d04dc20036dbd8313ed055', '');

-- ----------------------------
-- Table structure for `teacher_class`
-- ----------------------------
DROP TABLE IF EXISTS `teacher_class`;
CREATE TABLE `teacher_class` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tid` varchar(255) NOT NULL,
  `cid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tid` (`tid`),
  KEY `cid` (`cid`),
  CONSTRAINT `teacher_class_ibfk_1` FOREIGN KEY (`tid`) REFERENCES `teacher` (`id`),
  CONSTRAINT `teacher_class_ibfk_2` FOREIGN KEY (`cid`) REFERENCES `class` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of teacher_class
-- ----------------------------
INSERT INTO `teacher_class` VALUES ('6', '1111', '2');
INSERT INTO `teacher_class` VALUES ('7', '1111', '4');
INSERT INTO `teacher_class` VALUES ('11', '1112', '1');
INSERT INTO `teacher_class` VALUES ('12', '1000', '2');
INSERT INTO `teacher_class` VALUES ('13', '1000', '1');

-- ----------------------------
-- Procedure structure for `getExamById`
-- ----------------------------
DROP PROCEDURE IF EXISTS `getExamById`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getExamById`(in id int)
begin
declare examid int;
declare expire datetime;
declare loopFlag boolean default false;
declare idCursor cursor for select e.id, e.endtime from exam e where e.id = id and status = 'RUNNING';
declare continue handler for NOT FOUND set loopFlag = true;
open idCursor;
idCursorLoop:loop
fetch idCursor into examid, expire;
if now() > expire then
update exam e set e.status = 'RUNNED' where e.id = examid;
end if;
if loopFlag then
leave idCursorLoop;
end if;
end loop;
select * from exam e where e.id = id;
end
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `getExamByStudent`
-- ----------------------------
DROP PROCEDURE IF EXISTS `getExamByStudent`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getExamByStudent`(in sid varchar(20), in pn int, in ps int)
begin
declare start int;
declare examid int;
declare expire datetime;
declare loopFlag boolean default false;
declare pageCursor cursor for select e.id, e.endtime from exam e where e.id in (select eid from exam_class where cid = (select cid from student where id = sid)) and status = 'RUNNING' limit start, ps;
declare continue handler for NOT FOUND set loopFlag = true;
set start = (pn - 1) * ps;
open pageCursor;
pageCursorLoop:loop
fetch pageCursor into examid, expire;
if now() > expire then
update exam e set e.status = 'RUNNED' where e.id = examid;
end if;
if loopFlag then
leave pageCursorLoop;
end if;
end loop;
select e.* from exam e where e.id in (select eid from exam_class where cid = (select cid from student where id = sid)) limit start, ps;
end
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `getExamByTeacher`
-- ----------------------------
DROP PROCEDURE IF EXISTS `getExamByTeacher`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getExamByTeacher`(in pn int, in ps int, in tid varchar(20))
begin
declare start int;
declare examid int;
declare expire datetime;
declare loopFlag boolean default false;
declare pageCursor cursor for select e.id, e.endtime from exam e where e.tid = tid and status = 'RUNNING' limit start, ps;
declare continue handler for NOT FOUND set loopFlag = true;
set start = (pn - 1) * ps;
open pageCursor;
set loopFlag = false;
pageCursorLoop:loop
fetch pageCursor into examid, expire;
if now() > expire then
update exam e set e.status = 'RUNNED' where e.id = examid;
end if;
if loopFlag then
leave pageCursorLoop;
end if;
end loop;
select * from exam e where e.tid = tid limit start, ps;
end
;;
DELIMITER ;

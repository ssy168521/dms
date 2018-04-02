/*
Navicat MySQL Data Transfer

Source Server         : 175
Source Server Version : 50626
Source Host           : 192.168.1.175:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2017-03-02 15:53:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for geometry_columns
-- ----------------------------
DROP TABLE IF EXISTS `geometry_columns`;
CREATE TABLE `geometry_columns` (
  `F_TABLE_CATALOG` varchar(256) DEFAULT NULL,
  `F_TABLE_SCHEMA` varchar(256) DEFAULT NULL,
  `F_TABLE_NAME` varchar(256) NOT NULL,
  `F_GEOMETRY_COLUMN` varchar(256) NOT NULL,
  `COORD_DIMENSION` int(11) DEFAULT NULL,
  `SRID` int(11) DEFAULT NULL,
  `TYPE` varchar(256) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of geometry_columns
-- ----------------------------
INSERT INTO `geometry_columns` VALUES (null, null, 'borders2', 'SHAPE2', '2', '1', 'POLYGON');
INSERT INTO `geometry_columns` VALUES (null, null, 'plugintable', 'SHAPE', '2', '1', 'POLYGON');
INSERT INTO `geometry_columns` VALUES (null, null, 'tbs_zy3_bm', 'SHAPE', '2', '1', 'POLYGON');
INSERT INTO `geometry_columns` VALUES (null, null, 'tbs_zy3_sc', 'SHAPE', '2', '1', 'POLYGON');
INSERT INTO `geometry_columns` VALUES (null, null, 'tbs_zy3_test', 'SHAPE', '2', '1', 'POLYGON');

-- ----------------------------
-- Table structure for spatial_ref_sys
-- ----------------------------
DROP TABLE IF EXISTS `spatial_ref_sys`;
CREATE TABLE `spatial_ref_sys` (
  `SRID` int(11) NOT NULL,
  `AUTH_NAME` varchar(256) DEFAULT NULL,
  `AUTH_SRID` int(11) DEFAULT NULL,
  `SRTEXT` varchar(2048) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of spatial_ref_sys
-- ----------------------------
INSERT INTO `spatial_ref_sys` VALUES ('1', null, null, 'GEOGCS[\"GCS_WGS_1984\",DATUM[\"WGS_1984\",SPHEROID[\"WGS_84\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]]');

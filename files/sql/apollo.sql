-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.6.35 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 apollo 的数据库结构
CREATE DATABASE IF NOT EXISTS `apollo` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `apollo`;

-- 导出  表 apollo.tb_config_file 结构
CREATE TABLE IF NOT EXISTS `tb_config_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project` varchar(32) NOT NULL COMMENT '项目编码',
  `env` varchar(32) NOT NULL COMMENT '环境编码',
  `name` varchar(64) NOT NULL COMMENT '文件名称',
  `content` text NOT NULL COMMENT '文件内容',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本',
  `latest` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否最新版本 1:是 0:否',
  `desc` varchar(64) DEFAULT NULL COMMENT '描述',
  `enable` tinyint(1) NOT NULL DEFAULT '1',
  `creation_time` datetime DEFAULT NULL,
  `creator` int(11) DEFAULT NULL,
  `memo` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_project_env_name_version` (`project`,`env`,`name`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 apollo.tb_config_item 结构
CREATE TABLE IF NOT EXISTS `tb_config_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project` varchar(32) DEFAULT NULL COMMENT '项目编码',
  `env` varchar(32) NOT NULL COMMENT '环境编码',
  `type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '类型 1:String 2:Number 3:Boolean 4:List 5:Map',
  `risk_level` tinyint(1) NOT NULL DEFAULT '1' COMMENT '风险等级 0-10 数字越大 修改风险越高',
  `key` varchar(64) NOT NULL COMMENT '配置key',
  `name` varchar(32) DEFAULT NULL COMMENT '名称，可用于展示',
  `value` varchar(512) NOT NULL COMMENT '配置值',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本',
  `latest` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否最新版本 1:是 0:否',
  `desc` varchar(64) DEFAULT NULL COMMENT '描述',
  `enable` tinyint(1) NOT NULL DEFAULT '1',
  `creation_time` datetime DEFAULT NULL,
  `creator` int(11) DEFAULT NULL,
  `memo` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_project_env_key` (`project`,`env`,`key`,`version`),
  KEY `idx_key` (`key`),
  KEY `idx_project_env` (`project`,`env`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 apollo.tb_environment 结构
CREATE TABLE IF NOT EXISTS `tb_environment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL,
  `desc` varchar(64) DEFAULT NULL COMMENT '认证信息',
  `enable` tinyint(1) NOT NULL DEFAULT '1',
  `creation_time` datetime DEFAULT NULL,
  `creator` int(11) DEFAULT NULL,
  `modification_time` datetime DEFAULT NULL,
  `modifier` int(11) DEFAULT NULL,
  `memo` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 apollo.tb_project 结构
CREATE TABLE IF NOT EXISTS `tb_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL,
  `principal` varchar(32) NOT NULL COMMENT '负责人',
  `phone` varchar(16) NOT NULL COMMENT '电话',
  `email` varchar(32) NOT NULL COMMENT '邮箱',
  `desc` varchar(64) DEFAULT NULL COMMENT '描述',
  `enable` tinyint(1) NOT NULL DEFAULT '1',
  `creation_time` datetime DEFAULT NULL,
  `creator` int(11) DEFAULT NULL,
  `modification_time` datetime DEFAULT NULL,
  `modifier` int(11) DEFAULT NULL,
  `memo` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 apollo.tb_security 结构
CREATE TABLE IF NOT EXISTS `tb_security` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project` varchar(32) NOT NULL,
  `env` varchar(32) NOT NULL,
  `certification` varchar(32) NOT NULL COMMENT '认证信息 uuid',
  `whitelists` varchar(256) NOT NULL DEFAULT '0.0.0.0' COMMENT '白名单,0.0.0.0表示不限制',
  `creation_time` datetime DEFAULT NULL,
  `creator` int(11) DEFAULT NULL,
  `modification_time` datetime DEFAULT NULL,
  `modifier` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_project_env` (`project`,`env`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

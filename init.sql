-- MySQL 首次初始化脚本（仅在数据卷为空时执行）
-- Spring Boot 启动时也会通过 schema.sql 自动建表（CREATE TABLE IF NOT EXISTS）

USE message_board;

CREATE TABLE IF NOT EXISTS usermessage (
    id         INT PRIMARY KEY AUTO_INCREMENT,
    username   VARCHAR(100) NOT NULL,
    message    TEXT         NOT NULL,
    createtime DATETIME
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS operation_log (
    id       INT PRIMARY KEY AUTO_INCREMENT,
    time     VARCHAR(20)  NOT NULL,
    ip       VARCHAR(50),
    username VARCHAR(100),
    action   VARCHAR(50)  NOT NULL,
    detail   VARCHAR(500)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

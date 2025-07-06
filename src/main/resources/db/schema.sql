-- 创建数据库
CREATE DATABASE IF NOT EXISTS canteen DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE canteen;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(64) NOT NULL COMMENT '用户名',
    password VARCHAR(64) NOT NULL COMMENT '密码',
    phone VARCHAR(11) COMMENT '手机号',
    user_type TINYINT NOT NULL COMMENT '用户类型 1-学生 2-管理员',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_username (username)
) COMMENT '用户表';

-- 菜品表
CREATE TABLE IF NOT EXISTS dish (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜品ID',
    name VARCHAR(64) NOT NULL COMMENT '菜品名称',
    price DECIMAL(10,2) NOT NULL COMMENT '菜品价格',
    description VARCHAR(200) COMMENT '菜品描述',
    category VARCHAR(50) COMMENT '菜品分类',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '菜品状态 0-停售 1-起售',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
) COMMENT '菜品表';

-- 套餐表
CREATE TABLE IF NOT EXISTS setmeal (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '套餐ID',
    name VARCHAR(64) NOT NULL COMMENT '套餐名称',
    price DECIMAL(10,2) NOT NULL COMMENT '套餐价格',
    description VARCHAR(200) COMMENT '套餐描述',
    vitamin_type VARCHAR(100) COMMENT '维生素类型，多个用逗号分隔',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '套餐状态 0-停售 1-起售',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
) COMMENT '套餐表';

-- 套餐菜品关联表
CREATE TABLE IF NOT EXISTS setmeal_dish (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '套餐菜品关联ID',
    setmeal_id BIGINT NOT NULL COMMENT '套餐ID',
    dish_id BIGINT NOT NULL COMMENT '菜品ID',
    dish_name VARCHAR(64) NOT NULL COMMENT '菜品名称',
    dish_price DECIMAL(10,2) NOT NULL COMMENT '菜品价格',
    copies INT NOT NULL COMMENT '份数',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (setmeal_id) REFERENCES setmeal(id),
    FOREIGN KEY (dish_id) REFERENCES dish(id)
) COMMENT '套餐菜品关联表';

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(32) NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '订单金额',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态 0-待支付 1-已支付 2-已完成 3-已取消',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES user(id)
) COMMENT '订单表';

-- 订单详情表
CREATE TABLE IF NOT EXISTS order_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单详情ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    dish_id BIGINT COMMENT '菜品ID',
    setmeal_id BIGINT COMMENT '套餐ID',
    quantity INT NOT NULL COMMENT '数量',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (dish_id) REFERENCES dish(id),
    FOREIGN KEY (setmeal_id) REFERENCES setmeal(id)
) COMMENT '订单详情表'; 
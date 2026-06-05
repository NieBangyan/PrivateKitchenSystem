-- 创建数据库
CREATE DATABASE private_kitchen;
USE private_kitchen;

-- 用户表
CREATE TABLE `user` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `real_name` VARCHAR(50),
    `email` VARCHAR(100),
    `phone` VARCHAR(20),
    `role` ENUM('admin', 'user') DEFAULT 'user',
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_email CHECK (email LIKE '%@%.%'),
    CONSTRAINT chk_phone CHECK (phone REGEXP '^1[3-9][0-9]{9}$')
);

-- 菜谱分类表
CREATE TABLE `category` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL UNIQUE,
    `description` VARCHAR(200),
    `sort_order` INT DEFAULT 0
);

-- 菜谱表
CREATE TABLE `recipe` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `title` VARCHAR(100) NOT NULL,
    `category_id` INT NOT NULL,
    `ingredients` TEXT NOT NULL,
    `steps` TEXT NOT NULL,
    `cooking_time` INT DEFAULT 30,
    `difficulty` ENUM('easy', 'medium', 'hard') DEFAULT 'medium',
    `image_url` VARCHAR(500),
    `author_id` INT NOT NULL,
    `view_count` INT DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`category_id`) REFERENCES `category`(`id`) ON DELETE RESTRICT,
    FOREIGN KEY (`author_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX idx_category (`category_id`),
    INDEX idx_title (`title`),
    INDEX idx_author (`author_id`)
);

-- 插入示例数据
INSERT INTO `user` (`username`, `password`, `real_name`, `email`, `phone`, `role`) VALUES
('admin', '123456', '管理员', 'admin@kitchen.com', '13800000000', 'admin'),
('chef_wang', '123456', '王大厨', 'wang@kitchen.com', '13912345678', 'user');

INSERT INTO `category` (`name`, `description`, `sort_order`) VALUES
('川菜', '麻辣鲜香，四川特色', 1),
('粤菜', '清淡鲜美，广东风味', 2),
('家常菜', '简单易做，家庭美味', 3);

INSERT INTO `recipe` (`title`, `category_id`, `ingredients`, `steps`, `cooking_time`, `difficulty`, `author_id`) VALUES
('麻婆豆腐', 1, '豆腐500g、牛肉末100g、豆瓣酱2勺、花椒适量', '1.豆腐切块焯水\n2.炒香牛肉末\n3.加入豆瓣酱炒出红油\n4.加入豆腐煮5分钟\n5.勾芡出锅', 20, 'easy', 2),
('清蒸鲈鱼', 2, '鲈鱼1条、姜丝、葱丝、蒸鱼豉油', '1.鱼处理干净\n2.放姜丝蒸8分钟\n3.倒掉汤汁\n4.淋豉油放葱丝\n5.浇热油', 25, 'medium', 2);
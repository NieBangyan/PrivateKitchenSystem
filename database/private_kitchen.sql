
CREATE DATABASE IF NOT EXISTS `private_kitchen` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `private_kitchen`;



-- 1. 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `role` ENUM('admin', 'user') DEFAULT 'user' COMMENT '角色',
    `status` TINYINT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    `recipe_count` INT DEFAULT 0 COMMENT '发布菜谱数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT `chk_email` CHECK (`email` LIKE '%@%.%'),
    CONSTRAINT `chk_phone` CHECK (`phone` REGEXP '^1[3-9][0-9]{9}$')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 分类表
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    `name` VARCHAR(50) NOT NULL UNIQUE COMMENT '分类名称',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '分类描述',
    `sort_order` INT DEFAULT 0 COMMENT '排序值',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜谱分类表';

-- 3. 菜谱表
DROP TABLE IF EXISTS `recipe`;
CREATE TABLE `recipe` (
    `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '菜谱ID',
    `title` VARCHAR(100) NOT NULL COMMENT '菜谱标题',
    `category_id` INT NOT NULL COMMENT '分类ID',
    `ingredients` TEXT NOT NULL COMMENT '食材清单',
    `steps` TEXT NOT NULL COMMENT '制作步骤',
    `cooking_time` INT DEFAULT 30 COMMENT '烹饪时间(分钟)',
    `difficulty` ENUM('easy', 'medium', 'hard') DEFAULT 'medium' COMMENT '难度',
    `image_url` VARCHAR(500) DEFAULT NULL COMMENT '图片URL',
    `author_id` INT NOT NULL COMMENT '作者ID',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`category_id`) REFERENCES `category`(`id`) ON DELETE RESTRICT,
    FOREIGN KEY (`author_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜谱表';

-- 4. 评论表
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    `recipe_id` INT NOT NULL COMMENT '菜谱ID',
    `user_id` INT NOT NULL COMMENT '用户ID',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`recipe_id`) REFERENCES `recipe`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';


CREATE UNIQUE INDEX `idx_category_name` ON `category` (`name`);


CREATE INDEX `idx_recipe_category` ON `recipe` (`category_id`);
CREATE INDEX `idx_recipe_title` ON `recipe` (`title`);
CREATE INDEX `idx_recipe_author` ON `recipe` (`author_id`);
CREATE INDEX `idx_recipe_create_time` ON `recipe` (`create_time`);

CREATE INDEX `idx_comment_recipe` ON `comment` (`recipe_id`);
CREATE INDEX `idx_comment_create_time` ON `comment` (`create_time`);


CREATE OR REPLACE VIEW `v_recipe_stats` AS
SELECT 
    c.id AS category_id,
    c.name AS category_name,
    COUNT(r.id) AS recipe_count,
    IFNULL(ROUND(AVG(r.view_count), 0), 0) AS avg_views,
    IFNULL(SUM(r.view_count), 0) AS total_views
FROM category c
LEFT JOIN recipe r ON c.id = r.category_id
GROUP BY c.id, c.name
ORDER BY recipe_count DESC;

CREATE OR REPLACE VIEW `v_user_activity` AS
SELECT 
    u.id AS user_id,
    u.username,
    u.real_name,
    u.role,
    u.recipe_count,
    (SELECT COUNT(*) FROM comment c WHERE c.user_id = u.id) AS comment_count
FROM user u;

CREATE OR REPLACE VIEW `v_hot_recipes` AS
SELECT 
    r.id,
    r.title,
    c.name AS category_name,
    u.username AS author_name,
    (r.view_count + r.like_count * 10 + r.comment_count * 5) AS hot_score
FROM recipe r
LEFT JOIN category c ON r.category_id = c.id
LEFT JOIN user u ON r.author_id = u.id
ORDER BY hot_score DESC;



DELIMITER //
DROP TRIGGER IF EXISTS `tr_recipe_update_time`//
CREATE TRIGGER `tr_recipe_update_time`
BEFORE UPDATE ON `recipe`
FOR EACH ROW
BEGIN
    SET NEW.`update_time` = NOW();
END//
DELIMITER ;

DELIMITER //
DROP TRIGGER IF EXISTS `tr_comment_update_time`//
CREATE TRIGGER `tr_comment_update_time`
BEFORE UPDATE ON `comment`
FOR EACH ROW
BEGIN
    SET NEW.`update_time` = NOW();
END//
DELIMITER ;

DELIMITER //
DROP TRIGGER IF EXISTS `tr_after_insert_recipe`//
CREATE TRIGGER `tr_after_insert_recipe`
AFTER INSERT ON `recipe`
FOR EACH ROW
BEGIN
    UPDATE `user` SET `recipe_count` = `recipe_count` + 1 WHERE `id` = NEW.`author_id`;
END//
DELIMITER ;

DELIMITER //
DROP TRIGGER IF EXISTS `tr_after_delete_recipe`//
CREATE TRIGGER `tr_after_delete_recipe`
AFTER DELETE ON `recipe`
FOR EACH ROW
BEGIN
    UPDATE `user` SET `recipe_count` = `recipe_count` - 1 WHERE `id` = OLD.`author_id`;
END//
DELIMITER ;

DELIMITER //
DROP TRIGGER IF EXISTS `tr_after_insert_comment`//
CREATE TRIGGER `tr_after_insert_comment`
AFTER INSERT ON `comment`
FOR EACH ROW
BEGIN
    UPDATE `recipe` SET `comment_count` = `comment_count` + 1 WHERE `id` = NEW.`recipe_id`;
END//
DELIMITER ;

DELIMITER //
DROP TRIGGER IF EXISTS `tr_after_delete_comment`//
CREATE TRIGGER `tr_after_delete_comment`
AFTER DELETE ON `comment`
FOR EACH ROW
BEGIN
    UPDATE `recipe` SET `comment_count` = `comment_count` - 1 WHERE `id` = OLD.`recipe_id`;
END//
DELIMITER ;

DELIMITER //
DROP TRIGGER IF EXISTS `tr_before_insert_user`//
CREATE TRIGGER `tr_before_insert_user`
BEFORE INSERT ON `user`
FOR EACH ROW
BEGIN
    IF NEW.`role` IS NULL THEN SET NEW.`role` = 'user'; END IF;
    IF NEW.`status` IS NULL THEN SET NEW.`status` = 1; END IF;
END//
DELIMITER ;



DELIMITER //
DROP PROCEDURE IF EXISTS `sp_get_recipes_by_category`//
CREATE PROCEDURE `sp_get_recipes_by_category`(
    IN p_category_id INT,
    IN p_page INT,
    IN p_page_size INT,
    OUT p_total INT
)
BEGIN
    DECLARE v_offset INT DEFAULT (p_page - 1) * p_page_size;
    
    SELECT r.*, u.username AS author_name
    FROM recipe r
    LEFT JOIN user u ON r.author_id = u.id
    WHERE r.category_id = p_category_id
    ORDER BY r.create_time DESC
    LIMIT v_offset, p_page_size;
    
    SELECT COUNT(*) INTO p_total FROM recipe WHERE category_id = p_category_id;
END//
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS `sp_get_hot_recipes`//
CREATE PROCEDURE `sp_get_hot_recipes`(IN p_limit INT)
BEGIN
    IF p_limit IS NULL OR p_limit <= 0 THEN SET p_limit = 10; END IF;
    
    SELECT 
        r.id, r.title, c.name AS category_name, u.username AS author_name,
        (r.view_count + r.like_count * 10 + r.comment_count * 5) AS hot_score
    FROM recipe r
    LEFT JOIN category c ON r.category_id = c.id
    LEFT JOIN user u ON r.author_id = u.id
    ORDER BY hot_score DESC
    LIMIT p_limit;
END//
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS `sp_get_user_full_info`//
CREATE PROCEDURE `sp_get_user_full_info`(IN p_user_id INT)
BEGIN
    SELECT 
        u.*,
        (SELECT COUNT(*) FROM recipe WHERE author_id = u.id) AS total_recipes,
        (SELECT COUNT(*) FROM comment WHERE user_id = u.id) AS total_comments
    FROM user u
    WHERE u.id = p_user_id;
END//
DELIMITER ;

DELIMITER //
DROP FUNCTION IF EXISTS `fn_get_recipe_hot_score`//
CREATE FUNCTION `fn_get_recipe_hot_score`(p_recipe_id INT)
RETURNS INT
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_score INT DEFAULT 0;
    SELECT (view_count + like_count * 10 + comment_count * 5) INTO v_score
    FROM recipe WHERE id = p_recipe_id;
    RETURN v_score;
END//
DELIMITER ;

DELIMITER //
DROP FUNCTION IF EXISTS `fn_get_user_recipe_count`//
CREATE FUNCTION `fn_get_user_recipe_count`(p_user_id INT)
RETURNS INT
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_count INT DEFAULT 0;
    SELECT COUNT(*) INTO v_count FROM recipe WHERE author_id = p_user_id;
    RETURN v_count;
END//
DELIMITER ;



-- 用户数据
INSERT INTO `user` (`id`, `username`, `password`, `real_name`, `email`, `phone`, `role`, `status`) VALUES
(1, 'admin', '123456', '管理员', 'admin@kitchen.com', '13800000000', 'admin', 1),
(2, 'chef_wang', '123456', '王大厨', 'wang@kitchen.com', '13912345678', 'user', 1),
(3, 'chef_li', '123456', '李师傅', 'li@kitchen.com', '13812345678', 'user', 1),
(7, 'chef_an', '123456', 'anjie', 'an@kitchen.com', '17212345561', 'user', 1),
(8, 'zhangwei', '123456', '张伟', 'zhangwei@kitchen.com', '13812345001', 'user', 1),
(9, 'lihong', '123456', '李红', 'lihong@kitchen.com', '13812345002', 'user', 1),
(10, 'wangfang', '123456', '王芳', 'wangfang@kitchen.com', '13812345003', 'user', 1),
(11, 'chenming', '123456', '陈明', 'chenming@kitchen.com', '13812345004', 'user', 1),
(12, 'xiaoming', '123456', '小明', 'xiaoming@kitchen.com', '13812345012', 'user', 1),
(13, 'xiaohong', '123456', '小红', 'xiaohong@kitchen.com', '13812345013', 'user', 1),
(14, 'xiaogang', '123456', '小刚', 'xiaogang@kitchen.com', '13812345014', 'user', 1),
(15, 'xiaoli', '123456', '小丽', 'xiaoli@kitchen.com', '13812345015', 'user', 1),
(16, 'xiaohua', '123456', '小华', 'xiaohua@kitchen.com', '13812345016', 'user', 1),
(17, 'daming', '123456', '大明', 'daming@kitchen.com', '13812345017', 'user', 1),
(18, 'meimei', '123456', '美美', 'meimei@kitchen.com', '13812345018', 'user', 1),
(19, 'qiangge', '123456', '强哥', 'qiangge@kitchen.com', '13812345019', 'user', 1);

INSERT INTO `category` (`id`, `name`, `description`, `sort_order`) VALUES
(1, '川菜', '麻辣鲜香，四川特色传统菜系，代表菜有麻婆豆腐、宫保鸡丁等', 1),
(2, '粤菜', '清淡鲜美，广东风味，讲究原汁原味', 2),
(3, '家常菜', '简单易做，家庭美味，适合日常烹饪', 3),
(4, '西餐', '西式料理，牛排、意面、披萨等', 4),
(5, '甜品', '甜点、蛋糕、布丁、冰淇淋等', 5);

INSERT INTO `recipe` (`id`, `title`, `category_id`, `ingredients`, `steps`, `cooking_time`, `difficulty`, `image_url`, `author_id`, `view_count`, `like_count`) VALUES

(3, '麻婆豆腐', 1,
 '【主料】\n• 嫩豆腐 500g\n• 牛肉末 100g\n\n【调料】\n• 豆瓣酱 2勺\n• 花椒粉 1茶匙\n• 水淀粉 适量',
 '【步骤】\n1. 豆腐切块焯水\n2. 炒香牛肉末\n3. 加入豆瓣酱炒出红油\n4. 加入豆腐煮5分钟\n5. 勾芡出锅',
 20, 'easy', '/images/MapoTofu.png', 2, 156, 25),

(6, '宫保鸡丁', 1,
 '【主料】\n• 鸡腿肉 300g\n• 花生米 50g\n\n【配料】\n• 干辣椒 10个\n• 花椒 1小把\n\n【调料】\n• 生抽 2勺\n• 醋 1勺\n• 糖 1勺',
 '【步骤】\n1. 鸡肉切丁腌制\n2. 花生米炸熟\n3. 炒香花椒辣椒\n4. 下鸡丁滑炒\n5. 倒入料汁\n6. 加花生米翻炒',
 30, 'medium', '/images/Chicken.png', 2, 203, 18),

(16, '冷吃兔', 1,
 '【主料】\n• 兔肉 500g\n\n【配料】\n• 干辣椒 50g\n• 花椒 20g\n\n【调料】\n• 料酒 2勺\n• 生抽 2勺\n• 老抽 1勺\n• 白糖 1勺',
 '【步骤】\n1. 兔肉切块腌制\n2. 炸至变色捞出\n3. 炒香干辣椒花椒\n4. 倒入兔肉翻炒\n5. 调味后小火炒干\n6. 撒白芝麻出锅',
 40, 'hard', '/images/rabbit.png', 10, 89, 12),

(4, '清蒸鲈鱼', 2,
 '【主料】\n• 鲈鱼 1条\n\n【配料】\n• 姜丝 30g\n• 葱丝 30g\n\n【调料】\n• 蒸鱼豉油 3勺\n• 食用油 2勺',
 '【步骤】\n1. 鱼处理干净\n2. 放姜丝蒸8分钟\n3. 倒掉汤汁\n4. 淋豉油放葱丝\n5. 浇热油',
 25, 'medium', '/images/fish.png', 2, 98, 15),

(5, '番茄炒蛋', 3,
 '【主料】\n• 鸡蛋 4个\n• 番茄 3个\n\n【调料】\n• 盐 1茶匙\n• 糖 1茶匙',
 '【步骤】\n1. 鸡蛋打散\n2. 番茄切块\n3. 炒熟鸡蛋盛出\n4. 炒番茄出汁\n5. 倒入鸡蛋翻炒',
 10, 'easy', '/images/tomato_egg.png', 7, 567, 35),

(15, '手工饺子', 3,
 '【主料-皮】\n• 面粉 500g\n• 温水 250ml\n\n【主料-馅】\n• 猪肉末 300g\n• 白菜 200g\n• 香菇 5朵',
 '【步骤】\n1. 和面醒发30分钟\n2. 调馅搅拌上劲\n3. 擀皮包饺子\n4. 水开后下饺子\n5. 煮至浮起加凉水\n6. 重复2次即可',
 60, 'medium', '/images/Dumpling.png', 10, 234, 20),

(18, '鸡蛋羹', 3,
 '【主料】\n• 鸡蛋 3个\n• 温水 250ml\n\n【调料】\n• 盐 1/2茶匙\n• 生抽 1勺\n• 香油 几滴',
 '【步骤】\n1. 鸡蛋打散加盐\n2. 加入温水搅匀\n3. 过滤泡沫\n4. 盖上保鲜膜扎孔\n5. 蒸8-10分钟\n6. 淋生抽和香油',
 15, 'easy', '/images/egg.png', 8, 178, 22),

(17, '蚂蚁上树', 3,
 '【主料】\n• 粉丝 100g\n• 猪肉末 150g\n\n【调料】\n• 豆瓣酱 1勺\n• 生抽 1勺\n• 老抽 1/2勺',
 '【步骤】\n1. 粉丝泡软\n2. 炒香肉末\n3. 加豆瓣酱炒出红油\n4. 加调料和水\n5. 放入粉丝煮至收汁\n6. 撒葱花出锅',
 25, 'medium', '/images/mayishangshu.png', 9, 145, 16),

(19, '经典意大利肉酱面', 4,
 '【主料】\n• 意大利面条 150g\n• 猪肉末 150g\n• 番茄 2个\n\n【配料】\n• 洋葱 半个\n• 大蒜 3瓣\n• 番茄酱 2勺',
 '【步骤】\n1. 煮面8-10分钟\n2. 炒香蒜末洋葱\n3. 加入肉末炒至变色\n4. 加入番茄丁和番茄酱\n5. 调味后浇在面上',
 25, 'medium', '/images/pasta.png', 8, 89, 35),

(20, '香煎牛排', 4,
 '【主料】\n• 牛排 200g\n• 黄油 20g\n\n【配料】\n• 大蒜 2瓣\n• 迷迭香 2根\n\n【调料】\n• 海盐 适量\n• 黑胡椒 适量',
 '【步骤】\n1. 牛排室温回温\n2. 吸干水分撒盐和胡椒\n3. 热锅大火煎1分钟\n4. 翻面再煎1分钟\n5. 加黄油大蒜迷迭香\n6. 淋油2-3分钟\n7. 醒肉5分钟',
 15, 'hard', '/images/steak.png', 9, 156, 48),

(21, '自制披萨', 4,
 '【主料-饼皮】\n• 高筋面粉 200g\n• 酵母 3g\n• 温水 120ml\n\n【主料-馅料】\n• 披萨酱 3勺\n• 马苏里拉芝士 150g\n• 香肠 50g\n• 蘑菇 3个',
 '【步骤】\n1. 和面发酵至2倍大\n2. 擀成圆形薄饼\n3. 涂抹披萨酱\n4. 铺芝士和配料\n5. 烤箱220度烤15-20分钟',
 50, 'medium', '/images/pizza.png', 8, 67, 32),
(22, '提拉米苏', 5,
 '【主料】\n• 马斯卡彭奶酪 250g\n• 淡奶油 150ml\n• 蛋黄 2个\n• 细砂糖 50g\n• 手指饼干 1包\n\n【配料】\n• 浓缩咖啡 1杯\n• 咖啡酒 2勺\n• 可可粉 适量',
 '【步骤】\n1. 蛋黄加糖隔水加热\n2. 加入马斯卡彭拌匀\n3. 淡奶油打发\n4. 奶酪糊和奶油混合\n5. 手指饼干蘸咖啡液\n6. 铺一层饼干一层糊\n7. 冷藏4小时\n8. 筛可可粉',
 40, 'hard', '/images/tiramisu.png', 10, 234, 65),

(23, '焦糖布丁', 5,
 '【焦糖材料】\n• 细砂糖 50g\n• 水 15ml\n\n【布丁材料】\n• 鸡蛋 2个\n• 蛋黄 1个\n• 细砂糖 40g\n• 牛奶 250ml\n• 淡奶油 50ml',
 '【步骤】\n1. 糖加水熬至琥珀色\n2. 倒入模具\n3. 鸡蛋加糖打散\n4. 加入牛奶和淡奶油\n5. 过筛2次\n6. 水浴法150度烤40分钟\n7. 冷藏2小时脱模',
 50, 'medium', '/images/pudding.png', 11, 167, 38),

(24, '芒果慕斯', 5,
 '【饼底材料】\n• 消化饼干 80g\n• 黄油 40g\n\n【慕斯材料】\n• 芒果泥 200g\n• 淡奶油 200ml\n• 细砂糖 40g\n• 吉利丁片 10g',
 '【步骤】\n1. 饼干压碎加黄油铺底\n2. 吉利丁泡软\n3. 芒果打成泥\n4. 淡奶油打至6分发\n5. 芒果泥加吉利丁拌匀\n6. 倒入淡奶油拌匀\n7. 倒入模具冷藏4小时',
 60, 'hard', '/images/mango_mousse.png', 2, 89, 42);
INSERT INTO `comment` (`id`, `recipe_id`, `user_id`, `content`, `like_count`) VALUES
(1, 3, 2, '太好吃啦！麻婆豆腐是我的最爱！', 5),
(2, 3, 7, '做得不错，就是有点咸', 2),
(3, 3, 8, '麻辣鲜香，下饭神器', 12),
(4, 3, 10, '豆腐很嫩，牛肉末很香', 8),
(5, 4, 1, '鱼很新鲜，蒸的时间刚刚好', 8),
(6, 4, 2, '简单易学，第一次就成功了', 4),
(7, 4, 3, '淋热油这步很关键，学到了', 6),
(8, 5, 1, '最家常的菜，但做好也不容易', 10),
(9, 5, 2, '番茄炒蛋yyds！', 7),
(10, 5, 9, '加一点点糖更好吃', 3),
(11, 5, 10, '简单美味，孩子很喜欢', 5),
(12, 5, 11, '国民菜，简单又好吃', 15),
(13, 5, 7, '加一点点糖更好吃', 9),
(14, 6, 1, '宫保鸡丁很正宗，花生米很脆', 4),
(15, 6, 7, '鸡丁很嫩，辣度刚好', 6),
(16, 6, 11, '又麻又辣，太下饭了', 7),
(17, 15, 2, '自己包的饺子就是香', 4),
(18, 15, 1, '皮薄馅大，教程很详细', 3),
(19, 15, 8, '和面技巧很实用', 5),
(20, 16, 1, '麻辣鲜香，冷吃兔绝了！', 8),
(21, 16, 7, '就是有点辣，但是很过瘾', 5),
(22, 16, 9, '下酒神器！', 10),
(23, 17, 2, '蚂蚁上树很下饭', 3),
(24, 17, 1, '粉丝吸满了汤汁，太好吃了', 2),
(25, 17, 10, '简单快手，上班族福音', 4),
(26, 18, 7, '蒸鸡蛋羹真的很嫩，学到了', 2),
(27, 18, 1, '加温水这步很关键', 1),
(28, 18, 11, '小时候的味道', 3),
(29, 19, 12, '第一次做就成功了，教程超详细！', 8),
(30, 19, 13, '肉酱可以多做点，拌面拌饭都好吃', 12),
(31, 19, 14, '罗勒碎是灵魂，一定要加', 5),
(32, 19, 15, '比外卖好吃太多了', 10),
(33, 19, 16, '孩子最爱吃这个，每次都光盘', 15),
(34, 20, 17, '按照这个方法煎，比西餐厅还好吃', 22),
(35, 20, 18, '醒肉很重要，之前都不知道', 14),
(36, 20, 19, '黄油大蒜迷迭香绝配', 18),
(37, 20, 12, '第一次煎牛排，七分熟刚刚好', 9),
(38, 20, 13, '大火煎出焦壳，太香了', 16),
(39, 21, 14, '面团发酵很成功，饼皮很脆', 7),
(40, 21, 15, '芝士拉丝效果太好了', 11),
(41, 21, 16, '自己做的料超足，完胜必胜客', 20),
(42, 21, 17, '烤的时候满屋飘香', 8),
(43, 21, 18, '可以放任意喜欢的食材', 6),
(44, 22, 19, '甜品店水平，朋友都说好吃', 30),
(45, 22, 12, '咖啡酒不要放太多，会苦', 12),
(46, 22, 13, '冷藏一夜口感更好', 18),
(47, 22, 14, '马斯卡彭一定要用进口的', 22),
(48, 22, 15, '做了好多次了，零失败配方', 25),
(49, 23, 16, '布丁很嫩，焦糖微苦很解腻', 9),
(50, 23, 17, '水浴法很关键，烤出来很嫩', 11),
(51, 23, 18, '小朋友超级爱吃', 15),
(52, 23, 19, '简单好做，下午茶必备', 8),
(53, 23, 12, '容器可以用小杯子，很可爱', 10),
(54, 24, 13, '夏天吃太爽了，芒果味浓郁', 20),
(55, 24, 14, '镜面很漂亮，像店里卖的', 14),
(56, 24, 15, '慕斯很顺滑，入口即化', 16),
(57, 24, 16, '芒果要选熟透的才甜', 11),
(58, 24, 17, '饼干底可以用奥利奥，别有风味', 13);


-- 查看所有表
SHOW TABLES;

-- 查看所有索引
SHOW INDEX FROM `user`;
SHOW INDEX FROM `category`;
SHOW INDEX FROM `recipe`;
SHOW INDEX FROM `comment`;

-- 查看所有视图
SHOW FULL TABLES WHERE Table_type = 'VIEW';

-- 查看所有触发器
SHOW TRIGGERS;

-- 查看所有存储过程
SHOW PROCEDURE STATUS WHERE Db = 'private_kitchen';

-- 查看所有存储函数
SHOW FUNCTION STATUS WHERE Db = 'private_kitchen';

SELECT '用户数' AS 统计项, COUNT(*) AS 数量 FROM user
UNION SELECT '分类数', COUNT(*) FROM category
UNION SELECT '菜谱数', COUNT(*) FROM recipe
UNION SELECT '评论数', COUNT(*) FROM comment;
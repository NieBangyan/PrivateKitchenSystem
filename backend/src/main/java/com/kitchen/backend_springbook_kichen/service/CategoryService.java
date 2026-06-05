package com.kitchen.backend_springbook_kichen.service;

import com.kitchen.backend_springbook_kichen.entity.Category;
import java.util.List;
import java.util.Map;

/**
 * 菜谱分类服务接口
 */
public interface CategoryService {

    /**
     * 获取所有分类
     * @return 分类列表
     */
    List<Category> getAllCategories();

    /**
     * 根据ID获取分类
     * @param id 分类ID
     * @return 分类信息
     */
    Category getCategoryById(Integer id);

    /**
     * 根据名称获取分类
     * @param name 分类名称
     * @return 分类信息
     */
    Category getCategoryByName(String name);

    /**
     * 搜索分类
     * @param keyword 关键词
     * @return 分类列表
     */
    List<Category> searchCategories(String keyword);

    /**
     * 添加分类
     * @param category 分类信息
     * @return 添加后的分类
     */
    Category addCategory(Category category);

    /**
     * 更新分类
     * @param category 分类信息
     * @return 更新后的分类
     */
    Category updateCategory(Category category);

    /**
     * 删除分类（检查参照完整性）
     * @param id 分类ID
     * @return 是否成功
     */
    boolean deleteCategory(Integer id);

    /**
     * 批量删除分类
     * @param ids ID列表
     * @return 是否成功
     */
    boolean batchDeleteCategories(List<Integer> ids);

    /**
     * 获取分类统计（含菜谱数量）- 聚合函数
     * @return 统计数据
     */
    List<Map<String, Object>> getCategoryStats();

    /**
     * 获取分类总数
     * @return 总数
     */
    long getCategoryCount();
}
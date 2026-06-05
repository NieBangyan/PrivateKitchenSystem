package com.kitchen.backend_springbook_kichen.service.impl;

import com.kitchen.backend_springbook_kichen.entity.Category;
import com.kitchen.backend_springbook_kichen.mapper.CategoryMapper;
import com.kitchen.backend_springbook_kichen.mapper.RecipeMapper;
import com.kitchen.backend_springbook_kichen.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 菜谱分类服务实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RecipeMapper recipeMapper;

    /**
     * 验证分类数据合法性
     */
    private void validateCategory(Category category, boolean isNew) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new RuntimeException("分类名称不能为空");
        }
        if (category.getName().length() > 50) {
            throw new RuntimeException("分类名称不能超过50个字符");
        }

        if (isNew) {
            Category existing = categoryMapper.findByName(category.getName());
            if (existing != null) {
                throw new RuntimeException("分类名称已存在");
            }
        }
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.findAll();
    }

    @Override
    public Category getCategoryById(Integer id) {
        if (id == null) {
            throw new RuntimeException("分类ID不能为空");
        }
        Category category = categoryMapper.findById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        return category;
    }

    @Override
    public Category getCategoryByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("分类名称不能为空");
        }
        return categoryMapper.findByName(name);
    }

    @Override
    public List<Category> searchCategories(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCategories();
        }
        return categoryMapper.search(keyword);
    }

    @Override
    @Transactional
    public Category addCategory(Category category) {
        validateCategory(category, true);

        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }

        int result = categoryMapper.insert(category);
        if (result > 0) {
            return categoryMapper.findById(category.getId());
        }
        throw new RuntimeException("添加分类失败");
    }

    @Override
    @Transactional
    public Category updateCategory(Category category) {
        if (category.getId() == null) {
            throw new RuntimeException("分类ID不能为空");
        }

        Category existing = categoryMapper.findById(category.getId());
        if (existing == null) {
            throw new RuntimeException("分类不存在");
        }

        validateCategory(category, false);

        // 检查名称是否与其他分类重复
        Category nameCheck = categoryMapper.findByName(category.getName());
        if (nameCheck != null && !nameCheck.getId().equals(category.getId())) {
            throw new RuntimeException("分类名称已存在");
        }

        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        existing.setSortOrder(category.getSortOrder());

        int result = categoryMapper.update(existing);
        if (result > 0) {
            return categoryMapper.findById(category.getId());
        }
        throw new RuntimeException("更新分类失败");
    }

    @Override
    @Transactional
    public boolean deleteCategory(Integer id) {
        if (id == null) {
            throw new RuntimeException("分类ID不能为空");
        }

        Category category = categoryMapper.findById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }

        // 检查参照完整性：该分类下是否有菜谱
        long count = recipeMapper.countByCategory(id);
        if (count > 0) {
            throw new RuntimeException("该分类下还有 " + count + " 个菜谱，请先删除或移动菜谱");
        }

        int result = categoryMapper.deleteById(id);
        return result > 0;
    }

    @Override
    @Transactional
    public boolean batchDeleteCategories(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("请选择要删除的分类");
        }

        // 检查每个分类下是否有菜谱
        for (Integer id : ids) {
            long count = recipeMapper.countByCategory(id);
            if (count > 0) {
                Category category = categoryMapper.findById(id);
                throw new RuntimeException("分类【" + category.getName() + "】下还有 " + count + " 个菜谱，无法删除");
            }
        }

        int result = categoryMapper.batchDelete(ids);
        return result > 0;
    }

    @Override
    public List<Map<String, Object>> getCategoryStats() {
        return categoryMapper.getCategoryStats();
    }

    @Override
    public long getCategoryCount() {
        return categoryMapper.count();
    }
}
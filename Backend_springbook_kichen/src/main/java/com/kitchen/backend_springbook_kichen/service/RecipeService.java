package com.kitchen.backend_springbook_kichen.service;

import com.kitchen.backend_springbook_kichen.entity.Recipe;
import com.kitchen.backend_springbook_kichen.mapper.RecipeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class RecipeService {

    @Autowired
    private RecipeMapper recipeMapper;

    // 数据合法性检查
    private boolean validateRecipe(Recipe recipe) {
        if (recipe.getTitle() == null || recipe.getTitle().trim().isEmpty()) {
            throw new RuntimeException("菜谱标题不能为空");
        }
        if (recipe.getTitle().length() > 100) {
            throw new RuntimeException("菜谱标题长度不能超过100个字符");
        }
        if (recipe.getIngredients() == null || recipe.getIngredients().trim().isEmpty()) {
            throw new RuntimeException("食材清单不能为空");
        }
        if (recipe.getSteps() == null || recipe.getSteps().trim().isEmpty()) {
            throw new RuntimeException("制作步骤不能为空");
        }
        if (recipe.getCookingTime() != null && recipe.getCookingTime() <= 0) {
            throw new RuntimeException("烹饪时间必须大于0");
        }
        if (recipe.getCookingTime() != null && recipe.getCookingTime() > 600) {
            throw new RuntimeException("烹饪时间不能超过600分钟");
        }
        return true;
    }

    // 查询 - 支持单条件、多条件组合、模糊查询
    public Map<String, Object> searchRecipes(String title, Integer categoryId, String difficulty,
                                             Integer page, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("categoryId", categoryId);
        params.put("difficulty", difficulty);
        params.put("offset", (page - 1) * pageSize);
        params.put("pageSize", pageSize);

        List<Recipe> list = recipeMapper.search(params);
        Long total = recipeMapper.countSearch(params);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        return result;
    }

    // 插入 - 带合法性检查
    @Transactional
    public Recipe addRecipe(Recipe recipe) {
        validateRecipe(recipe);
        int result = recipeMapper.insert(recipe);
        if (result > 0) {
            return recipe;
        }
        throw new RuntimeException("添加菜谱失败");
    }

    // 修改 - 带合法性检查和参照完整性
    @Transactional
    public Recipe updateRecipe(Recipe recipe) {
        Recipe existing = recipeMapper.findById(recipe.getId());
        if (existing == null) {
            throw new RuntimeException("菜谱不存在");
        }
        validateRecipe(recipe);
        int result = recipeMapper.update(recipe);
        if (result > 0) {
            return recipeMapper.findDetailById(recipe.getId());
        }
        throw new RuntimeException("更新菜谱失败");
    }

    // 删除 - 带参照完整性检查（级联删除由数据库外键处理）
    @Transactional
    public boolean deleteRecipe(Integer id) {
        Recipe recipe = recipeMapper.findById(id);
        if (recipe == null) {
            throw new RuntimeException("菜谱不存在");
        }
        int result = recipeMapper.deleteById(id);
        return result > 0;
    }

    // 获取菜谱详情
    public Recipe getRecipeDetail(Integer id) {
        return recipeMapper.findDetailById(id);
    }

    // 存储过程示例 - 统计分类下的菜谱数量
    // 实际存储过程需要在数据库中创建
    public List<Map<String, Object>> getCategoryStats() {
        // 使用聚合函数查询
        return recipeMapper.getCategoryStats();
    }
}
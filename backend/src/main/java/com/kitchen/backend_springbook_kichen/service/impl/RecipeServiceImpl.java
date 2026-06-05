package com.kitchen.backend_springbook_kichen.service.impl;

import com.kitchen.backend_springbook_kichen.entity.Recipe;
import com.kitchen.backend_springbook_kichen.mapper.RecipeMapper;
import com.kitchen.backend_springbook_kichen.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜谱服务实现类
 */
@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeMapper recipeMapper;

    /**
     * 验证菜谱数据合法性
     */
    private void validateRecipe(Recipe recipe) {
        if (recipe.getTitle() == null || recipe.getTitle().trim().isEmpty()) {
            throw new RuntimeException("菜谱标题不能为空");
        }
        if (recipe.getTitle().length() > 100) {
            throw new RuntimeException("菜谱标题不能超过100个字符");
        }

        if (recipe.getCategoryId() == null) {
            throw new RuntimeException("请选择菜系分类");
        }

        if (recipe.getIngredients() == null || recipe.getIngredients().trim().isEmpty()) {
            throw new RuntimeException("食材清单不能为空");
        }
        if (recipe.getIngredients().length() > 2000) {
            throw new RuntimeException("食材清单不能超过2000个字符");
        }

        if (recipe.getSteps() == null || recipe.getSteps().trim().isEmpty()) {
            throw new RuntimeException("制作步骤不能为空");
        }
        if (recipe.getSteps().length() > 5000) {
            throw new RuntimeException("制作步骤不能超过5000个字符");
        }

        if (recipe.getCookingTime() == null) {
            throw new RuntimeException("烹饪时间不能为空");
        }
        if (recipe.getCookingTime() <= 0) {
            throw new RuntimeException("烹饪时间必须大于0");
        }
        if (recipe.getCookingTime() > 600) {
            throw new RuntimeException("烹饪时间不能超过600分钟");
        }

        if (recipe.getAuthorId() == null) {
            throw new RuntimeException("作者信息不能为空");
        }
    }

    @Override
    public Map<String, Object> searchRecipes(Map<String, Object> params) {
        // 设置默认值
        if (params.get("page") == null) {
            params.put("page", 1);
        }
        if (params.get("pageSize") == null) {
            params.put("pageSize", 10);
        }

        int page = (int) params.get("page");
        int pageSize = (int) params.get("pageSize");
        int offset = (page - 1) * pageSize;

        // 获取参数
        String title = (String) params.get("title");
        Integer categoryId = (Integer) params.get("categoryId");
        String difficulty = (String) params.get("difficulty");

        // 调用 Mapper 方法
        List<Recipe> list = recipeMapper.search(title, categoryId, difficulty, offset, pageSize);
        Long total = recipeMapper.countSearch(title, categoryId, difficulty);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", (total + pageSize - 1) / pageSize);

        return result;
    }

    @Override
    public Recipe getRecipeById(Integer id) {
        if (id == null) {
            throw new RuntimeException("菜谱ID不能为空");
        }
        Recipe recipe = recipeMapper.findById(id);
        if (recipe == null) {
            throw new RuntimeException("菜谱不存在");
        }
        return recipe;
    }

    @Override
    public Recipe getRecipeDetail(Integer id) {
        if (id == null) {
            throw new RuntimeException("菜谱ID不能为空");
        }
        Recipe recipe = recipeMapper.findDetailById(id);
        if (recipe == null) {
            throw new RuntimeException("菜谱不存在");
        }
        // 增加浏览量
        incrementViewCount(id);
        return recipe;
    }

    @Override
    public Recipe getFirstRecipe() {
        return recipeMapper.findFirst();
    }

    @Override
    public Recipe getLastRecipe() {
        return recipeMapper.findLast();
    }

    @Override
    public Recipe getPrevRecipe(Integer currentId) {
        if (currentId == null) {
            throw new RuntimeException("当前菜谱ID不能为空");
        }
        return recipeMapper.findPrev(currentId);
    }

    @Override
    public Recipe getNextRecipe(Integer currentId) {
        if (currentId == null) {
            throw new RuntimeException("当前菜谱ID不能为空");
        }
        return recipeMapper.findNext(currentId);
    }

    @Override
    @Transactional
    public Recipe addRecipe(Recipe recipe) {
        validateRecipe(recipe);

        // 设置默认值
        if (recipe.getDifficulty() == null) {
            recipe.setDifficulty("medium");
        }
        if (recipe.getViewCount() == null) {
            recipe.setViewCount(0);
        }

        int result = recipeMapper.insert(recipe);
        if (result > 0) {
            return recipeMapper.findDetailById(recipe.getId());
        }
        throw new RuntimeException("添加菜谱失败");
    }

    @Override
    @Transactional
    public Recipe updateRecipe(Recipe recipe) {
        if (recipe.getId() == null) {
            throw new RuntimeException("菜谱ID不能为空");
        }

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

    @Override
    @Transactional
    public boolean deleteRecipe(Integer id) {
        if (id == null) {
            throw new RuntimeException("菜谱ID不能为空");
        }

        Recipe recipe = recipeMapper.findById(id);
        if (recipe == null) {
            throw new RuntimeException("菜谱不存在");
        }

        int result = recipeMapper.deleteById(id);
        return result > 0;
    }

    @Override
    @Transactional
    public void incrementViewCount(Integer id) {
        recipeMapper.incrementViewCount(id);
    }

    @Override
    public long getRecipeCount() {
        return recipeMapper.count();
    }

    @Override
    public long getCountByCategory(Integer categoryId) {
        if (categoryId == null) {
            return 0;
        }
        return recipeMapper.countByCategory(categoryId);
    }

    @Override
    public List<Recipe> getHotRecipes(int limit) {
        return recipeMapper.findHotRecipes(limit);
    }

    @Override
    public List<Recipe> getLatestRecipes(int limit) {
        return recipeMapper.findLatestRecipes(limit);
    }

    @Override
    public List<Map<String, Object>> getStatsByDifficulty() {
        return recipeMapper.getStatsByDifficulty();
    }
}
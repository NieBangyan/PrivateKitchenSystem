package com.kitchen.backend_springbook_kichen.service;

import com.kitchen.backend_springbook_kichen.entity.Recipe;
import java.util.List;
import java.util.Map;

/**
 * 菜谱服务接口
 */
public interface RecipeService {

    /**
     * 多条件搜索菜谱（支持模糊查询、分页）
     * @param params 搜索参数（title, categoryId, difficulty, page, pageSize）
     * @return 分页结果
     */
    Map<String, Object> searchRecipes(Map<String, Object> params);

    /**
     * 根据ID获取菜谱详情
     * @param id 菜谱ID
     * @return 菜谱信息
     */
    Recipe getRecipeById(Integer id);

    /**
     * 获取菜谱详情（含分类名、作者名）
     * @param id 菜谱ID
     * @return 菜谱详情
     */
    Recipe getRecipeDetail(Integer id);

    /**
     * 获取第一个菜谱
     * @return 菜谱信息
     */
    Recipe getFirstRecipe();

    /**
     * 获取最后一个菜谱
     * @return 菜谱信息
     */
    Recipe getLastRecipe();

    /**
     * 获取上一个菜谱
     * @param currentId 当前ID
     * @return 上一个菜谱
     */
    Recipe getPrevRecipe(Integer currentId);

    /**
     * 获取下一个菜谱
     * @param currentId 当前ID
     * @return 下一个菜谱
     */
    Recipe getNextRecipe(Integer currentId);

    /**
     * 添加菜谱
     * @param recipe 菜谱信息
     * @return 添加后的菜谱
     */
    Recipe addRecipe(Recipe recipe);

    /**
     * 更新菜谱
     * @param recipe 菜谱信息
     * @return 更新后的菜谱
     */
    Recipe updateRecipe(Recipe recipe);

    /**
     * 删除菜谱
     * @param id 菜谱ID
     * @return 是否成功
     */
    boolean deleteRecipe(Integer id);

    /**
     * 增加浏览量
     * @param id 菜谱ID
     */
    void incrementViewCount(Integer id);

    /**
     * 获取菜谱总数
     * @return 总数
     */
    long getRecipeCount();

    /**
     * 获取分类下的菜谱数量
     * @param categoryId 分类ID
     * @return 数量
     */
    long getCountByCategory(Integer categoryId);

    /**
     * 获取热门菜谱（按浏览量）
     * @param limit 数量限制
     * @return 菜谱列表
     */
    List<Recipe> getHotRecipes(int limit);

    /**
     * 获取最新菜谱
     * @param limit 数量限制
     * @return 菜谱列表
     */
    List<Recipe> getLatestRecipes(int limit);

    /**
     * 获取菜谱统计（按难度分组）
     * @return 统计数据
     */
    List<Map<String, Object>> getStatsByDifficulty();
}
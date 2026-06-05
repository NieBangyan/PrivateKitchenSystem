package com.kitchen.backend_springbook_kichen.mapper;

import com.kitchen.backend_springbook_kichen.entity.Recipe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RecipeMapper {


    Recipe findById(Integer id);

    Recipe findDetailById(Integer id);

    List<Recipe> search(Map<String, Object> params);

    Long countSearch(Map<String, Object> params);


    int insert(Recipe recipe);

    int update(Recipe recipe);

    int deleteById(Integer id);

    void incrementViewCount(Integer id);


    /**
     * 获取第一个菜谱
     */
    Recipe findFirst();

    /**
     * 获取最后一个菜谱
     */
    Recipe findLast();

    /**
     * 获取上一个菜谱
     */
    Recipe findPrev(@Param("currentId") Integer currentId);

    /**
     * 获取下一个菜谱
     */
    Recipe findNext(@Param("currentId") Integer currentId);

    // ========== 统计方法（新增） ==========

    /**
     * 获取菜谱总数
     */
    long count();

    /**
     * 获取分类下的菜谱数量
     */
    long countByCategory(@Param("categoryId") Integer categoryId);

    /**
     * 获取热门菜谱（按浏览量排序）
     */
    List<Recipe> findHotRecipes(@Param("limit") int limit);

    /**
     * 获取最新菜谱
     */
    List<Recipe> findLatestRecipes(@Param("limit") int limit);

    /**
     * 按难度统计菜谱数量
     */
    List<Map<String, Object>> getStatsByDifficulty();

    /**
     * 获取分类下的菜谱统计
     */
    Map<String, Object> getRecipeCountByCategory(@Param("categoryId") Integer categoryId);
}
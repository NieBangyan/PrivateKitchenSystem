package com.kitchen.backend_springbook_kichen.mapper;

import com.kitchen.backend_springbook_kichen.entity.Recipe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RecipeMapper {

    // ========== 基础查询 ==========

    /**
     * 根据ID查询菜谱
     */
    Recipe findById(Integer id);

    /**
     * 查询菜谱详情（含分类名、作者名）
     */
    Recipe findDetailById(Integer id);

    // ========== 搜索方法 ==========

    /**
     * 多条件搜索菜谱
     */
    List<Recipe> search(@Param("title") String title,
                        @Param("categoryId") Integer categoryId,
                        @Param("difficulty") String difficulty,
                        @Param("offset") Integer offset,
                        @Param("pageSize") Integer pageSize);

    /**
     * 统计搜索结果数量
     */
    Long countSearch(@Param("title") String title,
                     @Param("categoryId") Integer categoryId,
                     @Param("difficulty") String difficulty);

    // ========== 增删改 ==========

    /**
     * 新增菜谱
     */
    int insert(Recipe recipe);

    /**
     * 更新菜谱
     */
    int update(Recipe recipe);

    /**
     * 删除菜谱
     */
    int deleteById(Integer id);

    /**
     * 增加浏览量
     */
    void incrementViewCount(Integer id);

    // ========== 浏览导航 ==========

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

    // ========== 统计方法 ==========

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
     * 获取分类下的菜谱数量（返回Long）
     */
    Long getRecipeCountByCategory(@Param("categoryId") Integer categoryId);
}
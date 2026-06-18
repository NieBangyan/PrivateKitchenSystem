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


    List<Recipe> search(@Param("title") String title,
                        @Param("categoryId") Integer categoryId,
                        @Param("difficulty") String difficulty,
                        @Param("offset") Integer offset,
                        @Param("pageSize") Integer pageSize);


    Long countSearch(@Param("title") String title,
                     @Param("categoryId") Integer categoryId,
                     @Param("difficulty") String difficulty);


    int insert(Recipe recipe);


    int update(Recipe recipe);


    int deleteById(Integer id);

    void incrementViewCount(Integer id);


    Recipe findFirst();

    Recipe findLast();


    Recipe findPrev(@Param("currentId") Integer currentId);

    Recipe findNext(@Param("currentId") Integer currentId);


    long count();


    long countByCategory(@Param("categoryId") Integer categoryId);


    List<Recipe> findHotRecipes(@Param("limit") int limit);

    List<Recipe> findLatestRecipes(@Param("limit") int limit);

    List<Map<String, Object>> getStatsByDifficulty();

    int likeRecipe(@Param("id") Integer id);

    Long getRecipeCountByCategory(@Param("categoryId") Integer categoryId);
}
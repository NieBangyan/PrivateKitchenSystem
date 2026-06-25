package com.kitchen.backend_springbook_kichen.service;

import com.kitchen.backend_springbook_kichen.entity.Recipe;

import java.util.List;
import java.util.Map;

public interface RecipeService {

    Map<String, Object> searchRecipes(Map<String, Object> params);

    Recipe getRecipeById(Integer id);

    Recipe getRecipeDetail(Integer id);

    Recipe getFirstRecipe();

    Recipe getLastRecipe();

    Recipe getPrevRecipe(Integer currentId);

    Recipe getNextRecipe(Integer currentId);

    Recipe addRecipe(Recipe recipe);

    Recipe updateRecipe(Recipe recipe);

    boolean deleteRecipe(Integer id);

    void incrementViewCount(Integer id);

    long getRecipeCount();

    long getCountByCategory(Integer categoryId);

    List<Recipe> getHotRecipes(int limit);

    List<Recipe> getLatestRecipes(int limit);

    List<Map<String, Object>> getStatsByDifficulty();

    boolean likeRecipe(Integer id);
}
package com.kitchen.backend_springbook_kichen.controller;

import com.kitchen.backend_springbook_kichen.entity.Recipe;
import com.kitchen.backend_springbook_kichen.service.CategoryService;
import com.kitchen.backend_springbook_kichen.service.RecipeService;
import com.kitchen.backend_springbook_kichen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    /**
     * GET /api/recipe/search?title=xxx&categoryId=1&difficulty=easy&page=1&pageSize=10
     */
    @GetMapping("/search")
    public Map<String, Object> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String difficulty,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("title", title);
            params.put("categoryId", categoryId);
            params.put("difficulty", difficulty);
            params.put("page", page);
            params.put("pageSize", pageSize);

            Map<String, Object> data = recipeService.searchRecipes(params);
            result.put("code", 200);
            result.put("data", data);
            result.put("message", "search completed");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * GET /api/recipe/detail/{id}
     */
    @GetMapping("/detail/{id}")
    public Map<String, Object> detail(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Recipe recipe = recipeService.getRecipeDetail(id);
            result.put("code", 200);
            result.put("data", recipe);
            result.put("message", "get detail completed");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * GET /api/recipe/first
     */
    @GetMapping("/first")
    public Map<String, Object> first() {
        Map<String, Object> result = new HashMap<>();
        try {
            Recipe recipe = recipeService.getFirstRecipe();
            result.put("code", 200);
            result.put("data", recipe);
            result.put("message", "get first completed");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * GET /api/recipe/last
     */
    @GetMapping("/last")
    public Map<String, Object> last() {
        Map<String, Object> result = new HashMap<>();
        try {
            Recipe recipe = recipeService.getLastRecipe();
            result.put("code", 200);
            result.put("data", recipe);
            result.put("message", "get last completed");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * GET /api/recipe/navigate?type=prev&currentId=5
     */
    @GetMapping("/navigate")
    public Map<String, Object> navigate(@RequestParam String type, @RequestParam Integer currentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Recipe recipe = null;
            if ("prev".equals(type)) {
                recipe = recipeService.getPrevRecipe(currentId);
            } else if ("next".equals(type)) {
                recipe = recipeService.getNextRecipe(currentId);
            }
            result.put("code", 200);
            result.put("data", recipe);
            result.put("message", "get navigate completed");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * POST /api/recipe/add
     */
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Recipe recipe, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            result.put("code", 401);
            result.put("message", "please login first");
            return result;
        }

        try {
            recipe.setAuthorId(userId);
            Recipe newRecipe = recipeService.addRecipe(recipe);
            result.put("code", 200);
            result.put("data", newRecipe);
            result.put("message", "post add completed");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * PUT /api/recipe/update
     */
    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Recipe recipe, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null) {
            result.put("code", 401);
            result.put("message", "Please login first");
            return result;
        }

        try {
            Recipe existing = recipeService.getRecipeById(recipe.getId());
            if (!existing.getAuthorId().equals(userId) && !"admin".equals(role)) {
                result.put("code", 403);
                result.put("message", "No permission");
                return result;
            }

            recipe.setAuthorId(existing.getAuthorId());
            Recipe updatedRecipe = recipeService.updateRecipe(recipe);
            result.put("code", 200);
            result.put("data", updatedRecipe);
            result.put("message", "update completed");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**

     * DELETE /api/recipe/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null) {
            result.put("code", 401);
            result.put("message", "Please login first");
            return result;
        }

        try {
            Recipe existing = recipeService.getRecipeById(id);
            if (!existing.getAuthorId().equals(userId) && !"admin".equals(role)) {
                result.put("code", 403);
                result.put("message", "No permission");
                return result;
            }

            boolean success = recipeService.deleteRecipe(id);
            if (success) {
                result.put("code", 200);
                result.put("message", "delete completed");
            } else {
                result.put("code", 500);
                result.put("message", "delete failed");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 获取热门菜谱
     * GET /api/recipe/hot?limit=5
     */
    @GetMapping("/hot")
    public Map<String, Object> getHotRecipes(@RequestParam(defaultValue = "5") Integer limit) {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("code", 200);
            result.put("data", recipeService.getHotRecipes(limit));
            result.put("message", "Get Success");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 获取最新菜谱
     * GET /api/recipe/latest?limit=5
     */
    @GetMapping("/latest")
    public Map<String, Object> getLatestRecipes(@RequestParam(defaultValue = "5") Integer limit) {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("code", 200);
            result.put("data", recipeService.getLatestRecipes(limit));
            result.put("message", "get latest recipes");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 点赞菜谱
     * POST /api/recipe/like/{id}
     */
    @PostMapping("/like/{id}")
    public Map<String, Object> likeRecipe(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = recipeService.likeRecipe(id);
            if (success) {
                result.put("code", 200);
                result.put("message", "like success");
            } else {
                result.put("code", 500);
                result.put("message", "like failed");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 获取首页统计数据
     * GET /api/recipe/stats
     */
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> result = new HashMap<>();
        try {
            long recipeCount = recipeService.getRecipeCount();
            long userCount = userService.getUserCount();
            long categoryCount = categoryService.getCategoryCount();

            Map<String, Object> data = new HashMap<>();
            data.put("recipeCount", recipeCount);
            data.put("userCount", userCount);
            data.put("categoryCount", categoryCount);

            result.put("code", 200);
            result.put("data", data);
            result.put("message", "get stats");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * GET /api/recipe/stats/difficulty
     */
    @GetMapping("/stats/difficulty")
    public Map<String, Object> getStatsByDifficulty() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("code", 200);
            result.put("data", recipeService.getStatsByDifficulty());
            result.put("message", "get stats by Difficulty");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }
}
package com.kitchen.backend_springbook_kichen.controller;

import com.kitchen.backend_springbook_kichen.entity.Category;
import com.kitchen.backend_springbook_kichen.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜谱分类 Controller
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Category> categories = categoryService.getAllCategories();
            result.put("code", 200);
            result.put("data", categories);
            result.put("message", "Get Success");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }


    @GetMapping("/detail/{id}")
    public Map<String, Object> detail(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Category category = categoryService.getCategoryById(id);
            result.put("code", 200);
            result.put("data", category);
            result.put("message", "Get Success");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 搜索分类
     */
    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam(required = false) String keyword) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Category> categories = categoryService.searchCategories(keyword);
            result.put("code", 200);
            result.put("data", categories);
            result.put("message", "Search Success");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }


    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Category category, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        // 权限检查
        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role)) {
            result.put("code", 403);
            result.put("message", "No Permission.Admin only");
            return result;
        }

        try {
            Category newCategory = categoryService.addCategory(category);
            result.put("code", 200);
            result.put("data", newCategory);
            result.put("message", "add success");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 更新分类（管理员）
     */
    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Category category, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        // 权限检查
        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role)) {
            result.put("code", 403);
            result.put("message", "");
            return result;
        }

        try {
            Category updatedCategory = categoryService.updateCategory(category);
            result.put("code", 200);
            result.put("data", updatedCategory);
            result.put("message", "update success");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        // 权限检查
        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role)) {
            result.put("code", 403);
            result.put("message", "NO Permission.Admin only");
            return result;
        }

        try {
            boolean success = categoryService.deleteCategory(id);
            if (success) {
                result.put("code", 200);
                result.put("message", "delete success");
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


    @DeleteMapping("/batch-delete")
    public Map<String, Object> batchDelete(@RequestBody List<Integer> ids, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        // 权限检查
        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role)) {
            result.put("code", 403);
            result.put("message", "No Permission.Admin only");
            return result;
        }

        try {
            boolean success = categoryService.batchDeleteCategories(ids);
            if (success) {
                result.put("code", 200);
                result.put("message", "Batch delete success");
            } else {
                result.put("code", 500);
                result.put("message", "Batch delete failed");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }


    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> stats = categoryService.getCategoryStats();
            result.put("code", 200);
            result.put("data", stats);
            result.put("message", "get stats success");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }
}

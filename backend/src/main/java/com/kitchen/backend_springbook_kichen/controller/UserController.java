package com.kitchen.backend_springbook_kichen.controller;

import com.kitchen.backend_springbook_kichen.entity.User;
import com.kitchen.backend_springbook_kichen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * POST /api/user/login
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            String username = params.get("username");
            String password = params.get("password");

            // 直接使用明文密码登录
            User user = userService.login(username, password);

            if (user != null) {
                // 保存登录状态到session
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole());

                // 返回用户信息（不包含密码）
                user.setPassword(null);
                result.put("code", 200);
                result.put("data", user);
                result.put("message", "登录成功");
            } else {
                result.put("code", 401);
                result.put("message", "用户名或密码错误");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 用户注册
     * POST /api/user/register
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 不加密，直接存储明文密码
            // user.setPassword(MD5Util.md5(user.getPassword()));  // 删除这行
            User newUser = userService.register(user);
            newUser.setPassword(null);
            result.put("code", 200);
            result.put("data", newUser);
            result.put("message", "注册成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 用户登出
     * POST /api/user/logout
     */
    @PostMapping("/logout")
    public Map<String, Object> logout(HttpSession session) {
        session.invalidate();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "登出成功");
        return result;
    }

    /**
     * 获取当前登录用户信息
     * GET /api/user/current
     */
    @GetMapping("/current")
    public Map<String, Object> getCurrentUser(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            result.put("code", 401);
            result.put("message", "未登录");
            return result;
        }

        try {
            User user = userService.getUserById(userId);
            user.setPassword(null);
            result.put("code", 200);
            result.put("data", user);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 获取用户列表（管理员）
     * GET /api/user/list?keyword=xxx
     */
    @GetMapping("/list")
    public Map<String, Object> getUserList(@RequestParam(required = false) String keyword, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role)) {
            result.put("code", 403);
            result.put("message", "权限不足");
            return result;
        }

        try {
            List<User> users = userService.searchUsers(keyword);
            // 隐藏密码
            users.forEach(user -> user.setPassword(null));
            result.put("code", 200);
            result.put("data", users);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 添加用户（管理员）
     * POST /api/user/add
     */
    @PostMapping("/add")
    public Map<String, Object> addUser(@RequestBody User user, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role)) {
            result.put("code", 403);
            result.put("message", "权限不足");
            return result;
        }

        try {
            // 默认密码123456，直接存储明文
            user.setPassword("123456");
            User newUser = userService.addUser(user);
            newUser.setPassword(null);
            result.put("code", 200);
            result.put("data", newUser);
            result.put("message", "添加成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 删除用户（管理员）
     * DELETE /api/user/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteUser(@PathVariable Integer id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role)) {
            result.put("code", 403);
            result.put("message", "权限不足");
            return result;
        }

        // 不能删除自己
        Integer currentUserId = (Integer) session.getAttribute("userId");
        if (currentUserId.equals(id)) {
            result.put("code", 400);
            result.put("message", "不能删除当前登录账号");
            return result;
        }

        try {
            boolean success = userService.deleteUser(id);
            if (success) {
                result.put("code", 200);
                result.put("message", "删除成功");
            } else {
                result.put("code", 500);
                result.put("message", "删除失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 修改密码
     * PUT /api/user/change-password
     */
    @PutMapping("/change-password")
    public Map<String, Object> changePassword(@RequestBody Map<String, String> params, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            result.put("code", 401);
            result.put("message", "请先登录");
            return result;
        }

        try {
            String oldPassword = params.get("oldPassword");
            String newPassword = params.get("newPassword");

            // 直接使用明文密码
            boolean success = userService.changePassword(userId, oldPassword, newPassword);
            if (success) {
                result.put("code", 200);
                result.put("message", "密码修改成功");
            } else {
                result.put("code", 500);
                result.put("message", "密码修改失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 更新用户信息
     * PUT /api/user/update
     */
    @PutMapping("/update")
    public Map<String, Object> updateUser(@RequestBody User user, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null) {
            result.put("code", 401);
            result.put("message", "请先登录");
            return result;
        }

        // 只能修改自己的信息，管理员可以修改任何用户
        if (!user.getId().equals(userId) && !"admin".equals(role)) {
            result.put("code", 403);
            result.put("message", "权限不足");
            return result;
        }

        try {
            User updatedUser = userService.updateUser(user);
            updatedUser.setPassword(null);
            result.put("code", 200);
            result.put("data", updatedUser);
            result.put("message", "更新成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 获取用户统计（管理员）
     * GET /api/user/stats
     */
    @GetMapping("/stats")
    public Map<String, Object> getUserStats(HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role)) {
            result.put("code", 403);
            result.put("message", "权限不足");
            return result;
        }

        try {
            long count = userService.getUserCount();
            List<Map<String, Object>> roleStats = userService.getUserStats();

            result.put("code", 200);
            result.put("data", Map.of("total", count, "roleStats", roleStats));
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }
}
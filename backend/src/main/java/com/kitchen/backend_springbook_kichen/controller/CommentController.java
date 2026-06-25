package com.kitchen.backend_springbook_kichen.controller;

import com.kitchen.backend_springbook_kichen.entity.Comment;
import com.kitchen.backend_springbook_kichen.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/list/{recipeId}")
    public Map<String, Object> list(@PathVariable Integer recipeId,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = commentService.getCommentsByRecipe(recipeId, page, pageSize);
            result.put("code", 200);
            result.put("data", data);
            result.put("message", "get comments success");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }


    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Comment comment, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            result.put("code", 401);
            result.put("message", "Please login");
            return result;
        }

        try {
            comment.setUserId(userId);
            Comment newComment = commentService.addComment(comment);
            result.put("code", 200);
            result.put("data", newComment);
            result.put("message", "comment added successfully");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    // 点赞评论
    @PostMapping("/like/{id}")
    public Map<String, Object> like(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = commentService.likeComment(id);
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

    // 删除评论
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null) {
            result.put("code", 401);
            result.put("message", "please login");
            return result;
        }

        try {
            boolean success = commentService.deleteComment(id, userId, role);
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
}
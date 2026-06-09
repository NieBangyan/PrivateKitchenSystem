package com.kitchen.backend_springbook_kichen.service;

import com.kitchen.backend_springbook_kichen.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService {

    // 获取菜谱评论
    Map<String, Object> getCommentsByRecipe(Integer recipeId, Integer page, Integer pageSize);

    // 添加评论
    Comment addComment(Comment comment);

    // 删除评论
    boolean deleteComment(Integer id, Integer userId, String role);

    // 点赞评论
    boolean likeComment(Integer id);
}
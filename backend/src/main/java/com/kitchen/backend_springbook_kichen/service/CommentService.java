package com.kitchen.backend_springbook_kichen.service;

import com.kitchen.backend_springbook_kichen.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService {

    //get recipe comment
    Map<String, Object> getCommentsByRecipe(Integer recipeId, Integer page, Integer pageSize);

    // add comment
    Comment addComment(Comment comment);

    //delete comment
    boolean deleteComment(Integer id, Integer userId, String role);

    // like comment
    boolean likeComment(Integer id);
}
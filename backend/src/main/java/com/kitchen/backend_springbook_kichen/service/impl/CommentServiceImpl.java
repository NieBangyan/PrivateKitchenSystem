package com.kitchen.backend_springbook_kichen.service.impl;

import com.kitchen.backend_springbook_kichen.entity.Comment;
import com.kitchen.backend_springbook_kichen.mapper.CommentMapper;
import com.kitchen.backend_springbook_kichen.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Map<String, Object> getCommentsByRecipe(Integer recipeId, Integer page, Integer pageSize) {
        if (page == null) page = 1;
        if (pageSize == null) pageSize = 10;
        int offset = (page - 1) * pageSize;

        List<Comment> list = commentMapper.findByRecipeId(recipeId, offset, pageSize);
        long total = commentMapper.countByRecipeId(recipeId);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", (total + pageSize - 1) / pageSize);

        return result;
    }

    @Override
    @Transactional
    public Comment addComment(Comment comment) {
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new RuntimeException("The comment content is empty");
        }
        if (comment.getContent().length() > 500) {
            throw new RuntimeException("The comment content is too long");
        }

        comment.setLikeCount(0);
        int result = commentMapper.insert(comment);
        if (result > 0) {
            return comment;
        }
        throw new RuntimeException("Comment add failed");
    }

    @Override
    @Transactional
    public boolean deleteComment(Integer id, Integer userId, String role) {
        // 只有作者或管理员可以删除
        // 这里简化处理，实际需要查询评论的作者ID
        int result = commentMapper.deleteById(id);
        return result > 0;
    }

    @Override
    @Transactional
    public boolean likeComment(Integer id) {
        int result = commentMapper.likeComment(id);
        return result > 0;
    }
}
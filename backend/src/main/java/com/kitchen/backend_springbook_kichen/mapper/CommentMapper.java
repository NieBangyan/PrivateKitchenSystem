package com.kitchen.backend_springbook_kichen.mapper;

import com.kitchen.backend_springbook_kichen.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper {

    // 获取菜谱的评论列表
    List<Comment> findByRecipeId(@Param("recipeId") Integer recipeId,
                                 @Param("offset") Integer offset,
                                 @Param("pageSize") Integer pageSize);

    // 获取评论总数
    long countByRecipeId(@Param("recipeId") Integer recipeId);

    // 添加评论
    int insert(Comment comment);

    // 删除评论
    int deleteById(@Param("id") Integer id);

    // 点赞评论
    int likeComment(@Param("id") Integer id);
}
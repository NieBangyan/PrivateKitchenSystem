package com.kitchen.backend_springbook_kichen.mapper;

import com.kitchen.backend_springbook_kichen.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    // ========== 查询方法 ==========

    User login(@Param("username") String username, @Param("password") String password);

    User findById(Integer id);

    User findByUsername(@Param("username") String username);

    List<User> search(String keyword);

    List<User> findAll();

    // ========== 增删改方法 ==========

    int insert(User user);

    int update(User user);

    int updatePassword(@Param("id") Integer id, @Param("password") String password);

    int deleteById(Integer id);

    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    int batchDelete(@Param("ids") List<Integer> ids);

    // ========== 统计方法 ==========

    long count();

    List<Map<String, Object>> getStatsByRole();

    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
package com.kitchen.backend_springbook_kichen.mapper;

import com.kitchen.backend_springbook_kichen.entity.User;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE username = #{username} AND password = #{password} AND status = 1")
    User login(@Param("username") String username, @Param("password") String password);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Integer id);

    @Select("SELECT * FROM user WHERE username LIKE CONCAT('%', #{keyword}, '%') OR real_name LIKE CONCAT('%', #{keyword}, '%')")
    List<User> search(String keyword);

    @Insert("INSERT INTO user(username, password, real_name, email, phone, role) " +
            "VALUES(#{username}, #{password}, #{realName}, #{email}, #{phone}, #{role})")
    int insert(User user);

    @Update("UPDATE user SET password=#{password}, real_name=#{realName}, email=#{email}, phone=#{phone} WHERE id=#{id}")
    int update(User user);

    @Delete("DELETE FROM user WHERE id=#{id}")
    int deleteById(Integer id);

    @Select("SELECT * FROM user")
    List<User> findAll();
}
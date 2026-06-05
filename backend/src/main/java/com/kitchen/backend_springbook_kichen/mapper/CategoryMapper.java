package com.kitchen.backend_springbook_kichen.mapper;

import com.kitchen.backend_springbook_kichen.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CategoryMapper {

    List<Category> findAll();

    Category findById(Integer id);

    Category findByName(String name);

    List<Category> search(String keyword);

    int insert(Category category);

    int update(Category category);

    int deleteById(Integer id);

    int batchDelete(@Param("ids") List<Integer> ids);

    List<Map<String, Object>> getCategoryStats();

    long count();
}
package com.kitchen.backend_springbook_kichen.mapper;
import com.kitchen.backend_springbook_kichen.entity.Recipe;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface RecipeMapper {

    // 单表查询
    @Select("SELECT * FROM recipe WHERE id = #{id}")
    Recipe findById(Integer id);

    // 多表查询 - 带聚合函数
    @Select("SELECT r.*, c.name as categoryName, u.real_name as authorName " +
            "FROM recipe r " +
            "LEFT JOIN category c ON r.category_id = c.id " +
            "LEFT JOIN user u ON r.author_id = u.id " +
            "WHERE r.id = #{id}")
    Recipe findDetailById(Integer id);

    // 多条件组合查询 + 模糊查询 + 分页
    @Select("<script>" +
            "SELECT r.*, c.name as categoryName, u.real_name as authorName " +
            "FROM recipe r " +
            "LEFT JOIN category c ON r.category_id = c.id " +
            "LEFT JOIN user u ON r.author_id = u.id " +
            "WHERE 1=1 " +
            "<if test='title != null and title != \"\"'>" +
            " AND r.title LIKE CONCAT('%', #{title}, '%')" +
            "</if>" +
            "<if test='categoryId != null'>" +
            " AND r.category_id = #{categoryId}" +
            "</if>" +
            "<if test='difficulty != null and difficulty != \"\"'>" +
            " AND r.difficulty = #{difficulty}" +
            "</if>" +
            " ORDER BY r.create_time DESC " +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<Recipe> search(Map<String, Object> params);

    // 统计总数
    @Select("<script>" +
            "SELECT COUNT(*) FROM recipe r WHERE 1=1 " +
            "<if test='title != null and title != \"\"'>" +
            " AND r.title LIKE CONCAT('%', #{title}, '%')" +
            "</if>" +
            "<if test='categoryId != null'>" +
            " AND r.category_id = #{categoryId}" +
            "</if>" +
            "<if test='difficulty != null and difficulty != \"\"'>" +
            " AND r.difficulty = #{difficulty}" +
            "</if>" +
            "</script>")
    Long countSearch(Map<String, Object> params);

    // 插入数据
    @Insert("INSERT INTO recipe(title, category_id, ingredients, steps, cooking_time, difficulty, image_url, author_id) " +
            "VALUES(#{title}, #{categoryId}, #{ingredients}, #{steps}, #{cookingTime}, #{difficulty}, #{imageUrl}, #{authorId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Recipe recipe);

    // 更新数据
    @Update("UPDATE recipe SET title=#{title}, category_id=#{categoryId}, ingredients=#{ingredients}, " +
            "steps=#{steps}, cooking_time=#{cookingTime}, difficulty=#{difficulty}, image_url=#{imageUrl} " +
            "WHERE id=#{id}")
    int update(Recipe recipe);

    // 删除数据
    @Delete("DELETE FROM recipe WHERE id=#{id}")
    int deleteById(Integer id);

    // 创建索引
    @Update("CREATE INDEX idx_recipe_title ON recipe(title)")
    void createIndex();

    // 创建视图
    @Update("CREATE OR REPLACE VIEW v_recipe_stats AS " +
            "SELECT c.name as category_name, COUNT(r.id) as recipe_count, " +
            "AVG(r.view_count) as avg_views " +
            "FROM category c LEFT JOIN recipe r ON c.id = r.category_id " +
            "GROUP BY c.id")
    void createView();
}
package com.kitchen.backend_springbook_kichen.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Excel导出工具类
 * 用于导出数据报表
 */
public class ExcelUtil {

    /**
     * 设置响应头
     * @param response HttpServletResponse
     * @param fileName 文件名
     */
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            // Set the response content type
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");

            // Set filename to resolve words garbling.
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8")
                    .replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + encodedFileName + ".xlsx");

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("文件名编码失败", e);
        }
    }

    /**
     * 导出Excel（简单版，输出CSV格式）
     * @param response HttpServletResponse
     * @param headers 表头
     * @param dataList 数据列表
     * @param fileName 文件名
     */
    public static void exportToCsv(HttpServletResponse response,
                                   String[] headers,
                                   List<String[]> dataList,
                                   String fileName) {
        setResponseHeader(response, fileName);

        StringBuilder sb = new StringBuilder();

        // 写入表头
        for (int i = 0; i < headers.length; i++) {
            sb.append(headers[i]);
            if (i < headers.length - 1) {
                sb.append(",");
            }
        }
        sb.append("\n");

        // 写入数据
        for (String[] row : dataList) {
            for (int i = 0; i < row.length; i++) {
                String cell = row[i];
                if (cell != null) {
                    // 处理包含逗号或换行的内容
                    if (cell.contains(",") || cell.contains("\n")) {
                        cell = "\"" + cell.replace("\"", "\"\"") + "\"";
                    }
                    sb.append(cell);
                }
                if (i < row.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("\n");
        }

        try (OutputStream os = response.getOutputStream()) {
            os.write(sb.toString().getBytes("UTF-8"));
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException("导出失败", e);
        }
    }

    /**
     * 导出Excel（Map数据格式）
     * @param response HttpServletResponse
     * @param headers 表头（key:字段名, value:显示名称）
     * @param dataList 数据列表
     * @param fileName 文件名
     */
    public static void exportToCsvWithMap(HttpServletResponse response,
                                          Map<String, String> headers,
                                          List<Map<String, Object>> dataList,
                                          String fileName) {
        // 获取表头数组
        String[] headerNames = headers.values().toArray(new String[0]);
        String[] headerKeys = headers.keySet().toArray(new String[0]);

        // 转换数据
        List<String[]> rows = new java.util.ArrayList<>();
        for (Map<String, Object> data : dataList) {
            String[] row = new String[headerKeys.length];
            for (int i = 0; i < headerKeys.length; i++) {
                Object value = data.get(headerKeys[i]);
                row[i] = value != null ? value.toString() : "";
            }
            rows.add(row);
        }

        exportToCsv(response, headerNames, rows, fileName);
    }

    /**
     * 导出菜谱报表
     * @param response HttpServletResponse
     * @param recipes 菜谱列表
     * @param fileName 文件名
     */
    public static void exportRecipes(HttpServletResponse response,
                                     List<Map<String, Object>> recipes,
                                     String fileName) {
        String[] headers = {"ID", "菜谱名称", "分类", "难度", "烹饪时间", "作者", "浏览量", "发布时间"};
        List<String[]> dataList = new java.util.ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Map<String, Object> recipe : recipes) {
            String[] row = new String[8];
            row[0] = String.valueOf(recipe.get("id"));
            row[1] = String.valueOf(recipe.get("title"));
            row[2] = String.valueOf(recipe.get("categoryName"));

            Object difficulty = recipe.get("difficulty");
            if ("easy".equals(difficulty)) row[3] = "初级";
            else if ("medium".equals(difficulty)) row[3] = "中级";
            else if ("hard".equals(difficulty)) row[3] = "高级";
            else row[3] = String.valueOf(difficulty);

            row[4] = String.valueOf(recipe.get("cookingTime")) + "分钟";
            row[5] = String.valueOf(recipe.get("authorName"));
            row[6] = String.valueOf(recipe.get("viewCount"));

            Object createTime = recipe.get("createTime");
            row[7] = createTime != null ? sdf.format((Date) createTime) : "";

            dataList.add(row);
        }

        exportToCsv(response, headers, dataList, fileName);
    }

    /**
     * 导出用户报表
     * @param response HttpServletResponse
     * @param users 用户列表
     * @param fileName 文件名
     */
    public static void exportUsers(HttpServletResponse response,
                                   List<Map<String, Object>> users,
                                   String fileName) {
        String[] headers = {"ID", "用户名", "真实姓名", "邮箱", "手机号", "角色", "状态", "注册时间"};
        List<String[]> dataList = new java.util.ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Map<String, Object> user : users) {
            String[] row = new String[8];
            row[0] = String.valueOf(user.get("id"));
            row[1] = String.valueOf(user.get("username"));
            row[2] = String.valueOf(user.get("realName"));
            row[3] = String.valueOf(user.get("email"));
            row[4] = String.valueOf(user.get("phone"));

            Object role = user.get("role");
            row[5] = "admin".equals(role) ? "管理员" : "普通用户";

            Object status = user.get("status");
            row[6] = Integer.valueOf(1).equals(status) ? "正常" : "禁用";

            Object createTime = user.get("createTime");
            row[7] = createTime != null ? sdf.format((Date) createTime) : "";

            dataList.add(row);
        }

        exportToCsv(response, headers, dataList, fileName);
    }

    /**
     * 导出分类统计报表
     * @param response HttpServletResponse
     * @param stats 统计数据
     * @param fileName 文件名
     */
    public static void exportCategoryStats(HttpServletResponse response,
                                           List<Map<String, Object>> stats,
                                           String fileName) {
        String[] headers = {"分类ID", "分类名称", "菜谱数量"};
        List<String[]> dataList = new java.util.ArrayList<>();

        for (Map<String, Object> stat : stats) {
            String[] row = new String[3];
            row[0] = String.valueOf(stat.get("id"));
            row[1] = String.valueOf(stat.get("name"));
            row[2] = String.valueOf(stat.get("recipe_count"));
            dataList.add(row);
        }

        exportToCsv(response, headers, dataList, fileName);
    }
}
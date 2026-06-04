```bush
private-kitchen-system/
│
├── backend/                        # 后端代码
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/kitchen/
│   │   │   │   ├── entity/
│   │   │   │   ├── mapper/
│   │   │   │   ├── service/
│   │   │   │   └── controller/
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── mapper/
│   │   └── test/
│   └── pom.xml
│
├── frontend/                       # 前端代码
│   ├── index.html
│   ├── css/
│   │   └── styles.css
│   └── js/
│       ├── app.js
│       └── components/
│
├── database/                       # 数据库脚本
│   ├── schema.sql
│   ├── data.sql
│   ├── views.sql
│   ├── procedures.sql
│   └── triggers.sql
│
├── docs/                           # 文档
│   ├── 课程设计报告.md
│   ├── 软件安装使用说明书.md
│   └── API文档.md
│
└── README.md                       # 项目说明
```
java+Spring boot

```bush
private-kitchen-backend/
│
├── pom.xml                          # Maven配置文件
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── kitchen/
│       │           │
│       │           ├── PrivateKitchenApplication.java    # 启动类
│       │           │
│       │           ├── entity/                           # 实体类层
│       │           │   ├── User.java                     # 用户实体
│       │           │   ├── Recipe.java                   # 菜谱实体
│       │           │   └── Category.java                 # 分类实体
│       │           │
│       │           ├── mapper/                           # 数据访问层
│       │           │   ├── UserMapper.java               # 用户Mapper
│       │           │   ├── RecipeMapper.java             # 菜谱Mapper
│       │           │   └── CategoryMapper.java           # 分类Mapper
│       │           │
│       │           ├── service/                          # 业务逻辑层
│       │           │   ├── UserService.java              # 用户服务
│       │           │   ├── RecipeService.java            # 菜谱服务
│       │           │   └── CategoryService.java          # 分类服务
│       │           │
│       │           ├── controller/                       # 控制器层
│       │           │   ├── UserController.java           # 用户接口
│       │           │   ├── RecipeController.java         # 菜谱接口
│       │           │   └── CategoryController.java       # 分类接口
│       │           │
│       │           ├── config/                           # 配置类
│       │           │   ├── WebConfig.java                # Web配置
│       │           │   └── DatabaseConfig.java           # 数据库配置
│       │           │
│       │           └── util/                             # 工具类
│       │               ├── MD5Util.java                  # 加密工具
│       │               └── ExcelUtil.java                # Excel导出
│       │
│       └── resources/
│           ├── application.yml                           # 主配置文件
│           ├── application-dev.yml                       # 开发环境配置
│           ├── application-prod.yml                      # 生产环境配置
│           │
│           ├── mapper/                                   # MyBatis XML映射
│           │   ├── UserMapper.xml
│           │   ├── RecipeMapper.xml
│           │   └── CategoryMapper.xml
│           │
│           └── db/
│               ├── schema.sql                            # 数据库建表语句
│               └── data.sql                              # 初始化数据
│
└── src/
    └── test/
        └── java/
            └── com/
                └── kitchen/
                    ├── UserServiceTest.java              # 单元测试
                    └── RecipeControllerTest.java         # 接口测试
```
前端Vue.js + JavaScript

```bush
private-kitchen-frontend/
│
├── index.html                      # 入口HTML文件
│
├── css/
│   └── styles.css                  # 全局样式文件
│
├── js/
│   ├── app.js                      # 主应用文件
│   │
│   ├── components/                 # 组件目录
│   │   ├── HomeView.js             # 首页组件
│   │   ├── RecipeListView.js       # 菜谱列表组件
│   │   ├── RecipeDetailView.js     # 菜谱详情组件
│   │   ├── RecipeFormView.js       # 菜谱表单组件
│   │   ├── LoginView.js            # 登录组件
│   │   ├── RegisterView.js         # 注册组件
│   │   ├── UserManageView.js       # 用户管理组件
│   │   ├── ProfileView.js          # 个人中心组件
│   │   └── HelpView.js             # 帮助组件
│   │
│   ├── utils/                      # 工具类
│   │   ├── api.js                  # API接口封装
│   │   ├── utils.js                # 通用工具函数
│   │   └── validator.js            # 表单验证
│   │
│   └── config/
│       └── config.js               # 配置文件
│
├── assets/                         # 静态资源
│   ├── images/                     # 图片资源
│   ├── fonts/                      # 字体文件
│   └── icons/                      # 图标文件
│
└── README.md                       # 项目说明

```

文件依赖关系图

```bush
┌─────────────────────────────────────────────────────────────┐
│                       index.html                             │
│                    (HTML入口文件)                             │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                         app.js                               │
│              (Vue主应用 + 组件定义)                           │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐           │
│  │HomeView │ │RecipeView│ │LoginView│ │HelpView │           │
│  └────┬────┘ └────┬────┘ └────┬────┘ └────┬────┘           │
│       └───────────┼───────────┼───────────┘                 │
│                   ▼           ▼                             │
│              axios HTTP请求                                  │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                    Spring Boot后端                           │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Controller层 (接收请求)                             │    │
│  └─────────────────────────┬───────────────────────────┘    │
│                            ▼                                 │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Service层 (业务逻辑 + 数据验证)                      │    │
│  └─────────────────────────┬───────────────────────────┘    │
│                            ▼                                 │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Mapper层 (MyBatis数据库操作)                        │    │
│  └─────────────────────────┬───────────────────────────┘    │
│                            ▼                                 │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  MySQL数据库                                         │    │
│  │  (user, category, recipe表)                         │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

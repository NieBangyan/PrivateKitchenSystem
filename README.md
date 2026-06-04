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
src/
├── main.js                    # 入口文件
├── App.vue                    # 根组件
├── style.css                  # 全局样式
│
├── api/                       # API接口
│   ├── index.js               # axios配置
│   ├── recipe.js              # 菜谱接口
│   └── user.js                # 用户接口
│
├── router/                    # 路由配置
│   └── index.js
│
├── stores/                    # Pinia状态管理
│   ├── userStore.js           # 用户状态
│   └── recipeStore.js         # 菜谱状态
│
├── views/                     # 页面视图
│   ├── HomeView.vue           # 首页
│   ├── RecipeListView.vue     # 菜谱列表
│   ├── RecipeDetailView.vue   # 菜谱详情
│   ├── RecipeFormView.vue     # 发布/编辑菜谱
│   ├── LoginView.vue          # 登录
│   ├── RegisterView.vue       # 注册
│   ├── UserManageView.vue     # 用户管理
│   ├── ProfileView.vue        # 个人中心
│   └── HelpView.vue           # 帮助
│
├── components/                # 组件
│   ├── NavBar.vue             # 导航栏
│   ├── RecipeCard.vue         # 菜谱卡片
│   ├── SearchBar.vue          # 搜索栏
│   ├── Pagination.vue         # 分页组件
│   ├── LoadingSpinner.vue     # 加载动画
│   └── MessageToast.vue       # 消息提示
│
└── utils/                     # 工具函数
    ├── validator.js           # 表单验证
    └── format.js              # 格式化函数
```

```bush
private-kitchen/
│
├── backend/                    # 后端（Spring Boot）- 保持不变
│   ├── src/
│   │   ├── main/java/com/kitchen/
│   │   │   ├── entity/
│   │   │   ├── mapper/
│   │   │   ├── service/
│   │   │   └── controller/
│   │   └── resources/
│   │       └── application.yml
│   └── pom.xml
│
├── frontend/                   # 前端（Vue3 + Vite）- 新的
│   ├── src/
│   │   ├── api/
│   │   ├── components/
│   │   ├── views/
│   │   ├── stores/
│   │   ├── router/
│   │   ├── App.vue
│   │   ├── main.js
│   │   └── style.css
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
│
└── database/                   # 数据库脚本 - 保持不变
    ├── schema.sql
    └── data.sql

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

# 食堂管理系统前端

## 项目简介

这是一个基于 Vue 3 + Element Plus 的食堂管理系统前端项目，提供了完整的食堂管理功能。

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **Element Plus** - 基于 Vue 3 的组件库
- **Axios** - HTTP 客户端
- **Vite** - 构建工具

## 功能模块

### 1. 用户管理
- 用户列表查看
- 用户信息增删改查
- 用户状态管理
- 用户搜索和筛选

### 2. 菜品管理
- 菜品列表查看
- 菜品信息增删改查
- 菜品分类管理
- 菜品状态管理

### 3. 套餐管理
- 套餐列表查看
- 套餐信息增删改查
- 套餐菜品配置
- 套餐状态管理

### 4. 订单管理
- 订单列表查看
- 订单状态管理
- 订单详情查看
- 订单导出功能

### 5. 智能推荐
- 套餐推荐配置
- 推荐结果展示
- 推荐统计信息

### 6. 用户偏好分析
- 菜品偏好统计
- 营养偏好分析
- 偏好报告生成

### 7. 报表统计
- 销售数据统计
- 菜品销售排行
- 报表导出功能

## 安装和运行

### 1. 安装依赖
```bash
npm install
```

### 2. 启动开发服务器
```bash
npm run dev
```

### 3. 构建生产版本
```bash
npm run build
```

### 4. 预览生产版本
```bash
npm run preview
```

## 项目结构

```
frontend/
├── src/
│   ├── components/          # 组件目录
│   │   ├── UserManagement.vue           # 用户管理
│   │   ├── DishManagement.vue           # 菜品管理
│   │   ├── SetmealManagement.vue        # 套餐管理
│   │   ├── OrderManagement.vue          # 订单管理
│   │   ├── RecommendationManagement.vue # 智能推荐
│   │   ├── UserPreferenceAnalysis.vue   # 用户偏好分析
│   │   └── ReportStatistics.vue         # 报表统计
│   ├── App.vue              # 主应用组件
│   └── main.js              # 应用入口
├── index.html               # HTML 模板
├── package.json             # 项目配置
├── vite.config.js           # Vite 配置
└── README.md                # 项目说明
```

## API 接口

项目需要后端提供以下 API 接口：

### 用户管理
- `GET /api/user/list` - 获取用户列表
- `POST /api/user/add` - 新增用户
- `PUT /api/user/update/{id}` - 更新用户
- `DELETE /api/user/delete/{id}` - 删除用户

### 菜品管理
- `GET /api/dish/list` - 获取菜品列表
- `POST /api/dish/add` - 新增菜品
- `PUT /api/dish/update/{id}` - 更新菜品
- `DELETE /api/dish/delete/{id}` - 删除菜品

### 套餐管理
- `GET /api/setmeal/list` - 获取套餐列表
- `POST /api/setmeal/add` - 新增套餐
- `PUT /api/setmeal/update/{id}` - 更新套餐
- `DELETE /api/setmeal/delete/{id}` - 删除套餐

### 订单管理
- `GET /api/order/list` - 获取订单列表
- `GET /api/order/detail/{id}` - 获取订单详情
- `PUT /api/order/status/{id}` - 更新订单状态
- `GET /api/order/export` - 导出订单

### 智能推荐
- `GET /api/recommendation/list` - 获取推荐列表
- `POST /api/recommendation/generate` - 生成推荐
- `DELETE /api/recommendation/clear` - 清空推荐

### 用户偏好分析
- `GET /api/preference/dish-ranking` - 获取菜品排行
- `GET /api/preference/nutrition-analysis` - 获取营养分析
- `GET /api/preference/dish-likes-table` - 获取菜品喜爱表
- `POST /api/preference/generate-dish-likes-table` - 生成喜爱表
- `GET /api/preference/export` - 导出分析

### 报表统计
- `GET /api/report/stats` - 获取统计数据
- `GET /api/report/dish-ranking` - 获取菜品排行
- `GET /api/report/detail` - 获取详细报表
- `GET /api/report/export` - 导出报表

## 开发说明

### 1. 组件开发规范
- 使用 Vue 3 Composition API
- 组件命名采用 PascalCase
- 文件命名采用 PascalCase.vue

### 2. 样式规范
- 使用 scoped 样式
- 采用 BEM 命名规范
- 优先使用 Element Plus 组件样式

### 3. API 调用规范
- 统一使用 Axios 进行 HTTP 请求
- 统一错误处理
- 使用 async/await 语法

### 4. 状态管理
- 使用 Vue 3 的 reactive 和 ref
- 组件内部状态管理
- 避免全局状态污染

## 部署说明

### 1. 开发环境
- 后端服务运行在 `http://localhost:8080`
- 前端开发服务器运行在 `http://localhost:5173`

### 2. 生产环境
- 构建后的文件在 `dist` 目录
- 需要配置 Nginx 或其他 Web 服务器
- 需要配置 API 代理

### 3. 环境变量
- 开发环境：`.env.development`
- 生产环境：`.env.production`

## 注意事项

1. 确保后端服务正常运行
2. 检查 API 接口是否正确配置
3. 注意跨域问题处理
4. 生产环境需要配置 HTTPS

## 更新日志

### v1.0.0 (2024-01-01)
- 初始版本发布
- 完成基础功能模块
- 实现用户、菜品、套餐、订单管理
- 添加智能推荐和用户偏好分析
- 集成报表统计功能 
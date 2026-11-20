# 小区物业管理系统

基于 **Spring Boot + Vue** 的前后端分离小区物业管理系统，包含管理员后台和业主端两个角色。

## 功能模块

- 业主管理 / 物业管理 / 停车位管理
- 缴费管理 / 报修管理 / 投诉管理
- 访客登记 / 社区活动 / 数据仪表盘

## 技术栈

| 层级 | 技术 |
|:----:|:-----|
| 后端 | Spring Boot 2.7.18 + MyBatis-Plus 3.5.3 |
| 前端 | Vue 2.7 + Element UI + Vite 5 |
| 数据库 | MySQL 8.x |

## 开发环境要求

- JDK 1.8+
- Maven 3.6+
- Node.js 16+
- MySQL 8.x

## 快速启动

### 方式一：一键启动（Windows）

双击运行 `start(1).bat` 即可自动启动前后端服务。

### 方式二：手动启动

```bash
# 1. 启动后端
cd backend
mvn clean compile
mvn spring-boot:run

# 2. 启动前端（新终端）
cd frontend
npm install
npm run dev
```

### 3. 导入数据库

将 `db/hx5134_property_management.sql` 导入 MySQL。

### 4. 访问系统

浏览器打开：**http://localhost:10087**

## 默认账号

| 角色 | 用户名 | 密码 |
|:----:|:------:|:----:|
| 管理员 | `admin` | `123456` |
| 业主 | `zhangsan` | `123456` |
| 业主 | `lisi` | `123456` |
| 业主 | `wangwu` | `123456` |
| 业主 | `zhaoliu` | `123456` |
| 业主 | `qianqi` | `123456` |

## 项目结构

```
小区物业管理系统/
├── backend/          # Spring Boot 后端
│   └── src/main/java/com/property/
│       ├── controller/   # 控制层
│       ├── service/      # 业务逻辑层
│       ├── mapper/       # 数据访问层
│       ├── entity/       # 实体类
│       ├── config/       # 配置类
│       └── interceptor/  # 拦截器
├── frontend/         # Vue 前端
│   └── src/views/
│       ├── admin/    # 管理员页面
│       ├── user/     # 业主页面
│       └── login/    # 登录注册页
├── db/               # 数据库脚本
└── start(1).bat      # 一键启动脚本
```


## LoopForge 链路验证

- 时间：2026-11-20 19:38:56 UTC
- 说明：本段由 LoopForge 的 SOP 脚本节点写入，用于验证「仓库 -> 分支 -> PR」链路。

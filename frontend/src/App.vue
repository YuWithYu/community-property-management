<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
/* 全局CSS变量 - 清新蓝绿主题 */
:root {
  --primary-color: #1890ff;
  --primary-light: #40a9ff;
  --primary-dark: #096dd9;
  --success-color: #52c41a;
  --warning-color: #faad14;
  --danger-color: #ff4d4f;
  --text-primary: #262626;
  --text-secondary: #595959;
  --text-muted: #8c8c8c;
  --border-color: #d9d9d9;
  --bg-primary: #f0f5ff;
  --bg-secondary: #fafafa;
  --shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.08);
  --shadow-md: 0 4px 16px rgba(0, 0, 0, 0.12);
  --shadow-lg: 0 8px 32px rgba(0, 0, 0, 0.16);
  --radius-sm: 4px;
  --radius-md: 8px;
  --radius-lg: 12px;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  background-color: var(--bg-primary);
  color: var(--text-primary);
  line-height: 1.6;
}

#app {
  min-height: 100vh;
}

/* 表格样式优化 */
.el-table {
  width: 100% !important;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.el-table th {
  background: #f0f7ff !important;
  color: var(--text-primary);
  font-weight: 600;
  border-bottom: 2px solid var(--border-color) !important;
  padding: 14px 12px !important;
}

.el-table td {
  border-bottom: 1px solid #f0f0f0 !important;
  padding: 12px !important;
}

.el-table--striped .el-table__body tr.el-table__row--striped td {
  background-color: #fafbfc !important;
}

.el-table__body tr:hover > td {
  background-color: #e6f7ff !important;
}

/* 搜索表单样式 */
.search-form {
  background: #fff;
  padding: 24px;
  margin-bottom: 20px;
  border-radius: var(--radius-md);
  border: 1px solid var(--border-color);
  box-shadow: var(--shadow-sm);
}

/* 表格容器样式 */
.table-container {
  background: #fff;
  padding: 24px;
  border-radius: var(--radius-md);
  border: 1px solid var(--border-color);
  box-shadow: var(--shadow-sm);
}

/* 分页样式 */
.pagination-container {
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid var(--border-color);
  text-align: right;
}

.el-pagination .el-pager li.active {
  background-color: var(--primary-color) !important;
  border-color: var(--primary-color) !important;
}

/* 按钮样式 */
.el-button {
  border-radius: var(--radius-sm);
  font-weight: 500;
  transition: all 0.3s ease;
}

.el-button--primary {
  background: var(--primary-color);
  border-color: var(--primary-color);
  box-shadow: 0 2px 4px rgba(24, 144, 255, 0.3);
}

.el-button--primary:hover {
  background: var(--primary-light);
  border-color: var(--primary-light);
  box-shadow: 0 4px 8px rgba(24, 144, 255, 0.4);
  transform: translateY(-1px);
}

/* 对话框样式 */
.el-dialog {
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  border: 1px solid var(--border-color);
}

.el-dialog__header {
  border-bottom: 2px solid var(--border-color);
  padding: 20px 24px;
  background: #f0f7ff;
  border-radius: var(--radius-lg) var(--radius-lg) 0 0;
}

.el-dialog__title {
  font-weight: 600;
  color: var(--text-primary);
  font-size: 18px;
}

.el-dialog__body {
  padding: 24px;
}

.el-dialog__footer {
  border-top: 1px solid var(--border-color);
  padding: 16px 24px;
  background: var(--bg-secondary);
  border-radius: 0 0 var(--radius-lg) var(--radius-lg);
}

/* 卡片样式 */
.el-card {
  border-radius: var(--radius-md);
  border: 1px solid var(--border-color);
  box-shadow: var(--shadow-sm);
  transition: all 0.3s ease;
}

.el-card:hover {
  box-shadow: var(--shadow-md);
}

.el-card__header {
  border-bottom: 2px solid var(--border-color);
  padding: 16px 20px;
  background: #f0f7ff;
  font-weight: 600;
}

/* 输入框样式 */
.el-input__inner {
  border-radius: var(--radius-sm);
  border: 1px solid var(--border-color);
  transition: all 0.3s ease;
}

.el-input__inner:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

/* 标签样式 */
.el-tag {
  border-radius: var(--radius-sm);
  font-weight: 500;
  border: 1px solid transparent;
}

/* 菜单样式 */
.el-menu-item {
  border-left: 3px solid transparent;
  transition: all 0.3s ease;
}

.el-menu-item.is-active {
  border-left-color: var(--primary-color);
  background: rgba(24, 144, 255, 0.1) !important;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>

<template>
  <div class="admin-layout">
    <el-container style="height: 100vh;">
      <el-aside width="240px" class="sidebar">
        <div class="logo">
          <i class="el-icon-s-home"></i>
          <span>物业管理系统</span>
        </div>
        <el-menu
          :default-active="activeMenu"
          background-color="#001529"
          text-color="#a6adb4"
          active-text-color="#fff"
          router>
          <el-menu-item index="/admin/dashboard">
            <i class="el-icon-s-data"></i>
            <span slot="title">数据概览</span>
          </el-menu-item>
          <el-menu-item index="/admin/owner">
            <i class="el-icon-user"></i>
            <span slot="title">业主管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/property">
            <i class="el-icon-office-building"></i>
            <span slot="title">房产管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/parking">
            <i class="el-icon-truck"></i>
            <span slot="title">停车位管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/fee">
            <i class="el-icon-money"></i>
            <span slot="title">收费管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/repair">
            <i class="el-icon-s-tools"></i>
            <span slot="title">报修管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/complaint">
            <i class="el-icon-warning"></i>
            <span slot="title">投诉管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/activity">
            <i class="el-icon-date"></i>
            <span slot="title">活动管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/visitor">
            <i class="el-icon-s-custom"></i>
            <span slot="title">访客管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <div class="header-left">
            <span class="breadcrumb">{{ $route.meta.title || '管理后台' }}</span>
          </div>
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                <i class="el-icon-user-solid"></i>
                {{ userInfo.username }}
                <i class="el-icon-arrow-down"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                <el-dropdown-item command="password">修改密码</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'AdminLayout',
  computed: {
    activeMenu() {
      return this.$route.path
    },
    userInfo() {
      return this.$store.state.user || {}
    }
  },
  methods: {
    handleCommand(command) {
      switch (command) {
        case 'profile':
          this.$router.push('/home/profile')
          break
        case 'password':
          this.$message.info('功能开发中')
          break
        case 'logout':
          this.$confirm('确认退出登录？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$store.dispatch('logout')
            this.$router.push('/login')
            this.$message.success('已退出登录')
          })
          break
      }
    }
  }
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
}

.sidebar {
  background-color: #001529;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.15);
  border-right: 1px solid #1890ff;
}

.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  border-bottom: 2px solid #40a9ff;
}

.logo i {
  font-size: 28px;
  margin-right: 12px;
}

.sidebar .el-menu {
  border-right: none;
  background-color: #001529;
}

.sidebar .el-menu-item {
  height: 50px;
  line-height: 50px;
  border-left: 4px solid transparent;
  margin: 4px 0;
  transition: all 0.3s ease;
}

.sidebar .el-menu-item:hover {
  background-color: #1890ff !important;
  color: #fff !important;
  border-left-color: #40a9ff;
}

.sidebar .el-menu-item.is-active {
  background-color: #1890ff !important;
  color: #fff !important;
  border-left-color: #40a9ff;
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.4);
}

.sidebar .el-menu-item i {
  font-size: 18px;
  margin-right: 10px;
}

.header {
  background: linear-gradient(135deg, #fff 0%, #f0f7ff 100%);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 2px solid #1890ff;
}

.breadcrumb {
  font-size: 16px;
  font-weight: 600;
  color: #1890ff;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  cursor: pointer;
  color: #1890ff;
  font-weight: 500;
  padding: 8px 16px;
  border: 1px solid #1890ff;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.user-info:hover {
  background-color: #1890ff;
  color: #fff;
}

.user-info i {
  margin-right: 5px;
}

.main-content {
  background: linear-gradient(135deg, #f0f7ff 0%, #e6f7ff 100%);
  padding: 24px;
  overflow-y: auto;
  border-top: 3px solid #1890ff;
}
</style>

<template>
  <div class="user-layout">
    <el-header class="header">
      <div class="logo">
        <i class="el-icon-s-home"></i>
        <span>小区物业管理系统</span>
      </div>
      <el-menu 
        :default-active="$route.path" 
        mode="horizontal" 
        router 
        background-color="transparent" 
        text-color="#fff" 
        active-text-color="#ffd04b">
        <el-menu-item index="/home">首页</el-menu-item>
        <el-menu-item index="/home/fee">费用缴纳</el-menu-item>
        <el-menu-item index="/home/parking">车位查询</el-menu-item>
        <el-menu-item index="/home/repair">报修服务</el-menu-item>
        <el-menu-item index="/home/activity">社区活动</el-menu-item>
        <el-menu-item index="/home/complaint">投诉建议</el-menu-item>
        <el-menu-item index="/home/visitor">访客录入</el-menu-item>
      </el-menu>
      <div class="user-info">
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link">
            <i class="el-icon-user-solid"></i>
            {{ user.realName || user.username }} <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="profile">个人中心</el-dropdown-item>
            <el-dropdown-item command="password">修改密码</el-dropdown-item>
            <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>
    
    <el-main class="main-content">
      <router-view/>
    </el-main>
  </div>
</template>

<script>
export default {
  name: 'UserLayout',
  computed: {
    user() {
      return this.$store.state.user
    }
  },
  methods: {
    handleCommand(command) {
      if (command === 'logout') {
        this.$confirm('确认退出登录?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.dispatch('logout')
          this.$router.push('/login')
          this.$message.success('已退出登录')
        })
      } else if (command === 'profile') {
        this.$router.push('/home/profile')
      } else if (command === 'password') {
        this.$router.push('/home/profile')
      }
    }
  }
}
</script>

<style scoped>
.user-layout {
  min-height: 100vh;
  background: linear-gradient(135deg, #f0f7ff 0%, #f6ffed 100%);
}

.header {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
  padding: 0 30px;
  color: #fff;
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
  border-bottom: 2px solid rgba(255, 255, 255, 0.2);
  position: sticky;
  top: 0;
  z-index: 100;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 22px;
  font-weight: bold;
  margin-right: 50px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.logo i {
  font-size: 28px;
  background: rgba(255, 255, 255, 0.2);
  padding: 8px;
  border-radius: 8px;
}

.el-menu {
  flex: 1;
  border-bottom: none;
  background: transparent !important;
}

.el-menu-item {
  font-size: 15px;
  font-weight: 500;
  height: 60px;
  line-height: 60px;
  border-bottom: 3px solid transparent;
  transition: all 0.3s ease;
}

.el-menu-item:hover {
  background: rgba(255, 255, 255, 0.1) !important;
  border-bottom-color: rgba(255, 255, 255, 0.5);
}

.el-menu-item.is-active {
  background: rgba(255, 255, 255, 0.15) !important;
  border-bottom-color: #ffd04b;
}

.user-info {
  color: #fff;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.2);
}

.el-dropdown-link {
  color: #fff;
  font-size: 14px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
}

.el-dropdown-link i.el-icon-user-solid {
  font-size: 18px;
  background: rgba(255, 255, 255, 0.2);
  padding: 4px;
  border-radius: 50%;
}

.main-content {
  padding: 24px;
  min-height: calc(100vh - 60px);
}
</style>

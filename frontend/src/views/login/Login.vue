<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-left">
        <div class="system-intro">
          <div class="logo-icon">
            <i class="el-icon-s-home"></i>
          </div>
          <h1 class="system-title">智慧小区物业管理系统</h1>
          <p class="system-desc">打造智能化、便捷化的小区物业管理平台</p>
          <div class="feature-list">
            <div class="feature-item">
              <div class="feature-icon">
                <i class="el-icon-s-home"></i>
              </div>
              <span>房产管理</span>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <i class="el-icon-user"></i>
              </div>
              <span>业主服务</span>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <i class="el-icon-s-order"></i>
              </div>
              <span>费用缴纳</span>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <i class="el-icon-s-tools"></i>
              </div>
              <span>报修服务</span>
            </div>
          </div>
        </div>
      </div>
      <div class="login-right">
        <div class="login-form-container">
          <div class="form-header">
            <div class="header-icon">
              <i class="el-icon-user"></i>
            </div>
            <h2 class="login-title">用户登录</h2>
            <p class="login-subtitle">欢迎回来，请登录您的账号</p>
          </div>
          <el-form :model="loginForm" :rules="loginRules" ref="loginForm" label-width="0">
            <el-form-item prop="username">
              <el-input 
                v-model="loginForm.username" 
                prefix-icon="el-icon-user" 
                placeholder="请输入用户名"
                size="large">
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input 
                v-model="loginForm.password" 
                prefix-icon="el-icon-lock" 
                placeholder="请输入密码"
                type="password"
                size="large"
                @keyup.enter.native="handleLogin">
              </el-input>
            </el-form-item>
            <el-form-item prop="type">
              <el-radio-group v-model="loginForm.type" class="login-type">
                <el-radio label="1">
                  <i class="el-icon-user-solid"></i> 业主
                </el-radio>
                <el-radio label="2">
                  <i class="el-icon-s-custom"></i> 管理员
                </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button 
                type="primary" 
                size="large" 
                class="login-btn"
                :loading="loading"
                @click="handleLogin">
                <i class="el-icon-right"></i> 登 录
              </el-button>
            </el-form-item>
            <el-form-item>
              <div class="login-options">
                <span>还没有账号？</span>
                <el-button type="text" @click="goRegister" class="register-link">
                  立即注册 <i class="el-icon-arrow-right"></i>
                </el-button>
              </div>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
    <div class="login-footer">
      <p>© 2024 智慧小区物业管理系统 版权所有</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      loading: false,
      loginForm: {
        username: '',
        password: '',
        type: '1'
      },
      loginRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$axios.post('/user/login', this.loginForm).then(res => {
            this.$store.dispatch('login', res.data)
            this.$message.success('登录成功')
            if (res.data.type === 2) {
              this.$router.push('/admin/dashboard')
            } else {
              this.$router.push('/home')
            }
          }).finally(() => {
            this.loading = false
          })
        }
      })
    },
    goRegister() {
      this.$router.push('/register')
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 50%, #52c41a 100%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  padding: 20px;
}

.login-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    radial-gradient(circle at 20% 80%, rgba(255, 255, 255, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
}

.login-box {
  display: flex;
  width: 1000px;
  height: 560px;
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  position: relative;
  z-index: 1;
  border: 2px solid #e8e8e8;
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 50px;
  position: relative;
  overflow: hidden;
}

.login-left::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 1px, transparent 1px);
  background-size: 30px 30px;
  opacity: 0.3;
}

.system-intro {
  color: #fff;
  text-align: center;
  position: relative;
  z-index: 1;
}

.logo-icon {
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px;
  font-size: 40px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.system-title {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 12px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.system-desc {
  font-size: 16px;
  margin-bottom: 50px;
  opacity: 0.95;
  font-weight: 300;
}

.feature-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.feature-item:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateY(-3px);
}

.feature-icon {
  width: 44px;
  height: 44px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
}

.feature-item span {
  font-size: 14px;
  font-weight: 500;
}

.login-right {
  width: 420px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 50px;
  background: #fff;
}

.login-form-container {
  width: 100%;
}

.form-header {
  text-align: center;
  margin-bottom: 35px;
}

.header-icon {
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  font-size: 32px;
  color: #fff;
  box-shadow: 0 8px 24px rgba(24, 144, 255, 0.3);
}

.login-title {
  text-align: center;
  font-size: 26px;
  color: #262626;
  margin-bottom: 8px;
  font-weight: 600;
}

.login-subtitle {
  font-size: 14px;
  color: #8c8c8c;
}

.login-type {
  display: flex;
  justify-content: center;
  gap: 30px;
  width: 100%;
}

.login-type .el-radio {
  font-size: 15px;
}

.login-type .el-radio i {
  margin-right: 6px;
  color: #1890ff;
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
  border: none;
  box-shadow: 0 4px 16px rgba(24, 144, 255, 0.3);
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(24, 144, 255, 0.4);
}

.login-options {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  color: #8c8c8c;
  gap: 8px;
}

.register-link {
  font-weight: 600;
  font-size: 14px;
}

.login-footer {
  position: absolute;
  bottom: 30px;
  color: #fff;
  font-size: 14px;
  z-index: 1;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

/* 输入框样式优化 */
:deep(.el-input__inner) {
  border-radius: 8px;
  height: 46px;
  border: 2px solid #e8e8e8;
  transition: all 0.3s ease;
}

:deep(.el-input__inner:focus) {
  border-color: #1890ff;
  box-shadow: 0 0 0 3px rgba(24, 144, 255, 0.1);
}

:deep(.el-input__prefix) {
  left: 12px;
}

:deep(.el-input__icon) {
  font-size: 18px;
  color: #bfbfbf;
}
</style>

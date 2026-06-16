<template>
  <div class="home-container">
    <!-- 顶部欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <div class="welcome-text">
          <h1>欢迎回家，{{ user.realName || user.username }}</h1>
          <p class="welcome-desc">智慧物业，让生活更美好</p>
          <div class="weather-info">
            <i class="el-icon-sunny"></i>
            <span>今天天气晴朗，祝您生活愉快！</span>
          </div>
        </div>
        <div class="quick-stats">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #1890ff, #36cfc9);">
              <i class="el-icon-s-order"></i>
            </div>
            <div class="stat-info">
              <span class="stat-value">{{ pendingFees }}</span>
              <span class="stat-label">待缴费</span>
            </div>
          </div>
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #52c41a, #95de64);">
              <i class="el-icon-s-tools"></i>
            </div>
            <div class="stat-info">
              <span class="stat-value">{{ pendingRepairs }}</span>
              <span class="stat-label">待处理报修</span>
            </div>
          </div>
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #fa8c16, #ffc53d);">
              <i class="el-icon-s-promotion"></i>
            </div>
            <div class="stat-info">
              <span class="stat-value">{{ upcomingActivities }}</span>
              <span class="stat-label">即将开始活动</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 主体内容区域 -->
    <div class="main-section">
      <!-- 左侧快捷入口 -->
      <div class="left-panel">
        <div class="panel-header">
          <i class="el-icon-menu"></i>
          <span>快捷服务</span>
        </div>
        <div class="quick-menu">
          <div class="menu-item" @click="$router.push('/home/fee')">
            <div class="menu-icon" style="background: #e6f7ff; color: #1890ff;">
              <i class="el-icon-s-order"></i>
            </div>
            <span class="menu-title">费用缴纳</span>
            <span class="menu-desc">物业费、水电燃气费</span>
          </div>
          
          <div class="menu-item" @click="$router.push('/home/parking')">
            <div class="menu-icon" style="background: #f6ffed; color: #52c41a;">
              <i class="el-icon-truck"></i>
            </div>
            <span class="menu-title">车位查询</span>
            <span class="menu-desc">车位信息和租赁状态</span>
          </div>
          
          <div class="menu-item" @click="$router.push('/home/repair')">
            <div class="menu-icon" style="background: #fff7e6; color: #fa8c16;">
              <i class="el-icon-s-tools"></i>
            </div>
            <span class="menu-title">报修服务</span>
            <span class="menu-desc">在线提交报修申请</span>
          </div>
          
          <div class="menu-item" @click="$router.push('/home/activity')">
            <div class="menu-icon" style="background: #fff1f0; color: #f5222d;">
              <i class="el-icon-s-promotion"></i>
            </div>
            <span class="menu-title">社区活动</span>
            <span class="menu-desc">参与社区各类活动</span>
          </div>
          
          <div class="menu-item" @click="$router.push('/home/complaint')">
            <div class="menu-icon" style="background: #f9f0ff; color: #722ed1;">
              <i class="el-icon-chat-dot-square"></i>
            </div>
            <span class="menu-title">投诉建议</span>
            <span class="menu-desc">反馈问题和建议</span>
          </div>
          
          <div class="menu-item" @click="$router.push('/home/visitor')">
            <div class="menu-icon" style="background: #e6fffb; color: #13c2c2;">
              <i class="el-icon-user"></i>
            </div>
            <span class="menu-title">访客录入</span>
            <span class="menu-desc">提前录入访客信息</span>
          </div>
        </div>
      </div>

      <!-- 右侧信息展示 -->
      <div class="right-panel">
        <!-- 最新公告 -->
        <div class="info-card notice-card">
          <div class="card-header">
            <div class="header-title">
              <i class="el-icon-bell"></i>
              <span>最新公告</span>
            </div>
            <el-button type="text" @click="$router.push('/home/notice')">查看更多</el-button>
          </div>
          <div class="notice-list">
            <div v-for="(notice, index) in notices" :key="index" class="notice-item" @click="viewNotice(notice)">
              <div class="notice-dot" :class="{ 'is-new': index < 2 }"></div>
              <div class="notice-content">
                <span class="notice-title">{{ notice.title }}</span>
                <span class="notice-time">{{ notice.time }}</span>
              </div>
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </div>

        <!-- 社区动态 -->
        <div class="info-card activity-card">
          <div class="card-header">
            <div class="header-title">
              <i class="el-icon-date"></i>
              <span>即将开始的活动</span>
            </div>
          </div>
          <div class="activity-list">
            <div v-for="(activity, index) in activities" :key="index" class="activity-item">
              <div class="activity-date">
                <span class="date-day">{{ activity.day }}</span>
                <span class="date-month">{{ activity.month }}</span>
              </div>
              <div class="activity-info">
                <span class="activity-title">{{ activity.title }}</span>
                <span class="activity-location">
                  <i class="el-icon-location"></i>
                  {{ activity.location }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- 快捷联系方式 -->
        <div class="info-card contact-card">
          <div class="card-header">
            <div class="header-title">
              <i class="el-icon-phone"></i>
              <span>物业服务</span>
            </div>
          </div>
          <div class="contact-list">
            <div class="contact-item">
              <i class="el-icon-phone-outline"></i>
              <span>物业热线：400-888-8888</span>
            </div>
            <div class="contact-item">
              <i class="el-icon-time"></i>
              <span>服务时间：9:00-18:00</span>
            </div>
            <div class="contact-item">
              <i class="el-icon-location-outline"></i>
              <span>物业地址：小区A栋1楼</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      pendingFees: 2,
      pendingRepairs: 1,
      upcomingActivities: 3,
      notices: [
        { title: '关于春节期间物业服务中心值班安排的通知', time: '2024-01-15' },
        { title: '小区电梯维护保养公告', time: '2024-01-10' },
        { title: '2024年第一季度物业费缴纳通知', time: '2024-01-05' },
        { title: '关于小区停车位重新规划的通知', time: '2024-01-01' }
      ],
      activities: [
        { title: '春节联欢晚会', location: '小区广场', day: '10', month: '2月' },
        { title: '亲子手工活动', location: '社区活动中心', day: '15', month: '2月' },
        { title: '健康义诊活动', location: '小区医务室', day: '20', month: '2月' }
      ]
    }
  },
  computed: {
    user() {
      return this.$store.state.user || {}
    }
  },
  methods: {
    viewNotice(notice) {
      this.$message.info(`查看公告：${notice.title}`)
    }
  }
}
</script>

<style scoped>
.home-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 欢迎区域 */
.welcome-section {
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
  border-radius: 12px;
  padding: 30px 40px;
  margin-bottom: 24px;
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.2);
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-text h1 {
  color: #fff;
  font-size: 28px;
  margin-bottom: 8px;
  font-weight: 600;
}

.welcome-desc {
  color: rgba(255, 255, 255, 0.9);
  font-size: 16px;
  margin-bottom: 12px;
}

.weather-info {
  display: flex;
  align-items: center;
  gap: 8px;
  color: rgba(255, 255, 255, 0.85);
  font-size: 14px;
}

.weather-info i {
  font-size: 20px;
  color: #ffd93d;
}

/* 统计卡片 */
.quick-stats {
  display: flex;
  gap: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(255, 255, 255, 0.95);
  padding: 16px 20px;
  border-radius: 10px;
  min-width: 160px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 24px;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #262626;
  line-height: 1;
}

.stat-label {
  font-size: 13px;
  color: #8c8c8c;
  margin-top: 4px;
}

/* 主体内容区域 */
.main-section {
  display: grid;
  grid-template-columns: 380px 1fr;
  gap: 24px;
}

/* 左侧面板 */
.left-panel {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: 2px solid #e8e8e8;
  height: fit-content;
}

.panel-header {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: 600;
  color: #262626;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 2px solid #f0f0f0;
}

.panel-header i {
  color: #1890ff;
  font-size: 20px;
}

.quick-menu {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 16px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

.menu-item:hover {
  background: #f6ffed;
  border-color: #b7eb8f;
  transform: translateX(5px);
}

.menu-icon {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  flex-shrink: 0;
}

.menu-title {
  font-size: 15px;
  font-weight: 600;
  color: #262626;
  display: block;
}

.menu-desc {
  font-size: 12px;
  color: #8c8c8c;
  display: block;
  margin-top: 2px;
}

/* 右侧面板 */
.right-panel {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.info-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: 2px solid #e8e8e8;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 2px solid #f0f0f0;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 16px;
  font-weight: 600;
  color: #262626;
}

.header-title i {
  color: #1890ff;
  font-size: 18px;
}

/* 公告列表 */
.notice-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.notice-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #fafafa;
}

.notice-item:hover {
  background: #e6f7ff;
}

.notice-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #d9d9d9;
  flex-shrink: 0;
}

.notice-dot.is-new {
  background: #ff4d4f;
  box-shadow: 0 0 0 3px rgba(255, 77, 79, 0.2);
}

.notice-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.notice-title {
  font-size: 14px;
  color: #262626;
  font-weight: 500;
}

.notice-time {
  font-size: 12px;
  color: #8c8c8c;
}

.notice-item > .el-icon-arrow-right {
  color: #bfbfbf;
  font-size: 14px;
}

/* 活动列表 */
.activity-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  border-radius: 10px;
  background: linear-gradient(135deg, #f6ffed 0%, #ffffff 100%);
  border: 1px solid #b7eb8f;
}

.activity-date {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 56px;
  height: 56px;
  background: #52c41a;
  border-radius: 10px;
  color: #fff;
  flex-shrink: 0;
}

.date-day {
  font-size: 22px;
  font-weight: 700;
  line-height: 1;
}

.date-month {
  font-size: 12px;
  margin-top: 2px;
}

.activity-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.activity-title {
  font-size: 15px;
  font-weight: 600;
  color: #262626;
}

.activity-location {
  font-size: 13px;
  color: #8c8c8c;
  display: flex;
  align-items: center;
  gap: 4px;
}

.activity-location i {
  color: #1890ff;
}

/* 联系信息 */
.contact-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 8px;
  background: #f6ffed;
  color: #262626;
  font-size: 14px;
}

.contact-item i {
  color: #52c41a;
  font-size: 18px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .welcome-content {
    flex-direction: column;
    gap: 24px;
    text-align: center;
  }
  
  .main-section {
    grid-template-columns: 1fr;
  }
  
  .quick-stats {
    flex-wrap: wrap;
    justify-content: center;
  }
}

@media (max-width: 768px) {
  .home-container {
    padding: 0 12px;
  }
  
  .welcome-section {
    padding: 20px;
  }
  
  .welcome-text h1 {
    font-size: 22px;
  }
  
  .quick-menu {
    grid-template-columns: 1fr;
  }
}
</style>

<template>
  <div class="activity-container">
    <el-row :gutter="20">
      <el-col :span="8" v-for="item in activityList" :key="item.id">
        <el-card class="activity-card" shadow="hover">
          <img :src="item.image || 'https://picsum.photos/400/200'" class="activity-image">
          
          <div class="activity-content">
            <h3 class="activity-title">{{ item.title }}</h3>
            <p class="activity-desc">{{ item.content }}</p>
            
            <div class="activity-info">
              <p><i class="el-icon-location"></i> {{ item.location }}</p>
              <p><i class="el-icon-time"></i> {{ formatDate(item.startTime) }}</p>
              <p><i class="el-icon-user"></i> {{ item.currentParticipants }}/{{ item.maxParticipants }}人</p>
            </div>
            
            <div class="activity-footer">
              <el-tag :type="getStatusType(item.status)">{{ getStatusText(item.status) }}</el-tag>
              <el-button 
                v-if="item.status === 0 && !isParticipated(item.id)" 
                type="primary" 
                size="small"
                @click="handleJoin(item)">
                立即报名
              </el-button>
              <el-tag v-else-if="isParticipated(item.id)" type="success">已报名</el-tag>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-empty v-if="activityList.length === 0" description="暂无活动"></el-empty>
  </div>
</template>

<script>
export default {
  name: 'UserActivity',
  data() {
    return {
      activityList: [],
      participatedList: []
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    }
  },
  created() {
    this.loadActivityList()
    this.loadParticipatedList()
  },
  methods: {
    async loadActivityList() {
      try {
        const res = await this.$axios.get('/activity/page', {
          params: { page: 1, size: 100 }
        })
        this.activityList = res.data.records
      } catch (error) {
        console.error(error)
      }
    },
    async loadParticipatedList() {
      try {
        const res = await this.$axios.get('/activity-participant/list')
        this.participatedList = res.data.filter(item => item.ownerId === this.user.id)
      } catch (error) {
        console.error(error)
      }
    },
    isParticipated(activityId) {
      return this.participatedList.some(item => item.activityId === activityId)
    },
    getStatusType(status) {
      const types = ['primary', 'success', 'info']
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = ['未开始', '进行中', '已结束']
      return texts[status] || '未知'
    },
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString()
    },
    async handleJoin(item) {
      try {
        await this.$confirm(`确认报名参加 "${item.title}"?`, '提示', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'info'
        })
        await this.$axios.post('/activity-participant', {
          activityId: item.id,
          ownerId: this.user.id
        })
        this.$message.success('报名成功')
        this.loadParticipatedList()
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>

<style scoped>
.activity-container {
  max-width: 1200px;
  margin: 0 auto;
}

.activity-card {
  margin-bottom: 20px;
}

.activity-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px 4px 0 0;
}

.activity-content {
  padding: 15px;
}

.activity-title {
  margin: 0 0 10px 0;
  font-size: 18px;
  color: #303133;
}

.activity-desc {
  color: #606266;
  font-size: 14px;
  line-height: 1.5;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.activity-info {
  font-size: 13px;
  color: #909399;
  margin-bottom: 15px;
}

.activity-info p {
  margin: 5px 0;
}

.activity-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

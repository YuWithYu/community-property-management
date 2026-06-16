<template>
  <div class="parking-container">
    <el-card>
      <div slot="header">
        <span>我的车位</span>
      </div>
      
      <el-empty v-if="parkingList.length === 0" description="暂无车位信息"></el-empty>
      
      <el-row v-else :gutter="20">
        <el-col :span="8" v-for="item in parkingList" :key="item.id">
          <el-card class="parking-card" shadow="hover">
            <div class="parking-header">
              <h3>{{ item.spaceNo }}</h3>
              <el-tag :type="item.status === 1 ? 'success' : 'info'">
                {{ item.status === 1 ? '已占用' : '空闲' }}
              </el-tag>
            </div>
            
            <div class="parking-info">
              <p><strong>车位类型：</strong>{{ item.type }}</p>
              <p><strong>开始日期：</strong>{{ item.startDate }}</p>
              <p><strong>结束日期：</strong>{{ item.endDate }}</p>
              <p><strong>价格：</strong>¥{{ item.price }}</p>
              <p v-if="item.endDate">
                <strong>剩余天数：</strong>
                <el-tag :type="getRemainingDays(item.endDate) > 30 ? 'success' : 'warning'">
                  {{ getRemainingDays(item.endDate) }}天
                </el-tag>
              </p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'UserParking',
  data() {
    return {
      parkingList: []
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    }
  },
  created() {
    this.loadParkingList()
  },
  methods: {
    async loadParkingList() {
      try {
        const res = await this.$axios.get('/parking/list')
        this.parkingList = res.data.filter(item => item.ownerId === this.user.id)
      } catch (error) {
        console.error(error)
      }
    },
    getRemainingDays(endDate) {
      if (!endDate) return 0
      const end = new Date(endDate)
      const now = new Date()
      const diff = end - now
      return Math.max(0, Math.ceil(diff / (1000 * 60 * 60 * 24)))
    }
  }
}
</script>

<style scoped>
.parking-container {
  max-width: 1000px;
  margin: 0 auto;
}

.parking-card {
  margin-bottom: 20px;
}

.parking-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #EBEEF5;
}

.parking-header h3 {
  margin: 0;
  color: #303133;
}

.parking-info p {
  margin: 8px 0;
  color: #606266;
}
</style>

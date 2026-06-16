<template>
  <div class="fee-container">
    <div class="fee-stats">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="stat-card">
            <div class="stat-value" style="color: #F56C6C">¥{{ unpaidAmount }}</div>
            <div class="stat-label">待缴费金额</div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="stat-card">
            <div class="stat-value" style="color: #67C23A">¥{{ paidAmount }}</div>
            <div class="stat-label">已缴费金额</div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="stat-card">
            <div class="stat-value" style="color: #409EFF">{{ feeList.length }}</div>
            <div class="stat-label">费用记录</div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <el-card class="fee-list">
      <div slot="header">
        <span>费用明细</span>
      </div>
      
      <el-table :data="feeList" style="width: 100%">
        <el-table-column prop="type" label="费用类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTypeType(scope.row.type)">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.amount }}
          </template>
        </el-table-column>
        <el-table-column prop="feeMonth" label="费用月份" width="120">
          <template slot-scope="scope">
            {{ formatMonth(scope.row.feeMonth) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'">
              {{ scope.row.status === 1 ? '已缴费' : '未缴费' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="payTime" label="缴费时间">
          <template slot-scope="scope">
            {{ scope.row.payTime ? formatDate(scope.row.payTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status === 0" type="primary" size="mini" @click="handlePay(scope.row)">
              缴费
            </el-button>
            <span v-else>-</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'UserFee',
  data() {
    return {
      feeList: []
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    },
    unpaidAmount() {
      return this.feeList
        .filter(item => item.status === 0)
        .reduce((sum, item) => sum + item.amount, 0)
        .toFixed(2)
    },
    paidAmount() {
      return this.feeList
        .filter(item => item.status === 1)
        .reduce((sum, item) => sum + item.amount, 0)
        .toFixed(2)
    }
  },
  created() {
    this.loadFeeList()
  },
  methods: {
    async loadFeeList() {
      try {
        const res = await this.$axios.get('/fee/page', {
          params: { ownerId: this.user.id, page: 1, size: 100 }
        })
        this.feeList = res.data.records
      } catch (error) {
        console.error(error)
      }
    },
    getTypeType(type) {
      const types = {
        '物业费': 'primary',
        '水费': 'success',
        '电费': 'warning',
        '燃气费': 'danger'
      }
      return types[type] || ''
    },
    formatMonth(month) {
      if (!month) return '-'
      return month.substring(0, 7)
    },
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString()
    },
    async handlePay(row) {
      try {
        await this.$confirm(`确认缴纳 ${row.type} ¥${row.amount}?`, '提示', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await this.$axios.put(`/fee/pay/${row.id}`)
        this.$message.success('缴费成功')
        this.loadFeeList()
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>

<style scoped>
.fee-container {
  max-width: 1000px;
  margin: 0 auto;
}

.fee-stats {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 8px;
}

.stat-label {
  color: #909399;
  font-size: 14px;
}

.fee-list {
  margin-bottom: 20px;
}
</style>

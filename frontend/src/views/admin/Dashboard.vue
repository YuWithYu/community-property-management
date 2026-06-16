<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="stat-card stat-card-1">
          <div class="stat-icon"><i class="el-icon-user"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.ownerCount }}</div>
            <div class="stat-label">业主总数</div>
          </div>
          <div class="stat-trend"><i class="el-icon-top"></i> +12%</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-2">
          <div class="stat-icon"><i class="el-icon-office-building"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.propertyCount }}</div>
            <div class="stat-label">房产总数</div>
          </div>
          <div class="stat-trend"><i class="el-icon-top"></i> +5%</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-3">
          <div class="stat-icon"><i class="el-icon-s-tools"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.pendingRepair }}</div>
            <div class="stat-label">待处理报修</div>
          </div>
          <div class="stat-trend down"><i class="el-icon-bottom"></i> -3%</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-4">
          <div class="stat-icon"><i class="el-icon-money"></i></div>
          <div class="stat-info">
            <div class="stat-value">¥{{ statistics.monthFee }}</div>
            <div class="stat-label">本月应收</div>
          </div>
          <div class="stat-trend"><i class="el-icon-top"></i> +8%</div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-header">
            <span class="chart-title"><i class="el-icon-s-marketing"></i> 费用统计趋势</span>
            <el-tag type="success">近6个月</el-tag>
          </div>
          <div ref="feeChart" class="chart-content"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-header">
            <span class="chart-title"><i class="el-icon-pie-chart"></i> 报修状态分布</span>
            <el-tag type="warning">实时</el-tag>
          </div>
          <div ref="repairChart" class="chart-content"></div>
        </div>
      </el-col>
    </el-row>

    <!-- 最新数据表格 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <div class="data-card">
          <div class="data-header">
            <span class="data-title"><i class="el-icon-s-order"></i> 最新报修</span>
            <el-button type="text" @click="$router.push('/admin/repair')">查看更多 <i class="el-icon-arrow-right"></i></el-button>
          </div>
          <el-table :data="latestRepairs" style="width: 100%" :show-header="false">
            <el-table-column width="50">
              <template slot-scope="scope">
                <span class="rank-num" :class="'rank-' + (scope.$index + 1)">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="ownerName" label="业主" min-width="80"></el-table-column>
            <el-table-column prop="content" label="内容" min-width="150" show-overflow-tooltip></el-table-column>
            <el-table-column prop="status" label="状态" width="80">
              <template slot-scope="scope">
                <el-tag :type="getStatusType(scope.row.status)" size="mini">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="时间" width="140">
              <template slot-scope="scope">
                {{ formatTime(scope.row.createTime) }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="data-card">
          <div class="data-header">
            <span class="data-title"><i class="el-icon-s-comment"></i> 最新投诉</span>
            <el-button type="text" @click="$router.push('/admin/complaint')">查看更多 <i class="el-icon-arrow-right"></i></el-button>
          </div>
          <el-table :data="latestComplaints" style="width: 100%" :show-header="false">
            <el-table-column width="50">
              <template slot-scope="scope">
                <span class="rank-num" :class="'rank-' + (scope.$index + 1)">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="ownerName" label="业主" min-width="80"></el-table-column>
            <el-table-column prop="content" label="内容" min-width="150" show-overflow-tooltip></el-table-column>
            <el-table-column prop="status" label="状态" width="80">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'" size="mini">
                  {{ scope.row.status === 1 ? '已处理' : '待处理' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="时间" width="140">
              <template slot-scope="scope">
                {{ formatTime(scope.row.createTime) }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Dashboard',
  data() {
    return {
      statistics: {
        ownerCount: 0,
        propertyCount: 0,
        pendingRepair: 0,
        monthFee: 0
      },
      latestRepairs: [],
      latestComplaints: [],
      feeChart: null,
      repairChart: null
    }
  },
  mounted() {
    this.initCharts()
    this.loadData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.feeChart) this.feeChart.dispose()
    if (this.repairChart) this.repairChart.dispose()
  },
  methods: {
    initCharts() {
      this.feeChart = echarts.init(this.$refs.feeChart)
      this.repairChart = echarts.init(this.$refs.repairChart)
    },
    loadData() {
      this.$axios.get('/dashboard/statistics').then(res => {
        this.statistics = res.data
      })
      
      this.$axios.get('/dashboard/feeChart').then(res => {
        this.feeChart.setOption({
          tooltip: {
            trigger: 'axis',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: '#1890ff',
            borderWidth: 1,
            textStyle: { color: '#333' },
            formatter: '{b}<br/>费用: ¥{c}'
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: res.data.months,
            axisLine: { lineStyle: { color: '#d9d9d9' } },
            axisLabel: { color: '#666' }
          },
          yAxis: {
            type: 'value',
            axisLine: { show: false },
            axisTick: { show: false },
            splitLine: { lineStyle: { color: '#f0f0f0' } },
            axisLabel: { color: '#666' }
          },
          series: [{
            data: res.data.values,
            type: 'bar',
            barWidth: '50%',
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#40a9ff' },
                { offset: 1, color: '#1890ff' }
              ]),
              borderRadius: [6, 6, 0, 0]
            },
            emphasis: {
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#69c0ff' },
                  { offset: 1, color: '#40a9ff' }
                ])
              }
            }
          }]
        })
      })
      
      this.$axios.get('/dashboard/repairChart').then(res => {
        this.repairChart.setOption({
          tooltip: {
            trigger: 'item',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: '#1890ff',
            borderWidth: 1,
            textStyle: { color: '#333' },
            formatter: '{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            right: '10%',
            top: 'center',
            textStyle: { color: '#666' }
          },
          series: [{
            type: 'pie',
            radius: ['45%', '70%'],
            center: ['35%', '50%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 8,
              borderColor: '#fff',
              borderWidth: 3
            },
            label: {
              show: false
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 16,
                fontWeight: 'bold'
              },
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { 
                value: res.data[0].value, 
                name: res.data[0].name,
                itemStyle: { color: '#ff4d4f' }
              },
              { 
                value: res.data[1].value, 
                name: res.data[1].name,
                itemStyle: { color: '#faad14' }
              },
              { 
                value: res.data[2].value, 
                name: res.data[2].name,
                itemStyle: { color: '#52c41a' }
              }
            ]
          }]
        })
      })
      
      this.$axios.get('/dashboard/latestRepairs').then(res => {
        this.latestRepairs = res.data
      })
      
      this.$axios.get('/dashboard/latestComplaints').then(res => {
        this.latestComplaints = res.data
      })
    },
    handleResize() {
      if (this.feeChart) this.feeChart.resize()
      if (this.repairChart) this.repairChart.resize()
    },
    formatTime(date) {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
    },
    getStatusType(status) {
      const types = { '0': 'info', '1': 'warning', '2': 'success' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { '0': '待处理', '1': '处理中', '2': '已完成' }
      return texts[status] || '未知'
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: 0;
}

/* 统计卡片样式 */
.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  position: relative;
  overflow: hidden;
  border: 2px solid transparent;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.stat-card-1 {
  background: linear-gradient(135deg, #e6f7ff 0%, #fff 100%);
  border-color: #1890ff;
}

.stat-card-2 {
  background: linear-gradient(135deg, #f6ffed 0%, #fff 100%);
  border-color: #52c41a;
}

.stat-card-3 {
  background: linear-gradient(135deg, #fff7e6 0%, #fff 100%);
  border-color: #faad14;
}

.stat-card-4 {
  background: linear-gradient(135deg, #fff1f0 0%, #fff 100%);
  border-color: #ff4d4f;
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  margin-right: 16px;
}

.stat-card-1 .stat-icon {
  background: #1890ff;
  color: #fff;
}

.stat-card-2 .stat-icon {
  background: #52c41a;
  color: #fff;
}

.stat-card-3 .stat-icon {
  background: #faad14;
  color: #fff;
}

.stat-card-4 .stat-icon {
  background: #ff4d4f;
  color: #fff;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #262626;
  line-height: 1.2;
}

.stat-label {
  font-size: 14px;
  color: #8c8c8c;
  margin-top: 4px;
}

.stat-trend {
  position: absolute;
  top: 16px;
  right: 16px;
  font-size: 12px;
  color: #52c41a;
  font-weight: 500;
  background: rgba(82, 196, 26, 0.1);
  padding: 4px 8px;
  border-radius: 4px;
}

.stat-trend.down {
  color: #ff4d4f;
  background: rgba(255, 77, 79, 0.1);
}

/* 图表卡片样式 */
.chart-card {
  background: #fff;
  border-radius: 12px;
  border: 2px solid #e8e8e8;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
}

.chart-header {
  padding: 16px 20px;
  border-bottom: 2px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fafafa;
}

.chart-title {
  font-size: 16px;
  font-weight: 600;
  color: #262626;
}

.chart-title i {
  color: #1890ff;
  margin-right: 8px;
}

.chart-content {
  height: 320px;
  padding: 20px;
}

/* 数据卡片样式 */
.data-card {
  background: #fff;
  border-radius: 12px;
  border: 2px solid #e8e8e8;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
}

.data-header {
  padding: 16px 20px;
  border-bottom: 2px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fafafa;
}

.data-title {
  font-size: 16px;
  font-weight: 600;
  color: #262626;
}

.data-title i {
  color: #1890ff;
  margin-right: 8px;
}

/* 排名数字样式 */
.rank-num {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 600;
  color: #fff;
  background: #d9d9d9;
}

.rank-1 {
  background: #ff4d4f;
}

.rank-2 {
  background: #faad14;
}

.rank-3 {
  background: #1890ff;
}
</style>

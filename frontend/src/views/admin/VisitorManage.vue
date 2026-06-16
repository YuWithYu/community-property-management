<template>
  <div class="visitor-manage">
    <div class="page-header">
      <h2 class="page-title">
        <i class="el-icon-user"></i>
        访客管理
      </h2>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="访客姓名">
          <el-input v-model="searchForm.visitorName" placeholder="请输入访客姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待审核" :value="0"></el-option>
            <el-option label="已通过" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch" class="search-btn">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset" class="reset-btn">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-container">
      <el-table :data="tableData" v-loading="loading" border style="width: 100%" :header-cell-style="headerCellStyle">
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="ownerId" label="业主" min-width="120">
          <template slot-scope="scope">
            {{ getOwnerName(scope.row.ownerId) }}
          </template>
        </el-table-column>
        <el-table-column prop="visitorName" label="访客姓名" min-width="120"></el-table-column>
        <el-table-column prop="visitorPhone" label="访客电话" min-width="150"></el-table-column>
        <el-table-column prop="idCard" label="身份证号" min-width="180"></el-table-column>
        <el-table-column prop="visitReason" label="来访事由" min-width="150"></el-table-column>
        <el-table-column prop="visitTime" label="来访时间" min-width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.visitTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="parkingNeeded" label="停车位" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.parkingNeeded === 1 ? 'success' : 'info'" effect="plain">
              {{ scope.row.parkingNeeded === 1 ? '需要' : '不需要' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="审核状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'" effect="plain">
              {{ scope.row.status === 1 ? '已通过' : '待审核' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status === 0" type="success" size="mini" icon="el-icon-check" @click="handleApprove(scope.row)" class="action-btn">通过</el-button>
            <el-button type="danger" size="mini" icon="el-icon-delete" @click="handleDelete(scope.row)" class="action-btn">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination 
          @current-change="handleCurrentChange" 
          :current-page="page" 
          :page-size="size" 
          :total="total"
          layout="total, prev, pager, next, jumper">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'VisitorManage',
  data() {
    return {
      loading: false,
      tableData: [],
      ownerList: [],
      page: 1,
      size: 10,
      total: 0,
      searchForm: {
        visitorName: '',
        status: null
      }
    }
  },
  computed: {
    headerCellStyle() {
      return {
        background: '#f0f7ff',
        color: '#262626',
        fontWeight: '600',
        borderBottom: '2px solid #e8e8e8'
      }
    }
  },
  created() {
    this.loadData()
    this.loadOwnerList()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await this.$axios.get('/visitor/page', {
          params: {
            page: this.page,
            size: this.size,
            ...this.searchForm
          }
        })
        this.tableData = res.data.records
        this.total = res.data.total
      } catch (error) {
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    async loadOwnerList() {
      try {
        const res = await this.$axios.get('/owner/list')
        this.ownerList = res.data
      } catch (error) {
        console.error(error)
      }
    },
    handleSearch() {
      this.page = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = { visitorName: '', status: null }
      this.handleSearch()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadData()
    },
    getOwnerName(ownerId) {
      const owner = this.ownerList.find(item => item.id === ownerId)
      return owner ? owner.realName : '-'
    },
    formatDate(date) {
      if (!date) return '-'
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      const hour = String(d.getHours()).padStart(2, '0')
      const minute = String(d.getMinutes()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}`
    },
    async handleApprove(row) {
      try {
        await this.$confirm('确认通过该访客申请?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        })
        await this.$axios.put(`/visitor/approve/${row.id}`)
        this.$message.success('审核通过')
        this.loadData()
      } catch (error) {
        console.error(error)
      }
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该记录?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await this.$axios.delete(`/visitor/${row.id}`)
        this.$message.success('删除成功')
        this.loadData()
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>

<style scoped>
.visitor-manage {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid #e8e8e8;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  color: #262626;
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 0;
}

.page-title i {
  color: #1890ff;
  font-size: 24px;
}

.search-form {
  background: #fff;
  padding: 20px;
  border-radius: 12px;
  margin-bottom: 20px;
  border: 2px solid #e8e8e8;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.search-btn {
  background: #1890ff;
  border-radius: 6px;
  border: none;
  transition: all 0.3s ease;
}

.search-btn:hover {
  background: #40a9ff;
  transform: translateY(-1px);
}

.reset-btn {
  border-radius: 6px;
  border: 1px solid #d9d9d9;
  transition: all 0.3s ease;
}

.reset-btn:hover {
  border-color: #1890ff;
  color: #1890ff;
}

.table-container {
  background: #fff;
  padding: 20px;
  border-radius: 12px;
  border: 2px solid #e8e8e8;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background: #f0f7ff !important;
}

:deep(.el-table td) {
  border-bottom: 1px solid #f0f0f0;
}

.action-btn {
  border-radius: 6px;
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-pagination) {
  padding: 0;
}

:deep(.el-pagination .el-pager li) {
  border-radius: 6px;
  margin: 0 4px;
}

:deep(.el-pagination .el-pager li.active) {
  background: #1890ff;
}
</style>

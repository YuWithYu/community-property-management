<template>
  <div class="complaint-manage">
    <div class="page-header">
      <h2 class="page-title">
        <i class="el-icon-chat-dot-square"></i>
        投诉建议管理
      </h2>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待处理" :value="0"></el-option>
            <el-option label="已处理" :value="1"></el-option>
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
        <el-table-column prop="type" label="类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTypeType(scope.row.type)" effect="plain">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="150"></el-table-column>
        <el-table-column prop="content" label="内容" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'" effect="plain">
              {{ scope.row.status === 1 ? '已处理' : '待处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reply" label="回复" min-width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.reply || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" min-width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status === 0" type="primary" size="mini" icon="el-icon-edit" @click="handleReply(scope.row)" class="action-btn">回复</el-button>
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

    <!-- 回复对话框 -->
    <el-dialog title="回复投诉建议" :visible.sync="dialogVisible" width="500px" class="custom-dialog">
      <el-form :model="form" ref="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" disabled></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="3" disabled></el-input>
        </el-form-item>
        <el-form-item label="回复" prop="reply">
          <el-input v-model="form.reply" type="textarea" :rows="4" placeholder="请输入回复内容"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
        <el-button type="primary" @click="submitReply" class="confirm-btn">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ComplaintManage',
  data() {
    return {
      loading: false,
      tableData: [],
      page: 1,
      size: 10,
      total: 0,
      searchForm: {
        title: '',
        status: null
      },
      dialogVisible: false,
      form: {}
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
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await this.$axios.get('/complaint/page', {
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
    handleSearch() {
      this.page = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = { title: '', status: null }
      this.handleSearch()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadData()
    },
    getTypeType(type) {
      const types = {
        '投诉': 'danger',
        '建议': 'primary',
        '需求': 'success'
      }
      return types[type] || 'info'
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
    handleReply(row) {
      this.form = { ...row }
      this.dialogVisible = true
    },
    async submitReply() {
      if (!this.form.reply) {
        this.$message.warning('请输入回复内容')
        return
      }
      try {
        await this.$axios.put('/complaint', {
          id: this.form.id,
          reply: this.form.reply,
          status: 1
        })
        this.$message.success('回复成功')
        this.dialogVisible = false
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
        await this.$axios.delete(`/complaint/${row.id}`)
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
.complaint-manage {
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

/* 对话框样式 */
.custom-dialog :deep(.el-dialog) {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

.custom-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
  padding: 20px;
  margin: 0;
}

.custom-dialog :deep(.el-dialog__title) {
  color: #fff;
  font-weight: 600;
}

.custom-dialog :deep(.el-dialog__headerbtn .el-dialog__close) {
  color: #fff;
}

.custom-dialog :deep(.el-dialog__body) {
  padding: 30px 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.cancel-btn {
  border-radius: 6px;
  border: 1px solid #d9d9d9;
  padding: 10px 24px;
}

.confirm-btn {
  background: #1890ff;
  border-radius: 6px;
  border: none;
  padding: 10px 24px;
  transition: all 0.3s ease;
}

.confirm-btn:hover {
  background: #40a9ff;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #262626;
}

:deep(.el-input__inner) {
  border-radius: 6px;
  border: 1px solid #d9d9d9;
  transition: all 0.3s ease;
}

:deep(.el-input__inner:focus) {
  border-color: #1890ff;
  box-shadow: 0 0 0 3px rgba(24, 144, 255, 0.1);
}
</style>

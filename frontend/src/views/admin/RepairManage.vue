<template>
  <div class="repair-manage">
    <div class="page-header">
      <h2 class="page-title">
        <i class="el-icon-s-tools"></i>
        报修管理
      </h2>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="业主">
          <el-input v-model="searchForm.ownerName" placeholder="请输入业主姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待处理" :value="0"></el-option>
            <el-option label="处理中" :value="1"></el-option>
            <el-option label="已完成" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch" class="search-btn">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset" class="reset-btn">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-container">
      <el-table :data="tableData" border style="width: 100%" v-loading="loading" :header-cell-style="headerCellStyle">
        <el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
        <el-table-column prop="ownerName" label="业主" min-width="100"></el-table-column>
        <el-table-column prop="phone" label="联系电话" min-width="120"></el-table-column>
        <el-table-column prop="content" label="报修内容" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="address" label="地址" min-width="150"></el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" effect="plain">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="handlerName" label="处理人" min-width="100"></el-table-column>
        <el-table-column prop="result" label="处理结果" min-width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createTime" label="报修时间" min-width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template slot-scope="scope">
            <el-button 
              size="mini" 
              type="primary" 
              icon="el-icon-edit"
              @click="handleProcess(scope.row)"
              v-if="scope.row.status !== '2'"
              class="action-btn">
              处理
            </el-button>
            <el-button size="mini" @click="handleView(scope.row)" class="view-btn">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :current-page="page.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="page.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total">
        </el-pagination>
      </div>
    </div>

    <!-- 处理对话框 -->
    <el-dialog title="处理报修" :visible.sync="dialogVisible" width="500px" class="custom-dialog">
      <el-form :model="form" :rules="formRules" ref="form" label-width="100px">
        <el-form-item label="报修内容">
          <el-input v-model="form.content" type="textarea" :rows="3" disabled></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" style="width: 100%;">
            <el-option label="处理中" :value="1"></el-option>
            <el-option label="已完成" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处理结果" prop="reply">
          <el-input v-model="form.reply" type="textarea" :rows="3" placeholder="请输入处理结果"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading" class="confirm-btn">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看对话框 -->
    <el-dialog title="报修详情" :visible.sync="viewVisible" width="600px" class="custom-dialog">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="业主">{{ viewData.ownerName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ viewData.phone }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ viewData.address }}</el-descriptions-item>
        <el-descriptions-item label="报修内容" :span="2">{{ viewData.content }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(viewData.status)" effect="plain">{{ getStatusText(viewData.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理人">{{ viewData.handlerName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理结果" :span="2">{{ viewData.result || '-' }}</el-descriptions-item>
        <el-descriptions-item label="报修时间">{{ formatDate(viewData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ formatDate(viewData.updateTime) }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'RepairManage',
  data() {
    return {
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      viewVisible: false,
      searchForm: {
        ownerName: '',
        status: null
      },
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      form: {
        id: null,
        content: '',
        status: 1,
        reply: ''
      },
      viewData: {},
      formRules: {
        status: [{ required: true, message: '请选择状态', trigger: 'change' }],
        reply: [{ required: true, message: '请输入处理结果', trigger: 'blur' }]
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
  },
  methods: {
    loadData() {
      this.loading = true
      const params = {
        current: this.page.current,
        size: this.page.size,
        ...this.searchForm
      }
      this.$axios.get('/repair/page', { params }).then(res => {
        this.tableData = res.data.records
        this.page.total = res.data.total
      }).finally(() => {
        this.loading = false
      })
    },
    handleSearch() {
      this.page.current = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        ownerName: '',
        status: null
      }
      this.handleSearch()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadData()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.page.current = 1
      this.loadData()
    },
    handleProcess(row) {
      this.form = {
        id: row.id,
        content: row.content,
        status: row.status === '0' ? 1 : 2,
        reply: row.result || ''
      }
      this.dialogVisible = true
    },
    handleView(row) {
      this.viewData = row
      this.viewVisible = true
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          this.$axios.put('/repair', this.form).then(() => {
            this.$message.success('处理成功')
            this.dialogVisible = false
            this.loadData()
          }).finally(() => {
            this.submitLoading = false
          })
        }
      })
    },
    getStatusType(status) {
      const types = { 0: 'info', 1: 'warning', 2: 'success' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 0: '待处理', 1: '处理中', 2: '已完成' }
      return texts[status] || '未知'
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      const hour = String(d.getHours()).padStart(2, '0')
      const minute = String(d.getMinutes()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}`
    }
  }
}
</script>

<style scoped>
.repair-manage {
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

.view-btn {
  border-radius: 6px;
  border: 1px solid #d9d9d9;
  transition: all 0.3s ease;
}

.view-btn:hover {
  border-color: #1890ff;
  color: #1890ff;
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

:deep(.el-descriptions) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-descriptions__header) {
  background: #f0f7ff;
  padding: 16px;
  margin: 0;
}

:deep(.el-descriptions__title) {
  font-weight: 600;
  color: #262626;
}

:deep(.el-descriptions__body) {
  background: #fff;
}

:deep(.el-descriptions-item__label) {
  background: #f0f7ff;
  font-weight: 500;
  color: #262626;
}
</style>

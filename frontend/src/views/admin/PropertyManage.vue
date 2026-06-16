<template>
  <div class="property-manage">
    <div class="page-header">
      <h2 class="page-title">
        <i class="el-icon-office-building"></i>
        房产管理
      </h2>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd" class="add-btn">
        新增房产
      </el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="楼栋号">
          <el-input v-model="searchForm.buildingNo" placeholder="请输入楼栋号" clearable></el-input>
        </el-form-item>
        <el-form-item label="单元号">
          <el-input v-model="searchForm.unitNo" placeholder="请输入单元号" clearable></el-input>
        </el-form-item>
        <el-form-item label="房号">
          <el-input v-model="searchForm.roomNo" placeholder="请输入房号" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="空置" :value="0"></el-option>
            <el-option label="已售" :value="1"></el-option>
            <el-option label="出租" :value="2"></el-option>
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
        <el-table-column prop="buildingNo" label="楼栋号" min-width="100"></el-table-column>
        <el-table-column prop="unitNo" label="单元号" min-width="100"></el-table-column>
        <el-table-column prop="roomNo" label="房号" min-width="100"></el-table-column>
        <el-table-column prop="area" label="面积(m²)" min-width="100"></el-table-column>
        <el-table-column prop="type" label="户型" min-width="100"></el-table-column>
        <el-table-column label="业主" min-width="100">
          <template slot-scope="scope">
            {{ getOwnerName(scope.row.ownerId) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" effect="plain">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)" class="action-btn">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)" class="action-btn">删除</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" class="custom-dialog">
      <el-form :model="form" :rules="formRules" ref="form" label-width="100px">
        <el-form-item label="楼栋号" prop="buildingNo">
          <el-input v-model="form.buildingNo" placeholder="请输入楼栋号"></el-input>
        </el-form-item>
        <el-form-item label="单元号" prop="unitNo">
          <el-input v-model="form.unitNo" placeholder="请输入单元号"></el-input>
        </el-form-item>
        <el-form-item label="房号" prop="roomNo">
          <el-input v-model="form.roomNo" placeholder="请输入房号"></el-input>
        </el-form-item>
        <el-form-item label="面积" prop="area">
          <el-input-number v-model="form.area" :min="0" :precision="2" style="width: 100%;" placeholder="请输入面积"></el-input-number>
        </el-form-item>
        <el-form-item label="户型" prop="type">
          <el-input v-model="form.type" placeholder="如：三室两厅"></el-input>
        </el-form-item>
        <el-form-item label="业主">
          <el-select v-model="form.ownerId" filterable clearable placeholder="请选择业主" style="width: 100%;">
            <el-option
              v-for="item in ownerList"
              :key="item.id"
              :label="item.realName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" style="width: 100%;">
            <el-option label="空置" :value="0"></el-option>
            <el-option label="已售" :value="1"></el-option>
            <el-option label="出租" :value="2"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading" class="confirm-btn">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'PropertyManage',
  data() {
    return {
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      isEdit: false,
      ownerList: [],
      searchForm: {
        buildingNo: '',
        unitNo: '',
        roomNo: '',
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
        buildingNo: '',
        unitNo: '',
        roomNo: '',
        area: 0,
        type: '',
        ownerId: null,
        status: 0
      },
      formRules: {
        buildingNo: [{ required: true, message: '请输入楼栋号', trigger: 'blur' }],
        unitNo: [{ required: true, message: '请输入单元号', trigger: 'blur' }],
        roomNo: [{ required: true, message: '请输入房号', trigger: 'blur' }],
        area: [{ required: true, message: '请输入面积', trigger: 'blur' }],
        type: [{ required: true, message: '请输入户型', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑房产' : '新增房产'
    },
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
    this.loadOwners()
  },
  methods: {
    loadData() {
      this.loading = true
      const params = {
        current: this.page.current,
        size: this.page.size,
        ...this.searchForm
      }
      this.$axios.get('/property/page', { params }).then(res => {
        this.tableData = res.data.records
        this.page.total = res.data.total
      }).finally(() => {
        this.loading = false
      })
    },
    loadOwners() {
      this.$axios.get('/owner/list').then(res => {
        this.ownerList = res.data
      })
    },
    handleSearch() {
      this.page.current = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        buildingNo: '',
        unitNo: '',
        roomNo: '',
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
    handleAdd() {
      this.isEdit = false
      this.form = {
        id: null,
        buildingNo: '',
        unitNo: '',
        roomNo: '',
        area: 0,
        type: '',
        ownerId: null,
        status: 0
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该房产？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/property/${row.id}`).then(() => {
          this.$message.success('删除成功')
          this.loadData()
        })
      })
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          const request = this.isEdit
            ? this.$axios.put('/property', this.form)
            : this.$axios.post('/property', this.form)
          request.then(() => {
            this.$message.success(this.isEdit ? '修改成功' : '新增成功')
            this.dialogVisible = false
            this.loadData()
          }).finally(() => {
            this.submitLoading = false
          })
        }
      })
    },
    getStatusType(status) {
      const types = { 0: 'info', 1: 'success', 2: 'warning' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 0: '空置', 1: '已售', 2: '出租' }
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
    },
    getOwnerName(ownerId) {
      if (!ownerId) return '-'
      const owner = this.ownerList.find(item => item.id === ownerId)
      return owner ? owner.realName : '-'
    }
  }
}
</script>

<style scoped>
.property-manage {
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

.add-btn {
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
  border: none;
  border-radius: 8px;
  padding: 10px 20px;
  font-weight: 500;
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
  transition: all 0.3s ease;
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(24, 144, 255, 0.4);
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

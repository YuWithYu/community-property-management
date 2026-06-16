<template>
  <div class="parking-manage">
    <div class="page-header">
      <h2 class="page-title">
        <i class="el-icon-truck"></i>
        车位管理
      </h2>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd" class="add-btn">
        新增车位
      </el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="车位编号">
          <el-input v-model="searchForm.spaceNo" placeholder="请输入车位编号" clearable></el-input>
        </el-form-item>
        <el-form-item label="区域">
          <el-input v-model="searchForm.area" placeholder="请输入区域" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="空闲" value="0"></el-option>
            <el-option label="已售" value="1"></el-option>
            <el-option label="已租" value="2"></el-option>
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
        <el-table-column prop="spaceNo" label="车位编号" min-width="120"></el-table-column>
        <!-- <el-table-column prop="area" label="区域" min-width="120"></el-table-column> -->
        <el-table-column prop="type" label="类型" min-width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.type === 1 ? 'primary' : 'success'" effect="plain">
              {{ scope.row.type === 1 ? '产权车位' : '租赁车位' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格(元)" min-width="100"></el-table-column>
        <el-table-column prop="ownerName" label="业主" min-width="100"></el-table-column>
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
        <el-form-item label="车位编号" prop="spaceNo">
          <el-input v-model="form.spaceNo" placeholder="请输入车位编号"></el-input>
        </el-form-item>
        <!-- <el-form-item label="区域" prop="area">
          <el-input v-model="form.area" placeholder="请输入区域"></el-input>
        </el-form-item> -->
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio :label="1">产权车位</el-radio>
            <el-radio :label="2">租赁车位</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%;" placeholder="请输入价格"></el-input-number>
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
            <el-option label="空闲" :value="0"></el-option>
            <el-option label="已售" :value="1"></el-option>
            <el-option label="已租" :value="2"></el-option>
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
  name: 'ParkingManage',
  data() {
    return {
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      isEdit: false,
      ownerList: [],
      searchForm: {
        spaceNo: '',
        area: '',
        status: ''
      },
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      form: {
        id: null,
        spaceNo: '',
        area: '',
        type: 1,
        price: 0,
        ownerId: null,
        status: 0
      },
      formRules: {
        spaceNo: [{ required: true, message: '请输入车位编号', trigger: 'blur' }],
        area: [{ required: true, message: '请输入区域', trigger: 'blur' }],
        type: [{ required: true, message: '请选择类型', trigger: 'change' }],
        price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑车位' : '新增车位'
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
      this.$axios.get('/parking/page', { params }).then(res => {
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
        spaceNo: '',
        area: '',
        status: ''
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
        spaceNo: '',
        area: '',
        type: 1,
        price: 0,
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
      this.$confirm('确认删除该车位？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/parking/${row.id}`).then(() => {
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
            ? this.$axios.put('/parking', this.form)
            : this.$axios.post('/parking', this.form)
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
      const types = { '0': 'success', '1': 'danger', '2': 'warning' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { '0': '空闲', '1': '已售', '2': '已租' }
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
.parking-manage {
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

<template>
  <div class="fee-manage">
    <div class="page-header">
      <h2 class="page-title">
        <i class="el-icon-s-order"></i>
        费用管理
      </h2>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd" class="add-btn">
        新增费用
      </el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="业主">
          <el-select v-model="searchForm.ownerId" placeholder="请选择业主" clearable style="width: 200px">
            <el-option v-for="item in ownerList" :key="item.id" :label="item.realName" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="费用类型">
          <el-select v-model="searchForm.type" placeholder="请选择类型" clearable>
            <el-option label="物业费" value="物业费"></el-option>
            <el-option label="水费" value="水费"></el-option>
            <el-option label="电费" value="电费"></el-option>
            <el-option label="燃气费" value="燃气费"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="未缴费" :value="0"></el-option>
            <el-option label="已缴费" :value="1"></el-option>
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
        <el-table-column prop="type" label="费用类型" min-width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTypeType(scope.row.type)" effect="plain">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" min-width="100" align="center">
          <template slot-scope="scope">
            <span style="color: #f5222d; font-weight: 600;">¥{{ scope.row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="feeMonth" label="费用月份" min-width="120" align="center">
          <template slot-scope="scope">
            {{ formatMonth(scope.row.feeMonth) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" min-width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'" effect="plain">
              {{ scope.row.status === 1 ? '已缴费' : '未缴费' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="payTime" label="缴费时间" min-width="160">
          <template slot-scope="scope">
            {{ scope.row.payTime ? formatDate(scope.row.payTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleEdit(scope.row)" class="action-btn">编辑</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" class="custom-dialog">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="业主" prop="ownerId">
          <el-select v-model="form.ownerId" placeholder="请选择业主" style="width: 100%">
            <el-option v-for="item in ownerList" :key="item.id" :label="item.realName" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="费用类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="物业费" value="物业费"></el-option>
            <el-option label="水费" value="水费"></el-option>
            <el-option label="电费" value="电费"></el-option>
            <el-option label="燃气费" value="燃气费"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number v-model="form.amount" :precision="2" :min="0" style="width: 100%" placeholder="请输入金额"></el-input-number>
        </el-form-item>
        <el-form-item label="费用月份" prop="feeMonth">
          <el-date-picker v-model="form.feeMonth" type="month" placeholder="选择月份" style="width: 100%"></el-date-picker>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">未缴费</el-radio>
            <el-radio :label="1">已缴费</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
        <el-button type="primary" @click="submitForm" class="confirm-btn">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'FeeManage',
  data() {
    return {
      loading: false,
      tableData: [],
      ownerList: [],
      page: 1,
      size: 10,
      total: 0,
      searchForm: {
        ownerId: null,
        type: '',
        status: null
      },
      dialogVisible: false,
      dialogTitle: '新增费用',
      form: {
        ownerId: null,
        type: '',
        amount: 0,
        feeMonth: '',
        status: 0
      },
      rules: {
        ownerId: [{ required: true, message: '请选择业主', trigger: 'change' }],
        type: [{ required: true, message: '请选择费用类型', trigger: 'change' }],
        amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
        feeMonth: [{ required: true, message: '请选择费用月份', trigger: 'change' }]
      },
      isEdit: false
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
        const res = await this.$axios.get('/fee/page', {
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
      this.searchForm = { ownerId: null, type: '', status: null }
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
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      const hour = String(d.getHours()).padStart(2, '0')
      const minute = String(d.getMinutes()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}`
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增费用'
      this.form = {
        ownerId: null,
        type: '',
        amount: 0,
        feeMonth: '',
        status: 0
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑费用'
      this.form = { ...row }
      this.dialogVisible = true
    },
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            if (this.isEdit) {
              await this.$axios.put('/fee', this.form)
              this.$message.success('修改成功')
            } else {
              await this.$axios.post('/fee', this.form)
              this.$message.success('新增成功')
            }
            this.dialogVisible = false
            this.loadData()
          } catch (error) {
            console.error(error)
          }
        }
      })
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该记录?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await this.$axios.delete(`/fee/${row.id}`)
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
.fee-manage {
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

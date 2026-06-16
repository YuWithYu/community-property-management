<template>
  <div class="visitor-container">
    <el-card>
      <div slot="header" class="card-header">
        <span>访客记录</span>
        <el-button type="primary" @click="handleAdd">录入访客</el-button>
      </div>
      
      <el-table :data="visitorList" style="width: 100%">
        <el-table-column prop="visitorName" label="访客姓名" width="120"></el-table-column>
        <el-table-column prop="visitorPhone" label="访客电话" width="150"></el-table-column>
        <el-table-column prop="idCard" label="身份证号" min-width="180"></el-table-column>
        <el-table-column prop="visitReason" label="来访事由" min-width="150"></el-table-column>
        <el-table-column prop="visitTime" label="来访时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.visitTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="parkingNeeded" label="停车位" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.parkingNeeded === 1 ? 'success' : 'info'">
              {{ scope.row.parkingNeeded === 1 ? '需要' : '不需要' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="审核状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'">
              {{ scope.row.status === 1 ? '已通过' : '待审核' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      
      <el-empty v-if="visitorList.length === 0" description="暂无访客记录"></el-empty>
    </el-card>
    
    <!-- 新增访客对话框 -->
    <el-dialog title="录入访客信息" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="访客姓名" prop="visitorName">
          <el-input v-model="form.visitorName" placeholder="请输入访客姓名"></el-input>
        </el-form-item>
        <el-form-item label="访客电话" prop="visitorPhone">
          <el-input v-model="form.visitorPhone" placeholder="请输入访客电话"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号"></el-input>
        </el-form-item>
        <el-form-item label="来访事由" prop="visitReason">
          <el-input v-model="form.visitReason" placeholder="请输入来访事由"></el-input>
        </el-form-item>
        <el-form-item label="来访时间" prop="visitTime">
          <el-date-picker v-model="form.visitTime" type="datetime" placeholder="选择来访时间" style="width: 100%"></el-date-picker>
        </el-form-item>
        <el-form-item label="停车位" prop="parkingNeeded">
          <el-radio-group v-model="form.parkingNeeded">
            <el-radio :label="0">不需要</el-radio>
            <el-radio :label="1">需要</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'UserVisitor',
  data() {
    return {
      visitorList: [],
      dialogVisible: false,
      form: {
        visitorName: '',
        visitorPhone: '',
        idCard: '',
        visitReason: '',
        visitTime: '',
        parkingNeeded: 0
      },
      rules: {
        visitorName: [{ required: true, message: '请输入访客姓名', trigger: 'blur' }],
        visitorPhone: [{ required: true, message: '请输入访客电话', trigger: 'blur' }],
        idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
        visitReason: [{ required: true, message: '请输入来访事由', trigger: 'blur' }],
        visitTime: [{ required: true, message: '请选择来访时间', trigger: 'change' }]
      }
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    }
  },
  created() {
    this.loadVisitorList()
  },
  methods: {
    async loadVisitorList() {
      try {
        const res = await this.$axios.get('/visitor/page', {
          params: { page: 1, size: 100 }
        })
        this.visitorList = res.data.records.filter(item => item.ownerId === this.user.id)
      } catch (error) {
        console.error(error)
      }
    },
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString()
    },
    handleAdd() {
      this.form = {
        visitorName: '',
        visitorPhone: '',
        idCard: '',
        visitReason: '',
        visitTime: '',
        parkingNeeded: 0
      }
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await this.$axios.post('/visitor', {
              ...this.form,
              ownerId: this.user.id
            })
            this.$message.success('录入成功')
            this.dialogVisible = false
            this.loadVisitorList()
          } catch (error) {
            console.error(error)
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.visitor-container {
  max-width: 1000px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

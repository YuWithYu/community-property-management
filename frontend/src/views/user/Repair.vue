<template>
  <div class="repair-container">
    <el-card>
      <div slot="header" class="card-header">
        <span>我的报修</span>
        <el-button type="primary" @click="handleAdd">新增报修</el-button>
      </div>
      
      <el-table :data="repairList" style="width: 100%">
        <el-table-column prop="title" label="报修标题" min-width="150"></el-table-column>
        <el-table-column prop="location" label="报修地点" width="150"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reply" label="回复" min-width="200">
          <template slot-scope="scope">
            {{ scope.row.reply || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 新增报修对话框 -->
    <el-dialog title="新增报修" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="报修标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入报修标题"></el-input>
        </el-form-item>
        <el-form-item label="报修地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入报修地点"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="报修内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请详细描述报修内容"></el-input>
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
  name: 'UserRepair',
  data() {
    return {
      repairList: [],
      dialogVisible: false,
      form: {
        title: '',
        content: '',
        location: '',
        contactPhone: ''
      },
      rules: {
        title: [{ required: true, message: '请输入报修标题', trigger: 'blur' }],
        location: [{ required: true, message: '请输入报修地点', trigger: 'blur' }],
        contactPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
        content: [{ required: true, message: '请输入报修内容', trigger: 'blur' }]
      }
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    }
  },
  created() {
    this.loadRepairList()
  },
  methods: {
    async loadRepairList() {
      try {
        const res = await this.$axios.get('/repair/page', {
          params: { page: 1, size: 100 }
        })
        this.repairList = res.data.records.filter(item => item.ownerId === this.user.id)
      } catch (error) {
        console.error(error)
      }
    },
    getStatusType(status) {
      const types = ['warning', 'primary', 'success']
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = ['待处理', '处理中', '已完成']
      return texts[status] || '未知'
    },
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString()
    },
    handleAdd() {
      this.form = {
        title: '',
        content: '',
        location: '',
        contactPhone: this.user.phone
      }
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await this.$axios.post('/repair', {
              ...this.form,
              ownerId: this.user.id
            })
            this.$message.success('提交成功')
            this.dialogVisible = false
            this.loadRepairList()
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
.repair-container {
  max-width: 1000px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

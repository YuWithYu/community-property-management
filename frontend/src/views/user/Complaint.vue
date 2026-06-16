<template>
  <div class="complaint-container">
    <el-card>
      <div slot="header" class="card-header">
        <span>我的投诉建议</span>
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </div>
      
      <el-table :data="complaintList" style="width: 100%">
        <el-table-column prop="type" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getTypeType(scope.row.type)">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="150"></el-table-column>
        <el-table-column prop="content" label="内容" min-width="200">
          <template slot-scope="scope">
            <el-tooltip :content="scope.row.content" placement="top">
              <span class="ellipsis">{{ scope.row.content }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'">
              {{ scope.row.status === 1 ? '已处理' : '待处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reply" label="回复" min-width="150">
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
    
    <!-- 新增对话框 -->
    <el-dialog title="新增投诉建议" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="投诉" value="投诉"></el-option>
            <el-option label="建议" value="建议"></el-option>
            <el-option label="需求" value="需求"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入详细内容"></el-input>
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
  name: 'UserComplaint',
  data() {
    return {
      complaintList: [],
      dialogVisible: false,
      form: {
        type: '',
        title: '',
        content: ''
      },
      rules: {
        type: [{ required: true, message: '请选择类型', trigger: 'change' }],
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
      }
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    }
  },
  created() {
    this.loadComplaintList()
  },
  methods: {
    async loadComplaintList() {
      try {
        const res = await this.$axios.get('/complaint/page', {
          params: { page: 1, size: 100 }
        })
        this.complaintList = res.data.records.filter(item => item.ownerId === this.user.id)
      } catch (error) {
        console.error(error)
      }
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
      return new Date(date).toLocaleString()
    },
    handleAdd() {
      this.form = {
        type: '',
        title: '',
        content: ''
      }
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await this.$axios.post('/complaint', {
              ...this.form,
              ownerId: this.user.id
            })
            this.$message.success('提交成功')
            this.dialogVisible = false
            this.loadComplaintList()
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
.complaint-container {
  max-width: 1000px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.ellipsis {
  display: inline-block;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>

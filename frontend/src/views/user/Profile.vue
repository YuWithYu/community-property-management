<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <div slot="header" class="card-header">
        <span>个人信息</span>
        <el-button type="primary" size="small" @click="handleEdit">编辑</el-button>
      </div>
      
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户名">{{ user.username }}</el-descriptions-item>
        <el-descriptions-item label="真实姓名">{{ user.realName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ user.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ user.email }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ user.idCard }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="user.status === 1 ? 'success' : 'danger'">{{ user.status === 1 ? '正常' : '禁用' }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
    
    <el-card class="password-card">
      <div slot="header">
        <span>修改密码</span>
      </div>
      
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordForm" label-width="100px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请确认新密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleChangePassword">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 编辑对话框 -->
    <el-dialog title="编辑个人信息" :visible.sync="dialogVisible" width="500px">
      <el-form :model="editForm" :rules="editRules" ref="editForm" label-width="100px">
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="editForm.realName"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="editForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'UserProfile',
  data() {
    const validateConfirm = (rule, value, callback) => {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      dialogVisible: false,
      editForm: {},
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      editRules: {
        realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }]
      },
      passwordRules: {
        oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
        newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
        confirmPassword: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { validator: validateConfirm, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    }
  },
  methods: {
    handleEdit() {
      this.editForm = { ...this.user }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.editForm.validate(async valid => {
        if (valid) {
          try {
            await this.$axios.put('/user/update', this.editForm)
            this.$store.dispatch('login', { user: this.editForm, type: 'owner' })
            this.$message.success('保存成功')
            this.dialogVisible = false
          } catch (error) {
            console.error(error)
          }
        }
      })
    },
    handleChangePassword() {
      this.$refs.passwordForm.validate(async valid => {
        if (valid) {
          try {
            await this.$axios.put('/user/password', this.passwordForm)
            this.$message.success('密码修改成功')
            this.passwordForm = { oldPassword: '', newPassword: '', confirmPassword: '' }
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
.profile-container {
  max-width: 800px;
  margin: 0 auto;
}

.profile-card,
.password-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

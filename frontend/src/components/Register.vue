<template>
  <div class="register-container">
    <div class="register-box">
      <h2>🍽️ 用户注册</h2>
      <el-form :model="registerForm" :rules="registerRules" ref="registerForm" class="register-form">
        <el-form-item prop="username">
          <el-input 
            v-model="registerForm.username" 
            placeholder="请输入用户名"
            prefix-icon="el-icon-user">
          </el-input>
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input 
            v-model="registerForm.password" 
            type="password" 
            placeholder="请输入密码（6-20位数字和字母组合）"
            prefix-icon="el-icon-lock">
          </el-input>
        </el-form-item>
        
        <el-form-item prop="confirmPassword">
          <el-input 
            v-model="registerForm.confirmPassword" 
            type="password" 
            placeholder="请确认密码"
            prefix-icon="el-icon-lock">
          </el-input>
        </el-form-item>
        
        <el-form-item prop="phone">
          <el-input 
            v-model="registerForm.phone" 
            placeholder="请输入手机号"
            prefix-icon="el-icon-phone">
          </el-input>
        </el-form-item>
        
        <el-form-item prop="userType">
          <el-select v-model="registerForm.userType" placeholder="请选择用户角色" style="width: 100%">
            <el-option label="学生" :value="1"></el-option>
            <el-option label="管理员" :value="2"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleRegister" 
            :loading="registerLoading"
            style="width: 100%">
            注册
          </el-button>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            link
            @click="goToLogin"
            style="width: 100%">
            已有账号？返回登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Register',
  data() {
    // 密码确认验证
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };
    
    return {
      registerLoading: false,
      registerForm: {
        username: '',
        password: '',
        confirmPassword: '',
        phone: '',
        userType: null
      },
      registerRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '用户名长度在2到20个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在6到20个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        userType: [
          { required: true, message: '请选择用户角色', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    // 处理注册
    async handleRegister() {
      try {
        // 验证表单
        await this.$refs.registerForm.validate()
        this.registerLoading = true
        
        // 发送注册请求
        const response = await this.registerRequest(this.registerForm)
        
        if (response.code === 200) {
          this.$message.success('注册成功！请返回登录页面登录')
          this.goToLogin()
        } else {
          this.$message.error(response.message || '注册失败')
        }
      } catch (error) {
        this.$message.error('注册失败：' + error.message)
      } finally {
        this.registerLoading = false
      }
    },

    // 返回登录页面
    goToLogin() {
      this.$emit('switch-to-login')
    },

    // 发送注册请求
    async registerRequest(formData) {
      try {
        // 调用后端注册API
        const response = await fetch('/user/register', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            username: formData.username,
            password: formData.password,
            phone: formData.phone,
            userType: formData.userType
          })
        });
        
        const result = await response.json();
        return result;
      } catch (error) {
        console.error('注册请求失败:', error);
        return {
          code: 500,
          message: '注册失败：网络错误，请检查后端服务是否启动'
        };
      }
    }
  }
}
</script>

<style scoped>
.register-container {
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.register-box {
  background: white;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.2);
  width: 400px;
  text-align: center;
}

.register-box h2 {
  margin-bottom: 30px;
  color: #333;
}

.register-form {
  margin-bottom: 20px;
}
</style> 
<template>
    <div id="app">
      <!-- 登录页面 -->
      <div v-if="!isLoggedIn && currentPage === 'login'" class="login-container">
        <div class="login-box">
          <h2>🍽️ 食堂点餐系统</h2>
          <el-form :model="loginForm" :rules="loginRules" ref="loginForm" class="login-form">
            <el-form-item prop="username">
              <el-input 
                v-model="loginForm.username" 
                placeholder="请输入用户名"
                prefix-icon="el-icon-user">
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input 
                v-model="loginForm.password" 
                type="password" 
                placeholder="请输入密码"
                prefix-icon="el-icon-lock"
                @keyup.enter.native="handleLogin">
              </el-input>
            </el-form-item>
            <el-form-item prop="userType">
              <el-select v-model="loginForm.userType" placeholder="请选择用户角色" style="width: 100%">
                <el-option label="学生" :value="1"></el-option>
                <el-option label="管理员" :value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button 
                type="primary" 
                @click="handleLogin" 
                :loading="loginLoading"
                style="width: 100%">
                登录
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button 
                link
                @click="goToRegister">
                没有账号？立即注册
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
  <!-- 条件渲染切换页面 -->
      <!-- 注册页面 -->
      <Register v-if="!isLoggedIn && currentPage === 'register'" 
                @switch-to-login="goToLogin" 
                @register-success="handleRegisterSuccess" />
  
      <!-- 主应用页面 -->
      <el-container v-if="isLoggedIn">
        <!-- 头部 -->
        <el-header class="header">
          <h1>🍽️ 食堂点餐系统</h1>
          <div class="header-info">
            <span>欢迎，{{ currentUser.username }} ({{ currentUser.userTypeDesc }})</span>
            <el-button link @click="handleLogout" style="color: white; margin-left: 20px;">
              退出登录
            </el-button>
          </div>
        </el-header>
        
        <!-- 主要内容 -->
        <el-main>
          <!-- 消费者端 -->
          <div v-if="currentUser.userType === 1">
            <el-tabs v-model="consumerActiveTab" type="border-card">
              <!-- 菜品展示 -->
              <el-tab-pane label="菜品展示" name="dishes">
                <DishDisplay />
              </el-tab-pane>
              
              <!-- 套餐购买 -->
              <el-tab-pane label="套餐购买" name="setmeals">
                <SetmealPurchase :currentUser="currentUser" @order-created="handleOrderCreated" />
              </el-tab-pane>
              
              <!-- 我的订单 -->
              <el-tab-pane label="我的订单" name="my-orders">
                <UserOrderHistory ref="userOrderHistory" :currentUser="currentUser" />
              </el-tab-pane>
            </el-tabs>
          </div>
  
          <!-- 管理员端 -->
          <div v-if="currentUser.userType === 2">
            <el-tabs v-model="staffActiveTab" type="border-card">
              <!-- 菜品管理 -->
              <el-tab-pane label="菜品管理" name="dishes">
                <DishManagement />
              </el-tab-pane>
              
              <!-- 订单管理 -->
              <el-tab-pane label="订单管理" name="orders">
                <OrderManagement />
              </el-tab-pane>
              
              <!-- 报表统计 -->
              <el-tab-pane label="报表统计" name="reports">
                <ReportStatistics />
              </el-tab-pane>
              
              <!-- 用户偏好分析 -->
              <el-tab-pane label="用户偏好分析" name="preferences">
                <UserPreferenceAnalysis />
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-main>
      </el-container>
    </div>
  </template>
  
  <script>
  // 导入组件
  import DishDisplay from './components/DishDisplay.vue'
  import SetmealPurchase from './components/SetmealPurchase.vue'
  import UserOrderHistory from './components/UserOrderHistory.vue'
  import OrderManagement from './components/OrderManagement.vue'
  import ReportStatistics from './components/ReportStatistics.vue'
  import UserPreferenceAnalysis from './components/UserPreferenceAnalysis.vue'
  import Register from './components/Register.vue'
  import DishManagement from './components/DishManagement.vue'

  //注册组件
  export default {
    name: 'App',
    components: {
      DishDisplay,
      SetmealPurchase,
      UserOrderHistory,
      OrderManagement,
      ReportStatistics,
      UserPreferenceAnalysis,
      Register,
      DishManagement
    },
    data() {
      return {
        isLoggedIn: false,//是否登录
        currentPage: 'login',//当前页面
        loginLoading: false,//登录加载
        currentUser: {},//当前用户
        loginForm: {
          username: '',
          password: '',
          userType: null
        },
        loginRules: {
          username: [
            { required: true, message: '请输入用户名', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' }
          ],
          userType: [
            { required: true, message: '请选择用户角色', trigger: 'change' }
          ]
        },
        consumerActiveTab: 'dishes',
        staffActiveTab: 'dishes'
      }
    },
    methods: {
      async handleLogin() {
        try {
          await this.$refs.loginForm.validate()
          this.loginLoading = true
          
          const response = await this.loginRequest(this.loginForm.username, this.loginForm.password, this.loginForm.userType)
          
          if (response.code === 200) {
            this.currentUser = response.data
            this.isLoggedIn = true
            this.$message.success('登录成功！')
          } else {
            this.$message.error(response.message || '登录失败')
          }
        } catch (error) {
          this.$message.error('登录失败：' + error.message)
        } finally {
          this.loginLoading = false
        }
      },
  
      goToRegister() {
        this.currentPage = 'register'
      },
  
      goToLogin() {
        this.currentPage = 'login'
        this.loginForm = {
          username: '',
          password: '',
          userType: null
        }
      },
  
      handleLogout() {
        this.isLoggedIn = false
        this.currentUser = {}
        this.currentPage = 'login'
        this.loginForm = { username: '', password: '', userType: null }
        this.$message.success('已退出登录')
      },
  
      async loginRequest(username, password, userType) {
        try {
          const response = await fetch('/user/login', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              username: username,
              password: password,
              userType: userType
            })
          });
          
          const result = await response.json();
          return result;
        } catch (error) {
          console.error('登录请求失败:', error);
          return {
            code: 500,
            message: '登录失败：网络错误，请检查后端服务是否启动'
          };
        }
      },
  
      handleRegisterSuccess(userData) {
        this.$message.success('注册成功！')
        this.currentPage = 'login'
      },

      handleOrderCreated() {
        if (this.$refs.userOrderHistory) {
            this.$refs.userOrderHistory.loadOrders()
            }
        }
    }
  }
  </script>
  
  <style>
  #app {
    font-family: 'Microsoft YaHei', Arial, sans-serif;
    height: 100vh;
  }
  
  .login-container {
    height: 100vh;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .login-box {
    background: white;
    padding: 40px;
    border-radius: 10px;
    box-shadow: 0 10px 30px rgba(0,0,0,0.2);
    width: 400px;
    text-align: center;
  }
  
  .login-box h2 {
    margin-bottom: 30px;
    color: #333;
  }
  
  .login-form {
    margin-bottom: 20px;
  }
  
  .header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    padding: 0 20px;
  }
  
  .header-info {
    display: flex;
    align-items: center;
  }
  
  .el-tabs--border-card {
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  }
  
  @media (max-width: 768px) {/* 响应式布局 */
    .login-box {
      width: 90%;
      padding: 20px;
    }
    
    .header {
      padding: 0 10px;
    }
    
    .header h1 {
      font-size: 18px;
    }
  }
  </style>
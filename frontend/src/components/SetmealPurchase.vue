<template>
  <div class="setmeal-purchase">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>套餐购买</span>
        </div>
      </template>

      <!-- 搜索和筛选条件 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="适合人群">
          <el-input 
            v-model="searchForm.suitableCrowd" 
            placeholder="请选择适合人群" 
            readonly
            style="width: 140px;">
            <template #append>
              <el-button @click="clearSuitableCrowd" v-if="searchForm.suitableCrowd" size="small">清除</el-button>
            </template>
          </el-input>
          <el-dropdown @command="selectSuitableCrowd" style="margin-left: 5px;">
            <el-button type="primary" size="small">
              选择<el-icon class="el-icon--right"><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="学生">学生</el-dropdown-item>
                <el-dropdown-item command="上班族">上班族</el-dropdown-item>
                <el-dropdown-item command="素食者">素食者</el-dropdown-item>
                <el-dropdown-item command="健身人群">健身人群</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-form-item>
        <el-form-item label="维生素类型">
          <el-input 
            v-model="searchForm.vitamin" 
            placeholder="请选择维生素类型" 
            readonly
            style="width: 140px;">
            <template #append>
              <el-button @click="clearVitamin" v-if="searchForm.vitamin" size="small">清除</el-button>
            </template>
          </el-input>
          <el-dropdown @command="selectVitamin" style="margin-left: 5px;">
            <el-button type="primary" size="small">
              选择<el-icon class="el-icon--right"><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="维生素A">维生素A</el-dropdown-item>
                <el-dropdown-item command="维生素C">维生素C</el-dropdown-item>
                <el-dropdown-item command="维生素B">维生素B</el-dropdown-item>
                <el-dropdown-item command="维生素D">维生素D</el-dropdown-item>
                <el-dropdown-item command="维生素E">维生素E</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-form-item>
      </el-form>

      <!-- 套餐列表 -->
      <div class="setmeal-grid">
        <el-card 
          v-for="setmeal in setmealList" 
          :key="setmeal.id" 
          class="setmeal-card"
          shadow="hover">
          <div class="setmeal-info">
            <h3>{{ setmeal.name }}</h3>
            <p class="description">{{ setmeal.description }}</p>
            <div class="price">¥{{ setmeal.price }}</div>
            <!-- 套餐包含的菜品 -->
            <div class="dishes">
              <el-tag 
                v-for="dish in setmeal.setmealDishes" 
                :key="dish.dishId" 
                size="small" 
                style="margin-right: 5px; margin-bottom: 5px;">
                {{ dish.dishName }}
              </el-tag>
            </div>
            <!-- 套餐的维生素和适合人群 -->
            <div class="recommendation-info" v-if="setmeal.vitamins || setmeal.suitableCrowds">
              <div v-if="setmeal.vitamins" class="vitamins">
                <el-tag type="warning" size="small">{{ setmeal.vitamins }}</el-tag>
              </div>
              <div v-if="setmeal.suitableCrowds" class="crowds">
                <el-tag 
                  v-for="crowd in setmeal.suitableCrowds" 
                  :key="crowd"
                  type="info" 
                  size="small" 
                  style="margin-right: 5px;">
                  {{ crowd }}
                </el-tag>
              </div>
            </div>
            <el-button 
              type="primary" 
              @click="orderSetmeal(setmeal)"
              :loading="ordering === setmeal.id">
              立即购买
            </el-button>
          </div>
        </el-card>
      </div>
    </el-card>

    <!-- 下单确认对话框 -->
    <el-dialog v-model="orderDialogVisible" title="确认订单" width="500px">
      <div v-if="selectedSetmeal">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="套餐名称">{{ selectedSetmeal.name }}</el-descriptions-item>
          <el-descriptions-item label="套餐描述">{{ selectedSetmeal.description }}</el-descriptions-item>
          <el-descriptions-item label="套餐价格">¥{{ selectedSetmeal.price }}</el-descriptions-item>
          <el-descriptions-item label="包含菜品">
            <el-tag 
              v-for="dish in selectedSetmeal.setmealDishes" 
              :key="dish.dishId" 
              style="margin-right: 5px; margin-bottom: 5px;">
              {{ dish.dishName }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        
        <el-form :model="orderForm" :rules="orderRules" ref="orderFormRef" style="margin-top: 20px;">
          <el-form-item label="数量" prop="quantity">
            <el-input-number v-model="orderForm.quantity" :min="1" :max="10"></el-input-number>
          </el-form-item>
          <el-form-item label="总金额">
            <span class="total-amount">¥{{ totalAmount }}</span>
          </el-form-item>
        </el-form>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="orderDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmOrder" :loading="confirming">确认下单</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
import { ArrowDown } from '@element-plus/icons-vue'

export default {
  name: 'SetmealPurchase',
  components: {
    ArrowDown
  },
  //接收父组件传递的当前用户信息
  props: {
    currentUser: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      searchForm: {
        suitableCrowd: '',//适合人群筛选
        vitamin: ''//维生素筛选
      },
      setmealList: [],//套餐列表
      ordering: null,//当前正在下单的套餐id
      orderDialogVisible: false,//下单确认对话框是否显示
      selectedSetmeal: null,//当前选中的套餐
      confirming: false,//确认下单按钮是否显示
      orderForm: {
        quantity: 1//订单数量
      },
      orderRules: {
        quantity: [
          { required: true, message: '请选择数量', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    //计算总金额
    totalAmount() {
      if (this.selectedSetmeal) {
        return (this.selectedSetmeal.price * this.orderForm.quantity).toFixed(2)
      }
      return '0.00'
    }
  },

  //自动加载套餐数据
  mounted() {
    this.loadSetmeals()
  },
  methods: {
    async loadSetmeals() {
      try {
        // 先尝试获取现有套餐
        const response = await axios.get('/api/setmeals')
        this.setmealList = response.data.data || response.data
        
        // 如果没有套餐，自动生成
        if (this.setmealList.length === 0) {
            const generateResponse = await axios.post('/api/setmeals/generate')
            this.setmealList = generateResponse.data.data || generateResponse.data
        }
      } catch (error) {
          console.error('加载套餐失败:', error)
          this.$message.error('加载套餐失败，请确保后端服务已启动')
      }
    },

    //下单套餐
    orderSetmeal(setmeal) {
      this.selectedSetmeal = setmeal//将当前选中的套餐赋值给selectedSetmeal
      this.orderForm = {
        quantity: 1//将订单数量赋值给orderForm
      }
      this.orderDialogVisible = true//将下单确认对话框显示
    },

    //确认下单
    async confirmOrder() {
      try {
        this.confirming = true
        //创建订单数据
        const orderData = {
          userId: this.currentUser.id,
          amount: parseFloat(this.totalAmount),
          orderDetails: [
            {
              setmealId: this.selectedSetmeal.id,
              dishId: null,
              quantity: this.orderForm.quantity,
              price: parseFloat(this.selectedSetmeal.price)
            }
          ]
        }
        //发送post请求,后端 OrderController.create() 处理->OrderService.create() 处理->OrderMapper.insert() 处理
        const response = await axios.post('/api/orders', orderData)
        //如果订单创建成功，则显示成功消息，并关闭下单确认对话框
        if (response.data.code === 200) {
          this.$message.success('下单成功！请等待食堂工作人员处理')
          this.orderDialogVisible = false
          this.$emit('order-created')//通知父组件订单创建成功
        } else {
          this.$message.error(response.data.message || '下单失败')
        }
      } catch (error) {
        console.error('下单失败:', error)
        if (error.response) {
          this.$message.error('下单失败：' + (error.response.data.message || '服务器错误'))
        } else {
          this.$message.error('下单失败：请确保后端服务已启动在8080端口')
        }
      } finally {
        this.confirming = false
      }
    },

    //选择适合人群
    selectSuitableCrowd(value) {
      this.searchForm.suitableCrowd = value
      this.performSearch()
    },

    //选择维生素
    selectVitamin(value) {
      this.searchForm.vitamin = value
      this.performSearch()
    },

    //清除适合人群
    clearSuitableCrowd() {
      this.searchForm.suitableCrowd = ''
      this.performSearch()
    },

    //清除维生素
    clearVitamin() {
      this.searchForm.vitamin = ''
      this.performSearch()
    },

    //执行搜索
    async performSearch() {
      try {
        const params = {}//构建查询参数
        
        if (this.searchForm.suitableCrowd) {
          params.suitableCrowd = this.searchForm.suitableCrowd
        }
        if (this.searchForm.vitamin) {
          params.vitamin = this.searchForm.vitamin
        }
        
        const response = await axios.get('/api/setmeals', { params })
        this.setmealList = response.data.data || response.data
      } catch (error) {
        console.error('查询套餐失败:', error)
        this.$message.error('查询套餐失败')
      }
    }
  }
}
</script>

<style scoped>
.setmeal-purchase {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  display: flex;
  align-items: center;
  flex-wrap: nowrap;
}

.search-form .el-form-item {
  margin-bottom: 0;
  margin-right: 15px;
}

.search-form .el-form-item:last-child {
  margin-right: 0;
}

.setmeal-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.setmeal-card {
  transition: transform 0.3s;
}

.setmeal-card:hover {
  transform: translateY(-5px);
}

.setmeal-info h3 {
  margin: 0 0 10px 0;
  color: #303133;
}

.description {
  color: #606266;
  margin-bottom: 15px;
  line-height: 1.5;
}

.price {
  font-size: 24px;
  font-weight: bold;
  color: #e6a23c;
  margin-bottom: 15px;
}

.dishes {
  margin-bottom: 15px;
}

.recommendation-info {
  margin-bottom: 15px;
}

.vitamins {
  margin-bottom: 10px;
}

.crowds {
  margin-bottom: 10px;
}

.total-amount {
  font-size: 20px;
  font-weight: bold;
  color: #e6a23c;
}
</style> 
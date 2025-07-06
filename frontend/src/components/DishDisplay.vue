<template>
  <div class="dish-display">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>今日菜单</span>
        </div>
      </template>

      <!-- 分类筛选 -->
      <div class="filter-section">
        <el-radio-group v-model="selectedCategory" @change="filterByCategory">
          <el-radio-button value="">全部</el-radio-button>
          <el-radio-button value="主食">主食</el-radio-button>
          <el-radio-button value="荤菜">荤菜</el-radio-button>
          <el-radio-button value="素菜">素菜</el-radio-button>
          <el-radio-button value="汤类">汤类</el-radio-button>
        </el-radio-group>
      </div>

      <!-- 菜品列表 -->
      <div class="dish-grid">

        <el-card 
          v-for="dish in filteredDishList" 
          :key="dish.id" 
          class="dish-card"
          shadow="hover">
          <div class="dish-info">
            <h3>{{ dish.name }}</h3>
            <p class="description">{{ dish.description }}</p>
            <div class="category">
              <el-tag size="small" :type="getCategoryType(dish.category)">
                {{ dish.category }}
              </el-tag>
            </div>
            <div class="price">¥{{ dish.price }}</div>
            <div class="status">
              <el-tag :type="dish.status === 1 ? 'success' : 'danger'" size="small">
                {{ dish.status === 1 ? '可购买' : '已售罄' }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 空状态 -->
      <div v-if="filteredDishList.length === 0" class="empty-state">
        <el-empty description="暂无菜品数据" />
      </div>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'DishDisplay',
  data() {
    return {
      dishList: [],// 存储从后端获取的所有菜品数据
      selectedCategory: '',// 当前选中的分类
    }
  },
  computed: {
    // 计算属性：根据当前选中的分类过滤菜品列表
    filteredDishList() {
      if (!this.selectedCategory) {
        return this.dishList
      }
      return this.dishList.filter(dish => dish.category === this.selectedCategory)
    }
  },
  mounted() {
    // 在组件挂载时自动加载今日菜单数据
    this.loadDailyMenu()
  },
  methods: {
    //异步回调函数，从后端获取今日菜单数据
    async loadDailyMenu() {
      try {
        const response = await axios.get('/api/daily-menu')
        
        if (response.data.code === 200) {
          this.dishList = response.data.data || []// 将获取到的数据赋值给dishList
          this.$message.success('今日菜单加载成功')
        } else {
          throw new Error(response.data.message || '加载今日菜单失败')
        }
      } catch (error) {
        console.error('加载今日菜单失败:', error)
        this.$message.error('加载今日菜单失败: ' + (error.response?.data?.message || error.message))
      }
    },

    filterByCategory() {
      // 分类筛选逻辑已在computed中处理
    },

    // 根据菜品分类返回不同的颜色类型
    getCategoryType(category) {
      const typeMap = {
        '主食': 'primary',
        '荤菜': 'danger',
        '素菜': 'success',
        '汤类': 'warning',
        '饮品': 'info'
      }
      return typeMap[category] || 'default'
    }
  }
}
</script>

<style scoped>
.dish-display {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-section {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.dish-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.dish-card {
  transition: transform 0.3s;
}

.dish-card:hover {
  transform: translateY(-5px);
}

.dish-info h3 {
  margin: 0 0 10px 0;
  color: #303133;
}

.description {
  color: #606266;
  margin-bottom: 15px;
  line-height: 1.5;
  height: 40px;
  overflow: hidden;
}

.category {
  margin-bottom: 10px;
}

.price {
  font-size: 20px;
  font-weight: bold;
  color: #e6a23c;
  margin-bottom: 10px;
}

.status {
  text-align: right;
}

.empty-state {
  text-align: center;
  padding: 40px;
}
</style> 
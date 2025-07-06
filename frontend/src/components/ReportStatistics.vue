<template>
  <div class="report-statistics">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>报表统计</span>
        </div>
      </template>

      <!-- 统计卡片 -->
      <el-row :gutter="20" class="statistics-cards">
        <el-col :span="8">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-number">{{ statistics.totalOrders }}</div>
              <div class="stat-label">总订单数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-number">¥{{ statistics.totalRevenue }}</div>
              <div class="stat-label">总收入</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-number">{{ statistics.totalUsers }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 详细数据表格 -->
      <el-card class="detail-table">
        <template #header>
          <span>详细统计</span>
        </template>
        
        <el-tabs v-model="activeTab">
          <el-tab-pane label="订单统计" name="orders">
            <el-table :data="orderData" style="width: 100%">
              <el-table-column prop="date" label="日期" width="120"></el-table-column>
              <el-table-column prop="orderCount" label="订单数量" width="120"></el-table-column>
              <el-table-column prop="revenue" label="收入" width="120">
                <template #default="scope">
                  ¥{{ scope.row.revenue }}
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          
          <el-tab-pane label="菜品统计" name="dishes">
            <el-table :data="dishData" style="width: 100%">
              <el-table-column prop="dishName" label="菜品名称" width="150"></el-table-column>
              <el-table-column prop="salesCount" label="销量" width="120"></el-table-column>
              <el-table-column prop="revenue" label="收入" width="120">
                <template #default="scope">
                  ¥{{ scope.row.revenue }}
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ReportStatistics',
  data() {
    return {
      statistics: {
        totalOrders: 0,
        totalRevenue: 0,
        totalUsers: 0
      },
      orderData: [],
      dishData: [],
      activeTab: 'orders'
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        // 加载统计数据
        const statsResponse = await axios.get('/api/reports/stats')
        this.statistics = statsResponse.data.data || statsResponse.data
        
        // 加载订单数据
        const orderResponse = await axios.get('/api/reports/orders')
        this.orderData = orderResponse.data.data || orderResponse.data
        
        // 加载菜品数据
        const dishResponse = await axios.get('/api/reports/dishes')
        this.dishData = dishResponse.data.data || dishResponse.data
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      }
    }
  }
}
</script>

<style scoped>
.report-statistics {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.statistics-cards {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
}

.stat-item {
  padding: 20px;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.detail-table {
  margin-bottom: 20px;
}
</style> 
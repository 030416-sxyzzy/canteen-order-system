<template>
  <div class="order-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
        </div>
      </template>

      <!-- 搜索条件 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单号">
          <el-input
            v-model="searchForm.orderNo"
            placeholder="请输入订单号"
            clearable
            @clear="onSearchClear"
            @input="onSearchInput">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchOrders">搜索</el-button>
        </el-form-item>
      </el-form>

      <!-- 订单列表 -->
      <el-table :data="orderList" style="width: 100%" v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" width="180"></el-table-column>
        <el-table-column prop="userId" label="用户ID" width="100"></el-table-column>
        <el-table-column prop="amount" label="订单金额" width="120">
          <template #default="scope">
            ¥{{ scope.row.amount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="viewOrderDetail(scope.row)">查看详情</el-button>
            <el-button 
              size="small" 
              type="primary" 
              v-if="scope.row.status === 0"
              @click="updateOrderStatus(scope.row.id, 1)">
              确认支付
            </el-button>
            <el-button 
              size="small" 
              type="success" 
              v-if="scope.row.status === 1"
              @click="updateOrderStatus(scope.row.id, 2)">
              完成订单
            </el-button>
            <el-button 
              size="small" 
              type="danger" 
              v-if="scope.row.status === 0"
              @click="updateOrderStatus(scope.row.id, 3)">
              取消订单
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>


    <!-- 订单详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="订单详情" width="60%">
      <div v-if="currentOrder">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="用户ID">{{ currentOrder.userId }}</el-descriptions-item>
          <el-descriptions-item label="订单金额">¥{{ currentOrder.amount }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="getStatusType(currentOrder.status)">
              {{ getStatusText(currentOrder.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentOrder.createTime }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ currentOrder.updateTime }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">订单详情</el-divider>
        
        <el-table :data="orderDetails" style="width: 100%">
          <el-table-column prop="dishName" label="菜品名称" width="150"></el-table-column>
          <el-table-column prop="setmealName" label="套餐名称" width="150"></el-table-column>
          <el-table-column prop="quantity" label="数量" width="100"></el-table-column>
          <el-table-column prop="price" label="单价" width="100">
            <template #default="scope">
              ¥{{ scope.row.price }}
            </template>
          </el-table-column>
          <el-table-column prop="totalPrice" label="小计" width="100">
            <template #default="scope">
              ¥{{ (scope.row.price * scope.row.quantity).toFixed(2) }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'OrderManagement',
  data() {
    return {
      searchForm: {
        orderNo: ''
      },
      orderList: [],
      orderDetails: [],
      currentOrder: null,
      loading: false,
      detailDialogVisible: false
    }
  },
  mounted() {
    this.loadOrders()
  },
  methods: {
    async loadOrders() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm
        }
        
        const response = await axios.get('/api/orders', { params })
        this.orderList = response.data.data || response.data
      } catch (error) {
        console.error('加载订单失败:', error)
        this.$message.error('加载订单失败')
      } finally {
        this.loading = false
      }
    },

    searchOrders() {
      this.loadOrders()
    },

    onSearchClear() {
      this.loadOrders();
    },

    onSearchInput() {
      if (this.searchForm.orderNo === '') {
        this.loadOrders();
      }
    },
    
    async viewOrderDetail(order) {
      this.currentOrder = order
      this.detailDialogVisible = true
      
      try {
        const response = await axios.get(`/api/orders/${order.id}/details`)
        this.orderDetails = response.data.data || response.data
      } catch (error) {
        console.error('加载订单详情失败:', error)
        this.$message.error('加载订单详情失败')
      }
    },

    async updateOrderStatus(orderId, status) {
      try {
        await axios.put(`/api/orders/${orderId}/status`, { status })
        this.$message.success('订单状态更新成功')
        this.loadOrders()
      } catch (error) {
        console.error('更新订单状态失败:', error)
        this.$message.error('更新订单状态失败')
      }
    },

    getStatusType(status) {
      const statusMap = {
        0: 'warning',
        1: 'primary',
        2: 'success',
        3: 'danger'
      }
      return statusMap[status] || 'info'
    },

    getStatusText(status) {
      const statusMap = {
        0: '待支付',
        1: '已支付',
        2: '已完成',
        3: '已取消'
      }
      return statusMap[status] || '未知'
    }
  }
}
</script>

<style scoped>
.order-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}
</style> 
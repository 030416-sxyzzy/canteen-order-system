<template>
  <div class="user-order-history">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>我的订单</span>
        </div>
      </template>

      <!-- 订单列表 -->
      <el-table :data="orderList" style="width: 100%" v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" width="180"></el-table-column>
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
        <el-table-column prop="createTime" label="下单时间" width="180"></el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" @click="viewOrderDetail(scope.row)">查看详情</el-button>
            <el-button 
              size="small" 
              type="primary" 
              v-if="scope.row.status === 0"
              @click="payOrder(scope.row.id)">
              立即支付
            </el-button>
            <!-- 取消订单按钮 -->
            <el-button 
              size="small" 
              type="danger" 
              v-if="scope.row.status === 0"
              @click="cancelOrder(scope.row.id)">
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
          <el-descriptions-item label="下单时间">{{ currentOrder.createTime }}</el-descriptions-item>
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
  name: 'UserOrderHistory',
  props: {
    currentUser: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      orderList: [],//订单列表
      orderDetails: [],//订单详情
      currentOrder: null,//当前订单
      loading: false,//加载状态
      detailDialogVisible: false//订单详情对话框是否显示
    }
  },
  mounted() {
    this.loadOrders()
  },
  methods: {
    //加载订单列表
    async loadOrders() {
      this.loading = true
      try {
        const response = await axios.get(`/api/orders/user/${this.currentUser.id}`)
        
        if (response.data.code === 200) {
          this.orderList = response.data.data || response.data
        } else {
          throw new Error(response.data.message || '加载订单失败')
        }
      } catch (error) {
        console.error('加载订单失败:', error)
        this.$message.error('加载订单失败')
      } finally {
        this.loading = false
      }
    },
    //查看订单详情
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
    //支付订单
    async payOrder(orderId) {
      try {
        await axios.put(`/api/orders/${orderId}/status`, { status: 1 })
        this.$message.success('支付成功！')
        this.loadOrders()
      } catch (error) {
        console.error('支付失败:', error)
        this.$message.error('支付失败，请重试')
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
    },
    //取消订单
    async cancelOrder(orderId) {
      try {
        await axios.put(`/api/orders/${orderId}/status`, { status: 3 })
        this.$message.success('取消订单成功！')
        this.loadOrders()//刷新订单列表
      } catch (error) {
        console.error('取消订单失败:', error)
        this.$message.error('取消订单失败，请重试')
      }
    }
  }
}
</script>

<style scoped>
.user-order-history {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 
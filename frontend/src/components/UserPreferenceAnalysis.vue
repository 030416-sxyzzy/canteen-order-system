<template>
  <div class="user-preference-analysis">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>用户偏好分析</span>
        </div>
      </template>

      <!-- 营养人群分析 -->
      <el-card class="analysis-card">
        <template #header>
          <div class="card-header">
            <span>营养人群分析</span>
          </div>
        </template>
        
        <el-row :gutter="20" style="margin-bottom: 20px;">
          <el-col :span="6">
            <el-button 
              :type="selectedCrowd === '学生' ? 'primary' : 'default'"
              @click="selectCrowd('学生')">
              学生
            </el-button>
          </el-col>
          <el-col :span="6">
            <el-button 
              :type="selectedCrowd === '健身人群' ? 'primary' : 'default'"
              @click="selectCrowd('健身人群')">
              健身人群
            </el-button>
          </el-col>
          <el-col :span="6">
            <el-button 
              :type="selectedCrowd === '素食者' ? 'primary' : 'default'"
              @click="selectCrowd('素食者')">
              素食者
            </el-button>
          </el-col>
          <el-col :span="6">
            <el-button 
              :type="selectedCrowd === '上班族' ? 'primary' : 'default'"
              @click="selectCrowd('上班族')">
              上班族
            </el-button>
          </el-col>
        </el-row>

        <el-table :data="dishPreferenceData" style="width: 100%">
          <el-table-column prop="setmealName" label="套餐名称" width="200"></el-table-column>
          <el-table-column prop="purchaseCount" label="购买次数" width="120"></el-table-column>
          <el-table-column prop="userCount" label="购买用户数" width="120"></el-table-column>
          <el-table-column prop="preferenceLevel" label="喜爱度" width="120">
            <template #default="scope">
              <el-progress 
                :percentage="scope.row.preferenceLevel" 
                :color="getPreferenceColor(scope.row.preferenceLevel)">
              </el-progress>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'UserPreferenceAnalysis',
  data() {
    return {
      dishPreferenceData: [],
      selectedCrowd: '学生'
    }
  },
  mounted() {
    this.loadDishPreference()
  },
  methods: {
    async loadDishPreference() {
      try {
        const response = await axios.get(`/api/preferences/setmeal-purchase-stats`)
        const allSetmealData = response.data.data || response.data
        
        // 根据选择的人群过滤套餐数据
        this.dishPreferenceData = allSetmealData.filter(setmeal => 
          setmeal.suitableCrowd === this.selectedCrowd
        )
      } catch (error) {
        console.error('加载套餐偏好失败:', error)
        this.$message.error('加载套餐偏好失败')
      }
    },

    getPreferenceColor(score) {
      if (score >= 80) return '#67C23A'
      if (score >= 60) return '#E6A23C'
      return '#F56C6C'
    },

    selectCrowd(crowd) {
      this.selectedCrowd = crowd
      this.loadDishPreference() // 重新加载对应人群的套餐偏好
    }
  }
}
</script>

<style scoped>
.user-preference-analysis {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.analysis-card {
  margin-bottom: 20px;
}

.nutrition-item {
  padding: 10px 0;
}

.nutrition-item h4 {
  margin-bottom: 15px;
  color: #303133;
}
</style> 
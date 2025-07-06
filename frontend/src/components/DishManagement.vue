<template>
    <div class="dish-management">
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>菜品管理</span>
            <el-button type="primary" @click="showAddDialog">
              <el-icon><Plus /></el-icon>
              新增菜品
            </el-button>
          </div>
        </template>
  
        <!-- 搜索和筛选 -->
        <div class="filter-section">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input
                v-model="searchForm.name"
                placeholder="搜索菜品名称"
                clearable
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :span="4">
              <el-select v-model="searchForm.category" placeholder="选择分类" clearable>
                <el-option label="主食" value="主食"></el-option>
                <el-option label="荤菜" value="荤菜"></el-option>
                <el-option label="素菜" value="素菜"></el-option>
                <el-option label="汤类" value="汤类"></el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="searchForm.status" placeholder="选择状态" clearable>
                <el-option label="可售" :value="1"></el-option>
                <el-option label="停售" :value="0"></el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="handleSearch">搜索</el-button>
              <el-button @click="resetSearch">重置</el-button>
            </el-col>
          </el-row>
        </div>
  
        <!-- 菜品列表 -->
        <el-table :data="filteredDishList" style="width: 100%" v-loading="loading">
          <el-table-column prop="id" label="ID" width="80"></el-table-column>
          <el-table-column prop="name" label="菜品名称" width="150"></el-table-column>
          <el-table-column prop="description" label="描述" width="200" show-overflow-tooltip></el-table-column>
          <el-table-column prop="category" label="分类" width="100">
            <template #default="scope">
              <el-tag :type="getCategoryType(scope.row.category)" size="small">
                {{ scope.row.category }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="price" label="价格" width="100">
            <template #default="scope">
              <span class="price">¥{{ scope.row.price }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
                {{ scope.row.status === 1 ? '可售' : '停售' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="showEditDialog(scope.row)">编辑</el-button>
              <el-button 
                size="small" 
                type="danger" 
                @click="handleDelete(scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
  
      <!-- 新增/编辑对话框 -->
      <el-dialog 
        v-model="dialogVisible" 
        :title="isEdit ? '编辑菜品' : '新增菜品'"
        width="500px"
      >
        <el-form :model="dishForm" :rules="dishRules" ref="dishFormRef" label-width="80px">
          <el-form-item label="菜品名称" prop="name">
            <el-input v-model="dishForm.name" placeholder="请输入菜品名称"></el-input>
          </el-form-item>
          <el-form-item label="描述" prop="description">
            <el-input 
              v-model="dishForm.description" 
              type="textarea" 
              :rows="3"
              placeholder="请输入菜品描述"
            ></el-input>
          </el-form-item>
          <el-form-item label="分类" prop="category">
            <el-select v-model="dishForm.category" placeholder="请选择分类" style="width: 100%">
              <el-option label="主食" value="主食"></el-option>
              <el-option label="荤菜" value="荤菜"></el-option>
              <el-option label="素菜" value="素菜"></el-option>
              <el-option label="汤类" value="汤类"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="价格" prop="price">
            <el-input-number 
              v-model="dishForm.price" 
              :precision="2" 
              :step="0.5" 
              :min="0"
              style="width: 100%"
            ></el-input-number>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="dishForm.status">
              <el-radio :label="1">可售</el-radio>
              <el-radio :label="0">停售</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
              {{ isEdit ? '更新' : '新增' }}
            </el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script>
  import axios from 'axios'
  import { Plus, Search } from '@element-plus/icons-vue'
  
  export default {
    name: 'DishManagement',
    components: {
      Plus,
      Search
    },
    data() {
      return {
        loading: false,
        submitLoading: false,
        dishList: [],
        filteredList: [], // 存储筛选后的结果
        dialogVisible: false,
        isEdit: false,
        searchForm: {
          name: '',
          category: '',
          status: ''
        },
        dishForm: {
          name: '',
          description: '',
          category: '',
          price: 0,
          status: 1
        },
        dishRules: {
          name: [
            { required: true, message: '请输入菜品名称', trigger: 'blur' }
          ],
          description: [
            { required: true, message: '请输入菜品描述', trigger: 'blur' }
          ],
          category: [
            { required: true, message: '请选择菜品分类', trigger: 'change' }
          ],
          price: [
            { required: true, message: '请输入菜品价格', trigger: 'blur' }
          ]
        }
      }
    },
    computed: {
      filteredDishList() {
        // 如果没有进行搜索，显示所有菜品
        if (this.filteredList.length === 0 && !this.hasSearched) {
          return this.dishList
        }
        return this.filteredList
      }
    },
    mounted() {
      this.loadDishList()
    },
    methods: {
      async loadDishList() {
        try {
          this.loading = true
          const response = await axios.get('/api/dishes')
          
          if (response.data.code === 200) {
            this.dishList = response.data.data || []
            this.filteredList = [] // 清空筛选结果
            this.hasSearched = false // 重置搜索状态
            this.$message.success('菜品列表加载成功')
          } else {
            throw new Error(response.data.message || '加载菜品列表失败')
          }
        } catch (error) {
          console.error('加载菜品列表失败:', error)
          this.$message.error('加载菜品列表失败: ' + (error.response?.data?.message || error.message))
        } finally {
          this.loading = false
        }
      },
  
      showAddDialog() {
        this.isEdit = false
        this.dishForm = {
          name: '',
          description: '',
          category: '',
          price: 0,
          status: 1
        }
        this.dialogVisible = true
      },
  
      showEditDialog(dish) {
        this.isEdit = true
        this.dishForm = { ...dish }
        this.dialogVisible = true
      },
  
      async handleSubmit() {
        try {
          await this.$refs.dishFormRef.validate()
          this.submitLoading = true
  
          if (this.isEdit) {
            // 更新菜品
            const response = await axios.put(`/api/dishes/${this.dishForm.id}`, this.dishForm)
            if (response.data.code === 200) {
              this.$message.success('菜品更新成功')
            } else {
              throw new Error(response.data.message || '更新菜品失败')
            }
          } else {
            // 新增菜品
            const response = await axios.post('/api/dishes', this.dishForm)
            if (response.data.code === 200) {
              this.$message.success('菜品新增成功')
            } else {
              throw new Error(response.data.message || '新增菜品失败')
            }
          }
  
          this.dialogVisible = false
          this.loadDishList()
        } catch (error) {
          console.error('操作失败:', error)
          this.$message.error('操作失败: ' + (error.response?.data?.message || error.message))
        } finally {
          this.submitLoading = false
        }
      },
  
      async handleDelete(dish) {
        try {
          await this.$confirm(`确定要删除菜品"${dish.name}"吗？`, '确认删除', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
  
          const response = await axios.delete(`/api/dishes/${dish.id}`)
          if (response.data.code === 200) {
            this.$message.success('删除成功')
            this.loadDishList()
          } else {
            throw new Error(response.data.message || '删除失败')
          }
        } catch (error) {
          if (error !== 'cancel') {
            console.error('删除失败:', error)
            this.$message.error('删除失败: ' + (error.response?.data?.message || error.message))
          }
        }
      },
  
      handleSearch() {
        // 点击搜索按钮时才进行筛选
        let filtered = this.dishList
  
        if (this.searchForm.name) {
          filtered = filtered.filter(dish => 
            dish.name.toLowerCase().includes(this.searchForm.name.toLowerCase())
          )
        }
  
        if (this.searchForm.category) {
          filtered = filtered.filter(dish => dish.category === this.searchForm.category)
        }
  
        if (this.searchForm.status !== '') {
          filtered = filtered.filter(dish => dish.status === this.searchForm.status)
        }
  
        this.filteredList = filtered
        this.hasSearched = true
        
        if (filtered.length === 0) {
          this.$message.info('没有找到符合条件的菜品')
        } else {
          this.$message.success(`找到 ${filtered.length} 个菜品`)
        }
      },
  
      resetSearch() {
        this.searchForm = {
          name: '',
          category: '',
          status: ''
        }
        this.filteredList = []
        this.hasSearched = false
        this.$message.success('已重置搜索条件')
      },
  
      getCategoryType(category) {
        const typeMap = {
          '主食': 'primary',
          '荤菜': 'danger',
          '素菜': 'success',
          '汤类': 'warning'
        }
        return typeMap[category] || 'default'
      }
    }
  }
  </script>
  
  <style scoped>
  .dish-management {
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
  
  .price {
    font-weight: bold;
    color: #e6a23c;
  }
  
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
  </style>
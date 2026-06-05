<template>
  <div class="user-manage-container">
    <div class="manage-header">
      <h2>用户管理</h2>
      <div class="manage-actions">
        <input 
          type="text" 
          v-model="searchKeyword" 
          placeholder="搜索用户名或姓名"
          @keyup.enter="loadUsers"
        />
        <button class="btn-primary" @click="showAddDialog = true">
          + 添加用户
        </button>
      </div>
    </div>
    
    <div v-if="userStore.isLoading" class="loading-wrapper">
      <div class="spinner"></div>
    </div>
    
    <div v-else class="user-table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>真实姓名</th>
            <th>邮箱</th>
            <th>手机号</th>
            <th>角色</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="u in users" :key="u.id">
            <td>{{ u.id }}</td>
            <td>{{ u.username }}</td>
            <td>{{ u.realName || '-' }}</td>
            <td>{{ u.email || '-' }}</td>
            <td>{{ u.phone || '-' }}</td>
            <td>
              <span :class="['badge', u.role === 'admin' ? 'badge-warning' : 'badge-success']">
                {{ u.role === 'admin' ? '管理员' : '普通用户' }}
              </span>
            </td>
            <td>
              <button 
                class="btn-danger" 
                @click="deleteUser(u.id)"
                :disabled="u.id === userStore.user?.id"
              >
                删除
              </button>
            </td>
          </tr>
          <tr v-if="users.length === 0">
            <td colspan="7" class="empty-row">暂无用户数据</td>
          </tr>
        </tbody>
      </table>
    </div>
    
    <!-- 添加用户对话框 -->
    <div v-if="showAddDialog" class="modal" @click.self="showAddDialog = false">
      <div class="modal-content">
        <h3>添加用户</h3>
        <div class="form-group">
          <label>用户名 *</label>
          <input type="text" v-model="newUser.username" />
        </div>
        <div class="form-group">
          <label>真实姓名</label>
          <input type="text" v-model="newUser.realName" />
        </div>
        <div class="form-group">
          <label>邮箱</label>
          <input type="email" v-model="newUser.email" />
        </div>
        <div class="form-group">
          <label>手机号</label>
          <input type="tel" v-model="newUser.phone" />
        </div>
        <div class="form-group">
          <label>角色</label>
          <select v-model="newUser.role">
            <option value="user">普通用户</option>
            <option value="admin">管理员</option>
          </select>
        </div>
        <div class="modal-actions">
          <button class="btn-primary" @click="addUser">确认</button>
          <button class="btn-secondary" @click="showAddDialog = false">取消</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/userStore'

const userStore = useUserStore()

const users = ref([])
const searchKeyword = ref('')
const showAddDialog = ref(false)
const newUser = ref({
  username: '',
  realName: '',
  email: '',
  phone: '',
  role: 'user'
})

const loadUsers = async () => {
  const result = await userStore.getUserList(searchKeyword.value)
  if (result.success) {
    users.value = result.data
  }
}

const deleteUser = async (id) => {
  if (confirm('确定要删除该用户吗？')) {
    const result = await userStore.deleteUser(id)
    if (result.success) {
      window.showMessage('删除成功', 'success')
      await loadUsers()
    } else {
      window.showMessage(result.message, 'error')
    }
  }
}

const addUser = async () => {
  if (!newUser.value.username) {
    window.showMessage('请填写用户名', 'error')
    return
  }
  
  const result = await userStore.addUser(newUser.value)
  if (result.success) {
    window.showMessage('添加成功', 'success')
    showAddDialog.value = false
    newUser.value = { username: '', realName: '', email: '', phone: '', role: 'user' }
    await loadUsers()
  } else {
    window.showMessage(result.message, 'error')
  }
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.user-manage-container {
  background: white;
  border-radius: 20px;
  padding: 25px;
  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.1);
}

.manage-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 15px;
}

.manage-actions {
  display: flex;
  gap: 10px;
}

.manage-actions input {
  padding: 8px 15px;
  border: 1px solid #ddd;
  border-radius: 25px;
  width: 200px;
  outline: none;
}

.manage-actions input:focus {
  border-color: #667eea;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.data-table th {
  background: #f5f5f5;
  font-weight: 600;
  color: #555;
}

.data-table tr:hover {
  background: #f9f9f9;
}

.empty-row {
  text-align: center;
  color: #999;
  padding: 40px;
}

.loading-wrapper {
  display: flex;
  justify-content: center;
  padding: 40px;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #f3f3f3;
  border-top-color: #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000;
}

.modal-content {
  background: white;
  border-radius: 20px;
  padding: 30px;
  width: 90%;
  max-width: 450px;
}

.modal-content h3 {
  margin-bottom: 20px;
}

.modal-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 20px;
}

@media (max-width: 768px) {
  .manage-header {
    flex-direction: column;
  }
  
  .manage-actions {
    width: 100%;
  }
  
  .manage-actions input {
    flex: 1;
  }
  
  .data-table {
    font-size: 12px;
  }
  
  .data-table th,
  .data-table td {
    padding: 8px;
  }
}
</style>
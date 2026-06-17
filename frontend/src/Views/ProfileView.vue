<template>
  <div class="profile-container">
    <div class="profile-card">
      <h2>个人资料</h2>
      <div v-if="userStore.user" class="profile-info">
        <p><strong>用户名：</strong> {{ userStore.user.username }}</p>
        <p><strong>真实姓名：</strong> {{ userStore.user.realName || '未设置' }}</p>
        <p><strong>邮箱：</strong> {{ userStore.user.email || '未设置' }}</p>
        <p><strong>手机号：</strong> {{ userStore.user.phone || '未设置' }}</p>
        <p><strong>角色：</strong> {{ userStore.user.role === 'admin' ? '管理员' : '普通用户' }}</p>
        <p><strong>发布菜谱数：</strong> {{ userStore.user.recipeCount || userStore.user.recipe_count || 0 }} 篇</p>
        <p><strong>注册时间：</strong> {{ formatDate(userStore.user.createTime) }}</p>
      </div>
    </div>
    
    <div class="profile-card">
      <h2>修改密码</h2>
      <form @submit.prevent="changePassword">
        <div class="form-group">
          <label>原密码</label>
          <input 
            type="password" 
            v-model="passwordForm.oldPassword" 
            placeholder="请输入原密码"
          />
        </div>
        <div class="form-group">
          <label>新密码</label>
          <input 
            type="password" 
            v-model="passwordForm.newPassword" 
            placeholder="至少6个字符"
          />
        </div>
        <div class="form-group">
          <label>确认新密码</label>
          <input 
            type="password" 
            v-model="passwordForm.confirmPassword" 
            placeholder="请再次输入新密码"
          />
        </div>
        <button type="submit" class="btn-primary" :disabled="submitting">
          {{ submitting ? '提交中...' : '修改密码' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useUserStore } from '@/stores/userStore'

const userStore = useUserStore()

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const submitting = ref(false)

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

const changePassword = async () => {
  if (!passwordForm.oldPassword || !passwordForm.newPassword) {
    window.showMessage('请填写完整信息', 'error')
    return
  }
  
  if (passwordForm.newPassword.length < 6) {
    window.showMessage('新密码至少6个字符', 'error')
    return
  }
  
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    window.showMessage('两次输入的密码不一致', 'error')
    return
  }
  
  submitting.value = true
  const result = await userStore.changePassword(passwordForm.oldPassword, passwordForm.newPassword)
  
  if (result.success) {
    window.showMessage('密码修改成功', 'success')
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
  } else {
    window.showMessage(result.message, 'error')
  }
  
  submitting.value = false
}
</script>

<style scoped>
.profile-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 30px;
}

.profile-card {
  background: white;
  border-radius: 20px;
  padding: 25px;
  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.1);
}

.profile-card h2 {
  margin-bottom: 20px;
  color: #333;
  padding-bottom: 10px;
  border-bottom: 2px solid #e5882c;
}

.profile-info p {
  padding: 10px 0;
  color: #555;
  border-bottom: 1px solid #eee;
}

.profile-info strong {
  display: inline-block;
  width: 80px;
  color: #333;
}

.profile-card .btn-primary {
  width: 100%;
  margin-top: 10px;
}

@media (max-width: 768px) {
  .profile-container {
    grid-template-columns: 1fr;
  }
}
</style>
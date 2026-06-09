<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2>登录</h2>
      <form @submit.prevent="submit">
        <div class="form-group">
          <label>用户名</label>
          <input 
            type="text" 
            v-model="form.username" 
            placeholder="请输入用户名"
            autocomplete="off"
          />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input 
            type="password" 
            v-model="form.password" 
            placeholder="请输入密码"
            @keyup.enter="submit"
          />
        </div>
        <button type="submit" class="btn-primary" :disabled="submitting">
          {{ submitting ? '登录中...' : '登录' }}
        </button>
      </form>
      <p class="auth-link">
        还没有账号？<router-link to="/register">立即注册</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const userStore = useUserStore()

const form = reactive({
  username: '',
  password: ''
})

const submitting = ref(false)

const submit = async () => {
  if (!form.username || !form.password) {
    window.showMessage('请填写用户名和密码', 'error')
    return
  }
  
  submitting.value = true
  const result = await userStore.login(form)
  
  if (result.success) {
    window.showMessage('登录成功', 'success')
    router.push('/')
  } else {
    window.showMessage(result.message || '登录失败', 'error')
  }
  
  submitting.value = false
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
}

.auth-card {
  background: white;
  border-radius: 20px;
  padding: 40px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.auth-card h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.auth-card .btn-primary {
  width: 100%;
  margin-top: 10px;
}

.auth-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.auth-link a {
  color: #efefef;
  text-decoration: none;
}

.auth-link a:hover {
  text-decoration: underline;
}
</style>
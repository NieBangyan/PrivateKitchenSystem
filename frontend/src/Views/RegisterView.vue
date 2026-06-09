<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2>注册</h2>
      <form @submit.prevent="submit">
        <div class="form-group">
          <label>用户名 *</label>
          <input 
            type="text" 
            v-model="form.username" 
            :class="{ error: errors.username }"
            placeholder="至少3个字符"
          />
          <span class="error-msg">{{ errors.username }}</span>
        </div>
        
        <div class="form-group">
          <label>密码 *</label>
          <input 
            type="password" 
            v-model="form.password" 
            :class="{ error: errors.password }"
            placeholder="至少6个字符"
          />
          <span class="error-msg">{{ errors.password }}</span>
        </div>
        
        <div class="form-group">
          <label>确认密码 *</label>
          <input 
            type="password" 
            v-model="form.confirmPassword" 
            :class="{ error: errors.confirmPassword }"
            placeholder="请再次输入密码"
          />
          <span class="error-msg">{{ errors.confirmPassword }}</span>
        </div>
        
        <div class="form-group">
          <label>真实姓名</label>
          <input type="text" v-model="form.realName" />
        </div>
        
        <div class="form-group">
          <label>邮箱</label>
          <input type="email" v-model="form.email" :class="{ error: errors.email }" />
          <span class="error-msg">{{ errors.email }}</span>
        </div>
        
        <div class="form-group">
          <label>手机号</label>
          <input type="tel" v-model="form.phone" :class="{ error: errors.phone }" />
          <span class="error-msg">{{ errors.phone }}</span>
        </div>
        
        <button type="submit" class="btn-primary" :disabled="submitting">
          {{ submitting ? '注册中...' : '注册' }}
        </button>
      </form>
      <p class="auth-link">
        已有账号？<router-link to="/login">立即登录</router-link>
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
  password: '',
  confirmPassword: '',
  realName: '',
  email: '',
  phone: ''
})

const errors = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  phone: ''
})

const submitting = ref(false)

const validate = () => {
  let isValid = true
  
  errors.username = ''
  errors.password = ''
  errors.confirmPassword = ''
  errors.email = ''
  errors.phone = ''
  
  if (!form.username || form.username.length < 3) {
    errors.username = '用户名至少3个字符'
    isValid = false
  }
  
  if (!form.password || form.password.length < 6) {
    errors.password = '密码至少6个字符'
    isValid = false
  }
  
  if (form.password !== form.confirmPassword) {
    errors.confirmPassword = '两次输入的密码不一致'
    isValid = false
  }
  
  if (form.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
    errors.email = '邮箱格式不正确'
    isValid = false
  }
  
  if (form.phone && !/^1[3-9]\d{9}$/.test(form.phone)) {
    errors.phone = '手机号格式不正确'
    isValid = false
  }
  
  return isValid
}

const submit = async () => {
  if (!validate()) {
    window.showMessage('请检查表单填写', 'error')
    return
  }
  
  submitting.value = true
  const { confirmPassword, ...registerData } = form
  const result = await userStore.register(registerData)
  
  if (result.success) {
    window.showMessage('注册成功，请登录', 'success')
    router.push('/login')
  } else {
    window.showMessage(result.message || '注册失败', 'error')
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
  max-width: 450px;
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
  color: #e28d2d;
  text-decoration: none;
}

.auth-link a:hover {
  text-decoration: underline;
}
</style>
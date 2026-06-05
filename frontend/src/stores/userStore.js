import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { userApi } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  // 状态
  const user = ref(null)
  const token = ref(localStorage.getItem('token') || '')
  const isLoading = ref(false)
  
  // 计算属性
  const isLoggedIn = computed(() => !!user.value)
  const isAdmin = computed(() => user.value?.role === 'admin')
  const username = computed(() => user.value?.username || '')
  
  // 登录
  async function login(credentials) {
    isLoading.value = true
    try {
      const res = await userApi.login(credentials)
      if (res.code === 200) {
        user.value = res.data
        token.value = res.data.token
        localStorage.setItem('token', res.data.token)
        localStorage.setItem('user', JSON.stringify(res.data))
        return { success: true, data: res.data }
      }
      return { success: false, message: res.message }
    } catch (error) {
      return { success: false, message: error.message }
    } finally {
      isLoading.value = false
    }
  }
  
  // 注册
  async function register(data) {
    isLoading.value = true
    try {
      const res = await userApi.register(data)
      if (res.code === 200) {
        return { success: true, message: res.message }
      }
      return { success: false, message: res.message }
    } catch (error) {
      return { success: false, message: error.message }
    } finally {
      isLoading.value = false
    }
  }
  
  // 登出
  async function logout() {
    try {
      await userApi.logout()
    } finally {
      user.value = null
      token.value = ''
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
  
  // 获取当前用户
  async function fetchCurrentUser() {
    if (!token.value) return
    try {
      const res = await userApi.getCurrent()
      if (res.code === 200) {
        user.value = res.data
      }
    } catch (error) {
      console.error('获取用户信息失败', error)
    }
  }
  
  // 修改密码
  async function changePassword(oldPassword, newPassword) {
    isLoading.value = true
    try {
      const res = await userApi.changePassword({ oldPassword, newPassword })
      if (res.code === 200) {
        return { success: true, message: res.message }
      }
      return { success: false, message: res.message }
    } catch (error) {
      return { success: false, message: error.message }
    } finally {
      isLoading.value = false
    }
  }
  
  // 从localStorage恢复用户
  function restoreUser() {
    const savedUser = localStorage.getItem('user')
    if (savedUser) {
      user.value = JSON.parse(savedUser)
    }
  }
  
  // 初始化
  restoreUser()
  
  return {
    user,
    token,
    isLoading,
    isLoggedIn,
    isAdmin,
    username,
    login,
    register,
    logout,
    fetchCurrentUser,
    changePassword
  }
})
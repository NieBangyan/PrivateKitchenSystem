<template>
  <nav class="navbar">
    <div class="nav-container">
      <div class="nav-brand" @click="router.push('/')">
        🍳 私房菜学习与分享   PRIVATE KITCHEN
      </div>
      
      <div class="nav-menu">
        <router-link to="/">首页</router-link>
        <router-link to="/recipes">菜谱大全</router-link>
        <router-link v-if="userStore.isLoggedIn" to="/recipe/add">发布菜谱</router-link>
        <router-link v-if="userStore.isAdmin" to="/users">用户管理</router-link>
        <router-link v-if="userStore.isLoggedIn" to="/profile">个人中心</router-link>
        <router-link to="/help">帮助</router-link>
        
        <div v-if="!userStore.isLoggedIn" class="auth-buttons">
          <button class="btn-login" @click="router.push('/login')">登录</button>
          <button class="btn-register" @click="router.push('/register')">注册</button>
        </div>
        
        <div v-else class="user-info">
          <span class="username">欢迎, {{ userStore.user?.realName || userStore.user?.username }}</span>
          <button class="btn-logout" @click="handleLogout">退出</button>
        </div>
      </div>
      
      <button class="mobile-menu-btn" @click="mobileMenuOpen = !mobileMenuOpen">
        ☰
      </button>
    </div>
    
    <!-- 移动端菜单 -->
    <div v-show="mobileMenuOpen" class="mobile-menu">
      <router-link to="/" @click="mobileMenuOpen = false">首页</router-link>
      <router-link to="/recipes" @click="mobileMenuOpen = false">菜谱大全</router-link>
      <router-link v-if="userStore.isLoggedIn" to="/recipe/add" @click="mobileMenuOpen = false">发布菜谱</router-link>
      <router-link v-if="userStore.isAdmin" to="/users" @click="mobileMenuOpen = false">用户管理</router-link>
      <router-link v-if="userStore.isLoggedIn" to="/profile" @click="mobileMenuOpen = false">个人中心</router-link>
      <router-link to="/help" @click="mobileMenuOpen = false">帮助</router-link>
    </div>
  </nav>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { showMessage } from '@/utils/format'

const router = useRouter()
const userStore = useUserStore()
const mobileMenuOpen = ref(false)

const handleLogout = async () => {
  await userStore.logout()
  showMessage('已退出登录', 'success')
  router.push('/')
}
</script>

<style scoped>
.navbar {
  background: linear-gradient(135deg, #ecdf99 0%, #feb93b 100%);
  color: white;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
}

.nav-brand {
  font-size: 22px;
  font-weight: bold;
  cursor: pointer;
  transition: opacity 0.3s;
}

.nav-brand:hover {
  opacity: 0.9;
}

.nav-menu {
  display: flex;
  gap: 20px;
  align-items: center;
}

.nav-menu a {
  color: white;
  text-decoration: none;
  padding: 8px 15px;
  border-radius: 5px;
  transition: background 0.3s;
}

.nav-menu a:hover,
.nav-menu a.router-link-active {
  background: rgba(255, 255, 255, 0.2);
}

.auth-buttons {
  display: flex;
  gap: 10px;
}

.btn-login,
.btn-register,
.btn-logout {
  padding: 6px 16px;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.btn-login {
  background: transparent;
  border: 1px solid white;
  color: white;
}

.btn-login:hover {
  background: white;
  color: #d8b14c;
}

.btn-register {
  background: #ee5f22;
  color: white;
}

.btn-register:hover {
  background: #f57c00;
}

.btn-logout {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

.btn-logout:hover {
  background: rgba(255, 255, 255, 0.3);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.username {
  font-size: 14px;
}

.mobile-menu-btn {
  display: none;
  background: none;
  border: none;
  color: white;
  font-size: 24px;
  cursor: pointer;
}

.mobile-menu {
  display: none;
  flex-direction: column;
  background: rgba(242, 206, 29, 0.95);
  padding: 15px;
}

.mobile-menu a {
  color: white;
  text-decoration: none;
  padding: 12px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

@media (max-width: 768px) {
  .nav-menu {
    display: none;
  }
  
  .mobile-menu-btn {
    display: block;
  }
  
  .mobile-menu {
    display: flex;
  }
}
</style>
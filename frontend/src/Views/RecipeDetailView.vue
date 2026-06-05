<template>
  <div class="recipe-detail-container">
    <button class="btn-back" @click="router.back()">
      ← 返回
    </button>
    
    <div v-if="recipeStore.isLoading" class="loading-wrapper">
      <div class="spinner"></div>
    </div>
    
    <div v-else-if="!recipe" class="empty-state">
      菜谱不存在
    </div>
    
    <div v-else class="detail-card">
      <h1>{{ recipe.title }}</h1>
      
      <div class="detail-meta">
        <span :class="['badge', difficultyClass]">
          {{ difficultyText }}
        </span>
        <span>⏱️ 烹饪时间：{{ recipe.cookingTime }}分钟</span>
        <span>👤 作者：{{ recipe.authorName || '匿名' }}</span>
        <span>📅 发布时间：{{ formatDate(recipe.createTime) }}</span>
        <span>👁️ 浏览：{{ recipe.viewCount }}次</span>
      </div>
      
      <div class="detail-section">
        <h2>🥬 所需食材</h2>
        <div class="ingredients">{{ recipe.ingredients }}</div>
      </div>
      
      <div class="detail-section">
        <h2>📝 制作步骤</h2>
        <div class="steps">{{ recipe.steps }}</div>
      </div>
      
      <div v-if="userStore.isLoggedIn && (userStore.user?.id === recipe.authorId || userStore.isAdmin)" class="action-buttons">
        <button class="btn-primary" @click="editRecipe">编辑菜谱</button>
        <button class="btn-danger" @click="confirmDelete">删除菜谱</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { useRecipeStore } from '@/stores/recipeStore'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const recipeStore = useRecipeStore()

const recipe = computed(() => recipeStore.currentRecipe)

const difficultyMap = {
  easy: '初级',
  medium: '中级',
  hard: '高级'
}

const difficultyClassMap = {
  easy: 'badge-success',
  medium: 'badge-warning',
  hard: 'badge-danger'
}

const difficultyText = computed(() => difficultyMap[recipe.value?.difficulty] || recipe.value?.difficulty)
const difficultyClass = computed(() => difficultyClassMap[recipe.value?.difficulty] || 'badge-info')

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

const loadDetail = async () => {
  const id = route.params.id
  await recipeStore.getDetail(id)
}

const editRecipe = () => {
  router.push(`/recipe/edit/${recipe.value.id}`)
}

const confirmDelete = async () => {
  if (confirm('确定要删除这个菜谱吗？')) {
    const result = await recipeStore.deleteRecipe(recipe.value.id)
    if (result.success) {
      window.showMessage('删除成功', 'success')
      router.push('/recipes')
    } else {
      window.showMessage(result.message, 'error')
    }
  }
}

onMounted(() => {
  loadDetail()
})
</script>

<style scoped>
.recipe-detail-container {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.btn-back {
  padding: 10px 20px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 25px;
  cursor: pointer;
  margin-bottom: 20px;
  transition: all 0.3s;
}

.btn-back:hover {
  background: #667eea;
  color: white;
  border-color: #667eea;
}

.loading-wrapper {
  display: flex;
  justify-content: center;
  padding: 60px;
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

.detail-card {
  background: white;
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.1);
}

.detail-card h1 {
  margin-bottom: 20px;
  color: #333;
  font-size: 28px;
}

.detail-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
  margin-bottom: 25px;
  color: #666;
  font-size: 14px;
}

.detail-section {
  margin-bottom: 30px;
}

.detail-section h2 {
  font-size: 20px;
  margin-bottom: 15px;
  color: #555;
}

.ingredients, .steps {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 12px;
  line-height: 1.8;
  color: #555;
  white-space: pre-wrap;
}

.action-buttons {
  display: flex;
  gap: 15px;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

@media (max-width: 768px) {
  .detail-card {
    padding: 20px;
  }
  
  .detail-card h1 {
    font-size: 22px;
  }
}
</style>
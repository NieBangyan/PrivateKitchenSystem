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
      <!-- 图片显示 -->
      <div v-if="recipe.imageUrl" class="detail-image">
        <img :src="recipe.imageUrl" :alt="recipe.title" />
      </div>
      
      <h1>{{ recipe.title }}</h1>
      
      <div class="detail-meta">
        <span :class="['badge', difficultyClass]">
          {{ difficultyText }}
        </span>
        <span>⏳ 烹饪时间：{{ recipe.cookingTime }}分钟</span>
        <span>👤 作者：{{ recipe.authorName || '匿名' }}</span>
        <span>📅 发布时间：{{ formatDate(recipe.createTime) }}</span>
        <span>👀 浏览：{{ recipe.viewCount }}次</span>
        <span>💬 评论：{{ recipe.commentCount || 0 }}</span>
      </div>
      
      <!-- 点赞区域 -->
      <div class="like-section">
        <button @click="likeRecipe" class="like-recipe-btn" :disabled="liking">
          <span v-if="recipe.liked">❤️</span>
          <span v-else>🤍</span>
          {{ recipe.likeCount || 0 }} 点赞
        </button>
      </div>
      
      <div class="detail-section">
        <h2>🥬 所需食材</h2>
        <div class="ingredients">{{ recipe.ingredients }}</div>
      </div>
      
      <div class="detail-section">
        <h2>📝 制作步骤</h2>
        <div class="steps">{{ recipe.steps }}</div>
      </div>
      
      <!-- 评论区域 -->
      <div class="comment-section">
        <h3>评论 ({{ commentTotal }})</h3>
        
        <!-- 发表评论 -->
        <div v-if="userStore.isLoggedIn" class="comment-input">
          <textarea 
            v-model="newComment" 
            placeholder="写下你的评论..."
            rows="3"
          ></textarea>
          <button @click="submitComment" :disabled="submitting">发表评论</button>
        </div>
        <div v-else class="login-tip">
          <router-link to="/login">登录</router-link> 后发表评论
        </div>
        
        <!-- 评论列表 -->
        <div class="comment-list" v-if="comments.length > 0">
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <div class="comment-header">
              <span class="username">{{ comment.userName || '匿名用户' }}</span>
              <span class="time">{{ formatDateTime(comment.createTime) }}</span>
            </div>
            <div class="comment-content">{{ comment.content }}</div>
            <div class="comment-actions">
              <button @click="likeComment(comment.id)" class="like-btn">
                👍 {{ comment.likeCount || 0 }}
              </button>
              <button 
                v-if="userStore.isLoggedIn && (userStore.user?.id === comment.userId || userStore.isAdmin)" 
                @click="deleteComment(comment.id)" 
                class="delete-btn"
              >
                删除
              </button>
            </div>
          </div>
        </div>
        <div v-else class="empty-comments">
          暂无评论，快来抢沙发吧~
        </div>
        
        <!-- 评论分页 -->
        <div class="comment-pagination" v-if="commentTotalPages > 1">
          <button @click="prevPage" :disabled="commentPage <= 1">上一页</button>
          <span>第 {{ commentPage }} / {{ commentTotalPages }} 页</span>
          <button @click="nextPage" :disabled="commentPage >= commentTotalPages">下一页</button>
        </div>
      </div>
      
      <div v-if="userStore.isLoggedIn && (userStore.user?.id === recipe.authorId || userStore.isAdmin)" class="action-buttons">
        <button class="btn-primary" @click="editRecipe">编辑菜谱</button>
        <button class="btn-danger" @click="confirmDelete">删除菜谱</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { useRecipeStore } from '@/stores/recipeStore'
import axios from '@/api'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const recipeStore = useRecipeStore()

const recipe = computed(() => recipeStore.currentRecipe)

// 点赞相关
const liking = ref(false)

// 评论相关
const comments = ref([])
const commentTotal = ref(0)
const commentPage = ref(1)
const commentPageSize = ref(10)
const commentTotalPages = ref(0)
const newComment = ref('')
const submitting = ref(false)

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

const formatDateTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

// 点赞菜谱
const likeRecipe = async () => {
  if (!userStore.isLoggedIn) {
    window.showMessage('请先登录', 'error')
    router.push('/login')
    return
  }
  
  liking.value = true
  try {
    const res = await axios.post(`/recipe/like/${recipe.value.id}`)
    if (res.code === 200) {
      recipe.value.likeCount = (recipe.value.likeCount || 0) + 1
      recipe.value.liked = true
      window.showMessage('点赞成功', 'success')
    } else {
      window.showMessage(res.message, 'error')
    }
  } catch (error) {
    window.showMessage('点赞失败', 'error')
  } finally {
    liking.value = false
  }
}

// 加载评论
const loadComments = async () => {
  if (!recipe.value || !recipe.value.id) {
    console.log('等待菜谱数据加载完成')
    return
  }
  
  try {
    const res = await axios.get(`/comment/list/${recipe.value.id}`, {
      params: { page: commentPage.value, pageSize: commentPageSize.value }
    })
    if (res.code === 200) {
      comments.value = res.data.list || []
      commentTotal.value = res.data.total || 0
      commentTotalPages.value = res.data.totalPages || 0
    }
  } catch (error) {
    console.error('加载评论失败', error)
  }
}

// 发表评论
const submitComment = async () => {
  if (!newComment.value.trim()) {
    window.showMessage('请输入评论内容', 'error')
    return
  }
  if (newComment.value.length > 500) {
    window.showMessage('评论内容不能超过500字', 'error')
    return
  }
  
  submitting.value = true
  try {
    const res = await axios.post('/comment/add', {
      recipeId: recipe.value.id,
      content: newComment.value
    })
    if (res.code === 200) {
      window.showMessage('评论成功', 'success')
      newComment.value = ''
      commentPage.value = 1
      await loadComments()
    } else {
      window.showMessage(res.message, 'error')
    }
  } catch (error) {
    window.showMessage('评论失败', 'error')
  } finally {
    submitting.value = false
  }
}

// 点赞评论
const likeComment = async (commentId) => {
  try {
    const res = await axios.post(`/comment/like/${commentId}`)
    if (res.code === 200) {
      const comment = comments.value.find(c => c.id === commentId)
      if (comment) {
        comment.likeCount = (comment.likeCount || 0) + 1
      }
      window.showMessage('点赞成功', 'success')
    }
  } catch (error) {
    window.showMessage('点赞失败', 'error')
  }
}

// 删除评论
const deleteComment = async (commentId) => {
  if (!confirm('确定要删除这条评论吗？')) return
  
  try {
    const res = await axios.delete(`/comment/delete/${commentId}`)
    if (res.code === 200) {
      window.showMessage('删除成功', 'success')
      await loadComments()
    } else {
      window.showMessage(res.message, 'error')
    }
  } catch (error) {
    window.showMessage('删除失败', 'error')
  }
}

// 分页
const prevPage = () => {
  if (commentPage.value > 1) {
    commentPage.value--
    loadComments()
  }
}

const nextPage = () => {
  if (commentPage.value < commentTotalPages.value) {
    commentPage.value++
    loadComments()
  }
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

// 监听菜谱数据变化，加载评论
watch(recipe, (newRecipe) => {
  if (newRecipe && newRecipe.id) {
    loadComments()
  }
})

onMounted(async () => {
  await loadDetail()
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
  background: #efb238;
  color: white;
  border-color: #e67c1e;
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
  border-top-color: #f6b567;
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

.detail-image {
  width: 100%;
  max-height: 600px;
  overflow: hidden;
  border-radius: 12px;
  margin-bottom: 20px;
}

.detail-image img {
  width: 100%;
  height: auto;
  object-fit: cover;
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

/* 点赞区域 */
.like-section {
  text-align: center;
  margin: 20px 0;
  padding: 15px 0;
  border-top: 1px solid #eee;
  border-bottom: 1px solid #eee;
}

.like-recipe-btn {
  padding: 10px 40px;
  background: #ff6b6b;
  color: white;
  border: none;
  border-radius: 40px;
  font-size: 18px;
  cursor: pointer;
  transition: all 0.3s;
}

.like-recipe-btn:hover:not(:disabled) {
  transform: scale(1.05);
  background: #ff4757;
}

.like-recipe-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 评论区域 */
.comment-section {
  margin-top: 30px;
  padding-top: 20px;
}

.comment-section h3 {
  margin-bottom: 20px;
  color: #333;
}

.comment-input {
  margin-bottom: 30px;
}

.comment-input textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  font-size: 14px;
  resize: vertical;
  font-family: inherit;
  transition: border-color 0.3s;
}

.comment-input textarea:focus {
  outline: none;
  border-color: #f0c243;
}

.comment-input button {
  margin-top: 10px;
  padding: 8px 24px;
  background: linear-gradient(135deg, #ead76b 0%, #f1781c 100%);
  color: white;
  border: none;
  border-radius: 25px;
  cursor: pointer;
  transition: transform 0.2s;
}

.comment-input button:hover:not(:disabled) {
  transform: translateY(-2px);
}

.comment-input button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.login-tip {
  padding: 20px;
  text-align: center;
  background: #f9f9f9;
  border-radius: 12px;
  color: #666;
}

.login-tip a {
  color: #fa881d;
  text-decoration: none;
}

.login-tip a:hover {
  text-decoration: underline;
}

.comment-list {
  max-height: 500px;
  overflow-y: auto;
}

.comment-item {
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.comment-header {
  display: flex;
  gap: 15px;
  margin-bottom: 8px;
}

.username {
  font-weight: 600;
  color: #333;
}

.time {
  font-size: 12px;
  color: #999;
}

.comment-content {
  color: #555;
  line-height: 1.6;
  margin-bottom: 8px;
}

.comment-actions {
  display: flex;
  gap: 15px;
}

.like-btn, .delete-btn {
  background: none;
  border: none;
  color: #999;
  cursor: pointer;
  font-size: 12px;
  transition: color 0.2s;
}

.like-btn:hover {
  color: #667eea;
}

.delete-btn:hover {
  color: #ff4757;
}

.empty-comments {
  text-align: center;
  padding: 40px;
  color: #999;
}

.comment-pagination {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.comment-pagination button {
  padding: 6px 16px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.2s;
}

.comment-pagination button:hover:not(:disabled) {
  background: #e72e15;
  color: white;
  border-color: #080808;
}

.comment-pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.comment-pagination span {
  color: #666;
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
  
  .like-recipe-btn {
    padding: 8px 30px;
    font-size: 16px;
  }
}
</style>
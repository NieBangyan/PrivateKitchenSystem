<template>
  <div class="comment-section">
    <h3>评论 ({{ total }})</h3>
    
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
    <div class="comment-list">
      <div v-for="comment in comments" :key="comment.id" class="comment-item">
        <div class="comment-header">
          <span class="username">{{ comment.userName }}</span>
          <span class="time">{{ formatDate(comment.createTime) }}</span>
        </div>
        <div class="comment-content">{{ comment.content }}</div>
        <div class="comment-actions">
          <button @click="likeComment(comment.id)" class="like-btn">
            👍 {{ comment.likeCount || 0 }}
          </button>
        </div>
      </div>
    </div>
    
    <!-- 分页 -->
    <Pagination
      v-if="totalPages > 1"
      :current="page"
      :total="total"
      :page-size="pageSize"
      @change="changePage"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/userStore'
import Pagination from './Pagination.vue'
import axios from '@/api'

const props = defineProps({
  recipeId: {
    type: Number,
    required: true
  }
})

const userStore = useUserStore()
const comments = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const totalPages = ref(0)
const newComment = ref('')
const submitting = ref(false)

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

const loadComments = async () => {
  try {
    const res = await axios.get(`/comment/list/${props.recipeId}`, {
      params: { page: page.value, pageSize: pageSize.value }
    })
    if (res.code === 200) {
      comments.value = res.data.list
      total.value = res.data.total
      totalPages.value = res.data.totalPages
    }
  } catch (error) {
    console.error('加载评论失败', error)
  }
}

const submitComment = async () => {
  if (!newComment.value.trim()) {
    window.showMessage('请输入评论内容', 'error')
    return
  }
  
  submitting.value = true
  try {
    const res = await axios.post('/comment/add', {
      recipeId: props.recipeId,
      content: newComment.value
    })
    if (res.code === 200) {
      window.showMessage('评论成功', 'success')
      newComment.value = ''
      page.value = 1
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

const likeComment = async (commentId) => {
  try {
    const res = await axios.post(`/comment/like/${commentId}`)
    if (res.code === 200) {
      // 更新本地点赞数
      const comment = comments.value.find(c => c.id === commentId)
      if (comment) {
        comment.likeCount = (comment.likeCount || 0) + 1
      }
    }
  } catch (error) {
    console.error('点赞失败', error)
  }
}

const changePage = async (newPage) => {
  page.value = newPage
  await loadComments()
}

onMounted(() => {
  loadComments()
})
</script>

<style scoped>
.comment-section {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #eee;
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
  border-radius: 8px;
  font-size: 14px;
  resize: vertical;
  font-family: inherit;
}

.comment-input textarea:focus {
  outline: none;
  border-color: #667eea;
}

.comment-input button {
  margin-top: 10px;
  padding: 8px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
}

.login-tip {
  padding: 20px;
  text-align: center;
  background: #f9f9f9;
  border-radius: 8px;
  color: #666;
}

.login-tip a {
  color: #667eea;
  text-decoration: none;
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

.like-btn {
  background: none;
  border: none;
  color: #999;
  cursor: pointer;
  font-size: 12px;
}

.like-btn:hover {
  color: #667eea;
}
</style>
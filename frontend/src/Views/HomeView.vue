<template>
  <div class="home-container">
    <div class="hero-section">
      <h1>发现美味，分享快乐</h1>
      <p>每一个热爱生活的人，都值得拥有一道拿手好菜</p>
      <p style="font-size:14px;opacity:0.7;">Every person who loves life deserves to have a signature dish</p>
     
      <div class="hero-buttons">
        <router-link to="/recipes" class="btn-primary">浏览菜谱</router-link>
        <router-link v-if="userStore.isLoggedIn" to="/recipe/add" class="btn-secondary">
          分享菜谱
        </router-link>
      </div>
    </div>
    
    <div class="stats-section">
      <div class="stat-card">
        <div class="stat-number">{{ stats.recipeCount }}</div>
        <div class="stat-label">精选菜谱</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.categoryCount }}</div>
        <div class="stat-label">美食分类</div>
      </div>
    </div>
  
    <div class="latest-section">
      <h2>最新菜谱</h2>
      <div v-if="latestRecipes.length === 0" class="empty-state">
        暂无菜谱数据
      </div>
      <div v-else class="recipes-grid">
        <RecipeCard
          v-for="recipe in latestRecipes"
          :key="recipe.id"
          :recipe="recipe"
          @click="viewDetail"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { useRecipeStore } from '@/stores/recipeStore'
import RecipeCard from '@/components/RecipeCard.vue'
import axios from '@/api'

const router = useRouter()
const userStore = useUserStore()
const recipeStore = useRecipeStore()

const latestRecipes = ref([])
const stats = ref({
  recipeCount: 0,
  categoryCount: 0
})

const viewDetail = (id) => {
  router.push(`/recipe/${id}`)
}

const loadLatestRecipes = async () => {
  const result = await recipeStore.search({ page: 1, pageSize: 6 })
  if (result.success) {
    latestRecipes.value = recipeStore.recipes
  }
}

const loadStats = async () => {
  try {
    const res = await axios.get('/recipe/stats')
    if (res.code === 200) {
      stats.value = res.data
    }
  } catch (error) {
    console.error('加载统计数据失败', error)
  }
}

onMounted(() => {
  loadLatestRecipes()
  loadStats()
})
</script>

<style scoped>
.home-container {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.hero-section {
  background: url('/images/bg3.png');
  color: rgb(18, 18, 18);
  padding: 60px 40px;
  border-radius: 24px;
  text-align: center;
  margin-bottom: 40px;
  position: relative;
}

.hero-section h1 {
  font-size: 42px;
  margin-bottom: 16px;
  color: rgba(248, 243, 243);
}

.hero-section p {
  font-size: 18px;
  margin-bottom: 30px;
  opacity: 0.9;
  color: rgba(55, 52, 52, 0.55);
}

.hero-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
}

.hero-buttons .btn-primary,
.hero-buttons .btn-secondary {
  background: linear-gradient(135deg, #ffe385 0%, #fef1cf 100%);
  color: #666;
  border: none;
  text-decoration: none;
}

.stats-section {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 40px;  
  margin-bottom: 50px;
  max-width: 1200px; 
  margin-left: auto;
  margin-right: auto;
  padding: 0 20px;  
}

.stat-card {
  background: rgba(243, 242, 241, 0.8);
  padding: 35px 30px; 
  border-radius: 20px;
  text-align: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.stat-number {
  font-size: 40px;  
  font-weight: bold;
  color: #dd7717;
  margin-bottom: 10px;
}

.stat-label {
  color: #666;
  font-size: 16px; 
}

.latest-section {
  margin-top: 20px;
}

.latest-section h2 {
  font-size: 28px;
  margin-bottom: 24px;
  color: #333;
}

.recipes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.empty-state {
  text-align: center;
  padding: 60px;
  color: #999;
  font-size: 16px;
  background: white;
  border-radius: 16px;
}

@media (max-width: 768px) {
  .hero-section h1 {
    font-size: 28px;
  }
  
  .hero-section p {
    font-size: 14px;
  }
  
  .stats-section {
    gap: 15px;
  }
  
  .stat-number {
    font-size: 24px;
  }
  
  .recipes-grid {
    grid-template-columns: 1fr;
  }
}
</style>
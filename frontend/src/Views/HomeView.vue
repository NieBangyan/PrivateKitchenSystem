<template>
  <div class="home-container">
    <div class="hero-section">
      <h1>发现美味，分享快乐</h1>
      <p>每一个热爱生活的人，都值得拥有一道拿手好菜</p>
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
        <div class="stat-number">{{ stats.userCount }}</div>
        <div class="stat-label">注册用户</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.categoryCount }}</div>
        <div class="stat-label">美食分类</div>
      </div>
    </div>
    
    <div class="features-section">
      <h2>系统特色</h2>
      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon">📚</div>
          <h3>海量菜谱</h3>
          <p>收录全国各地特色菜谱，满足您的烹饪需求</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">🔍</div>
          <h3>智能搜索</h3>
          <p>支持多条件组合查询，快速找到心仪菜谱</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">✏️</div>
          <h3>分享交流</h3>
          <p>发布您的独家菜谱，与美食爱好者交流心得</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">📊</div>
          <h3>数据报表</h3>
          <p>支持数据导出为Excel、PDF等格式</p>
        </div>
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

const router = useRouter()
const userStore = useUserStore()
const recipeStore = useRecipeStore()

const latestRecipes = ref([])
const stats = ref({
  recipeCount: 0,
  userCount: 0,
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

onMounted(() => {
  loadLatestRecipes()
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
 background: url('/images/background1.png');
  color: rgb(18, 18, 18);
  padding: 60px 40px;
  border-radius: 24px;
  text-align: center;
  margin-bottom: 40px;
  position: relative;
  color: white;
  padding: 60px 40px;
  border-radius: 24px;
  text-align: center;
  margin-bottom: 40px;
}

.hero-section h1 {
  font-size: 42px;
  margin-bottom: 16px;
}

.hero-section p {
  font-size: 18px;
  margin-bottom: 30px;
  opacity: 0.9;
}

.hero-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
}

.hero-buttons .btn-primary,
.hero-buttons .btn-secondary {
  text-decoration: none;
}

.stats-section {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin-bottom: 50px;
}

.stat-card {
  background: rgb(255, 255, 255);
  padding: 30px;
  border-radius: 20px;
  text-align: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.stat-number {
  font-size: 36px;
  font-weight: bold;
  color: #516ca2;
  margin-bottom: 8px;
}

.stat-label {
  color: #666;
  font-size: 14px;
}

.features-section {
  text-align: center;
  margin-bottom: 50px;
}

.features-section h2 {
  font-size: 32px;
  margin-bottom: 40px;
  color: #333;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
}

.feature-card {
  background: rgb(233, 139, 139);
  padding: 30px;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 48px;
  margin-bottom: 15px;
}

.feature-card h3 {
  margin-bottom: 10px;
  color: #333;
}

.feature-card p {
  color: #666;
  line-height: 1.6;
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
  
  .features-grid {
    grid-template-columns: 1fr;
  }
  
  .recipes-grid {
    grid-template-columns: 1fr;
  }
}
</style>
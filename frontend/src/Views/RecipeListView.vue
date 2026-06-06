<template>
  <div class="recipe-list-container">
    <SearchBar
      v-model="searchForm"
      :categories="recipeStore.categories"
      @search="handleSearch"
      @reset="handleReset"
    />
    
    <div v-if="recipeStore.isLoading" class="loading-wrapper">
      <div class="spinner"></div>
    </div>
    
    <div v-else-if="recipeStore.recipes.length === 0" class="empty-state">
      🍽️ 暂无菜谱数据，试试其他关键词吧~
    </div>
    
    <div v-else class="recipes-grid">
      <RecipeCard
        v-for="recipe in recipeStore.recipes"
        :key="recipe.id"
        :recipe="recipe"
        @click="viewDetail"
      />
    </div>
    
    <Pagination
      :current="pagination.page"
      :total="recipeStore.total"
      :page-size="pagination.pageSize"
      @change="changePage"
    />
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useRecipeStore } from '@/stores/recipeStore'
import SearchBar from '@/components/SearchBar.vue'
import RecipeCard from '@/components/RecipeCard.vue'
import Pagination from '@/components/Pagination.vue'

const router = useRouter()
const recipeStore = useRecipeStore()

const searchForm = reactive({
  title: '',
  categoryId: null,
  difficulty: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10
})

const handleSearch = async () => {
  console.log('=== 搜索参数 ===', {
    title: searchForm.title,
    categoryId: searchForm.categoryId,
    difficulty: searchForm.difficulty
  })
  pagination.page = 1
  await loadRecipes()
}

const handleReset = async () => {
  searchForm.title = ''
  searchForm.categoryId = null
  searchForm.difficulty = ''
  pagination.page = 1
  await loadRecipes()
}

const loadRecipes = async () => {
  await recipeStore.search({
    ...searchForm,
    page: pagination.page,
    pageSize: pagination.pageSize
  })
  
  // 前端手动过滤
  let filteredRecipes = [...recipeStore.recipes]
  
  if (searchForm.title) {
    filteredRecipes = filteredRecipes.filter(r => 
      r.title.includes(searchForm.title)
    )
  }
  
  if (searchForm.categoryId) {
    filteredRecipes = filteredRecipes.filter(r => 
      r.categoryId === searchForm.categoryId
    )
  }
  
  if (searchForm.difficulty) {
    filteredRecipes = filteredRecipes.filter(r => 
      r.difficulty === searchForm.difficulty
    )
  }
  
  recipeStore.recipes = filteredRecipes
  recipeStore.total = filteredRecipes.length
  
  console.log('过滤后数量:', filteredRecipes.length)
}

const changePage = async (page) => {
  pagination.page = page
  await loadRecipes()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const viewDetail = (id) => {
  router.push(`/recipe/${id}`)
}

onMounted(async () => {
  await recipeStore.loadCategories()
  await loadRecipes()
})
</script>

<style scoped>
.recipe-list-container {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
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

.recipes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 24px;
  margin-bottom: 30px;
}

@media (max-width: 768px) {
  .recipes-grid {
    grid-template-columns: 1fr;
  }
}
</style>
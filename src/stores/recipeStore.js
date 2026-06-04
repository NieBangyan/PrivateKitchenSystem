import { defineStore } from 'pinia'
import { ref } from 'vue'
import { recipeApi } from '@/api/recipe'

export const useRecipeStore = defineStore('recipe', () => {
  const recipes = ref([])
  const currentRecipe = ref(null)
  const categories = ref([])
  const total = ref(0)
  const isLoading = ref(false)
  
  // 搜索菜谱
  async function search(params) {
    isLoading.value = true
    try {
      const res = await recipeApi.search(params)
      if (res.code === 200) {
        recipes.value = res.data.list
        total.value = res.data.total
        return { success: true, data: res.data }
      }
      return { success: false, message: res.message }
    } catch (error) {
      return { success: false, message: error.message }
    } finally {
      isLoading.value = false
    }
  }
  
  // 获取详情
  async function getDetail(id) {
    isLoading.value = true
    try {
      const res = await recipeApi.getDetail(id)
      if (res.code === 200) {
        currentRecipe.value = res.data
        return { success: true, data: res.data }
      }
      return { success: false, message: res.message }
    } catch (error) {
      return { success: false, message: error.message }
    } finally {
      isLoading.value = false
    }
  }
  
  // 添加菜谱
  async function addRecipe(data) {
    isLoading.value = true
    try {
      const res = await recipeApi.add(data)
      if (res.code === 200) {
        return { success: true, data: res.data, message: res.message }
      }
      return { success: false, message: res.message }
    } catch (error) {
      return { success: false, message: error.message }
    } finally {
      isLoading.value = false
    }
  }
  
  // 更新菜谱
  async function updateRecipe(data) {
    isLoading.value = true
    try {
      const res = await recipeApi.update(data)
      if (res.code === 200) {
        return { success: true, data: res.data, message: res.message }
      }
      return { success: false, message: res.message }
    } catch (error) {
      return { success: false, message: error.message }
    } finally {
      isLoading.value = false
    }
  }
  
  // 删除菜谱
  async function deleteRecipe(id) {
    isLoading.value = true
    try {
      const res = await recipeApi.delete(id)
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
  
  // 获取分类
  async function loadCategories() {
    try {
      const res = await recipeApi.getCategories()
      if (res.code === 200) {
        categories.value = res.data
      }
    } catch (error) {
      console.error('加载分类失败', error)
    }
  }
  
  return {
    recipes,
    currentRecipe,
    categories,
    total,
    isLoading,
    search,
    getDetail,
    addRecipe,
    updateRecipe,
    deleteRecipe,
    loadCategories
  }
})
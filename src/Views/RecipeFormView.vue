<template>
  <div class="form-container">
    <h2>{{ isEdit ? '编辑菜谱' : '发布新菜谱' }}</h2>
    
    <form @submit.prevent="submit">
      <div class="form-group">
        <label>菜谱名称 *</label>
        <input 
          type="text" 
          v-model="form.title" 
          :class="{ error: errors.title }"
          placeholder="请输入菜谱名称"
        />
        <span class="error-msg">{{ errors.title }}</span>
      </div>
      
      <div class="form-group">
        <label>菜系分类 *</label>
        <select v-model="form.categoryId" :class="{ error: errors.categoryId }">
          <option :value="null">请选择分类</option>
          <option v-for="c in recipeStore.categories" :key="c.id" :value="c.id">
            {{ c.name }}
          </option>
        </select>
        <span class="error-msg">{{ errors.categoryId }}</span>
      </div>
      
      <div class="form-row">
        <div class="form-group">
          <label>烹饪时间（分钟）*</label>
          <input 
            type="number" 
            v-model="form.cookingTime" 
            min="1" 
            max="600"
            :class="{ error: errors.cookingTime }"
          />
          <span class="error-msg">{{ errors.cookingTime }}</span>
        </div>
        
        <div class="form-group">
          <label>难度等级</label>
          <select v-model="form.difficulty">
            <option value="easy">初级</option>
            <option value="medium">中级</option>
            <option value="hard">高级</option>
          </select>
        </div>
      </div>
      
      <div class="form-group">
        <label>食材清单 *</label>
        <textarea 
          v-model="form.ingredients" 
          rows="5" 
          :class="{ error: errors.ingredients }"
          placeholder="请列出所需食材，每行一个&#10;例如：&#10;豆腐 500g&#10;牛肉末 100g&#10;豆瓣酱 2勺"
        ></textarea>
        <span class="error-msg">{{ errors.ingredients }}</span>
      </div>
      
      <div class="form-group">
        <label>制作步骤 *</label>
        <textarea 
          v-model="form.steps" 
          rows="8" 
          :class="{ error: errors.steps }"
          placeholder="请详细描述制作步骤，每步换行&#10;例如：&#10;1. 豆腐切块焯水&#10;2. 炒香牛肉末&#10;3. 加入豆瓣酱炒出红油"
        ></textarea>
        <span class="error-msg">{{ errors.steps }}</span>
      </div>
      
      <div class="form-actions">
        <button type="submit" class="btn-primary" :disabled="submitting">
          {{ submitting ? '提交中...' : (isEdit ? '保存修改' : '发布菜谱') }}
        </button>
        <button type="button" class="btn-secondary" @click="router.back()">
          取消
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { useRecipeStore } from '@/stores/recipeStore'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const recipeStore = useRecipeStore()

const isEdit = ref(false)
const submitting = ref(false)
const recipeId = ref(null)

const form = reactive({
  title: '',
  categoryId: null,
  ingredients: '',
  steps: '',
  cookingTime: 30,
  difficulty: 'medium',
  imageUrl: ''
})

const errors = reactive({
  title: '',
  categoryId: '',
  ingredients: '',
  steps: '',
  cookingTime: ''
})

const validate = () => {
  let isValid = true
  
  errors.title = ''
  errors.categoryId = ''
  errors.ingredients = ''
  errors.steps = ''
  errors.cookingTime = ''
  
  if (!form.title.trim()) {
    errors.title = '请输入菜谱名称'
    isValid = false
  } else if (form.title.length > 100) {
    errors.title = '菜谱名称不能超过100个字符'
    isValid = false
  }
  
  if (!form.categoryId) {
    errors.categoryId = '请选择菜系分类'
    isValid = false
  }
  
  if (!form.ingredients.trim()) {
    errors.ingredients = '请输入食材清单'
    isValid = false
  }
  
  if (!form.steps.trim()) {
    errors.steps = '请输入制作步骤'
    isValid = false
  }
  
  if (form.cookingTime <= 0) {
    errors.cookingTime = '烹饪时间必须大于0'
    isValid = false
  } else if (form.cookingTime > 600) {
    errors.cookingTime = '烹饪时间不能超过600分钟'
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
  
  let result
  if (isEdit.value) {
    result = await recipeStore.updateRecipe({ ...form, id: recipeId.value })
  } else {
    result = await recipeStore.addRecipe(form)
  }
  
  if (result.success) {
    window.showMessage(isEdit.value ? '修改成功' : '发布成功', 'success')
    router.push('/recipes')
  } else {
    window.showMessage(result.message, 'error')
  }
  
  submitting.value = false
}

const loadRecipeForEdit = async () => {
  const id = route.params.id
  recipeId.value = id
  const result = await recipeStore.getDetail(id)
  if (result.success && result.data) {
    const recipe = result.data
    form.title = recipe.title
    form.categoryId = recipe.categoryId
    form.ingredients = recipe.ingredients
    form.steps = recipe.steps
    form.cookingTime = recipe.cookingTime
    form.difficulty = recipe.difficulty
    form.imageUrl = recipe.imageUrl || ''
  }
}

onMounted(async () => {
  await recipeStore.loadCategories()
  
  if (route.name === 'EditRecipe') {
    isEdit.value = true
    await loadRecipeForEdit()
  }
})
</script>

<style scoped>
.form-container {
  max-width: 700px;
  margin: 0 auto;
  background: white;
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.1);
}

.form-container h2 {
  margin-bottom: 25px;
  text-align: center;
  color: #333;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.form-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
  margin-top: 30px;
}

textarea {
  resize: vertical;
  font-family: inherit;
}

@media (max-width: 768px) {
  .form-container {
    padding: 20px;
  }
  
  .form-row {
    grid-template-columns: 1fr;
    gap: 0;
  }
}
</style>
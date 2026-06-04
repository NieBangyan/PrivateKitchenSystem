<template>
  <div class="search-bar">
    <div class="search-row">
      <div class="search-input-wrapper">
        <span class="search-icon">🔍</span>
        <input
          type="text"
          v-model="localTitle"
          placeholder="输入菜谱名称..."
          @keyup.enter="handleSearch"
        />
        <button v-if="localTitle" class="clear-btn" @click="clearTitle">✕</button>
      </div>
      
      <select v-model="localCategoryId" class="filter-select">
        <option :value="null">全部分类</option>
        <option v-for="c in categories" :key="c.id" :value="c.id">
          {{ c.name }}
        </option>
      </select>
      
      <select v-model="localDifficulty" class="filter-select">
        <option value="">全部难度</option>
        <option value="easy">初级</option>
        <option value="medium">中级</option>
        <option value="hard">高级</option>
      </select>
      
      <button class="btn-primary" @click="handleSearch">搜索</button>
      <button class="btn-secondary" @click="handleReset">重置</button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  modelValue: {
    type: Object,
    default: () => ({ title: '', categoryId: null, difficulty: '' })
  },
  categories: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['update:modelValue', 'search', 'reset'])

const localTitle = ref(props.modelValue.title || '')
const localCategoryId = ref(props.modelValue.categoryId || null)
const localDifficulty = ref(props.modelValue.difficulty || '')

// 监听外部变化
watch(() => props.modelValue, (newVal) => {
  localTitle.value = newVal.title || ''
  localCategoryId.value = newVal.categoryId || null
  localDifficulty.value = newVal.difficulty || ''
}, { deep: true })

const handleSearch = () => {
  const searchParams = {
    title: localTitle.value,
    categoryId: localCategoryId.value,
    difficulty: localDifficulty.value
  }
  emit('update:modelValue', searchParams)
  emit('search', searchParams)
}

const handleReset = () => {
  localTitle.value = ''
  localCategoryId.value = null
  localDifficulty.value = ''
  const emptyParams = { title: '', categoryId: null, difficulty: '' }
  emit('update:modelValue', emptyParams)
  emit('reset')
}

const clearTitle = () => {
  localTitle.value = ''
  handleSearch()
}
</script>

<style scoped>
.search-bar {
  background: white;
  padding: 20px;
  border-radius: 16px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.search-row {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  align-items: center;
}

.search-input-wrapper {
  flex: 2;
  min-width: 200px;
  position: relative;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 12px;
  font-size: 16px;
}

.search-input-wrapper input {
  width: 100%;
  padding: 10px 30px 10px 36px;
  border: 1px solid #e0e0e0;
  border-radius: 25px;
  font-size: 14px;
  outline: none;
  transition: all 0.3s;
}

.search-input-wrapper input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.clear-btn {
  position: absolute;
  right: 10px;
  background: none;
  border: none;
  color: #999;
  cursor: pointer;
  font-size: 16px;
  padding: 0 5px;
}

.clear-btn:hover {
  color: #666;
}

.filter-select {
  padding: 10px 15px;
  border: 1px solid #e0e0e0;
  border-radius: 25px;
  font-size: 14px;
  background: white;
  cursor: pointer;
  outline: none;
}

.filter-select:focus {
  border-color: #667eea;
}

@media (max-width: 768px) {
  .search-row {
    flex-direction: column;
  }
  
  .search-input-wrapper {
    width: 100%;
  }
  
  .filter-select {
    width: 100%;
  }
  
  .btn-primary, .btn-secondary {
    width: 48%;
  }
}
</style>
<template>
  <div class="recipe-card card" @click="$emit('click', recipe.id)">
    <div class="recipe-image">
      <img 
        :src="recipe.imageUrl" 
        :alt="recipe.title"
        class="recipe-img"
      />
    </div>
    <div class="recipe-info">
      <h3>{{ recipe.title }}</h3>
      <div class="recipe-meta">
        <span :class="['badge', difficultyClass]">
          {{ difficultyText }}
        </span>
        <span>⏳ {{ recipe.cookingTime }}分钟</span>
        <span>👀 {{ recipe.viewCount }}次浏览</span>
        <span>❤️ {{ recipe.likeCount }}点赞</span>
        <span>💬 {{ recipe.commentCount || 0 }}</span> 
      </div>
      <p class="author">{{ recipe.authorName || '匿名' }}</p>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  recipe: {
    type: Object,
    required: true
  }
})

defineEmits(['click'])

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

const difficultyText = computed(() => difficultyMap[props.recipe.difficulty] || props.recipe.difficulty)
const difficultyClass = computed(() => difficultyClassMap[props.recipe.difficulty] || 'badge-info')
</script>

<style scoped>
.recipe-card {
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  background:rgba(204, 130, 20,0.35);
}

.recipe-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(222, 132, 14, 0.15);
}

.recipe-image {
  height: 180px;
  overflow: hidden;
}

.recipe-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.recipe-info {
  padding: 15px;
}

.recipe-info h3 {
  margin-bottom: 10px;
  font-size: 18px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.recipe-meta {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 10px;
  font-size: 12px;
  color: #666;
}

.author {
  font-size: 12px;
  color: #999;
}
</style>
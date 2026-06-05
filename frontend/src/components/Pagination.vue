<template>
  <div v-if="totalPages > 1" class="pagination">
    <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">
      上一页
    </button>
    <span class="page-info">
      第 {{ currentPage }} / {{ totalPages }} 页
    </span>
    <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">
      下一页
    </button>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  current: {
    type: Number,
    default: 1
  },
  total: {
    type: Number,
    default: 0
  },
  pageSize: {
    type: Number,
    default: 10
  }
})

const emit = defineEmits(['update:current', 'change'])

const currentPage = computed({
  get: () => props.current,
  set: (val) => {
    emit('update:current', val)
    emit('change', val)
  }
})

const totalPages = computed(() => Math.ceil(props.total / props.pageSize))

const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
  }
}
</script>
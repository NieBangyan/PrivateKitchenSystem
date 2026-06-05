<template>
  <Transition name="toast">
    <div v-if="message" :class="['message-toast', message.type]" class="toast">
      {{ message.text }}
    </div>
  </Transition>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const message = ref(null)
let timeoutId = null

const showMessage = (text, type = 'info', duration = 3000) => {
  if (timeoutId) clearTimeout(timeoutId)
  message.value = { text, type }
  timeoutId = setTimeout(() => {
    message.value = null
  }, duration)
}

// 全局方法
window.showMessage = showMessage

onUnmounted(() => {
  if (timeoutId) clearTimeout(timeoutId)
})
</script>

<style scoped>
.toast {
  position: fixed;
  top: 80px;
  right: 20px;
  padding: 12px 20px;
  border-radius: 8px;
  z-index: 10000;
  font-size: 14px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.toast.success {
  background: #4caf50;
  color: white;
}

.toast.error {
  background: #f44336;
  color: white;
}

.toast.info {
  background: #2196f3;
  color: white;
}

.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s ease;
}

.toast-enter-from {
  transform: translateX(100%);
  opacity: 0;
}

.toast-leave-to {
  transform: translateX(100%);
  opacity: 0;
}
</style>
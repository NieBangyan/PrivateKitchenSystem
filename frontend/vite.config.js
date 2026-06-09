import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    },
    // ========== 新增：减少磁盘写入 ==========
    watch: {
      ignored: [
        '**/node_modules/**',
        '**/.git/**',
        '**/dist/**',
        '**/.vite/**'
      ]
    },
    // 关掉轮询（你不是在用网络驱动器）
    usePolling: false
  },

  // ========== 新增：优化依赖预构建 ==========
  optimizeDeps: {
    include: [
      'vue'
      // 如果你项目中还有其他常用大依赖，可以加进去，比如：
      // 'axios', 'element-plus', 'vue-router'
    ]
  },

  // ========== 新增：关掉开发环境 sourcemap ==========
  build: {
    sourcemap: false
  },

  // 关掉 CSS 的 sourcemap（如果你有 CSS 预处理器）
  css: {
    devSourcemap: false
  }
})
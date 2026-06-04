/**
 * 日期格式化
 * @param {string|Date} date - 日期对象或字符串
 * @param {string} format - 格式化模板，默认 'YYYY-MM-DD'
 * @returns {string} 格式化后的日期字符串
 */
export const formatDate = (date, format = 'YYYY-MM-DD') => {
  if (!date) return ''
  
  const d = new Date(date)
  if (isNaN(d.getTime())) return ''
  
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hour = String(d.getHours()).padStart(2, '0')
  const minute = String(d.getMinutes()).padStart(2, '0')
  const second = String(d.getSeconds()).padStart(2, '0')
  
  return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hour)
    .replace('mm', minute)
    .replace('ss', second)
}

/**
 * 格式化日期时间
 * @param {string|Date} date - 日期对象或字符串
 * @returns {string} YYYY-MM-DD HH:mm:ss
 */
export const formatDateTime = (date) => {
  return formatDate(date, 'YYYY-MM-DD HH:mm:ss')
}

/**
 * 获取相对时间（如：刚刚、5分钟前、昨天）
 * @param {string|Date} date - 日期对象或字符串
 * @returns {string} 相对时间描述
 */
export const formatRelativeTime = (date) => {
  if (!date) return ''
  
  const d = new Date(date)
  const now = new Date()
  const diff = now - d
  const seconds = Math.floor(diff / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)
  
  if (seconds < 60) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`
  if (days < 30) return `${Math.floor(days / 7)}周前`
  if (days < 365) return `${Math.floor(days / 30)}个月前`
  return `${Math.floor(days / 365)}年前`
}

/**
 * 获取难度文本
 * @param {string} difficulty - easy/medium/hard
 * @returns {string} 中文描述
 */
export const getDifficultyText = (difficulty) => {
  const map = {
    easy: '初级',
    medium: '中级',
    hard: '高级'
  }
  return map[difficulty] || difficulty || '未知'
}

/**
 * 获取难度样式类名
 * @param {string} difficulty - easy/medium/hard
 * @returns {string} CSS类名
 */
export const getDifficultyClass = (difficulty) => {
  const map = {
    easy: 'badge-success',
    medium: 'badge-warning',
    hard: 'badge-danger'
  }
  return map[difficulty] || 'badge-info'
}

/**
 * 截断文本
 * @param {string} text - 原文本
 * @param {number} length - 最大长度
 * @param {string} suffix - 后缀，默认 '...'
 * @returns {string} 截断后的文本
 */
export const truncateText = (text, length = 50, suffix = '...') => {
  if (!text) return ''
  if (text.length <= length) return text
  return text.substring(0, length) + suffix
}

/**
 * 高亮关键词
 * @param {string} text - 原文本
 * @param {string} keyword - 关键词
 * @returns {string} 带高亮标签的HTML
 */
export const highlightKeyword = (text, keyword) => {
  if (!text || !keyword) return text
  const regex = new RegExp(`(${keyword.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')})`, 'gi')
  return text.replace(regex, '<mark class="highlight">$1</mark>')
}

/**
 * 数字格式化（千分位）
 * @param {number} num - 数字
 * @returns {string} 格式化后的字符串
 */
export const formatNumber = (num) => {
  if (num === null || num === undefined) return '0'
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

/**
 * 文件大小格式化
 * @param {number} bytes - 字节数
 * @returns {string} 格式化后的字符串
 */
export const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

/**
 * 显示消息提示（全局方法）
 * @param {string} message - 消息内容
 * @param {string} type - 类型：success/error/info/warning
 * @param {number} duration - 显示时长（毫秒）
 */
export const showMessage = (message, type = 'info', duration = 3000) => {
  if (window.showMessage) {
    window.showMessage(message, type, duration)
  } else {
    // 降级处理
    alert(message)
  }
}
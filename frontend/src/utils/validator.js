/**
 * 验证用户名
 * @param {string} username - 用户名
 * @returns {object} { valid: boolean, message: string }
 */
export const validateUsername = (username) => {
  if (!username) {
    return { valid: false, message: '用户名不能为空' }
  }
  if (username.length < 3) {
    return { valid: false, message: '用户名至少3个字符' }
  }
  if (username.length > 20) {
    return { valid: false, message: '用户名不能超过20个字符' }
  }
  if (!/^[a-zA-Z0-9_\u4e00-\u9fa5]+$/.test(username)) {
    return { valid: false, message: '用户名只能包含字母、数字、下划线或中文' }
  }
  return { valid: true, message: '' }
}

/**
 * 验证密码
 * @param {string} password - 密码
 * @returns {object} { valid: boolean, message: string }
 */
export const validatePassword = (password) => {
  if (!password) {
    return { valid: false, message: '密码不能为空' }
  }
  if (password.length < 6) {
    return { valid: false, message: '密码至少6个字符' }
  }
  if (password.length > 50) {
    return { valid: false, message: '密码不能超过50个字符' }
  }
  return { valid: true, message: '' }
}

/**
 * 验证确认密码
 * @param {string} password - 密码
 * @param {string} confirmPassword - 确认密码
 * @returns {object} { valid: boolean, message: string }
 */
export const validateConfirmPassword = (password, confirmPassword) => {
  if (password !== confirmPassword) {
    return { valid: false, message: '两次输入的密码不一致' }
  }
  return { valid: true, message: '' }
}

/**
 * 验证邮箱
 * @param {string} email - 邮箱地址
 * @returns {object} { valid: boolean, message: string }
 */
export const validateEmail = (email) => {
  if (!email) {
    return { valid: true, message: '' } // 邮箱可选
  }
  const emailRegex = /^[^\s@]+@([^\s@]+\.)+[^\s@]+$/
  if (!emailRegex.test(email)) {
    return { valid: false, message: '邮箱格式不正确' }
  }
  return { valid: true, message: '' }
}

/**
 * 验证手机号（中国大陆）
 * @param {string} phone - 手机号
 * @returns {object} { valid: boolean, message: string }
 */
export const validatePhone = (phone) => {
  if (!phone) {
    return { valid: true, message: '' } // 手机号可选
  }
  const phoneRegex = /^1[3-9]\d{9}$/
  if (!phoneRegex.test(phone)) {
    return { valid: false, message: '手机号格式不正确（11位数字，以1开头）' }
  }
  return { valid: true, message: '' }
}

/**
 * 验证菜谱标题
 * @param {string} title - 菜谱标题
 * @returns {object} { valid: boolean, message: string }
 */
export const validateRecipeTitle = (title) => {
  if (!title || !title.trim()) {
    return { valid: false, message: '菜谱标题不能为空' }
  }
  if (title.length > 100) {
    return { valid: false, message: '菜谱标题不能超过100个字符' }
  }
  return { valid: true, message: '' }
}

/**
 * 验证食材清单
 * @param {string} ingredients - 食材清单
 * @returns {object} { valid: boolean, message: string }
 */
export const validateIngredients = (ingredients) => {
  if (!ingredients || !ingredients.trim()) {
    return { valid: false, message: '食材清单不能为空' }
  }
  if (ingredients.length > 2000) {
    return { valid: false, message: '食材清单不能超过2000个字符' }
  }
  return { valid: true, message: '' }
}

/**
 * 验证制作步骤
 * @param {string} steps - 制作步骤
 * @returns {object} { valid: boolean, message: string }
 */
export const validateSteps = (steps) => {
  if (!steps || !steps.trim()) {
    return { valid: false, message: '制作步骤不能为空' }
  }
  if (steps.length > 5000) {
    return { valid: false, message: '制作步骤不能超过5000个字符' }
  }
  return { valid: true, message: '' }
}

/**
 * 验证烹饪时间
 * @param {number} time - 烹饪时间（分钟）
 * @returns {object} { valid: boolean, message: string }
 */
export const validateCookingTime = (time) => {
  if (time === null || time === undefined) {
    return { valid: false, message: '烹饪时间不能为空' }
  }
  const num = Number(time)
  if (isNaN(num)) {
    return { valid: false, message: '烹饪时间必须是数字' }
  }
  if (num <= 0) {
    return { valid: false, message: '烹饪时间必须大于0' }
  }
  if (num > 600) {
    return { valid: false, message: '烹饪时间不能超过600分钟' }
  }
  return { valid: true, message: '' }
}

/**
 * 验证分类ID
 * @param {number} categoryId - 分类ID
 * @returns {object} { valid: boolean, message: string }
 */
export const validateCategoryId = (categoryId) => {
  if (!categoryId) {
    return { valid: false, message: '请选择菜系分类' }
  }
  return { valid: true, message: '' }
}

/**
 * 验证URL
 * @param {string} url - URL地址
 * @returns {boolean} 是否有效
 */
export const isValidUrl = (url) => {
  if (!url) return true // 可选
  try {
    new URL(url)
    return true
  } catch {
    return false
  }
}

/**
 * 验证是否为纯数字
 * @param {string} str - 字符串
 * @returns {boolean} 是否纯数字
 */
export const isNumeric = (str) => {
  return /^\d+$/.test(str)
}

/**
 * 验证是否为手机号格式（宽松）
 * @param {string} phone - 手机号
 * @returns {boolean}
 */
export const isPhoneNumber = (phone) => {
  return /^1[3-9]\d{9}$/.test(phone)
}

/**
 * 验证是否为邮箱格式
 * @param {string} email - 邮箱
 * @returns {boolean}
 */
export const isEmail = (email) => {
  return /^[^\s@]+@([^\s@]+\.)+[^\s@]+$/.test(email)
}

/**
 * 综合验证表单
 * @param {object} data - 表单数据
 * @param {array} rules - 验证规则
 * @returns {object} { valid: boolean, errors: object }
 */
export const validateForm = (data, rules) => {
  const errors = {}
  let isValid = true
  
  for (const [field, rule] of Object.entries(rules)) {
    const value = data[field]
    const validators = Array.isArray(rule) ? rule : [rule]
    
    for (const validator of validators) {
      let result
      if (typeof validator === 'function') {
        result = validator(value)
      } else if (validator.validator) {
        result = validator.validator(value)
      } else {
        continue
      }
      
      if (!result.valid) {
        errors[field] = result.message
        isValid = false
        break
      }
    }
  }
  
  return { valid: isValid, errors }
}
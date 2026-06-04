import request from './index'

export const recipeApi = {
  // 搜索菜谱
  search(params) {
    return request.get('/recipe/search', { params })
  },
  
  // 获取菜谱详情
  getDetail(id) {
    return request.get(`/recipe/detail/${id}`)
  },
  
  // 添加菜谱
  add(data) {
    return request.post('/recipe/add', data)
  },
  
  // 更新菜谱
  update(data) {
    return request.put('/recipe/update', data)
  },
  
  // 删除菜谱
  delete(id) {
    return request.delete(`/recipe/delete/${id}`)
  },
  
  // 获取分类列表
  getCategories() {
    return request.get('/category/list')
  }
}
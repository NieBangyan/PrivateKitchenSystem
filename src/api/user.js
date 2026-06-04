import request from './index'

export const userApi = {
  // 登录
  login(data) {
    return request.post('/user/login', data)
  },
  
  // 注册
  register(data) {
    return request.post('/user/register', data)
  },
  
  // 登出
  logout() {
    return request.post('/user/logout')
  },
  
  // 获取当前用户
  getCurrent() {
    return request.get('/user/current')
  },
  
  // 获取用户列表
  getUserList(params) {
    return request.get('/user/list', { params })
  },
  
  // 添加用户
  addUser(data) {
    return request.post('/user/add', data)
  },
  
  // 删除用户
  deleteUser(id) {
    return request.delete(`/user/delete/${id}`)
  },
  
  // 修改密码
  changePassword(data) {
    return request.put('/user/change-password', data)
  }
}
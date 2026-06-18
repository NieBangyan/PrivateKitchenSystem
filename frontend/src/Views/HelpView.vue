<template>
  <div class="help-container">
    <h2>系统帮助</h2>
    
    <div class="help-section">
      <h3>🔍 如何使用本系统？</h3>
      <p>1. <strong>浏览菜谱</strong>：点击导航栏的"菜谱大全"即可查看所有菜谱，支持按名称、菜系、难度进行筛选搜索。</p>
      <p>2. <strong>发布菜谱</strong>：登录后点击"发布菜谱"，填写菜谱信息即可分享您的独家美食。</p>
      <p>3. <strong>用户管理</strong>：管理员可以添加、删除用户，管理用户权限。</p>
      <p>4. <strong>个人中心</strong>：可以修改个人信息和登录密码。</p>
      <p>5. <strong>数据导出</strong>：在菜谱列表页面可以导出数据为Excel格式。</p>
    </div>
    
    <div class="help-section">
      <h3>⚡ 快捷键说明</h3>
      <p>• <kbd>Enter</kbd> - 在搜索框中快速搜索</p>
      <p>• <kbd>Esc</kbd> - 关闭对话框</p>
    </div>
    
    <div class="help-section">
      <h3>📧 联系我们</h3>
      <p>如有问题或建议，请联系我们：</p>
      <p>邮箱：support@private-kitchen.com</p>
      <p>电话：0377-000-XXXX</p>
      <p>地址：南阳市</p>
    </div>
    
    <div class="help-section">
      <h3>📝 系统说明</h3>
      <p>版本：v2.0.0</p>
      <p>更新日期：2026年6月</p>
      <p>技术支持：私房菜团队</p>
      <p>技术栈：Spring Boot + Vue 3 + Vite + Pinia</p>
    </div>
  </div>
</template>

<style scoped>
.help-container {
  background: white;
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.1);
}

.help-container h2 {
  margin-bottom: 25px;
  color: #333;
  padding-bottom: 10px;
  border-bottom: 2px solid #eeac31;
}

.help-section {
  margin-bottom: 30px;
}

.help-section h3 {
  color: #f0a356;
  margin-bottom: 15px;
  font-size: 18px;
}

.help-section p {
  color: #666;
  line-height: 1.8;
  margin-bottom: 8px;
}

.help-section kbd {
  background: #f5f5f5;
  padding: 2px 8px;
  border-radius: 4px;
  font-family: monospace;
  border: 1px solid #ddd;
  font-size: 12px;
}

@media (max-width: 768px) {
  .help-container {
    padding: 20px;
  }
}
</style>
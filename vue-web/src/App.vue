<script>
  import UserIndex from "./pages/user/UserIndex.vue"
  import PostMessage from './compoents/PostMessage.vue'
  export default {
  // 2. 注册组件
  components: {
    UserIndex, // 注册后才能在模板中使用
    PostMessage,
  }
}
</script>

<template>
  <div class="common-layout">
    <el-container style="height: 100vh;">
      <el-header  height="20%" style="background: linear-gradient(45deg, rgb(160, 207, 255), transparent);">
        <h1 class="artistic-title">Welcome to y7's Message Board!!!</h1>
      </el-header>
      <el-container>
        <el-aside width="13%" height="100%" style="background: linear-gradient(180deg, rgb(160, 207, 255), transparent);">天气API

        </el-aside>
        <el-main>
          <PostMessage></PostMessage>
          <UserIndex></UserIndex>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
  .artistic-title {
  /* 基础布局 */
  text-align: center;
  height: fit-content; /* 高度自适应内容 */
  margin: 20px auto;
  padding: 15px 20px;
  
  /* 艺术字样式 */
  font-size: 2.5rem;
  font-weight: bold;
  color: transparent; /* 文字颜色透明，显示背景渐变 */
  
  /* 渐变背景 */
  background: linear-gradient(45deg, #ff6b6b, #4ecdc4, #45b7d1);
  background-clip: text;
  -webkit-background-clip: text;
  
  /* 文字阴影 */
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
  
  /* 字符间距和字体 */
  letter-spacing: 2px;
  font-family: 'Arial Rounded MT Bold', 'Helvetica Rounded', sans-serif;
  }

  /* 可选：响应式调整 */
  @media (max-width: 768px) {
    .artistic-title {
      font-size: 1.8rem;
      letter-spacing: 1px;
    }
  }
</style>

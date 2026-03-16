<script setup>
import { RouterView, RouterLink } from 'vue-router'
import WeatherWidget from '@/compoents/WeatherWidget.vue'
</script>

<template>
  <div class="common-layout">
    <el-container style="min-height: 100vh;">
      <el-header class="app-header">
        <div class="header-inner">
          <h1 class="artistic-title">✉️ y7's Message Board</h1>
          <nav class="header-nav">
            <RouterLink to="/">
              <el-button type="primary" round>💬 留言板</el-button>
            </RouterLink>
            <RouterLink to="/admin">
              <el-button type="warning" round>🛠️ 管理后台</el-button>
            </RouterLink>
          </nav>
        </div>
      </el-header>

      <el-container class="main-container">
        <el-aside class="app-aside" width="210px">
          <WeatherWidget />
        </el-aside>
        <el-main class="app-main">
          <RouterView />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style>
/* 全局重置 */
* { margin: 0; padding: 0; box-sizing: border-box; }
html, body { width: 100%; height: 100%; }
</style>

<style scoped>
.app-header {
  background: linear-gradient(135deg, #1a73e8 0%, #0d47a1 50%, #1565c0 100%);
  box-shadow: 0 2px 12px rgba(13, 71, 161, 0.35);
  height: auto !important;
  padding: 0;
}

.header-inner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 28px;
}

.artistic-title {
  margin: 0;
  padding: 0;
  font-size: 1.8rem;
  font-weight: 800;
  color: #fff;
  letter-spacing: 2px;
  font-family: 'Arial Rounded MT Bold', 'Helvetica Rounded', sans-serif;
  text-shadow: 0 2px 8px rgba(0,0,0,0.25);
  white-space: nowrap;
}

.header-nav {
  display: flex;
  gap: 10px;
  align-items: center;
  flex-shrink: 0;
}

.main-container {
  background: #f0f4ff;
  min-height: calc(100vh - 62px);
}

.app-aside {
  background: linear-gradient(180deg, #ddeeff 0%, #eef6ff 60%, #f8fbff 100%);
  border-right: 1px solid rgba(58, 123, 213, 0.15);
  min-height: 100%;
}

.app-main {
  padding: 24px;
  background: #f0f4ff;
}

/* ——— 手机适配 ——— */
@media (max-width: 768px) {
  .header-inner {
    padding: 8px 12px;
    gap: 6px;
    flex-wrap: wrap;
  }

  .artistic-title {
    font-size: 0.95rem;
    letter-spacing: 0;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 180px;
  }

  .header-nav {
    gap: 6px;
    flex-shrink: 0;
  }

  .header-nav :deep(.el-button) {
    padding: 5px 8px !important;
    font-size: 12px !important;
    height: 30px !important;
    border-radius: 15px !important;
  }

  .app-aside {
    display: none !important;
  }

  .app-main {
    padding: 12px;
  }
}

@media (max-width: 400px) {
  .artistic-title {
    font-size: 0.85rem;
    max-width: 130px;
  }
}
</style>

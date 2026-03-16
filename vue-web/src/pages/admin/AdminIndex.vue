<template>
    <div class="admin-page">
        <!-- 登录对话框 -->
        <el-dialog
            v-model="loginVisible"
            title="🔐 管理员登录"
            :width="isMobile ? '92%' : '400px'"
            :close-on-click-modal="true"
            :close-on-press-escape="true"
            :show-close="true"
            align-center
            @close="onLoginClose"
        >
            <div class="login-body">
                <div class="login-tip">请输入管理员账号密码以进入后台管理</div>
                <el-form :model="loginForm" label-width="72px">
                    <el-form-item label="账号">
                        <el-input
                            v-model="loginForm.username"
                            placeholder="请输入管理员账号"
                            clearable
                            @keyup.enter="doLogin"
                        />
                    </el-form-item>
                    <el-form-item label="密码">
                        <el-input
                            v-model="loginForm.password"
                            type="password"
                            placeholder="请输入密码"
                            show-password
                            @keyup.enter="doLogin"
                        />
                    </el-form-item>
                </el-form>
            </div>
            <template #footer>
                <div style="display: flex; gap: 10px;">
                    <el-button style="flex: 1; height: 42px; border-radius: 8px;" @click="onLoginClose">↩ 返回</el-button>
                    <el-button
                        type="primary"
                        :loading="loginLoading"
                        style="flex: 2; height: 42px; font-size: 15px; border-radius: 8px;"
                        @click="doLogin"
                    >
                        登 录
                    </el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 已登录内容 -->
        <template v-if="isAuthed">
            <!-- 顶部统计 -->
            <div class="stats-bar">
                <div class="stat-card">
                    <div class="stat-num">{{ tableData.length }}</div>
                    <div class="stat-label">💬 总留言数</div>
                </div>
                <div class="stat-card">
                    <div class="stat-num">{{ uniqueUsers }}</div>
                    <div class="stat-label">👤 总用户数</div>
                </div>
                <div class="stat-card">
                    <div class="stat-num">{{ todayCount }}</div>
                    <div class="stat-label">📅 今日新增</div>
                </div>
            </div>

            <el-tabs v-model="activeTab" class="admin-tabs">
                <el-tab-pane label="🛠️ 留言管理" name="messages">
                    <!-- 标题栏 -->
                    <div class="table-header">
                        <div style="display: flex; align-items: center; gap: 10px;">
                            <span class="table-title">🛠️ 留言管理</span>
                            <el-tag v-if="selectedRows.length > 0" type="danger" size="small">
                                已选 {{ selectedRows.length }} 条
                            </el-tag>
                        </div>
                        <div style="display: flex; gap: 8px; align-items: center;">
                            <el-popconfirm
                                v-if="selectedRows.length > 0"
                                :title="`确定删除选中的 ${selectedRows.length} 条留言？`"
                                confirm-button-type="danger"
                                @confirm="deleteBatch"
                            >
                                <template #reference>
                                    <el-button type="danger" :icon="Delete">
                                        批量删除 ({{ selectedRows.length }})
                                    </el-button>
                                </template>
                            </el-popconfirm>
                            <el-button type="primary" :icon="Refresh" circle @click="fetchData" title="刷新" />
                        </div>
                    </div>

                    <!-- 数据表格 -->
                    <div class="table-wrap">
                        <el-table
                            ref="tableRef"
                            :data="tableData"
                            stripe
                            style="width: 100%; border-radius: 12px; overflow: hidden;"
                            :header-cell-style="{ background: '#f0f4ff', color: '#2c3e50', fontWeight: '700' }"
                            @selection-change="handleSelectionChange"
                        >
                            <el-table-column type="selection" width="46" align="center" />
                            <el-table-column prop="id" label="#" width="60" align="center" />
                            <el-table-column prop="username" label="用户名" width="140">
                                <template #default="scope">
                                    <div style="display: flex; align-items: center; gap: 8px;">
                                        <div class="mini-avatar" :style="{ background: avatarColor(scope.row.username) }">
                                            {{ scope.row.username?.[0]?.toUpperCase() }}
                                        </div>
                                        <span>{{ scope.row.username }}</span>
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column prop="message" label="留言内容" show-overflow-tooltip />
                            <el-table-column prop="createtime" label="发布时间" width="180" align="center" />
                            <el-table-column label="操作" width="90" align="center" fixed="right">
                                <template #default="scope">
                                    <el-popconfirm
                                        title="确定删除此留言？"
                                        confirm-button-type="danger"
                                        @confirm="deleteMessage(scope.row.id)"
                                    >
                                        <template #reference>
                                            <el-button type="danger" size="small" plain>删除</el-button>
                                        </template>
                                    </el-popconfirm>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </el-tab-pane>

                <el-tab-pane label="📋 操作日志" name="logs">
                    <!-- 标题栏 -->
                    <div class="table-header">
                        <span class="table-title">📋 操作日志</span>
                        <el-button type="primary" :icon="Refresh" circle @click="fetchLogs" title="刷新" />
                    </div>
                    <div class="table-wrap">
                        <el-table
                            :data="logsData"
                            stripe
                            style="width: 100%; border-radius: 12px; overflow: hidden;"
                            :header-cell-style="{ background: '#f0f4ff', color: '#2c3e50', fontWeight: '700' }"
                        >
                            <el-table-column prop="time" label="时间" width="175" align="center" />
                            <el-table-column prop="ip" label="IP地址" width="130" align="center" />
                            <el-table-column prop="username" label="用户名" width="110" align="center" />
                            <el-table-column prop="action" label="操作类型" width="110" align="center">
                                <template #default="scope">
                                    <el-tag
                                        :type="scope.row.action === '登录' ? 'success' : scope.row.action === '登录失败' ? 'warning' : scope.row.action === '留言' ? 'primary' : 'danger'"
                                        size="small"
                                    >
                                        {{ scope.row.action }}
                                    </el-tag>
                                </template>
                            </el-table-column>
                            <el-table-column prop="detail" label="详情" />
                        </el-table>
                        <div v-if="logsData.length === 0" class="empty-tip">暂无操作日志</div>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </template>
    </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, reactive } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { Refresh, Delete } from '@element-plus/icons-vue'

const isMobile = ref(window.innerWidth <= 768)
const onResize = () => { isMobile.value = window.innerWidth <= 768 }
onMounted(() => window.addEventListener('resize', onResize))
onUnmounted(() => window.removeEventListener('resize', onResize))

const tableData = ref([])
const selectedRows = ref([])
const tableRef = ref(null)

// ——— 鉴权 ———
const isAuthed = ref(!!sessionStorage.getItem('adminToken'))
const loginVisible = ref(!sessionStorage.getItem('adminToken'))
const loginLoading = ref(false)
const loginForm = reactive({ username: '', password: '' })

const onLoginClose = () => {
    loginVisible.value = false
    loginForm.username = ''
    loginForm.password = ''
    if (!isAuthed.value) {
        // 未登录时关闭弹窗，跳回首页
        window.location.href = '/'
    }
}

onMounted(() => {
    if (isAuthed.value) {
        fetchData()
        fetchLogs()
    }
})

const doLogin = () => {
    if (!loginForm.username.trim() || !loginForm.password.trim()) {
        ElMessage.warning('请输入账号和密码')
        return
    }
    loginLoading.value = true
    axios.post('/admin/login', {
        username: loginForm.username,
        password: loginForm.password,
    })
        .then(res => {
            const data = res.data
            if (data.success) {
                sessionStorage.setItem('adminToken', data.token)
                isAuthed.value = true
                loginVisible.value = false
                loginForm.username = ''
                loginForm.password = ''
                ElMessage.success('登录成功，欢迎管理员')
                fetchData()
                fetchLogs()
            } else {
                ElMessage.error(data.message || '用户名或密码错误')
            }
        })
        .catch(() => ElMessage.error('登录请求失败，请检查后端服务'))
        .finally(() => { loginLoading.value = false })
}

// ——— 数据 ———
const uniqueUsers = computed(() => new Set(tableData.value.map(i => i.username)).size)
const todayCount = computed(() => {
    const today = new Date().toISOString().slice(0, 10)
    return tableData.value.filter(i => i.createtime?.startsWith(today)).length
})

const COLORS = ['#ff6b6b','#4ecdc4','#45b7d1','#96ceb4','#a29bfe','#fd79a8','#f0a500','#00cec9']
const avatarColor = (name = '') => {
    let hash = 0
    for (const c of name) hash = (hash * 31 + c.charCodeAt(0)) | 0
    return COLORS[Math.abs(hash) % COLORS.length]
}

const fetchData = () => {
    axios.get('/UMs')
        .then(response => { tableData.value = [...response.data].reverse() })
        .catch(error => console.log(error))
}

// ——— 多选 ———
const handleSelectionChange = (rows) => {
    selectedRows.value = rows
}

// ——— 删除 ———
const deleteMessage = (id) => {
    axios.delete(`/DM/${id}`)
        .then(() => { ElMessage.success('删除成功'); fetchData() })
        .catch(() => ElMessage.error('删除失败'))
}

const deleteBatch = () => {
    const ids = selectedRows.value.map(r => r.id)
    axios.delete('/DM/batch', { data: ids })
        .then(() => {
            ElMessage.success(`已删除 ${ids.length} 条留言`)
            selectedRows.value = []
            fetchData()
        })
        .catch(() => ElMessage.error('批量删除失败'))
}

// ——— 日志 ———
const activeTab = ref('messages')
const logsData = ref([])
const fetchLogs = () => {
    axios.get('/admin/logs')
        .then(res => { logsData.value = res.data })
        .catch(() => {})
}
</script>

<style scoped>
.admin-page {
    max-width: 900px;
    margin: 0 auto;
}

.login-body {
    padding: 8px 0 4px;
}

.login-tip {
    font-size: 13px;
    color: #888;
    margin-bottom: 20px;
    text-align: center;
}

.stats-bar {
    display: flex;
    gap: 16px;
    margin-bottom: 24px;
}

.stat-card {
    flex: 1;
    background: #fff;
    border-radius: 14px;
    padding: 18px 14px;
    text-align: center;
    box-shadow: 0 2px 12px rgba(58, 123, 213, 0.1);
}

.stat-num {
    font-size: 2rem;
    font-weight: 800;
    color: #1a73e8;
    line-height: 1.1;
}

.stat-label {
    font-size: 12px;
    color: #888;
    margin-top: 4px;
}

.table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 14px;
}

.table-title {
    font-size: 16px;
    font-weight: 700;
    color: #2c3e50;
}

.table-wrap {
    background: #fff;
    border-radius: 14px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(58, 123, 213, 0.08);
}

.mini-avatar {
    width: 26px;
    height: 26px;
    border-radius: 50%;
    color: #fff;
    font-size: 12px;
    font-weight: 700;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
}

.admin-tabs {
    margin-top: 4px;
}

.empty-tip {
    text-align: center;
    padding: 32px;
    color: #aaa;
    font-size: 14px;
}

@media (max-width: 768px) {
  .admin-page {
    max-width: 100%;
  }

  .stats-bar {
    flex-wrap: wrap;
    gap: 10px;
  }

  .stat-card {
    flex: 1 1 calc(33% - 8px);
    min-width: 80px;
    padding: 12px 8px;
  }

  .stat-num {
    font-size: 1.5rem;
  }

  .table-wrap {
    overflow-x: auto;
    -webkit-overflow-scrolling: touch;
  }

  .table-header {
    flex-wrap: wrap;
    gap: 8px;
  }
}
</style>

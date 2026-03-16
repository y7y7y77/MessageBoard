<template>
    <div class="user-page">
        <PostMessage @submitted="onSubmitted" />

        <div class="list-header">
            <span class="list-title">💬 留言列表</span>
            <el-tag type="info" size="small">共 {{ tableData.length }} 条</el-tag>
        </div>

        <el-empty v-if="tableData.length === 0" description="暂无留言，快来发第一条吧！" :image-size="120" />

        <template v-else>
            <div class="msg-list">
                <div
                    class="msg-card"
                    v-for="item in pagedData"
                    :key="item.id"
                >
                    <div class="msg-avatar" :style="{ background: avatarColor(item.username) }">
                        {{ item.username?.[0]?.toUpperCase() ?? '?' }}
                    </div>
                    <div class="msg-body">
                        <div class="msg-meta">
                            <span class="msg-username">{{ item.username }}</span>
                            <span class="msg-time">🕐 {{ item.createtime }}</span>
                        </div>
                        <div class="msg-content">{{ item.message }}</div>
                    </div>
                </div>
            </div>

            <div class="pagination-bar">
                <el-pagination
                    v-model:current-page="currentPage"
                    :page-size="pageSize"
                    :total="tableData.length"
                    layout="prev, pager, next, jumper"
                    background
                    small
                    @current-change="currentPage = $event"
                />
            </div>
        </template>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import PostMessage from '@/compoents/PostMessage.vue'

const tableData = ref([])
const currentPage = ref(1)
const pageSize = 5

const pagedData = computed(() => {
    const start = (currentPage.value - 1) * pageSize
    return tableData.value.slice(start, start + pageSize)
})

const COLORS = ['#ff6b6b','#4ecdc4','#45b7d1','#96ceb4','#a29bfe','#fd79a8','#f0a500','#00cec9']
const avatarColor = (name = '') => {
    let hash = 0
    for (const c of name) hash = (hash * 31 + c.charCodeAt(0)) | 0
    return COLORS[Math.abs(hash) % COLORS.length]
}

const fetchData = () => {
    axios.get('/UMs')
        .then(response => {
            tableData.value = [...response.data].reverse()
        })
        .catch(error => console.log(error))
}

const onSubmitted = () => {
    fetchData()
    currentPage.value = 1
}

onMounted(fetchData)
</script>

<style scoped>
.user-page {
    max-width: 760px;
    margin: 0 auto;
}

.list-header {
    display: flex;
    align-items: center;
    gap: 10px;
    margin: 20px 0 12px;
}

.list-title {
    font-size: 16px;
    font-weight: 700;
    color: #2c3e50;
}

.msg-list {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.msg-card {
    display: flex;
    gap: 12px;
    align-items: flex-start;
    background: #fff;
    border-radius: 12px;
    padding: 14px 16px;
    box-shadow: 0 1px 6px rgba(58, 123, 213, 0.08);
    border-left: 3px solid transparent;
    transition: border-color 0.2s, box-shadow 0.2s, transform 0.15s;
}

.msg-card:hover {
    border-left-color: #1a73e8;
    box-shadow: 0 4px 14px rgba(58, 123, 213, 0.14);
    transform: translateX(2px);
}

.msg-avatar {
    width: 38px;
    height: 38px;
    border-radius: 50%;
    color: #fff;
    font-size: 16px;
    font-weight: 700;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
}

.msg-body {
    flex: 1;
    min-width: 0;
}

.msg-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 5px;
}

.msg-username {
    font-weight: 700;
    font-size: 13px;
    color: #2c3e50;
}

.msg-time {
    font-size: 11px;
    color: #bbb;
    white-space: nowrap;
}

.msg-content {
    font-size: 13.5px;
    color: #555;
    line-height: 1.6;
    word-break: break-word;
}

.pagination-bar {
    display: flex;
    justify-content: center;
    padding: 16px 0 4px;
}

@media (max-width: 768px) {
  .user-page {
    max-width: 100%;
  }

  .msg-card {
    padding: 10px 12px;
    gap: 10px;
  }

  .msg-avatar {
    width: 32px;
    height: 32px;
    font-size: 13px;
  }

  .msg-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 2px;
    margin-bottom: 4px;
  }

  .msg-time {
    font-size: 10px;
  }
}
</style>
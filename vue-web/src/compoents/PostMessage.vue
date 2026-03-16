<template>
    <div class="post-card">
        <div class="post-header">
            <span class="post-header-icon">✏️</span>
            <span class="post-header-title">发表留言</span>
        </div>
        <div class="post-body">
            <el-form :model="form" :label-width="isMobile ? 'auto' : '72px'" :label-position="isMobile ? 'top' : 'right'">
                <el-form-item label="用户名">
                    <el-input
                        v-model="form.name"
                        placeholder="请输入您的用户名"
                        prefix-icon="User"
                        clearable
                    />
                </el-form-item>
                <el-form-item label="留言内容">
                    <el-input
                        v-model="form.desc"
                        type="textarea"
                        :rows="4"
                        placeholder="写点什么吧……"
                        resize="none"
                    />
                </el-form-item>
                <el-form-item>
                    <el-button
                        type="primary"
                        :loading="loading"
                        style="min-width: 110px;"
                        @click="onSubmit"
                    >
                        🚀 提交留言
                    </el-button>
                    <el-button @click="onReset">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script setup>
import { reactive, ref, onMounted, onUnmounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const emit = defineEmits(['submitted'])

const isMobile = ref(window.innerWidth <= 768)
const onResize = () => { isMobile.value = window.innerWidth <= 768 }
onMounted(() => window.addEventListener('resize', onResize))
onUnmounted(() => window.removeEventListener('resize', onResize))

const form = reactive({
    name: '',
    desc: '',
})

const loading = ref(false)

const onSubmit = () => {
    if (!form.name.trim()) {
        ElMessage.warning('请输入用户名')
        return
    }
    if (!form.desc.trim()) {
        ElMessage.warning('请输入留言内容')
        return
    }
    loading.value = true
    axios.post('/CM', {
        username: form.name,
        message: form.desc,
    })
        .then(() => {
            ElMessage.success('留言成功！')
            form.name = ''
            form.desc = ''
            emit('submitted')
        })
        .catch(() => {
            ElMessage.error('留言失败，请稍后再试')
        })
        .finally(() => {
            loading.value = false
        })
}

const onReset = () => {
    form.name = ''
    form.desc = ''
}
</script>

<style scoped>
.post-card {
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(58, 123, 213, 0.1);
    overflow: hidden;
    margin-bottom: 0;
}

.post-header {
    background: linear-gradient(135deg, #1a73e8, #3a9bd5);
    padding: 14px 20px;
    display: flex;
    align-items: center;
    gap: 8px;
}

.post-header-icon {
    font-size: 18px;
}

.post-header-title {
    font-size: 15px;
    font-weight: 700;
    color: #fff;
    letter-spacing: 1px;
}

.post-body {
    padding: 16px 20px 12px;
}

@media (max-width: 768px) {
  .post-body {
    padding: 12px 12px 8px;
  }
}
</style>
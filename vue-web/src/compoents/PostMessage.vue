<template>
    <el-card>
        <el-form :model="form" label-width="auto" style="max-width: 600px" >
            <el-form-item label="用户名：" label-position="left">
                <el-input v-model="form.name" />
            </el-form-item>
            <el-form-item label="留言：" label-position="left">
                <el-input v-model="form.desc" type="textarea" />
            </el-form-item>
        </el-form>
        <el-button type="primary" @click="onSubmit" >提交留言</el-button>
    </el-card>      
</template>

<script setup>
    import { reactive,ref } from 'vue'
    const form = reactive({
        name: '',
        desc: '',
    })
    const onSubmit = () => {
    console.log('submit!')
    }

</script>

<style>

</style>
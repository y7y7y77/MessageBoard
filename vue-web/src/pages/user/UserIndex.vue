<template>
    <el-card v-for="item in tableData" :key="item.id" >
        <el-descriptions :title="item.username">
            <el-descriptions-item label='留言:'>
            {{item.message}}
            </el-descriptions-item>
            <el-descriptions-item label="时间">
            <el-tag size="small">2025/10/2</el-tag>
            </el-descriptions-item>
        </el-descriptions>
    </el-card>
</template>

<script setup>
        import { reactive,ref } from 'vue'
    import axios from 'axios'
    const tableData=ref([])
    axios.get('/UMs')
    .then(function (response) {
        // 处理成功情况
        tableData.value = response.data;
        console.log(tableData);
    })
    .catch(function (error) {
        // 处理错误情况
        console.log(error);
    })
    .finally(function () {
        // 总是会执行
    });

</script>

<style>

</style>
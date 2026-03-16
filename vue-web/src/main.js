import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from 'axios'

const app = createApp(App)
// 开发时直接请求后端8081，生产(Docker)时前后端同源使用相对路径
axios.defaults.baseURL = import.meta.env.PROD ? '' : 'http://localhost:8081'
app.config.globalProperties.$http=axios
app.use(axios)
app.use(router)
app.use(ElementPlus)
app.mount('#app')

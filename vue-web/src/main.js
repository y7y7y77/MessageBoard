import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from 'axios'

const app = createApp(App)
axios.defaults.baseURL='http://localhost:8081'
app.config.globalProperties.$http=axios
app.use(axios)
app.use(router)
app.use(ElementPlus)
app.mount('#app')

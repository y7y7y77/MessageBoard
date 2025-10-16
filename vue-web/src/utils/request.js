import axios from 'axios'

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:8081', // 基础请求地址（可根据环境配置）
  timeout: 5000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 发送请求前的处理，例如添加token
    // const token = localStorage.getItem('token')
    // if (token) {
    //   config.headers.Authorization = `Bearer ${token}`
    // }
    return config
  },
  error => {
    // 请求错误处理
    console.error('请求拦截器错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 对响应数据的处理
    const res = response.data
    // 例如：统一处理业务错误码
    if (res.code !== 200) {
      console.error('业务错误:', res.msg)
      // 可根据实际需求添加错误提示逻辑
    }
    return res // 返回处理后的数据
  },
  error => {
    // 响应错误处理（如网络错误、404等）
    console.error('响应拦截器错误:', error)
    // 可添加统一的错误提示，如弹框提示网络异常
    return Promise.reject(error)
  }
)

export default service // 导出配置好的axios实例

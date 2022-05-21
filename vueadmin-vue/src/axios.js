import axios from 'axios'
import router from 'vue-router'
import Element from 'element-ui'

// 与后端交互时打开注释
axios.defaults.baseURL = 'http://localhost:8081'

// 创建一个新的axios实例
const request = axios.create({
    timeout: 5000,
    headers: {
        'Content-Type': "application/json; charset=utf-8"
    }
})

// 前置拦截
// 拦截，因为是管理系统，所以每次请求都需要jwt来认证身份
request.interceptors.request.use(config => {
    config.headers['Authorization'] = localStorage.getItem("token")
    return config
})

/*
状态码==200：正常
状态码==500：异常
状态码==401：权限不够/没有权限
状态码==404：无此接口
 */

// 后置拦截
// 请求返回结果拦截
request.interceptors.response.use(
    response => {
        let res = response.data
        if (res.code === 200){
            return response
        } else {
            // res.msg可能为空（此时弹出框为空内容），所以需要判断处理
            Element.Message.error(!res.msg ? '系统异常' : res.msg)
            // 返回拒绝原因
            return Promise.reject(res.msg)
        }
    },
    error => {
        if (error.response.data){
            error.message = error.response.data.msg
        }
        if (error.response.status === 401) {
            router.push('/login')
        }
        Element.Message.error(error.message, {duration: 3000})
        return Promise.reject(error)
    }
)

//导出实例以共享
export default request








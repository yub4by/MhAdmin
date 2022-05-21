import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import global from './globalFun'


/*
element-ui：基于Vue.js 2.0的桌面组件库
axios：一个基于 promise 的 HTTP 库，类ajax
qs：查询参数序列化和解析库
mockjs：为我们生成随机数据的工具库
*/

// npm/cnpm install element-ui --save
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
Vue.use(Element)

// npm/cnpm i axios --save
// import axios from 'axios' //引入axios库
import axios from './axios' //引入自定义的axios.js（在原axios库的基础上做了自定义拦截）
Vue.prototype.$axios = axios
// 设置之后可以通过this.$axios.get()来发起我们的请求了

// npm/cnpm i qs --save
// qs是一个流行的查询参数序列化和解析库。可以将一个普通的object序列化成一个查询字符串，或者反过来将一个查询字符串解析成一个object,帮助我们查询字符串解析和序列化字符串

// 因为后台我们现在还没有搭建，无法与前端完成数据交互，因此我们这里需要mock数据，方便后续我们提供api返回数据
// npm/cnpm i mockjs --save
// 在src目录下新建mock.js文件，用于编写随机数据的api，然后我们需要在main.js中引入这个文件
// mackjs会自动为我们拦截ajax，并自动匹配路径返回数据

// require('./mock') //引入mock数据，（与后端交互时）关闭则注释该行即可


Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

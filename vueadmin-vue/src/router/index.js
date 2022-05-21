import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Index from '../views/Index.vue'
import User from '../views/system/User'
import Role from '../views/system/Role'
import Menu from '../views/system/Menu'
// import axios from 'axios' //原装axios
import axios from '../axios' //自己封装的axios
import store from '../store'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        /**
         * 直接访问http://localhost:8080/时Home正常显示，但其main为空
         * 访问http://localhost:8080/#/index时，Home的main显示Index内容
         * 同样访问http://localhost:8080/#/user，Home的main显示User内容
         */
        children: [
            //子路由
            {
                path: '/index',
                name: 'Index',
                meta: {
                    title: "首页"
                },
                component: Index
            },
            /*{
              path: '/sys/user',
              name: 'SysUser',
              component: User
            },
            {
              path: '/sys/role',
              name: 'SysRole',
              component: Role
            },
            {
              path: '/sys/menu',
              name: 'SysMenu',
              component: Menu
            },*/
            {
                path: '/userCenter',
                name: 'UserCenter',
                meta: {
                    title: "个人中心"
                },
                component: () => import('../views/UserCenter.vue')
            }
        ]
    },
    {
        path: '/login',
        name: 'Login',
        //懒加载形式导入组件；home这个是预先加载形式
        component: () => import('../views/Login.vue')
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

router.beforeEach((to, from, next) => {
    //动态路由
    let hasRoute = store.state.menus.hasRoutes
    let token = localStorage.getItem("token")

    if (to.path == '/login') {
        next()
    } else if (!token) {
        next({path: '/login'})
    } else if(token && !hasRoute) {
        axios.get("/sys/menu/nav", {
            headers: {
                Authorization: localStorage.getItem("token")
            }
        }).then(res => {
            //获取menulist数据
            store.commit('setMenuList', res.data.data.navs)
            //获取用户(操作)权限
            store.commit('setPermList', res.data.data.authorities)
            // 动态绑定路由
            let newRoutes = router.options.routes //原有路由
            res.data.data.navs.forEach(menu => {
                if (menu.children){
                    menu.children.forEach(e => {
                        //  转换为路由
                        let route = menuToRoute(e)
                        //  将路由添加到路由管理器中
                        if (route){
                            newRoutes[0].children.push(route)
                        }
                    })
                }
            })
            router.addRoutes(newRoutes)
            hasRoute = true
            store.commit("changeRouteStatus", hasRoute)
        })
    }
    next()
})

//导航转为路由的函数
const menuToRoute = ((menu) => {
    if (!menu.component){
        return null
    }
    let route = {
        name: menu.name,
        path: menu.path,
        meta: {
            icon: menu.icon,
            title: menu.title
        }
    }
    route.component = (
        () => import('@/views/' + menu.component +'.vue')
    )
    return route
})

export default router

import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default {
    state: {
        //导航列表
        menuList: [],
        //权限列表
        permList: [],
        hasRoutes: false,

        editableTabsValue: 'Index',
        editableTabs: [{
            title: '首页',
            name: 'Index'
        }],
    },
    mutations: {
        setMenuList(state, menus) {
            state.menuList = menus
        },
        setPermList(state, perms) {
            state.permList = perms
        },
        changeRouteStatus(state, hasRoutes) {
            state.hasRoutes = hasRoutes
            sessionStorage.setItem('hasRoutes', hasRoutes)
        },
        addTab(state, tab) {
            //解决点击左侧菜单栏横向tab出现多个重复问题，相同tab唯一性保证
            let index = state.editableTabs.findIndex(e => e.name===tab.name)

            if (index === -1){
                state.editableTabs.push({
                    title: tab.title,
                    name: tab.name
                });
            }
            state.editableTabsValue = tab.name;
        },
        resetState: (state) => {
            // state.token = ''
            state.menuList = []
            state.permList = []
            state.hasRoutes = false
            state.editableTabsValue = 'Index'
            state.editableTabs = [{
                title: '首页',
                name: 'Index',
            }]
        }
    },
    actions: {
    }
}
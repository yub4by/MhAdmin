import Vue from "vue"

//前端全局函数
//用于权限控制，例如User.vue中的<el-button type="primary" @click="dialogVisible = true" v-if="hasAuth('sys:user:save')">新增</el-button>
//'sys:user:save'字符串是自定义的权限指令

Vue.mixin({
    methods: {
        hasAuth(perm) {
            var authority = this.$store.state.menus.permList
            return authority.indexOf(perm) > -1
        }
    }
})
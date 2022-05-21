<template>
    <div>
        <!--<el-menu
                default-active="2"
                class="el-menu-vertical-demo"
                @open="handleOpen"
                @close="handleClose"
                background-color="#545c64"
                text-color="#fff"
                active-text-color="#ffd04b">

            <router-link to="/index">
                <el-menu-item index="0">
                    <template slot="title">
                        <i class="el-icon-s-custom"></i>
                        <span slot="title">首页</span>
                    </template>
                </el-menu-item>
            </router-link>

            <el-submenu index="1">
                <template slot="title">
                    <i class="el-icon-s-operation"></i>
                    <span>系统管理</span>
                </template>

                <router-link to="/sys/user">
                    <el-menu-item index="1-1">
                        <template slot="title">
                            <i class="el-icon-s-custom"></i>
                            <span slot="title">用户管理</span>
                        </template>
                    </el-menu-item>
                </router-link>

                <router-link to="/sys/role">
                    <el-menu-item index="1-2">
                        <template slot="title">
                            <i class="el-icon-rank"></i>
                            <span slot="title">角色管理</span>
                        </template>
                    </el-menu-item>
                </router-link>

                <router-link to="/sys/menu">
                    <el-menu-item index="1-3">
                        <template slot="title">
                            <i class="el-icon-menu"></i>
                            <span slot="title">菜单管理</span>
                        </template>
                    </el-menu-item>
                </router-link>

            </el-submenu>

            <el-submenu index="2">
                <template slot="title">
                    <i class="el-icon-s-tools"></i>
                    <span>系统工具</span>
                </template>
                <el-menu-item index="2-2">
                    <template slot="title">
                        <i class="el-icon-s-order"></i>
                        <span slot="title">数字字典</span>
                    </template>
                </el-menu-item>
            </el-submenu>
        </el-menu>-->

        <el-menu
                :default-active="this.$store.state.menus.editableTabsValue"
                class="el-menu-vertical-demo"
                @open="handleOpen"
                @close="handleClose"
                background-color="#545c64"
                text-color="#fff"
                active-text-color="#ffd04b">

            <router-link to="/index">
                <el-menu-item index="Index" @click="selectMenu({name:'Index', title:'首页'})">
                    <template slot="title">
                        <i class="el-icon-s-custom"></i>
                        <span slot="title">首页</span>
                    </template>
                </el-menu-item>
            </router-link>

            <el-submenu :index="menu.name" v-for="(menu,index) in menuList" :key="index">
                <template slot="title">
                    <i :class="menu.icon"></i>
                    <span>{{menu.title}}</span>
                </template>
                <router-link :to="item.path" v-for="(item,i) in menu.children" :key="i">
                    <el-menu-item :index="item.name" @click="selectMenu(item)">
                        <template slot="title">
                            <i :class="item.icon"></i>
                            <span slot="title">{{item.title}}</span>
                        </template>
                    </el-menu-item>
                </router-link>
            </el-submenu>

        </el-menu>
    </div>
</template>

<script>
    export default {
        name: "SideMenu",
        data() {
            return {
                // 动态导航与动态路由
                /*
                menuList: [
                    {
                        title: '系统管理',
                        name: 'SysManage',
                        icon: 'el-icon-s-operation',
                        path: '',
                        children: [
                            {
                                title: '用户管理',
                                name: 'SysUser',
                                icon: 'el-icon-s-custom',
                                path: '/sys/user',
                                children: []
                            }
                        ]
                    },
                    {
                        title: '系统工具',
                        name: 'SysTools',
                        icon: 'el-icon-s-tools',
                        path: '',
                        children: [
                            {
                                title: '数字字典',
                                name: 'SysDict',
                                icon: 'el-icon-s-order',
                                path: '/sys/dicts',
                                children: []
                            }
                        ]
                    }
                ]*/
            }
        },
        //计算属性
        computed:  {
            menuList: {
                get() {
                    return this.$store.state.menus.menuList
                }
            }
        },
        methods: {
            selectMenu(item) {
                this.$store.commit('addTab', item)
            }
        }
    }
</script>

<style scoped>
    /*左侧纵向导航菜单自定义样式*/
    .el-menu-vertical-demo{
        height: 100%;
    }
    /*超链接去下划线*/
    a {
        text-decoration: none;
    }
</style>
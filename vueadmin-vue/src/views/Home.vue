<template>
    <div>
        <el-container>
            <el-aside width="200px">
                <!--<el-menu
                        default-active="2"
                        class="el-menu-vertical-demo"
                        @open="handleOpen"
                        @close="handleClose"
                        background-color="#545c64"
                        text-color="#fff"
                        active-text-color="#ffd04b">
                    <el-menu-item index="0">
                        <template slot="title">
                            <i class="el-icon-s-custom"></i>
                            <span slot="title">首页</span>
                        </template>
                    </el-menu-item>
                    <el-submenu index="1">
                        <template slot="title">
                            <i class="el-icon-s-operation"></i>
                            <span>系统管理</span>
                        </template>
                        <el-menu-item index="1-1">
                            <template slot="title">
                                <i class="el-icon-s-custom"></i>
                                <span slot="title">用户管理</span>
                            </template>
                        </el-menu-item>
                        <el-menu-item index="1-2">
                            <template slot="title">
                                <i class="el-icon-rank"></i>
                                <span slot="title">角色管理</span>
                            </template>
                        </el-menu-item>
                        <el-menu-item index="1-3">
                            <template slot="title">
                                <i class="el-icon-menu"></i>
                                <span slot="title">菜单管理</span>
                            </template>
                        </el-menu-item>
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
                <SideMenu></SideMenu>
            </el-aside>
            <el-container>
                <el-header>
                    <!--<strong>VueAdmin后台管理系统</strong>
                    <div class="header-avatar">
                        <el-avatar size="middle" src="https://cdn.jsdelivr.net/gh/yub4by/picx-image-hosting@master/20211102/3.5usxk9itngw0.jpg"></el-avatar>
                        <el-dropdown>
                            <span class="el-dropdown-link">
                                Admin<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item>个人中心</el-dropdown-item>
                                <el-dropdown-item>退出登录</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                        <el-link href="https://gitee.com/yub4by" target="_blank">gitee</el-link>
                        <el-link href="http://yppah.cnblogs.com" target="_blank">blog</el-link>
                    </div>-->
                    <Header></Header>
                </el-header>
                <el-main>
                    <Tabs></Tabs>
                    <div style="margin: 0 15px;">
                        <!--在router/index.js中配置Home的子路由，此处展示Index.vue，-->
                        <router-view></router-view>
                    </div>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    import SideMenu from './include/SideMenu'
    import Header from './include/Header'
    import Tabs from './include/Tabs'
    export default {
        name: "Home",
        components: {SideMenu, Header, Tabs}
    }
</script>

<style scoped>
    .el-container{
        padding: 0;
        margin: 0;
        height: 100vh;
    }

    .el-header{
        background-color: #17B3A3; /*header背景颜色#B3C0D1*/
        color: #333;
        text-align: center;
        line-height: 60px;
    }

    .el-aside {
        background-color: #545c64; /*原左侧背景颜色#D3DCE6*/
        color: #333;
        /*text-align: center;*/
        line-height: 200px;
    }

    .el-main {
        /*background-color: #E9EEF3;*/ /*main背景颜色*/
        color: #333;
        /*text-align: center;*/
        /*line-height: 160px;*/
        padding: 0;
    }

    /*header右侧菜单栏样式(已被抽取到子组件Header)*/
    .header-avatar{
        float: right;
        width: 210px;
        display: flex;
        justify-content: space-around;
        align-items: center;
    }
    el-dropdown-link {
        cursor: pointer;
    }

    /*左侧纵向导航菜单自定义样式(已被抽取到子组件SideMenu)*/
    .el-menu-vertical-demo{
        height: 100%;
    }
</style>
<template>
    <div>
        <strong>VueAdmin后台管理系统</strong>
        <div class="header-avatar">
            <el-avatar size="middle" :src="userInfo.avatar"></el-avatar>
            <el-dropdown>
            <span class="el-dropdown-link">
                {{userInfo.username}}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>
                        <router-link :to="{name: 'UserCenter'}">个人中心</router-link>
                        <!--两种to的绑定形式都行，效果一样-->
                        <!--<router-link to="/userCenter">个人中心</router-link>-->
                    </el-dropdown-item>
                    <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
            <el-link href="https://gitee.com/yub4by" target="_blank">gitee</el-link>
            <el-link href="http://yppah.cnblogs.com" target="_blank">blog</el-link>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Header",
        data() {
            return{
                userInfo: {
                    id: '',
                    username: '',
                    avatar: ''
                }
            }
        },
        created() {
            this.getUserInfo()
        },
        methods: {
            getUserInfo() {
                this.$axios.get('/sys/userInfo').then(res => {
                    this.userInfo = res.data.data
                })
            },
            logout() {
                this.$axios.post("/logout").then(res => {
                    localStorage.clear()
                    sessionStorage.clear()
                    this.$store.commit("resetState")
                    this.$router.push("/login")
                })
            }
        }
    }
</script>

<style scoped>
    /*header右侧菜单栏样式*/
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
    /*超链接去下划线*/
    a {
        text-decoration: none;
    }
</style>
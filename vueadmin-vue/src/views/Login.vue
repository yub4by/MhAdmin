<template>
    <div>
        <el-row type="flex" class="row-bg" justify="center">
            <!--<el-col :span="6">-->
            <el-col :xl="6" :lg="7">
                <div class="grid-content bg-purple">
                    <h2>welcome to vueadmin</h2>
                    <el-image :src="require('@/assets/vxclub.jpg')" style="height: 180px; width: 180px;"></el-image>
                    <p>微信公众号：IRUNTOU</p>
                    <p>欢迎扫描关注，回复【vueadmin】获取登录密码</p>
                </div>
            </el-col>
            <el-col :span="1">
                <div class="grid-content bg-purple-light">
                    <!--垂直分割线-->
                    <el-divider direction="vertical"></el-divider>
                </div>
            </el-col>
            <!--<el-col :span="6">-->
            <el-col :xl="6" :lg="7">
                <div class="grid-content bg-purple">
                    <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="100px">
                        <el-form-item label="用户名" prop="username" style="width: 380px;">
                            <el-input v-model="loginForm.username"></el-input>
                        </el-form-item>
                        <el-form-item label="密码" prop="password" style="width: 380px;">
                            <el-input type="password" v-model="loginForm.password"></el-input>
                        </el-form-item>
                        <el-form-item label="验证码" prop="code" style="width: 380px;">
                            <el-input v-model="loginForm.code" style="width: 172px; float: left;" maxlength="5"></el-input>
                            <el-image :src="myCaptchaImg" class="captchaImg" @click="getCaptcha"></el-image>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="submitForm('loginForm')">登录</el-button>
                            <el-button @click="resetForm('loginForm')">重置</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import qs from 'qs'
    export default {
        name: "Login",
        data() {
            return {
                loginForm: {
                    username: 'admin',
                    password: 'admin',
                    code: '11111',
                    token: '' //后端返回来的
                },
                rules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' }
                    ],
                    code: [
                        { required: true, message: '请输入验证码', trigger: 'blur' },
                        { min: 5, max: 5, message: '长度为 5 个字符', trigger: 'blur' }
                    ]
                },
                myCaptchaImg: null
            };
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        // this.$axios.post('/login', this.loginForm).then(res => {
                        this.$axios.post('/login?'+ qs.stringify(this.loginForm)).then(res => {
                            console.log("login请求发出")
                            console.log(res)
                            const jwt = res.headers['authorization']
                            this.$store.commit('SET_TOKEN', jwt)
                            this.$router.push('/index')
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            getCaptcha() {
                this.$axios.get('/captcha').then(res => {
                    // console.log(res)
                    this.loginForm.token = res.data.data.token
                    this.myCaptchaImg = res.data.data.myCaptchaImg
                    this.loginForm.code = '' //点击验证码图片请求新的验证码时清空验证码输入框
                })
            }
        },
        created() {
            this.getCaptcha()
        }
    }
</script>

<style scoped>
    .el-divider {
        height: 200px;
    }
    .el-row {
        background-color: #fafafa;
        height: 100%;
        display: flex; /*内容上下居中*/
        align-items: center; /*内容水平居中*/
        text-align: center;
        justify-content: center;
    }
    .captchaImg {
        float: right;
        margin-left: 8px; /*设置验证码图片与左侧文本框距离*/
        border-radius: 4px; /*设置验证码图片圆角*/
        width: 100px;
    }
</style>
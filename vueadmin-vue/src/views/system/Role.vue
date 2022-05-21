<template>
    <div>
        <el-form :inline="true">
            <el-form-item>
                <el-input
                    v-model="searchForm.name"
                    placeholder="名称"
                    clearable
                >
                </el-input>
            </el-form-item>
            <el-form-item>
                <el-button @click="getRoleList">搜索</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="dialogVisible = true">新增</el-button>
            </el-form-item>
            <el-form-item>
                <el-popconfirm title="确定批量删除吗？" @confirm="delHandle(null)">
                    <el-button type="danger" slot="reference" :disabled="delBtlStatu">批量删除</el-button>
                </el-popconfirm>
            </el-form-item>
        </el-form>

        <el-table
                ref="multipleTable"
                :data="tableData"
                tooltip-effect="dark"
                style="width: 100%"
                border
                stripe
                @selection-change="handleSelectionChange">
            <el-table-column
                    type="selection"
                    width="55">
            </el-table-column>
            <el-table-column
                    prop="name"
                    label="名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="code"
                    label="唯一编码"
                    show-overflow-tooltip>
            </el-table-column>
            <el-table-column
                    prop="remark"
                    label="描述"
                    show-overflow-tooltip>
            </el-table-column>
            <el-table-column
                    prop="statu"
                    label="状态">
                <template slot-scope="scope">
                    <el-tag size="small" v-if="scope.row.statu === 1" type="success">正常</el-tag>
                    <el-tag size="small" v-else-if="scope.row.statu === 0" type="danger">禁用</el-tag>
                </template>
            </el-table-column>
            <el-table-column
                    prop="icon"
                    label="操作">
                <template slot-scope="scope">
                    <el-button type="text" @click="permHandle(scope.row.id)">分配权限</el-button>
                    <el-divider direction="vertical"></el-divider>
                    <el-button type="text" @click="editHandle(scope.row.id)">编辑</el-button>
                    <el-divider direction="vertical"></el-divider>
                    <template>
                        <el-popconfirm title="确定删除吗？" @confirm="delHandle(scope.row.id)">
                            <el-button type="text" slot="reference">删除</el-button>
                        </el-popconfirm>
                    </template>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                layout="total, sizes, prev, pager, next, jumper"
                :page-sizes="[10, 20, 50, 100]"
                :current-page="current"
                :page-size="size"
                :total="total">
        </el-pagination>

        <el-dialog
                title="编辑/新增"
                :visible.sync="dialogVisible"
                width="600px"
                :before-close="handleClose">
            <el-form :model="editForm" :rules="editFormRules" ref="editForm" label-width="100px" class="demo-editForm">
                <el-form-item label="角色名称" prop="name" label-width="100px">
                    <el-input v-model="editForm.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="唯一编码" prop="code" label-width="100px">
                    <el-input v-model="editForm.code" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="remark" label-width="100px">
                    <el-input v-model="editForm.remark" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="状态" prop="statu" label-width="100px">
                    <el-radio-group v-model="editForm.statu">
                        <el-radio :label=0>禁用</el-radio>
                        <el-radio :label=1>正常</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('editForm')">立即创建</el-button>
                    <el-button @click="resetForm('editForm')">重 置</el-button>
                    <el-button @click="cancleForm('editForm')">取 消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog
                title="分配权限"
                :visible.sync="permDialogVisible"
                width="600px">
            <el-form :model="permForm" label-width="100px" class="demo-editForm">
                    <el-tree
                            :data="permTreeData"
                            show-checkbox
                            node-key="id"
                            ref="permTree"
                            :check-strictly="true"
                            :default-expand-all="true"
                            :props="defaultProps">
                    </el-tree>
            </el-form>
            <span slot="footer" class="dialog-footer">
			    <el-button @click="permDialogVisible = false">取 消</el-button>
			    <el-button type="primary" @click="submitPermFormHandle('permForm')">确 定</el-button>
			</span>
        </el-dialog>
    </div>
</template>

<script>
    //角色管理组件
    export default {
        name: "Role",
        data() {
            return {
                tableData: [],
                /*tableData: [{
                    name: '普通用户',
                    remark: '上海市普陀区金沙江路 1518 弄',
                    code: 'normal',
                    statu: 0
                }, {
                    name: '超级管理员',
                    remark: '上海市普陀区金沙江路 1518 弄',
                    code: 'admin',
                    statu: 1
                }],*/
                multipleSelection: [],

                current: 1,
                total: 0,
                size: 10,

                dialogVisible: false,
                permDialogVisible: false,

                delBtlStatu: true,
                searchForm: {},
                permForm: {},
                editForm: {},
                editFormRules: {
                    name: [
                        {required: true, message: '请输入角色名称', trigger: 'blur'}
                    ],
                    code: [
                        {required: true, message: '请输入唯一编码', trigger: 'blur'}
                    ],
                    statu: [
                        {required: true, message: '请选择状态', trigger: 'blur'}
                    ]
                },

                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                permTreeData: [],
            }
        },
        methods: {
            /*getRoleList(){
                this.$axios.get("/sys/role/list").then(res => {
                    this.tableData = res.data.data.records
                    this.size = res.data.data.size
                    this.current = res.data.data.curren
                    this.total = res.data.data.total
                })
            },*/
            getRoleList(){
                this.$axios.get("/sys/role/list", {
                    params: {
                        //搜索栏参数-名称
                        name: this.searchForm.name,
                        //分页参数-当前页&每页条数
                        current: this.current,
                        size: this.size
                    }
                }).then(res => {
                    this.tableData = res.data.data.records
                    this.size = res.data.data.size
                    this.current = res.data.data.curren
                    this.total = res.data.data.total
                })
            },
            permHandle(id){
                this.permDialogVisible = true
                //弹出分配权限框时，回显后端返回的已勾选数据
                this.$axios.get("/sys/role/info/" + id).then(res => {
                    this.$refs.permTree.setCheckedKeys(res.data.data.menuIds) //为permTree进行赋值
                    this.permForm = res.data.data
                })
            },
            submitPermFormHandle(formName){
                var menuIds = this.$refs.permTree.getCheckedKeys()
                console.log(menuIds)
                this.$axios.post('/sys/role/perm/' + this.permForm.id, menuIds).then(res => {
                    this.$message({
                        showClose: true,
                        message: '操作成功',
                        type: 'success',
                        onClose: () => {
                            this.getRoleList()
                        }
                    });
                    this.permDialogVisible = false
                    // this.permForm = {}
                })
            },
            toggleSelection(rows) {
                if (rows) {
                    rows.forEach(row => {
                        this.$refs.multipleTable.toggleRowSelection(row);
                    });
                } else {
                    this.$refs.multipleTable.clearSelection();
                }
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
                this.delBtlStatu = (val.length === 0)
            },
            handleSizeChange() {
                console.log(`每页 ${val} 条`);
                this.size = val
                this.getRoleList()
            },
            handleCurrentChange() {
                console.log(`当前页: ${val}`);
                this.current = val
                this.getRoleList()
            },
            resetForm(formName) {
                this.$refs[formName].resetFields()
                this.editForm = {}
            },
            cancleForm(formName) {
                this.dialogVisible = false
                this.editForm = {}
            },
            handleClose(done) {
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done()
                        this.editForm = {}
                    })
                    .catch(_ => {})
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post('/sys/role/'+(this.editForm.id ? 'update' : 'save'), this.editForm).then(res => {
                            this.$message({
                                showClose: true,
                                message: '操作成功',
                                type: 'success',
                                onClose: () => {
                                    this.getRoleList()
                                }
                            });
                            this.dialogVisible = false
                            this.editForm = {} // 或者 this.resetForm(formName)
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            editHandle(id) {
                this.$axios.get('/sys/role/info/' + id).then(res => {
                    this.editForm = res.data.data
                    this.dialogVisible = true
                })
            },
            delHandle(id) {
                var ids = []
                if (id) {
                    //若id不为空，则是表格中删除按钮触发的该方法
                    ids.push(id)
                } else {
                    //若id为空，则是批量删除按钮触发的该方法
                    //此时可以从multipleSelection中获取复选框选中记录的id
                    this.multipleSelection.forEach(row => {
                        ids.push(row.id)
                    })
                }
                console.log(ids)
                //单条删除
                // this.$axios.post('/sys/role/delete/' + id).then(res => {
                //批量删除
                this.$axios.post('/sys/role/delete', ids).then(res => {
                    this.$message({
                        showClose: true,
                        message: '删除成功',
                        type: 'success',
                        onClose: () => {
                            this.getRoleList()
                        }
                    });
                })
            },
        },
        created(){
            this.getRoleList()
            //页面一加载时就可以去获取目录数据，用于分配权限功能
            this.$axios.get('/sys/menu/list').then(res => {
                this.permTreeData = res.data.data
            })
        }
    }
</script>

<style scoped>
    .el-pagination{
        float: right;
        margin: 22px;
    }
</style>
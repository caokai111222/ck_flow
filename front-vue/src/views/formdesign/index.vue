<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="表单名称" prop="formName">
        <el-input
          v-model="queryParams.formName"
          placeholder="请输入表单名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表单编号" prop="formCode">
        <el-input
          v-model="queryParams.formCode"
          placeholder="请输入表单编号"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['form:main:add']"
        >新增
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="formList">
      <el-table-column label="表单编号" align="center" prop="formCode"/>
      <el-table-column label="表单名称" align="center" prop="formName"/>
      <el-table-column label="版本号" align="center" prop="edition"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['form:main:edit']"
          >修改
          </el-button>
<!--          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['form:main:remove']"
          >删除
          </el-button>-->
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
   <DrawForm
      @ok="getList"
      ref="DrawFormRef"
    />
  </div>
</template>

<script>
import {listFormMain, delFormMain} from '@/api/flow/form'
import DrawForm from './drawform'

export default {
  name: "FormMain",
  components: { DrawForm },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表单表格数据
      formList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询表单类型列表 */
    getList() {
      this.loading = true;
      listFormMain(this.queryParams).then(response => {
          this.formList = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      )
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        dictId: undefined,
        dictName: undefined,
        dictType: undefined,
        status: "0",
        remark: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
     this.$refs.DrawFormRef.openModal()
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$refs.DrawFormRef.openModal(row)
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除表单编号为"' + row.formCode + '"的数据项？').then(function () {
        return delFormMain(row.id)
      }).then(() => {
        this.getList();
        this.$message.success("删除成功");
      }).catch(() => {
      })
    }
  }
};
</script>

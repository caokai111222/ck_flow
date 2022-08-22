<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程名称" prop="flowName">
        <el-input
          v-model="queryParams.flowName"
          placeholder="请输入流程名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流程编号" prop="flowKey">
        <el-input
          v-model="queryParams.flowKey"
          placeholder="请输入流程编号"
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

    <el-table v-loading="loading" :data="flowList">
      <el-table-column label="流程编号" align="center" prop="flowKey"/>
      <el-table-column label="流程名称" align="center" prop="flowName"/>
      <el-table-column label="版本号" align="center" prop="nowEdition"/>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleRun(scope.row)"
          >发动新流程
          </el-button>
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
   <Flowform ref="FlowformRef" />
  </div>
</template>

<script>
import {listFlowMain, getFlowMain} from '@/api/flow/flow'
import  Flowform from '@/views/flow/flowrun/flowform'
export default {
  name: "FlowRun",
  components: {Flowform},
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
      // 流程表格数据
      flowList: [],
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
    /** 查询流程类型列表 */
    getList() {
      this.loading = true;
      listFlowMain(this.queryParams).then(response => {
          this.flowList = response.data.list;
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
    handleRun(row) {
      getFlowMain(row.id).then(res => {
    this.form = {...res.data}
      const moduleList = res.data.moduleList.filter(t => {
        return t.edition === row.nowEdition
      })
      const conditionList = res.data.conditionList.filter(t => {
        return t.edition === row.nowEdition
      })
      const start = moduleList.find(t => { return t.type === 'bpmn:startEvent'})
      const conditions = conditionList.filter(t => { return t.flowModuleId === start.id})
      this.$refs.FlowformRef.openModal(row, start, conditions)
      })
    },
  }
};
</script>

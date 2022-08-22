<template>
  <div class="app-container">
    <el-table v-loading="loading" :data="flowList">
      <el-table-column label="操作人" align="center" prop="nickName"/>
      <el-table-column label="流程id" align="center" prop="orderId"/>
      <el-table-column label="流程名称" align="center" prop="flowName"/>
      <el-table-column label="节点" align="center" prop="moduleName"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleRun(scope.row)"
          >处理
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleHistory(scope.row)"
          >处理记录
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
    <el-dialog title="审批记录" @cancel="cancelRecord" :visible.sync="visible" width="800px" top="5vh" append-to-body>
      <el-table v-loading="loadingRecord" :data="userRecordList">
        <el-table-column label="操作人" prop="nickName" :show-overflow-tooltip="true" />
        <el-table-column label="节点" prop="moduleName" :show-overflow-tooltip="true" />
        <el-table-column label="审批结果" prop="applyRes" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.flow_apply_res" :value="scope.row.applyRes"/>
          </template>
        </el-table-column>
        <el-table-column label="审批时间" prop="updateTime" :show-overflow-tooltip="true" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleRunHistorty(scope.row)"
            >查看明细
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="totalRecord>0"
        :total="totalRecord"
        :page.sync="queryRecordParams.pageNum"
        :limit.sync="queryRecordParams.pageSize"
        @pagination="getRecordList"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelRecord">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {myTodo, getFlowMain, flowRecord} from '@/api/flow/flow'
import  Flowform from '@/views/flow/flowrun/flowform'
export default {
  name: "FlowRun",
  dicts: ['flow_apply_res'],
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
      // 以下审批记录
      selectRow: {},
      loadingRecord: false,
      userRecordList: [],
      visible: false,
      // 总条数
      totalRecord: 0,
      // 查询参数
      queryRecordParams: {
        pageNum: 1,
        pageSize: 10
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询流程类型列表 */
    getList() {
      this.loading = true;
      myTodo(this.queryParams).then(response => {
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
      getFlowMain(row.flowMainId).then(res => {
      this.form = {...res.data}
      const node = res.data.moduleList.find(t => { return t.id === row.flowModuleId})
        const conditionList = res.data.conditionList.filter(t => {
          return t.flowModuleId === node.id
        })
      const conditions = conditionList.filter(t => { return t.flowModuleId === node.id})
      this.$refs.FlowformRef.openModalRun({...res.data}, node, conditions, row)
      })
    },
    handleHistory(row) {
      this.visible = true
      this.selectRow = row
      this.getRecordList()
    },
    getRecordList() {
      const param = {orderId: this.selectRow.orderId, ...this.queryRecordParams}
      this.loadingRecord = true
      flowRecord(param).then(response => {
        this.userRecordList = response.data.list;
        this.totalRecord = response.data.total;
      }).finally(() => {
        this.loadingRecord = false
      })
    },
    cancelRecord() {
      this.visible = false
    },
    handleRunHistorty(row) {
      this.$refs.FlowformRef.openModalDetail(row)
    }
  }
};
</script>

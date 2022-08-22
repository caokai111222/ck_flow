<template>
  <div>
    <el-dialog
    title="选择表单"
    :visible.sync="open"
    width="800px"
    append-to-body
    :close-on-click-modal="false"
    @cancel="cancel"
  >
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
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="formList">
        <el-table-column label="表单编号" align="center" prop="formCode"/>
        <el-table-column label="表单名称" align="center" prop="formName"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >预览
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-setting"
              @click="handleSelect(scope.row)"
            >选择
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
    </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
  </el-dialog>
    <Preview
      ref="previewref"
    />
  </div>
</template>

<script>
import {listFormMain, delFormMain} from '@/api/flow/form'
import Preview from './preview'
import {getFormMain} from '@/api/flow/form'
import {doWithFormSaveData} from '@/components/formdesign/formutil'
export default {
  name: "FormMainSelect",
  components: { Preview },
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
    openModal(){
      this.open = true
      this.resetQuery()
    },
    // 取消按钮
    cancel() {
      this.open = false;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 预览 */
    handleView(row) {
      getFormMain(row.id).then(res => {
        this.$refs.previewref.loadData(doWithFormSaveData(res.data))
      })

    },
    /** 选中 */
    handleSelect(row) {
      this.$emit('ok', row)
      this.cancel()
    },
  }
};
</script>

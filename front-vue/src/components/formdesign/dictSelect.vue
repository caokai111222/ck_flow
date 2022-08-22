<template>
  <div>
    <el-col :span="9" class="propertyborder-title">
      <el-tooltip effect="dark" :content="content" placement="top">
        <span>{{title}}</span>
      </el-tooltip>
    </el-col>
    <el-col :span="15" class="propertyborder-content">
      <el-input readonly placeholder="请选择字典" v-model="data.options.dictTypeName" class="input-with-select">
        <el-button @click="openSelectDict" slot="append" icon="el-icon-search"></el-button>
      </el-input>
      <el-input type="hidden" v-model="data.options.dictType" slot="prepend" placeholder="请选择"></el-input>
    </el-col>
    <el-dialog title="选择数据字典" :visible.sync="openDict" append-to-body :close-on-click-modal="true" @cancel="cancal">
      <el-table v-loading="loading" :data="typeList">
        <el-table-column label="字典名称" align="center" prop="dictName" :show-overflow-tooltip="true"/>
        <el-table-column label="字典类型" align="center" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span>{{ scope.row.dictType }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
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
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancal()">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import draggable from 'vuedraggable'
import {listType} from '@/api/system/dict/type'

export default {
  props: ['title', 'content', 'data'],
  data() {
    return {
      openDict: false,
      loading: false,
      typeList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      }
    }
  },
  components: {draggable},
  methods: {
    /** 查询字典类型列表 */
    getList() {
      this.loading = true
      listType(this.queryParams).then(response => {
          this.typeList = response.rows
          this.total = response.total
        }
      ).finally(() => {
        this.loading = false
      })
    },
    openSelectDict() {
      this.openDict = true
      this.getList()
    },
    cancal() {
      this.openDict = false
    },
    handleSelect(row) {
      this.data.options.dictTypeName = row.dictName
      this.data.options.dictType = row.dictType
      this.cancal()
    }
  }
}
</script>
<style scoped lang="scss">
.propertyborder-title {
  height: 42px;
  line-height: 42px;
  padding-left: 3px;
  border-top: none;
  border-right: none;
}

.propertyborder-content {

  border-top: none;
  border-right: none;
  padding-right: 3px;
  padding-left: 3px;
  padding-top: 4px;
  padding-bottom: 4px;
  height: 42px;
}
</style>

<template>
  <div>
    <el-input readonly v-model="showName" :disabled="disabled" @focus="openDialog" suffix-icon="el-icon-s-home"/>
    <el-dialog
      title="选择部门"
      :visible.sync="open"
      width="800px"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-skeleton :loading="loading" animated>
        <template slot="template">
          <div style="padding: 14px;">
            <el-skeleton-item variant="h3"/>
            <el-skeleton-item variant="h3"/>
            <el-skeleton-item variant="h3"/>
            <el-skeleton-item variant="h3"/>
            <el-skeleton-item variant="h3"/>
          </div>
        </template>
        <template>
          <el-form label-width="100px">
            <el-form-item label="部门选择">
              <el-checkbox v-model="selectAll" @change="handleCheckedTreeNodeAll">全选/全不选</el-checkbox>
              <el-checkbox v-model="checkStrictly" @change="handleCheckedTreeConnect">父子联动</el-checkbox>
            </el-form-item>
            <el-tree
              :check-strictly="!checkStrictly"
              ref="tree"
              :default-checked-keys="selectIds"
              :data="data"
              show-checkbox
              node-key="id"
              :default-expand-all="true"
              :props="defaultProps"
            />
          </el-form>
        </template>
      </el-skeleton>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { treeselect } from '@/api/system/dept'

export default {
  name: 'DeptSelect',
  props: {
    value: {
      required: true,
      type: String
    },
    name: {
      required: true,
      type: String
    },
    disabled: {
      required: false,
      type: Boolean,
      default: false
    },
    maxnum: {
      required: false,
      type: Number
    },

  },
  data() {
    return {
      selectAll: false,
      checkStrictly: false,
      selectIds: [],
      showName: '',
      data: [],
      loading: false,
      open: false,
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    }
  },
  watch: {
    value: {
      immediate: true,
      handler(newVal, oldVal) {
        if (newVal) {
          const ids = newVal.split(',')
          this.selectIds = ids
        } else {
          this.selectIds = []
          this.showName = ''
          if (this.$refs && this.$refs.tree) {
            this.$refs.tree.setCheckedNodes([])
          }
        }
      }
    }
  },
  created() {
    this.getTreeselect()
  },
  methods: {
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then((response) => {
        this.data = response.data
        const names = []
        if (this.selectIds && this.selectIds.length) {
          this.pushForName(names, response.data)
          this.showName = names.join(',')
        } else {
          this.showName = ''
        }
      })
    },
    pushForName(names, list) {
      list.forEach(t => {
        if (this.selectIds.findIndex(n => n === t.id) > -1) {
          names.push(t.label)
        }
        if (t.children) {
          this.pushForName(names, t.children)
        }
      })
    },
    pushForId(names, list) {
      list.forEach(t => {
        names.push(t.id)
        if (t.children) {
          this.pushForId(names, t.children)
        }
      })
    },
    openDialog() {
      if (!this.disabled) {
        this.open = true
      }
    },
    cancel() {
      this.open = false
    },
    submitForm() {
      const list = this.$refs.tree.getCheckedNodes(false, false)
      let names = []
      let ids = []
      if (list && list.length) {
        names = list.map(t => t.label)
        ids = list.map(t => t.id)
      }
      this.showName = names.join(',')
      if (this.maxnum && ids.length > this.maxnum) {
        this.$message.error(`最多只能选择${this.maxnum}个部门`)
      } else {
        this.$emit('change', {ids: ids.join(','), name: this.name})
        this.cancel()
      }
    },
    handleCheckedTreeConnect() {
      this.checkStrictly = !this.checkStrictly
    },
    handleCheckedTreeNodeAll() {
      this.selectAll = !this.selectAll
      if (this.selectAll) {
        const ids = []
        this.pushForId(ids, this.data)
        this.$refs.tree.setCheckedKeys(ids)
      } else {
        this.$refs.tree.setCheckedNodes([])
      }
    }
  }
}
</script>

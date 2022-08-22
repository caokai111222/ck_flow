<template>
  <div>
    <el-dialog
      title="预览"
      :visible.sync="openPreview"
      width="1200px"
      append-to-body
      :close-on-click-modal="false"
      @cancel="cancelDataScope"
    >
      <el-form :model="form" ref="form" size="small" :inline="true" :rules="rules" label-width="100px">
      <el-row v-if="showData">
        <el-col :span="24" v-for="(element, idx) in showData.layoutitemlist[0].widgetForm.list">
          <template v-if="element && element.type === 'gridPanel'">
              <layout-item
                @changeform="changeform"
                v-for="col in element.columns"
                :form="form"
                :list="col.list || []"
              />
          </template>
          <template v-if="element && element.type === 'tabs'">
            <el-tabs>
              <el-tab-pane
                v-for="tabel in element.options.panelist"
                :key="tabel.name"
                :label="tabel.title"
                :name="tabel.name"
              >
                <layout-item
                  @changeform="changeform"
                  v-for="(col, colIndex) in tabel.columns"
                  :form="form"
                  :list="col.list"
                />
              </el-tab-pane>
            </el-tabs>
          </template>
          <template v-if="element && element.type === 'collapse'">
            <el-collapse>
              <el-collapse-item
                v-for="(coolapse, index) in element.options.panelist"
                :key="index"
                :title="coolapse.title"
              >
                <layout-item
                  @changeform="changeform"
                  v-for="(col, colIndex) in coolapse.columns"
                  :form="form"
                  :list="col.list"
                />
              </el-collapse-item>
            </el-collapse>
          </template>
        </el-col>
      </el-row>
    </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitData">确 定</el-button>
        <el-button @click="cancelDataScope">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import  LayoutItem from '@/components/formdesign/layoutItem'
import {doWithFormShow} from '@/components/formdesign/formutil'
import {getDictsMutil} from "../../api/system/dict/data";
export default {
  components: { LayoutItem },
  data() {
    return {
      openPreview: false,
      showData: null,
      rules: {},
      form: {},
      formConf: null,
      dt: null,
      dicts: {}
    };
  },
  methods: {
    submitData () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const res = this.form
          console.log(res)
        }
      })
    },
    changeform(val) {
      this.form = { ...this.form,
        [val.name]: val.val
      }
    },
    cancelDataScope() {
      this.openPreview = false
    },
    loadData(data) {
      this.openPreview = true
      const showData = {...data}
      this.showData = showData
      const ls = showData.layoutitemlist[0].widgetForm.list
      const rules = {}
      const formData = {}
      ls.forEach(l => {
        if (l.type === 'gridPanel') {
          l.columns.forEach(n =>{
            doWithFormShow(n.list, formData, rules)
          })
        } else {
          l.options.panelist.forEach(col => {
            col.columns.forEach(n =>{
              doWithFormShow(n.list, formData, rules)
            })
          })
        }
      })
      this.rules = rules
      this.form = formData
    }
  },
};
</script>

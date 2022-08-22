<template>
  <div>
    <el-dialog
      title="流程"
      :visible.sync="open"
      width="1200px"
      append-to-body
      :close-on-click-modal="false"
      @cancel="cancelDataScope"
    >
      <el-skeleton :loading="loading" animated>
        <template slot="template">
            <el-skeleton-item
              variant="image"
              style="width: 240px; height: 240px;"
            />
            <div style="padding: 14px;">
              <el-skeleton-item variant="h3" style="width: 50%;" />
              <div
                style="display: flex; align-items: center; justify-items: space-between; margin-top: 16px; height: 16px;"
              >
                <el-skeleton-item variant="text" style="margin-right: 16px;" />
                <el-skeleton-item variant="text" style="width: 30%;" />
              </div>
            </div>
        </template>
        <template>
          <el-form :model="form" ref="form" size="small" :inline="true" :rules="rules" label-width="100px">
            <el-row v-if="showData">
              <el-col :span="24" v-for="(element, idx) in showData.layoutitemlist[0].widgetForm.list">
                <template v-if="element && element.type === 'gridPanel'">
                  <layout-item
                    :dicts="dictList"
                    :disabled="fromInfo.disabled"
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
                        :dicts="dictList"
                        :disabled="fromInfo.disabled"
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
                        :dicts="dictList"
                        :disabled="fromInfo.disabled"
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
        </template>
      </el-skeleton>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary"
                   @click="submitData(button.conditionKey)"
                   v-for="button in buttons">
          {{ button.conditionTxt }}
        </el-button>
        <el-button @click="cancelDataScope">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import  LayoutItem from '@/components/formdesign/layoutItem'
import {doWithFormShow, doWithFormSaveData, doWithFormForFlow} from '@/components/formdesign/formutil'
import {getFormMain} from '@/api/flow/form'
import {flowAddStart, flowToNext, getFormDataById} from "@/api/flow/flow";
export default {
  components: { LayoutItem },
  dicts: ['flow_apply_res'],
  data() {
    return {
      loading: false,
      buttons: [],
      open: false,
      showData: null,
      rules: {},
      form: {},
      formConf: null,
      dt: null,
      fromInfo: {},
      main: {},
      flow: {},
      runType: 0,
      dictList: {}
    };
  },
  methods: {
    // 打开弹窗（新增节点）
    openModal(maindt, flow, buttons) {
      this.open = true
      this.runType = 0
      this.main = maindt
      let buttonList = [...buttons]
      buttonList.forEach(t => {
        const dict = this.dict.type.flow_apply_res.find(dt => {return dt.value === t.conditionKey})
        t.conditionTxt = dict.label
      })
      this.buttons = buttonList
      const id = flow.formMainId
      this.loading = true
      getFormMain(id).then(res => {
        this.fromInfo = doWithFormSaveData(res.data)
        this.loadData(this.fromInfo)
      }).finally(() => {
        this.loading = false
      })
    },
    // 打开弹窗 节点流动
    openModalRun(maindt, flow, buttons, node) {
      this.open = true
      this.runType = 1
      this.main = maindt
      this.flow = node
      let buttonList = [...buttons]
      buttonList.forEach(t => {
        const dict = this.dict.type.flow_apply_res.find(dt => {return dt.value === t.conditionKey})
        t.conditionTxt = dict.label
      })
      this.buttons = buttonList
      const id = flow.formMainId
      this.loading = true
      getFormMain(id).then(res => {
        this.fromInfo = doWithFormSaveData(res.data)
        this.loadData(this.fromInfo)
      }).finally(() => {
        this.loading = false
      })
    },
    // 打开弹窗 查看历史
    openModalDetail(flowInfo){
      this.open = true
      this.buttons = []
      getFormMain(flowInfo.formMainId).then(res => {
        this.fromInfo = doWithFormSaveData(res.data)
        getFormDataById({taskId: flowInfo.id}).then(resp => {
          const list = resp.data
          this.loadData(this.fromInfo, list)
          this.fromInfo.disabled = true
        })
      }).finally(() => {
        this.loading = false
      })
    },
    cancelDataScope() {
      this.open = false
    },
    submitData(key) {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const resCheck = doWithFormForFlow({...this.form}, this.fromInfo)
          const res = resCheck.data
          if (resCheck.isOk) {
            if (this.runType === 0) {
              const postDt = {
                businessId: new Date().getTime(),
                businessKey: 'ceshi',
                flowMainKey: this.main.flowKey,
                userId: '1',
                autoCommit: 'Y',
                dataJson: JSON.stringify(res)
              }
              flowAddStart(postDt).then(res => {
                if (res.code === 200) {
                  this.$message.success('操作成功')
                  this.$emit('ok')
                  this.cancelDataScope()
                } else {
                  this.$message.error(res.msg)
                }
              })
            } else {
              const postDt = {
                orderId: this.flow.orderId,
                nodeId: this.flow.nodeId,
                applyRes: key,
                userId: this.flow.userId,
                dataJson: JSON.stringify(res)
              }
              flowToNext(postDt).then(res => {
                if (res.code === 200) {
                  this.$message.success('操作成功')
                  this.$emit('ok')
                  this.cancelDataScope()
                } else {
                  this.$message.error(res.msg)
                }
              })
            }
          } else {
            this.$alert(resCheck.msg.join(''), '请注意', {
              dangerouslyUseHTMLString: true,
              confirmButtonText: '确定',
              callback: () => {

              }
            })
          }
        }
      })
    },
    changeform(val) {
      this.form = { ...this.form,
        [val.name]: val.val
      }
    },
    loadData(data, saveData) {
      this.openPreview = true
      const showData = {...data}
      this.showData = showData
      const ls = showData.layoutitemlist[0].widgetForm.list
      const rules = {}
      let formData = {}
      ls.forEach(l => {
        if (l.type === 'gridPanel') {
          l.columns.forEach(n =>{
            doWithFormShow(n.list, formData, rules, saveData)
          })
        } else {
          l.options.panelist.forEach(col => {
            col.columns.forEach(n =>{
              doWithFormShow(n.list, formData, rules, saveData)
            })
          })
        }
      })
      this.rules = rules
      this.form = formData
    }
  }
}
</script>

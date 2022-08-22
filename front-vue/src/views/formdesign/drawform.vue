<template>
  <div>
    <el-dialog
      title="表单绘制"
      :visible.sync="open"
      append-to-body
      :close-on-click-modal="false"
      fullscreen
      @cancel="closeForm"
    >
    <el-container>
      <el-header>
        <el-row>
          <el-col :span="24" style="text-align: right">
            <el-button type="danger" @click="clearAll" icon="el-icon-delete">清空</el-button>
            <el-button type="info" @click="doPreview" icon="el-icon-view">预览</el-button>
            <el-button type="primary" :loading="subLoaing" @click="saveform()" icon="el-icon-folder">保存</el-button>
            <el-button @click="closeForm()" icon="el-icon-close">关闭</el-button>
          </el-col>
        </el-row>
      </el-header>
      <el-container v-if="!showcodeeditor">
        <el-aside width="260px" style="padding: 0" v-if="showleftright">
          <tools :data="widgetForm"></tools>
        </el-aside>
        <el-main>
          <template v-if="editcontroltype === 0">
            <el-row>
              <el-col
                v-for="(item, index) in fromInfo.layoutitemlist"
                :span="item.span"
                v-bind:key="index"
                style="border: 2px dashed #eee;"
              >
                <widgetForm
                  :data="item.widgetForm"
                  :activePanel.sync="activePanel"
                  :select.sync="widgetFormSelect"
                  :editcontroltype.sync="editcontroltype"
                  :currentcontroldata.sync="currentcontroldata"
                  :basecontroldata.sync="basecontroldata"
                ></widgetForm>
              </el-col>
            </el-row>
          </template>
        </el-main>
        <el-aside width="350px" style="padding: 0" v-if="showleftright">
          <el-tabs type="border-card">
            <el-tab-pane label="控件">
              <widgetConfig
              :data="widgetFormSelect"
              :currentcontroldata.sync="currentcontroldata"
              :basecontroldata.sync="basecontroldata"
              :editcontroltype.sync="editcontroltype"
              :select.sync="widgetFormSelect"
            />
            </el-tab-pane>
            <el-tab-pane label="表单">
              <formConfig
                :data="fromInfo"
                :select.sync="widgetFormSelect"
                :dialogData.sync="dialogData"
                :editcontroltype.sync="editcontroltype"
              />
            </el-tab-pane>
          </el-tabs>
        </el-aside>
      </el-container>
    </el-container>
    <Preview
      ref="previewref"
    />
    </el-dialog>
  </div>
</template>
<script>
// 拖拽
import Draggable from 'vuedraggable'
import tools from '@/components/formdesign/tools'
import widgetForm from '@/components/formdesign/widgetForm'
import widgetConfig from '@/components/formdesign/widgetConfig'
import formConfig from '@/components/formdesign/formConfig.vue'
import Preview from './preview'
import {addFormMain, getFormMain} from '@/api/flow/form'
import {doWithFormSaveData} from '@/components/formdesign/formutil'
export default {
  components: {
    tools, widgetForm, Draggable, widgetConfig,formConfig, Preview
  },
  data() {
    return {
      subLoaing: false,
      loading: false,
      formData: {},
      open: false,
      dialogData: null,
      widgetForm: {
        list: []
      },
      showcodeeditor: false,
      showleftright: true,
      fromInfo: {
        formName: '',
        formDesc: '',
        //formCode: '',
        formid: '',
        layoutitemlist: [
          {
            span: 24,
            name: '布局面板',
            widgetForm: {
              list: [
                {
                  type: 'gridPanel',
                  name: '模块控件',
                  icon: 'el-icon-s-grid',
                  title: '模块控件',
                  isShowSpanSetting: true,
                  columns: [
                    {
                      span: 24,
                      list: []
                    }
                  ],
                  options: {
                    idPrefix: 'pnl_',
                    controlId: new Date().getTime() / 1000 + Math.ceil(Math.random() * 99999),
                    tagattr: ''
                  },
                  key: Date.parse(new Date()) / 1000 + '_' + Math.ceil(Math.random() * 99999),
                  model: 'gridPanel_' + Date.parse(new Date()) / 1000 + '_' + Math.ceil(Math.random() * 99999)
                }
              ]
            }
          }
        ]
      },
      activePanel: 'formProperty',
      widgetFormSelect: null,
      // 等于0 代表编辑表单 1 编辑弹出层，2代表编辑局部模块
      editcontroltype: 0,
      currentcontroldata: null,
      basecontroldata: null,
    }
  },
  methods: {
    openModal(data) {
      this.open = true
      this.doClear()
      if (data) {
        getFormMain(data.id).then(res => {
          this.fromInfo = doWithFormSaveData(res.data)
          this.widgetFormSelect = {}
        })
      }
    },
    // 清空
    clearAll() {
      this.$confirm('确定要清空所有组件吗？', '提示', { type: 'warning' }).then(
        () => {
          this.doClear()
        }
      )
    },
    doClear() {
      this.fromInfo.layoutitemlist = [
        {
          span: 24,
          name: '布局面板',
          widgetForm: {
            list: [
              {
                type: 'gridPanel',
                name: '模块控件',
                icon: 'el-icon-s-grid',
                title: '模块控件',
                isShowSpanSetting: true,
                columns: [
                  {
                    span: 24,
                    list: []
                  }
                ],
                options: {
                  idPrefix: 'pnl_',
                  controlId: new Date().getTime() / 1000 + Math.ceil(Math.random() * 99999),
                  tagattr: ''
                },
                key: Date.parse(new Date()) / 1000 + '_' + Math.ceil(Math.random() * 99999),
                model: 'gridPanel_' + Date.parse(new Date()) / 1000 + '_' + Math.ceil(Math.random() * 99999)
              }
            ]
          }
        }
      ]
      this.formData.html = ''
      this.formData.js = ''
      this.formData.css = ''
    },
    // 预览
    doPreview() {
      this.openPreview = true
      this.$refs.previewref.loadData({...this.fromInfo})
    },
    saveform() {
      const fromInfo = {...this.fromInfo}
      if (!this.fromInfo.formName) {
        this.$message.error("请输入表单名称")
        return false
      }
      const postData = {
        id: this.fromInfo.formid,
        formName: this.fromInfo.formName,
        //formCode: this.fromInfo.formCode,
        formDesc: this.fromInfo.formDesc
      }
      const ls = []
      const layoutitemlist = fromInfo.layoutitemlist
      layoutitemlist.forEach(layout=>{
        const widgetForm = layout.widgetForm
        const layList = widgetForm.list
        layList.forEach((l, idx) => {
          // 遍历容器
          const item = {
            key: l.key,
            itemType: l.type,
            configJson: JSON.stringify(l),
            itemSort: idx
          }
          ls.push(item)
          if(l.options.panelist?.length) {
            // 如果有面板，遍历面板
            l.options.panelist.forEach((p,idxp) => {
              const pane = {
                key: l.key + '_' + idxp,
                pkey: l.key,
                itemType: l.type + '_' + 'pane',
                configJson: JSON.stringify(p),
                itemSort: idxp
              }
              ls.push(pane)
              const items = p.columns
              this.addItems(items,pane.key, ls)
            })
          } else {
            // 如果没有面板，遍历colums
            const items = l.columns
            this.addItems(items,l.key, ls)
          }
        })
      })
      postData.items = ls
      this.subLoaing = true
      addFormMain(postData).then(res => {
        if (res.code === 200) {
          this.$message.success('操作成功')
          this.closeForm()
          this.$emit('ok')
        } else {
          this.$message.error('操作失败:' + res.msg)
        }
      }).finally(() => {
        this.subLoaing = false
      })
    },
    // 遍历增加控件
    addItems(columns, pkey, ls) {
      columns.forEach(column => {
        const list = column.list || []
        list.forEach((t, idx)=>{
          const item = {
            key: t.key,
            pkey: pkey,
            itemType: t.type,
            itemName: t.options.filed,
            configJson: JSON.stringify(t),
            itemSort: idx
          }
          ls.push(item)
        })
      })
    },
    closeForm() {
      this.doClear()
      this.open = false
    }
  },
}
</script>

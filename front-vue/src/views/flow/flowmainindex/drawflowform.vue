<template>
  <div>
    <el-dialog title="流程设置" :visible.sync="open" fullscreen append-to-body :close-on-click-modal="false">
      <el-container style="padding-bottom: 0px;height: 100%">
        <el-header>
          <el-form :model="form" ref="form" size="small" :inline="true" :rules="rules" label-width="100px">
            <el-row>
              <el-col :span="8">
                <el-form-item label="流程名称" prop="flowName">
                  <el-input
                    v-model="form.flowName"
                    placeholder="请输入流程名称"
                    clearable
                    style="width: 240px"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="流程编号" prop="flowKey">
                  <el-input
                    v-model="form.flowKey"
                    placeholder="请输入流程编号"
                    clearable
                    style="width: 240px"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-button
                  type="primary"
                  plain
                  icon="el-icon-document"
                  size="mini"
                  @click="handleSaveAll"
                  :loading="loadingSumit"
                  v-hasPermi="['flow:main:add']"
                >保存
                </el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-header>
        <el-container style="border: 1px solid #eee">
          <el-aside width="450px" style="border-right: 1px solid #eee; margin-bottom: 0 !important;">
            <el-row>
              <el-form :model="formDetail" ref="formDetail" size="small" :inline="true" :rules="rulesDetail"
                       label-width="80px">
                <template v-if="formDetail && formDetail.type === 'bpmn:sequenceFlow'">
                  <el-col :span="24">
                    <el-col :span="24">
                      <el-form-item label="标签备注" prop="remark">
                        <el-input
                          v-model="formDetail.remark"
                          placeholder="请输入标签备注"
                          clearable
                          style="width: 240px"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="24">
                      <el-form-item label="触发条件" prop="conditionKey">
                        <el-radio-group v-model="formDetail.conditionKey">
                          <el-radio-button
                            v-for="dict in dict.type.flow_apply_res"
                            :key="dict.value"
                            :label="dict.value"
                          >{{dict.label}}</el-radio-button>
                        </el-radio-group>
                      </el-form-item>
                    </el-col>
                    <el-col :span="24">
                      <el-form-item label="触发类型" prop="conditionType">
                        <el-radio-group v-model="formDetail.conditionType" size="small">
                          <el-radio-button label="1">等于触发条件</el-radio-button>
<!--                          <el-radio-button label="2">不等于触发条件</el-radio-button>
                          <el-radio-button label="3">包含触发条件</el-radio-button>-->
                        </el-radio-group>
                      </el-form-item>
                    </el-col>
                  </el-col>
                </template>
                <template v-if="formDetail && (formDetail.type === 'bpmn:userTask' || formDetail.type === 'bpmn:serviceTask')">
                  <el-col :span="24">
                    <el-form-item label="节点名称" prop="moduleName">
                      <el-input
                        v-model="formDetail.moduleName"
                        placeholder="请输入节点名称"
                        clearable
                        style="width: 240px"
                      />
                    </el-form-item>
                  </el-col>
                </template>
                <template v-if="formDetail && (formDetail.type === 'bpmn:serviceTask')">
                  <el-col :span="24">
                    <el-form-item label="通过策略" prop="autoPassType">
                      <el-radio-group v-model="formDetail.autoPassType" size="small">
                        <el-radio-button label="1">立即通过</el-radio-button>
                        <el-radio-button label="2">延迟通过</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="延迟时间" prop="delayTime" v-if="formDetail.autoPassType === '2'">
                      <el-input-number v-model='formDetail.delayTime' :min="0" :precision="0"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="时间单位" prop="delayTimeUnit" v-if="formDetail.autoPassType === '2'">
                      <el-radio-group v-model="formDetail.delayTimeUnit" size="small">
                        <el-radio-button label="1">分钟</el-radio-button>
                        <el-radio-button label="2">小时</el-radio-button>
                        <el-radio-button label="3">天</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                </template>
                <template v-if="formDetail && (formDetail.type === 'bpmn:userTask')">
                  <el-col :span="24">
                    <el-form-item label="通过策略" prop="applyType">
                      <el-radio-group v-model="formDetail.applyType" size="small">
                        <el-radio-button label="1">1人通过即可</el-radio-button>
                        <el-radio-button label="2">必须所有人通过</el-radio-button>
                        <el-radio-button label="3">超过指定比例的人通过</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24" v-if="formDetail.applyType === '3'">
                    <el-form-item label="通过比例" prop="applyScale">
                      <el-input-number v-model='formDetail.applyScale' :min="0" :precision="2" :max="100"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="人员策略" prop="refType">
                      <el-radio-group v-model="formDetail.refType" size="small">
                        <el-radio-button label="1">指定人</el-radio-button>
                        <el-radio-button label="2">指定角色</el-radio-button>
                        <el-radio-button label="3">指定部门的角色</el-radio-button>
                        <el-radio-button label="4">业务传入</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24" v-if="formDetail.refType ==='1'">
                    <el-form-item label="人员名单">
                      <el-tag
                        :key="tag.userId"
                        v-for="tag in selectUserlist"
                        closable
                        :disable-transitions="false"
                        @close="handleClose(tag)">
                        {{ tag.nickName }}
                      </el-tag>
                      <el-button class="button-new-tag" size="small" @click="addNew">+ 新增</el-button>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24"
                          v-if="formDetail.refType && (formDetail.refType === '2' || formDetail.refType === '3')">
                    <el-form-item label="角色选择" prop="roleIds">
                      <el-select v-model="formDetail.roleIds" multiple placeholder="请选择角色">
                        <el-option
                          v-for="item in roleOptions"
                          :key="item.roleId"
                          :label="item.roleName"
                          :value="item.roleId"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24" v-if="formDetail.refType ==='3'">
                    <el-form-item label="部门策略" prop="nextDeptType">
                      <el-radio-group v-model="formDetail.nextDeptType" size="small">
                        <el-radio-button label="1">发起人部门</el-radio-button>
                        <el-radio-button label="2">发起人上级部门</el-radio-button>
                        <el-radio-button label="3">指定部门</el-radio-button>
                        <el-radio-button label="5">指定类型的部门</el-radio-button>
                        <el-radio-button label="4">业务传入</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24" v-if="formDetail.refType ==='3'&& formDetail.nextDeptType ==='5'">
                    <el-form-item label="部门类型" prop="deptType">
                      <el-select
                        v-model="formDetail.deptType"
                        placeholder="请选择部门类型"
                        clearable
                        style="width: 100%"
                      >
                        <el-option
                          v-for="dict in dict.type.dept_type"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                        />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24" v-if="formDetail.refType ==='3' && formDetail.nextDeptType ==='3'">
                    <el-form-item label="指定部门" prop="nextDeptIds">
                      <DeptSelect
                        :value="formDetail.nextDeptIds"
                        name="nextDeptIds"
                        @change="changeDept"
                      />
                    </el-form-item>
                  </el-col>
                </template>
                <template v-if="formDetail && (formDetail.type === 'bpmn:userTask' || formDetail.type === 'bpmn:serviceTask')">
                  <el-col :span="24">
                    <el-form-item label="进入条件" prop="flowInType">
                      <el-radio-group v-model="formDetail.flowInType" size="small">
                        <el-radio-button label="1">任一路径满足</el-radio-button>
                        <el-radio-button label="2">全部路径满足</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                </template>
                <template v-if="formDetail && (formDetail.type === 'bpmn:userTask' || formDetail.type === 'bpmn:startEvent')">
                  <el-col :span="24">
                    <el-form-item label="关联表单" prop="formMainId">
                      <el-input type="text" v-model="formDetail.formMainName">
                        <el-button slot="append" icon="el-icon-search" @click="showForm">选择表单</el-button>
                      </el-input>
                      <el-input type="hidden" v-model="formDetail.formMainId" />
                    </el-form-item>
                  </el-col>
                </template>
              </el-form>
              <el-col :span="24" v-if="formDetail && formDetail.type" style="text-align: right">
                <el-button
                  type="primary"
                  plain
                  icon="el-icon-setting"
                  size="mini"
                  @click="doSaveCell"
                >保存
                </el-button>
              </el-col>
            </el-row>
          </el-aside>
          <el-main>
            <div id="flowMain"></div>
          </el-main>
        </el-container>
      </el-container>
    </el-dialog>
    <SelectUser ref="SelectUserRef" @callback="doSelectUser"/>
    <SelectForm ref="SelectFormRef" @ok="doSelectForm"/>
  </div>
</template>
<script>

import {addFlowMain, updateFlowMain, getFlowMain} from '@/api/flow/flow'
import {getUser} from '@/api/system/user'
import SelectUser from '@/views/system/user/selectUser'
import SelectForm from '@/views/formdesign/formselect'
import LogicFlow from '@logicflow/core'
import { DndPanel, BpmnElement } from '@logicflow/extension';
import { ContextPad } from '@/components/LogicFlow/contextPad'
import setContextPad from '@/components/LogicFlow/setContextPad'
import setDndPanel from '@/components/LogicFlow/setDndPanel'
import "@logicflow/core/dist/style/index.css";
import '@logicflow/extension/lib/style/index.css'

export default {
  name: "FlowDraw",
  dicts: ['flow_apply_res', 'dept_type'],
  components: {
    LogicFlow,DndPanel,BpmnElement,setContextPad, setDndPanel, SelectUser,SelectForm
  },
  data() {
    return {
      loading: false,
      loadingSumit: false,
      // 角色选项
      roleOptions: [],
      deptOptions: [],
      graph: null,
      judgeGraphDiv: null,
      form: {},
      rules: {
        flowName: [
          {required: true, trigger: "blur", message: "请输入流程名称"}
        ],
        flowKey: [
          {required: true, trigger: "blur", message: "请输入流程编号"}
        ]},
      selectUserlist: [],
      formDetail: {},
      flowInList: [],
      rulesDetail: {
        remark: [
          {required: true, trigger: "blur", message: "请输入标签备注"}
        ],
        conditionKey: [
          {required: true, trigger: "blur", message: "请输入触发条件"}
        ],
        conditionType: [
          {required: true, trigger: "blur", message: "请选择触发类型"}
        ],
        moduleName: [
          {required: true, trigger: "blur", message: "请输入节点名称"}
        ],
        applyType: [
          {required: true, trigger: "blur", message: "请选择通过策略"}
        ],
        applyScale: [
          {required: true, trigger: "blur", message: "请输入通过比例"}
        ],
        refType: [
          {required: true, trigger: "blur", message: "请输入人员策略"}
        ],
        roleIds: [
          {required: true, trigger: "blur", message: "请选择角色"}
        ],
        nextDeptType: [
          {required: true, trigger: "blur", message: "请选择部门策略"}
        ],
        nextDeptId: [
          {required: true, trigger: "blur", message: "请选择指定部门"}
        ],
        autoPassType: [
          {required: true, trigger: "blur", message: "请选择通过策略"}
        ],
        delayTime: [
          {required: true, trigger: "blur", message: "请输入延迟时间"}
        ],
        delayTimeUnit: [
          {required: true, trigger: "blur", message: "请选择时间单位"}
        ],
        flowInType: [
          {required: true, trigger: "blur", message: "请选择进入条件"}
        ],
        deptType: [
          {required: true, trigger: "blur", message: "请选择部门类型"}
        ],
        formMainId: [
          {required: true, trigger: "blur", message: "请选择表单"}
        ],
      },
      open: false,
      data: {},
      flowInIds: []
    }
  },
  mounted() {

  },
  created() {
    getUser().then(response => {
      this.roleOptions = response.roles;
    })
  },
  methods: {
    // 指定人回调
    doSelectUser(users) {
      const list = [...this.selectUserlist]
      if (users && users.length) {
        users.forEach(t => {
          const idx = list.findIndex(n => {
            return n.userId === t.userId
          })
          if (idx === -1) {
            list.push(t)
          }
        })
      }
      this.selectUserlist = list
    },
    // 打开弹窗界面
    openModal(row) {
      this.open = true
      this.form = {}
      this.formDetail = {}
      this.selectUserlist = []
      this.createGraph(row)
    },
    // 关闭弹窗界面
    closeModal(row) {
      this.open = false
      this.form = {}
      this.formDetail = {}
      this.selectUserlist = []
    },
    // 删除指定人
    handleClose(tag) {
      this.selectUserlist = this.selectUserlist.filter(t => {
        return t.userId !== tag.userId
      })
    },
    // 选择指定人
    addNew() {
      this.$refs.SelectUserRef.show()
    },
    // 绘制界面
    drawGraph(row) {
      if (!this.graph) {
        this.graph = new LogicFlow({
          container: document.getElementById('flowMain'),
          grid: {
            size: 10,      // 网格大小 10px
            visible: true, // 绘制网格，默认绘制 dot 类型网格
            type: 'dot'
          },
          plugins: [BpmnElement, DndPanel, ContextPad]
        })
        setContextPad(this.graph)
        setDndPanel(this.graph)
        // 监听新增事件
        this.graph.on('node:add,edge:add', ({data, e, position}) => {
          if (data.type === 'bpmn:sequenceFlow') {
            const edgeModel = this.graph.getEdgeModelById(data.id)
            edgeModel.setProperties({
              conditionKey: 'Y',
              conditionType: '1'
            })
            const edgeList = this.graph.getGraphData().edges
            edgeModel.updateText(`路径${edgeList.length + 1} (触发条件:${(edgeModel.getProperties().conditionKey)})`)
            const nodes = edgeList.filter(t => { return t.sourceNodeId === data.sourceNodeId && t.targetNodeId === data.targetNodeId && t.id !== data.id})
            if (nodes && nodes.length) {
              this.$message.error('相同2个节点之间只能有一条路径')
              this.graph.deleteEdge(data.id)
            }
            const sourceNode = this.graph.getNodeModelById(data.sourceNodeId)
            const targetNode = this.graph.getNodeModelById(data.targetNodeId)
            if (sourceNode.type === 'bpmn:startEvent') {
              const edges = edgeList.filter(t => { return t.sourceNodeId === data.sourceNodeId && t.id !== data.id})
              if (edges && edges.length) {
                this.$message.error('开始节点只能有一条出口路径')
                this.graph.deleteEdge(data.id)
              }
            }

            if (targetNode.type === 'bpmn:endEvent') {
              const edges = edgeList.filter(t => { return t.targetNodeId === data.targetNodeId && t.id !== data.id})
              if (edges && edges.length) {
                this.$message.error('结束节点只能有一条入口路径')
                this.graph.deleteEdge(data.id)
              }
            }

          } else if (data.type === 'bpmn:startEvent' || data.type === 'bpmn:endEvent') {
            const nodesList = this.graph.getGraphData().nodes
            const nodes = nodesList.filter(t => { return t.type === data.type && t.id !== data.id})
            if (nodes && nodes.length) {
              this.$message.error('开始节点和结束节点都只能有且只有一个')
              this.graph.deleteNode(data.id)
            }
          }
        })
        // 监听点击事件
        this.graph.on('node:click,edge:click', ({data, e, position}) => {
          const formDetail = {
            ...data.properties,
            id: data.id
          }
          if (data.properties && data.properties.roleIds && typeof data.properties.roleIds === 'string') {
            formDetail.roleIds = (data.properties.roleIds || '').split(',')
          } else {
            formDetail.roleIds = formDetail.roleIds? formDetail.roleIds: []
          }
          formDetail.type = data.type
          if (data.type === 'bpmn:sequenceFlow') {
            formDetail.moduleName = data.text
          } else {
            if (data.text) {
              formDetail.moduleName = data.text.value
            }
            if (data.properties && data.properties.userList) {
              this.selectUserlist = data.properties.userList
            }
          }
          if (!formDetail.flowInType) {
            formDetail.flowInType = '1'
          }
          this.formDetail = formDetail
        })
        // 监听点击画布
        this.graph.on('blank:click', ({data, e, position}) => {
          this.formDetail = {}
        })
      }
      if (row && row.id) {
        this.loading = true
        getFlowMain(row.id).then(res => {
          this.form = {...res.data}
          const moduleList = res.data.moduleList.filter(t => { return t.edition === row.nowEdition })
          const conditionList = res.data.conditionList.filter(t => { return t.edition === row.nowEdition })
          const nodes = []
          const edges = []
          moduleList.forEach(t => {
            nodes.push({
              id: t.id,
              type: t.type,
              x: t.coordinateX,
              y: t.coordinateY,
              text: {
                value: t.moduleName,
                x: t.coordinateX,
                y: t.coordinateY,
              },
              properties: {...t,
              roleIds: t.roleIds || '',
              }
            })
          })
          conditionList.forEach(t => {
            edges.push({
              id: t.id,
              type: 'bpmn:sequenceFlow',
              sourceNodeId: t.flowModuleId,
              targetNodeId: t.nextFlowModuleId,
              text: `${t.remark || ''} (触发条件:${this.getKeName(t.conditionKey)})`,
              properties: {...t}
            })
          })
          this.graph.render({
            nodes,
            edges
          })
        }).finally(() => {
          this.loading = false
        })
      } else {
        this.graph.render()
      }
    },
    // 判断是否绘制界面
    createGraph(row) {
      if (!document.getElementById('flowMain')) {
        this.judgeGraphDiv = setInterval(() => {
          if (document.getElementById('flowMain')) {
            window.clearInterval(this.judgeGraphDiv);
            this.drawGraph(row);
          }
        }, 1000)
      } else {
        this.drawGraph(row);
      }
    },
    changeDept(val) {
      this.formDetail = { ...this.formDetail,
        [val.name]: val.ids
      }
    },
    // 保存节点/路径属性
    doSaveCell() {
      this.$refs["formDetail"].validate(valid => {
        if (valid) {
          const formDetail = {...this.formDetail}
          if (formDetail.type !== 'bpmn:sequenceFlow') {
            if (formDetail.type === 'bpmn:userTask') {
              if (formDetail.refType === '1' && (!this.selectUserlist || !this.selectUserlist.length)) {
                this.$message.error('人员策略为指定人时，请选择指定人员')
                return false
              }
              if (formDetail.refType === '1') {
                formDetail.userList = [...this.selectUserlist]
              }
            }
            const nodeModel = this.graph.getNodeModelById(formDetail.id)
            nodeModel.updateText(formDetail.moduleName)
            this.graph.setProperties(formDetail.id, {...formDetail})
          } else {
            const edgeModel = this.graph.getEdgeModelById(formDetail.id)
            edgeModel.setProperties({...formDetail})
            edgeModel.updateText(`${formDetail.remark} (触发条件:${this.getKeName(formDetail.conditionKey)})`)
          }
          if (formDetail.roleIds) {
            formDetail.roleIds = formDetail.roleIds.join(',')
          }
          this.formDetail = {}
          this.selectUserlist = []
        }
      })
    },
    getKeName(key) {
      const dict = this.dict.type.flow_apply_res.find(t => { return t.value === key })
      return dict ? dict.label : key
    },
    // 弹出表单选择
    showForm () {
      this.$refs.SelectFormRef.openModal()
    },
    // 表单选择完毕
    doSelectForm (formDt) {
      this.formDetail = {
        ...this.formDetail,
        formMainName: formDt.formName,
        formMainId: formDt.id
      }
    },
    // 保存流程
    handleSaveAll() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const form = {...this.form}
          const graphData = this.graph.getGraphData()
          let hasStart = false
          let hasEnd = false
          let errmsg = []
          const nodes = []
          const edges = []
          graphData.nodes.forEach(t => {
            const eg = this.graph.getNodeEdges(t.id)
            const egsize = eg&&eg.length ? eg.length : 0
            if (t.type === 'bpmn:startEvent') {
              hasStart = true
              if (egsize === 0) {
                errmsg.push('开始节点应当最少有一个出口路径')
              }
              if (!t.properties.formMainId) {
                errmsg.push('开始节点应当配置一个关联表单')
              }
            } else if (t.type === 'bpmn:endEvent') {
              hasEnd = true
              if (egsize === 0) {
                errmsg.push('结束节点应当最少有一个入口路径')
              }
            } else {
              if (t.type === 'bpmn:userTask' && !t.properties.formMainId) {
                errmsg.push('用户任务节点应当配置一个关联表单')
              }
              const rk = eg.filter(n => { return n.targetNodeId === t.id})
              const ck = eg.filter(n => { return n.sourceNodeId === t.id})
              if (rk.length === 0 || ck.length === 0) {
                errmsg.push('任务节点应当最少有一个入口一个出口路径')
              }
              if (!t.text) {
                errmsg.push('任务节点应当点击设置属性并保存')
              }
            }
            let roleIds = []
            if (t.properties.roleIds) {
              if (typeof t.properties.roleIds === 'string') {
                roleIds = t.properties.roleIds.split(',')
              } else {
                roleIds = t.properties.roleIds ? t.properties.roleIds : []
              }
            }
            nodes.push({
              ...t.properties,
              type: t.type,
              roleIds,
              id: t.id,
              coordinateX: t.x,
              coordinateY: t.y,
              userIds: t.properties.userList ? t.properties.userList.map((value) => value.userId).join(',') : null
            })
          })
          graphData.edges.forEach(t => {
            edges.push({
              conditionType: t.properties.conditionType,
              conditionKey: t.properties.conditionKey,
              remark: t.properties.remark,
              sourceNodeId: t.sourceNodeId,
              targetNodeId: t.targetNodeId,
              type: t.type,
              id: t.id
            })
          })
          form.edges = edges
          form.nodes = nodes
          if (!(hasStart && hasEnd)) {
            this.$message.error('流程必须有且只有开始和结束节点各一个!')
            return false
          }
          if (errmsg && errmsg.length) {
            this.$message.error(errmsg.join(' , '))
            return false
          }
          console.log(form)
          this.loadingSumit = true
          if (form.id) {
            updateFlowMain(form).then((res) => {
              if (res && res.code) {
                this.$message.success('操作成功')
                this.closeModal()
                this.$emit("ok")
              } else {
                this.$message.error(res.msg)
              }
            }).finally(() => {
              this.loadingSumit = false
            })
          } else {
            addFlowMain(form).then((res) => {
              if (res && res.code) {
                this.$message.success('操作成功')
                this.closeModal()
                this.$emit("ok")
              } else {
                this.$message.error(res.msg)
              }
            }).finally(() => {
              this.loadingSumit = false
            })
          }
        }
      })
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss">
.el-aside {
  height: 750px;
  background-color: #ffffff;
}

.el-main {
  height: 750px;
}

#flowMain {
  width: 100%;
  height: 100%;
}
</style>

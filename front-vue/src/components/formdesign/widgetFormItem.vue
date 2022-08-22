<template>
  <el-col
    v-if="element.type === 'wmpDetail' || element.type === 'listview'|| element.type === 'table'"
    :md="24"
    :lg="24"
    class="widget-view"
    style="padding:0px"
    @click.native.stop="handleSelectWidget(index)"
    :class="{
      active: selectWidget !== null && selectWidget.key === element.key
    }"
  >
    <template v-if="element && element.type === 'wmpDetail'">
      <div
        v-if="element && element.key"
        class="widget-grid-container data-grid widget-grid"
        :class="{
          active: selectWidget !== null && selectWidget.key === element.key
        }"
        :key="element.key"
        style="position: relative;"
        @click.stop="handleSelectWidget(index)"
      >
        <el-row
          style="padding: 5px 5px;"
          @click.stop="handleSelectWidget(index)"
          v-if="
            element.options.buttons !== undefined &&
              element.options.buttons.length > 0
          "
        >
          <template v-for="btn in element.options.buttons">
            <el-button style="margin-left:5px;" size="mini" :key="btn.id">
              <i :class="btn.options.icon"></i>
              {{ btn.options.labelTitle }}
            </el-button>
          </template>
        </el-row>
        <el-row
          type="flex"
          align="center"
          style="overflow-y:hidden;overflow-x:auto"
        >
          <el-col
            v-if="element.options.enableChoose"
            class="detail-column"
            style="width:40px;line-height: 47px;"
          >
            <div class="label-name" style="padding: 7px;">
              <el-checkbox v-if="element.options.multy"></el-checkbox>
            </div>
            <div class="column-content" style="padding: 7px;">
              <el-checkbox v-if="element.options.multy"></el-checkbox>
              <el-radio v-if="element.options.multy === false"></el-radio>
            </div>
          </el-col>
          <el-col
            :style="{
              width: col.type === 'showno' ? col.width + 'px' : auto
            }"
            v-for="(col, colIndex) in element.options.columns"
            :key="colIndex"
            class="detail-column"
            :class="{
              active: selectWidget !== null && selectWidget.key === col.key
            }"
          >
            <template
              v-if="
                col.type !== 'showno' ||
                  (element.options.showNo && col.type === 'showno')
              "
            >
              <label
                class="label-name"
                @click.stop="handleDetailSelectWidget(index, colIndex)"
                :class="{ required: col.options.required }"
              >
                <span>{{ col.options.labelTitle }}</span>
              </label>
              <div class="column-content">
                <!-- <input type="text" v-model="col.options.defaultValue" name placeholder="请输入"> -->
                <WidgetDetailItem
                  v-if="col.type !== 'showno'"
                  :element="col"
                ></WidgetDetailItem>
                <span v-if="col.type == 'showno'">1</span>
              </div>
            </template>
          </el-col>
        </el-row>
        <el-button
          title="删除"
          style="bottom: -20px;"
          @click.stop="handleWidgetDelete(index)"
          class="widget-action-delete"
          v-if="selectWidget !== null && selectWidget.key === element.key"
          circle
          plain
          type="danger"
        >
          <!-- <icon name="icon-trash" style="width: 12px;height: 12px;"></icon> -->
          <i class="el-icon-delete"></i>
        </el-button>
      </div>
    </template>
    <template v-else-if="element && element.type === 'listview'">
      <div
        v-if="element && element.key"
        class="widget-grid-container data-grid widget-grid"
        :class="{
          active: selectWidget !== null && selectWidget.key === element.key
        }"
        :key="element.key"
        style="position: relative;min-height: 47px;"
        @click.stop="handleSelectWidget(index)"
      >
        <el-row
          type="flex"
          align="center"
          style="overflow-y:hidden;overflow-x:auto"
          v-if="element.options.columns.length > 0"
        >
          <el-col
            v-if="element.options.enableChoose === 'true'"
            class="detail-column"
            style="width:40px;line-height: 47px;"
          >
            <div class="label-name" style="padding:7px;">
              <!-- <input type="checkbox">   -->
              <el-checkbox
                v-if="element.options.multy === 'true'"
              ></el-checkbox>
              <el-radio v-if="element.options.multy === 'false'"></el-radio>
            </div>
          </el-col>
          <el-col
            v-if="element.options.showNo === 'true'"
            class="detail-column"
            style="width:80px;line-height: 47px;"
          >
            <div class="label-name">
              <span>序号</span>
            </div>
          </el-col>
          <el-col
            v-for="(col, colIndex) in element.options.columns"
            :key="colIndex"
            class="detail-column"
          >
            <label class="label-name">
              <span>{{ col.title }}</span>
            </label>
          </el-col>
        </el-row>
        <el-button
          title="删除"
          style="bottom: -20px;"
          @click.stop="handleWidgetDelete(index)"
          class="widget-action-delete"
          v-if="selectWidget !== null && selectWidget.key === element.key"
          circle
          plain
          type="danger"
        >
          <!-- <icon name="icon-trash" style="width: 12px;height: 12px;"></icon> -->
          <i class="el-icon-delete"></i>
        </el-button>
      </div>
    </template>
    <template v-else-if="element && element.type === 'table'">
      <div
        v-if="element && element.key"
        class="widget-grid-container data-grid widget-grid"
        :class="{
                active:
                  selectWidget && selectWidget.key === element.key
              }"
        :key="element.key"
        @click="handleSelectWidget(index)"
        style="position: relative;"
      >
        <el-form-item :label="element.options.labelTitle">
          <el-row :gutter="10" class="mb8">
            <el-col :span="2" :offset="20">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
              >新增</el-button>
            </el-col>
          </el-row>
          <el-table
            :data="element.datas"
            style="width: 100%">
            <el-table-column label="拖入控件" align="center" class-name="small-padding fixed-width" fixed="left">
              <draggable
                class="widget-form-list"
                v-model="element.list"
                style="border:1px solid #e8e8e8"

                filter="widget-grid-container"
                v-bind="{
                group: 'people1',
                ghostClass: 'ghost',
                animation: 150
              }"
                @end="handleMoveEnd"
                @add="handleWidgetColAdd($event, element, 1)"
              >
              </draggable>
            </el-table-column>
            <el-table-column
              v-for="(column, idx) in element.list"
              :key="'table_' + index + '_' + idx"
              :prop="column.options.filed"
              :label="column.options.labelTitle">
              <template slot-scope="scope">
                <div  @click.stop="handleSelectWidget(idx)">
                <widget-form-item
                  :element="column"
                  :select.sync="selectWidget"
                  :index="idx"
                  :data="element"
                  @editpartlayout="editpartlayout"
                ></widget-form-item>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

        </el-form-item>
        <el-button
          title="删除"
          style="bottom: -20px;"
          @click.stop="handleWidgetDelete(index)"
          class="widget-action-delete"
          v-if="selectWidget !== null && selectWidget.key === element.key"
          circle
          plain
          type="danger"
        >
          <!-- <icon name="icon-trash" style="width: 12px;height: 12px;"></icon> -->
          <i class="el-icon-delete"></i>
        </el-button>
      </div>
    </template>
  </el-col>
  <el-col
    v-else-if="rowcontrolstr.indexOf(element.type + ',') >= 0"
    :md="element.options.spans * 2"
    :lg="element.options.spans * 2"
    class="widget-view"
    style="padding:0px"
    @click.native.stop="handleSelectWidget(index)"
    :class="{
      active: selectWidget !== null && selectWidget.key === element.key
    }"
  >
    <el-row>
      <el-col
        class="border-content"
        :style="{
          'min-height':
            minheight.indexOf(element.type + ',') >= 0 ? '42px' : '',
          padding: '0px',
          height:
            dynamicheight.indexOf(element.type + ',') >= 0
              ? 'auto'
              : element.options.rowSpan !== undefined
              ? element.options.rowSpan * 42 + 'px'
              : ''
        }"
      >
        <template v-if="element.type === 'calendar'">
          <el-calendar>
            <!-- 这里使用的是 2.5 slot 语法，对于新项目请使用 2.6 slot 语法-->
            <template slot="dateCell" slot-scope="{ date, data }">
              <p :style="{ color: data.isSelected === true ? '#1989FA;' : '' }">
                {{
                  data.day
                    .split('-')
                    .slice(1)
                    .join('-')
                }}
                {{ data.isSelected ? '✔️' : '' }}
              </p>
            </template>
          </el-calendar>
        </template>
        <template v-else-if="element.type === 'imgage'">
          <ImageUpload
            v-model="element.options.defaultValue"
            :limit="element.options.limit"
            :fileSize="element.options.fileSize"
            :fileType="element.options.fileType?element.options.fileType.split(','):[]"
            :isShowTip="element.options.isShowTip"
          />
        </template>
        <template v-else-if="element.type === 'upFilesComp'">
          <FileUpload
            v-model="element.options.defaultValue"
            :limit="element.options.limit"
            :fileSize="element.options.fileSize"
            :fileType="element.options.fileType?element.options.fileType.split(','):[]"
            :isShowTip="element.options.isShowTip"
          />
        </template>
        <el-button
          title="删除"
          @click.stop="handleWidgetDelete(index)"
          v-if="selectWidget !== null && selectWidget.key === element.key"
          class="el-icon-delete"
          style="position: absolute;right: 5px;bottom: -10px;z-index: 100;padding:5px;"
          circle
          plain
          type="danger"
        ></el-button>
      </el-col>
    </el-row>
  </el-col>
  <el-col
    :md="totalSpans"
    :lg="totalSpans"
    v-else
    class="widget-view"
    style="padding:0px"
    :class="{
      active: selectWidget !== null && selectWidget.key === element.key,
      is_req: element.options.required === true,
      'border-mult':
        element.type === 'textarea' ||
        element.type === 'htmlArea' ||
        element.type === 'editor'
    }"
    @click.native.stop="handleSelectWidget(index)"
  >
    <el-row style="padding-left: 1px;">
      <el-col
        v-if="element.type !== 'hidden'"
        class="border-content"
        :style="{
          height:
            element.options.rowSpan !== undefined
              ? 42 * element.options.rowSpan + 'px'
              : ''
        }"
      >
        <el-form-item :label="element.options.labelTitle">
        <template v-if="element.type === 'input'">
          <el-input
            type="text"
            class="form-control"
            v-model="element.options.defaultValue"
            :placeholder="element.options.placeholder"
          />
        </template>
        <template v-else-if="element.type === 'textarea'">
          <el-input
            type="textarea"
            :maxlength="element.options.dataLength"
            class="form-control multy_textarea"
            :placeholder="element.options.placeholder"
            :rows="element.options.rows"
            v-model="element.options.defaultValue"
          ></el-input>
        </template>

        <template v-else-if="element.type === 'radio'">
          <el-radio-group
            v-model="element.options.defaultValue"
            size="mini"
            style="line-height:32px; display:block;"
          >
            <el-radio
              style="inline"
              :label="item.value"
              v-for="(item, index) in element.options.options"
              :key="item.value + index"
              >{{ item.label }}</el-radio
            >
          </el-radio-group>
        </template>
        <template v-else-if="element.type === 'checkbox'">
          <el-checkbox-group
            v-model="element.options.defaultValue"
            size="mini"
            style="line-height:32px;"
          >
            <el-checkbox
              style="inline"
              :label="item.value"
              v-for="(item, index) in element.options.options"
              :key="item.value + index"
              >{{ item.label }}</el-checkbox
            >
          </el-checkbox-group>
        </template>
        <template v-else-if="element.type === 'timepicker'">
          <el-time-picker
            v-model="element.options.defaultValue"
            :is-range="element.options.isRange"
            :placeholder="element.options.placeholder"
            :start-placeholder="element.options.startPlaceholder"
            :end-placeholder="element.options.endPlaceholder"
            :readonly="element.options.readonly"
            :disabled="element.options.disabled"
            :editable="element.options.editable"
            :clearable="element.options.clearable"
            :arrowControl="element.options.arrowControl"
            :style="{ width: element.options.width }"
          ></el-time-picker>
        </template>
        <template v-else-if="element.type === 'date'">
          <template v-if="element.options.modeltype === 'date'">
            <el-date-picker
              v-model="element.options.defaultValue"
              :placeholder="element.options.placeholder"
              :format="element.options.format"
              :disabled="element.options.disabled"
            />
          </template>
          <template v-else-if="element.options.modeltype === 'month'">
            <el-date-picker
              v-model="element.options.defaultValue"
              type="month"
              format="yyyy-MM"
              :placeholder="element.options.placeholder"
              :disabled="element.options.disabled"
            >
            </el-date-picker>
          </template>
          <template v-else-if="element.options.modeltype === 'week'">
            <el-date-picker
              v-model="element.options.defaultValue"
              type="week"
              format="yyyy 第 WW 周"
              :placeholder="element.options.placeholder"
              :disabled="element.options.disabled"
            >
            </el-date-picker>
          </template>
          <template v-else-if="element.options.modeltype === 'range'">
            <el-date-picker
              v-model="element.options.defaultValue"
              :disabled="element.options.disabled"
              type="daterange"
              style="width:100%"
              :format="element.options.format"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </template>
        </template>
        <template v-else-if="element.type === 'select'">
          <el-select
            v-model="element.options.isMulty?(element.options.defaultValue||'').split(',') : element.options.defaultValue"
            :multiple="element.options.isMulty"
            :filterable="element.options.isSearch"
            :placeholder="element.options.placeholder"
            size="small"
            collapse-tags
            style="width: 100%;"
          >
            <el-option
              v-for="item in element.options.options"
              :key="item.label"
              :value="item.value"
              :label="item.label"
            ></el-option>
          </el-select>
        </template>
        <template v-else-if="element.type === 'rate'">
          <el-rate
            v-model="element.options.defaultValue"
            :readonly="element.options.readonly"
            :disabled="element.options.disabled"
          ></el-rate>
        </template>
        <template v-else-if="element.type === 'inputnumber'">
          <el-input-number
            v-model="element.options.defaultValue"
            :disabled="element.options.disabled"
            :min="element.options.min"
            :max="element.options.max"
            :label="element.options.describe"
            :step="element.options.step"
            :precision="element.options.precision"
          ></el-input-number>
        </template>
          <template v-else-if="element.type === 'departmentMutl'">
            <DeptSelect
              v-model="element.options.defaultValue"
            />
          </template>
        <template
          v-else-if="
            element.type === 'user' ||
              element.type === 'choose' ||
              element.type === 'department' ||
              element.type === 'project' ||
              element.type === 'cost'
          "
        >
          <el-input
            type="text"
            class="form-control"
            v-model="element.options.defaultValue"
            :placeholder="element.options.placeholder"
          />
          <i
            class="fa fa-search"
            aria-hidden="true"
            style="position: absolute;right: 25px;top: 15px;"
          ></i>
        </template>
        <template v-else-if="element.type === 'editor'">
          <Editor :value="element.options.defaultValue"></Editor>
        </template>
        <template v-else-if="element.type === 'hidden'"></template>
        <template v-else-if="element.type === 'blank'">
          <div
            style="height: 50px;color: #999;background: #eee;line-height:50px;text-align:center;"
          >
            自定义区域
          </div>
        </template>
        <template v-else-if="element.type === 'htmlArea'">
          <div
            style="color: #999;height:auto;"
            v-html="element.options.htmlContent"
          ></div>
        </template>
        <el-button
          title="删除"
          @click.stop="handleWidgetDelete(index)"
          v-if="selectWidget !== null && selectWidget.key === element.key"
          class="el-icon-delete"
          style="position: absolute;right: 5px;bottom: -10px;z-index: 100;padding:5px;"
          circle
          plain
          type="danger"
        ></el-button>
        <el-button
          title="复制"
          @click.stop="handleWidgetClone(index)"
          class="el-icon-circle-plus-outline"
          v-if="selectWidget !== null && selectWidget.key === element.key"
          style="position: absolute;right: 35px;bottom: -10px;z-index: 100;padding:5px;"
          circle
          plain
          type="danger"
        ></el-button>
        </el-form-item>
      </el-col>
    </el-row>
  </el-col>
</template>

<script>
/* eslint-disable */
import Draggable from 'vuedraggable'
import WidgetDetailItem from '@/components/formdesign/WidgetDetailItem'
export default {
  name: 'WidgetFormItem',
  props: ['element', 'select', 'index', 'data'],
  components: { Draggable, WidgetDetailItem },
  data() {
    return {
      selectWidget: this.select,
      clicktime: new Date(),
      rowcontrolstr:
        'tree,navmenu,blank,partlayout,dynamiclayout,calendar,imgage,upFilesComp,', ///占整行的控件
      dynamicheight: 'partlayout,tree,calendar,imgage,upFilesComp,', ///由内部控件撑高度的控件
      minheight: 'partlayout,dynamiclayout,'
    }
  },
  mounted() {},
  methods: {

    handleMoveEnd({ newIndex, oldIndex }) {
      console.log('index', newIndex, oldIndex)
    },
    handleWidgetAdd(evt) {
      console.log(evt)
    },
    handleWidgetColAdd($event, row, colIndex) {
      console.log('handleWidgetColAdd', $event, row, colIndex)
      const newIndex = $event.newIndex
      const oldIndex = $event.oldIndex
      const item = $event.item
      console.log('handleWidgetColAdd newIndex', newIndex)
      console.log('handleWidgetColAdd oldIndex', oldIndex)
      if (item.className.indexOf('data-grid') >= 0) {
        item.tagName === 'DIV' &&
          this.data.list.splice(oldIndex, 0, row.list[newIndex])

        row.list.splice(newIndex, 1)

        return false
      }

      console.log('from', item)

      const key =
        Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999)
      if (row.list[newIndex].type === 'table') {
        this.$message.warning('无法在控件里面添加子表格')
        row.list.splice(newIndex, 1)
        return false
      }
      const contrloptions = JSON.parse(JSON.stringify({...row.list[newIndex].options || {}}))
  /*    const contrloptions = JSON.parse(
        JSON.stringify(row.columns[colIndex].list[newIndex].options)
      )*/
      this.$set(row.list, newIndex, {
        ...row.list[newIndex],
        options: {
          ...Object.assign({}, contrloptions),
          remoteFunc: 'func_' + key
        },
        key,
        model: row.list[newIndex].type + '_' + key,
        rules: []
      })
      console.log('newItem', row.list[newIndex].type)
      console.log('this.data.list', this.data.list[newIndex])
      const labelId =
        Date.parse(new Date()) / 1000 + '' + Math.ceil(Math.random() * 99999)
      const ctrlId =
        Date.parse(new Date()) / 1000 + '' + Math.ceil(Math.random() * 99999)
      console.log(labelId + '_' + ctrlId)
      if (row.list[newIndex].options.labelId !== undefined) {
        this.$set(
          row.list[newIndex].options,
          'labelId',
          row.list[newIndex].options.labelId + labelId
        )
      }
      if (row.list[newIndex].options.filed !== undefined) {
        this.$set(row.list[newIndex].options, 'filed', 'filed' + ctrlId)
      }
      if (row.list[newIndex].options.controlId !== undefined) {
        this.$set(
          row.list[newIndex].options,
          'controlId',
          row.list[newIndex].options.controlId + ctrlId
        )
      }
      if (
        row.list[newIndex].type === 'radio' ||
        row.list[newIndex].type === 'checkbox' ||
        row.list[newIndex].type === 'select' ||
        (this.data.list[newIndex] && this.data.list[newIndex].type === 'select')
      ) {
        this.$set(row.list, newIndex, {
          ...row.list[newIndex],
          options: {
            ...row.list[newIndex].options,
            options: row.list[newIndex].options.options.map(item => ({
              ...item
            }))
          }
        })
      } else if (row.list[newIndex].type === 'button') {
        console.log('before', row.list)
        this.$set(row.list, newIndex, {
          ...row.list[newIndex],
          key: Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999),
          options: {
            ...row.list[newIndex].options
          },
          buttons: row.list[newIndex].options.buttons.map(item => ({
            ...item,
            id:
              'btn_' +
              Date.parse(new Date()) / 1000 +
              '' +
              Math.ceil(Math.random() * 99999)
          }))
        })
        console.log('after', this.data.list[newIndex])
      }
      this.selectWidget = row.list[newIndex]
      console.log('handleWidgetColAdd end', row.list[newIndex].type)
    },
    handleSelectWidget(index) {
      this.selectWidget = this.data.list[index]
    },
    handleWidgetDelete(index) {
      if (this.data.list.length - 1 === index) {
        if (index === 0) {
          this.selectWidget = {}
        } else {
          this.selectWidget = this.data.list[index - 1]
        }
      } else {
        this.selectWidget = this.data.list[index + 1]
      }

      this.$nextTick(() => {
        this.data.list.splice(index, 1)
      })
    },
    handleDetailSelectWidget(index, colIndex) {
      this.selectWidget = this.data.list[index].options.columns[colIndex]
      this.activeControlPanel = 'fieldProperty'
    },
    handleWidgetClone(index) {
      let cloneData = {
        ...this.data.list[index],
        options: { ...this.data.list[index].options },
        key: Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999)
      }

      const labelId =
        Date.parse(new Date()) / 1000 + '' + Math.ceil(Math.random() * 99999)
      const ctrlId =
        Date.parse(new Date()) / 1000 + '' + Math.ceil(Math.random() * 99999)
      console.log(labelId + '_' + ctrlId)
      if (cloneData.options.labelId !== undefined) {
        cloneData.options.labelId = labelId
      }

      if (cloneData.options.controlId !== undefined) {
        cloneData.options.controlId = ctrlId
      }

      if (
        this.data.list[index].type === 'radio' ||
        this.data.list[index].type === 'checkbox'
      ) {
        cloneData = {
          ...cloneData,
          options: {
            ...cloneData.options,
            options: cloneData.options.options.map(item => ({ ...item }))
          }
        }
      }

      this.data.list.splice(index, 0, cloneData)

      this.$nextTick(() => {
        this.selectWidget = this.data.list[index + 1]
      })
    },
    editpartlayout(docelement) {
      const date1 = new Date()
      const date3 = date1.getTime() - this.clicktime.getTime()
      console.log(date3)
      if (date3 <= 800) {
        this.$emit('editpartlayout', docelement)
      } else {
        this.selectWidget = docelement
      }
      this.clicktime = date1
    },
    editpartlayout1(docelement) {
      this.$emit('editpartlayout', docelement)
    }
  },
  watch: {
    select(val) {
      this.selectWidget = val
    },
    selectWidget: {
      handler(val) {
        this.$emit('update:select', val)
      },
      deep: true
    }
  },
  computed: {
    totalSpans() {
      return parseInt(this.element.options.ctrlSpan)
    },
    innerLabelSpan() {
      return parseInt(this.element.options.ctrlSpan)
    },
    innerCtrlSpan() {
      return parseInt(this.element.options.ctrlSpan)
    },
    ctrlSpan() {
      let span = parseInt(
        this.element.options.span === undefined ? 2 : this.element.options.span
      )
      return span
    },
    multiple() {
      if (this.element.options) {
        return eval(this.element.options.isMulty)
      }
      return false
    }
  }
}
</script>

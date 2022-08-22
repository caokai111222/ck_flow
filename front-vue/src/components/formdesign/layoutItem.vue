<template>
  <div>
    <el-row>
      <el-col v-for="(element, idx) in list" :key="element.key" :span="element.options.ctrlSpan ? element.options.ctrlSpan : 24">
        <el-form-item :label="element.options.labelTitle" :prop="element.options.filed">
          <template v-if="element.type === 'input'">
            <el-input
              type="text"
              class="form-control"
              v-model="form[element.options.filed]"
              :placeholder="element.options.placeholder"
              :maxlength="element.options.dataLength"
              :disabled="element.options.disabled || disabled"
            />
          </template>
          <template v-else-if="element.type === 'textarea'">
            <el-input
              type="textarea"
              :maxlength="element.options.dataLength"
              class="form-control multy_textarea"
              :placeholder="element.options.placeholder"
              :rows="element.options.rows"
              v-model="form[element.options.filed]"
              :disabled="element.options.disabled || disabled"
            ></el-input>
          </template>
          <template v-else-if="element.type === 'radio'">
            <el-radio-group
              v-model="form[element.options.filed]"
              size="mini"
              style="line-height:32px; display:block;"
              :disabled="element.options.disabled || disabled"
            >
              <el-radio
                :label="item.value"
                v-for="(item, index) in element.options.options"
                :key="item.value + index"
              >{{ item.label }}</el-radio
              >
            </el-radio-group>
          </template>
          <template v-else-if="element.type === 'checkbox'">
            <el-checkbox-group
              v-model="form[element.options.filed]"
              size="mini"
              style="line-height:32px;"
              :disabled="element.options.disabled || disabled"
            >
              <el-checkbox
                :label="item.value"
                v-for="(item, index) in element.options.options"
                :key="item.value + index"
              >{{ item.label }}</el-checkbox>
            </el-checkbox-group>
          </template>
          <template v-else-if="element.type === 'timepicker'">
            <el-time-picker
              v-model="form[element.options.filed]"
              :is-range="element.options.isRange"
              :placeholder="element.options.placeholder"
              :start-placeholder="element.options.startPlaceholder"
              :end-placeholder="element.options.endPlaceholder"
              :readonly="element.options.readonly"
              :editable="element.options.editable"
              :clearable="element.options.clearable"
              :arrowControl="element.options.arrowControl"
              :style="{ width: element.options.width }"
              :disabled="element.options.disabled || disabled"
            ></el-time-picker>
          </template>
          <template v-else-if="element.type === 'date'">
            <template v-if="element.options.modeltype === 'date'">
              <el-date-picker
                v-model="form[element.options.filed]"
                :placeholder="element.options.placeholder"
                :format="element.options.format"
                :disabled="element.options.disabled || disabled"
              />
            </template>
            <template v-else-if="element.options.modeltype === 'month'">
              <el-date-picker
                v-model="form[element.options.filed]"
                type="month"
                format="yyyy-MM"
                :placeholder="element.options.placeholder"
                :disabled="element.options.disabled || disabled"
              >
              </el-date-picker>
            </template>
            <template v-else-if="element.options.modeltype === 'week'">
              <el-date-picker
                v-model="form[element.options.filed]"
                type="week"
                format="yyyy 第 WW 周"
                :placeholder="element.options.placeholder"
                :disabled="element.options.disabled || disabled"
              >
              </el-date-picker>
            </template>
            <template v-else-if="element.options.modeltype === 'range'">
              <el-date-picker
                v-model="form[element.options.filed]"
                :disabled="element.options.disabled || disabled"
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
              v-model="form[element.options.filed]"
              :multiple="element.options.isMulty"
              :filterable="element.options.isSearch"
              :placeholder="element.options.placeholder"
              size="small"
              style="width: 100%;"
              :disabled="element.options.disabled || disabled"
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
              v-model="form[element.options.filed]"
              :readonly="element.options.readonly"
              :disabled="element.options.disabled || disabled"
            ></el-rate>
          </template>
          <template v-else-if="element.type === 'inputnumber'">
            <el-input-number
              v-model="form[element.options.filed]"
              :disabled="element.options.disabled || disabled"
              :min="element.options.min"
              :max="element.options.max"
              :label="element.options.describe"
              :step="element.options.step"
              :precision="element.options.precision"
            ></el-input-number>
          </template>
          <template v-else-if="element.type === 'departmentMutl'">
            <DeptSelect
              :disabled="element.options.disabled || disabled"
              v-model="form[element.options.filed]"
              :name="element.options.filed"
              @change="changeDept"
            />
          </template>
          <template v-else-if="element.type === 'imgage'">
            <ImageUpload
              v-model="form[element.options.filed]"
              :limit="element.options.limit"
              :fileSize="element.options.fileSize"
              :fileType="element.options.fileType?element.options.fileType.split(','):[]"
              :isShowTip="element.options.isShowTip"
              :disabled="element.options.disabled || disabled"
            />
          </template>
          <template v-else-if="element.type === 'upFilesComp'">
            <FileUpload
              v-model="form[element.options.filed]"
              :limit="element.options.limit"
              :fileSize="element.options.fileSize"
              :fileType="element.options.fileType?element.options.fileType.split(','):[]"
              :isShowTip="element.options.isShowTip"
              :disabled="element.options.disabled || disabled"
            />
          </template>
          <template v-else-if="element.type === 'editor' && !(element.options.disabled || disabled)">
            <Editor  v-model="form[element.options.filed]"></Editor>
          </template>
          <template v-else-if="element.type === 'editor' && (element.options.disabled || disabled)">
            <div v-html="form[element.options.filed]"></div>
          </template>
          <template v-if="element.type === 'table'">
            <el-row :gutter="10" class="mb8">
              <el-col :span="2" :offset="20" style="margin-bottom: 10px;">
                <el-button
                  type="primary"
                  plain
                  icon="el-icon-plus"
                  size="mini"
                  @click="addTableData(element)"
                  v-if="!(element.options.disabled || disabled)"
                >新增</el-button>
              </el-col>
              <el-col :span="24">
                <el-table
                  :data="form[element.options.filed]"
                  style="width: 100%">
                  <el-table-column
                    v-for="(column, idx) in element.list"
                    :key="'table_' + index + '_' + idx"
                    :prop="column.options.filed"
                    :label="column.options.labelTitle">
                    <template slot-scope="scope">
                      <layout-row-item
                        :disabled="element.options.disabled || disabled"
                        @changeform="changeform"
                        :form="scope.row"
                        :element="column"
                        :rowidx="scope.$index"
                      />
                    </template>
                  </el-table-column>
                  <el-table-column v-if="!(element.options.disabled || disabled)" label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
                    <template slot-scope="scope">
                      <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-delete"
                        @click="delRow(scope.row,scope.$index, element)"
                      >删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-col>
            </el-row>
          </template>
        </el-form-item>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import LayoutRowItem from './layoutrowItem'
export default {
  name: "LayoutItem",
  components: {LayoutRowItem},
  props: {
    form: {
      type: Object,
      required: true
    },
    list: {
      type: Array,
      required: true
    },
    disabled: {
      type: Boolean,
      required: false,
      default: false
    },
  },
  data() {
    return {
      formConf: null,
      dt: null,
    };
  },
  methods: {
    changeform(val) {
      this.$emit('changeform', val)
    },
    changeDept(val) {
      this.$emit('changeform', {name:val.name, val:val.ids})
    },
    addTableData(el) {
      const dt = this.form[el.options.filed] || []
      dt.push({})
      this.form[el.options.filed] = dt
    },
    delRow(row, idx, element) {
      const dt = element.datas || []
      dt.splice(idx, 1)
      element.datas = dt
    },
  },
};
</script>

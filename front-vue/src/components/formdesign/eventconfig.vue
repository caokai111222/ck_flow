<template>
  <div>
    <el-col :span="24" class="propertyborder-title" v-if="isadd">
      <el-button
        type="button"
        size="mini"
        @click="addevent"
        style="    float: right; margin-top: 5px;"
      >
        <i class="fa fa-plus"></i>&nbsp;添加事件
      </el-button>
    </el-col>
    <el-col :span="24" class="widget-config-container" v-if="isadd">
      <template>
        <draggable
          tag="ul"
          :list="datalist"
          v-bind="{
            group: { name: 'options1' },
            ghostClass: 'ghost',
            handle: '.drag-item'
          }"
          handle=".drag-item"
        >
          <li
            v-for="(item, index) in datalist"
            :key="index"
            style="background: lightgray;padding: 2px;margin-bottom:2px;"
          >
            <!-- <el-input placeholder="栅格值" size="mini" style="width: 100px;" type="number" v-model.number="item.span"></el-input> -->
            <el-input
              style="width: 35%;"
              size="mini"
              maxlength="50"
              v-model="item.label"
            ></el-input>
            <el-input
              style="width: 35%;"
              size="mini"
              maxlength="50"
              v-model="item.value"
            ></el-input>

            <i
              class="drag-item"
              style="font-size: 16px;margin: 0 2px;cursor: move;"
            >
              <i class="iconfont icon-icon_bars"></i>
            </i>
            <el-button
              @click="handleButtonRemove(index)"
              circle
              plain
              type="danger"
              size="mini"
              icon="el-icon-minus"
              style="padding: 2px;"
            ></el-button>
            <i
              class="drag-item fa fa-bars"
              style="font-size: 16px;margin: 0 5px;cursor: move;"
            ></i>
          </li>
        </draggable>
      </template>
    </el-col>
  </div>
</template>
<script>
import draggable from 'vuedraggable'
export default {
  props: ['controldata'],
  data() {
    return {
      isadd: false,
      defaullist: [
        {
          label: 'click',
          value: ''
        },
        { label: 'dblclick', value: '' },
        { label: 'mouseover', value: '' },
        { label: 'mouseout', value: '' },
        { label: 'mouseenter', value: '' },
        { label: 'mousedown', value: '' },
        { label: 'mouseup', value: '' },
        { label: 'mousemove', value: '' },
        { label: 'mouseleave', value: '' },
        { label: 'wheel', value: '' },
        { label: 'contextmenu', value: '' },
        { label: 'keydown', value: '' },
        { label: 'keyup', value: '' },
        { label: 'keypress', value: '' },
        { label: 'focus', value: '' },
        { label: 'blur', value: '' },
        { label: 'change', value: '' },
        { label: 'input', value: '' }
      ],
      datalist: [],
      defaultevent: [
        'click',
        'dblclick',
        'mouseover',
        'mouseout',
        'mouseenter',
        'mousedown',
        'mouseup',
        'mousemove',
        'wheel',
        'contextmenu',
        'keydown',
        'keyup',
        'keypress',
        'focus',
        'blur',
        'change',
        'input'
      ]
    }
  },
  components: { draggable },
  methods: {
    addevent() {
      this.datalist.push({
        value: '',
        label: ''
      })
    },
    handleButtonRemove(index) {
      if (this.defaultevent.indexOf(this.datalist[index].label) < 0) {
        this.datalist.splice(index, 1)
      } else {
        this.datalist[index].value = ''
      }
    }
  },
  watch: {
    controldata(val) {
      if (val != null && val.eventlist !== undefined) {
        this.isadd = true
        if (val.eventlist !== null && val.eventlist.length > 0) {
          this.datalist = val.eventlist
        } else {
          val.eventlist = JSON.parse(JSON.stringify(this.defaullist))
          this.datalist = val.eventlist
        }
      } else {
        this.isadd = false
      }
    }
  },
  mounted() {
    if (this.controldata != null && this.controldata.eventlist !== undefined) {
      this.isadd = true
      if (
        this.controldata.eventlist !== null &&
        this.controldata.eventlist.length > 0
      ) {
        this.datalist = this.controldata.eventlist
      } else {
        this.controldata.eventlist = JSON.parse(JSON.stringify(this.defaullist))
        this.datalist = this.controldata.eventlist
      }
    } else {
      this.isadd = false
    }
  }
}
</script>
<style scoped lang="scss">
.propertyborder-title {
  height: 42px;
  line-height: 42px;
  padding-left: 3px;
  border: 1px solid #ccc;
  border-top: none;
  border-right: none;
}

.propertyborder-content {
  border: 1px solid #ccc;
  border-top: none;
  border-right: none;
  padding-right: 3px;
  padding-left: 3px;
  padding-top: 4px;
  padding-bottom: 4px;
  height: 42px;
}
.widget-config-container {
  position: relative;

  .el-header {
    border-bottom: solid 2px #e4e7ed;
    padding: 0px;
  }

  .column-setting .el-form-item__label {
    float: none;
    display: inline-block;
    text-align: left;
    padding: 0 0 2px;
  }

  .config-content {
    border-left: solid 1px #e6e6e6;

    .el-form-item__label {
      padding: 0;
      font-weight: 500;
      float: none;
    }

    .el-form-item {
      /*border-bottom: solid 1px #e1e1e1;
            margin-bottom: 0px;*/
      margin: 5px;
    }
  }

  .ghost {
    background: #fff;
    border: 1px dashed #409eff;

    &::after {
      background: #fff;
      display: block;
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
    }
  }

  ul {
    margin: 0;
    padding: 0;
  }

  li.ghost {
    list-style: none;
    font-size: 0;
    display: block;
    position: relative;
  }
}
</style>

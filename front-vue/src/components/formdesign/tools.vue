<template>
  <el-collapse accordion
               style="padding: 2px;">
    <el-collapse-item
      v-for="(item, index) in controlComponents"
      v-bind:key="index"
    >
      <template slot="title" style="text-align: center;padding: 12px">
        {{ item.title }}
      </template>
      <draggable
        class="tool-draggale"
        :list="item.controls"
        v-bind="item.draggablebind"
        @end="handleMoveEnd"
        @start="handleMoveStart"
        :move="handleMove"
      >
        <el-tag
          effect="plain"
          v-for="(controlitem, index) in item.controls"
          :key="index"
        >
          <i
            :class="controlitem.icon"
            style="font-size: 12px;margin-right: 1px;width: 12px;"
          ></i>
          {{ controlitem.name }}
        </el-tag>
      </draggable>
    </el-collapse-item>
  </el-collapse>
</template>
<script>
import Draggable from 'vuedraggable'
import {controlComponents} from '@/components/formdesign/controlconfig.js'

export default {
  components: {
    Draggable
  },
  props: ['data'],
  data() {
    return {controlComponents}
  },
  methods: {
    handleMoveEnd(evt) {
      console.log('end', evt)
    },
    handleMoveStart() {
    },
    handleMove(evt, originalEvent) {
      console.log('handleMove', evt.draggedContext)
      let canDrag = true
      if (
        evt.draggedContext.element.type === 'jsPanel' &&
        this.data.list.length > 0
      ) {
        this.data.list.forEach(item => {
          if (item.type === 'jsPanel') {
            console.log('jsPancel is exists.')
            canDrag = false
          }
        })
      }
      console.log('handleMove', this.data.list.length)
      console.log('handleMove1', originalEvent)
      return canDrag
    }
  }
}
</script>
<style lang="scss">
.el-aside > .el-collapse > .el-collapse-item {
  .el-collapse-item__header{
    padding: 0 0 0 24px
  }
  .el-tag {
    margin: 8px 10px;
    width: 100px;
    display: inline-block;
    word-break: keep-all;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
  }
}
</style>

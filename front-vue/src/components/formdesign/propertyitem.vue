<template>
  <div>
    <el-col :span="9" class="propertyborder-title">
      <el-tooltip effect="dark" :content="content" placement="top">
        <span>{{title}}</span>
      </el-tooltip>
    </el-col>
    <el-col :span="15" class="propertyborder-content">
      <slot></slot>
    </el-col>
  </div>
</template>
<script>
export default {
  props: ['title', 'content'],
  data() {
    return {}
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

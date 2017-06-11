<template>
  <div class="tab-pane" v-show="display">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: 'tab',
  props: {
    title: String,
  },
  data() {
    return {
      display: false,
      id: null,
    }
  },
  mounted() {
    console.log(this.id);
  }
}
</script>

<style lang="less">
.tab-content>.tab-pane {
  display: block;
}
</style>

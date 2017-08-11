<template>
  <div class="tab-pane" v-show="display" v-loading="loading">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: 'tab',
  props: {
    title: String
  },
  data () {
    return {
      display: false,
      id: null,
      loading: false
    };
  },
  watch: {
    display: 'navigationChanged'
  },
  methods: {
    navigationChanged (value) {
      if (value) {
        this.$emit('navigated', this.id, (value) => {
          this.loading = value;
        });
      }
    }
  }
};
</script>

<style lang="less">
.tab-content>.tab-pane {
  display: block;
}
</style>

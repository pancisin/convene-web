<template>
  <div>
    <ul class="nav nav-tabs navtab-custom nav-justified">
      <li class="tab" v-for="tab in tabs" :class="{ 'active' : selected.id == tab.id }" :key="tab.id">
        <a :class="{ 'active' : selected.id == tab.id }" @click="selected = tab">
          <span class="visible-xs">
            <i class="fa fa-home"></i>
          </span>
          <span class="hidden-xs" v-text="tab.title"></span>
        </a>
      </li>
      <div class="indicator"></div>
    </ul>
    <div class="tab-content">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'tab-container',
  data () {
    return {
      selected: null,
      tabs: []
    };
  },
  watch: {
    selected (val) {
      this.tabs.forEach(tab => {
        this.$set(tab, 'display', false);
      });

      this.$set(val, 'display', true);
    }
  },
  mounted () {
    this.tabs = this.$children;
    for (var i = 0; i < this.tabs.length; i++) {
      this.$set(this.tabs[i], 'id', i);
    }

    this.selected = this.tabs[0];
  }
};
</script>

<style lang="less">
.nav.nav-tabs+.tab-content {
  background: #ffffff;
  margin-bottom: 20px;
  padding: 20px;
}
</style>
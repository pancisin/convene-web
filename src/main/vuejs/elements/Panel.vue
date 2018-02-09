<template>
  <div class="panel" :class="`panel-${type}`">
    <div 
      class="panel-heading" 
      :class="{ 'pattern-background' : type === 'primary' }" 
      ref="header">

      <h3 class="panel-title">
        <slot name="title"></slot>
      </h3>
      <p class="panel-sub-title font-13 text-muted">
        <slot name="subtitle"></slot>
      </p>
    </div>
  
    <div 
      class="panel-body" 
      :style="bodyStyle">

      <slot></slot>
    </div>

    <div 
      class="panel-footer" 
      ref="footer" 
      v-if="$slots.footer">
      
      <slot name="footer"></slot>
    </div>
  </div>
</template>

<script>
import debounce from 'debounce';
export default {
  name: 'panel',
  props: ['type'],
  data () {
    return {
      bodyStyle: {}
    };
  },
  mounted () {
    if (this.$slots.footer) {
      this.updateBodyStyle();
      window.onresize = debounce(this.updateBodyStyle, 200);
    }
  },
  methods: {
    updateBodyStyle () {
      const headerHeight = this.$slots.title ? this.$refs.header.offsetHeight : 0;
      const footerHeight = this.$slots.footer ? this.$refs.footer.offsetHeight : 0;

      const height = this.$el.offsetHeight - (headerHeight + footerHeight);

      this.bodyStyle = {
        height: `${height}px`
      };
    }
  }
};
</script>
<template>
  <div class="masonry" :style="style">
    <slot>
    </slot>
  </div>
</template>

<script>
export default {
  name: 'masonry',
  props: {
    columns: {
      type: Number | String,
      default () {
        return 3;
      }
    }
  },
  data () {
    return {
      cols: 1
    };
  },
  created () {
    this.cols = this.columns;

    window.matchMedia('(min-width: 700px)').addListener((data) => {
      this.cols = 1;
    });

    window.matchMedia('(min-width: 900px)').addListener((data) => {
      this.cols = this.columns - 1;
    });

    window.matchMedia('(min-width: 1100px)').addListener((data) => {
      this.cols = this.columns;
    });
  },
  computed: {
    style () {
      return {
        '-moz-column-count': this.cols,
        '-webkit-column-count': this.cols,
        'column-count': this.cols
      };
    }
  }
};
</script>

<style lang="less">
.masonry {
  padding: 0;
  -moz-column-gap: 1.5em;
  -webkit-column-gap: 1.5em;
  column-gap: 1.5em;
}
</style>

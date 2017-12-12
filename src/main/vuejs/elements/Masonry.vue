<template>
  <div class="masonry" :style="style">
    <slot>
    </slot>
  </div>
</template>

<script>
import debounce from 'debounce';
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
  mounted () {

    const recalculate = (width) => {
      if (width > 1100) {
        this.cols = this.columns;
      } else if (width > 900) {
        this.cols = this.columns - 1;
      } else if (width > 600) {
        this.cols = 2;
      } else this.cols = 1;
    };

    window.onresize = debounce((event) => {
      recalculate(event.target.innerWidth);
    }, 200);

    recalculate(window.innerWidth);
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

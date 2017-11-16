<template>
  <div class="widget-simple-chart text-right card-box flex-widget">
    <h3 class="text-primary counter">4</h3>
    <p class="text-muted text-nowrap">Attendees</p>
  </div>
</template>

<script>
export default {
  name: 'followers-widget',
  props: {
    parent: {
      validator (value) { 
        return ['event', 'page', 'conference'].includes(value);
      },
      default() {
        return 'page';
      }
    }
  },
  data () {
    return {

    };
  },
  methods: {

  }
};
</script>

<style lang="less">
.flex-widget {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}
</style>

<template>
  <div class="widget-simple-chart text-right card-box flex-widget">
    <h3 class="text-primary counter">{{ conference.attendeesCount }}</h3>
    <p class="text-muted text-nowrap">Attendees</p>
  </div>
</template>

<script>
export default {
  name: 'followers-widget',
  props: ['conference'],
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

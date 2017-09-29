<template>
  <div>
    <i class="fa fa-check text-success" v-if="run.state.name === 'SUCCESS'"></i>
    <i class="fa fa-clock-o text-warning" v-else-if="run.state.name === 'RUNNING'"></i>
    <i class="fa fa-exclamation-triangle text-danger" v-else></i>

    <b v-if="run.state.name !== 'RUNNING'">{{ run.date | moment('L LT') }}</b>
    <b v-else>Running...</b>
  </div>
</template>

<script>
export default {
  name: 'bot-run-indicator',
  props: {
    run: {
      type: Object,
      default () {
        return {
          state: {
            name: 'UNKNOWN'
          }
        };
      }
    }
  },
};
</script>

<style>

</style>

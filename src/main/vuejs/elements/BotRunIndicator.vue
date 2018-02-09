<script>
import { DateTime } from 'luxon';

export default {
  name: 'bot-run-indicator',
  render (h) {
    var iconClass = '';

    if (this.run.state.prop === 'SUCCESS') {
      iconClass = 'fa-check text-success';
    } else if (this.run.state.prop === 'RUNNING') {
      iconClass = 'fa-clock-o text-warning';
    } else {
      iconClass = 'fa-exclamation-triangle text-danger';
    }

    return h('div', [
      h('i', {
        class: `fa ${iconClass}`
      }),
      h('b', this.run.state.prop === 'RUNNING' ? ' Running...' : DateTime.fromMillis(this.run.date).toFormat('fff'))
    ]);
  },
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
  }
};
</script>

<style>

</style>

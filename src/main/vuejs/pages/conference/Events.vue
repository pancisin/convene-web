<script>
import EventsTemplate from '../templates/Events.vue'
export default {
  name: 'conference-events',
  extends: EventsTemplate,
  props: ['conference'],
  inject: ['api'],
  watch: {
    conference() {
      this.getEvents(0);
    }
  },
  methods: {
    getEvents(page) {
      this.loading = true;
      this.api.getEvents(this.conference.id, page, 8, paginator => {
        this.paginator = paginator;
        this.loading = false;
      })
    }
  }
}
</script>
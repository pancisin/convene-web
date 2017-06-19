<script>
import EventsTemplate from '../templates/Events.vue'
export default {
  name: 'user-events',
  extends: EventsTemplate,
  props: ['page'],
  inject: ['api'],
  watch: {
    page() {
      this.getEvents(0);
    }
  },
  methods: {
    getEvents(page) {
      this.loading = true;
      this.api.getEvents(page, 8, paginator => {
        this.paginator = paginator;
        this.loading = false;
      })
    }
  }
}
</script>
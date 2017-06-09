<script>
import EventsTemplate from '../templates/Events.vue'
export default {
  name: 'conference-events',
  extends: EventsTemplate,
  props: ['conference'],
  watch: {
    conference() {
      this.getEvents(0);
    }
  },
  methods: {
    getEvents(page) {
      if (this.conference.id == null) return;

      this.loading = true;
      var size = 8;
      
      var url = ['api/conference', this.conference.id, 'event', page, size].join('/');

      this.$http.get(url).then(response => {
        this.paginator = response.body;
        this.loading = false;
      })
    }
  }
}
</script>
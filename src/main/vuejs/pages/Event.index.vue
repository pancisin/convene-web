<script>
import EventsTemplate from './templates/Events.vue'

export default {
  name: 'event-index',
  extends: EventsTemplate,
  methods: {
    getEvents(page) {
      this.loading = true;
      var size = 8;

      var url = ['api/user/event', page, size].join('/');
      
      this.$http.get(url).then(response => {
        this.loading = false;
        this.paginator = response.body;
      })
    },
  }
}
</script>
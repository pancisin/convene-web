<script>
import Events from '../templates/Events.vue'
export default {
  extends: Events,
  name: 'events-impl',
  props: ['page'],
  watch: {
    page() {
      this.getEvents(0);
    }
  },
  methods: {
    getEvents(page) {
      if (this.page.id == null) return;

      this.loading = true;
      var size = 8;

      var url = ['api/page', this.page.id, 'event', page, size].join('/');

      this.$http.get(url).then(response => {
        this.paginator = response.body;
        this.loading = false;
      });
    },
  }
}
</script>
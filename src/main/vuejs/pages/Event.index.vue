<script>
import EventsTemplate from './templates/Events.vue';
import UserApi from 'api/user.api';

export default {
  name: 'event-index',
  extends: EventsTemplate,
  methods: {
    getEvents (page) {
      this.loading = true;
      UserApi.getEvents(0, 8, paginator => {
        this.paginator = paginator;
        this.loading = false;
      });
    }
  }
};
</script>
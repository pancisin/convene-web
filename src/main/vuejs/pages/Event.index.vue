<script>
import EventsTemplate from './templates/Events.vue'
import UserApi from '../services/api/user.api.js'

export default {
  name: 'event-index',
  extends: EventsTemplate,
  provide() {
    return {
      api: UserApi
    }
  },
  methods: {
    getEvents(page) {
      this.loading = true;
      UserApi.getEvents(0, 8, paginator => {
        this.paginator = paginator;
        this.loading = false;
      })
    }
  }
}
</script>
<template>
  <panel type="table">
    <span slot="title">{{ $t('admin.page.events') }}</span>
  
    <table class="table table-striped">
      <thead>
        <tr>
          <th>Name</th>
          <th>Date</th>
          <th class="text-center">Action</th>
        </tr>
      </thead>
      <tbody is="transition-group" name="fade">
        <tr v-for="event in events" :key="event.id">
          <td>
            <router-link :to="'/admin/event/' + event.id">
              {{ event.name }}
            </router-link>
          </td>
          <td>{{ event.date | moment('DD.MM.YYYY') }}</td>
          <td class="text-center">
            <a @click="deleteEvent(event)" class="btn btn-rounded btn-xs btn-danger">
              <i class="fa fa-trash"></i>
            </a>
          </td>
        </tr>
      </tbody>
    </table>
  
    <div class="text-center">
      <router-link to="create-event" class="btn btn-default btn-rounded text-center">
        Create event
      </router-link>
    </div>
  </panel>
</template>

<script>
export default {
  name: 'conference-events',
  props: {
    conference: Object,
  },
  data() {
    return {
      events: [],
    }
  },
  watch: {
    'conference': 'getEvents',
  },
  created() {

  },
  methods: {
    getEvents() {
      var url = ['api/conference', this.conference.id, 'event'].join('/');

      this.$http.get(url).then(response => {
        this.events = response.body;
      })
    }
  }
}
</script>
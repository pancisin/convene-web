<template>
  <panel type="table">
    <span slot="title">Events</span>
  
    <table class="table">
      <thead>
        <tr>
          <th>Name</th>
          <th>Date</th>
        </tr>
      </thead>
      <tbody is="transition-group" name="fade">
        <tr v-for="event in events" :key="event.id">
          <td>
            <router-link :to="'/admin/event/' + event.id" v-text="event.name">
            </router-link>
          </td>
          <td>{{ event.date | moment('DD.MM.YYYY') }}</td>
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
  name: 'events',
  props: ['page'],
  data() {
    return {
      events: [],
    }
  },
  watch: {
    'page': 'getEvents',
  },
  created() {
    this.getEvents();
  },
  methods: {
    getEvents() {
      if (this.page.id == null) return;
      this.$http.get('api/page/' + this.page.id + '/event').then(response => {
        this.events = response.body;
      });
    },
    createEvent() {

    }
  }
}
</script>
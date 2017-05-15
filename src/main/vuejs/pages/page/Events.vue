<template>
  <div class="card-box">
    <table class="table table-bordered">
      <thead>
        <tr>
          <th>Name</th>
          <th>Date</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="event in events">
          <td>
            <router-link :to="'/admin/event/' + event.id" v-text="event.name">
            </router-link>
          </td>
          <td>{{ event.date | moment('DD.MM.YYYY') }}</td>
        </tr>
      </tbody>
    </table>
  
    <div class="text-center">
      <router-link to="create-event" class="btn btn-primary btn-rounded text-center">
        Create event
      </router-link>
    </div>
  </div>
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
  methods: {
    getEvents() {
      this.$http.get('api/page/' + this.page.id + '/event').then(response => {
        this.events = response.body;
      });
    },
    createEvent() {

    }
  }
}
</script>
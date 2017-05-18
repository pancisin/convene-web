<template>
  <panel type="table">
    <span slot="title">My events</span>
  
    <table class="table">
      <thead>
        <tr>
          <th>name</th>
          <th class="text-center">date</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="event in events">
          <td>
            <router-link :to="'event/' + event.id">
              {{ event.name }}
            </router-link>
          </td>
          <td class="text-center">{{ event.date | moment('DD.MM.YYYY') }}</td>
        </tr>
      </tbody>
    </table>
  </panel>
</template>

<script>
export default {
  name: 'event-index',
  data() {
    return {
      events: [],
    }
  },
  created: function () {
    this.getEvents();
  },
  methods: {
    getEvents() {
      this.$http.get('api/user/event').then(response => {
        this.events = response.body;
      })
    }
  }
}
</script>
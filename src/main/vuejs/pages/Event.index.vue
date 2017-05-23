<template>
  <div class="row">
    <div class="col-md-4">
      <panel type="table">
        <span slot="title" @click="">My events</span>
  
        <table class="table table-striped">
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
            <tr v-if="events.length == 0">
              <td colspan="2" class="text-center">There's nothing to display.</td>
            </tr>
          </tbody>
        </table>
  
        <div class="text-center">
          <router-link to="/admin/event/create" class="btn btn-default btn-rounded text-center">
            Create event
          </router-link>
        </div>
      </panel>
    </div>
  </div>
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
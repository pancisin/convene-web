<template>
  <panel type="table">
    <span slot="title">Events</span>
  
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
            <router-link :to="'/admin/event/' + event.id" v-text="event.name">
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
    deleteEvent(event) {
      this.$http.delete('api/event/' + event.id).then(response => {
        this.events = this.events.filter(e => {
          return event.id != e.id;
        })
      })
    }
  }
}
</script>
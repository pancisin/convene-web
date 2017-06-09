<template>
  <panel type="table" v-loading="loading">
    <span slot="title">{{ $t('admin.page.events') }}</span>
  
    <table class="table table-striped">
      <thead>
        <tr>
          <th>Name</th>
          <th>Date</th>
        </tr>
      </thead>
      <tbody is="transition-group" name="fade">
        <tr v-for="ev in events" :key="ev.id" @contextmenu.prevent="$refs.menu.open($event, ev)">
          <td>
            <router-link :to="'/admin/event/' + ev.id" v-text="ev.name">
            </router-link>
          </td>
          <td>{{ ev.date | moment('DD.MM.YYYY') }}</td>
        </tr>
      </tbody>
    </table>
  
    <context-menu ref="menu">
      <template scope="props">
        <ul>
          <li>
            <router-link :to="{ name: 'event.public', params: { id: props.data.id } }">
              Go to event
            </router-link>
          </li>
          <li class="separator"></li>
          <li>
            <router-link :to="{ name: 'event.overview', params: { id: props.data.id } }">
              Overview
            </router-link>
          </li>
          <li>
            <router-link :to="{ name: 'event.programme', params: { id: props.data.id } }">
              Programme
            </router-link>
          </li>
          <li>
            <router-link :to="{ name: 'event.attendees', params: { id: props.data.id } }">
              Attendees
            </router-link>
          </li>
          <li class="separator"></li>
          <li>
            <a @click="deleteEvent(props.data)">
              Delete
            </a>
          </li>
        </ul>
      </template>
    </context-menu>
  
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
      loading: false,
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
      this.loading = true;
      this.$http.get('api/page/' + this.page.id + '/event').then(response => {
        this.events = response.body;
        this.loading = false;
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
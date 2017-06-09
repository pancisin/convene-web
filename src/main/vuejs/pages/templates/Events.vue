<template>
  <panel type="table" v-loading="loading">
    <span slot="title">{{ $t('admin.page.events') }}</span>
  
    <table class="table table-striped">
      <thead>
        <tr>
          <th>Name</th>
          <th>Date</th>
          <th>Created</th>
          <th>Place</th>
        </tr>
      </thead>
      <tbody is="transition-group" name="fade">
        <tr v-for="ev in paginator.content" :key="ev.id" @contextmenu.prevent="$refs.menu.open($event, ev)">
          <td>
            <router-link :to="'/admin/event/' + ev.id" v-text="ev.name">
            </router-link>
          </td>
          <td>{{ ev.date | moment('DD.MM.YYYY') }}</td>
          <td>{{ ev.created | moment('DD.MM.YYYY') }}</td>
          <td>
            <span v-if="ev.place != null" v-text="ev.place.name">
            </span>
          </td>
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
import Paginator from '../../elements/Paginator.vue'

export default {
  name: 'events',
  data() {
    return {
      paginator: {},
      loading: false,
    }
  },
  components: {
    Paginator
  },
  created() {
    this.getEvents(0);
  },
  methods: {
    deleteEvent(event) {
      this.$http.delete('api/event/' + event.id).then(response => {
        this.paginator.content = this.paginator.content.filter(e => {
          return e.id != event.id;
        })
      })
    },
    paginatorNavigate(e) {
      if (e.direction != null) {
        this.getEvents(this.paginator.number + e.direction)
      } else if (e.page != null) {
        this.getEvents(e.page);
      }
    },
  }
}
</script>
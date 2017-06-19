<template>
  <panel type="table" v-loading="loading">
    <span slot="title">{{ $t('admin.page.events') }}</span>
  
    <table class="table table-striped table-hover">
      <thead>
        <tr>
          <th>Name</th>
          <th>Date</th>
          <th>Created</th>
          <th>Place</th>
          <th class="text-center">Attendees</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="ev in paginator.content" :key="ev.id" @contextmenu.prevent="$refs.menu.open($event, ev)">
          <td>
            <router-link :to="'/admin/event/' + ev.id" v-text="ev.name">
            </router-link>
          </td>
          <td>{{ ev.date | moment('L') }} {{ ev.startsAt }}</td>
          <td>{{ ev.created | moment('L') }}</td>
          <td>
            <span v-if="ev.place != null" v-text="ev.place.name">
            </span>
          </td>
          <td v-text="ev.attendeesCount" class="text-center"></td>
        </tr>
        <tr v-if="paginator.content && paginator.content.length == 0">
          <td colspan="5" class="text-center">
            There's nothing to display
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
            <router-link to="events/create">
              Create event
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
      <paginator :history="true" :paginator="paginator" @navigate="paginatorNavigate" />
    </div>
  
    <div class="text-center">
      <router-link to="events/create" class="btn btn-default btn-rounded text-center">
        Create event
      </router-link>
    </div>
  </panel>
</template>

<script>
import Paginator from '../../elements/Paginator.vue'

export default {
  name: 'events',
  inject: ['api'],
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
    try {
      this.getEvents(0);
    } catch (ex) {
      
    }
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
    getEvents(page) {
      this.loading = true;
      this.api.getEvents(page, 8, events => {
        this.loading = false;
        this.events = events;
      })
    }
  }
}
</script>
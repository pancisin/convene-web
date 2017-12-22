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
            <router-link :to="{ name: 'event', params: { id: ev.id } }" v-text="ev.name">
            </router-link>
          </td>
          <td>{{ ev.date | luxon('D') }} {{ ev.startsAt }}</td>
          <td>{{ ev.created | luxon('F') }}</td>
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
      <template slot-scope="props">
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
          <li :class="{ 'disabled' : !editable }">
            <router-link to="events/create">
              Create event
            </router-link>
          </li>
          <li class="separator"></li>
          <li :class="{ 'disabled' : !editable }">
            <a @click="deleteEvent(props.data)">
              Delete
            </a>
          </li>
        </ul>
      </template>
    </context-menu>

    <div class="text-center">
      <paginator :history="true" :paginator="paginator" :fetch="getEvents"></paginator>
    </div>

    <div class="text-center" v-if="editable">
      <a @click="displayEventCreateModal = true" class="btn btn-default btn-rounded text-center">
        Create event
      </a>
    </div>

    <modal :show.sync="displayEventCreateModal">
      <span slot="header">Create an event</span>
      <div slot="body">
        <event-create-wizard 
          :postFunc="postEvent" 
          @success="updateContent" 
          v-if="displayEventCreateModal"
        ></event-create-wizard>
      </div>
    </modal>
  </panel>
</template>

<script>
import {
  Paginator,
  EventCreateWizard
} from 'elements';
import EventApi from 'api/event.api';

export default {
  name: 'events',
  inject: ['provider'],
  props: {
    editable: Boolean
  },
  data () {
    return {
      paginator: {},
      loading: false,
      displayEventCreateModal: false
    };
  },
  components: {
    Paginator,
    EventCreateWizard
  },
  computed: {
    api () {
      if (this.provider != null) {
        return this.provider.api;
      }
    }
  },
  created () {
    try {
      this.initialize();
    } catch (ex) {

    }
  },
  watch: {
    'api': 'initialize'
  },
  methods: {
    initialize () {
      this.getEvents(0);
    },
    deleteEvent (event) {
      this.$prompt('notification.event.delete_prompt', () => {
        EventApi.deleteEvent(event.id, result => {
          this.paginator.content = this.paginator.content.filter(e => {
            return e.id !== event.id;
          });
        });
      });
    },
    getEvents (page) {
      if (this.api != null) {
        this.loading = true;
        this.api.getEvents(page, 8, paginator => {
          this.paginator = paginator;
          this.loading = false;
        });
      }
    },
    postEvent (event, success) {
      this.api.postEvent(event, success);
    },
    updateContent (event) {
      this.paginator.content.push(event);
      this.displayEventCreateModal = false;
    }
  }
};
</script>
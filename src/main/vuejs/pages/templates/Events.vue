<template>
  <panel type="table" v-loading="loading">
    <span slot="title">{{ $t('admin.page.events') }}</span>

    <vue-table :func="tableRender" :data="paginator.content" :contextmenu="contextMenuRender" />

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
  EventCreateWizard,
  VueTable
} from 'elements';
import EventApi from 'api/event.api';
import { DateTime } from 'luxon';

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
    EventCreateWizard,
    VueTable
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
    tableRender (event, index) {
      return {
        name: {
          el: 'a',
          content: event.name,
          onClick: () => {
            this.$router.push({ name: 'event', params: { id: event.id } });
          }
        },
        date: DateTime.fromMillis(event.date).toFormat('D'),
        created: DateTime.fromMillis(event.created).toFormat('F'),
        status: {
          el: 'i',
          colClass: 'text-center',
          class: `fa ${ event.state === 'PUBLISHED' ? 'fa-check text-success' : 'fa-eye-slash text-warning' }`
        },
        attendees: event.attendeesCount
      };
    },
    contextMenuRender (item) {
      return [
        item('Go to event', event => this.$router.push({ name: 'event.public', params: { id: event.id } })),
        item('Overview', event => this.$router.push({ name: 'event.overview', params: { id: event.id } })),
        item('Programme', event => this.$router.push({ name: 'event.programme', params: { id: event.id } })),
        item('Attendees', event => this.$router.push({ name: 'event.attendees', params: { id: event.id } })),
        item('Create event', () => { this.displayEventCreateModal = true; }),
        item('Delete', event => this.deleteEvent(event))
      ];
    },
    initialize () {
      this.getEvents(0);
    },
    deleteEvent (event) {
      this.$prompt('notification.delete_prompt', event.name, () => {
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
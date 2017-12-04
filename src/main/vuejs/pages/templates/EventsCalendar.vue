<template>
  <div class="row">
    <div class="col-xs-12 col-lg-8 col-lg-offset-2">
      <panel type="primary" v-loading="loading">
        <span slot="title">Events calendar</span>
        <calendar 
          :events="paginator.content" 
          @navigate="getEvents" 
          ref="calendar" 
          :editable="editable"
          @selectDate="selectDate"></calendar>
      </panel>
    </div>

    <modal :show.sync="displayEventCreateModal">
      <span slot="header">Create an event</span>
      <div slot="body">
        <event-create-wizard 
          :postFunc="postEvent" 
          @success="updateContent" 
          :date="date"
          v-if="displayEventCreateModal"
        ></event-create-wizard>
      </div>
    </modal>
  </div>
</template>

<script>
import {
  Calendar,
  EventCreateWizard
} from 'elements';
import EventApi from 'api/event.api';

export default {
  name: 'events-calendar',
  inject: ['provider'],
  props: {
    editable: Boolean
  },
  data () {
    return {
      paginator: {},
      loading: false,
      displayEventCreateModal: false,
      date: null
    };
  },
  components: {
    Calendar,
    EventCreateWizard
  },
  computed: {
    api () {
      if (this.provider != null) {
        return this.provider.api;
      }
    }
  },
  watch: {
    api () {
      this.$refs.calendar.navigate();
    }
  },
  methods: {
    deleteEvent (event) {
      this.$prompt('notification.event.delete_prompt', () => {
        EventApi.deleteEvent(event.id, result => {
          this.paginator.content = this.paginator.content.filter(e => {
            return e.id !== event.id;
          });
        });
      });
    },
    getEvents ({ from, to }) {
      if (this.api != null) {
        this.loading = true;
        this.api.getEvents(0, 1000, paginator => {
          this.paginator = paginator;
          this.loading = false;
        }, {
          from,
          to
        });
      }
    },
    postEvent (event, success) {
      this.api.postEvent(event, success);
    },
    selectDate (timestamp) {
      this.date = timestamp;
      this.displayEventCreateModal = true;
    },
    updateContent (event) {
      this.paginator.content.push(event);
      this.displayEventCreateModal = false;
    }
  }
};
</script>

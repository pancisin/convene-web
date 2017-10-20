<template>
  <div class="row">
    <div class="col-xs-12 col-lg-8 col-lg-offset-2">
      <panel type="primary" v-loading="loading">
        <span slot="title">Events calendar</span>
        <calendar :events="paginator.content" @navigate="getEvents"></calendar>
      </panel>
    </div>
  </div>
</template>

<script>
import { Calendar } from 'elements';
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
      loading: false
    };
  },
  components: {
    Calendar
  },
  computed: {
    api () {
      if (this.provider != null) {
        return this.provider.api;
      }
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
    }
  }
};
</script>

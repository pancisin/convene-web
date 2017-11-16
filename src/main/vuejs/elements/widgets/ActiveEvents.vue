<template>
  <panel type="primary" v-loading="loading">
    <span slot="title">
      {{ title }}
    </span>

    <events-list :events="events" administrator></events-list>
  </panel>
</template>

<script>
import EventsList from '../EventsList';
export default {
  name: 'active-events',
  props: ['title'],
  inject: ['provider'],
  data () {
    return {
      events: [],
      loading: false
    };
  },
  watch: {
    'api': 'getActiveEvents'
  },
  components: {
    EventsList
  },
  computed: {
    api () {
      if (this.provider != null) {
        return this.provider.api;
      }
    }
  },
  created () {
    this.getActiveEvents();
  },
  methods: {
    getActiveEvents () {
      this.loading = true;
      this.api.getEvents(0, 10, paginator => {
        this.events = paginator.content;
        this.loading = false;
      });
    }
  }
};
</script>

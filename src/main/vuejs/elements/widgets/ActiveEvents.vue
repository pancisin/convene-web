<template>
  <panel type="primary" v-loading="loading">
    <span slot="title">
      {{ title }}
    </span>

    <div class="inbox-widget">
      <router-link :to="{ name: 'event', params: { id: event.id } }" v-for="(event, index) in events" :key="event.id" :data-index="index" class="inbox-item clearfix">
        <div class="inbox-item-img" v-if="event.poster != null">
          <img :src="event.poster.path" class="img-circle" alt="">
        </div>
        <p class="inbox-item-author" v-text="event.name"></p>
        <p class="inbox-item-text clearfix" v-if="event.summary != null" v-strip="event.summary.substr(0, 200)"></p>
        <p class="inbox-item-date">{{ event.date | moment('L') }}</p>
      </router-link>
    </div>
  </panel>
</template>

<script>
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

<style>

</style>

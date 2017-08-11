<template>
  <div class="">
    <ul>
      <li v-for="event in events" :key="event.id">
        {{ event.name }} - {{ event.startsAt }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'events-list',
  inject: ['api'],
  data () {
    return {
      events: []
    };
  },
  created () {
    this.api.getEvents(0, 100, events => {
      this.events = events.content;
    });
  },
  methods: {

  }
};
</script>

<style>

</style>

<template>
  <div class="inbox-widget">
    <router-link 
      :to="{ name: administrator ? 'event' : 'event.public', params: { id: event.id }}" 
      v-for="(event, index) in events" :key="event.id" 
      class="inbox-item">

      <div class="inbox-item-img" v-if="event.poster != null">
        <img :src="event.poster.path" class="img-circle" alt="">
      </div>
      
      <p class="inbox-item-author" v-text="event.name"></p>
      <p class="inbox-item-text">
        {{ event.date | moment('L') }} {{ event.startsAt }}
      </p>
    </router-link>
  </div>
</template>

<script>
export default {
  name: 'events-list',
  props: {
    events: {
      type: Array,
      default: []
    },
    administrator: {
      type: Boolean,
      default () {
        return false;
      }
    }
  },
  created () {
  }
};
</script>

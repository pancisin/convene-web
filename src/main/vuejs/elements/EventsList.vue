<template>
  <div class="inbox-widget">
    <router-link 
      :to="{ name: administrator ? 'event' : 'event.public', params: { id: event.id }}" 
      v-for="(event, index) in events" :key="index" 
      class="inbox-item">

      <div class="inbox-item-img" v-if="event.poster != null">
        <vue-image :src="event.poster.path" class="img-circle" />
      </div>
      
      <div>
        <p class="inbox-item-author" v-text="event.name"></p>
        <p class="inbox-item-text">
          {{ event.date | luxon('D') }} {{ event.startsAt }}
        </p>
      </div>
    </router-link>

    <div v-if="events.length == 0">
      There's nothing to display.
    </div>
  </div>
</template>

<script>
import VueImage from './VueImage';
export default {
  name: 'events-list',
  props: {
    events: {
      type: Array,
      default () {
        return [];
      }
    },
    administrator: {
      type: Boolean,
      default () {
        return false;
      }
    }
  },
  components: {
    VueImage
  }
};
</script>
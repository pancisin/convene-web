<template>
  <div class="inbox-widget">
    <stagger-transition>
      <router-link :to="{ name: 'event.public', params: { id: event.id }}" v-for="(event, index) in events" :key="event.id" :data-index="index" class="inbox-item">
        <div class="inbox-item-img" v-if="event.poster != null">
          <img :src="event.poster.path" class="img-circle" alt="">
        </div>
        <p class="inbox-item-author" v-text="event.name"></p>
        <p class="inbox-item-text">
          {{ event.date | moment('L') }} {{ event.startsAt }}
        </p>
      </router-link>
    </stagger-transition>
  </div>
</template>

<script>
import StaggerTransition from '../functional/StaggerTransition.vue';

export default {
  name: 'events-list',
  props: {
    events: {
      type: Array,
      default: []
    }
  },
  components: {
    StaggerTransition,
  },
  created() {
    console.log(this.events);
  },
}
</script>

<style>

</style>

<template>
  <dashboard v-if="authenticated">
  </dashboard>
  <landing v-else>
  </landing>
</template>

<script>
import Dashboard from './Dashboard.vue';
import Landing from './Landing.vue';
import { mapGetters } from 'vuex';

export default {
  name: 'home',
  components: {
    Dashboard, Landing
  },
  computed: {
    ...mapGetters(['authenticated'])
  }
};
</script>

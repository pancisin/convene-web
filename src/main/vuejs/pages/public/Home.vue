<template>
  <component :is="currentView">
  </component>
</template>

<script>
import Dashboard from './Dashboard.vue';
import Landing from './Landing.vue';
import { mapGetters } from 'vuex';

export default {
  name: 'home',
  data () {
    return {
      currentView: 'landing'
    };
  },
  components: {
    Dashboard, Landing
  },
  computed: {
    ...mapGetters(['authenticated'])
  },
  created () {
    if (this.authenticated) {
      this.currentView = 'dashboard';
    }
  }
};
</script>

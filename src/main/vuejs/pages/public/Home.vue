<template>
  <component :is="currentView">
  </component>
</template>

<script>
import Dashboard from './Dashboard.vue';
import Landing from './Landing.vue';
import Auth from '../../services/auth.js';
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
  created () {
    if (Auth.user.authenticated) {
      this.currentView = 'dashboard';
    }
  }
};
</script>

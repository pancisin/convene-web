// CORE
import Vue from 'vue'
import VueResource from 'vue-resource'

// GLOBAL COMPONENTS
import VueMoment from 'vue-moment'
import vSelect from "vue-select"

// SERVICES
import Auth from './services/auth.js'
import store from './services/store.js'
import router from './services/router.js'

Vue.use(VueResource);
Vue.use(VueMoment);
Vue.component('v-select', vSelect);

Vue.config.devtools = true;
// Vue.http.options.root = 'http://104.251.219.31:8080/employger';
Vue.http.options.root = 'http://localhost:8180'
Vue.http.headers.common['Authorization'] = Auth.getAuthHeader();

import App from './components/App.vue';

const app = new Vue({
  el: '#application',
  store,
  router,
  created: function () {
    this.initialize();
  },
  methods: {
    initialize: function () {
      this.$http.get('api/user/me').then(response => {
        var user = response.body;
        this.$store.commit('setUser', { user });
        this.$emit('user-loaded', user.id);
      }, response => {
      });
    },
  },
  render: h => h(App)
})
// CORE
import Vue from 'vue'
import VueResource from 'vue-resource'

// GLOBAL COMPONENTS
import VueMoment from 'vue-moment'
import vSelect from "vue-select"
import Modal from './elements/Modal.vue'

// SERVICES
import Auth from './services/auth.js'
import store from './services/store.js'
import router from './services/router.js'

Vue.use(VueResource);
Vue.use(VueMoment);
Vue.component('v-select', vSelect);
Vue.component('modal', Modal)

Vue.config.devtools = true;
Vue.http.options.root = 'http://104.251.219.31:8080/employger'
// Vue.http.options.root = 'http://localhost:8180'

Vue.http.interceptors.push((request, next) => {
  request.headers.set('Authorization', 'Bearer ' + Auth.getAuthHeader());

  next(response => {
    if (response.status == 401) {
      Auth.logout();
    }
  });
});

import App from './components/App.vue';

const app = new Vue({
  el: '#application',
  store,
  router,
  created: function () {
    if (Auth.user.authenticated)
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
// CORE
import Vue from 'vue'
import VueResource from 'vue-resource'
import VueI18n from 'vue-i18n'
import VueWebsocket from './services/websocket.js'

// GLOBAL COMPONENTS
import VueMoment from 'vue-moment'
import vSelect from "vue-select"
import Modal from './elements/Modal.vue'

// SERVICES
import Auth from './services/auth.js'
import store from './services/store.js'
import router from './services/router.js'


// LOAD STYLES
require("bootstrap")
require("node-waves")

import 'bootstrap/dist/css/bootstrap.min.css';

require("./assets/less/core.less")
require("./assets/less/components.less")
require("./assets/less/menu.less")
require("./assets/less/pages.less")
require("./assets/less/responsive.less")
require("./assets/less/custom.less")

// TRANSLATIONS
import sk from './locale/sk-SK.js'
import en from './locale/en-US.js'

Vue.use(VueResource)
Vue.use(VueI18n)
Vue.use(VueWebsocket);
Vue.use(VueMoment)
Vue.component('v-select', vSelect)
Vue.component('modal', Modal)

Vue.config.devtools = true;

Vue.http.interceptors.push((request, next) => {
  request.headers.set('Authorization', 'Bearer ' + Auth.getAuthHeader());

  next(response => {
    if (response.status == 401) {
      Auth.logout();
    }
  });
});

const i18n = new VueI18n({
  locale: 'sk',
  fallbackLocale: 'en',
  messages: {
    sk, en
  }
})

import App from './components/App.vue';

const app = new Vue({
  el: '#application',
  store,
  router,
  i18n,
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
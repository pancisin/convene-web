// CORE
import Vue from 'vue'
import VueResource from 'vue-resource'
import VueI18n from 'vue-i18n'
import VueWebsocket from './services/websocket.js'

// GLOBAL COMPONENTS
import VueMoment from 'vue-moment'
import Modal from './elements/Modal.vue'

require('font-awesome/css/font-awesome.css')

// SERVICES
import Auth from './services/auth.js'
import store from './services/store.js'
import router from './services/router.js'
import notifier from './services/notifier.js'

// LOAD STYLES
require("bootstrap")
require("node-waves")

import 'bootstrap/dist/css/bootstrap.min.css';

require("./assets/less/core.less")
require("./assets/less/components.less")
require("./assets/less/menu.less")
// require("./assets/less/pages.less")
require("./assets/less/responsive.less")

// TRANSLATIONS
import sk from './locale/sk-SK.js'
import en from './locale/en-US.js'

Vue.use(VueResource)
Vue.use(VueI18n)
Vue.use(VueWebsocket)
Vue.use(VueMoment)
Vue.use(notifier)
Vue.component('modal', Modal)

// DIRECTIVES
import clickOutside from './directives/clickOutside.js'
Vue.directive('click-outside', clickOutside);

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
  locale: 'en',
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
  render: h => h(App)
})
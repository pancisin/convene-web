// CORE
import Vue from 'vue'
import VueResource from 'vue-resource'
import VueI18n from 'vue-i18n'
import VueWebsocket from './services/websocket.js'
import VueAnalytics from 'vue-analytics'

// GLOBAL COMPONENTS
import VueMoment from 'vue-moment'
import Modal from './elements/Modal.vue'
import Panel from './elements/Panel.vue'
import ContextMenu from './elements/ContextMenu.vue'

require('font-awesome/css/font-awesome.css')

// SERVICES
import Auth from './services/auth.js'
import store from './services/store.js'
import router from './services/router.js'
import notifier from './services/notifier.js'

// LOAD STYLES
require("bootstrap")
import Waves from 'node-waves'
Waves.init();

import 'bootstrap/dist/css/bootstrap.min.css';

require("./assets/less/core.less")
require("node-waves/src/less/waves.less")
require("./assets/less/components.less")
require("./assets/less/responsive.less")

// TRANSLATIONS
import sk from './locale/sk-SK.js'
import en from './locale/en-US.js'

Vue.use(VueResource)
Vue.use(VueI18n)
Vue.use(VueWebsocket)
Vue.use(VueMoment)
Vue.use(notifier)
Vue.use(VueAnalytics, {
  id: 'UA-100879912-1',
  router
})
Vue.component('modal', Modal)
Vue.component('panel', Panel)
Vue.component('context-menu', ContextMenu)

// DIRECTIVES
import clickOutside from './directives/clickOutside.js'
import stripTags from './directives/stripTags.js'
import loading from './directives/loading.js'
Vue.directive('click-outside', clickOutside);
Vue.directive('strip', stripTags);
Vue.directive('loading', loading)

Vue.config.devtools = true;

Vue.http.interceptors.push((request, next) => {
  request.headers.set('Authorization', 'Bearer ' + Auth.getAuthHeader());

  next(response => {
    if (response.status == 401) {
      Auth.logout();
      router.push('/login')
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
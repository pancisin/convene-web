// CORE
import Vue from 'vue';
import VueResource from 'vue-resource';
import VueI18n from 'vue-i18n';
import VueWebsocket from './services/websocket.js';
import VueAnalytics from 'vue-analytics';
import VeeValidate from 'vee-validate';
import MetaManager from './services/meta.manager';
import GoogleMapsApi from './services/google-maps-api';
import FacebookApi from './services/facebook-api';

// GLOBAL COMPONENTS
import VueMoment from 'vue-moment';
import { Modal, Panel, ContextMenu } from 'elements';
import Fullscreen from 'vue-fullscreen';

import 'font-awesome/css/font-awesome.css';

// SERVICES
import store from 'store/index.js';
import router from './services/router.js';
import notifier from './services/notifier.js';

// LOAD STYLES
import 'bootstrap/dist/css/bootstrap.min.css';

import Waves from 'node-waves';
Waves.init();

import 'assets/less/core.less';
import 'assets/less/components.less';
import 'assets/less/responsive.less';
import 'node-waves/src/less/waves.less';

Vue.use(MetaManager);
Vue.use(FacebookApi);
Vue.use(GoogleMapsApi);
Vue.use(VueResource);
Vue.use(VueI18n);
Vue.use(VueWebsocket);
Vue.use(VueMoment);
Vue.use(notifier);
Vue.use(VueAnalytics, {
  id: 'UA-100879912-1',
  router
});
Vue.use(VeeValidate);
Vue.use(Fullscreen);
Vue.component('modal', Modal);
Vue.component('panel', Panel);
Vue.component('context-menu', ContextMenu);

// DIRECTIVES
import { ClickOutside, StripTags, Loading } from './directives';
Vue.directive('click-outside', ClickOutside);
Vue.directive('strip', StripTags);
Vue.directive('loading', Loading);

import { DateTime } from 'luxon';
Vue.filter('luxon', (value, arg) => {
  return DateTime.fromMillis(value, {
    zone: 'utc'
  }).toLocal().toFormat(arg);
});

Vue.config.devtools = true;
Vue.config.productionTip = false;

Vue.http.interceptors.push((request, next) => {
  const token = window.localStorage.getItem('id_token') || window.sessionStorage.getItem('id_token');

  request.headers.set('Authorization', 'Bearer ' + token);
  next(response => {
    if (response.status === 401) {
      // should be logged out or something...
    }
  });
});

// TRANSLATIONS
import translations from './locale';

const i18n = new VueI18n({
  locale: navigator.language || 'en',
  fallbackLocale: 'en',
  messages: translations
});

import App from './components/App.vue';

const app = new Vue({
  el: '#application',
  store,
  router,
  i18n,
  render: h => h(App)
});

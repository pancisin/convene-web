// CORE
import Vue from 'vue';
import VueResource from 'vue-resource';
import VueI18n from 'vue-i18n';
import VueWebsocket from './services/websocket.js';
import VueAnalytics from 'vue-analytics';
import VeeValidate from 'vee-validate';
import MetaManager from './services/meta.manager';

// GLOBAL COMPONENTS
import VueMoment from 'vue-moment';
import { Modal, Panel, ContextMenu } from 'elements';
import Fullscreen from 'vue-fullscreen';

require('font-awesome/css/font-awesome.css');

// SERVICES
import store from 'store/index.js';
import router from './services/router.js';
import notifier from './services/notifier.js';

// LOAD STYLES
require('bootstrap');
import Waves from 'node-waves';
Waves.init();

import 'bootstrap/dist/css/bootstrap.min.css';

require('./assets/less/core.less');
require('node-waves/src/less/waves.less');
require('./assets/less/components.less');
require('./assets/less/responsive.less');

Vue.use(MetaManager);
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

Vue.config.devtools = true;

Vue.http.interceptors.push((request, next) => {
  request.headers.set('Authorization', 'Bearer ' + window.localStorage.getItem('id_token'));
  next(response => {
    if (response.status === 401) {
      // should be logged out or something...
    }
  });
});

// TRANSLATIONS
import * as translations from './locale';

const i18n = new VueI18n({
  locale: 'en',
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

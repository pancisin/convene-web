import store from 'store/index';
import * as AdminRoutes from './admin';

export default [
  {
    path: 'dashboard',
    name: 'admin.dashboard',
    component: resolve => require(['pages/Dashboard.vue'], resolve),
    meta: {
      title: 'Dashboard'
    }
  },
  {
    path: 'event',
    name: 'admin.event',
    component: resolve => require(['pages/Event.index.vue'], resolve),
    meta: {
      title: 'Events'
    }
  },
  {
    path: 'pages',
    name: 'admin.pages',
    component: resolve => require(['pages/Pages.vue'], resolve),
    meta: {
      title: 'Pages'
    }
  },
  {
    path: 'media-manager',
    name: 'admin.media-manager',
    component: resolve => require(['pages/MediaManager.vue'], resolve),
    meta: {
      title: 'Media manager'
    }
  },
  {
    path: 'users',
    name: 'system.users',
    component: resolve => require(['pages/system/Users.vue'], resolve)
  },
  {
    path: 'maintenance',
    name: 'system.maintenance',
    component: resolve => require(['pages/system/Maintenance.vue'], resolve)
  },
  {
    path: 'page-import',
    name: 'system.page-import',
    component: resolve => require(['pages/system/PageImport.vue'], resolve)
  },
  {
    path: 'conference/create',
    component: resolve => require(['pages/Conference.create.vue'], resolve),
    beforeEnter: (to, from, next) => {
      var license = store.getters.license;
      if (license && license.subscription.conferenceLimit > store.getters.conferences.length) {
        next();
      } else {
        next('/pricing');
      }
    },
    meta: {
      title: 'Create conference'
    }
  },
  {
    path: 'page/create',
    component: resolve => require(['pages/Page.create.vue'], resolve),
    beforeEnter: (to, from, next) => {
      var license = store.getters.license;
      if (license && license.subscription.pageLimit > store.getters.pages.length) {
        next();
      } else {
        next('/pricing');
      }
    },
    meta: {
      titlle: 'Create page'
    }
  },
  ...Object.values(AdminRoutes),
  {
    path: 'event-bot/:bot_id',
    component: resolve => require(['pages/EventBot.vue'], resolve),
    name: 'event-bot'
  },
  {
    path: 'notifications',
    name: 'admin.notification',
    component: resolve => require(['pages/Notifications.vue'], resolve),
    meta: {
      titlle: 'Notifications'
    }
  },
  {
    path: 'lists',
    component: resolve => require(['pages/ArticlesLists.vue'], resolve),
    name: 'system.lists'
  },
  {
    path: '*',
    component: resolve => require(['pages/error/404.vue'], resolve)
  }
];

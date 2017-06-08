import store from './store.js'
export default [
  {
    path: 'dashboard',
    component: resolve => require(['../pages/Dashboard.vue'], resolve)
  },
  {
    path: 'event',
    component: resolve => require(['../pages/Event.index.vue'], resolve)
  },
  {
    path: 'conference/create',
    component: resolve => require(['../pages/conference/Overview.vue'], resolve),
    beforeEnter: (to, from, next) => {
      var license = store.getters.getLicense;
      if (license && license.subscription.conferenceLimit > store.getters.getConferences.length)
        next();
      else
        next('/pricing');
    }
  },
  {
    path: 'conference/:id',
    component: resolve => require(['../pages/Conference.vue'], resolve),
    redirect: '/admin/conference/:id/overview',
    children: [
      {
        path: 'overview',
        name: 'conference.overview',
        component: resolve => require(['../pages/conference/Overview.vue'], resolve)
      },
      {
        path: 'events',
        name: 'conference.events',
        component: resolve => require(['../pages/conference/Events.vue'], resolve)
      },
      {
        path: 'create-event',
        component: resolve => require(['../pages/event/Overview.vue'], resolve),
        props: (route) => (
          {
            conference_id: route.params.id,
            edit: false
          }
        )
      }
    ]
  },
  {
    path: 'event/create',
    component: resolve => require(['../pages/event/Overview.vue'], resolve)
  },
  {
    path: 'event/:id',
    component: resolve => require(['../pages/Event.vue'], resolve),
    redirect: '/admin/event/:id/overview',
    children: [
      {
        path: 'overview',
        component: resolve => require(['../pages/event/Overview.vue'], resolve)
      },
      {
        path: 'programme',
        component: resolve => require(['../pages/event/Programme.vue'], resolve)
      },
      {
        path: 'attendees',
        component: resolve => require(['../pages/event/Attendees.vue'], resolve)
      }
    ]
  },
  {
    path: 'page/create',
    component: resolve => require(['../pages/page/Compose.vue'], resolve),
    beforeEnter: (to, from, next) => {
      var license = store.getters.getLicense;
      if (license && license.subscription.pageLimit > store.getters.getPages.length)
        next();
      else
        next('/pricing');
    }
  },
  {
    path: 'page/:id',
    component: resolve => require(['../pages/Page.vue'], resolve),
    redirect: '/admin/page/:id/overview',
    children: [
      {
        path: 'overview',
        name: 'page.overview',
        component: resolve => require(['../pages/page/Overview.vue'], resolve)
      },
      {
        path: 'settings',
        name: 'page.settings',
        component: resolve => require(['../pages/page/Compose.vue'], resolve),
      },
      {
        path: 'events',
        name: 'page.events',
        component: resolve => require(['../pages/page/Events.vue'], resolve)
      },
      {
        path: 'create-event',
        component: resolve => require(['../pages/event/Overview.vue'], resolve),
        props: (route) => (
          {
            page_id: route.params.id,
            edit: false
          }
        )
      },
      {
        path: 'calendar',
        component: resolve => require(['../pages/page/Calendar.vue'], resolve)
      },
      {
        path: 'services',
        name: 'page.services',
        component: resolve => require(['../pages/page/Services.vue'], resolve)
      },
      {
        path: 'requests',
        name: 'page.requests',
        component: resolve => require(['../pages/page/Requests.vue'], resolve)
      },
      {
        path: 'administrators',
        name: 'page.administrators',
        component: resolve => require(['../pages/page/Administrators.vue'], resolve)
      },
      {
        path: 'places',
        name: 'page.places',
        component: resolve => require(['../pages/page/Places.vue'], resolve)
      },
      {
        path: 'create-place',
        component: resolve => require(['../pages/place/Overview.vue'], resolve),
        props: (route) => (
          {
            page_id: route.params.id,
            edit: false
          }
        )
      },

    ]
  },
  {
    path: 'place/:id',
    component: resolve => require(['../pages/Place.vue'], resolve),
    redirect: '/admin/place/:id/overview',
    name: 'place',
    children: [
      {
        path: 'overview',
        component: resolve => require(['../pages/place/Overview.vue'], resolve)
      },
      {
        path: 'gallery',
        component: resolve => require(['../pages/place/Gallery.vue'], resolve)
      }
    ]
  },
  {
    path: 'settings',
    component: resolve => require(['../pages/Settings.vue'], resolve),
    redirect: '/admin/settings/account',
    children: [
      {
        path: 'account',
        component: resolve => require(['../pages/settings/Account.vue'], resolve)
      },
      {
        path: 'license',
        component: resolve => require(['../pages/settings/License.vue'], resolve)
      },
      {
        path: 'notifications',
        component: resolve => require(['../pages/settings/Notifications.vue'], resolve)
      },
    ]
  },
  {
    path: 'invoice/:invoice_id',
    name: 'invoice',
    component: resolve => require(['../pages/Invoice.vue'], resolve)
  },
  {
    path: 'invoice/:invoice_id/payment',
    name: 'invoice.payment',
    component: resolve => require(['../pages/Payment.vue'], resolve)
  }
]
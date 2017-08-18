import store from 'store/index';
export default [
  {
    path: 'dashboard',
    component: resolve => require(['../pages/Dashboard.vue'], resolve),
    meta: {
      title: 'Dashboard'
    }
  },
  {
    path: 'event',
    component: resolve => require(['../pages/Event.index.vue'], resolve),
    meta: {
      title: 'Events'
    }
  },
  {
    path: 'conference/create',
    component: resolve => require(['../pages/Conference.create.vue'], resolve),
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
    path: 'conference/:id',
    component: resolve => require(['../pages/Conference.vue'], resolve),
    redirect: '/admin/conference/:id/overview',
    children: [
      {
        path: 'overview',
        name: 'conference.overview',
        component: resolve => require(['../pages/conference/Overview.vue'], resolve),
        meta: {
          title: 'Overview'
        }
      },
      {
        path: 'events',
        name: 'conference.events',
        component: resolve => require(['../pages/templates/Events.vue'], resolve),
        meta: {
          title: 'Events'
        }
      },
      {
        path: 'attendees_old',
        name: 'conference.attendees.old',
        component: resolve => require(['../pages/conference/Attendees.vue'], resolve)
      },
      {
        path: 'attendees',
        name: 'conference.attendees',
        component: resolve => require(['../pages/conference/Attendees.new.vue'], resolve),
        meta: {
          title: 'Attendees'
        }
      },
      {
        path: 'administrators',
        name: 'conference.administrators',
        component: resolve => require(['../pages/templates/Administrators.vue'], resolve),
        meta: {
          title: 'Administrators'
        }
      },
      {
        path: 'events/create',
        component: resolve => require(['../pages/event/Overview.vue'], resolve),
        props: (route) => (
          {
            conference_id: route.params.id,
            edit: false
          }
        ),
        meta: {
          title: 'Create event'
        }
      },
      {
        path: 'settings',
        name: 'conference.settings',
        component: resolve => require(['../pages/conference/Settings.vue'], resolve),
        redirect: '/admin/conference/:id/settings/information',
        children: [
          {
            path: 'information',
            component: resolve => require(['../pages/conference/settings/Information.vue'], resolve),
            meta: {
              title: 'Conference information settings'
            }
          },
          {
            path: 'registration',
            component: resolve => require(['../pages/conference/settings/Registration.vue'], resolve),
            meta: {
              title: 'Conference registration settings'
            }
          }
        ]
      },
      {
        path: 'articles',
        name: 'conference.articles',
        component: resolve => require(['../pages/templates/Articles.vue'], resolve),
        meta: {
          title: 'Articles'
        }
      },
      {
        path: 'create-article',
        name: 'conference.article.create',
        component: resolve => require(['../pages/Article.vue'], resolve),
        meta: {
          title: 'Create article'
        }
      }
    ]
  },
  {
    path: 'events/create',
    component: resolve => require(['../pages/event/Overview.vue'], resolve),
    meta: {
      title: 'Create event'
    }
  },
  {
    path: 'event/:id',
    component: resolve => require(['../pages/Event.vue'], resolve),
    redirect: '/admin/event/:id/overview',
    children: [
      {
        path: 'overview',
        name: 'event.overview',
        component: resolve => require(['../pages/event/Overview.vue'], resolve)
      },
      {
        path: 'programme',
        name: 'event.programme',
        component: resolve => require(['../pages/event/Programme.vue'], resolve)
      },
      {
        path: 'attendees',
        name: 'event.attendees',
        component: resolve => require(['../pages/event/Attendees.vue'], resolve)
      }
    ]
  },
  {
    path: 'article/:article_id',
    name: 'article',
    component: resolve => require(['../pages/Article.vue'], resolve)
  },
  {
    path: 'page/create',
    component: resolve => require(['../pages/page/Compose.vue'], resolve),
    beforeEnter: (to, from, next) => {
      var license = store.getters.license;
      if (license && license.subscription.pageLimit > store.getters.pages.length) {
        next();
      } else {
        next('/pricing');
      }
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
        component: resolve => require(['../pages/page/Compose.vue'], resolve)
      },
      {
        path: 'events',
        name: 'page.events',
        component: resolve => require(['../pages/templates/Events.vue'], resolve)
      },
      {
        path: 'events/create',
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
        component: resolve => require(['../pages/templates/Services.vue'], resolve)
      },
      {
        path: 'requests',
        name: 'page.requests',
        component: resolve => require(['../pages/templates/Requests.vue'], resolve)
      },
      {
        path: 'administrators',
        name: 'page.administrators',
        component: resolve => require(['../pages/templates/Administrators.vue'], resolve)
      },
      {
        path: 'places',
        name: 'page.places',
        component: resolve => require(['../pages/templates/Places.vue'], resolve)
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
      }
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
    path: 'notifications',
    component: resolve => require(['../pages/Notifications.vue'], resolve)
  },
  {
    path: 'notifications',
    component: resolve => require(['../pages/Notifications.vue'], resolve)
  },
  {
    path: '*',
    component: resolve => require(['../pages/error/404.vue'], resolve)
  }
];

export default {
  path: 'page/:id',
  component: resolve => require(['pages/Page.vue'], resolve),
  redirect: '/admin/page/:id/overview',
  name: 'page',
  children: [
    {
      path: 'overview',
      name: 'page.overview',
      component: resolve => require(['pages/templates/Dashboard.vue'], resolve),
      meta: {
        titlle: 'Page Dashboard'
      }
    },
    {
      path: 'settings',
      name: 'page.settings',
      component: resolve => require(['pages/page/Compose.vue'], resolve),
      meta: {
        titlle: 'Settings'
      }
    },
    {
      path: 'events',
      name: 'page.events',
      component: resolve => require(['pages/templates/EventsCalendar.vue'], resolve),
      meta: {
        titlle: 'Page events'
      }
    },
    {
      path: 'services',
      name: 'page.services',
      component: resolve => require(['pages/templates/Services.vue'], resolve),
      meta: {
        titlle: 'Services'
      }
    },
    {
      path: 'requests',
      name: 'page.requests',
      component: resolve => require(['pages/templates/Requests.vue'], resolve),
      meta: {
        titlle: 'Requests'
      }
    },
    {
      path: 'administrators',
      name: 'page.administrators',
      component: resolve => require(['pages/templates/Administrators.vue'], resolve),
      meta: {
        titlle: 'Administrators'
      }
    },
    {
      path: 'places',
      name: 'page.places',
      component: resolve => require(['pages/templates/Places.vue'], resolve),
      meta: {
        titlle: 'Places'
      }
    },
    {
      path: 'create-place',
      name: 'page.create-place',
      component: resolve => require(['pages/place/Overview.vue'], resolve),
      props: (route) => (
        {
          page_id: route.params.id,
          edit: false
        }
      ),
      meta: {
        titlle: 'Create place'
      }
    },
    {
      path: 'gallery',
      name: 'page.gallery',
      component: resolve => require(['pages/templates/Gallery.vue'], resolve),
      props: {
        columns: 4,
        limit: 1024 * 1024 * 20
      },
      meta: {
        title: 'Gallery'
      }
    },
    {
      path: 'create-event-bot',
      name: 'page.create-event-bot',
      component: resolve => require(['pages/EventBot.vue'], resolve),
      props: (route) => ({
        edit: false
      }),
      meta: {
        title: 'Create event bot'
      }
    },
    {
      path: 'bots',
      name: 'page.bots',
      component: resolve => require(['pages/templates/Bots.vue'], resolve),
      meta: {
        title: 'Bots'
      }
    }
  ]
};

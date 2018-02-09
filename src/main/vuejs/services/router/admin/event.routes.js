export default {
  path: 'event/:id',
  component: resolve => require(['pages/Event.vue'], resolve),
  redirect: '/admin/event/:id/overview',
  name: 'event',
  children: [
    {
      path: 'overview',
      name: 'event.overview',
      component: resolve => require(['pages/event/Overview.vue'], resolve),
      meta: {
        title: 'Overview'
      }
    },
    {
      path: 'programme',
      name: 'event.programme',
      component: resolve => require(['pages/event/Programme.vue'], resolve),
      meta: {
        title: 'Programme'
      }
    },
    {
      path: 'attendees',
      name: 'event.attendees',
      component: resolve => require(['pages/templates/Attendees.vue'], resolve),
      meta: {
        title: 'Attendees'
      }
    },
    {
      path: 'gallery',
      name: 'event.gallery',
      component: resolve => require(['pages/templates/Gallery.vue'], resolve),
      meta: {
        title: 'Gallery'
      }
    },
    {
      path: 'advanced',
      name: 'event.advanced',
      component: resolve => require(['pages/event/Advanced.vue'], resolve),
      meta: {
        title: 'Advanced'
      }
    }
  ]
};

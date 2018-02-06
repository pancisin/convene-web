export default {
  path: 'place/:place_id',
  component: resolve => require(['pages/Place.vue'], resolve),
  redirect: '/admin/place/:place_id/overview',
  name: 'place',
  children: [
    {
      path: 'overview',
      name: 'place.overview',
      component: resolve => require(['pages/place/Overview.vue'], resolve),
      meta: {
        titlle: 'Overview'
      }
    },
    {
      path: 'gallery',
      name: 'place.gallery',
      component: resolve => require(['pages/templates/Gallery.vue'], resolve),
      meta: {
        titlle: 'Gallery'
      }
    },
    {
      path: 'venue',
      name: 'place.venue',
      component: resolve => require(['pages/place/VenueEditor.vue'], resolve),
      meta: {
        title: 'Venue editor'
      }
    }
  ]
};

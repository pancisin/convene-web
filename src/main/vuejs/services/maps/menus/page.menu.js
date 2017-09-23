export default {
  hasPermission: (user) => {
    return true;
  },
  routes: [{
      name: 'page.overview',
      code: 'admin.page.overview'
    },
    {
      name: 'page.events',
      code: 'admin.page.events'
    },
    // {
    //   name: 'page.places',
    //   code: 'admin.page.places'
    // },
    {
      name: 'page.services',
      code: 'admin.page.services'
    },
    {
      name: 'page.requests',
      code: 'admin.page.requests'
    },
    {
      name: 'page.gallery',
      code: 'admin.page.gallery'
    },
    {
      name: 'page.administrators',
      code: 'admin.page.administrators'
    },
    {
      name: 'page.settings',
      code: 'admin.page.settings'
    },
    {
      name: 'page.bots',
      code: 'admin.page.bots',
      hasPermission: (user) => {
        return user.role.name === 'ROLE_SUPERADMIN';
      }
    }
  ]
}
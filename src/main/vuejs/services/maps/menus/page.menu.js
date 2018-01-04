export default {
  hasPermission: (user) => {
    return true;
  },
  routes: [
    {
      name: 'page.overview',
      code: 'admin.page.overview',
      hasPermission: (pa) => {
        return true;
      }
    },
    {
      name: 'page.events',
      code: 'admin.page.events',
      hasPermission: (pa) => {
        return pa.active;
      }
    },
    // {
    //   name: 'page.places',
    //   code: 'admin.page.places'
    // },
    {
      name: 'page.services',
      code: 'admin.page.services',
      hasPermission: (pa) => {
        return pa.active && pa.role.level >= 60;
      }
    },
    {
      name: 'page.requests',
      code: 'admin.page.requests',
      hasPermission: (pa) => {
        return pa.active && pa.role.level >= 60;
      }
    },
    {
      name: 'page.gallery',
      code: 'admin.page.gallery',
      hasPermission: (pa) => {
        return pa.active;
      }
    },
    {
      name: 'page.administrators',
      code: 'admin.page.administrators',
      hasPermission: (pa) => {
        return pa.active && pa.role.level >= 60;
      }
    },
    {
      name: 'page.settings',
      code: 'admin.page.settings',
      hasPermission: (pa) => {
        return pa.active;
      }
    },
    {
      name: 'page.bots',
      code: 'admin.page.bots',
      hasPermission: (pa) => {
        return pa.user.role.prop === 'ROLE_SUPERADMIN';
      }
    }
  ]
};

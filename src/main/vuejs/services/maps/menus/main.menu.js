export default {
  hasPermission: (user) => {
    return true;
  },
  routes: [{
      name: 'admin.dashboard',
      code: 'admin.menu.dashboard',
      icon: 'dashboard'
    },
    {
      name: 'admin.event',
      code: 'admin.menu.events',
      icon: 'event'
    },
    // {
    //   name: 'admin.media-manager',
    //   code: 'admin.menu.media-manager',
    //   icon: 'picture-o'
    // },
    {
      name: 'admin.notification',
      code: 'admin.menu.notifications',
      icon: 'notifications_none'
    }
  ]
}
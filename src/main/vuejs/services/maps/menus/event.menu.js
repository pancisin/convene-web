export default {
  hasPermission: (user) => {
    return true;
  },
  routes: [{
    name: 'event.overview',
    code: 'admin.event.edit',
    icon: 'dashboard'
  },
  {
    name: 'event.programme',
    code: 'admin.event.programme',
    icon: 'list'
  },
  {
    name: 'event.gallery',
    code: 'admin.event.gallery',
    icon: 'picture-o'
  },
  {
    name: 'event.attendees',
    code: 'admin.event.attendees',
    icon: 'users'
  },
  {
    name: 'event.advanced',
    code: 'admin.event.advanced',
    icon: 'wrench'
  }]
};

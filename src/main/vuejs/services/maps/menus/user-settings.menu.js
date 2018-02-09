export default {
  hasPermission: (user) => {
    return true;
  },
  routes: [{
    name: 'settings.account',
    code: 'settings.account',
    icon: 'user-o'
  },
  {
    name: 'settings.privacy',
    code: 'settings.privacy.default',
    icon: 'key'
  },
  {
    name: 'settings.license',
    code: 'settings.license.default',
    icon: 'file-text-o'
  },
  {
    name: 'settings.notifications',
    code: 'settings.notifications',
    icon: 'bell-o'
  }]
};

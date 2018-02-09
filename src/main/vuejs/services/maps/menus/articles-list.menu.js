export default {
  hasPermission: (user) => {
    return true;
  },
  routes: [{
    name: 'system.list.articles',
    code: 'admin.articles_list.articles',
    icon: 'file'
  },
  {
    name: 'system.list.bots',
    code: 'admin.articles_list.bots',
    icon: 'android'
  },
  {
    name: 'system.list.settings',
    code: 'admin.articles_list.settings',
    icon: 'cog'
  }]
};

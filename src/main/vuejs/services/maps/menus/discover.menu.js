export default {
  hasPermission: user => {
    return true;
  },
  routes: [
    {
      name: 'client.event.explore',
      code: 'client.menu.events',
      icon: 'event'
    },
    {
      name: 'client.page.explore',
      code: 'client.menu.explore',
      icon: 'explore'
    },
    {
      name: 'conferences',
      code: 'client.menu.conferences',
      icon: 'people'
    }
  ]
};

export default {
  hasPermission: (user) => {
    return true;
  },
  routes: [{
      name: 'client.home',
      code: 'client.menu.home',
      icon: 'home'
    },
    {
      name: 'client.event.explore',
      code: 'client.menu.events',
      icon: 'event',
      children: [{
          name: 'client.event.explore',
          code: 'client.menu.events',
        },
        {
          name: 'client.event.my',
          code: 'client.menu.my_events'
        }
      ]
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
    },
    {
      name: 'about',
      code: 'client.menu.about',
      icon: 'question_answer',
      children: [{
          name: 'about',
          code: 'client.menu.about'
        },
        {
          name: 'pricing',
          code: 'client.menu.pricing'
        },
        {
          name: 'faq',
          code: 'client.menu.faq'
        },
        {
          name: 'terms',
          code: 'client.menu.terms'
        },
        {
          name: 'privacy-policy',
          code: 'client.menu.privacy'
        }
      ]
    },
    {
      name: 'admin.dashboard',
      code: 'client.menu.admin',
      icon: 'dashboard',
      hasPermission: user => {
        return user != null && user.role != null && user.role.level >= 40;
      }
    }
  ]
};
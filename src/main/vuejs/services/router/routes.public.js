export default [
  {
    path: '',
    name: 'client.home',
    component: resolve => require(['pages/public/Home.vue'], resolve),
    meta: {
      title: 'Home'
    }
  },
  {
    path: 'event/:id',
    name: 'event.public',
    component: resolve => require(['pages/public/Event.public.vue'], resolve)
  },
  {
    path: 'pages',
    name: 'client.page.explore',
    component: resolve => require(['pages/public/Page.index.vue'], resolve),
    meta: {
      title: 'Pages'
    }
  },
  {
    path: 'page/:id',
    name: 'page.public',
    component: resolve => require(['pages/public/Page.vue'], resolve)
  },
  {
    path: 'conferences',
    name: 'conferences',
    component: resolve => require(['pages/public/Conference.index.vue'], resolve),
    meta: {
      title: 'Conferences'
    }
  },
  {
    path: 'conference/:id',
    name: 'conference',
    component: resolve => require(['pages/public/Conference.vue'], resolve)
  },
  {
    path: 'article/:article_id',
    name: 'article.public',
    component: resolve => require(['pages/public/Article.vue'], resolve)
  },
  {
    path: 'about',
    alias: 'admin/about',
    name: 'about',
    component: resolve => require(['pages/static/About.vue'], resolve),
    meta: {
      title: 'About'
    }
  },
  {
    path: 'faq',
    name: 'faq',
    component: resolve => require(['pages/static/FAQ.vue'], resolve),
    meta: {
      title: 'Frequently asked questions'
    }
  },
  {
    path: 'terms',
    name: 'terms',
    component: resolve => require(['pages/static/Terms.vue'], resolve),
    meta: {
      title: 'Terms of usage'
    }
  },
  {
    path: 'privacy-policy',
    name: 'privacy-policy',
    component: resolve => require(['pages/static/Privacy.vue'], resolve),
    meta: {
      title: 'Privacy policy'
    }
  },
  {
    path: 'pricing',
    alias: 'admin/pricing',
    name: 'pricing',
    component: resolve => require(['pages/static/Pricing.vue'], resolve),
    meta: {
      title: 'Pricing'
    }
  },
  {
    path: 'events',
    name: 'client.event.explore',
    component: resolve => require(['pages/public/Event.index.vue'], resolve),
    meta: {
      title: 'Events'
    }
  },
  {
    path: 'settings',
    name: 'settings',
    component: resolve => require(['pages/Settings.vue'], resolve),
    redirect: '/settings/account',
    children: [
      {
        path: 'account',
        name: 'settings.account',
        component: resolve => require(['pages/settings/Account.vue'], resolve),
        meta: {
          title: 'Account settings'
        }
      },
      {
        path: 'privacy',
        name: 'settings.privacy',
        component: resolve => require(['pages/settings/Privacy.vue'], resolve),
        meta: {
          title: 'Privacy settings'
        }
      },
      {
        path: 'license',
        name: 'settings.license',
        component: resolve => require(['pages/settings/License.vue'], resolve),
        meta: {
          title: 'License settings'
        }
      },
      {
        path: 'notifications',
        name: 'settings.notifications',
        component: resolve => require(['pages/settings/Notifications.vue'], resolve),
        meta: {
          title: 'Notification settings'
        }
      }
    ]
  },
  {
    path: 'invoice/:invoice_id',
    name: 'invoice',
    component: resolve => require(['pages/Invoice.vue'], resolve)
  },
  {
    path: 'user/:user_id',
    name: 'user',
    component: resolve => require(['pages/public/User.vue'], resolve)
  }
];

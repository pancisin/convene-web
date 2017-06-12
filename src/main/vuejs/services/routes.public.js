export default [
  {
    path: '',
    component: resolve => require(['../pages/public/Home.vue'], resolve)
  },
  {
    path: 'my-events',
    component: resolve => require(['../pages/public/Event.my.vue'], resolve)
  },
  {
    path: 'event/:id',
    name: 'event.public',
    component: resolve => require(['../pages/public/Event.public.vue'], resolve),
  },
  {
    path: 'explore',
    component: resolve => require(['../pages/public/Page.index.vue'], resolve)
  },
  {
    path: 'page/:id',
    component: resolve => require(['../pages/public/Page.vue'], resolve),
  },
  {
    path: 'about',
    alias: 'admin/about',
    component: resolve => require(['../pages/static/About.vue'], resolve)
  },
  {
    path: 'faq',
    alias: 'admin/faq',
    component: resolve => require(['../pages/static/faq.vue'], resolve)
  },
  {
    path: 'terms',
    alias: 'admin/terms',
    component: resolve => require(['../pages/static/Terms.vue'], resolve)
  },
  {
    path: 'privacy-policy',
    alias: 'admin/privacy-policy',
    component: resolve => require(['../pages/static/Privacy.vue'], resolve)
  },
  {
    path: 'pricing',
    alias: 'admin/pricing',
    component: resolve => require(['../pages/static/Pricing.vue'], resolve)
  },
  {
    path: 'events',
    component: resolve => require(['../pages/public/Event.index.vue'], resolve)
  },
  {
    path: 'settings',
    name: 'settings',
    component: resolve => require(['../pages/Settings.vue'], resolve),
    redirect: '/settings/account',
    children: [
      {
        path: 'account',
        name: 'settings.account',
        component: resolve => require(['../pages/settings/Account.vue'], resolve)
      },
      {
        path: 'license',
        name: 'settings.license',
        component: resolve => require(['../pages/settings/License.vue'], resolve)
      },
      {
        path: 'notifications',
        name: 'settings.notifications',
        component: resolve => require(['../pages/settings/Notifications.vue'], resolve)
      },
    ]
  },
]
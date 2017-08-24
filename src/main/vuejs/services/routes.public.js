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
    component: resolve => require(['../pages/public/Event.public.vue'], resolve)
  },
  {
    path: 'explore',
    name: 'event.explore',
    component: resolve => require(['../pages/public/Page.index.vue'], resolve)
  },
  {
    path: 'page/:id',
    name: 'page.public',
    component: resolve => require(['../pages/public/Page.vue'], resolve)
  },
  {
    path: 'conferences',
    name: 'conferences',
    component: resolve => require(['../pages/public/Conference.index.vue'], resolve)
  },
  {
    path: 'conference/:id',
    name: 'conference',
    component: resolve => require(['../pages/public/Conference.vue'], resolve)
  },
  {
    path: 'about',
    alias: 'admin/about',
    name: 'about',
    component: resolve => require(['../pages/static/About.vue'], resolve)
  },
  {
    path: 'faq',
    alias: 'admin/faq',
    name: 'faq',
    component: resolve => require(['../pages/static/faq.vue'], resolve)
  },
  {
    path: 'terms',
    alias: 'admin/terms',
    name: 'terms',
    component: resolve => require(['../pages/static/Terms.vue'], resolve)
  },
  {
    path: 'privacy-policy',
    alias: 'admin/privacy-policy',
    name: 'privacy-policy',
    component: resolve => require(['../pages/static/Privacy.vue'], resolve)
  },
  {
    path: 'pricing',
    alias: 'admin/pricing',
    name: 'pricing',
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
      }
    ]
  },
  {
    path: 'invoice/:invoice_id',
    name: 'invoice',
    component: resolve => require(['../pages/Invoice.vue'], resolve)
  },
  {
    path: 'invoice/:invoice_id/payment',
    name: 'invoice.payment',
    component: resolve => require(['../pages/Payment.vue'], resolve)
  }
];

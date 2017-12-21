export default [
  {
    path: '',
    name: 'client.home',
    component: resolve => require(['../pages/public/Home.vue'], resolve)
  },
  {
    path: 'my-events',
    name: 'client.event.my',
    component: resolve => require(['../pages/public/Event.my.vue'], resolve)
  },
  {
    path: 'event/:id',
    name: 'event.public',
    component: resolve => require(['../pages/public/Event.public.vue'], resolve)
  },
  {
    path: 'explore',
    name: 'client.page.explore',
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
    path: 'article/:article_id',
    name: 'article.public',
    component: resolve => require(['../pages/public/Article.vue'], resolve)
  },
  {
    path: 'survey/:survey_id',
    name: 'survey.public',
    component: resolve => require(['../pages/public/Survey.vue'], resolve)
  },
  {
    path: 'about',
    alias: 'admin/about',
    name: 'about',
    component: resolve => require(['../pages/static/About.vue'], resolve)
  },
  {
    path: 'faq',
    name: 'faq',
    component: resolve => require(['../pages/static/FAQ.vue'], resolve)
  },
  {
    path: 'terms',
    name: 'terms',
    component: resolve => require(['../pages/static/Terms.vue'], resolve)
  },
  {
    path: 'privacy-policy',
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
    name: 'client.event.explore',
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
    path: 'user/:user_id',
    name: 'user',
    component: resolve => require(['../pages/public/User.vue'], resolve)
  }
];

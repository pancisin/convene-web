import Vue from 'vue';
import VueRouter from 'vue-router';
Vue.use(VueRouter);

import store from './store.js'
import Auth from './auth.js';

const require_auth = (to, from, next) => {
  if (!Auth.user.authenticated) {
    next({
      path: '/login',
      query: { redirect: to.fullPath }
    })
  } else {
    next();
  }
}

const afterAuth = (_to, from, next) => {
  if (Auth.user.authenticated) {
    next(from.path)
  } else {
    next()
  }
}

export default new VueRouter({
  linkActiveClass: 'active',
  routes: [
    {
      path: '/',
      component: resolve => require(['../layouts/Client.vue'], resolve),
      children: [
        {
          path: '',
          component: resolve => require(['../pages/public/Dashboard.vue'], resolve),
        },
        {
          path: 'event',
          component: resolve => require(['../pages/Event.index.vue'], resolve)
        },
        {
          path: 'event/:id',
          component: resolve => require(['../pages/public/Event.public.vue'], resolve),
        },
        // {
        //   path: 'conference',
        //   component: resolve => require(['../pages/public/Conference.index.vue'], resolve)
        // },
        {
          path: 'explore',
          component: resolve => require(['../pages/public/Page.index.vue'], resolve)
        },
        {
          path: 'page/:id',
          component: resolve => require(['../pages/public/Page.vue'], resolve),
        },
        {
          path: 'faq',
          component: resolve => require(['../pages/static/faq.vue'], resolve)
        },
        {
          path: 'terms',
          component: resolve => require(['../pages/static/Terms.vue'], resolve)
        },
        {
          path: 'privacy-policy',
          component: resolve => require(['../pages/static/Privacy.vue'], resolve)
        },
        {
          path: 'pricing',
          component: resolve => require(['../pages/static/Pricing.vue'], resolve)
        }
      ]
    },
    {
      path: '/admin',
      component: resolve => require(['../layouts/Admin.vue'], resolve),
      beforeEnter: require_auth,
      redirect: '/admin/dashboard',
      children: [
        {
          path: 'dashboard',
          component: resolve => require(['../pages/Dashboard.vue'], resolve)
        },
        {
          path: 'event',
          component: resolve => require(['../pages/Event.index.vue'], resolve)
        },
        {
          path: 'conference/create',
          component: resolve => require(['../pages/conference/Overview.vue'], resolve),
          beforeEnter: (to, from, next) => {
            var license = store.getters.license;
            if (license && license.subscription.conferenceLimit > store.state.conferences.length)
              next();
            else
              next('/pricing');
          }
        },
        {
          path: 'conference/:id',
          component: resolve => require(['../pages/Conference.vue'], resolve),
          redirect: '/admin/conference/:id/overview',
          children: [
            {
              path: 'overview',
              component: resolve => require(['../pages/conference/Overview.vue'], resolve)
            },
            {
              path: 'events',
              component: resolve => require(['../pages/conference/Events.vue'], resolve)
            }
          ]
        },
        {
          path: 'users',
          component: resolve => require(['../pages/Users.vue'], resolve)
        },
        {
          path: 'event/create',
          component: resolve => require(['../pages/event/Overview.vue'], resolve)
        },
        {
          path: 'event/:id',
          component: resolve => require(['../pages/Event.vue'], resolve),
          redirect: '/admin/event/:id/overview',
          children: [
            {
              path: 'overview',
              component: resolve => require(['../pages/event/Overview.vue'], resolve)
            },
            {
              path: 'programme',
              component: resolve => require(['../pages/event/Programme.vue'], resolve)
            },
            {
              path: 'attendees',
              component: resolve => require(['../pages/event/Attendees.vue'], resolve)
            }
          ]
        },
        {
          path: 'page/create',
          component: resolve => require(['../pages/page/Overview.vue'], resolve),
          beforeEnter: (to, from, next) => {
            var license = store.getters.license;
            if (license && license.subscription.pageLimit > store.state.pages.length)
              next();
            else
              next('/pricing');
          }
        },
        {
          path: 'page/:id',
          component: resolve => require(['../pages/Page.vue'], resolve),
          redirect: '/admin/page/:id/overview',
          children: [
            {
              path: 'overview',
              component: resolve => require(['../pages/page/Overview.vue'], resolve)
            },
            {
              path: 'events',
              component: resolve => require(['../pages/page/Events.vue'], resolve)
            },
            {
              path: 'create-event',
              component: resolve => require(['../pages/event/Overview.vue'], resolve),
              props: (route) => (
                {
                  page_id: route.params.id,
                  edit: false
                }
              )
            },
            {
              path: 'calendar',
              component: resolve => require(['../pages/page/Calendar.vue'], resolve)
            },
            {
              path: 'services',
              component: resolve => require(['../pages/page/Services.vue'], resolve)
            },
            {
              path: 'requests',
              component: resolve => require(['../pages/page/Requests.vue'], resolve)
            },
            {
              path: 'administrators',
              component: resolve => require(['../pages/page/Administrators.vue'], resolve)
            },
            {
              path: 'places',
              component: resolve => require(['../pages/page/Places.vue'], resolve)
            },
            {
              path: 'create-place',
              component: resolve => require(['../pages/place/Overview.vue'], resolve),
              props: (route) => (
                {
                  page_id: route.params.id,
                  edit: false
                }
              )
            },

          ]
        },
        {
          path: 'place/:id',
          component: resolve => require(['../pages/Place.vue'], resolve),
          redirect: '/admin/place/:id/overview',
          name: 'place',
          children: [
            {
              path: 'overview',
              component: resolve => require(['../pages/place/Overview.vue'], resolve)
            },
          ]
        },
        {
          path: 'faq',
          component: resolve => require(['../pages/static/faq.vue'], resolve)
        },
        {
          path: 'terms',
          component: resolve => require(['../pages/static/Terms.vue'], resolve)
        },
        {
          path: 'privacy-policy',
          component: resolve => require(['../pages/static/Privacy.vue'], resolve)
        },


        {
          path: 'chat',
          component: resolve => require(['../pages/Chat.vue'], resolve)
        },
        {
          path: '/settings',
          component: resolve => require(['../pages/settings/Settings.layout.vue'], resolve),
          redirect: '/settings/account',
          children: [
            {
              path: 'account',
              component: resolve => require(['../pages/settings/Account.vue'], resolve)
            },
            {
              path: 'company',
              component: resolve => require(['../pages/settings/Company.vue'], resolve)
            },
            {
              path: 'license',
              component: resolve => require(['../pages/settings/License.vue'], resolve)
            },
            {
              path: 'task',
              component: resolve => require(['../pages/settings/Tasks.vue'], resolve)
            },
            {
              path: 'users',
              component: resolve => require(['../pages/settings/Users.vue'], resolve)
            }
          ]
        }
      ]
    },
    {
      path: '/login',
      component: resolve => require(['../pages/Login.vue'], resolve),
      beforeEnter: afterAuth
    },
    {
      path: '/register',
      component: resolve => require(['../pages/Register.vue'], resolve),
      beforeEnter: afterAuth
    },
  ],
})
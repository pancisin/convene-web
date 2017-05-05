import Vue from 'vue';
import VueRouter from 'vue-router';
Vue.use(VueRouter);

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
      component: resolve => require(['../components/Layout.vue'], resolve),
      beforeEnter: require_auth,
      redirect: '/dashboard',
      children: [
        {
          path: '/dashboard',
          component: resolve => require(['../pages/Dashboard.vue'], resolve)
        },
        {
          path: '/event',
          component: resolve => require(['../pages/Event.index.vue'], resolve)
        },
        {
          path: '/page/create',
          component: resolve => require(['../pages/Page.create.vue'], resolve)
        },
        {
          path: '/conference/create',
          component: resolve => require(['../pages/Conference.create.vue'], resolve)
        },
        {
          path: '/users',
          component: resolve => require(['../pages/Users.vue'], resolve)
        },
        {
          path: '/event/create',
          component: resolve => require(['../pages/Event.create.vue'], resolve)
        },
        {
          path: '/event/:id',
          component: resolve => require(['../pages/Event.create.vue'], resolve)
        },


        {
          path: '/chat',
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
    }
  ],
})
import Vue from 'vue';
import VueRouter from 'vue-router';
import Auth from './auth.js';
Vue.use(VueRouter);

import publicRoutes from './routes.public.js'
import adminRoutes from './routes.admin.js'

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
      component: resolve => {
        var reg = new RegExp("www|bookster|localhost:3000");
        var parts = window.location.host.split(".");
        return reg.test(parts[0]) ? require(['../layouts/Client.vue'], resolve) : require(['../pages/public/Page.standalone.vue'], resolve);
      },
      children: publicRoutes.concat([
        {
          path: 'create-event',
          component: resolve => require(['../pages/public/Event.create.vue'], resolve),
          beforeEnter: require_auth
        },
        {
          path: 'subscription-sign-up',
          name: 'sub.signup',
          component: resolve => require(['../pages/public/Subscribe.vue'], resolve),
          beforeEnter: require_auth,
        }
      ])
    },
    {
      path: '/admin',
      component: resolve => require(['../layouts/Admin.vue'], resolve),
      beforeEnter: require_auth,
      redirect: '/admin/dashboard',
      children: adminRoutes
    },
    {
      path: '/confirm-email',
      component: resolve => require(['../pages/EmailVerify.vue'], resolve)
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
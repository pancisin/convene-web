import Vue from 'vue';
import VueRouter from 'vue-router';
Vue.use(VueRouter);
import store from 'store';

import publicRoutes from './routes.public';
import adminRoutes from './routes.admin';

const require_auth = (to, from, next) => {
  if (!store.getters.authenticated) {
    next({
      path: '/login',
      query: { redirect: to.fullPath }
    });
  } else {
    next();
  }
};

const afterAuth = (to, from, next) => {
  if (store.getters.authenticated) {
    next(from.path);
  } else {
    next();
  }
};

var router = new VueRouter({
  linkActiveClass: 'active',
  mode: 'history',
  routes: [
    {
      path: '/',
      component: resolve => {
        var reg = new RegExp('www|bookster|localhost:3000|convene');
        var parts = window.location.host.split('.');
        return reg.test(parts[0]) ? require(['src/main/vuejs/layouts/Client.vue'], resolve) : require(['pages/public/Page.standalone.vue'], resolve);
      },
      children: publicRoutes
    },
    {
      path: '/admin',
      component: resolve => require(['src/main/vuejs/layouts/Admin.vue'], resolve),
      beforeEnter: require_auth,
      redirect: '/admin/dashboard',
      children: adminRoutes
    },
    {
      path: '/confirm-email',
      component: resolve => require(['pages/EmailVerify.vue'], resolve)
    },
    {
      path: '/login',
      name: 'login',
      component: resolve => require(['pages/Login.vue'], resolve),
      beforeEnter: afterAuth,
      meta: {
        title: 'Login'
      }
    },
    {
      path: '/register',
      name: 'register',
      component: resolve => require(['pages/Register.vue'], resolve),
      beforeEnter: afterAuth,
      meta: {
        title: 'Register'
      }
    }
  ]
});

export default router;

import Vue from 'vue';
import VueRouter from 'vue-router';
Vue.use(VueRouter);

import Auth from './auth.js';

import UsersComponent from '../components/Users.vue';
import DashboardComponent from '../components/Dashboard.vue';
import LicensesComponent from '../components/Licenses.vue';
import EmployeesComponent from '../components/Employees.vue';
import WorkManagement from '../components/WorkManagement.vue';
import CreateDuty from '../components/CreateDuty.vue';
import Layout from '../components/Layout.vue';
import Login from '../pages/Login.vue';
import Register from '../pages/Register.vue';

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
  if (auth.user.authenticated) {
    next(from.path)
  } else {
    next()
  }
}

const router = new VueRouter({
  routes: [
    {
      path: '/',
      component: Layout,
      beforeEnter: require_auth,
      children: [
        {
          path: '/dashboard',
          component: DashboardComponent
        },
        {
          path: '/users',
          component: UsersComponent
        },
        {
          path: '/licenses',
          component: LicensesComponent
        },
        {
          path: '/employees',
          component: EmployeesComponent
        },
        {
          path: '/work-management',
          component: WorkManagement
        },
        {
          path: '/duty/create',
          component: CreateDuty
        },
        {
          path: '/duty/:id',
          component: CreateDuty
        }
      ]
    },
    {
      path: '/login',
      component: Login
    },
    {
      path: '/register',
      component: Register
    }
  ],
})

export default router;
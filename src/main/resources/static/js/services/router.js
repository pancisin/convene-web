import Vue from 'vue';
import VueRouter from 'vue-router';
Vue.use(VueRouter);

import Auth from './auth.js';

import UsersComponent from '../components/Users.vue'
import DashboardComponent from '../components/Dashboard.vue'
import LicensesComponent from '../components/Licenses.vue'
import EmployeesComponent from '../components/Employees.vue'
import WorkManagement from '../components/WorkManagement.vue'
import CreateDuty from '../pages/CreateDuty.vue'
import Layout from '../components/Layout.vue'
import Login from '../pages/Login.vue'
import Register from '../pages/Register.vue'
import Account from '../pages/Account.vue'
import Company from '../pages/Company.vue'
import Customers from '../pages/Customers.vue'
import CustomerCreate from '../pages/Customer.create.vue'
import Instance from '../pages/Instance.vue'
import Chat from '../pages/Chat.vue'

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

const router = new VueRouter({
  routes: [
    {
      path: '/',
      component: Layout,
      beforeEnter: require_auth,
      redirect: '/dashboard',
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
          path: '/duty',
          component: WorkManagement
        },
        {
          path: '/duty/create',
          component: CreateDuty
        },
        {
          path: '/duty/:id',
          component: CreateDuty
        },
        {
          path: '/account',
          component: Account
        },
        {
          path: '/company',
          component: Company
        },
        {
          path: '/customers',
          component: Customers
        },
        {
          path: '/customers/create',
          component: CustomerCreate
        },
        {
          path: '/customers/:id',
          component: CustomerCreate
        },
        {
          path: '/instance/:id/:timestamp',
          component: Instance
        },
        {
          path: '/chat',
          component: Chat
        }
      ]
    },
    {
      path: '/login',
      component: Login,
      beforeEnter: afterAuth
    },
    {
      path: '/register',
      component: Register,
      beforeEnter: afterAuth
    }
  ],
})

export default router;
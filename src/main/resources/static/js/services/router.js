import Vue from 'vue';
import VueRouter from 'vue-router';
Vue.use(VueRouter);

import UsersComponent from '../components/Users.vue';
import DashboardComponent from '../components/Dashboard.vue';
import LicensesComponent from '../components/Licenses.vue';
import EmployeesComponent from '../components/Employees.vue';
import WorkManagement from '../components/WorkManagement.vue';
import CreateDuty from '../components/CreateDuty.vue';
import Layout from '../components/Layout.vue';
import Login from '../pages/Login.vue';

const router = new VueRouter({
  routes: [
    {
      path: '/',
      component: Layout,
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
    // {
    //   path: '/register',
    //   component: Register
    // }
  ],
})

router.replace('/dashboard');

export default router;

// authentication service
// import Auth from './services/auth.js';

// router.beforeEach(function (transition) {
//   if (transition.to.auth && !Auth.authenticated) {
//     // if route requires auth and user isn't authenticated
//     transition.redirect('/login')
//   } else {
//     transition.next()
//   }
// });
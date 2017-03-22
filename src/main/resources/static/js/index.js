import Vue from 'vue'
import VueRouter from 'vue-router';
import VueResource from 'vue-resource';
import VueMoment from 'vue-moment';

Vue.use(VueResource);
Vue.use(VueRouter);
Vue.use(VueMoment);

Vue.config.devtools = true;

import App from './components/App.vue';
import UsersComponent from './components/Users.vue';
import DashboardComponent from './components/Dashboard.vue';
import LicensesComponent from './components/Licenses.vue';
import EmployeesComponent from './components/Employees.vue';
import WorkManagement from './components/WorkManagement.vue';

const router = new VueRouter({
  routes: [
    { path: '/', component: DashboardComponent },
    { path: '/users', component: UsersComponent },
    { path: '/licenses', component: LicensesComponent },
    { path: '/employees', component: EmployeesComponent },
    { path: '/work-management', component: WorkManagement }
  ]
})

const app = new Vue({
  el: '#application',
  router,
  render: h => h(App)
})
import Vue from 'vue'
import VueRouter from 'vue-router';
import Vuex from 'vuex';
import VueResource from 'vue-resource';
import VueMoment from 'vue-moment';

Vue.use(VueRouter);
Vue.use(Vuex);
Vue.use(VueResource);
Vue.use(VueMoment);

Vue.config.devtools = true;

import App from './components/App.vue';
import UsersComponent from './components/Users.vue';
import DashboardComponent from './components/Dashboard.vue';
import LicensesComponent from './components/Licenses.vue';
import EmployeesComponent from './components/Employees.vue';
import WorkManagement from './components/WorkManagement.vue';
import CreateDuty from './components/CreateDuty.vue';

const store = new Vuex.Store({
  state: {
    user: null
  },
  mutations: {
    setUser(state, { user }) {
      state.user = user;
    }
  },
  getters: {
    company_id: state => {
      return state.user.company.id;
    }
  }
})

const router = new VueRouter({
  routes: [
    { path: '/', component: DashboardComponent },
    { path: '/users', component: UsersComponent },
    { path: '/licenses', component: LicensesComponent },
    { path: '/employees', component: EmployeesComponent },
    { path: '/work-management', component: WorkManagement },
    { path: '/duty/create', component: CreateDuty }
  ],
  redirect: '/',
})

const app = new Vue({
  el: '#application',
  store,
  router,
  created: function () {
    this.initialize();
  },
  methods: {
    initialize: function () {
      this.$http.get('/api/user/me').then(response => {
        var user = response.body;
        this.$store.commit('setUser', { user });
      }, response => {
      });
    },
  },
  render: h => h(App)
})
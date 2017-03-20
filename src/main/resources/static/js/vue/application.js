Vue.config.devtools = true;

Vue.use(VueRouter);
Vue.use(VueResource);

var dashboard = Vue.component('dashboard', {
  template: '#dashboard-component-template'
});

var users = Vue.component('users', {
  template: '#users-component-template',
  data: function() {
    return {
      users: [],
    }
  },
  created: function() {
    this.getUsers();
  },
  methods: {
    getUsers: function() {
      this.$http.get('/api/user/').then(response => {
        this.users = response.body;
      }, response => {
        // error callback
      });
    }
  }
});

var header_component = Vue.component('header-component', {
  template: '#header-component-template',
  data : function() {
    return {
      user : null
    }
  },
  created : function() {
    this.getUser();
  },
  methods: {
    getUser : function() {
      this.$http.get('/api/user/me').then(response => {
        this.user = response.body;
      }, response => {
        // error callback
      });
    }
  }
});

const router = new VueRouter({
  history: true,
  root: '/',
  routes : [
    {
      path : '/',
      component : dashboard
    },
    {
      path: '/users',
      component: users
    }
  ]
})

var application = new Vue({
  el: '#application-container',
  router,
});
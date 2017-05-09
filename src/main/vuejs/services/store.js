import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

const store = new Vuex.Store({
  state: {
    user: null,
    notifications: []
  },
  mutations: {
    setUser(state, { user }) {
      state.user = user;
    },
    addNotification(state, notification) {
      state.notifications.push(notification);
    },
    removeNotification(state, notification) {
      state.notifications.splice(state.notifications.indexOf(notification), 1);
    }
  },
  getters: {
    company_id: state => {
      // return state.user.company.id;
      return 0;
    }
  }
});

export default store;
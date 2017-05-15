import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

const store = new Vuex.Store({
  state: {
    user: null,
    notifications: [],
    toasts: [],
  },
  mutations: {
    setUser(state, { user }) {
      state.user = user;
    },
    addNotification(state, notification) {
      state.notifications.push(notification);
    },
    addToast(state, notification) {
      state.toasts.push(notification);
      setTimeout(() => {
        state.toasts.splice(state.toasts.indexOf(toast), 1);
      }, 5000)
    },
    removeNotification(state, notification) {
      state.notifications.push(state.notifications.filter(n => {
        return n.id != notification.id;
      }));
    }
  },
  actions: {
    initNotifications({ commit, state }, notifications) {
      state.notifications = notifications;
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
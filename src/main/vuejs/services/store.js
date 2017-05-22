import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

const store = new Vuex.Store({
  state: {
    user: null,
    notifications: [],
    toasts: [],
    pages: [],
    conferences: []
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
      state.notifications = state.notifications.filter(n => {
        return n.id != notification.id;
      });
    },
    addPage(state, page) {
      state.pages.push(page);
    },
    removePage(state, page) {
      state.pages = state.pages.filter(p => {
        return page.id != p.id;
      });
    }
  },
  actions: {
    initNotifications({ commit, state }, notifications) {
      state.notifications = notifications;
    },
    initPages({ commit, state }, pages) {
      state.pages = pages;
    },
    initConferences({ commit, state }, conferences) {
      state.conferences = conferences;
    }
  },
  getters: {
    locale: state => {
      return state.user.locale;
    },
    isAdmin: state => {
      return state.user.role != null && state.user.role.name == "ROLE_ADMINISTRATOR";
    },
    license: state => {
      if (state.user.license)
        return state.user.license;
      else null;
    }
  }
});

export default store;
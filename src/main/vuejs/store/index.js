import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

import user from './modules/user.js';
import page from './modules/page.js';
import conference from './modules/conference.js';

import UserApi from 'api/user.api';

const store = new Vuex.Store({
  state: {
    notifications: [],
    toasts: []
  },
  modules: {
    user, page, conference
  },
  getters: {
    notifications: state => state.notifications,
    toasts: state => state.toasts
  },
  mutations: {
    setNotifications: (state, { notifications }) => {
      state.notifications = notifications;
    },
    removeNotification: (state, { notification }) => {
      state.notifications = state.notifications.filter(n => {
        return n.id !== notification.id;
      });
    },
    addNotification: (state, { notification }) => {
      state.notifications.push(notification);
    },
    addToast: (state, { toast }) => {
      state.toasts.push(toast);
    },
    removeToast: (state, { toast }) => {
      state.toasts.splice(state.toasts.indexOf(toast), 1);
    }
  },
  actions: {
    initializeNotifications ({ commit }) {
      UserApi.getNotifications((notifications) => {
        commit('setNotifications', { notifications });
      });
    },
    addNotification ({ commit }, notification) {
      commit('addNotification', { notification });
    },
    removeNotification ({ commit }, notification) {
      commit('removeNotification', { notification });
    },
    addToast ({ commit }, toast) {
      commit('addToast', { toast });

      setTimeout(() => {
        commit('removeToast', { toast });
      }, 5000);
    }
  }
});

export default store;

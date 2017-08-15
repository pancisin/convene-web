import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

import user from './modules/user.js';
import page from './modules/page.js';
import conference from './modules/conference.js';

import UserApi from 'api/user.api';

import * as types from 'store/mutation-types';

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
  actions: {
    initializeNotifications ({ commit }) {
      UserApi.getNotifications((notifications) => {
        commit(types.SET_NOTIFICATIONS, { notifications });
      });
    },
    addNotification ({ commit }, notification) {
      commit(types.ADD_NOTIFICATION, { notification });
    },
    removeNotification ({ commit }, notification) {
      commit(types.REMOVE_NOTIFICATION, { notification });
    },
    addToast ({ commit }, toast) {
      commit(types.ADD_TOAST, { toast });

      setTimeout(() => {
        commit(types.REMOVE_TOAST, { toast });
      }, 5000);
    }
  },
  mutations: {
    [types.SET_NOTIFICATIONS] (state, { notifications }) {
      state.notifications = notifications;
    },

    [types.REMOVE_NOTIFICATION] (state, { notification }) {
      state.notifications = state.notifications.filter(n => {
        return n.id !== notification.id;
      });
    },

    [types.ADD_NOTIFICATION] (state, { notification }) {
      state.notifications.push(notification);
    },

    [types.ADD_TOAST] (state, { toast }) {
      state.toasts.push(toast);
    },

    [types.REMOVE_TOAST] (state, { toast }) {
      state.toasts.splice(state.toasts.indexOf(toast), 1);
    }
  }
});

export default store;

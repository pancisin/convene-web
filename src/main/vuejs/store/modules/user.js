import UserApi from 'api/user.api';
import * as types from 'store/mutation-types';

const state = {
  user: null,
  notifications: []
};

const getters = {
  user: state => {
    if (state.user) {
      if (!state.user.address) {
        state.user.address = {};
      }
    }
    return state.user;
  },
  locale: state => state.user.locale,
  isAdmin: state => state.user != null && state.user.role != null && state.user.role.level >= 40,
  license: state => state.user.license ? state.user.license : null,
  notifications: state => state.notifications
};

const actions = {
  initializeUser ({ commit }) {
    return new Promise((resolve, reject) => {
      UserApi.getUser((user) => {
        commit(types.SET_USER, { user });
        resolve(user);
      });
    });
  },
  clearUser ({ commit }) {
    commit(types.SET_USER, { user: null });
  },
  updateUser ({ commit }, user) {
    return new Promise((resolve) => {
      UserApi.putUser(user, result => {
        commit(types.SET_USER, { result });
        resolve(user);
      });
    });
  },
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
  }
};

const mutations = {
  [types.SET_USER] (state, { user }) {
    state.user = user;
  },

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
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};

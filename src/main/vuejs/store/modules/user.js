import UserApi from 'api/user.api';
import AuthApi from 'api/auth.api';
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
  notifications: state => state.notifications,
  authenticated: state => {
    return state.user != null && state.user.id != null && window.localStorage.getItem('id_token');
  }
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
  updateUser ({ commit }, user) {
    return new Promise((resolve) => {
      UserApi.putUser(user, result => {
        commit(types.SET_USER, { result });
        resolve(user);
      });
    });
  },
  login ({ commit }, credentials) {
    return new Promise((resolve, reject) => {
      AuthApi.login(credentials, user => {
        window.localStorage.setItem('id_token', user.token);
        commit(types.SET_USER, { user });
        resolve(user);
      }, response => {
        reject(response.fieldErrors);
      });
    });
  },
  register ({ commit }, user_data) {
    return new Promise((resolve, reject) => {
      AuthApi.register(user_data, user => {
        window.localStorage.setItem('id_token', user.token);
        commit(types.SET_USER, { user });
        resolve(user);
      }, response => {
        reject(response.fieldErrors);
      });
    });
  },
  logout ({ commit }) {
    return new Promise((resolve) => {
      window.localStorage.removeItem('id_token');
      commit(types.SET_USER, { user: null });
      resolve();
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

import UserApi from 'api/user.api';
import AuthApi from 'api/auth.api';
import * as types from 'store/mutation-types';

const state = {
  user: null,
  loadingUser: false,
  notifications: [],
  authenticated: false
};

const getters = {
  user: state => {
    if (state.user && !state.user.address) {
      state.user.address = {};
    }

    return state.user;
  },
  locale: state => state.user ? state.user.locale : null,
  isAdmin: state => state.user != null && state.user.role != null && state.user.role.level >= 40,
  license: state => state.user ? state.user.license : null,
  notifications: state => state.notifications,
  authenticated: state => {
    return state.authenticated || window.localStorage.getItem('id_token') != null;
  },
  loadingUser: state => state.loadingUser
};

const actions = {
  initializeUser ({ commit }) {
    return new Promise((resolve, reject) => {
      commit(types.LOADING_USER, true);

      UserApi.getUser((user) => {
        commit(types.SET_USER, { user });
        commit(types.LOADING_USER, false);
        resolve(user);
      });
    });
  },
  updateUser ({ commit }, user) {
    return new Promise((resolve) => {
      commit(types.LOADING_USER, true);

      UserApi.putUser(user, result => {
        commit(types.SET_USER, { result });
        commit(types.LOADING_USER, false);
        resolve(user);
      });
    });
  },
  login ({ commit }, credentials) {
    return new Promise((resolve, reject) => {
      commit(types.LOADING_USER, true);

      AuthApi.login(credentials, user => {
        window.localStorage.setItem('id_token', user.token);
        commit(types.SET_USER, { user });
        commit(types.LOADING_USER, false);
        resolve(user);
      }, response => {
        reject(response.fieldErrors);
      });
    });
  },
  register ({ commit }, user_data) {
    return new Promise((resolve, reject) => {
      commit(types.LOADING_USER, true);

      AuthApi.register(user_data, user => {
        window.localStorage.setItem('id_token', user.token);
        commit(types.SET_USER, { user });
        commit(types.LOADING_USER, false);
        resolve(user);
      }, response => {
        reject(response.fieldErrors);
      });
    });
  },
  logout ({ commit }) {
    commit(types.LOADING_USER, true);

    return new Promise((resolve) => {
      window.localStorage.removeItem('id_token');
      commit(types.SET_USER, { user: null });
      commit(types.LOADING_USER, false);
      commit(types.SET_PAGES, { pages: [] });
      commit(types.SET_CONFERENCES, { conferences: [] });
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
    state.authenticated = user != null;
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
  },

  [types.LOADING_USER] (state, loading_state) {
    state.loadingUser = loading_state;
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};

import UserApi from 'api/user.api';
import * as types from 'store/mutation-types';

const state = {
  user: null
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
  license: state => state.user.license ? state.user.license : null
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
    // might be changed.
    commit(types.SET_USER, { user: null });
  },
  updateUser ({ commit }, user) {
    return new Promise((resolve) => {
      UserApi.putUser(user, result => {
        commit(types.SET_USER, { result });
        resolve(user);
      });
    });
  }
};

const mutations = {
  [types.SET_USER] (state, { user }) {
    state.user = user;
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};

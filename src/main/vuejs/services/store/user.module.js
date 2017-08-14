import UserApi from '../api/user.api.js';

const state = {
  user: null
};

const SET_USER = 'SET_USER';

const getters = {
  getUser: state => {
    if (state.user) {
      if (!state.user.address) {
        state.user.address = {};
      }
    }
    return state.user;
  },
  getLocale: state => state.user.locale,
  isAdmin: state => state.user != null && state.user.role != null && state.user.role.level >= 40,
  getLicense: state => state.user.license ? state.user.license : null
};

const actions = {
  initializeUser ({ commit }) {
    return new Promise((resolve, reject) => {
      UserApi.getUser((user) => {
        commit(SET_USER, { user });
        resolve();
      });
    });
  },
  setUser ({ commit }, user) {
    commit(SET_USER, { user });
  },
  updateUser ({ commit }, user) {
    commit(SET_USER, { user });
  }
};

const mutations = {
  [SET_USER] (state, { user }) {
    state.user = user;
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};

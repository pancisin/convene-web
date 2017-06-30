import UserApi from '../api/user.api.js';

const state = {
  user: null,
  pages: [],
  conferences: []
};

const getters = {
  getPages: state => state.pages,
  getConferences: state => state.conferences,
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
        commit('setUser', { user });
        resolve();
      });
    });
  },
  initializePages ({ commit }) {
    return new Promise((resolve, reject) => {
      UserApi.getPages((pages) => {
        commit('setPages', { pages });
        resolve();
      });
    });
  },
  initializeConferences ({ commit }) {
    return new Promise((resolve, reject) => {
      UserApi.getConferences((conferences) => {
        commit('setConferences', { conferences });
        resolve();
      });
    });
  },
  updateUser ({ commit }, user) {
    commit('setUser', { user });
  },
  addPage ({ commit }, page) {
    commit('addPage', { page });
  },
  removePage ({ commit }, page) {
    commit('removePage', { page });
  },
  updatePage ({ commit }, page) {
    commit('updatePage', { page });
  },
  addConference ({ commit }, conference) {
    commit('addConference', { conference });
  },
  removeConference ({ commit }, conference) {
    commit('removeConference', { conference });
  },
  updateConference ({ commit }, conference) {
    commit('updateConference', { conference });
  }
};

const mutations = {
  setUser: (state, { user }) => {
    state.user = user;
  },
  setPages: (state, { pages }) => {
    state.pages = pages;
  },
  setConferences: (state, { conferences }) => {
    state.conferences = conferences;
  },
  updatePage: (state, { page }) => {
    var index = null;
    state.pages.forEach((e, i) => {
      if (e.id === page.id) {
        index = i;
      }
    });

    state.pages.splice(index, 1, page);
  },
  addPage: (state, { page }) => {
    state.pages.push(page);
  },
  removePage: (state, { page }) => {
    state.pages = state.pages.filter(p => {
      return page.id !== p.id;
    });
  },
  addConference: (state, { conference }) => {
    state.conferences.push(conference);
  },
  removeConference: (state, { conference }) => {
    state.conferences = state.conferences.filter(c => {
      return conference.id !== c.id;
    });
  },
  updateConference: (state, { conference }) => {
    var index = null;
    state.conferences.forEach((c, i) => {
      if (c.id === conference.id) {
        index = i;
      }
    });

    state.conferences.splice(index, 1, conference);
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};

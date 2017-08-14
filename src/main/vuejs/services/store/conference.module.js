import UserApi from '../api/user.api';
import ConferenceApi from '../api/conference.api';
import * as types from './mutation-types';

const state = {
  conferences: [],
  loadingConferences: false
};

const getters = {
  conferences: state => state.conferences,
  loadingConferences: state => state.loadingConferences
};

const actions = {
  initializeConferences ({ commit }) {
    return new Promise((resolve) => {
      UserApi.getConferences((conferences) => {
        commit(types.SET_CONFERENCES, { conferences });
        commit(types.LOADING_CONFERENCES, false);
        resolve(conferences);
      });

      commit(types.LOADING_CONFERENCES, true);
    });
  },
  createConference ({ commit }, page) {
    return new Promise(resolve => {
      UserApi.postConference(page, result => {
        commit(types.ADD_CONFERENCE, { page: result });
        commit(types.LOADING_CONFERENCES, false);
        resolve(result);
      });

      commit(types.LOADING_CONFERENCES, true);
    });
  },
  deleteConference ({ commit }, page) {
    return new Promise(resolve => {
      UserApi.deleteConference(page.id, () => {
        commit(types.REMOVE_CONFERENCE, { page });
        commit(types.LOADING_CONFERENCES, false);
        resolve(page);
      });

      commit(types.LOADING_CONFERENCES, true);
    });
  },
  updateConference ({ commit }, page) {
    return new Promise(resolve => {
      ConferenceApi.putConference(page, result => {
        commit(types.UPDATE_CONFERENCE, { page: result });
        commit(types.LOADING_CONFERENCES, false);
        resolve(result);
      });

      commit(types.LOADING_CONFERENCES, true);
    });
  }
};

const mutations = {
  [types.SET_CONFERENCES] (state, { conferences }) {
    state.conferences = conferences;
  },

  [types.UPDATE_CONFERENCE] (state, { page }) {
    var index = null;
    state.conferences.forEach((e, i) => {
      if (e.id === page.id) {
        index = i;
      }
    });

    state.conferences.splice(index, 1, page);
  },

  [types.ADD_CONFERENCE] (state, { page }) {
    state.conferences.push(page);
  },

  [types.REMOVE_CONFERENCE] (state, { page }) {
    state.conferences = state.conferences.filter(p => {
      return page.id !== p.id;
    });
  },

  [types.LOADING_CONFERENCES] (state, loading_state) {
    state.loadingConferences = loading_state;
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};

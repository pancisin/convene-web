import UserApi from 'api/user.api';
import ConferenceApi from 'api/conference.api';
import * as types from 'store/mutation-types';

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
  createConference ({ commit }, conference) {
    return new Promise(resolve => {
      UserApi.postConference(conference, result => {
        commit(types.ADD_CONFERENCE, { conference: result });
        commit(types.LOADING_CONFERENCES, false);
        resolve(result);
      });

      commit(types.LOADING_CONFERENCES, true);
    });
  },
  deleteConference ({ commit }, conference) {
    return new Promise(resolve => {
      UserApi.deleteConference(conference.id, () => {
        commit(types.REMOVE_CONFERENCE, { conference });
        commit(types.LOADING_CONFERENCES, false);
        resolve(conference);
      });

      commit(types.LOADING_CONFERENCES, true);
    });
  },
  updateConference ({ commit }, conference) {
    return new Promise(resolve => {
      ConferenceApi.putConference(conference, result => {
        commit(types.UPDATE_CONFERENCE, { conference: result });
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

  [types.UPDATE_CONFERENCE] (state, { conference }) {
    var index = null;
    state.conferences.forEach((e, i) => {
      if (e.id === conference.id) {
        index = i;
      }
    });

    state.conferences.splice(index, 1, conference);
  },

  [types.ADD_CONFERENCE] (state, { conference }) {
    state.conferences.push(conference);
  },

  [types.REMOVE_CONFERENCE] (state, { conference }) {
    state.conferences = state.conferences.filter(p => {
      return conference.id !== p.id;
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

import UserApi from '../api/user.api';
import ConferenceApi from '../api/conference.api';

const state = {
  conferences: [],
  loadingConferences: false
};

const getters = {
  conferences: state => state.conferences,
  loadingConferences: state => state.loadingConferences
};

const SET_CONFERENCES = 'SET_CONFERENCES';
const LOADING_CONFERENCES = 'LOADING_CONFERENCES';
const UPDATE_CONFERENCE = 'UPDATE_CONFERENCE';
const ADD_CONFERENCE = 'ADD_CONFERENCE';
const REMOVE_CONFERENCE = 'REMOVE_CONFERENCE';

const actions = {
  initializeConferences ({ commit }) {
    UserApi.getConferences((conferences) => {
      commit(SET_CONFERENCES, { conferences });
      commit(LOADING_CONFERENCES, false);
    });

    commit(LOADING_CONFERENCES, true);
  },
  createConference ({ commit }, page) {
    return new Promise(resolve => {
      UserApi.postConference(page, result => {
        commit(ADD_CONFERENCE, { page: result });
        commit(LOADING_CONFERENCES, false);
        resolve(result);
      });

      commit(LOADING_CONFERENCES, true);
    });
  },
  deleteConference ({ commit }, page) {
    return new Promise(resolve => {
      UserApi.deleteConference(page.id, () => {
        commit(REMOVE_CONFERENCE, { page });
        commit(LOADING_CONFERENCES, false);
        resolve(page);
      });

      commit(LOADING_CONFERENCES, true);
    });
  },
  updateConference ({ commit }, page) {
    return new Promise(resolve => {
      ConferenceApi.putConference(page, result => {
        commit(UPDATE_CONFERENCE, { page: result });
        commit(LOADING_CONFERENCES, false);
        resolve(result);
      });

      commit(LOADING_CONFERENCES, true);
    });
  }
};

const mutations = {
  [SET_CONFERENCES] (state, { conferences }) {
    state.conferences = conferences;
  },

  [UPDATE_CONFERENCE] (state, { page }) {
    var index = null;
    state.conferences.forEach((e, i) => {
      if (e.id === page.id) {
        index = i;
      }
    });

    state.conferences.splice(index, 1, page);
  },

  [ADD_CONFERENCE] (state, { page }) {
    state.conferences.push(page);
  },

  [REMOVE_CONFERENCE] (state, { page }) {
    state.conferences = state.conferences.filter(p => {
      return page.id !== p.id;
    });
  },

  [LOADING_CONFERENCES] (state, loading_state) {
    state.loadingConferences = loading_state;
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};

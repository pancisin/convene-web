import * as types from 'store/mutation-types';

const state = {
  position: null
};

const getters = {
  position: state => state.position,
  positionKnown: state => state.position != null
};

const actions = {
  initializeMachine ({ commit }) {
    const position = JSON.parse(window.localStorage.getItem('position'));
    if (position != null) {
      commit(types.SET_POSITION, {position});
    }
  },
  setPosition ({ commit }, position) {
    window.localStorage.setItem('position', JSON.stringify(position));
    commit(types.SET_POSITION, { position });
  }
};

const mutations = {
  [types.SET_POSITION] (state, { position }) {
    state.position = position;
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};

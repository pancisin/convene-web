import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

import user from './modules/user';
import page from './modules/page';
import conference from './modules/conference';

import * as types from 'store/mutation-types';

const store = new Vuex.Store({
  state: {
    toasts: []
  },
  modules: {
    user, page, conference
  },
  getters: {
    toasts: state => state.toasts
  },
  actions: {
    addToast ({ commit }, toast) {
      commit(types.ADD_TOAST, { toast });

      if (toast.type !== 'prompt') {
        setTimeout(() => {
          commit(types.REMOVE_TOAST, { toast });
        }, 5000);
      }
    },
    removeToast ({ commit }, toast) {
      commit(types.REMOVE_TOAST, { toast });
    }
  },
  mutations: {
    [types.ADD_TOAST] (state, { toast }) {
      state.toasts.push(toast);
    },

    [types.REMOVE_TOAST] (state, { toast }) {
      state.toasts.splice(state.toasts.indexOf(toast), 1);
    }
  }
});

export default store;

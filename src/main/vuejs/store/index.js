import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

// IMPORT MODULES
import user from './modules/user';
import page from './modules/page';
import conference from './modules/conference';

// IMPORT MUTATION TYPES
import * as types from 'store/mutation-types';

// IMPORT APIS
import PublicApi from 'api/public.api';

const store = new Vuex.Store({
  state: {
    toasts: [],
    locales: []
  },
  modules: {
    user, page, conference
  },
  getters: {
    toasts: state => state.toasts,
    locales: state => state.locales
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
    },

    initializeLocales ({ commit }) {
      PublicApi.getLocales(locales => {
        commit(types.SET_LOCALES, { locales });
      });
    }
  },
  mutations: {
    [types.ADD_TOAST] (state, { toast }) {
      state.toasts.push(toast);
    },

    [types.REMOVE_TOAST] (state, { toast }) {
      state.toasts.splice(state.toasts.indexOf(toast), 1);
    },

    [types.SET_LOCALES] (state, { locales }) {
      state.locales = locales;
    }
  }
});

[user, page, conference].forEach((module) => {
  if (module.watchers) {
    module.watchers.forEach((params) => {
      store.watch(...params);
    });
  }
});

export default store;

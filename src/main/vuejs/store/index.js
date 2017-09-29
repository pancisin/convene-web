import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

import user from './modules/user';
import page from './modules/page';
import conference from './modules/conference';

import * as types from 'store/mutation-types';

import PublicApi from 'api/public.api';

const store = new Vuex.Store({
  state: {
    toasts: [],
    categories: [],
    loadingCategories: false
  },
  modules: {
    user, page, conference
  },
  getters: {
    toasts: state => state.toasts,
    categories: state => state.categories,
    getBranchesByCategoryId: state => id => {
      let branches = [];

      state.categories.forEach(c => {
        if (c.id === id) {
          branches = c.branches;
        }
      });

      return branches;
    }
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
    initializeCategories ({ commit }) {
      PublicApi.getCategories(categories => {
        commit(types.SET_CATEGORIES, { categories });
      });
    },
    initalizeBranches ({ commit }, category_id) {
      PublicApi.getBranches(category_id, branches => {
        commit(types.SET_BRANCHES, { category_id, branches });
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

    [types.SET_CATEGORIES] (state, { categories }) {
      state.categories = categories;
    },

    [types.SET_BRANCHES] (state, { category_id, branches }) {
      state.categories.forEach((c, index) => {
        if (c.id === category_id) {
          state.categories.splice(index, 1, {
            ...c,
            branches
          });
        }
      });

    }
  }
});

export default store;

import UserApi from 'api/user.api';
import PageApi from 'api/page.api';
import * as types from 'store/mutation-types';

const state = {
  pages: [],
  loadingPages: false
};

const getters = {
  pages: state => state.pages,
  loadingPages: state => state.loadingPages,
  getPageById: state => id => {
    let index = state.pages.findIndex(p => {
      return p.id === id;
    });

    if (index !== -1) {
      return state.pages[index];
    } else return null;
  }
};

const actions = {
  initializePages ({ commit }) {
    UserApi.getPages((pages) => {
      commit(types.SET_PAGES, { pages });
      commit(types.LOADING_PAGES, false);
    });

    commit(types.LOADING_PAGES, true);
  },
  createPage ({ commit }, page) {
    return new Promise((resolve, reject) => {
      UserApi.postPage(page, result => {
        commit(types.ADD_PAGE, { page: result });
        commit(types.LOADING_PAGES, false);
        resolve(result);
      });

      commit(types.LOADING_PAGES, true);
    });
  },
  deletePage ({ commit }, page) {
    return new Promise((resolve) => {
      UserApi.deletePage(page.id, () => {
        commit(types.REMOVE_PAGE, { page });
        commit(types.LOADING_PAGES, false);
        resolve(page);
      });

      commit(types.LOADING_PAGES, true);
    });
  },
  updatePage ({ commit }, page) {
    return new Promise((resolve) => {
      PageApi.putPage(page, result => {
        commit(types.UPDATE_PAGE, { page: result });
        commit(types.LOADING_PAGES, false);
        resolve(result);
      });

      commit(types.LOADING_PAGES, true);
    });
  },
  togglePagePublished ({ commit }, page) {
    PageApi.togglePublished(page.id, result => {
      commit(types.UPDATE_PAGE, { page: result });
      commit(types.LOADING_PAGES, false);
    });

    commit(types.LOADING_PAGES, true);
  }
};

const mutations = {
  [types.SET_PAGES] (state, { pages }) {
    state.pages = pages;
  },

  [types.UPDATE_PAGE] (state, { page }) {
    var index = null;
    state.pages.forEach((e, i) => {
      if (e.id === page.id) {
        index = i;
      }
    });

    state.pages.splice(index, 1, page);
  },

  [types.ADD_PAGE] (state, { page }) {
    state.pages.push(page);
  },

  [types.REMOVE_PAGE] (state, { page }) {
    state.pages = state.pages.filter(p => {
      return page.id !== p.id;
    });
  },

  [types.LOADING_PAGES] (state, loading_state) {
    state.loadingPages = loading_state;
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};

import UserApi from '../api/user.api.js';
import PageApi from '../api/page.api.js';

const state = {
  pages: [],
  loadingPages: false
};

const getters = {
  pages: state => state.pages,
  loadingPages: state => state.loadingPages
};

const SET_PAGES = 'SET_PAGES';
const LOADING_PAGES = 'LOADING_PAGES';
const UPDATE_PAGE = 'UPDATE_PAGE';
const ADD_PAGE = 'ADD_PAGE';
const REMOVE_PAGE = 'REMOVE_PAGE';

const actions = {
  initializePages ({ commit }) {
    UserApi.getPages((pages) => {
      commit(SET_PAGES, { pages });
      commit(LOADING_PAGES, false);
    });

    commit(LOADING_PAGES, true);
  },
  createPage ({ commit }, page) {
    return new Promise((resolve, reject) => {
      UserApi.postPage(page, result => {
        commit(ADD_PAGE, { page: result });
        commit(LOADING_PAGES, false);
        resolve(result);
      });

      commit(LOADING_PAGES, true);
    });
  },
  deletePage ({ commit }, page) {
    return new Promise((resolve) => {
      UserApi.deletePage(page.id, () => {
        commit(REMOVE_PAGE, { page });
        commit(LOADING_PAGES, false);
        resolve(page);
      });

      commit(LOADING_PAGES, true);
    });
  },
  updatePage ({ commit }, page) {
    return new Promise((resolve) => {
      PageApi.putPage(page, result => {
        commit(UPDATE_PAGE, { page: result });
        commit(LOADING_PAGES, false);
        resolve(result);
      });

      commit(LOADING_PAGES, true);
    });
  },
  publishPage ({ commit }, page_id) {
    PageApi.publishPage(page_id, result => {
      commit(UPDATE_PAGE, { page: result });
      commit(LOADING_PAGES, false);
    });

    commit(LOADING_PAGES, true);
  },
  deactivatePage ({ commit }, page_id) {
    PageApi.deactivatePage(page_id, result => {
      commit(UPDATE_PAGE, { page: result });
      commit(LOADING_PAGES, false);
    });

    commit(LOADING_PAGES, true);
  }
};

const mutations = {
  [SET_PAGES] (state, { pages }) {
    state.pages = pages;
  },

  [UPDATE_PAGE] (state, { page }) {
    var index = null;
    state.pages.forEach((e, i) => {
      if (e.id === page.id) {
        index = i;
      }
    });

    state.pages.splice(index, 1, page);
  },

  [ADD_PAGE] (state, { page }) {
    state.pages.push(page);
  },

  [REMOVE_PAGE] (state, { page }) {
    state.pages = state.pages.filter(p => {
      return page.id !== p.id;
    });
  },

  [LOADING_PAGES] (state, loading_state) {
    state.loadingPages = loading_state;
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};

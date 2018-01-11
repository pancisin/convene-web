import UserApi from 'api/user.api';
import PageApi from 'api/page.api';
import PublicApi from 'api/public.api';
import * as types from 'store/mutation-types';

const state = {
  pages: [],
  loadingPages: false,
  categories: [],
  loadingCategories: false,
  loadingBranches: false
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
  },
  categories: state => state.categories,
  getBranchesByCategoryId: state => id => {
    let branches = [];

    state.categories.forEach(c => {
      if (c.id === id) {
        branches = c.branches;
      }
    });

    return branches;
  },
  loadingCategories: state => state.loadingCategories,
  loadingBranches: state => state.loadingBranches
};

const actions = {
  initializePages ({ commit }) {
    UserApi.getPages((pages) => {
      commit(types.SET_PAGES, { pages });
      commit(types.LOADING_PAGES, false);
    });

    commit(types.LOADING_PAGES, true);
  },
  updatePages ({ commit, state }, page) {
    if (state.pages.every(p => p.id !== page.id)) {
      commit(types.ADD_PAGE, { page });
    } else {
      commit(types.UPDATE_PAGE, { page });
    }
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
      PageApi.deletePage(page.id, () => {
        commit(types.REMOVE_PAGE, { page });
        commit(types.LOADING_PAGES, false);
        resolve(page);
      });

      commit(types.LOADING_PAGES, true);
    });
  },
  updatePage ({ commit }, page) {
    return new Promise((resolve) => {
      PageApi.putPage(page.id, page, result => {
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
  },
  initializeCategories ({ commit }) {
    commit(types.SET_CATEGORIES_LOADING, true);

    PublicApi.getCategories({ used: false }, categories => {
      commit(types.SET_CATEGORIES, { categories });
      commit(types.SET_CATEGORIES_LOADING, false);
    });
  },
  initalizeBranches ({ commit }, category_id) {
    commit(types.SET_BRANCHES_LOADING, true);

    PublicApi.getBranches(category_id, { used: false }, branches => {
      commit(types.SET_BRANCHES, { category_id, branches });
      commit(types.SET_BRANCHES_LOADING, false);
    });
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
  },

  [types.SET_CATEGORIES_LOADING] (state, value) {
    state.loadingCategories = value;
  },

  [types.SET_BRANCHES_LOADING] (state, value) {
    state.loadingBranches = value;
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};

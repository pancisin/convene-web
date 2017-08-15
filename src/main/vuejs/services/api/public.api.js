import Vue from 'vue';

export default {

  /**
   * Get all public popular pages.
   * @param {Function} success - success callback function
   */
  getPopularPages (success) {
    Vue.http.get('api/public/popular-pages/').then(response => {
      success(response.body);
    });
  },

  /**
   * Get all page categories.
   * @param {Function} success - success callback function
   */
  getCategories (success) {
    Vue.http.get('api/categories').then(response => {
      success(response.body);
    });
  },

  /**
   * Get all page branches for category.
   * @param {Integer} category_id - category id
   * @param {Function} success - success callback function
   */
  getBranches (category_id, success) {
    var url = ['api/categories', category_id, 'branches'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  }
};

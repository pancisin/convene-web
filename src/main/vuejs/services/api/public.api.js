import Vue from 'vue';
import moment from 'moment';
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

  getPage (id, success) {
    Vue.http.get(`public/page/${id}`).then(response => {
      success(response.body);
    });
  },

  getEvent (id, success) {
    Vue.http.get(`public/event/${id}`).then(response => {
      success(response.body);
    });
  },
 
  page: {
    getEvents (page_id, page, size, success) {
      Vue.http.get(`public/page/${page_id}/event/${page}/${size}`, {
        params: {
          fromDate: moment().format('YYYY-MM-DD'),
          orderBy: 'date ASC'
        }
      }).then(response => {
        success(response.body);
      });
    },

    getServices (id, success) {
      Vue.http.get(`public/page/${id}/service`).then(response => {
        success(response.body);
      });
    }
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

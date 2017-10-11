import Vue from 'vue';

export default {

  /**
   * Get conferences
   * @param {*} page - paginator page property
   * @param {*} size - paginator size property
   * @param {*} success - success callback function
   */
  getConferences (page, size, success) {
    Vue.http.get(`api/conferences/${page}/${size}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get all users for system maintanance
   * @param {*} page - paginator page attribute
   * @param {*} size - paginator size attribute
   * @param {*} success - success callback function
   */
  getUsers (page, size, success) {
    Vue.http.get(`api/user`, {
      params: {
        page, size
      }
    }).then(response => {
      success(response.body);
    });
  },

  /**
   * Get Unit enum vales
   * @param {*} success - success callback function
   */
  getUnits (success) {
    Vue.http.get('api/enum/unit').then(response => {
      success(response.body);
    });
  },

  getHeadlines (language, success) {
    Vue.http.get('api/articles', {
      params: {
        tags: `language:${language || 'en'}, headlines`,
        page: 0,
        size: 6
      }
    }).then(response => {
      success(response.body);
    });
  }
};

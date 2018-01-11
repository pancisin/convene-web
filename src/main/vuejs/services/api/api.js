import Vue from 'vue';

export default {

   /**
   * Get pages
   * @param {*} page - paginator page attribute
   * @param {*} size - paginator size attribute
   * @param {*} filters - filters
   * @param {*} success - success callback function
   */
  getPages (page, size, filters, success) {
    Vue.http.get(`/api/v1/pages/${page}/${size}`, {
      params: {
        ...filters
      }
    }).then(response => {
      success(response.body);
    });
  },

  /**
   * Get conferences
   * @param {*} page - paginator page property
   * @param {*} size - paginator size property
   * @param {*} success - success callback function
   */
  getConferences (page, size, success) {
    Vue.http.get(`/api/v1/conferences/${page}/${size}`).then(response => {
      success(response.body);
    });
  },

  getEvents (page, size, filters, success) {
    Vue.http.get(`/api/v1/events/${page}/${size}`, {
      params: filters
    }).then(response => {
      success(response.body);
    });
  },

  /**
   * Get all system articles lists
   * @param {*} success - success callback function
   */
  getArticlesLists (success) {
    Vue.http.get('/api/articles-list').then(response => {
      success(response.body);
    });
  },

  /**
   * Create new system articles list element
   * @param {*} articlesList - articles list data object
   * @param {*} success - success callback function
   */
  postArticlesList (articlesList, success) {
    Vue.http.post('/api/articles-list', articlesList).then(response => {
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
    Vue.http.get('/api/user', {
      params: {
        page, size
      }
    }).then(response => {
      success(response.body);
    });
  },

  getHeadlines (language, page, size, success, error) {
    Vue.http.get('/api/v1/articles', {
      params: {
        tags: `language:${language || 'en'}, headlines`,
        page: page || 0,
        size: size || 10
      }
    }).then(response => {
      success(response.body);
    }, errors => {
      if (error != null) {
        error(errors);
      }
    });
  }
};

import Vue from 'vue';
const ARTICLES_LIST_API_URL = '/api/v1/articles-list';
export default {

  /**
   * Update articles list object element
   * @param {*} id - articles list id
   * @param {*} articlesList - articles list data object
   * @param {*} success - success callback function
   */
  putArticlesList (id, articlesList, success) {
    Vue.http.put(`${ARTICLES_LIST_API_URL}/${id}`, articlesList).then(response => {
      success(response.body);
    });
  },

  /**
   * Get single articles list element
   * @param {*} id - articles list id
   * @param {*} success - success callback function
   */
  getArticlesList (id, success) {
    Vue.http.get(`${ARTICLES_LIST_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Post new article to articles list
   * @param {*} id - articles list id
   * @param {*} article - article data object
   * @param {*} success - success callback function
   */
  postArticle (id, article, success) {
    Vue.http.post(`${ARTICLES_LIST_API_URL}/${id}/article`, article).then(response => {
      success(response.body);
    });
  },

  /**
   * Get all articles with relations to articles list
   * @param {*} id - articles list id
   * @param {*} success - success callback function
   */
  getArticles (id, page, size, success) {
    Vue.http.get(`${ARTICLES_LIST_API_URL}/${id}/article/${page}/${size}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Create new article bot for list specified.
   * @param {*} id - articles list id
   * @param {*} articleBot - article bot data object
   * @param {*} success - success callback function
   */
  postBot (id, articleBot, success) {
    Vue.http.post(`${ARTICLES_LIST_API_URL}/${id}/bot`, articleBot).then(response => {
      success(response.body);
    });
  },

  /**
   * Get all article bots for list specified
   * @param {*} id - articles list id
   * @param {*} success - success callback function
   */
  getBots (id, page, size, success) {
    Vue.http.get(`${ARTICLES_LIST_API_URL}/${id}/bot/${page}/${size}`).then(response => {
      success(response.body);
    });
  }
};

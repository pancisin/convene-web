import Vue from 'vue';
const ARTICLES_LIST_API_URL = 'api/articles-list';
export default {

  /**
   * Get all system articles lists
   * @param {*} success - success callback function
   */
  getArticlesLists (success) {
    Vue.http.get(`${ARTICLES_LIST_API_URL}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Create new system articles list element
   * @param {*} articlesList - articles list data object
   * @param {*} success - success callback function
   */
  postArticlesList (articlesList, success) {
    Vue.http.post(ARTICLES_LIST_API_URL, articlesList).then(response => {
      success(response.body);
    });
  },

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
  getArticles (id, success) {
    Vue.http.get(`${ARTICLES_LIST_API_URL}/${id}/article`).then(response => {
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
  getBots (id, success) {
    Vue.http.get(`${ARTICLES_LIST_API_URL}/${id}/bot`).then(response => {
      success(response.body);
    });
  }
};

import Vue from 'vue';

const ARTICLE_BOT_API_URL = 'api/article-bot';

export default {

  /**
   * Get article bot by uuid/id
   * @param {*} id - article bot uuid
   * @param {*} success - success callback function
   */
  getArticleBot (id, success) {
    Vue.http.get(`${ARTICLE_BOT_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Update article bot
   * @param {*} id - article bot id
   * @param {*} articleBot - article bot data object
   * @param {*} success - success callback function
   */
  putArticleBot (id, articleBot, success) {
    Vue.http.put(`${ARTICLE_BOT_API_URL}/${id}`, articleBot).then(response => {
      success(response.body);
    });
  },

  /**
   * Completely delete article bot object from database
   * @param {*} id - article bot id/uuid
   * @param {*} success - success callback function
   */
  deleteArticleBot (id, success) {
    Vue.http.delete(`${ARTICLE_BOT_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Toggle active state of article bot
   * @param {*} id - article bot id/uuid
   * @param {*} success - success callback function
   */
  toggleActive (id, success) {
    Vue.http.patch(`${ARTICLE_BOT_API_URL}/${id}/toggle-active`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get all recent runs
   * @param {*} id - article bot id
   * @param {*} success - success callback function
   */
  run (id, success) {
    Vue.http.get(`${ARTICLE_BOT_API_URL}/${id}/run`).then(response => {
      success(response.body);
    });
  }
};

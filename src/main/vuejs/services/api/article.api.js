import Vue from 'vue';
const ARTICLE_API_URL = 'api/article';
export default {

  /**
   * Get article
   * @param {*} id - article id
   * @param {*} success - success callback function
   */
  getArticle (id, success) {
    if (id == null) {
      success({});
    }

    Vue.http.get(`${ARTICLE_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Update article
   * @param {*} id - article id
   * @param {*} article - article data object
   * @param {*} success - success callback function
   */
  putArticle (id, article, success) {
    Vue.http.put(`${ARTICLE_API_URL}/${id}`, article).then(response => {
      success(response.body);
    });
  },

  /**
   * Toggle published state of article to published
   * @param {*} id - article id
   * @param {*} success - success callback function
   */
  togglePublished (id, success) {
    Vue.http.patch(`${ARTICLE_API_URL}/${id}/toggle-published`).then(response => {
      success(response.body);
    });
  },

  /**
   * Delete article
   * @param {*} id - article id
   * @param {*} success - success callback function
   */
  deleteArticle (id, success) {
    Vue.http.delete(`${ARTICLE_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  }
};

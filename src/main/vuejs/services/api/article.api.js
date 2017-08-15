import Vue from 'vue';
const ARTICLE_API_URL = 'api/article';
export default {
  getArticle (id, success) {
    if (id == null) {
      success({});
    }

    Vue.http.get(`${ARTICLE_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },
  putArticle (id, article, success) {
    Vue.http.put(`${ARTICLE_API_URL}/${id}`, article).then(response => {
      success(response.body);
    });
  },
  togglePublished (id, success) {
    Vue.http.patch(`${ARTICLE_API_URL}/${id}/toggle-published`).then(response => {
      success(response.body);
    });
  },
  deleteArticle (id, success) {
    Vue.http.delete(`${ARTICLE_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  }
};

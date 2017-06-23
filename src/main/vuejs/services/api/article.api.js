import Vue from 'vue'

export default {
  getArticle(id, success) {
    if (id == null)
      success(new Object());

    Vue.http.get(`api/article/${id}`).then(response => {
      success(response.body);
    })
  },
  putArticle(id, article, success) {
    Vue.http.put(`api/article/${id}`, article).then(response => {
      success(response.body);
    })
  },
  togglePublished(id, success) {
    Vue.http.patch(`api/article/${id}/toggle-published`).then(response => {
      success(response.body);
    })
  }
}
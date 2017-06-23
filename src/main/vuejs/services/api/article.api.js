import Vue from 'vue'

export default {
  getArticle(id, success) {
    Vue.http.get(`api/article/${id}`).then(response => {
      success(response.body);
    })
  },
  putArticle(id, article, success) {
    Vue.http.put(`api/article/${id}`, article).then(response => {
      success(response.body);
    })
  },
}
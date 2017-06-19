import Vue from 'vue';

export default {
  getPopularPages(success) {
    Vue.http.get('api/public/popular-pages/').then(response => {
      success(response.body);
    })
  },
  getCategories(success) {
    Vue.http.get('api/categories').then(response => {
      success(response.body);
    })
  },
  getBranches(category_id, success) {
    var url = ['api/categories', category_id, 'branches'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  }
}
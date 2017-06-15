import Vue from 'vue';

export default { 
  getPopularPages(success) {
    Vue.http.get('api/public/popular-pages/').then(response => {
      success(response.body);
    })
  },
}
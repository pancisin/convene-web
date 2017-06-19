import Vue from 'vue';

export default {
  getPage(id, success) {
    Vue.http.get('api/page/' + id).then(response => {
      success(response.body);
    })
  },
  getEvents(id, page, size, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = ['api/page', id, 'event', page, size].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  }
}
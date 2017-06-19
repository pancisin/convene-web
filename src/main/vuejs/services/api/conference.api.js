import Vue from 'vue';

export default {
  getConference(id, success) {
    Vue.http.get('api/conference/' + id).then(response => {
      success(response.body);
    })
  },
  getEvents(id, page, size, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = ['api/conference', id, 'event', page, size].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  }
}
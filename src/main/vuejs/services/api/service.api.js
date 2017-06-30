import Vue from 'vue';

export default {
  getService (id, success) {
    Vue.http.get('api/service/' + id).then(response => {
      success(response.body);
    });
  },
  putService (service, success) {
    Vue.http.put('api/service/' + service.id, service).then(response => {
      success(response.body);
    });
  },
  deleteService (id, success) {
    Vue.http.delete('api/service/' + id).then(response => {
      success(response.body);
    });
  }
};

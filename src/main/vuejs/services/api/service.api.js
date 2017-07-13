import Vue from 'vue';

function checkId (id) {
  if (id == null || id === '') throw new Error('missing entity id');
}

export default {
  getService (id, success) {
    checkId(id);
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
    checkId(id);
    Vue.http.delete('api/service/' + id).then(response => {
      success(response.body);
    });
  }
};

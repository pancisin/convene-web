import Vue from 'vue';

export default {
  getMetaTypes(success) {
    Vue.http.get('public/meta-types').then(response => {
      success(response.body);
    })
  },
  putMetaField(field, success) {
    var url = `api/meta-field/${field.id}`;
    Vue.http.put(url, field).then(response => {
      success(response.body);
    })
  },
  deleteMetaField(id, success) {
    var url = `api/meta-field/${id}`;
    Vue.http.delete(url).then(response => {
      success(response.body);
    })
  }
} 
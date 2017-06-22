import Vue from 'vue';

export default {
  getMetaTypes(success) {
    Vue.http.get('public/meta-types').then(response => {
      success(response.body);
    })
  },
  putMetaField(field, success) {
    Vue.http.put('api/meta-field/' + field.id, field).then(response => {
      success(response.body);
    })
  }
} 
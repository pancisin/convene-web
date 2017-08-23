import Vue from 'vue';

export default {

  /**
   * Get meta types
   * @param {*} success - success callback function
   */
  getMetaTypes (success) {
    Vue.http.get('public/meta-types').then(response => {
      success(response.body);
    });
  },

  /**
   * Update meta field
   * @param {*} field - field data object
   * @param {*} success - success callback function
   */
  putMetaField (field, success) {
    var url = `api/meta-field/${field.id}`;
    Vue.http.put(url, field).then(response => {
      success(response.body);
    });
  },

  /**
   * Delete meta field
   * @param {*} id - meta field id
   * @param {*} success - success callback function
   */
  deleteMetaField (id, success) {
    var url = `api/meta-field/${id}`;
    Vue.http.delete(url).then(response => {
      success(response.body);
    });
  }
};

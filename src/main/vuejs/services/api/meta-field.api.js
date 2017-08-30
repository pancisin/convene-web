import Vue from 'vue';

const META_FIELD_API_URL = 'api/meta-field';

export default {

  /**
   * Update meta field
   * @param {*} field - field data object
   * @param {*} success - success callback function
   */
  putMetaField (field, success) {
    Vue.http.put(`${META_FIELD_API_URL}/${field.id}`, field).then(response => {
      success(response.body);
    });
  },

  /**
   * Delete meta field
   * @param {*} id - meta field id
   * @param {*} success - success callback function
   */
  deleteMetaField (id, success) {
    Vue.http.delete(`${META_FIELD_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  }
};

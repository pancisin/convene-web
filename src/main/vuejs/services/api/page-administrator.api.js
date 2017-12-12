import Vue from 'vue';

export default {

  /**
   * Update page administrator object (relation to page as administrator)
   * @param {*} administrator_id - page administrator id
   * @param {*} administrator - page administrator data object
   * @param {*} success - success callback function
   */
  putAdministrator (administrator_id, administrator, success) {
    Vue.http.put(`/api/page-administrator/${administrator_id}`, administrator).then(response => {
      success(response.body);
    });
  },

  /**
   * Delete page administrator object
   * @param {*} administrator_id - page administrator id
   * @param {*} success - success callback function
   */
  deleteAdministrator (administrator_id, success) {
    Vue.http.delete(`/api/page-administrator/${administrator_id}`).then(response => {
      success(response.body);
    });
  }
};

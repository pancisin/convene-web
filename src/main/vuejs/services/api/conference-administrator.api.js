import Vue from 'vue';

export default {
  /**
   * Update conference administrator.
   * @param {*} administrator_id - conference administrator id
   * @param {*} administrator - administrator data object
   * @param {*} success - success callback function
   */
  putAdministrator (administrator_id, administrator, success) {
    Vue.http.put(`/api/conference-administrator/${administrator_id}`, administrator).then(response => {
      success(response.body);
    });
  },

  /**
   * Delete conference adminsitrator.
   * @param {*} administrator_id - conference administrator id
   * @param {*} success - success callback function
   */
  deleteAdministrator (administrator_id, success) {
    Vue.http.delete(`/api/conference-administrator/${administrator_id}`).then(response => {
      success(response.body);
    });
  }
};

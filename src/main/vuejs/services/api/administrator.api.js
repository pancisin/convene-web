import Vue from 'vue';

const ADMINISTRATOR_API_URL = '/api/v1/administrator';

export default {

  /**
   * Get administrator
   * @param {*} administrator_id - administrator id
   * @param {*} success - success callback function
   */
  getAdministrator (administrator_id, success) {
    Vue.http.get(`${ADMINISTRATOR_API_URL}/${administrator_id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Update administrator object
   * @param {*} administrator_id - administrator id
   * @param {*} administrator - administrator data object
   * @param {*} success - success callback function
   */
  putAdministrator (administrator_id, administrator, success) {
    Vue.http.put(`${ADMINISTRATOR_API_URL}/${administrator_id}`, administrator).then(response => {
      success(response.body);
    });
  },

  /**
   * Delete administrator object
   * @param {*} administrator_id - administrator id
   * @param {*} success - success callback function
   */
  deleteAdministrator (administrator_id, success) {
    Vue.http.delete(`${ADMINISTRATOR_API_URL}/${administrator_id}`).then(response => {
      success(response.body);
    });
  }
};

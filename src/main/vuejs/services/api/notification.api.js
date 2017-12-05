import Vue from 'vue';

const NOTIFICATION_API_URL = '/api/notification';

export default {

  /**
   * Toggle notification seen state
   * @param {*} notification_id - notification id
   * @param {*} success - success callback function
   */
  toggleSeen (notification_id, success) {
    Vue.http.patch(`${NOTIFICATION_API_URL}/${notification_id}/toggle-seen`).then(response => {
      success(response.body);
    });
  }
};

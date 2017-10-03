import Vue from 'vue';

const EVENT_BOT_API_URL = 'api/event-bot';

export default {

  /**
   * Get event bot by uuid/id
   * @param {*} id - event bot uuid
   * @param {*} success - success callback function
   */
  getEventBot (id, success) {
    Vue.http.get(`${EVENT_BOT_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Completely delete event bot object from database
   * @param {*} id - event bot id/uuid
   * @param {*} success - success callback function
   */
  deleteEventBot (id, success) {
    Vue.http.delete(`${EVENT_BOT_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Toggle active state of event bot
   * @param {*} id - event bot id/uuid
   * @param {*} success - success callback function
   */
  toggleActive (id, success) {
    Vue.http.patch(`${EVENT_BOT_API_URL}/${id}/toggle-active`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get all recent runs
   * @param {*} id - event bot id
   * @param {*} success - success callback function
   */
  getRuns (id, success) {
    Vue.http.get(`${EVENT_BOT_API_URL}/${id}/run`).then(response => {
      success(response.body);
    });
  }
};

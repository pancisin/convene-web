import Vue from 'vue';

const MEDIA_API_URL = 'api/media';

export default {

  /**
   * Get media item
   * @param {*} uuid - media item uuid
   * @param {*} success - success callback function
   */
  getMedia (uuid, success) {
    Vue.http.get(`${MEDIA_API_URL}/${uuid}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Delete media item
   * @param {*} uuid - media item uuid
   * @param {*} success - success callback function
   */
  deleteMedia (uuid, success) {
    Vue.http.delete(`${MEDIA_API_URL}/${uuid}`).then(response => {
      success(response.body);
    });
  }
};

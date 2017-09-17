import Vue from 'vue';

const PLACE_API_URL = 'api/place';

export default {

  /**
   * Get place by id
   * @param {*} id - place id
   * @param {*} success - success callback function
   */
  getPlace(id, success) {
    Vue.http.get(`${PLACE_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Delete place
   * @param {*} id - place id
   * @param {*} success - success callback function
   */
  deletePlace(id, success) {
    Vue.http.delete(`${PLACE_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Update place data
   * @param {*} id - place id
   * @param {*} place - place data object
   * @param {*} success - success callback function
   */
  putPlace(id, place, success) {
    Vue.http.put(`${PLACE_API_URL}/${id}`, place).then(response => {
      success(response.body);
    });
  },

  /**
   * Update place venue graphic data
   * @param {*} id - place id
   * @param {*} venue_data - venue data object
   * @param {*} success - success callback function
   */
  patchVenue(id, venue_data, success) {
    Vue.http.patch(`${PLACE_API_URL}/${id}/venue`, venue_data).then(response => {
      success(response.body);
    });
  },

  /**
   * Get venue data by place id
   * @param {*} id - place id
   * @param {*} success - success callback function
   */
  getVenue(id, success) {},

  /**
   * Get gallery images
   * @param {*} id - place id
   * @param {*} success - success callback function
   */
  getGallery(id, success) {
    Vue.http.get(`${PLACE_API_URL}/${id}/gallery`).then(response => {
      success(response.body);
    });
  },


  /**
   * Post new image to gallery
   * @param {*} id - place id
   * @param {*} itemData - media entity data
   * @param {*} success - success callback function
   */
  postGalleryItem(id, itemData, success, progress_func) {
    Vue.http.post(`${PLACE_API_URL}/${id}/gallery`, itemData, {
      progress(e) {
        if (e.lengthComputable) {
          progress_func((e.loaded / e.total) * 100);
        }
      }
    }).then(response => {
      success(response.body);
    });
  }
};
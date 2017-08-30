import Vue from 'vue';

const PLACE_API_URL = 'api/place';

export default {

  /**
   * Delete place
   * @param {*} id - place id
   * @param {*} success - success callback function
   */
  deletePlace (id, success) {
    Vue.http.delete(`${PLACE_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  }
};

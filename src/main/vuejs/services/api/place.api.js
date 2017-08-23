import Vue from 'vue';

export default {

  /**
   * Delete place
   * @param {*} id - place id
   * @param {*} success - success callback function
   */
  deletePlace (id, success) {
    Vue.http.delete('api/place/' + id).then(response => {
      success(response.body);
    });
  }
};

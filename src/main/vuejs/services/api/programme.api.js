import Vue from 'vue';
const PROGRAMME_API_URL = 'api/programme';
export default {

  /**
   * Get programme
   * @param {*} id - programme id
   * @param {*} success - success callback function
   */
  getProgramme (id, success) {
    Vue.http.get(`${PROGRAMME_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Delete programme
   * @param {*} id - programme id
   * @param {*} success - success callback function
   */
  deleteProgramme (id, success) {
    Vue.http.delete(`${PROGRAMME_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Update programme
   * @param {*} id - programme id
   * @param {*} programme - programme data object
   * @param {*} success - success callback function
   */
  putProgramme (id, programme, success) {
    Vue.http.put(`${PROGRAMME_API_URL}/${id}`, programme).then(response => {
      success(response.body);
    });
  }
};

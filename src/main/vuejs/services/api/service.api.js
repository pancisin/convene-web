import Vue from 'vue';

const SERVICE_API_URL = 'api/service';

function checkId (id) {
  if (id == null || id === '') throw new Error('missing entity id');
}

export default {

  /**
   * Get page service by id.
   * @param {Integer} id - service id
   * @param {Function} success - success callback function
   */
  getService (id, success) {
    checkId(id);
    Vue.http.get(`${SERVICE_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Update page service.
   * @param {Object} service - service data object
   * @param {Function} success - success callback function
   */
  putService (service, success) {
    Vue.http.put('api/service/' + service.id, service).then(response => {
      success(response.body);
    });
  },

  /**
   * Delete page service.
   * @param {Integer} id - service id
   * @param {Function} success - success callback function
   */
  deleteService (id, success) {
    checkId(id);
    Vue.http.delete('api/service/' + id).then(response => {
      success(response.body);
    });
  }
};

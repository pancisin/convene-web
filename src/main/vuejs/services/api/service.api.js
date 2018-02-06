import Vue from 'vue';

const SERVICE_API_URL = '/api/service';

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
  putService (id, service, success) {
    Vue.http.put(`${SERVICE_API_URL}/${id}`, service).then(response => {
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
    Vue.http.delete(`${SERVICE_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  getServiceRequests (id, success) {
    Vue.http.get(`${SERVICE_API_URL}/${id}/request`).then(response => {
      success(response.body);
    });
  },

  postServiceRequest (id, values, success) {
    Vue.http.post(`${SERVICE_API_URL}/${id}/request`, values).then(response => {
      success(response.body);
    });
  }
};

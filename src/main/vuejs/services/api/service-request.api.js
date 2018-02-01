import Vue from 'vue';

const SERVICE_REQUEST_API_URL = '/api/v1/service-request';

export default {

  acceptRequest (id, success) {
    Vue.http.patch(`${SERVICE_REQUEST_API_URL}/${id}/accept`).then(response => {
      success(response.body);
    });
  },

  refuseRequest (id, success) {
    Vue.http.patch(`${SERVICE_REQUEST_API_URL}/${id}/refuse`).then(response => {
      success(response.body);
    });
  }
};

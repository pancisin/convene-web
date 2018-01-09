import Vue from 'vue';

const IMPORTER_API_URL = '/api/v1/facebook-importer';

export default {

  searchPlace (latitude, longitude, success, filters = {}) {
    Vue.http.get(`${IMPORTER_API_URL}/search`, {
      params: {
        latitude,
        longitude,
        ...filters
      }
    }).then(response => {
      success(response.body);
    });
  },

  convert (facebookId, success) {
    Vue.http.get(`${IMPORTER_API_URL}/convert`, {
      params: {
        facebookId
      }
    }).then(response => {
      success(response.body);
    });
  }
};

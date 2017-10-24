import Vue from 'vue';

const IMPORTER_API_URL = 'api/facebook-importer';

export default {

  searchPlace (latitude, longitude, success) {
    Vue.http.get(`${IMPORTER_API_URL}/search`, {
      params: {
        latitude,
        longitude
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

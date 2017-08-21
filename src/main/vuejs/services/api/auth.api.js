import Vue from 'vue';

const AUTH_API_URL = 'api';

export default {

  login (credentials, success, error) {
    Vue.http.post(`${AUTH_API_URL}/login`, credentials).then(response => {
      success(response.body);
    }, response => {
      console.error(response.body.fieldErrors);
    });
  },

  register (user, success, error) {
    Vue.http.post(`${AUTH_API_URL}/register`, user).then(response => {
      success(response.body);
    }, response => {
      console.error(response.body.fieldErrors);
    });
  }
};

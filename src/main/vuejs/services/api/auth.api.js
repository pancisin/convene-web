import Vue from 'vue';

export default {

  /**
   * Make an login post request.
   * @param {Object} credentials - user object that only contains creadentials { email, password }
   * @param {Function} success - success callback function
   * @param {Function} error - error callback function
   */
  login (credentials, success, error) {
    Vue.http.post('/public/login', credentials).then(response => {
      success(response.body);
    }, response => {
      error(response.body);
    });
  },

  /**
   * Register post request.
   * @param {Object} user - new user object that will be registered
   * @param {Function} success - success callback function
   * @param {Function} error - error callback function
   */
  register (user, success, error) {
    Vue.http.post('/public/register', user).then(response => {
      success(response.body);
    }, response => {
      error(response.body);
    });
  },

  /**
   * Login using facebook graph api data provider.
   * @param {*} loginData - user login data containing userID and user access token
   * @param {*} success - success callback function
   * @param {*} error - error callback function
   */
  loginFacebook (loginData, success, error) {
    Vue.http.post('/public/login-facebook', loginData).then(response => {
      success(response.body);
    }, response => {
      error(response.body);
    });
  }
};

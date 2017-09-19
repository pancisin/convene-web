import Vue from 'vue';

const USER_API_URL = 'api/user';

export default {

  /**
   * Update user data.
   * @param {Object} user user object
   * @param {Function} success callback function
   */
  putUser (user, success) {
    Vue.http.put(USER_API_URL, user).then(response => {
      success(response.body);
    });
  },

  /**
   * Get user data.
   * @param {Function} success callback function
   */
  getUser (success) {
    Vue.http.get(`${USER_API_URL}/me`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get user owned pages.
   * @param {Function} success callback function
   */
  getPages (success) {
    Vue.http.get(`${USER_API_URL}/page`).then(response => {
      success(response.body);
    });
  },

  /**
   * Create new page owned by current user.
   * @param {Object} page - page object data
   * @param {Function} success - success callback, newly created page is returned as param.
   */
  postPage (page, success) {
    Vue.http.post(`${USER_API_URL}/page`, page).then(response => {
      success(response.body);
    });
  },

  /**
   * Get user owned conferences.
   * @param {*} success - callback function
   */
  getConferences (success) {
    Vue.http.get(`${USER_API_URL}/conference`).then(response => {
      success(response.body);
    });
  },

  /**
   * Create new conference owned by current user.
   * @param {*} conference - conference object data
   * @param {*} success - callback function
   */
  postConference (conference, success) {
    Vue.http.post(`${USER_API_URL}/conference`, conference).then(response => {
      success(response.body);
    });
  },

  /**
   * Get notifications for current user.
   * @param {*} success - callback function
   */
  getNotifications (success) {
    Vue.http.get(`${USER_API_URL}/notification/0/5`).then(response => {
      success(response.body.content);
    });
  },

  /**
   * Search users by keyword.
   * @param {String} key - searched keyword
   * @param {*} success - callback function
   */
  searchUsers (key, success) {
    Vue.http.get(`${USER_API_URL}/search`, {
      params: {
        q: key
      }
    }).then(response => {
      success(response.body);
    }, response => {
      console.error(response);
    });
  },

  /**
   * Get events created by current user.
   * @param {*} page - paginator page
   * @param {*} size - paginator size
   * @param {*} success - callback function
   */
  getEvents (page, size, success) {
    Vue.http.get(`${USER_API_URL}/event/${page}/${size}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get events that is current user attending.
   * @param {*} success - callback function
   */
  getAttendingEvents (success) {
    Vue.http.get(`${USER_API_URL}/event/attending`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get pages followed by current user.
   * @param {*} success - callback function
   */
  getFollowedPages (success) {
    Vue.http.get(`${USER_API_URL}/followed-pages`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get current user contacts
   * @param {Function} success - success callback function
   */
  getContacts (success) {
    Vue.http.get(`${USER_API_URL}/contacts`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get all users medias
   * @param {*} success - success callback function
   */
  getMedia (success) {
    Vue.http.get(`${USER_API_URL}/media`).then(response => {
      success(response.body);
    });
  }
};

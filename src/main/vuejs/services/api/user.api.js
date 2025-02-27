import Vue from 'vue';

const USER_API_URL = '/api/v1/user/me';

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
    Vue.http.get(USER_API_URL).then(response => {
      success(response.body);
    });
  },

  /**
   * Post new event for user.
   */
  postEvent (event, success) {
    Vue.http.post(`${USER_API_URL}/event`, event).then(response => {
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
  getNotifications (page, size, filters, success) {
    Vue.http.get(`${USER_API_URL}/notification/${page}/${size}`, {
      params: {
        ...filters
      }
    }).then(response => {
      success(response.body);
    });
  },

  setNotificationsSeen (filters, success) {
    Vue.http.patch(`${USER_API_URL}/set-notifications-seen`, null, {
      params: {
        ...filters
      }
    }).then(response => {
      success(response.body);
    });
  },

  /**
   * Search users by keyword.
   * @param {String} key - searched keyword
   * @param {*} success - callback function
   */
  searchUsers (key, success) {
    Vue.http.get('api/v1/user/search', {
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
  },

  /**
   * Get users conversations list. Returns conversations with last
   * message and user object.
   * @param {*} success - success callback function
   */
  getConversations (success) {
    Vue.http.get('/api/v1/message/conversations').then(response => {
      success(response.body);
    });
  },

  /**
   * Request to change curent users password
   * @param {*} data - user data with old and new password
   * @param {*} success - success callback function
   * @param {*} error - error callback function
   */
  changePassword (data, success, error) {
    Vue.http.post('/api/v1/user/changePassword', data).then(response => {
      success(response.body);
    }, response => {
      error(response.body);
    });
  },

  /**
   * Set currents user locale
   * @param {*} locale - selected locale
   * @param {*} success - success callback function
   */
  setLocale (locale, success) {
    Vue.http.put(`${USER_API_URL}/locale`, locale).then(response => {
      success(response.body);
    });
  },

  /**
   * Get users privacy constraints
   * @param {*} success - success callback function
   */
  getPrivacyConstraints (success) {
    Vue.http.get(`${USER_API_URL}/privacy-constraints`).then(response => {
      success(response.body);
    });
  },

  /**
   * Update privacy constraints
   * @param {*} constraints - constrains map object
   * @param {*} success = success callback function
   */
  updatePrivacyConstraints (constraints, success) {
    Vue.http.patch(`${USER_API_URL}/privacy-constraints`, constraints).then(response => {
      success(response.body);
    });
  },

  /**
   * Get current use activity feed
   * @param {*} page - paginator page property
   * @param {*} size - paginator size property
   * @param {*} success - success callback function
   */
  getActivityFeed (page, size, success) {
    Vue.http.get(`${USER_API_URL}/activity-feed/${page}/${size}`).then(response => {
      success(response.body);
    });
  },

  getSuggestedPages (except, page, size, success) {
    Vue.http.get(`${USER_API_URL}/suggested-pages/${page}/${size}`, {
      params: {
        except: except ? except.join(',') : []
      }
    }).then(response => {
      success(response.body);
    });
  }
};

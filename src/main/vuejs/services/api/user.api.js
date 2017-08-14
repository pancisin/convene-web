import Vue from 'vue';

export default {
  putUser (user, success) {
    Vue.http.put('api/user', user).then(response => {
      success(response.body);
    });
  },

  getUser (success) {
    Vue.http.get('api/user/me').then(response => {
      success(response.body);
    });
  },

  getPages (success) {
    Vue.http.get('api/user/page').then(response => {
      success(response.body);
    });
  },

  /**
   * @param {*} page - data for page.
   * @param {*} success - success callback, newly created page is returned as param.
   */
  postPage (page, success) {
    Vue.http.post('api/user/page', page).then(response => {
      success(response.body);
    });
  },
  getConferences (success) {
    Vue.http.get('api/user/conference').then(response => {
      success(response.body);
    });
  },
  postConference (conference, success) {
    Vue.http.post('api/user/conference', conference).then(response => {
      success(conference);
    });
  },
  getNotifications (success) {
    Vue.http.get('api/user/notification/0/5').then(response => {
      success(response.body.content);
    });
  },
  searchUsers (key, success) {
    Vue.http.get('api/user/search', {
      params: {
        q: key
      }
    }).then(response => {
      success(response.body);
    }, response => {
      console.error(response);
    });
  },
  getEvents (page, size, success) {
    var url = ['api/user/event', page, size].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  getAttendingEvents (success) {
    Vue.http.get('api/user/event/attending').then(response => {
      success(response.body);
    });
  },
  getFollowedPages (success) {
    Vue.http.get('api/user/followed-pages').then(response => {
      success(response.body);
    });
  }
};

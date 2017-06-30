import Vue from 'vue';

export default {
  getUser (success) {
    Vue.http.get('api/me').then(response => {
      success(response.body);
    });
  },
  getPages (success) {
    Vue.http.get('api/user/page').then(response => {
      success(response.body);
    });
  },
  postPage (page, success) {
    Vue.http.post('api/user/page', this.page).then(response => {
      success(response.body);
    });
  },
  getConferences (success) {
    Vue.http.get('api/user/conference').then(response => {
      success(response.body);
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

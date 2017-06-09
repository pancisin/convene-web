import Vue from 'vue';

export default { 
  getUser(success) {
    Vue.http.get('api/me').then(response => {
      success(response.body);
    })
  },
  getPages(success) {
    Vue.http.get('api/user/page').then(response => {
      success(response.body);
    })
  },
  getConferences(success) {
    Vue.http.get('api/user/conference').then(response => {
      success(response.body);
    })
  },
  getNotifications(success) {
    Vue.http.get('api/user/notification/0/5').then(response => {
      success(response.body.content);
    })
  }
}
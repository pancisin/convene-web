import Vue from 'vue';

export default {
  getEvent(id, auth, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = [auth ? 'api' : 'public', 'event', id].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  },
  getRelated(author_type, author_id, success) {
    var url = ['public', author_type, author_id, 'event'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  },
  getAttendanceStatus(id, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = ['api/event', id, 'attend-status'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  },
  toggleAttendanceStatus(id, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = ['api/event', id, 'toggle-attend'].join('/');
    Vue.http.patch(url).then(response => {
      success(response.body);
    });
  }
}
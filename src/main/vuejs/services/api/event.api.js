import Vue from 'vue';

export default {
  getEvent (id, auth, success) {
    if (id == null || id === '') throw 'missing entity id';

    var url = `${auth ? 'api' : 'public'}/event/${id}`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  putEvent (id, event, success) {
    Vue.http.put(`api/event/${id}`, event).then(response => {
      success(response.body);
    });
  },
  getRelated (author_type, author_id, success) {
    var url = ['public', author_type, author_id, 'event'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  getAttendanceStatus (id, success) {
    if (id == null || id === '') throw 'missing entity id';

    var url = ['api/event', id, 'attend-status'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  toggleAttendanceStatus (id, success) {
    if (id == null || id === '') throw 'missing entity id';

    var url = ['api/event', id, 'toggle-attend'].join('/');
    Vue.http.patch(url).then(response => {
      success(response.body);
    });
  },
  getInvitations (id, success) {
    var url = ['api/event', id, 'invitation'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  getAttendees (id, success) {
    var url = ['api/event', id, 'attendees'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  postInvitation (id, invitation, success) {
    var url = `api/event/${id}/invite`;
    Vue.http.post(url, invitation).then(response => {
      success(response.body);
    });
  },
  getProgramme (id, success) {
    Vue.http.get(`api/event/${id}/programme`).then(response => {
      success(response.body);
    });
  },
  postProgramme (id, programme, success) {
    Vue.http.post(`api/event/${id}/programme`, programme).then(response => {
      success(response.body);
    });
  }
};

import Vue from 'vue';
const EVENT_API_URL = 'api/event';
export default {
  getEvent (id, auth, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${auth ? 'api' : 'public'}/event/${id}`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  putEvent (id, event, success) {
    Vue.http.put(`${EVENT_API_URL}/${id}`, event).then(response => {
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
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.get(`${EVENT_API_URL}/${id}/attend-status`).then(response => {
      success(response.body);
    });
  },
  toggleAttendanceStatus (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.patch(`${EVENT_API_URL}/${id}/toggle-attend`).then(response => {
      success(response.body);
    });
  },
  getInvitations (id, success) {
    Vue.http.get(`${EVENT_API_URL}/${id}/invitation`).then(response => {
      success(response.body);
    });
  },
  getAttendees (id, success) {
    Vue.http.get(`${EVENT_API_URL}/${id}/attendees`).then(response => {
      success(response.body);
    });
  },
  postInvitation (id, invitation, success) {
    Vue.http.post(`${EVENT_API_URL}/${id}/invite`, invitation).then(response => {
      success(response.body);
    });
  },
  getProgramme (id, success) {
    Vue.http.get(`${EVENT_API_URL}/${id}/programme`).then(response => {
      success(response.body);
    });
  },
  postProgramme (id, programme, success) {
    Vue.http.post(`${EVENT_API_URL}/${id}/programme`, programme).then(response => {
      success(response.body);
    });
  }
};

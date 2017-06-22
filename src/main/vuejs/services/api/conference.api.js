import Vue from 'vue';

export default {
  getConference(id, success) {
    Vue.http.get('api/conference/' + id).then(response => {
      success(response.body);
    })
  },
  postConference(conference, success) {
    Vue.http.post('api/user/conference', conference).then(response => {
      success(response.body);
    })
  },
  putConference(conference, success) {
    var url = ['api/conference', conference.id].join('/');
    Vue.http.put(url, conference).then(response => {
      success(response.body);
    })
  },
  getEvents(id, page, size, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = ['api/conference', id, 'event', page, size].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  },
  getInvitations(id, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = ['api/conference', id, 'invitation'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  },
  getAttendees(id, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = ['api/conference', id, 'attendees'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  },
  postInvitation(id, invitation, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = ['api/conference', id, 'invite'].join('/');
    Vue.http.post(url, invitation).then(response => {
      return response.body;
    })
  },
  getMetaFields(id, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = ['api/conference', id, 'meta-field'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  },
  postMetaField(id, field, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = ['api/conference', id, 'meta-field'].join('/');
    Vue.http.post(url, field).then(response => {
      success(response.body);
    })
  },
  getAdministrators(id, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = ['api/conference', id, 'administrator'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  },
  postAdministrator(id, administrator, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = ['api/conference', id, 'administrator'].join('/');
    Vue.http.post(url, { id: administrator.id }).then(response => {
      success(response.body);
    })
  },
  getAttendStatus(id, success) {
    var url = ['api/conference', id, 'attend-status'].join('/');
    Vue.http.get(url).then(response => {
      success(response.bodyText);
    })
  },
  postAttend(id, meta, success) {
    var url = ['api/conference', id, 'attend'].join('/');
    Vue.http.post(url, meta).then(response => {
      success(response.bodyText);
    })
  },
  putCancelAttend(id, success) {
    var url = ['api/conference', id, 'cancel-attend'].join('/');
    Vue.http.put(url).then(response => {
      success(response.body);
    })
  },
}
import Vue from 'vue';

const CONFERENCE_API_URL = 'api/conference';
function checkId (id) {
  if (id == null || id === '') throw new Error('missing entity id');
}

export default {
  getConference (id, success) {
    checkId(id);

    var url = `${CONFERENCE_API_URL}/${id}`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  postConference (conference, success) {
    Vue.http.post('api/user/conference', conference).then(response => {
      success(response.body);
    });
  },
  putConference (conference, success) {
    var url = `${CONFERENCE_API_URL}/${conference.id}`;
    Vue.http.put(url, conference).then(response => {
      success(response.body);
    });
  },
  getEvents (id, page, size, success) {
    checkId(id);

    var url = `${CONFERENCE_API_URL}/${id}/event/${page}/${size}`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  getInvitations (id, success) {
    checkId(id);

    var url = `${CONFERENCE_API_URL}/${id}/invitation`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  getAttendees (id, success) {
    checkId(id);

    var url = `${CONFERENCE_API_URL}/${id}/attendees`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  postInvitation (id, invitation, success) {
    checkId(id);

    var url = `${CONFERENCE_API_URL}/${id}/invite`;
    Vue.http.post(url, invitation).then(response => {
      return response.body;
    });
  },
  getMetaFields (id, success) {
    checkId(id);

    var url = `${CONFERENCE_API_URL}/${id}/meta-field`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  postMetaField (id, field, success) {
    checkId(id);

    var url = `${CONFERENCE_API_URL}/${id}/meta-field`;
    Vue.http.post(url, field).then(response => {
      success(response.body);
    });
  },
  getAdministrators (id, success) {
    checkId(id);

    var url = `${CONFERENCE_API_URL}/${id}/administrator`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  postAdministrator (id, administrator, success) {
    checkId(id);

    var url = `${CONFERENCE_API_URL}/${id}/administrator`;
    Vue.http.post(url, { id: administrator.id }).then(response => {
      success(response.body);
    });
  },
  getAttendStatus (id, success) {
    checkId(id);

    var url = `${CONFERENCE_API_URL}/${id}/attend-status`;
    Vue.http.get(url).then(response => {
      success(response.bodyText);
    });
  },
  postAttend (id, meta, success) {
    checkId(id);

    var url = `${CONFERENCE_API_URL}/${id}/attend`;
    Vue.http.post(url, meta).then(response => {
      success(response.bodyText);
    });
  },
  putCancelAttend (id, success) {
    checkId(id);

    var url = `${CONFERENCE_API_URL}/${id}/cancel-attend`;
    Vue.http.put(url).then(response => {
      success(response.body);
    });
  },
  getArticles (id, success) {
    checkId(id);

    var url = `${CONFERENCE_API_URL}/${id}/article`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  postArticle (id, article, success) {
    checkId(id);

    var url = `${CONFERENCE_API_URL}/${id}/article`;
    Vue.http.post(url, article).then(response => {
      success(response.body);
    });
  }
};

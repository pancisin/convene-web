import Vue from 'vue';

export default {
  base_url: 'api/conference',
  getConference (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${this.base_url}/${id}`;
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
    var url = `${this.base_url}/${conference.id}`;
    Vue.http.put(url, conference).then(response => {
      success(response.body);
    });
  },
  getEvents (id, page, size, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${this.base_url}/${id}/event/${page}/${size}`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  getInvitations (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${this.base_url}/${id}/invitation`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  getAttendees (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${this.base_url}/${id}/attendees`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  postInvitation (id, invitation, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${this.base_url}/${id}/invite`;
    Vue.http.post(url, invitation).then(response => {
      return response.body;
    });
  },
  getMetaFields (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${this.base_url}/${id}/meta-field`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  postMetaField (id, field, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${this.base_url}/${id}/meta-field`;
    Vue.http.post(url, field).then(response => {
      success(response.body);
    });
  },
  getAdministrators (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${this.base_url}/${id}/administrator`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  postAdministrator (id, administrator, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${this.base_url}/${id}/administrator`;
    Vue.http.post(url, { id: administrator.id }).then(response => {
      success(response.body);
    });
  },
  getAttendStatus (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${this.base_url}/${id}/attend-status`;
    Vue.http.get(url).then(response => {
      success(response.bodyText);
    });
  },
  postAttend (id, meta, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${this.base_url}/${id}/attend`;
    Vue.http.post(url, meta).then(response => {
      success(response.bodyText);
    });
  },
  putCancelAttend (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${this.base_url}/${id}/cancel-attend`;
    Vue.http.put(url).then(response => {
      success(response.body);
    });
  },
  getArticles (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${this.base_url}/${id}/article`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  postArticle (id, article, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${this.base_url}/${id}/article`;
    Vue.http.post(url, article).then(response => {
      success(response.body);
    });
  }
};

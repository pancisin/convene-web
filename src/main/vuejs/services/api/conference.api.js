import Vue from 'vue';
const CONFERENCE_API_URL = '/api/conference';
function checkId (id) {
  if (id == null || id === '') throw new Error('missing entity id');
}

export default {

  get parent_type () {
    return 'conference';
  },

  /**
   * Get conference by id
   * @param {*} id - conference id
   * @param {*} success - success callback function
   */
  getConference (id, success, error) {
    checkId(id);
    Vue.http.get(`${CONFERENCE_API_URL}/${id}`).then(response => {
      success(response.body);
    }, response => {
      if (error) {
        error(response);
      }
    });
  },

  /**
   * Update conference data
   * @param {*} conference - conference data object
   * @param {*} success - success callback function
   */
  putConference (id, conference, success) {
    Vue.http.put(`${CONFERENCE_API_URL}/${id}`, conference).then(response => {
      success(response.body);
    });
  },

  /**
   * Get conference events
   * @param {*} id - conference id
   * @param {*} page - paginator page property
   * @param {*} size - paginator size property
   * @param {*} success - success callback function
   */
  getEvents (id, page, size, success) {
    checkId(id);
    Vue.http.get(`${CONFERENCE_API_URL}/${id}/event/${page}/${size}`).then(response => {
      success(response.body);
    });
  },

  postEvent (id, event, success) {
    checkId(id);
    Vue.http.post(`${CONFERENCE_API_URL}/${id}/event`, event).then(response => {
      success(response.body);
    });
  },

  /**
   * Get conference invitations
   * @param {*} id - conference id
   * @param {*} success - success callback function
   */
  getInvitations (id, success) {
    checkId(id);
    Vue.http.get(`${CONFERENCE_API_URL}/${id}/invitation`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get conference attendees.
   * @param {*} id - conference id
   * @param {*} success - success callback function
   */
  getAttendees (id, success) {
    checkId(id);
    Vue.http.get(`${CONFERENCE_API_URL}/${id}/attendees`).then(response => {
      success(response.body);
    });
  },

  /**
   * Post invitation to conference.
   * @param {*} id - conference id
   * @param {*} invitation - invitation data object
   * @param {*} success - success callback function
   */
  postInvitation (id, invitation, success) {
    checkId(id);
    Vue.http.post(`${CONFERENCE_API_URL}/${id}/invite`, invitation).then(response => {
      return response.body;
    });
  },

  /**
   * Get meta fields.
   * @param {*} id - conference id
   * @param {*} success - success callback function
   */
  getMetaFields (id, success) {
    checkId(id);
    Vue.http.get(`${CONFERENCE_API_URL}/${id}/meta-field`).then(response => {
      success(response.body);
    });
  },

  /**
   * Post meta field.
   * @param {*} id - conference id
   * @param {*} field
   * @param {*} success - success callback function
   */
  postMetaField (id, field, success) {
    checkId(id);
    Vue.http.post(`${CONFERENCE_API_URL}/${id}/meta-field`, field).then(response => {
      success(response.body);
    });
  },

  /**
   * Get conference administrators.
   * @param {*} id - conference id
   * @param {*} success - success callback function
   */
  getAdministrators (id, success) {
    checkId(id);
    Vue.http.get(`${CONFERENCE_API_URL}/${id}/administrator`).then(response => {
      success(response.body);
    });
  },

  /**
   * Post conference administrator.
   * @param {*} id - conference id
   * @param {*} administrator - administrator data object
   * @param {*} success - success callback function
   */
  postAdministrator (id, administrator, success) {
    checkId(id);
    Vue.http.post(`${CONFERENCE_API_URL}/${id}/administrator`, { id: administrator.id, email: administrator.email }).then(response => {
      success(response.body);
    });
  },

  /**
   * Get current user attend status for conference.
   * @param {*} id - conference id
   * @param {*} success - success callback function
   */
  getAttendStatus (id, success) {
    checkId(id);
    Vue.http.get(`${CONFERENCE_API_URL}/${id}/attend-status`).then(response => {
      success(response.bodyText);
    });
  },

  /**
   * Post attend status with meta field values inserted.
   * @param {*} id - conference id
   * @param {*} meta - meta field value
   * @param {*} success - success callback function
   */
  postAttend (id, meta, success) {
    checkId(id);
    Vue.http.post(`${CONFERENCE_API_URL}/${id}/attend`, meta).then(response => {
      success(response.bodyText);
    });
  },

  /**
   * Cancel conference attendance status for current user.
   * @param {*} id - conference id
   * @param {*} success - success callback function
   */
  putCancelAttend (id, success) {
    checkId(id);
    Vue.http.put(`${CONFERENCE_API_URL}/${id}/cancel-attend`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get conference articles.
   * @param {*} id - conference id
   * @param {*} success - success callback function
   */
  getArticles (id, page, size, success) {
    checkId(id);
    Vue.http.get(`${CONFERENCE_API_URL}/${id}/article/${page}/${size}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Post new article to conference.
   * @param {*} id - conference id
   * @param {*} article - article data object
   * @param {*} success - success callback function
   */
  postArticle (id, article, success) {
    checkId(id);
    Vue.http.post(`${CONFERENCE_API_URL}/${id}/article`, article).then(response => {
      success(response.body);
    });
  },

  /**
   * Toggle publish state of conference.
   * @param {*} id - conference id
   * @param {*} success - success callback function
   */
  togglePublished (id, success) {
    checkId(id);
    Vue.http.patch(`${CONFERENCE_API_URL}/${id}/toggle-published`).then(response => {
      success(response.body);
    });
  },

  /**
   * Post survey to conference.
   * @param {*} id - conference id
   * @param {*} survey - survey data object
   * @param {*} success - success callback function
   */
  postSurvey (id, survey, success) {
    Vue.http.post(`${CONFERENCE_API_URL}/${id}/survey`, survey).then(response => {
      success(response.body);
    });
  },

  /**
   * Get conference surveys
   * @param {*} id - conference id
   * @param {*} success - success callback function
   */
  getPublicSurveys (id, success) {
    Vue.http.get(`${CONFERENCE_API_URL}/${id}/survey`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get conference surveys
   * @param {*} id - conference id
   * @param {*} success - success callback function
   */
  getSurveys (id, success) {
    Vue.http.get(`${CONFERENCE_API_URL}/${id}/survey`, {
      params: {
        submitted: true
      }
    }).then(response => {
      success(response.body);
    });
  },

  /**
   * Get conference recent activity.
   * @param {*} id - conference id
   * @param {*} success - success callback function
   */
  getActivities (id, page, size, success) {
    Vue.http.get(`${CONFERENCE_API_URL}/${id}/activity/${page}/${size}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get conference dashboard widgets data.
   * @param {*} id - conference id
   * @param {*} success - success callback function
   */
  getWidgets (id, success) {
    Vue.http.get(`${CONFERENCE_API_URL}/${id}/widget`).then(response => {
      success(response.body);
    });
  },

  /**
   * Update conference widgets positions and existence
   * @param {*} id - conference id
   * @param {*} widgets - widgets array
   * @param {*} success - success callback function
   */
  putWidgets (id, widgets, success) {
    Vue.http.put(`${CONFERENCE_API_URL}/${id}/widget`, widgets).then(response => {
      success(response.body);
    });
  },

  /**
   * Post new place and map it to conference by id
   * @param {*} id - conference id
   * @param {*} place - place data object
   * @param {*} success - success callback function
   */
  postPlace (id, place, success) {
    Vue.http.post(`${CONFERENCE_API_URL}/${id}/place`, place).then(response => {
      success(response.body);
    });
  },

  /**
   * Get all places mapped to conference by conference id
   * @param {*} id - conference id
   * @param {*} success - success callback function
   */
  getPlaces (id, success) {
    Vue.http.get(`${CONFERENCE_API_URL}/${id}/place`).then(response => {
      success(response.body);
    });
  }
};

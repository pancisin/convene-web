import ConferenceApi from 'api/conference.api';

export default class ConferenceInjector {
  constructor (conference_id, auth) {
    this.conference_id = conference_id;
    this.auth = auth;
  }

  getConference (success) {
    if (this.conference_id == null) {
      return {};
    }

    return ConferenceApi.getConference(this.conference_id, success);
  }

  putConference (conference, success) {
    ConferenceApi.putConference(conference, success);
  }

  postConference (conference, success) {
    ConferenceApi.postConference(conference, success);
  }

  getAttendees (success) {
    ConferenceApi.getAttendees(this.conference_id, success);
  }

  getMetaFields (success) {
    ConferenceApi.getMetaFields(this.conference_id, success);
  }

  getEvents (page, size, success) {
    ConferenceApi.getEvents(this.conference_id, page, size, success);
  }

  getInvitations (success) {
    ConferenceApi.getInvitations(this.conference_id, success);
  }

  postInvitation (invitation, success) {
    ConferenceApi.postInvitation(this.conference_id, invitation, success);
  }

  postMetaField (field, success) {
    ConferenceApi.postMetaField(this.conference_id, field, success);
  }

  getAdministrators (success) {
    ConferenceApi.getAdministrators(this.conference_id, success);
  }

  postAdministrator (administrator, success) {
    ConferenceApi.postAdministrator(this.conference_id, administrator, success);
  }

  getAttendStatus (success) {
    ConferenceApi.getAttendStatus(this.conference_id, success);
  }

  postAttend (meta, success) {
    ConferenceApi.postAttend(this.conference_id, meta, success);
  }

  putCancelAttend (success) {
    ConferenceApi.putCancelAttend(this.conference_id, success);
  }

  getArticles (success) {
    ConferenceApi.getArticles(this.conference_id, success);
  }

  postArticle (article, success) {
    ConferenceApi.postArticle(this.conference_id, article, success);
  }
};

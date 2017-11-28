import ConferenceApi from 'api/conference.api';

export default class ConferenceInjector {

  get base_route () {
    return {
      name: 'conference',
      props: {
        id: this.conference_id
      }
    };
  }

  get parent_type () {
    return 'conference';
  }

  constructor (conference_id) {
    this.conference_id = conference_id;
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

  putAdministrator (administrator_id, administrator, success) {
    ConferenceApi.putAdministrator(administrator_id, administrator, success);
  }

  deleteAdministrator (administrator_id, success) {
    ConferenceApi.deleteAdministrator(administrator_id, success);
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

  getArticles (page, size, success) {
    ConferenceApi.getArticles(this.conference_id, page, size, success);
  }

  postArticle (article, success) {
    ConferenceApi.postArticle(this.conference_id, article, success);
  }

  togglePublished (success) {
    ConferenceApi.togglePublished(this.conference_id, success);
  }

  getSurveys (success) {
    ConferenceApi.getSurveys(this.conference_id, success);
  }

  postSurvey (survey, success) {
    ConferenceApi.postSurvey(this.conference_id, survey, success);
  }

  getPublicSurveys (success) {
    ConferenceApi.getPublicSurveys(this.conference_id, success);
  }

  getActivities (success) {
    ConferenceApi.getActivities(this.conference_id, success);
  }

  getWidgets (success) {
    ConferenceApi.getWidgets(this.conference_id, success);
  }

  putWidgets (widgets, success) {
    ConferenceApi.putWidgets(this.conference_id, widgets, success);
  }

  getPlaces (success) {
    ConferenceApi.getPlaces(this.conference_id, success);
  }

  postPlace (place, success) {
    ConferenceApi.postPlace(this.conference_id, place, success);
  }
};

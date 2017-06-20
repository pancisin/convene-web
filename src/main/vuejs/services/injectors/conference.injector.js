import ConferenceApi from '../api/conference.api.js'

export default class ConferenceInjector {
  constructor(conference_id) {
    this.conference_id = conference_id;
  }

  getConference(success) {
    if (this.conference_id == null)
      return new Object();

    return ConferenceApi.getConference(this.conference_id, success);
  }

  putConference(conference, success) {
    ConferenceApi.putConference(conference, success);
  }

  postConference(conference, success) {
    ConferenceApi.postConference(conference, success);
  }

  getAttendees(success) {
    ConferenceApi.getAttendees(this.conference_id, success);
  }

  getMetaFields(success) {
    ConferenceApi.getMetaFields(this.conference_id, success);
  }

  getEvents(page, size, success) {
    ConferenceApi.getEvents(this.conference_id, page, size, success);
  }
}
import EventApi from '../api/event.api.js'

export default class PageInjector {
  constructor(event_id) {
    this.event_id = event_id;
  }

  getEvent(auth, success) {
    if (this.event_id == null)
      return new Object();

    EventApi.getEvent(this.event_id, auth, success);
  }

  postEvent(event, success) {

  }

  putEvent(event, success) {
    
  }

  getAttendess(success) {
    EventApi.getAttendees(this.event_id, success);
  }

  getInvitations(success) {
    EventApi.getInvitations(this.event_id, success);
  }
}
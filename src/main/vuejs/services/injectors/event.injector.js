import EventApi from 'api/event.api';

export default class PageInjector {
  constructor (event_id) {
    this.event_id = event_id;
  }

  getEvent (auth, success) {
    if (this.event_id == null) {
      return {};
    }

    EventApi.getEvent(this.event_id, success);
  }

  postEvent (event, success) {

  }

  putEvent (event, success) {
    EventApi.putEvent(this.event_id, event, success);
  }

  getAttendanceStatus (success) {
    EventApi.getAttendanceStatus(this.event_id, success);
  }

  toggleAttendanceStatus (success) {
    EventApi.toggleAttendanceStatus(this.event_id, success);
  }

  getAttendees (success) {
    EventApi.getAttendees(this.event_id, success);
  }

  getInvitations (success) {
    EventApi.getInvitations(this.event_id, success);
  }

  postInvitation (invitation, success) {
    EventApi.postInvitation(this.event_id, invitation, success);
  }

  getProgramme (success) {
    EventApi.getProgramme(this.event_id, success);
  }

  postProgramme (programme, success) {
    EventApi.postProgramme(this.event_id, programme, success);
  }

  getGallery (success) {
    EventApi.getGallery(this.event_id, success);
  }

  postGalleryItem (gallery_item, success, progress_func) {
    EventApi.postGalleryItem(this.event_id, gallery_item, success, progress_func);
  }
}

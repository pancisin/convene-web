import Vue from 'vue';
const EVENT_API_URL = 'api/event';
export default {

  /**
   * Get event by id
   * @param {Number} id - event id
   * @param {Function} success - success callback function
   * @param {Function} error - error callback function
   */
  getEvent(id, success, error) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.get(`${EVENT_API_URL}/${id}`).then(response => {
      success(response.body);
    }, response => {
      if (error != null) {
        error(response.body);
      }
    });
  },

  /**
   * Update event data.
   * @param {*} id - event id
   * @param {*} event - event object data
   * @param {*} success - success callback function
   */
  putEvent(id, event, success) {
    Vue.http.put(`${EVENT_API_URL}/${id}`, event).then(response => {
      success(response.body);
    });
  },

  /**
   * Delete event by event id
   * @param {*} id - event id
   * @param {*} success - success callback function
   */
  deleteEvent(id, success) {
    Vue.http.delete(`${EVENT_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get event related events.
   * @param {*} id - event id
   * @param {*} success - success callback function
   */
  getAttendanceStatus(id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.get(`${EVENT_API_URL}/${id}/attend-status`).then(response => {
      success(response.body);
    });
  },

  /**
   * Toggle event attendance status for current user
   * @param {*} id - event id
   * @param {*} success - success callback function
   */
  toggleAttendanceStatus(id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.patch(`${EVENT_API_URL}/${id}/toggle-attend`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get invitations for event
   * @param {*} id - event id
   * @param {*} success - success callback function
   */
  getInvitations(id, success) {
    Vue.http.get(`${EVENT_API_URL}/${id}/invitation`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get event attendees.
   * @param {*} id - event id
   * @param {*} success - success callback function
   */
  getAttendees(id, success) {
    Vue.http.get(`${EVENT_API_URL}/${id}/attendees`).then(response => {
      success(response.body);
    });
  },

  /**
   * Create invitation for user.
   * @param {*} id - event id
   * @param {*} invitation - invitation object data
   * @param {*} success -success callback function
   */
  postInvitation(id, invitation, success) {
    Vue.http.post(`${EVENT_API_URL}/${id}/invite`, invitation).then(response => {
      success(response.body);
    });
  },

  /**
   * Get event programme.
   * @param {*} id - event id
   * @param {*} success - success callback function
   */
  getProgramme(id, success) {
    Vue.http.get(`${EVENT_API_URL}/${id}/programme`).then(response => {
      success(response.body);
    });
  },

  /**
   * Create new programme for event.
   * @param {*} id - event id
   * @param {*} programme - programme data object
   * @param {*} success - success callback function
   */
  postProgramme(id, programme, success) {
    Vue.http.post(`${EVENT_API_URL}/${id}/programme`, programme).then(response => {
      success(response.body);
    });
  },

  /**
   * Get related events to one specified by id
   * @param {*} id - event id
   * @param {*} success - success callback function
   */
  getRelated(id, success) {
    Vue.http.get(`${EVENT_API_URL}/${id}/related`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get gallery images
   * @param {*} id - event id
   * @param {*} success - success callback function
   */
  getGallery(id, success, progress_func) {
    Vue.http.get(`${EVENT_API_URL}/${id}/gallery`).then(response => {
      success(response.body);
    });
  },

  /**
   * Post new image to gallery
   * @param {*} id - event id
   * @param {*} itemData - media entity data
   * @param {*} success - success callback function
   */
  postGalleryItem(id, itemData, success, progress_func) {
    Vue.http.post(`${EVENT_API_URL}/${id}/gallery`, itemData, {
      progress(e) {
        if (e.lengthComputable) {
          progress_func((e.loaded / e.total) * 100);
        }
      }
    }).then(response => {
      success(response.body);
    });
  }
};
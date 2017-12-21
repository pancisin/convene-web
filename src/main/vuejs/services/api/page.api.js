import Vue from 'vue';
import moment from 'moment';
import PageAdministratorApi from './page-administrator.api';

const PAGE_API_URL = '/api/page';

function errorHandler (response) {
  console.error(response);
}

export default {

  get parent_type () {
    return 'page';
  },

  get administrator () {
    return PageAdministratorApi;
  },

  /**
   * Get page data
   * @param {*} id - page id
   * @param {*} success - success callback function
   */
  getPage (id, success, error) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.get(`${PAGE_API_URL}/${id}`).then(response => {
      success(response.body);
    }, response => {
      if (error) {
        error(response);
      }
    });
  },

  /**
   * Get page object by slug string
   * @param {*} slug - slug string
   * @param {*} success - success callback function
   */
  getPageBySlug (slug, success) {
    Vue.http.get(`${PAGE_API_URL}/s/${slug}`).then(response => {
      success(response.body);
    }, errorHandler);
  },

  /**
   * Update page data
   * @param {*} page - page data object
   * @param {*} success - success callback function
   */
  putPage (id, page, success) {
    Vue.http.put(`${PAGE_API_URL}/${id}`, page).then(response => {
      success(response.body);
    }, errorHandler);
  },

  /**
   * Delete page
   * @param {*} id - page id
   * @param {*} success - success callback function
   */
  deletePage (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.delete(`${PAGE_API_URL}/${id}`).then(response => {
      success(response.body);
    }, errorHandler);
  },

  /**
   * Toggle publish state of page.
   * @param {*} id - page id
   * @param {*} success - success callback function
   */
  togglePublished (id, success) {
    Vue.http.patch(`${PAGE_API_URL}/${id}/toggle-published`).then(response => {
      success(response.body);
    }, errorHandler);
  },

  /**
   * Get events for page
   * @param {*} id - page id
   * @param {*} page - paginator page property
   * @param {*} size - paginator size property
   * @param {*} success - success callback function, @returns events paginator object
   */
  getEvents (id, page, size, success, params = {}) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.get(`${PAGE_API_URL}/${id}/event/${page}/${size}`, {
      params: {
        orderBy: 'date ASC',
        fromDate: params.from != null ? moment(params.from).format('YYYY-MM-DD') : moment().format('YYYY-MM-DD'),
        toDate: params.to != null ? moment(params.to).format('YYYY-MM-DD') : moment().add(1, 'M').format('YYYY-MM-DD')
      }
    }).then(response => {
      success(response.body);
    }, errorHandler);
  },

  postEvent (id, event, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.post(`${PAGE_API_URL}/${id}/event`, event).then(response => {
      success(response.body);
    }, errorHandler);
  },

  /**
   * Get page services
   * @param {*} id - page id
   * @param {*} success - success callback function
   */
  getServices (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.get(`${PAGE_API_URL}/${id}/service`).then(response => {
      success(response.body);
    }, errorHandler);
  },

  /**
   * Post new service to the page
   * @param {*} id - page id
   * @param {*} service - service data object
   * @param {*} success - success callback function
   */
  postService (id, service, success) {
    Vue.http.post(`${PAGE_API_URL}/${id}/service`, service).then(response => {
      success(response.body);
    }, errorHandler);
  },

  /**
   * Get follow status of current user to page
   * @param {*} id - page id
   * @param {*} success - success callback function
   */
  getFollowStatus (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.get(`${PAGE_API_URL}/${id}/follow-status`).then(response => {
      success(response.body);
    }, errorHandler);
  },

  /**
   * Toggle follow status of current user to page
   * @param {*} id - page id
   * @param {*} success - success callback function
   */
  toggleFollowStatus (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.patch(`${PAGE_API_URL}/${id}/toggle-follow`).then(response => {
      success(response.body);
    }, errorHandler);
  },

  /**
   * Post new place and map it to page specified by id
   * @param {*} id - page id
   * @param {*} place - place data object
   * @param {*} success - success callback function
   */
  postPlace (id, place, success) {
    Vue.http.post(`${PAGE_API_URL}/${id}/place`, place).then(response => {
      success(response.body);
    }, errorHandler);
  },

  /**
   * Get page places
   * @param {*} id - page id
   * @param {*} success - success callback function
   */
  getPlaces (id, success) {
    Vue.http.get(`${PAGE_API_URL}/${id}/place`).then(response => {
      success(response.body);
    }, errorHandler);
  },

  /**
   * Get page requests
   * @param {*} id - page id
   * @param {*} success - success callback function
   */
  getRequests (id, page, size, success) {
    Vue.http.get(`${PAGE_API_URL}/${id}/requests/${page}/${size}`).then(response => {
      success(response.body);
    }, errorHandler);
  },

  /**
   * Get page administrators
   * @param {*} id - page id
   * @param {*} success - success callback function
   */
  getAdministrators (id, success) {
    Vue.http.get(`${PAGE_API_URL}/${id}/administrator`).then(response => {
      success(response.body);
    }, errorHandler);
  },

  /**
   * Post user as administrator to page
   * @param {*} id - page id
   * @param {*} administrator - user data object (should contain email)
   * @param {*} success - success callback function
   */
  postAdministrator (id, administrator, success) {
    Vue.http.post(`${PAGE_API_URL}/${id}/administrator`, {
      id: administrator.id,
      email: administrator.email
    }).then(response => {
      success(response.body);
    }, errorHandler);
  },

  /**
   * Get latest activity to page by page id.
   * @param {Number} id - page id
   * @param {Function} success - success callback function
   */
  getActivities (id, page, size, success) {
    Vue.http.get(`${PAGE_API_URL}/${id}/activity/${page}/${size}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get conference dashboard widgets data.
   * @param {*} id - conference id
   * @param {*} success - success callback function
   */
  getWidgets (id, success) {
    Vue.http.get(`${PAGE_API_URL}/${id}/widget`).then(response => {
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
    Vue.http.put(`${PAGE_API_URL}/${id}/widget`, widgets).then(response => {
      success(response.body);
    });
  },

  /**
   * Get gallery images
   * @param {*} id - page id
   * @param {*} success - success callback function
   */
  getGallery (id, success) {
    Vue.http.get(`${PAGE_API_URL}/${id}/gallery`).then(response => {
      success(response.body);
    });
  },

  /**
   * Post new image to gallery
   * @param {*} id - page id
   * @param {*} itemData - media entity data
   * @param {*} success - success callback function
   */
  postGalleryItem (id, itemData, success, progress_func) {
    Vue.http.post(`${PAGE_API_URL}/${id}/gallery`, itemData, {
      progress (e) {
        if (e.lengthComputable) {
          progress_func((e.loaded / e.total) * 100);
        }
      }
    }).then(response => {
      success(response.body);
    });
  },

  /**
   * Get event bots defined for page specified
   * @param {*} id - page id
   * @param {*} success - success callback function
   */
  getBots (id, success) {
    Vue.http.get(`${PAGE_API_URL}/${id}/bot`).then(response => {
      success(response.body);
    });
  },

  /**
   * Post new event bot to page specified
   * @param {*} id - page id
   * @param {*} bot - event bot data object
   * @param {*} success - success callback function
   */
  postBot (id, bot, success) {
    Vue.http.post(`${PAGE_API_URL}/${id}/bot`, bot).then(response => {
      success(response.body);
    });
  }
};

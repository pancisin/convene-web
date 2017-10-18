import Vue from 'vue';
import moment from 'moment';

const EVENT_PUBLIC_URL = 'public/event';
const PAGE_PUBLIC_URL = 'public/page';
const CONFERENCE_PUBLIC_URL = 'public/conference';

export default {

  /**
   * Get event public data.
   * @param {Number} id - event id
   * @param {Function} success - success callback function
   */
  getEvent (id, success) {
    Vue.http.get(`${EVENT_PUBLIC_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  getEvents (page, size, filters, success) {
    Vue.http.get(`public/events/${page}/${size}`, {
      params: filters
    }).then(response => {
      success(response.body);
    });
  },

  event: {
    /**
     * Get events related events
     * @param {*} event_id - event id
     * @param {*} success - success callback function
     */
    getRelated (event_id, success) {
      Vue.http.get(`${EVENT_PUBLIC_URL}/${event_id}/related`).then(response => {
        success(response.body);
      });
    },

    /**
     * Get public accessible gallery for event
     * @param {*} event_id - event id
     * @param {*} success - success callback function
     */
    getGallery (event_id, success) {
      Vue.http.get(`${EVENT_PUBLIC_URL}/${event_id}/gallery`).then(response => {
        success(response.body);
      });
    }
  },

  /**
   * Get all public popular pages.
   * @param {Function} success - success callback function
   */
  getPopularPages (success) {
    Vue.http.get('api/public/popular-pages/').then(response => {
      success(response.body);
    });
  },

  /**
   * Get public page data.
   * @param {Number} id - page id
   * @param {Function} success - success callback function
   */
  getPage (id, success) {
    Vue.http.get(`${PAGE_PUBLIC_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  page: {

    /**
     * Get public accessible events for page.
     * @param {Number} page_id - page id
     * @param {Number} page - paginator page property
     * @param {Number} size - paginator size property
     * @param {Function} success - success callback function
     */
    getEvents (page_id, page, size, success) {
      Vue.http.get(`${PAGE_PUBLIC_URL}/${page_id}/event/${page}/${size}`, {
        params: {
          fromDate: moment().format('YYYY-MM-DD'),
          orderBy: 'date ASC'
        }
      }).then(response => {
        success(response.body);
      });
    },

    /**
     * Get services for page
     * @param {Number} id - page id
     * @param {Function} success - success callback function
     */
    getServices (id, success) {
      Vue.http.get(`${PAGE_PUBLIC_URL}/${id}/service`).then(response => {
        success(response.body);
      });
    }
  },

  /**
   * Get public data for conference
   * @param {*} conference_id - conference id
   * @param {*} success - success callback function
   */
  getConference (conference_id, success) {
    Vue.http.get(`${CONFERENCE_PUBLIC_URL}/${conference_id}`).then(response => {
      success(response.body);
    });
  },

  /**
 * Get conferences
 * @param {*} page - paginator page property
 * @param {*} size - paginator size property
 * @param {*} auth - authenticated user
 * @param {*} success - success callback function
 */
  getConferences (page, size, success) {
    Vue.http.get(`${CONFERENCE_PUBLIC_URL}/${page}/${size}`).then(response => {
      success(response.body);
    });
  },

  conference: {

    /**
     * Get conference events
     * @param {Number} conference_id - conference id
     * @param {Function} success - success callback function
     */
    getEvents (conference_id, success) {
      Vue.http.get(`${CONFERENCE_PUBLIC_URL}/${conference_id}/event`).then(response => {
        success(response.body);
      });
    },

    /**
     * Get articles for conference
     * @param {Number} conference_id - conference id
     * @param {Function} success - success callback function
     */
    getArticles (conference_id, success) {
      Vue.http.get(`${CONFERENCE_PUBLIC_URL}/${conference_id}/article`).then(response => {
        success(response.body);
      });
    }
  },

  /**
   * Get all page categories.
   * @param {Function} success - success callback function
   */
  getCategories (success) {
    Vue.http.get('api/categories').then(response => {
      success(response.body);
    });
  },

  /**
   * Get all page branches for category.
   * @param {Number} category_id - category id
   * @param {Function} success - success callback function
   */
  getBranches (category_id, success) {
    var url = ['api/categories', category_id, 'branches'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },

  /**
   * Get meta types
   * @param {*} success - success callback function
   */
  getMetaTypes (success) {
    Vue.http.get('public/meta-types').then(response => {
      success(response.body);
    });
  },

  /**
   * Get widget types enum values.
   * @param {*} success - success callback function
   */
  getWidgetTypes (success) {
    Vue.http.get('public/widget-types').then(response => {
      success(response.body);
    });
  },

  getLocales (success) {
    Vue.http.get('public/locales').then(response => {
      success(response.body);
    });
  }
};

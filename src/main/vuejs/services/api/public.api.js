import Vue from 'vue';
import { DateTime } from 'luxon';

const EVENT_PUBLIC_URL = '/public/v1/event';
const PAGE_PUBLIC_URL = '/public/v1/page';
const CONFERENCE_PUBLIC_URL = '/public/v1/conference';
const USER_PUBLIC_URL = '/public/v1/user';

export default {

  getEvents (page, size, filters, success) {
    Vue.http.get(`/public/v1/events/${page}/${size}`, {
      params: filters
    }).then(response => {
      success(response.body);
    });
  },

  getFeaturedEvents (page, size, filters, success) {
    Vue.http.get(`/public/v1/featured-events/${page}/${size}`, {
      params: {
        ...filters
      }
    }).then(response => {
      success(response.body);
    });
  },

  /**
   * Get events near location specified and distance.
   * @param {Int} page - paginator page property
   * @param {Int} size - paginator size property
   * @param {Object} params - request params
   * @param {Long} params.lat - requested latitude (required)
   * @param {Long} params.lng - requested longitude (required)
   * @param {Long} params.distance - requested distance from location specified (default: 10)
   * @param {*} params.fromDate - from date timestamp
   * @param {*} params.toDate - to date timestamp
   * @param {Function} success - success callback function
   */
  getNearEvents (page, size, params, success) {
    Vue.http.get(`/public/v1/near-events/${page}/${size}`, {
      params: {
        lat: params.lat,
        lng: params.lng,
        distance: params.distance || 10,
        fromDate: params.fromDate || DateTime.utc().valueOf(),
        toDate: params.toDate || DateTime.utc().plus({ months: 1 }).valueOf()
      }
    }).then(response => {
      success(response.body);
    });
  },

  user: {
    getUser (id, success, error) {
      Vue.http.get(`${USER_PUBLIC_URL}/${id}`).then(response => {
        success(response.body);
      }, response => {
        if (error) {
          error(response);
        }
      });
    },

    getEvents (id, page, size, success) {
      Vue.http.get(`${USER_PUBLIC_URL}/${id}/event/${page}/${size}`).then(response => {
        success(response.body);
      }, response => {

      });
    }
  },

  event: {
    /**
    * Get event public data.
    * @param {Number} id - event id
    * @param {Function} success - success callback function
    */
    getEvent (id, success, error) {
      Vue.http.get(`${EVENT_PUBLIC_URL}/${id}`).then(response => {
        success(response.body);
      }, response => {
        if (error) {
          error(response);
        }
      });
    },

    /**
     * Get events related events
     * @param {*} id - event id
     * @param {*} success - success callback function
     */
    getRelated (id, success) {
      Vue.http.get(`${EVENT_PUBLIC_URL}/${id}/related`).then(response => {
        success(response.body);
      });
    },

    /**
     * Get public accessible gallery for event
     * @param {*} id - event id
     * @param {*} success - success callback function
     */
    getGallery (id, success) {
      Vue.http.get(`${EVENT_PUBLIC_URL}/${id}/gallery`).then(response => {
        success(response.body);
      });
    }
  },

  /**
   * Get all public popular pages.
   * @param {Function} success - success callback function
   */
  getPopularPages (page, size, success) {
    Vue.http.get(`/public/v1/popular-pages/${page}/${size}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get public pages
   * @param {*} page - paginator page attribute
   * @param {*} size - paginator size attribute
   * @param {*} filters - filters
   * @param {*} success - success callback function
   */
  getPages (page, size, filters, success) {
    Vue.http.get(`/public/v1/pages/${page}/${size}`, {
      params: {
        ...filters
      }
    }).then(response => {
      success(response.body);
    });
  },

  page: {
    /**
     * Get public page data.
     * @param {Number} id - page id
     * @param {Function} success - success callback function
     */
    getPage (id, success, error) {
      Vue.http.get(`${PAGE_PUBLIC_URL}/${id}`).then(response => {
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
      Vue.http.get(`${PAGE_PUBLIC_URL}/${slug}`).then(response => {
        success(response.body);
      });
    },

    /**
     * Get public accessible events for page.
     * @param {Number} id - page id
     * @param {Number} page - paginator page property
     * @param {Number} size - paginator size property
     * @param {Function} success - success callback function
     */
    getEvents (id, page, size, success) {
      Vue.http.get(`${PAGE_PUBLIC_URL}/${id}/event/${page}/${size}`, {
        params: {
          fromDate: DateTime.local().toSQLDate(),
          toDate: DateTime.local().plus({ months: 1 }).toSQLDate(),
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
     * Get public data for conference
     * @param {*} conference_id - conference id
     * @param {*} success - success callback function
     */
    getConference (conference_id, success, error) {
      Vue.http.get(`${CONFERENCE_PUBLIC_URL}/${conference_id}`).then(response => {
        success(response.body);
      }, response => {
        if (error) {
          error(response);
        }
      });
    },

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
    getArticles (conference_id, page, size, success) {
      Vue.http.get(`${CONFERENCE_PUBLIC_URL}/${conference_id}/article/${page}/${size}`).then(response => {
        success(response.body);
      });
    }
  },

  /**
   * Get available page categories.
   * @param {Function} success - success callback function
   */
  getCategories (filters, success) {
    Vue.http.get('/public/v1/categories', {
      params: {
        used: filters.used
      }
    }).then(response => {
      const categories = response.body.filter(c => c);
      success(categories);
    });
  },

  /**
   * Get available page branches for category.
   * @param {Number} category_id - category id
   * @param {Function} success - success callback function
   */
  getBranches (category_id, filters, success) {
    Vue.http.get(`/public/v1/categories/${category_id}/branches`, {
      params: {
        used: filters.used
      }
    }).then(response => {
      success(response.body);
    });
  },

  /**
   * Get meta types
   * @param {*} success - success callback function
   */
  getMetaTypes (success) {
    Vue.http.get('/public/v1/meta-types').then(response => {
      success(response.body);
    });
  },

  getLocales (success) {
    Vue.http.get('/public/v1/locales').then(response => {
      success(response.body);
    });
  },

  /**
   * Get Unit enum vales
   * @param {*} success - success callback function
   */
  getUnits (success) {
    Vue.http.get('/public/v1/unit').then(response => {
      success(response.body);
    });
  },

  getRoles (success) {
    Vue.http.get('/public/v1/roles').then(response => {
      success(response.body);
    });
  }
};

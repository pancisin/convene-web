import Vue from 'vue';
import moment from 'moment';

const EVENT_PUBLIC_URL = '/public/event';
const PAGE_PUBLIC_URL = '/public/page';
const CONFERENCE_PUBLIC_URL = '/public/conference';
const USER_PUBLIC_URL = '/public/user';

export default {

  getEvents (page, size, filters, success) {
    Vue.http.get(`/public/events/${page}/${size}`, {
      params: filters
    }).then(response => {
      success(response.body);
    });
  },

  getFeaturedEvents (page, size, filters, success) {
    Vue.http.get(`/public/featured-events/${page}/${size}`, {
      params: {
        ...filters
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
  getPopularPages (success) {
    Vue.http.get('/api/public/popular-pages/').then(response => {
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
    Vue.http.get(`/public/pages/${page}/${size}`, {
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
          fromDate: moment().format('YYYY-MM-DD'),
          toDate: moment().add(1, 'M').format('YYYY-MM-DD'),
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
  getCategories (success) {
    Vue.http.get('/public/categories').then(response => {
      const categories = response.body.filter(c => c);
      success(categories);
    });
  },

  /**
   * Get available page branches for category.
   * @param {Number} category_id - category id
   * @param {Function} success - success callback function
   */
  getBranches (category_id, success) {
    Vue.http.get(`/public/categories/${category_id}/branches`).then(response => {
      success(response.body);
    });
  },

  /**
   * Get meta types
   * @param {*} success - success callback function
   */
  getMetaTypes (success) {
    Vue.http.get('/public/meta-types').then(response => {
      success(response.body);
    });
  },

  /**
   * Get widget types enum values.
   * @param {*} success - success callback function
   */
  getWidgetTypes (success) {
    Vue.http.get('/public/widget-types').then(response => {
      success(response.body);
    });
  },

  getLocales (success) {
    Vue.http.get('/public/locales').then(response => {
      success(response.body);
    });
  }
};

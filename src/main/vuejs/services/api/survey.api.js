import Vue from 'vue';

const SURVEY_API_URL = '/api/v1/survey';

export default {

  /**
   * Get survey by id.
   * @param {Number} id - survey id
   * @param {Function} success - success callback function
   */
  getSurvey (id, success) {
    Vue.http.get(`${SURVEY_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Update survey data.
   * @param {Number} id - survey id
   * @param {Object} survey - survey data object
   * @param {Function} success - success callback function
   */
  putSurvey (id, survey, success) {
    Vue.http.put(`${SURVEY_API_URL}/${id}`, survey).then(response => {
      success(response.body);
    });
  },

  /**
   * Delete survey by id.
   * @param {*} id - survey id
   * @param {*} success success callback function
   */
  deleteSurvey (id, success) {
    Vue.http.delete(`${SURVEY_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Post new meta field to survey.
   * @param {*} id - survey id
   * @param {*} metaField - meta field data object
   * @param {*} success - success callback function
   */
  postMetaField (id, metaField, success) {
    Vue.http.post(`${SURVEY_API_URL}/${id}/meta-field`, metaField).then(response => {
      success(response.body);
    });
  },

  /**
   * Post batch update of meta fields.
   * @param {*} id - survey id
   * @param {*} metaFields - array of meta field data objects
   * @param {*} success - success callback function
   */
  postMetaFields (id, metaFields, success) {
    Vue.http.post(`${SURVEY_API_URL}/${id}/meta-fields`, metaFields).then(response => {
      success(response.body);
    });
  },

  /**
   * Get meta fields for survey.
   * @param {*} id - survey id
   * @param {*} success - success callback function
   */
  getMetaFields (id, success) {
    Vue.http.get(`${SURVEY_API_URL}/${id}/meta-field`).then(response => {
      success(response.body);
    });
  },

  /**
   * Post answers to meta fields and meta values
   * @param {*} id - survey id
   * @param {*} values - meta values data objects array
   * @param {*} success - success callback function
   */
  postSubmission (id, values, success) {
    Vue.http.post(`${SURVEY_API_URL}/${id}/submission`, values).then(response => {
      success(response.body);
    });
  },

  /**
   * Get survey meta values
   * @param {*} id - survey id
   * @param {*} success - success callback function
   */
  getSubmissions (id, success) {
    Vue.http.get(`${SURVEY_API_URL}/${id}/submission`).then(response => {
      success(response.body);
    });
  },

  togglePublished (id, success) {
    Vue.http.patch(`${SURVEY_API_URL}/${id}/toggle-published`).then(response => {
      success(response.body);
    });
  }
};

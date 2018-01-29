import Vue from 'vue';

const FORM_API_URL = '/api/v1/form';

export default {

  /**
   * Get custom form definition by id
   * @param {*} id - form id
   * @param {*} success - success callback function
   */
  getForm (id, success) {
    Vue.http.get(`${FORM_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * put custom form definition
   * @param {*} id - form id
   * @param {*} form - form data object
   * @param {*} success - success callback function
   */
  putForm (id, form, success) {
    Vue.http.put(`${FORM_API_URL}/${id}`, form).then(response => {
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
    Vue.http.post(`${FORM_API_URL}/${id}/submission`, values).then(response => {
      success(response.body);
    });
  },

  /**
   * Get survey meta values
   * @param {*} id - survey id
   * @param {*} success - success callback function
   */
  getSubmissions (id, success) {
    Vue.http.get(`${FORM_API_URL}/${id}/submission`).then(response => {
      success(response.body);
    });
  }
};

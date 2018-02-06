import Vue from 'vue';

const FORM_SUBMISSION_API_URL = '/api/v1/form-submission';

export default {
  getFormSubmission (form_submission_id, success) {
    Vue.http.get(`${FORM_SUBMISSION_API_URL}/${form_submission_id}`).then(response => {
      success(response.body);
    });
  },

  putFormSubmission  (form_submission_id, survey_submission, success) {
    Vue.http.put(`${FORM_SUBMISSION_API_URL}/${form_submission_id}`, survey_submission).then(response => {
      success(response.body);
    });
  },

  deleteFormSubmission (form_submission_id, success) {
    Vue.http.delete(`${FORM_SUBMISSION_API_URL}/${form_submission_id}`).then(response => {
      success(response.body);
    });
  }
};

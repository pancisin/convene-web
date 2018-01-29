import Vue from 'vue';

const SURVEY_SUBMISSION_API_URL = '/api/v1/survey-submission';

export default {
  getSurveySubmission (survey_submission_id, success) {
    Vue.http.get(`${SURVEY_SUBMISSION_API_URL}/${survey_submission_id}`).then(response => {
      success(response.body);
    });
  },

  putSurveySubmission  (survey_submission_id, survey_submission, success) {
    Vue.http.put(`${SURVEY_SUBMISSION_API_URL}/${survey_submission_id}`, survey_submission).then(response => {
      success(response.body);
    });
  },

  deleteSurveySubmission (survey_submission_id, success) {
    Vue.http.delete(`${SURVEY_SUBMISSION_API_URL}/${survey_submission_id}`).then(response => {
      success(response.body);
    });
  }
};

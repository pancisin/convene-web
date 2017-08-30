import SurveyApi from 'api/survey.api';

export default class SurveyInjector {
  constructor (survey_id) {
    this.survey_id = survey_id;
  }

  getSurvey (success) {
    SurveyApi.getSurvey(this.survey_id, success);
  }

  putSurvey (survey, success) {
    SurveyApi.putSurvey(this.survey_id, survey, success);
  }
}

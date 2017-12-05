<template>
  <div class="container" v-loading="loading">
    <div class="col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3" v-if="survey != null">
      <h3>
        {{ survey.name }}
      </h3>
      <hr />
      <survey-form :survey="survey"></survey-form>
    </div>
  </div>
</template>

<script>
import SurveyApi from 'api/survey.api';
import SurveyForm from './survey/Survey.form';

export default {
  name: 'survey',
  data () {
    return {
      loading: false,
      survey: null
    };
  },
  components: {
    SurveyForm
  },
  created () {
    this.getSurvey();
  },
  methods: {
    getSurvey () {
      this.loading = true;
      SurveyApi.getSurvey(this.$route.params.survey_id, survey => {
        this.survey = survey;
        this.loading = false;
      });
    }
  }
};
</script>

<template>
 <panel type="primary" v-if="surveys.length > 0">
    <span slot="title">Surveys</span>
    <small slot="subtitle">Please spare a little time to complete these surveys.</small>
    <div class="surveys-list">
      <survey-form v-if="surveys.length == 1" :survey="surveys[0]"></survey-form>
      <ul class="list-unstyled" v-else>
        <li v-for="survey in surveys" :key="survey.id">
          <button class="btn btn-link" @click="startSurvey(survey)">
            {{ survey.name }}
          </button>
        </li>
      </ul>

      <modal :show.sync="showSurveyModal">
        <div slot="body">
          <survey-form v-if="selectedSurvey != null" :survey="selectedSurvey" @submit="surveySubmitted" />
        </div>
      </modal>
    </div>
  </panel>
</template>

<script>
import { SurveyForm } from 'elements/forms';
export default {
  name: 'survey-list',
  data () {
    return {
      surveys: [],
      showSurveyModal: false,
      selectedSurvey: null
    };
  },
  inject: ['provider'],
  computed: {
    api () {
      return this.provider.api;
    }
  },
  components: {
    SurveyForm
  },
  watch: {
    'api': 'getSurveys'
  },
  created () {
    this.getSurveys();
  },
  methods: {
    getSurveys () {
      this.api.getPublicSurveys(surveys => {
        this.surveys = surveys;
      });
    },
    startSurvey (survey) {
      this.selectedSurvey = survey;
      this.showSurveyModal = true;
    },
    surveySubmitted (survey) {
      this.surveys = this.surveys.filter(s => s.id !== survey.id);
      this.showSurveyModal = false;
    }
  }
};
</script>

<style lang="less">
.surveys-list>ul {
  li {
    a {
      padding: 5px 0;
      display: block;
    }
  }
}
</style>

<template>
 <panel type="primary" v-if="surveys.length > 0">
    <span slot="title">Surveys</span>
    <small slot="subtitle">Please spare a little time to complete these surveys.</small>
    <div class="surveys-list">
      <custom-form v-if="surveys.length == 1" :form="surveys[0].form" @submit="formSubmitted" />
      <ul class="list-unstyled" v-else>
        <li v-for="survey in surveys" :key="survey.id">
          <button class="btn btn-link" @click="startSurvey(survey)">
            {{ survey.name }}
          </button>
        </li>
      </ul>

      <modal :show.sync="showSurveyModal">
        <div slot="body">
          <custom-form v-if="selectedSurvey != null" :form="selectedSurvey.form" @submit="formSubmitted" />
        </div>
      </modal>
    </div>
  </panel>
</template>

<script>
import { CustomForm } from 'elements/forms';
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
    CustomForm
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
    formSubmitted (form) {
      const index = this.surveys.findIndex(s => s.form.id === form.id);
      const survey = this.surveys.splice(index, 1);

      this.$success('notification.survey.completed', survey.name);
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

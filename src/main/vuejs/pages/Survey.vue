<template>
  <div class="row">
    <div class="col-sm-12">
      <div class="page-title-box">
        <h4 class="page-title" v-text="survey.name"></h4>
      </div>
    </div>
  
    <div class="col-md-3">
      <div class="list-group mail-list">
        <router-link :to="{ name: 'survey.results' }" class="list-group-item waves-effect">
          <i class="fa fa-dashboard"></i>
          Results
        </router-link>
        <router-link :to="{ name: 'survey.edit' }" class="list-group-item waves-effect">
          <i class="fa fa-pencil"></i>
          Edit
        </router-link>
      </div>
    </div>
    <div class="col-md-9">
      <transition name="fade-up" mode="out-in">
        <router-view :survey="survey" @updated="updateSurvey"></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
import PublicApi from 'api/public.api';
import SurveyApi from 'api/survey.api';
import InjectorGenerator from '../services/InjectorGenerator';

export default {
  name: 'survey',
  data () {
    return {
      survey: {},
      injector: null
    };
  },
  provide () {
    const provider = {};

    Object.defineProperty(provider, 'api', {
      get: () => this.injector
    });

    return { provider };
  },
  watch: {
    '$route': 'getEvent'
  },
  created () {
    PublicApi.getMetaTypes(meta_types => {
      this.meta_types = meta_types;
    });

    this.getSurvey();
  },
  methods: {
    getSurvey () {

      this.injector = InjectorGenerator.generate(SurveyApi, this.$route.params.survey_id);

      this.injector.getSurvey(survey => {
        this.survey = survey;
        this.survey.metaFields.sort((a, b) => {
          return a.ordering >= b.ordering;
        });
      });
    },
    updateSurvey (survey) {
      this.survey = survey;
    }
  }
};
</script>
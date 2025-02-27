<template>
  <div>
    <panel type="table" v-for="(survey, index) in surveys" :key="index">
      <span slot="title">
        {{ survey.name }}
        <button type="button" class="btn btn-link pull-right" @click="editSurvey(survey)">
          <i class="fa fa-cogs"></i>
        </button>
      </span>

      <survey-panel :survey="survey" />
    </panel>

    <div class="text-center" v-if="editable">
      <button type="button" @click="createSurvey" class="btn btn-default">
        {{ $t('admin.survey.create') }}
      </button>
    </div>

    <modal :show.sync="displaySurveyModal">
      <span slot="header">Edit survey</span>
      
      <div slot="body">
        <survey-form 
          v-if="displaySurveyModal" 
          :survey="selectedSurvey" 
          @update="updateSurvey" />
      </div>
    </modal>

    <!-- <panel type="table" v-loading="loading">
      <span slot="title">{{ $t('admin.page.events') }}</span>

      <table class="table table-striped table-hover">
        <thead>
          <tr>
            <th>
              Name
            </th>
            <th>Start date</th>
            <th>End date</th>
            <th class="text-center">Submissions</th>
            <th class="text-center">State</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="survey in surveys" :key="survey.id" @contextmenu.prevent="$refs.menu.open($event, survey)" v-if="survey.state != 'DELETED'">
            <td>
              <router-link :to="{ name: 'survey', params: { survey_id: survey.id } }">
                {{ survey.name }}
              </router-link>
            </td>
            <td>{{ survey.start_date | luxon('D') }}</td>
            <td>{{ survey.end_date | luxon('D') }}</td>
            <td class="text-center">{{ survey.submissionsCount }}</td>
            <td class="text-center">
              <i class="fa fa-clock-o text-warning" aria-hidden="true" v-if="survey.state == 'IN_PROGRESS'"></i>
              <i class="fa fa-check text-success" aria-hidden="true" v-if="survey.state == 'COMPLETED'"></i>
              <i class="fa fa-circle-o" aria-hidden="true" v-if="survey.state == 'NEW'"></i>
              <i class="fa fa-trash-o text-danger" aria-hidden="true" v-if="survey.state == 'DELETED'"></i>
            </td>
          </tr>
        </tbody>
      </table>

      <context-menu ref="menu">
        <template slot-scope="props">
          <ul>
            <li>
              <router-link :to="{ name: 'survey.public', params: { survey_id: props.data.id } }">
                Go to survey
              </router-link>
            </li>
            <li>
              <router-link :to="{ name: 'survey', params: { survey_id: props.data.id } }">
                Overview
              </router-link>
            </li>

            <li class="separator"></li>
            <li :class="{ 'disabled' : !editable }">
              <a @click="deleteSurvey(props.data)">
                Delete
              </a>
            </li>
          </ul>
        </template>
      </context-menu>

      <div class="text-center" v-if="editable">
        <router-link :to="{ name: 'conference.survey.create' }" class="btn btn-default btn-rounded text-center">
          Create survey
        </router-link>
      </div>
    </panel> -->
  </div>
</template>

<script>
import SurveyApi from 'api/survey.api';
import { SurveyPanel } from 'components';
import { SurveyForm } from 'elements/forms';

export default {
  name: 'surveys',
  inject: ['provider'],
  props: {
    editable: Boolean
  },
  data () {
    return {
      surveys: [],
      loading: false,
      displaySurveyModal: false,
      selectedSurvey: {}
    };
  },
  computed: {
    api () {
      if (this.provider != null) {
        return this.provider.api;
      }
    }
  },
  components: {
    SurveyPanel,
    SurveyForm
  },
  created () {
    this.getSurveys();
  },
  watch: {
    'api': 'getSurveys'
  },
  methods: {
    getSurveys () {
      if (this.api != null) {
        this.loading = true;
        this.api.getSurveys(surveys => {
          this.surveys = surveys;
          this.loading = false;
        });
      }
    },
    editSurvey (survey) {
      this.selectedSurvey = survey;
      this.displaySurveyModal = true;
    },
    createSurvey () {
      this.selectedSurvey = {};
      this.displaySurveyModal = true;
    },
    updateSurvey (survey) {
      this.surveys = this.surveys.filter(s => {
        return s.id !== survey.id;
      });
      this.surveys.push(survey);
      this.displaySurveyModal = false;
    },
    deleteSurvey (survey) {
      this.$prompt('notification.delete_prompt', survey.name, () => {
        SurveyApi.deleteSurvey(survey.id, result => {
          this.surveys.forEach((s, index) => {
            if (s.id === result.id) {
              this.surveys.splice(index, 1, result);
            };
          });
        });
      });
    }
  }
};
</script>
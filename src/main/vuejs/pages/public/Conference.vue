<template>
  <div class="container" v-if="conference != null">
    <h3>
      {{ conference.name }}
    </h3>

    <div class="row">
      <div class="col-md-9">

        <div class="row">
          <div class="col-sm-4">
            <img :src="conference.bannerUrl" class="img-poster">
          </div>
          <div class="col-sm-8 custom-content">
            <div v-html="conference.summary"></div>
          </div>
        </div>

        <div class="row">
          <div class="col-xs-12">
            <panel>
              <span slot="title">News</span>
              <articles-list></articles-list>
            </panel>
          </div>
        </div>

      </div>

      <div class="col-md-3">
        <panel v-if="attend_status != 'ACTIVE'" type="primary">
          <span slot="title">Join conference</span>
          <attend-form @statusChanged="statusChanged"></attend-form>
        </panel>
        <div v-else>
          <panel type="primary" v-if="surveys.length > 0">
            <span slot="title">Surveys</span>
            <small slot="subtitle">Please spare a little time to complete these surveys.</small>

            <div class="surveys-list" v-if="surveys != null && surveys.length > 0">
              <survey-form v-if="surveys.length == 1" :survey="surveys[0]"></survey-form>
              <ul class="list-unstyled" v-else>
                <li v-for="survey in surveys" :key="survey.id">
                  <router-link :to="{ name: 'survey.public', params: { survey_id: survey.id } }">
                    {{ survey.name }}
                  </router-link>
                </li>
              </ul>
            </div>
          </panel>

          <events-list></events-list>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ConferenceInjector from '../../services/injectors/conference.injector.js';
import AttendForm from './conference/Attend.form.vue';
import { ArticlesList } from 'elements';
import EventsList from './conference/Events.list.vue';
import SurveyForm from './survey/Survey.form';

export default {
  name: 'conference',
  provide () {
    return {
      api: new ConferenceInjector(this.$route.params.id)
    };
  },
  components: {
    AttendForm, ArticlesList, EventsList, SurveyForm
  },
  data () {
    return {
      conference: null,
      attend_status: false,
      surveys: [],
      display_survey_modal: true
    };
  },
  created () {
    var injector = new ConferenceInjector(this.$route.params.id);
    injector.getConference(conference => {
      this.conference = conference;
    });

    injector.getAttendStatus(status => {
      this.attend_status = status;
    });

    injector.getPublicSurveys(surveys => {
      this.surveys = surveys;
    });
  },
  methods: {
    statusChanged (status) {
      this.attend_status = status;
    }
  }
};
</script>

<style lang="less">
.img-poster {
  width: 100%;
  border: 1px solid #ccc;
  margin-bottom: 20px;
}

.surveys-list>ul {
  li {
    a {
      padding: 5px 0;
      display: block;
    }
  }
}
</style>
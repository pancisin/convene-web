<template>
  <div v-if="conference != null">
    <hero-unit :background="conference.poster != null ? conference.poster.path : null" class="m-b-20">
      <h1 class="text-uppercase text-primary">{{ conference.name }}</h1>
    </hero-unit>

    <div class="container">
      <div class="row">
        <div class="col-sm-8 col-md-5 col-md-offset-2 custom-content">
          <panel>
            <div v-html="conference.summary" class="m-b-20"></div>
          </panel>

          <panel>
            <span slot="title">News</span>
            <articles-list 
              :articles="articles">
            </articles-list>
          </panel>
        </div>

        <div class="col-sm-4 col-md-3">
          <img v-if="conference.poster != null" :src="conference.poster.path" class="img-poster">
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
  </div>
</template>

<script>
import AttendForm from './conference/Attend.form.vue';
import { ArticlesList, HeroUnit } from 'elements';
import EventsList from './conference/Events.list.vue';
import SurveyForm from './survey/Survey.form';

import ConferenceApi from 'api/conference.api';
import InjectorGenerator from '../../services/InjectorGenerator';

export default {
  name: 'conference',
  provide () {
    return {
      api: InjectorGenerator.generate(ConferenceApi, this.$route.params.id)
    };
  },
  components: {
    AttendForm, ArticlesList, EventsList, SurveyForm, HeroUnit
  },
  data () {
    return {
      conference: null,
      attend_status: false,
      surveys: [],
      articles: []
    };
  },
  created () {
    var injector = InjectorGenerator.generate(ConferenceApi, this.$route.params.id);
    injector.getConference(conference => {
      this.conference = conference;
    });

    injector.getAttendStatus(status => {
      this.attend_status = status;
    });

    injector.getPublicSurveys(surveys => {
      this.surveys = surveys;
    });

    injector.getArticles(articles => {
      this.articles = articles;
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
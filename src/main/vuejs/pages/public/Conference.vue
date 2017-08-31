<template>
  <div class="container" v-if="conference != null">
    <h3>
      {{conference.name}}
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
          <events-list></events-list>
        </div>
      </div>
    </div>

    <modal :show.sync="display_survey_modal" @close="display_survey_modal = false" :full="false">
      <h4 slot="header">{{ surveys[0].name }}</h4>
      <div slot="body">
        <survey :survey="surveys[0]" v-if="surveys.length > 0"></survey>
      </div>
    </modal>
  </div>
</template>

<script>
import ConferenceInjector from '../../services/injectors/conference.injector.js';
import AttendForm from './conference/Attend.form.vue';
import ArticlesList from '../../elements/ArticlesList.vue';
import EventsList from './conference/Events.list.vue';
import Survey from './conference/Survey';

export default {
  name: 'conference',
  provide () {
    return {
      api: new ConferenceInjector(this.$route.params.id)
    };
  },
  components: {
    AttendForm, ArticlesList, EventsList, Survey
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

    injector.getSurveys(surveys => {
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

<style>
.img-poster {
  width: 100%;
  border: 1px solid #ccc;
  margin-bottom: 20px;
}
</style>
<template>
  <div class="container">
    <h3 v-if="conference != null">
      {{conference.name}}
    </h3>
  
    <div class="row">
      <div class="col-md-9">
  
        <div class="row">
          <div class="col-sm-4">
            <img :src="conference.bannerUrl" class="img-poster" v-if="conference != null">
          </div>
          <div class="col-sm-8">
            <div v-html="conference.summary" v-if="conference != null"></div>
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
  </div>
</template>

<script>
import ConferenceInjector from '../../services/injectors/conference.injector.js';
import AttendForm from './conference/Attend.form.vue';
import ArticlesList from '../../elements/ArticlesList.vue';
import EventsList from './conference/Events.list.vue';

export default {
  name: 'conference',
  provide () {
    return {
      api: new ConferenceInjector(this.$route.params.id)
    };
  },
  components: {
    AttendForm, ArticlesList, EventsList
  },
  data () {
    return {
      conference: null,
      attend_status: false
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
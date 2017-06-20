<template>
  <div class="row">
    <div class="col-sm-12">
      <div class="page-title-box">
        <h4 class="page-title" v-text="conference.name"></h4>
      </div>
    </div>
  
    <div class="col-xs-12">
      <transition name="fade-down" mode="out-in">
        <keep-alive>
          <router-view :conference="conference" :edit="edit" @updated="conferenceUpdated"></router-view>
        </keep-alive>
      </transition>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import ConferenceApi from '../services/api/conference.api.js'
import ConferenceInjector from '../services/injectors/conference.injector.js'

export default {
  name: 'conference',
  data() {
    return {
      conference: new Object(),
      edit: false
    }
  },
  provide() {
    return {
      api: new ConferenceInjector(this.$route.params.id)
    }
  },
  created() {
    this.getConference();
  },
  methods: {
    ...mapActions([
      'updateConference'
    ]),
    getConference() {
      var injector = new ConferenceInjector(this.$route.params.id);
      injector.getConference(conference => {
        this.conference = conference;
        this.edit = conference.id != null;
      });
    },
    conferenceUpdated(conference) {
      this.conference = conference;
      this.updateConference(conference);
    }
  }
}
</script>
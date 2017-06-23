<template>
  <div class="container">
    <h3 v-if="conference != null">
      {{conference.name}}
    </h3>
  
    <div class="row">
      <div class="col-md-9">
  
        <div class="row">
          <div class="col-md-4">
            <img :src="conference.bannerUrl" class="img-poster">
          </div>
          <div class="col-md-8">
            <panel>
              <div v-html="conference.summary" v-if="conference != null"></div>
            </panel>
          </div>
        </div>
  
        <panel type="primary">
          <span slot="title">News</span>
  
          <articles-list></articles-list>
        </panel>
      </div>
  
      <div class="col-md-3">
        <panel v-if="attend_status != 'ACTIVE'" type="success">
          <span slot="title">Join conference</span>
          <attend-form @statusChanged="statusChanged"></attend-form>
        </panel>
        <panel v-else>
          <span slot="title">Details</span>
  
        </panel>
      </div>
    </div>
  </div>
</template>

<script>
import ConferenceInjector from '../../services/injectors/conference.injector.js'
import AttendForm from './conference/Attend.form.vue'
import ArticlesList from '../../elements/ArticlesList.vue'

export default {
  name: 'conference',
  provide() {
    return {
      api: new ConferenceInjector(this.$route.params.id)
    }
  },
  components: {
    AttendForm, ArticlesList
  },
  data() {
    return {
      conference: null,
      attend_status: false,
    }
  },
  created() {
    var injector = new ConferenceInjector(this.$route.params.id);
    injector.getConference(conference => {
      this.conference = conference;
    })

    injector.getAttendStatus(status => {
      this.attend_status = status;
    })
  },
  methods: {
    statusChanged(status) {
      this.attend_status = status;
    }
  }
}
</script>

<style>
.img-poster {
  width: 100%;
  border: 1px solid #ccc;
  margin-bottom: 20px;
}
</style>
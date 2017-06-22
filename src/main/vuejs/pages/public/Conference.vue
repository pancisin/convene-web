<template>
  <div class="container">
    <h3 v-if="conference != null">
      {{conference.name}}
    </h3>
  
    <div class="row">
      <div class="col-md-3">
        <img :src="conference.bannerUrl" class="img-poster" />
      </div>
  
      <div class="col-md-6">
        <panel>
          <div v-html="conference.summary" v-if="conference != null"></div>
        </panel>
      </div>
  
      <div class="col-md-3">
        <panel v-if="attend_status != 'ACTIVE'" type="success">
          <span slot="title">Join conference</span>
          <attend-form @statusChanged="statusChanged" />
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

export default {
  name: 'conference',
  provide() {
    return {
      api: new ConferenceInjector(this.$route.params.id)
    }
  },
  components: {
    AttendForm
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
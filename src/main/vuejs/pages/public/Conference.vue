<template>
  <div class="container">
    <h2 v-if="conference != null">
      {{conference.name}}
    </h2>
  
    <div class="row">
      <div class="col-md-3">
      </div>
  
      <div class="col-md-6">
        <panel>
          <div v-html="conference.summary"></div>
        </panel>
      </div>
  
      <div class="col-md-3">
        <panel v-if="attend_status == 'INACTIVE'">
          <span slot="title">Register</span>
          <attend-form />
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

  }
}
</script>

<style>

</style>
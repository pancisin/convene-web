<template>
  <div class="container">
    <h1 v-if="conference != null">
      {{conference.name}}
    </h1>
  
    <div class="row">
      <div class="col-md-4 col-md-offset-4">
        <attend-form />
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
    }
  },
  created() {
    var injector = new ConferenceInjector(this.$route.params.id);
    injector.getConference(conference => {
      this.conference = conference;
    })
  },
  methods: {

  }
}
</script>

<style>

</style>
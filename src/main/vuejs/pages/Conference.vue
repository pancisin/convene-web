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
export default {
  name: 'conference',
  data() {
    return {
      conference: new Object(),
      edit: false
    }
  },
  watch: {
    '$route': 'getConference'
  },
  created() {
    this.getConference();
  },
  methods: {
    ...mapActions([
      'updateConference'
    ]),
    getConference() {
      this.conference = new Object();
      this.edit = false;

      var conference_id = this.$route.params.id;
      if (conference_id != null) {
        this.edit = true;
        this.$http.get('api/conference/' + conference_id).then(response => {
          this.conference = response.body;
        })
      }
    },
    conferenceUpdated(conference) {
      this.conference = conference;
      this.updateConference(conference);
    }
  }
}
</script>
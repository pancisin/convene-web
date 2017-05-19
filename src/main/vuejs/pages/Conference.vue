<template>
  <div class="row">
    <div class="col-xs-12">
      <h3 v-text="conference.name" class="page-title"></h3>
    </div>
    <div class="col-md-3">
      <div class="list-group">
        <router-link to="overview" class="list-group-item waves-effect">
          Overview
        </router-link>
        <router-link to="events" class="list-group-item waves-effect">
          Events
        </router-link>
        <!--<router-link to="followers" class="list-group-item">
          Attendants
        </router-link>-->
      </div>
    </div>
    <div class="col-md-9">
      <transition name="fade-up" mode="out-in">
        <router-view :conference="conference" :edit="edit"></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
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
  }
}
</script>
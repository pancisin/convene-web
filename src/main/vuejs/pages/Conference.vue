<template>
  <div class="row">
    <div class="col-xs-12">
      <h3 v-text="conference.name" class="page-title"></h3>
    </div>
    <div class="col-xs-12">
      <transition name="fade-down" mode="out-in">
        <keep-alive>
          <router-view :conference="conference" :edit="edit"></router-view>
        </keep-alive>
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
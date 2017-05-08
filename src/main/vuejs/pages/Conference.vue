<template>
  <div class="container" v-if="conference != null">
    <div class="card-box p-b-0">
      <h4 class="text-dark  header-title m-t-0" v-text="conference.name"></h4>
      <p class="text-muted m-b-25 font-13" v-text="conference.summary"></p>
  
      <ul>
        <li v-for="event in conference.events" v-text="event.name">
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: 'conference',
  data() {
    return {
      conference: new Object(),
    }
  },
  created() {
    var conference_id = this.$route.params.id;
    if (conference_id != null) {
      this.$http.get('api/conference/' + conference_id).then(response => {
        this.conference = response.body;
        this.getEvents(conference_id);
      })
    }
  },
  methods: {
    getEvents(conference_id) {
      var url = ['api/conference', conference_id, 'events'].join('/');
      this.$http.get(url).then(response => {
        this.conference.events = response.body;
      });
    }
  }
}
</script>
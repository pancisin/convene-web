<template>
  <div class="container" v-if="page != null">
    <h1>
      {{ page.name }}
    </h1>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'page',
  data () {
    return {
      follows: false,
      page: null,
      services: [],
      events: []
    };
  },
  created () {
    this.getPage();
  },
  computed: {
    ...mapGetters(['authenticated'])
  },
  methods: {
    getPage () {
      var page_id = this.$route.params.id;

      if (page_id == null) {
        var reg = new RegExp('www|bookster|convene|localhost:3000');
        var parts = window.location.host.split('.');

        if (!reg.test(parts[0])) {
          page_id = parts[0];
        }
      }

      if (page_id != null) {
        if (this.authenticated) {
          this.$http.get('api/page/' + page_id).then(response => {
            this.page = response.body;
            this.checkFollow();
            this.getServices('api');
            this.getEvents('api');
          });
        } else {
          this.$http.get('public/page/' + page_id).then(response => {
            this.page = response.body;
            this.getServices('public');
            this.getEvents('public');
          });
        }
      }
    },
    checkFollow () {
      var url = ['api/page', this.page.id, 'follow-status'].join('/');
      this.$http.get(url).then(response => {
        this.follows = response.body;
      });
    },
    getServices (context) {
      this.$http.get(context + '/page/' + this.page.id + '/service').then(response => {
        this.services = response.body;
      });
    },
    getEvents (context) {
      this.$http.get(context + '/page/' + this.page.id + '/event').then(response => {
        this.events = response.body;
      });
    },
    toggleFollow () {
      var url = ['api/page', this.page.id, 'toggle-follow'].join('/');
      this.$http.patch(url).then(response => {
        this.follows = response.body;
      });
    }
  }
};
</script>
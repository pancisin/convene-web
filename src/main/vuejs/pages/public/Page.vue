<template>
  <div class="container" v-if="page != null">
  
    <div class="row">
      <div class="col-md-8">
        <div class="panel panel-border panel-default">
          <div class="panel-heading">
            <h3 class="panel-title">{{ page.name }}
              <span class="label label-primary pull-right"> {{ page.followersCount }} followers</span>
            </h3>
  
            <p class="panel-sub-title font-13 text-muted"></p>
          </div>
          <div class="panel-body">
            <div v-html="page.summary" class="m-b-20"></div>
  
            <a class="btn btn-rounded btn-inverse" @click="toggleFollow">
              <span v-if="follows">Unfollow</span>
              <span v-else>Follow</span>
            </a>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card-box">
          <h4 class="text-dark  header-title m-t-0">Services</h4>
  
          <table class="table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Duration</th>
                <th>Price</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="service in services">
                <td v-text="service.name"></td>
                <td v-text="service.duration"></td>
                <td v-text="service.price"></td>
                <td>
                  <a class="btn btn-rounded btn-primary btn-xs">Book</a>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="card-box">
          <h4 class="text-dark  header-title m-t-0">Events</h4>
  
          <table class="table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="event in events">
                <td v-text="event.name"></td>
                <td v-text="event.date"></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  
  </div>
</template>

<script>
export default {
  name: 'page',
  data() {
    return {
      follows: false,
      page: null,
      services: [],
      events: [],
    }
  },
  watch: {
    '$route': 'getPage'
  },
  created() {
    this.getPage();
  },
  methods: {
    getPage() {
      var page_id = this.$route.params.id;
      if (page_id != null) {
        this.$http.get('api/page/' + page_id).then(response => {
          this.page = response.body;
          this.checkFollow();
          this.getServices();
          this.getEvents();
        })
      }
    },
    checkFollow() {
      var url = ['api/page', this.page.id, 'follow-status'].join('/');
      this.$http.get(url).then(response => {
        this.follows = response.body;
      })
    },
    getServices() {
      this.$http.get('api/page/' + this.page.id + '/service').then(response => {
        this.services = response.body;
      });
    },
    getEvents() {
      this.$http.get('api/page/' + this.page.id + '/event').then(response => {
        this.events = response.body;
      })
    },
    toggleFollow() {
      var url = ['api/page', this.page.id, 'toggle-follow'].join('/');
      this.$http.patch(url).then(response => {
        this.follows = response.body;
      })
    }
  }
}
</script>
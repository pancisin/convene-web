<template>
  <div class="container" v-if="page != null">
  
    <div class="row">
      <div class="col-md-8">
  
        <div class="panel panel-primary panel-blur">
          <div class="panel-heading">
            <img :src="page.bannerUrl" class="img" />
  
            <h3 class="panel-title">{{ page.name }}
              <span class="label label-primary pull-right"> {{ page.followersCount }} followers</span>
            </h3>
          </div>
          <div class="panel-body">
            <div v-html="page.summary" class="m-b-20"></div>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <a class="btn btn-block m-b-20" :class="{ 'btn-default' : follows, 'btn-inverse' : !follows }" @click="toggleFollow">
          <span v-if="follows">Unfollow</span>
          <span v-else>Follow</span>
        </a>
  
        <panel type="table" v-if="services && services.length > 0">
          <span slot="title">Services</span>
  
          <table class="table table-striped">
            <tbody>
              <tr v-for="service in services">
                <td v-text="service.name"></td>
                <td class="text-right">
                  <a class="btn btn-rounded btn-primary btn-xs" @click="bookService(service)">Book</a>
                </td>
              </tr>
            </tbody>
          </table>
        </panel>
  
        <panel type="table table-striped">
          <span slot="title">Events</span>
          <table class="table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="event in events">
                <td>
                  <router-link :to="'/event/' + event.id" v-text="event.name">
                  </router-link>
                </td>
                <td>{{ event.date | moment('DD.MM.YYYY') }}</td>
              </tr>
              <tr v-if="events.length == 0">
                <td colspan="2" class="text-center">There's nothing to display.</td>
              </tr>
            </tbody>
          </table>
        </panel>
  
      </div>
    </div>
  
    <modal :show.sync="displayBookModal" @close="displayBookModal = false" :full="false">
      <h4 slot="header">Book a service</h4>
      <div slot="body">
        <service-book :service="selectedService" @submitted="submitted" />
      </div>
    </modal>
  </div>
</template>

<script>
import ServiceBook from './page/Service.book.vue'
export default {
  name: 'page',
  data() {
    return {
      follows: false,
      page: null,
      services: [],
      events: [],
      selectedService: null,
      displayBookModal: false,
    }
  },
  components: {
    ServiceBook
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
    },
    bookService(service) {
      this.selectedService = service;
      this.displayBookModal = true;
    },
    submitted: function (bookRequest) {
      this.displayBookModal = false;
    }
  }
}
</script>
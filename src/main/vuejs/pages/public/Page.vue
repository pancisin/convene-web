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
        <a class="btn btn-block m-b-20 waves-effect" :class="{ 'btn-default' : follows, 'btn-inverse' : !follows }" @click="toggleFollow">
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

        <panel type="table">
          <span slot="title">Events</span>
          <table class="table table-striped">
            <tbody>
              <tr v-for="event in events">
                <td>
                  <router-link :to="'/event/' + event.id" v-text="event.name">
                  </router-link>
                </td>
                <td>{{ event.date | moment('L') }}</td>
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
import ServiceBook from './page/Service.book.vue';
import PageApi from 'api/page.api';
import PublicApi from 'api/public.api';

import { mapGetters } from 'vuex';

import PageInjector from '../../services/injectors/page.injector.js';

export default {
  name: 'page',
  provide () {
    return {
      api: new PageInjector(this.$route.params.id)
    };
  },
  data () {
    return {
      follows: false,
      page: null,
      services: [],
      events: [],
      selectedService: null,
      displayBookModal: false
    };
  },
  components: {
    ServiceBook
  },
  watch: {
    '$route': 'getPage'
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
        var reg = new RegExp('www|bookster|localhost:3000');
        var parts = window.location.host.split('.');

        if (!reg.test(parts[0])) {
          page_id = parts[0];
        }
      } else {
        if (this.authenticated) {
          PageApi.getPage(page_id, page => {
            this.page = page;

            PageApi.getFollowStatus(page.id, status => {
              this.follows = status;
            });

            PageApi.getEvents(this.page.id, 0, 10, paginator => {
              this.events = paginator.content;
            });
          });
        } else {
          PublicApi.getPage(page_id, page => {
            this.page = page;

            PublicApi.page.getEvents(page.id, 0, 10, paginator => {
              this.events = paginator.content;
            });

            PublicApi.getServices(this.page.id, services => {
              this.services = services;
            });
          });
        }
      }
    },
    toggleFollow () {
      PageApi.toggleFollowStatus(this.page.id, status => {
        this.follows = status;
      });
    },
    bookService (service) {
      this.selectedService = service;
      this.displayBookModal = true;
    },
    submitted: function (bookRequest) {
      this.displayBookModal = false;
    }
  }
};
</script>
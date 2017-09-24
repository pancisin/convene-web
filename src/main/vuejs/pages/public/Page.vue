<template>
  <div class="container" v-if="page != null">

    <div class="row">
      <div class="col-sm-3 col-sm-push-9">
        <a class="btn btn-block m-b-20 waves-effect" :class="{ 'btn-default' : follows, 'btn-inverse' : !follows }" @click="toggleFollow">
          {{ follows ? 'Unfollow' : 'Follow' }}
        </a>
        <img class="img-poster m-b-20" v-if="page.poster != null" :src="page.poster.path">
      </div>
      <div class="col-sm-6">
        <div class="panel panel-primary panel-blur">
          <div class="panel-heading">
            <img v-if="page.poster != null" :src="page.poster.path" class="img" />

            <h3 class="panel-title">{{ page.name }}
              <span class="label label-primary pull-right"> {{ page.followersCount }} followers</span>
            </h3>
          </div>
          <div class="panel-body">
            <div v-html="page.summary" class="m-b-20"></div>
          </div>
        </div>
      </div>
      <div class="col-sm-3 col-sm-pull-9">
        <panel type="table" v-if="services && services.length > 0">
          <span slot="title">Services</span>

          <table class="table table-striped">
            <tbody>
              <tr v-for="(service, index) in services" :key="index">
                <td v-text="service.name"></td>
                <td class="text-right">
                  <a class="btn btn-rounded btn-primary btn-xs" @click="bookService(service)">Book</a>
                </td>
              </tr>
            </tbody>
          </table>
        </panel>

        <panel>
          <span slot="title">Events</span>
          <events-list :events="events"></events-list>
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
import { EventsList } from 'elements';

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
    ServiceBook, EventsList
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

            PublicApi.page.getServices(this.page.id, services => {
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

<style lang="less">
.img-poster {
  width: 100%;
  border: 1px solid #ccc;
}
</style>
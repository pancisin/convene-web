<template>
  <div v-if="page != null">
    <hero-unit :background="page.poster != null ? page.poster.path : null" class="m-b-20">
      <h1 class="text-uppercase text-primary">{{ page.name }}</h1>
    </hero-unit>  

    <div class="container">
      <div class="row">
        <div class="col-sm-8 col-md-5 col-md-offset-2 custom-content">
          <panel v-if="page.summary != null && page.summary.length > 0">
            <div v-html="page.summary" class="m-b-20"></div>
          </panel>

          <panel>
            <span slot="title">Upcoming events</span>
            <events-list :events="events"></events-list>
          </panel>
        </div>

        <div class="col-sm-4 col-md-3">
          <a class="btn btn-block m-b-20 waves-effect" :class="{ 'btn-default' : follows, 'btn-inverse' : !follows }" @click="togglePageFollow(page)">
            {{ follows ? 'Unfollow' : 'Follow' }}
          </a>
          <img class="img-poster m-b-20" v-if="page.poster != null" :src="page.poster.path">

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

          <div class="m-b-20">
            <a @click="displayReportModal = true" class="btn btn-link btn-xs text-danger">Report abuse or illegal activity</a>
          </div>
        </div>
      </div>
    </div>

    <modal :show.sync="displayBookModal">
      <span slot="header">Book a service</span>
      <div slot="body">
        <service-book 
          :service="selectedService" 
          @submitted="displayBookModal = false" 
        />
      </div>
    </modal>

    <modal :show.sync="displayReportModal">
      <span slot="header">Report abuse or illegal activity</span>
      <div slot="body">
        <abuse-report v-if="displayReportModal" />
      </div>
    </modal>
  </div>
</template>

<script>
import ServiceBook from './page/Service.book.vue';
import { EventsList, HeroUnit, AbuseReport } from 'elements';

import PageApi from 'api/page.api';
import PublicApi from 'api/public.api';

import { mapGetters, mapActions } from 'vuex';

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
      page: null,
      services: [],
      events: [],
      selectedService: null,
      displayBookModal: false,
      displayReportModal: false
    };
  },
  components: {
    ServiceBook,
    EventsList,
    HeroUnit,
    AbuseReport
  },
  watch: {
    '$route': 'getPage'
  },
  created () {
    this.getPage();
  },
  computed: {
    ...mapGetters(['authenticated', 'pageFollowStatus']),
    follows () {
      return this.pageFollowStatus(this.page.id);
    }
  },
  methods: {
    ...mapActions(['togglePageFollow']),
    getPage () {
      var page_id = this.$route.params.id;

      if (page_id == null) {
        var reg = new RegExp('www|bookster|convene|localhost:3000');
        var parts = window.location.host.split('.');

        if (!reg.test(parts[0])) {
          page_id = parts[0];
        }
      } else {
        if (this.authenticated) {
          PageApi.getPage(page_id, page => {
            this.page = page;

            PageApi.getEvents(this.page.id, 0, 10, paginator => {
              this.events = paginator.content;
            });

            PageApi.getServices(this.page.id, services => {
              this.services = services;
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
    bookService (service) {
      this.selectedService = service;
      this.displayBookModal = true;
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
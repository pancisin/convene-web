<template>
  <div v-loading="loading">
    <div v-if="page.id">
      <hero-unit :background="page.poster != null ? page.poster.path : null" class="m-b-20">
        <h1 class="text-uppercase text-primary">{{ page.name }}</h1>
      </hero-unit>  

      <div class="container">
        <div class="row">
          <div class="col-sm-8 col-md-5 col-md-offset-2 custom-content">
            <panel type="default" v-show="page.metadata && JSON.stringify(page.metadata) != '{}'">
              <span slot="title">Information</span>
              <dl class="dl-horizontal">
                <template v-for="(value, key) in page.metadata">
                  <dt :key="key">
                    {{ key }}
                  </dt>
                  <dd :key="key">
                    <a target="_blank" 
                      :href="value"
                      v-if="validUrl(value)">
                      {{ value }}
                    </a>
                    <span v-else>
                      {{ value }}
                    </span>
                  </dd>
                </template>
              </dl>
            </panel>
            
            <panel v-if="page.summary != null && page.summary.length > 0">
              <div v-html="page.summary" class="m-b-20"></div>
            </panel>

            <panel>
              <span slot="title">Upcoming events</span>
              <events-list :events="events"></events-list>
            </panel>
          </div>

          <div class="col-sm-4 col-md-3">
            <div class="btn-group-vertical btn-block m-b-20">
              <a 
                class="btn waves-effect" 
                :class="{ 'btn-default' : follows, 'btn-inverse' : !follows }" 
                @click="toggleFollow">

                {{ follows ? 'Unfollow' : 'Follow' }}
              </a>
              <router-link 
                :to="{ name: 'page.settings', params: { id: page.id }}" 
                v-if="page.privilege && page.privilege.active"
                class="btn btn-primary waves-effect">
                Settings
              </router-link>
            </div>

            <light-box v-if="page.poster != null" :image="page.poster.path">
              <vue-image class="img-poster m-b-20" :src="page.poster.path" />
            </light-box>

            <panel type="table" v-if="services && services.length > 0">
              <span slot="title">Services</span>

              <table class="table table-striped">
                <tbody>
                  <tr v-for="(service, index) in services" :key="index">
                    <td v-text="service.name"></td>
                    <td class="text-right">
                      <a class="btn btn-default btn-xs" @click="bookService(service)">Book</a>
                    </td>
                  </tr>
                </tbody>
              </table>
            </panel>

            <share-panel 
              class="m-b-20" 
              :title="page.name" 
              :description="page.summary"
              :media="page.poster != null ? page.poster.path : ''" />

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

    <error v-if="error" :status="error.status" />
  </div>
</template>

<script>
import ServiceBook from './page/Service.book.vue';
import {
  EventsList,
  HeroUnit,
  AbuseReport,
  SharePanel,
  LightBox,
  VueImage,
  Error
} from 'elements';
import PageApi from 'api/page.api';
import PublicApi from 'api/public.api';

import { mapGetters, mapActions } from 'vuex';
import { validUrl } from '../../services/helpers';

export default {
  name: 'page',
  data () {
    return {
      page: {},
      services: [],
      events: [],
      selectedService: null,
      displayBookModal: false,
      displayReportModal: false,
      loading: false,
      error: null
    };
  },
  components: {
    ServiceBook,
    EventsList,
    HeroUnit,
    AbuseReport,
    SharePanel,
    LightBox,
    VueImage,
    Error
  },
  watch: {
    '$route': 'initialize'
  },
  created () {
    this.initialize();
  },
  computed: {
    ...mapGetters(['authenticated', 'pageFollowStatus']),
    follows () {
      return this.pageFollowStatus(this.page.id);
    }
  },
  methods: {
    ...mapActions(['togglePageFollow']),
    initialize () {
      const page_id = this.$route.params.id;
      const api = this.authenticated ? PageApi : PublicApi.page;

      this.loading = true;
      api.getPage(page_id, page => {
        if (page.id) {
          this.page = page;
          api.getEvents(page.id, 0, 10, paginator => {
            this.events = paginator.content;
          });

          api.getServices(page.id, services => {
            this.services = services;
          });
        }
        this.loading = false;
      }, error => {
        this.error = error;
        this.loading = false;
      });
    },
    bookService (service) {
      const book = () => {
        this.selectedService = service;
        this.displayBookModal = true;
      };

      if (this.authenticated) {
        book();
      } else {
        this.$tryAuthenticate(() => {
          book();
        });
      }
    },
    validUrl (value) {
      return validUrl(value);
    },
    toggleFollow () {
      if (this.authenticated) {
        this.togglePageFollow(this.page);
      } else {
        this.$tryAuthenticate(() => {
          this.togglePageFollow(this.page);
        });
      }
    }
  },
  head: {
    title () {
      return this.page.name;
    },
    meta () {
      return {
        description: this.page.summary,
        'og:title': this.page.name,
        'og:image': this.page.poster != null ? this.page.poster.path : '',
        'og:site_name': 'Convene',
        'og:description': this.page.summary
      };
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
<template>
  <div>
    <div class="container">
      <div class="row">
        <div class="col-sm-6 col-md-3">
          <panel type="primary">
            <span slot="title">{{ $t('client.dashboard.attending') }}</span>
            <events-list :events="attendingEvents"></events-list>
          </panel>

          <h3 class="text-uppercase">Featured</h3>
          <featured-events />
        </div>

        <div class="col-md-3 col-sm-6 col-md-push-6">
          <tab-container>
            <tab :title="$t('client.dashboard.followed')"
              @navigated="tabNavigation">
              <pages-list :pages="followedPages" />
            </tab>

            <tab :title="$t('client.dashboard.popular')"
              @navigated="tabNavigation">
              <pages-list :pages="popular.content" followers />
            </tab>
          </tab-container>
        </div>

        <div class="col-sm-12 col-md-6 col-md-pull-3">
          <near-events interactive showWarning/>

          <articles-list 
            :articles="headlinesPaginator.content"
            v-loading="loadingHeadlines"
            hasHeadline
          />
          <div class="text-center">
            <paginator :paginator="headlinesPaginator"
              :fetch="getHeadlines" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  Paginator,
  Tab,
  TabContainer,
  HeroUnit,
  ArticlesList,
  EventsList,
  PagesList,
  EventMap
} from 'elements';

import { mapGetters } from 'vuex';
import RootApi from 'api/api';
import PublicApi from 'api/public.api';
import { FeaturedEvents, NearEvents } from 'components';
import { DateTime } from 'luxon';

export default {
  name: 'dashboard',
  data () {
    return {
      eventsPaginator: {},
      popular: [],
      headlinesPaginator: {},
      loadingHeadlines: false
    };
  },
  components: {
    Paginator,
    TabContainer,
    Tab,
    HeroUnit,
    ArticlesList,
    EventsList,
    PagesList,
    FeaturedEvents,
    EventMap,
    NearEvents
  },
  computed: {
    ...mapGetters([
      'user',
      'followedPages',
      'attendingEvents'
    ])
  },
  watch: {
    'user.locale': 'getHeadlines'
  },
  created () {
    this.getEvents(0);
  },
  methods: {
    getEvents (page) {
      navigator.geolocation.getCurrentPosition(position => {
        PublicApi.getNearEvents(page, 100, {
          lat: position.coords.latitude,
          lng: position.coords.longitude,
          distance: 10,
          fromDate: DateTime.utc().valueOf(),
          toDate: DateTime.utc().plus({ week: 1 }).valueOf()
        }, paginator => {
          this.eventsPaginator = paginator;
        });
      }, error => {
        console.warn(error);
      });
    },
    getHeadlines (page) {
      this.loadingHeadlines = true;
      RootApi.getHeadlines(this.user.locale.prop, page, 4, paginator => {
        this.headlinesPaginator = paginator;
        this.loadingHeadlines = false;
      });
    },
    tabNavigation (id, loading) {
      if (id === 1 && (this.popular == null || this.popular.length === 0)) {
        loading(true);
        PublicApi.getPopularPages(0, 10, paginator => {
          this.popular = paginator;
          loading(false);
        });
      }
    }
  }
};
</script>
<template>
  <div>
    <div class="suggested-pages-section">
      <div class="container">
        <suggested-pages />
      </div>
    </div>

    <div class="container">
      <!-- <suggested-pages class="m-b-20" /> -->

      <div class="row">
        <div class="col-sm-12 col-md-6 col-md-push-3">
          <near-events interactive showWarning/>
          <user-activity-feed />

          <!-- <panel type="default"> -->
          <!-- </panel> -->


          <!-- <articles-list 
            :articles="headlinesPaginator.content"
            v-loading="loadingHeadlines"
            hasHeadline
          />
          <div class="text-center">
            <paginator :paginator="headlinesPaginator"
              :fetch="getHeadlines" />
          </div> -->
        </div>

        <div class="col-sm-6 col-md-3 col-md-pull-6">
          <panel type="primary">
            <span slot="title">{{ $t('client.dashboard.attending') }}</span>
            <small slot="subtitle">Udalosti na ktoré som prihlásený a ešte sa nekonali.</small>
            <events-list :events="attendingEvents"></events-list>
          </panel>

          <h3 class="text-uppercase">Featured</h3>
          <featured-events />
        </div>

        <div class="col-md-3 col-sm-6">
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
import {
  FeaturedEvents,
  NearEvents,
  UserActivityFeed,
  SuggestedPages
} from 'components';
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
    NearEvents,
    UserActivityFeed,
    SuggestedPages
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

<style lang="less">
.suggested-pages-section {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
  // background: linear-gradient(to right, #f2994a, #f2c94c);
  margin-bottom: 20px;
  margin-top: -20px;

  background: linear-gradient(to right, #ff5f6d, #ffc371); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  padding: 20px 0;
}
</style>
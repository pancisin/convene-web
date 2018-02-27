<template>
  <div>
    <div class="suggested-pages-section">
      <div class="container">
        <suggested-pages />
      </div>
    </div>

    <div class="container">
      <div class="row">
        <div class="col-sm-12 col-md-6 col-md-push-3">
          <near-events interactive showWarning/>
          <user-activity-feed />
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
          <panel type="default">
            <span slot="title">{{ $t('client.dashboard.followed') }}</span>
            <pages-list 
              :pages="followedPages" 
              mini/>
          </panel>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  Paginator,
  PagesList,
  EventMap
} from 'elements';

import { mapGetters } from 'vuex';
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
      headlinesPaginator: {},
      loadingHeadlines: false
    };
  },
  components: {
    Paginator,
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
    }
  }
};
</script>

<style lang="less">
.suggested-pages-section {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
  margin-bottom: 20px;
  margin-top: -20px;

  background: linear-gradient(to right, #ff5f6d, #ffc371);
  padding: 20px 0;
}
</style>
<template>
  <div>
    <div class="container">
      <div class="row">
        <div class="col-sm-6 col-md-3">
          <panel type="primary">
            <span slot="title">{{ $t('client.dashboard.attending') }}</span>
            <events-list :events="attendingEvents"></events-list>
          </panel>

          <panel type="primary"
            v-if="eventsPaginator.content != null">
            <span slot="title">{{ $t('client.dashboard.near_events') }}</span>
            <events-list :events="eventsPaginator.content"></events-list>

            <div class="text-center">
              <paginator :paginator="eventsPaginator"
                :fetch="getEvents" />
            </div>
          </panel>
        </div>

        <div class="col-md-3 col-sm-6 col-md-push-6">
          <tab-container>
            <tab :title="$t('client.dashboard.followed')"
              @navigated="tabNavigation">
              <pages-list :pages="followedPages" />
            </tab>

            <tab :title="$t('client.dashboard.popular')"
              @navigated="tabNavigation">
              <pages-list :pages="popular" followers />
            </tab>
          </tab-container>
        </div>

        <div class="col-md-6 col-md-pull-3">
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
  PagesList
  } from 'elements';

import { mapGetters } from 'vuex';
import RootApi from 'api/api';

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
    PagesList
  },
  computed: {
    ...mapGetters([
      'authenticated',
      'user',
      'followedPages',
      'attendingEvents'
    ])
  },
  watch: {
    'user.locale': 'getHeadlines'
  },
  methods: {
    getEvents (page) {
      navigator.geolocation.getCurrentPosition(position => {
        var url = ['public/near-events', page, 5].join('/');
        this.$http
          .get(url, {
            params: {
              lat: position.coords.latitude,
              lng: position.coords.longitude,
              distance: 20
            }
          }).then(response => {
            this.eventsPaginator = response.body;
            this.eventsPaginator.content = this.eventsPaginator.content.filter(
              x => x
            );
          });
      });
    },
    getHeadlines (page) {
      this.loadingHeadlines = true;
      RootApi.getHeadlines(this.user.locale.name, page, 6, paginator => {
        this.headlinesPaginator = paginator;
        this.loadingHeadlines = false;
      });
    },
    tabNavigation (id, loading) {
      if (id === 1 && (this.popular == null || this.popular.length === 0)) {
        loading(true);
        this.$http.get('public/popular-pages/0/5').then(response => {
          this.popular = response.body.content;
          loading(false);
        });
      }
    }
  }
};
</script>
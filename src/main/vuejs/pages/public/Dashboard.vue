<template>
  <div>
    <div class="container">
      <div class="row">
        <div class="col-sm-6 col-md-3">
          <panel type="primary">
            <span slot="title">{{ $t('client.dashboard.attending') }}</span>
            <events-list :events="attending"></events-list>
          </panel>

          <panel type="primary"
            v-if="eventsPaginator.content != null">
            <span slot="title">{{ $t('client.dashboard.near_events') }}</span>
            <events-list :events="eventsPaginator.content"></events-list>

            <div class="text-center">
              <paginator :paginator="eventsPaginator"
                @navigate="eventsPaginatorNavigate" />
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
              @navigate="headlinesPaginatorNavigate" />
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

import UserApi from 'api/user.api';
import { mapGetters, mapActions } from 'vuex';
import RootApi from 'api/api';

export default {
  name: 'dashboard',
  data () {
    return {
      events: [],
      attending: [],
      eventsPaginator: {},
      popular: [],
      headlinesPaginator: [],
      loadingHeadlines: false
    };
  },
  created () {
    if (this.followedPages.length === 0) {
      this.initializeFollowedPages();
    }

    if (this.authenticated) {
      UserApi.getAttendingEvents(events => {
        this.attending = events;
      });
    }

    this.getHeadlines();
    this.getEvents(0);
    this.getPages(0);
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
    ...mapGetters(['authenticated', 'user', 'followedPages'])
  },
  watch: {
    'user.locale': 'getHeadlines'
  },
  methods: {
    ...mapActions(['initializeFollowedPages']),
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
          })
          .then(response => {
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
    eventsPaginatorNavigate (e) {
      if (e.direction != null) {
        this.getEvents(this.eventsPaginator.number + e.direction);
      } else if (e.page != null) {
        this.getEvents(e.page);
      }
    },
    headlinesPaginatorNavigate (e) {
      if (e.direction != null) {
        this.getHeadlines(this.headlinesPaginator.number + e.direction);
      } else if (e.page != null) {
        this.getHeadlines(e.page);
      }
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
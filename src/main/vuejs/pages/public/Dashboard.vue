<template>
  <div>
    <div class="container">
      <div class="row">
        <div class="col-md-3 col-lg-2 col-lg-offset-2">
          <panel type="primary">
            <span slot="title">{{ $t('client.dashboard.attending') }}</span>

            <div class="inbox-widget">
              <router-link :to="{ name: 'event.public', params: { id: event.id} }"
                v-for="(event, index) in attending"
                :key="event.id"
                :data-index="index"
                class="inbox-item">

                <div class="inbox-item-img"
                  v-if="event.poster != null">
                  <img :src="event.poster.path"
                    class="img-circle"
                    alt="">
                </div>

                <p class="inbox-item-author"
                  v-text="event.name"></p>
                <!-- <p class="inbox-item-date">{{ event.date | moment('L') }}</p> -->
                <small>
                  <i>{{ event.date | moment('L') }}</i>
                </small>
              </router-link>
            </div>
          </panel>

          <panel type="primary"
            v-if="eventsPaginator.content != null">
            <span slot="title">{{ $t('client.dashboard.near_events') }}</span>

            <div class="inbox-widget">
              <router-link :to="{ name: 'event.public', params: { id: event.id }}"
                v-for="(event, index) in eventsPaginator.content"
                :key="event.id"
                :data-index="index"
                class="inbox-item">

                <div class="inbox-item-img"
                  v-if="event.poster != null">
                  <img :src="event.poster.path"
                    class="img-circle"
                    alt="">
                </div>
                <p class="inbox-item-author"
                  v-text="event.name"></p>
                <p class="inbox-item-text"
                  v-if="event.summary != null"
                  v-strip="event.summary.substr(0, 200)">
                </p>
                <p class="inbox-item-date">{{ event.date | moment('L') }}</p>
              </router-link>

              <div class="text-center">
                <paginator :paginator="eventsPaginator"
                  @navigate="eventsPaginatorNavigate" />
              </div>
            </div>
          </panel>
        </div>
        <div class="col-md-6 col-lg-4">
          <articles-list :articles="headlines"
            v-loading="loadingHeadlines"></articles-list>
        </div>

        <div class="col-md-3 col-lg-2">
          <tab-container>
            <!-- <tab :title="$t('client.dashboard.suggested')">
                              <div class="inbox-widget">
                                <router-link :to="'page/' + page.id" v-for="(page, index) in pagesPaginator.content" :key="index" :data-index="index" class="inbox-item">
                                  <div class="inbox-item-img" v-if="page.poster != null">
                                    <img :src="page.poster.path" class="img-circle">
                                  </div>
                                  <p class="inbox-item-author" v-text="page.name"></p>
                                  <p class="inbox-item-text" v-if="page.category != null">
                                    {{ $t('category.' + page.category.code + '.' + page.branch.code) }}
                                  </p>
                                </router-link>

                                <div class="text-center">
                                  <paginator :paginator="pagesPaginator" @navigate="pagesPaginatorNavigate" />
                                </div>
                              </div>
                            </tab> -->
            <tab :title="$t('client.dashboard.followed')"
              @navigated="tabNavigation">
              <div class="inbox-widget">
                <router-link :to="'page/' + page.id"
                  v-for="(page, index) in followedPages"
                  :key="page.id"
                  :data-index="index"
                  class="inbox-item">

                  <div class="inbox-item-img"
                    v-if="page.poster != null">
                    <img :src="page.poster.path"
                      class="img-circle">
                  </div>
                  <p class="inbox-item-author"
                    v-text="page.name"></p>
                  <p class="inbox-item-text"
                    v-if="page.category != null">
                    {{ $t('category.' + page.category.code + '.' + page.branch.code) }}
                  </p>
                </router-link>
              </div>
            </tab>
            
            <tab :title="$t('client.dashboard.popular')"
              @navigated="tabNavigation">
              <div class="inbox-widget">
                <router-link :to="'page/' + page.id"
                  v-for="(page, index) in popular"
                  :key="page.id"
                  :data-index="index"
                  class="inbox-item">
                  <div class="inbox-item-img"
                    v-if="page.poster != null">
                    <img :src="page.poster.path"
                      class="img-circle">
                  </div>
                  <p class="inbox-item-author">
                    {{ page.name }}
                    <span class="pull-right label label-default">{{ page.followersCount }} followers</span>
                  </p>
                  <p class="inbox-item-text"
                    v-if="page.category != null">
                    {{ $t('category.' + page.category.code + '.' + page.branch.code) }}
                  </p>
                </router-link>
              </div>
            </tab>
          </tab-container>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Paginator, Tab, TabContainer, HeroUnit, ArticlesList } from 'elements';
import UserApi from 'api/user.api';
import { mapGetters, mapActions } from 'vuex';
import RootApi from 'api/api';

export default {
  name: 'dashboard',
  data () {
    return {
      events: [],
      attending: [],
      pagesPaginator: {},
      eventsPaginator: {},
      popular: [],
      headlines: [],
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
    Paginator, TabContainer, Tab, HeroUnit, ArticlesList
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
        this.$http.get(url, {
          params: {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
            distance: 20
          }
        }).then(response => {
          this.eventsPaginator = response.body;
          this.eventsPaginator.content = this.eventsPaginator.content.filter(x => x);
        });
      });
    },
    getHeadlines () {
      this.loadingHeadlines = true;
      RootApi.getHeadlines(this.user.locale.name, paginator => {
        this.headlines = paginator.content;
        this.loadingHeadlines = false;
      });
    },
    getPages (page) {
      var url = ['public/pages', page, 5].join('/');
      this.$http.get(url).then(response => {
        this.pagesPaginator = response.body;
      });
    },
    pagesPaginatorNavigate (e) {
      if (e.direction != null) {
        this.getPages(this.pagesPaginator.number + e.direction);
      } else if (e.page != null) {
        this.getPages(e.page);
      }
    },
    eventsPaginatorNavigate (e) {
      if (e.direction != null) {
        this.getEvents(this.eventsPaginator.number + e.direction);
      } else if (e.page != null) {
        this.getEvents(e.page);
      }
    },
    tabNavigation (id, loading) {
      if (id === 2 && (this.popular == null || this.popular.length === 0)) {
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
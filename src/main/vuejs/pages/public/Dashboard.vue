<template>
  <div class="container">
    <div class="row">
      <div class="col-md-4" v-if="authenticated">
  
        <panel type="primary">
          <span slot="title">{{ $t('client.dashboard.attending') }}</span>
  
          <div class="inbox-widget">
            <stagger-transition>
              <router-link :to="'event/' + event.id" v-for="(event, index) in attending" :key="event.id" :data-index="index">
                <div class="inbox-item">
                  <div class="inbox-item-img" v-if="event.bannerUrl != null">
                    <img :src="event.bannerUrl" class="img-circle" alt="">
                  </div>
                  <p class="inbox-item-author" v-text="event.name"></p>
                  <p class="inbox-item-text" v-if="event.summary != null" v-strip="event.summary.substr(0, 200)"></p>
                  <p class="inbox-item-date">{{ event.date | moment('L') }}</p>
                </div>
              </router-link>
            </stagger-transition>
          </div>
        </panel>
      </div>
  
      <div :class="{ 'col-md-4' : authenticated, 'col-md-8' : !authenticated }">
        <panel type="primary">
          <span slot="title">{{ $t('client.dashboard.near_events') }}</span>
  
          <div class="inbox-widget">
            <stagger-transition>
              <router-link :to="'event/' + event.id" v-for="(event, index) in eventsPaginator.content" :key="event.id" :data-index="index">
                <div class="inbox-item">
                  <div class="inbox-item-img" v-if="event.bannerUrl != null">
                    <img :src="event.bannerUrl" class="img-circle" alt="">
                  </div>
                  <p class="inbox-item-author" v-text="event.name"></p>
                  <p class="inbox-item-text" v-if="event.summary != null" v-strip="event.summary.substr(0, 200)"></p>
                  <p class="inbox-item-date">{{ event.date | moment('L') }}</p>
                </div>
              </router-link>
            </stagger-transition>
  
            <div class="text-center">
              <paginator :paginator="eventsPaginator" @navigate="eventsPaginatorNavigate" />
            </div>
          </div>
        </panel>
      </div>
  
      <div class="col-md-4">
        <!--<div class="page-title-box">
                    <h4 class="page-title">{{ $t('client.dashboard.pages') }}</h4>
                  </div>-->
        <tab-container>
          <tab :title="$t('client.dashboard.suggested')">
            <div class="inbox-widget">
              <stagger-transition>
                <router-link :to="'page/' + page.id" v-for="(page, index) in pagesPaginator.content" :key="page.id" :data-index="index">
                  <div class="inbox-item">
                    <div class="inbox-item-img" v-if="page.bannerUrl != null">
                      <img :src="page.bannerUrl" class="img-circle">
                    </div>
                    <p class="inbox-item-author" v-text="page.name"></p>
                    <p class="inbox-item-text" v-if="page.category != null">
                      {{ $t('category.' + page.category.code + '.' + page.branch.code) }}
                    </p>
                  </div>
                </router-link>
              </stagger-transition>
  
              <div class="text-center">
                <paginator :paginator="pagesPaginator" @navigate="pagesPaginatorNavigate" />
              </div>
            </div>
          </tab>
          <tab :title="$t('client.dashboard.followed')" @navigated="tabNavigation">
            <div class="inbox-widget">
              <stagger-transition>
                <router-link :to="'page/' + page.id" v-for="(page, index) in followed" :key="page.id" :data-index="index">
                  <div class="inbox-item">
                    <div class="inbox-item-img" v-if="page.bannerUrl != null">
                      <img :src="page.bannerUrl" class="img-circle">
                    </div>
                    <p class="inbox-item-author" v-text="page.name"></p>
                    <p class="inbox-item-text" v-if="page.category != null">
                      {{ $t('category.' + page.category.code + '.' + page.branch.code) }}
                    </p>
                  </div>
                </router-link>
              </stagger-transition>
            </div>
          </tab>
          <tab :title="$t('client.dashboard.popular')" @navigated="tabNavigation">
            <div class="inbox-widget">
              <stagger-transition>
                <router-link :to="'page/' + page.id" v-for="(page, index) in popular" :key="page.id" :data-index="index">
                  <div class="inbox-item">
                    <div class="inbox-item-img" v-if="page.bannerUrl != null">
                      <img :src="page.bannerUrl" class="img-circle">
                    </div>
                    <p class="inbox-item-author">
                      {{ page.name }}
                      <span class="pull-right badge badge-primary">{{ page.followersCount }} followers</span>
                    </p>
                    <p class="inbox-item-text" v-if="page.category != null">
                      {{ $t('category.' + page.category.code + '.' + page.branch.code) }}
                    </p>
                  </div>
                </router-link>
              </stagger-transition>
            </div>
          </tab>
        </tab-container>
      </div>
    </div>
  </div>
</template>

<script>
import Auth from '../../services/auth.js'
import Paginator from '../../elements/Paginator.vue'
import StaggerTransition from '../../functional/StaggerTransition.vue'
import TabContainer from '../../elements/TabContainer.vue'
import Tab from '../../elements/Tab.vue'

export default {
  name: 'dashboard',
  data() {
    return {
      events: [],
      attending: [],
      pagesPaginator: {},
      eventsPaginator: {},
      followed: [],
      popular: [],
    }
  },
  created() {
    if (Auth.user.authenticated) {
      this.getAttending();
    }

    this.getEvents(0);
    this.getPages(0);
  },
  components: {
    Paginator, StaggerTransition, TabContainer, Tab
  },
  computed: {
    authenticated() {
      return Auth.user.authenticated;
    }
  },
  methods: {
    getEvents(page) {
      console.log(navigator.geolocation.getCurrentPosition(position => {
        var url = ['public/near-events', page, 5].join('/');
        this.$http.get(url, {
          params: {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
            distance: 20,
          }
        }).then(response => {
          this.eventsPaginator = response.body;
        })
      }))
    },
    getAttending() {
      this.$http.get('api/user/event/attending').then(response => {
        this.attending = response.body;
      })
    },
    getPages(page) {
      var url = ['public/pages', page, 5].join('/');
      this.$http.get(url).then(response => {
        this.pagesPaginator = response.body;
      })
    },
    pagesPaginatorNavigate(e) {
      if (e.direction != null) {
        this.getPages(this.pagesPaginator.number + e.direction);
      } else if (e.page != null) {
        this.getPages(e.page);
      }
    },
    eventsPaginatorNavigate(e) {
      if (e.direction != null) {
        this.getEvents(this.eventsPaginator.number + e.direction);
      } else if (e.page != null) {
        this.getEvents(e.page);
      }
    },
    tabNavigation(id, loading) {
      if (id === 2 && (this.popular == null || this.popular.length == 0)) {
        loading(true);
        this.$http.get('public/popular-pages/0/5').then(response => {
          this.popular = response.body.content;
          loading(false);
        })
      }

      if (id == 1 && Auth.user.authenticated && (this.followed == null || this.followed.length == 0)) {
        loading(true);
        this.$http.get('api/user/followed-pages').then(response => {
          this.followed = response.body;
          loading(false);
        })
      }
    }
  }
}
</script>
<template>
  <div class="container">
    <div class="row">
      <div class="col-md-4" v-if="authenticated">
  
        <panel type="primary">
          <span slot="title">Where am i going</span>
  
          <div class="inbox-widget mx-box">
            <stagger-transition>
              <router-link :to="'event/' + event.id" v-for="(event, index) in attending" :key="event.id" :data-index="index">
                <div class="inbox-item">
                  <div class="inbox-item-img" v-if="event.bannerUrl != null">
                    <img :src="event.bannerUrl" class="img-circle" alt="">
                  </div>
                  <p class="inbox-item-author" v-text="event.name"></p>
                  <p class="inbox-item-text" v-if="event.summary != null" v-strip="event.summary.substr(0, 200)"></p>
                  <p class="inbox-item-date">{{ event.date | moment('DD.MM.YYYY') }}</p>
                </div>
              </router-link>
            </stagger-transition>
          </div>
        </panel>
  
      </div>
  
      <div :class="{ 'col-md-4' : authenticated, 'col-md-8' : !authenticated }">
        <panel type="primary">
          <span slot="title">Events near you</span>
  
          <div class="inbox-widget">
            <stagger-transition>
              <router-link :to="'event/' + event.id" v-for="(event, index) in eventsPaginator.content" :key="event.id" :data-index="index">
                <div class="inbox-item">
                  <div class="inbox-item-img" v-if="event.bannerUrl != null">
                    <img :src="event.bannerUrl" class="img-circle" alt="">
                  </div>
                  <p class="inbox-item-author" v-text="event.name"></p>
                  <p class="inbox-item-text" v-if="event.summary != null" v-strip="event.summary.substr(0, 200)"></p>
                  <p class="inbox-item-date">{{ event.date | moment('DD.MM.YYYY') }}</p>
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
        <panel type="default">
          <span slot="title">Most popular events</span>
          There's nothing to display.
        </panel>
        <panel type="default">
          <span slot="title">Suggested pages</span>
  
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
        </panel>
  
      </div>
    </div>
  </div>
</template>

<script>
import Auth from '../../services/auth.js'
import Paginator from '../../elements/Paginator.vue'
import StaggerTransition from '../../functional/StaggerTransition.vue'

export default {
  name: 'dashboard',
  data() {
    return {
      events: [],
      attending: [],
      pagesPaginator: {},
      eventsPaginator: {},
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
    Paginator, StaggerTransition
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
    }
  }
}
</script>
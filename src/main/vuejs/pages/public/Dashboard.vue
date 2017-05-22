<template>
  <div class="container">
    <div class="row">
      <div class="col-md-4" v-if="authenticated">
  
        <panel type="primary">
          <span slot="title">Where am i going</span>
  
          <div class="inbox-widget mx-box">
            <router-link :to="'event/' + event.id" v-for="event in attending" :key="event.id">
              <div class="inbox-item">
                <div class="inbox-item-img" v-if="event.bannerUrl != null">
                  <img :src="event.bannerUrl" class="img-circle" alt="">
                </div>
                <p class="inbox-item-author" v-text="event.name"></p>
                <p class="inbox-item-text" v-if="event.summary != null" v-strip="event.summary.substr(0, 200)"></p>
                <p class="inbox-item-date">{{ event.date | moment('DD.MM.YYYY') }}</p>
              </div>
            </router-link>
          </div>
        </panel>
  
      </div>
  
      <div :class="{ 'col-md-4' : authenticated, 'col-md-8' : !authenticated }">
        <panel type="primary">
          <span slot="title">Latest events</span>
  
          <div class="inbox-widget mx-box">
            <router-link :to="'event/' + event.id" v-for="event in events" :key="event.id">
              <div class="inbox-item">
                <div class="inbox-item-img" v-if="event.bannerUrl != null">
                  <img :src="event.bannerUrl" class="img-circle" alt="">
                </div>
                <p class="inbox-item-author" v-text="event.name"></p>
                <p class="inbox-item-text"  v-if="event.summary != null" v-strip="event.summary.substr(0, 200)"></p>
                <p class="inbox-item-date">{{ event.date | moment('DD.MM.YYYY') }}</p>
              </div>
            </router-link>
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
            <router-link :to="'page/' + page.id" v-for="page in pages" :key="page.id">
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
          </div>
        </panel>
  
      </div>
    </div>
  </div>
</template>

<script>
import Auth from '../../services/auth.js'
export default {
  name: 'dashboard',
  data() {
    return {
      page: 0,
      events: [],
      attending: [],
      pages: []
    }
  },
  created() {
    if (Auth.user.authenticated) {
      this.getAttending();
    }

    this.getEvents();
    this.getPages();
  },
  computed: {
    authenticated() {
      return Auth.user.authenticated;
    }
  },
  methods: {
    getEvents() {
      var url = ['public/events', this.page, 10].join('/');
      this.$http.get(url).then(response => {
        this.events = response.body.content;
      })
    },
    getAttending() {
      this.$http.get('api/user/event/attending').then(response => {
        this.attending = response.body;
      })
    },
    getPages() {
      var url = ['public/pages', this.page, 10].join('/');
      this.$http.get(url).then(response => {
        this.pages = response.body.content;
      })
    }
  }
}
</script>
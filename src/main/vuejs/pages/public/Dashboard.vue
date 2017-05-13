<template>
  <div class="container">
    <div class="row">
      <div class="col-md-4">
  
        <div class="card-box">
          <h4 class="text-dark m-t-0 m-b-30 header-title">
            Where am I going
          </h4>
  
          <div class="inbox-widget mx-box">
            <router-link :to="'event/' + event.id" v-for="event in attending" :key="event.id">
              <div class="inbox-item">
                <div class="inbox-item-img">
                  <img src="assets/images/users/avatar-1.jpg" class="img-circle" alt="">
                </div>
                <p class="inbox-item-author" v-text="event.name"></p>
                <p class="inbox-item-text" v-if="event.summary != null">{{ event.summary.substr(0, 100) }}</p>
                <p class="inbox-item-date">{{ event.date | moment('DD.MM.YYYY') }}</p>
              </div>
            </router-link>
          </div>
        </div>
      </div>
  
      <div class="col-md-4">
  
        <div class="card-box">
          <h4 class="text-dark m-t-0 m-b-30 header-title">
            <b>Newest events</b>
          </h4>
  
          <div class="inbox-widget mx-box">
            <router-link :to="'event/' + event.id" v-for="event in events" :key="event.id">
              <div class="inbox-item">
                <div class="inbox-item-img">
                  <img src="assets/images/users/avatar-1.jpg" class="img-circle" alt="">
                </div>
                <p class="inbox-item-author" v-text="event.name"></p>
                <p class="inbox-item-text" v-if="event.summary != null">{{ event.summary.substr(0, 100) }}</p>
                <p class="inbox-item-date">{{ event.date | moment('DD.MM.YYYY') }}</p>
              </div>
            </router-link>
          </div>
        </div>
      </div>
  
      <div class="col-md-4">
        <div class="card-box">
          <h4 class="text-dark  header-title m-t-0 m-b-30">Most popular events</h4>
  
          <ul class="list-unstyled">
            <li>
              Event
              <hr>
            </li>
          </ul>
        </div>
  
        <div class="card-box">
          <h4 class="text-dark  header-title m-t-0 m-b-30">Suggested pages</h4>
  
          <div class="inbox-widget">
            <router-link :to="'page/' + page.id" v-for="page in pages" :key="page.id">
              <div class="inbox-item">
                <div class="inbox-item-img">
                  <img src="assets/images/users/avatar-1.jpg" class="img-circle" alt="">
                </div>
                <p class="inbox-item-author" v-text="page.name"></p>
                <p class="inbox-item-text" v-if="page.category != null" v-text="page.category.name"></p>
              </div>
            </router-link>
          </div>
        </div>
  
        <div class="card-box">
          <h4 class="text-dark  header-title m-t-0 m-b-30">Opened conferences</h4>
  
          <ul class="list-unstyled">
            <li>
              page
              <hr>
            </li>
          </ul>
        </div>
  
      </div>
    </div>
  </div>
</template>

<script>
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
    this.getEvents();
    this.getAttending();
    this.getPages();
  },
  methods: {
    getEvents() {
      var url = ['api/events', this.page, 10].join('/');
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
      var url = ['api/pages', this.page, 10].join('/');
      this.$http.get(url).then(response => {
        this.pages = response.body.content;
      })
    }
  }
}
</script>
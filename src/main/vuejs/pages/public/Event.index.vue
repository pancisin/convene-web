<template>
  <div class="container">
    <div class="row">
      <div class="col-md-3 m-b-10">
        <router-link to="/create-event" class="btn btn-block btn-rounded btn-inverse m-b-20">Create event</router-link>
        <date-picker v-model="filters.timestamp" inline="true"></date-picker>
      </div>
      <div class="col-md-9">
        <div class="events-masonry">
          <router-link v-for="event in eventsPaginator.content" :to="'/event/' + event.id" class="event-item" :key="event.id">
            <img v-if="event.bannerUrl != null" :src="event.bannerUrl">

            <div class="content">
              <h4 v-text="event.name"></h4>
              <small>By {{ event.author.displayName }}</small>
              <br>
              <small v-if="event.place != null" v-text="event.place.address.formatted"></small>

              <span class="date">
                {{ event.date | moment('L') }}
                <span class="time" v-if="event.startsAt != null">
                  at {{ event.startsAt }}
                </span>
              </span>
              <!--<p v-strip="event.summary"></p>-->
            </div>
          </router-link>
        </div>
      </div>
    </div>

    <div class="text-center">
      <paginator :paginator="eventsPaginator" @navigate="eventsPaginatorNavigate"></paginator>
    </div>
  </div>
</template>

<script>
import Paginator from '../../elements/Paginator.vue';
import DatePicker from '../../elements/DatePicker.vue';
export default {
  name: 'events',
  data () {
    return {
      eventsPaginator: {},
      filters: {
        timestamp: Date.now()
      }
    };
  },
  components: {
    Paginator, DatePicker
  },
  watch: {
    filters: {
      handler () {
        this.getEvents(this.eventsPaginator.number);
      },
      deep: true
    }
  },
  created () {
    this.getEvents(0);
  },
  methods: {
    getEvents (page) {
      this.$http.get('public/events/' + page + '/5', {
        params: this.filters
      }).then(response => {
        this.eventsPaginator = response.body;
      });
    },
    eventsPaginatorNavigate (e) {
      if (e.direction != null) {
        this.getEvents(this.eventsPaginator.number + e.direction);
      } else if (e.page != null) {
        this.getEvents(e.page);
      }
    }
  }
};
</script>

<style lang="less">
.events-masonry {
  padding: 0;
  -moz-column-gap: 1.5em;
  -webkit-column-gap: 1.5em;
  column-gap: 1.5em;

  .event-item {
    display: inline-block;
    margin: 0 0 1.5em;
    width: 100%;
    box-sizing: border-box;
    -moz-box-sizing: border-box;
    -webkit-box-sizing: border-box;

    background: white;

    img {
      width: 100%;
    }

    .content {
      padding: 0px 10px 10px 10px;
      color: #000;

      h4 {
        text-transform: uppercase;
        font-size: 15px;
        line-height: 18px;
      }
    }
  }
}

@media only screen and (min-width: 700px) {
  .events-masonry {
    -moz-column-count: 2;
    -webkit-column-count: 2;
    column-count: 2;
  }
}

@media only screen and (min-width: 900px) {
  .events-masonry {
    -moz-column-count: 3;
    -webkit-column-count: 3;
    column-count: 3;
  }
}

@media only screen and (min-width: 1100px) {
  .events-masonry {
    -moz-column-count: 4;
    -webkit-column-count: 4;
    column-count: 4;
  }
}
</style>

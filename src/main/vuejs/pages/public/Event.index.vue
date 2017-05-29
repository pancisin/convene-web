<template>
  <div class="container">
    <div class="row">
      <div class="col-md-3">
        <router-link to="/event/create" class="btn btn-block btn-rounded btn-inverse m-b-20">Create event</router-link>
        <date-picker inline="true"></date-picker>
      </div>
      <div class="col-md-9">
        <div class="events-list">
          <router-link :to="'/event/' + event.id" class="event-item clearfix" v-for="event in eventsPaginator.content">
            <img v-if="event.bannerUrl != null" :src="event.bannerUrl" />
  
            <div class="content">
              <h4 v-text="event.name"></h4>
              <small>By {{ event.author.displayName }}</small>
  
              <span class="date">
                {{ event.date | moment($store.getters.locale.dateFormat) }}
              </span>
              <!--<p v-strip="event.summary"></p>-->
            </div>
          </router-link>
        </div>
      </div>
    </div>
  
    <div class="text-center">
      <paginator :paginator="eventsPaginator" @navigate="eventsPaginatorNavigate" />
    </div>
  </div>
</template>

<script>
import Paginator from '../../elements/Paginator.vue'
import DatePicker from '../../elements/DatePicker.vue'
export default {
  name: 'events',
  data() {
    return {
      eventsPaginator: {},
    }
  },
  components: {
    Paginator, DatePicker
  },
  created() {
    this.getEvents(0);
  },
  methods: {
    getEvents(page) {
      this.$http.get('public/events/' + page + '/5').then(response => {
        this.eventsPaginator = response.body;
      })
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

<style lang="less">
.events-list {
  display: flex;
  flex-wrap: wrap;

  .event-item {
    clear: both;
    flex: 49% 1 0;
    margin: .5%;
    background: #fff;
    border: 1px solid #ccc;
    box-shadow: 3px 3px 10px 0px rgba(111, 110, 110, 0.3);
    height: 125px;
    overflow: hidden;
    display: flex;
    transition: 0.2s cubic-bezier(0.165, 0.84, 0.44, 1);

    .content {
      display: inline-block;
      padding: 0 10px;
      width: 100%;
      position: relative;

      .date {
        position: absolute;
        bottom: 0;
        right: 0;
        padding: 10px;
      }
    }

    img {
      width: 200px;
      float: left;
      min-height: 100%;
    }

    &:hover {
      transform: scale(1.01);
    }
  }
}
</style>

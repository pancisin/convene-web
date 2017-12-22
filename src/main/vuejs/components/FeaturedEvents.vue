<template>
  <div>
    <!-- <events-list :events="paginator.content" /> -->


    <router-link 
      class="featured-event" 
      v-for="(event, index) in paginator.content" 
      :key="index"
      :to="{ name: 'event.public', params: { id: event.id } }">

      <div class="featured-event-image">
        <div class="featured-event-date">
          <span class="day">{{ event.date | moment('D') }}.</span>
          <span class="month">{{ event.date | moment('MMMM') }}</span>
          <span class="year">{{ event.date | moment('YYYY') }}</span>
        </div>

        <img :src="event.poster.path" />
      </div>

      <div class="featured-event-content">
        <h4>
          {{ event.name }}
        </h4>
      </div>
    </router-link>

    <div class="text-center">
      <paginator :paginator="paginator" :fetch="getFeaturedEvents" />
    </div>
  </div>
</template>

<script>
import PublicApi from 'api/public.api';
import moment from 'moment';
import {
  Paginator,
  EventsList,
  VueImage
} from 'elements';

export default {
  name: 'featured-events',
  data () {
    return {
      paginator: {}
    };
  },
  components: {
    Paginator,
    EventsList,
    VueImage
  },
  methods: {
    getFeaturedEvents (page) {
      PublicApi.getFeaturedEvents(page, 4, {
        fromDate: moment().valueOf(),
        toDate: moment().add(1, 'month').valueOf()
      }, paginator => {
        this.paginator = paginator;
      });
    }
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';
@event-accent: @color-purple;

.featured-event {
  margin-bottom: 20px;
  display: block;
  border-left: 5px solid @event-accent;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);

  .featured-event-image {
    height: 120px;
    position: relative;
    overflow: hidden;

    img {
      width: 100%;
    }

    .featured-event-date {
      position: absolute;
      left: 0;
      top: 0;
      bottom: 0;
      background: fadeout(@event-accent, 30%);
      color: #fff;
      padding: 10px;

      text-align: center;

      .day {
        display: block;
        font-size: 24px;
        font-weight: bold;
      }

      .month {
        text-transform: uppercase;
        display: block;
      }
    }
  }

  .featured-event-content {
    background-color: #fff;
    padding: 15px;

    h4 {
      margin: 0;
      font-size: 15px;
    }
  }

  &:hover {
    box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22); // transform: translateY(-5px);
  }
}
</style>

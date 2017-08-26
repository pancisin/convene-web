<template>
  <div>
    <ul class="list-unstyled p-0">
      <li v-for="(date, key) in sorted_events" :key="key">
        <h4>{{ format(key, 'LL') }}</h4>
  
        <ul class="events-list">
          <li v-for="event in date" :key="event.id" :class="{ 'banner' : event.bannerUrl != null }">
            <router-link :to="{ name: 'event.public', params: { id: event.id } }">
  
              <div v-if="event.bannerUrl != null">
                <img :src="event.bannerUrl">
  
                <div class="event-content">
                  <h4>
                    {{ event.name }}
                  </h4>
                  <small class="text-muted">
                    {{ event.date | moment('LL') }} - {{ event.startsAt }}
                  </small>
                  <p v-if="event.summary != null" v-strip="event.summary.substring(0, 400)">
                  </p>
                </div>
              </div>
              <div v-else>
                <h5>
                  {{ event.name }}
                </h5>
              </div>
  
            </router-link>
          </li>
        </ul>
      </li>
  
    </ul>
  </div>
</template>

<script>
import moment from 'moment';
export default {
  name: 'events-list',
  inject: ['api'],
  data () {
    return {
      events: [],
      sorted_events: []
    };
  },
  created () {
    this.api.getEvents(0, 100, events => {
      this.events = events.content;
      let data = {};
      events.content.forEach(e => {
        if (data[e.date] == null) {
          data[e.date] = [];
        }

        data[e.date].push(e);
      })

      this.sorted_events = data;
    });
  },
  methods: {
    format (timestamp, pattern) {
      let t = parseInt(timestamp);
      return moment(t).format(pattern);
    }
  }
};
</script>

<style lang="less">
ul.events-list {
  list-style-type: none;
  margin: 0;
  padding: 0;

  li {
    a {
      color: #fff;
    }

    &~li {
      margin-top: 2px;
    }

    h4 {
      margin: 0;
    }

    &.banner {
      height: 150px;
      overflow: hidden;
      position: relative;
      img {
        width: 100%;
      }

      &:hover .event-content {
        top: 0;
        opacity: 1;

        small,
        p {
          opacity: 1;
        }
      }

      .event-content {
        background: #333;
        position: absolute;
        bottom: 0;
        width: 100%;
        opacity: .8;
        padding: 10px;
        transition: all .5s ease;
        top: 110px;

        small,
        p {
          opacity: 0;
          transition: all 1s ease;
        }

        p {
          font-size: 12px;
          color: #bbb;
        }

        h4 {
          color: #fff;
          white-space: nowrap;
          text-overflow: ellipsis;
          overflow: hidden;
        }
      }
    }
  }
}
</style>

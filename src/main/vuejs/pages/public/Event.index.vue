<template>
  <div class="container">
    <div class="row">
      <div class="col-sm-6 col-md-3 m-b-10">
        <date-picker 
          v-model="filters.timestamp"
          inline
          class="m-b-20">
        </date-picker>

        <panel type="primary" v-if="authenticated">
          <span slot="title">Filters</span>
          <div class="checkbox checkbox-primary">
            <input id="checkbox-mine"
              type="checkbox"
              v-model="createdByMe">
            <label for="checkbox-mine">
              Created by me only
            </label>
          </div>
        </panel>
      </div>
      <div class="col-sm-6 col-md-9"
        v-loading="loading">
        <div class="events-masonry">
          <router-link v-for="event in eventsPaginator.content"
            :to="'/event/' + event.id"
            class="event-item"
            :key="event.id">
            <div class="image-wrapper">
              <img v-if="event.poster != null"
                :src="event.poster.path">
            </div>

            <div class="content">
              <h4 v-text="event.name"></h4>
              <small>By {{ event.author.displayName }}</small>
              <br>
              <small v-if="event.place != null"
                v-text="event.place.address.formatted"></small>

              <small class="date">
                {{ event.date | moment('L') }}
                <span class="time"
                  v-if="event.startsAt != null">
                  at {{ event.startsAt }}
                </span>
              </small>
            </div>
          </router-link>
        </div>
      </div>
    </div>

    <div class="text-center">
      <paginator :paginator="eventsPaginator"
        :fetch="getEvents"></paginator>
    </div>
  </div>
</template>

<script>
import { Paginator, DatePicker, Masonry, MasonryItem } from 'elements';
import PublicApi from 'api/public.api';
import { mapGetters } from 'vuex';

export default {
  name: 'events',
  data () {
    return {
      eventsPaginator: {},
      filters: {
        timestamp: Date.now(),
        authorType: '',
        authorId: '0'
      },
      loading: false
    };
  },
  components: {
    Paginator, DatePicker, Masonry, MasonryItem
  },
  watch: {
    filters: {
      handler () {
        this.getEvents(this.eventsPaginator.number);
        this.$router.replace({
          query: {
            ...this.filters
          }
        });
      },
      deep: true
    }
  },
  created () {
    this.filters = {
      ...this.filters,
      ...this.$route.query
    };
  },
  computed: {
    ...mapGetters(['user', 'authenticated']),
    createdByMe: {
      get () {
        return this.filters.authorType === 'USER' && parseInt(this.filters.authorId, 10) === this.user.id;
      },
      set (value) {
        this.filters = {
          ...this.filters,
          authorId: value ? this.user.id : '0',
          authorType: value ? 'USER' : ''
        };
      }
    }
  },
  methods: {
    getEvents (page) {
      this.loading = true;

      PublicApi.getEvents(page, 100, this.filters, paginator => {
        this.eventsPaginator = paginator;
        this.loading = false;
      });
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

    background: #fff;
    box-shadow: 3px 3px 10px 0px rgba(111, 110, 110, 0.3);
    transition: box-shadow .2s ease-out;

    img {
      width: 100%;
      transition: all .2s ease-out;
    }

    .image-wrapper {
      overflow: hidden;
      position: relative;
      z-index: 2;
    }

    &:hover {
      box-shadow: 0px 0px 15px 2px rgba(111, 110, 110, 0.3);
      
      img {
        transform: scale(1.1);
      }
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

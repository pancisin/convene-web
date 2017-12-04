<template>
  <div class="container" v-loading="loading">
    <div class="row" v-show="event.id">
      <div class="col-sm-6 col-md-push-3">

        <hero-unit :background="hero" solid class="event-hero-unit">
          <h1 class="text-pink">{{ event.name }}</h1>

          <p class="panel-sub-title font-13">
            {{ event.date | moment('LL') }} {{ event.startsAt }}
            <br><b>Usporiadatel:</b> {{ event.author != null ? event.author.displayName : '' }}
            <br><b>Place:</b> {{ address }}
          </p>
       
          <div class="socials">
            <a class="btn btn-link" :href="'https://www.facebook.com/' + event.facebookId" target="_blank" v-if="event.facebookId != null">
              <i class="fa fa-facebook"></i>
            </a>
            <a class="btn btn-link" :href="'https://www.facebook.com/' + event.facebookId" target="_blank">
              <i class="fa fa-twitter"></i>
            </a>
          </div>
        </hero-unit>

        <div class="panel panel-primary">
          <div class="panel-body">
            <div class="row">
              <div class="col-md-4">
                <div class="widget-simple text-center card-box">
                  <h3 class="text-primary counter font-bold">{{ event.attendeesCount }} / {{ event.place != null ? event.place.capacity : "N" }}</h3>
                  <p class="text-muted">Attendees</p>
                </div>

                <a class="btn btn-primary btn-block waves-effect" :class="{ 'btn-danger' : attending }" @click="toggleEventAttending(event)">
                  <span v-if="attending">Cancel</span>
                  <span v-else>Attend</span>
                </a>

                <div class="timeline-2 m-t-20">
                  <div class="time-item" v-for="p in event.programme" :key="p.id">
                    <div class="item-info">
                      <small class="text-muted" v-text="p.time"></small>
                      <p>
                        <strong v-text="p.description">
                        </strong>
                      </p>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-md-8 m-b-10">
                <div v-html="event.summary"></div>
              </div>
            </div>

            <div v-if="event.latitude != null && event.longitude != null" class="map-container">
              <!-- <address class="event-address">
                <strong v-text="event.place.name"></strong>
                <br> {{ event.place.address.street + " " + event.place.address.number }}
                <br> {{ event.place.address.zip + " " + event.place.address.city }}
                <br> {{ event.place.address.state }}
              </address> -->

              <google-map :location="location"></google-map>
            </div>

            <masonry columns="4">
              <masonry-item v-for="media in gallery" :key="media.id">
                <img :src="media.path" class="img-thumbnail">
                <h5>{{ media.title }}</h5>
                <p>{{ media.description }}</p>
              </masonry-item>
            </masonry>
          </div>
        </div>
      </div>

      <div class="col-sm-6 col-md-3 col-md-push-3">
        <img class="img-poster m-b-20" v-if="event.poster != null" :src="event.poster.path">

        <panel type="default" class="panel-p-0">
          <span slot="title">Live chat</span>
          <chat v-if="authenticated && event.id != null" type="event" :recipient="event" />
          <div v-else class="p-20 text-center text-muted">You must be logged in to use live chat !</div>
        </panel>
      </div>

      <div class="col-sm-12 col-md-3 col-md-pull-9">
        <panel type="default" v-if="relatedEvents.length > 0">
          <span slot="title">
            Also created by {{ event.author != null ? event.author.displayName : '' }}
          </span>
          <events-list :events="relatedEvents" />
        </panel>
      </div>
    </div>
  </div>
</template>

<script>
import {
  GoogleMap,
  Masonry,
  MasonryItem,
  EventsList,
  HeroUnit,
  Chat
} from 'elements';
import StaggerTransition from '../../functional/StaggerTransition.vue';
import EventApi from 'api/event.api';
import PublicApi from 'api/public.api';
import {
  mapGetters,
  mapActions
} from 'vuex';

export default {
  name: 'public-event',
  data () {
    return {
      event: {},
      relatedEvents: [],
      gallery: [],
      loading: false,
      address: null
    };
  },
  components: {
    GoogleMap,
    StaggerTransition,
    Masonry,
    MasonryItem,
    EventsList,
    HeroUnit,
    Chat
  },
  created () {
    this.getEvent();
  },
  computed: {
    ...mapGetters(['authenticated', 'eventAttendingStatus']),
    attending () {
      return this.eventAttendingStatus(this.event.id);
    },
    hero () {
      if (this.event != null) {
        if (this.event.banner) {
          return this.event.banner.path;
        } else if (this.event.poster != null) {
          return this.event.poster.path;
        }
      }

      return null;
    },
    location () {
      return {
        lat: parseFloat(this.event.latitude),
        lng: parseFloat(this.event.longitude)
      };
    }
  },
  watch: {
    $route: 'getEvent'
  },
  methods: {
    ...mapActions(['toggleEventAttending']),
    getEvent () {
      var event_id = this.$route.params.id;
      if (event_id != null) {
        const api = this.authenticated ? EventApi : PublicApi.event;

        this.loading = true;
        api.getEvent(event_id, event => {
          this.event = event;

          api.getRelated(this.event.id, paginator => {
            const events = this.randomize(paginator.content);
            this.relatedEvents = events.slice(0, 8);
          });

          PublicApi.event.getGallery(event_id, gallery => {
            this.gallery = gallery;
          });

          this.$googleMapApi.load(context => {
            context.geocode(this.location, result => {
              this.address = result.address;
            });
          });

          this.loading = false;
        }, error => {
          this.$error(error.error, error.message);
        });
      }
    },
    randomize (array) {
      for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
      }
      return array;
    }
  },
  head: {
    title () {
      return this.event.name;
    },
    meta () {
      return {
        description: this.event.summary,
        'og:title': this.event.name,
        'og:image': this.event.poster != null ? this.event.poster.path : '',
        'og:site_name': 'Convene',
        'og:description': this.event.summary
      };
    }
  }
};
</script>

<style lang="less">
.event-address {
  position: absolute;
  padding: 20px;
  z-index: 1;
  background-color: #fff;
  height: 100%;
}

.map-container {
  border: 1px solid #ccc;
  position: relative;
  margin-bottom: 20px;
}

.img-poster {
  width: 100%;
  border: 1px solid #ccc;
}

.event-hero-unit {
  p {
    color: #fff;
    display: inline-block;
  }

  h1 {
    font-weight: normal;
    font-size: 32px;
    line-height: 32px;
  }

  .socials {
    position: absolute;
    bottom: 0;
    right: 0;

    a {
      color: #fff;
      font-size: 21px;
      transition: all .3s ease;

      &:hover {
        transform: scale(1.2);
      }
    }
  }
}

.panel-p-0 {
  .panel-body {
    padding: 0;
  }
}
</style>

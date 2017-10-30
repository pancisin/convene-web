<template>
  <div class="container" v-if="event != null">
    <div class="row">
      <div class="col-sm-6 col-sm-push-3" :class="{ 'col-sm-9' : event.poster }">

        <hero-unit :background="event.banner != null ? event.banner.path : event.poster != null ? event.poster.path : null" solid class="event-hero-unit">
          <h1 class="text-pink">{{ event.name }}</h1>

          <p class="panel-sub-title font-13">{{ event.date | moment('LL') }} {{ event.startsAt }}
                <br>Usporiadatel : {{ event.author.displayName }}</p>
       
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

            <div v-if="event.place != null" class="map-container">
              <address class="event-address">
                <strong v-text="event.place.name"></strong>
                <br> {{ event.place.address.street + " " + event.place.address.number }}
                <br> {{ event.place.address.zip + " " + event.place.address.city }}
                <br> {{ event.place.address.state }}
              </address>

              <g-map :lat="event.place.address.latitude" :lng="event.place.address.longitude"></g-map>
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
      <div class="col-sm-3 col-sm-push-3">
        <img class="img-poster m-b-20" v-if="event.poster != null" :src="event.poster.path">
      </div>
      <div class="col-sm-3 col-sm-pull-9">
        <panel type="default">
          <span slot="title">Also created by {{ event.author.displayName }}</span>
          <events-list :events="relatedEvents" />
        </panel>
      </div>
    </div>
  </div>
  <div v-else>

  </div>
</template>

<script>
import { GMap, Masonry, MasonryItem, EventsList, HeroUnit } from 'elements';
import StaggerTransition from '../../functional/StaggerTransition.vue';
import EventApi from 'api/event.api';
import PublicApi from 'api/public.api';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'public-event',
  data () {
    return {
      event: null,
      relatedEvents: [],
      gallery: []
    };
  },
  components: {
    GMap,
    StaggerTransition,
    Masonry,
    MasonryItem,
    EventsList,
    HeroUnit
  },
  created () {
    this.getEvent();
  },
  computed: {
    ...mapGetters(['authenticated', 'eventAttendingStatus']),
    attending () {
      return this.eventAttendingStatus(this.event.id);
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
        const getAdditionalData = () => {
          PublicApi.event.getGallery(event_id, gallery => {
            this.gallery = gallery;
          });
        };

        if (this.authenticated) {
          EventApi.getEvent(
            event_id,
            event => {
              this.event = event;

              EventApi.getRelated(this.event.id, paginator => {
                this.relatedEvents = paginator.content;
              });

              EventApi.getAttendanceStatus(event_id, status => {
                this.attending = status;
              });

              getAdditionalData();
            },
            error => {
              this.$error(error.error, error.message);
            }
          );
        } else {
          PublicApi.getEvent(event_id, event => {
            this.event = event;

            PublicApi.event.getRelated(this.event.id, paginator => {
              this.relatedEvents = paginator.content;
            });

            getAdditionalData();
          });
        }
      }
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
    }
  }
}
</style>

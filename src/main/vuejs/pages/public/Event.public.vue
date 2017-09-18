<template>
  <div class="container" v-if="event != null">
    <div class="row">
      <div class="col-sm-9" :class="{ 'col-sm-9' : event.poster }">
        <div class="panel panel-primary panel-blur">
          <div class="panel-heading">
            <img v-if="event.poster != null" :src="event.poster.path">
            <h3 class="panel-title">{{ event.name }}</h3>
            <p class="panel-sub-title font-13 text-muted">{{ event.date | moment('LL') }} {{ event.startsAt }}
              <br>Usporiadatel : {{ event.author.displayName }}</p>
          </div>
          <div class="panel-body">
            <div class="row">
              <div class="col-md-8 m-b-10">
                <div v-if="event.place != null" class="map-container">
                  <address class="event-address">
                    <strong v-text="event.place.name"></strong>
                    <br> {{ event.place.address.street + " " + event.place.address.number }}
                    <br> {{ event.place.address.zip + " " + event.place.address.city }}
                    <br> {{ event.place.address.state }}
                  </address>

                  <g-map :lat="event.place.address.latitude" :lng="event.place.address.longitude"></g-map>
                </div>

                <div v-html="event.summary"></div>
              </div>
              <div class="col-md-4">
                <div class="widget-simple text-center card-box">
                  <h3 class="text-primary counter font-bold">{{ event.attendeesCount }} / {{ event.place != null ? event.place.capacity : "N" }}</h3>
                  <p class="text-muted">Attendees</p>
                </div>

                <a class="btn btn-primary btn-block waves-effect" :class="{ 'btn-danger' : attending }" @click="attend">
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
      <div class="col-sm-3">
        <img class="img-poster m-b-20" v-if="event.poster != null" :src="event.poster.path">

        <panel type="default">
          <span slot="title">Also created by {{ event.author.displayName }}</span>

          <div class="inbox-widget">
            <stagger-transition>
              <router-link :to="'/event/' + related.id" v-for="(related, index) in relatedEvents" :key="related.id" :data-index="index" v-if="related.id != event.id" class="inbox-item">
                <div class="inbox-item-img" v-if="related.poster != null">
                  <img :src="related.poster.path" class="img-circle" alt="">
                </div>
                <p class="inbox-item-author" v-text="related.name"></p>
                <p class="inbox-item-text pull-right">
                  {{ related.date | moment('L') }} {{ related.startsAt }}
                </p>
              </router-link>
            </stagger-transition>
          </div>
        </panel>
      </div>
    </div>
  </div>
  <div v-else>

  </div>
</template>

<script>
import { GMap, Masonry, MasonryItem } from 'elements';
import StaggerTransition from '../../functional/StaggerTransition.vue';
import EventApi from 'api/event.api';
import PublicApi from 'api/public.api';
import { mapGetters } from 'vuex';

export default {
  name: 'public-event',
  data () {
    return {
      event: null,
      attending: false,
      relatedEvents: [],
      gallery: []
    };
  },
  components: {
    GMap, StaggerTransition, Masonry, MasonryItem
  },
  created () {
    this.getEvent();
  },
  computed: {
    ...mapGetters(['authenticated'])
  },
  watch: {
    '$route': 'getEvent'
  },
  methods: {
    getEvent () {
      var event_id = this.$route.params.id;
      if (event_id != null) {

        const getAdditionalData = () => {
          PublicApi.event.getGallery(event_id, gallery => {
            this.gallery = gallery;
          });
        };

        if (this.authenticated) {
          EventApi.getEvent(event_id, event => {
            this.event = event;

            EventApi.getRelated(this.event.id, paginator => {
              this.relatedEvents = paginator.content;
            });

            EventApi.getAttendanceStatus(event_id, status => {
              this.attending = status;
            });

            getAdditionalData();
          }, error => {
            this.$error(error.error, error.message);
          });
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
    },
    attend () {
      if (this.authenticated) {
        EventApi.toggleAttendanceStatus(this.event.id, status => {
          this.attending = status;
          this.event.attendeesCount += status ? 1 : -1;
        });
      } else {
        this.$router.push({ name: 'login' });
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
</style>

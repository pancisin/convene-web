 <template>
  <div class="container" v-loading="loading">
    <div class="alert alert-danger" v-if="event.id && event.state != 'PUBLISHED'">
      This event is not published yet so it's not visible for other users.
    </div>

    <div class="row" v-show="event.id">
      <div class="col-sm-6 col-md-push-3">

        <hero-unit :background="hero" solid class="event-hero-unit">
          <h1 class="text-pink">{{ event.name }}</h1>

          <p class="panel-sub-title font-13">
            {{ event.date | luxon('DDDD, T') }} {{ event.startsAt }}
            
            <span v-if="event.author">
              <br>
              <b>{{ $t('event.convener') }}: </b> 
              <router-link 
                v-if="event.author.type == 'page'"
                :to="{ name: 'page.public', params: { id: event.author.slug || event.author.id }}"
                class="text-primary">
                {{ event.author.displayName }}
              </router-link>
              <router-link v-else-if="event.author.type == 'conference'"
                :to="{ name: 'conference', params: { id: event.author.id }}"
                class="text-pink">
                {{ event.author.displayName }}
              </router-link>
              <router-link v-else :to="{ name: 'user', params: { user_id: event.author.id} }" class="text-pink">
                {{ event.author.displayName }}
              </router-link>
            </span>

            <span v-if="address">
              <br><b>{{ $t('event.place') }}: </b> {{ address }}
            </span>
          </p>
       
          <!-- <div class="socials">
            <a class="btn btn-link" :href="'https://www.facebook.com/' + event.facebookId" target="_blank" v-if="event.facebookId != null">
              <i class="fa fa-facebook"></i>
            </a>
          </div> -->
        </hero-unit>

        <div class="panel panel-primary">
          <div class="panel-body">
            <div class="row">
              <div class="col-md-4">
                <!-- <div class="widget-simple text-center card-box">
                  <h3 class="text-primary counter font-bold">{{ event.attendeesCount }} / {{ event.place != null ? event.place.capacity : "N" }}</h3>
                  <p class="text-muted">{{ $t('event.attendees') }}</p>
                </div> -->

                <a class="btn btn-primary btn-block waves-effect" 
                  :class="{ 'btn-danger' : attending }" 
                  v-show="!attending"
                  @click="toggleAttending"
                  v-if="event.state == 'PUBLISHED'">

                  <span v-if="attending">{{ $t('event.cancel_attending') }}</span>
                  <span v-else>{{ $t('event.attend') }}</span>
                </a>

                <div class="text-center">
                  <h3 class="text-primary">{{ members.length }} <small class="text-muted">{{ $t('event.attendees') }}</small></h3>
                </div>
                
                <member-list :users="members" v-loading="loadingMembers" />

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
              <google-map :location="location"></google-map>
            </div>

            <div class="event-gallery">
              <light-box :image="media.path" v-for="media in gallery" :key="media.id">
                <img :src="media.path" class="img-thumbnail">
              </light-box>
            </div>
          </div>
        </div>
      </div>

      <div class="col-sm-6 col-md-3 col-md-push-3">
        <light-box v-if="event.poster != null" :image="event.poster.path">
          <vue-image :src="event.poster.path" class="img-poster m-b-20" />
        </light-box>

        <panel type="default" class="panel-p-0">
          <span slot="title">{{ $t('event.live_chat.header') }}</span>
          <chat v-if="authenticated && event.id != null" type="event" :recipient="event" />
          <div v-else class="p-20 text-center text-muted">
            {{ $t('event.live_chat.authentication') }}
            <a class="btn btn-link" @click="$tryAuthenticate">{{ $t('authenticate.login') }}</a>
          </div>
        </panel>
      </div>

      <div class="col-sm-12 col-md-3 col-md-pull-9">
        <share-panel 
          class="m-b-20" 
          :title="event.name" 
          :description="event.summary"
          :media="event.poster != null ? event.poster.path : ''" />
        <panel type="default">
          <span slot="title">
            {{ $t('event.related_events') }} {{ event.author != null ? event.author.displayName : '' }}
          </span>
          <events-list :events="relatedEvents" />
        </panel>
      </div>
    </div>

    <error v-if="error" :status="error.status" />
  </div>
</template>

<script>
import {
  GoogleMap,
  Masonry,
  MasonryItem,
  EventsList,
  HeroUnit,
  Chat,
  SharePanel,
  LightBox,
  VueImage,
  Error,
  MemberList
} from 'elements';
import EventApi from 'api/event.api';
import PublicApi from 'api/public.api';
import {
  mapGetters,
  mapActions
} from 'vuex';
import InjectorGenerator from '../../services/InjectorGenerator';

export default {
  name: 'public-event',
  data () {
    return {
      event: {},
      relatedEvents: [],
      gallery: [],
      loading: false,
      address: null,
      error: null,
      members: [],
      loadingMembers: false
    };
  },
  components: {
    GoogleMap,
    Masonry,
    MasonryItem,
    EventsList,
    HeroUnit,
    Chat,
    SharePanel,
    LightBox,
    VueImage,
    Error,
    MemberList
  },
  created () {
    this.getEvent();
  },
  computed: {
    ...mapGetters(['authenticated', 'eventAttendingStatus', 'user']),
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

        const api = this.authenticated
          ? InjectorGenerator.generate(EventApi, event_id)
          : InjectorGenerator.generate(PublicApi.event, event_id);

        this.loading = true;
        api.getEvent(event => {
          this.event = event;

          api.getRelated(paginator => {
            const events = this.randomize(paginator.content);
            this.relatedEvents = events.slice(0, 8);
          });

          api.getGallery(gallery => {
            this.gallery = gallery;
          });

          this.$googleMapApi.load(context => {
            context.geocode(this.location, result => {
              this.address = result.address;
            });
          });

          this.loadingMembers = true;
          api.getAttendees(members => {
            this.members = members;
            this.loadingMembers = false;
          });

          this.loading = false;
        }, error => {
          this.error = error;
          this.loading = false;
        });
      }
    },
    randomize (array) {
      for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
      }
      return array;
    },
    toggleAttending () {
      const toggle = () => {
        this.toggleEventAttending(this.event).then(attending => {
          if (attending) {
            this.members.push(this.user);
          } else {
            this.members = this.members.filter(u => u.id !== this.user.id);
          }
        });
      };

      if (this.authenticated) {
        toggle();
      } else {
        this.$tryAuthenticate(() => {
          toggle();
        });
      }
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

.event-gallery {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  align-items: stretch;

  & > * {
    flex: 0 0 25%;

    a {
      display: block;
      height: 100%;
    }

    img {
      height: 100%;
      width: 100%;
    }
  }
}
</style>

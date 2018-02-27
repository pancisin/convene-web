<template>
  <div class="container">
    <div class="row">
      <div class="col-sm-4 col-md-3 m-b-10">
        <date-picker 
          v-model="filters.timestamp"
          inline
          class="m-b-20">
        </date-picker>
        
        <a class="btn btn-primary btn-block m-b-20" @click="createEvent">Create event</a>

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
      <div class="col-sm-8 col-md-9" v-loading="loading">
        <masonry 
          class="events-masonry" 
          :columns="4">
          <stagger-transition>
            <masonry-item 
              class="card"
              :class="{ 'card-danger': event.state != 'PUBLISHED' }"
              v-for="event in eventsPaginator.content"
              :key="event.id">

              <div class="card-state" v-if="event.state != 'PUBLISHED'">
                {{ event.state }}
              </div> 

              <router-link :to="{ name: 'event.public', params: { id: event.id }}">
                <div class="ribbon" v-if="event.featured">
                  <span>{{ $t('event.featured') }}</span>
                </div>
                
                <div v-if="event.poster != null" class="image-wrapper">
                  <vue-image :src="event.poster.path" placeholder />
                </div>
    
                <div class="title">
                  <h5 v-text="event.name"></h5>
                  <small class="text-muted" v-if="event.author">
                    {{ event.author.displayName }}
                    <br>
                    {{ event.date | luxon('f') }}
                    <span
                      v-if="event.startsAt != null">
                      at {{ event.startsAt }}
                    </span>
                  </small>
                </div>
              </router-link>
              <div class="actions" v-if="isSuperAdmin || (event.privilege && event.privilege.active)">
                <a @click="editEvent(event)">
                  <i class="fa fa-pencil-square-o" aria-hidden="true"></i>
                </a>
                <a @click="toggleFeatured(event)" v-if="isSuperAdmin">
                  <i class="fa" :class="{ 'fa-star' : event.featured, 'fa-star-o' : !event.featured }" aria-hidden="true"></i>
                </a>
                <!-- <a href="javascript:;" @click="editEvent(event)"><i class="fa fa-times"></i></a> -->
              </div> 
            </masonry-item>
          </stagger-transition>
        </masonry>

        <div class="text-center" v-show="eventsPaginator.content && eventsPaginator.content.length === 0">
          <h4>
            There's nothing happening on {{ filters.timestamp | luxon('D') }}.
          </h4>
        </div>

        <div class="text-center">
          <paginator :paginator="eventsPaginator"
            :fetch="getEvents"></paginator>
        </div>  
      </div>
    </div>

    <modal :show.sync="displayEventCreateModal">
      <span slot="header">Create an event</span>
      <div slot="body">
        <event-create-wizard 
          :postFunc="UserApi.postEvent" 
          @success="updateContent" 
          :date="filters.timestamp"
          v-if="displayEventCreateModal"
        ></event-create-wizard>
      </div>
    </modal>

    <modal :show.sync="displayEventEditModal">
      <span slot="header">Edit event</span>
      <div slot="body">
        <event-editor 
          :event="editedEvent" 
          @updated="updateContent"
          compact
        ></event-editor>
      </div>
    </modal>
  </div>
</template>

<script>
import {
  Paginator,
  DatePicker,
  Masonry,
  MasonryItem,
  EventCreateWizard,
  VueImage
} from 'elements';
import PublicApi from 'api/public.api';
import RootApi from 'api/api';
import { mapGetters } from 'vuex';
import UserApi from 'api/user.api';
import EventEditor from '../event/Editor';
import EventApi from 'api/event.api';
import { DateTime } from 'luxon';
import { StaggerTransition } from '../../functional/transitions';

export default {
  name: 'events',
  data () {
    return {
      eventsPaginator: {},
      filters: {
        timestamp: DateTime.utc().startOf('day').valueOf(),
        authorType: '',
        authorId: '0'
      },
      loading: false,
      displayEventCreateModal: false,
      displayEventEditModal: false,
      editedEvent: {}
    };
  },
  components: {
    Paginator,
    DatePicker,
    Masonry,
    MasonryItem,
    EventCreateWizard,
    EventEditor,
    VueImage,
    StaggerTransition
  },
  watch: {
    filters: {
      handler () {
        this.getEvents(this.eventsPaginator.number || 0);
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

    if (this.$route.query.timestamp) {
      this.filters.timestamp = parseInt(this.$route.query.timestamp, 10);
    }
  },
  computed: {
    ...mapGetters(['user', 'authenticated', 'isSuperAdmin']),
    createdByMe: {
      get () {
        return (
          this.filters.authorType === 'USER' &&
          parseInt(this.filters.authorId, 10) === this.user.id
        );
      },
      set (value) {
        this.filters = {
          ...this.filters,
          authorId: value ? this.user.id : '0',
          authorType: value ? 'USER' : ''
        };
      }
    },
    UserApi () {
      return UserApi;
    }
  },
  methods: {
    getEvents (page) {
      this.loading = true;

      const api = this.authenticated ? RootApi : PublicApi;
      api.getEvents(page, 100, this.filters, paginator => {
        this.eventsPaginator = paginator;
        this.sortEvents();
        this.loading = false;
      });
    },
    editEvent (event) {
      this.editedEvent = event;
      this.displayEventEditModal = true;
    },
    updateContent (event) {
      const index = this.eventsPaginator.content.findIndex(
        e => e.id === event.id
      );

      const remove = DateTime.fromMillis(event.date, {
        zone: 'utc'
      }).startOf('day').valueOf() !== this.filters.timestamp;

      if (index === -1) {
        if (!remove) {
          this.eventsPaginator.content.push(event);
        }
      } else {
        this.eventsPaginator.content.splice(
          index,
          1,
          remove ? null : { ...event }
        );
        this.eventsPaginator.content = this.eventsPaginator.content.filter(
          x => x
        );
      }

      this.sortEvents();

      this.displayEventCreateModal = false;
      this.displayEventEditModal = false;
    },
    toggleFeatured (event) {
      EventApi.toggleFeatured(event.id, event => {
        const index = this.eventsPaginator.content.findIndex(e => e.id === event.id);
        this.eventsPaginator.content.splice(index, 1, event);
        this.sortEvents();
      });
    },
    sortEvents () {
      this.eventsPaginator.content.sort((a, b) => {
        return b.featured || (a.date - b.date);
      });
    },
    createEvent () {
      this.$tryAuthenticate(() => {
        this.displayEventCreateModal = true;
      });
    }
  }
};
</script>

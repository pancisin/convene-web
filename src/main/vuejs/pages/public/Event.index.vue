<template>
  <div class="container">
    <div class="row">
      <div class="col-sm-6 col-md-3 m-b-10">
        <date-picker 
          v-model="filters.timestamp"
          inline
          class="m-b-20">
        </date-picker>
        
        <a class="btn btn-primary btn-block m-b-20" v-if="authenticated" @click="displayEventCreateModal = true">Create event</a>

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
      <div class="col-sm-6 col-md-9" v-loading="loading">
        <masonry 
          class="events-masonry" 
          :columns="4">
          <masonry-item 
            class="card"
            :class="{ 'card-danger': event.state != 'PUBLISHED' }"
            v-for="event in eventsPaginator.content"
            :key="event.id">

            <router-link :to="{ name: 'event.public', params: { id: event.id }}">
              <div v-if="event.poster != null" class="image-wrapper">
                <vue-image :src="event.poster.path" />
              </div>
  
              <div class="title">
                <h5 v-text="event.name"></h5>
                <small class="text-muted" v-if="event.author">
                  By {{ event.author.displayName }}
                  <br>
                  {{ event.date | moment('L') }}
                  <span
                    v-if="event.startsAt != null">
                    at {{ event.startsAt }}
                  </span>
                </small>
              </div>
            </router-link>
            <div class="actions" v-if="event.privilege && event.privilege.active">
              <a @click="editEvent(event)">
                <i class="fa fa-pencil-square-o" aria-hidden="true"></i>
              </a>
              <!-- <a href="javascript:;" @click="editEvent(event)"><i class="fa fa-times"></i></a> -->
            </div>

            <div class="text-center text-danger m-t-5" v-if="event.state != 'PUBLISHED'">
              {{ event.state }}
            </div>  
          </masonry-item>
        </masonry>

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
import moment from 'moment';
import UserApi from 'api/user.api';
import EventEditor from '../event/Editor';

export default {
  name: 'events',
  data () {
    return {
      eventsPaginator: {},
      filters: {
        timestamp: moment()
          .startOf('day')
          .valueOf(),
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
    VueImage
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
  },
  computed: {
    ...mapGetters(['user', 'authenticated']),
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
      const remove =
        moment(event.date)
          .startOf('day')
          .valueOf() !== this.filters.timestamp;

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

      this.displayEventCreateModal = false;
      this.displayEventEditModal = false;
    }
  }
};
</script>

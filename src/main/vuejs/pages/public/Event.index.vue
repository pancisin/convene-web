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
      <div class="col-sm-6 col-md-9">
        <div class="events-masonry" v-loading="loading">
          <div v-for="event in eventsPaginator.content"
            class="event-item"
            :key="event.id">
            <router-link :to="'/event/' + event.id">
              <div class="image-wrapper">
                <img v-if="event.poster != null"
                  :src="event.poster.path">
              </div>

              <div class="content">
                <h4 v-text="event.name"></h4>
                <small class="text-muted" v-if="event.author">By {{ event.author.displayName }}</small>
                <br>
                <small v-if="event.place != null"
                  v-text="event.place.address.formatted"></small>

                <small class="text-muted">
                  {{ event.date | moment('L') }}
                  <span
                    v-if="event.startsAt != null">
                    at {{ event.startsAt }}
                  </span>
                </small>
              </div>
            </router-link>
            <div class="actions" v-if="event.author && user && event.author.id === user.id">
              <a @click="editEvent(event)">
                <i class="fa fa-pencil-square-o" aria-hidden="true"></i>
              </a>
              <!-- <a href="javascript:;" @click="editEvent(event)"><i class="fa fa-times"></i></a> -->
            </div>
          </div>
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
        ></event-editor>
      </div>
    </modal>
  </div>
</template>

<script>
import { Paginator, DatePicker, Masonry, MasonryItem, EventCreateWizard } from 'elements';
import PublicApi from 'api/public.api';
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
        timestamp: moment().startOf('day').valueOf(),
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
    EventEditor
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
        return this.filters.authorType === 'USER' && parseInt(this.filters.authorId, 10) === this.user.id;
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

      PublicApi.getEvents(page, 100, this.filters, paginator => {
        this.eventsPaginator = paginator;
        this.loading = false;
      });
    },
    editEvent (event) {
      this.editedEvent = event;
      this.displayEventEditModal = true;
    },
    updateContent (event) {
      const index = this.eventsPaginator.content.findIndex(e => e.id === event.id);
      const remove = moment(event.date).startOf('day').valueOf() !== this.filters.timestamp;

      if (index === -1) {
        if (!remove) {
          this.eventsPaginator.content.push(event);
        }
      } else {
        this.eventsPaginator.content.splice(index, 1, remove ? null : { ...event });
        this.eventsPaginator.content = this.eventsPaginator.content.filter(x => x);
      }

      this.displayEventCreateModal = false;
      this.displayEventEditModal = false;
    }
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';
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
    box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);
    transition: all 0.3s cubic-bezier(.25,.8,.25,1);

    img {
      width: 100%;
    }

    &:hover {
      box-shadow: 0 14px 28px rgba(0,0,0,0.25), 0 10px 10px rgba(0,0,0,0.22);
    }

    .content {
      line-height: 18px;
      padding: 0px 10px 10px 10px;

      h4 {
        color: #000;
        text-transform: uppercase;
        font-size: 15px;
        line-height: 18px;
      }
    }
    
    .actions {
      border-top: 1px solid #eee;
      text-align: right;
      display: flex;

      & > a {
        transition: all .3s ease-in-out;
        color: #000;
        padding: 10px;
        display: inline-block;
        flex: 1 1 auto;
        text-align: center;

        &:hover {
          background-color: @color-primary;
          color: #fff;
        }
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

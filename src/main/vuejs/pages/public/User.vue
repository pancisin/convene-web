<template>
  <div>
    <div class="container" v-if="user.id">
      <div class="row">
        <div class="col-md-offset-2 col-md-8">
          <h1 class="text-uppercase m-b-20">{{ user.displayName }}</h1>
        </div>
      </div>
      <div class="row">
        <div class="col-sm-8 col-md-5 col-md-offset-2 custom-content">
          <panel>
            <span slot="title">About me</span>

            {{ user.metadata.about }}
          </panel>
        </div>
        <div class="col-sm-4 col-md-3">
          <light-box :image="user.profilePicture.path" v-if="user.profilePicture">
            <vue-image :src="user.profilePicture.path" class="img-poster" />
          </light-box>

          <panel type="default" v-loading="loadingEvents" v-if="hasAccess('events')">
            <span slot="title">
              Events
            </span>
            <events-list :events="eventsPaginator.content" />
            <div class="text-center">
              <paginator :paginator="eventsPaginator" :fetch="getEvents" />
            </div>
          </panel>

          <panel type="default" v-if="hasAccess('attending-events')">
            <span slot="title">Attending</span>
            <events-list :events="attendingEvents" />
          </panel>
        </div>
      </div>
    </div>

    <error v-if="error" :status="error.status" />
  </div>
</template>

<script>
import {
  HeroUnit,
  EventsList,
  Paginator,
  LightBox,
  VueImage,
  Error
} from 'elements';
import PublicApi from 'api/public.api';
import InjectorGenerator from '../../services/InjectorGenerator';

export default {
  name: 'profile',
  data () {
    return {
      user: {},
      api: null,
      eventsPaginator: {},
      error: null,
      loadingEvents: false,
      attendingEvents: [],
      constraints: []
    };
  },
  components: {
    HeroUnit,
    EventsList,
    Paginator,
    LightBox,
    VueImage,
    Error
  },
  created () {
    const user_id = this.$route.params.user_id;
    this.api = InjectorGenerator.generate(PublicApi.user, user_id);

    this.api.getUser(user => {
      this.user = user;

      if (this.hasAccess('attending-events')) {
        this.api.getAttendingEvents(events => {
          this.attendingEvents = events;
        });
      }

      this.api.getPrivacyConstraints(constraints => {
        this.constraints = constraints;
      });
    }, error => {
      this.error = error;
    });
  },
  methods: {
    getEvents (page) {
      if (this.api != null && this.hasAccess('events')) {
        this.loadingEvents = true;
        this.api.getEvents(page, 8, paginator => {
          this.eventsPaginator = paginator;
          this.loadingEvents = false;
        });
      }
    },
    hasAccess (constraint) {
      const key = Object.keys(this.constraints).find(c => c === constraint);
      return !key || this.constraints[key] === 'PUBLIC';
    }
  },
  head: {
    title () {
      return this.user.displayName;
    },
    meta () {
      return {

      };
    }
  }
};
</script>

<style lang="less">
.img-poster {
  width: 100%;
  border: 1px solid #ccc;
  margin-bottom: 20px;
}
</style>

<template>
  <div class="row">
    <div class="col-sm-12">
      <div class="page-title-box">
        <h4 class="page-title" v-text="event.name"></h4>
      </div>
    </div>
  
    <div class="col-md-3">
      <div class="list-group mail-list">
        <router-link :to="{ name: 'event.overview' }" class="list-group-item waves-effect">
          <i class="fa fa-dashboard"></i>
          Edit
        </router-link>
        <router-link :to="{ name: 'event.programme' }" class="list-group-item waves-effect">
          <i class="fa fa-list" aria-hidden="true"></i>
          Programme
        </router-link>
        <router-link :to="{ name: 'event.gallery' }" class="list-group-item waves-effect">
          <i class="fa fa-picture-o" aria-hidden="true"></i>
          Gallery
        </router-link>
        <router-link :to="{ name: 'event.attendees' }" class="list-group-item waves-effect">
          <i class="fa fa-users" aria-hidden="true"></i>
          Attendees
        </router-link>
         <router-link :to="{ name: 'event.advanced' }" class="list-group-item waves-effect">
          <i class="fa fa-wrench" aria-hidden="true"></i>
          Advanced settings
        </router-link>
      </div>
      <div class="widget-simple-chart text-right card-box">
        <h3 class="text-primary counter" v-text="event.attendeesCount"></h3>
        <p class="text-muted text-nowrap">Attender</p>
      </div>
    </div>
    <div class="col-md-9">
      <transition name="fade-up" mode="out-in">
        <router-view :event="event" :edit="edit" @updated="eventUpdated"></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
import EventApi from 'api/event.api';
import InjectorGenerator from '../services/InjectorGenerator';

export default {
  name: 'event',
  data () {
    return {
      event: {},
      edit: false,
      injector: null
    };
  },
  provide () {
    const provider = {};

    Object.defineProperty(provider, 'api', {
      get: () => this.injector
    });

    return { provider };
  },
  watch: {
    '$route': 'getEvent'
  },
  created () {
    this.getEvent();
  },
  methods: {
    getEvent () {
      this.injector = InjectorGenerator.generate(EventApi, this.$route.params.id);

      this.injector.getEvent(event => {
        this.event = event;
        this.edit = event.id != null;
      });
    },
    eventUpdated (event) {
      this.event = event;
    }
  }
};
</script>
<template>
  <div class="row">
    <div class="col-sm-12">
      <div class="page-title-box">
        <h4 class="page-title" v-text="event.name"></h4>
      </div>
    </div>
  
    <div class="col-md-3">
      <div class="list-group mail-list">
        <router-link to="overview" class="list-group-item waves-effect">
          <i class="fa fa-dashboard"></i>
          Overview
        </router-link>
        <router-link to="programme" class="list-group-item waves-effect">
          <i class="fa fa-list" aria-hidden="true"></i>
          Programme
        </router-link>
        <router-link to="attendees" class="list-group-item waves-effect">
          <i class="fa fa-users" aria-hidden="true"></i>
          Attendees
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
import EventApi from '../services/api/event.api.js'
export default {
  name: 'event',
  data() {
    return {
      event: new Object(),
      edit: false,
    }
  },
  watch: {
    '$route': 'getEvent'
  },
  created() {
    this.getEvent();
  },
  methods: {
    getEvent() {
      var event_id = this.$route.params.id;

      if (this.event.id != null && this.event.id == event_id)
        return;

      if (event_id != null) {
        EventApi.getEvent(event_id, true, event => {
          this.event = event;
          this.edit = true;
        })
      } else
        this.event = new Object();
    },
    eventUpdated(event) {
      this.event = event;
    }
  }
}
</script>
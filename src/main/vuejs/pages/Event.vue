<template>
  <div class="row">
    <div class="col-xs-12">
      <h3 v-text="event.name" class="page-title"></h3>
    </div>
    <div class="col-md-3">
      <div class="list-group">
        <router-link to="overview" class="list-group-item">
          Overview
        </router-link>
        <router-link to="programme" class="list-group-item">
          Programme
        </router-link>
        <router-link to="attendees" class="list-group-item">
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
        <router-view :event="event" :edit="edit"></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
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

      this.event = new Object();
      if (event_id != null) {
        this.$http.get('api/event/' + event_id).then(response => {
          this.event = response.body;
          this.edit = true;
        })
      } else {
        this.event = new Object();
      }

    }
  }
}
</script>
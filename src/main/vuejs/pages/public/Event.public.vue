<template>
  <div class="container" v-if="event != null">
    <div class="row">
      <div class="col-md-3" v-if="event.bannerUrl">
        <img :src="event.bannerUrl" class="img-thumbnail" style="width: 100%" />
      </div>
      <div :class="{ 'col-md-9' : event.bannerUrl }">
        <div class="panel panel-primary panel-blur">
          <div class="panel-heading">
            <img :src="event.bannerUrl" />
            <h3 class="panel-title">{{ event.name }}</h3>
            <p class="panel-sub-title font-13 text-muted">{{ event.date | moment('dddd, DD. MMMM YY') }}</p>
          </div>
          <div class="panel-body">
            <hr>
            <div class="row">
              <div class="col-md-8 m-b-10">
                <div v-html="event.summary"></div>
              </div>
              <div class="col-md-4">
                <a class="btn btn-primary btn-block" :class="{ 'btn-danger' : attending }" @click="attend">
                  <span v-if="attending">Cancel</span>
                  <span v-else>Attend</span>
                </a>
  
                <div class="timeline-2 m-t-20">
                  <div class="time-item" v-for="p in event.programme">
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
  
          </div>
        </div>
      </div>
    </div>
  
  </div>
</template>

<script>
export default {
  name: 'public-event',
  data() {
    return {
      event: null,
      attending: false,
    }
  },
  created() {
    var event_id = this.$route.params.id;
    if (event_id != null) {
      this.$http.get('api/event/' + event_id).then(response => {
        this.event = response.body;
        this.checkAttendance();
      })
    }

  },
  methods: {
    attend() {
      var url = ['api/event', this.event.id, 'toggle-attend'].join('/');
      this.$http.patch(url).then(response => {
        this.attending = response.body;
      });
    },
    checkAttendance() {
      var url = ['api/event', this.event.id, 'attend-status'].join('/');

      this.$http.get(url).then(response => {
        this.attending = response.body;
      })
    }
  }
}
</script>
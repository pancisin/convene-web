<template>
  <div class="container" v-if="event != null">
    <div class="panel panel-border panel-success">
      <div class="panel-heading">
        <h3 class="panel-title">{{ event.name }}</h3>
        <p class="panel-sub-title font-13 text-muted">{{ event.date | moment('dddd, DD. MMMM YY') }}</p>
      </div>
      <div class="panel-body">
        <hr>
        <div class="row">
          <div class="col-md-9 m-b-10">
            <p v-text="event.summary"></p>
          </div>
          <div class="col-md-3 bg-muted">
            <div class="timeline-2">
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
</template>

<script>
export default {
  name: 'public-event',
  data() {
    return {
      event: null,
    }
  },
  created() {
    var event_id = this.$route.params.id;
    if (event_id != null) {
      this.$http.get('public/api/event/' + event_id).then(response => {
        this.event = response.body;
      })
    }
  },
  methods: {

  }
}
</script>
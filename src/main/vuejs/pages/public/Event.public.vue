<template>
  <div class="container" v-if="event != null">
    <div class="panel panel-border panel-success">
      <div class="panel-heading">
        <h3 class="panel-title">{{ event.name }}</h3>
        <p class="panel-sub-title font-13 text-muted">{{ event.date | moment('dddd, DD. MMMM YY') }}</p>
      </div>
      <div class="panel-body">
        <p v-text="event.summary"></p>
        <hr>
        <ul>
          <li v-for="p in event.programme">
            {{ p.time }} - {{ p.description }}
          </li>
        </ul>
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
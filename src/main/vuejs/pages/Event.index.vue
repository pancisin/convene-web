  <template>
  <div>
    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title">My events</h3>
        <p class="panel-sub-title font-13 text-muted">Sub title goes here with small font</p>
      </div>
      <div class="panel-body">
        <table class="table table-bordered">
          <thead>
            <tr>
            <th>#ID</th>
            <th>name</th>
            <th class="text-center">visibility</th>
            <th>programme</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="event in events">
              <td v-text="event.id"></td>
              <td>
                <router-link :to="'event/' + event.id">
                  {{ event.name }}
                </router-link>
              </td>
              <td v-text="event.visibility" class="text-center"></td>
              <td>
                <ul>
                  <li v-for="p in event.programme">
                    {{ p.time }} - {{ p.description }}
                  </li>
                </ul>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'event-index',
  data() {
    return {
      events: [],
    }
  },
  created: function () {
    this.getEvents();
  },
  methods: {
    getEvents() {
      this.$http.get('api/user/event').then(response => {
        this.events = response.body;
      })
    }
  }
}
</script>
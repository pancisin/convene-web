  <template>
  <div>
    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title">My events</h3>
      </div>
      <div class="panel-body">
        <table class="table">
          <thead>
            <tr>
            <th>name</th>
            <th class="text-center">date</th>
            <!--<th>programme</th>-->
            </tr>
          </thead>
          <tbody>
            <tr v-for="event in events">
              <td>
                <router-link :to="'event/' + event.id">
                  {{ event.name }}
                </router-link>
              </td>
              <td class="text-center">{{ event.date | moment('DD.MM.YYYY') }}</td>
              <!--<td>
                <ul>
                  <li v-for="p in event.programme">
                    {{ p.time }} - {{ p.description }}
                  </li>
                </ul>
              </td>-->
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
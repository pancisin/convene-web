  <template>
  <div class="row">
    <div class="col-md-8">
      <div class="card card-mini">
        <div class="card-header">
          <div class="card-title">Work in week</div>
        </div>
        <div class="card-body no-padding table-responsive">
          <table class="table card-table">
            <thead>
              <tr>
                <th>#</th>
                <th>Date</th>
                <th>Location</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="d in duties">
                <th scope="row"
                    v-text="d.duty.id"></th>
                <td>{{ d.date | moment("dddd, DD.MM.YYYY") }}</td>
                <td v-text="d.duty.location"></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card">
        <div class="card-header">
          <div class="card-title">Card</div>
        </div>
        <div class="card-body">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</div>
      </div>
    </div>
  </div>
</template>
  
<script>
import later from 'later'
import 'moment'

export default {
  name: 'dashboard',
  data: function () {
    return {
      duties: [],
    }
  },
  created: function () {
    this.fetchDuties();
  },
  methods: {
    fetchDuties: function () {
      var url = ['api/company', this.$store.getters.company_id, 'duties', '2017-04-13'].join('/');
      this.$http.get(url).then(response => {

        for (var i = 0; i < response.body.length; i++) {
          var duty = response.body[i];
          var schedule = later.parse.cron(duty.cronRecurrence, true);
          var ocurrences = later.schedule(schedule).next(20, Date.now(), new Date('2017-04-30'));

          for (var j = 0; j < ocurrences.length; j++) {
            this.duties.push({
              duty,
              date: ocurrences[j]
            });
          }
        }

        this.duties.sort((a, b) => {
          return new Date(a.date) > new Date(b.date);
        })
      });
    },
  }
}
</script>

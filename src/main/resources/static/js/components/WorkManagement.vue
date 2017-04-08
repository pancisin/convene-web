  <template>
  <div class="row">
    <div class="col-xs-12 col-md-8">
      <div class="card card-mini">
        <div class="card-header">
          <div class="card-title">Duties</div>
          <ul class="card-action">
            <li>
              <a href="javascript:;" @click="fetchData">
                <i class="fa fa-refresh"></i>
              </a>
            </li>
            <li>
              <router-link to="/duty/create">
                <i class="fa fa-plus"></i>
              </router-link>
            </li>
          </ul>
        </div>
  
        <div class="card-body no-padding table-responsive">
          <table class="table card-table">
            <thead>
              <tr>
                <th>Location</th>
                <th>Closest occurrence</th>
                <th class="text-center">Tasks</th>
                <th>Employees</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="duty in duties">
                <td>
                  <router-link :to="'/duty/' + duty.id">
                    {{ duty.location }}
                  </router-link>
                </td>
                <td>{{ getClosestOccurrence(duty) | moment("dddd, DD.MM.YYYY") }}</td>
                <td>
                  <div class="list-group __timeline">
                    <a v-for="task in duty.tasks"
                       class="list-group-item">{{ task.section }} : {{ task.fixture }} - {{ task.action }}</a>
                  </div>
                </td>
                <td>
                  <ul class="list-unstyled">
                    <li v-for="emp in duty.employees">
                      {{ emp.firstName }} {{ emp.lastName }}
                    </li>
                  </ul>
                </td>
                <td>
                  <a :href="'/report/duty/' + duty.id"><i class="fa fa-download" aria-hidden="true"></i></a>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class="col-xs-12 col-md-4">
      <div class="card card-mini">
        <div class="card-header">
          <div class="card-title">Employees</div>
        </div>
        <div class="card-body no-padding table-responsive">
          <table class="table card-table">
            <thead>
              <tr>
                <th>Name</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="emp in employees">
                <td>{{ emp.firstName }} {{ emp.lastName }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import later from 'later';

export default {
  name: 'work-management',
  data: function () {
    return {
      employees: [],
      duties: [],
      display: {
        modal: false
      }
    }
  },
  created: function () {
    if (this.$store.getters.company_id != null)
      this.fetchData();
  },
  methods: {
    fetchData: function () {
      var emp_url = ['api/company', this.$store.getters.company_id, 'employees'].join('/');
      var dut_url = ['api/company', this.$store.getters.company_id, 'duties'].join('/');

      this.$http.get(emp_url).then(response => {
        this.employees = response.body;
      });

      this.$http.get(dut_url).then(response => {
        this.duties = response.body;
      });
    },
    getClosestOccurrence: function (duty) {
      var occurrence = later.parse.cron(duty.cronRecurrence);
      return later.schedule(occurrence).next(1);
    }
  }
}
</script>
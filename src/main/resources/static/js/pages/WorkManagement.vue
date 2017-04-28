  <template>
  <div class="row">
    <div class="col-xs-12 col-md-9">
      <div class="card card-mini">
        <div class="card-header">
          <div class="card-title">{{ $tc('duty.default', 2) }}</div>
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
                <th>{{ $tc('customer.default', 1) }}</th>
                <th>{{ $t('duty.location') }}</th>
                <th>{{ $t('duty.closest_occ') }}</th>
                <th class="text-center">{{ $t('duty.tasks') }}</th>
                <th>{{ $tc('employee.default', 2) }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="duty in duties">
                <td>
                  {{ duty.customer.name }}
                </td>
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
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class="col-xs-12 col-md-3">
      <div class="card card-mini">
        <div class="card-header">
          <div class="card-title">{{ $tc('customer.default', 2) }}</div>
        </div>
        <div class="card-body no-padding table-responsive">
          <table class="table card-table">
            <thead>
              <tr>
                <th>{{ $t('customer.name') }}</th>
                <th class="text-center">Report</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="cust in customers">
                <td v-text="cust.name"></td>
                <td class="text-center"><a :href="'report/customer/' + cust.id"><i class="fa fa-download" aria-hidden="true"></i></a></td>
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
      customers: [],
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
      var cust_url = ['api/company', this.$store.getters.company_id, 'customers'].join('/');
      var dut_url = ['api/company', this.$store.getters.company_id, 'duties'].join('/');

      this.$http.get(cust_url).then(response => {
        this.customers = response.body;
      });

      this.$http.get(dut_url).then(response => {
        this.duties = response.body;
      });
    },
    getClosestOccurrence: function (duty) {
      var schedule = {
        schedules: [
          duty.recurrence,
        ]
      }
      // var occurrence = later.parse.cron('0 ' + duty.cronRecurrence, true);
      return later.schedule(schedule).next(1);
    }
  }
}
</script>
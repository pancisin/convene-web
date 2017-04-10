  <template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card card-mini">
        <div class="card-header">
          <div class="card-title">{{ $tc('employee.default', 2) }}</div>
          <ul class="card-action">
            <li>
              <a @click="getEmployees">
                <i class="fa fa-refresh"></i>
              </a>
            </li>
            <li>
              <a @click="display.modal = true">
                <i class="fa fa-plus"></i>
              </a>
            </li>
          </ul>
        </div>
        <div class="card-body no-padding table-responsive">
          <table class="table card-table">
            <thead>
              <tr>
                <th>#</th>
                <th>{{ $t('employee.name') }}</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="emp in employees">
                <th scope="row"
                    v-text="emp.id"></th>
                <td>
                  <a class="btn-link"
                     @click="editEmployee(emp)">{{ emp.firstName }} {{ emp.lastName }} </a>
                </td>
                <td>
                  <a @click="deleteEmployee(emp.id)"><i class="fa fa-trash-o" aria-hidden="true"></i></a>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  
    <modal :show="display.modal"
           :question="true"
           @accept="submitEmployee"
           @close="display.modal = false">
      <h4 slot="header">{{ $t('employee.create') }}</h4>
      <form class="form"
            slot="body"
            @submit.prevent="submitEmployee">
        <label>{{ $t('employee.firstName') }}</label>
        <input type="text"
               v-model="employee.firstName"
               class="form-control"
               placeholder="First Name">
        <label>{{ $t('employee.lastName') }}</label>
        <input type="text"
               v-model="employee.lastName"
               class="form-control"
               placeholder="Last Name">
      </form>
    </modal>
  </div>
</template>

<script>
export default {
  name: 'employees',
  data: function () {
    return {
      employees: [],
      employee: {
        firstName: null,
        lastName: null,
      },
      display: {
        modal: false
      }
    }
  },
  created: function () {
    this.getEmployees();
  },
  methods: {
    getEmployees: function () {
      var url = ['api/company', this.$store.getters.company_id, 'employees'].join('/');
      this.$http.get(url).then(response => {
        this.employees = response.body;
      });
    },
    submitEmployee: function () {
      var push = this.employee.id == null;

      var url = ['api/company', this.$store.getters.company_id, 'employees'].join('/');
      this.$http.post(url, this.employee).then(response => {
        if (push)
          this.employees.push(response.body);

        this.display.modal = false;
      }, response => {
        response.body.fieldErrors;
      });
    },
    deleteEmployee: function (employee_id) {
      var url = ['api/employee', employee_id].join('/');
      this.$http.delete(url).then(response => {
        for (var i = 0; i < this.employees.length; i++) {
          if (this.employees[i].id == employee_id)
            this.employees.splice(i, 1);
        }
      });
    },
    editEmployee: function (employee) {
      this.employee = employee;
      this.display.modal = true;
    }
  }
}
</script>
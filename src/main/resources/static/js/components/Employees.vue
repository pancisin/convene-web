  <template>
  <div class="row">
    <div class="col-xs-12 col-md-4 col-md-offset-8">
      <div class="card card-mini">
        <div class="card-header">
        	<div class="card-title">Employees</div>
	          <ul class="card-action">
	            <li>
	              <a @click="">
	                <i class="fa fa-refresh"></i>
	              </a>
	            </li>
	          </ul>
          </div>
        <div class="card-body no-padding table-responsive">
          <table class="table card-table">
            <thead>
              <tr>
                <th>#</th>
                <th>Name</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="emp in employees">
                <th scope="row" v-text="emp.id"></th>
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
 export default {
 	name: 'employees',
  data: function() {
    return {
      employees: [],
    }
  },
  created: function() {
    this.getUsers();
  },
  methods: {
    getUsers: function() {
      this.$http.get('/api/company/1/employees').then(response => {
        this.employees = response.body;
      }, response => {
      });
    }
  }
 }
</script>
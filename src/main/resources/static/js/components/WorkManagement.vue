  <template>
  <div class="row">
  	<div class="col-xs-12 col-md-8">
			 <div class="card card-mini">
			 	<div class="card-header">
        	<div class="card-title">Duties</div>
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
                  <th>Location</th>
                  <th>Starts</th>
                  <th>Ends</th>
                  <th>Tasks</th>
                  <th>Employees</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="duty in duties">
                  <th scope="row" v-text="duty.id"></th>
                  <td>{{ duty.location }}</td>
                  <td>{{ duty.startDate | moment("DD.MM.YYYY") }}</td>
                  <td>{{ duty.endDate | moment("DD.MM.YYYY") }}</td>
                  <td>
                  	<div class="list-group __timeline">
                  		<a v-for="task in duty.tasks" class="list-group-item">
                  			{{ task.section }} : {{ task.fixture }} - {{ task.action }}
                  		</a>
                  	</div>
                  </td>
                  <td>
                  	<ul>
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
    <div class="col-xs-12 col-md-4">
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
 	name: 'work-management',
  data: function() {
    return {
      employees: [],
      duties: []
    }
  },
  created: function() {
    this.fetchData();
  },
  methods: {
    fetchData: function() {
      this.$http.get('/api/company/1/employees').then(response => {
        this.employees = response.body;
      }, response => {
        //error
      });

      this.$http.get('/api/company/1/duties').then(response => {
        this.duties = response.body;
      }, response => {
        //error
      });
    }
  }
 }
</script>
  <template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card">
        <div class="card-header">Users</div>
        <div class="card-body">
          <table class="table">
            <thead>
              <tr>
                <th>#</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
                <th>Company</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in users">
                <th scope="row" v-text="user.id"></th>
                <td v-text="user.firstName"></td>
                <td v-text="user.lastName">Otto</td>
                <td v-text="user.email"></td>
                <td v-text="user.company.name"></td>
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
 	name: 'users',
  data: function() {
    return {
      users: [],
    }
  },
  created: function() {
    this.getUsers();
  },
  methods: {
    getUsers: function() {
      this.$http.get('/api/user/').then(response => {
        this.users = response.body;
      }, response => {
        // error callback
      });
    }
  }
 }
</script>
<template>
  <div class="card card-mini">
    <div class="card-header">
      Users
    </div>
    <div class="card-body no-padding">
      <table class="table">
        <thead>
          <tr>
            <th>#</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users">
            <th scope="row"
                v-text="user.id">1</th>
            <td v-text="user.firstName"></td>
            <td v-text="user.lastName"></td>
            <td v-text="user.email"></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'settings-users',
  data() {
    return {
      users: [],
    }
  },
  created() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers: function () {
      this.$http.get('api/company/' + this.$store.getters.company_id + '/users').then(response => {
        this.users = response.body;
      });
    },
  }
}
</script>
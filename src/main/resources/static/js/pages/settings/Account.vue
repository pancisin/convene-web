<template>
  <div class="card card-mini">
    <div class="card-header">
      Personal information
    </div>
    <div class="card-body">
      <form class="form form-horizontal"
            @submit.prevent="submit">
        <div class="row">
          <div class="col-md-6">
            <input type="email"
                   v-model="user.email"
                   class="form-control"
                   placeholder="Email"
                   disabled>
          </div>
          <div class="col-md-6">
  
          </div>
        </div>
        <div class="row">
          <div class="col-md-6">
            <input type="text"
                   v-model="user.firstName"
                   class="form-control"
                   placeholder="First Name">
          </div>
          <div class="col-md-6">
            <input type="text"
                   v-model="user.lastName"
                   class="form-control"
                   placeholder="Last Name">
          </div>
        </div>
  
        <div class="form-footer">
          <div class="form-group">
            <div class="col-md-9 col-md-offset-3">
              <button type="submit"
                      class="btn btn-primary">
                Update
              </button>
              <button type="button"
                      class="btn btn-default"
                      @click="$router.go(-1)">Cancel</button>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import Auth from '../../services/auth.js'
export default {
  data: function () {
    return {
      user: null,
    }
  },
  created: function () {
    Auth.currentUser(this);
  },
  methods: {
    submit: function () {
      this.$http.put('api/user/me', this.user).then(response => {
        console.log(response.body);
      });
    }
  }
}
</script>
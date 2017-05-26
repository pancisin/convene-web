<template>
  <div class="wrapper-page">
  
    <div class="text-center">
      <router-link to="/" class="logo-lg">
        <i class="fa fa-book"></i>
        <span>Bookster</span>
      </router-link>
    </div>
  
    <form class="form-horizontal m-t-20" @submit.prevent="submit">
  
      <div class="form-group">
        <div class="col-xs-12">
          <input class="form-control" type="text" required="" placeholder="Email" v-model="user.email">
          <i class="fa fa-user form-control-feedback l-h-34"></i>
        </div>
      </div>
  
      <div class="form-group">
        <div class="col-xs-12">
          <input class="form-control" type="password" required="" placeholder="Password" v-model="user.password">
          <i class="fa fa-key form-control-feedback l-h-34"></i>
        </div>
      </div>
  
      <div class="form-group">
        <div class="col-xs-12">
          <div class="checkbox checkbox-primary">
            <input id="checkbox-signup" type="checkbox">
            <label for="checkbox-signup">
              Remember me
            </label>
          </div>
  
        </div>
      </div>
  
      <div class="form-group text-right m-t-20">
        <div class="col-xs-12">
          <button class="btn btn-primary btn-custom w-md waves-effect waves-light" type="submit">Log In</button>
        </div>
      </div>
  
      <div class="form-group m-t-30">
        <div class="col-sm-7">
          <a class="text-muted">
            <i class="fa fa-lock m-r-5"></i> Forgot your password?</a>
        </div>
        <div class="col-sm-5 text-right">
          <router-link to="/register" class="text-muted">
            Create an account
          </router-link>
        </div>
      </div>
    </form>
  </div>
</template>
<script>
import Auth from '../services/auth.js'
export default {
  data: function () {
    return {
      user: {
        email: null,
        password: null,
      },
      fieldErrors: [],
    }
  },
  methods: {
    submit: function () {
      this.fieldErrors = [];

      var redirect = this.$route.query.redirect ? this.$route.query.redirect : '/';
      Auth.login(this, this.user, redirect);
    }
  }
};
</script>

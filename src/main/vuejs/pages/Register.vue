<template>
  <div class="wrapper-page">
  
    <div class="text-center">
      <a href="index.html" class="logo-lg"><i class="md md-equalizer"></i> <span>Bookster</span> </a>
    </div>
  
    <form class="form-horizontal m-t-20" @submit.prevent="submit">
      <div class="form-group">
        <div class="col-xs-12">
          <input v-model="user.email" class="form-control" type="email" required="" placeholder="Email">
          <i class="md md-email form-control-feedback l-h-34"></i>
        </div>
      </div>
  
      <div class="form-group">
        <div class="col-xs-12">
          <input v-model="user.firstName" class="form-control" type="text" required="" placeholder="First Name">
          <i class="md md-account-circle form-control-feedback l-h-34"></i>
        </div>
      </div>
  
      <div class="form-group">
        <div class="col-xs-12">
          <input v-model="user.lastName" class="form-control" type="text" required="" placeholder="Last Name">
          <i class="md md-account-circle form-control-feedback l-h-34"></i>
        </div>
      </div>
  
      <div class="form-group">
        <div class="col-xs-12">
          <input v-model="user.password" class="form-control" type="password" required="" placeholder="Password">
          <i class="md md-vpn-key form-control-feedback l-h-34"></i>
        </div>
      </div>
  
      <div class="form-group">
        <div class="col-xs-12">
          <input v-model="user.company.name" class="form-control" type="text" required="" placeholder="Company name">
          <i class="md md-account-circle form-control-feedback l-h-34"></i>
        </div>
      </div>
  
      <div class="form-group">
        <div class="col-xs-12">
          <input v-model="user.company.ico" class="form-control" type="text" required="" placeholder="ICO">
          <i class="md md-account-circle form-control-feedback l-h-34"></i>
        </div>
      </div>
  
      <div class="form-group">
        <div class="col-xs-12">
          <div class="checkbox checkbox-primary">
            <input id="checkbox-signup" type="checkbox" checked="checked">
            <label for="checkbox-signup">
              I accept <a href="#">Terms and Conditions</a>
            </label>
          </div>
  
        </div>
      </div>
  
      <div class="form-group text-right m-t-20">
        <div class="col-xs-12">
          <button class="btn btn-primary btn-custom waves-effect waves-light w-md" type="submit">Register</button>
        </div>
      </div>
  
      <div class="form-group m-t-30">
        <div class="col-sm-12 text-center">
          <a href="pages-login.html" class="text-muted">Already have account?</a>
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
      working: false,
      user: {
        firstName: null,
        lastName: null,
        email: null,
        password: null,
        passwordConfirm: null,
        company: {
          name: null,
          ico: null,
        }
      },
      fieldErrors: [],
    }
  },
  methods: {
    submit: function () {
      this.working = true;
      this.fieldErrors = [];

      Auth.signup(this, this.user, '/').then(token => {
        this.working = false;
      }, () => {
        this.working = false;
      });
    }
  }
}
</script>

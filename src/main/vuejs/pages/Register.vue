<template>
  <div class="wrapper-page">

    <div class="text-center">
      <router-link to="/" class="logo-lg">
        <i class="fa fa-book"></i>
        <span>Bookster</span>
      </router-link>
    </div>

    <form class="form-horizontal m-t-20" @submit.prevent="submit">
      <div class="form-group" :class="{'has-error': errors.has('email') }">
        <div class="col-xs-12">
          <input v-model="user.email" class="form-control" type="text" name="email" v-validate data-vv-rules="required|email" placeholder="Email">
          <i class="fa fa-envelope form-control-feedback l-h-34"></i>
          <span class="text-danger" v-if="errors.has('email')">{{ errors.first('email') }}</span>
        </div>
      </div>

      <div class="form-group" :class="{'has-error': errors.has('firstName') }">
        <div class="col-xs-12">
          <input v-model="user.firstName" class="form-control" type="text" placeholder="First Name" v-validate data-vv-rules="required" name="firstName">
          <i class="md md-account-circle form-control-feedback l-h-34"></i>
          <span class="text-danger" v-if="errors.has('firstName')">{{ errors.first('firstName') }}</span>
        </div>
      </div>

      <div class="form-group" :class="{'has-error': errors.has('lastName') }">
        <div class="col-xs-12">
          <input v-model="user.lastName" class="form-control" type="text" placeholder="Last Name" v-validate data-vv-rules="required" name="lastName">
          <i class="md md-account-circle form-control-feedback l-h-34"></i>
          <span class="text-danger" v-if="errors.has('lastName')">{{ errors.first('lastName') }}</span>
        </div>
      </div>

      <div class="form-group" :class="{'has-error': errors.has('password') }">
        <div class="col-xs-12">
          <input v-model="user.password" class="form-control" type="password" placeholder="Password" name="password" v-validate data-vv-rules="required|min:6">
          <i class="fa fa-key form-control-feedback l-h-34"></i>
          <span class="text-danger" v-if="errors.has('password')">{{ errors.first('password') }}</span>
        </div>
      </div>

      <div class="form-group" :class="{'has-error': errors.has('confirmPassword') }">
        <div class="col-xs-12">
          <input class="form-control" type="password" placeholder="Confirm password" name="confirmPassword" v-validate data-vv-rules="required|confirmed:password">
          <span class="text-danger" v-if="errors.has('confirmPassword')">{{ errors.first('confirmPassword') }}</span>
        </div>
      </div>

      <div class="form-group" :class="{'has-error': errors.has('acceptTerms') }">
        <div class="col-xs-12">
          <div class="checkbox checkbox-primary">
            <input id="checkbox-signup" type="checkbox" checked="checked" name="acceptTerms" v-validate data-vv-rules="required">
            <label for="checkbox-signup">
              I accept
              <router-link to="terms">
                Terms and Conditions
              </router-link>
            </label>
          </div>
          <span class="text-danger" v-if="errors.has('acceptTerms')">{{ errors.first('acceptTerms') }}</span>
        </div>
      </div>

      <div class="form-group text-right m-t-20">
        <div class="col-xs-12">
          <button class="btn btn-primary btn-custom waves-effect waves-light w-md" type="submit">Register</button>
        </div>
      </div>

      <div class="form-group m-t-30">
        <div class="col-sm-12 text-center">
          <router-link to="/login" class="text-muted">
            Already have account?
          </router-link>
        </div>
      </div>
    </form>

  </div>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  data: function () {
    return {
      user: {
        firstName: null,
        lastName: null,
        email: null,
        password: null
      }
    };
  },
  methods: {
    ...mapActions(['register']),
    submit: function () {
      this.working = true;

      this.$validator.validateAll().then(valid => {
        if (valid) {
          this.register(this.user).then(token => {
            this.$router.push('/');
          }, errors => {

          });
        }
      });
    }
  }
};
</script>

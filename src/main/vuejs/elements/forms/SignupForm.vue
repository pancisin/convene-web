<template>
  <form class="form-horizontal m-t-20" @submit.prevent="submit">
    <div class="form-group" :class="{ 'has-error' : errors.has('email') }">
      <div class="input-group">
        <div class="input-group-addon input-group-prepend">
          <span class="input-group-text">@</span>
        </div>
        <input v-model="user.email" class="form-control" type="text" name="email" v-validate data-vv-rules="required|email" placeholder="Email">
      </div>
      <span class="text-danger" v-if="errors.has('email')">{{ errors.first('email') }}</span>
    </div>

    <div class="form-group" :class="{'has-error': errors.has('firstName') }">
      <input v-model="user.firstName" class="form-control" type="text" placeholder="First Name" v-validate data-vv-rules="required" name="firstName">
      <i class="md md-account-circle form-control-feedback l-h-34"></i>
      <span class="text-danger" v-if="errors.has('firstName')">{{ errors.first('firstName') }}</span>
    </div>

    <div class="form-group" :class="{'has-error': errors.has('lastName') }">
      <input v-model="user.lastName" class="form-control" type="text" placeholder="Last Name" v-validate data-vv-rules="required" name="lastName">
      <i class="md md-account-circle form-control-feedback l-h-34"></i>
      <span class="text-danger" v-if="errors.has('lastName')">{{ errors.first('lastName') }}</span>
    </div>

    <div class="form-group" :class="{ 'has-error' : errors.has('password') }">
      <div class="input-group">
        <div class="input-group-addon input-group-prepend">
          <span class="input-group-text">
            <i class="fa fa-lock"></i>
          </span>
        </div>
        <input v-model="user.password" class="form-control" type="password" placeholder="Password" name="password" v-validate data-vv-rules="required|min:6">
      </div>
      <span class="text-danger" v-if="errors.has('password')">{{ errors.first('password') }}</span>
    </div>

    <div class="form-group" :class="{ 'has-error' : errors.has('confirmPassword') }">
      <div class="input-group">
        <div class="input-group-addon input-group-prepend">
          <span class="input-group-text">
            <i class="fa fa-lock"></i>
          </span>
        </div>
        <input class="form-control" type="password" placeholder="Confirm password" name="confirmPassword" v-validate data-vv-rules="required|confirmed:password">
      </div>
      <span class="text-danger" v-if="errors.has('confirmPassword')">{{ errors.first('confirmPassword') }}</span>
    </div>

    <div class="form-group" :class="{'has-error': errors.has('acceptTerms') }">
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

    <div class="form-group text-right m-t-20">
      <button class="btn btn-primary btn-custom waves-effect waves-light w-md" type="submit">Register</button>
    </div>

    <div class="form-group m-t-30">
      <div class="col-sm-12 text-center">
        <router-link to="/login" class="text-muted">
          Already have account?
        </router-link>
      </div>
    </div>
  </form>
</template>

<script>
import { mapActions } from 'vuex';
export default {
  name: 'signup-form',
  data () {
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

<style>

</style>

<template>
  <div>
    <form @submit.prevent="submit">
      <div class="form-group" :class="{ 'has-error' : fieldErrors.email }">
        <div class="input-group">
          <div class="input-group-addon input-group-prepend">
            <span class="input-group-text">
              <i class="fa fa-user"></i>
            </span>
          </div>
          <input type="text" class="form-control" placeholder="Email" v-model="user.email" required>
        </div>

        <span class="text-danger" v-if="fieldErrors.name">{{ $t(fieldErrors.name.code) }}</span>
      </div>

      <div class="form-group" :class="{ 'has-error' : fieldErrors.password }">
        <div class="input-group">
          <div class="input-group-addon input-group-prepend">
            <span class="input-group-text">
              <i class="fa fa-lock"></i>
            </span>
          </div>
          <input class="form-control" type="password" required="" placeholder="Password" v-model="user.password">
        </div>

        <span class="text-danger" v-if="fieldErrors.password">{{ $t(fieldErrors.password.code) }}</span>
      </div>

      <div class="form-group">
        <div class="checkbox checkbox-primary">
          <input id="checkbox-signup" type="checkbox" v-model="remember">
          <label for="checkbox-signup">
            {{ $t('authenticate.remember_me') }}
          </label>
        </div>

        <transition name="fade">
          <div class="alert alert-warning" v-show="remember">
            Be careful. Your account will be accessible even when you close your browser.
            Please use this option on your private computer only !
          </div>
        </transition>
      </div>

      <div class="form-group text-right m-t-20">
        <button class="btn btn-primary w-md waves-effect waves-light" type="submit">Log In</button>
      </div>

      <div class="form-group m-t-30">
        <div class="row">
          <div class="col-sm-7">
            <a class="text-muted">
              <i class="fa fa-lock m-r-5"></i> {{ $t('authenticate.forgot_password') }}</a>
          </div>
          <div class="col-sm-5 text-right">
            <router-link to="/register" class="text-muted">
              {{ $t('authenticate.create_account') }}
            </router-link>
          </div>
        </div>
      </div>
    </form>

    <div class="text-center text-muted m-b-20">
      <hr />
      {{ $t('authenticate.or') }}
    </div>

    <a @click="facebookLogin" class="btn btn-block btn-facebook">
      <i class="fa fa-facebook"></i>
      {{ $t('authenticate.facebook_login') }}
    </a>
  </div>
</template>

<script>
import { mapActions } from 'vuex';
export default {
  props: {
    success: Function
  },
  data () {
    return {
      user: {
        email: null,
        password: null
      },
      fieldErrors: {},
      remember: false
    };
  },
  methods: {
    ...mapActions(['login', 'loginFacebook' ]),
    submit () {
      this.fieldErrors = {};

      const data = {
        credentials: this.user,
        remember: this.remember
      };

      this.login(data).then(result => {
        if (this.success) {
          this.success();
        }
      }, fieldErrors => {
        fieldErrors.forEach((e) => {
          this.$set(this.fieldErrors, e.field, e);
        });
      });
    },
    facebookLogin () {
      var redirect = this.$route.query.redirect ? this.$route.query.redirect : '/';

      this.$facebookApi.load(context => {
        context.login(response => {
          this.loginFacebook({
            userId: response.authResponse.userID,
            accessToken: response.authResponse.accessToken
          }).then(user => {
            this.$router.push({ path: redirect });
          });
        });
      });
    }
  }
};
</script>

<style lang="less">
.btn-facebook {
  background-color: 	#3b5998;
  color: #fff;
  transition: background-color .2s ease;

  i {
    float: left;
    margin: 3px;
  }

  &:hover {
    background-color: darken(	#3b5998, 5%);
    color: #fff;
  }
}
</style>
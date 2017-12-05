<template>
  <div class="wrapper-page container">
    <product-logo large />

    <form class="form-horizontal m-t-20" @submit.prevent="submit">

      <div class="form-group" :class="{ 'has-error' : fieldErrors.email }">
        <div class="col-xs-12">
          <input class="form-control" type="text" required="" placeholder="Email" v-model="user.email">
          <i class="fa fa-user form-control-feedback l-h-34"></i>
          <span class="text-danger" v-if="fieldErrors.name">{{ $t(fieldErrors.name.code) }}</span>
        </div>
      </div>

      <div class="form-group" :class="{ 'has-error' : fieldErrors.password }">
        <div class="col-xs-12">
          <input class="form-control" type="password" required="" placeholder="Password" v-model="user.password">
          <i class="fa fa-key form-control-feedback l-h-34"></i>
          <span class="text-danger" v-if="fieldErrors.password">{{ $t(fieldErrors.password.code) }}</span>
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

    <div class="text-center text-muted m-b-20">
      <hr />
      OR
    </div>

    <a @click="facebookLogin" class="btn btn-block btn-facebook">
      <i class="fa fa-facebook"></i>
      Login with facebook
    </a>

  </div>
</template>
<script>
import { mapActions } from 'vuex';
import { ProductLogo } from 'elements';

export default {
  data: function () {
    return {
      user: {
        email: null,
        password: null
      },
      fieldErrors: {}
    };
  },
  components: {
    ProductLogo
  },
  methods: {
    ...mapActions(['login', 'loginFacebook' ]),
    submit: function () {
      this.fieldErrors = {};

      var redirect = this.$route.query.redirect ? this.$route.query.redirect : '/';
      this.login(this.user).then(result => {
        this.$router.push({ path: redirect });
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
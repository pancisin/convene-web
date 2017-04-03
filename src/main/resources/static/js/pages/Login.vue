<template>
  <div class="app-container app-login"
       :class="{'__loading' : working}">
    <div class="flex-center">
      <div class="app-header"></div>
  
      <div class="app-body">
        <div class="loader-container text-center">
          <div class="icon">
            <div class="sk-folding-cube">
              <div class="sk-cube1 sk-cube"></div>
              <div class="sk-cube2 sk-cube"></div>
              <div class="sk-cube4 sk-cube"></div>
              <div class="sk-cube3 sk-cube"></div>
            </div>
          </div>
          <div class="title">Logging in...</div>
        </div>
  
        <div class="app-block">
          <div class="app-form">
            <div class="form-header">
              <div class="app-brand">
                <span class="highlight">Employger</span> Admin
              </div>
            </div>
            <form @submit.prevent="submit">
              <div class="input-group">
                <span class="input-group-addon"
                      id="basic-addon1"><i class="fa fa-user" aria-hidden="true"></i></span>
                <input type="email"
                       name="email"
                       class="form-control"
                       placeholder="Username"
                       aria-describedby="basic-addon1"
                       v-model="user.email">
              </div>
              <div class="input-group">
                <span class="input-group-addon"
                      id="basic-addon2"><i class="fa fa-key" aria-hidden="true"></i></span>
                <input type="password"
                       name="password"
                       class="form-control"
                       placeholder="Password"
                       aria-describedby="basic-addon2"
                       v-model="user.password">
              </div>
              <div class="text-center">
                <input type="submit"
                       class="btn btn-success btn-submit"
                       value="Login">
              </div>
            </form>
          </div>
        </div>
      </div>
      <div class="app-footer"></div>
    </div>
  </div>
</template>
<script>
import Auth from '../services/auth.js';
export default {
  data: function () {
    return {
      working: false,
      user: {
        email: null,
        password: null,
      },
      fieldErrors: [],
    }
  },
  methods: {
    submit: function () {
      this.working = true;
      this.fieldErrors = [];

      Auth.login(this, this.user);

      // this.$http.post('login', JSON.stringify(this.user)).then(response => {
      //   // router.go('/dashboard');
      //   self.working = false;
      // }, response => {
      //   this.fieldErrors = response.responseJSON.fieldErrors;
      //   self.working = false;
      // });
    }
  }
};
</script>

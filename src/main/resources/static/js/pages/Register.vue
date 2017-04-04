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
          <div class="title">Registering...</div>
        </div>
  
        <div class="app-block">
          <div class="app-right-section">
            <div class="app-brand">
              <span class="highlight">Employger</span> Admin
            </div>
  
            <div class="app-info">
              <ul class="list">
                <li>
                  <div class="icon">
                    <i class="fa fa-paper-plane-o"
                       aria-hidden="true"></i>
                  </div>
                  <div class="title">
                    Increase <b>Productivity</b>
                  </div>
                </li>
                <li>
                  <div class="icon">
                    <i class="fa fa-cubes"
                       aria-hidden="true"></i>
                  </div>
                  <div class="title">
                    Lot of <b>Components</b>
                  </div>
                </li>
                <li>
                  <div class="icon">
                    <i class="fa fa-usd"
                       aria-hidden="true"></i>
                  </div>
                  <div class="title">
                    Forever <b>Free</b>
                  </div>
                </li>
              </ul>
            </div>
          </div>
  
          <div class="app-form">
  
            <div class="alert alert-danger  alert-dismissible"
                 role="alert"
                 v-if="fieldErrors.length > 0">
              <button type="button"
                      class="close"
                      data-dismiss="alert"
                      aria-label="Close">
                <span aria-hidden="true"></span>
              </button>
              <strong>Oh snap!</strong>
              <ul>
                <li v-for="field in fieldErrors">{{ field.field }} {{ field.message }}</li>
              </ul>
            </div>
  
            <div class="form-suggestion">Create an account for free. And pay later.</div>
  
            <form @submit.prevent="submit">
              <div class="input-group">
                <span class="input-group-addon"><i class="fa fa-user" aria-hidden="true"></i></span>
                <input type="text"
                       v-model="user.firstName"
                       class="form-control"
                       placeholder="First name">
              </div>
              <div class="input-group">
                <span class="input-group-addon"><i class="fa fa-user" aria-hidden="true"></i></span>
                <input type="text"
                       v-model="user.lastName"
                       class="form-control"
                       placeholder="Last name">
              </div>
              <div class="input-group">
                <span class="input-group-addon"><i class="fa fa-envelope-o" aria-hidden="true"></i></span>
                <input type="email"
                       v-model="user.email"
                       class="form-control"
                       placeholder="Email">
              </div>
              <div class="input-group">
                <span class="input-group-addon"><i class="fa fa-key" aria-hidden="true"></i></span>
                <input type="password"
                       v-model="user.password"
                       class="form-control"
                       placeholder="Password">
              </div>
              <div class="input-group">
                <span class="input-group-addon"><i class="fa fa-check" aria-hidden="true"></i></span>
                <input type="password"
                       v-model="user.passwordConfirm"
                       class="form-control"
                       placeholder="Confirm Password">
              </div>
  
              <div class="form-line">
                <div class="title">Company detail</div>
              </div>
  
              <div class="input-group">
                <span class="input-group-addon"><i class="fa fa-building-o" aria-hidden="true"></i></span>
                <input type="text"
                       v-model="user.company.name"
                       class="form-control"
                       placeholder="Name">
              </div>
  
              <div class="input-group">
                <span class="input-group-addon"><i class="fa fa-hashtag" aria-hidden="true"></i></span>
                <input type="number"
                       v-model="user.company.ico"
                       class="form-control"
                       placeholder="ICO">
              </div>
  
              <div class="text-center">
                <input type="submit"
                       class="btn btn-success btn-submit"
                       value="Register" />
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
          ico: null
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

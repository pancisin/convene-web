<template>
  <header id="topnav">
    <div class="topbar-main">
      <div class="container">
  
        <div class="logo">
          <router-link to="/" class="logo">
            <i class="fa fa-book"></i>
            <span>Bookster</span>
          </router-link>
  
        </div>
  
        <div class="menu-extras">
          <ul class="nav navbar-nav navbar-right pull-right">
            <li>
              <form role="search" class="navbar-left app-search pull-left hidden-xs">
                <input type="text" placeholder="Search..." class="form-control">
                <a href="">
                  <i class="fa fa-search"></i>
                </a>
              </form>
            </li>

            <notifications />

            <li class="dropdown">
              <a href="" class="dropdown-toggle waves-effect waves-light profile" data-toggle="dropdown" aria-expanded="true">
                <img src="https://static1.squarespace.com/static/56ba4348b09f95db7f71a726/t/58d7f267ff7c50b172895560/1490547315597/justin.jpg" alt="user-img" class="img-circle">
              </a>
              <ul class="dropdown-menu" v-if="auth.user.authenticated">
                <li>
                  <router-link to="/admin">
                    <i class="ti-user m-r-5"></i> Admin</router-link>
                </li>
                <li>
                  <a @click="logout">
                    <i class="ti-power-off m-r-5"></i> Logout</a>
                </li>
              </ul>
              <ul class="dropdown-menu" v-else>
                <li>
                  <router-link to="/login">
                    Login
                  </router-link>
                </li>
                <li>
                  <router-link to="/register">
                    Register
                  </router-link>
                </li>
              </ul>
            </li>
          </ul>
  
          <div class="menu-item">
            <a class="navbar-toggle">
              <div class="lines">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </a>
          </div>
        </div>
      </div>
    </div>
  
    <div class="navbar-custom active">
      <div class="container">
        <div id="navigation" class="active">
          <ul class="navigation-menu">
            <li>
              <router-link to="/">
                <i class="material-icons">home</i> Home
              </router-link>
            </li>
            <li>
              <router-link to="/event">
                <i class="material-icons">event</i> My events</router-link>
            </li>
            <li>
              <router-link to="/explore">
                <i class="material-icons">explore</i> Explore pages</router-link>
            </li>
            <li>
              <router-link to="/conference">
                <i class="material-icons">people</i> Conferences</router-link>
            </li>
            <li class="has-submenu">
              <a>
                <i class="material-icons">question_answer</i> About bookster</a>
              <ul class="submenu megamenu">
                <li>
                  <ul>
                    <li>
                      <a href="pages-blank.html">About</a>
                    </li>
                    <li>
                      <a>Developers</a>
                    </li>
                  </ul>
                </li>
                <li>
                  <ul>
                    <li>
                      <a>Pricing</a>
                    </li>
                  </ul>
                </li>
                <li>
                  <ul>
                    <li>
                      <a href="pages-login.html">FAQ</a>
                    </li>
                    <li>
                      <a href="pages-register.html">Terms & Agreements</a>
                    </li>
                    <li>
                      <a href="pages-recoverpw.html">Contact</a>
                    </li>
                  </ul>
                </li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
require("../assets/less/custom-menu.less")
import Auth from '../services/auth.js'
import Notifications from '../elements/Notifications.vue'

export default {
  name: 'header',
  computed: {
    auth() {
      return Auth;
    }
  },
  components: {
    Notifications,
  },
  methods: {
    logout: function () {
      Auth.logout(this, '/login')
    },
  }
}
</script>
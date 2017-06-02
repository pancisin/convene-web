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
  
            <lang-switcher />
  
            <notifications v-if="auth.user.authenticated" />
  
            <li class="dropdown" v-if="auth.user.authenticated">
              <a href="" class="dropdown-toggle waves-effect waves-light profile" data-toggle="dropdown" aria-expanded="true">
                <img src="https://static1.squarespace.com/static/56ba4348b09f95db7f71a726/t/58d7f267ff7c50b172895560/1490547315597/justin.jpg" alt="user-img" class="img-circle">
              </a>
              <ul class="dropdown-menu" v-if="auth.user.authenticated">
                <li>
                  <router-link to="/admin" v-if="$store.getters.isAdmin">
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
            <template v-else>
              <li>
                <router-link to="login" class="waves-effect">Login</router-link>
              </li>
              <li>
                <router-link to="register" class="waves-effect">Register</router-link>
              </li>
            </template>
          </ul>
  
          <div class="menu-item">
            <a class="navbar-toggle waves-effect" @click="collapsed = !collapsed">
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
        <slide-transition>
          <div id="navigation" class="active" v-show="!collapsed">
            <ul class="navigation-menu">
              <li>
                <router-link to="/" class="waves-effect" exact>
                  <i class="material-icons">home</i> {{ $t('client.menu.home') }}
                </router-link>
              </li>
              <li>
                <router-link to="/events" class="waves-effect">
                  <i class="material-icons">event</i> Events
                </router-link>
              </li>
              <li>
                <router-link to="/explore" class="waves-effect">
                  <i class="material-icons">explore</i> {{ $t('client.menu.explore') }}</router-link>
              </li>
              <!--<li><router-link to="/conference"><i class="material-icons">people</i> Conferences</router-link></li>-->
              <li class="has-submenu">
                <a>
                  <i class="material-icons">question_answer</i> {{ $t('client.menu.about') }}</a>
                <ul class="submenu megamenu">
                  <li>
                    <ul>
                      <li>
                        <router-link to="pricing">
                          Pricing
                        </router-link>
                      </li>
                    </ul>
                  </li>
  
                  <li>
                    <ul>
                      <li>
                        <router-link to="faq">
                          FAQ
                        </router-link>
                      </li>
                      <li>
                        <router-link to="terms">
                          Terms & Conditions
                        </router-link>
                      </li>
                      <li>
                        <router-link to="privacy-policy">
                          Privacy policy
                        </router-link>
                      </li>
                    </ul>
                  </li>
                </ul>
              </li>
            </ul>
          </div>
        </slide-transition>
      </div>
    </div>
  </header>
</template>

<script>
// require("../assets/less/custom-menu.less")
import Auth from '../services/auth.js'
import Notifications from '../elements/Notifications.vue'
import LangSwitcher from '../elements/LangSwitcher.vue'
import SlideTransition from '../functional/SlideTransition'

export default {
  name: 'header',
  data() {
    return {
      collapsed: true,
    }
  },
  watch: {
    '$route': 'closeNavbar',
  },
  computed: {
    auth() {
      return Auth;
    }
  },
  components: {
    Notifications, LangSwitcher, SlideTransition
  },
  methods: {
    logout() {
      Auth.logout(this, '/login')
    },
    closeNavbar() {
      this.collapsed = true;
    }
  }
}
</script>
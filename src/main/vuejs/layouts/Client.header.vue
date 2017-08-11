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
                <img :src="avatar" alt="user-img" class="img-circle">
              </a>
              <ul class="dropdown-menu" v-if="auth.user.authenticated">
                <li>
                  <router-link :to="{ name : 'settings' }" class="waves-effect waves-light">
                    <i class="fa fa-cog m-r-10"></i> Settings
                  </router-link>
                </li>
                <li>
                  <a @click="logout">
                    <i class="fa fa-power-off m-r-10"></i> Logout</a>
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
              <li class="has-submenu">
                <router-link to="/events" class="waves-effect">
                  <i class="material-icons">event</i> Events
                </router-link>
                <ul class="submenu">
                  <li>
                    <router-link to="/events" class="waves-effect">
                      Explore
                    </router-link>
                  </li>
                  <li>
                    <router-link to="/my-events" class="waves-effect">
                      My events
                    </router-link>
                  </li>
                </ul>
              </li>
              <li>
                <router-link to="/explore" class="waves-effect">
                  <i class="material-icons">explore</i> {{ $t('client.menu.explore') }}</router-link>
              </li>
              <li>
                <router-link to="/conferences">
                  <i class="material-icons">people</i> Conferences</router-link>
              </li>
              <li class="has-submenu">
                <router-link to="/about">
                  <i class="material-icons">question_answer</i> {{ $t('client.menu.about') }}
                </router-link>
                <ul class="submenu megamenu">
                  <li>
                    <ul>
                      <li>
                        <router-link to="/about">
                          About
                        </router-link>
                      </li>
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
              <li>
                <router-link to="/admin" v-if="isAdmin">
                  <i class="material-icons">dashboard</i> Admin</router-link>
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
import Auth from '../services/auth.js';
import Notifications from '../elements/Notifications.vue';
import LangSwitcher from '../elements/LangSwitcher.vue';
import SlideTransition from '../functional/SlideTransition';
import { mapGetters } from 'vuex';
import gravatar from 'gravatar';

export default {
  name: 'header',
  data () {
    return {
      collapsed: true
    };
  },
  watch: {
    '$route': 'closeNavbar'
  },
  computed: {
    ...mapGetters({
      isAdmin: 'isAdmin',
      user: 'getUser'
    }),
    auth () {
      return Auth;
    },
    avatar () {
      return gravatar.url(this.user.email, {
        protocol: 'https',
        size: 36
      });
    }
  },
  components: {
    Notifications, LangSwitcher, SlideTransition
  },
  methods: {
    logout () {
      Auth.logout(this, '/login');
    },
    closeNavbar () {
      this.collapsed = true;
    }
  }
};
</script>
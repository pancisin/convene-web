<template>
  <header id="topnav">
    <div class="topbar-main">
      <div class="container">

        <product-logo inline light />

        <div class="menu-extras">
          <ul class="nav navbar-nav navbar-right pull-right">
            <li>
              <form role="search"
                class="navbar-left app-search pull-left hidden-xs">
                <input type="text"
                  placeholder="Search..."
                  class="form-control">
                <a href="">
                  <i class="fa fa-search"></i>
                </a>
              </form>
            </li>

            <lang-switcher />
            <notifications v-if="authenticated" />
            <user-profile v-if="authenticated" />
            <template v-else>
              <li>
                <router-link :to="{ name: 'login', query: { redirect: $route.path } }"
                  class="waves-effect">Login</router-link>
              </li>
              <li>
                <router-link :to="{ name: 'register' }"
                  class="waves-effect">Register</router-link>
              </li>
            </template>
          </ul>

          <div class="menu-item">
            <a class="navbar-toggle waves-effect"
              @click="collapsed = !collapsed">
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

    <div class="navbar-custom">
      <div class="container">
        <slide-transition>
          <div id="navigation"
            class="active"
            v-show="!collapsed">
            <ul class="navigation-menu"
              v-if="clientMenu.hasPermission(user)">
              <li v-for="(route, index) in clientMenu.routes"
                :key="index"
                v-if="route.hasPermission(user)"
                :class="{ 'has-submenu' : route.children && route.children.length > 0 }">
                <router-link :to="{ name: route.name }"
                  class="waves-effect"
                  exact>
                  <i class="material-icons">{{ route.icon }}</i> {{ $t(route.code) }}
                </router-link>
                <ul class="submenu"
                  v-if="route.children && route.children.length > 0">
                  <li v-for="(child, child_index) in route.children"
                    :key="child_index">
                    <router-link :to="{ name: child.name }"
                      class="waves-effect">
                      {{ $t(child.code) }}
                    </router-link>
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
import { Notifications, LangSwitcher, UserProfile, ProductLogo } from 'elements';
import SlideTransition from '../functional/SlideTransition';
import { mapGetters } from 'vuex';
import menus from '../services/maps/menus.map';

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
    ...mapGetters(['user', 'authenticated']),
    clientMenu () {
      return menus.client;
    }
  },
  components: {
    Notifications, LangSwitcher, SlideTransition, UserProfile, ProductLogo
  },
  methods: {
    closeNavbar () {
      this.collapsed = true;
    }
  }
};
</script>
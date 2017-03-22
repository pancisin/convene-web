<template>
<div class="app app-default">
 <aside class="app-sidebar" id="sidebar">
  <div class="sidebar-header">
    <button type="button" class="sidebar-toggle">
      <i class="fa fa-times"></i>
    </button>
  </div>
  <div class="sidebar-menu">
    <ul class="sidebar-nav">
      <li>
        <router-link to="/">
        <div class="icon">
          <i class="fa fa-tasks" aria-hidden="true"></i>
        </div>
        <div class="title">Dashboard</div>
        </router-link>
        </a>
      </li>
      <li>
        <router-link to="/work-management">
        <div class="icon">
          <i class="fa fa-suitcase" aria-hidden="true"></i>
        </div>
        <div class="title">Work management</div>
        </router-link>
      </li>
      <li>
        <router-link to="/employees">
        <div class="icon">
          <i class="fa fa-users" aria-hidden="true"></i>
        </div>
        <div class="title">Employees</div>
        </router-link>
      </li>
    </ul>
  </div>
  <div class="sidebar-footer">
    <ul class="menu">
      <li>
        <a href="/" class="dropdown-toggle" data-toggle="dropdown">
          <i class="fa fa-cogs" aria-hidden="true"></i>
        </a>
      </li>
      <li>
        <a href="#">
          <span class="flag-icon flag-icon-th flag-icon-squared"></span>
        </a>
      </li>
    </ul>
  </div>
</aside>

<div class="app-container">
  <header-component></header-component>
  <transition name="fade" mode="out-in">
    <router-view></router-view>
  </transition>
</div>
</div>
</template>

<script>
import HeaderComponent from './Header.vue';

export default {
  name: 'app',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  },
  components: {
    headerComponent : HeaderComponent,
  },
}
</script>

<style scoped>
a {
  cursor: pointer;
}
.fade-enter-active, .fade-leave-active {
  transition: opacity .2s
}
.fade-enter, .fade-leave-to {
  opacity: 0
}
</style>
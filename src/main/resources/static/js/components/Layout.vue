<template>
  <div class="app app-default">
    <aside class="app-sidebar"
           id="sidebar">
      <div class="sidebar-header">
        <a class="sidebar-brand"
           href="#">
          <img :src="$store.state.user.company.logo"
               class="img-thumbnail" />
        </a>
        <button type="button"
                class="sidebar-toggle">
          <i class="fa fa-times"></i>
        </button>
      </div>
      <div class="sidebar-menu">
        <ul class="sidebar-nav">
          <li>
            <router-link to="/dashboard">
              <div class="icon">
                <i class="fa fa-tasks"
                   aria-hidden="true"></i>
              </div>
              <div class="title">{{ $t('menu.dashboard') }}</div>
            </router-link>
          </li>
          <li>
            <router-link to="/duty">
              <div class="icon">
                <i class="fa fa-suitcase"
                   aria-hidden="true"></i>
              </div>
              <div class="title">{{ $t('menu.work_management') }}</div>
            </router-link>
          </li>
          <li>
            <router-link to="/employees">
              <div class="icon">
                <i class="fa fa-users"
                   aria-hidden="true"></i>
              </div>
              <div class="title">{{ $t('menu.employees') }}</div>
            </router-link>
          </li>
          <li>
            <router-link to="/customers">
              <div class="icon">
                <i class="fa fa-users"
                   aria-hidden="true"></i>
              </div>
              <div class="title">{{ $t('menu.customers') }}</div>
            </router-link>
          </li>
        </ul>
      </div>
    </aside>
  
    <div class="app-container">
      <header-component></header-component>
      <transition name="fade-down"
                  mode="out-in">
        <router-view></router-view>
      </transition>
    </div>
  </div>
</template>

<script> 
import HeaderComponent from './Header.vue';

export default {
  name: 'app',
  components: {
    headerComponent: HeaderComponent,
  },
}
</script>

<style lang="less">
aside.app-sidebar {
  background-color: #333;
}
</style>
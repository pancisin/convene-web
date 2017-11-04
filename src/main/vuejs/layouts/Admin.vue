<template>
  <div id="wrapper">
    <header-component @hamburgerClicked="toggleSidebar" />
    <sidebar-component :collapsed="sidebarCollapsed" />

    <div class="content-page">
      <div class="content">
        <transition name="fade-down"
          mode="out-in">
          <router-view></router-view>
        </transition>
        <footer-component></footer-component>
      </div>
    </div>
  </div>
</template>

<script>
import HeaderComponent from './Admin.header.vue';
import FooterComponent from './Footer.vue';
import SidebarComponent from './Admin.sidebar.vue';

export default {
  name: 'app',
  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (!vm.$store.getters.isAdmin) {
        next('/');
      }
    });
  },
  data() {
    return {
      sidebarCollapsed: true
    };
  },
  watch: {
    $route: 'closeSidebar'
  },
  components: {
    HeaderComponent,
    FooterComponent,
    SidebarComponent
  },
  methods: {
    toggleSidebar() {
      this.sidebarCollapsed = !this.sidebarCollapsed;
    },
    closeSidebar() {
      this.sidebarCollapsed = true;
    }
  }
};
</script>
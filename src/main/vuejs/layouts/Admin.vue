<template>
  <div id="wrapper">
    <chat-container />
    <toast-container />
    <header-component />
    <sidebar-component />
  
    <div class="content-page">
      <div class="content">
        <transition name="fade-down" mode="out-in">
          <router-view></router-view>
        </transition>
        <footer-component></footer-component>
      </div>
    </div>
  </div>
</template>

<script> 
import HeaderComponent from './Admin.header.vue'
import FooterComponent from './Footer.vue'
import ToastContainer from '../elements/ToastContainer.vue'
import ChatContainer from '../elements/ChatContainer.vue'
import SidebarComponent from './Admin.sidebar.vue'

export default {
  name: 'app',
  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (!vm.$store.getters.isAdmin)
        next("/");
    })
  },
  components: {
    HeaderComponent,
    FooterComponent,
    ToastContainer,
    ChatContainer,
    SidebarComponent
  },
}
</script>
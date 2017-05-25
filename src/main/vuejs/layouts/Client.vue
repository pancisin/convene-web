<template>
  <div id="wrapper">
    <header-component></header-component>
  
    <div class="content">
      <transition name="fade-down" mode="out-in">
        <router-view></router-view>
      </transition>
    </div>
  
    <footer-component></footer-component>
  </div>
</template>

<script> 
import HeaderComponent from './Client.header.vue'
import FooterComponent from './Footer.vue'

export default {
  name: 'layout',
  components: {
    HeaderComponent,
    FooterComponent,
  },
}
</script>

<style lang="less" scoped>
.content {
  margin-top: 145px;
}
</style>
<template>
  <div id="wrapper">
    <header-component></header-component>
  
    <div class="content">
      <transition name="fade-down" mode="out-in">
        <router-view></router-view>
      </transition>
    </div>
  
    <footer class="footer text-right">
      <div class="container">
        <div class="row">
          <div class="col-xs-12">
            2017 © Minton.
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<script> 
import HeaderComponent from './PublicHeader.vue';

export default {
  name: 'layout',
  components: {
    headerComponent: HeaderComponent,
  },
}
</script>

<style lang="less" scoped>
.content {
  margin-top: 145px;
}
</style>
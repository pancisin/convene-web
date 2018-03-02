<template>
  <div id="wrapper">
    <header-component />
  
    <div class="content">
      <keep-alive>
        <transition 
          name="fade-down"
          mode="out-in">

          <router-view :key="$route.path" />
        </transition>
      </keep-alive>
    </div>
  
    <footer-component />
  </div>
</template>

<script>
import HeaderComponent from './Client.header.vue';
import FooterComponent from './Footer.vue';

export default {
  name: 'layout',
  components: {
    HeaderComponent,
    FooterComponent
  }
};
</script>

<style lang="less" scoped>
@media(max-width: 992px) {
  .content {
    margin-top: 75px;
  }
}

@media(min-width: 992px) {
  .content {
    margin-top: 145px;
  }
}
</style>
<template>
  <div class="router-tab-navigation">
    <ul class="nav nav-tabs">
      <li class="tab" 
        v-for="(route, index) in menu.routes" 
        :key="index"
        :class="{ 'active' : $route.name === route.name }">

        <router-link :to="{ name: route.name }">
          <i class="fa" :class="`fa-${route.icon}`"></i>
          {{ $t(route.code) }}
        </router-link>
      </li>
      <div class="indicator"></div>
    </ul>
    <div class="tab-content">
      <slot>
        <router-view></router-view>
      </slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'router-tab-navigation',
  props: ['menu'],
  created () {
    console.log(this.menu);
  }
};
</script>

<style lang="less">
.router-tab-navigation {
  margin-bottom: 20px;
  
  ul {
    .tab i {
      margin-right: 10px;
    }
  }

  .tab-content {
    box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.1) !important;
    background: #fff;
    padding: 15px;
  }
}
</style>

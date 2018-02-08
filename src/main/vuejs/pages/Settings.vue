<template>
  <div class="container">
    <div class="page-title-box">
      <h4 class="page-title">{{ $t('settings.default') }}</h4>
    </div>
  
    <div class="user-settings">
      <ul class="nav nav-tabs">
        <li class="tab" 
          v-for="route in routes" 
          :key="route.name"
          :class="{ 'active' : $route.name === route.name }">

          <router-link :to="{ name: route.name }">
            <i class="fa" :class="route.icon"></i>
            {{ route.label }}
          </router-link>
        </li>
        <div class="indicator"></div>
      </ul>
      <div class="tab-content">
        <transition name="fade" mode="out-in">
          <router-view></router-view>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'user-settings',
  computed: {
    routes () {
      return [
        {
          name: 'settings.account',
          label: this.$t('settings.account'),
          icon: 'fa-user-o'
        },
        {
          name: 'settings.privacy',
          label: this.$t('settings.privacy.default'),
          icon: 'fa-key'
        },
        {
          name: 'settings.license',
          label: this.$t('settings.license.default'),
          icon: 'fa-file-text-o'
        },
        {
          name: 'settings.notifications',
          label: this.$t('settings.notifications'),
          icon: 'fa-bell-o'
        }
      ];
    }
  }
};
</script> 

<style lang="less">
.user-settings {
  margin-bottom: 20px;
  
  ul {
    .tab i {
      margin-right: 10px;
    }
  }

  .tab-content {
    background: #fff;
    padding: 15px;
  }
}
</style>

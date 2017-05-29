<template>
  <div>
    <h3 class="page-title">{{ $t('settings.default') }}</h3>
  
    <div class="row">
      <div class="col-md-9">
        <transition name="fade-up" mode="out-in">
          <keep-alive>
            <router-view></router-view>
          </keep-alive>
        </transition>
      </div>
      <div class="col-md-3">
        <div class="list-group mail-list">
          <router-link to="account" class="list-group-item waves-effect">
            <i class="fa fa-user-o"></i>
            {{ $t('settings.account') }}
          </router-link>
          <router-link to="license" class="list-group-item waves-effect">
            <i class="fa fa-file-text-o"></i>
            {{ $t('settings.license.default') }}
          </router-link>
          <router-link to="notifications" class="list-group-item waves-effect">
            <i class="fa fa-bell-o"></i>
            {{ $t('settings.notifications') }}
          </router-link>
        </div>
      </div>
  
    </div>
  </div>
</template>

<script>
export default {
  name: 'settings'
}
</script>
<template>
  <div class="row">
    <div class="col-xs-12">
      <h3 class="page-title">Settings</h3>
    </div>
    <div class="col-md-3">
      <div class="list-group">
        <router-link to="account" class="list-group-item waves-effect">
          Account
        </router-link>
        <router-link to="license" class="list-group-item waves-effect">
          License
        </router-link>
        <router-link to="notifications" class="list-group-item waves-effect">
          Notifications
        </router-link>
      </div>
    </div>
    <div class="col-md-9">
      <transition name="fade-up" mode="out-in">
        <keep-alive>
          <router-view></router-view>
        </keep-alive>
      </transition>
    </div>
  </div>
</template>

<script>
export default {

}
</script>

<style>

</style>

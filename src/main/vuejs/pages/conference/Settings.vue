<template>
  <div class="row">
    <div class="col-md-3">
      <div class="list-group mail-list">
        <router-link to="information" class="list-group-item waves-effect">
          <i class="fa fa-info"></i>
          Basic information
        </router-link>
        <router-link to="registration" class="list-group-item waves-effect">
          <i class="fa fa-users" aria-hidden="true"></i>
          Registration
        </router-link>
      </div>
    </div>
    <div class="col-md-9">
      <transition name="fade-up" mode="out-in">
        <router-view :conference="conference"></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
export default {
  name: 'conference-settings',
  props: ['conference']
};
</script>
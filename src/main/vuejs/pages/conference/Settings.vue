<template>
  <div class="row">
    <div class="col-md-3">
      <div class="list-group mail-list">
        <router-link :to="{ name: 'conference.settings.information' }" class="list-group-item waves-effect">
          <i class="fa fa-info"></i>
          Basic information
        </router-link>
        <router-link :to="{ name: 'conference.settings.registration' }" class="list-group-item waves-effect">
          <i class="fa fa-users" aria-hidden="true"></i>
          Registration
        </router-link>
        <router-link :to="{ name: 'conference.settings.deletion' }" class="list-group-item waves-effect">
          <i class="fa fa-trash-o" aria-hidden="true"></i>
          Delete
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
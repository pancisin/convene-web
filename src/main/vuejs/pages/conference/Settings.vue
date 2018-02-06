<template>
  <div>
    <ul class="nav nav-tabs">
      <li class="tab" 
        v-for="route in routes" 
        :key="route.name">

        <router-link :to="{ name: route.name }">
          {{ route.label }}
        </router-link>
      </li>
      <div class="indicator"></div>
    </ul>
    <div class="tab-content">
      <transition name="fade-up" mode="out-in">
        <router-view :conference="conference"></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
export default {
  name: 'conference-settings',
  props: ['conference'],
  computed: {
    routes () {
      return [
        {
          name: 'conference.settings.information',
          label: 'Basic information'
        },
        {
          name: 'conference.settings.registration',
          label: 'Registration form'
        },
        {
          name: 'conference.settings.partners',
          label: 'Partners'
        },
        {
          name: 'conference.settings.deletion',
          label: 'Delete'
        }
      ];
    }
  }
};
</script>
<template>
  <div class="notification-container" is="transition-group" name="fade-up">
    <toast v-for="(toast, index) in toasts" :notification="toast" @close-notification="removeToast" :key="index">
    </toast>
  </div>
</template>

<script>
import Toast from './Toast.vue';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'notification-container',
  components: {
    Toast
  },
  computed: {
    ...mapGetters(['toasts'])
  },
  methods: {
    ...mapActions([
      'removeToast'
    ])
  }
};
</script>

<style>
.notification-container {
  position: fixed;
  right: 10px;
  bottom: 10px;
  width: 350px;
  z-index: 9999;
}

@media only screen and (max-width: 320px) {
  .notification-container {
    max-width: 100%;
    right: 0;
    bottom: 0;
    width: 100%;
  }
}
</style>
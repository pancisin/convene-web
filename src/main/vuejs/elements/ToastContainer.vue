<template>
  <div class="notification-container" is="transition-group" name="fade-up">
    <toast v-for="(toast, index) in toasts" :notification="toast" @close-notification="removeToast" :key="index">
    </toast>
  </div>
</template>

<script>
import Toast from './Toast.vue'
import { mapGetters, mapActions } from 'vuex'

export default {
  name: 'notification-container',
  components: {
    Toast
  },
  computed: {
    ...mapGetters({
      toasts: 'getToasts'
    })
  },
  methods: {
    ...mapActions([
      'removeToast'
    ])
  }
}
</script>

<style>
.notification-container {
  position: fixed;
  right: 10px;
  bottom: 10px;
  width: 350px;
  z-index: 1;
}
</style>
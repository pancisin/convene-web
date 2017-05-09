<template>
  <div class="notification-container" is="transition-group" name="fade-up">
    <notification v-for="(notification, index) in notifications" :notification="notification" @close-notification="removeNotification" :key="index">
    </notification>
  </div>
</template>

<script>
import Notification from './Notification.vue'

export default {
  name: 'notification-container',
  components: {
    Notification
  },
  computed: {
    notifications() {
      return this.$store.state.notifications;
    }
  },
  methods: {
    removeNotification: function (notification) {
      this.$store.commit('removeNotification', notification); 
    }
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
<template>
  <transition name="fade" mode="out-in">
    <router-view>
    </router-view>
  </transition>
</template>

<script>
import Auth from '../services/auth.js'
export default {
  name: 'app-root',
  created() {
    if (Auth.user.authenticated)
      Auth.updateUserData(this);

    this.initializeStomp();
    this.fetchNotifications();
  },
  methods: {
    initializeStomp() {
      this.connectWM('stomp').then(frame => {
        this.$stompClient.subscribe('/user/queue/notifier', response => {
          var notification = JSON.parse(response.body);
          this.$store.commit('addNotification', notification);
        });
      })
    },
    fetchNotifications() {
      this.$http.get('api/user/notification').then(response => {
        this.$store.dispatch('initNotifications', response.body)
      });
    }
  }
};
</script>
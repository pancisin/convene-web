<template>
  <transition name="fade" mode="out-in">
    <router-view>
    </router-view>
  </transition>
</template>

<script>
export default {
  name: 'app-root',
  created() {
    this.initializeStomp();
    this.fetchNotifications();
  },
  methods: {
    initializeStomp: function () {
      this.connectWM('stomp').then(frame => {
        this.$stompClient.subscribe('/user/queue/notifier', response => {
          var notification = JSON.parse(response.body);
          this.$store.commit('addNotification', notification);
        });
      })
    },
    fetchNotifications: function () {
      this.$http.get('api/user/notification').then(response => {
        this.$store.dispatch('initNotifications', response.body)
      });
    },
  }
};
</script>
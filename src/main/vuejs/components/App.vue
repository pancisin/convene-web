<template>
  <transition name="fade" mode="out-in">
    <router-view>
    </router-view>
  </transition>
</template>

<script>
import Auth from '../services/auth.js'
import moment from "moment"
export default {
  name: 'app-root',
  created() {
    if (Auth.user.authenticated)
      Auth.updateUserData(this).then(user => {
        moment.locale(user.locale.code);
        this.$i18n.locale = user.locale.code;
      });

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
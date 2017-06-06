<template>
  <transition name="fade" mode="out-in">
    <router-view>
    </router-view>
  </transition>
</template>

<script>
import Auth from '../services/auth.js'
import moment from "moment"
import { mapActions } from 'vuex'

export default {
  name: 'app-root',
  created() {
    if (Auth.user.authenticated) {
      Auth.updateUserData(this).then(user => {
        moment.locale(user.locale.code);
        this.$i18n.locale = user.locale.code;
      });

      this.initializeStomp();
      this.initializeNotifications();
    }
  },
  methods: {
    ...mapActions([
      'initializeNotifications',
      'addNotification'
    ]),
    initializeStomp() {
      this.connectWM('stomp').then(frame => {
        this.$stompClient.subscribe('/user/queue/notifier', response => {
          var notification = JSON.parse(response.body);
          this.addNotification(notification);
          this.$info(notification.title, notification.message);
        });
      })
    },
  }
};
</script>
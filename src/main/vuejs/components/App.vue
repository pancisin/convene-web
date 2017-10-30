<template>
  <transition name="fade" mode="out-in">
    <router-view>
    </router-view>
  </transition>
</template>

<script>
import moment from 'moment';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'app-root',
  created () {
    if (this.authenticated) {
      this.initializeUser().then(user => {
        moment.locale(user.locale.name);
        this.$i18n.locale = user.locale.name;
        this.$ga.set('userId', user.id);
      });

      this.initializeFollowedPages();
      this.initializeStomp();
      this.initializeNotifications();
    }
  },
  computed: {
    ...mapGetters(['authenticated'])
  },
  methods: {
    ...mapActions([
      'initializeNotifications',
      'addNotification',
      'initializeUser',
      'initializeFollowedPages'
    ]),
    initializeStomp () {
      this.connectWM('stomp').then(frame => {
        this.$stompClient.subscribe('/user/queue/notifier', response => {
          var notification = JSON.parse(response.body);
          this.addNotification(notification);
          this.$info(notification.code, notification.target);
        });
      });
    }
  }
};
</script>
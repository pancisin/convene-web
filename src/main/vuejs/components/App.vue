<template>
  <div>
    <chat-container v-if="authenticated" />
    <toast-container />

    <transition name="fade"
        mode="out-in">
      <router-view>
      </router-view>
    </transition>

    <!-- <cookie-law /> -->
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import { ToastContainer, ChatContainer, CookieLaw } from 'elements';
import { Settings } from 'luxon';

export default {
  name: 'app-root',
  created () {
    if (this.authenticated) {
      this.initializeUser().then(user => {
        Settings.defaultLocale = user.locale.prop;
        this.$i18n.locale = user.locale.prop;
        this.$ga.set('userId', user.id);
      });
    }
  },
  components: {
    ToastContainer,
    ChatContainer,
    CookieLaw
  },
  computed: {
    ...mapGetters(['authenticated', 'user'])
  },
  watch: {
    async user (newVal) {
      if (this.authenticated) {
        this.initializeFollowedPages();
        this.initializeAttendingEvents();
        this.initializeStomp();
        this.initializeNotifications();
        this.initializeConversations();

        await this.initializeContacts();
        this.connectWM('/stomp').then(frame => {
          this.$stompClient.subscribe(
            '/user/queue/chat.activeUsers',
            response => {
              this.sendWM('/app/activeUsers', {});
              this.updateContactsActivityState(JSON.parse(response.body));
            }
          );

          this.sendWM('/app/activeUsers', {});
        });
      }
    }
  },
  methods: {
    ...mapActions([
      'initializeNotifications',
      'addNotification',
      'initializeUser',
      'initializeFollowedPages',
      'initializeAttendingEvents',
      'initializeContacts',
      'initializeConversations',
      'updateContactsActivityState'
    ]),
    initializeStomp () {
      this.connectWM('/stomp').then(frame => {
        this.$stompClient.subscribe('/user/queue/notifier', response => {
          var notification = JSON.parse(response.body);
          this.addNotification(notification);
          this.$info(notification.code, {
            target: notification.target,
            subject: notification.subject
          });
        });
      });
    }
  }
};
</script>
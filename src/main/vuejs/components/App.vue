<template>
  <div>
    <chat-container v-if="authenticated" />
    <toast-container />

    <transition name="fade"
        mode="out-in">
      <router-view>
      </router-view>
    </transition>
  </div>
</template>

<script>
import moment from 'moment';
import { mapGetters, mapActions } from 'vuex';
import { ToastContainer, ChatContainer } from 'elements';

export default {
  name: 'app-root',
  created () {
    if (this.authenticated) {
      this.initializeUser().then(user => {
        moment.locale(user.locale.name);
        this.$i18n.locale = user.locale.name;
        this.$ga.set('userId', user.id);
      });
    }
  },
  components: {
    ToastContainer,
    ChatContainer
  },
  computed: {
    ...mapGetters(['authenticated', 'user'])
  },
  watch: {
    user (newVal) {
      if (this.authenticated) {
        this.initializeFollowedPages();
        this.initializeAttendingEvents();
        this.initializeStomp();
        this.initializeNotifications();
        this.initializeContacts().then(() => {
          this.connectWM('stomp').then(frame => {
            this.$stompClient.subscribe(
              '/user/queue/chat.activeUsers',
              response => {
                this.sendWM('/app/activeUsers', {});
                this.updateContactsActivityState(JSON.parse(response.body));
              }
            );

            this.sendWM('/app/activeUsers', {});
          });
        });
        this.initializeConversations();
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
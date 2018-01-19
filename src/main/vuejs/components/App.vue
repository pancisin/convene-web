<template>
  <div>
    <chat-container v-if="authenticated" />
    <toast-container />

    <transition name="fade"
        mode="out-in">
      <router-view>
      </router-view>
    </transition>

    <guest-catcher />
    <position-query />

    <ad-sense
      ad-client="ca-pub-8590347690879523"
      ad-slot="XXXXXXXX">
    </ad-sense>
    <!-- <cookie-law /> -->
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import { ToastContainer, ChatContainer, CookieLaw, AdSense } from 'elements';
import { Settings } from 'luxon';
import { GuestCatcher, PositionQuery } from 'components';

export default {
  name: 'app-root',
  data () {
    return {
      displayLoginModal: true
    };
  },
  created () {
    this.initializeMachine();
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
    CookieLaw,
    GuestCatcher,
    AdSense,
    PositionQuery
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
      'updateContactsActivityState',
      'initializeMachine'
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
<template>
  <div class="conversation-container">
    <transition name="fade-up" mode="out-in">
      <div class="chat-conversation" v-if="!collapsed">
        <div class="chat-header">
          <a @click="navigateBack" v-if="currentView == 'conversation-list'">
            <i class="fa fa-angle-left fa-lg m-r-15"></i>
          </a>

          {{ user != null ? user.displayName : "Conversations" }}
          <a @click="collapsed = true" class="pull-right">
            <i class="fa fa-times"></i>
          </a>
        </div>

        <div class="chat-wrapper">
          <transition name="fade-down" mode="out-in">
            <keep-alive>
              <component :is="currentView" :recipient="user" @selected="userSelected"></component>
            </keep-alive>
          </transition>
        </div>
      </div>
      <a class="btn btn-primary btn-rounded btn-chat pull-right waves-effect" @click="collapsed = !collapsed" v-else>
        <i class="fa fa-comment-o fa-lg"></i>
      </a>
    </transition>
  </div>
</template>

<script>
import ContactsList from './parts/ContactsList.vue';
import ConversationList from './parts/ConversationList.vue';

export default {
  name: 'conversation-container',
  data () {
    return {
      collapsed: true,
      currentView: 'contacts-list',
      user: null,
      activeUsers: []
    };
  },
  created () {
    this.connectWM('stomp').then(frame => {
      this.$stompClient.subscribe('/user/queue/chat.message', response => {
        let message = JSON.parse(response.body);
        this.$emit('messageReceived', message);

        if (this.collapsed) {
          this.$info('Message', message.content);
        }
      });

      this.$stompClient.subscribe('/topic/active', response => {
        this.sendWM('/app/activeUsers', {});
        let active_us = JSON.parse(response.body);
        if (this.activeUsers.length !== active_us.length) {
          this.activeUsers = active_us;
        }
      });
    }, frame => {
      // console.log(frame);
    });
  },
  watch: {
    activeUsers (newVal) {
      this.$emit('activityChanged', this.activeUsers);
    }
  },
  components: {
    ContactsList,
    ConversationList
  },
  methods: {
    userSelected (user) {
      this.user = user;
      this.currentView = 'conversation-list';
    },
    navigateBack () {
      this.currentView = 'contacts-list';
      this.user = null;
    }
  }
};
</script>

<style lang="less" scoped>
.conversation-container {
  position: fixed;
  right: 0;
  bottom: 0;
  z-index: 9;
  margin: 14px;

  width: 320px;

  .chat-header {
    padding: 15px;
    background: #039cfd;
    color: white;
    font-weight: bold;

    a {
      color: #fff;
    }
  }

  .chat-wrapper {
    border: 1px solid #ccc;
    border-radius: 2px;
    border-top-left-radius: 0;
    border-top-right-radius: 0;

    background: white;
    overflow: hidden;
  }
}

.btn-chat {
  padding: 15px 16px;
  border-radius: 30px;
}

@media(max-width: 768px) {
  .conversation-container {
    left: 0;
    width: auto;
    z-index: 9999;
  }
}
</style>

<template>
  <div class="conversation-container">
    <transition name="fade-up"
      mode="out-in">
      <div class="chat-conversation"
        v-if="!collapsed">
        <div class="chat-header">
          <a @click="navigateBack"
            v-if="currentView == 'conversation-list'">
            <i class="fa fa-angle-left fa-lg"></i>
          </a>

          <span v-if="selectedUserId != null">
            <span>
              {{ user.displayName }}
            </span>
            <i class="fa fa-circle status"
              :class="{ 'online' : user.active }"></i>
          </span>
          <span v-else>Conversations</span>
          <a @click="collapsed = true"
            class="pull-right">
            <i class="fa fa-times"></i>
          </a>
        </div>

        <div class="chat-wrapper">
          <transition name="fade"
            mode="out-in">
            <keep-alive>
              <component :is="currentView"
                :recipient="user"
                @selected="userSelected">
              </component>
            </keep-alive>
          </transition>
        </div>
      </div>
      <a class="btn btn-primary btn-rounded btn-chat pull-right waves-effect"
        @click="collapsed = !collapsed"
        v-else>
        <i class="fa fa-comment-o fa-lg"></i>
      </a>
    </transition>
  </div>
</template>

<script>
import ContactsList from './parts/ContactsList.vue';
import ConversationList from './parts/ConversationList.vue';
import { mapGetters } from 'vuex';

export default {
  name: 'conversation-container',
  data () {
    return {
      collapsed: true,
      currentView: 'contacts-list',
      selectedUserId: null
    };
  },
  computed: {
    ...mapGetters(['getContactById']),
    user () {
      return this.getContactById(this.selectedUserId);
    }
  },
  created () {
    this.connectWM('/stomp').then(
      frame => {
        this.$stompClient.subscribe('/user/queue/chat.message', response => {
          let message = JSON.parse(response.body);
          this.$emit('messageReceived', message);

          if (this.collapsed) {
            this.$info('notification.chat.message', message.content);
          }
        });
      },
      frame => {
        // console.log(frame);
      }
    );
  },
  components: {
    ContactsList,
    ConversationList
  },
  methods: {
    userSelected (userId) {
      this.selectedUserId = userId;
      this.currentView = 'conversation-list';
    },
    navigateBack () {
      this.currentView = 'contacts-list';
      this.selectedUserId = null;
    }
  }
};
</script>

<style lang="less" scoped>
@import (reference) '~less/variables.less';

.conversation-container {
  position: fixed;
  right: 0;
  bottom: 0;
  z-index: 9;
  margin: 14px;

  width: 320px;

  .chat-header {
    background-color: @color-primary;
    color: white;
    font-weight: bold;

    & > * {
      padding: 10px 15px;
      display: inline-block;
    }

    a {
      color: #fff;
      display: inline-block;
      transition: background-color .3s ease;

      &:hover {
        background-color: @color-primary-active;
      }
    }

    span > span {
      vertical-align: middle;
    }

    i.status {
      color: #dddddd;
      font-size: 9px;
      margin-left: 10px;

      &.online {
        color: #a0d269;
      }
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

@media (max-width: 768px) {
  // .conversation-container {
  //   left: 0;
  //   width: auto;
  //   z-index: 9999;
  // }
}
</style>

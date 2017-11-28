<template>
  <div class="app-messaging-container">
    <div class="app-messaging" id="collapseMessaging">
      <div class="chat-group">
        <div class="heading">{{ $t('chat.conversations') }}</div>
        <ul class="group full-height">
          <li class="section">{{ $tc('user.default', 2) }}</li>
          <li class="message" v-for="user in users" :class="{ 'selected' : recipient != null && recipient.id == user.id }" :key="user.id">
            <a @click="recipient = user">
              <!--<span class="badge badge-warning pull-right">10</span>-->
              <div class="message">
                <div class="profile">
                  {{ user.firstName.charAt(0) }} {{ user.lastName.charAt(0) }}
                </div>
                <div class="content">
                  <div class="title">{{ user.firstName }} {{ user.lastName }}</div>
                  <div class="description">{{ user.email }}</div>
                </div>
              </div>
            </a>
          </li>
        </ul>
      </div>
      <div class="messaging">
        <div class="heading">
          <div class="title">
            <a class="btn-back" @click="recipient = null" v-if="recipient != null">
              <i class="fa fa-angle-left" aria-hidden="true"></i>
            </a>
            <span v-if="recipient != null">{{ recipient.firstName }} {{ recipient.lastName }}</span>
            <span v-else>{{ user.company.name }}</span>
  
            <!--<span class="badge badge-success badge-icon"><i class="fa fa-circle" aria-hidden="true"></i><span>Online</span></span>-->
          </div>
          <div class="action"></div>
        </div>
        <ul class="chat" ref="chatContainer" is="transition-group" name="fade">
          <li class="line" v-if="messages[0] != null" :key="-1">
            <div class="title">{{ messages[0].created | moment("llll") }}</div>
          </li>
          <li v-for="(mes, index) in messages" :class="{ 'right' : mes.sender.id == user.id }" :key="index">
            <div class="message" v-text="mes.content"></div>
            <div class="info">
              <div class="datetime">{{ mes.created | moment("L LT") }}</div>
              <div class="status" v-if="recipient != null && recipient.id != mes.sender.id && mes.sender.id != user.id" v-text="mes.sender.name"></div>
            </div>
          </li>
        </ul>
        <div class="footer">
          <div class="message-box">
            <textarea :placeholder="$t('chat.placeholder')" class="form-control" v-model="message" @keyup.enter="sendMessage"></textarea>
            <button class="btn btn-default" @click="sendMessage">
              <i class="fa fa-paper-plane" aria-hidden="true"></i>
              <span>{{ $t('chat.send') }}</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import moment from 'moment';
import { mapGetters } from 'vuex';

export default {
  name: 'chat',
  data: function () {
    return {
      messages: [],
      message: null,
      recipient: null,
      users: [],
      loading: false,
      subscriptions: []
    };
  },
  watch: {
    recipient: function () {
      this.messages = [];
      this.fetchMessages();
    }
  },
  computed: {
    ...mapGetters(['user'])
  },
  created: function () {
    this.initializeChatStomp();
    this.fetchMessages();
    this.fetchCompanyUsers();
  },
  beforeDestroy: function () {
    console.log('destroying chat...');
    this.subscriptions.forEach(s => s.unsubscribe({}));
  },
  methods: {
    initializeChatStomp: function () {
      this.connectWM('/stomp').then(frame => {
        this.subscriptions.push(
          this.$stompClient.subscribe('/queue/chat', response => this.addMessage(JSON.parse(response.body)))
        );

        this.subscriptions.push(
          this.$stompClient.subscribe('/user/queue/chat.message', response => this.addMessage(JSON.parse(response.body)))
        );
      }, frame => {
        console.log(frame);
      });
    },
    fetchCompanyUsers: function () {
      this.$http.get('/api/company/' + this.user.company.id + '/users').then(response => {
        this.users = response.body;
      });
    },
    fetchMessages: function () {
      if (this.recipient != null) {
        this.$http.get('/api/message/user/' + this.recipient.id + '/0').then(response => this.addMessage(response.body.reverse()));
      } else {
        this.$http.get('/api/message/0').then(response => this.addMessage(response.body.reverse()));
      }
    },
    sendMessage: function () {
      if (this.message == null || this.message.trim() === '' || this.message.trim() === '\n') {
        this.message = null;
        return;
      }
      var data = {
        content: this.message
      };

      var self = this;
      function completed (result) {
        var mes = {
          content: self.message,
          sender: self.user,
          recipient: self.recipient,
          created: moment()
        };

        self.addMessage(mes);
        self.message = null;
      }

      if (this.recipient == null) {
        this.sendWM('/app/chat', data).then(completed);
      } else {
        this.sendWM('/app/chat.private.' + this.recipient.email, data).then(completed);
      }
    },
    addMessage: function (message) {
      if (message instanceof Array) {
        this.messages = message;
      } else {
        this.messages.push(message);
      }

      this.$nextTick(() => {
        var container = this.$refs.chatContainer.$el;
        container.scrollTop = container.scrollHeight;
      });
    }
  }
};
</script>

<style lang="less">
.messaging {
  min-height: 600px;
}

.app-messaging {
  .messaging ul.chat {
    max-height: 500px;
  }

  .chat-group ul.group {
    li.message {
      .profile {
        display: table-cell;
        font-size: 16px;
        line-height: 40px;
        text-align: center;
        background: #f4f7f7;
        letter-spacing: -2px;
      }

      &.selected {
        background: #f4f7f7;

        .description {
          opacity: 1 !important;
        }

        .profile {
          background: #fff;
        }
      }
    }
  }
}
</style>
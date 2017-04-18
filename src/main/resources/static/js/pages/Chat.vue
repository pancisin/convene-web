<template>
  <div class="app-messaging-container">
    <div class="app-messaging"
         id="collapseMessaging">
      <div class="chat-group">
        <div class="heading">Conversation</div>
        <ul class="group full-height">
          <li class="section">Users</li>
          <li class="message"
              v-for="user in users"
              :class="{ 'selected' : recipient != null && recipient.id == user.id }">
            <a @click="recipient = user">
              <!--<span class="badge badge-warning pull-right">10</span>-->
              <div class="message">
                <!--<img class="profile" src="https://placehold.it/100x100">-->
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
            <a class="btn-back"
               @click="recipient = null"
               v-if="recipient != null">
              <i class="fa fa-angle-left"
                 aria-hidden="true"></i>
            </a>
            <span v-if="recipient != null">{{ recipient.firstName }} {{ recipient.lastName }}</span>
            <span v-else><template v-if="company != null">{{ company.name }}</template></span>
  
            <!--<span class="badge badge-success badge-icon"><i class="fa fa-circle" aria-hidden="true"></i><span>Online</span></span>-->
          </div>
          <div class="action"></div>
        </div>
        <ul class="chat">
          <!--<li class="line">
                              <div class="title">24 Jun 2016</div>
                            </li>-->
          <li v-for="mes in messages"
              :class="{ 'right' : mes.sender.id == user.id }">
            <div class="message"
                 v-text="mes.content"></div>
            <div class="info">
              <div class="datetime">{{ mes.created | moment("dddd, DD.MM.YYYY") }}</div>
              <div class="status"
                   v-text="mes.sender.name"></div>
            </div>
          </li>
        </ul>
        <div class="footer">
          <div class="message-box">
            <textarea placeholder="type something..."
                      class="form-control"
                      v-model="message"
                      @keyup.enter="sendMessage"></textarea>
            <button class="btn btn-default"
                    @click="sendMessage"><i class="fa fa-paper-plane"
                 aria-hidden="true"></i><span>Send</span></button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Auth from '../services/auth.js'
import moment from "moment"
export default {
  name: 'chat',
  data: function () {
    return {
      messages: [],
      message: null,
      recipient: null,
      users: [],
      user: null,
    }
  },
  watch: {
    recipient: function() {
      this.fetchMessages();
    }
  },
  created: function () {
    this.initializeChatStomp();
    this.fetchMessages();

    Auth.currentUser(this).then((user) => {
      this.user = user;
      this.fetchCompanyUsers();
    });
  },
  methods: {
    initializeChatStomp: function () {
      this.connectWM('stomp').then(frame => {
        this.$stompClient.subscribe('/queue/chat', response => {
          var message = JSON.parse(response.body);
          if (message.sender.id != this.user.id)
            this.messages.push(message);
        });

        this.$stompClient.subscribe("/user/exchange/amq.direct/chat.message", response => {
          this.messages.push(JSON.parse(response.body));
        })
      }, frame => {
        console.log(frame);
      })
    },
    fetchCompanyUsers: function () {
      this.$http.get('api/company/' + this.user.company.id + '/users').then(response => {
        this.users = response.body;
      });
    },
    fetchMessages: function () {
      if (this.recipient != null)
        this.$http.get('/api/message/user/' + this.recipient.id).then(response => {
          this.messages = response.body;
        })
    },
    sendMessage: function () {
      if (this.message == null || this.message == '') return;
      var data = {
        content: this.message,
      }

      var self = this;
      function completed(result) {
        self.messages.push({
          content: self.message,
          sender: self.user,
          recipient: self.recipient,
          created: moment()
        })
        self.message = null;
      }

      if (this.recipient == null) {
        this.sendWM('/app/chat', data).then(completed);
      } else {
        this.sendWM('/app/chat.private.' + this.recipient.email, data).then(completed)
      }
    },
  }
}
</script>

<style lang="less">
ul.chat {
  max-height: 500px;
}

ul.group {
  .message.selected {
    background: #eef4d4;
  }
}

.messaging {
  min-height: 600px;
}
</style>
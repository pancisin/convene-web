<template>
  <div v-loading="loading">
    <ul class="conversation-list" ref="conversationList">
      <li class="clearfix" v-for="message in messages" :class="{ 'odd' : message.sender.email === user.email }" :key="message.id">
        <div class="chat-avatar">
          <img :src="getAvatar(message.sender)" alt="male">
        </div>
        <div class="conversation-text">
          <div class="ctext-wrap">
            <p v-text="message.content"></p>
            <small>{{ getTimeString(message.created) }}</small>
          </div>
        </div>
      </li>
    </ul>

    <form class="form conversation-composer" @submit.prevent="send">
      <div class="input-group">
        <input type="text" class="form-control" placeholder="Type something" v-model="message">
        <span class="input-group-btn">
          <button type="button" class="btn waves-effect waves-light btn-primary" @click="send">Send</button>
        </span>
      </div>
    </form>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import gravatar from 'gravatar';
import moment from 'moment';

export default {
  name: 'conversation',
  props: {
    recipient: Object
  },
  data () {
    return {
      messages: [],
      message: null,
      loading: false
    };
  },
  computed: {
    ...mapGetters(['user'])
  },
  watch: {
    'recipient': 'getMessages'
  },
  created () {
    this.$parent.$on('messageReceived', (message) => {
      if (message.sender.email === this.recipient.email) {
        this.addMessage(message);
      }
    });
    this.getMessages();
  },
  methods: {
    getMessages () {
      this.loading = true;
      this.messages = [];
      this.$http.get('api/message/user/' + this.recipient.id + '/0').then(response => {
        let messages = response.body;
        messages.sort((a, b) => {
          return a.created - b.created;
        });

        this.addMessage(messages);
        this.loading = false;
      });
    },
    getAvatar (recipient) {
      return gravatar.url(recipient.email, {
        protocol: 'https',
        size: 30
      });
    },
    send () {
      if (this.message == null || this.message.trim() === '' || this.message.trim() === '\n') {
        this.message = null;
        return;
      }

      this.sendWM('/app/chat.private.' + this.recipient.email, {
        content: this.message.trim()
      }).then(() => {
        var mes = {
          content: this.message,
          sender: this.user,
          recipient: this.recipient,
          created: moment().toISOString()
        };

        this.addMessage(mes);
        this.message = null;
      });
    },
    addMessage (message) {
      if (message instanceof Array) {
        this.messages = message;
      } else {
        this.messages.push(message);
      }

      this.$nextTick(() => {
        let container = this.$refs.conversationList;
        container.scrollTop = container.scrollHeight;
      });
    },
    getTimeString (timestamp) {
      return moment().isSame(timestamp, 'd') ? moment(timestamp).format('LT') : moment(timestamp).format('L LT');
    }
  }
};
</script>

<style lang="less">
.conversation-list {
  max-height: 380px;
  min-height: 380px;

  overflow-y: auto;
  padding: 10px;
  margin-bottom: 0;
}

.chat-inputbar {
  padding-left: 10px;

  input {
    border: 1px solid #ccc;
  }
}

.chat-send {
  padding-right: 10px;
}
</style>

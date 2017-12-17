<template>
  <div v-loading="loadingMessages || loadingStomp">
    <div v-if="messages.length === 0" class="text-muted empty-conversations">
      There are any messages. Be first to start conversation !
    </div>
    <ul class="conversation-list" ref="conversationList" v-else>
      <li class="clearfix" v-for="message in messages" :class="{ 'odd' : message.sender.email === user.email }" :key="message.id">
        <div class="chat-avatar">
          <profile-picture :user="message.sender" />
        </div>
        <div class="conversation-text">
          <div class="ctext-wrap">
            <i v-if="message.sender.email != user.email">{{ message.sender.firstName }}</i>
            <p v-text="message.content"></p>
            <small>{{ getTimeString(message.created) }}</small>
          </div>
        </div>
      </li>
    </ul>

    <form class="form conversation-composer" @submit.prevent="send">
      <div class="input-group">
        <input type="text" class="form-control" placeholder="Type something" v-model.trim="message">
        <span class="input-group-btn">
          <button type="button" class="btn waves-effect waves-light btn-default" @click="send">
            <i class="fa fa-paper-plane"></i>            
          </button>
        </span>
      </div>
    </form>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import moment from 'moment';
import ProfilePicture from './ProfilePicture';

export default {
  name: 'chat',
  props: {
    type: String,
    recipient: Object
  },
  data () {
    return {
      messages: [],
      message: null,
      loadingMessages: false,
      loadingStomp: false,
      subscription: null
    };
  },
  componnets: {
    ProfilePicture
  },
  computed: {
    ...mapGetters(['user', 'authenticated'])
  },
  watch: {
    'recipient': 'initialize'
  },
  mounted () {
    if (this.authenticated) {
      this.initialize();
    }
  },
  beforeDestroy () {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  },
  methods: {
    initialize () {
      this.getMessages();
      this.loadingStomp = true;
      this.connectWM('/stomp').then(() => {
        this.subscription = this.$stompClient.subscribe(`/topic/${this.type}/${this.recipient.id}/chat`, response => {
          let message = JSON.parse(response.body);
          if (message.sender.id !== this.user.id) {
            this.addMessage(message);
          }
        });

        this.loadingStomp = false;
      });
    },
    getMessages () {
      this.loadingMessages = true;
      this.messages = [];
      this.$http.get(`/api/message/${this.type}/${this.recipient.id}/0`).then(response => {
        let messages = response.body;
        messages.sort((a, b) => {
          return a.created - b.created;
        });

        this.addMessage(messages);
        this.loadingMessages = false;
      });
    },
    send () {
      if (this.message == null || this.message.trim() === '' || this.message.trim() === '\n') {
        this.message = null;
        return;
      }

      this.sendWM(`/app/chat.${this.type}.${this.recipient.id}`, {
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
        if (container) {
          container.scrollTop = container.scrollHeight;
        }
      });
    },
    getTimeString (timestamp) {
      return moment().isSame(timestamp, 'd') ? moment(timestamp).format('LT') : moment(timestamp).format('L LT');
    }
  }
};
</script>

<style lang="less">
.empty-conversations {
  text-align: center;
  padding: 20px 10px;
}

.conversation-list {
  &::-webkit-scrollbar { 
    width: 0 !important 
  }

  max-height: 380px;
  min-height: 380px;

  overflow-y: auto;
}
</style>

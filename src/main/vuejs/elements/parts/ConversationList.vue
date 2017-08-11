<template>
  <div>
    <ul class="conversation-list">
      <li class="clearfix" v-for="message in messages" :class="{ 'odd' : message.sender.id == current.id }" :key="message.id">
        <div class="chat-avatar">
          <img :src="getAvatar(message.sender)" alt="male">
          <i>{{ message.created | moment('LT') }}</i>
        </div>
        <div class="conversation-text">
          <div class="ctext-wrap">
            <p v-text="message.content"></p>
          </div>
        </div>
      </li>
    </ul>
  
    <div class="input-group">
      <input type="email" id="example-input2-group2" name="example-input2-group2" class="form-control" placeholder="Type something">
      <span class="input-group-btn">
        <button type="button" class="btn waves-effect waves-light btn-primary">Send</button>
      </span>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import gravatar from 'gravatar';
export default {
  name: 'conversation',
  props: {
    user: Object
  },
  data () {
    return {
      messages: []
    };
  },
  computed: {
    ...mapGetters({
      current: 'getUser'
    })
  },
  watch: {
    'user': 'getMessages'
  },
  methods: {
    getMessages () {
      this.$http.get('api/message/user/' + this.user.id + '/0').then(response => {
        this.messages = response.body;
      });
    },
    getAvatar (user) {
      return gravatar.url(user.email, {
        protocol: 'https',
        size: 30
      });
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

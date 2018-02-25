<template>
  <div v-loading="loadingMessages || loadingStomp">
    <div 
      v-if="messages.length === 0" 
      class="text-muted empty-conversations">
      There are any messages. Be first to start conversation !
    </div>
    <ul 
      class="conversation-list" 
      ref="chat"
      v-else>

      <li 
        class="clearfix" 
        v-for="message in messages" 
        :class="{ 'odd' : message.sender.email === user.email }" 
        :key="message.id">

        <div class="chat-avatar">
          <!-- <profile-picture :user="message.sender" /> -->
        </div>
       
        <div class="conversation-text">
          <div class="ctext-wrap">
            <i v-if="message.sender.email != user.email">
              {{ message.sender.firstName }}
            </i>
            <p v-text="message.content"></p>
            <small>{{ getTimeString(message.created) }}</small>
          </div>
        </div>
      </li>
    </ul>

    <div class="form conversation-composer">
      <div class="input-group">
        <input 
          type="text" 
          class="form-control" 
          placeholder="Type something"
          ref="messageInput"
          v-stream:keydown="messageInput$">

        <span class="input-group-btn">
          <button 
            type="submit" 
            class="btn waves-effect waves-light btn-default" 
            v-stream:click="messageSend$">

            <i class="fa fa-paper-plane"></i>            
          </button>
        </span>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import ProfilePicture from '../ProfilePicture';
import { DateTime } from 'luxon';
import { Observable, Subject } from 'rxjs';

export default {
  name: 'conversation',
  props: {
    recipient: {
      type: Object,
      default () {
        return null;
      }
    }
  },
  data () {
    return {
      loadingMessages: false,
      loadingStomp: false,
      subscription: null
    };
  },
  components: {
    ProfilePicture
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
  },
  updated () {
    let container = this.$refs.chat;
    if (container) {
      container.scrollTop = container.scrollHeight;
    }
  },
  subscriptions () {
    this.messageSend$ = new Subject()
      .map(() => {
        return {
          content: this.$refs.messageInput != null ? this.$refs.messageInput.value.trim() : ''
        };
      });

    this.messageInput$ = new Subject();
    this.messageInput$
      .pluck('event')
      .filter(e => [13].includes(e.keyCode))
      .do(e => e.preventDefault())
      .pluck('target')
      .map(t => {
        const message = {
          content: t.value.trim()
        };
        t.value = '';
        return message;
      })
      .merge(this.messageSend$)
      .flatMap(message => Observable.fromPromise(this.sendWM(`/app/chat.private.${this.recipient.email}`, message)))
      .subscribe(console.log);

    const onCreate$ = Observable.merge(
      this.$eventToObservable('hook:created'),
      this.$watchAsObservable('recipient').filter((newVal, oldVal) => newVal.id !== oldVal.id)
    );

    return {
      messages: onCreate$
        .do(() => { this.loadingMessages = true; })
        .flatMap(() => Observable.fromPromise(this.$http.get(`/api/v1/message/user/${this.recipient.id}/0`)))
        .do(() => { this.loadingMessages = false; })
        .pluck('body')
        // .flatMap(x => x)
        // .merge(websocket$)
        .scan((acc, cur) => {
          if (cur instanceof Array) {
            acc = cur;
          } else {
            acc.push(cur);
          }

          return acc;
        }, [])
        .map(m => {
          const messages = [ ...m ];
          messages.sort((a, b) => {
            return a.created - b.created;
          });
          return messages;
        })
        .startWith([])
    };
  },
  methods: {
    getTimeString (timestamp) {
      const dateTime = DateTime.fromMillis(timestamp);
      const format = DateTime.local().day === dateTime.day ? 'T' : 'F';
      return dateTime.toFormat(format);
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
</style>

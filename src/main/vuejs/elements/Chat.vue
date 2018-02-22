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
import { DateTime } from 'luxon';
import ProfilePicture from './ProfilePicture';
import { Observable, Subject } from 'rxjs';

export default {
  name: 'chat',
  props: {
    type: String,
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
  componnets: {
    ProfilePicture
  },
  computed: {
    ...mapGetters(['user', 'authenticated'])
  },
  beforeDestroy () {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
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
      .flatMap(message => Observable.fromPromise(this.sendWM(`/app/chat.${this.type}.${this.recipient.id}`, message)))
      .subscribe(console.log);

    const onCreate$ = this.$eventToObservable('hook:created');
    const websocket$ = Observable.merge(
        onCreate$.filter(() => this.recipient != null),
        this.$watchAsObservable('recipient')
      )
      .do(() => {
        if (this.subscription) {
          this.subscription.unsubscribe();
        }

        this.loadingStomp = true;
      })
      .flatMap(() => Observable.fromPromise(this.connectWM('/stomp')))
      .do(() => { this.loadingStomp = false; })
      .flatMap(() => Observable.create(ob => {
        this.subscription = this.$stompClient.subscribe(`/topic/${this.type}/${this.recipient.id}/chat`, response => {
          ob.next(JSON.parse(response.body));
        });
      }));

    return {
      messages: onCreate$
        .do(() => { this.loadingMessages = true; })
        .flatMap(() => Observable.fromPromise(this.$http.get(`/api/v1/message/${this.type}/${this.recipient.id}/0`)))
        .do(() => { this.loadingMessages = false; })
        .pluck('body')
        .flatMap(x => x)
        .merge(websocket$)
        .scan((acc, cur) => {
          acc.push(cur);
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

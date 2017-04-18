<template>
  <div class="app-messaging-container">
    <div class="app-messaging"
         id="collapseMessaging">
      <div class="chat-group">
        <div class="heading">Conversation</div>
        <ul class="group full-height">
          <li class="section">unread</li>
          <li class="message">
            <a data-toggle="collapse"
               href="#collapseMessaging"
               aria-expanded="false"
               aria-controls="collapseMessaging">
              <span class="badge badge-warning pull-right">10</span>
              <div class="message">
                <img class="profile"
                     src="https://placehold.it/100x100">
                <div class="content">
                  <div class="title">"Payment Confirmation.."</div>
                  <div class="description">Alan Anderson</div>
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
               data-toggle="collapse"
               href="#collapseMessaging"
               aria-expanded="false"
               aria-controls="collapseMessaging">
              <i class="fa fa-angle-left"
                 aria-hidden="true"></i>
            </a>
            Lucia Marshall <span class="badge badge-success badge-icon"><i class="fa fa-circle" aria-hidden="true"></i><span>Online</span></span>
          </div>
          <div class="action"></div>
        </div>
        <ul class="chat">
          <li class="line">
            <div class="title">24 Jun 2016</div>
          </li>
          <li v-for="mes in messages">
            <div class="message"
                 v-text="mes.content"></div>
            <div class="info">
              <div class="datetime"
                   v-text="mes.created"></div>
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
export default {
  name: 'chat',
  data: function () {
    return {
      messages: [],
      message: null,
    }
  },
  created: function () {
    this.initializeChatStomp();
    this.fetchMessages();
  },
  methods: {
    initializeChatStomp: function () {
      this.connectWM('stomp').then(frame => {
        this.$stompClient.subscribe('/queue/chat', response => {
          this.messages.push(JSON.parse(response.body));
        });
      }, frame => {
        console.log(frame);
      })
    },
    fetchMessages: function () {

    },
    sendMessage: function () {
      if (this.message == null || this.message == '') return;
      var data = {
        content: this.message,
      }

      this.sendWM('/app/chat', data).then(result => {
        this.message = null;
      });
    }
  }
}
</script>

<style lang="less">
ul.chat {
  max-height: 500px;
}
</style>
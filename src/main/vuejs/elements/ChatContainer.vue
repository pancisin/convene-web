<template>
  <div class="conversation-container">
    <transition name="fade-up" mode="out-in">
      <div class="chat-conversation" v-if="!collapsed">
        <div class="chat-header">
          <a class="">
            <i class="fa fa-angle-left fa-lg m-r-15"></i>
          </a>
          John Doe
          <a @click="collapsed = true" class="pull-right">
            <i class="fa fa-times"></i>
          </a>
        </div>
  
        <ul class="conversation-list">
          <li class="clearfix">
            <div class="chat-avatar">
              <img src="https://static1.squarespace.com/static/56ba4348b09f95db7f71a726/t/58d7f267ff7c50b172895560/1490547315597/justin.jpg" alt="male">
              <i>10:00</i>
            </div>
            <div class="conversation-text">
              <div class="ctext-wrap">
                <p>
                  Hello!
                </p>
              </div>
            </div>
          </li>
          <li class="clearfix odd">
            <div class="chat-avatar">
              <img src="https://static1.squarespace.com/static/56ba4348b09f95db7f71a726/t/58d7f267ff7c50b172895560/1490547315597/justin.jpg" alt="Female">
              <i>10:01</i>
            </div>
            <div class="conversation-text">
              <div class="ctext-wrap">
                <p>
                  Hi, How are you? What about our next meeting?
                </p>
              </div>
            </div>
          </li>
          <li class="clearfix">
            <div class="chat-avatar">
              <img src="https://static1.squarespace.com/static/56ba4348b09f95db7f71a726/t/58d7f267ff7c50b172895560/1490547315597/justin.jpg" alt="male">
              <i>10:01</i>
            </div>
            <div class="conversation-text">
              <div class="ctext-wrap">
                <p>
                  Yeah everything is fine
                </p>
              </div>
            </div>
          </li>
          <li class="clearfix odd">
            <div class="chat-avatar">
              <img src="https://static1.squarespace.com/static/56ba4348b09f95db7f71a726/t/58d7f267ff7c50b172895560/1490547315597/justin.jpg" alt="male">
              <i>10:02</i>
            </div>
            <div class="conversation-text">
              <div class="ctext-wrap">
                <p>
                  Wow that's great
                </p>
              </div>
            </div>
          </li>
        </ul>
        <div class="row">
          <div class="col-sm-9 chat-inputbar">
            <input type="text" class="form-control chat-input" placeholder="Enter your text">
          </div>
          <div class="col-sm-3 chat-send">
            <button type="submit" class="btn btn-md btn-primary btn-block waves-effect waves-light">Send</button>
          </div>
        </div>
      </div>
      <a class="btn btn-primary btn-rounded btn-chat pull-right waves-effect" @click="collapsed = !collapsed" v-else>
        <i class="fa fa-comment-o fa-lg"></i>
      </a>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'conversation-container',
  data() {
    return {
      collapsed: true,
    }
  },
  created() {

  },
  methods: {

  }
}
</script>

<style lang="less" scoped>
.conversation-container {
  position: fixed;
  right: 0;
  bottom: 0;
  z-index: 9;
  margin: 14px;

  width: 320px;

  .chat-inputbar {
    padding-left: 10px;

    input {
      border: 1px solid #ccc;
    }
  }

  .chat-send {
    padding-right: 10px;
  }

  .chat-header {
    padding: 15px;
    background: #039cfd;
    color: white;
    font-weight: bold;

    a {
      color: #fff;
    }
  }

  .conversation-list {
    // box-shadow: 0px 0px 10px 2px rgba(0, 0, 0, 0.2);
    border: 1px solid #ccc;
    border-radius: 2px;
    border-top-left-radius: 0;
    border-top-right-radius: 0;

    background: white;
    overflow-y: auto;
    padding: 10px;
  }
}

.btn-chat {
  padding: 15px 16px;
  border-radius: 30px;
}
</style>

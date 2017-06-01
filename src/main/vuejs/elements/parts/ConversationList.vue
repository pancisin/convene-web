<template>
  <div>
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
  
    <div class="input-group">
      <input type="email" id="example-input2-group2" name="example-input2-group2" class="form-control" placeholder="Type something">
      <span class="input-group-btn">
        <button type="button" class="btn waves-effect waves-light btn-primary">Send</button>
      </span>
    </div>
  </div>
</template>

<script>
export default {

}
</script>

<style lang="less">
.conversation-list {
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

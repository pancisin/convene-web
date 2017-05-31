  <template>
  <div>
  
    <div class="row">
      <div class="col-lg-4">
        <div class="panel panel-default">
          <div class="panel-heading">
            <h3 class="panel-title">Panel Default</h3>
            <p class="panel-sub-title font-13 text-muted">Sub title goes here with small font</p>
          </div>
          <div class="panel-body">
            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
          </div>
        </div>
      </div>
      <div class="col-lg-4">
        <div class="panel panel-color panel-primary">
          <div class="panel-heading">
            <h3 class="panel-title">Panel Primary</h3>
            <p class="panel-sub-title font-13 text-muted">Sub title goes here with small font</p>
          </div>
          <div class="panel-body">
            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
          </div>
        </div>
      </div>
      <div class="col-lg-4">
        <div class="panel panel-color panel-success">
          <div class="panel-heading">
            <h3 class="panel-title">Panel Success</h3>
            <p class="panel-sub-title font-13 text-muted">Sub title goes here with small font</p>
          </div>
          <div class="panel-body">
            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
          </div>
        </div>
      </div>
    </div>
  
    <div class="row">
      <div class="col-md-6">
  
        <div class="card-box">
          <h4 class="m-t-0 m-b-20 header-title">
            <b>Chat</b>
          </h4>
  
          <div class="chat-conversation">
            <ul class="conversation-list nicescroll" style="overflow: hidden; outline: none;" tabindex="5002">
              <li class="clearfix">
                <div class="chat-avatar">
                  <img src="https://static1.squarespace.com/static/56ba4348b09f95db7f71a726/t/58d7f267ff7c50b172895560/1490547315597/justin.jpg" alt="male">
                  <i>10:00</i>
                </div>
                <div class="conversation-text">
                  <div class="ctext-wrap">
                    <i>John Deo</i>
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
                    <i>Smith</i>
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
                    <i>John Deo</i>
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
                    <i>Smith</i>
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
        </div>
  
      </div>
    </div>
  </div>
</template>
  
<script>
import later from 'later'
import calendar from '../elements/Calendar.vue'

export default {
  name: 'dashboard',
  components: {
    calendar,
  },
}
</script>

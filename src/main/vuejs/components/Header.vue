 <template>
  <div class="topbar">
  
    <div class="topbar-left">
      <div class="text-center">
        <router-link to="/" class="logo">
          <i class="fa fa-book"></i>
          <span>Bookster</span>
        </router-link>
      </div>
    </div>
  
    <div class="navbar navbar-default" role="navigation">
      <div class="container">
        <div class="">
          <div class="pull-left">
            <button class="button-menu-mobile open-left waves-effect">
              <i class="md md-menu"></i>
            </button>
            <span class="clearfix"></span>
          </div>
  
          <ul class="nav navbar-nav navbar-right pull-right">
            <li class="dropdown hidden-xs" v-click-outside="closeNotifications">
              <a @click="display.notifications = !display.notifications" class="dropdown-toggle waves-effect waves-light">
                <i class="fa fa-bell-o"></i>
                <span class="badge badge-xs" v-text="notifications.length"></span>
              </a>
              <ul class="dropdown-menu dropdown-menu-lg" v-show="display.notifications">
                <li class="text-center notifi-title">Notification</li>
                <li class="list-group notification-list">
  
                  <a v-for="not in notifications" class="list-group-item">
                    <div class="media">
                      <div class="pull-left p-r-10">
                        <em class="fa fa-diamond noti-primary"></em>
                      </div>
                      <div class="media-body">
                        <h5 class="media-heading" v-text="not.title"></h5>
                        <p class="m-0">
                          <small v-text="not.message"></small>
                        </p>
                      </div>
                    </div>
                  </a>
  
                  <div v-if="notifications.length == 0" class="text-center m-t-10 text-muted">There's nothing to display</div>
  
                </li>
  
                <li>
                  <a href="javascript:void(0);" class=" text-right">
                    <small>
                      <b>See all notifications</b>
                    </small>
                  </a>
                </li>
  
              </ul>
            </li>
            <li class="hidden-xs">
              <a href="#" class="right-bar-toggle waves-effect waves-light">
                <i class="material-icons">settings</i>
              </a>
            </li>
  
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script>
import Auth from '../services/auth.js'
import moment from "moment"

export default {
  name: 'header',
  data: function () {
    return {
      display: {
        notifications: false,
      }
    }
  },
  computed: {
    notificationsClass: function () {
      if (this.notifications.length > 0)
        return "warning";
      else
        return "";
    },
    notifications() {
      return this.$store.state.notifications;
    }
  },
  created: function () {
    // this.fetchNotifications();
    // this.initializeStomp();
  },
  methods: {
    logout: function () {
      Auth.logout(this, '/login')
    },
    switchLanguage: function (e) {
      this.$i18n.locale = e.target.value;
    },
    initializeStomp: function () {
      this.connectWM('stomp').then(frame => {
        this.$stompClient.subscribe('/user/queue/notifier', response => {
          var notification = JSON.parse(response.body);
          this.notifications.unshift(notification);
        });
      }, frame => {
        console.log(frame);
      })
    },
    fetchNotifications: function () {
      this.$http.get('api/user/me/notifications').then(response => {
        this.notifications = response.body;
        this.notifications.sort((a, b) => {
          return moment(b.created).isAfter(a.created);
        })
      });
    },
    markAsSeen: function (notification) {
      var url = ['api/notification', notification.id, 'toggle-seen'].join('/');

      this.$http.patch(url).then(response => {
        this.notifications = this.notifications.filter(elem => {
          return elem.id != notification.id;
        })
      })
    },
    closeNotifications(e) {
      if (this.display.notifications)
        this.display.notifications = false;
    }
  },
  filters: {
    timeFromNow: function (date) {
      if (date == null) return "";
      return moment(date).fromNow();
    }
  },
}
</script>
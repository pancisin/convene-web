 <template>
  <div class="topbar">
  
    <!-- LOGO -->
    <div class="topbar-left">
      <div class="text-center">
        <a href="index.html" class="logo"><i class="md md-equalizer"></i> <span>Bookster</span> </a>
      </div>
    </div>
  
    <!-- Navbar -->
    <div class="navbar navbar-default" role="navigation">
      <div class="container">
        <div class="">
          <div class="pull-left">
            <button class="button-menu-mobile open-left waves-effect">
              <i class="md md-menu"></i>
            </button>
            <span class="clearfix"></span>
          </div>
  
          <ul class="nav navbar-nav hidden-xs">
            <li><a href="#" class="waves-effect">Files</a></li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle waves-effect" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Projects <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="#">Web design</a></li>
                <li><a href="#">Projects two</a></li>
                <li><a href="#">Graphic design</a></li>
                <li><a href="#">Projects four</a></li>
              </ul>
            </li>
          </ul>
  
          <form role="search" class="navbar-left app-search pull-left hidden-xs">
            <input type="text" placeholder="Search..." class="form-control app-search-input">
            <a href=""><i class="fa fa-search"></i></a>
          </form>
  
          <ul class="nav navbar-nav navbar-right pull-right">
  
            <li class="dropdown hidden-xs">
              <a href="#" data-target="#" class="dropdown-toggle waves-effect waves-light" data-toggle="dropdown" aria-expanded="true">
                <i class="md md-notifications"></i> <span class="badge badge-xs badge-pink">3</span>
              </a>
              <ul class="dropdown-menu dropdown-menu-lg">
                <li class="text-center notifi-title">Notification</li>
                <li class="list-group nicescroll notification-list" style="overflow: hidden; outline: none;" tabindex="5000">
                  <!-- list item-->
                  <a href="javascript:void(0);" class="list-group-item">
                    <div class="media">
                      <div class="pull-left p-r-10">
                        <em class="fa fa-diamond noti-primary"></em>
                      </div>
                      <div class="media-body">
                        <h5 class="media-heading">A new order has been placed A new
                                                                order has been placed</h5>
                        <p class="m-0">
                          <small>There are new settings available</small>
                        </p>
                      </div>
                    </div>
                  </a>
  
                  <!-- list item-->
                  <a href="javascript:void(0);" class="list-group-item">
                    <div class="media">
                      <div class="pull-left p-r-10">
                        <em class="fa fa-cog noti-warning"></em>
                      </div>
                      <div class="media-body">
                        <h5 class="media-heading">New settings</h5>
                        <p class="m-0">
                          <small>There are new settings available</small>
                        </p>
                      </div>
                    </div>
                  </a>
  
                  <!-- list item-->
                  <a href="javascript:void(0);" class="list-group-item">
                    <div class="media">
                      <div class="pull-left p-r-10">
                        <em class="fa fa-bell-o noti-success"></em>
                      </div>
                      <div class="media-body">
                        <h5 class="media-heading">Updates</h5>
                        <p class="m-0">
                          <small>There are <span class="text-primary">2</span> new
                                                                    updates available
                                                                </small>
                        </p>
                      </div>
                    </div>
                  </a>
  
                </li>
  
                <li>
                  <a href="javascript:void(0);" class=" text-right">
                    <small><b>See all notifications</b></small>
                  </a>
                </li>
  
                <div id="ascrail2000" class="nicescroll-rails" style="width: 8px; z-index: 1000; cursor: default; position: absolute; top: 0px; left: -8px; height: 0px; display: none;">
                  <div style="position: relative; top: 0px; float: right; width: 6px; height: 0px; background-color: rgb(152, 166, 173); border: 1px solid rgb(255, 255, 255); background-clip: padding-box; border-radius: 5px;"></div>
                </div>
                <div id="ascrail2000-hr" class="nicescroll-rails" style="height: 8px; z-index: 1000; top: -8px; left: 0px; position: absolute; cursor: default; display: none;">
                  <div style="position: relative; top: 0px; height: 6px; width: 0px; background-color: rgb(152, 166, 173); border: 1px solid rgb(255, 255, 255); background-clip: padding-box; border-radius: 5px;"></div>
                </div>
              </ul>
            </li>
            <li class="hidden-xs">
              <a href="#" class="right-bar-toggle waves-effect waves-light"><i class="md md-settings"></i></a>
            </li>
  
          </ul>
        </div>
        <!--/.nav-collapse -->
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
      notifications: [],
    }
  },
  computed: {
    notificationsClass: function () {
      if (this.notifications.length > 0)
        return "warning";
      else
        return "";
    }
  },
  created: function () {
    this.fetchNotifications();
    this.initializeStomp();
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
    }
  },
  filters: {
    timeFromNow: function (date) {
      if (date == null) return "";
      return moment(date).fromNow();
    }
  }
}
</script>

<style lang="less">
.navbar {
  height: auto;
  border: 0;
  padding: 0;
}

.navbar .navbar-collapse .navbar-nav {
  height: auto;
}

.navbar .navbar-collapse .navbar-nav>li.navbar-title {
  height: 60px;
}
</style>

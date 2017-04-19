 <template>
  <nav class="navbar navbar-default"
       id="navbar">
    <div class="navbar-collapse collapse in">
      <ul class="nav navbar-nav navbar-mobile">
        <li>
          <button type="button"
                  class="sidebar-toggle">
            <i class="fa fa-bars"></i>
          </button>
        </li>
        <li class="logo">
          <a class="navbar-brand"
             href="#">
            <span class="highlight">Flat v3</span> Admin
          </a>
        </li>
        <li>
          <button type="button"
                  class="navbar-toggle">
          </button>
        </li>
      </ul>
      <ul class="nav navbar-nav navbar-left">
        <li class="navbar-title"
            v-if="$store.state.user != null">
          <span class="highlight"
                v-text="$store.state.user.company.name"></span>
        </li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <li>
          <select @change="switchLanguage"
                  class="form-control">
            <option value="en">English</option>
            <option value="sk">Slovak</option>
          </select>
        </li>
  
        <li class="dropdown notification"
            :class="notificationsClass">
          <a href="#"
             class="dropdown-toggle"
             data-toggle="dropdown">
            <div class="icon"><i class="fa fa-bell"
                 aria-hidden="true"></i></div>
            <div class="title">System Notifications</div>
            <div class="count"
                 v-text="notifications.length"></div>
          </a>
          <div class="dropdown-menu">
            <ul is="transition-group"
                name="fade-down">
              <li class="dropdown-header"
                  key="0">{{ $tc('notification.default', 2) }}</li>
              <li v-for="notification in notifications"
                  :key="notification.id">
                <a>
                  <span class="pull-right"
                        @click="markAsSeen(notification)"><i class="fa fa-check" aria-hidden="true"></i></span>
                  <div class="message">
                    <div class="content">
                      <div class="title"
                           v-text="notification.title"></div>
                      <div class="description">{{ notification.created | timeFromNow }}</div>
                    </div>
                  </div>
                </a>
              </li>
            </ul>
          </div>
        </li>
  
        <li class="dropdown profile"
            v-if="$store.state.user != null">
          <a href="javascript:;"
             class="dropdown-toggle"
             data-toggle="dropdown">
            <div class="icon"><i class="fa fa-cog fa-2x"
                 aria-hidden="true"></i></div>
            <div class="title">{{ $t('settings.default') }}</div>
          </a>
  
          <div class="dropdown-menu">
            <div class="profile-info">
              <h4 class="username">{{ $store.state.user.firstName }} {{ $store.state.user.lastName }} <br> <small v-text="$store.state.user.company.name"></small></h4>
            </div>
            <ul class="action">
              <!--<li><router-link to="/licenses"> License & Billing </router-link></li>-->
              <li>
                <router-link to="/account">{{ $t('settings.account') }}</router-link>
              </li>
              <li>
                <router-link to="/company">{{ $t('settings.company') }}</router-link>
              </li>
              <li>
                <a href="javascript:;"
                   @click="logout">{{ $t('settings.logout' )}}</a>
              </li>
            </ul>
          </div>
        </li>
      </ul>
    </div>
  </nav>
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
        this.$stompClient.subscribe('/queue/notifications', response => {
          var notification = JSON.parse(response.body);
          this.notifications.unshift(notification);
        });
      }, frame => {
        console.log(frame);
      })
    },
    fetchNotifications: function () {
      var url = ['api/company', this.$store.getters.company_id, 'notifications'].join('/');

      this.$http.get(url).then(response => {
        this.notifications = response.body;
        this.notifications.sort((a, b) => {
          return moment(b.created).isAfter(a.created);
        })
      });
    },
    markAsSeen: function (notification) {
      this.notifications = this.notifications.filter(elem => {
        return elem.id != notification.id;
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

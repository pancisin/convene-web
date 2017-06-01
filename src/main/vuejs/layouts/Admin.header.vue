 <template>
  <header id="topnav">
    <div class="topbar-main">
      <div class="container">
  
        <div class="logo">
          <router-link to="/" class="logo">
            <i class="fa fa-book"></i>
            <span>Bookster</span>
            <sup v-if="$store.state.user.license != null">
              {{ $t($store.state.user.license.subscription.code) }}
            </sup>
          </router-link>
        </div>
  
        <div class="menu-extras">
          <ul class="nav navbar-nav navbar-right pull-right">
            <lang-switcher />
            <notifications />
  
            <li class="dropdown">
              <a href="" class="dropdown-toggle waves-effect waves-light profile" data-toggle="dropdown" aria-expanded="true">
                <img src="https://static1.squarespace.com/static/56ba4348b09f95db7f71a726/t/58d7f267ff7c50b172895560/1490547315597/justin.jpg" alt="user-img" class="img-circle">
              </a>
              <ul class="dropdown-menu">
                <li>
                  <router-link to="/admin/settings" class="waves-effect waves-light">
                    <i class="fa fa-cog m-r-10"></i> Settings
                  </router-link>
                </li>
                <li>
                  <a @click="logout">
                    <i class="fa fa-power-off m-r-10"></i> Logout</a>
                </li>
              </ul>
            </li>
          </ul>
  
          <div class="menu-item">
            <a class="navbar-toggle">
              <div class="lines">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </a>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>
  
<script>
import Auth from '../services/auth.js'
import Notifications from '../elements/Notifications.vue'
import LangSwitcher from '../elements/LangSwitcher.vue'

export default {
  name: 'header',
  methods: {
    logout: function () {
      Auth.logout(this, '/login')
    },
  },
  components: {
    Notifications, LangSwitcher
  },
}
</script>
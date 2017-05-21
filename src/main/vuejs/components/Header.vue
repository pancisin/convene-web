 <template>
  <div class="topbar">
  
    <div class="topbar-left">
      <div class="text-center">
        <router-link to="/" class="logo">
          <i class="fa fa-book"></i>
          <span>Bookster</span> <small class="text-muted" v-text="$store.state.user.license.subscription.name" v-if="$store.state.user.license != null"></small>
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
            <notifications />
            <lang-switcher />
            <li class="hidden-xs">
              <a class="right-bar-toggle waves-effect waves-light">
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
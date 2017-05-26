 <template>
  <div class="topbar">
  
    <div class="topbar-left">
      <div class="text-center">
        <router-link to="/" class="logo">
          <i class="fa fa-book"></i>
          <span>Bookster</span>
          <sup class="text-muted" v-if="$store.state.user.license != null">
            {{ $t($store.state.user.license.subscription.code) }}
          </sup>
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
              <router-link to="/admin/settings" class="waves-effect waves-light">
                <i class="material-icons">settings</i>
              </router-link>
              <!--<a class="right-bar-toggle waves-effect waves-light"></a>-->
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
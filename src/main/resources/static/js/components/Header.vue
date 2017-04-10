 <template>
  <nav class="navbar navbar-default"
       id="navbar">
    <div class="container-fluid">
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
          <select @change="switchLanguage">
            <option value="en">English</option>
            <option value="sk">Slovak</option>
          </select>
  
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
    </div>
  </nav>
</template>
  
<script>
import Auth from '../services/auth.js';
export default {
  name: 'header',
  methods: {
    logout: function () {
      Auth.logout(this, '/login')
    },
    switchLanguage: function (e) {
      this.$i18n.locale = e.target.value;
    }
  }
}
</script>

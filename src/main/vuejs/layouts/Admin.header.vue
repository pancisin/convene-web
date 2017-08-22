 <template>
  <header id="topnav">
    <div class="topbar-main">
      <div class="container">
        <div class="menu-item">
          <a class="navbar-toggle waves-effect" @click="hamburgerClicked">
            <div class="lines">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </a>
        </div>
  
        <div class="logo">
          <router-link to="/" class="logo">
            <i class="fa fa-book"></i>
            <span>Bookster</span>
          </router-link>
        </div>
  
        <div class="menu-extras">
          <ul class="nav navbar-nav navbar-right pull-right">
            <lang-switcher />
            <notifications />
  
            <li class="dropdown">
              <a href="" class="dropdown-toggle waves-effect waves-light profile" data-toggle="dropdown" aria-expanded="true">
                <img :src="avatar" alt="user-img" class="img-circle">
              </a>
              <ul class="dropdown-menu">
                <li>
                  <router-link :to="{ name: 'settings' }" class="waves-effect waves-light">
                    <i class="fa fa-cog m-r-10"></i> Settings
                  </router-link>
                </li>
                <li>
                  <a @click="logoutUser">
                    <i class="fa fa-power-off m-r-10"></i> Logout</a>
                </li>
              </ul>
            </li>
          </ul>
  
        </div>
      </div>
    </div>
  </header>
</template>
  
<script>
import Notifications from '../elements/Notifications.vue';
import LangSwitcher from '../elements/LangSwitcher.vue';
import { mapGetters, mapActions } from 'vuex';
import gravatar from 'gravatar';

export default {
  name: 'header',
  methods: {
    ...mapActions(['logout']),
    hamburgerClicked () {
      this.$emit('hamburgerClicked');
    },
    logoutUser () {
      this.logout().then(response => {
        this.$router.push({ path: '/' });
      })
    }
  },
  computed: {
    ...mapGetters(['user']),
    avatar () {
      if (this.user.email != null) {
        return gravatar.url(this.user.email, {
          protocol: 'https',
          size: 36
        });
      } else return 'https://upload.wikimedia.org/wikipedia/en/b/b1/Portrait_placeholder.png';
    }
  },
  components: {
    Notifications, LangSwitcher
  }
};
</script>

<style lang="less" scoped>
#topnav .navbar-toggle {
  float: left;
  margin-right: 10px;
}
</style>

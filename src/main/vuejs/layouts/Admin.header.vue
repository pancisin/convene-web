 <template>
  <header id="topnav">
    <div class="topbar-main">
      <div class="container">
        <div class="menu-item">
          <a class="navbar-toggle waves-effect"
            @click="hamburgerClicked">
            <div class="lines">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </a>
        </div>

        <div class="logo">
          <router-link to="/"
            class="logo">
            <i class="fa fa-book"></i>
            <span>Bookster</span>
          </router-link>
        </div>

        <div class="menu-extras">
          <ul class="nav navbar-nav navbar-right pull-right">
            <lang-switcher />
            <notifications />
            <user-profile />
          </ul>
        </div>
      </div>
    </div>
  </header>
</template>
  
<script>
import { Notifications, LangSwitcher, UserProfile } from 'elements';

export default {
  name: 'header',
  components: {
    Notifications, LangSwitcher, UserProfile
  },
  methods: {
    hamburgerClicked () {
      this.$emit('hamburgerClicked');
    }
  }
};
</script>

<style lang="less" scoped>
#topnav .navbar-toggle {
  float: left;
  margin-right: 10px;
}
</style>

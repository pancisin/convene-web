 <template>
  <header id="topnav">
    <div class="topbar-main bg-inverse">
      <div class="container-fluid">
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

        <product-logo inline light>
        </product-logo>

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
import { Notifications, LangSwitcher, UserProfile, ProductLogo } from 'elements';

export default {
  name: 'header',
  components: {
    Notifications, LangSwitcher, UserProfile, ProductLogo
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

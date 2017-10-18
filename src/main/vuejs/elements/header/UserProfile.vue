<template>
  <drop-down-menu>
    <span slot="button"
      class="profile">
      <img :src="avatar"
        alt="user-img"
        class="img-circle">
    </span>

    <drop-down-menu-item header>
      User profile
    </drop-down-menu-item>
    <drop-down-menu-item>
      <router-link :to="{ name: 'settings' }"
        class="waves-effect waves-light">
        <i class="fa fa-cog m-r-10"></i> Settings
      </router-link>
    </drop-down-menu-item>
    <drop-down-menu-item divider>
    </drop-down-menu-item>
    <drop-down-menu-item>
      <a @click="logoutUser">
        <i class="fa fa-power-off m-r-10"></i> Logout
      </a>
    </drop-down-menu-item>
  </drop-down-menu>
</template>

<script>
import { DropDownMenu, DropDownMenuItem } from 'elements';
import { mapGetters, mapActions } from 'vuex';
import gravatar from 'gravatar';

export default {
  name: 'user-profile',
  data () {
    return {

    };
  },
  components: {
    'drop-down-menu': DropDownMenu,
    'drop-down-menu-item': DropDownMenuItem
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
  methods: {
    ...mapActions(['logout']),
    logoutUser () {
      this.logout().then(response => {
        this.$router.push({ path: '/' });
      });
    }
  }
};
</script>

<style>

</style>

<template>
  <dropdown-menu>
    <span slot="button"
      class="profile">
      <img :src="avatar"
        alt="user-img"
        class="img-circle">
    </span>

    <dropdown-menu-item header>
      User profile
    </dropdown-menu-item>
    <dropdown-menu-item>
      <router-link :to="{ name: 'settings' }"
        class="waves-effect waves-light">
        <i class="fa fa-cog m-r-10"></i> Settings
      </router-link>
    </dropdown-menu-item>
    <dropdown-menu-item divider>
    </dropdown-menu-item>
    <dropdown-menu-item>
      <a @click="logoutUser">
        <i class="fa fa-power-off m-r-10"></i> Logout
      </a>
    </dropdown-menu-item>
  </dropdown-menu>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import gravatar from 'gravatar';
import DropdownMenu from '../DropdownMenu';
import DropdownMenuItem from '../DropdownMenuItem';

export default {
  name: 'user-profile',
  data () {
    return {

    };
  },
  components: {
    DropdownMenu, DropdownMenuItem
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

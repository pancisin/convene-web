<template>
  <dropdown-menu>
    <span slot="button"
      class="profile">
      <profile-picture :user="user" class="img-circle" />
    </span>

    <dropdown-menu-item header>
      User profile
    </dropdown-menu-item>
    <dropdown-menu-item>
      <router-link :to="{ name: 'user', params: { user_id: user.id } }"
        class="waves-effect waves-light">
        <i class="fa fa-user m-r-10"></i> Profile
      </router-link>
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
import DropdownMenu from '../DropdownMenu';
import DropdownMenuItem from '../DropdownMenuItem';
import ProfilePicture from '../ProfilePicture';

export default {
  name: 'user-profile',
  components: {
    DropdownMenu,
    DropdownMenuItem,
    ProfilePicture
  },
  computed: {
    ...mapGetters(['user'])
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

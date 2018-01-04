<template>
  <dropdown-menu large>
    <span slot="button"
      class="profile">
      <profile-picture :user="user" class="img-circle" />
    </span>

    <dropdown-menu-item header>
      User profile
    </dropdown-menu-item>
    <dropdown-menu-item class="user-profile-dropdown">
      <div class="user-picture">
        <profile-picture :user="user" style="width: 100%" />
        <div>
          {{ user.displayName }}
        </div>
      </div>
    </dropdown-menu-item>
    <dropdown-menu-item divider />
    <dropdown-menu-item>
      <router-link :to="{ name: 'user', params: { user_id: user.id } }"
        class="waves-effect waves-light">
        Profile
      </router-link>
    </dropdown-menu-item>
    <dropdown-menu-item>
      <router-link :to="{ name: 'settings' }"
        class="waves-effect waves-light">
        Settings
      </router-link>
    </dropdown-menu-item>
    <dropdown-menu-item divider>
    </dropdown-menu-item>
    <dropdown-menu-item>
      <a @click="logoutUser">
        Logout
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

<style lang="less">
@color-profile: darken(#e5e5e5, 10%);
.user-profile-dropdown {
  .user-picture {
    height: 150px;
    overflow: hidden;
    position: relative;

    img {
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
    }

    div {
      background: linear-gradient(
        to bottom,
        fade(@color-profile, 0%) 0%,
        fade(@color-profile, 60%) 40%,
        @color-profile 100%
      );
      position: absolute;
      padding: 20px 20px;
      font-size: 21px;
      width: 100%;
      bottom: 0;
    }
  }
}
</style>

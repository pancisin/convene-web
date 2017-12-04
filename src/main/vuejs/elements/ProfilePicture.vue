<template>
  <img 
    :src="src" 
    :alt="user.displayName">
</template>

<script>
import ProfilePicturePlaceholder from 'assets/img/profile_picture_placeholder.png';

export default {
  props: {
    user: {
      type: Object,
      default () {
        return {};
      }
    }
  },
  computed: {
    src () {
      return this.user.profilePicture
        ? this.user.profilePicture.path
        : ProfilePicturePlaceholder;
    }
  }
};
</script>
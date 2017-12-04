<template>
  <img :src="src" alt="">
</template>

<script>
import ProfilePicturePlaceholder from 'assets/img/profile_picture_placeholder.png';
import gravatar from 'gravatar';

export default {
  props: {
    user: {
      type: Object,
      default () {
        return {};
      }
    },
    gravatar: Boolean
  },
  computed: {
    src () {
      return this.user.profilePicture
        ? this.user.profilePicture.path
        : ProfilePicturePlaceholder;
    }
  },
  methods: {
    getAvatar (recipient) {
      return gravatar.url(this.user.email, {
        protocol: 'https',
        size: 30
      });
    }
  }
};
</script>
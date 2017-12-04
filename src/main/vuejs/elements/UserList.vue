<template>
  <div class="user-list">
    <div v-for="(user, index) in users" :key="index" v-if="users.length > 0" @click="select(user)">
      <profile-picture :user="user" class="user-list-img img-circle" />

      <div class="user-list-content">
        {{ user.displayName }}

        <small class="text-muted">
         {{ user.email }}
        </small>
      </div>
      
    </div>
    <span v-else>
      There's nothing to display
    </span>
  </div>
</template>

<script>
import ProfilePicture from './ProfilePicture';
export default {
  props: {
    users: Array
  },
  components: {
    ProfilePicture
  },
  methods: {
    select (user) {
      this.$emit('select', user);
    }
  }
};
</script>

<style lang="less">
.user-list {
  padding: 15px;

  & > div {
    padding: 10px 0;

    & > * {
      vertical-align: middle;
    }

    .user-list-img {
      margin-right: 15px;
      width: 40px;
      height: 40px;
    }

    .user-list-content {
      display: inline-flex;
      flex-direction: column;
    }

    & ~ div {
      border-top: 1px solid #f1f1f1;
    }
  }
}
</style>

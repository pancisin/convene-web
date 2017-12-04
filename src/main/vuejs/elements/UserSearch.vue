<template>
  <div class="suggest-input"
    :class="{ '_loading' : loading }">
    <input class="form-control"
      @keyup="search($event); update($event.target.value)"
      v-model="selected"
      @focus="collapsed = false"
      @blur="collapsed = true" />

    <div class="loader">
      <div class="spinner"></div>
    </div>

    <transition name="fade-up">
      <div class="suggest-box"
        v-show="!collapsed">
        <user-list :users="users" @select="select" />
      </div>
    </transition>
  </div>
</template>

<script>
import debounce from 'debounce';
import UserApi from 'api/user.api';
import ProfilePicture from './ProfilePicture';

import UserList from './UserList';

export default {
  name: 'user-search',
  props: {
    value: String,
    options: Array
  },
  watch: {
    value (v) {
      this.selected = v;
    }
  },
  components: {
    ProfilePicture,
    UserList
  },
  data () {
    return {
      loading: false,
      selected: null,
      collapsed: true,
      users: []
    };
  },
  methods: {
    search: debounce(function (e) {
      this.collapsed = false;
      this.loading = true;

      UserApi.searchUsers(e.target.value, users => {
        this.users = users;
        this.loading = false;
      });
    }, 500),
    select (option) {
      this.collapsed = true;
      this.selected = option.firstName + ' ' + option.lastName;
      this.update(option.email);
    },
    update (value) {
      this.$emit('input', value);
    }
  }
};
</script>

<style lang="less">
.suggest-input {
  position: relative;

  .loader {
    z-index: 3;
    right: 5px;
    width: auto;
    margin: 5px;

    .spinner {
      width: 25px;
      height: 25px;
    }
  }

  .suggest-box {
    position: absolute;
    background: #fff;
    width: 100%;
    border: 1px solid #ccc;
    max-height: 350px;
    overflow-y: auto;
    left: 0;
    top: 34px;

    .inbox-item {
      padding-left: 10px;
      padding-right: 10px;
    }
  }
}
</style>

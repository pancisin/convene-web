<template>
  <div class="contact-list">
    <ul class="list-group contacts-list">
      <li class="list-group-item" v-for="user in contacts" :key="user.id">
        <a @click="selectUser(user)">
          <div class="avatar">
            <img :src="getAvatar(user)" alt="">
          </div>
          <span class="name" v-text="user.displayName"></span>
          <i class="fa fa-circle" :class="{ 'online' : isOnline(user.email) }"></i>
        </a>
        <span class="clearfix"></span>
      </li>
    </ul>
  </div>
</template>

<script>
import gravatar from 'gravatar';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'contacts-list',
  data () {
    return {
      activeUsers: []
    };
  },
  created () {
    this.initializeContacts();
    this.$parent.$on('activityChanged', (userNames) => {
      this.activeUsers = userNames;
    });
  },
  computed: {
    ...mapGetters(['contacts'])
  },
  methods: {
    ...mapActions(['initializeContacts']),
    selectUser (user) {
      this.$emit('selected', user);
    },
    getAvatar (user) {
      return gravatar.url(user.email, {
        protocol: 'https',
        size: 30
      });
    },
    isOnline (email) {
      return this.activeUsers.indexOf(email) !== -1;
    }
  }
};
</script>

<style lang="less" scoped>
.conversation-container .contact-list {
  max-height: 380px;
  min-height: 380px;

  overflow-y: auto;
  // padding: 10px;
}

.contact-list {
  max-height: 600px;
  .list-group-item {
    border: none;
    &:hover {
      background: #f5f5f5;
    }
  }
  i.offline {
    color: #ef5350;
  }
  i.away {
    color: #ffaa00;
  }
}

.contacts-list {
  .avatar {
    display: inline-block;
    margin-right: 5px;
    width: 30px;
    vertical-align: middle;
    img {
      border-radius: 50%;
      width: 100%;
    }
  }
  .list-group-item {
    padding: 15px 20px;
    span.name {
      color: #707780;
      display: inline-block;
      overflow: hidden;
      padding-left: 5px;
      text-overflow: ellipsis;
      white-space: nowrap;
      width: 130px;
      vertical-align: middle;
    }
  }
  i.online {
    color: #a0d269;
  }
  i {
    color: #dddddd;
    float: right;
    font-size: 9px;
    line-height: 30px;
  }
}
</style>

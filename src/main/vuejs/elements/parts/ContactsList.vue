<template>
  <div class="contact-list">
    <div class="contact-list-separator">
      Recent conversations
    </div>
    <ul class="list-group contacts-list">
      <li class="list-group-item" v-for="(conversation, index) in conversations" :key="index">
        <a @click="selectUser(conversation.participant)">
          <div class="avatar">
            <img :src="getAvatar(conversation.participant)" alt="">
          </div>
          <div class="content">
            <span class="name">
              {{ conversation.participant.displayName }}
            </span>
            <br >
            <small class="text-muted">
              {{ conversation.recentMessages[0].content }}
            </small>
          </div>
            <i class="fa fa-circle" :class="{ 'online' : conversation.participant.active }"></i>
        </a>
        <span class="clearfix"></span>
      </li>
    </ul>

    <div class="contact-list-separator">
      Contacts
    </div>
    <ul class="list-group contacts-list">
      <li class="list-group-item" v-for="user in contacts" :key="user.id">
        <a @click="selectUser(user)">
          <div class="avatar">
            <img :src="getAvatar(user)" alt="">
          </div>
          <span class="name" v-text="user.displayName"></span>
          <i class="fa fa-circle" :class="{ 'online' : user.active }"></i>
        </a>
        <span class="clearfix"></span>
      </li>
    </ul>
  </div>
</template>

<script>
import gravatar from 'gravatar';
import { mapGetters } from 'vuex';

export default {
  name: 'contacts-list',
  computed: {
    ...mapGetters(['contacts', 'conversations'])
  },
  methods: {
    selectUser (user) {
      this.$emit('selected', user);
    },
    getAvatar (user) {
      return gravatar.url(user.email, {
        protocol: 'https',
        size: 30
      });
    }
  }
};
</script>

<style lang="less" scoped>
@import (reference) '~less/variables.less';
@color-dark: #707780;

.conversation-container .contact-list {
  max-height: 380px;
  min-height: 380px;

  overflow-y: auto;
  // padding: 10px;
}

.contact-list {
  max-height: 600px;

  .contact-list-separator {
    padding: 15px 20px;
    border-bottom: 1px solid #eee;
    color: @color-dark;
  }

  .list-group-item {
    border: none;
    &:hover {
      background: @color-light;
    }
  }
  i.offline {
    color: @color-danger;
  }
  i.away {
    color: @color-warning;
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
  .content {
    display: inline-block;
    vertical-align: middle;
    padding-left: 5px;
  }
  .list-group-item {
    padding: 15px 20px;
    span.name {
      color: @color-dark;
      display: inline-block;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      width: 130px;
      vertical-align: middle;
    }
  }
  i.online {
    color: @color-success;
  }
  i {
    color: #dddddd;
    float: right;
    font-size: 9px;
    line-height: 30px;
  }
}
</style>

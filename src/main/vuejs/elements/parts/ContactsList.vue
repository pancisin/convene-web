<template>
  <div class="contact-list">
    <div class="contact-list-separator">
      Recent conversations
    </div>
    <ul class="list-group contacts-list">
      <li class="list-group-item" v-for="(conversation, index) in conv" :key="index">
        <a @click="selectUser(conversation.participant)">
          <div class="avatar">
            <profile-picture :user="conversation.participant" />
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
            <i class="fa fa-circle" :class="{ 'online' : isOnline(conversation.participant.id) }"></i>
        </a>
        <span class="clearfix"></span>
      </li>
    </ul>

    <div class="contact-list-separator">
      Contacts online
    </div>
    <ul class="list-group contacts-list">
      <li class="list-group-item" v-for="user in onlineContacts" :key="user.id">
        <a @click="selectUser(user)">
          <div class="avatar">
            <profile-picture :user="user" />
          </div>
          <span class="name" v-text="user.displayName"></span>
          <i class="fa fa-circle" :class="{ 'online' : user.active }"></i>
        </a>
        <span class="clearfix"></span>
      </li>
    </ul>

    <div class="contact-list-separator">
      Contacts offline
    </div>
    <ul class="list-group contacts-list">
      <li class="list-group-item" v-for="user in offlineContacts" :key="user.id">
        <a @click="selectUser(user)">
          <div class="avatar">
            <profile-picture :user="user" />
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
import { mapGetters } from 'vuex';
import ProfilePicture from '../ProfilePicture';

export default {
  name: 'contacts-list',
  components: {
    ProfilePicture
  },
  computed: {
    ...mapGetters(['contacts', 'conversations']),
    conv () {
      let conversations = [ ...this.conversations ];
      conversations.sort((a, b) => {
        return b.recentMessages[0].created - a.recentMessages[0].created;
      });
      return conversations;
    },
    onlineContacts () {
      return this.contacts.filter(c => c.active);
    },
    offlineContacts () {
      return this.contacts.filter(c => !c.active);
    }
  },
  methods: {
    isOnline (userId) {
      return this.contacts.filter(u => u.id === userId && u.active).length > 0;
    },
    selectUser (user) {
      this.$emit('selected', user.id);
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
}

.contact-list {
  max-height: 600px;

  .contact-list-separator {
    padding: 10px 20px;
    border-bottom: 1px solid #eee;
    color: @color-dark;
  }

  .list-group-item {
    padding: 0 !important;
    border: none;
    &:hover {
      background: @color-light;
    }

    & > a {
      display: block;
      padding: 15px 20px;
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
  margin-bottom: 0;
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
    line-height: 14px;
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

<template>
  <dropdown-menu large>
    <span slot="button">
      <i class="fa fa-bell-o"></i>
      <span class="badge badge-xs"
        v-text="notifications.length"></span>
    </span>

    <dropdown-menu-item class="notifi-title">
      Notification
    </dropdown-menu-item>
    <dropdown-menu-item class="notifications-list">
      <transition-group name="fade">
        <div v-for="not in notifications"
          :key="not.id"
          class="notification-item">
          <!-- <em class="fa fa-diamond"></em> -->
          <div class="notification-body">
            <h5 class="media-heading">{{ $t(not.code + '.title') }}</h5>
            <p class="m-0">
              <small>{{ $t(not.code + '.message') }}</small>
              <br> <small>{{ not.created | moment('from') }}</small>
            </p>
          </div>
          <a @click="toggleSeenNotification(not)" class="toggle-seen-button">
            <i class="fa fa-times"></i>
          </a>
        </div>
      </transition-group>

      <div v-if="notifications.length == 0"
        class="text-center m-t-10 text-muted">There's nothing to display
      </div>
    </dropdown-menu-item>
    <dropdown-menu-item>
      <router-link :to="{ name: 'settings.notifications' }" class="text-right">
        <small>
          <b>See all notifications</b>
        </small>
      </router-link>
    </dropdown-menu-item>
  </dropdown-menu>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import DropdownMenu from '../DropdownMenu';
import DropdownMenuItem from '../DropdownMenuItem';
import NotificationApi from 'api/notification.api';

export default {
  name: 'notifications',
  data: function () {
    return {
      display: false
    };
  },
  computed: {
    ...mapGetters({
      notifications: 'unread_notifications'
    })
  },
  components: {
    DropdownMenu, DropdownMenuItem
  },
  methods: {
    ...mapActions(['toggleSeenNotification']),
    toggleSeen (not) {
      NotificationApi.toggleSeen(not.id, notification => {
        const index = this.notifications.findIndex(n => n.id === notification.id);
        this.notifications.splice(index, 1, notification);
      });
    },
    closeNotifications (e) {
      if (this.display) {
        this.display = false;
      }
    }
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';
.notifications-list {
  max-height: 300px;
  overflow-y: auto;
  padding: 10px;

  .notification-item {
    display: flex;
    align-items: center;

    em {
      color: @color-primary;
      border: 2px solid @color-primary;

      width: 30px;
      text-align: center;
      height: 30px;
      line-height: 28px;
      border-radius: 50%;
      flex: 0 0 30px;
      margin-right: 10px;
    }

    .notification-body {
      flex-grow: 1;
    
      h5 {
        text-overflow: ellipsis;
        white-space: nowrap;
        display: block;
        width: 100%;
        overflow: hidden;
      }

      p {
        color: #828282;
      }
    }

    .toggle-seen-button {
      margin-left: 10px;
      color: @color-primary;
    }

    & ~ div {
      margin-top: 15px;
    }
  }
}
</style>
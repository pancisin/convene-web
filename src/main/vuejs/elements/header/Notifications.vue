<template>
  <dropdown-menu large v-if="notifications">
    <span slot="button" class="notifications-bell" :class="{ 'unread-messages': notifications.length > 0 }">
      <i class="fa fa-bell-o"></i>
      <span class="badge badge-xs"
        v-text="notifications.length"></span>
    </span>

    <dropdown-menu-item header>
      Notification
    </dropdown-menu-item>
    <dropdown-menu-item v-if="notifications.length > 0">
      <a class="btn btn-link btn-block text-primary text-right" @click="setAllNotificationsSeen">
        <small>
          Set all as seen
        </small>
      </a>
    </dropdown-menu-item>
    <dropdown-menu-item class="notifications-list">
      <transition-group name="fade">
        <div v-for="not in notifications"
          :key="not.id"
          class="notification-item">
          <!-- <em class="fa fa-diamond"></em> -->
          <div class="notification-body">
            <p class="m-0">
              {{ $t(not.code, { object: not.target, subject: not.subject }) }}
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

export default {
  name: 'notifications',
  data: function () {
    return {
      display: false
    };
  },
  computed: {
    ...mapGetters(['notifications'])
  },
  components: {
    DropdownMenu,
    DropdownMenuItem
  },
  methods: {
    ...mapActions(['toggleSeenNotification', 'setAllNotificationsSeen']),
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
@keyframes ring {
  0% { transform: rotate(0); }
  1% { transform: rotate(30deg); }
  3% { transform: rotate(-28deg); }
  5% { transform: rotate(34deg); }
  7% { transform: rotate(-32deg); }
  9% { transform: rotate(30deg); }
  11% { transform: rotate(-28deg); }
  13% { transform: rotate(26deg); }
  15% { transform: rotate(-24deg); }
  17% { transform: rotate(22deg); }
  19% { transform: rotate(-20deg); }
  21% { transform: rotate(18deg); }
  23% { transform: rotate(-16deg); }
  25% { transform: rotate(14deg); }
  27% { transform: rotate(-12deg); }
  29% { transform: rotate(10deg); }
  31% { transform: rotate(-8deg); }
  33% { transform: rotate(6deg); }
  35% { transform: rotate(-4deg); }
  37% { transform: rotate(2deg); }
  39% { transform: rotate(-1deg); }
  41% { transform: rotate(1deg); }

  43% { transform: rotate(0); }
  100% { transform: rotate(0); }
}

.notifications-bell {
  .badge {
    display: none;
  }

  &.unread-messages {
    i {
      transform-origin: top center;
      animation: ring 4s 0.7s cubic-bezier(0.36, 0.07, 0.19, 0.97) both infinite;
      transform: translate3d(0, 0, 0);
      backface-visibility: hidden;
      perspective: 1000px;
    }

    .badge {
      background: @color-danger;
      display: initial;
    }
  }
}

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
      font-size: 13px;

      h5 {
        text-overflow: ellipsis;
        white-space: nowrap;
        display: block;
        width: 100%;
        overflow: hidden;
      }

      p {
        small {
          color: #828282;
        }
      }
    }

    .toggle-seen-button {
      margin-left: 10px;
      color: #ccc;
      transition: opacity 0.2s ease;
      opacity: 0;

      &:hover {
        color: @color-primary;
      }
    }

    &:hover .toggle-seen-button {
      opacity: 1;
    }

    & ~ div {
      margin-top: 7px;
      padding-top: 7px;
      // border-top: 1px solid #ddd;
    }
  }
}
</style>
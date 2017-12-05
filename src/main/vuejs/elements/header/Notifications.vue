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
    <dropdown-menu-item class="list-group notification-list">
      <a v-for="not in notifications"
        class="list-group-item"
        :key="not.id">
        <div class="media">
          <div class="pull-left p-r-10">
            <em class="fa fa-diamond noti-primary"></em>
          </div>
          <div class="media-body">
            <h5 class="media-heading">{{ $t(not.code + '.title') }}</h5>
            <p class="m-0">
              <small>{{ $t(not.code + '.message') }}</small>
            </p>
          </div>
        </div>
      </a>

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
import { mapGetters } from 'vuex';
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
    DropdownMenu, DropdownMenuItem
  },
  methods: {
    markAsSeen: function (notification) {
      var url = ['api/notification', notification.id, 'toggle-seen'].join('/');

      this.$http.patch(url).then(response => {
        this.notifications = this.notifications.filter(elem => {
          return elem.id !== notification.id;
        });
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
<template>
  <drop-down-menu large>
    <span slot="button">
      <i class="material-icons">notifications</i>
      <span class="badge badge-xs"
        v-text="notifications.length"></span>
    </span>

    <drop-down-menu-item class="notifi-title">
      Notification
    </drop-down-menu-item>
    <drop-down-menu-item class="list-group notification-list">
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
    </drop-down-menu-item>
    <drop-down-menu-item>
      <a href="javascript:void(0);"
        class=" text-right">
        <small>
          <b>See all notifications</b>
        </small>
      </a>
    </drop-down-menu-item>
  </drop-down-menu>
</template>

<script>
import { mapGetters } from 'vuex';
import { DropDownMenu, DropDownMenuItem } from 'elements';

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
    DropDownMenu, DropDownMenuItem
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
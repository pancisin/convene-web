<template>
  <li class="dropdown hidden-xs" :class="{ 'open' : display }" v-click-outside="closeNotifications">
    <a @click="display = !display" class="dropdown-toggle waves-effect waves-light">
       <i class="material-icons">notifications</i>
      <span class="badge badge-xs" v-text="notifications.length"></span>
    </a>

    <ul class="dropdown-menu dropdown-menu-lg">
      <li class="text-center notifi-title">Notification</li>
      <li class="list-group notification-list">
  
        <a v-for="not in notifications" class="list-group-item">
          <div class="media">
            <div class="pull-left p-r-10">
              <em class="fa fa-diamond noti-primary"></em>
            </div>
            <div class="media-body">
              <h5 class="media-heading" v-text="not.title"></h5>
              <p class="m-0">
                <small v-text="not.message"></small>
              </p>
            </div>
          </div>
        </a>
  
        <div v-if="notifications.length == 0" class="text-center m-t-10 text-muted">There's nothing to display</div>
  
      </li>
  
      <li>
        <a href="javascript:void(0);" class=" text-right">
          <small>
            <b>See all notifications</b>
          </small>
        </a>
      </li>
  
    </ul>
  </li>
</template>

<script>
export default {
  name: 'notifications',
  data: function () {
    return {
      display: false
    }
  },
  computed: {
    notifications() {
      return this.$store.state.notifications;
    }
  },
  methods: {
    markAsSeen: function (notification) {
      var url = ['api/notification', notification.id, 'toggle-seen'].join('/');

      this.$http.patch(url).then(response => {
        this.notifications = this.notifications.filter(elem => {
          return elem.id != notification.id;
        })
      })
    },
    closeNotifications(e) {
      if (this.display)
        this.display = false;
    }
  }
}
</script>
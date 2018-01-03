<template>
 <panel type="table" v-loading="loading">
    <span slot="title">Notifications</span>
    <button type="button" class="btn btn-link pull-right" @click="setAllSeen">Set all as seen</button>
    
    <table class="table table-striped">
      <thead>
        <tr>
          <th>
          </th>
          <th>
            Message
          </th>
          <th class="text-center">
            Seen
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="not in paginator.content" @contextmenu.prevent="$refs.menu.open($event, not)" :key="not.id">
          <td>{{ not.created | moment('from') }}</td>
          <td>
            <span>
              {{ $t(not.code, { object: not.target, subject: not.subject }) }}
            </span>
          </td>
          <td class="text-center">
            <a @click="toggleSeen(not)">
              <i class="material-icons" v-if="not.seen" key="1">radio_button_checked</i>
              <i class="material-icons" v-else key="0">radio_button_unchecked</i>
            </a>
          </td>
        </tr>
      </tbody>
    </table>

    <context-menu ref="menu">
      <template slot-scope="props">
        <ul>
          <li>
            <a @click="toggleSeen(props.data)">
              Toggle seen
            </a>
          </li>
        </ul>
      </template>
    </context-menu>
  
    <div class="text-center">
      <paginator :paginator="paginator" :fetch="getNotifications" history/>
    </div>
  </panel>
</template>

<script>
import UserApi from 'api/user.api';
import { Paginator } from 'elements';
import { mapActions } from 'vuex';

export default {
  data () {
    return {
      paginator: {},
      loading: false
    };
  },
  components: {
    Paginator
  },
  methods: {
    ...mapActions(['toggleSeenNotification', 'setAllNotificationsSeen']),
    getNotifications (page) {
      UserApi.getNotifications(page, 7, { seen: true }, paginator => {
        this.paginator = paginator;
      });
    },
    async toggleSeen (not) {
      const notification = await this.toggleSeenNotification(not);

      const index = this.paginator.content.findIndex(n => n.id === notification.id);
      this.paginator.content.splice(index, 1, notification);
    },
    async setAllSeen () {
      await this.setAllNotificationsSeen();
      this.getNotifications(0);
    }
  }
};
</script>
